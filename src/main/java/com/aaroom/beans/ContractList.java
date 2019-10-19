package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ContractList{
    private Integer id;

    private String contractNumber;

    private Integer contractType;

    private String contractNumberTouch;

    private Integer contractRecipientId;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date contractReceiveTime;

    private Integer contractSignatoryId;

    private Integer contractStatus;

    private String comments;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Byte isActive;
}