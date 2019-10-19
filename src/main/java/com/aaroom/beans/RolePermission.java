package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class RolePermission {
    private Integer id;

    private Integer role_id;

    private Integer permission_id;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}