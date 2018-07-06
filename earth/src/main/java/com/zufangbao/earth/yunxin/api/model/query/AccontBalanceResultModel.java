package com.zufangbao.earth.yunxin.api.model.query;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.financial.FinancialContract;

import java.math.BigDecimal;
import java.util.Date;

public class AccontBalanceResultModel {

	private String productCode;// 信托产品代码

	private String capitalAccountNo;// 信托专户帐号

	private String capitalAccountName;// 信托专户名

	private String capitalAccountBankName;// 专户开户行

	private String queryTime;// 查询时间

	private BigDecimal balance;// 专户余额
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCapitalAccountNo() {
		return capitalAccountNo;
	}

	public void setCapitalAccountNo(String capitalAccountNo) {
		this.capitalAccountNo = capitalAccountNo;
	}

	public String getCapitalAccountName() {
		return capitalAccountName;
	}

	public void setCapitalAccountName(String capitalAccountName) {
		this.capitalAccountName = capitalAccountName;
	}

	public String getCapitalAccountBankName() {
		return capitalAccountBankName;
	}

	public void setCapitalAccountBankName(String capitalAccountBankName) {
		this.capitalAccountBankName = capitalAccountBankName;
	}

	public String getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public AccontBalanceResultModel() {
		super();
	}

	public AccontBalanceResultModel(FinancialContract financialContract, Account capitalAccount,
			BigDecimal accountBalance, Date queryTime) {
		this.productCode = financialContract.getContractNo();
		this.capitalAccountNo = capitalAccount.getAccountNo();
		this.capitalAccountName = capitalAccount.getAccountName();
		this.capitalAccountBankName = capitalAccount.getBankName();
		if (queryTime != null) {
			this.queryTime = DateUtils.format(queryTime, "yyyy-MM-dd HH:mm:ss");
		}
		this.balance = accountBalance;

	}

}
