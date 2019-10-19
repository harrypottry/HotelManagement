package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.persistence.CollectedClaimMapper;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * Created by 温建成 on 2019/1/8.
 */
@Service
public class CollectedClaimService {

    @Autowired
    private CollectedClaimMapper collectedClaimMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private HotelBaseService hotelBaseService;

    //获取指定的月的应收和实收及代收金额列表
    public List<CollectedClaimView> getList(String hotelId,String time, Integer type) throws ParseException {
        List<Account> list = accountService.getList(hotelId, time, type);
        List<CollectedClaimView> claimList = new ArrayList<>();
        for (Account account:list
             ) {
            CollectedClaimView collectedClaimView = new CollectedClaimView();
            collectedClaimView.setMonthTime(time);
            collectedClaimView.setHotelId(account.getHotelId());
            HotelBase hotelBase = hotelBaseService.getById(account.getHotelId());
            collectedClaimView.setHotelName(hotelBase.getHotel_name());
            collectedClaimView.setOughtPrice(account.getPrice());
            collectedClaimView.setStatus(account.getStatus());
            CollectedClaimExample example = new CollectedClaimExample();
            CollectedClaimExample.Criteria criteria = example.createCriteria();
            criteria.andAccountIdEqualTo(account.getId());
            List<CollectedClaim> collectedList = collectedClaimMapper.selectByExample(example);
            Double price = 0.00;
            Double abjustPrice = 0.00;
            for (CollectedClaim collectedClaim:collectedList
                 ) {
                price += collectedClaim.getPrice();
                abjustPrice += collectedClaim.getAdjustPrice();
            }
            collectedClaimView.setActualPrice(price);
            collectedClaimView.setWaitPrice(account.getPrice()-price+abjustPrice);
            collectedClaimView.setAdjustPrice(abjustPrice);

            claimList.add(collectedClaimView);
        }
        return claimList;
    }
    public Map getAllList(String hotelId,String time, Integer pageNo,Integer pageSize,Integer status) throws ParseException {
        String[] totelKey = {"totelRentalOughtPrice","totelRentalActualPrice","totelRentalWaitPrice","totelRentalAdjustPrice","totelWashOughtPrice","totelWashActualPrice","totelWashWaitPrice","totelWashAdjustPrice",
                            "totelDamageOughtPrice","totelDamageActualPrice","totelDamageWaitPrice","totelDamageAdjustPrice","totelPersonnelOughtPrice","totelPersonnelActualPrice","totelPersonnelWaitPrice","totelPersonnelAdjustPrice",
                            "totelConveyOughtPrice","totelConveyActualPrice","totelConveyWaitPrice","totelConveyAdjustPrice","totelCooperationOughtPrice","totelCooperationActualPrice","totelCooperationWaitPrice","totelCooperationAdjustPrice",
                            "totelNightOughtPrice","totelNightActualPrice","totelNightWaitPrice","totelNightAdjustPrice","totelPmsOughtPrice","totelPmsActualPrice","totelPmsWaitPrice","totelPmsAdjustPrice",
                            "totelOtaOughtPrice","totelOtaActualPrice","totelOtaWaitPrice","totelOtaAdjustPrice"};
        String[] pageKey = {"pageRentalOughtPrice","pageRentalActualPrice","pageRentalWaitPrice","pageRentalAdjustPrice","pageWashOughtPrice","pageWashActualPrice","pageWashWaitPrice","pageWashAdjustPrice",
                            "pageDamageOughtPrice","pageDamageActualPrice","pageDamageWaitPrice","pageDamageAdjustPrice","pagePersonnelOughtPrice","pagePersonnelActualPrice","pagePersonnelWaitPrice","pagePersonnelAdjustPrice",
                            "pageConveyOughtPrice","pageConveyActualPrice","pageConveyWaitPrice","pageConveyAdjustPrice","pageCooperationOughtPrice","pageCooperationActualPrice","pageCooperationWaitPrice","pageCooperationAdjustPrice",
                            "pageNightOughtPrice","pageNightActualPrice","pageNightWaitPrice","pageNightAdjustPrice","pagePmsOughtPrice","pagePmsActualPrice","pagePmsWaitPrice","pagePmsAdjustPrice",
                            "pageOtaOughtPrice","pageOtaActualPrice","pageOtaWaitPrice","pageOtaAdjustPrice"};
        String[] listKey = {"rentalOughtPrice","rentalActualPrice","rentalWaitPrice","rentalAdjustPrice","washOughtPrice","washActualPrice","washWaitPrice","washAdjustPrice",
                            "damageOughtPrice","damageActualPrice","damageWaitPrice","damageAdjustPrice","personnelOughtPrice","personnelActualPrice","personnelWaitPrice","personnelAdjustPrice",
                            "conveyOughtPrice","conveyActualPrice","conveyWaitPrice","conveyAdjustPrice","cooperationOughtPrice","cooperationActualPrice","cooperationWaitPrice","cooperationAdjustPrice",
                            "nightOughtPrice","nightActualPrice","nightWaitPrice","nightAdjustPrice","pmsOughtPrice","pmsActualPrice","pmsWaitPrice","pmsAdjustPrice",
                            "otaOughtPrice","otaActualPrice","otaWaitPrice","otaAdjustPrice"};
        String[] key = {"monthTime","hotelId","hotelName","status"};
        List list = new ArrayList();
        Map<String,Object> totel = new HashMap<>();
        Double[] totelprice  = new Double[totelKey.length];
        Double[] pagePrice  = new Double[pageKey.length];
        //总记录
        if(hotelId.equals("0")){
            List<HotelBase> allHotelBase = hotelBaseService.getAllHotelBase();
            for (int i = 0; i < totelprice.length; i++){
                totelprice[i] = 0.00;
            }
            for (HotelBase hb:allHotelBase
                    ) {
                Map<String, Object> allList = getAllList(hb.getId() + "", hb.getHotel_name(), time);
                if (status == allList.get("status")) {
                    if (allList.size() != 0) {
                        for (int i = 0; i < listKey.length; i++) {
                            totelprice[i] += Double.parseDouble(allList.get(listKey[i])+"");
                        }
                    }
                    list.add(allList);
                }
            }
            totel.put(key[2],"合计");
            for (int i = 0; i < totelprice.length; i++){
                totel.put(totelKey[i],totelprice[i]);
            }
        }else{
            HotelBase hb = hotelBaseService.getById(Integer.parseInt(hotelId));
            Map<String, Object> allList = getAllList(hb.getId() + "", hb.getHotel_name(), time);
            for (int i = 0; i < totelprice.length; i++){
                totelprice[i] = 0.00;
            }
            if (status == allList.get("status")) {
                if (allList.size() != 0) {
                    for (int i = 0; i < listKey.length; i++) {
                        totelprice[i] += Double.parseDouble(allList.get(listKey[i])+"");
                    }
                }
                list.add(allList);
            }
            totel.put(key[2],"合计");
            for (int i = 0; i < totelprice.length; i++){
                    totel.put(totelKey[i],totelprice[i]);

            }
        }
        Page<Map> data = PageUtils.getPageList(list, pageNo, pageSize);
        for (int i = 0; i < pageKey.length; i++){
            pagePrice[i] = 0.00;
        }
        for (int i = 0; i < pageKey.length;i++){
            for (Map data1:data.getDataList()
                    ) {
                pagePrice[i] += Double.parseDouble(data1.get(listKey[i])+"");
            }
        }
        Map<String,Object> pagetotel = new HashMap<>();
        pagetotel.put(key[2],"本页合计");
        for (int i = 0; i < totelprice.length; i++){
                pagetotel.put(pageKey[i],pagePrice[i]);

        }
        Map map = new HashMap();
        map.put("data",data);
        map.put("list",list);
        map.put("totel",totel);
        map.put("pagetotel",pagetotel);
        return map;
    }


    //修改
    //获取指定的月的应收和实收及代收金额列表
    public Map<String,Object> getAllList(String hotelId,String hotelName,String time) throws ParseException {
        String[][] accountKey={{"monthTime","hotelId","hotelName","status"},{"rentalOughtPrice","rentalActualPrice","rentalWaitPrice","rentalAdjustPrice"},{"washOughtPrice","washActualPrice","washWaitPrice","washAdjustPrice"},
                {"damageOughtPrice","damageActualPrice","damageWaitPrice","damageAdjustPrice"},{"personnelOughtPrice","personnelActualPrice","personnelWaitPrice","personnelAdjustPrice"},
                {"conveyOughtPrice","conveyActualPrice","conveyWaitPrice","conveyAdjustPrice"},{"cooperationOughtPrice","cooperationActualPrice","cooperationWaitPrice","cooperationAdjustPrice"},
                {"nightOughtPrice","nightActualPrice","nightWaitPrice","nightAdjustPrice"},{"pmsOughtPrice","pmsActualPrice","pmsWaitPrice","pmsAdjustPrice"},
                {"otaOughtPrice","otaActualPrice","otaWaitPrice","otaAdjustPrice"}};
        List<Account> list = accountService.getList(hotelId, time, null);
        Map<String,Object> map = new HashMap<>();
        int flog = 1;
        for (int i = 0; i < accountKey.length; i++){
            if(i!=0){
                map.put(accountKey[i][0],0);
                map.put(accountKey[i][1],0);
                map.put(accountKey[i][2],0);
                map.put(accountKey[i][3],0);
            }
            for (Account account:list
                    ) {
                if(i==0){
                    map.put(accountKey[0][0],time);
                    map.put(accountKey[0][1],account.getHotelId());
                    map.put(accountKey[0][2],hotelName);
                }
                if(i==account.getType()){
                    map.put(accountKey[i][0],account.getPrice());
                    CollectedClaimExample example = new CollectedClaimExample();
                    CollectedClaimExample.Criteria criteria = example.createCriteria();
                    criteria.andAccountIdEqualTo(account.getId());
                    List<CollectedClaim> collectedList = collectedClaimMapper.selectByExample(example);
                    Double price = 0.00;
                    Double abjustPrice = 0.00;
                    for (CollectedClaim collectedClaim:collectedList
                            ) {
                        price += collectedClaim.getPrice();
                        abjustPrice += collectedClaim.getAdjustPrice();
                    }
                    map.put(accountKey[i][1],price);
                    map.put(accountKey[i][2],account.getPrice()-price+abjustPrice);
                    map.put(accountKey[i][3],abjustPrice);
                    if(!map.get(accountKey[i][2]).toString().equals("0.0")){
                        flog = 0;
                    }
                }
                map.put(accountKey[0][3],flog);
            }

        }
        return map;
    }

}
