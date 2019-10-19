package com.aaroom.service;

import com.aaroom.beans.HotelEmployee;
import com.aaroom.persistence.EmployeeMapper;
import com.aaroom.persistence.HotelEmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sosoda on 2018/10/22.
 */
@Service
public class HotelEmployeeService {

    @Autowired
    private HotelEmployeeMapper hotelEmployeeMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public HotelEmployee getHotelEmployeeByEmployeeId(Integer employee_id, Integer type) {
        return hotelEmployeeMapper.getHotelEmployeeByEmployeeId(employee_id, type);
    }

    //根据员工id查所属就酒店id (只查酒店id 对象中只有酒店id 没有其他不必要的*)【张赢】
    public HotelEmployee getHotelEmployeeByEmployeeIdSoloParam(Integer employee_id) {
        return hotelEmployeeMapper.getHotelEmployeeByEmployeeIdSoloParam(employee_id);
    }

    //根据酒店id找到对应酒店下的所有员工的中文名字【张赢】
    public List getemployeenamebyloginid(String hotel_id)throws Exception{
        return hotelEmployeeMapper.getemployeenamebyloginid(hotel_id);
    }

    //fast二用根据酒店id找到对应酒店下的所有员工的id +中文名字 弄一个map集合返回去【张赢】
    public Map getEmployeeNameIdbyloginid(int hotel_id)throws Exception{
        Map empmap=new HashMap();
        List<Integer> empIdList = hotelEmployeeMapper.getEmployeeNameIdbyloginid(hotel_id);
        for (Integer empId:empIdList
             ) {
            String empname = employeeMapper.getEmployeenamebyid(empId);
            empmap.put(empId,empname);
        }
        return empmap;
    }

    //根据酒店id找到对应酒店下的所有员工的中文名字(正在上班中的)【张赢】
    public List getemloyeeinworking(int hotel_id){
        return hotelEmployeeMapper.getemloyeeinworking(hotel_id);
    }

    //根据酒店id找到对应酒店下的所有员工的中文名字(所有的 不管上不上班)【张赢】
    public List GetEmloyee(int hotel_id)throws Exception{
        return hotelEmployeeMapper.GetEmloyee(hotel_id);
    }

    public HotelEmployee exist(HotelEmployee hotelEmployee) {
        return hotelEmployeeMapper.exist(hotelEmployee);
    }

    public void insertOrUpdateHotelEmployee(HotelEmployee hotelEmployee) {
        HotelEmployee temp = this.exist(hotelEmployee);
        if(temp!=null){
            hotelEmployeeMapper.updateByPrimaryKeySelective(hotelEmployee);
        }else {
            hotelEmployeeMapper.insertSelective(hotelEmployee);
        }
    }

    public void deleteHotelEmployee(String hotel_id, String employee_id) {
        hotelEmployeeMapper.deleteByPrimaryKey(hotel_id, employee_id);
    }

    public List<HotelEmployee> getHotelEmployeesByHotelId(int hotel_id) {
        return hotelEmployeeMapper.getHotelEmployeesByHotelId(hotel_id);
    }

    public HotelEmployee getHotelIdByEmpId(Integer employee_id){
        return hotelEmployeeMapper.getHotelIdByEmpId(employee_id);
    }

    public List<HotelEmployee> getHotelIdsByEmpId(Integer employee_id){
        return hotelEmployeeMapper.getHotelIdsByEmpId(employee_id);
    }

}
