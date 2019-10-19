package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 意向门店签约信息
 * @create: 2019-02-20 13:44
 **/
@Data
public class IntentionStoreInfo {

    private String id;

    private String surveyId;

    private String[] giveMaterial;

    private String giveMaterials;

    private Integer giveOther;

    private Integer type;

    private Integer typeS;

    private String content;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
