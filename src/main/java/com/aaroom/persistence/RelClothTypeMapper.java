package com.aaroom.persistence;

import com.aaroom.beans.RelClothType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelClothTypeMapper {

    RelClothType getClothIdByClothTypeIds(Integer ids);

    void insertSelective(RelClothType relClothType);
    //两个参数 clothid与clothtype_id
    void insert(@Param("cloth_id") Integer cloth_id,@Param("cloth_type_id") Integer cloth_type_id);

    Integer save(RelClothType relClothType);

    List<Integer> getIdsByClothId(@Param("cloth_id") Integer cloth_id,@Param("is_active") Integer is_active);
    //两表联查 根绝clothid查对应的clothtype的中文desc
    List getDescByclothid(Integer cloth_id);

    //判断RelClothType表中是否已经有该id
    List<RelClothType> getRelClothTypeByclothid(@Param("cloth_id") Integer cloth_id);

    //两表联查 根据clothid查对应的clothtype_brand的中文desc
    List getclothtype_brand(Integer cloth_id);
    //两表联查 根据clothid查对应的cloth_size的中文desc
    String getcloth_size(Integer cloth_id);

    void deleteRelClothTypesByClothId(@Param("cloth_id") Integer cloth_id);
}
