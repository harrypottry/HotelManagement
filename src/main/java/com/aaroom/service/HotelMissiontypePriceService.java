package com.aaroom.service;

import com.aaroom.beans.HotelMissiontypePrice;
import com.aaroom.persistence.HotelMissiontypePriceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class HotelMissiontypePriceService {

    @Resource
    private HotelMissiontypePriceMapper hotelMissiontypePriceMapper;



    public HotelMissiontypePrice getHMPByHotelAndMissionType(Integer hotel_id,Integer mission_type){
        return hotelMissiontypePriceMapper.getHMPByHotelAndMissionType(hotel_id,mission_type);
    }

    public Double getPriceDetail(Integer hotel_id,Integer mission_type,Integer ok_num,Integer unok_num){
        HotelMissiontypePrice hmtp = hotelMissiontypePriceMapper.getHMPByHotelAndMissionType(hotel_id, mission_type);
        //返回结果
        Double count = 0.00;

        //根据每种任务类型算出对应的工资
        switch (mission_type){
            case 1:
                count = (ok_num * hmtp.getClean_price()) +(unok_num * hmtp.getClean_price() - hmtp.getRework_price());
                break;
            case 2:
                count = (ok_num * hmtp.getClean_price()) +(unok_num * hmtp.getClean_price() - hmtp.getRework_price());
                break;
            case 3:
                count = (ok_num * hmtp.getClean_price()) +(unok_num * hmtp.getClean_price() - hmtp.getRework_price());
                break;
            case 4:
                count = (ok_num * hmtp.getClean_price()) +(unok_num * hmtp.getClean_price() - hmtp.getRework_price());
                break;
        }
        return count;
    }
}
