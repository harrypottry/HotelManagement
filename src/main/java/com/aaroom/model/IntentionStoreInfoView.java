package com.aaroom.model;

import com.aaroom.beans.ChargeConfig;
import com.aaroom.beans.IntentionStoreInfo;
import com.aaroom.beans.IsiBed;
import lombok.Data;

import java.util.List;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 意向门店签约信息View
 * @create: 2019-02-20 13:49
 **/
@Data
public class IntentionStoreInfoView {

    private String surveyId;

    private List<ChargeConfig> chargeConfig;

    private IntentionStoreInfo intentionStoreInfo;

    private List<IsiBed> isiBed;
}
