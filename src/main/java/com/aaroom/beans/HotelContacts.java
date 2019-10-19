package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class HotelContacts{
    private Integer id;

    private Integer hotelId;

    private String contactsName;

    private String contactsPhone;

    private String contactsQq;

    private String contactsEmail;

    private Integer contactsType;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

}