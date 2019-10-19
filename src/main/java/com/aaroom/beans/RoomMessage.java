package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoomMessage implements Serializable {
    private Integer id;

    private Integer hotelId;

    private String roomName;

    private Integer roomCount;

    private String roomNumber;

    private Double roomPrice;

    private Double goldPrice;

    private Double silverPrice;

    private Double networdPrice;

    private Double channelPrice;

    private String comments;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

}