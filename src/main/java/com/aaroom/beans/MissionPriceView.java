package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by 温建成 on 2018/12/25.
 */
@Data
public class MissionPriceView {

    //人员姓名
    private String employeeName;
    //人员id
    private String employeeId;
    //酒店名称
    private String hotelName;
    //酒店标号
    private String hotelId;
    //工作任务类型
    private String missionType;
    //工作任务编号
    private String missionId;
    //备注
    private String comment;
    //任务开始时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String beginTime;
    //任务结束时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String endTime;
    //返工情况
    private String isOk;
    //AA供应酒店价格
    private double price;
    //扣款金额
    private double reworkPrice;
    //和阿姨结算金额
    private double cleanPrice;

    private String roomName;

    private String street_address;//任务 酒店地址

    private Integer jobType;// 1是兼职 2是全职

    private Integer roomNum;//房间数

    private String jobTime;//到岗时间

    private Double distance;//距离

    private Double priceForCleaner;//保洁员获得的报酬

    private String jobCreateTime;//到岗时间



}
