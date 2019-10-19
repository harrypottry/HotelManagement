package com.aaroom.beans;

import lombok.Data;

/**
 * @className ClothCountView
 * @Description 这个类主要是干 给App端 显示关于当前布草数量的
 * @Author 张赢
 * @Date 2018/12/21 0021下午 13:42
 * @Version 1.0
 **/
@Data
public class ClothCountView {

    private Integer  clean_count;

    private Integer  dirty_count;

    private Integer total_count;

    private Integer washing_count;

    private Integer washover_count;
}
