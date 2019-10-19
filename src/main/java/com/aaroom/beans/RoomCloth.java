package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class RoomCloth {
    private Integer id;



    private Integer room_type_id;

    private String cloth_type_id;

    private Integer number;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}