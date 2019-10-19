package com.aaroom.beans;

import lombok.Data;

/**
 * @className MissionsView
 * @Description
 * @Author 张赢
 * @Date 2018/11/15 18:09
 * @Version 1.0
 **/
@Data
public class MissionsView {

    private Integer room_id;

    private String room_name;

    private String mission_type_name;

    private String accept_employee_name;

    private String status_name;

    private String room_type_name;

}
