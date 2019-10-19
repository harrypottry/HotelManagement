package com.aaroom.persistence;
import com.aaroom.beans.CentralWarehouseEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CentralWarehouseEmployeeMapper {

	public CentralWarehouseEmployee getById(@Param(value = "id")Integer id)throws Exception;

	public List<CentralWarehouseEmployee>	getListByMap(Map<String, Object> param)throws Exception;

	public Integer save(CentralWarehouseEmployee centralWarehouseEmployee)throws Exception;

	public Integer modify(CentralWarehouseEmployee centralWarehouseEmployee)throws Exception;

	public Integer removeById(@Param(value = "id")Integer id)throws Exception;

}
