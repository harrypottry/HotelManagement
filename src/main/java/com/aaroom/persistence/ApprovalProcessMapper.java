package com.aaroom.persistence;

import com.aaroom.beans.ApprovalParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalProcessMapper {
    void saveApprovalProcess(@Param(value = "ap")ApprovalParams approvalParams);

    ApprovalParams getApprovalByModuleId(@Param(value = "moduleId")String moduleId);
}
