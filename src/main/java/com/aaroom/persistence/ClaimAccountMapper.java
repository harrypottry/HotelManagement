package com.aaroom.persistence;

import com.aaroom.beans.ClaimAccount;
import com.aaroom.beans.ClaimAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimAccountMapper {
    int countByExample(ClaimAccountExample example);

    int deleteByExample(ClaimAccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClaimAccount record);

    int insertSelective(ClaimAccount record);

    List<ClaimAccount> selectByExample(ClaimAccountExample example);

    ClaimAccount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClaimAccount record, @Param("example") ClaimAccountExample example);

    int updateByExample(@Param("record") ClaimAccount record, @Param("example") ClaimAccountExample example);

    int updateByPrimaryKeySelective(ClaimAccount record);

    int updateByPrimaryKey(ClaimAccount record);
}