package com.suidifu.bridgewater.handler;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryResult;
import com.suidifu.bridgewater.api.model.DeductQueryResult;
import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;

public interface DeductQueryApiHandler {

	public DeductQueryResult apideductQuery(OverdueDeductResultQueryModel queryModel,
			HttpServletRequest request);
	
	public List<BatchDeductStatusQueryResult> apiBatchDeductStatusQuery(
			BatchDeductStatusQueryModel queryModel, HttpServletRequest request);

}
