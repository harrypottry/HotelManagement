package com.aaroom.beans;

import lombok.Data;
import org.omg.CORBA.StringHolder;

import java.util.List;

/**
 * Created by sosoda on 2018/10/22.
 */
@Data
public class HotelView {

    private Integer hotel_id;

    private Double profit_rate;

    private Double profit_rate_owner;

    private Double profit_rate_manager;

    private Double profit_rate_bd;

    private Integer room_num;

    private List<EmployeeView> employees;

    public HotelExtra getHotelExtra() {
        HotelExtra ret = new HotelExtra();
        ret.setId(this.getHotel_id());
        ret.setProfit_rate(this.getProfit_rate());
        ret.setProfit_rate_owner(this.getProfit_rate_owner());
        ret.setProfit_rate_manager(this.getProfit_rate_manager());
        ret.setProfit_rate_bd(this.getProfit_rate_bd());
        ret.setRoom_num(this.getRoom_num());
        return ret;
    }


    public HotelView(){

    }

    public HotelView(HotelExtra hotelExtra) {
        this.hotel_id = hotelExtra.getId();
        this.profit_rate = hotelExtra.getProfit_rate();
        this.profit_rate_owner = hotelExtra.getProfit_rate_owner();
        this.profit_rate_manager = hotelExtra.getProfit_rate_manager();
        this.profit_rate_bd = hotelExtra.getProfit_rate_bd();
        this.room_num = hotelExtra.getRoom_num();
    }

    //给app端返回 hotelname和相关的洗衣厂name
    private String hotel_name;

    //洗衣厂名称 list
    private List<Employee> WashFactoryList;

}
