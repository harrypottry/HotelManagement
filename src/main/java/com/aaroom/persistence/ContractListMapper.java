package com.aaroom.persistence;

import com.aaroom.beans.ContractList;
import com.aaroom.beans.ContractListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractListMapper {
    int countByExample(ContractListExample example);

    int deleteByExample(ContractListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContractList record);

    int insertSelective(ContractList record);

    List<ContractList> selectByExample(ContractListExample example);

    ContractList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContractList record, @Param("example") ContractListExample example);

    int updateByExample(@Param("record") ContractList record, @Param("example") ContractListExample example);

    int updateByPrimaryKeySelective(ContractList record);

    int updateByPrimaryKey(ContractList record);
}