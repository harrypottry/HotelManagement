package com.aaroom.service;

import com.aaroom.beans.HotelExtra;
import com.aaroom.persistence.HotelExtraMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sosoda on 2018/10/22.
 */
@Service
public class HotelExtraService {

    @Autowired
    private HotelExtraMapper hotelExtraMapper;


    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateHotelExtra(HotelExtra hotelExtra) {
        if(hotelExtra.getId()!=0) {
            hotelExtraMapper.updateByPrimaryKeySelective(hotelExtra);
        }else {
            hotelExtraMapper.insertSelective(hotelExtra);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void insert(HotelExtra hotelExtra) {
        hotelExtraMapper.insertSelective(hotelExtra);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(HotelExtra hotelExtra) {
        hotelExtraMapper.updateByPrimaryKeySelective(hotelExtra);
    }

    public List<HotelExtra> listAll() {
        return hotelExtraMapper.listAll();
    }

    public HotelExtra getHotelExtra(String hotel_id) {
        return hotelExtraMapper.selectByPrimaryKey(hotel_id);
    }
}
