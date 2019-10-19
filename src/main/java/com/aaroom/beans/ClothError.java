package com.aaroom.beans;

import com.aaroom.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ClothError {
    private Integer id;

    private Integer cloth_id;

    private Integer reporter_id;

    private Constants.ClothErrorType type;

    private String comment;

    private Integer status;

    private Integer responser_id;

    private Integer responser_type;

    private String pic;

    private Integer hotel_id;

    private Integer creater_id;

    private Integer updater_id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_time;

    private Boolean is_active;

    private String  cloth_type_id;
}