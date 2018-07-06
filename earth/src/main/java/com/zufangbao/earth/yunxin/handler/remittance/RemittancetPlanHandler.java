package com.zufangbao.earth.yunxin.handler.remittance;

import java.util.List;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanShowModel;


public interface RemittancetPlanHandler {
	
	public List<RemittancePlanShowModel>  queryShowModelList(RemittancePlanQueryModel queryModel, Page page);
	
}
