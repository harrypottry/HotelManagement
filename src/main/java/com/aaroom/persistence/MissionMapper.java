package com.aaroom.persistence;
import com.aaroom.beans.Mission;
import com.aaroom.beans.MissionPriceView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface MissionMapper {


    int deleteByPrimaryKey(Integer id);

    int insert(Mission record);

    int insertSelective(Mission record);

    Mission getMissionById(Integer id);

    Mission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Mission record);

    int updateByPrimaryKey(Mission record);

    Mission getById(@Param(value = "id") Long id);

	public List<Mission> getListByMap(Map<String, Object> param)throws Exception;

	public Integer getCountByMap(Map<String, Object> param)throws Exception;
	//根据入参对象查有多少条记录 【张赢】
	public Integer getCountBymission(@Param(value = "mission") Mission mission)throws Exception;

	public Integer save(Mission mission);

	public Integer modify(Mission mission)throws Exception;

	public Integer removeById(@Param(value = "id") Long id)throws Exception;

	//保洁完成任务列表与未完成任务列表
	List<HashMap> getMissionDoneByTime(Integer employeeid);

	//根据员工id得到所有未完成的任务列表 包含状态 0发出和状态1工作中的 【张赢】
	List<HashMap> getMissionRoomtypeListByemployee_id(@Param(value = "accept_employee_id")Integer employeeid);

	List<HashMap<String,Object>> getMissionByRoomIdZjy(Integer employeeid);

	//
	List<Integer> getRoomIdByEmpIdAndHotelId(@Param(value = "accept_employee_id") Integer accept_employee_id,@Param("hotel_id") String hotel_id);

	//根据fast选项得到对应mission对象 分页查询【张赢】
	List<Mission> getListByfastmissionlimit(@Param(value = "mission") Mission mission,
									   @Param(value = "index") Integer index,
									   @Param(value = "pageSize") Integer pageSize);
	//【frontDesk】不分页查询missionlist 用于导出表格 todaymissionlist
	List<Mission> getListByMissionNoLimitToday(@Param(value = "mission") Mission mission);

	//根据fast选项得到对应mission对象 分页查询【张赢】app端适配 根据员工id查当前员工任务
	List<Mission> getMissionListByEmpIdLimit(@Param(value = "accept_employee_id") Integer accept_employee_id,
									   @Param(value = "index") Integer index,
									   @Param(value = "pageSize") Integer pageSize);
	//页查询missionlist app端适配 当前没有 accept_employee_id 为null的misslist
	List<Mission> getMissionListByAcceptemployeeidnullLimit(@Param(value = "index") Integer index,
									   @Param(value = "pageSize") Integer pageSize);

	//根据fast选项得到对应mission对象不分页查询 用于导出表格【张赢】
	List<Mission> getListByfastmission(@Param(value = "mission") Mission mission);
	//查询保洁员身上有没有未完成的任务 根据入参hotelid与empid 查出status list中有没有2
	List<Integer> getListstatusbyhotelidandempid(@Param(value = "accept_employee_id") Integer employee_id,
												 @Param(value = "hotel_id") int hotel_id);

	//根据hotelid与status得到mission对象【张赢】
	List<Mission> getmissionbyhotelidandstatus(@Param(value = "hotel_id") int hotel_id,
													  @Param(value = "status") Integer status)throws Exception;

	//根据 入参1：hotelid 2:roomid 3:房间状态码status=2 完成状态 找到 时间最新的一个missionid 【张赢】
	public Integer getMissionIdByHidRidS2(@Param(value = "hotel_id") int hotel_id,
										 @Param(value = "room_id") String possessor_id,
										 @Param(value = "status") Integer status)throws Exception;

    //查询任务有几种状态【张赢】
    public List<Integer> getStatusList()throws Exception;

	Integer isActiveMission(Integer id);

	List<Mission> showHistoryMissionByEmp(@Param(value = "hotel_id") String hotel_id,@Param(value = "accept_employee_id") Integer accept_employee_id);

	//根据员工id得到所有该员工对应的所有未完成的任务list 【领取仓库布草】展示用一 【张赢】
	void updateMissionStatus(@Param(value = "status")Integer status,@Param(value = "id") Integer id);

    List<Mission> getMissionsByRoomId(@Param("room_id")Integer room_id,
									  @Param("status") Integer[] status,
									  @Param("limit") Integer limit);

	List<Mission> getMissionsByEmployeeId(@Param("employee_id") Integer employee_id, @Param("status")  Integer[] status);

	List<Mission> getMissionsByEmployeeIdAndTime(@Param("employee_id") Integer employee_id,
												 @Param("status")  Integer[] status,
												 @Param("StartTime") Date StartTime,
												 @Param("EndTime")  Date EndTime);

	List<Mission> getMissionByMonthEmp(@Param(value = "hotel_id") int hotel_id,@Param(value = "accept_employee_id") String accept_employee_id);


    List<Mission> getMissionDetailByMonth( @Param("accept_employee_id") Integer accept_employee_id,
										   @Param("status") Integer[] status,
										   @Param(value = "hotel_id") int hotel_id,
										   @Param(value = "index") Integer index,
										   @Param(value = "pageSize") Integer pageSize);

	List<Mission> getMissionDetailByDay(@Param("accept_employee_id") Integer accept_employee_id,
										@Param(value = "hotel_id") int hotel_id,
										@Param("status") Integer[] status);

	void insertOrUpdateMission(@Param(value = "id") Integer id,@Param(value = "comment")String comment);

    List<MissionPriceView> getMissionList( @Param(value = "employeeId")String employeeId,
										   @Param(value = "hotelId")String hotelId,
										  @Param(value = "beginDate")String beginDate,
										  @Param(value = "endDate")String endDate);

}
