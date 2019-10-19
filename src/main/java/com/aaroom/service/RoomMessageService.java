package com.aaroom.service;

import com.aaroom.beans.RoomMessage;
import com.aaroom.beans.RoomMessageExample;
import com.aaroom.persistence.RoomMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/27.
 */
@Service
public class RoomMessageService {

    @Autowired
    private RoomMessageMapper roomMessageMapper;

    public List<RoomMessage> getRoomMessageList(Integer hotelId) {
        RoomMessageExample example = new RoomMessageExample();
        RoomMessageExample.Criteria criteria = example.createCriteria();
        criteria.andHotelIdEqualTo(hotelId);
        List<RoomMessage> list = roomMessageMapper.selectByExample(example);
        return list;
    }

    public Map<String,Object> insertRoomMessage(RoomMessage roomMessage) {
        try {
            String roomNumber = roomMessage.getRoomNumber();
            String[] split = roomNumber.split(",");
            for (int i = 0 ;i < split.length ;i++){
                RoomMessageExample example = new RoomMessageExample();
                RoomMessageExample.Criteria criteria = example.createCriteria();
                criteria.andRoomNumberLike(split[i]);
                criteria.andHotelIdEqualTo(roomMessage.getHotelId());
                if(roomMessage.getId() != null){
                    criteria.andIdNotEqualTo(roomMessage.getId());
                }
                List<RoomMessage> roomMessages = roomMessageMapper.selectByExample(example);
                if(roomMessages.size() != 0){
                    return resultMap("error", "房间编号有重复！");
                }
            }
            if(roomMessage.getId() != null){
                roomMessageMapper.updateByPrimaryKeySelective(roomMessage);
            }else{
                roomMessageMapper.insertSelective(roomMessage);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "添加信息失败！");
        }
        return resultMap("success", "添加信息成功！");
    }

    public Map<String, Object> resultMap(String value1, String value2) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", value1);
        map.put("msg", value2);
        return map;
    }

    public Map<String, Object> deleteRoomMessageList(Integer[] ids) {
        try {
            for (int i = 0 ;i < ids.length ;i++){
                roomMessageMapper.deleteByPrimaryKey(ids[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "删除信息失败！");
        }
            return resultMap("success", "删除信息成功！");
    }

    public List<Map<String,Object>> getRoomMessageListView(Integer hotelId) {
        List<RoomMessage> roomMessageList = getRoomMessageList(hotelId);
        HashMap<String, Object> map = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        for (RoomMessage roomMessage:roomMessageList
             ) {
            String[] split = roomMessage.getRoomNumber().split(",");
            map.put("roomMessage",roomMessage);
            map.put("roomNumbers",split);
            list.add(map);
        }
        return list;
    }

    public Map<String,Object> submitRoomMessage(Integer hotelId, Integer status) {
        try {
            List<RoomMessage> roomMessageList = getRoomMessageList(hotelId);
            for (RoomMessage roomMessage:roomMessageList
                    ) {
               roomMessage.setStatus(status);
               roomMessageMapper.updateByPrimaryKeySelective(roomMessage);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultMap("error", "提交信息失败！");
        }
        return resultMap("success", "提交信息成功！");
    }

    public Map<String,Object> getRoomMessageById(Integer id) {
        RoomMessage roomMessage = roomMessageMapper.selectByPrimaryKey(id);
        String[] split = roomMessage.getRoomNumber().split(",");
        HashMap<String, Object> map = new HashMap<>();
        map.put("roomMessage",roomMessage);
        map.put("roomNumbers",split);
        return map;
    }
}
