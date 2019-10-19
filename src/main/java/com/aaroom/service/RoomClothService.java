package com.aaroom.service;

import com.aaroom.beans.RoomCloth;
import com.aaroom.persistence.RoomClothMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomClothService {

    @Autowired
    private RoomClothMapper roomClothMapper;

    public RoomCloth selectByPrimaryKey(Integer id){
        return roomClothMapper.selectByPrimaryKey(id);
    }

    public int getClothTypeIdByRoomTypeId(Integer room_type_id){
        return roomClothMapper.getClothTypeIdByRoomTypeId(room_type_id);
    }

    public int getNumberByRoomTypeId(Integer room_type_id){
        return roomClothMapper.getNumberByRoomTypeId(room_type_id);
    }

    public List<Map<String,Integer>> getTotalByRoomTypeId(int hotel_id){
        return roomClothMapper.getTotalByRoomTypeId(hotel_id);
    }

    public List<RoomCloth> getAllByClothTypeId(Integer cloth_type_id){
        return roomClothMapper.getAllByClothTypeId(cloth_type_id);
    }

    public List<RoomCloth> getRoomClothByHotelIdAndRoomId(Integer room_type_id){
        return roomClothMapper.getRoomClothByHotelIdAndRoomId(room_type_id);
    }
    //传入酒店idList sql排序出来有序的RoomCloth对象集合  用group by
    public List<RoomCloth> getRoomClothByroomtypeidList(List room_type_idList){
        return roomClothMapper.getRoomClothByroomtypeidList(room_type_idList);
    }
}
