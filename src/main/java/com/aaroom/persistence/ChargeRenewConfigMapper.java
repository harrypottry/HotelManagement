package com.aaroom.persistence;

import com.aaroom.beans.ChargeRenewConfig;
import com.aaroom.beans.ChargeRenewConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRenewConfigMapper {
    int countByExample(ChargeRenewConfigExample example);

    int deleteByExample(ChargeRenewConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChargeRenewConfig record);

    int insertSelective(ChargeRenewConfig record);

    List<ChargeRenewConfig> selectByExample(ChargeRenewConfigExample example);

    ChargeRenewConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ChargeRenewConfig record, @Param("example") ChargeRenewConfigExample example);

    int updateByExample(@Param("record") ChargeRenewConfig record, @Param("example") ChargeRenewConfigExample example);

    int updateByPrimaryKeySelective(ChargeRenewConfig record);

    int updateByPrimaryKey(ChargeRenewConfig record);
}