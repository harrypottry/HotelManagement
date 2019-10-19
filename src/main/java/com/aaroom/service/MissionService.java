package com.aaroom.service;
import com.aaroom.beans.Mission;
import com.aaroom.persistence.MissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("missionService")
public class MissionService{

    @Resource
    private MissionMapper missionMapper;

    public Mission selectByPrimaryKey(Integer id){
        return missionMapper.selectByPrimaryKey(id);
    }

    public Integer updateByPrimaryKeySelective(Mission mission){
        return missionMapper.updateByPrimaryKeySelective(mission);
    }

    public Mission getById(Long id){
        return missionMapper.getById(id);
    }
    public List<Mission> getListByMap(Map<String,Object> param)throws Exception{
        return missionMapper.getListByMap(param);
    }

    public Mission getMissionById(Integer id){
        return missionMapper.getMissionById(id);
    }
    public Integer getCountByMap(Map<String,Object> param)throws Exception{
        return missionMapper.getCountByMap(param);
    }
        //根据入参对象查有多少条记录 【张赢】
    public Integer getCountBymission(Mission misson)throws Exception{
        return missionMapper.getCountBymission(misson);
    }

    public Integer save(Mission mission){
            return missionMapper.save(mission);
    }

    public Integer modify(Mission mission)throws Exception{
        return missionMapper.modify(mission);
    }

    public Integer removeById(Long id)throws Exception{
        return missionMapper.removeById(id);
    }


    public  List<HashMap> getMissionDoneByTime(Integer employeeid){
        return  missionMapper.getMissionDoneByTime(employeeid);
    }
    //根据员工id得到所有未完成的任务列表 包含状态 0发出和状态1工作中的 【张赢】
    public  List<HashMap> getMissionRoomtypeListByemployee_id(Integer employeeid){
        return  missionMapper.getMissionRoomtypeListByemployee_id(employeeid);
    }
    public List<HashMap<String,Object>> getMissionByRoomIdZjy(Integer employeeid){
        return missionMapper.getMissionByRoomIdZjy(employeeid);
    }

    public List<Integer> getRoomIdByEmpIdAndHotelId(Integer accept_employee_id, String hotel_id){
        return missionMapper.getRoomIdByEmpIdAndHotelId(accept_employee_id,hotel_id);
    }
    //分页查询missionlist
    public List<Mission> getListByfastmissionlimit(Mission mission, Integer index, Integer pageSize)throws Exception{
        return missionMapper.getListByfastmissionlimit(mission,index,pageSize);
    }
    //【frontDesk】不分页查询missionlist 用于导出表格 todaymissionlist
    public List<Mission> getListByMissionNoLimitToday(Mission mission)throws Exception{
        return missionMapper.getListByMissionNoLimitToday(mission);
    }
    //分页查询missionlist app端适配 根据员工id查当前员工任务
    public List<Mission> getMissionListByEmpIdLimit(Integer accept_employee_id,Integer index,Integer pageSize)throws Exception{
        return missionMapper.getMissionListByEmpIdLimit(accept_employee_id,index,pageSize);
    }
    //分页查询missionlist app端适配 当前没有 accept_employee_id 为null的misslist
    public List<Mission> getMissionListByAcceptemployeeidnullLimit(Integer index,Integer pageSize)throws Exception{
        return missionMapper.getMissionListByAcceptemployeeidnullLimit(index,pageSize);
    }
    //不不不不分页查询missionlist app端适配 当前没有 accept_employee_id 为null的misslist
    public List<Mission> getMissionListByAcceptemployeeidnull()throws Exception{
        return missionMapper.getMissionListByAcceptemployeeidnullLimit(null,null);
    }
    //不分页查询missionlist 用于导出表格
    public List<Mission> getListByfastmission(Mission mission)throws Exception{
        return missionMapper.getListByfastmission(mission);
    }
    //查询保洁员身上有没有未完成的任务 根据入参hotelid与empid 查出status list中有没有2
    public List<Integer> getListstatusbyhotelidandempid(Integer employee_id,int hotel_id)throws Exception{
        return missionMapper.getListstatusbyhotelidandempid(employee_id,hotel_id);
    }

    //根据hotelid与状态status码 得到mission对象list【张赢】
    public List<Mission> getmissionbyhotelidandstatus(int hotel_id,Integer status)throws Exception{
        return missionMapper.getmissionbyhotelidandstatus(hotel_id,status);
    }

    //根据 入参1：hotelid 2:roomid 3:房间状态码status=2 完成状态 找到 时间最新的一个missionid【张赢】
    public Integer getMissionIdByHidRidS2(int hotel_id,String possessor_id,Integer status)throws Exception{
        return missionMapper.getMissionIdByHidRidS2(hotel_id,possessor_id,status);
    }
    //查询任务有几种状态给前台三种
    public Map getStatusList()throws Exception{
        Map statusmap=new HashMap();
        List<Integer> statusList = missionMapper.getStatusList();
        for (Integer status:statusList
             ) {
            if(status ==0 ){
                statusmap.put(0,"发布");
            }else if(status ==1){
                statusmap.put(1,"工作中");
            } else if(status ==2){
                statusmap.put(2,"已完成");
            }else if(status ==3){
                statusmap.put(3,"任务出错");
            }
        }
        return statusmap;
    }

    public Integer isActiveMission(Integer id){
        return missionMapper.isActiveMission(id);
    }

    public List<Mission> showHistoryMissionByEmp(Integer accept_employee_id, String hotel_id){
        return  missionMapper.showHistoryMissionByEmp(hotel_id, accept_employee_id);
    }
        //根据员工id得到所有该员工对应的所有未完成的任务list 【张赢】

    //修改mission状态
    public void updateMissionStatus(Integer status,Integer id){
        missionMapper.updateMissionStatus(status,id);
    }

    public List<Mission> getMissionsByRoomId(Integer room_id, Integer[] status, Integer limit) {
        return missionMapper.getMissionsByRoomId(room_id, status, limit);
    }

    public List<Mission> getMissionsByEmployeeId(Integer employee_id, Integer[] status) {
        return missionMapper.getMissionsByEmployeeId( employee_id, status);
    }
//查询本月账单 和 上月账单 带between and 开始与 结束时间  1.6 APP 保洁员 我的账单
    public List<Mission> getMissionsByEmployeeIdAndTime(Integer employee_id, Integer[] status, Date StartTime, Date EndTime) {
        return missionMapper.getMissionsByEmployeeIdAndTime( employee_id, status,StartTime,EndTime);
    }

    public List<Mission> getMissionByMonthEmp(int hotel_id,String accept_employee_id){
        return  missionMapper.getMissionByMonthEmp(hotel_id, accept_employee_id);
    }
    public List<Mission> getMissionDetailByMonth(Integer accept_employee_id,int hotel_id,Integer[] status,Integer index,Integer pageSize){
        return  missionMapper.getMissionDetailByMonth(accept_employee_id,status,hotel_id,index,pageSize);
    }
    public List<Mission> getMissionDetailByDay(Integer accept_employee_id,int hotel_id,Integer[] status){
        return missionMapper.getMissionDetailByDay(accept_employee_id,hotel_id,status);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertInBatch(List<Mission> missions) {
        missions.forEach(this::save);
    }
}
