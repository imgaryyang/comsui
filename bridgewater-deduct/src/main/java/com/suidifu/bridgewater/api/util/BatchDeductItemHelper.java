/**
 * 
 */
package com.suidifu.bridgewater.api.util;

import java.math.BigDecimal;
import java.util.Map;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductRequestLog;

/**
 * @author wukai
 *
 */
public class BatchDeductItemHelper {
	
	public static BatchDeductItem  buildBatchDeductItem(String source,int lineNumber) throws IllegalArgumentException{
		
		BatchDeductItem batchDeductItem = JsonUtils.parse(source,BatchDeductItem.class);
		
		if(null  == batchDeductItem){
			
			throw new IllegalArgumentException("明细解析json错误！明细内容["+source+"]");
		}
		batchDeductItem.setIndex(lineNumber);
		
		return batchDeductItem;
		
	}
	public static BatchDeductItem buildBatchDeductItem(Map<String,String> map){
		
		BatchDeductItem batchDeductItem = new BatchDeductItem();
		
		batchDeductItem.setDeductId(map.get("deductId"));
		batchDeductItem.setFinancialProductCode(map.get("financialProductCode"));
		batchDeductItem.setUniqueId(map.get("uniqueId"));
		batchDeductItem.setContractNo(map.get("contractNo"));
		batchDeductItem.setTransType((Integer.parseInt(map.get("transType"))));
		batchDeductItem.setAmount(new BigDecimal(map.get("amount")));
		batchDeductItem.setRepaymentType(Integer.parseInt(map.get("repaymentType")));
		batchDeductItem.setMobile(map.get("mobile"));
		batchDeductItem.setAccountName(map.get("accountName"));
		batchDeductItem.setAccountNo(map.get("accountNo"));
		batchDeductItem.setGateway(map.get("gateway"));
		
		return batchDeductItem;
	}
	public static BatchDeductItem buildBatchDeductItemByModel(DeductRequestModel deductRequestModel){
		
		BatchDeductItem batchDeductItem = new BatchDeductItem();
		
		batchDeductItem.setDeductId( deductRequestModel.getDeductId());
		batchDeductItem.setFinancialProductCode (deductRequestModel.getFinancialProductCode());
		batchDeductItem.setUniqueId (deductRequestModel.getUniqueId());
		batchDeductItem.setContractNo(deductRequestModel.getContractNo());
		batchDeductItem.setTransType(Integer.parseInt(deductRequestModel.getTransType()));
		batchDeductItem.setAmount(new BigDecimal(deductRequestModel.getDeductAmount()));
		batchDeductItem.setRepaymentType ((deductRequestModel.getRepaymentType()));
		batchDeductItem.setMobile(deductRequestModel.getMobile());
		batchDeductItem.setAccountNo(deductRequestModel.getDeductAccountNo());
		batchDeductItem.setAccountName(deductRequestModel.getAccountHolderName());
		batchDeductItem.setGateway(deductRequestModel.getGateway());
		batchDeductItem.setRepaymentDetails(JsonUtils.parseArray(deductRequestModel.getRepayDetailInfo(),RepaymentDetail.class));
		batchDeductItem.setDeductApplicationUuid(deductRequestModel.getDeductApplicationUuid());
		
		return batchDeductItem;
		
	}

	public static BatchDeductItem buildBatchDeductItemByModel(DeductRequestLog deductRequestLog, String deductApplicationUuid){

		BatchDeductItem batchDeductItem = new BatchDeductItem();

		batchDeductItem.setDeductId( deductRequestLog.getDeductId());
		batchDeductItem.setFinancialProductCode (deductRequestLog.getFinancialProductCode());
		batchDeductItem.setUniqueId (deductRequestLog.getUniqueId());
		batchDeductItem.setContractNo(deductRequestLog.getContractNo());
		batchDeductItem.setTransType(deductRequestLog.getTransType());
		batchDeductItem.setAmount(deductRequestLog.getDeductAmount());
		batchDeductItem.setRepaymentType ((deductRequestLog.getRepaymentType()).ordinal());
		batchDeductItem.setMobile(deductRequestLog.getMobile());
		batchDeductItem.setAccountNo(deductRequestLog.getDeductAccountNo());
		batchDeductItem.setAccountName(deductRequestLog.getAccountHolderName());
		batchDeductItem.setGateway(deductRequestLog.getGateway());
		batchDeductItem.setRepaymentDetails(JsonUtils.parseArray(deductRequestLog.getRepayDetailInfo(),RepaymentDetail.class));
		batchDeductItem.setDeductApplicationUuid(deductApplicationUuid);

		return batchDeductItem;

	}
}
