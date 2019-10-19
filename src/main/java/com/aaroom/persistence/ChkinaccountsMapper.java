package com.aaroom.persistence;

import java.util.List;

import com.aaroom.beans.Chkinaccounts;
import com.aaroom.beans.ChkinaccountsExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChkinaccountsMapper {
    int countByExample(ChkinaccountsExample example);

    int deleteByExample(ChkinaccountsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Chkinaccounts record);

    int insertSelective(Chkinaccounts record);

    List<Chkinaccounts> selectByExample(ChkinaccountsExample example);

    Chkinaccounts selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Chkinaccounts record, @Param("example") ChkinaccountsExample example);

    int updateByExample(@Param("record") Chkinaccounts record, @Param("example") ChkinaccountsExample example);

    int updateByPrimaryKeySelective(Chkinaccounts record);

    int updateByPrimaryKey(Chkinaccounts record);
}