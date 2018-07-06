package com.zufangbao.earth.handler.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.handler.PositionHandler;
import com.zufangbao.earth.service.PositionService;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.TemporaryRepurchaseJson;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.Position;
import com.zufangbao.sun.yunxin.entity.model.PositionDetailModel;
import com.zufangbao.sun.yunxin.entity.model.PositionQueryModel;
import com.zufangbao.sun.yunxin.entity.model.PositionShowModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.log.TemporaryRepurchaseLog;

@Component("positionHandler")
public class PositionHandlerImpl implements PositionHandler{
	
	@Autowired
	public FinancialContractService financialContractService;
	@Autowired
	public PositionService positionService;
	@Autowired
	public PrincipalService principalService;
	
	@Override
	public Map<String, Object> queryPosition(PositionQueryModel queryModel,Page page) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(queryModel == null) {
			result.put("size", 0);
			result.put("list", Collections.emptyList());
			return result;
		}
		List<FinancialContract> financialContractList =financialContractService.loadAll(FinancialContract.class);
		List<Position> positionList = null;
		if(queryModel.getPositionName() != null){
			positionList = positionService.getPositionListByName(queryModel.getPositionName());
		}
		List<FinancialContract> list = financialContractService.getFinancialContractListForPositionBy(queryModel, page, financialContractList, positionList);
		List<FinancialContract> all  = financialContractService.getFinancialContractListForPositionBy(queryModel, null, financialContractList,positionList);
		List<PositionShowModel> showModelList = new ArrayList<>();
		for(FinancialContract financialContract : list){
			List<Position> positions = positionService.getPositionListBy(financialContract.getFinancialContractUuid());
			PositionShowModel showModel = new PositionShowModel(financialContract,positions);
			showModelList.add(showModel);
		}
		result.put("size", all.size());
		result.put("list", showModelList);
		return result;
	}
	
	@Override
	public List<PositionDetailModel> getPositionDetailModel(List<Position> positions) {
		if(positions == null){
			return null;
		}
		List<PositionDetailModel> PositionDetailModelList = new ArrayList<>();
		for(Position position:positions){
			Principal principal = principalService.getPrincipalById(position.getPrincipalId());
			PositionDetailModel positionDetailModel = new PositionDetailModel(position,principal);
			PositionDetailModelList.add(positionDetailModel);
		}
		return PositionDetailModelList;
	}
	
	@Override
	public String createPosition(List<PositionDetailModel>positionDetailList,String financialContractUuid){
		if(financialContractUuid == null ){
			return "系统错误！";
		}
		int n = 1;
		for(PositionDetailModel positionDetailModel:positionDetailList){
			Position position = new Position();
			position.setFinancialContractUuid(financialContractUuid);
			position.setPosition(positionDetailModel.getPosition());
			position.setPositionNo(n);
			position.setName(positionDetailModel.getName());
			position.setPhone(positionDetailModel.getPhone());
			position.setPrincipalId(positionDetailModel.getPrincipalId());
			n = n+1 ;	
			positionService.save(position);
		}
		return null;
	}
	
	@Override
	public String deletePosition(List<Position> positions){
		if(positions == null){
			return null;
		}
		for(Position position : positions){
			Long id = position.getId();
			positionService.delete(Position.class, id);
		}
		return null;
	}

}
