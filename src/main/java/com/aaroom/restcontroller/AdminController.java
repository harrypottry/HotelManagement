package com.aaroom.restcontroller;

import com.aaroom.beans.Configuration;
import com.aaroom.beans.HotelBase;
import com.aaroom.exception.RestError;
import com.aaroom.service.ConfigurationService;
import com.aaroom.service.HotelBaseService;
import com.aaroom.utils.PinyinUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sosoda on 2019/1/23.
 * 管理性质的接口
 */
@RestController
public class AdminController {

    @Autowired
    private ConfigurationService configurationService;

    @Value("${admin.token}")
    private String adminToken;

    @Autowired
    private HotelBaseService hotelBaseService;


    @PostMapping(value = "/appVersion")
    public Object appVersion(HttpServletRequest request){
        Configuration configuration = configurationService.getConfigurationByName("appVersion");
        return new RestError(JSON.parseObject(configuration.getData()));
    }

    @PostMapping(value = "/postConfiguration")
    public Object postConfiguration(HttpServletRequest request, @RequestBody Map<String, String> data){
        if(data==null || StringUtils.isEmpty(data.get("token")) || !adminToken.equals(data.get("token"))) {
            return RestError.E_AUTH_FAILED;
        }
        Configuration configuration = configurationService.getConfigurationByName(data.get("name"));
        configuration.setData(data.get("data"));
        configurationService.update(configuration);
        return RestError.E_OK;
    }

    @PostMapping("/refreshHotels")
    public Object refreshHotels(HttpServletRequest request, @RequestBody Map<String, String> data) throws IOException {
        if(data==null || StringUtils.isEmpty(data.get("token")) || !adminToken.equals(data.get("token"))) {
            return RestError.E_AUTH_FAILED;
        }

        StringBuilder sb = new StringBuilder();

        //组织数据
        List<HotelBase> hotelBases = hotelBaseService.getAllHotelBase();
        {
            List<List<String>> hotels = new ArrayList<>();
            PinyinUtil pinyinUtil = new PinyinUtil();
            for (HotelBase hotelBase : hotelBases) {
                List<String> dataMap = new ArrayList<>();
                dataMap.add(hotelBase.getId().toString());
                dataMap.add(hotelBase.getHotel_name());
                dataMap.add(pinyinUtil.getUpperCase(hotelBase.getHotel_name(), true) + pinyinUtil.getUpperCase(hotelBase.getHotel_name(), false));
                hotels.add(dataMap);
            }
            sb.append("var hotels=");
            sb.append(JSON.toJSONString(hotels));
        }

        {
            Map<String, String> hotels = new LinkedHashMap<>();
            for (HotelBase hotelBase: hotelBases) {
                hotels.put(hotelBase.getId().toString(), hotelBase.getHotel_name());
            }
            sb.append(";var getstatus=");
            sb.append(JSON.toJSONString(hotels));
        }

        FileWriter fileWriter = null;
        try {
            String filePath = request.getSession().getServletContext().getRealPath("/");
            File file = new File(filePath+"import/adminlte/js/options.js");
            boolean exists = file.exists();
            if(!exists) {
                boolean newFile = file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(sb.toString());
        }catch (IOException e) {
            e.printStackTrace();
            return RestError.E_DATA_FAIL;
        }finally {
            fileWriter.close();
        }



        return RestError.E_OK;
    }
}
