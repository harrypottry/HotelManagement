package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HotelShopList implements Serializable {
    private Integer id;

    private Integer hotelId;

    private Integer proprietorId;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date signatoryTime;

    private Integer expandId;

    private Integer shopManagerId;

    private Integer shopStatus;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;
}