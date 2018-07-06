package com.suidifu.bridgewater.handler.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.api.model.RemittanceBlackListCommandModel;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceBlackListHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceBlackList;
import com.zufangbao.sun.yunxin.service.remittance.RemittanceBlackListService;

@Component("remittanceBlackListHandler")
public class RemittanceBlackListHandlerImpl implements RemittanceBlackListHandler{
	
	@Autowired
	private RemittanceBlackListService remittanceBlackListService;
	
	@Autowired
	private IRemittanceApplicationHandler remittanceApplicationHandler;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Override
	public void recordRemittanceBlackList(RemittanceBlackListCommandModel model, String ip, String creatorName) {
		String requestNo = model.getRequestNo();
		if(remittanceBlackListService.existRequestNo(requestNo)) {
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}
		if(remittanceBlackListService.existUniqueId(model.getUniqueId())) {
			return;
		}
		String productCode = model.getProductCode();
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productCode);
		validate_before_record_remittance_black_list(model, financialContract);
		
		RemittanceBlackList remittanceBlackList = new RemittanceBlackList(model.getRequestNo(), model.getUniqueId(),
				model.getContractNo(), financialContract.getFinancialContractUuid(), financialContract.getId(),
				productCode, ip, creatorName);
		remittanceBlackListService.save(remittanceBlackList);
	}
	
	/**
	 * 记录放款黑名单前进行校验
	 */
	private void validate_before_record_remittance_black_list(RemittanceBlackListCommandModel model, FinancialContract financialContract) {
		if(financialContract == null) {
			throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
		}
		
		List<RemittanceApplication> remittanceApplications = remittanceApplicationHandler.getRemittanceApplicationsBy(model.getUniqueId(), model.getContractNo());
		if(!CollectionUtils.isEmpty(remittanceApplications)) {
			for (RemittanceApplication application : remittanceApplications) {
				if(!StringUtils.equals(application.getFinancialProductCode(),model.getProductCode())) {
					throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
				}
				ExecutionStatus status = application.getExecutionStatus();
				if(status == ExecutionStatus.SUCCESS) {
					throw new ApiException(ApiResponseCode.CONTRACT_REMITTANCE_SUCCESS);
				}
				if(status != ExecutionStatus.FAIL) {
					throw new ApiException(ApiResponseCode.CONTRACT_REMITTANCE_PROCESSING);
				}
			}
		}
		
		
	}

}
