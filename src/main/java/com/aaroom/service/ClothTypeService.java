package com.aaroom.service;

import com.aaroom.beans.ClothType;
import com.aaroom.persistence.ClothTypeMapper;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.Constants;
import com.alibaba.druid.sql.visitor.functions.Char;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothTypeService {

    @Autowired
    private ClothTypeMapper clothTypeMapper;

    @Autowired
    private RedisCacheService redisCacheService;

    public Integer getClothNumber(ClothType clothType){
        return clothTypeMapper.getClothNumber(clothType);
    }

    public List<ClothType> getClothTypeByHotelId(String hotel_id){
        return clothTypeMapper.getClothTypeByHotelId(hotel_id);
    }

    public String getDescByIdAndHotelId(ClothType clothType){
        return clothTypeMapper.getDescByIdAndHotelId(clothType);
    }

    public void insert(ClothType clothType) {
        clothTypeMapper.insertSelective(clothType);
    }

    public void update(ClothType clothType) {
        clothTypeMapper.updateByPrimaryKeySelective(clothType);
    }

    public List<ClothType> getClothTypeDescByIds(String[] clothTypeIds){
        return clothTypeMapper.getClothTypeDescByIds(clothTypeIds);
    }
    //根据cloth_type_id查出对应的中文名字list
    public List<ClothType> getClothTypeDescById(Integer cloth_type_id){
        return clothTypeMapper.getClothTypeDescById(cloth_type_id);
    }
    //按照clothkind 查到clothtype对象
    public List<ClothType> getClothTypeByKind(Constants.ClothKind kind) {
        return clothTypeMapper.getClothTypeByKind(kind);
    }
    public List<ClothType> getClothTypeByIds(Integer[] idsByClothId){
        return clothTypeMapper.getClothTypeByIds(idsByClothId);
    }
    //根据传入的listid 得到colthtype对象
    public List<ClothType> getClothTypeByListId(List clothtypeid){
        return clothTypeMapper.getClothTypeByListId(clothtypeid);
    }
    //根据传入的listid 得到colthtype对应desc描述
    public List<ClothType> getClothTypedescByIds(List clothtypeid){
        return clothTypeMapper.getClothTypedescByIds(clothtypeid);
    }

    public ClothType selectByPrimaryKey(Integer id){
        ClothType clothType = redisCacheService.get(Constants.CLOTHTYPE + id, ClothType.class);
        if(clothType==null) {
            clothType = clothTypeMapper.selectByPrimaryKey(id);
            redisCacheService.put(Constants.CLOTHTYPE + id, clothType);
        }
        return  clothType;
    }
}
