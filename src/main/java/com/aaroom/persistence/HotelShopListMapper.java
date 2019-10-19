package com.aaroom.persistence;

import com.aaroom.beans.HotelShopList;
import com.aaroom.beans.HotelShopListExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelShopListMapper {
    int countByExample(HotelShopListExample example);

    int deleteByExample(HotelShopListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HotelShopList record);

    int insertSelective(HotelShopList record);

    List<HotelShopList> selectByExample(HotelShopListExample example);

    HotelShopList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HotelShopList record, @Param("example") HotelShopListExample example);

    int updateByExample(@Param("record") HotelShopList record, @Param("example") HotelShopListExample example);

    int updateByPrimaryKeySelective(HotelShopList record);

    int updateByPrimaryKey(HotelShopList record);

    List<Map<String,Object>> getHotelShopList(@Param(value = "hotelId")Integer hotelId,
                                              @Param(value = "proprietorName")String proprietorName,
                                              @Param(value = "expandId")Integer expandId,
                                              @Param(value = "shopStatus")Integer shopStatus,
                                              @Param(value = "shopManagerId")Integer shopManagerId);
}