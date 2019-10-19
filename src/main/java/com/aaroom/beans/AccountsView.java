package com.aaroom.beans;

import lombok.Data;

import java.util.List;

/**
 * Created by 温建成 on 2019/1/16.
 */
@Data
public class AccountsView {

    private String hotelName;

    private List<Account> accounts;
}
