package com.zufangbao.earth.yunxin.api.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.api.model.query.MutableFeeDetailsResultModel;
import com.zufangbao.sun.yunxin.log.MutableFeeDetailLog;

public interface MutableFeeDetailApiHandler {
	public MutableFeeDetailsResultModel queryMutableFeeDetails(String singleLoanContractNo, Page page);
	public MutableFeeDetailLog getMutableFeeDetailLog(String singleLoanContractNo);

}
