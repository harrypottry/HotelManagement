package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 意向门店合同相关
 * @create: 2019-02-21 14:56
 **/
@Data
public class IntentionStoreContract {

    private String id;

    private String isiId;

    private String firstParty;

    private String firstPartySignatory;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date cooperationDate;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date signingDate;

    private String protocolNum;

    private String protocolPhoto;

    private String supplementProtocolNum;

    private String supplementProtocolPhoto;

    private String fiveCardSfzz;

    private String fiveCardSfzf;

    private String fiveCardYy;

    private String fiveCardWs;

    private String fiveCardTxhy;

    private Integer type;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
