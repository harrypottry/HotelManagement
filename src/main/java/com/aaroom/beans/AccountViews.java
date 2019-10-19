package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 温建成 on 2019/1/24.
 */
@Data
public class AccountViews {

    private Integer hotelId;

    private Integer type;

    private Integer monthNum;

    @JsonFormat(pattern="yyyy-MM",timezone="GMT+8")
    private Date monthTime;

    private Double price;
}
