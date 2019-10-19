package com.aaroom.persistence;

import com.aaroom.beans.AppBanner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @className AppBannerMapper
 * @Description
 * @Author 张赢
 * @Date 2019/2/21 0021 下午 17:30
 * @Version 1.0
 **/
public interface AppBannerMapper {

    int insert(AppBanner appBanner);//增 对象 动态

    int deleteByPrimaryKey(Integer id);//删 根据id

    int updateByPrimaryKeySelective(AppBanner appBanner);;//改 对象 动态

    AppBanner selectByPrimaryKey(Integer id); //根据id查对象

    //查所有对象
    List<AppBanner> getAppBanner();

}
