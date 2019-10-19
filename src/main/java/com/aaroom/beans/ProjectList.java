package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectList{
    private Integer id;

    private Integer hotelId;

    private String dirname;

    private String jointName;

    private String jointPhone;

    private String comments;

    private Integer projectType;

    private Integer projectStatus;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

}