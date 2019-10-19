package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 认领账单
 * @create: 2019-01-04 09:50
 **/
@Data
public class ClaimAccount {

    private Integer id;

    //流水号
    private String flowNumber;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date transactionDate;

    private Integer hotelId;

    private String hotelName;

    //付款人
    private String drawee;

    //打款是否一致
    private Integer type;

    //付款账号
    private String paymentAccount;

    //交易金额
    private String transactionAmount;

    //摘要
    private String summary;

    //收款银行
    private String receivingBank;

    //已认领金额
    private double claimedAmount;

    //待认领金额
    private double tobeClaimedAmount;

    //认领次数
    private int claimTimes;

    //修改日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateDate;

    private int status;

    //private String remark;暂时不用
}
