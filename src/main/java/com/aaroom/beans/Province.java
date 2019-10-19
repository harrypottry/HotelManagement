package com.aaroom.beans;

import lombok.Data;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-02-25 18:00
 **/
@Data
public class Province {

    private String id;

    private String name;

    private Integer flag;

    private Integer isHot;

    private Integer status;
}
