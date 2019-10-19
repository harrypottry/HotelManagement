package com.aaroom.persistence;

import com.aaroom.beans.ClothError;
import com.aaroom.model.ClothErrorView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClothErrorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClothError record);

    Integer insertSelective(ClothError record);

    ClothError selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClothError record);

    int updateByPrimaryKey(ClothError record);

    List<ClothError> getErrorsByHotelId(@Param("hotel_id") int hotel_id);

    List<ClothError> getClothErrorsByHotelIdAndClothId(@Param("hotel_id") int hotel_id,
                                                       @Param("cloth_id") int cloth_id);
    //直接查询本酒店报损的clothidlist 上面是查clotherror对象list
    List<Integer> getErrorClothidByHotelId(@Param("hotel_id") int hotel_id);

    //根据hotelid 开始时间 结束时间查询报损列表 【张赢】 近一个月的（自然月）
    List<ClothError> getClothErrorByDuringTime(@Param("hotel_id") int hotel_id,
                                               @Param("StartTime") Date StartTime,
                                               @Param("EndTime") Date EndTime);

    List<ClothErrorView> getClothErrorList(@Param(value = "hotelId") Integer hotelId,
                                           @Param(value = "beginDate") String beginDate,
                                           @Param(value = "endDate") String endDate,
                                           @Param(value = "pageSize")Integer pageSize,
                                           @Param(value = "index")Integer index);

    List<ClothError> getErrorClothListByHotelidOrType(@Param(value = "hotelId") Integer hotelId,
                                                          @Param(value = "type") String type,
                                           @Param(value = "pageSize")Integer pageSize,
                                           @Param(value = "index")Integer index);

    Integer getErrorClothListByHotelidOrTypeNolimit(@Param(value = "hotelId") Integer hotelId,
                                                          @Param(value = "type") String type);


    Long getErrorClothListCount(@Param(value = "hotelId")Integer hotelId,
                                @Param(value = "beginDate") String beginDate,
                                @Param(value = "endDate")String endDate);


    List<ClothErrorView> getAllErrorClothListInfo();


}