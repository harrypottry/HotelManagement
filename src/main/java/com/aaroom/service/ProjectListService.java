package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.persistence.ChargeConfigMapper;
import com.aaroom.persistence.HotelBaseMapper;
import com.aaroom.persistence.IntentionStoreMapper;
import com.aaroom.persistence.ProjectListMapper;
import com.aaroom.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/25.
 */
@Service
public class ProjectListService {

    @Autowired
    private ProjectListMapper projectListMapper;

    @Autowired
    private HotelBaseMapper hotelBaseMapper;

    @Autowired
    private ChargeConfigMapper chargeConfigMapper;

    @Autowired
    private IntentionStoreMapper intentionStoreMapper;

    public Map<String,Object> getintention(Integer hotelId){
        Map<String, Object> map = new HashMap<>();
        IntentionView intentionView = projectListMapper.getIntention(hotelId);
        ChargeConfigExample example = new ChargeConfigExample();
        ChargeConfigExample.Criteria criteria = example.createCriteria();
        criteria.andHotelIdEqualTo(hotelId);
        List<ChargeConfig> chargeConfigs = chargeConfigMapper.selectByExample(example);
        map.put("intentionView",intentionView);
        map.put("chargeConfigs",chargeConfigs);
        return map;
    }

    public Map<String,Object> getProjectList(Integer hotelId) {
        Map<String, Object> map = new HashMap<>();
        HotelBase hotelBase = hotelBaseMapper.getById(hotelId);
        ProjectListExample example = new ProjectListExample();
        ProjectListExample.Criteria criteria = example.createCriteria();
        criteria.andHotelIdEqualTo(hotelId);
        List<ProjectList> projectLists = projectListMapper.selectByExample(example);
        map.put("hotelName",hotelBase.getHotel_name());
        map.put("projectLists",projectLists);
        return map;
    }

    public Map<String,Object> insertProjectList(ProjectListView projectListView) {
        try {
            List<ProjectList> projectLists = projectListView.getProjectLists();
            ProjectList projectList1 = projectLists.get(0);
            Integer hotelId = projectList1.getHotelId();
            HotelBase byId = hotelBaseMapper.getById(hotelId);
            byId.setHotel_name(projectListView.getHotelName());
            hotelBaseMapper.modify(byId);
            for (ProjectList projectList: projectLists
                 ) {
                if(projectList.getId()!= null){
                    projectListMapper.updateByPrimaryKeySelective(projectList);
                }else{
                    projectListMapper.insertSelective(projectList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap("error", "保存信息失败！");
        }
        return resultMap("success", "保存信息成功！");
    }

    public Map<String, Object> resultMap(String value1, String value2) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", value1);
        map.put("msg", value2);
        return map;
    }
}
