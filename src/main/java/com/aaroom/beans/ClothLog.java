package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class ClothLog {
    private Integer id;

    private Integer cloth_id;

    private Integer hotel_id;

    private Integer mission_id;

    private Integer direction;

    private Integer possessor_id;

    private Integer possessor_type;

    private Integer status;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    private String uuid;

    public ClothLog(Integer id, Integer cloth_id, int hotel_id, Integer mission_id, Integer direction,
                    Integer possessor_id, Integer possessor_type, Integer status, Date create_time, Date update_time,
                    Integer creater_id, Integer updater_id, Byte is_active) {
        this.id = id;
        this.cloth_id = cloth_id;
        this.hotel_id = hotel_id;
        this.mission_id = mission_id;
        this.direction = direction;
        this.possessor_id = possessor_id;
        this.possessor_type = possessor_type;
        this.status = status;
        this.create_time = create_time;
        this.update_time = update_time;
        this.creater_id = creater_id;
        this.updater_id = updater_id;
        this.is_active = is_active;
    }

    public ClothLog() {
    }


    public ClothLog(Integer cloth_id, Integer hotel_id, Integer mission_id, Integer direction, Integer possessor_id, Integer possessor_type, Integer status) {
        this.cloth_id = cloth_id;
        this.hotel_id = hotel_id;
        this.mission_id = mission_id;
        this.direction = direction;
        this.possessor_id = possessor_id;
        this.possessor_type = possessor_type;
        this.status = status;
    }

    public ClothLog(Integer id, Integer cloth_id, Integer hotel_id, Integer mission_id, Integer direction, Integer possessor_id, Integer possessor_type, Integer status) {
        this.id = id;
        this.cloth_id = cloth_id;
        this.hotel_id = hotel_id;
        this.mission_id = mission_id;
        this.direction = direction;
        this.possessor_id = possessor_id;
        this.possessor_type = possessor_type;
        this.status = status;
    }

    public ClothLog(Integer id, Integer cloth_id, Integer hotel_id, Integer mission_id, Integer direction, Integer possessor_id, Integer possessor_type, Integer status, Date create_time, Date update_time, Integer creater_id, Integer updater_id, Byte is_active) {
        this.id = id;
        this.cloth_id = cloth_id;
        this.hotel_id = hotel_id;
        this.mission_id = mission_id;
        this.direction = direction;
        this.possessor_id = possessor_id;
        this.possessor_type = possessor_type;
        this.status = status;
        this.create_time = create_time;
        this.update_time = update_time;
        this.creater_id = creater_id;
        this.updater_id = updater_id;
        this.is_active = is_active;
    }


}