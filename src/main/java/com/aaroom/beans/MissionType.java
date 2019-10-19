package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class MissionType {
    private Integer id;

    private String desc;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}