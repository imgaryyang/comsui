package com.zufangbao.earth.yunxin.api.controller;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.earth.yunxin.api.handler.RepaymentInformationApiHandler;
import com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2.DeductBusinessNotifyJobServer;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.EARTH_ASSETPACKAGE_FUNCTION_POINT;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.CardBinUtil;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageRequestModel;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeRequestModel;
import com.zufangbao.sun.api.model.modify.RepaymentPlanModifyModel;
import com.zufangbao.sun.api.model.repayment.ImportAssetPackageResponseModel;
import com.zufangbao.sun.api.model.repayment.RepaymentPlanModifyDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.modify.RepaymentInformationModifyModel;
import com.zufangbao.sun.ledgerbook.InvalidWriteOffException;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.PrepaymentModifyModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategyMode;
import com.zufangbao.sun.yunxin.exception.DuplicatedDeductionException;
import com.zufangbao.sun.yunxin.service.CardBinService;
import com.zufangbao.sun.yunxin.service.repayment.UpdateRepaymentInformationLogService;
import com.zufangbao.wellsfargo.api.util.ApiConstant;
import com.zufangbao.wellsfargo.yunxin.handler.*;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.yunxin.api.constant.ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS;
import static com.zufangbao.earth.yunxin.api.constant.ModifyOpsFunctionCodes.*;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.CONTRACT_NOT_EXIST;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.EXSIT_PROCESSING_OR_SUCCESS_REPAYMENT_PLAN;

/**
 * @author louguanyang
 */
@Controller
@RequestMapping("/api/modify")
public class ModifyApiController extends BaseApiController {

	private static final Log logger = LogFactory.getLog(ModifyApiController.class);

	@Resource
	private ContractApiHandler contractApiHandler;

	@Resource
	private SignUpHandler signUpHandler;

	@Resource
	private AssetSetApiHandler assetSetApiHandler;

	@Resource
	private RepaymentInformationApiHandler repaymentInformationApiHandler;

	@Resource
	private UpdateRepaymentInformationLogService updateRepaymentInformationLogService;

	@Resource
	private ModifyOverDueFeeHandlerNoSession modifyOverDueFeeHandlerNoSession;

	@Resource
	private ImportAssetPackageApiHandler importAssetPackageHandler;

	@Resource
	private DeductBusinessNotifyJobServer deductBusinessNotifyJobServer;

	@Resource
	private ContractService contractService;

	@Resource
	private PrepaymentHandler prepaymentHandler;

	@Value("#{config['notifyserver.groupCacheJobQueueMap_group2']}")
	private String groupNameForChannelsSignUp;

	@Value("#{config['zhonghang.signTransType']}")
	private String signTransType;

	@Value("#{config['zhonghang.notifyUrlToSignUp']}")
	private String notifyUrlToSignUp;

	@Resource
	private CardBinService cardBinService;

	@Resource
	private SupplierInfoHandler supplierInfoHandler;

	/**
	 * 变更还款计划
	 * @param modifyModel 还款计划变更模型
	 * @param request Http请求
	 * @param response  Http响应
	 * @return 返回变更的结果信息，包括成功和其他各种失败情况
	 */
	@RequestMapping(value = "", params = { PARAMS_FN_KEY_WITH_COMBINATORS + MODIFY_REPAYMENT_PLAN}, method =
			{RequestMethod.POST, RequestMethod.GET})
	@Deprecated
	public @ResponseBody String modifyRepaymentPlan(@ModelAttribute RepaymentPlanModifyModel modifyModel, HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("开始调用变更还款计划接口, 时间:" + DateUtils.getCurrentTimeMillis() + "请求参数：" + modifyModel.getRequestParamsInfo(request));
			//判断要变更的数据中是否存在异常数据
			if (modifyModel.paramsHasError()) {
				//添加调用接口出现异常数据的log
				logger.info("开始调用异常变更还款计划接口错误！");
				//返回变更的结果为出现了异常的参数，变更失败
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, modifyModel.getCheckFailedMsg());
			}
			//获得变更的还款计划所属的贷款合同
			Contract contract = contractApiHandler.getContractBy(modifyModel.getUniqueId(), modifyModel.getContractNo());
			if (null==contract){
				logger.info("contract is null");
			}else {
				logger.info("contract is not null");
				logger.info("post contractUuid : "+contract.getUuid());
			}

			//获得贷款合同未被变更前的版本号
			Integer oldActiveVersionNo = contractService.getActiveVersionNo(contract.getUuid());
			//修改还款计划，返回修改后的还款计划集合
			List<RepaymentPlanModifyDetail> details = assetSetApiHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, modifyModel, IpUtil.getIpAddress
					(request));

			Map<String, Object> resultMap = new HashMap<>(2);
			try {
				resultMap.put("repaymentPlanList", details);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("modifyRepaymentPlan-convertRepaymentPlanDetails fail, error msg:" + e.getMessage());
			}
			return signSucResult(response, resultMap);
		} catch (DuplicatedDeductionException | InvalidWriteOffException e) {
			e.printStackTrace();
			logger.error("#modifyRepaymentPlan occur error [requestNo : " + modifyModel.getRequestNo() + " ]!", e);
			return signErrorResult(response, new ApiException(EXSIT_PROCESSING_OR_SUCCESS_REPAYMENT_PLAN));

		} catch (GlobalRuntimeException e) {
			e.printStackTrace();
			ApiException exception = ApiException.getApiException(e);
			logger.error("#modifyRepaymentPlan occur error [requestNo : " + modifyModel.getRequestNo() + " ]!" + e.getCode() + ",msg" + e.getMessage());
			return signErrorResult(response, exception.getCode(), exception.getMessage());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("#modifyRepaymentPlan occur error [requestNo : " + modifyModel.getRequestNo() + " ]!" + e.getCode() + ",msg" + e.getMessage());
			return signErrorResult(response, e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#modifyRepaymentPlan occur error [requestNo : " + modifyModel.getRequestNo() + " ]!", e);
			return signErrorResult(response, e);
		}
	}

	@RequestMapping(value = "", params = { PARAMS_FN_KEY_WITH_COMBINATORS
			+ MODIFY_REPAYMENT_INFORMATION }, method = RequestMethod.POST)
	public @ResponseBody String modifyRepaymentInformation(
			@ModelAttribute RepaymentInformationModifyModel modifyModel,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("开始调用变更还款信息接口，时间:" + DateUtils.getCurrentTimeMillis()
				+ "请求参数：[ uniqueId: " + modifyModel.getUniqueId() + ", contractNo: "
				+ modifyModel.getContractNo() + ", requestNo: " + modifyModel.getRequestNo()
				+ IpUtil.getIpAddress(request) + "]");
		try {
			String bankCode = modifyModel.getBankCode();
			if (StringUtils.isEmpty(bankCode)) {
				Map<String, String> cardBinBankCodeMap = cardBinService.getCachedCardBins();
				bankCode = CardBinUtil.getBankCodeViaCardNo(cardBinBankCodeMap, modifyModel.getBankAccount());
				if (StringUtils.isEmpty(bankCode)) {
					return signErrorResult(response, ApiResponseCode.RECOGNIZE_BANK_BIN_FAILED,
							"[bankCode] 的值为空, 尝试自动进行匹配失败");
				} else {
					modifyModel.setBankCode(bankCode);
				}
			}
			if (!modifyModel.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, modifyModel.getCheckFailedMsg());
			}
			Contract contract = contractApiHandler.getContractBy(modifyModel.getUniqueId(), modifyModel.getContractNo());

			if (contract == null) {
				throw new ApiException(CONTRACT_NOT_EXIST);
			}
			/*
			* 请求参数新增“信托产品代码”选填字段，当该字段为非空时，做如下校验：
            * a.校验信托产品代码是否正确存在，若错误或不存在，返回失败+失败原因；
            * b.校验贷款合同唯一编号/贷款合同与产品代码是否存在映射关系，若不存在映射关系，返回失败+失败原因
            */
			String financialContractNo = modifyModel.getFinancialContractNo();
			if (StringUtils.isNotEmpty(financialContractNo)) {
				contractApiHandler.checkAndReturnFinancialContract(financialContractNo, contract);
			}
			checkRequestNoAndSaveLog(modifyModel, request, contract);
			repaymentInformationApiHandler.modifyRepaymentInformation(modifyModel, request, contract);
			return signSucResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#modifyRepaymentInformation occur error [requestNo : "+ modifyModel.getRequestNo() +" ]!");
			return signErrorResult(response, e);
		}

	}

	private void checkRequestNoAndSaveLog(
			RepaymentInformationModifyModel modifyModel,
			HttpServletRequest request, Contract contract) {
		updateRepaymentInformationLogService.checkRequestNo(modifyModel.getRequestNo());
		updateRepaymentInformationLogService.generateAndSaveUpdateRepaymentInformationLog(modifyModel, request, contract);
	}

	/**
	 * 提前还款
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + PREPAYMENT}, method = RequestMethod.POST)
	@Deprecated
	public @ResponseBody
	String prepayment(
			@ModelAttribute PrepaymentModifyModel model,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String ipAddress = IpUtil.getIpAddress(request);
			logger.info("开始调用提前还款接口, 时间:" + DateUtils.getCurrentTimeMillis() + ", 请求 ip:" + ipAddress + "]");
			if (!model.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
			Contract contract = contractApiHandler.getContractBy(model.getUniqueId(), model.getContractNo());
			String repaymentPlanNo = prepaymentHandler.new_prepayment(contract.getUuid(), model, ipAddress, null,
					Priority.High.getPriority());
			Map<String, Object> resultMap = new HashMap<>(2);
			resultMap.put("repaymentPlanNo", repaymentPlanNo);
			return signSucResult(response, resultMap);
		} catch (GlobalRuntimeException e) {
			ApiException exception = ApiException.getApiException(e);
			exception.printStackTrace();
			logger.error("#prepayment occur error [requestNo : " + model.getRequestNo() + " ]!");
			return signErrorResult(response, exception.getCode());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("#prepayment occur error [requestNo : " + model.getRequestNo() + " ]!" + e.getCode() + ",msg" + e.getMessage());
			return signErrorResult(response, e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#prepayment occur error [requestNo : " + model.getRequestNo() + " ]!");
			return signErrorResult(response, e);
		}
	}


	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + IMPORT_ASSET_PACKAGE}, method = RequestMethod.POST)
	@Deprecated
	public @ResponseBody
	String importAssetPackage(
			@ModelAttribute ImportAssetPackageRequestModel model,
			HttpServletRequest request, HttpServletResponse response) {
		String remoteKeyWord = "[remoteKeyWord=" + model.getRequestNo() + "]";

		try {

			String ipAddress = IpUtil.getIpAddress(request);
			remoteKeyWord += "ip=" + ipAddress + "]";
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + EARTH_ASSETPACKAGE_FUNCTION_POINT.IMPORT_ASSET_PACKAGE + remoteKeyWord + GloableLogSpec.RawData(model
					.getImportAssetPackageContent()));

			//校验model参数
			FinancialContract fc = importAssetPackageHandler.checkImportAssetPackageRequestModel(model, request);
			boolean notOfflineRemittance = fc.getRemittanceStrategyMode() != null && fc.getRemittanceStrategyMode() != RemittanceStrategyMode.OFFLINE_REMITTANCE;

			//校验生效日期,生成返回信息
			List<String> responseMessage = importAssetPackageHandler.checkContractEffectDate(model, notOfflineRemittance);

			supplierInfoHandler.saveSupplierInfo(model);
			//请求数据校验
			importAssetPackageHandler.dataVerification(model, notOfflineRemittance);

			ImportAssetPackageResponseModel responseModel = importAssetPackageHandler.importAssetPackage(model, notOfflineRemittance);
			responseModel.setResponseMessage(responseMessage);

			pushJobToEarthForSignUp(deductBusinessNotifyJobServer, request, model.getRequestContentObject().getContractDetails(), fc, groupNameForChannelsSignUp,
					signTransType, notifyUrlToSignUp);

			logger.info(GloableLogSpec.AuditLogHeaderSpec() + EARTH_ASSETPACKAGE_FUNCTION_POINT.IMPORT_ASSET_PACKAGE + remoteKeyWord + "[SUCC:import assetPackage " +
					"success]");
			return signSucResult(response, "batchResponse", responseModel);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(GloableLogSpec.AuditLogHeaderSpec() + EARTH_ASSETPACKAGE_FUNCTION_POINT.IMPORT_ASSET_PACKAGE + remoteKeyWord + "ERR:" + e.getMessage());
			return signErrorResult(response, e);
		}
	}

	private void pushJobToEarthForSignUp(DeductBusinessNotifyJobServer deductBusinessNotifyJobServer, HttpServletRequest request, List<ContractDetail>
			contractDetails, FinancialContract financialContract, String groupName, String signTransType, String notifyUrlToSignUp) {
		//yun xin跳过
		if (!signUpHandler.judgeZhongHangByfinancialContractCode(financialContract.getFinancialContractUuid())) {
			return;
		}
		List<NotifyApplication> jobList = new ArrayList<>();
		String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);
		String secret = request.getHeader(ApiConstant.PARAMS_SECRET);
		Map<String, String> paramForSignUp;
		for (ContractDetail contractDetail : contractDetails) {
			paramForSignUp = new HashMap<>(8);
			paramForSignUp.put("accName", contractDetail.getLoanCustomerName());
			paramForSignUp.put("accNo", contractDetail.getRepaymentAccountNo());
			paramForSignUp.put("certId", contractDetail.getIDCardNo());
			paramForSignUp.put("phoneNo", contractDetail.getMobile() == null ? StringUtils.EMPTY : contractDetail.getMobile());
			paramForSignUp.put("bankAliasName", contractDetail.getBankCode());
			paramForSignUp.put("bankFullName", StringUtils.EMPTY);
			List<NotifyApplication> jobs = signUpHandler.pushJobFromImportAssetAndContract(paramForSignUp, financialContract.getFinancialContractUuid(),
					financialContract.getContractNo(), notifyUrlToSignUp, merId, secret, signTransType, groupName, "1");
			jobList.addAll(jobs);
		}
		deductBusinessNotifyJobServer.pushJobs(jobList);
	}


	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + MODIFY_OVERDUE_FEE}, method = RequestMethod.POST)
	@Deprecated
	public @ResponseBody
	String modifyOverDueFee(
			@ModelAttribute ModifyOverDueFeeRequestModel model,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String ipAddress = IpUtil.getIpAddress(request);
			logger.info("开始调用逾期费用修改接口, 时间:" + DateUtils.getCurrentTimeMillis() + "content:" + JsonUtils.toJsonString(model) + ", 请求 ip:" + ipAddress + "]");
			if (!model.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
			List<Map<String, String>> repaymentPlanNoForUpdateFail;
			repaymentPlanNoForUpdateFail = modifyOverDueFeeHandlerNoSession.modifyOverDueFeeAndCheckData(model, request);
			if (repaymentPlanNoForUpdateFail.size() == 0) {
				return signSucResult(response);
			}
			return signErrorResult(response, ApiResponseCode.SYSTEM_ERROR, "系统错误", "repaymentPlanNoList", repaymentPlanNoForUpdateFail);
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("#modify over due fee error [requestNo : " + model.getRequestNo() + " ]!");
			return signErrorResult(response, e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#modify over due fee error [requestNo : " + model.getRequestNo() + " ]!");
			return signErrorResult(response, e);
		}
	}

}
