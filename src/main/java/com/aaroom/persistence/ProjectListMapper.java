package com.aaroom.persistence;

import com.aaroom.beans.IntentionView;
import com.aaroom.beans.ProjectList;
import com.aaroom.beans.ProjectListExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectListMapper {
    int countByExample(ProjectListExample example);

    int deleteByExample(ProjectListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectList record);

    int insertSelective(ProjectList record);

    List<ProjectList> selectByExample(ProjectListExample example);

    ProjectList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectList record, @Param("example") ProjectListExample example);

    int updateByExample(@Param("record") ProjectList record, @Param("example") ProjectListExample example);

    int updateByPrimaryKeySelective(ProjectList record);

    int updateByPrimaryKey(ProjectList record);

    IntentionView getIntention(@Param("hotelId")Integer hotelId);
}