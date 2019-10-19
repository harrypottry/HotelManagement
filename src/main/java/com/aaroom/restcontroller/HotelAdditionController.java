package com.aaroom.restcontroller;

import com.aaroom.beans.HotelAdditionView;
import com.aaroom.service.HotelAdditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/26.
 */
@RestController
public class HotelAdditionController {

    @Autowired
    private HotelAdditionService hotelAdditionService;

    //查询
    @GetMapping(value = "/console/api/getHotelAdditionView")
    public HotelAdditionView getHotelAdditionView(HttpServletResponse response,
                                             @RequestParam(value = "hotelId",required = false ) Integer hotelId
    ){
        HotelAdditionView hotelAdditionView = hotelAdditionService.getHotelAdditionView(hotelId);
        return hotelAdditionView;
    }
    //
    @PostMapping(value = "/console/api/insertHotelAdditionView")
    public Map<String,Object> insertHotelAdditionView(@RequestBody HotelAdditionView HotelAdditionView){
        Map<String,Object> map = hotelAdditionService.insertHotelAdditionView(HotelAdditionView);
        return map;
    }
}
