package com.aaroom.service;
import com.aaroom.beans.CentralWarehouseEmployee;
import com.aaroom.persistence.CentralWarehouseEmployeeMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("centralWarehouseEmployeeService")
public class CentralWarehouseEmployeeService {

    @Resource
    private CentralWarehouseEmployeeMapper centralWarehouseEmployeeMapper;

    public CentralWarehouseEmployee getById(Integer id)throws Exception{
        return centralWarehouseEmployeeMapper.getById(id);
    }

    public List<CentralWarehouseEmployee> getListByMap(Map<String,Object> param)throws Exception{
        return centralWarehouseEmployeeMapper.getListByMap(param);
    }


    public Integer save(CentralWarehouseEmployee centralWarehouseEmployee)throws Exception{
            return centralWarehouseEmployeeMapper.save(centralWarehouseEmployee);
    }

    public Integer modify(CentralWarehouseEmployee centralWarehouseEmployee)throws Exception{
        return centralWarehouseEmployeeMapper.modify(centralWarehouseEmployee);
    }

    public Integer removeById(Integer id )throws Exception{
        return centralWarehouseEmployeeMapper.removeById(id);
    }


}
