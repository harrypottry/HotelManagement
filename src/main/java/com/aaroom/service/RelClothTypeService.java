package com.aaroom.service;

import com.aaroom.beans.Cloth;
import com.aaroom.beans.ClothType;
import com.aaroom.beans.RelClothType;
import com.aaroom.persistence.RelClothTypeMapper;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.Constants;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RelClothTypeService {



    @Resource
    private RelClothTypeMapper relClothTypeMapper;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private ClothService clothService;


    public List<Integer> getIdsByClothId(Integer cloth_id){
        return relClothTypeMapper.getIdsByClothId(cloth_id,1);
    }

    public List<Integer> getIdsByClothId(Integer cloth_id, Integer is_active) {
        return relClothTypeMapper.getIdsByClothId(cloth_id, is_active);
    }
    //两表联查 根据clothid查对应的clothtype的中文desc
    public List getDescByclothid(Integer cloth_id){
        return relClothTypeMapper.getDescByclothid(cloth_id);
    }
    //两表联查 根据clothid查对应的clothtype_brand的中文desc
    public List getclothtype_brand(Integer cloth_id){
        return relClothTypeMapper.getclothtype_brand(cloth_id);
    }
    //两表联查 根据clothid查对应的cloth_size的中文desc
    public String getcloth_size(Integer cloth_id){
        return relClothTypeMapper.getcloth_size(cloth_id);
    }

    //在业务中只用到了Material,Type,Size,所以如果是其他类型，那就is_active==0
    public void insert(RelClothType relClothType) {
        redisCacheService.hdel(Constants.CLOTH+relClothType.getCloth_id(), Constants.CLOTHTYPEIDS);
        ClothType clothType = clothTypeService.selectByPrimaryKey(relClothType.getCloth_type_id());
        Constants.ClothKind[] kinds = {Constants.ClothKind.Type,Constants.ClothKind.Size,Constants.ClothKind.Material};
        if(!ArrayUtils.contains(kinds, clothType.getCloth_kind())) {
            relClothType.setIs_active(new Byte("0"));
        }
        relClothTypeMapper.insertSelective(relClothType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void buildclothTypeRelInBatch(Integer[] cloth_ids, Integer[] cloth_type_ids) {
        for (Integer cloth_id: cloth_ids) {
            relClothTypeMapper.deleteRelClothTypesByClothId(cloth_id);
                clothService.updateStatusByclothId(cloth_id, 1);

            for (Integer cloth_type_id: cloth_type_ids){
                RelClothType relClothType = new RelClothType();
                relClothType.setCloth_id(cloth_id);
                relClothType.setCloth_type_id(cloth_type_id);
                insert(relClothType);
            }
        }
    }

    public void buildRelClothType(List<Integer> cloth_ids, List<Integer> cloth_type_ids) {
        for (Integer cloth_id: cloth_ids) {
            //判断RelClothType存在这个cloth_id
            List<RelClothType> relClothTypeOld = relClothTypeMapper.getRelClothTypeByclothid(cloth_id);
            if(relClothTypeOld !=null || relClothTypeOld.size()>0){
                relClothTypeMapper.deleteRelClothTypesByClothId(cloth_id);
            }
            //判断Cloth表存在这个cloth_id
            Cloth clothOld = clothService.getClothByid(cloth_id);
            if(clothOld!=null){
                clothService.updateStatusByclothId(cloth_id, 1);
            }
            for (Integer cloth_type_id: cloth_type_ids){
                    relClothTypeMapper.insert(cloth_id,cloth_type_id);
                }
        }
    }

    public Integer save(RelClothType relClothType)throws Exception{
        return relClothTypeMapper.save(relClothType);
    }
}
