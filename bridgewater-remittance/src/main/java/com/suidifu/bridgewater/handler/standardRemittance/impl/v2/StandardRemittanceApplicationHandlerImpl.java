package com.suidifu.bridgewater.handler.standardRemittance.impl.v2;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.remittance.v2.AbstractRemittanceApplicationHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;

@Component("standardRemittanceApplicationHandler")
public class StandardRemittanceApplicationHandlerImpl extends AbstractRemittanceApplicationHandler {

	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;

	@Autowired
	private FinancialContractService financialContractService;

	@Override
	public List<RemittanceApplication> getRemittanceApplicationsBy(String contractUniqueId, String contractNo) {
		if(!StringUtils.isEmpty(contractUniqueId)) {
			return iRemittanceApplicationService.getRemittanceApplicationsByUniqueId(contractUniqueId);
		}

		if(!StringUtils.isEmpty(contractNo)) {
			return iRemittanceApplicationService.getRemittanceApplicationsByContractNo(contractNo);
		}
		return Collections.emptyList();
	}

	@Override
	public FinancialContract checkRemittanceInfoByLogic(RemittanceCommandModel commandModel, String ip, String operator) {
		//放款明细内，银行编号校验
//		validateRemittanceDetailsBankCode(commandModel);

		String requestNo = commandModel.getRequestNo();
		//请求编号重复性校验
		if(iRemittanceApplicationService.existsRequestNo(requestNo)) {
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}

		String remittanceId = commandModel.getRemittanceId();
		//订单编号重复性校验
		if(iRemittanceApplicationService.existsRemittanceId(remittanceId)) {
			throw new ApiException(ApiResponseCode.REPEAT_REMITTANCE_ID);
		}

		String contractUniqueId = commandModel.getUniqueId();

		//该贷款合同下存在处理中或已成功的放款请求
		if(iRemittanceApplicationService.existsProcessingOrSuccessedRemittanceApplication(contractUniqueId)) {
			throw new ApiException(ApiResponseCode.HAS_EXIST_PROCESSING_OR_SUCCESSED_REMITTANCE);
		}

//		Contract contract = contractService.getContractByUniqueId(contractUniqueId);
//		if(contract == null){
//			throw new ApiException(ApiResponseCode.CONTRACT_UNIQUE_ID_ERROR);
//		}
//		if(!StringUtils.equals(commandModel.getContractNo(), contract.getContractNo())){
//			throw new ApiException(ApiResponseCode.CONTRACT_UNIQUE_ID_NO_NOT_MATCH);
//		}
		String productCode = commandModel.getProductCode();
		//信托产品代码存在性校验
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productCode);
		if(financialContract == null) {
			throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
		}
		return financialContract;
	}

}