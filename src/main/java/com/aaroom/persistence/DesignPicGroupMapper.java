package com.aaroom.persistence;

import com.aaroom.beans.DesignPicGroup;
import com.aaroom.beans.DesignPicGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignPicGroupMapper {
    int countByExample(DesignPicGroupExample example);

    int deleteByExample(DesignPicGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DesignPicGroup record);

    int insertSelective(DesignPicGroup record);

    List<DesignPicGroup> selectByExample(DesignPicGroupExample example);

    DesignPicGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DesignPicGroup record, @Param("example") DesignPicGroupExample example);

    int updateByExample(@Param("record") DesignPicGroup record, @Param("example") DesignPicGroupExample example);

    int updateByPrimaryKeySelective(DesignPicGroup record);

    int updateByPrimaryKey(DesignPicGroup record);
}