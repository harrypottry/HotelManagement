package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

/**
 * @className AppBanner
 * @Description 这个类主要是干 app端 banner图类
 * @Author 张赢
 * @Date 2019/2/21 0021下午 17:19
 * @Version 1.0
 **/
@Data
public class AppBanner {
    private Integer id;

    private String url;

    private String pic_path;

    private Integer img_num;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    private String rel_rul;
}
