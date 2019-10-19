package com.aaroom.model;

import com.aaroom.beans.ChargeConfig;
import com.aaroom.beans.ChargeConfigAttach;
import lombok.Data;

import java.util.List;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 酒店收费配置相关
 * @create: 2019-01-18 16:21
 **/
@Data
public class ChargeConfigView {

    private Integer hotelId;

    private List<ChargeConfig> list;

    private ChargeConfigAttach cca;
}
