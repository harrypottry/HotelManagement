package com.aaroom.persistence;

import com.aaroom.beans.ClothType;
import com.aaroom.utils.Constants;
import com.alibaba.druid.sql.visitor.functions.Char;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClothType record);

    int insertSelective(ClothType record);

    ClothType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClothType record);

    int updateByPrimaryKey(ClothType record);

    int getClothNumber(ClothType clothType);

    List<ClothType> getClothTypeByHotelId(String hotel_id);

    String getDescByIdAndHotelId(ClothType clothType);
    //根据string 数组ids查clothtype对象list
    List<ClothType> getClothTypeDescByIds(String[] clothTypeIds);
    //根据cloth_type_id查出对应的中文名字list
    List<ClothType> getClothTypeDescById(Integer cloth_type_id);

    List<ClothType> getClothTypeByKind(@Param("kind") Constants.ClothKind kind);

    List<ClothType> getClothTypeByIds(Integer[] idsByClothId);
    //根据传入的listid 得到colthtype对象
    List<ClothType> getClothTypeByListId(@Param("clothtypeid")List clothtypeid);
    //根据传入的listid 得到colthtype对应desc描述
    List<ClothType> getClothTypedescByIds(@Param("clothtypeid")List clothtypeid);
}