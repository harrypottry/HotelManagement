package com.aaroom.beans;

import lombok.Data;

import java.util.List;

/**
 * @className ClothViewWeb
 * @Description 这个类主要是干【web端显示布草库存管理查询展示】
 * @Author 张赢
 * @Date 2018/12/4 0004下午 15:58
 * @Version 1.0
 **/
@Data
public class ClothViewWeb {

    private Integer cloth_id;//布草id

    private List cloth_type_brand_size_material;//布草类型，品牌，规格，材质

    private String position;//所在位置

    private Integer num;//房间内布草数量和

    private String status;//洁净程度

    private Integer recycle_num;//流转次数

    private String comment;//备注

    private String possessor_id;//对应类型的id  0.房间。1.员工 2.库房(hotel) 3.洗衣厂(emp) 下对应的id

    private String possessor_name;//对应的类型possessor_id所对应的possessor_name

    private List cloth_type_brand;//布草类型+品牌

    private String cloth_type;//布草的名称

    private String cloth_size;//布草的尺寸

    private String cloth_material;//布草的材质
}
