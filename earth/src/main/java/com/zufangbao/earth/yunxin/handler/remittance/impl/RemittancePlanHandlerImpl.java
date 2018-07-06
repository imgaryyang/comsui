package com.zufangbao.earth.yunxin.handler.remittance.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.handler.remittance.RemittancetPlanHandler;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanShowModel;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("remittancetPlanHandler")
public class RemittancePlanHandlerImpl implements RemittancetPlanHandler{

	@Autowired
	private IRemittancePlanService iRemittancePlanService;

	@Value("#{config['notify_url']}")
	private String notify_url;

	@Override
	public List<RemittancePlanShowModel> queryShowModelList(RemittancePlanQueryModel queryModel, Page page) {
		List<RemittancePlan> remittancePlans = iRemittancePlanService.queryRemittancePlan(queryModel, page);
		List<RemittancePlanShowModel> remittancePlanShowModels = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(remittancePlans)){
			remittancePlanShowModels = remittancePlans.stream().map(c-> new RemittancePlanShowModel(c)).collect(Collectors.toList());
		}
		return remittancePlanShowModels;
	}

}
