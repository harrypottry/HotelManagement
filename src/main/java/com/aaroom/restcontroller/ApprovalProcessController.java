package com.aaroom.restcontroller;

import com.aaroom.beans.ApprovalParams;
import com.aaroom.model.ProcessParams;
import com.aaroom.service.ApprovalProcessService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 审批流程处理
 * @create: 2019-02-28 10:50
 **/
@RestController
public class ApprovalProcessController {

    @Autowired
    private ApprovalProcessService approvalProcessService;

    @PostMapping(value = "/consolea/api/submitApproval")
    public void submitApproval(@RequestBody ApprovalParams approvalParams) {
        approvalProcessService.submitApproval(approvalParams);
    }

    @PostMapping(value = "/consolea/api/completeMyPersonTask")
    public void completeMyPersonTask(@RequestBody ProcessParams processParams) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", processParams.getType());
        map.put("typeb", processParams.getTypeb());
        map.put("transactor", processParams.getTransactor());
        ApprovalParams approvalParams = approvalProcessService.getApprovalByModuleId(processParams.getModuleId());
        Task task = approvalProcessService.getTaskByprocInstId(approvalParams.getProcInstId());
        approvalProcessService.completeMyPersonTask(map, processParams.getUserName(), task.getId(), processParams.getComment(), task.getProcessInstanceId(), true);
    }
}
