package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class ConsumptionLog {
    private Integer id;

    private Integer missionId;

    private Integer comsumptionId;

    private Integer num;

    private Date createTime;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;

    private Byte isActive;


}