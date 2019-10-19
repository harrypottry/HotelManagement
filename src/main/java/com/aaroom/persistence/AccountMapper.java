package com.aaroom.persistence;

import com.aaroom.beans.Account;
import com.aaroom.beans.AccountExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {
    int countByExample(AccountExample example);

    int deleteByExample(AccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountExample example);

    Account selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Account record, @Param("example") AccountExample example);

    int updateByExample(@Param("record") Account record, @Param("example") AccountExample example);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    //根据酒店ID查询该酒店各个账单总和
    List<Account> getAccountSum(@Param(value = "hotelId")Integer hotelId);

    //查询该账单本次认领是否完成
    Account getAccountInfo(@Param(value = "id")Integer id);

    void updateAccountStatus(Account account);
}