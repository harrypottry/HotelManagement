package com.aaroom.persistence;

import com.aaroom.beans.HotelPath;
import com.aaroom.beans.HotelPathExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelPathMapper {
    int countByExample(HotelPathExample example);

    int deleteByExample(HotelPathExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HotelPath record);

    int insertSelective(HotelPath record);

    List<HotelPath> selectByExample(HotelPathExample example);

    HotelPath selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HotelPath record, @Param("example") HotelPathExample example);

    int updateByExample(@Param("record") HotelPath record, @Param("example") HotelPathExample example);

    int updateByPrimaryKeySelective(HotelPath record);

    int updateByPrimaryKey(HotelPath record);
}