package com.aaroom.restcontroller;

import com.aaroom.beans.Employee;
import com.aaroom.beans.HotelBase;
import com.aaroom.beans.HotelEmployee;
import com.aaroom.beans.Permission;
import com.aaroom.exception.RestError;
import com.aaroom.service.*;
import com.aaroom.utils.SessionUtil;
import com.aaroom.wechat.Wechat;
import com.aaroom.wechat.WechatToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sosoda on 2018/10/27.
 */
@RestController
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private HotelBaseService hotelBaseService;

    @Autowired
    private Wechat wechat;

    @Autowired
    private ConfigurationService configurationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, String account, String password,
                        @RequestParam(value = "code", required = false) String code) {
        Employee user = employeeService.auth(account, password);
        Map<String, Object> ret = new HashMap<>();
        if (user != null) {
                // 商家\推广员 账号可用性检验
                if (user.getRole_id() == 10 || user.getRole_id() == 8){
                    if (user.getIs_active() == 0 || StringUtils.isBlank(user.getRole_id().toString())){
                    return RestError.E_AUTH_FAILED;
                    }else {
                        //获取尚美酒店id
                        List<HotelEmployee> HotelEmployeeList = hotelEmployeeService.getHotelIdsByEmpId(user.getId());//根据登录人获取酒店ids列表
                        if (HotelEmployeeList.size()>0){
                            for (HotelEmployee hotelEmployee:HotelEmployeeList){
                                Integer hotel_id = hotelEmployee.getHotel_id();
                                String shopID = hotelBaseService.getById(hotel_id).getSmhotel_id();
                                ret.put("shopID",shopID);//登录给一个默认值
                            }
                        }
                    }
                }
                request.getSession().setAttribute("employee_id", user.getId());
                List<Permission> permissions = permissionService.getPermissionUrlsByUserId(user.getId(), 0);
                if (permissions.size() > 0) {
                    for (Permission permission: permissions) {
                        if(permission.getUrl()!=null && permission.getUrl().length()>0) {
                            ret.put("message", permission.getUrl());
                            break;
                        }
                    }
                }
                ret.put("role_id", user.getRole_id()+"");
                //request.getSession().setMaxInactiveInterval(0);

                //对于微信第一次登录,如果之前没有用此微信登陆过，则绑定此微信
                if (code != null && !"".equals(code)) {
                    WechatToken wechatToken = wechat.mediaToken(code, Wechat.HotelManagement);
                    String openid = wechatToken.getOpenid();
                    Employee employee = employeeService.getEmployeeByOpenId(openid);
                    if (employee == null) {
                        user.setOpen_id(openid);
                        employeeService.insertOrUpdateEmployee(user);
                    }
                }
        } else {
            //request.getSession().setAttribute("message", "用户名不存在，请重新登录");

            //ret.put("message", "登陆信息有误，请重新登录");
            return RestError.E_AUTH_FAILED;
        }
        return new RestError(ret);
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest request) {

        request.getSession().removeAttribute("employee_id");

        return RestError.E_OK;
    }


    @RequestMapping(value = "/console/api/menus", method = RequestMethod.GET)
    public Object menus(HttpServletRequest request) {
        Integer user_id = (Integer) request.getSession().getAttribute("employee_id");
        List<Permission> permissions = permissionService.getMenusByUserId(user_id, 0);

        //menu第二层
        for (int i = 0; i < permissions.size(); i++) {
            Permission permission = permissions.get(i);
            List<Permission> subMenu = permissionService.getMenusByUserId(user_id, permission.getId());
            if (subMenu != null && subMenu.size() > 0) {
                permission.setPermissions(subMenu);
                permissions.set(i, permission);
            }
        }

        return permissions;
    }


    @RequestMapping(value = "/autoLogin", method = RequestMethod.GET)
    public Object autoLogin(HttpServletRequest request,
                            @RequestParam("code") String code) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("sessionId", request.getSession().getId());
        WechatToken wechatToken = wechat.mediaToken(code, Wechat.HotelManagement);
        String openid = wechatToken.getOpenid();
        Employee employee = employeeService.getEmployeeByOpenId(openid);
        if (employee != null) {
            request.getSession().setAttribute("employee_id", employee.getId());
            ret.put("role_id", employee.getRole_id());
        }
        return ret;
    }

    @PostMapping(value = "/myHotelInfo")
    public Object myHotelInfo(HttpServletRequest request) {
        Integer employee_id = (Integer) request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(employee_id);
        HotelBase hotel = hotelBaseService.getById(hotelEmployee.getHotel_id());
        return new RestError(hotel);
    }

    @PostMapping(value = "/wx/console/api/loginByShopId")
    public Object loginByShopId(HttpSession session) {
        List<Map<String,String>> ret = new LinkedList<>();
        Map<String,String> dataMap ;
        List<HotelEmployee> HotelEmployeeList = hotelEmployeeService.getHotelIdsByEmpId(SessionUtil.getIdBySession(session));//根据登录人获取酒店ids列表
        if (HotelEmployeeList.size()>0){
            for (HotelEmployee hotelEmployee:HotelEmployeeList){
                Integer hotel_id = hotelEmployee.getHotel_id();
                String shopID = hotelBaseService.getById(hotel_id).getSmhotel_id();
                if (StringUtils.isNotBlank(shopID)){
                    dataMap = new HashMap<>();
                    dataMap.put("shopID",shopID);
                    String hotel_name = hotelBaseService.getById(hotel_id).getHotel_name();
                    dataMap.put("hotel_name",hotel_name);
                    ret.add(dataMap);
                }
            }
        }
        return new RestError(ret);
    }




}
