package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

/**
 * @className ClothPrice
 * @Description 这个类主要是干
 * @Author 张赢
 * @Date 2019/1/22 0022下午 15:10
 * @Version 1.0
 **/
@Data
public class ClothPrice {

    private Integer id;

    private Double rental_price; //布草租赁费用

    private Double pay_wash_price;//布草清洗支付给洗衣厂的费用

    private Double collect_wash_price;//布草清洗费用（向酒店方收取的费用 交给我们AA的费用）

    private Integer cloth_id;

    private Integer hotel_id;

    private String cloth_type_id;//类型

    private Double cloth_price; //布草本身费用

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Integer is_active;

}
