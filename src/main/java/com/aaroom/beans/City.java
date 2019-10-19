package com.aaroom.beans;

import lombok.Data;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-02-25 18:01
 **/
@Data
public class City {

    private String id;

    private String provinceCode;

    private String name;

    private Integer flag;

    private Integer isHot;

    private Integer status;
}
