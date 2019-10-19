package com.aaroom.persistence;
import com.aaroom.beans.Mission;
import com.aaroom.beans.MissionType;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MissionTypeMapper {

	MissionType getById(@Param(value = "id") Long id);

	MissionType getMissionTypeById(@Param(value = "id") Integer id);

	List<MissionType> getListByMap(Map<String, Object> param)throws Exception;

	Integer getCountByMap(Map<String, Object> param)throws Exception;

	Integer save(MissionType missionType)throws Exception;

	Integer modify(MissionType missionType)throws Exception;

	Integer removeById(@Param(value = "id") Long id)throws Exception;
	//获得missiontype
	List<MissionType> getMissionType()throws Exception;

	//获得missiontype对象 根据desc
	MissionType getmissiontypebydesc(String desc)throws Exception;

	List<Mission> getEmpClothNumDone(@Param(value = "accept_employee_id") Integer accept_employee_id, @Param("hotel_id") String hotel_id);

	List<HashMap<String,Object>> getEmpCleanNum(@Param(value = "accept_employee_id") String accept_employee_id);

	List<HashMap<String,Object>> getEmpDirNum(@Param(value = "accept_employee_id") String accept_employee_id);

	//根据typeid获得中文desc
	String getdescbyid(@Param(value = "id") Integer id);

	List<HashMap<String,Object>> showHistoryMissionByEmp(@Param(value = "accept_employee_id") Integer accept_employee_id,
										  @Param("hotel_id") int hotel_id,
										  @Param(value = "index") Integer index,
										  @Param(value = "pageSize") Integer pageSize);

	Integer showHistoryMissionByEmpNum(@Param(value = "accept_employee_id") Integer accept_employee_id,
														 @Param("hotel_id") int hotel_id);

	Integer getMissionOldZjy( @Param("hotel_id") int hotel_id,
						   @Param("room_id")   Integer room_id,
						   @Param("status")   Integer[] status);
}
