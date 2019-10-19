package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-01-21 11:38
 **/
@Data
public class ChargeRenewConfig {
    private Integer id;

    private Double charge;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date beginDate;

    private String contractNum;

    private Integer chargeConfigId;

    private Integer status;

    private Integer hotelId;

    private Integer chargeRate;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
