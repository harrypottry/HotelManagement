package com.aaroom.beans;

import com.aaroom.model.ClaimOptionsView;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-01-08 12:30
 **/
@Data
public class CollectClaim {

    private Integer id;

    private List<ClaimOptionsView> list;

    private Integer claimAccountId;

    private double adjustPriceSum;

    private double claimPriceSum;

    private Integer hotelId;

    private int status;

    //private String remark;暂时不用

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
