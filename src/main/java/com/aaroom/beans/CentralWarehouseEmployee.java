package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class CentralWarehouseEmployee implements Serializable {

    //
    private Integer central_warehouse_id;
    //
    private Integer employee_id;
    //1，店长 2，店员
    private Integer type;
    //
    private Date create_time;
    //
    private Date update_time;
    //
    private Integer creater_id;
    //
    private Integer updater_id;
    //
    private String is_active;



}
