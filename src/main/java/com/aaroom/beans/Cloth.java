package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

/**
 * 布草表
 */
@Data
public class Cloth {
    private Integer id;

    private Integer status;

    private Integer recycle_num;

    private String comment;

    private Integer hotel_id;

    private String rfID;

    private Integer scan_num;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;


}