package com.aaroom.service;

import com.shangmei.beans.Hotel;
import com.shangmei.persistence.ShangmeiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sosoda on 2018/10/24.
 */
@Service
public class ShangmeiService {

    @Autowired
    private ShangmeiMapper shangmeiMapper;

    public Hotel getHotel(String id) {
        Hotel hotel = new Hotel();
        hotel.setName("ceshi");
        //Hotel hotel = shangmeiMapper.getHotel(id);
        return hotel;
    }
}
