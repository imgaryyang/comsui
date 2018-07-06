package com.zufangbao.earth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.zufangbao.earth.service.PositionService;
import com.zufangbao.sun.yunxin.entity.Position;
import com.zufangbao.sun.yunxin.entity.RequestLog;

@Service("positionService")
public class PositionServiceImpl extends GenericServiceImpl<Position> implements PositionService{
	@Override
	public List<Position> getPositionListBy(String financialContractUuid) {
		String sql = "select * from t_position  where financial_contract_uuid =:financialContractUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuid", financialContractUuid);
		sql += " ORDER BY position_no ";
		return this.genericDaoSupport.queryForList(sql.toString(), params, Position.class);
	}
	@Override
	public List<Position> getPositionListByName(String PositionName) {
		String sql = "select * from t_position  where name =:name";
		Map<String, Object> params = new HashMap<>();
		params.put("name", PositionName);
		return this.genericDaoSupport.queryForList(sql.toString(), params, Position.class);
	}

}
