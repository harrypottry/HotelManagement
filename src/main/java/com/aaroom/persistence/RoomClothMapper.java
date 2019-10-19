package com.aaroom.persistence;

import com.aaroom.beans.RoomCloth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoomClothMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomCloth record);

    int insertSelective(RoomCloth record);

    RoomCloth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomCloth record);

    int updateByPrimaryKey(RoomCloth record);

    int getClothTypeIdByRoomTypeId(Integer room_type_id);

    int getNumberByRoomTypeId(Integer room_type_id);

    List<Map<String,Integer>> getTotalByRoomTypeId(int hotel_id);

    List<RoomCloth> getAllByClothTypeId(Integer cloth_type_id);


    List<RoomCloth> getRoomClothByHotelIdAndRoomId(@Param("room_type_id")Integer room_type_id);

    //传入酒店idList sql排序出来有序的RoomCloth对象集合
    List<RoomCloth> getRoomClothByroomtypeidList(@Param("room_type_idList")List<Integer> room_type_idList);

}