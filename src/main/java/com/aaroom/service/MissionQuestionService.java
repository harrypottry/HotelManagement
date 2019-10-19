package com.aaroom.service;
import com.aaroom.beans.MissionQuestion;
import com.aaroom.persistence.MissionMapper;
import com.aaroom.persistence.MissionQuestionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("missionQuestionService")
public class MissionQuestionService {

    @Resource
    private MissionQuestionMapper missionQuestionMapper;

    @Resource
    private MissionMapper missionMapper;

    public void insert(MissionQuestion record){
        missionQuestionMapper.insert(record);
    }

    public void insertOrUpdateMission(Integer id,String comment,List<String> fileNames){
        //问题备注
        if (comment!=null){
            missionMapper.insertOrUpdateMission(id,comment);
            //图片地址录入
        }if (fileNames.size()!=0){
            MissionQuestion missionQuestion = new MissionQuestion();
            missionQuestion.setMissionId(id);
            for (String s:fileNames){
                missionQuestion.setPic(s);
                missionQuestionMapper.insertSelective(missionQuestion);
            }
        }
    }

    public List<MissionQuestion> getMissionQuestionByEmp(Integer mission_id){
        return missionQuestionMapper.getMissionQuestionByEmp(mission_id);
    }
}
