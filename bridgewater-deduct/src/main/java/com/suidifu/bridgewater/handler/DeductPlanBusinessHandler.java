package com.suidifu.bridgewater.handler;

import java.util.List;

import com.suidifu.bridgewater.api.model.ReDeductDataPackage;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;

public interface DeductPlanBusinessHandler {

	public void updateDeductPlanStatusByQueryResult(String deductApplicationUuid, List<QueryStatusResult> queryStatusResults);
	
	
	public ReDeductDataPackage castRedeductPackage(String deductApplicationUuid, List<QueryStatusResult> queryStatusResults);
	
	public void updateDeductPlanStatusByQueryResultV2(String deductApplicationUuid, QueryStatusResult queryStatusResult);

	public void updateDeductPlanByRepaymentOrder(DeductApplication deductApplication);

}
