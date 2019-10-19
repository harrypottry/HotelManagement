package com.aaroom.beans;

import lombok.Data;

import java.util.List;

/**
 * Created by 温建成 on 2019/2/26.
 */
@Data
public class HotelAdditionView {

    private HotelAddition hotelAddition;

    private HotelContacts hotelContacts;

    private List<HotelPath> hotelPathList;

    private List<HotelWifi> hotelWifiList;
}
