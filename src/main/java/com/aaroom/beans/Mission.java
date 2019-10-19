package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Mission {
    private Integer id;

    private Integer hotel_id;

    private Integer room_id;

    private Integer mission_type;

    private Integer publish_employee_id;

    private Integer accept_employee_id;

    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date clean_begin_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date gaffer_begin_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date end_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date business_begin_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date business_end_time;

    private Integer is_ok;

    private String comment;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    //以下为missionview 特意增加的 因为只有一个页面需要，所以没新建missionview类
    private List mission_type_list;

    private List accept_employee_id_list;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date starttime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endtime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date position_time;

    private String accept_employee_name;

    private String mission_type_name;

    private String status_name;

    private String create_time_name;

    private String update_time_name;

    private Integer pageNo;

    private String room_name;

    private Double price;

    private String hotel_name;

    private Integer MissionWorkType;//兼职还是全职 1是兼职 2是全职
}