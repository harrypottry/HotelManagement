package com.aaroom.beans;

import lombok.Data;

/**
 * Created by 温建成 on 2019/1/8.
 */
@Data
public class AccountView {

    private Integer id;

    private String hotelName;

    private Integer hotelId;

    private Double rentalPrice;

    private Double washPrice;

    private Double damagePrice;

    private Double personnelPrice;

    private String beginTime;

    private String endTime;
}
