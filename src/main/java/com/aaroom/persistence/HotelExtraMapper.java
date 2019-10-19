package com.aaroom.persistence;

import com.aaroom.beans.HotelExtra;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelExtraMapper {
    int deleteByPrimaryKey(String id);

    int insert(HotelExtra record);

    int insertSelective(HotelExtra record);

    HotelExtra selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(HotelExtra record);

    int updateByPrimaryKey(HotelExtra record);

    List<HotelExtra> listAll();
}