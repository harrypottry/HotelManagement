package com.aaroom.persistence;

import com.aaroom.beans.RoomMessage;
import com.aaroom.beans.RoomMessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomMessageMapper {
    int countByExample(RoomMessageExample example);

    int deleteByExample(RoomMessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoomMessage record);

    int insertSelective(RoomMessage record);

    List<RoomMessage> selectByExample(RoomMessageExample example);

    RoomMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoomMessage record, @Param("example") RoomMessageExample example);

    int updateByExample(@Param("record") RoomMessage record, @Param("example") RoomMessageExample example);

    int updateByPrimaryKeySelective(RoomMessage record);

    int updateByPrimaryKey(RoomMessage record);
}