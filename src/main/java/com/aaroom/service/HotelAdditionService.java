package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.persistence.HotelAdditionMapper;
import com.aaroom.persistence.HotelContactsMapper;
import com.aaroom.persistence.HotelPathMapper;
import com.aaroom.persistence.HotelWifiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/26.
 */
@Service
public class HotelAdditionService {

    @Autowired
    private HotelAdditionMapper hotelAdditionMapper;

    @Autowired
    private HotelContactsMapper hotelContactsMapper;

    @Autowired
    private HotelPathMapper hotelPathMapper;

    @Autowired
    private HotelWifiMapper hotelWifiMapper;

    public HotelAdditionView getHotelAdditionView(Integer hotelId) {
        HotelAdditionView hotelAdditionView = new HotelAdditionView();
        HotelAdditionExample example = new HotelAdditionExample();
        HotelContactsExample example1 = new HotelContactsExample();
        HotelPathExample example2 = new HotelPathExample();
        HotelWifiExample example3 = new HotelWifiExample();
        HotelAdditionExample.Criteria criteria = example.createCriteria();
        HotelContactsExample.Criteria criteria1 = example1.createCriteria();
        HotelPathExample.Criteria criteria2 = example2.createCriteria();
        HotelWifiExample.Criteria criteria3 = example3.createCriteria();
        criteria.andHotelIdEqualTo(hotelId);
        criteria1.andHotelIdEqualTo(hotelId);
        criteria2.andHotelIdEqualTo(hotelId);
        criteria3.andHotelIdEqualTo(hotelId);
        HotelAddition hotelAdditions = (HotelAddition) hotelAdditionMapper.selectByExample(example);
        HotelContacts hotelContacts = (HotelContacts) hotelContactsMapper.selectByExample(example1);
        List<HotelPath> hotelPaths = hotelPathMapper.selectByExample(example2);
        List<HotelWifi> hotelWifis = hotelWifiMapper.selectByExample(example3);
        hotelAdditionView.setHotelAddition(hotelAdditions);
        hotelAdditionView.setHotelContacts(hotelContacts);
        hotelAdditionView.setHotelPathList(hotelPaths);
        hotelAdditionView.setHotelWifiList(hotelWifis);
        return hotelAdditionView;
    }

    public Map<String,Object> insertHotelAdditionView(HotelAdditionView hotelAdditionView) {
        try {
            Integer id = hotelAdditionView.getHotelAddition().getId();
            if (id!=null){
                hotelAdditionMapper.updateByPrimaryKeySelective(hotelAdditionView.getHotelAddition());
                hotelContactsMapper.updateByPrimaryKeySelective(hotelAdditionView.getHotelContacts());
                for (HotelPath hotelPath:hotelAdditionView.getHotelPathList()
                     ) {
                    hotelPathMapper.updateByPrimaryKeySelective(hotelPath);
                }
                for (HotelWifi hotelWifi:hotelAdditionView.getHotelWifiList()
                     ) {
                    hotelWifiMapper.updateByPrimaryKeySelective(hotelWifi);
                }
            }else{
                hotelAdditionMapper.insertSelective(hotelAdditionView.getHotelAddition());
                hotelContactsMapper.insertSelective(hotelAdditionView.getHotelContacts());
                for (HotelPath hotelPath:hotelAdditionView.getHotelPathList()
                        ) {
                    hotelPathMapper.insertSelective(hotelPath);
                }
                for (HotelWifi hotelWifi:hotelAdditionView.getHotelWifiList()
                        ) {
                    hotelWifiMapper.insertSelective(hotelWifi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap("error", "保存信息失败！");
        }
        return resultMap("success", "保存信息成功！");
    }
    public Map<String, Object> resultMap(String value1, String value2) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", value1);
        map.put("msg", value2);
        return map;
    }
}
