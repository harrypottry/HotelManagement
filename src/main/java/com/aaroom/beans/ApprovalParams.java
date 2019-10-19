package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-02-28 14:54
 **/
@Data
public class ApprovalParams {

    private Integer id;

    private String userName;

    private String transactor;

    private String moduleId;//模块ID

    private String procInstId;//对应流程的主线程ID

    private Date beginDate;

    private Date endDate;
}
