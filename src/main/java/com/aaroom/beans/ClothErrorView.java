package com.aaroom.beans;

import com.aaroom.utils.Constants;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @className ClothErrorView
 * @Description 这个类主要是干 app端库管端 报损使用
 * @Author 张赢
 * @Date 2018/12/21 0021下午 19:30
 * @Version 1.0
 **/
@Data
public class ClothErrorView {

    private Integer cloth_id;//布草id

    private String clothTypeIds;//布草类型字符串1,2,3

    private List clothTypes_name;//布草类型所对应的中文desc

    private Constants.ClothErrorType type;//报损类型枚举常量 对应报损情况

    private String Lost;
    ;
    private String Broken;

    private String QRBroken;

    private Integer status;//处理情况 0,已报损 1，已处理

    private String comment;//备注

    private Date create_time;//报损时间

    private Date update_time;//最新更新时间

    private Integer CleanStatus;//洁净程度 0干净 1脏

    private Integer possessor_type;//所在位置 0.房间。1.员工 2.库房(hotel) 3.洗衣厂(emp)

    private List<ClothType> clothTypeList;//布草对应的clothtype集合

}
