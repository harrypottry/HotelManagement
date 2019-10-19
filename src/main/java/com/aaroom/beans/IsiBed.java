package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 意向门店签约信息床型信息
 * @create: 2019-02-20 13:47
 **/

@Data
public class IsiBed {

    private Integer id;

    private String isiId;

    private String bedType;

    private String bedSize;

    private Integer bedNum;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
