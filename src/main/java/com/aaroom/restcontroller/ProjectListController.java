package com.aaroom.restcontroller;

import com.aaroom.beans.ProjectListView;
import com.aaroom.service.ProjectListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/25.
 */
@RestController
public class ProjectListController {

    @Autowired
    private ProjectListService projectListService;
    //获取立项单
    @GetMapping(value = "/console/api/getProjectList")
    public Map<String,Object> getProjectList(HttpServletResponse response,
                                             @RequestParam(value = "hotelId",required = false ) Integer hotelId
    ){
        HashMap<String, Object> map = new HashMap<>();
        Map<String,Object> projectList = projectListService.getProjectList(hotelId);
        Map<String, Object> intention = projectListService.getintention(hotelId);
        map.put("projectList",projectList);
        map.put("intention",intention);
        return map;
    }
    //保存立项单
    @PostMapping(value = "/console/api/insertProjectList")
    public Map<String,Object> insertProjectList(@RequestBody ProjectListView projectListView){
        Map<String,Object> map = projectListService.insertProjectList(projectListView);
        return map;
    }

}
