package com.aaroom.beans;

import lombok.Data;
import java.util.Date;
@Data
public class LaundryFactory {

    //
    private Integer id;
    //洗衣厂名
    private String laundryfactory_name;
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
