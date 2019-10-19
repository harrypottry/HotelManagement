package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Account{

    private Integer id;

    private String accountName;

    private Integer hotelId;

    private Double price;

    private Double withPrice;

    private String beginTime;

    private String endTime;

    private Integer status;

    private Integer type;

    private String comments;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Byte isActive;

    private Double updatePrice;



}