package com.aaroom.service;


import com.aaroom.beans.*;
import com.aaroom.model.ClothErrorView;
import com.aaroom.persistence.AccountMapper;

import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 温建成 on 2019/1/4.
 */
@Service
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(FinanceService.class);
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private ClothErrorService clothErrorService;
    @Autowired
    private HotelBaseService hotelBaseService;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private ChkinaccountsService chkinaccountsService;

    //根据传递的参数获取指定的账单
    public List<Account> getList(String hotelId, String time,Integer type) throws ParseException {
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        if(!hotelId.equals("0")){
            criteria.andHotelIdEqualTo(Integer.parseInt(hotelId));
        }
        if(!time.equals("null")){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
            Date parse = sdf1.parse(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01  00:00:00");
            String format = sdf.format(parse);
            Date date = sdf.parse(format);
            criteria.andBeginTimeEqualTo(date);
        }
        if(type!=null){
              criteria.andTypeEqualTo(type);
        }
        List<Account> list = accountMapper.selectByExample(example);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Account account:list
                ) {
            try {
                Date date1 = sdf2.parse(account.getBeginTime()+"");
                account.setBeginTime(sdf2.format(date1));
                Date date2 = sdf2.parse(account.getEndTime()+"");
                account.setEndTime(sdf2.format(date2));
            } catch (ParseException e) {
                log.error("日期parse失败。");
                e.printStackTrace();
            }
        }
        return list;
    }

    //根据酒店的id和时间查询对应该酒店的所有的账单
    public List<Account> getAllList(String hotelId, String time) throws ParseException {
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        if(!hotelId.equals("0")){
            criteria.andHotelIdEqualTo(Integer.parseInt(hotelId));
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        Date parse = sdf1.parse(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01  00:00:00");
        String format = sdf.format(parse);
        Date date = sdf.parse(format);
        criteria.andBeginTimeEqualTo(date);
        List<Account> list = accountMapper.selectByExample(example);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Account account:list
                ) {
            try {
                Date date1 = sdf2.parse(account.getBeginTime()+"");
                account.setBeginTime(sdf2.format(date1));
                Date date2 = sdf2.parse(account.getEndTime()+"");
                account.setEndTime(sdf2.format(date2));
            } catch (ParseException e) {
                log.error("日期parse失败。");
                e.printStackTrace();
            }
        }
        return list;
    }
    //根据传递的参数获取指定的账单
    public List<Account> getList(String hotelId, String time,Integer type,Integer type2) throws ParseException {
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        if(!hotelId.equals("0")){
            criteria.andHotelIdEqualTo(Integer.parseInt(hotelId));
        }
        if(!time.equals("null")){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
            Date parse = sdf1.parse(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01  00:00:00");
            String format = sdf.format(parse);
            Date date = sdf.parse(format);
            criteria.andBeginTimeEqualTo(date);
        }
        if(type!=null&&type2!=null){
            criteria.andTypeBetween(type,type2);
        }
        List<Account> list = accountMapper.selectByExample(example);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Account account:list
                ) {
            try {
                Date date1 = sdf2.parse(account.getBeginTime()+"");
                account.setBeginTime(sdf2.format(date1));
                Date date2 = sdf2.parse(account.getEndTime()+"");
                account.setEndTime(sdf2.format(date2));
            } catch (ParseException e) {
                log.error("日期parse失败。");
                e.printStackTrace();
            }
        }
        return list;
    }

    //定时对上个月的所有账单条目进行总计并保存到涨到中
    @Transactional
    public void insertAccount(){
        List<HotelBase> hotelBaseList = hotelBaseService.getAllHotelBase();
        Map date = CommonUtil.getSimpleDate();
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            criteria.andBeginTimeEqualTo(simpleDateFormat.parse(date.get("startDate")+""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Account> accounts = accountMapper.selectByExample(example);
    //    if(accounts.size()==0){
            for (HotelBase hotelBase: hotelBaseList
                    ) {
                double damagePrice = 0;
                double rentalPrice = 0;
                double washPrice = 0;
                double personnelPrice = 0;
                Integer id = hotelBase.getId();
                //租赁
                Map<String, Object> rentalList = financeService.getRentalList(id , date.get("startDate") + "", date.get("endDate") + "", 1, 1);
                if(rentalList.size()!=0){
                    rentalPrice = (double)rentalList.get("total");
                }
                Account account = new Account();
                if(rentalPrice==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                account.setHotelId(id);
                account.setPrice(rentalPrice);
                account.setUpdatePrice(rentalPrice);
                account.setBeginTime(date.get("startDate")+"");
                account.setEndTime(date.get("endDate")+"");
                account.setType(1);
                accountMapper.insertSelective(account);
                //洗涤
                Map<String, Object> washList = financeService.getWashList(id, "", date.get("startDate") + "", date.get("endDate") + "", 1, 1);
                if(washList.size()!=0){
                    washPrice = (double)washList.get("collecttotal");
                }
                if(washPrice==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                account.setPrice(washPrice);
                account.setUpdatePrice(washPrice);
                account.setType(2);
                accountMapper.insertSelective(account);

                //报损
                List<ClothErrorView> clothErrorListSum = clothErrorService.getClothErrorListSum(id, date.get("startDate") + "", date.get("endDate") + "");
                if(clothErrorListSum.size()!=0){
                    for (ClothErrorView cev : clothErrorListSum) {
                        damagePrice += cev.getSuggest_pay();
                    }
                }
                if(damagePrice==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                account.setPrice(damagePrice);
                account.setUpdatePrice(damagePrice);
                account.setType(3);
                accountMapper.insertSelective(account);
                //人力
                Map<String, Object> employeeList = financeService.getMissionList("", id + "", date.get("startDate") + "", date.get("endDate") + "", 1, 1);
                List list = (List) employeeList.get("list");
                if(list.size()!=0){
                    Map<String,Object> totel = (Map<String,Object>) employeeList.get("totel");
                    Object totelPrice = totel.get("totelPrice");
                    personnelPrice =Double.parseDouble(totelPrice.toString());
                }
                if(personnelPrice==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                account.setPrice(personnelPrice);
                account.setUpdatePrice(personnelPrice);
                account.setType(4);
                accountMapper.insertSelective(account);
                //输送客源  会员的房费收入
                double conveyPrice = chkinaccountsService.getChkinaccountsDouble("null", 1 + "",id, hotelBase.getSmhotel_id(),3);
                if(conveyPrice==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                account.setPrice(conveyPrice);
                account.setUpdatePrice(conveyPrice);
                account.setType(5);
                accountMapper.insertSelective(account);
                //合作服务费 房费收入
                double cooperationPrice = chkinaccountsService.getChkinaccountsDouble("null", "null" + "",id, hotelBase.getSmhotel_id(),4);
                if(cooperationPrice==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                account.setPrice(cooperationPrice);
                account.setUpdatePrice(cooperationPrice);
                account.setType(6);
                accountMapper.insertSelective(account);
                //间夜数  房费的收入
                double nightPrice = chkinaccountsService.getChkinaccountsDouble(1+"","null",id, hotelBase.getSmhotel_id(),5);
                if(nightPrice==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                account.setPrice(nightPrice);
                account.setUpdatePrice(nightPrice);
                account.setType(7);
                accountMapper.insertSelective(account);
            }
       // }
    }
    //手动的生成账单
    public Integer insertAccounts(List<AccountViews> list){
        Account account = new Account();
        for ( AccountViews avs:list
             ) {
            HotelBase hotelBase = hotelBaseService.getById(avs.getHotelId());
            if(hotelBase==null){
                return 1;
            }
            for (int i = 0; i < avs.getMonthNum();i++){
                account.setHotelId(avs.getHotelId());
                Calendar cal = Calendar.getInstance();
                Date createTime = cal.getTime();
                cal.setTime(avs.getMonthTime());
                cal.add(Calendar.MONTH,i);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                cal.set(Calendar.DAY_OF_MONTH,1);
                Date fromDateTime = cal.getTime();
                cal.add(Calendar.MONTH,1);
                cal.add(Calendar.MILLISECOND,-1);
                Date toDateTime = cal.getTime();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                account.setBeginTime(sdf2.format(fromDateTime));
                account.setEndTime(sdf2.format(toDateTime));
                account.setType(avs.getType());
                account.setPrice(avs.getPrice());
                account.setUpdatePrice(avs.getPrice());
                if(avs.getPrice()==0){
                    account.setStatus(1);
                }else{
                    account.setStatus(0);
                }
                accountMapper.insertSelective(account);
            }
        }
        return 0;
    }

    //查询记录封装到Accountview中将四条数据合并
    public Map<String,Object> getExportAccountList(String hotelId, String time, Integer pageSize, Integer pageNo) throws Exception {
        Double totelRentalPrice = 0.00;
        Double totelWashPrice = 0.00;
        Double totelDamagePrice = 0.00;
        Double totelPersonnelPrice = 0.00;
        Double pageRentalPrice = 0.00;
        Double pageWashPrice = 0.00;
        Double pageDamagePrice = 0.00;
        Double pagePersonnelPrice = 0.00;
        List<Account> list1 = getList(hotelId, time, 1);
        List<Account> list2 = getList(hotelId, time, 2);
        List<Account> list3 = getList(hotelId, time, 3);
        List<Account> list4 = getList(hotelId, time, 4);
        int size = list1.size();
        List<AccountView> list = new ArrayList<>();
        for (int i = 0 ; i < size ; i++){
            Account rentalAccount = list1.get(i);
            Account washAccount = list2.get(i);
            Account damageAccount = list3.get(i);
            Account personnelAccount = list4.get(i);
            AccountView accountView = new AccountView();
            accountView.setId(rentalAccount.getId());
            accountView.setHotelId(rentalAccount.getHotelId());
            HotelBase hotel = hotelBaseService.getById(rentalAccount.getHotelId());
            accountView.setHotelName(hotel.getHotel_name());
            accountView.setRentalPrice(rentalAccount.getPrice());
            accountView.setWashPrice(washAccount.getPrice());
            accountView.setDamagePrice(damageAccount.getPrice());
            accountView.setPersonnelPrice(personnelAccount.getPrice());
            accountView.setBeginTime(personnelAccount.getBeginTime());
            accountView.setEndTime(personnelAccount.getEndTime());
            list.add(accountView);
            totelRentalPrice += accountView.getRentalPrice();
            totelWashPrice += accountView.getWashPrice();
            totelDamagePrice += accountView.getDamagePrice();
            totelPersonnelPrice += accountView.getPersonnelPrice();
        }
        Map<String,Object> totel = new HashMap<>();
        totel.put("totelRentalPrice",totelRentalPrice);
        totel.put("totelWashPrice",totelWashPrice);
        totel.put("totelDamagePrice",totelDamagePrice);
        totel.put("totelPersonnelPrice",totelPersonnelPrice);
        //分页
        Page<AccountView> data = PageUtils.getPageList(list, pageNo, pageSize);
        for (AccountView accountView:data.getDataList()
                ) {
            pageRentalPrice += accountView.getRentalPrice();
            pageWashPrice += accountView.getWashPrice();
            pageDamagePrice += accountView.getDamagePrice();
            pagePersonnelPrice += accountView.getPersonnelPrice();
        }
        Map<String,Object> pagetotel = new HashMap<>();
        pagetotel.put("pageRentalPrice",pageRentalPrice);
        pagetotel.put("pageWashPrice",pageWashPrice);
        pagetotel.put("pageDamagePrice",pageDamagePrice);
        pagetotel.put("pagePersonnelPrice",pagePersonnelPrice);
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("data",data);
        map.put("totel",totel);
        map.put("pagetotel",pagetotel);
        return map;
    }

    //将想要的账单根据条件查询
    public Map<String, Object> getAllExportAccountList(String hotelId, String time, Integer pageSize, Integer pageNo) throws ParseException {
        String[] totelKey = {"","totelRentalPrice","totelWashPrice","totelDamagePrice","totelPersonnelPrice","totelConveyPrice","totelCooperationPrice","totelNightPrice","totelPmsPrice","totelOtaPrice"};
        String[] pageKey = {"","pageRentalPrice","pageWashPrice","pageDamagePrice","pagePersonnelPrice","pageConveyPrice","pageCooperationPrice","pageNightPrice","pagePmsPrice","pageOtaPrice"};
        String[] listKey = {"","rentalPrice","washPrice","damagePrice","personnelPrice","conveyPrice","cooperationPrice","nightPrice","pmsPrice","otaPrice"};
        String[] key = {"id","hotelId","hotelName","beginTime","endTime"};
        List<Map<String,Object>> list = new ArrayList<>();
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
                List<Account> allList = getAllList(hb.getId()+"", time);
                Map<String,Object> map = new HashMap<>();
                if(allList.size()!=0){
                    for ( int i = 0; i < listKey.length; i++) {
                        map.put(listKey[i],0);
                        for (Account account:allList
                                ) {
                            if(i==0){
                                map.put(key[0],account.getId());
                                map.put(key[1],account.getHotelId());
                                map.put(key[2],hb.getHotel_name());
                                map.put(key[3],account.getBeginTime());
                                map.put(key[4],account.getEndTime());
                            }else{
                                if(account.getType()==i){
                                    map.put(listKey[i],Double.parseDouble(map.get(listKey[i])+"")+account.getPrice());
                                    totelprice[i]+=account.getPrice();
                                }
                            }
                        }
                    }
                }
                if(map.size()!=0){
                    list.add(map);
                }
            }
            for (int i = 0; i < totelprice.length; i++){
                if(i==0){
                    totel.put(key[2],"合计");
                }else{
                    totel.put(totelKey[i],totelprice[i]);
                }
            }
        }else{
            List<Account> allList = getAllList(hotelId, time);
            HotelBase hotelBase = hotelBaseService.getById(Integer.parseInt(hotelId));
            Map<String,Object> map = new HashMap<>();
            for (int i = 0; i < totelprice.length; i++){
                totelprice[i] = 0.00;
            }
            if(allList.size()!=0){
                for ( int i = 0; i < listKey.length; i++) {
                    map.put(listKey[i],0);
                    for (Account account:allList
                            ) {
                        if(i==0){
                            map.put(key[0],account.getId());
                            map.put(key[1],account.getHotelId());
                            map.put(key[2],hotelBase.getHotel_name());
                            map.put(key[3],account.getBeginTime());
                            map.put(key[4],account.getEndTime());
                        }else{
                            if(account.getType()==i){
                                map.put(listKey[i],0);
                                map.put(listKey[i],Double.parseDouble(map.get(listKey[i])+"")+account.getPrice());
                                totelprice[i]+=account.getPrice();
                            }
                        }
                    }
                }
            }
            if(map.size()!=0){
                list.add(map);
            }
            for (int i = 0; i < totelprice.length; i++){
                if(i==0){
                    totel.put(key[2],"合计");
                }else{
                    totel.put(totelKey[i],totelprice[i]);
                }
            }
        }
        //分页
        Page<Map> data = PageUtils.getPageList(list, pageNo, pageSize);
        for (int i = 0; i < pageKey.length; i++){
            pagePrice[i] = 0.00;
        }
        for (int i = 1; i < pageKey.length-2;i++){
            for (Map data1:data.getDataList()
                    ) {
                pagePrice[i] += Double.parseDouble(data1.get(listKey[i])+"");
            }
        }
        Map<String,Object> pagetotel = new HashMap<>();
        for (int i = 0; i < totelprice.length; i++){
            if(i==0){
                pagetotel.put(key[2],"本页合计");
            }else{
                pagetotel.put(pageKey[i],pagePrice[i]);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("data",data);
        map.put("totel",totel);
        map.put("pagetotel",pagetotel);
        return map;
    }

    //根据酒店ID查询该酒店各个账单总和
    public List<Account> getAccountSum(Integer hotelId) {
        return accountMapper.getAccountSum(hotelId);
    }

    //获取该账单信息
    public Account getAccountInfo(Integer id) {
        return accountMapper.getAccountInfo(id);
    }

    //更新账单信息
    public void updateAccountStatus(Account account) {
        accountMapper.updateAccountStatus(account);
    }
    //展示新增账单
    public Map<String, Object> getAllHistoryList(String hotelId,Integer pageNo,Integer pageSize) throws ParseException {
        List<Account> list = getList(hotelId, "null", 8,9);
        Collections.reverse(list);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        for (Account account:list
             ) {
            account.setBeginTime(sdf1.format(sdf.parse(account.getBeginTime())));
        }
        Page<Map> data = PageUtils.getPageList(list, pageNo, pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("data",data);
        return map;
    }

}
