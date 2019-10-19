package com.aaroom.persistence;

import com.aaroom.beans.ChargeConfigAttach;
import com.aaroom.beans.ChargeConfigAttachExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeConfigAttachMapper {
    int countByExample(ChargeConfigAttachExample example);

    int deleteByExample(ChargeConfigAttachExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChargeConfigAttach record);

    int insertSelective(ChargeConfigAttach record);

    List<ChargeConfigAttach> selectByExample(ChargeConfigAttachExample example);

    ChargeConfigAttach selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ChargeConfigAttach record, @Param("example") ChargeConfigAttachExample example);

    int updateByExample(@Param("record") ChargeConfigAttach record, @Param("example") ChargeConfigAttachExample example);

    int updateByPrimaryKeySelective(ChargeConfigAttach record);

    int updateByPrimaryKey(ChargeConfigAttach record);
}