package com.aaroom.service;

import com.aaroom.beans.ApprovalParams;
import com.aaroom.persistence.ApprovalProcessMapper;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-02-28 10:52
 **/
@Service
public class ApprovalProcessService {

    private static final Logger log = LoggerFactory.getLogger(ApprovalProcessService.class);

    @Resource
    private ProcessEngine processEngine;

    @Autowired
    private ApprovalProcessMapper approvalProcessMapper;

    @Transactional
    public void submitApproval(ApprovalParams approvalParams) {
        String processDefinitionKey = "yxapply";
        Map<String, Object> map = new HashMap<>();
        map.put("applyer", approvalParams.getUserName());
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey, map);
        log.info("流程实例ID:" + pi.getId());
        log.info("流程定义ID:" + pi.getProcessDefinitionId());
        approvalParams.setBeginDate(new Date());
        approvalParams.setProcInstId(pi.getProcessInstanceId());
        approvalProcessMapper.saveApprovalProcess(approvalParams);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("transactor", approvalParams.getTransactor());
        Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
        completeMyPersonTask(map1, null, task.getId(), null, null, false);
    }

    public Task getTaskByprocInstId(String procInstId) {
        Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(procInstId).singleResult();
        return task;
    }

    public ApprovalParams getApprovalByModuleId(String moduleId) {
        ApprovalParams approvalParams = approvalProcessMapper.getApprovalByModuleId(moduleId);
        return approvalParams;
    }

    public void completeMyPersonTask(Map<String, Object> map, String userName, String taskId, String comment, String procInstId, boolean flag){
        if (flag) {
            Authentication.setAuthenticatedUserId(userName);
            processEngine.getTaskService().addComment(taskId, procInstId, comment);
        }
        processEngine.getTaskService().complete(taskId, map);
        log.info("完成任务:任务ID:"+taskId);
    }

}
