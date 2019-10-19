package com.aaroom.persistence;

import com.aaroom.beans.HotelWifi;
import com.aaroom.beans.HotelWifiExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelWifiMapper {
    int countByExample(HotelWifiExample example);

    int deleteByExample(HotelWifiExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HotelWifi record);

    int insertSelective(HotelWifi record);

    List<HotelWifi> selectByExample(HotelWifiExample example);

    HotelWifi selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HotelWifi record, @Param("example") HotelWifiExample example);

    int updateByExample(@Param("record") HotelWifi record, @Param("example") HotelWifiExample example);

    int updateByPrimaryKeySelective(HotelWifi record);

    int updateByPrimaryKey(HotelWifi record);
}