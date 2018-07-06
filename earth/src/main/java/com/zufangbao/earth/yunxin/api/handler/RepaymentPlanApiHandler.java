package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanBatchQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanQueryModel;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.yunxin.entity.AssetSet;

import java.util.List;
import java.util.Map;

public interface RepaymentPlanApiHandler {
	
	/**
	 * 根据还款计划查询模型，查询还款计划
	 * @param queryModel 还款计划查询模型，不能为NULL
	 * @return 还款计划列表 
	 */
	List<RepaymentPlanDetail> queryRepaymentPlanDetail(RepaymentPlanQueryModel queryModel);

    List<RepaymentPlanDetail> convertRepaymentPlanDetails(List<AssetSet> assetSetList,boolean isBatchQuery);

    /**
	 * 根据还款计划批量查询模型，查询还款计划。
	 * @param queryModel 还款计划批量查询模型
	 * @return key为合同uniqueId，value为还款计划列表
	 */
	List<Map<String, Object>> queryRepaymentPlanDetail(RepaymentPlanBatchQueryModel queryModel);
	
	/**
	 * 根据还款计划查询模型，查询回购单
	 * @param queryModel 还款计划批量查询模型
	 * @return 回购单
	 */
	RepurchaseDoc queryRepurchaseDoc(RepaymentPlanQueryModel queryModel);
	
}
