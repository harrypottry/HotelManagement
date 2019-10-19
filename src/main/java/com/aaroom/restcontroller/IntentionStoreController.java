package com.aaroom.restcontroller;

import com.aaroom.beans.IntentionStoreContract;
import com.aaroom.beans.IntentionStoreSurvey;
import com.aaroom.model.IntentionStoreInfoView;
import com.aaroom.service.IntentionStoreService;
import com.aaroom.service.StorageService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 意向门店相关
 * @create: 2019-02-18 16:33
 **/

@RestController
public class IntentionStoreController {

    private static final Logger log = LoggerFactory.getLogger(IntentionStoreController.class);

    @Autowired
    private IntentionStoreService intentionStoreService;

    @Autowired
    private StorageService storageService;

    //获取意向门店酒店列表
    @GetMapping(value = "/console/api/getIntentionStoreData")
    public Map<String, Object> getIntentionStoreData(@RequestParam(required = false) String hotelName, @RequestParam(required = false) String owernName,
                                                     @RequestParam Integer status, @RequestParam(required = false) Integer pageNo,
                                                     @RequestParam(required = false) Integer pageSize) {
        if (status == 0) status = null;
        Map<String, Object> map = intentionStoreService.getIntentionStoreData(hotelName, owernName, status, pageNo, pageSize);
        map.remove("dataAll");
        return map;
    }

    //导出意向门店酒店列表
    @GetMapping(value = "/console/api/exportIntentionStoreData")
    public void exportIntentionStoreData(HttpServletResponse response, @RequestParam(required = false) String hotelName, @RequestParam(required = false) String owernName,
                                         @RequestParam Integer status) {
        if (status == 0) status = null;
        Map<String, Object> map = intentionStoreService.getIntentionStoreData(hotelName, owernName, status, 1, 1);
        List<IntentionStoreSurvey> list = (List<IntentionStoreSurvey>) map.get("dataAll");
        String[] listTitle = {"门店名称","业主名称","门店地址","添加日期","门店状态"};
        String sheetName = "门店数据.xls";
        int valueLength = listTitle.length;
        if (list.size() > listTitle.length) valueLength = list.size();
        Object content[][] = new Object[valueLength][listTitle.length];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
            IntentionStoreSurvey obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                content[i][0] = obj.getHotelName();
                content[i][1] = obj.getOwernName();
                content[i][2] = obj.getHotelProvince() + obj.getHotelCity() + obj.getHotelCounty() + obj.getHotelStreetAddress();
                content[i][3] = simpleDateFormat.format(obj.getCreate_time());
                if (obj.getStatus() == 1) {
                    content[i][4] = "待入库";
                } else if (obj.getStatus() == 2) {
                    content[i][4] = "入库审批中";
                } else if (obj.getStatus() == 3) {
                    content[i][4] = "入库未通过";
                } else if (obj.getStatus() == 4) {
                    content[i][4] = "已入库";
                }
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb, sheetName, response);
    }


    //添加意向门店酒店概况
    @PostMapping(value = "/console/api/addIntentionStoreSurvey")
    public Map<String, Object> addIntentionStoreSurvey(@RequestBody IntentionStoreSurvey intentionStoreSurvey) {
        return intentionStoreService.addIntentionStoreSurvey(intentionStoreSurvey);
    }

    //获取意向门店酒店概况通过酒店名称
    @GetMapping(value = "/console/api/getIntentionStoreByHotelName")
    public IntentionStoreSurvey getIntentionStoreByHotelName(@RequestParam String hotelName) {
        return intentionStoreService.getIntentionStoreByHotelName(hotelName);
    }

    //添加文件
    @PostMapping(value = "/console/api/addIntentionStoreSurveyPhoto")
    public List<String> addIntentionStoreSurveyPhoto(@RequestParam(value = "file")MultipartFile[] files) {
        List<String> list = new ArrayList();
        if (files != null && files.length > 0) {
            if (files.length > 0) {
                for (MultipartFile multipartFile : files) {
                    String[] fileName = multipartFile.getOriginalFilename().split("\\.");
                    File temp = null;
                    try {
                        temp = File.createTempFile(fileName[0], fileName[1]);
                        multipartFile.transferTo(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String destFileName = CommonUtil.createUUID() +"."+ fileName[1];
                    storageService.uploadFile(temp, destFileName);
                    list.add(destFileName);
                }
            }
        }
        return list;
    }

    //获取意向门店酒店概况
    @GetMapping(value = "/console/api/getIntentionStoreSurvey")
    public IntentionStoreSurvey getIntentionStoreSurvey(@RequestParam String surveyId) {
        return intentionStoreService.getIntentionStoreSurvey(surveyId);
    }

    //更新意向门店酒店概况
    @PostMapping(value = "/console/api/updateIntentionStoreSurvey")
    public Map<String, Object> updateIntentionStoreSurvey(@RequestBody IntentionStoreSurvey intentionStoreSurvey) {
        return intentionStoreService.updateIntentionStoreSurvey(intentionStoreSurvey);
    }

    //添加意向门店签约信息
    @PostMapping(value = "/console/api/addIntentionStoreInfo")
    public Map<String, Object> addIntentionStoreInfo(@RequestBody IntentionStoreInfoView intentionStoreInfoView) {
        return intentionStoreService.addIntentionStoreInfo(intentionStoreInfoView);
    }

    //获取意向门店签约信息
    @GetMapping(value = "/console/api/getIntentionStoreInfo")
    public IntentionStoreInfoView getIntentionStoreInfo(@RequestParam String surveyId) {
        return intentionStoreService.getIntentionStoreInfo(surveyId);
    }

    //修改意向门店签约信息
    @PostMapping(value = "/console/api/updateIntentionStoreInfo")
    public Map<String, Object> updateIntentionStoreInfo(@RequestBody IntentionStoreInfoView intentionStoreInfoView) {
        return intentionStoreService.updateIntentionStoreInfo(intentionStoreInfoView);
    }

    //添加意向门店合同信息
    @PostMapping(value = "/console/api/addIntentionStoreContract")
    public Map<String, Object> addIntentionStoreContract(@RequestBody IntentionStoreContract intentionStoreContract) {
        return intentionStoreService.addIntentionStoreContract(intentionStoreContract);
    }

    //获取意向门店合同信息
    @GetMapping(value = "/console/api/getIntentionStoreContract")
    public IntentionStoreContract getIntentionStoreContract(@RequestParam String isiId) {
        return intentionStoreService.getIntentionStoreContract(isiId);
    }

    //修改意向门店合同信息
    @PostMapping(value = "/console/api/updateIntentionStoreContract")
    public Map<String, Object> updateIntentionStoreContract(@RequestBody IntentionStoreContract intentionStoreContract) {
        return intentionStoreService.updateIntentionStoreContract(intentionStoreContract);
    }

}
