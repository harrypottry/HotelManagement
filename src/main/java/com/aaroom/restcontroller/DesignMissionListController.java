package com.aaroom.restcontroller;

import com.aaroom.beans.DesignMissionList;
import com.aaroom.beans.DesignPic;
import com.aaroom.beans.DesignPicGroup;
import com.aaroom.service.DesignMissionListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/28.
 */
@RestController
public class DesignMissionListController {

    @Autowired
    private DesignMissionListService designMissionListService;
    //添加设计任务
    @PostMapping(value = "/console/api/insertDesignMissionList")
    public Map<String,Object> insertDesignMissionList(HttpServletRequest request,
                                                      @RequestBody DesignMissionList designMissionList
    ){
        Integer employeeId = (Integer) request.getSession().getAttribute("employee_id");
        designMissionList.setAddId(employeeId);
        Map<String,Object> map = designMissionListService.insertDesignMissionList(designMissionList);
        return map;
    }
    //查询设计任务列表
    @GetMapping(value = "/console/api/getDesignMissionList")
    public List<DesignMissionList> getDesignMissionList(HttpServletResponse response,
                                                @RequestParam(value = "hotelId",required = false ) Integer hotelId
    ){
        List<DesignMissionList> list = designMissionListService.getDesignMissionList(hotelId);
        return list;
    }
    //详情页查询
    @GetMapping(value = "/console/api/getDesignMission")
    public DesignMissionList getDesignMission(HttpServletResponse response,
                                                        @RequestParam(value = "id",required = false ) Integer id
    ){
        DesignMissionList list = designMissionListService.getDesignMission(id);
        return list;
    }
    //上传图片和附件
    @GetMapping(value = "/app/console/api/insertDesignPic")
    public Map<String,Object> insertDesignPic(
                                              @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                              @RequestParam(value = "designId", required = false) Integer designId ,
                                              @RequestParam(value = "status", required = false) Integer status) throws IOException {
        Map<String,Object> map = designMissionListService.insertDesignPic(files,designId,status);
        return map;
    }
    //添加分组
    @PostMapping(value = "/console/api/insertDesignPicGroup")
    public Map<String,Object> insertDesignPicGroup(HttpServletRequest request,
                                                      @RequestBody DesignPicGroup designPicGroup
    ){
        Map<String,Object> map = designMissionListService.insertDesignPicGroup(designPicGroup);
        return map;
    }
    //查询分组
    @GetMapping(value = "/console/api/getDesignPicGroup")
    public List<DesignPicGroup> getDesignPicGroup(HttpServletResponse response,
                                                        @RequestParam(value = "designId",required = false ) Integer designId
    ){
        List<DesignPicGroup> list = designMissionListService.getDesignPicGroup(designId);
        return list;
    }
    //修改名字
    @GetMapping(value = "/app/console/api/updatePicName")
    public Map<String,Object> updatePicName(
            @RequestParam(value = "id", required = false) Integer id ,
            @RequestParam(value = "newName", required = false) String newName) throws IOException {
        Map<String,Object> map = designMissionListService.updatePicName(id,newName);
        return map;
    }
    //图片分组
    @GetMapping(value = "/app/console/api/updatePicOnGroup")
    public Map<String,Object> updatePicOnGroup(
            @RequestParam(value = "groupId", required = false) Integer groupId ,
            @RequestParam(value = "picList", required = false) List<Integer> picList) throws IOException {
        Map<String,Object> map = designMissionListService.updatePicOnGroup(groupId,picList);
        return map;
    }
    //删除图片
    @GetMapping(value = "/app/console/api/deleteDesignPic")
    public Map<String,Object> deleteDesignPic(
            @RequestParam(value = "picList", required = false) List<Integer> picList) throws IOException {
        Map<String,Object> map = designMissionListService.deleteDesignPic(picList);
        return map;
    }
    //展示图片列表
    @GetMapping(value = "/console/api/getDesignPicList")
    public List<DesignPic> getDesignPicList(HttpServletResponse response,
                                            @RequestParam(value = "groupId",required = false ) Integer groupId
    ){
        List<DesignPic> list = designMissionListService.getDesignPicList(groupId);
        return list;
    }
}
