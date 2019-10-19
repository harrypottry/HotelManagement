package com.aaroom.persistence;

import com.aaroom.beans.DesignPic;
import com.aaroom.beans.DesignPicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignPicMapper {
    int countByExample(DesignPicExample example);

    int deleteByExample(DesignPicExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DesignPic record);

    int insertSelective(DesignPic record);

    List<DesignPic> selectByExample(DesignPicExample example);

    DesignPic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DesignPic record, @Param("example") DesignPicExample example);

    int updateByExample(@Param("record") DesignPic record, @Param("example") DesignPicExample example);

    int updateByPrimaryKeySelective(DesignPic record);

    int updateByPrimaryKey(DesignPic record);
}