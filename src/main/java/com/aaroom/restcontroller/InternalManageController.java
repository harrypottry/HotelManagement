package com.aaroom.restcontroller;

import com.aaroom.beans.Cloth;
import com.aaroom.beans.ClothType;
import com.aaroom.beans.HotelEmployee;
import com.aaroom.exception.RestError;
import com.aaroom.service.ClothService;
import com.aaroom.service.ClothTypeService;
import com.aaroom.service.HotelEmployeeService;
import com.aaroom.service.RelClothTypeService;
import com.aaroom.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @className InternalManageController
 * @Description 这个类主要是干 【app业务端】AA工作人员 进行内部抽查人员 使用的一些接口
 * @Author 张赢
 * @Date 2018/12/29 0029下午 17:05
 * @Version 1.0
 **/
@RestController
public class InternalManageController {

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private ClothService clothService;
    //业务端 E 三码合一 A 得到所有布草类型S
    @PostMapping("/app/console/api/GetAllClothTypes")
    public Object GetAllClothTypes(HttpServletRequest request){
        Constants.ClothKind[] kinds = Constants.ClothKind.values();
        Map<Object, Object> ret = new HashMap<>();
        List list=new ArrayList();
        for (Constants.ClothKind kind: kinds) {
            List<ClothType> clothTypeList = clothTypeService.getClothTypeByKind(kind);
            Map kindsmps=new LinkedHashMap();
            kindsmps.put("name",kind);
            kindsmps.put("desclist",clothTypeList);
            //ret.put(kind, clothTypeList);
            list.add(kindsmps);
        }
        return new RestError(list);
    }

    //业务端 E 三码合一 B 确认关联
    @PostMapping(value = "/app/console/api/RelClothTypeBuild")
    public Object RelClothTypeBuild(HttpServletRequest request,
                                    @RequestParam(value ="cloth_type_ids",required =false,defaultValue = "") List<Integer> cloth_type_ids,
                                    @RequestParam(value ="RfidList",required =false,defaultValue = "")  List<String> RfidList){
        List  RfidExistlist =new ArrayList();
        List  RfidMisslist =new ArrayList();

        for (String Rfid:
        RfidList) {
            //根据Rfid查询clothid
            Integer clothid = clothService.getClothByRfid(Rfid);
            if(clothid !=null){
                RfidExistlist.add(clothid);
            }else {
                RfidMisslist.add(clothid);
            }
        }
        Collections.sort(cloth_type_ids);
            relClothTypeService.buildRelClothType(RfidExistlist, cloth_type_ids);
        return new RestError("一共传进"+RfidList.size()+"个Rfid，已经成功关联"+RfidExistlist.size()+
                "个 Rfid,其中有"+RfidMisslist.size()+"个id，在库中没有初始化");
    }

    //V1.5 业务端 E-3 二码合一(扫描二维码与rfid关联起来  clothid=rfid 在三码合一之前) 首先要有clothid
    @PostMapping(value = "/app/console/api/ClothContactRfid")
    public Object ClothContactRfid(HttpServletRequest request,
                                   @RequestParam(value ="QRCode",required =false,defaultValue = "") Integer QRCode,
                                   @RequestParam(value ="Rfid",required =false,defaultValue = "")  String Rfid){
        //根据clothid查对象
        Cloth cloth = clothService.getClothByid(QRCode);
        //判空，如果对象有就修改对象，增加rfid 如果没有就inset对象
        if(cloth!=null){
            cloth.setRfID(Rfid);
            clothService.update(cloth);
            return new RestError("二维码ID与RFID已经成功关联");
        }else {
            Cloth CothNew=new Cloth();
            CothNew.setId(QRCode);
            CothNew.setRfID(Rfid);

            int i = clothService.insertQRCodeAndRfid(CothNew);
            return new RestError("库中没有:"+QRCode+"这个布草，系统新建了ID为:  "+QRCode+
                    "的"+i+"个布草，并已经进行了二维码ID:  "+QRCode+"与RFID:  "+Rfid+"的成功关联");
        }

    }


}
