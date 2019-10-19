package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.model.FinanceListView;
import com.aaroom.model.IntentionStoreInfoView;
import com.aaroom.persistence.FinanceMapper;
import com.aaroom.persistence.HotelBaseMapper;
import com.aaroom.persistence.IntentionStoreMapper;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2019-02-18 16:36
 **/

@Service
public class IntentionStoreService {

    @Autowired
    private IntentionStoreMapper intentionStoreMapper;

    @Autowired
    private HotelBaseMapper hotelBaseMapper;

    @Autowired
    private FinanceMapper financeMapper;

    @Autowired
    private StorageService storageService;

    public Map<String, Object> addIntentionStoreSurvey(IntentionStoreSurvey intentionStoreSurvey) {
        HotelBase hotelBase = null;
        Map<String, Object> map = new HashMap<>();
        //保存酒店信息
        /*hotelBaseMapper.saveTemp(intentionStoreSurveyView.getHotelBase());
        hotelBase = hotelBaseMapper.getHotelTempByName(intentionStoreSurveyView.getHotelBase().getHotel_name());
        if (hotelBase == null) {
            map.put("status", "fail");
            map.put("msg", "添加酒店信息失败！");
            return map;
        }*/
        try {
            IntentionStoreSurvey intentionStoreSurvey1 = intentionStoreMapper.getIntentionStoreByHotelName(intentionStoreSurvey.getHotelName());
            if (intentionStoreSurvey1 == null) {
                intentionStoreSurvey.setId(CommonUtil.createUUID());
                if (intentionStoreSurvey.getType() == 1) {
                    intentionStoreSurvey.setStatus(1);
                } else {
                    intentionStoreSurvey.setStatus(2);
                }
                intentionStoreMapper.addIntentionStoreSurvey(intentionStoreSurvey);
            } else {
                return resultMap("fail", "酒店名称已存在，请修改后在添加！");
            }
        } catch (Exception e) {
            //hotelBaseMapper.removeByIdTemp(hotelBase.getId());
            e.printStackTrace();
            return resultMap("fail", "添加信息失败！");
        }
        return resultMap("success", "添加信息成功！");
    }

    public IntentionStoreSurvey  getIntentionStoreByHotelName(String hotelName) {
        IntentionStoreSurvey intentionStoreSurvey = intentionStoreMapper.getIntentionStoreByHotelName(hotelName);
        return intentionStoreSurvey;
    }

    public Map<String, Object> resultMap(String value1, String value2) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", value1);
        map.put("msg", value2);
        return map;
    }

    @Transactional
    public Map<String, Object> addIntentionStoreInfo(IntentionStoreInfoView intentionStoreInfoView) {
        financeMapper.saveChargeConfigIntetionStore(intentionStoreInfoView.getSurveyId(), intentionStoreInfoView.getChargeConfig());
        List<ChargeConfig> list1 = financeMapper.getChargeConfigIntentionStore(intentionStoreInfoView.getSurveyId());
        for (ChargeConfig chargeConfig : intentionStoreInfoView.getChargeConfig()) {
            for (ChargeConfig chargeConfig1 : list1) {
                if (chargeConfig.getChargeItem() == chargeConfig1.getChargeItem()) {
                    ChargeRenewConfig chargeRenewConfig = new ChargeRenewConfig();
                    chargeRenewConfig.setCharge(chargeConfig1.getChargeRule());
                    chargeRenewConfig.setChargeConfigId(chargeConfig1.getId());
                    chargeRenewConfig.setChargeRate(chargeConfig.getChargeRate());
                    financeMapper.saveChargeRenewConfig(chargeRenewConfig);
                }
            }
        }
        intentionStoreInfoView.getIntentionStoreInfo().setSurveyId(intentionStoreInfoView.getSurveyId());
        String isiId = CommonUtil.createUUID();
        intentionStoreInfoView.getIntentionStoreInfo().setId(isiId);
        IntentionStoreInfo intentionStoreInfo = intentionStoreInfoView.getIntentionStoreInfo();
        StringBuffer sb = new StringBuffer();
        String[] strings = intentionStoreInfo.getGiveMaterial();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]+",");
        }
        intentionStoreInfo.setGiveMaterials(sb.toString());
        intentionStoreMapper.saveIntentionStoreInfo(intentionStoreInfoView.getIntentionStoreInfo());
        if (intentionStoreInfoView.getIsiBed().size() != 0) {
            intentionStoreMapper.saveIntentionStoreInfoBed(isiId, intentionStoreInfoView.getIsiBed());
        }
        return resultMap("success", "添加成功！");
    }

    public Map<String, Object> getIntentionStoreData(String hotelName, String owernName, Integer status, int pageNo, int pageSize) {
        List<IntentionStoreSurvey> list = intentionStoreMapper.getIntentionStoreData(hotelName, owernName, status);
        List<IntentionStoreSurvey> list1 = new ArrayList<>();
        for (IntentionStoreSurvey intentionStoreSurvey : list) {
            Province province = intentionStoreMapper.getProvinceInfo(intentionStoreSurvey.getHotelProvince());
            City city = intentionStoreMapper.getCityInfo(intentionStoreSurvey.getHotelCity());
            County county = intentionStoreMapper.getCountyInfo(intentionStoreSurvey.getHotelCounty());
            intentionStoreSurvey.setHotelProvince(province.getName());
            if (city != null) intentionStoreSurvey.setHotelCity(city.getName());
            else intentionStoreSurvey.setHotelCity("");
            if (county != null ) intentionStoreSurvey.setHotelCounty(county.getName());
            else intentionStoreSurvey.setHotelCounty("");
            list1.add(intentionStoreSurvey);
        }
        Page<FinanceListView> page = PageUtils.getPageList(list1, pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("data", page);
        map.put("dataAll", list1);
        return map;
    }

    public IntentionStoreSurvey getIntentionStoreSurvey(String surveyId) {
        IntentionStoreSurvey intentionStoreSurvey = intentionStoreMapper.getIntentionStoreSurvey(surveyId);
        intentionStoreSurvey.setDoorwayPhoto(storageService.generatePresignUrl(intentionStoreSurvey.getDoorwayPhoto()) + "&" + intentionStoreSurvey.getDoorwayPhoto() );
        intentionStoreSurvey.setOwernIdcardPhotoz(storageService.generatePresignUrl(intentionStoreSurvey.getOwernIdcardPhotoz()) + "&" + intentionStoreSurvey.getOwernIdcardPhotoz());
        intentionStoreSurvey.setOwernIdcardPhotof(storageService.generatePresignUrl(intentionStoreSurvey.getOwernIdcardPhotof()) + "&" + intentionStoreSurvey.getOwernIdcardPhotof());
        return intentionStoreSurvey;
    }

    public Map<String,Object> updateIntentionStoreSurvey(IntentionStoreSurvey intentionStoreSurvey) {
        try {
            IntentionStoreSurvey intentionStoreSurvey1 = intentionStoreMapper.getIntentionStoreByHotelName(intentionStoreSurvey.getHotelName());
            if ((intentionStoreSurvey1 != null && intentionStoreSurvey1.getId().equals(intentionStoreSurvey.getId())) || intentionStoreSurvey1 == null) {
                if (intentionStoreSurvey.getType() == 1) {
                    intentionStoreSurvey.setStatus(1);
                } else {
                    intentionStoreSurvey.setStatus(2);
                }
                intentionStoreMapper.updateIntentionStoreSurvey(intentionStoreSurvey);
            } else {
                return resultMap("fail", "酒店名称已存在，请修改后在保存！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap("fail", "修改信息失败！");
        }
        return resultMap("success", "修改信息成功！");
    }

    public Map<String,Object> addIntentionStoreContract(IntentionStoreContract intentionStoreContract) {
        try {
            intentionStoreContract.setId(CommonUtil.createUUID());
            intentionStoreMapper.addIntentionStoreContract(intentionStoreContract);
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap("fail", "添加信息失败！");
        }
        return resultMap("success", "添加信息成功！");
    }
    public IntentionStoreInfoView getIntentionStoreInfo(String surveyId) {
        IntentionStoreInfo intentionStoreInfo =  intentionStoreMapper.getIntentionStoreInfo(surveyId);
        List<IsiBed> list1 = null;
        if (intentionStoreInfo != null && intentionStoreInfo.getGiveMaterials() != null) {
            String[] strings = intentionStoreInfo.getGiveMaterials().split(",");
            intentionStoreInfo.setGiveMaterial(strings);
            list1 = intentionStoreMapper.getIsiBedByIsiId(intentionStoreInfo.getId());
        }
        List<ChargeConfig> list = financeMapper.getChargeConfigBySurveyId(surveyId);
        IntentionStoreInfoView intentionStoreInfoView = new IntentionStoreInfoView();
        intentionStoreInfoView.setIntentionStoreInfo(intentionStoreInfo);
        intentionStoreInfoView.setChargeConfig(list);
        intentionStoreInfoView.setIsiBed(list1);
        return intentionStoreInfoView;
    }

    public IntentionStoreContract getIntentionStoreContract(String isiId) {
        return intentionStoreMapper.getIntentionStoreContract(isiId);
    }

    public Map<String,Object> updateIntentionStoreContract(IntentionStoreContract intentionStoreContract) {
        try {
            intentionStoreMapper.updateIntentionStoreContract(intentionStoreContract);
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap("fail", "修改信息失败！");
        }
        return resultMap("success", "修改信息成功！");
    }

    @Transactional
    public Map<String,Object> updateIntentionStoreInfo(IntentionStoreInfoView intentionStoreInfoView) {
        intentionStoreMapper.updateIntentionStoreInfo(intentionStoreInfoView.getIntentionStoreInfo());
        List<ChargeConfig> list = intentionStoreInfoView.getChargeConfig();
        for (ChargeConfig chargeConfig : list) {
            financeMapper.updateChargeConfig(chargeConfig.getId(), chargeConfig.getChargeRule(), chargeConfig.getReduction());
            financeMapper.updateChargeRenewConfigByCcId(chargeConfig.getId(), chargeConfig.getChargeRule());//根据收费配置ID修改
        }
        List<IsiBed> list1 = intentionStoreInfoView.getIsiBed();
        for (IsiBed isiBed : list1) {
            intentionStoreMapper.updateIsiBed(isiBed);
        }
        return resultMap("success", "修改信息成功！");
    }
}

