package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.persistence.DesignMissionListMapper;

import com.aaroom.persistence.DesignPicGroupMapper;
import com.aaroom.persistence.DesignPicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * Created by 温建成 on 2019/2/28.
 */
@Service
public class DesignMissionListService {

    @Autowired
    private DesignMissionListMapper designMissionListMapper;

    @Autowired
    private DesignPicMapper designPicMapper;

    @Autowired
    private DesignPicGroupMapper designPicGroupMapper;

    @Autowired
    private StorageService storageService;

    public Map<String,Object> insertDesignMissionList(DesignMissionList designMissionList) {
        try {
            designMissionListMapper.insertSelective(designMissionList);
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "添加任务失败！");
        }
        return resultMap("success", "添加任务成功！");
    }
    public Map<String, Object> resultMap(String value1, String value2) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", value1);
        map.put("msg", value2);
        return map;
    }

    public List<DesignMissionList> getDesignMissionList(Integer hotelId) {
        DesignMissionListExample example = new DesignMissionListExample();
        DesignMissionListExample.Criteria criteria = example.createCriteria();
        criteria.andHotelIdEqualTo(hotelId);
        List<DesignMissionList> list = designMissionListMapper.selectByExample(example);
        return list;
    }

    public DesignMissionList getDesignMission(Integer id) {
        DesignMissionList designMissionList = designMissionListMapper.selectByPrimaryKey(id);
        return designMissionList;
    }

    public Map<String,Object> insertDesignPic(List<MultipartFile> files,Integer designId, Integer status) {
        try {
            DesignPic designPic = new DesignPic();
            designPic.setDesignId(designId);
            designPic.setStatus(status);
            if (files != null && files.size() > 0) {
                List<String> fileNames = new ArrayList<>();
                DesignPicGroupExample example = new DesignPicGroupExample();
                DesignPicGroupExample.Criteria criteria = example.createCriteria();
                criteria.andDesignIdEqualTo(designId);
                criteria.andGroupNameEqualTo("未分组");
                DesignPicGroup designPicGroups = (DesignPicGroup) designPicGroupMapper.selectByExample(example);
                if(designPicGroups!=null){
                    designPicGroups.setPicNum(designPicGroups.getPicNum()+files.size());
                    int i = designPicGroupMapper.updateByPrimaryKeySelective(designPicGroups);
                    designPic.setGroupId(i);
                }else{
                    DesignPicGroup designPicGroup = new DesignPicGroup();
                    designPicGroup.setGroupName("未分组");
                    designPicGroup.setDesignId(designId);
                    designPicGroup.setPicNum(files.size());
                    int i = designPicGroupMapper.insertSelective(designPicGroup);
                    designPic.setGroupId(i);
                }
                for (MultipartFile multipartFile : files) {
                    String[] fileName = multipartFile.getOriginalFilename().split("\\.");
                    File tempFile = File.createTempFile(fileName[0], fileName[1]);
                    multipartFile.transferTo(tempFile);
                    String destFileName = UUID.randomUUID().toString() + "." + fileName[1];
                    storageService.uploadFile(tempFile, destFileName);
                    fileNames.add(destFileName);
                    designPic.setPicName(destFileName);
                    designPic.setPic(storageService.generatePresignUrl(destFileName));
                    designPicMapper.insertSelective(designPic);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "图片上传失败！");
        }
        return resultMap("success", "图片上传成功！");
    }

    public Map<String,Object> insertDesignPicGroup(DesignPicGroup designPicGroup) {
        try {
            designPicGroupMapper.insertSelective(designPicGroup);
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "添加分组失败！");
        }
        return resultMap("success", "添加分组成功！");
    }

    public List<DesignPicGroup> getDesignPicGroup(Integer designId) {
        DesignPicGroupExample example = new DesignPicGroupExample();
        DesignPicGroupExample.Criteria criteria = example.createCriteria();
        criteria.andDesignIdEqualTo(designId);
        List<DesignPicGroup> list = designPicGroupMapper.selectByExample(example);
        return list;
    }

    public Map<String,Object> updatePicName(Integer id, String newName) {
        try {
            DesignPic designPic = designPicMapper.selectByPrimaryKey(id);
            String picName = designPic.getPicName();
            String[] fileName = newName.split("\\.");
            Calendar cal = Calendar.getInstance();
            Date createTime = cal.getTime();
            String newPicName = fileName[0]+createTime+fileName[1];
            storageService.renameFile(newPicName,picName);
            designPic.setPicName(newPicName);
            designPic.setPic(storageService.generatePresignUrl(newPicName));
            designPicMapper.updateByPrimaryKeySelective(designPic);
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "添加分组失败！");
        }
        return resultMap("success", "添加分组成功！");
    }

    public Map<String,Object> updatePicOnGroup(Integer groupId, List<Integer> picList) {
        try {
           if(picList!=null && picList.size()>0){
               for (Integer picId:picList
                    ) {
                   DesignPic designPic = designPicMapper.selectByPrimaryKey(picId);
                   designPic.setGroupId(groupId);
                   designPicMapper.updateByPrimaryKeySelective(designPic);
               }
           }
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "图片分组失败！");
        }
        return resultMap("success", "图片分组成功！");
    }

    public Map<String,Object> deleteDesignPic(List<Integer> picList) {
        try {
            if(picList!=null && picList.size()>0){
                for (Integer picId:picList
                        ) {
                    designPicMapper.deleteByPrimaryKey(picId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "图片删除失败！");
        }
        return resultMap("success", "图片删除成功！");
    }

    public List<DesignPic> getDesignPicList(Integer groupId) {
        DesignPicExample example = new DesignPicExample();
        example.createCriteria().andGroupIdEqualTo(groupId);
        List<DesignPic> designPics = designPicMapper.selectByExample(example);
        return designPics;
    }
}
