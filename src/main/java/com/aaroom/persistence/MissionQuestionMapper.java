package com.aaroom.persistence;

import com.aaroom.beans.MissionQuestion;

import java.util.List;

public interface MissionQuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MissionQuestion record);

    int insertSelective(MissionQuestion record);

    MissionQuestion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MissionQuestion record);

    int updateByPrimaryKey(MissionQuestion record);

    List<MissionQuestion> getMissionQuestionByEmp(Integer mission_id);

}
