package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class DesignMissionList{
    private Integer id;

    private Integer hotelId;

    private String missionName;

    private Integer addId;

    private Integer missionType;

    private String supplierName;

    private Date measureTime;

    private String sizeMessage;

    private String comments;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

}