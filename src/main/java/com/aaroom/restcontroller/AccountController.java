package com.aaroom.restcontroller;

import com.aaroom.beans.Account;
import com.aaroom.beans.AccountExample;
import com.aaroom.beans.AccountView;
import com.aaroom.beans.AccountViews;
import com.aaroom.service.AccountService;
import com.aaroom.utils.ExcelUtils;
import com.shangmei.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/1/4.
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    //获取指定酒店额指定月份的
    @GetMapping(value = "/console/api/exportAccountList")
    public Map<String,Object> exportAccountList(HttpServletResponse response,
                                                @RequestParam(required = false) String hotelId,
                                                @RequestParam(value = "monthTime",required = false ) String time,
                                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {
        Map<String, Object> map = accountService.getAllExportAccountList(hotelId, time, pageSize, pageNo);
        map.remove("list");
        return map;
    }

    //extel导出账单汇总
    @GetMapping(value = "/consoles/api/exportAccountListExtel")
    public void exportmissionbytimeandhotelidlist(HttpServletResponse response,
                                                  @RequestParam(required = false) String hotelId,
                                                  @RequestParam(value = "monthTime",required = false ) String time) throws Exception {
        //获取查询结果放到集合中
        Map<String,Object> map = accountService.getAllExportAccountList(hotelId,time,1,1);
        String[] listTitle = {"账单周期","酒店名称","酒店编号","PMS系统维护费","OTA代运营费","输送客源费","合作服务费","会员服务费","布草租赁收款","布草洗涤收款","布草报损赔偿款","人力总包收款","合计"};
        String[] listKey = {"","hotelName","hotelId","pmsPrice","otaPrice","conveyPrice","cooperationPrice","nightPrice","rentalPrice","washPrice","damagePrice","personnelPrice",""};
        String sheetName = "账单汇总"+time.replace("-", "")+".xls";
        List<Map> list = (List) map.get("list");
        Object content[][] = new Object[list.size()][listTitle.length];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < list.size(); i++) {
            Map obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                if(j==0){
                    content[i][0] = time+"";
                }else if(j==listTitle.length-1){
                    Double price = 0.00;
                    for (int z = 3; z < listKey.length-1;z++){
                        price += Double.parseDouble(obj.get(listKey[z])+"");
                    }
                    content[i][j] = df.format(price)+"";
                }else{
                    if(j>=3&&j<listKey.length-1){
                        content[i][j] = df.format(obj.get(listKey[j]))+"";
                    }else{
                        content[i][j] = obj.get(listKey[j])+"";
                    }
                }
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }

    //手动添加账单
    @PostMapping(value = "/console/api/insertAccounts")
    public  Map<String,String> insertAccounts(HttpServletResponse response,
                               @RequestBody List<AccountViews> list){
        Map<String,String> map = new HashMap();
        try {
            Integer integer = accountService.insertAccounts(list);
            if(integer==1){
               map.put("status","error");
               map.put("msg","操作失败，请重新操作！");
            }else{
                map.put("status","succeed");
                map.put("msg","生成账单成功！");
            }
        }catch (Exception e){
            map.put("status","error");
            map.put("msg","操作失败，请重新操作！");
        }
        return map;
    }

    //历史新增账单的页面显示
    @GetMapping(value = "/consoles/api/getAllHistoryList")
    public Map<String, Object> getAllHistoryList(HttpServletResponse response,
                                                  @RequestParam(required = false) String hotelId,
                                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Map<String, Object> allHistoryList = accountService.getAllHistoryList(hotelId, pageNo, pageSize);
        allHistoryList.remove("list");
        return allHistoryList;
    }

    public static void main(String[] args) {
        String str = "2016-07-18 00:00:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(DateUtil.parse(str));
    }
}
