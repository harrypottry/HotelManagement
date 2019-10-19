package com.aaroom.persistence;

import com.aaroom.beans.ClothExchange;

import java.util.List;

public interface ClothExchangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClothExchange record);

    int insertSelective(ClothExchange record);

    ClothExchange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClothExchange record);

    int updateByPrimaryKey(ClothExchange record);

    List<ClothExchange> getAllClothExchangeByAA();

    ClothExchange getClothExchangeByExample(ClothExchange example);
}