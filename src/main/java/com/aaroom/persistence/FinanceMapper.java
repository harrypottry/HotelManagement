package com.aaroom.persistence;


import com.aaroom.beans.ChargeConfig;
import com.aaroom.beans.ChargeConfigAttach;
import com.aaroom.beans.ChargeRenewConfig;
import com.aaroom.beans.ClaimAccount;
import com.aaroom.model.FinanceListView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceMapper {
    //获取租赁费用列表
    List<FinanceListView> getRentalList(@Param(value = "dateType")int dateType,
                                        @Param(value = "hotelId")Integer hotelId,
                                        @Param(value = "beginDate")String beginDate,
                                        @Param(value = "endDate")String endDate,
                                        @Param(value = "direction")int direction);

    //获取洗涤费用列表
    List<FinanceListView> getWashList(@Param(value = "dateType")int dateType,
                                      @Param(value = "hotelId")Integer hotelId,
                                      @Param(value = "employeeId")String employeeId,
                                      @Param(value = "beginDate")String beginDate,
                                      @Param(value = "endDate")String endDate,
                                      @Param(value = "direction")int direction);

    //导入银行账单数据
    void saveClaimImportData(@Param(value = "claimAccountList")List<ClaimAccount> claimAccountList);

    //生成账单与认领账单关联信息
    void saveClaimAccount(@Param(value = "accountId")Integer accountId,
                          @Param(value = "claimAccountId")Integer claimAccountId,
                          @Param(value = "claimPrice")double claimPrice,
                          @Param(value = "adjustPrice")double adjustPrice);

    //获取认领账单认领次数
    ClaimAccount getClaimTimes(@Param(value = "claimAccountId")Integer claimAccountId);

    //更新认领账单数据
    void updateClaimAccount(@Param(value = "ca")ClaimAccount claimAccount);

    //获取认领数据
    List<ClaimAccount> getClaimAccountData(@Param(value = "status")Integer status,
                                           @Param(value = "hotelId")Integer hotelId,
                                           @Param(value = "beginTime")String beginTime,
                                           @Param(value = "endTime")String endTime);

    //根据银行流水查询数据，防止数据导入重复。
    ClaimAccount getClaimAccount(@Param(value = "flowNumber")String flowNumber);

    //添加收费配置基本信息
    void saveChargeConfig(@Param(value = "hotelId")Integer hotelId,
                          @Param(value = "chargeConfigList")List<ChargeConfig> list);

    //添加收费配置基本信息
    void saveChargeConfigIntetionStore(@Param(value = "surveyId")String surveyId,
                          @Param(value = "chargeConfigList")List<ChargeConfig> list);

    //添加合同基本信息
    void saveChargeConfigAttach(@Param(value = "hotelId")Integer hotelId,
                               @Param(value = "cca")ChargeConfigAttach cca);

    //根据酒店ID获取相关配置信息
    List<ChargeConfig> getChargeConfig(@Param(value = "hotelId")Integer hotelId);

    List<ChargeConfig> getChargeConfigIntentionStore(@Param(value = "surveyId")String surveyId);

    //根据酒店ID获取合同基本信息
    ChargeConfigAttach getChargeConfigAttach(@Param(value = "hotelId")Integer hotelId);

    //更新配置信息
    void updateChargeRenewConfig(@Param(value = "crc")ChargeRenewConfig chargeRenewConfig);

    //根据配置信息ID获取配置内容
    ChargeRenewConfig getUpdateChargeConfig(@Param(value = "chargeConfigId")Integer chargeConfigId);

    //添加配置信息
    void saveChargeRenewConfig(@Param(value = "crc")ChargeRenewConfig chargeRenewConfig);

    //获取修改收费历史记录，包括生效与未生效
    List<ChargeRenewConfig> getChargeRenewConfig(@Param(value = "chargeConfigId")Integer chargeConfigId,
                                                 @Param(value = "hotelId")Integer hotelId);

    List<ChargeRenewConfig> getChargeRenewConfigByStatus();

    void updateChargeConfig(@Param(value = "chargeConfigId")Integer chargeConfigId,
                            @Param(value = "charge")double charge,
                            @Param(value = "reduction") Integer reduction);

    void updateChargeRenewConfigStatus(@Param(value = "id")int id);

    //查询该配置是否存在
    ChargeConfig queryChargeConfig(@Param(value = "hotelId")Integer hotelId, @Param(value = "chargeItem")Integer chargeItem);

    void deleteNostandardData(@Param(value = "hotelId")Integer hotelId);

    void updateChargeRate(@Param(value = "hotelId")Integer hotelId,
                          @Param(value = "chargeConfigId")Integer chargeConfigId,
                          @Param(value = "chargeRate")Integer chargeRate);

    List<ChargeConfig> getChargeConfigBySurveyId(@Param(value = "surveyId")String surveyId);

    void updateChargeRenewConfigByCcId(@Param(value = "ccid")Integer ccid,
                                       @Param(value = "chargeRule")Double chargeRule);
}
