package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.persistence.ConsumptionLogMapper;
import com.aaroom.persistence.MissionMapper;
import com.aaroom.persistence.MissionTypeMapper;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.Constants;
import com.aaroom.utils.StringSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("missionTypeService")
public class MissionTypeService {

    @Resource
    private MissionTypeMapper missionTypeMapper;

    @Resource
    private MissionMapper missionMapper;

    @Resource
    private ConsumptionLogMapper consumptionLogMapper;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClothService clothService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RoomClothService roomClothService;

    public MissionType getById(Long id){
        return missionTypeMapper.getById(id);
    }
    public MissionType getMissionTypeById(Integer id){
        return missionTypeMapper.getMissionTypeById(id);
    }

    public List<MissionType> getListByMap(Map<String,Object> param)throws Exception{
        return missionTypeMapper.getListByMap(param);
    }

    public Integer getCountByMap(Map<String,Object> param)throws Exception{
        return missionTypeMapper.getCountByMap(param);
    }

    public Integer save(MissionType missionType)throws Exception{
        missionType.setCreate_time(new Date());
        return missionTypeMapper.save(missionType);
    }

    public Integer modify(MissionType missionType)throws Exception{
        missionType.setUpdate_time(new Date());
        return missionTypeMapper.modify(missionType);
    }

    public Integer removeById(Long id)throws Exception{
        return missionTypeMapper.removeById(id);
    }

    //获得missiontype
    public List<MissionType> getMissionType()throws Exception{
        return missionTypeMapper.getMissionType();
    }

    //获得missiontype对象 根据desc
    public MissionType getmissiontypebydesc(String desc)throws Exception{
        return missionTypeMapper.getmissiontypebydesc(desc);
    }

    public List<Mission> getEmpClothNumDone(Integer accept_employee_id, String hotel_id){
        return missionTypeMapper.getEmpClothNumDone(accept_employee_id,hotel_id);
    }

    public  List<HashMap<String,Object>> getEmpCleanNum(String accept_employee_id){
        return missionTypeMapper.getEmpCleanNum(accept_employee_id);
    }
    public  List<HashMap<String,Object>> getEmpDirNum(String accept_employee_id){
        return missionTypeMapper.getEmpDirNum(accept_employee_id);
    }
    //根据id查到对应desc中文
    public String getdescbyid(Integer id){
        return missionTypeMapper.getdescbyid(id);
    }


    public List<HashMap<String,Object>> showHistoryMissionByEmp(Integer accept_employee_id,int hotel_id,Integer index,Integer pageSize){
        return missionTypeMapper.showHistoryMissionByEmp(accept_employee_id,hotel_id,index,pageSize);
    }


    public Integer showHistoryMissionByEmpNum(Integer accept_employee_id,int hotel_id){
        return missionTypeMapper.showHistoryMissionByEmpNum(accept_employee_id,hotel_id);
    }



    public Integer getMissionOldZjy(int hotel_id,Integer room_id,Integer[] status){
        return missionTypeMapper.getMissionOldZjy(hotel_id,room_id,status);
    }

    //完成任务
    @Transactional(rollbackFor = Exception.class)
    public void saveConsumptionByMissionId(Integer mission_id,int hotel_id,Integer room_id,Integer empId,ArrayList<Map<String,Object>> conAndSumList,Integer mission_type){
        if (mission_type!=2){
           //保洁出干净，房间入干净
            //先判断,是否当前已经将房间脏布草扫描
            //最新挂在此房间内的脏布草。从房间转移至保洁名下
            List<ClothLog> clothIdList = clothLogService.getLogListByEmpId(hotel_id, room_id, 0,1);

            //若是房间初始化，即原来房间布草为空，则无需插入日志
            if (!clothIdList.isEmpty()){
                //获取所有布草ids,进行遍历添加，将所有符合的布草记录日志

                for (ClothLog oldClothLog:clothIdList){
                    //房间出脏
                    ClothLog clothLogOut = new ClothLog(oldClothLog.getCloth_id(),hotel_id,null,0,room_id,0,1);
                    clothLogService.insertSelective(clothLogOut);
                    //保洁入脏
                    ClothLog clothLogIn = new ClothLog(oldClothLog.getCloth_id(),hotel_id,null,1,empId,1,1);
                    clothLogService.insertSelective(clothLogIn);
                    //同时改变布草状态
                    clothService.updateStatusByclothId(oldClothLog.getCloth_id(),1);
                }
            }
        }else {
            //客房更新不退房
            //清理redis M:中所有数据
            redisCacheService.del(Constants.MISSION+mission_id);
        }
        //批量插入消耗品

            for (Map<String,Object> map:conAndSumList){
                for (Map.Entry<String,Object> entry:map.entrySet()){
                    if (entry.getKey().equals("num") && (Integer)entry.getValue() >0){
                        ConsumptionLog consumptionLog = new ConsumptionLog();
                        consumptionLog.setMissionId(mission_id);
                        consumptionLog.setNum((Integer) entry.getValue());
                        String str = (String) map.get("id");
                        if (str!=null){
                            consumptionLog.setComsumptionId(Integer.valueOf(str));
                        }
                        consumptionLogMapper.insertSelective(consumptionLog);
                    }
                }
            }


        //将房间状态变为待检查
        Room roomById = roomService.getRoomById(room_id);
        roomById.setStatus(2);
        roomService.update(roomById);

        //修改任务状态为待检查
        missionMapper.updateMissionStatus(2,mission_id);
    }

    public List getMissionListByStatus(Integer empId,Integer hotel_id,Integer[] status){
        Map<String,Object> ret ;
        List resultList = new ArrayList();
        List<Mission> missionDetailList = missionService.getMissionDetailByDay(empId, hotel_id, status);
        for (Mission mission:missionDetailList){
            ret = new HashMap();

            Room room = roomService.getRoomById(mission.getRoom_id());

            MissionType missionType = getMissionTypeById(mission.getMission_type());
            RoomType roomType = roomTypeService.getRoomTypeByEmpId(room.getRoom_type_id());

            String mission_desc = missionType.getDesc();
            String room_name = room.getRoom_name();
            String room_desc = roomType.getDesc();

            ret.put("room_name",room_name);
            ret.put("mission_desc",mission_desc);
            ret.put("room_desc",room_desc);
            ret.put("mission",mission);
            resultList.add(ret);
        }
        return resultList;
    }

    public Object isActiveConfigCloth(Integer cloth_id,Integer hotel_id,Integer mission_id,Integer room_id){

        //1`获取当前布草标配数量
        Integer room_type_id = roomService.getRoomTypeIdByHotelAndId(hotel_id, room_id);
        List<RoomCloth> roomClothList = roomClothService.getRoomClothByHotelIdAndRoomId(room_type_id);
        Integer standard_num =0;
        List<String> tempList = new ArrayList();
        for (RoomCloth rc:roomClothList){
            tempList.add(rc.getCloth_type_id());
            //获取扫码的布草类型标配数量
            if (StringSort.StringSort(rc.getCloth_type_id(),clothLogService.getClothIdsByRedis(cloth_id))){
                standard_num =rc.getNumber();
            }
        }
        if (!tempList.contains(clothLogService.getClothIdsByRedis(cloth_id))){
            return RestError.E_START_UNCloth;   //当前扫码布草类型不匹配
        }
        //2`获取当前布草已经配置的数量
        List<ClothLog> clothLogList = clothLogService.getClothLogListByClean(hotel_id, mission_id, room_id);
        Map<String,Integer> numMap = new HashMap<>();
        if (!clothLogList.isEmpty()){
            for (ClothLog clothLog:clothLogList){
                //3.扫描相同ID布草异常
                if (clothLog.getCloth_id().equals(cloth_id)){
                    return RestError.E_START_SAME;
                }
                String ids = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                numMap.put(ids,numMap.get(ids)==null?1:numMap.get(ids)+1);
            }
            //比较返回错误码
            for (Map.Entry<String,Integer> entry:numMap.entrySet()){
                if (StringSort.StringSort(entry.getKey(),clothLogService.getClothIdsByRedis(cloth_id))&& entry.getValue() >=standard_num){
                    return RestError.E_START_OVERFLOWING;
                }
            }
        }
       return RestError.E_OK;
    }
}
