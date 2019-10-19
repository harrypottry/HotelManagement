package com.aaroom.service;

import com.aaroom.beans.Cloth;
import com.aaroom.beans.ClothView;
import com.aaroom.beans.RelClothType;
import com.aaroom.persistence.ClothMapper;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ClothService {

    @Autowired
    private ClothMapper clothMapper;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ClothTypeService clothTypeService;

    public int getIdByClothTypeId(Integer cloth_type_id) {
        return clothMapper.getIdByClothTypeId(cloth_type_id);
    }

    public int insertClothByClothTypeId(Integer cloth_type_id) {
        return clothMapper.insertClothByClothTypeId(cloth_type_id);
    }

    public Cloth selectByPrimaryKey(Integer id) {
        return clothMapper.selectByPrimaryKey(id);
    }
    //根据clothid获取cloth对象出来
    public Cloth getClothByid(Integer id) {
        return clothMapper.getClothByid(id);
    }
    //根据Rfid list 找cloth对象 list
    //同时在redis里寻找 C:Rfid的这个id是否为空，如果是空就存进去id 如果不是空就直接取值。
    //2019年1月9日 修改返回值由list改成map 因为批量进来的rfid 有的可能在库中没有（或真实情况中不知道扫了什么码进来了）
    public Map<String,List> getClothidListByRfidList(List<String> RfidList){
        Map ClothIdListAndRfidMissListmap=new HashMap();
        List<Integer> ClothIdList=new ArrayList<>();
        List<String> RfidMissList=new ArrayList<>();

        for (String Rfid:
        RfidList) {
            //循环RfidList 先根据key值 redis里是否已经有该值了
            Integer cloth_id = redisCacheService.get(Constants.CLOTHRFID + Rfid, Integer.class);
            if(cloth_id==null){
                Cloth clothByRfid = clothMapper.getClothByRfid(Rfid);
                if(clothByRfid==null){
                    RfidMissList.add(Rfid);
                }else{
                    redisCacheService.put(Constants.CLOTHRFID + Rfid,clothByRfid.getId());
                ClothIdList.add(clothByRfid.getId());
                }
            }else {
                ClothIdList.add(cloth_id);
            }
        }
        ClothIdListAndRfidMissListmap.put("ClothIdList",ClothIdList);
        ClothIdListAndRfidMissListmap.put("RfidMissList",RfidMissList);
        return ClothIdListAndRfidMissListmap;
    }

    //单个查询 根据rfid查clothid 同时在redis里寻找 C:Rfid的这个id是否为空，如果是空就存进去id 如果不是空就直接取值。
    public Integer getClothByRfid(String Rfid){
            //循环RfidList 先根据key值 redis里是否已经有该值了
        Integer clothid = redisCacheService.get(Constants.CLOTHRFID + Rfid, Integer.class);
            if(clothid==null){
                Cloth clothByRfid = clothMapper.getClothByRfid(Rfid);
                if(clothByRfid==null){
                    return null;
                }
                redisCacheService.put(Constants.CLOTHRFID + Rfid,clothByRfid.getId());
                return clothByRfid.getId();
            }else {
                return clothid;
            }
        }


    //直接获取现有所有clothidlist
    public List<Integer> getClothid() {
        return clothMapper.getClothid();
    }

    public int insertSelective(Cloth cloth) {
        return clothMapper.insertSelective(cloth);
    }
    //二码合一新增cloth对象
    public int insertQRCodeAndRfid(Cloth cloth) {
        return clothMapper.insertQRCodeAndRfid(cloth);
    }
    public int insert(Cloth cloth) {
        return clothMapper.insert(cloth);
    }

    public Cloth getClothDetail(Integer id) {
        return clothMapper.getClothDetail(id);
    }

    //发布任务的同时修改房间内干净布草变成脏布草  公共方法同样可以把脏布草变干净 第三个参数status为值 0为干净 1为脏【张赢】
    public int updateStatusByclothId(Integer id, Integer status) {
        return clothMapper.updateStatusByclothId(id, status);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertClothWithTypes(Cloth cloth, Integer[] cloth_type_ids) {
        //TODO：将与此条布草关联的信息全部删除
        clothMapper.insertSelective(cloth);
        for (Integer cloth_type_id : cloth_type_ids) {
            RelClothType relClothType = new RelClothType();
            relClothType.setCloth_id(cloth.getId());
            relClothType.setCloth_type_id(cloth_type_id);
            relClothTypeService.insert(relClothType);
        }
    }

    public List<Cloth> getLatestClothList(Date start, Integer[] cloth_type_ids) {
        return clothMapper.getLatestClothList(start, cloth_type_ids, cloth_type_ids.length);
    }
    //同时增加一条coth_log日志把房间对应的房间布草状态改为脏的日志


    public ClothView getClothView(Cloth cloth) {
        return getClothView(cloth, null, false, false, false);
    }

    public ClothView getClothView(Integer cloth_id) {
        return getClothView(null, cloth_id, false, false, false);
    }

    //
    public ClothView getClothView(Cloth cloth, Integer cloth_id, Boolean isAllClothTypes, Boolean isClothLogs, Boolean isMissions) {
        if (cloth == null) {
            cloth = clothMapper.selectByPrimaryKey(cloth_id);
            if (cloth == null) {
                return new ClothView();
            }
        }
        ClothView clothView = new ClothView(cloth);
        if(isAllClothTypes) {
            String clothTypeIds = redisCacheService.hget(Constants.CLOTH + cloth.getId(), Constants.ALLCLOTHTYPEIDS, String.class);
            //TODO:soda 把所有使用array的地方统一改成List
            if (StringUtils.isEmpty(clothTypeIds)) {
                List<Integer> clothTypeIdList = relClothTypeService.getIdsByClothId(cloth.getId());
                clothTypeIds = "";
                for (Integer id : clothTypeIdList) {
                    clothTypeIds += "," + id;
                }
                clothTypeIds = clothTypeIds.substring(1);
                redisCacheService.hset(Constants.CLOTH + cloth.getId(), Constants.ALLCLOTHTYPEIDS, clothTypeIds);

                {
                    Integer[] clothTypeIdArray = {};
                    clothTypeIdArray = clothTypeIdList.toArray(clothTypeIdArray);
                    clothView.setClothTypes(clothTypeService.getClothTypeByIds(clothTypeIdArray));
                }
            } else {
                clothView.setClothTypes(clothTypeService.getClothTypeDescByIds(clothTypeIds.split(",")));
            }
            clothView.setClothTypeIds(clothTypeIds);
        } else {
            String clothTypeIds = redisCacheService.hget(Constants.CLOTH + cloth.getId(), Constants.CLOTHTYPEIDS, String.class);
            //TODO:soda 把所有使用array的地方统一改成List
            if (StringUtils.isEmpty(clothTypeIds)) {
                List<Integer> clothTypeIdList = relClothTypeService.getIdsByClothId(cloth.getId(), 1);
                if(clothTypeIdList!=null && clothTypeIdList.size()>0) {
                    clothTypeIds = "";
                    for (Integer id : clothTypeIdList) {
                        clothTypeIds += "," + id;
                    }
                    clothTypeIds = clothTypeIds.substring(1);
                    redisCacheService.hset(Constants.CLOTH + cloth.getId(), Constants.CLOTHTYPEIDS, clothTypeIds);

                    {
                        Integer[] clothTypeIdArray = {};
                        clothTypeIdArray = clothTypeIdList.toArray(clothTypeIdArray);
                        clothView.setClothTypes(clothTypeService.getClothTypeByIds(clothTypeIdArray));
                    }
                }
            } else {
                clothView.setClothTypes(clothTypeService.getClothTypeDescByIds(clothTypeIds.split(",")));
            }
            clothView.setClothTypeIds(clothTypeIds);
        }

        //TODO:soda 补完
        //获取最近20条布草日志
        if (isClothLogs) {

        }

        //获取最近20个相关任务
        if (isMissions) {

        }


        return clothView;
    }

    public Cloth selectByrfID(String rfID) {
        return clothMapper.selectByrfID(rfID);
    }

    public void update(Cloth cloth) {
        clothMapper.updateByPrimaryKeySelective(cloth);
    }
}
