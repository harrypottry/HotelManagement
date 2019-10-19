package com.aaroom.beans;

import lombok.Data;

/**
 * Created by 温建成 on 2019/1/8.
 */
@Data
public class CollectedClaimView {

    private String monthTime;

    private Integer hotelId;

    private String hotelName;

    private Double oughtPrice;

    private Double actualPrice;

    private Double waitPrice;

    private Integer status;

    private Double adjustPrice;
}
