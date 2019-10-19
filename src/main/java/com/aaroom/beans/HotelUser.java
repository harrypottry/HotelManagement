package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class HotelUser {
    private String user_id;

    private Integer hotel_id;

    private Integer employee_id;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;


}