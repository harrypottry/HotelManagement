package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class HotelExtra {
    private Integer id;

    private Integer room_num;

    private Double profit_rate;

    private Double profit_rate_owner;

    private Double profit_rate_manager;

    private Double profit_rate_bd;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;


}