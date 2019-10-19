package com.aaroom.persistence;

import com.aaroom.beans.ClothLog;
import com.aaroom.utils.EmployeeOperator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClothLogMapper {
    int deleteByPrimaryKey(Integer id);

    @EmployeeOperator
    int insert(ClothLog record);
    //入参对象 增加新log日志【张赢】
    int  insertSelective(ClothLog record);

    ClothLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClothLog record);

    int updateByPrimaryKey(ClothLog record);

    List<ClothLog> getClothLogByClothId(@Param("cloth_id") Integer cloth_id, @Param("hotel_id") int hotel_id);
    //查询当前月（当前月1月0点开始到现在的）所有log对象
    List<ClothLog> getClothLogListByCurrentMonth(@Param("EarlyTime") Date MonthFirstTime,
                                                 @Param("NearTime") Date CurrentTime,
                                                 @Param("hotel_id") Integer hotel_id,
                                                 @Param("direction") Integer direction,
                                                 @Param("possessor_type") Integer possessor_type);
    //查询当前日（当前日所有log对象）模糊查询
    List<ClothLog> getClothLogListByDay(@Param("DayTime") String DayTime,
                                                 @Param("hotel_id") Integer hotel_id,
                                                 @Param("direction") Integer direction,
                                                 @Param("possessor_type") Integer possessor_type);
    //在酒店中停留的这个cloth的log
    List<ClothLog> getClothLogListDuringHotel(@Param("EarlyTime") Date EarlyTime,
                                                 @Param("hotel_id") Integer hotel_id,
                                              @Param("cloth_id") Integer cloth_id,
                                                 @Param("direction") Integer direction,
                                                 @Param("status") Integer status,
                                                 @Param("possessor_type") Integer possessor_type);
    //根据酒店id与布草id得到这个id最新的一次clothlog对象
    List<ClothLog> getClothLogByClothIdAndHotelId (@Param("hotel_id") int hotel_id,@Param("cloth_id") Integer cloth_id);
    //根据ClothId 查到最大的clothid 用以确定从后从到那个洗衣厂
    List<ClothLog> getMaxClothLogByClothId(@Param("cloth_id") Integer cloth_id);
    //1.6app 根据 possessortype 和possessorid来找最新的在此位置的布草集合
    List<ClothLog> getMaxClothLogByPossessor_typeAndPossessor_id(@Param("possessor_id") Integer possessor_id,
                                                                 @Param("possessor_type") Integer possessor_type);

    //根据missionId（旧）找到对应的不重复的clothidList【clothlog表】【张赢】
    List<Integer> getClothIdByClothId(@Param("mission_id") Integer missionIdByHidRidS2);

    //取判空条件(当前房间对应下没有任何log) 根据房间号查看这个房间有多少log <list>【张赢】
    List<ClothLog> getclothlogByroomId(ClothLog clothLog);
    //获取最新布草1.0
    List<ClothLog> getLogListByEmpId(@Param("hotel_id") int hotel_id,
                                     @Param(value = "possessor_id")Integer possessor_id,
                                     @Param("possessor_type")Integer possessor_type,@Param("status") Integer status);
    //根据唯一missid查找到clothlog对象
    List<ClothLog> getLogListByMissionId(@Param("mission_id") Integer mission_id);
    //根据唯一missid查找到clothlog对象
    Integer deleteLogById(@Param("id") Integer id);
    //根据唯一missid查找到clothlog对象
    Integer deleteLogByClothlog(ClothLog clothLog);
    //获取最新布草2.0
    List<ClothLog> GetLogListNearBy(@Param("hotel_id") int hotel_id,
                                    @Param("direction") Integer direction,
                                    @Param("possessor_id")Integer possessor_id,
                                    @Param("possessor_type")Integer possessor_type
                                   );
    //获取当前时间 所有本酒店布草数量总和
    Integer getCountByhotelId(@Param("hotel_id") int hotel_id);
    //得到当前酒店下 今日已经送去洗衣厂的布草总数 和 洗回来的是一个service【张赢】
    Integer getCountByhotelIdAndWashFactory(@Param("hotel_id") int hotel_id,
                                            @Param("possessor_type")Integer possessor_type,
                                            @Param("direction") Integer direction,
                                            @Param("starttime")Date starttime,
                                            @Param("endtime")Date endtime);

    //根据具体任务id给出具体新旧布草id与详情 检查该id下的新旧布草 【张赢】
    List<Integer> getLogListByMissionidPossessorTypeidDirectionid(ClothLog clothLog);



    //入参clothid 员工id 增加一条员工入log【张赢】---------------------------------一对出库操作 【员工入】
    int saveCleanerlog(@Param("cloth_id") Integer cloth_id,
                       @Param("possessor_id")String employee_id,
                       @Param("hotel_id")int hotel_id);
    //入参clothid 库房id 增加一条库房出log【张赢】--------------------------------一对出库操作【仓库出】
    int saveStorelog(@Param("cloth_id") Integer cloth_id,
                     @Param("possessor_id")int hotel_id,
                     @Param("hotel_id") int hotel_idr);

    //入参clothid 员工id 增加一条员工出log【张赢】+++++++++++++++++++++一对入库操作【员工出】
    int saveDirtyEmpOutlog(@Param("cloth_id") Integer cloth_id,
                       @Param("possessor_id")String employee_id,
                       @Param("hotel_id")int hotel_id);
    //入参clothid 库房id 增加一条库房入log【张赢】+++++++++++++++++++++一对入库操作【仓库入】
    int saveDirtyStoreInlog(@Param("cloth_id") Integer cloth_id,
                     @Param("possessor_id")int hotel_id,
                     @Param("hotel_id") int hotel_idr);


    //根据酒店id 根据出入状态 根据传值进来的possessor_type 数字 查出所有新的log 瞬时状态在谁手上
    List<ClothLog> getClothLogListBypossessor_type(@Param("hotel_id") String hotel_id,
                                                   @Param("possessor_type")Integer possessor_type);

    //根据酒店id 根据出入状态 根据传值进来的possessor_type 数字 查出所有新的log 瞬时状态在谁手上 分页专用
    List<ClothLog> getClothLogListBypossessor_type_comment(@Param("hotel_id") int hotel_id,
                                                   @Param("possessor_type")Integer possessor_type,
                                                   @Param("comment")Integer comment,
                                                           @Param("index")Integer index,
                                                           @Param("pageSize")Integer pageSize );
    //根据酒店id roomid 查log表 得到最新的 log对象
    List<ClothLog> getClothLogListByHotelidRoomid(@Param("hotel_id") int hotel_id,
                                                   @Param("possessor_type")Integer possessor_type,
                                                   @Param("room_id")Integer room_id );
    //根据酒店id 什么地方入的 possessor_type 就是在谁手里 和干净与否 status 查log表 得到最新的 log对象
    List<ClothLog> getClothLogListByPossessor_typeAndStutasAndTypeAndSize(@Param("hotel_id") int hotel_id,
                                                   @Param("possessor_type")Integer possessor_type,
                                                   @Param("status")Integer status,
                                                   @Param("index")Integer index,
                                                   @Param("pageSize")Integer pageSize);
    //根据酒店id 什么地方入的 possessor_type 就是在谁手里 和干净与否 status 查log表 得到最新的 log对象
    List<ClothLog> getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimitDownExcel(@Param("hotel_id") int hotel_id,
                                                   @Param("possessor_type")Integer possessor_type,
                                                   @Param("status")Integer status);
    //cout
    Integer getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimit(
                                                    @Param("hotel_id") int hotel_id,
                                                   @Param("possessor_type")Integer possessor_type,
                                                   @Param("status")Integer status);
    //导出log专用
    List<ClothLog> downgetClothLogList(@Param("hotel_id") int hotel_id,
                                                   @Param("possessor_type")Integer possessor_type,
                                                   @Param("comment")Integer comment);
    //算数专用
    Integer downgetClothLogListcount(@Param("hotel_id") int hotel_id,
                                                   @Param("possessor_type")Integer possessor_type,
                                                   @Param("comment")Integer comment);


    List<ClothLog> getClothLogListByClean(@Param("hotel_id") int hotel_id,
                                          @Param("mission_id")Integer mission_id,
                                          @Param("possessor_id")Integer possessor_id);


    ClothLog setUUID(@Param("cloth_id")Integer cloth_id);
}