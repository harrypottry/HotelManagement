package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.model.ClothErrorView;
import com.aaroom.persistence.ClothErrorMapper;
import com.aaroom.persistence.ClothLogMapper;
import com.aaroom.persistence.ClothTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClothErrorService {

    @Autowired
    private ClothErrorMapper clothErrorMapper;

    @Autowired
    private ClothService clothService;

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private ClothTypeMapper clothTypeMapper;

    @Autowired
    private ClothLogMapper clothLogMapper;


    public List<ClothError> getErrorsByHotelId(int hotel_id) {
        return clothErrorMapper.getErrorsByHotelId(hotel_id);
    }
    //根据clothid查询clotherror对象
    public List<ClothError> getClothErrorsByHotelIdAndClothId(int hotel_id,Integer clothid) {
        return clothErrorMapper.getClothErrorsByHotelIdAndClothId(hotel_id,clothid);
    }
        //直接查询本酒店报损的clothidlist 上面是查clotherror对象list
    public List<Integer> getErrorClothidByHotelId(int hotel_id) {
        return clothErrorMapper.getErrorClothidByHotelId(hotel_id);
    }
    //根据hotelid 开始时间 结束时间查询报损列表 【张赢】 近一个月的（自然月）
    public List<ClothError> getClothErrorByDuringTime(int hotel_id, Date StartTime,Date EndTime) {
        return clothErrorMapper.getClothErrorByDuringTime(hotel_id,StartTime,EndTime);
    }

    public Integer insert(ClothError clothError) {
       return clothErrorMapper.insertSelective(clothError);
    }

    public List<ClothErrorView> getErrorClothListInfo(@RequestParam(required = false) Integer hotelId,
                                                      @RequestParam(required = false) String beginDate,
                                                      @RequestParam(required = false) String endDate,
                                                      @RequestParam(required = false)Integer pageSize,
                                                      @RequestParam(required = false)Integer index) throws Exception {
        //查询所有报损布草
        List<ClothErrorView> clothErrorList = clothErrorMapper.getClothErrorList(hotelId,beginDate,endDate,pageSize,index);
        List<ClothErrorView> clothErrorViewList = new ArrayList<>();
        //遍历报损布草集合
        for (ClothErrorView clothError : clothErrorList) {
            if (clothError.getCloth_type_id()!= null) {
                //获取布草类型
                getClothType(clothError);
                Long error_time = clothError.getError_time().getTime();
                Long create_time = clothError.getCreate_time().getTime();
                //布草用了多久
                Long dateTime = error_time - create_time;
                //换算成月
                Double  m = Double.valueOf((dateTime/(24*60*60*1000))/30);
                Double suggestPrice = getSuggestPrice(m,clothError.getCloth_price());
                clothError.setSuggest_pay(suggestPrice);
                clothErrorViewList.add(clothError);
            }
        }
        return clothErrorViewList;
    }

    public List<com.aaroom.beans.ClothErrorView> getErrorClothListByHotelidOrType(@RequestParam(required = false) Integer hotelId,
                                                                                  @RequestParam(required = false) String type,
                                                                                  @RequestParam(required = false)Integer pageSize,
                                                                                  @RequestParam(required = false)Integer index) throws Exception {
        //查询所有报损布草
        List<ClothError> clothErrorList = clothErrorMapper.getErrorClothListByHotelidOrType(hotelId,type,pageSize,index);
        List<com.aaroom.beans.ClothErrorView> clothErrorViewList = new ArrayList<>();

        //遍历报损布草集合
        for (ClothError clothError : clothErrorList) {
            com.aaroom.beans.ClothErrorView clothErrorView =new com.aaroom.beans.ClothErrorView();
            clothErrorView.setCloth_id(clothError.getCloth_id());
            clothErrorView.setType(clothError.getType());
            clothErrorView.setStatus(clothError.getStatus());
            clothErrorView.setUpdate_time(clothError.getUpdate_time());

            //根据clothid查到cloth对象 存洁净程度 1
            Cloth clothByid = clothService.getClothByid(clothError.getCloth_id());
            clothErrorView.setCleanStatus(clothByid.getStatus());
            //根据clothid查对应clothtypeids与desc、3
            String clothIdsByRedis = clothLogService.getClothIdsByRedis(clothError.getCloth_id());
            String[] clothIdssplit = clothIdsByRedis.split(",");
            List<ClothType> clothTypeDescByIds = clothTypeMapper.getClothTypeDescByIds(clothIdssplit);
            clothErrorView.setClothTypeList(clothTypeDescByIds);
            //根据clothid查clothlog表查到所在位置1
            List<ClothLog> clothLogByClothIdAndHotelId = clothLogMapper.getClothLogByClothIdAndHotelId(hotelId, clothError.getCloth_id());
            clothErrorView.setPossessor_type(clothLogByClothIdAndHotelId.get(0).getPossessor_type());
            //存入对象
            clothErrorViewList.add(clothErrorView);
        }
        return clothErrorViewList;
    }
    public Integer getErrorClothListByHotelidOrTypeNolimit(@RequestParam(required = false) Integer hotelId,
                                                           @RequestParam(required = false) String type) throws Exception {
        //查询所有报损布草的个数
        Integer cout = clothErrorMapper.getErrorClothListByHotelidOrTypeNolimit(hotelId, type);
        return cout;
    }

    //获取布草描述
    private void getClothType(ClothErrorView errorView) throws Exception {
        ClothView clothView = clothService.getClothView(errorView.getCloth_id());
        List<ClothType> list2 = clothView.getClothTypes();
        if(list2==null){
            throw  new Exception();
        }
        errorView.setCloth_type(list2.get(0).getDesc());
        errorView.setCloth_size(list2.get(1).getDesc());
        errorView.setCloth_material(list2.get(2).getDesc());
    }
    //根据月份计算建议价格
    private Double getSuggestPrice(Double m, Double clothPrice) {
        Double suggestPrice = null;
        if (m<=3){
            suggestPrice = clothPrice;//使用时间小于三个月赔付100%
        }else if (3<m && m<=6){
            suggestPrice = clothPrice * 0.8;//使用时间大于三个月小于六个月赔付80%
        }else if (6<m && m<=12){
            suggestPrice = clothPrice * 0.6;//使用时间大于六个月小于十二个月赔付60%
        }else if (12<m && m<=18){
            suggestPrice = clothPrice * 0.3;//使用时间大于十二个月小于十八个月赔付30%
        }else if (18<m && m<=24){
            suggestPrice = clothPrice * 0.1;//使用时间大于十八个月小于二十四个月赔付10%
        }else{
            suggestPrice = 0.0;//使用时间大于二十四个月不用赔
        }
        return suggestPrice;
    }

    public Long getErrorClothListCount(Integer hotelId, String beginDate,String endDate) {
        return clothErrorMapper.getErrorClothListCount(hotelId,beginDate,endDate);
    }


    public List<ClothErrorView> getClothErrorListSum(Integer hotelId, String beginTime, String endTime){

        try {
            return getErrorClothListInfo(hotelId,beginTime,endTime,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

