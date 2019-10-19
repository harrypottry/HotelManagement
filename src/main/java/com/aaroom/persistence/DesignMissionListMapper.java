package com.aaroom.persistence;

import com.aaroom.beans.DesignMissionList;
import com.aaroom.beans.DesignMissionListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignMissionListMapper {
    int countByExample(DesignMissionListExample example);

    int deleteByExample(DesignMissionListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DesignMissionList record);

    int insertSelective(DesignMissionList record);

    List<DesignMissionList> selectByExample(DesignMissionListExample example);

    DesignMissionList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DesignMissionList record, @Param("example") DesignMissionListExample example);

    int updateByExample(@Param("record") DesignMissionList record, @Param("example") DesignMissionListExample example);

    int updateByPrimaryKeySelective(DesignMissionList record);

    int updateByPrimaryKey(DesignMissionList record);
}