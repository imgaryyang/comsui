/**
 * 
 */
package com.suidifu.bridgewater.fast.deductplan;

import com.suidifu.bridgewater.fast.FastDeductApplicationEnum;
import com.suidifu.giotto.handler.DataTemperature;
import com.suidifu.giotto.handler.FastCacheObjectWithTemperature;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author wukai
 *
 */
public class FastDeductPlan extends FastCacheObjectWithTemperature{

		private Long id;

		/**
		 * 扣款计划uuid
		 */
		private String deductPlanUuid;
		
		/**
		 * 扣款申请uuid
		 */
		private String deductApplicationUuid;
		
		/**
		 *扣款申请明细uuid
		 */
		private String deductApplicationDetailUuid;

		/**
		 * 信托合约Id
		 */
		private String  financialContractUuid;
		
		private String  contractUniqueId;
		
		private String  contractNo;
		/**
		 * 支付网关0:广州银联，1:超级网银，2:银企直连
		 */
		private Integer paymentGateway;

		/**
		 * 支付通道uuid
		 */
		private String paymentChannelUuid;
		
		/**
		 * 支付网关账户（或商户号）
		 */
		private String pgAccount;
		
		/**
		 * 清算号
		 */
		private String pgClearingAccount;

		/**
		 * 交易类型0:贷，1:借
		 */
		private Integer transactionType;
		/**
		 * 交易备注
		 */
		private String transactionRemark;
		/**
		 * 恒生银行编码
		 */
		private String cpBankCode;
		/**
		 * 交易对手方银行卡号
		 */
		private String cpBankCardNo;
		/**
		 * 交易对手方银行卡持有人
		 */
		private String cpBankAccountHolder;

		/**
		 * 交易对手方证件类型
		 */
		private Integer cpIdType;

		/**
		 * 交易对手方证件号
		 */
		private String cpIdNumber;

		/**
		 * 交易对手方开户行所在省
		 */
		private String cpBankProvince;

		/**
		 * 交易对手方开户行所在市
		 */
		private String cpBankCity;
		
		/**
		 * 交易对手方开户行名称
		 */
		private String cpBankName;

		/**
		 * 计划支付时间
		 */
		private Date plannedPaymentDate;
		
		/**
		 * 交易发起时间
		 */
		private Date transactionTime;
		/**
		 * 实际支付完成时间
		 */
		private Date completePaymentDate;

		/**
		 * 计划交易总金额
		 */
		private BigDecimal plannedTotalAmount;
		
		/**
		 * 实际交易总金额
		 */
		private BigDecimal actualTotalAmount;
		
		/**
		 * 执行前置条件
		 */
		private String executionPrecond;

		
		 private String executionRemark;
		/**
		 * 执行状态, 0:已创建、1:处理中、2:成功、3:失败、4:异常、5:撤销
		 */
		private Integer executionStatus;

		private Date createTime;

		private String creatorName;

		private Date lastModifiedTime;
		
		private Date completeTime;
		
		private String mobile;
		
		private String tradeUuid;
		
		 //还款类型
	    private Integer repaymentType;

	    private Integer sourceType;

		//扣款类型 0：多通道、1：拆单
		private Integer  deductMode;
	   
		/**
		 * 交易流水号
		 */
		private String transactionSerialNo;
		
		private Integer  transactionRecipient;

		/**
		 * 清算信息
		 */
		private Integer clearingStatus;//0 未清算 2已清算
		
		private String clearingCashFlowUuid;
		
		private Date clearingTime;
		

		//新增字段for批扣
		private String batchDeductApplicationUuid;
		private String batchDeductId;
		private Integer noneBusinessCheckStatus;
		private Integer businessCheckStatus;
		
		private Integer notifyStatus;
		
		private boolean retriable;
		
		private Integer retryTimes;
		
		private String paymentOrderUuid;
		
		private String version;
		
		// 新增的提供给jpmorgan使用的slotUuid
//		使用雪花算法生成
		private String tradeScheduleSlotUuid;
		
		// 提供给jpmrogan的outlierTransactionUuid
		//拆单这里生成方式是Md5(applicationUuid+deductPlanUuid)
		//多通道这里生成方式是Md5(applicationUuid+deductPlanUuid1+deductPlanUuid2)
		private String transactionUuid;
		
		public FastDeductPlan() {
			super();
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getDeductPlanUuid() {
			return deductPlanUuid;
		}

		public void setDeductPlanUuid(String deductPlanUuid) {
			this.deductPlanUuid = deductPlanUuid;
		}

		public String getDeductApplicationUuid() {
			return deductApplicationUuid;
		}

		public void setDeductApplicationUuid(String deductApplicationUuid) {
			this.deductApplicationUuid = deductApplicationUuid;
		}

		public String getDeductApplicationDetailUuid() {
			return deductApplicationDetailUuid;
		}

		public void setDeductApplicationDetailUuid(String deductApplicationDetailUuid) {
			this.deductApplicationDetailUuid = deductApplicationDetailUuid;
		}

		public String getFinancialContractUuid() {
			return financialContractUuid;
		}

		public void setFinancialContractUuid(String financialContractUuid) {
			this.financialContractUuid = financialContractUuid;
		}

		public String getContractUniqueId() {
			return contractUniqueId;
		}

		public void setContractUniqueId(String contractUniqueId) {
			this.contractUniqueId = contractUniqueId;
		}

		public String getContractNo() {
			return contractNo;
		}

		public void setContractNo(String contractNo) {
			this.contractNo = contractNo;
		}

		public Integer getPaymentGateway() {
			return paymentGateway;
		}

		public void setPaymentGateway(Integer paymentGateway) {
			this.paymentGateway = paymentGateway;
		}

		public String getPaymentChannelUuid() {
			return paymentChannelUuid;
		}

		public void setPaymentChannelUuid(String paymentChannelUuid) {
			this.paymentChannelUuid = paymentChannelUuid;
		}

		public String getPgAccount() {
			return pgAccount;
		}

		public void setPgAccount(String pgAccount) {
			this.pgAccount = pgAccount;
		}

		public String getPgClearingAccount() {
			return pgClearingAccount;
		}

		public void setPgClearingAccount(String pgClearingAccount) {
			this.pgClearingAccount = pgClearingAccount;
		}

		public Integer getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(Integer transactionType) {
			this.transactionType = transactionType;
		}

		public String getTransactionRemark() {
			return transactionRemark;
		}

		public void setTransactionRemark(String transactionRemark) {
			this.transactionRemark = transactionRemark;
		}

		public String getCpBankCode() {
			return cpBankCode;
		}

		public void setCpBankCode(String cpBankCode) {
			this.cpBankCode = cpBankCode;
		}

		public String getCpBankCardNo() {
			return cpBankCardNo;
		}

		public void setCpBankCardNo(String cpBankCardNo) {
			this.cpBankCardNo = cpBankCardNo;
		}

		public String getCpBankAccountHolder() {
			return cpBankAccountHolder;
		}

		public void setCpBankAccountHolder(String cpBankAccountHolder) {
			this.cpBankAccountHolder = cpBankAccountHolder;
		}

		public Integer getCpIdType() {
			return cpIdType;
		}

		public void setCpIdType(Integer cpIdType) {
			this.cpIdType = cpIdType;
		}

		public String getCpIdNumber() {
			return cpIdNumber;
		}

		public void setCpIdNumber(String cpIdNumber) {
			this.cpIdNumber = cpIdNumber;
		}

		public String getCpBankProvince() {
			return cpBankProvince;
		}

		public void setCpBankProvince(String cpBankProvince) {
			this.cpBankProvince = cpBankProvince;
		}

		public String getCpBankCity() {
			return cpBankCity;
		}

		public void setCpBankCity(String cpBankCity) {
			this.cpBankCity = cpBankCity;
		}

		public String getCpBankName() {
			return cpBankName;
		}

		public void setCpBankName(String cpBankName) {
			this.cpBankName = cpBankName;
		}

		public Date getPlannedPaymentDate() {
			return plannedPaymentDate;
		}

		public void setPlannedPaymentDate(Date plannedPaymentDate) {
			this.plannedPaymentDate = plannedPaymentDate;
		}

		public Date getTransactionTime() {
			return transactionTime;
		}

		public void setTransactionTime(Date transactionTime) {
			this.transactionTime = transactionTime;
		}

		public Date getCompletePaymentDate() {
			return completePaymentDate;
		}

		public void setCompletePaymentDate(Date completePaymentDate) {
			this.completePaymentDate = completePaymentDate;
		}

		public BigDecimal getPlannedTotalAmount() {
			return plannedTotalAmount;
		}

		public void setPlannedTotalAmount(BigDecimal plannedTotalAmount) {
			this.plannedTotalAmount = plannedTotalAmount;
		}

		public BigDecimal getActualTotalAmount() {
			return actualTotalAmount;
		}

		public void setActualTotalAmount(BigDecimal actualTotalAmount) {
			this.actualTotalAmount = actualTotalAmount;
		}

		public String getExecutionPrecond() {
			return executionPrecond;
		}

		public void setExecutionPrecond(String executionPrecond) {
			this.executionPrecond = executionPrecond;
		}

		public String getExecutionRemark() {
			return executionRemark;
		}

		public void setExecutionRemark(String executionRemark) {
			this.executionRemark = executionRemark;
		}

		public Integer getExecutionStatus() {
			return executionStatus;
		}

		public void setExecutionStatus(Integer executionStatus) {
			this.executionStatus = executionStatus;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getCreatorName() {
			return creatorName;
		}

		public void setCreatorName(String creatorName) {
			this.creatorName = creatorName;
		}

		public Date getLastModifiedTime() {
			return lastModifiedTime;
		}

		public void setLastModifiedTime(Date lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
		}

		public Date getCompleteTime() {
			return completeTime;
		}

		public void setCompleteTime(Date completeTime) {
			this.completeTime = completeTime;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getTradeUuid() {
			return tradeUuid;
		}

		public void setTradeUuid(String tradeUuid) {
			this.tradeUuid = tradeUuid;
		}

		public Integer getRepaymentType() {
			return repaymentType;
		}

		public void setRepaymentType(Integer repaymentType) {
			this.repaymentType = repaymentType;
		}

		public Integer getSourceType() {
			return sourceType;
		}

		public void setSourceType(Integer sourceType) {
			this.sourceType = sourceType;
		}

		public Integer getDeductMode() {
			return deductMode;
		}

		public void setDeductMode(Integer deductMode) {
			this.deductMode = deductMode;
		}

		public String getTransactionSerialNo() {
			return transactionSerialNo;
		}

		public void setTransactionSerialNo(String transactionSerialNo) {
			this.transactionSerialNo = transactionSerialNo;
		}

		public Integer getTransactionRecipient() {
			return transactionRecipient;
		}

		public void setTransactionRecipient(Integer transactionRecipient) {
			this.transactionRecipient = transactionRecipient;
		}

		public Integer getClearingStatus() {
			return clearingStatus;
		}

		public void setClearingStatus(Integer clearingStatus) {
			this.clearingStatus = clearingStatus;
		}

		public String getClearingCashFlowUuid() {
			return clearingCashFlowUuid;
		}

		public void setClearingCashFlowUuid(String clearingCashFlowUuid) {
			this.clearingCashFlowUuid = clearingCashFlowUuid;
		}

		public Date getClearingTime() {
			return clearingTime;
		}

		public void setClearingTime(Date clearingTime) {
			this.clearingTime = clearingTime;
		}

		public String getBatchDeductApplicationUuid() {
			return batchDeductApplicationUuid;
		}

		public void setBatchDeductApplicationUuid(String batchDeductApplicationUuid) {
			this.batchDeductApplicationUuid = batchDeductApplicationUuid;
		}

		public String getBatchDeductId() {
			return batchDeductId;
		}

		public void setBatchDeductId(String batchDeductId) {
			this.batchDeductId = batchDeductId;
		}

		public Integer getNoneBusinessCheckStatus() {
			return noneBusinessCheckStatus;
		}

		public void setNoneBusinessCheckStatus(Integer noneBusinessCheckStatus) {
			this.noneBusinessCheckStatus = noneBusinessCheckStatus;
		}

		public Integer getBusinessCheckStatus() {
			return businessCheckStatus;
		}

		public void setBusinessCheckStatus(Integer businessCheckStatus) {
			this.businessCheckStatus = businessCheckStatus;
		}

		public Integer getNotifyStatus() {
			return notifyStatus;
		}

		public void setNotifyStatus(Integer notifyStatus) {
			this.notifyStatus = notifyStatus;
		}

		public boolean isRetriable() {
			return retriable;
		}

		public void setRetriable(boolean retriable) {
			this.retriable = retriable;
		}

		public Integer getRetryTimes() {
			return retryTimes;
		}

		public void setRetryTimes(Integer retryTimes) {
			this.retryTimes = retryTimes;
		}

		public String getPaymentOrderUuid() {
			return paymentOrderUuid;
		}

		public void setPaymentOrderUuid(String paymentOrderUuid) {
			this.paymentOrderUuid = paymentOrderUuid;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getTradeScheduleSlotUuid() {
			return tradeScheduleSlotUuid;
		}

		public void setTradeScheduleSlotUuid(String tradeScheduleSlotUuid) {
			this.tradeScheduleSlotUuid = tradeScheduleSlotUuid;
		}

		public String getTransactionUuid() {
			return transactionUuid;
		}

		public void setTransactionUuid(String transactionUuid) {
			this.transactionUuid = transactionUuid;
		}

		@Override
		public DataTemperature temperature() {
			
			ExecutionStatus executionStatusEnum = EnumUtil.fromCode(ExecutionStatus.class, this.getExecutionStatus());
			
			if(ExecutionStatus.ABANDON == executionStatusEnum || ExecutionStatus.SUCCESS == executionStatusEnum || ExecutionStatus.FAIL == executionStatusEnum)
			{
				return DataTemperature.FROZEN;
			}
			return DataTemperature.HOT;
		}

		@Override
		public SqlAndParamTuple updateSqlAndParamTupleWithVersionLock(String oldVersion) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SqlAndParamTuple checkAfterUpdateSqlAndParamTuple() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SqlAndParamTuple updateSqlAndParamTuple() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String obtainAddCacheKey() {
			// TODO Auto-generated method stub
			
		    if (StringUtils.isEmpty(getDeductPlanUuid())){

	            throw new RuntimeException("all keys value is null.");
	        }
	        String result = FastDeductPlanEnum.PREFIX_KEY;
	        if (StringUtils.isNotEmpty(getDeductPlanUuid())){
	            result = result.concat(getDeductPlanUuid()).concat(":");
	        }else {
	            result = result.concat(":");
	        }
			return result;
		}

		@Override
		public SqlAndParamTuple obtainInsertSqlAndParam() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String obtainQueryCheckMD5Sql(String updateSql) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FastKey getColumnName() {
			// TODO Auto-generated method stub
			return FastDeductPlanEnum.DEDUCT_PLAN_UUID;
		}

		@Override
		public String getColumnValue() {
			// TODO Auto-generated method stub
			return getDeductPlanUuid();
		}

		@Override
		public List<String> obtainAddCacheKeyList() {
		
			 return new ArrayList<String>() {
	            private static final long serialVersionUID = -2637200652379763578L;

	            {
	                if (StringUtils.isNotBlank(getDeductPlanUuid())) {
	                    add(FastDeductPlanEnum.PREFIX_KEY.concat(getDeductPlanUuid()));
	                }
	               
	            }};
		}
		
		
}
