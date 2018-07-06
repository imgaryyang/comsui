package com.suidifu.hathaway.redis.entity.financial;

public class FastFinancialContract {

	private String financialContractUuid;

	/**
	 * 账本编号
	 */
	private String ledgerBookNo;

	/**
	 * 贷款逾期开始日 注：set此字段时，请同时 set还款宽限期（日）（advaRepaymentTerm）为 此字段值-1
	 */
	private int loanOverdueStartDay;

	/**
	 * 是否由系统产生逾期费用，逾期费用生成方式 T:系统生成F:对手方传递
	 */
	private boolean sysCreatePenaltyFlag = false;

	/**
	 * 是否系统产生结算单
	 */
	private boolean sysCreateStatementFlag = false;

	/**
	 * 正常扣款 T:系统定时发起 F:接口主动调用
	 */
	private boolean sysNormalDeductFlag = false;

	/**
	 * 逾期扣款 T:系统定时发起 F:接口主动调用
	 */
	private boolean sysOverdueDeductFlag = false;
	
	private String companyUuid;

	public FastFinancialContract() {
		super();
	}

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getLedgerBookNo() {
		return ledgerBookNo;
	}

	public void setLedgerBookNo(String ledgerBookNo) {
		this.ledgerBookNo = ledgerBookNo;
	}

	public int getLoanOverdueStartDay() {
		return loanOverdueStartDay;
	}

	public void setLoanOverdueStartDay(int loanOverdueStartDay) {
		this.loanOverdueStartDay = loanOverdueStartDay;
	}

	public boolean isSysCreatePenaltyFlag() {
		return sysCreatePenaltyFlag;
	}

	public void setSysCreatePenaltyFlag(boolean sysCreatePenaltyFlag) {
		this.sysCreatePenaltyFlag = sysCreatePenaltyFlag;
	}

	public boolean isSysCreateStatementFlag() {
		return sysCreateStatementFlag;
	}

	public void setSysCreateStatementFlag(boolean sysCreateStatementFlag) {
		this.sysCreateStatementFlag = sysCreateStatementFlag;
	}

	public boolean isSysNormalDeductFlag() {
		return sysNormalDeductFlag;
	}

	public void setSysNormalDeductFlag(boolean sysNormalDeductFlag) {
		this.sysNormalDeductFlag = sysNormalDeductFlag;
	}

	public boolean isSysOverdueDeductFlag() {
		return sysOverdueDeductFlag;
	}

	public void setSysOverdueDeductFlag(boolean sysOverdueDeductFlag) {
		this.sysOverdueDeductFlag = sysOverdueDeductFlag;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}
}
