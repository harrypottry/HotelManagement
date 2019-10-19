package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.persistence.ClothLogMapper;
import com.aaroom.persistence.ClothMapper;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ClothLogService {

    @Autowired
    private ClothLogMapper clothLogMapper;

    @Autowired
    private ClothMapper clothMapper;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private ClothService clothService;


/************************************************* 一统Log江山 【张赢】 Begin
  ClothLog 统一Service 开启事务增加新两条log日志 先出后入  以后都用这个Service操作
 调用时 参数：出的type 和id 入的type 和id   然后选择性加入mission_id         direction状态0为出 1为入
 */
    @Transactional
    //@EmployeeOperator
    public void InsertDoubleClothLog(Integer hotel_id,Integer cloth_id,Integer mission_id,
                                     Integer OutType,Integer OutId,Integer InType,Integer InId,Integer status){
        //封装对象 先出
        ClothLog clothLogOut=new ClothLog(cloth_id,hotel_id,mission_id,0,OutId,OutType,status);
        insertSelective(clothLogOut);
        //封装对象 后入
        ClothLog clothLogIn=new ClothLog(cloth_id,hotel_id,mission_id,1,InId,InType,status);
        insertSelective(clothLogIn);
    }
    /************************************************* 一统Log江山 【张赢】 End
     * 插入Log官方指定 唯一使用service 就是下面这个 insertSelective  入参对象 带判空 增加新log日志一条【张赢】
     */
    //@EmployeeOperator
    @Transactional
    public Integer insertSelective(ClothLog record){
        ClothLog clothLog = clothLogMapper.setUUID(record.getCloth_id());
        //处理布草首次进入酒店以及同一布草多次进入酒店
        if (clothLog == null || (record.getPossessor_type() == 2 && record.getDirection() == 1)) {
            record.setUuid(CommonUtil.createUUID());
        } else {
            record.setUuid(clothLog.getUuid());
        }
        /*if (clothLog == null || (clothLog.getPossessor_type() == 3 && clothLog.getDirection() == 0)){
            record.setUuid(CommonUtil.createUUID());
        } else {
            record.setUuid(clothLog.getUuid());
        }*/
        return clothLogMapper.insertSelective(record);
    }
    //根据酒店id与布草id得到clothlog对象
    public List<ClothLog> getClothLogByClothId(Integer cloth_id,int hotel_id){
        return  clothLogMapper.getClothLogByClothId(cloth_id,hotel_id);
    }
    //查询当前月（当前月1月0点开始到现在的所有log对象）
    public List<ClothLog> getClothLogListByCurrentMonth(Date MonthFirstTime,Date CurrentTime,Integer hotel_id,Integer direction,Integer possessor_type){
        return  clothLogMapper.getClothLogListByCurrentMonth(MonthFirstTime,CurrentTime,hotel_id ,direction,possessor_type);
    }
    //查询当前日（当前日所有log对象）模糊查询
    public List<ClothLog> getClothLogListByDay(String DayTime,Integer hotel_id,Integer direction,Integer possessor_type){
        return  clothLogMapper.getClothLogListByDay(DayTime,hotel_id ,direction,possessor_type);
    }
    //根据酒店id与布草id得到这个id最新的一次clothlog对象
    public List<ClothLog> getClothLogByClothIdAndHotelId(Integer hotel_id,Integer cloth_id){
        return  clothLogMapper.getClothLogByClothIdAndHotelId(hotel_id,cloth_id);
    }
    //根据ClothId 查到最大的clothid 用以确定从后从到那个洗衣厂
    public List<ClothLog> getMaxClothLogByClothId(Integer cloth_id){
        return  clothLogMapper.getMaxClothLogByClothId(cloth_id);
    }
    //1.6app 根据 possessortype 和possessorid来找最新的在此位置的布草集合
    public List<ClothLog> getMaxClothLogByPossessor_typeAndPossessor_id(Integer possessor_id,Integer possessor_type){
        return  clothLogMapper.getMaxClothLogByPossessor_typeAndPossessor_id(possessor_id,possessor_type);
    }
    //根据missionId（旧）找到对应的不重复的clothidList【clothlog表】 【张赢】
    public List<Integer> getClothIdByClothId(Integer missionIdByHidRidS2){
        return  clothLogMapper.getClothIdByClothId(missionIdByHidRidS2);
    }

    //取判空条件(当前房间对应下没有任何log) 根据房间号查看这个房间有多少log <list>【张赢】
    public List getclothlogByroomId(ClothLog clothLog){
        return  clothLogMapper.getclothlogByroomId(clothLog);
    }
    //插入一条日志
    public Integer insert(ClothLog record){
        return  clothLogMapper.insert(record);
    }


    //1.0版本用查最新布草
    public List<ClothLog> getLogListByEmpId(int hotel_id, Integer possessor_id,Integer possessor_type,Integer status){
        return clothLogMapper.getLogListByEmpId(hotel_id, possessor_id,possessor_type,status);
    }
    //根据唯一missid查找到clothlog对象
    public List<ClothLog> getLogListByMissionId(Integer MissionId){
        return clothLogMapper.getLogListByMissionId(MissionId);
    }
    //2.0版本用查最新布草
    public List<ClothLog> GetLogListNearBy(int hotel_id,Integer direction ,Integer possessor_id,Integer possessor_type){
        return clothLogMapper.GetLogListNearBy(hotel_id,direction, possessor_id,possessor_type);
    }
    //保洁端重置扫一扫布置干净布草 删除log 第一部分有missionid的 房间入干净
    public Integer deleteLogById(Integer Id){
        return clothLogMapper.deleteLogById(Id);
    }
    //保洁端重置扫一扫布置干净布草 删除log 第二部分clothlog对象的 保洁员出
    public Integer deleteLogByClothlog(ClothLog clothLog){
        return clothLogMapper.deleteLogByClothlog(clothLog);
    }
    //查看当前酒店内所有布草的数量【张赢】 possessor_type是0,1,2的
    public Integer getCountByhotelId(int hotel_id){
        return clothLogMapper.getCountByhotelId(hotel_id);
    }
    //得到当前酒店下 今日已经送去洗衣厂的布草总数 和 洗回来的是一个service【张赢】
    public Integer getCountByhotelIdAndWashFactory(int hotel_id,
                                                   Integer possessor_type,
                                                   Integer direction,
                                                   Date starttime,
                                                   Date endtime){
        return clothLogMapper.getCountByhotelIdAndWashFactory(hotel_id,possessor_type,direction,starttime,endtime);
    }
    //根据具体任务id给出具体新旧布草id与详情
    public Map<String, List> getLogListByMissionidPossessorTypeidDirectionid(Integer missionid){

        //因为都是int型 所以存到对象中
        ClothLog oldNeedParam=new ClothLog();
        oldNeedParam.setMission_id(missionid);
        oldNeedParam.setPossessor_type(1);
        oldNeedParam.setDirection(1);
        oldNeedParam.setStatus(1);
        ClothLog newNeedParam=new ClothLog();
        newNeedParam.setMission_id(missionid);
        newNeedParam.setPossessor_type(0);
        newNeedParam.setDirection(1);
        newNeedParam.setStatus(0);
        //两个参数分别去找出对应的显示map集合
        List<Integer> OldClothlist= clothLogMapper.getLogListByMissionidPossessorTypeidDirectionid(oldNeedParam);
        List<Integer> NewClothlist= clothLogMapper.getLogListByMissionidPossessorTypeidDirectionid(newNeedParam);
        //放到map中返回
        Map<String,List> newoldmap=new HashMap();
        newoldmap.put("OldClothlist",OldClothlist);
        newoldmap.put("NewClothlist",NewClothlist);
        return newoldmap;
    }

    //入参对象 开启事务增加新两条log日志 阿姨入一条 库房出一条 【张赢】带计算数量的
    @Transactional
    public Integer savedoublelog(Integer cloth_id,String employee_id,int hotel_id){
        try {
            clothLogMapper.saveCleanerlog(cloth_id,employee_id,hotel_id);
            clothLogMapper.saveStorelog(cloth_id,hotel_id,hotel_id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
    //void入参对象 开启事务增加新两条log日志 阿姨入一条 库房出一条 【张赢】-----------------------【出库操作】
    @Transactional
    public void savedoublelogvoid(Integer cloth_id,String employee_id,int hotel_id){
        clothLogMapper.saveStorelog(cloth_id,hotel_id,hotel_id);
        clothLogMapper.saveCleanerlog(cloth_id,employee_id,hotel_id);
    }

    //void入参对象 开启事务增加新两条log日志 阿姨出一条 库房入一条 【张赢】++++++++++++++++++【入库操作】
    @Transactional
    public void saveOutDoubleLog(Integer cloth_id,String employee_id,int hotel_id){
            clothLogMapper.saveDirtyEmpOutlog(cloth_id,employee_id,hotel_id);
            clothLogMapper.saveDirtyStoreInlog(cloth_id,hotel_id,hotel_id);
    }
    //根据酒店id 根据出入状态 根据传值进来的possessor_type 数字 查出所有新的log 瞬时状态在谁手上 【分页专用】张赢
    @Transactional
    public List<ClothLog> getClothLogListBypossessor_type_comment(int hotel_id ,Integer possessor_type,Integer comment,Integer index,Integer pageSize){
           return clothLogMapper.getClothLogListBypossessor_type_comment(hotel_id,possessor_type,comment, index, pageSize);
    }
    //分页查询库存布草 根据1，布草名称 2，布草规格 3，洁净程度 4，位置
    @Transactional
    public List<ClothLog> getClothLogListByPossessor_typeAndStutasAndTypeAndSize(int hotel_id ,Integer possessor_type,Integer status,Integer index,Integer pageSize){
           return clothLogMapper.getClothLogListByPossessor_typeAndStutasAndTypeAndSize(hotel_id,possessor_type,status, index, pageSize);
    }
    //不分页查询库存布草 根据1，布草名称 2，布草规格 3，洁净程度 4，位置 导出excel表用
    @Transactional
    public List<ClothLog> getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimitDownExcel(int hotel_id ,Integer possessor_type,Integer status){
           return clothLogMapper.getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimitDownExcel(hotel_id,possessor_type,status);
    }
    @Transactional
    public Integer getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimit(int hotel_id ,Integer possessor_type,Integer status){
           return clothLogMapper.getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimit(hotel_id,possessor_type,status);
    }
    //根据酒店id 房间id 查log表 得出最新的log状态
    public List<ClothLog> getClothLogListByHotelidRoomid(int hotel_id ,Integer possessor_type,Integer room_id){
           return clothLogMapper.getClothLogListByHotelidRoomid(hotel_id,possessor_type,room_id);
    }
    //根据酒店id 根据出入状态 根据传值进来的possessor_type 数字 查出所有新的log 瞬时状态在谁手上 【不分页 导出专用】张赢
    @Transactional
    public List<ClothLog> downgetClothLogList(int hotel_id ,Integer possessor_type,Integer comment){
           return clothLogMapper.downgetClothLogList(hotel_id,possessor_type,comment);
    }
    //根据酒店id 根据出入状态 根据传值进来的possessor_type 数字 查出所有新的log 瞬时状态在谁手上 【算数专用】张赢
    @Transactional
    public Integer downgetClothLogListcount(int hotel_id ,Integer possessor_type,Integer comment){
           return clothLogMapper.downgetClothLogListcount(hotel_id,possessor_type,comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public void savedoublelogs(List<Integer> cloth_ids,String employee_id,int hotel_id) {
        for (Integer cloth_id: cloth_ids) {
            savedoublelog(cloth_id, employee_id, hotel_id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
        public void conventClothStatus(Integer mission_id,Integer cloth_id,Integer room_id,int hotel_id,Integer empId){
        //2.插入4条日志
        ClothLog clothLog;
        /*
         * 房间脏出
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,null,0,room_id,0,1);
        insertSelective(clothLog);
        /*
         * 保洁脏入
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,null,1,empId,1,1);
        insertSelective(clothLog);
        /*
         * 保洁干净出
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,null,0,empId,1,0);
        insertSelective(clothLog);
        /*
         * 房间干净入（需绑定任务）
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,mission_id,1,room_id,0,0);
        insertSelective(clothLog);

        //将此布草状态由脏变为干净
        clothMapper.updateStatusByclothId(cloth_id,0);
    }

    @Transactional(rollbackFor = Exception.class)
    public void nomarlInsertLog(Integer mission_id,Integer cloth_id,Integer room_id,int hotel_id,Integer empId) {
        ClothLog clothLog;
        /*
         * 保洁干净出
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,null,0,empId,1,0);
        insertSelective(clothLog);
        /*
         * 房间干净入
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,mission_id,1,room_id,0,0);
        insertSelective(clothLog);
    }


    @Transactional(rollbackFor = Exception.class)
    public void highInsertLog(Integer cloth_id,Integer room_id,int hotel_id,Integer empId) {
        ClothLog clothLog = new ClothLog();
        /*
         * 房间脏出
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,null,0,room_id,0,1);
        insertSelective(clothLog);
        /*
         * 保洁脏入
         * */
        clothLog = new ClothLog(cloth_id,hotel_id,null,1,empId,1,1);
        insertSelective(clothLog);
    }



    public  List<ClothLog> getClothLogListByClean(int hotel_id,Integer mission_id,Integer possessor_id){
        return clothLogMapper.getClothLogListByClean(hotel_id, mission_id, possessor_id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void initHotelCloth(Integer[] cloth_ids, int hotel_id) {

        for (Integer cloth_id: cloth_ids) {
            ClothLog clothLog = new ClothLog(cloth_id,hotel_id,null,1,hotel_id,2,0);
            insertSelective(clothLog);
        }
    }

    @Transactional(rollbackFor = Exception.class)

    public void sendToLaundry(Integer[] cloth_ids, int hotel_id) {

        //查找当前酒店对应的洗衣厂id
        List<HotelEmployee> hotelEmployees = hotelEmployeeService.getHotelEmployeesByHotelId(hotel_id);
        Integer to_id = null;
        for (HotelEmployee hotelEmployee: hotelEmployees) {
            Employee employee = employeeService.getEmployee(hotelEmployee.getEmployee_id());
            if(employee.getRole_id()==7) {
                to_id = employee.getId();
            }
        }

        insertPairLogs(cloth_ids, hotel_id, hotel_id,2,1,to_id,3,1);
    }


    public void receiveFromLaundry(Integer[] cloth_ids, int hotel_id) {

        //查找当前酒店对应的洗衣厂id
        List<HotelEmployee> hotelEmployees = hotelEmployeeService.getHotelEmployeesByHotelId(hotel_id);
        Integer from_id = null;
        for (HotelEmployee hotelEmployee: hotelEmployees) {
            Employee employee = employeeService.getEmployee(hotelEmployee.getEmployee_id());
            if(employee.getRole_id()==7) {
                from_id = employee.getId();
            }
        }
        for (Integer cloth_id:cloth_ids) {
            Cloth cloth = clothService.getClothByid(cloth_id);
            cloth.setRecycle_num(cloth.getRecycle_num()+1);
            clothService.update(cloth);
        }
        insertPairLogs(cloth_ids, hotel_id,from_id,3,0,hotel_id,2,0);
    }

    private void insertPairLogs(Integer[] cloth_ids, int hotel_id, int from_id, Integer from_type, Integer from_status, int to_id, Integer to_type,  Integer to_status) {
        for (Integer cloth_id: cloth_ids){
            insertPairLog(cloth_id, hotel_id,from_id,from_type,from_status,to_id,to_type,to_status);
        }
    }

    private void insertPairLog(Integer cloth_id, int hotel_id, int from_id, Integer from_type, Integer from_status, int to_id, Integer to_type, Integer to_status) {
        ClothLog outClothLog = new ClothLog(cloth_id,hotel_id,null,0,from_id,from_type,from_status);
        insertSelective(outClothLog);
        ClothLog inClothLog = new ClothLog(cloth_id,hotel_id,null,1,to_id,to_type,to_status);
        insertSelective(inClothLog);
    }

    /**
     *
     * 统一处理redis热点布草
     * @param cloth_id
     * @return
     */
    public String getClothIdsByRedis(Integer cloth_id){
        String str = new String();

        String ids = redisCacheService.hget(Constants.CLOTH + cloth_id, Constants.CLOTHTYPEIDS, String.class);
        //Redis没有缓存，查询出对应此布草的属性
        if (ids==null||ids.length()==0){
            List<Integer> idsByClothId = relClothTypeService.getIdsByClothId(cloth_id,1);
            if (!idsByClothId.isEmpty()){
                // [1,2,3]-->"1,2,3"
                for (Integer id:idsByClothId){
                    //1,2,3,
                    str += id+",";
                }
                ids = str.substring(0, str.lastIndexOf(","));
                redisCacheService.hset(Constants.CLOTH+cloth_id,Constants.CLOTHTYPEIDS,ids);
            }else {
                ids = null;
            }
        }

        return ids;
}

    /**
     * 从cloth-log中获取ids-脏-干净-总和
     */
    public Map getClothViewMapDetail(List<ClothLog> logList){
        Map<String, Map<String, Object>> ret = new HashMap<>();
        for (ClothLog clothLog:logList){
        ClothView clothView = clothService.getClothView(clothLog.getCloth_id());
        Map<String, Object> clothMap;
        if (clothView.getClothTypeIds() == null) {
            break;
        } else if (ret.keySet().contains(clothView.getClothTypeIds())) {
            clothMap = ret.get(clothView.getClothTypeIds());
        } else {
            clothMap = new LinkedHashMap<>();
            clothMap.put("clothTypes", clothView.getClothTypes());
        }
        if (clothView.getStatus() == 1) {
            clothMap.put("dirty", clothMap.keySet().contains("dirty") ? (Integer) clothMap.get("dirty") + 1 : 1);
        } else {
            clothMap.put("clean", clothMap.keySet().contains("clean") ? (Integer) clothMap.get("clean") + 1 : 1);
        }
        if (clothMap.get("clean")==null){
            clothMap.put("clean", 0);
        }else if(clothMap.get("dirty")==null){
            clothMap.put("dirty", 0);
        }
        clothMap.put("all", clothMap.keySet().contains("all") ? (Integer) clothMap.get("all") + 1 : 1);
            ret.put(clothView.getClothTypeIds(), clothMap);
        }
        return ret;
    }


    //计算本月洗涤租赁信息 累加时间 和 数量
    public Map getClothViewMapIncludeTime(List<ClothLog> logList){
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (ClothLog clothLog:logList){
            ClothView clothView = clothService.getClothView(clothLog.getCloth_id());
            Map<String, Object> clothMap;

            if (clothView.getClothTypeIds() == null) {
                break;
            } else if (result.keySet().contains(clothView.getClothTypeIds())) {
                clothMap = result.get(clothView.getClothTypeIds());
            } else {
                clothMap = new HashMap<>();
                clothMap.put("clothTypes", clothView.getClothTypes());
            }

            clothMap.put("all", clothMap.keySet().contains("all") ? (Integer) clothMap.get("all") + 1 : 1);
            Integer cloth_id = clothLog.getCloth_id();//这条clothid
            Date create_time_infactory = clothLog.getCreate_time();//这条cloth这次进洗衣厂的时间
            Integer hotel_id = clothLog.getHotel_id();

//求出这个cloth离这个【进洗衣厂时间】最近的一次（order by排序 降序DESC 的第一个log）【进酒店的时间】的差 就是停留时间毫秒数
            List<ClothLog> clothLogListDuringHotel = clothLogMapper.getClothLogListDuringHotel(create_time_infactory,
                    hotel_id,cloth_id, 1, 0,2);

           // Integer spendTimeDay=0;
            Double spendTimeDay=0.00;
            //Date create_time_inhotel;//初始化最近一次进酒店的时间
            //循环第一个就是这个cloth进酒店的那个log
            for (int i = 0; i <clothLogListDuringHotel.size() ; i++) {
                Date create_time_inhotel = clothLogListDuringHotel.get(0).getCreate_time();
                //两时间相减是停留时间
                spendTimeDay= (double)(create_time_infactory.getTime() - create_time_inhotel.getTime())/(1000*3600*24);
                break;

            }

            //根据map特性 key键相同 value覆盖，key键不同，就新putvalue值 计算求和总数 和 时间总和
            clothMap.put("SUMspendTimeDay",clothMap.keySet().contains("SUMspendTimeDay")?(Double)clothMap.get("SUMspendTimeDay")+ spendTimeDay :spendTimeDay);

            result.put(clothView.getClothTypeIds(), clothMap);
        }
        //循环遍历map集合强转SUMspendTimeDay 为int
        for (String key:
        result.keySet()) {
            Map<String, Object> cloth_Map_time = result.get(key);
            for (String keycloth:
            cloth_Map_time.keySet()) {
                if(keycloth.equals("SUMspendTimeDay")){
                    Object suMspendTimeDay = cloth_Map_time.get("SUMspendTimeDay");
                    cloth_Map_time.put("SUMspendTimeDay",Integer.parseInt(new java.text.DecimalFormat("0").format(suMspendTimeDay)));
                }
            }



        }
        return result;
    }


    public List<ClothLog> getClothLogListDuringHotel(Date create_time_infactory, Integer hotel_id,Integer cloth_id, Integer direction,
                                                     Integer status,
                                                     Integer possessor_type){
        return clothLogMapper.getClothLogListDuringHotel(create_time_infactory, hotel_id,cloth_id,direction,status,possessor_type);
    }
}
