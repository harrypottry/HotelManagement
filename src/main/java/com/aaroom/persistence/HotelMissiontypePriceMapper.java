package com.aaroom.persistence;

import com.aaroom.beans.HotelMissiontypePrice;
import org.apache.ibatis.annotations.Param;

public interface HotelMissiontypePriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HotelMissiontypePrice record);

    int insertSelective(HotelMissiontypePrice record);

    HotelMissiontypePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HotelMissiontypePrice record);

    int updateByPrimaryKey(HotelMissiontypePrice record);

    HotelMissiontypePrice getHMPByHotelAndMissionType(@Param(value = "hotel_id")Integer hotel_id,@Param(value = "mission_type")Integer mission_type);

}