package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-01-18 16:26
 **/
@Data
public class ChargeConfigAttach {

    private Integer id;

    private Integer hotelId;

    //合同编号
    private String contractNum;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date endDate;

    private String remark;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
