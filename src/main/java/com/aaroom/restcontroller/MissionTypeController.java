package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.service.*;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.Constants;
import com.aaroom.utils.SessionUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.aaroom.utils.Constants.TEMPIDS;

/**
 * Tips:
 * <p>
 * 一、接口设计:
 * -保洁端任务列表，开始任务，配置房间，结束任务，历史详情，任务信息等
 * -V1.5 酒店端和业务端任务检查
 * -V1.5 保洁端任务功能微调
 * -V1.5 Android适配
 * <p>
 * 二、在插入clothLog表时，有两种情况需要关联mission_id
 * 1.房间入干净布草
 * 2.发布任务房间内布草变为脏
 */
@RestController
public class MissionTypeController {

    @Autowired
    private ClothService clothService;

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionTypeService missionTypeService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomClothService roomClothService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MissionQuestionService missionQuestionService;

    @Autowired
    private HotelBaseService hotelBaseService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ClothExchangeService clothExchangeService;

    @Autowired
    private HotelMissiontypePriceService hotelMissiontypePriceService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private AppBannerService appBannerService;

    /**
     * 保洁员任务列表
     * 分为完成与未完成，未完成排序以房间id排序asc，已完成排序按照update_time排序 desc
     */
    @PostMapping(value = "/app/console/api/getMyMission")
    @ResponseBody
    public Object getMyMission(HttpSession session) {
        List<HashMap<String, Object>> missionListOld = missionService.getMissionByRoomIdZjy(SessionUtil.getIdBySession(session));
        int[] ArrOrder = new int[]{80, 60, 1, 0, 2, 30, 90, 40};

        List<HashMap<String, Object>> missionList = new ArrayList<>();
        for (int i = 0; i < ArrOrder.length - 1; i++) {
            for (HashMap<String, Object> mission :
                    missionListOld) {
                if (ArrOrder[i] == (Integer) mission.get("status")) {
                    missionList.add(mission);
                }
            }
        }
        return new RestError(missionList);
    }

    /**
     * 任务匹配的所有布草列表
     * 输出格式为:ids+配置成功的数量/标配数量
     */
    @PostMapping(value = "/app/console/api/getEmpClothNumList")
    @ResponseBody
    public Object getEmpClothNumList(Integer room_type_id,
                                     Integer mission_id,
                                     HttpSession session,
                                     Integer room_id) {
        List<RoomCloth> roomClothList = roomClothService.getRoomClothByHotelIdAndRoomId(room_type_id);  //房型标配
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        List<HashMap> myList = new ArrayList<>();
        for (RoomCloth roomCloth : roomClothList) {
            HashMap myMap = new HashMap();
            //获取desc List
            String cloth_type_id = roomCloth.getCloth_type_id();
            String[] clothTypeIds = cloth_type_id.split(",");
            List<ClothType> clothTypeList = clothTypeService.getClothTypeDescByIds(clothTypeIds);
            myMap.put("cloth_type_ids", cloth_type_id);
            myMap.put("clothTypeList", clothTypeList);
            myMap.put("clearNum", 0);
            myMap.put("num", roomCloth.getNumber());
            //获取当前任务已经布置房间内的布草对应的数量
            List<ClothLog> clothLogList = clothLogService.getClothLogListByClean(hotel_id, mission_id, room_id);
            for (ClothLog clothlog : clothLogList) {
                String ids = clothLogService.getClothIdsByRedis(clothlog.getCloth_id());
                if (ids.equals(cloth_type_id)) {
                    myMap.put("clearNum", (Integer) myMap.get("clearNum") + 1);
                }
            }
            myList.add(myMap);
        }
        return new RestError(myList);
    }

    /**
     * 扫一扫开始任务判断事件
     * 判断当前是否有未执行的任务，
     * 若存在已经接受的任务，则不能接受其他任务
     * 传入roomId，判断当前任务状态
     * 判断任务列表里是否有未执行的任务（进行中的任务，status为1）
     * 判断当前扫码房间号是否为当前任务房间号
     */
    @PostMapping(value = "/wx/console/api/activeMission")
    @ResponseBody
    public Object activeMission(Integer room_id,
                                HttpSession session) {
        Integer employee_id = (Integer) session.getAttribute("employee_id");
        Integer[] status = new Integer[]{0, 1};
        Mission currentMission = null;
        List<Mission> missions = missionService.getMissionsByEmployeeId(employee_id, status);
        for (Mission mission : missions) {
            if (mission.getStatus() == 1) {
                return RestError.E_START_FAIL;
            }
            if (mission.getRoom_id() == room_id) {
                currentMission = mission;
            }
        }
        if (currentMission != null) {
            missionService.updateMissionStatus(1, currentMission.getId());
            //设置保洁任务开始时间
            currentMission.setClean_begin_time(new Date());
            currentMission.setIs_ok(0);
            currentMission.setStatus(1);
            missionService.updateByPrimaryKeySelective(currentMission);

            //同时将房间状态变为清洁中
            Room room = new Room();
            room.setId(room_id);
            room.setStatus(6);
            roomService.update(room);

            return RestError.E_OK;
        } else {
            return RestError.E_START_NOTIN;
        }
    }

    //保洁开始任务判断
    @PostMapping(value = "/app/console/api/checkActiveMission")
    @ResponseBody
    public Object checkActiveMission(@RequestParam(value = "room_id_list", defaultValue = "", required = false) Integer room_id_list,
                                     HttpSession session) {

        Mission currentMission = null;
        List<Mission> missionList = missionService.getMissionsByEmployeeId(SessionUtil.getIdBySession(session), new Integer[]{0, 1});
        for (Mission mission : missionList) {
            if (mission.getStatus() == 1) {
                return RestError.E_START_FAIL;
            }
            if (mission.getRoom_id() == room_id_list) {
                currentMission = mission;
            }
        }
        if (currentMission != null) {
            return RestError.E_OK;
        } else {
            return RestError.E_START_NOTIN;
        }
    }

    /**
     * 扫一扫配置接口：(WeChat版)
     * 一、判断当前布草是否与列表所选的类型一致
     * 不一致返回错误状态吗（前台提示不一致)
     * 一致进行下一步判断
     * 二、判断当前布草的状态
     * 1、脏两种情况
     * 一、脏并且为发布任务后房间脏的。返回状态吗（前台提示是否转换）
     * 二、否则（其他房间脏的）、返回错误状态吗
     * 2、干净则直接事务陪日志
     * 返回成功
     */
    @PostMapping(value = "/wx/console/api/configDepartmentCloth")
    @ResponseBody
    public Object configDepartmentCloth(Integer cloth_id,
                                        Integer room_id,
                                        Integer mission_id,
                                        HttpSession session,
                                        @RequestParam(value = "cloth_type_list", defaultValue = "") List<Integer> cloth_type_list) {

        //获取酒店id
        HotelEmployee hotelIdByEmpId = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session));
        int hotel_id = hotelIdByEmpId.getHotel_id();
        // 1.布草类型不匹配
        List<Integer> idsByClothId = relClothTypeService.getIdsByClothId(cloth_id, 1);

        if (!idsByClothId.containsAll(cloth_type_list)) {
            //当前扫码布草类型不匹配
            return RestError.E_START_UNCloth;
        }

        //2. 如果接下来扫码的布草id与已经配置的布草id相同。则提示异常
        List<ClothLog> clothLogList_clean = clothLogService.getClothLogListByClean(hotel_id, mission_id, room_id);
        //从数据库中获取当前酒店，当前任务，干净的 ，房间入干净的 List<cloth——log>
        for (ClothLog clothLog : clothLogList_clean) {
            Integer cloth_id_clean = clothLog.getCloth_id();
            if (cloth_id_clean.equals(cloth_id)) {
                return RestError.E_START_SAME;
            }
        }
        //3.扫描到脏的
        Integer[] sts = new Integer[]{2};
        Cloth clothDetail = clothService.getClothDetail(cloth_id);
        if (clothDetail.getStatus() == 1) {
            //上次任务的此房间脏布草

            Integer missionIdByHidRidS2 = missionTypeService.getMissionOldZjy(hotel_id, room_id, sts);
            List<Integer> clothIdListOld = clothLogService.getClothIdByClothId(missionIdByHidRidS2);

            //3，遍历旧布草id List写入log表一个clothId生成一条log
            for (Integer clothId : clothIdListOld) {
                if (clothId == cloth_id) {
                    //您当前扫码为本房间脏布草
                    return RestError.E_START_DirCloth;
                } else {
                    //您当前扫码脏布草不属于本房间！
                    return RestError.E_START_WORK;
                }
            }
        }
        //正常插入日志记录插入过程，需要事务
        clothLogService.nomarlInsertLog(mission_id, cloth_id, room_id, hotel_id, SessionUtil.getIdBySession(session));
        return RestError.E_OK;
    }

    /**
     * 安卓端适配,  扫一扫配置接口
     */
    @PostMapping(value = "/app/console/api/configDepartmentClothApp")
    @ResponseBody
    public Object configDepartmentClothApp(@RequestParam(value = "cloth_id") String cloth_idurl,
                                           @RequestParam(value = "room_id") Integer room_id,
                                           @RequestParam(value = "mission_id") Integer mission_id,
                                           HttpSession session) {

        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        String[] split = cloth_idurl.split("/");
        Integer cloth_id = Integer.parseInt(split[split.length - 1]);
        String ids_scan = clothLogService.getClothIdsByRedis(cloth_id);

        //1.扫描的二维码布草是否存在cloth表记录中
        if ("".equals(ids_scan)) {
            return RestError.E_START_UNCloth;
        }


        //3.扫描脏布草判断
        Integer[] sts = new Integer[]{30, 90};
        if (clothService.getClothDetail(cloth_id).getStatus() == 1) {
            Integer missionIdByHidRidS2 = missionTypeService.getMissionOldZjy(hotel_id, room_id, sts);
            List<Integer> clothIdListOld = clothLogService.getClothIdByClothId(missionIdByHidRidS2);
            if (clothIdListOld.contains(cloth_id)) {
                return RestError.E_START_DirCloth;  //您当前扫码为本房间脏布草
            } else {
                return RestError.E_START_WORK;    //您当前扫码脏布草不属于本房间！
            }
        }
        //2.判断当前布草是否属于自己
        Integer flag = 0;
        List<ClothLog> logListByEmpId = clothLogService.getLogListByEmpId(hotel_id, SessionUtil.getIdBySession(session), 1, null);
        for (ClothLog clothLog : logListByEmpId) {
            Integer cloth_id1 = clothLog.getCloth_id();
            if (cloth_id1.equals(cloth_id)) {
                flag = 1;
            }
        }
        if (flag == 0) {
            return RestError.E_CLOTHS_OTHERS;
        }


        //4.类型不匹配判断，重复判断，溢出判断
        RestError restError = (RestError) missionTypeService.isActiveConfigCloth(cloth_id, hotel_id, mission_id, room_id);
        if (restError.getErrorCode() != 0) {
            return restError;
        }

        //正常插入日志记录插入过程，配置事务
        clothLogService.nomarlInsertLog(mission_id, cloth_id, room_id, hotel_id, SessionUtil.getIdBySession(session));

        return new RestError(clothService.getClothView(cloth_id));
    }

    /**
     * 重置扫一扫配置接口 【张赢】 20190124 1.5测试时增加接口 2019年1月24日 17:46:25
     */
    @PostMapping("/app/console/api/resetting")
    public Object resetting(@RequestParam(value = "room_type_id", required = false) Integer room_type_id,
                            @RequestParam(value = "mission_id", required = false) Integer mission_id,
                            @RequestParam(value = "room_id", required = false) Integer room_id,
                            HttpSession session) {
        //获取酒店id
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();

        //获取登录人的id
        Integer possessor_id = (Integer) session.getAttribute("employee_id");

        //根据唯一mission_id查到对应loglist对象集合
        List<ClothLog> logListByMissionId = clothLogService.getLogListByMissionId(mission_id);
        //分两部分删除 第一：带missionid的房间入的log   //第二：groupbyclothid maxlogid 保洁员出的
        int num = 0;
        for (ClothLog clothLog :
                logListByMissionId) {
            //第一：带missionid的房间入的log
            Integer DeleteInNum = clothLogService.deleteLogById(clothLog.getId());
            //第二：groupbyclothid maxlogid 保洁员出的
            ClothLog clothLognew = new ClothLog();
            clothLognew.setHotel_id(hotel_id);
            clothLognew.setCloth_id(clothLog.getCloth_id());
            clothLognew.setDirection(0);
            clothLognew.setPossessor_id(possessor_id);
            clothLognew.setPossessor_type(1);
            clothLognew.setStatus(0);
            Integer DeleteOutNum = clothLogService.deleteLogByClothlog(clothLognew);
            num = DeleteInNum + DeleteOutNum;
        }
        return new RestError("已经重置了" + num + "个布草扫描信息");
    }

    /**
     * 将脏布草转化为干净布草  暂时不用
     */
    @PostMapping("/wx/console/api/conventClothStatus")
    public Object conventClothStatus(Integer mission_id,
                                     Integer cloth_id,
                                     Integer room_id,
                                     HttpSession session) {
        //获取酒店id
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        //写入事务
        clothLogService.conventClothStatus(mission_id, cloth_id, room_id, hotel_id, SessionUtil.getIdBySession(session));
        return RestError.E_OK;
    }

    /**
     * 安卓端适配，将脏布草转化为干净布草  （退房 大清 抹尘用）
     */
    @PostMapping("/app/console/api/conventClothStatusApp")
    public Object conventClothStatusApp(Integer mission_id,
                                        Integer cloth_id,
                                        Integer room_id,
                                        HttpSession session) {

        //获取酒店id
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();

        //判断当前脏转新布草类型是否超出标配数量，超出则转换失败
        RestError restError = (RestError) missionTypeService.isActiveConfigCloth(cloth_id, hotel_id, mission_id, room_id);
        if (restError.getErrorCode() != 0) {
            return restError;
        }

        clothLogService.conventClothStatus(mission_id, cloth_id, room_id, hotel_id, SessionUtil.getIdBySession(session));
        ClothView clothView = clothService.getClothView(cloth_id);
        return new RestError(clothView);
    }

    /**
     * 完成任务接口：
     * 事务配置：1，批量插入消耗品
     * 2.将房间名下布草转移至保洁名下即可
     * 3.将此次任务的状态从进行中改为完成
     */
    @PostMapping("/app/console/api/saveConsumptionByMissionId")
    public Object saveConsumptionByMissionId(Integer room_type_id,
                                             Integer mission_id,
                                             Integer room_id,
                                             String conAndSumMap,
                                             Integer mission_type,
                                             HttpSession session) {
        //获取酒店id
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        ArrayList<Map<String, Object>> conAndSumList = JSON.parseObject(conAndSumMap, ArrayList.class);

        if (mission_type != 2) { //客房更新不退房，直接插入消耗品即可,这里暂不进行房间配置检查

            //判断房间配置情况 (当前酒店，当前房间，当前任务 ，房间入干净)
            List<ClothLog> clothLogList = clothLogService.getClothLogListByClean(hotel_id, mission_id, room_id);

            //1、获取扫码配置数量 (ids，num)
            HashMap<String, Integer> currentMap = new HashMap();
            for (ClothLog clothlog : clothLogList) {
                String ids = clothLogService.getClothIdsByRedis(clothlog.getCloth_id());   //1,2,11,15
                currentMap.put(ids, currentMap.get(ids) == null ? 1 : currentMap.get(ids) + 1);
            }

            //2、获取标配数量    (ids，num)
            List<RoomCloth> roomClothList = roomClothService.getRoomClothByHotelIdAndRoomId(room_type_id);

            HashMap<String, Integer> standardMap = new HashMap<>();
            for (RoomCloth roomCloth : roomClothList) {
                standardMap.put(roomCloth.getCloth_type_id(), roomCloth.getNumber());
            }
            //3 比较
            for (String currentMapKey : currentMap.keySet()) {
                Integer currentMapValue = currentMap.get(currentMapKey);
                Integer standardMapValue = standardMap.get(currentMapKey);
                if (!currentMapValue.equals(standardMapValue)) {
                    return RestError.E_START_FINISH;
                }
            }
        }
        //4 写入事务
        missionTypeService.saveConsumptionByMissionId(mission_id, hotel_id, room_id, SessionUtil.getIdBySession(session), conAndSumList, mission_type);
        return RestError.E_OK;
    }

    /**
     * 保洁员当前所拥有的布草
     */
    @PostMapping(value = "/wx/console/api/getClothByEmpType")
    @ResponseBody
    public Object getClothByEmpType(HttpSession session) {
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        List<ClothLog> clothLogList = clothLogService.getLogListByEmpId(hotel_id,
                SessionUtil.getIdBySession(session), 1, null);
        Map<String, Object> finalMap = new HashMap<>();//用来返回结果
        Map<String, Object> descMap = new HashMap();  //描述文字
        Map<String, Integer> numMap = new HashMap<>();//存储当前数量
        Map<String, Integer> statusMap = new HashMap(); //洁净状态
        if (!clothLogList.isEmpty()) {
            List clothIdList = new ArrayList();
            for (ClothLog clothLog : clothLogList) {  //clothLog中遍历此保洁员最新的布草
                //1.获取每一个最新布草的id
                Integer cloth_id = clothLog.getCloth_id();
                clothIdList.add(cloth_id);
                String ids = clothLogService.getClothIdsByRedis(cloth_id);
                String[] clothTypeIds = ids.split(",");
                List<ClothType> clothTypeList = clothTypeService.getClothTypeDescByIds(clothTypeIds);
                Integer status = clothLog.getStatus();
                //2.获取所有布草的desc
                descMap.put(ids, clothTypeList);
                //3.计算每种布草的数量
                numMap.put(ids, numMap.get(ids) == null ? 1 : (numMap.get(ids)) + 1);
                //4.洁净状态
                statusMap.put(ids, status);
            }
            finalMap.put("num", numMap);
            finalMap.put("name", descMap);
            finalMap.put("status", statusMap);
            finalMap.put("clothIdList", clothIdList);
            //5.返回数据
            return finalMap;
        }
        return null;
    }

    /**
     * 历史任务
     * 当前保洁员所有的已经完成的任务(分页)
     */
    @PostMapping(value = "/wx/console/api/showHistoryMissionByEmp")
    @ResponseBody
    public Object showHistoryMissionByEmp(HttpSession session,
                                          @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                          @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int index = (pageNo - 1) * pageSize;
        //酒店id
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session));
        int hotel_id = hotelEmployee.getHotel_id();
        //返回
        List<HashMap<String, Object>> hashMapsList = missionTypeService.showHistoryMissionByEmp(SessionUtil.getIdBySession(session), hotel_id, index, pageSize);
        //获取总数量
        Integer numList = missionTypeService.showHistoryMissionByEmpNum(SessionUtil.getIdBySession(session), hotel_id);
        //修改此map的时间戳
        for (HashMap<String, Object> maps : hashMapsList) {
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(maps.get("update_time"));
            maps.put("room_name", roomService.getRoomById((Integer) maps.get("room_id")));
            maps.put("update_time", format);
        }
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("count", numList);
        myMap.put("data", hashMapsList);
        return myMap;
    }

    /**
     * 本月已做任务-V1.5
     */
    @GetMapping(value = "/app/console/api/showMissionByEmpMonth")
    @ResponseBody
    public Object showMissionByEmpMonth(HttpSession session) {
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        String accept_employee_id = SessionUtil.getIdBySession(session).toString();
        List<Mission> missionList = missionService.getMissionByMonthEmp(hotel_id, accept_employee_id);


        Double all_price = 0.00;  //用来返回本月工资
        Map<Integer, Map<String, Object>> ret = new HashMap<>();  //存放结果
        Map<String, Object> resultMap = new HashMap<>();

        for (Mission mission : missionList) {
            Map<String, Object> tempMap;
            String desc = missionTypeService.getdescbyid(mission.getMission_type());  //任务类型
            if (ret.keySet().contains(mission.getMission_type())) {
                tempMap = ret.get(mission.getMission_type());
            } else {
                tempMap = new HashMap<>();
                tempMap.put("desc", desc);
            }
            if (mission.getIs_ok() == 0 || mission.getIs_ok() == 1) {
                tempMap.put("OK", tempMap.get("OK") == null ? 1 : (Integer) tempMap.get("OK") + 1);
            } else {
                tempMap.put("UnOK", tempMap.get("UnOK") == null ? 1 : (Integer) tempMap.get("UnOK") + 1);
            }
            if (tempMap.get("OK") == null) {
                tempMap.put("OK", 0);
            } else if (tempMap.get("UnOK") == null) {
                tempMap.put("UnOK", 0);
            }
            ret.put(mission.getMission_type(), tempMap);
        }
        //计算本月收入
        for (Map.Entry<Integer, Map<String, Object>> entry : ret.entrySet()) {
            Integer mission_type = entry.getKey();
            Integer ok_num = (Integer) entry.getValue().get("OK");
            Integer unOk_num = (Integer) entry.getValue().get("UnOK");
            all_price += hotelMissiontypePriceService.getPriceDetail(hotel_id, mission_type, ok_num, unOk_num);
        }
        resultMap.put("count", all_price);
        resultMap.put("mission_detail", ret);
        //返回结果
        return new RestError(resultMap);
    }

    /**
     * 布草间的布草(保洁员当前所拥有的布草)-V1.5
     */
    @GetMapping(value = "/app/console/api/getClothByEmpFloor")
    @ResponseBody
    public Object getClothByEmpFloor(HttpSession session) {
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        List<ClothLog> logList = clothLogService.getLogListByEmpId(hotel_id, SessionUtil.getIdBySession(session), 1, null);
        return new RestError(clothLogService.getClothViewMapDetail(logList));
    }

    /**
     * 保洁员当月完成任务的列表-V1.5
     * 三种状态：
     * 1.正常完成：领班检查合格，抽查合格
     * 2.返工完成：领班不合格，抽查合格
     * 3.抽查完成：领班合格，抽查不合格
     */
    @GetMapping(value = "/app/console/api/getMissionDetailByMonth")
    @ResponseBody
    public Object getMissionDetailByRole(HttpSession session,
                                         @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                         @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        Map ret = new HashMap();
        int index = (pageNo - 1) * pageSize;
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        Integer accept_employee_id = SessionUtil.getIdBySession(session);
        Integer[] status_AA = new Integer[]{30, 90};  //领班合格 30;  业务合格 90;
        List<Mission> missionDetailByMonth = missionService.getMissionDetailByMonth(accept_employee_id, hotel_id, status_AA, index, pageSize);
        List<Mission> missionList = missionService.getMissionDetailByMonth(accept_employee_id, hotel_id, status_AA, null, null);
        Double price = 0.00;
        for (Mission mission : missionDetailByMonth) {
            Integer is_ok = mission.getIs_ok();
            //处理任务实际获取价格
            if (is_ok == 0 || is_ok == 1) {
                HotelMissiontypePrice hmtp = hotelMissiontypePriceService.getHMPByHotelAndMissionType(hotel_id, mission.getMission_type());
                price = hmtp.getClean_price();
            } else if (is_ok == 2) {
                price = hotelMissiontypePriceService.getPriceDetail(hotel_id, mission.getMission_type(), 0, 1);
            }
            mission.setPrice(price);
            //处理房间名称
            Room roomById = roomService.getRoomById(mission.getRoom_id());
            mission.setRoom_name(roomById.getRoom_name());
        }
        ret.put("pageCount", missionList.size());
        ret.put("data", missionDetailByMonth);
        return new RestError(ret);
    }

    /**
     * V1.5-领班端 -待检查列表
     */
    @GetMapping(value = "/app/console/api/getMissionDetailByHotel")
    @ResponseBody
    public Object getMissionDetailByRole(HttpSession session) {
        //酒店id
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(SessionUtil.getIdBySession(session)).getHotel_id();
        Integer[] status_Hotel = new Integer[]{2}; //2 代表首次待检查，60代表返工待检查
        //获取当前酒店下，今天所有的任务列表
        List missionListByStatus = missionTypeService.getMissionListByStatus(SessionUtil.getIdBySession(session), hotel_id, status_Hotel);
        return new RestError(missionListByStatus);
    }

    /**
     * V1.5-业务端 -待检查列表
     */
    @PostMapping(value = "/app/console/api/getMissionDetailByAA")
    @ResponseBody
    public Object getMissionDetailByAA(HttpSession session,
                                       @RequestParam(value = "hotel_id", defaultValue = "") Integer hotel_id) {
        /**
         *暂时获取所有酒店，业务扩展可能AA工作人员只负责地区所有酒店，而不是全部酒店
         */
        List<HotelBase> allHotelBase = hotelBaseService.getAllHotelBase();

        Integer hotel_id_ = null;
        HotelBase hotelBase_ = null;
        for (HotelBase hotelBase : allHotelBase) {
            if (hotelBase.getId().equals(hotel_id)) {
                hotel_id_ = hotelBase.getId();
                hotelBase_ = hotelBase;
            }
        }
        if (hotel_id_ == null) {
            return RestError.E_START_HOTELWRONG;
        }

        Map<String, Object> ret = new HashMap<>();
        Integer[] status_AA = new Integer[]{30};  //领班认为合格的任务
        List missionListByStatus = missionTypeService.getMissionListByStatus(SessionUtil.getIdBySession(session), hotelBase_.getId(), status_AA);
        ret.put("hotel_id", hotelBase_.getId());
        ret.put("hotel_name", hotelBase_.getHotel_name());
        ret.put("data", missionListByStatus);
        return new RestError(ret);
    }

    /**
     * V1.5-领班端/业务抽查端 -扫码进入任务列表房间-合格
     */
    @PostMapping(value = "/app/console/api/startMissionByRole")
    @ResponseBody
    public Object startMissionByRole(HttpSession session,
                                     @RequestParam(value = "mission_id") Integer id) {
        //判断角色
        Employee employee = employeeService.getEmployee(SessionUtil.getIdBySession(session));
        //AA工作人员审批合格
        Mission mission = missionService.getMissionById(id);
        if (employee.getRole_id() == 14) {
            mission.setStatus(90);
            mission.setBusiness_end_time(new Date());//业务任务结束时间
        } else if (employee.getRole_id() == 11) {
            mission.setStatus(30);
            mission.setEnd_time(new Date());//领班任务结束时间

            Room room = roomService.getRoomById(mission.getRoom_id());
            room.setStatus(0);
            roomService.update(room);
        }
        missionService.updateByPrimaryKeySelective(mission);
        return RestError.E_OK;
    }

    /**
     * V1.5-领班端/业务抽查端 -问题反馈-上传图片
     */
    @PostMapping(value = "/app/console/api/insertMissionQuestionByRole")
    @ResponseBody
    public Object insertMissionQuestionByRole(Integer id,
                                              HttpSession session,
                                              @RequestParam(value = "files", required = false) MultipartFile[] files,
                                              @RequestParam(value = "comment", required = false) String comment) throws IOException {
        //判断角色
        Employee employee = employeeService.getEmployee(SessionUtil.getIdBySession(session));

        if (files != null && files.length > 0) {
            List<String> fileNames = new ArrayList<>();
            for (MultipartFile multipartFile : files) {
                String[] fileName = multipartFile.getOriginalFilename().split("\\.");
                File tempFile = File.createTempFile(fileName[0], fileName[1]);
                multipartFile.transferTo(tempFile);
                String destFileName = UUID.randomUUID().toString() + "." + fileName[1];
                storageService.uploadFile(tempFile, destFileName);
                fileNames.add(destFileName);
            }
            missionQuestionService.insertOrUpdateMission(id, comment, fileNames);  //插入图片和描述信息
        }
        Mission missionById = missionService.getMissionById(id);
        if (employee.getRole_id() == 11) {            //领班问题反馈待检查
            missionById = missionService.getMissionById(id);
            missionById.setStatus(60); //返工待检查
            if (missionById.getIs_ok() != 2) {
                missionById.setIs_ok(1);
            }
            missionService.updateByPrimaryKeySelective(missionById);
        } else if (employee.getRole_id() == 14) {          //业务问题反馈待检查
            missionById.setIs_ok(2);
            missionById.setStatus(80);
            missionById.setBusiness_end_time(new Date());  //业务不合格，视为任务结束
            missionService.updateByPrimaryKeySelective(missionById);
        }
        return RestError.E_OK;
    }

    /**
     * V1.5-保洁端-查看返工原因-图片
     */
    @PostMapping(value = "/app/console/api/getMissionQuestionByEmp")
    @ResponseBody
    public Object getMissionQuestionByEmp(Integer mission_id) {
        List<MissionQuestion> missionQuestionByEmp = missionQuestionService.getMissionQuestionByEmp(mission_id);
        Mission mission = missionService.getMissionById(mission_id);
        List picList = new ArrayList();
        for (MissionQuestion missionQuestion : missionQuestionByEmp) {
            picList.add(storageService.generatePresignUrl(missionQuestion.getPic()));
        }
        String comment = mission.getComment();
        if (comment == null) {
            mission.setComment("暂无描述");
        }
        Map<String, Object> ret = new HashMap();
        ret.put("comment", comment);
        ret.put("url", picList);
        return new RestError(ret);
    }

    /**
     * 更新任务状态
     */
    @PostMapping(value = "/app/console/api/updateMissionStatusByEmp")
    @ResponseBody
    public Object updateMissionStatusByEmp(Integer mission_id,
                                           Integer status) {
        missionService.updateMissionStatus(status, mission_id);
        return RestError.E_OK;
    }

    /**
     * 终结任务
     */
    @PostMapping(value = "/app/console/api/endMissionStatusByEmp")
    @ResponseBody
    public Object endMissionStatusByEmp(Integer mission_id) {
        missionService.updateMissionStatus(40, mission_id);
        return RestError.E_OK;
    }

    /**
     * V1.5 保洁端 -客房更新-（不退房）
     */
    @PostMapping(value = "/app/console/api/getClothsListByDIY")
    @ResponseBody
    public Object getClothsListByDIY(@RequestParam(value = "mission_id", required = false) Integer mission_id) {
        Map<String, String> allRet = redisCacheService.hgetall(Constants.MISSION + mission_id);

        List<Map<String, Object>> retList = new ArrayList<>();
        Map<String, Object> tempMap;
        if (!allRet.isEmpty()) {
            for (Map.Entry<String, String> entry : allRet.entrySet()) {
                tempMap = new HashMap();
                ClothView clothView = clothService.getClothView(Integer.parseInt(entry.getValue()));
                String clothTypeIds = clothView.getClothTypeIds();  //1,2,3
                List clothTypes = clothView.getClothTypes();  //desc
                tempMap.put("clothTypes", clothTypes);
                tempMap.put("clothTypeIds", clothTypeIds);
                tempMap.put("num", 1);
                retList.add(tempMap);
            }
        }

        List<Map<String, Object>> countList = new ArrayList<>();// 用于存放最后的结果
        for (int i = 0; i < retList.size(); i++) {
            String clothTypes = (String) retList.get(i).get("clothTypeIds");
            Integer num = Integer.parseInt(String.valueOf(retList.get(i).get("num")));
            int flag = 0; // 0为新增数据，1为增加count
            for (int j = 0; j < countList.size(); j++) {
                String clothTypes_ = (String) countList.get(j).get("clothTypeIds");
                Integer num_ = Integer.parseInt(String.valueOf(countList.get(j).get("num")));
                if (clothTypes.equals(clothTypes_)) {
                    int sum = num + num_;
                    countList.get(j).put("num", sum + "");
                    flag = 1;
                    continue;
                }
            }
            if (flag == 0) {
                countList.add(retList.get(i));
            }
        }

        return new RestError(countList);
    }

    /**
     * V1.5 保洁端 -客房更新-（不退房）-扫一扫房间内脏布草
     * 发布任务后房间内的布草都为脏
     */
    @PostMapping(value = "/app/console/api/updateRoomByEmpDIY")
    @ResponseBody
    public Object updateRoomByEmpDIY(@RequestParam(value = "cloth_id", required = false) Integer cloth_id,
                                     HttpSession session,
                                     Integer room_id,
                                     Integer mission_id) {

        Integer empId = SessionUtil.getIdBySession(session);
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(empId).getHotel_id();

        //如果是干净的，失败
        ClothView clothView = clothService.getClothView(cloth_id);
        if (clothView.getStatus() != null) {
            Integer status = clothView.getStatus();
            if (status == 0) {
                return RestError.E_DATA_INVALID;  //无效的数据
            }
            //如果是脏的，并且不是这个房间内的布草
            List<ClothLog> logListByEmpId = clothLogService.getLogListByEmpId(hotel_id, room_id, 0, 1);
            Integer temp_cloth = null;
            for (ClothLog clothLog : logListByEmpId) {
                if (clothLog.getCloth_id().equals(cloth_id)) {
                    temp_cloth = clothLog.getCloth_id();
                }
            }
            if (temp_cloth == null) {
                return RestError.E_START_WORK; //当前扫码脏布草不属于本房间
            }
            Integer num = redisCacheService.hget(Constants.MISSION + mission_id, TEMPIDS + cloth_id, Integer.class);
            if (num == null) {
                redisCacheService.hset(Constants.MISSION + mission_id, TEMPIDS + cloth_id, cloth_id);
            }
            //成功。记录日志
            clothLogService.highInsertLog(cloth_id, room_id, hotel_id, empId);
            return new RestError(clothView);
        } else {
            return RestError.E_CLOTHIDMISS_FAIL;
        }

    }

    /**
     * V1.5 业务端 -布草流转
     */
    @PostMapping(value = "/app/console/api/orderClothListByAA")
    @ResponseBody
    public Object orderClothListByAA() {
        List resultList = new ArrayList();
        Map<String, Object> ret;
        //  暂时以模拟数据  -查询当天的数据
        List<ClothExchange> allClothExchangeByAA = clothExchangeService.getAllClothExchangeByAA();

        for (ClothExchange clothExchange : allClothExchangeByAA) {
            ret = new LinkedHashMap<>();
            String clothTypeId = clothExchange.getCloth_type_id();
            List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothTypeId.split(","));
            ret.put("hotel_id", clothExchange.getHotel_id());
            ret.put("hotel_name", hotelBaseService.getById(clothExchange.getHotel_id()).getHotel_name());
            ret.put("num", clothExchange.getNum());
            ret.put("ClothTypeIdsList", clothTypeDescByIds);
            resultList.add(ret);
        }
        return new RestError(resultList);
    }

    /**
     * V1.5 业务端 -客房更新·不退房
     * 获取自定义布草配置列表
     * 数据格式： ids - 已经的配置的布草数量/redis缓存的自定义布草数量
     */
    @PostMapping(value = "/app/console/api/getClothListByRedisDIY")
    @ResponseBody
    public Object getClothListByRedisDIY(Integer room_id,
                                         Integer mission_id,
                                         HttpSession session) {
        Integer empId = SessionUtil.getIdBySession(session);
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(empId).getHotel_id();
        List<HashMap<String, Object>> resultList = new ArrayList<>(); //result
        HashMap<String, Object> ret;  //temp
        Map<String, Integer> standerMap = new HashMap();
        Map<String, Integer> cleanMap = new HashMap();

        //1.标配已经扫的脏布草    （ids,num)
        Map<String, String> allRet = redisCacheService.hgetall(Constants.MISSION + mission_id);
        for (Map.Entry<String, String> entry : allRet.entrySet()) {
            String clothTypeIds = clothService.getClothView(clothService.getClothView(Integer.parseInt(entry.getValue())).getId()).getClothTypeIds();
            standerMap.put(clothTypeIds, standerMap.get(clothTypeIds) == null ? 1 : standerMap.get(clothTypeIds) + 1);
        }
        //2.已经扫码配置的布草   (ids,num)
        List<ClothLog> clothLogList = clothLogService.getClothLogListByClean(hotel_id, mission_id, room_id);
        for (ClothLog clothLog : clothLogList) {
            String clothTypeIds = clothService.getClothView(clothLog.getCloth_id()).getClothTypeIds();
            cleanMap.put(clothTypeIds, cleanMap.get(clothTypeIds) == null ? 1 : cleanMap.get(clothTypeIds) + 1);
        }

        //封装数据
        for (Map.Entry<String, Integer> entry_standard : standerMap.entrySet()) {  //至少会有一个值
            if (!cleanMap.isEmpty()) {
                if (cleanMap.keySet().contains(entry_standard.getKey())) {
                    ret = new LinkedHashMap<>();
                    ret.put("allNum", standerMap.get(entry_standard.getKey()));
                    ret.put("cleanNum", cleanMap.get(entry_standard.getKey()));
                    ret.put("clothTypeIds", entry_standard.getKey());
                    ret.put("clothTypes", clothTypeService.getClothTypeDescByIds(entry_standard.getKey().split(",")));
                    resultList.add(ret);
                } else {
                    ret = new LinkedHashMap<>();
                    ret.put("allNum", entry_standard.getValue());
                    ret.put("cleanNum", 0);
                    ret.put("clothTypeIds", entry_standard.getKey());
                    ret.put("clothTypes", clothTypeService.getClothTypeDescByIds(entry_standard.getKey().split(",")));
                    resultList.add(ret);
                }
            } else {
                ret = new LinkedHashMap<>();
                List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(entry_standard.getKey().split(","));
                ret.put("clothTypes", clothTypeDescByIds);
                ret.put("clothTypeIds", entry_standard.getKey());
                ret.put("allNum", entry_standard.getValue());
                ret.put("cleanNum", 0);
                resultList.add(ret);
            }
        }
        return new RestError(resultList);
    }

    /**
     * V1.5 保洁端 -客房更新-（不退房）
     * ids--currentNum/Redis标配数量
     * 扫干净的布草
     */
    @PostMapping(value = "/app/console/api/configClothByRedis")
    @ResponseBody
    public Object configClothByRedis(String cloth_id,
                                     Integer room_id,
                                     Integer mission_id,
                                     HttpSession session) {
        Integer cloth_idI;
        //异常处理
        try {
            cloth_idI = Integer.parseInt(cloth_id);
        } catch (Exception e) {
            return RestError.E_DATA_INVALID;  //无效的数据
        }

        Integer empId = SessionUtil.getIdBySession(session);
        int hotel_id = hotelEmployeeService.getHotelIdByEmpId(empId).getHotel_id();

        //从redis中获取所有的用户自定义布草 ids - num
        Map<String, String> allRet = redisCacheService.hgetall(Constants.MISSION + mission_id);
        String ids_scan = clothService.getClothView(cloth_idI).getClothTypeIds();

        //扫描的二维码布草，不在cloth表里，则报异常
        if ("".equals(ids_scan) || ids_scan == null) {
            return RestError.E_START_UNCloth;  //当前扫码布草不存在！
        }
        //如果是脏的，失败
        if (clothService.getClothView(cloth_idI).getStatus() == 1) {
            return RestError.E_DATA_INVALID;  //无效的数据
        }
        //成功配置的布草
        List<ClothLog> clothLogList = clothLogService.getClothLogListByClean(hotel_id, mission_id, room_id);
        Map<String, Integer> numMap = new HashMap<>();
        Map<String, Integer> standerMap = new HashMap<>();
        List<String> tempList = new ArrayList();
        Integer standard_num;
        for (Map.Entry<String, String> entry : allRet.entrySet()) {  //(TEMPIDS:cloth_id,cloth_id)
            String ids = clothLogService.getClothIdsByRedis(Integer.parseInt(entry.getValue()));
            tempList.add(ids);
            standerMap.put(ids, standerMap.get(ids) == null ? 1 : standerMap.get(ids) + 1);//（用户自定义脏布草）标配（ids,num）
        }
        standard_num = standerMap.get(clothService.getClothView(cloth_idI).getClothTypeIds());

        if (!tempList.contains(clothService.getClothView(cloth_idI).getClothTypeIds())) {
            return RestError.E_START_UNCloth;   //当前扫码布草类型不匹配
        }
        if (!clothLogList.isEmpty()) {
            for (ClothLog clothLog : clothLogList) {
                if (clothLog.getCloth_id().equals(cloth_idI)) {
                    return RestError.E_START_SAME;  //您已经配置过此布草了！
                }
                String ids = clothService.getClothView(clothLog.getCloth_id()).getClothTypeIds();
                numMap.put(ids, numMap.get(ids) == null ? 1 : numMap.get(ids) + 1);  //已经配置的干净布草（ids,num）
            }
            Integer userNum = numMap.get(clothService.getClothView(cloth_idI).getClothTypeIds());

            //比较返回错误码
            if (userNum != null && userNum >= standard_num) {
                return RestError.E_START_OVERFLOWING;   //您当前数量超过上限，请检查！
            }
        }
        //成功。记录日志
        clothLogService.nomarlInsertLog(mission_id, cloth_idI, room_id, hotel_id, empId);
        return new RestError(clothService.getClothView(cloth_idI));
    }

    /**
     * V1.5 领班端-业务抽查端 开始任务
     */
    @PostMapping(value = "/app/console/api/isActiveMission")
    @ResponseBody
    public Object isActiveMission(Integer id,
                                  @RequestParam(value = "isUseful") Integer isUseful,
                                  HttpSession session) {
        //判断角色
        Employee employee = employeeService.getEmployee(SessionUtil.getIdBySession(session));
        Mission mission = missionService.selectByPrimaryKey(id);
        if (employee.getRole_id() == 14 && isUseful == 1) {//AA工作人员业务开始时间
            mission.setBusiness_begin_time(new Date());
            missionService.updateByPrimaryKeySelective(mission);
        } else if (employee.getRole_id() == 11 && isUseful == 1) {
            mission.setGaffer_begin_time(new Date());  //领班开始时间
            missionService.updateByPrimaryKeySelective(mission);
        }
        return RestError.E_OK;
    }

    /**
     * 夏宇测试上传图片接口
     */
    @PostMapping(value = "/app/console/api/XiaYuPicTest")
    @ResponseBody
    public Object XiaYuPicTest(HttpServletRequest request, HttpSession session,
                               @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();
        if (files != null && files.length > 0) {
            if (files.length > 0) {
                for (MultipartFile multipartFile : files) {
                    String[] fileName = multipartFile.getOriginalFilename().split("\\.");
                    File temp = File.createTempFile(fileName[0], fileName[1]);
                    multipartFile.transferTo(temp);
                    String destFileName = UUID.randomUUID().toString() + "." + fileName[1];
                    storageService.uploadFile(temp, destFileName);
                    fileNames.add(destFileName);
                }
            }
            missionQuestionService.insertOrUpdateMission(1, "夏宇测试ABC", fileNames);  //插入图片和描述信息
        }

        List<MissionQuestion> missionQuestionByEmp = missionQuestionService.getMissionQuestionByEmp(1);
        Mission mission = missionService.getMissionById(1);
        List picList = new ArrayList();
        for (MissionQuestion missionQuestion : missionQuestionByEmp) {
            String pic = missionQuestion.getPic();
            picList.add(storageService.generatePresignUrl(pic));
        }


        return new RestError(picList);
    }


    /*
    1.6版APP 保洁端  我的账单 本月账单 and 上月账单
     */
    @RequestMapping(value = "/app/console/api/CleanerBill", method = RequestMethod.POST)
    public Object CleanerBill(HttpServletRequest request,
                              @RequestParam(value = "Month", defaultValue = "1") String Month) throws Exception {

        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        if (employee_id != null) {
            //根据employee_id 找到对应的 hotelEmployee关系对象(有两种情况 绑定酒店的是AA员工 没有绑定的为非AA员工)
            HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
            //新建view类对象 用于存放前台展示数据
            CleanerBillMissionsView cleanerBillMissionsView = new CleanerBillMissionsView();
            //判断是本月账单 还是上月账单 默认进来不传就是本月账单
            Date StartTime = null;
            Date EndTime = null;
            if (Month.equals("1")) {
                //获取本月1号0点时间 为本月查询时间点的开始时间
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, 0);
                c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                StartTime = c.getTime();
                //获取当前时间为本月查询时间点的结束时间
                EndTime = new Date();
            } else if (Month.equals("2")) {
                //获取上月1号0点时间问 为上月查询时间点的开始时间
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                StartTime = c.getTime();
                //获取上月1号0点时间问 为上月查询时间点的开始时间
                Calendar c2 = Calendar.getInstance();
                c2.add(Calendar.MONTH, -1);
                c2.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));//上月最后一天
                c2.set(Calendar.HOUR_OF_DAY, 23);
                c2.set(Calendar.MINUTE, 59);
                c2.set(Calendar.SECOND, 59);
                EndTime = c2.getTime();
            }
            if (hotelEmployee != null) {//不为空说明是AA的员工 空就是非AA的保洁员
                Integer[] status = new Integer[]{30, 90};//只查询 领班 抽查业务说合格 的 两种情况下的任务集
                Integer AAFullTimeMission = 0;
                Integer AAPartTimeMission = 0;
                Integer CheckMission = 0;
                Double MissionReward = 0.00;

                List<Mission> missionsByEmployeeId = missionService.getMissionsByEmployeeIdAndTime((Integer) employee_id, status, StartTime, EndTime);
                for (Mission mission :
                        missionsByEmployeeId) {
                    //计算【抽查返工订单】数
                    Integer is_ok = mission.getIs_ok();
                    if (is_ok == null) {
                        is_ok = 0;
                    }
                    if (is_ok == 1 || is_ok == 2) {
                        CheckMission++;
                    }


                    //list里存酒店名字
                    HotelBase HotleBase = hotelBaseService.getById(mission.getHotel_id());
                    mission.setHotel_name(HotleBase.getHotel_name());

                    //list存打扫类型
                    MissionType missionType = missionTypeService.getMissionTypeById(mission.getMission_type());
                    mission.setMission_type_name(missionType.getDesc());

                    //list里 存兼职还是全职    1兼职 2是全职
                    if (mission.getMission_type() < 5) {
                        mission.setMissionWorkType(2);
                    } else if (mission.getMission_type() > 4) {
                        mission.setMissionWorkType(1);
                    }

                    //存本单收入
                    HotelMissiontypePrice HotelAndMissionType = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), mission.getMission_type());
                    if (is_ok != null) {
                        if (is_ok == 2) {
                            mission.setPrice(HotelAndMissionType.getRework_price());
                        } else {
                            mission.setPrice(HotelAndMissionType.getClean_price());
                        }
                    } else {
                        mission.setPrice(HotelAndMissionType.getClean_price());
                    }


                    //计算总收入
                    Integer mission_type = mission.getMission_type();
                    switch (mission_type) {
                        case 1:
                            AAFullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 1);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice.getRework_price();
                            }
                            break;
                        case 2:
                            AAFullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice2 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 2);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice2.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice2.getRework_price();
                            }
                            break;
                        case 3:
                            AAFullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice3 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 3);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice3.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice3.getRework_price();
                            }
                            break;
                        case 4:
                            AAFullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice4 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 4);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice4.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice4.getRework_price();
                            }
                            break;
                        case 5:
                            AAPartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice5 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 5);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice5.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice5.getRework_price();
                            }
                            break;
                        case 6:
                            AAPartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice6 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 6);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice6.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice6.getRework_price();
                            }
                            break;
                        case 7:
                            AAPartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice7 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 7);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice7.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice7.getRework_price();
                            }
                            break;
                        case 8:
                            AAPartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice8 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 8);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice8.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice8.getRework_price();
                            }
                            break;
                    }

                }
                cleanerBillMissionsView.setFullTimeMission(0);
                cleanerBillMissionsView.setPartTimeMission(0);
                cleanerBillMissionsView.setComplaintMission(0);
                cleanerBillMissionsView.setAAFullTimeMission(AAFullTimeMission);
                cleanerBillMissionsView.setAAPartTimeMission(AAPartTimeMission);
                cleanerBillMissionsView.setCheckMission(CheckMission);
                cleanerBillMissionsView.setMissionReward(MissionReward);
                cleanerBillMissionsView.setMissionList(missionsByEmployeeId);

                return new RestError(cleanerBillMissionsView);
            } else { //以下为 计算非AA员工
                Integer[] status = new Integer[]{30, 90};//只查询 领班 抽查业务说合格 的 两种情况下的任务集
                Integer FullTimeMission = 0;
                Integer PartTimeMission = 0;
                Integer CheckMission = 0;
                Double MissionReward = 0.00;

                List<Mission> missionsByEmployeeId = missionService.getMissionsByEmployeeIdAndTime((Integer) employee_id, status, StartTime, EndTime);
                for (Mission mission :
                        missionsByEmployeeId) {
                    //计算【抽查返工订单】数
                    Integer is_ok = mission.getIs_ok();
                    if (is_ok == 1 || is_ok == 2) {
                        CheckMission++;
                    }
                    //list里存任务酒店名字
                    HotelBase HotleBase = hotelBaseService.getById(mission.getHotel_id());
                    mission.setHotel_name(HotleBase.getHotel_name());

                    //list存打扫类型
                    MissionType missionType = missionTypeService.getMissionTypeById(mission.getMission_type());
                    mission.setMission_type_name(missionType.getDesc());

                    //list里 存兼职还是全职    1兼职 2是全职
                    if (mission.getMission_type() < 5) {
                        mission.setMissionWorkType(2);
                    } else if (mission.getMission_type() > 4) {
                        mission.setMissionWorkType(1);
                    }

                    //存本单收入
                    HotelMissiontypePrice HotelAndMissionType = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), mission.getMission_type());
                    if (is_ok == 2) {
                        mission.setPrice(HotelAndMissionType.getRework_price());
                    } else {
                        mission.setPrice(HotelAndMissionType.getClean_price());
                    }
                    //计算总收入
                    Integer mission_type = mission.getMission_type();
                    switch (mission_type) {
                        case 1:
                            FullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 1);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice.getRework_price();
                            }
                            break;
                        case 2:
                            FullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice2 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 2);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice2.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice2.getRework_price();
                            }
                            break;
                        case 3:
                            FullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice3 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 3);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice3.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice3.getRework_price();
                            }
                            break;
                        case 4:
                            FullTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice4 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 4);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice4.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice4.getRework_price();
                            }
                            break;
                        case 5:
                            PartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice5 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 5);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice5.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice5.getRework_price();
                            }
                            break;
                        case 6:
                            PartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice6 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 6);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice6.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice6.getRework_price();
                            }
                            break;
                        case 7:
                            PartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice7 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 7);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice7.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice7.getRework_price();
                            }
                            break;
                        case 8:
                            PartTimeMission++;
                            HotelMissiontypePrice HotelMissiontypePrice8 = hotelMissiontypePriceService.getHMPByHotelAndMissionType(mission.getHotel_id(), 8);
                            if (is_ok == 2) {
                                MissionReward -= HotelMissiontypePrice8.getRework_price();
                            } else {
                                MissionReward += HotelMissiontypePrice8.getRework_price();
                            }
                            break;
                    }

                }
                cleanerBillMissionsView.setAAFullTimeMission(0);
                cleanerBillMissionsView.setAAPartTimeMission(0);
                cleanerBillMissionsView.setComplaintMission(0);
                cleanerBillMissionsView.setFullTimeMission(FullTimeMission);
                cleanerBillMissionsView.setPartTimeMission(PartTimeMission);
                cleanerBillMissionsView.setCheckMission(CheckMission);
                cleanerBillMissionsView.setMissionReward(MissionReward);
                cleanerBillMissionsView.setMissionList(missionsByEmployeeId);
                return new RestError(cleanerBillMissionsView);
            }
        } else {
            return RestError.E_AUTH_NEEDED;
        }

    }


    /*
  1.6版APP 保洁端  我的进度 + 轮播图展示（目前是三个图片）
   */
    @RequestMapping(value = "/app/console/api/AppGetEmployeeStatusAndBanner", method = RequestMethod.POST)
    public Object AppGetEmployeeStatusAndBanner(HttpServletRequest request) throws Exception {
        Map map =new HashMap();
        List<AppBanner> appBannerList = appBannerService.getAppBanner();

        for (AppBanner appbanner:
        appBannerList) {
            String s = storageService.generatePresignUrl(appbanner.getPic_path());
            appbanner.setRel_rul(s);
        }

        map.put("BannerUrl",appBannerList);
        //根据登录人的session找到对应的employee_id
        Integer employee_id = (Integer)request.getSession().getAttribute("employee_id");

        if (employee_id != null) {
            //根据employee_id 找到对应的 hotelEmployee关系对象(有两种情况 绑定酒店的是AA员工 没有绑定的为非AA员工)
            HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
            //新建view类对象 用于存放前台展示数据
            Employee employee = employeeService.getEmployee((Integer) employee_id);
            if (employee.getStatus() == null) {
                employee.setStatus(0);
            }
            map.put("StatusMap",employee);
            return new RestError(map);
        }else {
            map.put("StatusMap",null);
            return new RestError(map,20101,"使用此功能需要先登录");
        }
    }


    /*
    1.6版APP 保洁端  轮播图 上传接口
    */
    @RequestMapping(value = "/app/console/api/AddAppBanner", method = RequestMethod.POST)
    public Object AddAppBanner(HttpServletRequest request, HttpSession session,
                               @RequestParam(value = "files", required = false) MultipartFile[] files,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "img_num", required = false) Integer img_num) throws Exception {
        if (files != null && files.length > 0) {
            List<String> fileNames = new ArrayList<>(); //新建list<String>存
            for (MultipartFile multipartFile : files) {
                String[] fileName = multipartFile.getOriginalFilename().split("\\.");
                File tempFile = File.createTempFile(fileName[0], fileName[1]);
                multipartFile.transferTo(tempFile);
                String destFileName = UUID.randomUUID().toString() + "." + fileName[1];
                storageService.uploadFile(tempFile, destFileName);
                fileNames.add(destFileName);
            }
            for (String filename :
                    fileNames) {
                AppBanner appBanner = new AppBanner();
                appBanner.setImg_num(img_num);
                appBanner.setUrl(url);
                appBanner.setPic_path(filename);

                appBannerService.insert(appBanner);  //插入图片和描述信息
            }
            return RestError.E_OK;
        }return RestError.E_PICTURE_FAIL;//如果图片文件小于0 提示上传失败
    }


    /*
    1.6版APP 保洁端首页  工作推荐
    */
    @RequestMapping(value = "/app/console/api/SuggestWorkingPlace", method = RequestMethod.POST)
    public Object SuggestWorkingPlace(HttpServletRequest request,
                                      @RequestParam(value = "longitude", required = false) Double  longitude,
                                      @RequestParam(value = "latitude", required = false) Double  latitude,
                                      @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize,
                                      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {
        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo < 1) {
            pageNo = 1;
        }

        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        int index = (pageNo - 1) * pageSize;

        //根据登录人的session找到对应的employee_id
        Integer employee_id = (Integer) request.getSession().getAttribute("employee_id");

        if (employee_id != null) {
            //根据employee_id 找到对应的 hotelEmployee关系对象(有两种情况 绑定酒店的是AA员工 没有绑定的为非AA员工)
            HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
            //新建view类对象 用于存放前台展示数据
            Employee employee = employeeService.getEmployee((Integer) employee_id);
            if (employee.getStatus() == null) {
                employee.setStatus(0);
            }
        }
        //得到兼职任务list  分页
        List<Mission> missionList = missionService.getMissionListByAcceptemployeeidnullLimit(index, pageSize);
        //得到兼职任务list  不分页
        List<Mission> missionListUnlimit = missionService.getMissionListByAcceptemployeeidnull();


        Map map =new HashMap();
        map.put("PageNum",missionList.size() % pageSize==0 ? missionListUnlimit.size()/pageSize:missionListUnlimit.size()/pageSize+1);


        List SuggestJobList =new ArrayList();
        for (Mission mission:
        missionList) {
            Integer hotel_id = mission.getHotel_id();
            HotelBase hotel = hotelBaseService.getById(hotel_id);
            MissionPriceView missionPriceView =new MissionPriceView();
            missionPriceView.setJobType(1);
            missionPriceView.setStreet_address(hotel.getStreet_address());
            missionPriceView.setRoomNum(1);

            Date position_time = mission.getPosition_time();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String position_time_String = formatter.format(position_time);

            missionPriceView.setJobTime(position_time_String);
            missionPriceView.setDistance(2.85);

            Integer mission_typeid = mission.getMission_type();
            HotelMissiontypePrice hmpByHotelAndMissionType = hotelMissiontypePriceService.getHMPByHotelAndMissionType(0, mission_typeid);
            missionPriceView.setPriceForCleaner(hmpByHotelAndMissionType.getClean_price());

            String Create_time_String = formatter.format(mission.getCreate_time());
            missionPriceView.setJobCreateTime(Create_time_String);

            SuggestJobList.add(missionPriceView);
        }
        map.put("SuggestJobList",SuggestJobList);
        return new RestError(map);
    }






}
