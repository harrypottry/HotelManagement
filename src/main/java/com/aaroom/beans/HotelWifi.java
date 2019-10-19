package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class HotelWifi{
    private Integer id;

    private Integer hotelId;

    private String wifiName;

    private String wifiPassword;

    private Integer statusCode;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;
}