package com.aaroom.persistence;

import com.aaroom.beans.ChargeConfig;
import com.aaroom.beans.ChargeConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeConfigMapper {
    int countByExample(ChargeConfigExample example);

    int deleteByExample(ChargeConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChargeConfig record);

    int insertSelective(ChargeConfig record);

    List<ChargeConfig> selectByExample(ChargeConfigExample example);

    ChargeConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ChargeConfig record, @Param("example") ChargeConfigExample example);

    int updateByExample(@Param("record") ChargeConfig record, @Param("example") ChargeConfigExample example);

    int updateByPrimaryKeySelective(ChargeConfig record);

    int updateByPrimaryKey(ChargeConfig record);
}