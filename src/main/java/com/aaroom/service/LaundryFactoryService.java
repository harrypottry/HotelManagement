package com.aaroom.service;
import com.aaroom.beans.LaundryFactory;
import com.aaroom.persistence.LaundryFactoryMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("laundryFactoryService")
public class LaundryFactoryService{

    @Resource
    private LaundryFactoryMapper laundryFactoryMapper;

    public LaundryFactory getById(Integer id)throws Exception{
        return laundryFactoryMapper.getById(id);
    }

    public List<LaundryFactory> getListByMap(Map<String,Object> param)throws Exception{
        return laundryFactoryMapper.getListByMap(param);
    }


    public Integer save(LaundryFactory laundryFactory)throws Exception{
            return laundryFactoryMapper.save(laundryFactory);
    }

    public Integer modify(LaundryFactory laundryFactory)throws Exception{
        return laundryFactoryMapper.modify(laundryFactory);
    }

    public Integer removeById(Integer id)throws Exception{
        return laundryFactoryMapper.removeById(id);
    }


}
