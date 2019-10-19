package com.aaroom.beans;

import com.aaroom.utils.Constants.*;
import lombok.Data;

import java.util.Date;

@Data
public class ClothType {
    private Integer id;

    private String desc;

    private ClothKind cloth_kind;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}