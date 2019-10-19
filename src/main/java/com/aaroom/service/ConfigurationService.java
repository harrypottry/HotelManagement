package com.aaroom.service;

import com.aaroom.beans.Configuration;
import com.aaroom.persistence.ConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sosoda on 2018/11/2.
 */
@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationMapper configurationMapper;

    public Configuration getConfigurationByName(String name){
        return configurationMapper.getConfigurationByName(name);
    }

    public void update(Configuration configuration) {
        configurationMapper.updateByPrimaryKeySelective(configuration);
    }
}
