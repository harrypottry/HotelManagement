package com.aaroom.persistence;

import com.aaroom.beans.HotelContacts;
import com.aaroom.beans.HotelContactsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelContactsMapper {
    int countByExample(HotelContactsExample example);

    int deleteByExample(HotelContactsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HotelContacts record);

    int insertSelective(HotelContacts record);

    List<HotelContacts> selectByExample(HotelContactsExample example);

    HotelContacts selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HotelContacts record, @Param("example") HotelContactsExample example);

    int updateByExample(@Param("record") HotelContacts record, @Param("example") HotelContactsExample example);

    int updateByPrimaryKeySelective(HotelContacts record);

    int updateByPrimaryKey(HotelContacts record);
}