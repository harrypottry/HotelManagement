package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class HotelMissiontypePrice {
    private Integer id;

    private Integer hotel_id;

    private Integer mission_type;

    private Double price;

    private Double rework_price;

    private Double clean_price;

    private String comments;

    private Date create_time;

    private Date update_time;

    private Integer create_id;

    private Integer update_id;

    private Integer is_active;


}