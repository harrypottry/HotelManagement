package com.aaroom.beans;

import lombok.Data;
import java.util.List;

/**
 * @className CleanerBillMissionsView
 * @Description 这个类主要是干  1.6版APP 保洁员账单展示
 * @Author 张赢
 * @Date 2019/2/14 0014下午 17:18
 * @Version 1.0
 **/
@Data
public class CleanerBillMissionsView {

    private Integer FullTimeMission;//非AA全职任务

    private Integer PartTimeMission;//非AA兼职任务

    private Integer AAFullTimeMission;//AA全职任务

    private Integer AAPartTimeMission;//AA兼职任务

    private Integer CheckMission;//抽查返工订单

    private Integer ComplaintMission;//投诉单

    private Double MissionReward;//本月累计报酬

    private List<Mission> MissionList;//任务详情

}
