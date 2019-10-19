package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by sosoda on 2018/11/18.
 */
@Data
public class ClothView implements Comparable {

    private Integer id;

    private Integer status;//干净状态 0干净 1脏

    private String status_name;

    private Integer recycle_num;

    private String comment;

    private Integer hotel_id;

    private String rfID;

    private String clothTypeIds;

    private Integer scan_num;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_time;

    private List<ClothType> clothTypes;//20190102都用此属性表示中文属性

    private List clothTypes_name;

    private List<ClothLog> clothLogs;

    private List<Mission> missions;

    private Long StayTimeDay;//停留时间 （天）

    public ClothView(){

    }

    public ClothView(Cloth cloth) {
        this.id = cloth.getId();
        this.status = cloth.getStatus();
        this.recycle_num = cloth.getRecycle_num();
        this.comment = cloth.getComment();
        this.hotel_id = cloth.getHotel_id();
        this.rfID = cloth.getRfID();
        this.scan_num = cloth.getScan_num();
        this.update_time = cloth.getUpdate_time();
    }




    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
