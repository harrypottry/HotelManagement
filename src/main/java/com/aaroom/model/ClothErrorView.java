package com.aaroom.model;

import com.aaroom.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liyp on 2018/12/25 0025.
 */
@Data
public class ClothErrorView implements Serializable{
    private static final long serialVersionUID = 5077798817508060733L;
    private String  cloth_type_id;

    private String  cloth_type;

    private String  cloth_size;

    private String  cloth_material;

    private Integer cloth_id;

    private Constants.ClothErrorType error_type;

    private Integer recycle_num;

    private Double  suggest_pay;

    private Integer hotel_id;

    private String  hotel_name;

    private Double  cloth_price;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date   create_time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date   error_time;

    private Date beginDate;

    private Date endDate;

    public ClothErrorView(String cloth_type, double suggest_pay) {
        this.cloth_type = cloth_type;
        this.suggest_pay = suggest_pay;
    }
}
