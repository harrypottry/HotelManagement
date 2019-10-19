package com.aaroom.service;

import com.aaroom.beans.Configuration;
import com.aaroom.beans.Consumption;
import com.aaroom.persistence.ConfigurationMapper;
import com.aaroom.persistence.ConsumptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sosoda on 2018/11/2.
 */
@Service
public class ConsumptionService {

    @Resource
    private ConsumptionMapper consumptionMapper;

  public List<Consumption> getAllConsumption (){
      return consumptionMapper.getAllConsumption();
  }
}
