package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class HotelPath{
    private Integer id;

    private Integer hotelId;

    private String addressName;

    private Double kilometreNum;

    private String addressPath;

    private String comments;

    private Integer pathType;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

}