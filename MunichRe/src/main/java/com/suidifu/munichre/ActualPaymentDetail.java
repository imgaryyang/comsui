package com.suidifu.munichre;

import java.math.BigDecimal;

public class ActualPaymentDetail {
	/** 实际还款金额 **/
	private BigDecimal actualRepaymentAmount = BigDecimal.ZERO;
	/** 实收本金 **/
	private BigDecimal actualPrincipal = BigDecimal.ZERO;
	/** 实收利息 **/
	private BigDecimal actualInterest = BigDecimal.ZERO;
	/** 实收贷款服务费 **/
	private BigDecimal actualLoanServiceFee = BigDecimal.ZERO;
	/** 实收技术维护费 **/
	private BigDecimal actualTechFee = BigDecimal.ZERO;
	/** 实收其他费用 **/
	private BigDecimal actualOtherFee = BigDecimal.ZERO;
	/** 实收罚息 **/
	private BigDecimal actualOverduePenalty = BigDecimal.ZERO;
	/** 实收逾期违约金 **/
	private BigDecimal actualOverdueDefaultFee = BigDecimal.ZERO;
	/** 实收逾期服务费 **/
	private BigDecimal actualOverdueServiceFee = BigDecimal.ZERO;
	/** 实收逾期其他费用 **/
	private BigDecimal actualOverdueOtherFee = BigDecimal.ZERO;
	
	/** 实收金额 **/
	public BigDecimal getActualTotalFee() {
		return actualPrincipal
				.add(actualInterest)
				.add(actualLoanServiceFee)
				.add(actualTechFee)
				.add(actualOtherFee)
				.add(actualOverduePenalty)
				.add(actualOverdueDefaultFee)
				.add(actualOverdueServiceFee)
				.add(actualOverdueOtherFee);
	}

	public BigDecimal getActualRepaymentAmount() {
		return actualRepaymentAmount;
	}

	public void setActualRepaymentAmount(BigDecimal actualRepaymentAmount) {
		this.actualRepaymentAmount = actualRepaymentAmount;
	}

	public BigDecimal getActualPrincipal() {
		return actualPrincipal;
	}

	public void setActualPrincipal(BigDecimal actualPrincipal) {
		this.actualPrincipal = actualPrincipal;
	}

	public BigDecimal getActualInterest() {
		return actualInterest;
	}

	public void setActualInterest(BigDecimal actualInterest) {
		this.actualInterest = actualInterest;
	}

	public BigDecimal getActualLoanServiceFee() {
		return actualLoanServiceFee;
	}

	public void setActualLoanServiceFee(BigDecimal actualLoanServiceFee) {
		this.actualLoanServiceFee = actualLoanServiceFee;
	}

	public BigDecimal getActualTechFee() {
		return actualTechFee;
	}

	public void setActualTechFee(BigDecimal actualTechFee) {
		this.actualTechFee = actualTechFee;
	}

	public BigDecimal getActualOtherFee() {
		return actualOtherFee;
	}

	public void setActualOtherFee(BigDecimal actualOtherFee) {
		this.actualOtherFee = actualOtherFee;
	}

	public BigDecimal getActualOverduePenalty() {
		return actualOverduePenalty;
	}

	public void setActualOverduePenalty(BigDecimal actualOverduePenalty) {
		this.actualOverduePenalty = actualOverduePenalty;
	}

	public BigDecimal getActualOverdueDefaultFee() {
		return actualOverdueDefaultFee;
	}

	public void setActualOverdueDefaultFee(BigDecimal actualOverdueDefaultFee) {
		this.actualOverdueDefaultFee = actualOverdueDefaultFee;
	}

	public BigDecimal getActualOverdueServiceFee() {
		return actualOverdueServiceFee;
	}

	public void setActualOverdueServiceFee(BigDecimal actualOverdueServiceFee) {
		this.actualOverdueServiceFee = actualOverdueServiceFee;
	}

	public BigDecimal getActualOverdueOtherFee() {
		return actualOverdueOtherFee;
	}

	public void setActualOverdueOtherFee(BigDecimal actualOverdueOtherFee) {
		this.actualOverdueOtherFee = actualOverdueOtherFee;
	}
}

