package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class MissionQuestion {
    private Integer id;

    private Integer missionId;

    private String pic;

    private Date createTime;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;

    private Byte isActive;

}