package com.aaroom.persistence;

import com.aaroom.beans.ConsumptionLog;

public interface ConsumptionLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsumptionLog record);

    int insertSelective(ConsumptionLog record);

    ConsumptionLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsumptionLog record);

    int updateByPrimaryKey(ConsumptionLog record);



}