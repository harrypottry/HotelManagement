package com.aaroom.restcontroller;

import com.aaroom.beans.AccountView;
import com.aaroom.beans.CollectedClaimAllView;
import com.aaroom.beans.CollectedClaimView;
import com.aaroom.service.CollectedClaimService;
import com.aaroom.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/1/8.
 */
@RestController
public class CollectedClaimController {

    @Autowired
    private CollectedClaimService collectedClaimService;

    //获取指定酒店额指定月份的
    @GetMapping(value = "/console/api/exportCollectedClaimList")
    public Map<String,Object> exportAccountList(HttpServletResponse response,
                                                @RequestParam(value = "hotelId",required = false ) String hotelId,
                                                @RequestParam(value = "monthTime",required = false ) String time,
                                                @RequestParam(value = "status",required = false)Integer status,
                                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {
        Map<String,Object> map = collectedClaimService.getAllList(hotelId,time,pageNo,pageSize,status);
        map.remove("list");
        return map;
    }

    //extel导出收款统计
    @GetMapping(value = "/consoles/api/exportCollectedClaimListExtel")
    public void exportmissionbytimeandhotelidlist(HttpServletResponse response,
                                                  @RequestParam(value = "hotelId",required = false ) String hotelId,
                                                  @RequestParam(value = "status",required = false)Integer status,
                                                  @RequestParam(value = "monthTime",required = false ) String time) throws Exception {
        //获取查询结果放到集合中
        Map<String,Object> map = collectedClaimService.getAllList(hotelId,time,1,1,status);
        int n = 3+10*4,m = 2;
        String[][] listTitle = new String[m][n];
        String[] listTitle1 = {"账单周期","酒店名称","酒店编号","PMS系统维护费","OTA代运营费","输送客源费","合作服务费","会员服务费","布草租赁收款","布草洗涤收款","布草报损赔偿款","人力总包收款","合计"};
        String[] listTitle2 = {"应收","实收","未收","调整"};
        String[] listKey={"monthTime","hotelName","hotelId","pmsOughtPrice","pmsActualPrice","pmsWaitPrice","pmsAdjustPrice","otaOughtPrice","otaActualPrice","otaWaitPrice","otaAdjustPrice",
                "conveyOughtPrice","conveyActualPrice","conveyWaitPrice","conveyAdjustPrice","cooperationOughtPrice","cooperationActualPrice","cooperationWaitPrice","cooperationAdjustPrice",
                "nightOughtPrice","nightActualPrice","nightWaitPrice","nightAdjustPrice","rentalOughtPrice","rentalActualPrice","rentalWaitPrice","rentalAdjustPrice",
                "washOughtPrice","washActualPrice","washWaitPrice","washAdjustPrice","damageOughtPrice","damageActualPrice","damageWaitPrice","damageAdjustPrice",
                "personnelOughtPrice","personnelActualPrice","personnelWaitPrice","personnelAdjustPrice",};
        for (int i = 0;i < m;i++){
            for (int j = 0,z = 0;j < n;){
                if(i==0){
                    listTitle[i][j] = listTitle1[z];
                    if(j>=3){
                        j+=4;
                    }else{
                        j++;
                    }
                    z++;
                }else{
                    if(j >= 3){
                        if(z==4){
                            z = 0;
                        }
                        listTitle[i][j] = listTitle2[z];
                        z++;
                    }
                    j++;
                }
            }
        }
        String sheetName = "收款统计"+time.replace("-", "")+".xls";
        List<Map> list = (List) map.get("list");
        Object content[][] = new Object[list.size()][n];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < list.size(); i++) {
            Map obj = list.get(i);
            for (int j = 0; j < listKey.length; j++){
                if(j>=3){
                    content[i][j] = df.format(obj.get(listKey[j]));
                }
            }
            content[i][39] = df.format(Double.parseDouble(obj.get(listKey[3])+"")+Double.parseDouble(obj.get(listKey[7])+"")+Double.parseDouble(obj.get(listKey[11])+"")+Double.parseDouble(obj.get(listKey[15])+"")+Double.parseDouble(obj.get(listKey[19])+"")+Double.parseDouble(obj.get(listKey[23])+"")+Double.parseDouble(obj.get(listKey[27])+"")+Double.parseDouble(obj.get(listKey[31])+"")+Double.parseDouble(obj.get(listKey[35])+""));
            content[i][40] = df.format(Double.parseDouble(obj.get(listKey[4])+"")+Double.parseDouble(obj.get(listKey[8])+"")+Double.parseDouble(obj.get(listKey[12])+"")+Double.parseDouble(obj.get(listKey[16])+"")+Double.parseDouble(obj.get(listKey[20])+"")+Double.parseDouble(obj.get(listKey[24])+"")+Double.parseDouble(obj.get(listKey[28])+"")+Double.parseDouble(obj.get(listKey[32])+"")+Double.parseDouble(obj.get(listKey[36])+""));
            content[i][41] = df.format(Double.parseDouble(obj.get(listKey[5])+"")+Double.parseDouble(obj.get(listKey[9])+"")+Double.parseDouble(obj.get(listKey[13])+"")+Double.parseDouble(obj.get(listKey[17])+"")+Double.parseDouble(obj.get(listKey[21])+"")+Double.parseDouble(obj.get(listKey[25])+"")+Double.parseDouble(obj.get(listKey[29])+"")+Double.parseDouble(obj.get(listKey[33])+"")+Double.parseDouble(obj.get(listKey[37])+""));
            content[i][42] = df.format(Double.parseDouble(obj.get(listKey[6])+"")+Double.parseDouble(obj.get(listKey[10])+"")+Double.parseDouble(obj.get(listKey[14])+"")+Double.parseDouble(obj.get(listKey[18])+"")+Double.parseDouble(obj.get(listKey[22])+"")+Double.parseDouble(obj.get(listKey[26])+"")+Double.parseDouble(obj.get(listKey[30])+"")+Double.parseDouble(obj.get(listKey[34])+"")+Double.parseDouble(obj.get(listKey[38])+""));
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }

}
