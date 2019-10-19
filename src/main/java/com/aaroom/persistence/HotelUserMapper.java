package com.aaroom.persistence;

import com.aaroom.beans.HotelUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HotelUserMapper {
    int insert(HotelUser record);

    int insertSelective(HotelUser record);

    List<HotelUser> listAll();

    HotelUser getHotelUserByUserId(@Param("user_id") String user_id);

    Integer getMonthlyUserCountByHotelId(@Param("hotel_id")String hotel_id, @Param("date") Date date);

    void activeHotelUser(HotelUser hotelUser);
}