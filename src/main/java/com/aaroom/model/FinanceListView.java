package com.aaroom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 租赁账单数据
 * @create: 2018-12-25 13:38
 **/
@Data
public class FinanceListView {

    private String clothName;

    private String clothSize;

    private String clothMaterial;

    private String clothId;

    private String hotelName;

    private String hotelID;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date beginDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endDate;

    private double rentalPrice;//租赁字段

    private double payWashPrice;//洗涤字段

    private double collectWashPrice;//洗涤字段

    private String washFactory;//洗涤字段

    private String uuid;

    public FinanceListView(String clothName, double rentalPrice) {
        this.clothName = clothName;
        this.rentalPrice = rentalPrice;
    }

    public FinanceListView(String clothName, double payWashPrice, double collectWashPrice) {
        this.clothName = clothName;
        this.payWashPrice = payWashPrice;
        this.collectWashPrice = collectWashPrice;
    }
}
