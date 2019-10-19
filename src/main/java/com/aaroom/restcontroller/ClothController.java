package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.model.RoomView;
import com.aaroom.service.*;
import com.aaroom.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存初始化过程：
 * 库管扫码布草包，将cloth的id与cloth_type_id 传过来，插入即可，并将信息返回前端
 */
@RestController
public class ClothController {

    @Autowired
    private ClothService clothService;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private ClothErrorService clothErrorService;

    @Autowired
    private ConsumptionService consumptionService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private MissionService missionService;

    @PostMapping(value = "/wx/console/api/checkClothRfidRelBuild")
    public Object checkClothRfidRelBuild(HttpServletRequest request,
                                    Integer id,
                                    String rfID){

        Cloth cloth = clothService.selectByrfID(rfID);
        if(cloth!=null) {
            return RestError.E_RFTID_EXIST;
        }

        cloth = clothService.getClothByid(id);

        if(cloth!=null) {
            return RestError.E_CLOTHID_EXIST;
        }
        return RestError.E_OK;
    }

    @PostMapping(value = "/wx/console/api/clothRfidRelBuild")
    public Object clothRfidRelBuild(HttpServletRequest request,
                                    Integer id,
                                    String rfID){

        Cloth cloth = clothService.selectByrfID(rfID);
        if(cloth!=null) {
            return RestError.E_RFTID_EXIST;
        }

        cloth = clothService.getClothByid(id);

        if(cloth!=null) {
            return RestError.E_CLOTHID_EXIST;
        }

        cloth = new Cloth();
        cloth.setId(id);
        cloth.setRfID(rfID);
        cloth.setIs_active(new Byte("0"));
        clothService.insertSelective(cloth);
        return new RestError(cloth);
    }



    @PostMapping(value = "/wx/console/api/clothTypeRelBuild")
    public Object clothTypeRelBuild(HttpServletRequest request,
                                    Integer[] cloth_ids,
                                    Integer[] cloth_type_ids){
        if(cloth_ids!=null && cloth_ids.length>0 && cloth_type_ids!=null && cloth_type_ids.length>0) {
            relClothTypeService.buildclothTypeRelInBatch(cloth_ids, cloth_type_ids);
        }
        return RestError.E_OK;
    }

    @GetMapping("/wx/console/api/clothTypes")
    public Object clothTypes(HttpServletRequest request){
        Constants.ClothKind[] kinds = Constants.ClothKind.values();
        Map<Object, Object> ret = new HashMap<>();
        for (Constants.ClothKind kind: kinds) {
            List<ClothType> clothTypeList = clothTypeService.getClothTypeByKind(kind);
            ret.put(kind, clothTypeList);
        }
        return new RestError(ret);
    }

    @GetMapping("/wx/console/api/getLatestClothList")
    public Object getLatestClothList(HttpServletRequest request,
                                     Long time,
                                     Integer[] cloth_type_ids){
        ArrayList<ClothView> clothViews = new ArrayList<>();
        Date start = new Date(new Date().getTime()-time);
        List<Cloth> cloths = clothService.getLatestClothList(start, cloth_type_ids);
        clothViews.addAll(cloths.stream().map(cloth -> clothService.getClothView(cloth, null, true, false, false)).collect(Collectors.toList()));
        return new RestError(clothViews);
    }


    @GetMapping("/wx/console/api/getClothView")
    public Object getClothView(HttpServletRequest request,
                               @RequestParam(value = "id") Integer id,
                               @RequestParam(value = "isAllClothTypes",required = false, defaultValue = "false")Boolean isAllClothTypes,
                               @RequestParam(value = "isClothLogs",required = false, defaultValue = "false")Boolean isClothLogs,
                               @RequestParam(value = "isMissions",required = false, defaultValue = "false")Boolean isMissions){


        Cloth cloth = clothService.getClothByid(id);
        if (cloth != null) {
            return new RestError(clothService.getClothView(null, id, isAllClothTypes, isClothLogs, isMissions));
        } else {
            return RestError.E_DATA_FAIL;
        }
    }


    @GetMapping("/wx/console/api/getClothErrors")
    public Object getClothErrors(HttpServletRequest request){
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelIdByEmpId((Integer) employee_id);
        List<ClothError> clothErrors = clothErrorService.getErrorsByHotelId(hotelEmployee.getHotel_id());
        List<Map<String, Object>> ret = new ArrayList<>();
        for (ClothError clothError: clothErrors) {
            Map<String, Object> cloth = new HashMap<>();
            cloth.put("clothError", clothError);
            cloth.put("clothView", clothService.getClothView(clothError.getCloth_id()));
            ret.add(cloth);
        }
        return new RestError(ret);
    }

    @PostMapping("/wx/console/api/postClothError")
    public Object postClothError(HttpServletRequest request,
                                 Integer cloth_id,
                                 @RequestParam(value = "comment",required = false) String comment,
                                 Constants.ClothErrorType type){
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelIdByEmpId((Integer) employee_id);

        //验证此布草是否有效
        Cloth cloth = clothService.getClothByid(cloth_id);
        if(cloth==null) {
            return RestError.E_DATA_INVALID;
        }

        ClothError clothError = new ClothError();
        clothError.setHotel_id(hotelEmployee.getHotel_id());
        clothError.setCloth_id(cloth_id);
        clothError.setComment(comment);
        clothError.setType(type);
        clothErrorService.insert(clothError);
        return RestError.E_OK;
    }


    @PostMapping("/wx/api/getAllConsumption")
    public Object getAllConsumption(){
        return consumptionService.getAllConsumption();
    }




    @GetMapping("/c/{id}")
    public void clientH5Recirect(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @PathVariable Integer id) throws IOException {
        Cloth cloth = clothService.getClothByid(id);
        cloth.setScan_num(cloth.getScan_num()+1);
        clothService.update(cloth);

        response.sendRedirect("/aachainWeChat/h5/clothDetail.html?id="+id);
    }


    @PostMapping("/client/clothDetail")
    public Object clothDetailToClient(HttpServletRequest request,
                                    @RequestParam Integer id){

        Map<String, Object> ret = new HashMap<>();
        //布草基本信息
        ClothView cloth = clothService.getClothView(id);
        ret.put("cloth", cloth);
        //更换布草人员
        {
            List<ClothLog> clothLogs = clothLogService.getMaxClothLogByClothId(id);
            ClothLog clothLog = clothLogs.get(0);
            Integer accept_employee_id = clothLog.getCreater_id();
            Employee employee = employeeService.getEmployee(accept_employee_id);
            Map<String, Object> mission_worker_info = new HashMap<>();
            mission_worker_info.put("time", clothLog.getCreate_time().getTime());
            mission_worker_info.put("employee_name", employee.getName());
            ret.put("mission_worker_info", mission_worker_info);
        }
        //送洗人员
        //差所有此条布草洗衣厂入的记录
        {
            List<ClothLog> clothLogListDuringHotel = clothLogService.getClothLogListDuringHotel(null, null, id, 1, 1, 2);
            if(clothLogListDuringHotel==null || clothLogListDuringHotel.size()==0) {
                ret.put("hotel_worker_info", "暂无送洗记录");
            } else {
                ClothLog clothLog = clothLogListDuringHotel.get(0);
                Integer hotel_possessor_id = clothLog.getCreater_id();
                Employee employee = employeeService.getEmployee(hotel_possessor_id);
                Map<String, Object> hotel_worker_info = new HashMap<>();
                hotel_worker_info.put("time", clothLog.getCreate_time().getTime());
                hotel_worker_info.put("employee_name", employee.getName());
                ret.put("hotel_worker_info", hotel_worker_info);
            }
        }

        //此布草同房间内所有其他布草
        {
            List<ClothLog> clothLogListDuringHotel = clothLogService.getClothLogListDuringHotel(null, null, id, 1, 0, 0);
            ClothLog clothLog = clothLogListDuringHotel.get(0);
            Integer mission_id = clothLog.getMission_id();
            Map<String, List> map = clothLogService.getLogListByMissionidPossessorTypeidDirectionid(mission_id);
            List<Integer> newClothlist = map.get("NewClothlist");
            List<ClothView> clothList = new ArrayList<>();
            for (Integer cloth_id: newClothlist) {
                if(cloth_id.equals(id)) {
                    continue;
                }
                clothList.add(clothService.getClothView(cloth_id));
            }
            ret.put("clothList",clothList);

        }
        return new RestError(ret);

    }

    @PostMapping("/client/roomClothDetail")
    public RestError roomClothDetail(HttpServletRequest request,
                                     @RequestParam Integer id){

        //房间信息
        RoomView roomView = roomService.getRoomView(null,id);
        if(roomView==null) {
            return RestError.E_DATA_INVALID;
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("room", roomView);

        {
            Integer[] status = new Integer[]{30, 90};
            List<Mission> missions = missionService.getMissionsByRoomId(id, status, 1);
            if(missions!=null && missions.size()>0) {
                Mission mission = missions.get(0);
                Employee employee = employeeService.getEmployee(mission.getAccept_employee_id());
                Map<String, List> map = clothLogService.getLogListByMissionidPossessorTypeidDirectionid(mission.getId());
                List<Integer> newClothlist = map.get("NewClothlist");

                List<Object> clothList = new ArrayList<>();
                for (Integer cloth_id: newClothlist) {
                    Map<String, Object> clothObj = new HashMap<>();
                    clothObj.put("cloth",clothService.getClothView(cloth_id));
                    clothObj.put("employee_name", employee.getName());
                    clothObj.put("change_time", mission.getEnd_time().getTime());

                    List<ClothLog> clothLogListDuringHotel = clothLogService.getClothLogListDuringHotel(null, null, id, 1, 1, 2);
                    if(clothLogListDuringHotel==null || clothLogListDuringHotel.size()==0) {
                        clothObj.put("wash_time", "暂无送洗记录");
                    } else {
                        ClothLog clothLog = clothLogListDuringHotel.get(0);
                        clothObj.put("wash_time", clothLog.getCreate_time().getTime());
                    }
                    clothList.add(clothObj);
                }
                ret.put("clothList",clothList);
            }

        }
        return new RestError(ret);
    }

}
