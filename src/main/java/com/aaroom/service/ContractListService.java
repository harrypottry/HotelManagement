package com.aaroom.service;

import com.aaroom.beans.ContractList;
import com.aaroom.beans.ContractListExample;
import com.aaroom.beans.Employee;
import com.aaroom.persistence.ContractListMapper;
import com.aaroom.persistence.EmployeeMapper;
import com.aaroom.persistence.HotelBaseMapper;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/13.
 */
@Service
public class ContractListService {

    @Autowired
    private ContractListMapper contractListMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private HotelBaseMapper hotelBaseMapper;
    public Map<String,Object> exportGetContractListList( Integer contractType, Integer contractRecipientId, Integer contractSignatoryId, Integer contractStatus, Integer pageSize, Integer pageNo) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        ContractListExample example = new ContractListExample();
        ContractListExample.Criteria criteria = example.createCriteria();
        if(contractType != 0 && contractType != null){
            criteria.andContractTypeEqualTo(contractType);
        }
        if(contractRecipientId != 0 && contractRecipientId != null){
            criteria.andContractRecipientIdEqualTo(contractRecipientId);
        }
        if(contractSignatoryId != 0 && contractSignatoryId != null){
            criteria.andContractSignatoryIdEqualTo(contractSignatoryId);
        }
        if(contractStatus != 0 && contractStatus != null){
            criteria.andContractStatusEqualTo(contractStatus);
        }
        List<ContractList> contractLists = contractListMapper.selectByExample(example);
        //领用人
        Map<String, Object> employeeMap = exportemployeeList(19);
        Map<String,Object> contractMap = (Map<String, Object>) employeeMap.get("contractMap");
        //签约方
        Map<String, Object> hotelBaseMaps = exportHotelBaseName();
        //合同状态
        String[] contractStatusName = {"","未领用","领用中","已领用","已签约","待归档","已归档","已丢失","已作废","拒收"};
        String[] contractTypeName = {"","服务协议","补充协议"};
        Map<String,String> contractStatusMap = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i = 1; i < contractStatusName.length; i++){
            contractStatusMap.put(i+"",contractStatusName[i]);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (ContractList contract:contractLists
             ) {
            Map<String, Object> map = CommonUtil.convertBeanToMap(contract);
            map.put("contractTypeName",contractTypeName[contract.getContractType()]+"");
            map.put("contractRecipientName",contractMap.get(contract.getContractRecipientId()+""));
            map.put("contractSignatoryName",hotelBaseMaps.get(contract.getContractSignatoryId()+""));
            map.put("contractStatusName",contractStatusMap.get(contract.getContractStatus()+""));
            if(contract.getContractReceiveTime()!=null){
                map.put("contractReceiveTime",sdf.format(contract.getContractReceiveTime()));
            }
            list.add(map);
        }
        Page data = PageUtils.getPageList(list, pageNo, pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("data",data);
        return map;
    }
    //获取领用人
    public Map<String,Object> exportemployeeList(Integer roleId){
        List<Map<String,Object>> contractList = employeeMapper.getContractList(roleId);
        HashMap<String, Object> maps = new HashMap<>();
        for (Map<String,Object> contract:contractList
             ) {
            maps.put(contract.get("contractRecipientId")+"",contract.get("contractRecipientName"));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("contractList",contractList);
        map.put("contractMap",maps);
        return map;
    }
    //获取签约方也就是酒店的新名字和旧的名字并筛选名字
    public Map<String,Object> exportHotelBaseName(){
        List<Map<String,Object>> list = hotelBaseMapper.exportHotelBaseName();
        HashMap<String, Object> hashMap = new HashMap<>();
        for (Map<String,Object> hotelBase:list
             ) {
            if(hotelBase.get("newName") != null){
                hashMap.put(hotelBase.get("id")+"",hotelBase.get("newName"));
            }else{
                hashMap.put(hotelBase.get("id")+"",hotelBase.get("oldName"));
            }
        }
        return hashMap;
    }
    //添加
    public Integer insertContract(String contractNumber, Integer number, String contractNumberTouch,Integer contractType) {
        ContractList contractList = new ContractList();
        ContractListExample example = new ContractListExample();
        ContractListExample.Criteria criteria = example.createCriteria();
        criteria.andContractNumberEqualTo(contractNumber);
        List<ContractList> contractLists = contractListMapper.selectByExample(example);
        if(contractLists.size()!=0){
            return 1;
        }
        contractList.setContractNumber(contractNumber);
        contractList.setContractType(contractType);
        contractList.setContractStatus(1);
        contractList.setContractNumberTouch(contractNumberTouch);
        contractListMapper.insertSelective(contractList);
        return 0;
    }
    //领用
    public Integer contractReceive(Integer id, Integer contractRecipientId) {
        ContractList contractList = contractListMapper.selectByPrimaryKey(id);
        if(contractList == null){
            return 0;
        }
        contractList.setContractRecipientId(contractRecipientId);
        contractList.setContractStatus(2);
        int i = contractListMapper.updateByPrimaryKeySelective(contractList);
        return i;
    }

    public Integer contractDrop(Integer id, Integer contractStatus, String comments) {
        ContractList contractList = contractListMapper.selectByPrimaryKey(id);
        if(contractList == null){
            return 0;
        }
        contractList.setContractStatus(contractStatus);
        contractList.setComments(comments);
        int i = contractListMapper.updateByPrimaryKeySelective(contractList);
        return i;
    }

    public Integer contractPigeonhole(Integer id,Integer contractStatus) {
        ContractList contractList = contractListMapper.selectByPrimaryKey(id);
        if(contractList == null){
            return 0;
        }
        contractList.setContractStatus(contractStatus);
        int i = contractListMapper.updateByPrimaryKeySelective(contractList);
        return i;
    }
}
