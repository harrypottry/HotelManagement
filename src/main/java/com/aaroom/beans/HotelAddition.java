package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class HotelAddition{
    private Integer id;

    private Integer hotelId;

    private String longitud;

    private String latitude;

    private String phone;

    private String faxes;

    private String postalCode;

    private String placeAddress;

    private Date openingTime;

    private Date renovationTime;

    private Integer hotelBuild;

    private Integer receptionStatus;

    private String channelCooperation;

    private String payment;

    private String hotelIntro;

    private String guideBook;

    private String warmPrompt;

    private String hotelServe;

    private Double parkPrice;

    private Double breakfastPrice;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

}