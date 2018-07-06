package com.suidifu.bridgewater.model.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.utils.EnumUtil;

/**
 * @author wukai
 * 批扣明细
 */
public class BatchDeductItem {
	
	private int index;
	
	private String deductApplicationUuid;

	private String deductId;
	
	private String financialProductCode;
	
	private String uniqueId;
	
	private String contractNo;
	
	private Integer transType;
	
	private BigDecimal amount;
	
	private Integer repaymentType;
	
	private String mobile;
	
	private String accountName;
	
	private String accountNo;
	
	private String gateway;
	
	private List<RepaymentDetail> repaymentDetails = new ArrayList<>();

	private static List<Integer> transTypeList = new ArrayList<Integer>(){
		{
			add(0);
			add(1);
		}
	};
	private static List<Integer> repaymentTypeList = new ArrayList<Integer>(){
		{
			add(0);
			add(1);
			add(2);
		}
	};
	
	
	public BatchDeductItem(){
		
	}
	
	private static List<PaymentInstitutionName> gateWayList = Arrays.asList(PaymentInstitutionName.values());
	
	
	public void validate() throws IllegalArgumentException{
		
		//必填项校验
		if(StringUtils.isBlank(this.getDeductId())) {
			throw new IllegalArgumentException ("扣款唯一编号为空");
		}
		if(StringUtils.isBlank(this.getFinancialProductCode())){
			throw new IllegalArgumentException ("信托产品代码为空");
		}
		if(!transTypeList.contains(this.getSafeTransType())){
			throw new IllegalArgumentException("交易类型应该是0或1,实际上是["+this.getSafeTransType()+"]");
		}
		if(BigDecimal.ZERO.compareTo(this.getSafeAmount()) == 0){
			throw new IllegalArgumentException("扣款金额为空或0");
		}
		if(StringUtils.isBlank(this.getAccountName())){
			throw new IllegalArgumentException("账户名为空");
		}
		if(StringUtils.isBlank(this.getAccountNo())){
			throw new IllegalArgumentException("账户号为空");
		}
		//可选项校验
		if( !repaymentTypeList.contains(this.getSafeRepaymentType())){
			throw new IllegalArgumentException("还款类型期待是0或1，实际上是["+this.getRepaymentType()+"]");
		}
		if(StringUtils.isBlank(this.getUniqueId()) && StringUtils.isBlank(this.getContractNo())){
			throw new IllegalArgumentException("贷款合同唯一编号和贷款合同编号不能同时为空");
		}
		if(StringUtils.isNotBlank(this.getGateway()) && !gateWayList.contains(EnumUtil.fromOrdinal(PaymentInstitutionName.class, this.getSafeGateway()))){
			throw new IllegalArgumentException("网关不在期待的值范围中，实际上是["+this.getGateway()+"]");
		}
		
		if(CollectionUtils.isEmpty(this.repaymentDetails)){
			throw new IllegalArgumentException("还款计划明细为空，实际上是["+this.repaymentDetails+"]");
		}
	}
	
	public String getDeductId() {
		return deductId;
	}

	public void setDeductId(String deductId) {
		this.deductId = deductId;
	}

	public String getFinancialProductCode() {
		return financialProductCode;
	}

	public void setFinancialProductCode(String financialProductCode) {
		this.financialProductCode = financialProductCode;
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

	public Integer getTransType() {
		return transType;
	}
	public Integer getSafeTransType() {
		return this.transType == null ? -1 : this.transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public BigDecimal getSafeAmount() {
		
		return this.amount == null ? BigDecimal.ZERO : this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getRepaymentType() {
		return repaymentType;
	}
	public Integer getSafeRepaymentType() {
		
		return this.getRepaymentType() == null ? -1 : this.getRepaymentType();
	}

	public void setRepaymentType(Integer repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getGateway() {
		return gateway;
	}
	public Integer getSafeGateway(){
		
		Integer convertedValue = -1;
		
		try{
			convertedValue = Integer.parseInt(this.getGateway());
		}catch(Exception e){}
		
		return convertedValue;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}


	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public HashMap<String,String> toRequestParameters(){
		
		HashMap<String,String> hashMap = new HashMap<String,String>();
		
		hashMap.put("deductId", this.getDeductId());
		hashMap.put("financialProductCode", this.getFinancialProductCode());
		hashMap.put("uniqueId", this.getUniqueId());
		hashMap.put("contractNo", this.getContractNo());
		hashMap.put("transType", this.getSafeTransType()+"");
		hashMap.put("amount", this.getSafeAmount().toString());
		hashMap.put("repaymentType", this.getSafeTransType()+"");
		hashMap.put("mobile", this.getMobile());
		hashMap.put("accountName", this.getAccountName());
		hashMap.put("accountNo", this.getAccountNo());
		hashMap.put("gateway", this.getSafeGateway()+"");
		hashMap.put("repaymentDetails", JsonUtils.toJsonString(this.repaymentDetails));
		
		return hashMap;
	}

	public List<RepaymentDetail> getRepaymentDetails() {
		return repaymentDetails;
	}

	public void setRepaymentDetails(List<RepaymentDetail> repaymentDetails) {
		this.repaymentDetails = repaymentDetails;
	}

	public String getDeductApplicationUuid() {
		return deductApplicationUuid;
	}

	public void setDeductApplicationUuid(String deductApplicationUuid) {
		this.deductApplicationUuid = deductApplicationUuid;
	}

}
