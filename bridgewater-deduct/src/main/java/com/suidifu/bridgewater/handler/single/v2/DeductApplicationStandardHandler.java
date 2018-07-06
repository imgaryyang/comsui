package com.suidifu.bridgewater.handler.single.v2;

import javax.servlet.http.HttpServletRequest;

import com.suidifu.bridgewater.model.v2.StandardDeductModel;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;

/**
 * Created by zhenghangbo on 03/05/2017.
 */
public interface DeductApplicationStandardHandler {

public DeductApplication  createDeductApplicationByRule(HttpServletRequest request, StandardDeductModel deductModel, String creatorName);
    
	public  void dataPreCheckAndFillInformation(StandardDeductModel deductModel);

	public DeductApplication createDeductApplicationByNewRule(String IpAddress , StandardDeductModel deductModel,
			String creatorName);
	
	public DeductPlan createDeductPlanAndDeductApplicationByRule(HttpServletRequest request, StandardDeductModel deductModel, String creatorName);
}
