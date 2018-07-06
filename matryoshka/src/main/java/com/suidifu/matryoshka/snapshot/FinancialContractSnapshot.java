package com.suidifu.matryoshka.snapshot;

import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractType;

import java.util.Date;

/**
 * Created by fanxiaofan on 2017/4/24.
 */
public class FinancialContractSnapshot {
	private String financialContractUuid;
	/**
	 * 信托产品代码
	 */
	private String contractNo;
	/**
	 * 信托合同名称
	 */
	private String contractName;
	/**
	 * 还款宽限期（日） 注：set此字段时，请同时 set贷款逾期开始日（loanOverdueStartDay）为 此字段值+1
	 */
	private int advaRepaymentTerm;
	/**
	 * 是否支持随心还
	 */
	private boolean allowFreewheelingRepayment = false;
	/**
	 * 信托合同起始日期（期限）
	 */
	private Date advaStartDate;

	/**
	 * 信托合同截止日期（期限）
	 */
	private Date thruDate;

	/**
	 * 信托合约类型 0:消费贷,1:小微企业贷款
	 */
	private FinancialContractType financialContractType = FinancialContractType.ConsumerLoan;

	public FinancialContractSnapshot() {
		super();
	}

	public FinancialContractSnapshot(String financialContractUuid, String contractNo, String contractName,
			int advaRepaymentTerm, boolean allowFreewheelingRepayment, Date advaStartDate, Date thruDate,
			FinancialContractType financialContractType) {
		super();
		this.financialContractUuid = financialContractUuid;
		this.contractNo = contractNo;
		this.contractName = contractName;
		this.advaRepaymentTerm = advaRepaymentTerm;
		this.allowFreewheelingRepayment = allowFreewheelingRepayment;
		this.advaStartDate = advaStartDate;
		this.thruDate = thruDate;
		this.financialContractType = financialContractType;
	}

	public FinancialContractSnapshot(FinancialContract financialContract) {
		this.financialContractUuid = financialContract.getFinancialContractUuid();
		this.contractNo = financialContract.getContractNo();
		this.contractName = financialContract.getContractName();
		this.advaRepaymentTerm = financialContract.getAdvaRepaymentTerm();
		this.allowFreewheelingRepayment = financialContract.isAllowFreewheelingRepayment();
		this.advaStartDate = financialContract.getAdvaStartDate();
		this.thruDate = financialContract.getThruDate();
		this.financialContractType = financialContract.getFinancialContractType();
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public int getAdvaRepaymentTerm() {
		return advaRepaymentTerm;
	}

	public void setAdvaRepaymentTerm(int advaRepaymentTerm) {
		this.advaRepaymentTerm = advaRepaymentTerm;
	}

	public boolean isAllowFreewheelingRepayment() {
		return allowFreewheelingRepayment;
	}

	public void setAllowFreewheelingRepayment(boolean allowFreewheelingRepayment) {
		this.allowFreewheelingRepayment = allowFreewheelingRepayment;
	}

	public Date getAdvaStartDate() {
		return advaStartDate;
	}

	public void setAdvaStartDate(Date advaStartDate) {
		this.advaStartDate = advaStartDate;
	}

	public Date getThruDate() {
		return thruDate;
	}

	public void setThruDate(Date thruDate) {
		this.thruDate = thruDate;
	}

	public FinancialContractType getFinancialContractType() {
		return financialContractType;
	}

	public void setFinancialContractType(FinancialContractType financialContractType) {
		this.financialContractType = financialContractType;
	}

}
