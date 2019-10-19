package com.aaroom.persistence;

import com.aaroom.beans.Configuration;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Configuration record);

    int insertSelective(Configuration record);

    Configuration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Configuration record);

    int updateByPrimaryKey(Configuration record);

    Configuration getConfigurationByName(@Param("name") String name);
}