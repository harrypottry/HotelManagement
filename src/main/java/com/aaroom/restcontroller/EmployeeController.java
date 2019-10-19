package com.aaroom.restcontroller;

import com.aaroom.beans.ClothLog;
import com.aaroom.beans.Employee;
import com.aaroom.beans.HotelEmployee;
import com.aaroom.exception.RestError;
import com.aaroom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by sosoda on 2018/11/15.
 */
@RestController
public class EmployeeController {

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private EmployeeService employeeService;


    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private MissionService missionService;

    @PostMapping("/console/api/boundEmployeeCleaner")
    public Object boundEmployeeCleaner(HttpServletRequest request,
                                       @RequestParam(value = "hotel_id") int hotel_id,
                                       @RequestParam(value = "employee_id") Integer employee_id,
                                       @RequestParam(value = "role_id") Integer role_id){
        //TODO:soda 之后根据权限决定是否检查
        //Integer employee_id = (Integer)request.getSession().getAttribute("employee_id");
        employeeService.activeEmployee(employee_id,hotel_id,role_id);
        return RestError.E_OK;
    }

    //阿姨上下班接口
    @RequestMapping(value = "/console/api/AtworkOnOff", method = RequestMethod.POST)
    public Object AtworkOnOff(HttpServletRequest request,
                              @RequestParam(value = "working") Integer working) throws Exception {

        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
       // 0.下班 1.上班
        if (working==1){
            employeeService.workingOnOff(working, (Integer) employee_id);
            return RestError.E_OK;
        }else if (working==0){
            //1.判断保洁员身上是否有未完成的任务,查mission表
            List<Integer> StatusList = missionService.getListstatusbyhotelidandempid((Integer) employee_id, hotel_id);
            //2.判断手里是否有剩余布草,在clothlog去查询此保洁员最新的布草
            List<ClothLog> clothLogList = clothLogService.getLogListByEmpId(hotel_id, (Integer)employee_id,1,null);
            if(StatusList.size()>0){
                return RestError.E_WORKING_MISSION;
            }else if (!clothLogList.isEmpty()) {
                return RestError.E_WORKING_OTHER;
            }else{
                employeeService.workingOnOff(working, (Integer) employee_id);
                return RestError.E_OK;
            }
        }
        return null;
    }

    //阿姨上下班接口 安卓端适配
    @RequestMapping(value = "/app/api/AtworkOnOffApp", method = RequestMethod.POST)
    public Object AtworkOnOffApp(HttpServletRequest request,
                              @RequestParam(value = "working") Integer working) throws Exception {

        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
        // 0.下班 1.上班
        if (working==1){
            employeeService.workingOnOff(working, (Integer) employee_id);
            return RestError.E_OK;
        }else if (working==0){
            //1.判断保洁员身上是否有未完成的任务,查mission表
            List<Integer> StatusList = missionService.getListstatusbyhotelidandempid((Integer) employee_id, hotel_id);
            //2.判断手里是否有剩余布草,在clothlog去查询此保洁员最新的布草
            List<ClothLog> clothLogList = clothLogService.getLogListByEmpId(hotel_id, (Integer)employee_id,1,null);
            if(StatusList.size()>0){
                return RestError.E_WORKING_MISSION;
            }else if (!clothLogList.isEmpty()) {
                return RestError.E_WORKING_OTHER;
            }else{
                Integer integer = employeeService.workingOnOff(working, (Integer) employee_id);
                return new RestError(integer);
            }
        }
        return null;
    }
    
    
    @GetMapping(value = "/wx/console/api/getWorkStatus")
    public Object getWorkStatus(HttpSession session){
        Integer employee_id = (Integer) session.getAttribute("employee_id");
        Employee employee=null;
        if(employee_id != null){
            employee = employeeService.getEmployee(employee_id);
            return new RestError(employee);
        }else {
            return RestError.E_AUTH_NEEDED;
        }

    }
}
