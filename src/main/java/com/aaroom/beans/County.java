package com.aaroom.beans;

import lombok.Data;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-02-25 18:04
 **/
@Data
public class County {

    private String id;

    private String cityCode;

    private String name;

    private Integer flag;

    private Integer isHot;

    private Integer status;
}
