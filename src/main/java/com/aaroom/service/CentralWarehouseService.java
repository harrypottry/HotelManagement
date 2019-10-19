package com.aaroom.service;

import com.aaroom.beans.CentralWarehouse;
import com.aaroom.persistence.CentralWarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service("centralWarehouseService")
public class CentralWarehouseService{

    @Autowired
    private CentralWarehouseMapper centralWarehouseMapper;

    public CentralWarehouse getById(Long id)throws Exception{
        return centralWarehouseMapper.getById(id);
    }

    public List<CentralWarehouse> getListByMap(Map<String,Object> param)throws Exception{
        return centralWarehouseMapper.getListByMap(param);
    }


    public Integer save(CentralWarehouse centralWarehouse)throws Exception{
            return centralWarehouseMapper.save(centralWarehouse);
    }

    public Integer modify(CentralWarehouse centralWarehouse)throws Exception{
        return centralWarehouseMapper.modify(centralWarehouse);
    }

    public Integer removeById(Long id)throws Exception{
        return centralWarehouseMapper.removeById(id);
    }


}
