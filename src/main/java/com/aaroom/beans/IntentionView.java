package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

/**
 * Created by 温建成 on 2019/2/26.
 */
@Data
public class IntentionView {

    private Integer roomNum;

    private String firstParty;

    private String firstPartySignatory;

    private String protocolNum;

    private Date signingDate;

    private Date cooperationDate;

    private String giveMaterial;

    private String expandName;

}
