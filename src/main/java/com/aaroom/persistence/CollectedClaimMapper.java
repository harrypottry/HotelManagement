package com.aaroom.persistence;

import com.aaroom.beans.CollectedClaim;
import com.aaroom.beans.CollectedClaimExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectedClaimMapper {
    int countByExample(CollectedClaimExample example);

    int deleteByExample(CollectedClaimExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CollectedClaim record);

    int insertSelective(CollectedClaim record);

    List<CollectedClaim> selectByExample(CollectedClaimExample example);

    CollectedClaim selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CollectedClaim record, @Param("example") CollectedClaimExample example);

    int updateByExample(@Param("record") CollectedClaim record, @Param("example") CollectedClaimExample example);

    int updateByPrimaryKeySelective(CollectedClaim record);

    int updateByPrimaryKey(CollectedClaim record);
}