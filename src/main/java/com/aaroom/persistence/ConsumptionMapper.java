package com.aaroom.persistence;

import com.aaroom.beans.Consumption;

import java.util.List;

public interface ConsumptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Consumption record);

    int insertSelective(Consumption record);

    Consumption selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Consumption record);

    int updateByPrimaryKey(Consumption record);

//获取消耗品列表
List<Consumption> getAllConsumption();
}