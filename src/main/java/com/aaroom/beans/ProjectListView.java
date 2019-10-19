package com.aaroom.beans;

import lombok.Data;

import java.util.List;

/**
 * Created by 温建成 on 2019/2/25.
 */
@Data
public class ProjectListView {

    private String hotelName;

    private List<ProjectList> projectLists;
}
