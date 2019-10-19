package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.model.RoomView;
import com.aaroom.service.*;
import com.shangmei.util.DateUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sosoda on 2018/11/12.
 */
@RestController
@Component
public class MissionController {

    @Autowired
    private HotelBaseService hotelBaseService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionTypeService missionTypeService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ClothService clothService;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private RoomTypeService roomTypeService;


    //根据登录员工获得对应酒店房间列表 （分楼层展示）
    @RequestMapping(value = "/wx/console/api/getroomlistbyloginid", method = RequestMethod.GET)
    public Object roomlmissionchooseroomlistist(HttpServletRequest request) throws Exception {
        //根据当前登录人登录信息找到他的员工id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据员工id找到hotelemployee对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotleemployee对象找到对应的酒店id
        int hotel_id = hotelEmployee.getHotel_id();
        //根据hotelid得到不重复的floor集合
        List<Integer> floorlist = roomService.getFloorlistByHotelId(hotel_id);
        Map mapfloorroom = new HashMap();
        for (int i = 0; i < floorlist.size(); i++) {
            List<Room> floorroomlist = roomService.getFloorroomHotelId(hotel_id, floorlist.get(i));
            List<RoomView> roomViews = floorroomlist.stream().map(room -> roomService.getRoomView(room, null)).collect(Collectors.toList());
            mapfloorroom.put(floorlist.get(i), roomViews);
        }

        if (mapfloorroom.size() > 0) {
            return new RestError(mapfloorroom);
        } else {
            return RestError.E_DATA_INVALID;
        }
    }

    //获得missiontype
    @RequestMapping(value = "/wx/console/api/getmissiontype", method = RequestMethod.GET)
    public Object getmissiontypebyloginid() throws Exception {
        List<MissionType> MissionTypelist = missionTypeService.getMissionType();
        if (MissionTypelist.size() > 0) {
            return MissionTypelist;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }


    //获得本酒店标记为上班状态的保洁员inworking
    @RequestMapping(value = "/wx/console/api/getemployeenamebyloginidinworking", method = RequestMethod.GET)
    public Object getemloyeeinworking(HttpServletRequest request) throws Exception {
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        int hotel_id = hotelEmployee.getHotel_id();
        List employeelist = hotelEmployeeService.getemloyeeinworking(hotel_id);
        if (employeelist.size() > 0) {
            return employeelist;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }


    //发布任务【房间更新任务】   【后台】
    @RequestMapping(value = "/console/api/assigningmission", method = RequestMethod.POST)
    //接受前台传过来的 roomid集合 与 打扫类型 与 员工
    public Object assigningmission(HttpServletRequest request,
                                   @RequestParam(value = "employeeid", required = false) Integer employeeid,
                                   @RequestParam("missiontypeid") Integer missiontypeid,
                                   @RequestParam(value = "hotel_idchuan", required = false) Integer hotel_idchuan,
                                   @RequestParam(value = "publish_employee_idchuan", required = false) Integer publish_employee_idchuan,
                                   @RequestParam("roomlist[]") List<Integer> roomlist) throws Exception {
        //得到发布者name
        Integer empid = (Integer)request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee=null;
        if(empid!=null){
            hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(empid);

            //根据【酒店员工】对象得到酒店id
            hotel_idchuan = hotelEmployee.getHotel_id();
        }


        //新建list存下面遍历出来的对象存集合
        List<Mission> missionlist = new ArrayList();
        //TODO:如果状态改变，同时改变这里
        Integer[] status = new Integer[]{0,1};
        for (Integer room_id: roomlist) {
            List<Mission> missions = missionService.getMissionsByRoomId(room_id,status,1);
            if(missions!=null && missions.size()>0) {
                return RestError.E_DATA_EXIST;
            }
        }
        //TODO:暂时注释 这个任务存在 前台控制不能点击


        //遍历roomlist 存进对象返回mission对象
        for (int i = 0; i < roomlist.size(); i++) {
            Mission mission = new Mission();
            mission.setRoom_id(roomlist.get(i));
            //存状态 0为发出
            mission.setStatus(0);
            //存酒店id
            mission.setHotel_id(hotel_idchuan);
            if(empid==null){
                //存发出者id
                mission.setPublish_employee_id(publish_employee_idchuan);
            }else {
                //存发出者id
                mission.setPublish_employee_id(empid);
            }

            if(employeeid !=null){
                //存收到者id 根据传入人名得到id
                mission.setAccept_employee_id(employeeid);
            }

            //存打扫类型 根据出入打扫类型中文得到打扫类型id
            mission.setMission_type(missiontypeid);
            //执行插入sql
            missionService.save(mission);
            //每一个对象存入missionlist
            missionlist.add(mission);

            List<ClothLog> clothLogs = clothLogService.GetLogListNearBy(hotel_idchuan, 1,roomlist.get(i), 0);
            for (ClothLog clothLog: clothLogs) {
                ClothLog clothLogAlter = new ClothLog();
                clothLogAlter.setHotel_id(hotel_idchuan);
                clothLogAlter.setCloth_id(clothLog.getCloth_id());
                clothLogAlter.setPossessor_id(roomlist.get(i));
                clothLogAlter.setPossessor_type(0);
                clothLogAlter.setMission_id(mission.getId());
                clothLogAlter.setDirection(1);
                clothLogAlter.setStatus(1);
                clothLogService.insertSelective(clothLogAlter);
                clothService.updateStatusByclothId(clothLog.getCloth_id(),1);
            }
        }
        if (missionlist.size() > 0) {
            //返回码是0.发布=接收 1.完成
            return new RestError(missionlist);
        } else {
            return RestError.E_DATA_INVALID;
        }

    }

    //布草流转 【店内运营小程序】
    /*
    领取物料
     */
    @RequestMapping(value = "/wx/console/api/receivecloth", method = RequestMethod.POST)
    @ResponseBody  //接受前台传过来的员工id
    public Object assigningmission(@RequestParam Integer employeeId) throws Exception {
        Map map = new HashMap();
        map.put("employeeId", employeeId);
        List missionlistByMap = missionService.getListByMap(map);
        if (missionlistByMap.size() > 0) {
            //返回码是0.发布=接收 1.完成
            return missionlistByMap;
        } else return RestError.E_DATA_INVALID;
    }

    //【任务管理页面一】任务快速筛选 给出所有任务类型 :对象型 id和中文
    @RequestMapping(value = "/wx/console/api/getmissiontypefast", method = RequestMethod.POST)
    public Object getmissiontypefast() throws Exception {
        List<MissionType> MissionTypelist = missionTypeService.getMissionType();
        if (MissionTypelist.size() > 0) {
            return MissionTypelist;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }

    //【任务管理页面二】获得所有本酒店保洁员列表 给对象 id和中文
    @RequestMapping(value = "/wx/console/api/getemployeenamebyloginidfast", method = RequestMethod.GET)
    public Object getemployeenamebyloginidfast(HttpServletRequest request) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
        //根据hotel_id得到对应酒店下的所有员工
        Map employeelist = hotelEmployeeService.getEmployeeNameIdbyloginid(hotel_id);
        if (employeelist.size() > 0) {
            return employeelist;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }

    //【任务管理页面三】 给出mission完成情况的遍历结果 一对一
    @RequestMapping(value = "/wx/console/api/getStatusfast", method = RequestMethod.GET)
    public Object getStatusfast() throws Exception {
        Map statusmap = new HashMap();
        statusmap.put(0, "发布");
        statusmap.put(1, "工作中");
        statusmap.put(30, "已完成");
        statusmap.put(80, "纪检出错");
        statusmap.put(40, "终结（客人连住不让进）");
        if (statusmap.size() > 0) {
            return statusmap;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }

    //【任务管理页面四】 给出事件筛选选项对应的时间区间
    @RequestMapping(value = "/wx/console/api/getDuringTimeFast", method = RequestMethod.POST)
    public Object getDuringTimeFast() throws Exception {
        //获取系统当前时间
        Date currentTime = new Date();
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //新建集合存储
        //List timelist=new ArrayList();
        Map timemap = new HashMap();
        //使用calendar类的add方法 计算过去时间 +++++++++++++1天
        Calendar calendar24 = Calendar.getInstance();
        calendar24.setTime(currentTime);
        //-1今天的时间减一天
        calendar24.add(Calendar.DAY_OF_MONTH, -1);
        Date currentTime24 = calendar24.getTime();
        timemap.put("最近24小时内", currentTime24);
        //timelist.add(timemap);
        //+++++++++++++++++3天
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(currentTime);
        //-3今天的时间减一天
        calendar3.add(Calendar.DAY_OF_MONTH, -3);
        Date currentTime3 = calendar3.getTime();
        timemap.put("最近3天", currentTime3);
        //timelist.add(timemap);

        //+++++++++++++++++7天
        Calendar calendar7 = Calendar.getInstance();
        calendar7.setTime(currentTime);
        //-7今天的时间减一天
        calendar7.add(Calendar.DAY_OF_MONTH, -7);
        Date currentTime7 = calendar7.getTime();
        timemap.put("最近7天", currentTime7);
        // timelist.add(timemap);

        //+++++++++++++++++30天
        Calendar calendar30 = Calendar.getInstance();
        calendar30.setTime(currentTime);
        //-30今天的时间减一天
        calendar30.add(Calendar.DAY_OF_MONTH, -30);
        Date currentTime30 = calendar30.getTime();
        timemap.put("最近30天", currentTime30);
        // timelist.add(timemap);

        return timemap;
    }

    //【任务管理页面五】 根据前台所选选项得到相应查询结果
    @RequestMapping(value = "/wx/console/api/getmissionbyfast6",method = RequestMethod.POST)
    public Object getmissionbyfast(HttpServletRequest request,
                                   @RequestBody Mission mission,
                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {

        //加条件判断 如果没有开始时间（近点时间）就new一个系统当前时间
        Date starttime = mission.getStarttime();
        if (starttime == null) {
            Date Startdate = new Date();
            mission.setStarttime(Startdate);
        }
        if (mission.getPageNo()!=null){
            pageNo=mission.getPageNo();
        }
        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo< 1) {
            pageNo = 1;
        }
        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        int index = (pageNo - 1) * pageSize;

        //根据登录用户得到员工id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据员工id得到hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id
        int hotel_id = hotelEmployee.getHotel_id();
        //把hotel_id存进mission对象
        mission.setHotel_id(hotel_id);
        //进行遍历 动态sql只要有条件就查询出missionlist 时间是区间 当前时间+N天展示 用in
        if(mission.getRoom_name() !=""){
            Room room = roomService.getRoomByname(mission.getRoom_name(),hotel_id);
            mission.setRoom_id(room.getId());
        }
        List<Mission> missionlist = missionService.getListByfastmissionlimit(mission, index, pageSize);

        for (Mission missioncopy :
                missionlist) {
            //前台要三个中文显示 1：阿姨名称 2：任务属性 3：完成状态 都存进去
            // 1：阿姨名称 存进去
            Integer employee_idcopy = missioncopy.getAccept_employee_id();
            String employeenamebyidcopy = employeeService.getEmployeenamebyid(employee_idcopy);
            missioncopy.setAccept_employee_name(employeenamebyidcopy);
            //3：任务属性存进去
            Integer mission_typecopy = missioncopy.getMission_type();
            String getdescbyidcopy = missionTypeService.getdescbyid(mission_typecopy);
            missioncopy.setMission_type_name(getdescbyidcopy);
            // 2：完成状态  存进去
            Integer statuscopy = missioncopy.getStatus();
            if (statuscopy == 0) {
                missioncopy.setStatus_name("发布");
            } else if (statuscopy == 1) {
                missioncopy.setStatus_name("工作中");
            } else if (statuscopy == 2) {
                missioncopy.setStatus_name("已完成");
            } else if (statuscopy == 3) {
                missioncopy.setStatus_name("任务出错");
            }
            //3:转换时间格式的完成时间 存进去
            Date Create_time = missioncopy.getCreate_time();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatCreate_time = sdf.format(Create_time);
            missioncopy.setUpdate_time_name(formatCreate_time);

            //room_id转成room_name存进去
            Room roomById = roomService.getRoomById(missioncopy.getRoom_id());
            missioncopy.setRoom_name(roomById.getRoom_name());
        }
        //查出该条件下有多少个数量用作分页查询
        Integer count = missionService.getCountBymission(mission);
        //建一个map 把 分页数据和得到的 missionlist都返回给前端使用
        Map<String, Object> ret = new HashMap<>();
        ret.put("paging", new Paging(pageNo, pageSize, count));
        ret.put("data", missionlist);

        if (ret.size() > 0) {
            return ret;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }


    //【frontDesk】 根据前台所选选项得到相应查询结果 《任务类型》《完成状态》《阿姨名称》《房间号》
    @RequestMapping(value = "/wx/console/api/GetMissionListFrontDesk",method = RequestMethod.POST)
    public Object GetMissionListFrontDesk(HttpServletRequest request,
                                          @RequestParam(value = "mission_type", required = false) Integer mission_type,
                                          @RequestParam(value = "status", required = false) Integer status,
                                          @RequestParam(value = "accept_employee_id", required = false) Integer accept_employee_id,
                                          @RequestParam(value = "room_name", required = false) String room_name,
                                          @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                          @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {

        //根据登录用户得到员工id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据员工id得到hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id
        Integer hotel_id = hotelEmployee.getHotel_id();

        //按照任務對象來查看任務對象(首先製作一個任務對象 作爲查找條件)
           Mission mission=new Mission();
            mission.setHotel_id(hotel_id);
            if(mission_type!=null){
                mission.setMission_type(mission_type);
            }
           if(status!=null){
               mission.setStatus(status);
           }
           if(accept_employee_id !=null){
                mission.setAccept_employee_id(accept_employee_id);
           }
           if(room_name!=null && !room_name.equals("")){
               Room roomByname = roomService.getRoomByname(room_name, hotel_id);
               mission.setRoom_id(roomByname.getId());
           }
        //获取当前时间
        Date Startdate = new Date();
            mission.setStarttime(Startdate);


        //获取当天0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date ZeroTime = calendar.getTime();

        mission.setEndtime(ZeroTime);

        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo< 1) {
            pageNo = 1;
        }
        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        int index = (pageNo - 1) * pageSize;


        List<Mission> missionlist = missionService.getListByfastmissionlimit(mission, index, pageSize);

        for (Mission missioncopy :
                missionlist) {
            //前台要三个中文显示 1：阿姨名称 2：任务属性 3：完成状态 都存进去
            // 1：阿姨名称 存进去
            Integer employee_idcopy = missioncopy.getAccept_employee_id();
            String employeenamebyidcopy = employeeService.getEmployeenamebyid(employee_idcopy);
            missioncopy.setAccept_employee_name(employeenamebyidcopy);
            //3：任务属性存进去
            Integer mission_typecopy = missioncopy.getMission_type();
            String getdescbyidcopy = missionTypeService.getdescbyid(mission_typecopy);
            missioncopy.setMission_type_name(getdescbyidcopy);
            // 2：完成状态  存进去
            Integer statuscopy = missioncopy.getStatus();
            if (statuscopy == 0) {
                missioncopy.setStatus_name("发布");
            } else if (statuscopy == 1) {
                missioncopy.setStatus_name("工作中");
            } else if (statuscopy == 30) {
                missioncopy.setStatus_name("已完成");
            } else if (statuscopy == 40) {
                missioncopy.setStatus_name("终结（客人连住不让进）");
            }else if(statuscopy == 80){
                missioncopy.setStatus_name("纪检出错");
            }
            //3:转换时间格式的完成时间 存进去
            Date Create_time = missioncopy.getCreate_time();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatCreate_time = sdf.format(Create_time);
            missioncopy.setUpdate_time_name(formatCreate_time);

            //room_id转成room_name存进去
            Room roomById = roomService.getRoomById(missioncopy.getRoom_id());
            missioncopy.setRoom_name(roomById.getRoom_name());
        }
        //查出该条件下有多少个数量用作分页查询
        Integer count = missionService.getCountBymission(mission);

        //建一个map 把 分页数据和得到的 missionlist都返回给前端使用
        Map<String, Object> ret = new HashMap<>();
        ret.put("paging", new Paging(pageNo, pageSize, count));
        ret.put("data", missionlist);

        if (ret.size() > 0) {
            return ret;
        } else {
            return RestError.E_DATA_INVALID;
        }
    }
    //【frontDesk】【布草人力管理-今日任务倒出Excel】 根据前台所选选项得到相应查询结果 下載這個列表 不分頁
    @RequestMapping(value = "/wx/console/api/DownLoadMissionTodayExcel", method = RequestMethod.POST)
    public String DownLoadMissionTodayExcel(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam(value = "mission_type", required = false) Integer mission_type,
                                            @RequestParam(value = "status", required = false) Integer status,
                                            @RequestParam(value = "accept_employee_id", required = false) Integer accept_employee_id,
                                            @RequestParam(value = "room_name", required = false) String room_name) throws Exception {
        request.setCharacterEncoding("utf-8");
        String re = request.getParameter("state");
        String ad = request.getParameter("ad");
        String n = request.getParameter("n");

        //根据登录用户得到员工id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据员工id得到hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id
        Integer hotel_id = hotelEmployee.getHotel_id();

        //按照任務對象來查看任務對象(首先製作一個任務對象 作爲查找條件)
        Mission mission=new Mission();
        mission.setHotel_id(hotel_id);
        if(mission_type!=null){
            mission.setMission_type(mission_type);
        }
        if(status!=null){
            mission.setStatus(status);
        }
        if(accept_employee_id !=null){
            mission.setAccept_employee_id(accept_employee_id);
        }
        if(room_name!=null && !room_name.equals("")){
            Room roomByname = roomService.getRoomByname(room_name, hotel_id);
            mission.setRoom_id(roomByname.getId());
        }
        //获取当前时间
        Date Startdate = new Date();
        mission.setStarttime(Startdate);


        //获取当天0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date ZeroTime = calendar.getTime();
        mission.setEndtime(ZeroTime);

        List<Mission> missionlist = missionService.getListByMissionNoLimitToday(mission);

        for (Mission missioncopy :
                missionlist) {
            //前台要三个中文显示 1：阿姨名称 2：任务属性 3：完成状态 都存进去
            // 1：阿姨名称 存进去
            Integer employee_idcopy = missioncopy.getAccept_employee_id();
            String employeenamebyidcopy = employeeService.getEmployeenamebyid(employee_idcopy);
            missioncopy.setAccept_employee_name(employeenamebyidcopy);
            //3：任务属性存进去
            Integer mission_typecopy = missioncopy.getMission_type();
            String getdescbyidcopy = missionTypeService.getdescbyid(mission_typecopy);
            missioncopy.setMission_type_name(getdescbyidcopy);
            // 2：完成状态  存进去
            Integer statuscopy = missioncopy.getStatus();
            if (statuscopy == 0) {
                missioncopy.setStatus_name("发布");
            } else if (statuscopy == 1) {
                missioncopy.setStatus_name("工作中");
            } else if (statuscopy == 30) {
                missioncopy.setStatus_name("已完成");
            } else if (statuscopy == 40) {
                missioncopy.setStatus_name("终结（客人连住不让进）");
            }else if(statuscopy == 80){
                missioncopy.setStatus_name("纪检出错");
            }
            //3:转换时间格式的完成时间 存进去
            Date Create_time = missioncopy.getCreate_time();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatCreate_time = sdf.format(Create_time);
            missioncopy.setUpdate_time_name(formatCreate_time);

            //room_id转成room_name存进去
            Room roomById = roomService.getRoomById(missioncopy.getRoom_id());
            missioncopy.setRoom_name(roomById.getRoom_name());
        }

        List<String> title = new ArrayList<String>();
        title.add("房间号");
        title.add("任务ID");
        title.add("任务类型");
        title.add("完成状态");
        title.add("任务发布时间");
        title.add("阿姨名称");
        MissionTodayCreateExcel(request, response, missionlist, "今日任务列表导出Excel", title);
        return null;
    }

    public void MissionTodayCreateExcel(HttpServletRequest request, HttpServletResponse response,
                                          List<Mission> missionlist, String fileName, List<String> title) {
        try {
// 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle style = workbook.createCellStyle();
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            // 字体增粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = workbook.createSheet("sheet1");
            // 创建Excel的sheet的一行 (表头)
            HSSFRow row = sheet.createRow(0);
            // 表头内容填充
            for (int i = 0; i < title.size(); i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 5000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title.get(i));
                cell.setCellStyle(style);
            }
            // 创建内容行
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);// 自动换行
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
            for (int j = 0; j < missionlist.size(); j++) {
                HSSFRow contentRow = sheet.createRow(j + 1);
                for (int k = 0; k < title.size(); k++) {
                    HSSFCell cell = contentRow.createCell(k);
                    switch (k) {
                        case 0:
                            if (missionlist.get(j).getRoom_name() != null) {// 第一列房间号
                                cell.setCellValue(missionlist.get(j).getRoom_name());
                            }
                            break;
                        case 1:
                            if (missionlist.get(j).getId() != null) {//第二列任务ID
                                cell.setCellValue( missionlist.get(j).getId());
                            }
                            break;
                        case 2:
                            if (missionlist.get(j).getMission_type_name() != null) {//第三列任务类型
                                cell.setCellValue(missionlist.get(j).getMission_type_name());
                            }
                            break;
                        case 3:
                            if (missionlist.get(j).getStatus_name() != null) {//第四列完成状态
                                cell.setCellValue(missionlist.get(j).getStatus_name());
                            }
                            break;
                        case 4:
                            if (missionlist.get(j).getCreate_time() != null) {//第五列任务发布时间
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date create_time = missionlist.get(j).getCreate_time();
                                cell.setCellValue(df.format(create_time));
                            }
                            break;
                        case 5:
                            if (missionlist.get(j).getAccept_employee_name() != null) {//第六列阿姨名称
                                cell.setCellValue(missionlist.get(j).getAccept_employee_name());
                            }
                            break;
                        default:
                            break;
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //【任务管理页面六】 导出查询数据到excel表格
    @RequestMapping(value = "/wx/console/api/downloadmissionexcelfast", method = RequestMethod.POST)
    public String getmsg(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "mission_type", defaultValue = "0") List<Integer> mission_type,
                         @RequestParam(value = "accept_employee_id", defaultValue = "0") List<Integer> accept_employee_id,
                         @RequestParam(value = "status") Integer status,
                         @RequestParam(value = "room_name") String room_name,
                         @RequestParam(value = "id") Integer id,
                         @RequestParam(value = "comment") String comment,
                         @RequestParam(value = "create_time") String create_time,
                         @RequestParam(value = "starttime") String starttime,
                         @RequestParam(value = "endtime") String endtime
                         ) throws Exception {
        request.setCharacterEncoding("utf-8");
        String re = request.getParameter("state");
        String ad = request.getParameter("ad");
        String n = request.getParameter("n");

        //根据登录用户得到员工id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据员工id得到hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id
        int hotel_id = hotelEmployee.getHotel_id();
        //把hotel_id存进mission对象

        //转时间格式：
        //Date CreateTime=new Date(create_time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date CreateTime=sdf.parse(create_time);
        Date StartTime=sdf.parse(starttime);
        Date EndTime=sdf.parse(endtime);
        //建立一个新对象存进去值
        Mission mission =new Mission();
        mission.setMission_type_list(mission_type);
        mission.setAccept_employee_id_list(accept_employee_id);
        mission.setStatus(status);
        //room_id转成room_name存进去
        if(room_name !=""){
            Room roomByname = roomService.getRoomByname(room_name, hotel_id);
            mission.setRoom_id(roomByname.getId());
        }
        mission.setId(id);
        mission.setComment(comment);
        //加条件判断 如果create_time不等于0 那么往mission对象中存开始和结束时间
        //如果等于0 那就是就是有开始时间和结束时间 直接赋值
        //Date starttime = mission.getStarttime();
        if (create_time.equals(0)) {
             mission.setStarttime(StartTime);
            mission.setEndtime(EndTime);
        }else{
            Date Startdate = new Date();
            mission.setStarttime(Startdate);
            mission.setEndtime(CreateTime);
        }

        mission.setHotel_id(hotel_id);
        //进行遍历 动态sql只要有条件就查询出missionlist 时间是区间 当前时间+N天展示 用in

        List<Mission> missionlist = missionService.getListByfastmission(mission);


        for (Mission missioncopy :
                missionlist) {
            //前台要三个中文显示 1：阿姨名称 2：任务属性 3：完成状态 都存进去
            // 1：阿姨名称
            Integer employee_idcopy = missioncopy.getAccept_employee_id();
            String employeenamebyidcopy = employeeService.getEmployeenamebyid(employee_idcopy);
            missioncopy.setAccept_employee_name(employeenamebyidcopy);
            //3：完成状态 都存进去
            Integer mission_typecopy = missioncopy.getMission_type();
            String getdescbyidcopy = missionTypeService.getdescbyid(mission_typecopy);
            missioncopy.setMission_type_name(getdescbyidcopy);
            // 2：任务属性
            Integer statuscopy = missioncopy.getStatus();
            if (statuscopy == 0) {
                missioncopy.setStatus_name("发布");
            } else if (statuscopy == 1) {
                missioncopy.setStatus_name("工作中");
            } else if (statuscopy == 2) {
                missioncopy.setStatus_name("已完成");
            } else if (statuscopy == 3) {
                missioncopy.setStatus_name("任务出错");
            }
        }
        //查出该条件下有多少个数量
        Integer count = missionService.getCountBymission(mission);
        List<String> title = new ArrayList<String>();
        title.add("任务Id");
        title.add("房间Id");
        title.add("保洁员姓名");
        title.add("任务属性");
        title.add("完成状态");
        title.add("备注");
        createExcel(request, response, missionlist, "任务列表导出Excel", title);
        return null;

    }

    public void createExcel(HttpServletRequest request, HttpServletResponse response,
                            List<Mission> missionlist, String fileName, List<String> title) {
        try {
// 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle style = workbook.createCellStyle();
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            // 字体增粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = workbook.createSheet("sheet1");
            // 创建Excel的sheet的一行 (表头)
            HSSFRow row = sheet.createRow(0);
            // 表头内容填充
            for (int i = 0; i < title.size(); i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 5000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title.get(i));
                cell.setCellStyle(style);
            }
            // 创建内容行
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);// 自动换行
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
            for (int j = 0; j < missionlist.size(); j++) {
                HSSFRow contentRow = sheet.createRow(j + 1);
                for (int k = 0; k < title.size(); k++) {
                    HSSFCell cell = contentRow.createCell(k);
                    switch (k) {
                        case 0:
                            if (missionlist.get(j).getId() != null) {// 第一列任务id
                                cell.setCellValue(missionlist.get(j).getId());
                            }
                            break;
                        case 1:
                            if (missionlist.get(j).getRoom_id() != null) {//第二列房间号
                                Integer room_id = missionlist.get(j).getRoom_id();
                                cell.setCellValue(roomService.getRoomById(room_id).getRoom_name());
                            }
                            break;
                        case 2:
                            if (missionlist.get(j).getAccept_employee_name() != null) {//第三列保洁员姓名
                                String employeename = employeeService.getEmployeenamebyid(missionlist.get(j).getAccept_employee_id());
                                cell.setCellValue(employeename);
                            }
                            break;
                        case 3:
                            if (missionlist.get(j).getMission_type_name() != null) {//第四列任务属性
                                cell.setCellValue(missionlist.get(j).getMission_type_name());
                            }
                            break;
                        case 4:
                            if (missionlist.get(j).getStatus() != null) {//第五列完成状态
                                cell.setCellValue(missionlist.get(j).getStatus_name());
                            }
                            break;
                        case 5:
                            if (missionlist.get(j).getComment() != null) {//第六列备注
                                cell.setCellValue(missionlist.get(j).getComment());
                            }
                            break;
                        default:
                            break;
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



/*    public static final Integer WRITE_MAX_LEN =12;

    public static byte[] long2byte(long res) {
        byte[] buffer = new byte[WRITE_MAX_LEN];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i+WRITE_MAX_LEN- 8] = (byte) ((res >> offset) & 0xff);
        }
        return buffer;
    }

    public static String byteArrayToString(byte[] btAryHex, int nIndex, int nLen) {
        if (nIndex + nLen > btAryHex.length) {
            nLen = btAryHex.length - nIndex;
        }

        String strResult = String.format("%02X", btAryHex[nIndex]);
        for (int nloop = nIndex + 1; nloop < nIndex + nLen; nloop++ ) {
            String strTemp = String.format(" %02X", btAryHex[nloop]);

            strResult += strTemp;
        }

        return strResult;
    }*/


    //输出到Excel表
   /* @RequestMapping(value = "/wx/console/api/downloadmissionexcelfast", method = RequestMethod.POST)
    public String getmsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        String re = request.getParameter("state");
        String ad = request.getParameter("ad");
        String n = request.getParameter("n");



        List<Integer> list = new ArrayList();
        for (int i = 1; i <= 500; i++)
        {
            list.add(i);
        }
        List<String> ContentList = new ArrayList();
        for (int id:
                list) {
            long qrcode_long = id;
            byte[] epcarray;
            epcarray = long2byte(qrcode_long);
            String content = byteArrayToString(epcarray,0,WRITE_MAX_LEN).replaceAll(" ", "");
            System.out.println("二维码ID:  "+id+"            "+"Rfid:  "+content);
            ContentList.add(content);
        }

        List<String> title = new ArrayList<String>();
        title.add("Rfid1-500");
        createExcel(request, response,ContentList, "Rfid列表导出Excel", title);
        return null;
    }
    public static void createExcel(HttpServletRequest request, HttpServletResponse response,List<String> ContentList, String fileName, List<String> title) {
        try {
// 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle style = workbook.createCellStyle();
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            // 字体增粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = workbook.createSheet("sheet1");
            // 创建Excel的sheet的一行 (表头)
            HSSFRow row = sheet.createRow(0);
            // 表头内容填充
            for (int i = 0; i < title.size(); i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 10000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title.get(i));
                cell.setCellStyle(style);
            }
            // 创建内容行
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);// 自动换行
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
            for (int j = 0; j < ContentList.size(); j++) {
                HSSFRow contentRow = sheet.createRow(j + 1);
                for (int k = 0; k < title.size(); k++) {
                    HSSFCell cell = contentRow.createCell(k);
                    switch (k) {
                        case 0:
                            if (ContentList.get(j)!= null) {// 第一列rfid1-500
                                cell.setCellValue(ContentList.get(j));
                            }
                            break;
                        default:
                            break;
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/




    //【任务管理页面七】 根据具体任务id给出具体新旧布草id与详情
    @RequestMapping(value = "/wx/console/api/getnewoldclothlist", method = RequestMethod.POST)
    public Object getnewoldclothlist(Integer missionid) throws Exception {

    //根据missionid得到新旧布草两个list
    Map<String,List> newoldmap =clothLogService.getLogListByMissionidPossessorTypeidDirectionid(missionid);
        List<Integer> oldClothidlist = newoldmap.get("OldClothlist");
        List<Integer> NewClothidlist = newoldmap.get("NewClothlist");


        //建两个map存单独的新旧布草对
        Map oldclothmap=new HashMap();
        Map newclothmap=new HashMap();


        //遍历每一个list得到对应desc
        for (Integer oldClothid:
        oldClothidlist) {
            List oldClothtypename=relClothTypeService.getDescByclothid(oldClothid);
            oldclothmap.put(oldClothid,oldClothtypename);
        }
        for (Integer newClothid:
                NewClothidlist) {
            List newlothtypename=relClothTypeService.getDescByclothid(newClothid);
            newclothmap.put(newClothid,newlothtypename);
        }
        Map clothmapbigger=new HashMap();
        clothmapbigger.put("oldcloth",oldclothmap);
        clothmapbigger.put("newcloth",newclothmap);

        if (clothmapbigger.size()>0){
            return clothmapbigger;
        }else {
            return RestError.E_TRANSACTIONAL_FAIL;
        }
    }

    @RequestMapping("/wx/console/api/postRoomStatus")
    public RestError postRoomStatus(HttpServletRequest request,
                                    @RequestParam("room_ids[]") Integer[] room_ids,
                                    Integer status){
        Integer employee_id = (Integer) request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelIdByEmpId(employee_id);
        int hotel_id = hotelEmployee.getHotel_id();
        for (Integer room_id: room_ids) {
            Room room = roomService.getRoomById(room_id);
            if(room!=null && room.getHotel_id()== hotel_id) {
                room.setStatus(status);
                roomService.update(room);
            }
        }
        return RestError.E_OK;

    }
//=====================================================以下为安卓适配
    //按照入参empid查找对应人员下的任务列表转成missionview
@RequestMapping(value = "/app/console/api/GetMission", method = RequestMethod.POST)
public Object GetMission(HttpServletRequest request,
                               @Param("employee_id")Integer accept_employee_id,
                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                               @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {


    //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
    if (pageNo< 1) {
        pageNo = 1;
    }
    //每页显示多少行 默认20行
    //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
    // 他们存在函数关系 pageNo -1 * pagesize=index
    int index = (pageNo - 1) * pageSize;

    //根据登录用户得到员工id
    Object employee_id = request.getSession().getAttribute("employee_id");
    //根据员工id得到hotelEmployee关系对象
    HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
    //根据hotelEmployee关系对象得到hotel_id
    int hotel_id = hotelEmployee.getHotel_id();


    List<Mission> missionlist = missionService.getMissionListByEmpIdLimit(accept_employee_id, index, pageSize);
    MissionsView MV=new MissionsView();
    for (Mission missioncopy :
            missionlist) {
        //前台要三个中文显示 1：任务属性中文 2：mission status中文 3：房间的类型中文 4:房间的中文

        //1：任务属性存进去
        Integer mission_typecopy = missioncopy.getMission_type();
        String getdescbyidcopy = missionTypeService.getdescbyid(mission_typecopy);
        MV.setMission_type_name(getdescbyidcopy);
        // 2：完成状态  存进去
        Integer statuscopy = missioncopy.getStatus();
        if (statuscopy == 0) {
            MV.setStatus_name("发布");
        } else if (statuscopy == 1) {
            MV.setStatus_name("工作中");
        } else if (statuscopy == 2) {
            MV.setStatus_name("提交完成(待检查)");
        } else if (statuscopy == 30) {
            MV.setStatus_name("领班-合格");
        }else if (statuscopy == 40) {
            MV.setStatus_name("终结");
        }else if (statuscopy == 60) {
            MV.setStatus_name("返工（领班）");
        }else if (statuscopy == 70) {
            MV.setStatus_name("领班-不合格");
        }else if (statuscopy == 80) {
            MV.setStatus_name("业务-不合格");
        }else if (statuscopy == 90) {
            MV.setStatus_name("业务-合格");
        }
        //3:房间类型
        Room roomById = roomService.getRoomById(missioncopy.getRoom_id());
        Integer room_type_id = roomById.getRoom_type_id();
        RoomType roomTypeById = roomTypeService.getRoomTypeById(room_type_id);
        MV.setRoom_type_name(roomTypeById.getDesc());

        // 4:room_id转成room_name存进去
        MV.setRoom_name(roomById.getRoom_name());
    }

    //查出该条件下有多少个数量用作分页查询
    Mission mission=new Mission();
    mission.setAccept_employee_id(accept_employee_id);
    Integer count = missionService.getCountBymission(mission);
    //建一个map 把 分页数据和得到的 missionlist都返回给前端使用
    Map<String, Object> ret = new HashMap<>();
    ret.put("paging", new Paging(pageNo, pageSize, count));
    ret.put("data", missionlist);

    if (ret.size() > 0) {
        return ret;
    } else {
        return RestError.E_DATA_INVALID;
    }
}

    @PostMapping("/console/api/reAssignMission")
    public Object reAssignMission(HttpServletRequest request,
                                  @RequestParam(value = "mission_ids[]",required = false) Integer[] mission_ids,
                                  @RequestParam(value = "accept_employee_id",required = false) Integer accept_employee_id){
        //TODO:判断员工数任务/操作者/接手任务者是否为本酒店员工
        for (Integer mission_id: mission_ids) {
            Mission mission  = missionService.getMissionById(mission_id);
            if(mission.getStatus()!=0) {
                return RestError.E_MISSION_ASSIONFAIL;
            }
            mission.setAccept_employee_id(accept_employee_id);
            missionService.updateByPrimaryKeySelective(mission);
        }
        return RestError.E_OK;
    }

    //自动发配任务 每日八点
    @PostMapping("/autoAssignMission")
    @Scheduled(cron = "0 0 8 * * ?")
    public Object autoAssignMission(){
        //HttpServletRequest request
        //根据登录人的session找到对应的employee_id
        // Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        //HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性


        //【张赢】暂时遍历所有hotel_base表中的酒店
        List<HotelBase> allHotelBase = hotelBaseService.getAllHotelBase();

        for (HotelBase hotelBase:
        allHotelBase) {
            List<Room> rooms = roomService.getRoomsByHotelId(hotelBase.getId());
            //获取每个阿姨平均工作时长
            Integer totalMissionMinutes = 0;
            for (Room room: rooms) {
                if(room.getStatus()==0 || room.getStatus()==4 ) {
                    totalMissionMinutes+= 3;
                } else if(room.getStatus()==1) {
                    totalMissionMinutes+= 30;
                } else if(room.getStatus()==3) {
                    totalMissionMinutes+= 22;
                }
            }
            List<Employee> employees = hotelEmployeeService.getemloyeeinworking(hotelBase.getId());

            if(employees==null || employees.size()==0) {
                return RestError.E_MISSION_ASSIONFAIL;
            }
            Integer averageMissionMinutes = totalMissionMinutes / employees.size();

            //将任务分发给阿姨，如果不是最后一个阿姨并且将要给予的任务时长大于平均，则换至下个阿姨
            Integer currentMissionMinutes = 0;
            Integer employeeIndex = 0;
            List<Mission> missions = new ArrayList<>();
            for (Room room: rooms) {
                Integer mission_type = null;
                if(room.getStatus()==0 || room.getStatus()==4 ) {
                    mission_type = 3;
                    currentMissionMinutes +=3;
                } else if(room.getStatus()==1) {
                    mission_type = 1;
                    currentMissionMinutes +=30;
                } else if(room.getStatus()==3) {
                    mission_type = 2;
                    currentMissionMinutes +=22;
                }else {
                    continue;
                }
                if(currentMissionMinutes>averageMissionMinutes && employeeIndex<employees.size()- 1) {
                    currentMissionMinutes = 0;
                    employeeIndex += 1;
                }
                Mission mission = new Mission();
                mission.setHotel_id(hotelBase.getId());
                mission.setRoom_id(room.getId());
                mission.setAccept_employee_id(employees.get(employeeIndex).getId());
                mission.setPublish_employee_id(hotelBase.getId());
                mission.setMission_type(mission_type);
                mission.setStatus(0);
                missions.add(mission);
            }
            missionService.insertInBatch(missions);
        }

        return RestError.E_OK;
    }


    //【frontDesk】【布草人力管理-历史任务查询】 根据前台所选选项得到相应查询结果 《任务类型》《阿姨名称》《房间号》《開始時間》《結束時間》
    @RequestMapping(value = "/wx/console/api/GetMissionListFrontDeskHistory",method = RequestMethod.POST)
    public Object GetMissionListFrontDeskHistory(HttpServletRequest request,
                                          @RequestParam(value = "mission_type", required = false) Integer mission_type,
                                          @RequestParam(value = "accept_employee_id", required = false) Integer accept_employee_id,
                                          @RequestParam(value = "room_name", required = false) String room_name,
                                          @RequestParam(value = "StartTime", required = false) String EndTime,
                                          @RequestParam(value = "EndTime", required = false) String StartTime,
                                          @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                          @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {

        //根据登录用户得到员工id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据员工id得到hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id
        Integer hotel_id = hotelEmployee.getHotel_id();

        //按照任務對象來查看任務對象(首先製作一個任務對象 作爲查找條件)
        Mission mission=new Mission();
        mission.setHotel_id(hotel_id);
        if(mission_type!=null){
            mission.setMission_type(mission_type);
        }
        if(accept_employee_id !=null){
            mission.setAccept_employee_id(accept_employee_id);
        }
        if(room_name!=null && !room_name.equals("")){
            Room roomByname = roomService.getRoomByname(room_name, hotel_id);
            mission.setRoom_id(roomByname.getId());
        }
        //設置開始查詢時間 與結結束時間
        Date StartTimeparse = DateUtil.parse(StartTime);
        mission.setStarttime(StartTimeparse);
        Date EndTimeparse = DateUtil.parse(EndTime);
        mission.setEndtime(EndTimeparse);

        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo< 1) {
            pageNo = 1;
        }
        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        int index = (pageNo - 1) * pageSize;


        List<Mission> missionlist = missionService.getListByfastmissionlimit(mission, index, pageSize);

        for (Mission missioncopy :
                missionlist) {
            //前台要三个中文显示 1：房間號  2：任務id  3，任務類型  4，完成狀態  5，任務發配時間 6:完成時間 7：保潔名字  都存进去
            //1,room_id转成room_name存进去
            Room roomById = roomService.getRoomById(missioncopy.getRoom_id());
            missioncopy.setRoom_name(roomById.getRoom_name());
            //2，任務id直接有
            //3,任務類型直接有 1234
            //4，完成狀態直接有 status  0.发布 1.工作中 2.提交完成(待检查) 30.领班-合格 40.终结  60.返工（领班） 70.领班-不合格 80.业务-不合格 90.业务-合格
            //5,任務發配時間直接有 create_time
            //6,完成時間 可空 null
            //7,阿姨名称 存进去
            Integer employee_idcopy = missioncopy.getAccept_employee_id();
            String employeenamebyidcopy = employeeService.getEmployeenamebyid(employee_idcopy);
            missioncopy.setAccept_employee_name(employeenamebyidcopy);
        }
        //查出该条件下有多少个数量用作分页查询
        Integer count = missionService.getCountBymission(mission);

        //建一个map 把 分页数据和得到的 missionlist都返回给前端使用
        Map<String, Object> ret = new HashMap<>();
        ret.put("paging", new Paging(pageNo, pageSize, count));
        ret.put("data", missionlist);

        if (ret.size() > 0) {
            return new RestError(ret);
        } else {
            return RestError.E_DATA_INVALID;
        }
    }

    //【frontDesk】【布草人力管理-历史任务倒出Excel】 根据前台所选选项得到相应查询结果 下載這個列表 不分頁
    @RequestMapping(value = "/wx/console/api/DownLoadMissionHistoryExcel", method = RequestMethod.POST)
    public String DownLoadMissionHistoryExcel(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam(value = "mission_type", required = false) Integer mission_type,
                                              @RequestParam(value = "accept_employee_id", required = false) Integer accept_employee_id,
                                              @RequestParam(value = "room_name", required = false) String room_name,
                                              @RequestParam(value = "StartTime", required = false) String EndTime,
                                              @RequestParam(value = "EndTime", required = false) String StartTime
    ) throws Exception {
        request.setCharacterEncoding("utf-8");
        String re = request.getParameter("state");
        String ad = request.getParameter("ad");
        String n = request.getParameter("n");

        //根据登录用户得到员工id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据员工id得到hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id
        int hotel_id = hotelEmployee.getHotel_id();
        //把hotel_id存进mission对象

        //建立一个新对象存进去值
        Mission mission =new Mission();

        //設置開始查詢時間 與結結束時間
        Date StartTimeparse = DateUtil.parse(StartTime);
        mission.setStarttime(StartTimeparse);
        Date EndTimeparse = DateUtil.parse(EndTime);
        mission.setEndtime(EndTimeparse);

        mission.setHotel_id(hotel_id);
        if(mission_type!=null){
            mission.setMission_type(mission_type);
        }
        if(accept_employee_id !=null){
            mission.setAccept_employee_id(accept_employee_id);
        }
        if(room_name!=null && !room_name.equals("")){
            Room roomByname = roomService.getRoomByname(room_name, hotel_id);
            mission.setRoom_id(roomByname.getId());
        }


        //进行遍历 动态sql只要有条件就查询出missionlist 时间是区间 当前时间+N天展示 用in

        List<Mission> missionlist = missionService.getListByfastmissionlimit(mission,null,null);

        for (Mission missioncopy :
                missionlist) {
            //前台要三个中文显示 1：房間號  2：任務id  3，任務類型  4，完成狀態  5，任務發配時間 6:完成時間 7：保潔名字  都存进去
            //1,room_id转成room_name存进去
            Room roomById = roomService.getRoomById(missioncopy.getRoom_id());
            missioncopy.setRoom_name(roomById.getRoom_name());
            //2，任務id直接有
            //3,任務類型直接有
            MissionType missionTypeById = missionTypeService.getMissionTypeById(missioncopy.getMission_type());
            missioncopy.setMission_type_name(missionTypeById.getDesc());
            //4，完成狀態直接有 status  0.发布 1.工作中 2.提交完成(待检查) 30.领班-合格 40.终结  60.返工（领班） 70.领班-不合格 80.业务-不合格 90.业务-合格
            Integer status = missioncopy.getStatus();
            switch(status){
                case 0:missioncopy.setStatus_name("发布");
                    break;
                case 1:missioncopy.setStatus_name("工作中");
                    break;
                case 2:missioncopy.setStatus_name("提交完成(待检查)");
                    break;
                case 30:missioncopy.setStatus_name("领班-合格");
                    break;
                case 40:missioncopy.setStatus_name("终结");
                    break;
                case 60:missioncopy.setStatus_name("返工（领班）");
                    break;
                case 70:missioncopy.setStatus_name("领班-不合格");
                    break;
                case 80:missioncopy.setStatus_name("业务-不合格");
                    break;
                case 90:missioncopy.setStatus_name("业务-合格");
                    break;
            }
            //5,任務發配時間直接有 create_time
            //6,完成時間 可空 null
            //7,阿姨名称 存进去
            Integer employee_idcopy = missioncopy.getAccept_employee_id();
            String employeenamebyidcopy = employeeService.getEmployeenamebyid(employee_idcopy);
            missioncopy.setAccept_employee_name(employeenamebyidcopy);
        }


        //查出该条件下有多少个数量
        Integer count = missionService.getCountBymission(mission);
        List<String> title = new ArrayList<String>();
        title.add("房间号");
        title.add("任务ID");
        title.add("任务类型");
        title.add("完成状态");
        title.add("任务发布时间");
        title.add("任务完成时间");
        title.add("阿姨名称");
        MissionHistoryCreateExcel(request, response, missionlist, "任务历史列表导出Excel", title);
        return null;
    }

    public void MissionHistoryCreateExcel(HttpServletRequest request, HttpServletResponse response,
                            List<Mission> missionlist, String fileName, List<String> title) {
        try {
// 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle style = workbook.createCellStyle();
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            // 字体增粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = workbook.createSheet("sheet1");
            // 创建Excel的sheet的一行 (表头)
            HSSFRow row = sheet.createRow(0);
            // 表头内容填充
            for (int i = 0; i < title.size(); i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 5000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title.get(i));
                cell.setCellStyle(style);
            }
            // 创建内容行
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);// 自动换行
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
            for (int j = 0; j < missionlist.size(); j++) {
                HSSFRow contentRow = sheet.createRow(j + 1);
                for (int k = 0; k < title.size(); k++) {
                    HSSFCell cell = contentRow.createCell(k);
                    switch (k) {
                        case 0:
                            if (missionlist.get(j).getRoom_name() != null) {// 第一列房间号
                                cell.setCellValue(missionlist.get(j).getRoom_name());
                            }
                            break;
                        case 1:
                            if (missionlist.get(j).getId() != null) {//第二列任务
                                cell.setCellValue( missionlist.get(j).getId());
                            }
                            break;
                        case 2:
                            if (missionlist.get(j).getMission_type_name() != null) {//第三列任务类型
                                cell.setCellValue(missionlist.get(j).getMission_type_name());
                            }
                            break;
                        case 3:
                            if (missionlist.get(j).getStatus_name() != null) {//第四列完成状态
                                cell.setCellValue(missionlist.get(j).getStatus_name());
                            }
                            break;
                        case 4:
                            if (missionlist.get(j).getCreate_time() != null) {//第五列任务发布时间
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                cell.setCellValue(df.format(missionlist.get(j).getCreate_time()));
                            }
                            break;
                        case 5:
                            if (missionlist.get(j).getEnd_time() != null) {//第六列任务完成时间
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                cell.setCellValue(df.format(missionlist.get(j).getEnd_time()));
                            }
                            break;
                        case 6:
                            if (missionlist.get(j).getAccept_employee_name() != null) {//第七列阿姨名称
                                cell.setCellValue(missionlist.get(j).getAccept_employee_name());
                            }
                            break;
                        default:
                            break;
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}