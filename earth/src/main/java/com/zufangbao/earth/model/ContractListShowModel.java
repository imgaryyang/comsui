package com.zufangbao.earth.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.tag.TagConfigVO;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.ContractCompletionStatus;
import com.zufangbao.sun.yunxin.entity.ContractStateAdaptaterView;
import com.zufangbao.sun.yunxin.entity.RepaymentWay;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;

public class ContractListShowModel {

	private Long id;

	private String uniqueId;

	private String contractNo;

	private String contractUuid;

	private BigDecimal interestRate;

	private Date beginDate;

	private Date endDate;

	private RepaymentWay repaymentWay;

	private int periods;

	private int paymentFrequency;

	private String cpBankAccountHolder;

	private String customerName;

	private BigDecimal totalAmount;

	private ContractStateAdaptaterView state;

    /**
     * 非联动标签
     */
    List<TagConfigVO> tags;

    /**
     * 联动标签
     */
    List<TagConfigVO> transitivityTags;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public RepaymentWay getRepaymentWay() {
		return repaymentWay;
	}

	public String getRepaymentWayMsg() {
		return repaymentWay == null ? "" : repaymentWay.getChineseMessage();
	}

	public void setRepaymentWay(RepaymentWay repaymentWay) {
		this.repaymentWay = repaymentWay;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

	public int getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(int paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public String getCpBankAccountHolder() {
		return cpBankAccountHolder;
	}

	public void setCpBankAccountHolder(String cpBankAccountHolder) {
		this.cpBankAccountHolder = cpBankAccountHolder;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ContractStateAdaptaterView getState() {
		return state;
	}

	public String getStateMsg() {
		return getStateAllShowMsg();
	}

	public void setState(ContractStateAdaptaterView state) {
		this.state = state;
	}

	public List<TagConfigVO> getTags() {
		return tags;
	}

	public void setTags(List<TagConfigVO> tags) {
		this.tags = tags;
	}

	public String getContractUuid() {
		return contractUuid;
	}

	public void setContractUuid(String contractUuid) {
		this.contractUuid = contractUuid;
	}

    public List<TagConfigVO> getTransitivityTags() {
        return transitivityTags;
    }

    public void setTransitivityTags(List<TagConfigVO> transitivityTags) {
        this.transitivityTags = transitivityTags;
    }

    public ContractListShowModel() {
		super();
	}

	public ContractListShowModel(Contract contract, RemittancePlan remittancePlan, List<TagConfigVO> tags, List<TagConfigVO> transitivityTags) {
		super();
		this.id = contract.getId();
		this.contractUuid = contract.getUuid();
		this.uniqueId = contract.getUniqueId();
		this.contractNo = contract.getContractNo();
		this.interestRate = contract.getInterestRate();
		this.beginDate = contract.getBeginDate();
		this.endDate = contract.getEndDate();
		this.repaymentWay = contract.getRepaymentWay();
		this.periods = contract.getPeriods();
		this.paymentFrequency = contract.getPaymentFrequency();
		this.customerName = contract.getCustomer().getName();
		this.totalAmount = contract.getTotalAmount();
		this.state = ContractState.convertToContractStateAdaptaterView(contract);
		this.tags = tags;
		this.transitivityTags = transitivityTags;
		if (remittancePlan != null) {
			this.cpBankAccountHolder = remittancePlan.getCpBankAccountHolder();
		}
	}

	public String getStateAllShowMsg(){
		if(this.state == null){
			return "";
		}
		return getState().getChineseMessage();
	}

}
