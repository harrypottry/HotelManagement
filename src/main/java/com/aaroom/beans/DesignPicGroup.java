package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class DesignPicGroup{
    private Integer id;

    private Integer designId;

    private String groupName;

    private Integer picNum;

    private Integer groupStatus;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

}