package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class DesignPic{
    private Integer id;

    private Integer designId;

    private Integer groupId;

    private String picName;

    private String pic;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;
}