package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.query.AccontBalanceQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.AccontBalanceResultModel;

public interface AccountBalanceApiHandler {
	
	public  AccontBalanceResultModel queryAccountBalance(AccontBalanceQueryModel queryModel) ;
	
}
