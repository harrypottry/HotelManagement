package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @className ReceiveCothController
 * @Description 这个类主要是干 【小程序rp布草第二排 领取仓库布草】
 * @Author 张赢
 * @Date 2018/11/26 0026下午 19:23
 * @Version 1.0
 **/
@RestController
public class ReceiveCothController {
    @Autowired
    private MissionService missionService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomClothService roomClothService;

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    //根据登录人id 得到对应missionlist
    @RequestMapping(value = "/wx/console/api/getmissionListbyempId", method = RequestMethod.POST)
    public Object getmissionListbyempId(HttpServletRequest request) throws Exception {
        //根据登录人状态得到employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id找到所有未完成的mission 显示 房间号 房间属性中文 任务属性中文 任务状态中文
        List missionRoomtypeList = missionService.getMissionRoomtypeListByemployee_id((Integer) employee_id);
        if (missionRoomtypeList.size() > 0) {
            return missionRoomtypeList;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }

    //根据所选房间展示 建议领取布草
    @RequestMapping(value = "/wx/console/api/suggestReceiveCloth", method = RequestMethod.POST)
    public Object suggestReceiveCloth(HttpServletRequest request, Integer[] room_idlist) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
        //遍历roomid 结合上面的hotel_id 取得不同房型的 room_type_idlist
        Map<Integer, Integer> room_type_idMap = new HashMap<>();
        if (room_idlist!=null && room_idlist.length > 0) {
            for (Integer roomid :
                    room_idlist) {
                Room room = roomService.getRoomById(roomid);
                room_type_idMap.put(room.getRoom_type_id(), room_type_idMap.get(room.getRoom_type_id()) == null ? 1 : (room_type_idMap.get(room.getRoom_type_id()) + 1));
            }
            //把list排一下序
            //Collections.sort(room_type_idList);
            //根据所得的room_type_idlist 遍历 去room_cloth表查到 cloth_type_id 和 number （要相加如果之前的cloth_type_id相同）

            //传入酒店id，room—type—id

            Map<String, Object> ret = new HashMap<>();

            //获取床单类型对应的数量
            Map<String, Integer> numMap = new HashMap<>();
            for (Integer room_type_id : room_type_idMap.keySet()) {
                List<RoomCloth> roomClothList = roomClothService.getRoomClothByHotelIdAndRoomId(room_type_id);
                for (RoomCloth roomCloth : roomClothList) {
                    //获取（type，size，brand,Material），将其拆分成数组
                    String cloth_type_id = roomCloth.getCloth_type_id();

                    //根据查询出来的ids查找相对应的clothtype
                    //获取cloth_type_id(1,2,3)
                    //List<ClothType> clothTypeList = clothTypeService.getClothTypeDescByIds(clothTypeIds);
                    if (numMap.get(cloth_type_id) == null) {
                        numMap.put(cloth_type_id, roomCloth.getNumber() * room_type_idMap.get(room_type_id));
                    } else {
                        numMap.put(cloth_type_id, roomCloth.getNumber() * room_type_idMap.get(room_type_id) + numMap.get(cloth_type_id));
                    }
                }
            }
            ret.put("num", numMap);


            //获取床单对应的名字
            Map<String, Object> nameMap = new HashMap<>();
            for (String cloth_type_id: numMap.keySet()) {
                String[] clothTypeIds = cloth_type_id.split(",");
                nameMap.put(cloth_type_id, clothTypeService.getClothTypeDescByIds(clothTypeIds));
            }
            ret.put("name", nameMap);

            return ret;
        }
        return null;
    }

    //扫描领取布草 增加双事务日志【张赢】
    @RequestMapping(value = "/wx/console/api/scanclothreceive", method = RequestMethod.POST)
    public Object scanclothreceive(HttpServletRequest request,
                                   @RequestParam(value = "cloth_id_list")String[] cloth_id_list) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();

            for (int j = 0; j <cloth_id_list.length ; j++) {
                clothLogService.savedoublelogvoid(Integer.parseInt(cloth_id_list[j]), employee_id.toString(), hotel_id);
            }
            return RestError.E_OK ;
    }

}