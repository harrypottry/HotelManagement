package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-01-18 16:21
 **/
@Data
public class ChargeConfig {

    private Integer id;

    private Integer hotelId;

    //收费项目
    private Integer chargeItem;

    //收费频率(1:年度、2:季度、3:月度、4:一次性)
    private Integer chargeRate;

    //收费基准
    private String chargeStandard;

    //计费规则
    private Double chargeRule;

    //收费类型(1:按照百分比收费、2:按照固定金额收费)
    @Deprecated
    private Integer type;

    private Integer status;

    //是否减免
    private Integer reduction;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date beginDate;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
