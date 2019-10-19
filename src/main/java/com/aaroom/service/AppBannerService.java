package com.aaroom.service;

import com.aaroom.beans.AppBanner;
import com.aaroom.persistence.AppBannerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @className AppBannerService
 * @Description 这个类主要是干
 * @Author 张赢
 * @Date 2019/2/21 0021下午 17:38
 * @Version 1.0
 **/
@Service
public class AppBannerService {

    @Resource
    private AppBannerMapper appBannerMapper;

    public void insert(AppBanner appBanner){
        appBannerMapper.insert(appBanner);
    }

    public List<AppBanner> getAppBanner(){
       return appBannerMapper.getAppBanner();
    }

}
