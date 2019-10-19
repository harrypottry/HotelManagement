package com.aaroom.persistence;

import com.aaroom.beans.Cloth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClothMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cloth record);

    int insertSelective(Cloth record);

    int insertQRCodeAndRfid(Cloth record);

    Cloth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cloth record);

    int updateByPrimaryKey(Cloth record);

    int getIdByClothTypeId(Integer cloth_type_id);

    int insertClothByClothTypeId(Integer cloth_type_id);

    Cloth getClothDetail(Integer id);
    //根据clothid获取cloth对象出来
    Cloth getClothByid(Integer id);
    //直接获取现有所有clothidlist
    List<Integer> getClothid();
    //根据Rfid  找cloth 【张赢】
    Cloth getClothByRfid(@Param(value = "rfID") String rfID);

    //发布任务的同时修改房间内干净布草变成脏布草  公共方法可以把布草编程干净 第三个参数为值 0为干净 1为脏
    int updateStatusByclothId(@Param(value = "id")Integer id,
                              @Param(value = "status")Integer status);

    List<Cloth> getLatestClothList(@Param(value = "start")Date start,
                                   @Param(value = "cloth_type_ids")Integer[] cloth_type_ids,
                                   @Param(value = "size")Integer size);

    Cloth selectByrfID(@Param(value = "rfID")String rfID);
}