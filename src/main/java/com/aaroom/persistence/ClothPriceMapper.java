package com.aaroom.persistence;

import com.aaroom.beans.ClothPrice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * Created by liyp on 2018/12/26 0026.
 */
@Repository
public interface ClothPriceMapper {

    Double getPriceByHotelIdAndClothId(@Param("clothTypeId") String clothTypeId, @Param("hotelId") Integer hotelId);
    //根据id查对象
    ClothPrice getClothPriceById(@Param("id") Integer id,@Param("hotelId") Integer hotelId);
    //根据clothid查对象
    ClothPrice getClothPriceByClothId(@Param("ClothId") Integer ClothId,@Param("hotelId") Integer hotelId);
    //根据clothid查对象
    int insert(ClothPrice clothPrice);
}
