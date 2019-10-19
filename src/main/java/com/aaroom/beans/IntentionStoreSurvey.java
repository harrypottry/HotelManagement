package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-02-18 16:46
 **/
@Data
public class IntentionStoreSurvey {

    private String id;

    private String hotelName;

    private String hotelProvince;

    private String hotelCity;

    private String hotelCounty;

    private String hotelStreetAddress;

    private Integer roomNum;

    private String doorwayPhoto;

    private String owernName;

    private String owernPhone;

    private String owernEmail;

    private String owernIdcardPhotoz;

    private String owernIdcardPhotof;

    private String remark;

    private Integer status;

    private Integer type;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
