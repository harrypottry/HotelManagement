package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.model.ChargeConfigView;
import com.aaroom.model.ClaimImportExcelView;
import com.aaroom.model.ClaimOptionsView;
import com.aaroom.model.FinanceListView;
import com.aaroom.persistence.FinanceMapper;
import com.aaroom.persistence.MissionMapper;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Constants;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description:
 * @create: 2018-12-25 11:36
 **/
@Service
public class FinanceService {

    private final Logger log = LoggerFactory.getLogger(FinanceService.class);

    @Autowired
    private FinanceMapper finceMapper;

    @Autowired
    private ClothService clothService;

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private HotelBaseService hotelBaseService;

    //获取租赁费用列表
    public Map<String, Object> getRentalList(Integer hotelId, String beginDate, String endDate, int pageNo, int pageSize) {
        List<FinanceListView> list = finceMapper.getRentalList(2, hotelId, beginDate, endDate, 1);
        List<FinanceListView> list1 = finceMapper.getRentalList(2, hotelId, beginDate, endDate, 0);
        List<FinanceListView> listAll = getList(list, list1);
        Page<FinanceListView> page = PageUtils.getPageList(listAll, pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        Double total = 0.0;
        Double pagetotal = 0.0;
        for (FinanceListView flv : listAll) {
            total += flv.getRentalPrice() * 100;
        }
        for (FinanceListView flv : page.getDataList()) {
            pagetotal += flv.getRentalPrice() * 100;
        }
        map.put("data", page);
        map.put("total", total/100);
        map.put("pagetotal", pagetotal/100);
        map.put("dataAll", listAll);
        return map;
    }

    //获取洗涤费用列表
    public Map<String, Object> getWashList(Integer hotelId, String employeeId, String beginDate, String endDate, int pageNo, int pageSize) {
        List<FinanceListView> list = finceMapper.getWashList(3, hotelId, employeeId, beginDate, endDate, 1);
        List<FinanceListView> list1 = finceMapper.getWashList(3, hotelId, employeeId, beginDate, endDate, 0);
        List<FinanceListView> listAll = getList(list, list1);
        Page<FinanceListView> page = PageUtils.getPageList(listAll, pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        Double collecttotal = 0.0, pagecollecttotal = 0.0, paytotal = 0.0, pagepaytotal = 0.0;
        for (FinanceListView flv : listAll) {
            collecttotal += flv.getCollectWashPrice() * 100;
            paytotal += flv.getPayWashPrice() * 100;
        }
        for (FinanceListView flv : page.getDataList()) {
            pagecollecttotal += flv.getCollectWashPrice() * 100;
            pagepaytotal += flv.getPayWashPrice() * 100;
        }
        map.put("collecttotal", collecttotal/100);
        map.put("pagecollecttotal", pagecollecttotal/100);
        map.put("paytotal", paytotal/100);
        map.put("pagepaytotal", pagepaytotal/100);
        map.put("data", page);
        map.put("dataAll", listAll);
        return map;
    }

    //TODO
    public List<FinanceListView> getList(List<FinanceListView> listF, List<FinanceListView> listD) {
        List<FinanceListView> list = new ArrayList<>();
        for (FinanceListView financeListView : listF) {
            for (FinanceListView financeListView1 : listD) {
                if (financeListView.getUuid().equals(financeListView1.getUuid())) {
                    financeListView1.setEndDate(financeListView1.getBeginDate());
                    financeListView1.setBeginDate(financeListView.getBeginDate());
                    //获取cloth基本信息
                    ClothView cv = clothService.getClothView(Integer.parseInt(financeListView.getClothId()));
                    List<ClothType> list2 = cv.getClothTypes();
                    for (ClothType ct : list2) {
                        if (Constants.ClothKind.Type == ct.getCloth_kind()) {
                            financeListView1.setClothName(ct.getDesc());
                        } else if (Constants.ClothKind.Size == ct.getCloth_kind()) {
                            financeListView1.setClothSize(ct.getDesc());
                        } else if (Constants.ClothKind.Material == ct.getCloth_kind()) {
                            financeListView1.setClothMaterial(ct.getDesc());
                        }
                    }
                    list.add(financeListView1);
                }
            }
        }
        return list;
    }

    //人力列表
    public Map<String, Object> getMissionList(String employeeId, String hotelId, String beginDate, String endDate, Integer pageNo, Integer pageSize) {
        List<MissionPriceView> list = missionMapper.getMissionList(employeeId, hotelId, beginDate, endDate);
        Double totelPrice = 0.00;
        Double totelReworkPrice = 0.00;
        Double totelCleanPrice = 0.00;
        Double pagePrice = 0.00;
        Double pageReworkPrice = 0.00;
        Double pageCleanPrice = 0.00;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (MissionPriceView mpv : list
                ) {
            if (!mpv.getIsOk().equals("2")) {
                mpv.setReworkPrice(0);
            }
            try {
                Date date = simpleDateFormat.parse(mpv.getBeginTime());
                mpv.setBeginTime(simpleDateFormat.format(date));
                Date date1 = simpleDateFormat.parse(mpv.getEndTime());
                mpv.setEndTime(simpleDateFormat.format(date1));
            } catch (ParseException e) {
                log.error("日期parse失败。");
                e.printStackTrace();
            }
            totelPrice += mpv.getPrice();
            totelReworkPrice += mpv.getReworkPrice();
            totelCleanPrice += mpv.getCleanPrice();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        Map<String, Object> totel = new HashMap<>();
        totel.put("totelPrice", df.format(totelPrice));
        totel.put("totelReworkPrice", df.format(totelReworkPrice));
        totel.put("totelCleanPrice", df.format(totelCleanPrice));
        totel.put("totelSumPrice", df.format(totelCleanPrice - totelReworkPrice));
        Page<MissionPriceView> page = PageUtils.getPageList(list, pageNo, pageSize);
        Map<String, Object> pagetotel = new HashMap<>();
        for (MissionPriceView mpv : page.getDataList()
                ) {
            pagePrice += mpv.getPrice();
            pageReworkPrice += mpv.getReworkPrice();
            pageCleanPrice += mpv.getCleanPrice();
        }
        pagetotel.put("pagePrice", df.format(pagePrice));
        pagetotel.put("pageReworkPrice", df.format(pageReworkPrice));
        pagetotel.put("pageCleanPrice", df.format(pageCleanPrice));
        pagetotel.put("pageTotelSumPrice", df.format(pageCleanPrice - pageReworkPrice));

        Map<String, Object> map = new HashMap<>();
        map.put("data", page);
        map.put("list", list);
        map.put("totel", totel);
        map.put("pagetotel", pagetotel);
        return map;
    }

    //导入银行账单数据
    @Transactional
    public int saveClaimImportData(List<ClaimImportExcelView> list) {
        List<ClaimAccount> list1 = new ArrayList<>();
        List<String> existedList = new ArrayList();
        for (ClaimImportExcelView ciev : list) {
            ClaimAccount claimAccount = finceMapper.getClaimAccount(ciev.getFlowNumber());
            if (claimAccount != null) {
                existedList.add(claimAccount.getFlowNumber());
                continue;
            }
            ClaimAccount ca = new ClaimAccount();
            BeanUtils.copyProperties(ciev, ca);
            list1.add(ca);
        }
        if (list1.size() != 0) finceMapper.saveClaimImportData(list1);
        return existedList.size();
    }

    //保存账单数据以及修改认领账单相关信息
    @Transactional
    public Map<String, String> saveClaimAccount(CollectClaim collectClaim) {
        ClaimAccount claimAccount = finceMapper.getClaimTimes(collectClaim.getClaimAccountId());
        if (claimAccount.getStatus() == 1) return resultMap("fail", "该账单已经认领完成！");
        if (collectClaim.getAdjustPriceSum() > 0) return resultMap("fail", "本次调整金额总和不能大于0，请重新输入！");
        if (claimAccount.getClaimTimes() == 0) {
            if ((collectClaim.getClaimPriceSum() - collectClaim.getAdjustPriceSum()) > Double.parseDouble(claimAccount.getTransactionAmount()))
                return resultMap("fail", "认领总金额与调整总金额总和不能大于本次交易金额！");
            if ((collectClaim.getClaimPriceSum() - collectClaim.getAdjustPriceSum()) == Double.parseDouble(claimAccount.getTransactionAmount()))
                claimAccount.setStatus(1);
            else
                claimAccount.setTobeClaimedAmount(Double.parseDouble(claimAccount.getTransactionAmount()) - collectClaim.getClaimPriceSum() + collectClaim.getAdjustPriceSum());
            claimAccount.setClaimedAmount(collectClaim.getClaimPriceSum());
            claimAccount.setHotelId(collectClaim.getHotelId());
        } else {
            if ((collectClaim.getClaimPriceSum() - collectClaim.getAdjustPriceSum()) > claimAccount.getTobeClaimedAmount())
                return resultMap("fail", "本次认领金额与调整总金额总和不能大于待认领金额！");
            if ((collectClaim.getClaimPriceSum() + claimAccount.getClaimedAmount() - collectClaim.getAdjustPriceSum()) == Double.parseDouble(claimAccount.getTransactionAmount())) {
                claimAccount.setStatus(1);
                claimAccount.setTobeClaimedAmount(0.00);
            } else
                claimAccount.setTobeClaimedAmount(Double.parseDouble(claimAccount.getTransactionAmount()) - (collectClaim.getClaimPriceSum() + claimAccount.getClaimedAmount() - collectClaim.getAdjustPriceSum()));
            claimAccount.setClaimedAmount(collectClaim.getClaimPriceSum() + claimAccount.getClaimedAmount() - collectClaim.getAdjustPriceSum());
        }
        claimAccount.setClaimTimes(claimAccount.getClaimTimes() + 1);
        claimAccount.setUpdateDate(new Date());
        //claimAccount.setRemark(collectClaim.getRemark());
        finceMapper.updateClaimAccount(claimAccount);
        List<ClaimOptionsView> list = collectClaim.getList();
        for (ClaimOptionsView claimOptionsView : list) {
            Account account = accountService.getAccountInfo(claimOptionsView.getId());
            if (claimOptionsView.getAdjustPrice() > 0) return resultMap("fail", "该认领金额不能大于0，请重新输入！");
            //账单状态为未完成时，添加账单与认领账单记录
            if (account.getStatus() == 0) {
                if ((claimOptionsView.getClaimPrice() - claimOptionsView.getAdjustPrice()) > account.getUpdatePrice())
                    return resultMap("fail", "该认领金额与调整总金额总和不能大于账单金额！");
                //若该账单金额与认领金额一致，则修改账单状态
                if (account.getUpdatePrice() == (claimOptionsView.getClaimPrice() - claimOptionsView.getAdjustPrice())) {
                    account.setStatus(1);
                    account.setUpdatePrice(0.0);
                } else {
                    account.setUpdatePrice(account.getUpdatePrice() - (claimOptionsView.getClaimPrice() - claimOptionsView.getAdjustPrice()));
                    finceMapper.saveClaimAccount(claimOptionsView.getId(), collectClaim.getClaimAccountId(), claimOptionsView.getClaimPrice(), claimOptionsView.getAdjustPrice());
                }
                accountService.updateAccountStatus(account);
            }
        }
        return resultMap("success", "认领成功！");
    }

    public Map<String, String> resultMap(String value1, String value2) {
       Map<String, String> map = new HashMap<>();
       map.put("status", value1);
       map.put("msg", value2);
       return map;
    }

    //获取认领数据
    public Map<String, Object> getClaimAccountData(Integer status, Integer hotelId, String beginTime, String endTime, int pageNo, int pageSize) {
        List<ClaimAccount> list = finceMapper.getClaimAccountData(status, hotelId, beginTime, endTime);
        for (ClaimAccount claimAccount : list) {
            if (claimAccount.getHotelId() != null) {
                HotelBase hotelBase = hotelBaseService.getById(claimAccount.getHotelId());
                claimAccount.setHotelName(hotelBase.getHotel_name());
            }
        }
        Page<ClaimAccount> page = PageUtils.getPageList(list, pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("data", page);
        map.put("list", list);
        return map;
    }

    //添加酒店收款配置
    //TODO:需要改造优化
    @Transactional
    public Map<String, String> saveChargeConfig(ChargeConfigView ccv) {
        if (ccv.getList().size() != 0) {
            List list = new ArrayList();
            List<ChargeConfig> listSave = new ArrayList();
            ChargeConfigAttach cca = finceMapper.getChargeConfigAttach(ccv.getHotelId());
            if (cca == null) {
                finceMapper.saveChargeConfigAttach(ccv.getHotelId(), ccv.getCca());
                for (ChargeConfig cc : ccv.getList()) {
                    if (cc.getBeginDate() != null) {
                        //初始录入不需要判断是否为今天
                        if (cc.getBeginDate().getTime() < ccv.getCca().getStartDate().getTime() || cc.getBeginDate().getTime() > ccv.getCca().getEndDate().getTime()) {
                            finceMapper.deleteNostandardData(ccv.getHotelId());
                            return resultMap("fail", "生效日期必须在合同范围呢！");
                        }
                    }
                    //判断该酒店的该项配置是否存在
                    ChargeConfig chargeConfig = finceMapper.queryChargeConfig(ccv.getHotelId(), cc.getChargeItem());
                    if (chargeConfig == null) listSave.add(cc);
                    else list.add(chargeConfig.getChargeItem());

                }
            } else {
                for (ChargeConfig cc : ccv.getList()) {
                    if (cc.getBeginDate() != null) {
                        if (cc.getBeginDate().getTime() < ccv.getCca().getStartDate().getTime() || cc.getBeginDate().getTime() > ccv.getCca().getEndDate().getTime()) {
                            return resultMap("fail", "生效日期必须在合同范围呢！");
                        }
                    }
                    //判断该酒店的该项配置是否存在
                    ChargeConfig chargeConfig = finceMapper.queryChargeConfig(ccv.getHotelId(), cc.getChargeItem());
                    if (chargeConfig == null) listSave.add(cc);
                    else list.add(chargeConfig.getChargeItem());

                }
            }
            if (listSave.size() != 0) finceMapper.saveChargeConfig(ccv.getHotelId(), listSave);
            List<ChargeConfig> list1 = finceMapper.getChargeConfig(ccv.getHotelId());
            for (ChargeConfig chargeConfig : listSave) {
                for (ChargeConfig chargeConfig1 : list1) {
                    if (chargeConfig.getChargeItem() == chargeConfig1.getChargeItem()) {
                        ChargeRenewConfig chargeRenewConfig = new ChargeRenewConfig();
                        chargeRenewConfig.setCharge(chargeConfig1.getChargeRule());
                        chargeRenewConfig.setChargeConfigId(chargeConfig1.getId());
                        chargeRenewConfig.setChargeRate(chargeConfig.getChargeRate());
                        if (cca == null) chargeRenewConfig.setContractNum(ccv.getCca().getContractNum());
                        else chargeRenewConfig.setContractNum(cca.getContractNum());
                        chargeRenewConfig.setBeginDate(chargeConfig.getBeginDate());
                        Map<String, String> map = this.updateChargeConfig(ccv.getHotelId(), chargeRenewConfig, false);
                        if (map.get("status").equals("fail")) {
                            return resultMap("fail", map.get("msg"));
                        }
                    }
                }
            }
            return resultMap("success", "添加成功！");
            /*if (list.size() == 0) return resultMap("success", "添加成功");
            else return resultMap("success", "添加成功，其中收费项目为" + list + "已存在");*/
        } else {
            return resultMap("fail", "费用至少配置一项！");
        }
    }

    //获取酒店配置信息
    public ChargeConfigView getChargeConfigByHotelId(Integer hotelId) {
        List<ChargeConfig> list = finceMapper.getChargeConfig(hotelId);
        ChargeConfigAttach chargeConfigAttach = finceMapper.getChargeConfigAttach(hotelId);
        ChargeConfigView chargeConfigView = new ChargeConfigView();
        chargeConfigView.setList(list);
        chargeConfigView.setCca(chargeConfigAttach);
        return chargeConfigView;
    }

    //修改收费配置信息
    @Transactional
    public Map<String, String> updateChargeConfig(Integer hotelId, ChargeRenewConfig chargeRenewConfig, boolean flag) {
        ChargeConfigAttach chargeConfigAttach = finceMapper.getChargeConfigAttach(hotelId);
        if (chargeRenewConfig.getBeginDate() != null) {
            if (chargeRenewConfig.getBeginDate().getTime() < chargeConfigAttach.getStartDate().getTime() || chargeRenewConfig.getBeginDate().getTime() > chargeConfigAttach.getEndDate().getTime()) {
                return resultMap("fail", "调整生效日期必须在合同范围呢！");
            }
            if (flag) {
                Date date = CommonUtil.strToDate(CommonUtil.dateToStr(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
                if (chargeRenewConfig.getBeginDate().getTime() <= date.getTime() || chargeRenewConfig.getBeginDate().getTime() == chargeConfigAttach.getEndDate().getTime()) {
                    return resultMap("fail", "调整生效日期不能小于等于今天并且不能为合同结束日期！");
                }
            }
            //数据库中未生效的记录永远只存在一条记录
            ChargeRenewConfig chargeRenewConfig1 =  finceMapper.getUpdateChargeConfig(chargeRenewConfig.getChargeConfigId());
            if (chargeRenewConfig1 != null) {
                chargeRenewConfig.setStatus(0);
                finceMapper.updateChargeRenewConfig(chargeRenewConfig);
            } else {
                chargeRenewConfig.setHotelId(hotelId);
                finceMapper.saveChargeRenewConfig(chargeRenewConfig);
                finceMapper.updateChargeRate(hotelId, chargeRenewConfig.getChargeConfigId(), chargeRenewConfig.getChargeRate());
            }
        }
        return resultMap("success", "调整成功！");
    }

    //获取修改收费历史记录，包括生效与未生效
    public Map<String, Object> getChargeRenewConfig(Integer chargeConfigId, Integer hotelId) {
        List<ChargeRenewConfig> list = finceMapper.getChargeRenewConfig(chargeConfigId, hotelId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        return map;
    }

    //定时执行修改配置生效时间
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateChargeConfigPrice() {
        log.info("定时任务执行啦！！！！！！！！");
        Date date = CommonUtil.strToDate(CommonUtil.dateToStr(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
        List<ChargeRenewConfig> list = finceMapper.getChargeRenewConfigByStatus();
        for (ChargeRenewConfig crc : list) {
            if (crc.getBeginDate() != null && crc.getBeginDate().getTime() == date.getTime()) {
                finceMapper.updateChargeConfig(crc.getChargeConfigId(), crc.getCharge(), 0);
                finceMapper.updateChargeRenewConfigStatus(crc.getId());
            }
        }
    }
}
