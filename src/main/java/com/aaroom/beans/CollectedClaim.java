package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class CollectedClaim{
    private Integer id;

    private Integer accountId;

    private Integer claimAccountId;

    private Integer status;

    private Double price;

    private Double adjustPrice;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}