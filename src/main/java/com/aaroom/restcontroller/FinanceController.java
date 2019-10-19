package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.model.ChargeConfigView;
import com.aaroom.model.ClaimImportExcelView;
import com.aaroom.model.FinanceListView;
import com.aaroom.service.AccountService;
import com.aaroom.service.EmployeeService;
import com.aaroom.service.FinanceService;
import com.aaroom.service.HotelBaseService;
import com.aaroom.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 财务模块相关
 * @create: 2018-12-25 11:27
 **/
@RestController
public class FinanceController implements ServletContextAware {

    private static final Logger log = LoggerFactory.getLogger(FinanceController.class);

    private ServletContext servletContext;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private HotelBaseService hotelBaseService;

    //获取租赁费用列表
    @GetMapping(value = "/console/api/getRentalList")
    public Map<String, Object> getRentalList(@RequestParam Integer hotelId,
                                               @RequestParam(required = false) String beginTime,
                                               @RequestParam(required = false) String endTime,
                                               @RequestParam int pageNo,@RequestParam int pageSize) {
        if (hotelId == 0) hotelId = null;
        Map<String, Object> map = financeService.getRentalList(hotelId, beginTime, endTime, pageNo, pageSize);
        map.remove("dataAll");
        return map;
    }

    //导出租赁费用Excel
    @GetMapping(value = "/console/api/exportRentalList")
    public void exportRentalList(HttpServletResponse response, @RequestParam Integer hotelId,
                                 @RequestParam(required = false) String beginTime,
                                 @RequestParam(required = false) String endTime){
        if (hotelId == 0) hotelId = null;
        Map map = financeService.getRentalList(hotelId, beginTime, endTime, 1, 1);
        List<FinanceListView> list = (List<FinanceListView>) map.get("dataAll");
        FinanceListView financeListView = new FinanceListView("总计",(double)map.get("total"));
        list.add(financeListView);
        String[] listTitle = {"布草名称","尺寸","布草材质","布草ID","配属酒店名称","配属酒店编号","租赁开始时间","租赁结束时间","租赁结算价格(含税)"};
        String sheetName = null;
        if (beginTime != null && endTime != null) {
            sheetName = "布草租赁对账" + beginTime.replace("-", "") + "-" + endTime.replace("-", "") + ".xls";
        }
        else sheetName = "布草租赁对账.xls";
        int valueLength = listTitle.length;
        if (list.size() > listTitle.length) valueLength = list.size();
        Object content[][] = new Object[valueLength][listTitle.length];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < list.size(); i++) {
            FinanceListView obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                content[i][0] = obj.getClothName();
                content[i][1] = obj.getClothSize();
                content[i][2] = obj.getClothMaterial();
                content[i][3] = obj.getClothId();
                content[i][4] = obj.getHotelName();
                content[i][5] = obj.getHotelID();
                if (obj.getBeginDate() == null && obj.getEndDate() == null) {
                    content[i][6] = "";
                    content[i][7] = "";
                }else {
                    content[i][6] = simpleDateFormat.format(obj.getBeginDate());
                    content[i][7] = simpleDateFormat.format(obj.getEndDate());
                }
                content[i][8] = obj.getRentalPrice()+"";
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb, sheetName, response);
    }

    //获取洗涤费用列表
    @GetMapping(value = "/console/api/getWashList")
    public Map<String, Object> getWashList(@RequestParam Integer hotelId,
                                             @RequestParam(required = false) String employeeId,
                                             @RequestParam(required = false) String beginTime,
                                             @RequestParam(required = false) String endTime,
                                             @RequestParam int pageNo,@RequestParam int pageSize) {
        if (hotelId == 0) hotelId = null;
        Map<String, Object> map =  financeService.getWashList(hotelId,employeeId,beginTime,endTime, pageNo, pageSize);
        map.remove("dataAll");
        return map;
    }
    //导出洗涤费用Excel
    @GetMapping(value = "/console/api/exportWashList")
    public void exportWashList(HttpServletResponse response,
                               @RequestParam Integer hotelId,
                               @RequestParam(required = false) String employeeId,
                               @RequestParam(required = false) String beginTime,
                               @RequestParam(required = false) String endTime) {
        if (hotelId == 0) hotelId = null;
        Map map = financeService.getWashList(hotelId,employeeId,beginTime,endTime, 1, 1);
        List<FinanceListView> list = (List<FinanceListView>) map.get("dataAll");
        FinanceListView financeListView = new FinanceListView("总计",(double)map.get("collecttotal"), (double) map.get("paytotal"));
        list.add(financeListView);
        String[] listTitle = {"布草名称","尺寸","布草材质","布草ID","配属酒店名称","配属酒店编号","洗涤提供商","洗涤开始时间","洗涤结束时间","AA收取酒店价格(含税)","AA支付洗涤商价格(含税)"};
        String sheetName = null;
        if (beginTime != null && endTime != null) {
            sheetName = "布草洗涤对账" + beginTime.replace("-", "") + "-" + endTime.replace("-", "") + ".xls";

        }
        else sheetName = "布草洗涤对账.xls";
        int valueLength = listTitle.length;
        if (list.size() > listTitle.length) valueLength = list.size();
        Object content[][] = new Object[valueLength][listTitle.length];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < list.size(); i++) {
            FinanceListView obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                content[i][0] = obj.getClothName();
                content[i][1] = obj.getClothSize();
                content[i][2] = obj.getClothMaterial();
                content[i][3] = obj.getClothId();
                content[i][4] = obj.getHotelName();
                content[i][5] = obj.getHotelID();
                content[i][6] = obj.getWashFactory();
                if (obj.getBeginDate() == null && obj.getEndDate() == null) {
                    content[i][7] = "";
                    content[i][8] = "";
                }else {
                    content[i][7] = simpleDateFormat.format(obj.getBeginDate());
                    content[i][8] = simpleDateFormat.format(obj.getEndDate());
                }
                content[i][9] = obj.getCollectWashPrice()+"";
                content[i][10] = obj.getPayWashPrice()+"";
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }

    //获取洗衣厂数据
    @GetMapping(value = "/console/api/getWashFactory")
    public List getWashFactory() {
        return employeeService.getWashFactory();
    }

    //人力平台对账
    @GetMapping(value = "/console/api/getmissionbytimeandhotelid")
    public Map<String,Object> getMissionByTimeAndHotelId(
            HttpServletResponse response,
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String hotelId,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){

        if(employeeId.equals("0")){
            employeeId = "";
        }
        if(hotelId.equals("0")){
            hotelId = "";
        }
        //获取查询结果放到集合中
        Map<String,Object> map = financeService.getMissionList(employeeId,hotelId,beginTime,endTime,pageNo,pageSize);
        map.remove("list");
        return map;
    }

    //人力平台excel文件输出
    @GetMapping(value = "/consoles/api/exportmissionbytimeandhotelidlist")
    public void exportmissionbytimeandhotelidlist(HttpServletResponse response,
                                                  @RequestParam(required = false) String employeeId,
                                                  @RequestParam(required = false) String hotelId,
                                                  @RequestParam(required = false) String beginTime,
                                                  @RequestParam(required = false) String endTime) {
        if(employeeId.equals("0")){
            employeeId = null;
        }
        if(hotelId.equals("0")){
            hotelId = null;
        }
        //获取查询结果放到集合中
        Map<String,Object> map = financeService.getMissionList(employeeId,hotelId,beginTime,endTime,1,1);
        String[] listTitle = {"人员姓名","人员编号","配属酒店名称","配属酒店编号","工作任务类型","工作任务编号","任务开始时间","任务结算时间","AA供应酒店价格（含税）","扣款金额","AA供应保洁价格（含税）","AA给平台结算金额（含税）","保洁应到手金额（含税）"};
        String sheetName = "人力众包对账"+ beginTime.replace("-", "") + "-" + endTime.replace("-", "")+".xls";
        List<MissionPriceView> list = (List) map.get("list");
        Object content[][] = new Object[list.size()+1][listTitle.length];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < list.size()+1; i++) {
            if(i == list.size()){
                content[i][0] = "合计";
                Map<String,Object> pagetotel = (Map) map.get("totel");
                content[i][8] = pagetotel.get("totelPrice");
                content[i][9] = pagetotel.get("totelReworkPrice");
                content[i][10] = pagetotel.get("totelCleanPrice");
                content[i][11] = pagetotel.get("totelSumPrice");
                content[i][12] = pagetotel.get("totelSumPrice");
            }else{
                MissionPriceView obj = list.get(i);
                content[i][0] = obj.getEmployeeName();
                content[i][1] = obj.getEmployeeId()+"";
                content[i][2] = obj.getHotelName();
                content[i][3] = obj.getHotelId()+"";
                content[i][4] = obj.getMissionType();
                content[i][5] = obj.getMissionId()+"";
                content[i][6] =obj.getBeginTime();
                content[i][7] = obj.getEndTime();
                content[i][8] = df.format(obj.getPrice());
                content[i][9] = df.format(obj.getReworkPrice());
                content[i][10] = df.format(obj.getCleanPrice());
                Double f = obj.getCleanPrice()-obj.getReworkPrice();
                content[i][11] = df.format(f);
                content[i][12] = df.format(f);
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }

    //导入银行账单数据
    @PostMapping(value = "/console/api/claimExcelImport")
    public Map<String, Object> claimExcelImport(@RequestParam(value = "file") MultipartFile file) {
        Map<String, Object> map  = new HashMap<>();
        try {
            InputStream is = file.getInputStream();
            Workbook wb = ExcelUtils.workbookType(file.getOriginalFilename(), is);
            List<ClaimImportExcelView> list = ExcelUtils.readDateListT(wb, new ClaimImportExcelView(), 2, 0);
            if (list.size() == 0) {
                map.put("status", "fail");
                map.put("msg", "导入失败<br/>请严格按照模板格式重新上传！");
            } else {
                int data = financeService.saveClaimImportData(list);
                map.put("data", data);
                map.put("status", "success");
                map.put("msg", "导入数据成功");
            }
        } catch (Exception e) {
            log.error("==========导入Excel出错===========");
            map.put("status", "fail");
            map.put("msg", "导入失败<br/>请严格按照模板格式重新上传！");
            e.printStackTrace();
        }
        return map;
    }

    //获取认领数据
    @GetMapping(value = "/console/api/getClaimAccountData")
    public Map<String, Object> getClaimAccountData(@RequestParam Integer status,
                                                   @RequestParam Integer hotelId,
                                                   @RequestParam(required = false)String beginTime,
                                                   @RequestParam(required = false)String endTime,
                                                   @RequestParam int pageNo, @RequestParam int pageSize) {
        if (status == 2) status = null;
        if (hotelId == 0) hotelId = null;
        Map<String, Object> map =  financeService.getClaimAccountData(status, hotelId, beginTime, endTime, pageNo, pageSize);
        map.remove("list");
        return map;
    }

    //导出认领账单Excel
    @GetMapping(value = "/console/api/exportClaimAccountData")
    public void exportClaimAccountData(HttpServletResponse response ,@RequestParam Integer status,
                                       @RequestParam Integer hotelId,
                                       @RequestParam(required = false)String beginTime,
                                       @RequestParam(required = false)String endTime) {
        if (status == 2) status = null;
        if (hotelId == 0) hotelId = null;
        Map<String, Object> map =  financeService.getClaimAccountData(status, hotelId, beginTime, endTime, 1, 1);
        List<ClaimAccount> list = (List<ClaimAccount>) map.get("list");
        String[] listTitle = {"流水号","交易日期","酒店名称","酒店编号","打款人","打款是否一致","打款账号","金额","摘要","收款银行","已认领金额","待认领金额","认领笔数","最后修改时间","备注"};
        String sheetName = "认领账单"+System.currentTimeMillis()+".xls";
        int valueLength = listTitle.length;
        if (list.size() > listTitle.length) valueLength = list.size();
        Object content[][] = new Object[valueLength][listTitle.length];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < list.size(); i++) {
            ClaimAccount obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                content[i][0] = obj.getFlowNumber();
                content[i][1] = simpleDateFormat.format(obj.getTransactionDate());
                content[i][2] = obj.getHotelName();
                if (obj.getHotelId() == null) content[i][3] = "";
                else content[i][3] = obj.getHotelId()+"";
                content[i][4] = obj.getDrawee();
                if (obj.getType() == null) content[i][5] = "";
                else content[i][5] = obj.getType()+"";
                content[i][6] = obj.getPaymentAccount();
                content[i][7] = obj.getTransactionAmount();
                content[i][8] = obj.getSummary();
                content[i][9] = obj.getReceivingBank();
                content[i][10] = obj.getClaimedAmount()+"";
                content[i][11] = obj.getTobeClaimedAmount()+"";
                content[i][12] = obj.getClaimTimes()+"";
                if (obj.getUpdateDate() == null) content[i][13] = "";
                else content[i][13] = simpleDateFormat.format(obj.getUpdateDate());
                //content[i][14] = obj.getRemark();暂时不用
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb, sheetName, response);

    }

    //下载导入银行账单模板
    @GetMapping(value = "/console/api/exportClaimTemplate")
    public void exportClaimTemplate(HttpServletResponse response) {
        String path = servletContext.getRealPath("/");
        String fileName = "导入认领数据模板.xlsx";
        try {
            fileName = new String(fileName.getBytes("GB2312"), "ISO_8859_1");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            ServletOutputStream out;
            File file = new File(path + "导入认领数据模板.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[1024];
            while (b != -1){
                b = inputStream.read(buffer);
                out.write(buffer,0,b);
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    //根据酒店ID查询该酒店各个账单总和
    @GetMapping(value = "/console/api/getAccountSum")
    public Map<String, Object> getAccountSum(@RequestParam Integer hotelId) {
        Map<String, Object> map = new HashMap<>();
        HotelBase hotelBase = hotelBaseService.getById(hotelId);
        List<Account> list = null;
        double claimSum = 0;
        if (hotelBase != null) {
            list =  accountService.getAccountSum(hotelId);
            for (Account account : list) {
                claimSum += account.getUpdatePrice() * 100;
            }
        } else list = Collections.EMPTY_LIST;
        map.put("data", list);
        map.put("claimSum", claimSum / 100);
        return map;
    }

    //保存账单数据以及修改认领账单相关信息
    @PostMapping(value = "/console/api/saveClaimAccount")
    public Map<String, String> saveClaimAccount(@RequestBody CollectClaim collectClaim) {
        return financeService.saveClaimAccount(collectClaim);
    }

    //添加酒店收款配置
    @PostMapping(value = "/console/api/saveChargeConfig")
    public Map<String, String> saveChargeConfig(@RequestBody ChargeConfigView ccv) {
        return financeService.saveChargeConfig(ccv);
    }

    //获取酒店收款配置信息
    @GetMapping(value = "/console/api/getChargeConfigByHotelId")
    public ChargeConfigView getChargeConfigByHotelId(@RequestParam Integer hotelId) {
        return financeService.getChargeConfigByHotelId(hotelId);
    }

    //修改收费配置信息
    @PostMapping(value = "/console/api/updateChargeRenewConfig")
    public Map<String, String> updateChargeConfig(@RequestBody ChargeRenewConfig chargeRenewConfig) {
        return financeService.updateChargeConfig(chargeRenewConfig.getHotelId(), chargeRenewConfig, true);
    }

    //获取修改收费历史记录，包括生效与未生效
    @GetMapping(value = "/console/api/getChargeRenewConfig")
    public Map<String, Object> getChargeRenewConfig(@RequestParam Integer chargeConfigId, @RequestParam Integer hotelId) {
        return financeService.getChargeRenewConfig(chargeConfigId, hotelId);
    }

}
