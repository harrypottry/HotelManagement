package com.aaroom.persistence;
import com.aaroom.beans.CentralWarehouse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CentralWarehouseMapper {

	public CentralWarehouse getById(@Param(value = "id") Long id)throws Exception;

	public List<CentralWarehouse>	getListByMap(Map<String, Object> param)throws Exception;

	public Integer save(CentralWarehouse centralWarehouse)throws Exception;

	public Integer modify(CentralWarehouse centralWarehouse)throws Exception;

	public Integer removeById(@Param(value = "id") Long id)throws Exception;

}
