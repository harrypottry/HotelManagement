package com.aaroom.service;

import com.aaroom.beans.ClothExchange;
import com.aaroom.persistence.ClothExchangeMapper;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClothExchangeService {

    @Resource
    private ClothExchangeMapper clothExchangeMapper;

    public void insert(ClothExchange clothExchange){
        clothExchangeMapper.insertSelective(clothExchange);
    }

    public void update(ClothExchange clothExchange) {
        clothExchangeMapper.updateByPrimaryKeySelective(clothExchange);
    }

    public List<ClothExchange> getAllClothExchangeByAA(){
        return clothExchangeMapper.getAllClothExchangeByAA();
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateInBatch(Integer hotel_id, String clothBookingJson) {
        ArrayList<Map<String, Object>> clothBookingList = JSON.parseObject(clothBookingJson, ArrayList.class);
        for (Map<String, Object> clothBookingMap: clothBookingList) {
            ClothExchange example = new ClothExchange();
            example.setHotel_id(hotel_id);
            example.setCloth_type_id((String)clothBookingMap.get("cloth_type_id"));
            example.setCreate_time(new Date());
            ClothExchange clothExchange = getClothExchangeByExample(example);
            if(clothExchange!=null) {
                clothExchange.setNum((Integer.parseInt((String)clothBookingMap.get("num"))) );
                update(clothExchange);
            } else {
                example.setNum((Integer.parseInt((String)clothBookingMap.get("num"))));
                insert(example);
            }
        }
    }

    public ClothExchange getClothExchangeByExample(ClothExchange example) {
        return clothExchangeMapper.getClothExchangeByExample(example);
    }
}
