package com.zufangbao.earth.yunxin.api.handler.impl;

import com.zufangbao.earth.yunxin.api.handler.AccountBalanceApiHandler;
import com.zufangbao.earth.yunxin.api.model.query.AccontBalanceQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.AccontBalanceResultModel;
import com.zufangbao.earth.yunxin.exception.QueryBalanceException;
import com.zufangbao.earth.yunxin.handler.CapitalHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component("AccountBalanceApiHandler")
public class AccountBalanceApiHandlerImpl implements AccountBalanceApiHandler {
	
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private CapitalHandler capitalHandler;
	
	@Override
	public AccontBalanceResultModel queryAccountBalance(AccontBalanceQueryModel queryModel){
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(queryModel.getProductCode());
		if (financialContract == null) {
			throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
		}
		
		Account capitalAccount = financialContract.getCapitalAccount();
		if (capitalAccount == null) {
			throw new ApiException(ApiResponseCode.TRUST_CODE_NOT_AGREE_WITH_ACCOUNT_NO);
		}
		
		if (!queryModel.getAccountNo().equals(capitalAccount.getAccountNo())) {
			throw new ApiException(ApiResponseCode.TRUST_CODE_NOT_AGREE_WITH_ACCOUNT_NO);
		}
		
		Date queryTime = null;
		BigDecimal accountBalance = BigDecimal.ZERO;
		try {
			queryTime = new Date();
			accountBalance = capitalHandler.queryAccountBalance(capitalAccount);
		} catch (QueryBalanceException e) {
			throw new ApiException(ApiResponseCode.SYSTEM_BUSY);
		}
		
		return new AccontBalanceResultModel(financialContract, capitalAccount, accountBalance, queryTime);
	}

}
