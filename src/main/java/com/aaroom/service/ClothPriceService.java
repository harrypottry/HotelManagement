package com.aaroom.service;

import com.aaroom.beans.ClothPrice;
import com.aaroom.persistence.ClothPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className ClothPriceService
 * @Description 这个类主要是干 布草报价 送洗价 租赁价 收酒店价
 * @Author 张赢
 * @Date 2019/1/22 0022下午 15:06
 * @Version 1.0
 **/
@Service
public class ClothPriceService {

    @Autowired
    private ClothPriceMapper clothPriceMapper;

    public ClothPrice getClothPriceById(Integer id,Integer hotelId){
        return clothPriceMapper.getClothPriceById(id,hotelId);
    }

    public ClothPrice getClothPriceByClothId(Integer ClothId,Integer hotelId){
        return clothPriceMapper.getClothPriceByClothId(ClothId,hotelId);
    }
    //插入数据
    public int insert (ClothPrice clothPrice){
        return clothPriceMapper.insert(clothPrice);
    }

}
