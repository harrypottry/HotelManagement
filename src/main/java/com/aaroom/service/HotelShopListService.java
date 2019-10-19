package com.aaroom.service;


import com.aaroom.beans.ContractList;
import com.aaroom.beans.HotelShopList;
import com.aaroom.beans.HotelShopListExample;
import com.aaroom.persistence.EmployeeMapper;
import com.aaroom.persistence.HotelBaseMapper;
import com.aaroom.persistence.HotelShopListMapper;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by 温建成 on 2019/2/19.
 */
@Service
public class HotelShopListService {

    @Autowired
    private HotelShopListMapper hotelShopListMapper;

    @Autowired
    private ContractListService contractListService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private HotelBaseMapper hotelBaseMapper;

    public Map<String,Object> exportHotelShopList(Integer hotelId, String proprietorName, Integer expandId, Integer shopStatus,Integer shopManagerId, Integer pageSize, Integer pageNo) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        HotelShopListExample example = new HotelShopListExample();
        HotelShopListExample.Criteria criteria = example.createCriteria();
        if(hotelId == 0){
            hotelId = null;
        }
        if(expandId == 0){
            expandId = null;
        }
        if(shopStatus == 0){
            shopStatus = null;
        }
        if(shopManagerId == 0){
            shopManagerId = null;
        }
        List<Map<String,Object>> hotelShopList = hotelShopListMapper.getHotelShopList(hotelId, proprietorName, expandId, shopStatus, shopManagerId);
        //拓展列表
        Map<String, Object> expandLists = contractListService.exportemployeeList(19);
        Map expandList = (Map) expandLists.get("contractMap");
        //运营店长
        Map<String, Object> managerLists = contractListService.exportemployeeList(10);
        Map managerList = (Map) managerLists.get("contractMap");
        //酒店的状态
        String[] hotelStatusName = {"","合同已通过","待立项","筹建店","运营店"};
        String[] key = {"id","hotelId","proprietorId","signatoryTime","expandId","shopManagerId","shopStatus","newHotelName","oldHotelName","proprietorName"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String,Object> map:hotelShopList
                ) {
            for (int i = 0;i < key.length; i++
                    ) {
              if(map.get(key[i])==null){
                  map.put(key[i],null);
              }
            }
            map.put("expandName",expandList.get(map.get("expandId")+""));
            map.put("shopManagerName",managerList.get(map.get("shopManagerId")+""));
            map.put("shopStatusName",hotelStatusName[Integer.parseInt(map.get("shopStatus")+"")]);
            if(map.get("signatoryTime")!= null){
                map.put("signatoryTime",sdf.format(map.get("signatoryTime")));
            }
        }
        Page data = PageUtils.getPageList(hotelShopList, pageNo, pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("list",hotelShopList);
        map.put("data",data);
        return map;
    }

    public Integer insertShopManager(Integer id, Integer shopManagerId) {
        HotelShopList hotelShopList = hotelShopListMapper.selectByPrimaryKey(id);
        if(hotelShopList == null){
            return 0;
        }
        hotelShopList.setShopManagerId(shopManagerId);
        int i = hotelShopListMapper.updateByPrimaryKeySelective(hotelShopList);
        return i;
    }
    //获取运营店长列表
    public Map<String,Object> exportemployeeManager(Integer roleId){
        List<Map<String,Object>> contractList = employeeMapper.getContractList(roleId);
        HashMap<String, Object> maps = new HashMap<>();
        for (Map<String,Object> contract:contractList
                ) {
            contract.put("shopManagerId",contract.get("contractRecipientId"));
            contract.put("shopManagerName",contract.get("contractRecipientName"));
            contract.remove("contractRecipientId");
            contract.remove("contractRecipientName");
            maps.put(contract.get("shopManagerId")+"",contract.get("shopManagerName"));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("shopManagerList",contractList);
        map.put("shopManagerMap",maps);
        return map;
    }
}