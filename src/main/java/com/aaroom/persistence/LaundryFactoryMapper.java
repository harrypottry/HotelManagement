package com.aaroom.persistence;
import com.aaroom.beans.LaundryFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface LaundryFactoryMapper {

	public LaundryFactory getById(@Param(value = "id") Integer id)throws Exception;

	public List<LaundryFactory> getListByMap(Map<String, Object> param)throws Exception;

	public Integer save(LaundryFactory laundryFactory)throws Exception;

	public Integer modify(LaundryFactory laundryFactory)throws Exception;

	public Integer removeById(@Param(value = "id") Integer id)throws Exception;

}
