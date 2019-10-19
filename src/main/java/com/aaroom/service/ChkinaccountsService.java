package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.persistence.ChargeConfigAttachMapper;
import com.aaroom.persistence.ChargeConfigMapper;
import com.aaroom.persistence.ChargeRenewConfigMapper;
import com.aaroom.persistence.ChkinaccountsMapper;
import com.shangmei.persistence.ShangmeiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 温建成 on 2019/1/22.
 */
@Service
public class ChkinaccountsService {

    @Autowired
    private HotelBaseService hotelBaseService;

    @Autowired
    private ChkinaccountsMapper chkinaccountsMapper;

    @Autowired
    private ShangmeiMapper shangmeiMapper;

    @Autowired
    private ChargeConfigMapper chargeConfigMapper;

    @Autowired
    private ChargeConfigAttachMapper chargeConfigAttachMapper;

    @Autowired
    private ChargeRenewConfigMapper chargeRenewConfigMapper;

    private static final String AA_BOOKSOURCE = "581c79335aa411e598f2d8cb8a2f9c07";

    //定时抓取数据
    @Scheduled(cron = "0 0 2 * * ? ")
    public void fetchChkinaccountsJob(){
        System.out.println("任务执行中。。。");
        List<HotelBase> allHotelBase = hotelBaseService.getAllHotelBase();
        Calendar cal = Calendar.getInstance();
        Date createTime = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date toDateTime = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date fromDateTime = cal.getTime();
        List<Chkinaccounts> chkinAccountsList = shangmeiMapper.getChkinAccountsList(allHotelBase, AA_BOOKSOURCE, fromDateTime, toDateTime);
        for (Chkinaccounts chkinaccount:chkinAccountsList
                ) {
            chkinaccountsMapper.insertSelective(chkinaccount);
        }
        System.out.println("任务结束。。。");
    }
    //获取上个月的总金额
    public double getChkinaccountsDouble(String chkinType,String customeType,int id,String shopId,int flog){
        Calendar cal = Calendar.getInstance();
        Date createTime = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        Date toDateTime = cal.getTime();
        cal.add(Calendar.MONTH,-1);
        Date fromDateTime = cal.getTime();
        ChkinaccountsExample example = new ChkinaccountsExample();
        ChkinaccountsExample.Criteria criteria = example.createCriteria();
        if(!chkinType.equals("null")){
            criteria.andChkintypeEqualTo(Integer.parseInt(chkinType));
        }
        if(!customeType.equals("null")){
            criteria.andCustometypeEqualTo(Integer.parseInt(customeType));
        }
        criteria.andShopidEqualTo(shopId);
        criteria.andOvertimeBetween(fromDateTime,toDateTime);
        List<Chkinaccounts> list = chkinaccountsMapper.selectByExample(example);
        BigDecimal bigDecimal = new BigDecimal(0);

        ChargeConfigAttachExample example1 = new ChargeConfigAttachExample();
        ChargeConfigAttachExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andHotelIdEqualTo(id);
        List<ChargeConfigAttach> chargeConfigAttaches = chargeConfigAttachMapper.selectByExample(example1);
        BigDecimal bigDecimals = new BigDecimal(0);
        if (chargeConfigAttaches.size()!=0){
            ChargeConfigAttach chargeConfigAttach = chargeConfigAttaches.get(0);
            ChargeRenewConfigExample example2 = new ChargeRenewConfigExample();
            ChargeRenewConfigExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andHotelIdEqualTo(id);
            criteria2.andBeginDateBetween(chargeConfigAttach.getStartDate(),chargeConfigAttach.getEndDate());
            List<ChargeRenewConfig> chargeRenewConfigs = chargeRenewConfigMapper.selectByExample(example2);
            for (ChargeRenewConfig chargeRenewConfig:chargeRenewConfigs
                    ) {
                for (ChargeRenewConfig chargeRenewConfig1:chargeRenewConfigs
                        ) {
                    if(chargeRenewConfig.getBeginDate().before(chargeRenewConfig1.getBeginDate())|| chargeRenewConfig.getBeginDate().equals(chargeRenewConfig1.getBeginDate())){
                        for (Chkinaccounts chkinaccount: list
                                ) {
                            if(chkinaccount.getOvertime().before(chargeRenewConfig.getBeginDate())||chkinaccount.getOvertime().after(chargeRenewConfig1.getBeginDate())){
                                ChargeConfig chargeConfig = chargeConfigMapper.selectByPrimaryKey(chargeRenewConfig.getChargeConfigId());
                                if(flog==chargeConfig.getChargeItem()){
                                    BigDecimal bigDecimal1 = new BigDecimal(chargeConfig.getChargeRule());
                                    BigDecimal multiply = chkinaccount.getTotelfee().multiply(bigDecimal1);
                                    BigDecimal divide = multiply.divide(new BigDecimal(100));
                                    bigDecimals = bigDecimals.add(divide);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return bigDecimals.doubleValue();
    }
}
