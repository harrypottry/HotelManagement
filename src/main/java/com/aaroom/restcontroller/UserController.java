package com.aaroom.restcontroller;

import com.aaroom.beans.Employee;
import com.aaroom.exception.RestError;
import com.aaroom.service.EmployeeService;
import com.aaroom.service.HttpSenderService;
import com.aaroom.service.StorageService;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.DigestUtil;
import com.aaroom.wechat.Wechat;
import com.aaroom.wechat.WechatToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className UserController
 * @Description 这个类主要是干  用户 登录 注册 登出 修改密码 发验证码
 * @Author 张赢
 * @Date 2019/2/18 0018下午 19:43
 * @Version 1.0
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG            #
 * #                                                   #
 */

@RestController
public class UserController {
    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Wechat wechat;

    @Autowired
    private StorageService storageService;

    //1.6App 去重
    @RequestMapping(value = "/app/console/api/CheckRepeatPhone", method = RequestMethod.POST)
    public Object CheckRepeatPhone(@RequestParam(value = "phone")String phone) throws Exception {
        Employee employee = employeeService.getEmployeeByPhoneToCheck(phone);
        if(employee != null){
            return RestError.E_AUTH_REPEAT;
        }else{
            return RestError.E_OK;
        }
    }

    //1.6App 登出
    @RequestMapping(value = "/app/console/api/AppUserLogout", method = RequestMethod.GET)
    public Object AppUserLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee_id");
        return RestError.E_AUTH_LOGOUT;
    }

    //1.6App 发验证码
    @RequestMapping(value = "/app/console/api/AppSendMessage", method = RequestMethod.POST)
    public Object AppSendMessage(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "phone")String phone) throws Exception {
        HttpSenderService httpSenderService=new HttpSenderService();
        String JHM = DigestUtil.randomCode()+"";
        String result=null;
        try{ result= httpSenderService.sendMsgByPhone(phone, "【阿姨联盟】您的注册验证码是："+JHM);
        } catch (Exception e) {
            e.printStackTrace();
            return RestError.E_AUTH_SENDMESSAGE;
        }
        //调redis拼接用户手机号存储 验证码 有效期 300秒 5分钟
        redisCacheService.put("phone:"+phone, JHM, 300);
        //redis没好 先用session
        //session.setAttribute("phone:"+phone,JHM);
        if(result != null){
            return new RestError(JHM);
        }else {
            return RestError.E_AUTH_SENDMESSAGE;
        }
    }

    //1.6App 验证验证码
    @RequestMapping(value = "/app/console/api/AppCompareMessage", method = RequestMethod.POST)
    public Object AppCompareMessage(HttpServletRequest request, HttpSession session,
                                    @RequestParam(value = "phone")String phone,
                                    @RequestParam(value = "phoneJHM")String phoneJHM){
        String JHM = redisCacheService.get("phone:"+phone, String.class);
        //Object JHM = session.getAttribute("phone:" + phone);
        if (phoneJHM.equals(JHM)){
            return RestError.E_OK;
        }else{
            return RestError.E_COMPARE_FAIL;
        }
    }

    //1.6App 手机注册
    @RequestMapping(value = "/app/console/api/AppRegisterByPhone", method = RequestMethod.POST)
    public Object AppRegisterByPhone(HttpServletRequest request,
                                  @RequestParam(value = "phone")String phone,
                                     @RequestParam(value = "open_id")String open_id) throws Exception {
        Employee employee=new Employee();
        //封装进一个新对象中
        employee.setPhone(phone);
        employee.setOpen_id(open_id);
        //调接口新增用户方法把对象参数放进去
        employeeService.insertOrUpdateEmployee(employee);
        return RestError.E_OK;
    }

    // 1.6App 修改密码
    @RequestMapping(value = "/app/console/api/AppUpdatePassword", method = RequestMethod.POST)
    public Object AppUpdatePassword(@RequestParam(value = "phone")String phone,
                                 @RequestParam(value = "password")String password) throws Exception {
        Employee employee = employeeService.getEmployeeByPhoneToCheck(phone);
        String PasswordMD5 = DigestUtil.hmacSign(password);
        employee.setPassword(PasswordMD5);
        employeeService.insertOrUpdateEmployee(employee);

        return RestError.E_OK;
    }

    // 1.6App 自动登陆
    @GetMapping("/app/console/api/AppAutoLogin")
    public Object AppAutoLogin(HttpServletRequest request,String code){

        WechatToken wechatToken = wechat.appToken(code,1);
        String open_id = wechatToken.getOpenid();
        Employee employeeByOpenId = employeeService.getEmployeeByOpenId(open_id);


        Map<String, Object> ret = new HashMap<String, Object>();
        if (employeeByOpenId!=null) {
            request.getSession().setAttribute("employee_id", employeeByOpenId.getId());//如果有这个openid 那么就是setsttribute就是登陆
            request.getSession().setMaxInactiveInterval(0);//无限时间
            ret.put("loginStatus",true);
        } else {
            ret.put("loginStatus",false);//如果没有就返回失败 去登陆
        }
        ret.put("sessionId", request.getSession().getId());
        ret.put("open_id", open_id);
        ret.put("session_key", wechatToken.getSession_key());

        return ret;
    }

}
