package com.suidifu.bridgewater.api.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.DeductPlanApplicationCheckLog;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.ProcessDeductResultFailType;
import com.suidifu.bridgewater.api.entity.deduct.single.v2.BusinessCheckStatus;
import com.suidifu.bridgewater.api.entity.deduct.single.v2.NoneBusinessCheckStatus;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;

/**
 * @author wukai
 *
 */
public class DeductPlanApplicationCheckLogHelper {

	public static  DeductPlanApplicationCheckLog buildErrorDeductPlanApplicationCheckLog(BatchDeductApplication batchDeductApplication,BatchDeductItem batchDeductItem,String failReason,ProcessDeductResultFailType processDeductResultFailType){
		
		DeductPlanApplicationCheckLog deductPlanApplicationCheckLog = new DeductPlanApplicationCheckLog();
		
		deductPlanApplicationCheckLog.setDeductId(batchDeductItem.getDeductId());
		deductPlanApplicationCheckLog.setDeductApplicationUuid(batchDeductItem.getDeductApplicationUuid());
		
		deductPlanApplicationCheckLog.setRequestNo(batchDeductApplication.getRequestNo());
		deductPlanApplicationCheckLog.setFinancialContractUuid(batchDeductApplication.getFinancialContractUuid());
		deductPlanApplicationCheckLog.setFinancialProductCode(batchDeductItem.getFinancialProductCode());
		deductPlanApplicationCheckLog.setContractUniqueId(batchDeductItem.getUniqueId());
		
		deductPlanApplicationCheckLog.setRepaymentPlanCodeList(StringUtils.EMPTY);
		deductPlanApplicationCheckLog.setContractNo(batchDeductItem.getContractNo());
		deductPlanApplicationCheckLog.setPlannedDeductTotalAmount(batchDeductItem.getSafeAmount());
		deductPlanApplicationCheckLog.setActualDeductTotalAmount(BigDecimal.ZERO);
		deductPlanApplicationCheckLog.setNotifyUrl(batchDeductApplication.getNotifyUrl());
		deductPlanApplicationCheckLog.setTranscationType(batchDeductItem.getSafeTransType());
		deductPlanApplicationCheckLog.setRepaymentType(batchDeductItem.getSafeRepaymentType());
		deductPlanApplicationCheckLog.setExecutionStatus(ExecutionStatus.FAIL.ordinal());
		deductPlanApplicationCheckLog.setExecutionRemark("");
		deductPlanApplicationCheckLog.setCreateTime(new Date());
		deductPlanApplicationCheckLog.setIp(batchDeductApplication.getIp());
		deductPlanApplicationCheckLog.setLastModifiedTime(new Date());
		deductPlanApplicationCheckLog.setRecordStatus(-1);
		deductPlanApplicationCheckLog.setIsAvailable(-1);
		deductPlanApplicationCheckLog.setApiCalledTime(new Date());
		deductPlanApplicationCheckLog.setTransactionRecipient(TransactionRecipient.OPPOSITE.ordinal());
		deductPlanApplicationCheckLog.setCustomerName(StringUtils.EMPTY);
		deductPlanApplicationCheckLog.setMobile(batchDeductItem.getMobile());
		deductPlanApplicationCheckLog.setGateway(""+batchDeductItem.getSafeGateway());
		deductPlanApplicationCheckLog.setSourceType(SourceType.INTERFACEONLINEDEDUCT.ordinal());
		deductPlanApplicationCheckLog.setThirdPartVoucherStatus(-1);
		deductPlanApplicationCheckLog.setCompleteTime(null);
		deductPlanApplicationCheckLog.setTransactionTime(null);
		deductPlanApplicationCheckLog.setActualNotifyNumber(0);
		deductPlanApplicationCheckLog.setPlanNotifyNumber(0);
		deductPlanApplicationCheckLog.setBatchDeductId(batchDeductApplication.getBatchDeductId());
		deductPlanApplicationCheckLog.setNoneBusinessCheck(NoneBusinessCheckStatus.Done.ordinal());
		deductPlanApplicationCheckLog.setBusinessCheck(BusinessCheckStatus.Done.ordinal());
		deductPlanApplicationCheckLog.setFailReason(failReason);
		deductPlanApplicationCheckLog.setProcessDeductResultFailType(processDeductResultFailType);;
		deductPlanApplicationCheckLog.setBatchFilePath(batchDeductApplication.getFilePath());
		deductPlanApplicationCheckLog.setCurrentLineNumber(batchDeductItem.getIndex());
		deductPlanApplicationCheckLog.setAcccountNo(batchDeductItem.getAccountNo());
		deductPlanApplicationCheckLog.setAccountName(batchDeductItem.getAccountName());
		deductPlanApplicationCheckLog.setBatchDeductApplicationUuid(batchDeductApplication.getBatchDeductApplicationUuid());
		
		
		return deductPlanApplicationCheckLog;
	
	}
}
