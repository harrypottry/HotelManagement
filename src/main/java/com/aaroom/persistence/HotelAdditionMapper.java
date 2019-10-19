package com.aaroom.persistence;

import com.aaroom.beans.HotelAddition;
import com.aaroom.beans.HotelAdditionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelAdditionMapper {
    int countByExample(HotelAdditionExample example);

    int deleteByExample(HotelAdditionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HotelAddition record);

    int insertSelective(HotelAddition record);

    List<HotelAddition> selectByExample(HotelAdditionExample example);

    HotelAddition selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HotelAddition record, @Param("example") HotelAdditionExample example);

    int updateByExample(@Param("record") HotelAddition record, @Param("example") HotelAdditionExample example);

    int updateByPrimaryKeySelective(HotelAddition record);

    int updateByPrimaryKey(HotelAddition record);
}