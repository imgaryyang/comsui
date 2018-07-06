package com.zufangbao.earth.yunxin.api.handler.impl;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.handler.RepurchaseCommandHandler;
import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.earth.yunxin.api.model.logs.RepurchaseCommandLog;
import com.zufangbao.earth.yunxin.api.service.RepurchaseCommandLogService;
import com.zufangbao.earth.yunxin.handler.RepurchaseHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountEnvVar;
import com.zufangbao.sun.entity.repurchase.RepurchaseApiResponse;
import com.zufangbao.sun.entity.repurchase.RepurchaseDetails;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.handler.ContractHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("RepurchaseCommandHandler")
public class RepurchaseCommandHandlerImpl implements RepurchaseCommandHandler {

	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private RepurchaseService repurchaseService;
	@Autowired
	private RepurchaseCommandLogService repurchaseCommandLogService;
	@Autowired
	private ContractApiHandler contractApiHandler;
	@Autowired
	private ContractHandler contractHandler;
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	@Autowired
	private RepurchaseHandler repurchaseHandler;

	private static final String CONTRACT_IDENTIFIER = "contractIdentifier";

	private static final String REPURCHASE_NO = "repurchaseNo";


	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public RepurchaseApiResponse batchRepurchaseCommand(RepurchaseCommandModel model, String ip){
		repurchaseCommandLogService.checkByRequestNo(model.getRequestNo());
		repurchaseCommandLogService.checkByBatchNo(model.getBatchNo(), model.getTransactionType());
		RepurchaseApiResponse apiResponse = batchRepurchaseCommand(model);
		repurchaseCommandLogService.saveRepurchaseCommandLog(model, ip);
		return apiResponse;
	}


	private RepurchaseApiResponse batchRepurchaseCommand(RepurchaseCommandModel model) {
		RepurchaseApiResponse apiResponse = new RepurchaseApiResponse();
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
		if(financialContract == null) {
			return apiResponse.fail(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
		}
		if(!financialContract.canBeRepurchasedViaManual()){
			return apiResponse.fail(ApiResponseCode.NOT_ALLOW_REPURCHASE);
		}
		String batchNo = StringUtils.isEmpty(model.getBatchNo()) ? GeneratorUtils.generateInterBatchNo() : model.getBatchNo();
		List<RepurchaseDetails> details = model.getRepurchaseDetailModel();
		for (RepurchaseDetails detail : details) {
			Contract contract = contractApiHandler.getContractBy(detail.getUniqueId(), detail.getContractNo());
			String contractIdentifier = StringUtils.isNotBlank(detail.getUniqueId()) ? detail.getUniqueId() : detail.getContractNo();
			if (contract == null) {
				apiResponse.data(buildFailResult(ApiResponseCode.CONTRACT_NOT_EXIST, contractIdentifier));
				continue;
			}
			if(!financialContract.getFinancialContractUuid().equals(contract.getFinancialContractUuid())){
				apiResponse.data(buildFailResult(ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT, contractIdentifier));
				continue;
			}
			if(contract.getState() != ContractState.EFFECTIVE){
				if (contract.getState() == null
					|| Arrays.asList(ContractState.COMPLETION,ContractState.PRE_PROCESS, ContractState.INEFFECTIVE, ContractState.INVALIDATE).contains(contract.getState())) {
					apiResponse.data(buildFailResult(ApiResponseCode.CONTRACT_NOT_ALLOW_REPURCHASE, contractIdentifier));
				}else {
					String repurchaseDocUuid = repurchaseService.getRepurchaseDocUuidBy(contract.getId());
					apiResponse.data(buildFailResult(ApiResponseCode.REPURCHASED_ALREADY, contractIdentifier).data(REPURCHASE_NO, repurchaseDocUuid));
				}
				continue;
			}
			List<String> assetSetUuids = repaymentPlanService.getExecutingAssetSetUuids(contract.getId());
			if (CollectionUtils.isNotEmpty(assetSetUuids)) {
				apiResponse.data(buildFailResult(ApiResponseCode.EXIST_EXECUTING_ASSET, contractIdentifier));
				continue;
			}
			List<AssetSet> assetSets = contractHandler.getNeedRepurchasedAssetSetList(contract.getId());
			if(CollectionUtils.isEmpty(assetSets)){
				apiResponse.data(buildFailResult(ApiResponseCode.NO_ASSETSET_CAN_BE_REPURCHASED, contractIdentifier));
				continue;
			}
			boolean existRepaymentPlanInPayment = repaymentPlanHandler.existRepaymentPlanInPayment(assetSets);
			if(existRepaymentPlanInPayment){
				apiResponse.data(buildFailResult(ApiResponseCode.REPAYMENT_PLAN_IN_PAYING, contractIdentifier));
				continue;
			}
			RepurchaseAmountEnvVar envVar = contractHandler.parseRepurchaseEnvironmentVariables(assetSets);
			if(!AmountUtils.equals(envVar.getOutstandingPrincipal(), detail.getPrincipal())){
				apiResponse.data(buildFailResult(ApiResponseCode.ERROR_REPURCHASE_PRINCIPAL, contractIdentifier));
				continue;
			}
			RepurchaseAmountDetail repurchaseAmountDetail = new RepurchaseAmountDetail(detail);
			RepurchaseDoc repurchaseDoc = repurchaseHandler.conductRepurchaseViaInterface(contract, financialContract, repurchaseAmountDetail,batchNo);
			if(repurchaseDoc == null){
				apiResponse.data(buildFailResult(ApiResponseCode.REPURCHASE_FAILED, contractIdentifier));
				continue;
			}
			apiResponse.data(buildSuccessResult(contractIdentifier, repurchaseDoc.getRepurchaseDocUuid()));
		}
		return apiResponse;
	}

	private Result buildFailResult(int apiExceptionCode,  String contractIdentifier) {
		return new Result().initialize(apiExceptionCode + "", ApiMessageUtil.getMessage(apiExceptionCode))
			.data(CONTRACT_IDENTIFIER, contractIdentifier);
	}

	private Result buildSuccessResult(String contractIdentifier, String repurchaseDocUuid) {
		Result aResult = new Result().success();
		if(StringUtils.isNotEmpty(contractIdentifier)){
			aResult.data(CONTRACT_IDENTIFIER, contractIdentifier);
		}
		if(StringUtils.isNotEmpty(repurchaseDocUuid)){
			aResult.data(REPURCHASE_NO, repurchaseDocUuid);
		}
		return aResult;
	}

	@Override
	public RepurchaseApiResponse undoRepurchaseCommand(RepurchaseCommandModel model, String ip) {
		repurchaseCommandLogService.checkByRequestNo(model.getRequestNo());
		if(StringUtils.isNotBlank(model.getBatchNo())){
			repurchaseCommandLogService.checkByBatchNo(model.getBatchNo(), model.getTransactionType());
		}
		RepurchaseApiResponse apiResponse = undoRepurchase(model,ip);
		repurchaseCommandLogService.saveRepurchaseCommandLog(model, ip);
		return apiResponse;
	}

	private RepurchaseApiResponse undoRepurchase(RepurchaseCommandModel model, String ip) {

		RepurchaseApiResponse apiResponse = new RepurchaseApiResponse();

		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
		if(financialContract == null) {
			return apiResponse.fail(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
		}

		String batchNo = model.getBatchNo();
		String repurchaseDetail = model.getRepurchaseDetail();
		if(StringUtils.isNotBlank(batchNo)){
			RepurchaseCommandLog repurchaseCommandLog = repurchaseCommandLogService.getRepurchaseCommandLogByBatchNo(batchNo);
			if(repurchaseCommandLog == null){
				return apiResponse.fail(ApiResponseCode.INVALID_PARAMS+"","批次号［batchNo］不存在！");
			}
			repurchaseDetail = repurchaseCommandLog.getRepurchaseDetail();
			List<RepurchaseDetails> details = JsonUtils.parseArray(repurchaseDetail, RepurchaseDetails.class);
			if(details==null){
				return apiResponse.fail(ApiResponseCode.INVALID_PARAMS+"","明细格式不正确");
			}
			for (RepurchaseDetails detail : details) {
				String contractIdentifier = StringUtils.isNotBlank(detail.getUniqueId()) ? detail.getUniqueId() : detail.getContractNo();
				try{
					nullifyRepurchaseDoc(detail.getUniqueId(), detail.getContractNo(), financialContract,ip);
					apiResponse.data(buildSuccessResult(contractIdentifier, null));
				}catch (Exception e){
					if(e instanceof ApiException){
						apiResponse.data(buildFailResult(((ApiException) e).getCode(),  contractIdentifier));
					}else{
						apiResponse.data(buildFailResult(ApiResponseCode.INVALID_PARAMS, contractIdentifier));
					}
					e.printStackTrace();
				}
			}
		}else{
			List<RepurchaseDetails> details = JsonUtils.parseArray(repurchaseDetail, RepurchaseDetails.class);
			if (details == null || details.size() != 1) {
				return apiResponse.fail(ApiResponseCode.INVALID_PARAMS+"","回购详情［repurchaseDetail］格式错误！");
			}
			RepurchaseDetails details1 = details.get(0);
			String contractIdentifier = StringUtils.isNotBlank(details1.getUniqueId()) ? details1.getUniqueId() : details1.getContractNo();
			try {
				nullifyRepurchaseDoc(details1.getUniqueId(), details1.getContractNo(), financialContract, ip);
				apiResponse.data(buildSuccessResult(contractIdentifier, null));
			}catch (Exception e){
				if(e instanceof ApiException){
					apiResponse.data(buildFailResult(((ApiException) e).getCode(),  contractIdentifier));
				}else{
					apiResponse.data(buildFailResult(ApiResponseCode.SYSTEM_ERROR, contractIdentifier));
				}
				e.printStackTrace();
			}
		}
		return apiResponse;
	}

	private void nullifyRepurchaseDoc(String uniqueId, String contractNo, FinancialContract financialContract,String ip) throws Exception{
		Contract contract = contractApiHandler.getContractBy(uniqueId, contractNo);
		if(!financialContract.getFinancialContractUuid().equals(contract.getFinancialContractUuid())){
			throw new ApiException(ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT);
		}

		RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocBy(contract.getId());
	    if (repurchaseDoc == null) {
	    	throw new ApiException(ApiResponseCode.NOT_EXIST_REPURCHASE_DOC);
        }
        if (repurchaseDoc.getRepurchaseStatus() != RepurchaseStatus.REPURCHASING) {
        	throw new ApiException(ApiResponseCode.NOT_ALLOW_INVALID);
        }
        repurchaseHandler.nullifyRepurchaseDoc(repurchaseDoc, contract, financialContract, -1L, ip);
	}
}
