package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Consumption {
    private Integer id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;

    private Byte isActive;


}