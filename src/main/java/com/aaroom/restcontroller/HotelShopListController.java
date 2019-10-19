package com.aaroom.restcontroller;

import com.aaroom.persistence.HotelShopListMapper;
import com.aaroom.service.ContractListService;
import com.aaroom.service.HotelShopListService;
import com.aaroom.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/19.
 */
@RestController
public class HotelShopListController {
    @Autowired
    private HotelShopListService hotelShopListService;

    @Autowired
    private ContractListService contractListService;

    //合同列表页面的查询
    @GetMapping(value = "/console/api/exportHotelShopList")
    public Object exportHotelShopList(HttpServletResponse response,
                                    @RequestParam(value = "hotelId",required = false ) Integer hotelId, //酒店id
                                    @RequestParam(value = "proprietorName",required = false) String proprietorName,
                                    @RequestParam(value = "expandId",required = false ) Integer expandId,
                                    @RequestParam(value = "shopStatus",required = false ) Integer shopStatus,
                                    @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Map<String, Object> map = hotelShopListService.exportHotelShopList(hotelId, proprietorName, expandId, shopStatus,0, pageSize, pageNo);
        Object data = map.get("data");
        return data;
    }
    //门店列表的导出
    @GetMapping(value = "/console/api/exportHotelShopListExtel")
    public void exportHotelShopListExtel(HttpServletResponse response,
                                      @RequestParam(value = "hotelId",required = false ) Integer hotelId, //酒店id
                                      @RequestParam(value = "proprietorName",required = false) String proprietorName, //业主
                                      @RequestParam(value = "expandId",required = false ) Integer expandId,
                                      @RequestParam(value = "shopStatus",required = false ) Integer shopStatus,
                                      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Map<String, Object> map = hotelShopListService.exportHotelShopList(hotelId, proprietorName, expandId, shopStatus,0, pageSize, pageNo);
        //获取查询结果放到集合中
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        String[] listTitle = {"酒店名称","原酒店名称","酒店ID","业主姓名","签约时间","拓展","运营店长","酒店状态"};
        String[] listKey = {"newHotelName","oldHotelName","hotelId","proprietorName","signatoryTime","expandName","shopManagerName","shopStatusName"};
        String sheetName = "门店列表"+time+".xls";
        List<Map> list = (List) map.get("list");
        Object content[][] = new Object[list.size()][listTitle.length];
        for (int i = 0; i < list.size(); i++) {
            Map obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                if(obj.get(listKey[j])!=null){
                    content[i][j] = obj.get(listKey[j])+"";
                }else{
                    content[i][j] = "";
                }
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }
    //获取运营店长列表
    @GetMapping(value = "/console/api/exportManagerList")
    public List exportManagerList(){
        Map<String, Object> managerList = hotelShopListService.exportemployeeManager(10);
        List list = (List) managerList.get("shopManagerList");
        return list;
    }
    //分配运营店长
    @GetMapping(value = "/console/api/insertShopManager")
    public Map<String,String> insertShopManager(HttpServletResponse response,
                                                 @RequestParam(value = "id",required = false ) Integer id,  //id
                                                 @RequestParam(value = "shopManagerId",required = false ) Integer shopManagerId  //运营店长id
    ) throws Exception {
        Map<String, String> map = new HashMap();
        try {
            Integer integer = hotelShopListService.insertShopManager(id,shopManagerId);
            if (integer != 1) {
                map.put("status", "error");
                map.put("msg", "分配失败");
            } else {
                map.put("status", "succeed");
                map.put("msg", "分配成功");
            }
        } catch (Exception e) {
            map.put("status", "error");
            map.put("msg", "分配失败");
        }
        return map;
    }
    //运营店长—我的门店
    @GetMapping(value = "/console/api/exportManagerHotelShopList")
    public Object exportManagerHotelShopList(HttpServletResponse response,
                                                HttpServletRequest request,
                                      @RequestParam(value = "hotelId",required = false ) Integer hotelId, //酒店id
                                      @RequestParam(value = "proprietorName",required = false) String proprietorName,
                                      @RequestParam(value = "expandId",required = false ) Integer expandId,
                                      @RequestParam(value = "shopStatus",required = false ) Integer shopStatus,
                                      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Integer shopManagerId = (Integer) request.getSession().getAttribute("employee_id");
        Map<String, Object> map = hotelShopListService.exportHotelShopList(hotelId, proprietorName, expandId, shopStatus,shopManagerId, pageSize, pageNo);
        Object data = map.get("data");
        return data;
    }
    //运营店长—我的门店导出
    @GetMapping(value = "/console/api/exportManagerHotelShopListExtel")
    public void exportManagerHotelShopListExtel(HttpServletResponse response,
                                                   HttpServletRequest request,
                                         @RequestParam(value = "hotelId",required = false ) Integer hotelId, //酒店id
                                         @RequestParam(value = "proprietorName",required = false) String proprietorName, //业主
                                         @RequestParam(value = "expandId",required = false ) Integer expandId,
                                         @RequestParam(value = "shopStatus",required = false ) Integer shopStatus,
                                         @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Integer shopManagerId = (Integer) request.getSession().getAttribute("employee_id");
        Map<String, Object> map = hotelShopListService.exportHotelShopList(hotelId, proprietorName, expandId, shopStatus,shopManagerId, pageSize, pageNo);
        //获取查询结果放到集合中
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        String[] listTitle = {"酒店名称","原酒店名称","酒店ID","业主姓名","签约时间","拓展","运营店长","酒店状态"};
        String[] listKey = {"newHotelName","oldHotelName","hotelId","proprietorName","signatoryTime","expandName","shopManagerName","shopStatusName"};
        String sheetName = "我的门店"+time+".xls";
        List<Map> list = (List) map.get("list");
        Object content[][] = new Object[list.size()][listTitle.length];
        for (int i = 0; i < list.size(); i++) {
            Map obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                if(obj.get(listKey[j])!=null){
                    content[i][j] = obj.get(listKey[j])+"";
                }else{
                    content[i][j] = "";
                }
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }
    //拓展—我的签约
    @GetMapping(value = "/console/api/exportExpandHotelShopList")
    public Object exportExpandHotelShopList(HttpServletResponse response,
                                                HttpServletRequest request,
                                                @RequestParam(value = "hotelId",required = false ) Integer hotelId, //酒店id
                                                @RequestParam(value = "proprietorName",required = false) String proprietorName,
                                                @RequestParam(value = "shopStatus",required = false ) Integer shopStatus,
                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Integer expandId = (Integer) request.getSession().getAttribute("employee_id");
        Map<String, Object> map = hotelShopListService.exportHotelShopList(hotelId, proprietorName, expandId, shopStatus,0, pageSize, pageNo);
        Object data = map.get("data");
        return data;
    }
    //拓展—我的签约导出
    @GetMapping(value = "/console/api/exportExpandHotelShopListExtel")
    public void exportExpandHotelShopListExtel(HttpServletResponse response,
                                                   HttpServletRequest request,
                                                   @RequestParam(value = "hotelId",required = false ) Integer hotelId, //酒店id
                                                   @RequestParam(value = "proprietorName",required = false) String proprietorName, //业主
                                                   @RequestParam(value = "shopStatus",required = false ) Integer shopStatus,
                                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Integer expandId = (Integer) request.getSession().getAttribute("employee_id");
        Map<String, Object> map = hotelShopListService.exportHotelShopList(hotelId, proprietorName, expandId, shopStatus,0, pageSize, pageNo);
        //获取查询结果放到集合中
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        String[] listTitle = {"酒店名称","原酒店名称","酒店ID","业主姓名","签约时间","拓展","运营店长","酒店状态"};
        String[] listKey = {"newHotelName","oldHotelName","hotelId","proprietorName","signatoryTime","expandName","shopManagerName","shopStatusName"};
        String sheetName = "我的签约"+time+".xls";
        List<Map> list = (List) map.get("list");
        Object content[][] = new Object[list.size()][listTitle.length];
        for (int i = 0; i < list.size(); i++) {
            Map obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                if(obj.get(listKey[j])!=null){
                    content[i][j] = obj.get(listKey[j])+"";
                }else{
                    content[i][j] = "";
                }
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }
}
