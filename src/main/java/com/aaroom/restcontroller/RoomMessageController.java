package com.aaroom.restcontroller;

import com.aaroom.beans.RoomMessage;
import com.aaroom.service.RoomMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/27.
 */
@RestController
public class RoomMessageController {

    @Autowired
    private RoomMessageService roomMessageService;
    //查询客房信息列表
    @GetMapping(value = "/console/api/getRoomMessageList")
    public List<RoomMessage> getRoomMessageList(HttpServletResponse response,
                                                  @RequestParam(value = "hotelId",required = false ) Integer hotelId
    ){
        List<RoomMessage> list = roomMessageService.getRoomMessageList(hotelId);
        return list;
    }
    //添加客房信息
    @PostMapping(value = "/console/api/insertRoomMessage")
    public Map<String,Object> insertRoomMessage(@RequestBody RoomMessage roomMessage
    ){
        Map<String,Object> map = roomMessageService.insertRoomMessage(roomMessage);
        return map;
    }
    //删除客房信息
    @GetMapping(value = "/console/api/deleteRoomMessageList")
    public Map<String,Object> deleteRoomMessageList(HttpServletResponse response,
                                                @RequestParam(value = "ids",required = false ) Integer[] ids
    ){
        Map<String,Object> map = roomMessageService.deleteRoomMessageList(ids);
        return map;
    }
    //生成房型图
    @GetMapping(value = "/console/api/getRoomMessageListView")
    public List<Map<String,Object>> getRoomMessageListView(HttpServletResponse response,
                                                @RequestParam(value = "hotelId",required = false ) Integer hotelId
    ){
        List<Map<String,Object>> map = roomMessageService.getRoomMessageListView(hotelId);
        return map;
    }
    //提交
    @PostMapping(value = "/console/api/submitRoomMessage")
    public Map<String,Object> submitRoomMessage(@RequestBody RoomMessage roomMessage,
                                                @RequestParam(value = "hotelId",required = false ) Integer hotelId,
                                                @RequestParam(value = "status",required = false ) Integer status
    ){
        Map<String,Object> map = roomMessageService.submitRoomMessage(hotelId,status);
        return map;
    }
    //编辑
    @GetMapping(value = "/console/api/getRoomMessageById")
    public Map<String,Object> getRoomMessageById(HttpServletResponse response,
                                                @RequestParam(value = "id",required = false ) Integer id
    ){
        Map<String,Object> roomMessage = roomMessageService.getRoomMessageById(id);
        return roomMessage;
    }
}
