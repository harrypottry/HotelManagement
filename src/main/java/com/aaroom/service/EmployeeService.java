package com.aaroom.service;

import com.aaroom.beans.Employee;
import com.aaroom.beans.EmployeeView;
import com.aaroom.beans.HotelEmployee;
import com.aaroom.persistence.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sosoda on 2018/10/22.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    public void insertOrUpdateEmployee(Employee employee) {
        if(employee.getId()!=null && getEmployeeByPhone(employee.getPhone())!=null) {
            employeeMapper.updateByPrimaryKeySelective(employee);
        }else {
            employeeMapper.insertSelective(employee);
        }
    }

    public EmployeeView getEmployeeByPhone(String phone) {
        return employeeMapper.getEmployeeByPhone(phone);
    }
        //根据电话号吗查询是否有重复的
    public Employee getEmployeeByPhoneToCheck(String phone) {
        return employeeMapper.getEmployeeByPhoneToCheck(phone);
    }

    public List<EmployeeView> getEmployeeViewListByHotelId(String hotel_id) {
        return employeeMapper.getEmployeeViewListByHotelId(hotel_id);
    }
    //两表联查 根据hotelid查到对应的洗衣厂emp对象
    public List<Employee> getEmployeeWashFactoryListByHotelId(Integer hotel_id) {
        return employeeMapper.getEmployeeWashFactoryListByHotelId(hotel_id);
    }

    public Employee auth(String phone, String password) {
        return employeeMapper.auth(phone, password);
    }

    public Employee getEmployee(Integer id) {
        return employeeMapper.selectByPrimaryKey(id);
    }

    public Employee getempbyname(String name){
        return employeeMapper.getempbyname(name);
    }


    public Employee getEmployeeByOpenId(String openid) {
        return employeeMapper.getEmployeeByOpenId(openid);
    }

    @Transactional(rollbackFor = Exception.class)
    public void activeEmployee(Integer employee_id, int hotel_id, Integer role_id) {
        Employee employee = new Employee();
        employee.setId(employee_id);
        employee.setIs_active(new Byte("1"));
        employee.setRole_id(role_id);
        employeeMapper.updateByPrimaryKeySelective(employee);
        HotelEmployee hotelEmployee = new HotelEmployee();
        hotelEmployee.setEmployee_id(employee_id);
        hotelEmployee.setHotel_id(hotel_id);
        hotelEmployeeService.insertOrUpdateHotelEmployee(hotelEmployee);
    }

    //根据
    public String getEmployeenamebyid(Integer id){
       return employeeMapper.getEmployeenamebyid(id);
    }
    //更改阿姨上班状态 0为下班 1为上班
    public Integer workingOnOff(Integer working,Integer employee_id){
       return employeeMapper.workingOnOff(working,employee_id);
    }

    public List getWashFactory() {
        return employeeMapper.getWashFactory();
    }
}
