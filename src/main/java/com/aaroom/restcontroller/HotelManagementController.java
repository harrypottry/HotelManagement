package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 库管端业务逻辑操作接口
 */
@RestController
public class HotelManagementController {

    @Autowired
    private ClothService clothService;

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private ClothExchangeService clothExchangeService;

    /**
     * 获取微信端登陆成功的session中的employee_id
     *
     * @param session
     * @return
     */
    static Object getEmpIdSession(HttpSession session) {
        //登录成功拿到employee_id
        Integer employee_id = (Integer) session.getAttribute("employee_id");
        return employee_id;
    }

    /**
     * A-5  库管查看 -保洁端——列表
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */

    @GetMapping(value = "/wx/console/api/getEmpCloth")
    public Object getEmpCloth(HttpSession session) {
        //1.获取hotel——id
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelIdByEmpId((Integer) getEmpIdSession(session));
        int hotel_id = hotelEmployee.getHotel_id();
        //2.在clothlog去查询此酒店所有保洁员最新的布草列表
        List<ClothLog> clothLogList = clothLogService.getLogListByEmpId(hotel_id, null, 1,null);

        Map resultMap = new HashMap();  //存放结果
        Map numMap = new HashMap(); //计数
        Map descMap = new HashMap();//描述

        for (ClothLog clothLog : clothLogList) {
            //(possessor_id,num)
            Integer possessor_id = clothLog.getPossessor_id();
            numMap.put(possessor_id, numMap.get(possessor_id) == null ? 1 : (Integer) numMap.get(possessor_id) + 1);
            //获取描述
            Integer possessor_id_int = null;
            if (possessor_id != null) {
                possessor_id_int = Integer.valueOf(possessor_id);
            }
            Employee employee = employeeService.getEmployee(possessor_id_int);
            String name = employee.getName();
            descMap.put(possessor_id, name);
            resultMap.put("num", numMap);
            resultMap.put("desc", descMap);
        }
        return new RestError(resultMap);
    }


    /**
     * A-5 库管查看 -洗衣厂端-列表
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */

    @GetMapping(value = "/wx/console/api/getClothFactory")
    public Object getClothFactory(HttpSession session) {
        //1.获取hotel——id
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId((Integer) getEmpIdSession(session)).getHotel_id();
        //2.在clothlog去查询此洗衣厂最新的布草列表
        List<ClothLog> clothLogList = clothLogService.getLogListByEmpId(hotel_id, null, 3,null);

        Map<String, Object> resultMap = new HashMap<>(); //存放返回结果
        Map<Object, Integer> numMap = new HashMap<>(); //存放数量
        Map<Object, Object> descMap = new HashMap<>(); //desc map

        for (ClothLog clothLog : clothLogList) {
            //根据cloth_id从Redis中获取ids
            Integer cloth_id = clothLog.getCloth_id();
            String ids = clothLogService.getClothIdsByRedis(cloth_id);
            //根据ids查询desc
            List<ClothType> clothTypeList = clothTypeService.getClothTypeDescByIds(ids.split(","));
            descMap.put(ids, clothTypeList);
            //求和
            numMap.put(ids, numMap.get(ids) == null ? 1 : numMap.get(ids) + 1);
            //封装数据
            resultMap.put("desc", descMap);
            resultMap.put("num", numMap);
        }
        return resultMap;
    }

    /**
     * A-5 库管查看 -洗衣厂端-详情列表
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */

    @GetMapping(value = "/wx/console/api/getClothFactoryDetail")
    public Object getClothFactoryDetail(HttpSession session,
                                        @RequestParam(value = "cloth_type_list", required = false) String cloth_type_list) {
        //1.获取hotel——id
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId((Integer) getEmpIdSession(session)).getHotel_id();
        //2.在clothlog去查询此洗衣厂最新的布草列表
        List<ClothLog> clothLogList = clothLogService.getLogListByEmpId(hotel_id, null, 3,null);
        //
        ArrayList<ClothLog> new_clothLogList = new ArrayList();
        List<Integer> clothTypeList = new ArrayList<>();
        for (ClothLog clothLog : clothLogList) {
            Integer cloth_id = clothLog.getCloth_id();
            List<Integer> idsByClothId = relClothTypeService.getIdsByClothId(cloth_id, 1);

            //"1,2,3"->List<Integer>
            String[] split = cloth_type_list.split(",");
            for (String s : split) {
                if (s != null) {
                    clothTypeList.add(Integer.valueOf(s));
                }
            }

            //所有记录中的类型与前台传入的类型相同，则记录此cloth_log
            if (clothTypeList.containsAll(idsByClothId)) {
                new_clothLogList.add(clothLog);
            }
        }
        //从cloth_log中获取了符合前端传入的布草类型的日志
        Map<Integer, Object> resultMap = new HashMap<>(); //存放返回结果

        for (ClothLog clothLog : new_clothLogList) {
            //获取布草id
            Integer cloth_id_new = clothLog.getCloth_id();
            //获取布草送洗时间
            String format_time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(clothLog.getUpdate_time());
            //封装数据
            resultMap.put(cloth_id_new, format_time);
        }
        return resultMap;
    }

    //阿姨归库--确认归库 增加双事务日志
    @RequestMapping(value = "/wx/console/api/confirminto", method = RequestMethod.POST)
    public Object getclothmessage(HttpServletRequest request, @RequestParam(value = "cloth_ids") List<Integer> cloth_ids) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
        System.out.println(hotel_id);
        for (int i = 0; i < cloth_ids.size(); i++) {
            //调事务service层开启事务同时保洁员增加log和酒店（仓库）减少log 都带酒店id
            clothLogService.saveOutDoubleLog(cloth_ids.get(i), employee_id.toString(), hotel_id);
        }
        return RestError.E_OK;
    }

    @PostMapping("/wx/console/api/initHotelCloth")
    public RestError initHotelCloth(HttpServletRequest request,
                                    Integer[] cloth_ids) {
        Integer employee_id = (Integer) getEmpIdSession(request.getSession());
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeId(employee_id, null);
        clothLogService.initHotelCloth(cloth_ids, hotelEmployee.getHotel_id());
        return RestError.E_OK;
    }

    @PostMapping("/wx/console/api/sendToLaundry")
    public RestError sendToLaundry(HttpServletRequest request,
                                   Integer[] cloth_ids) {
        Integer employee_id = (Integer) getEmpIdSession(request.getSession());
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeId(employee_id, null);
        clothLogService.sendToLaundry(cloth_ids, hotelEmployee.getHotel_id());
        return RestError.E_OK;
    }


    @PostMapping("/wx/console/api/receiveFromLaundry")
    public RestError receiveFromLaundry(HttpServletRequest request,
                                        Integer[] cloth_ids) {
        Integer employee_id = (Integer) getEmpIdSession(request.getSession());
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeId(employee_id, null);
        clothLogService.receiveFromLaundry(cloth_ids, hotelEmployee.getHotel_id());
        return RestError.E_OK;
    }

    //得到制定房间内当前状态的布草的详细信息
    @RequestMapping(value = "/wx/console/api/getRoomClothDetail", method = RequestMethod.GET)
    public Object getAllStoreCloth(HttpServletRequest request,
                                   @RequestParam(value = "room_name") String room_name,
                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
        System.out.println(hotel_id);
        //【**分页用 暂不启用】第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
//        if (pageNo< 1) {
//            pageNo = 1;
//        }
        //【**分页用 暂不启用】每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        //Integer index = (pageNo - 1) * pageSize;

        //根据room_name得到room_id
        Room roomByname = roomService.getRoomByname(room_name, hotel_id);
        Integer room_id = roomByname.getId();

        //根据hotel_id得到对应酒店下的所有log对象
        List<ClothLog> clothLogList = clothLogService.getClothLogListByHotelidRoomid(hotel_id, 0, room_id);
        System.out.println("+++++++++++++++++++++++++++一共有" + clothLogList.size());
        for (ClothLog clothLog : clothLogList) {
            System.out.println("这是单个log" + clothLog);
        }

        List<ClothViewWeb> clothViewWebList = new ArrayList<>();
        //得到cloth_id
        for (ClothLog clothLog :
                clothLogList) {
            Integer cloth_id = clothLog.getCloth_id();
            //根据cloth_id查到所对应的ClothTypeids
            List<Integer> idsByClothId = relClothTypeService.getIdsByClothId(cloth_id, 1);

            //新建view对象存进去
            ClothViewWeb clothViewWeb = new ClothViewWeb();
            //根据ids得到对应描述
            List<ClothType> clothTypedesc = clothTypeService.getClothTypedescByIds(idsByClothId);
            clothViewWeb.setCloth_type_brand_size_material(clothTypedesc);

            //根据ids入参 查到对应描述clothtype对象
//            List<ClothType> clothTypeList = clothTypeService.getClothTypeByListId(idsByClothId);
//            for (ClothType clothType:
//            clothTypeList) {
//                if(clothType.getCloth_kind().equals("Type")){
//                    clothViewWeb.setCloth_type(clothType.getDesc());
//                }else if(clothType.getCloth_kind().equals("Material")){
//                    clothViewWeb.setCloth_material(clothType.getDesc());
//                }else if(clothType.getCloth_kind().equals("Size")){
//                    clothViewWeb.setCloth_size(clothType.getDesc());
//                }
//            }
            //根据cloth_id查到对应的流转次数
            Cloth clothByid = clothService.getClothByid(cloth_id);
            Integer recycle_num = clothByid.getRecycle_num();

            clothViewWeb.setCloth_id(cloth_id);
            clothViewWeb.setRecycle_num(recycle_num);
            //存入集合
            clothViewWebList.add(clothViewWeb);
        }

        //判断返回集合
        if (clothViewWebList.size() > 0) {
            return clothViewWebList;
        } else {
            return RestError.E_CLOTHPOSSESSORTYPE_FAIL;
        }
    }

    //得到房间内瞬时状态的布草的对象集合
    @RequestMapping(value = "/wx/console/api/getRoomClothStatusAndNum", method = RequestMethod.GET)
    public Object getAllStoreCloth(HttpServletRequest request,
                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
        System.out.println(hotel_id);
        //【**分页用 暂不启用】第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
//        if (pageNo< 1) {
//            pageNo = 1;
//        }
        //【**分页用 暂不启用】每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        //Integer index = (pageNo - 1) * pageSize;

        //根据hotel_id得到对应酒店下的所有log对象
        List<ClothLog> clothLogList = clothLogService.getClothLogListBypossessor_type_comment(hotel_id, 0, null, null, null);
        System.out.println("+++++++++++++++++++++++++++一共有" + clothLogList.size());
        for (ClothLog clothLog : clothLogList) {
            System.out.println("这是单个log" + clothLog);
        }
        //【**分页用 暂不启用】一共有多少条 公共变量
        //Integer count = clothLogService.downgetClothLogListcount(hotel_id, possessor_type,comment);
        Map<String, Map> ret = new HashMap<>();
        for (ClothLog clothLog : clothLogList) {
            Integer possessor_id = clothLog.getPossessor_id();
            if (ret.keySet().contains(possessor_id)) {
                Map<String, Object> data = ret.get(possessor_id);
                data.put("num", (Integer) data.get("num") + 1);
                ret.put(possessor_id.toString(), data);
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("num", 1);
                data.put("room_name", roomService.getRoomById(possessor_id).getRoom_name());
                Integer[] status = new Integer[]{0,1};
                List<Mission> missions = missionService.getMissionsByRoomId(possessor_id, status,1);
                if (missions.size() > 0){
                    data.put("status","脏");
                }else{
                    data.put("status","干净");
                }
                ret.put(possessor_id.toString(), data);
            }
        }
        if (ret.size() > 0) {
            return new RestError(ret);
        } else {
            return RestError.E_CLOTHPOSSESSORTYPE_FAIL;
        }
    }

    @GetMapping("/wx/console/api/getStorageCloths")
    public Object getStorageCloths(HttpServletRequest request) {
        Integer employee_id = (Integer) getEmpIdSession(request.getSession());
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeId(employee_id, null);
        List<ClothLog> clothLogs = clothLogService.getLogListByEmpId(hotelEmployee.getHotel_id(), null, 2,null);
        Map ret = clothLogService.getClothViewMapDetail(clothLogs);
        return new RestError(ret);
    }


    @GetMapping("/wx/console/api/getCleanerCloths")
    public Object getCleanerCloths(HttpServletRequest request, Integer cleaner_id) {
        Integer employee_id = (Integer) getEmpIdSession(request.getSession());
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeId(employee_id, null);
        List<ClothLog> clothLogs = clothLogService.getLogListByEmpId(hotelEmployee.getHotel_id(), cleaner_id, 1,null);
        Map clothViewMapDetail = clothLogService.getClothViewMapDetail(clothLogs);
        return new RestError(clothViewMapDetail);
    }


    @GetMapping("/wx/console/api/getClothsByClothTypeAndEmp")
    public Object getStorageClothsByTypes(HttpServletRequest request,
                                          String typeIds,
                                          Integer possessor_type,
                                          @RequestParam(value = "possessor_id", required = false) Integer possessor_id) {
        Integer employee_id = (Integer) getEmpIdSession(request.getSession());
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeId(employee_id, null);
        List<ClothLog> clothLogs = new ArrayList<>();
        if (possessor_type == 1) {
            clothLogs = clothLogService.getLogListByEmpId(hotelEmployee.getHotel_id(), possessor_id, possessor_type,null);
        } else if (possessor_type == 2 || possessor_type == 3) {
            clothLogs = clothLogService.getLogListByEmpId(hotelEmployee.getHotel_id(), null, possessor_type,null);
        }
        List<ClothView> clothViews = new ArrayList<>();
        for (ClothLog clothLog : clothLogs) {
            ClothView clothView = clothService.getClothView(clothLog.getCloth_id());
            if (clothView.getClothTypeIds().equals(typeIds)) {
                clothView.setUpdate_time(clothLog.getCreate_time());
                clothViews.add(clothView);
            }
        }
        return new RestError(clothViews);
    }


    @GetMapping(value = "/wx/console/api/getLaundryCloths")
    public Object getLanduaryCloths(HttpServletRequest request) {
        Integer employee_id = (Integer) getEmpIdSession(request.getSession());
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeId(employee_id, null);
        List<ClothLog> clothLogs = clothLogService.getLogListByEmpId(hotelEmployee.getHotel_id(), null, 3,null);
        Map<String, Map<String, Object>> ret = new HashMap<>();
        for (ClothLog clothLog : clothLogs) {
            ClothView clothView = clothService.getClothView(clothLog.getCloth_id());
            Map<String, Object> clothMap;
            if (ret.keySet().contains(clothView.getClothTypeIds())) {
                clothMap = ret.get(clothView.getClothTypeIds());
            } else {
                clothMap = new HashMap<>();
                clothMap.put("clothTypes", clothView.getClothTypes());
            }
            clothMap.put("all", clothMap.keySet().contains("all") ? (Integer) clothMap.get("all") + 1 : 1);
            ret.put(clothView.getClothTypeIds(), clothMap);
        }
        return new RestError(ret);
    }

    @PostMapping("/console/api/suggestClothBooking")
    public RestError suggestClothBooking(HttpServletRequest request){
        Integer employee_id = (Integer)request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(employee_id);
        List<Map<String, Object>> ret = roomService.suggestClothBooking(hotelEmployee.getHotel_id());
        for (Map<String, Object> dataObj: ret) {
            String cloth_type_id = (String)dataObj.get("cloth_type_id");
            dataObj.put("cloth_types",clothTypeService.getClothTypeDescByIds(cloth_type_id.split(",")));
            ClothExchange example = new ClothExchange();
            example.setHotel_id(hotelEmployee.getHotel_id());
            example.setCloth_type_id(cloth_type_id);
            example.setCreate_time(new Date());
            ClothExchange clothExchange =  clothExchangeService.getClothExchangeByExample(example);
            dataObj.put("currentNum",clothExchange.getNum());
        }
        return new RestError(ret);
    }

    @PostMapping("/console/api/postClothBooking")
    public RestError postClothBooking(HttpServletRequest request,
                                      @RequestBody String clothBookingJson){
        Integer employee_id = (Integer)request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(employee_id);
        hotelEmployee.getHotel_id();
        clothExchangeService.insertOrUpdateInBatch(hotelEmployee.getHotel_id(),clothBookingJson);
        return RestError.E_OK;
    }

}