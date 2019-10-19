package com.aaroom.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 导入银行流水Excel固定字段
 * @create: 2019-01-04 10:15
 **/
@Data
public class ClaimImportExcelView {

    private String flowNumber;

    private Date transactionDate;

    private String drawee;

    private String paymentAccount;

    private String transactionAmount;

    private String summary;

    private String receivingBank;
}
