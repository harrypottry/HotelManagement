package com.aaroom.beans;

import lombok.Data;

import java.util.Date;
@Data
public class CentralWarehouse{

    //
    private Integer id;
    //总仓名
    private String central_warehouse_name;
    //区代码
    private String area_id;
    //省代码
    private String province_id;
    //城市代码
    private String city_id;
    //街道地址
    private String street_address;
    //总仓类型(预留)
    private String warehouse_type;
    //目前营业状态(预留)
    private String status;
    //由谁创建
    private String create_by;
    //
    private Date create_time;
    //
    private Date update_time;
    //
    private Integer creater_id;
    //
    private Integer updater_id;
    //
    private Byte is_active;

}
