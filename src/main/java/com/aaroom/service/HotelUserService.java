package com.aaroom.service;

import com.aaroom.beans.HotelUser;
import com.aaroom.persistence.HotelUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by sosoda on 2018/10/25.
 */
@Service
public class HotelUserService {

    @Autowired
    private HotelUserMapper hotelUserMapper;

    public void insert(HotelUser hotelUser) {
        hotelUserMapper.insertSelective(hotelUser);
    }

    public HotelUser getHotelUserByUserId(String user_id) {
        return hotelUserMapper.getHotelUserByUserId(user_id);
    }

    public Integer getMonthlyUserCountByHotelId(String hotel_id, Date date) {
        return hotelUserMapper.getMonthlyUserCountByHotelId(hotel_id, date);
    }
}
