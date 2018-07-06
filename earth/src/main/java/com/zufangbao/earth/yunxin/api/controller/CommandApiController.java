package com.zufangbao.earth.yunxin.api.controller;

import static com.zufangbao.earth.yunxin.api.constant.ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS;
import static com.zufangbao.earth.yunxin.api.constant.CommandOpsFunctionCodes.COMMAND_ACTIVE_PAYMENT_VOUCHER;
import static com.zufangbao.earth.yunxin.api.constant.CommandOpsFunctionCodes.COMMAND_BUSINESS_PAYMENT_VOUCHER;
import static com.zufangbao.earth.yunxin.api.constant.CommandOpsFunctionCodes.COMMAND_THIRD_PART_PAY_VOUCHER;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.earth.yunxin.api.handler.ActivePaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.handler.BusinessPaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherCommandHandler;
import com.zufangbao.earth.yunxin.api.model.command.ActivePaymentVoucherCommandModel;
import com.zufangbao.earth.yunxin.api.model.command.BusinessPaymentVoucherCommandModel;
import com.zufangbao.gluon.exception.voucher.BankTransactionNoExistException;
import com.zufangbao.gluon.exception.voucher.CashFlowNotExistException;
import com.zufangbao.gluon.opensdk.BaseExceptionUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.handler.ThirdPartyVoucherHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowChargeProxy;
import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherProxy;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/api/command")
public class CommandApiController extends BaseApiController{

	private static final Log logger = LogFactory.getLog(CommandApiController.class);

	@Autowired
	private BusinessPaymentVoucherHandler businessPaymentVoucherHandler;
	@Autowired
	private ContractApiHandler contractApiHandler;

	@Autowired
	private ThirdPartVoucherCommandHandler thirdPartVoucherCommandHandler;
	@Autowired
	private ActivePaymentVoucherProxy activePaymentVoucherProxy;
	@Autowired
	private CashFlowChargeProxy cashFlowChargeProxy;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private ThirdPartyVoucherHandler thirdPartyVoucherHandler;

	private final String BankTransactionNoExistExceptionMsg = "打款流水号已关联凭证！";

	private final String cashFlowNotExistExceptionMsg = "凭证对应流水不存在或已提交！";
	/*
	*//**
	 * 执行扣款指令
	 *//*
    @RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + COMMAND_OVERDUE_DEDUCT}, method = RequestMethod.POST)
	public @ResponseBody String commandDeduct(
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute DeductCommandRequestModel commandModel
			) {
		try {
			if(!commandModel.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS,commandModel.getCheckFailedMsg());
			}
			String merchantId = getMerchantId(request);
			//创建
			List<TradeSchedule> tradeSchedules = deductApplicationHandler.checkAndsaveDeductInfoBeforePorcessing(commandModel, IpUtil.getIpAddress(request),
			merchantId);

			deductApplicationHandler.processingAndUpdateDeducInfo_NoRollback(tradeSchedules,  commandModel.getDeductApplicationUuid());
			return signSucResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#commandOverdueDeduct occur error [requestNo : "+ commandModel.getRequestNo() +" ]!");
			return signErrorResult(response, e);
		}
	} */

	/**
	 * 商户付款凭证指令-提交
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + COMMAND_BUSINESS_PAYMENT_VOUCHER, "transactionType=0"}, method = RequestMethod.POST)
	public @ResponseBody String submitBusinessPaymentVoucher(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BusinessPaymentVoucherCommandModel model) {
		try {
			if(!model.checkSubmitParams()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
            List<CashFlow> cashFlowList = businessPaymentVoucherHandler.businessPaymentVoucher(model, IpUtil.getIpAddress(request));
            if(cashFlowList.size() > 1) {
				return signSucResult(response, "处理中！", null, Collections.emptyMap(), SerializerFeature.DisableCircularReferenceDetect);
			}else {
				return signSucResult(response);
			}
		} catch (CashFlowNotExistException e) {
			e.printStackTrace();
			logger.error("#commandBusinessPaymentVoucher occur error ]: " + cashFlowNotExistExceptionMsg);
			return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, cashFlowNotExistExceptionMsg);
		}catch (BankTransactionNoExistException e) {
			e.printStackTrace();
			logger.error("#commandBusinessPaymentVoucher occur error ]: " + BankTransactionNoExistExceptionMsg);
			return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, BankTransactionNoExistExceptionMsg);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("#commandBusinessPaymentVoucher occur error ]: " + e.getMessage());
			return signErrorResult(response, e);
		}
	}

	/**
	 * 商户付款凭证指令-撤销
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + COMMAND_BUSINESS_PAYMENT_VOUCHER, "transactionType=1"}, method = RequestMethod.POST)
	public @ResponseBody String undoBusinessPaymentVoucher(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute BusinessPaymentVoucherCommandModel model) {
		try {
			if(!model.checkUndoParams()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
            businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model, IpUtil.getIpAddress(request));
            return signSucResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#undoBusinessPaymentVoucher occur error ]: " + e.getMessage());
			return signErrorResult(response, e);
		}
	}

	/**
	 * 主动付款凭证指令 - 提交
	 */
	@Autowired
	private ActivePaymentVoucherHandler activePaymentVoucherHandler;

	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + COMMAND_ACTIVE_PAYMENT_VOUCHER, "transactionType=0"}, method = RequestMethod.POST)
	public @ResponseBody String submitActivePaymentVoucher(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ActivePaymentVoucherCommandModel model) {
		try {
			MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
			if(model.submitParamsError()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
            activePaymentVoucherHandler.checkRequestNoAndSaveLog(model, IpUtil.getIpAddress(fileRequest));
            List<ContractActiveSourceDocumentMapper> mappers = activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);
			if(CollectionUtils.isNotEmpty(mappers)) {
				for(ContractActiveSourceDocumentMapper mapper : mappers) {
					sendChargeMsg2Mq(mapper);
					sendRecoverMsg2Mq(mapper);
				}
			}
//			if(mapper!=null){
//				sendChargeMsg2Mq(mapper);
//				sendRecoverMsg2Mq(mapper);
//			}
			return signSucResult(response);
		} catch (CashFlowNotExistException e) {
			e.printStackTrace();
			logger.error("#submitActivePaymentVoucher occur error ]: " + cashFlowNotExistExceptionMsg);
			return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, cashFlowNotExistExceptionMsg);
		}catch (BankTransactionNoExistException e) {
			logger.error("#submitActivePaymentVoucher occur error ]: " + BankTransactionNoExistExceptionMsg);
			return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, BankTransactionNoExistExceptionMsg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#submitActivePaymentVoucher occur error ]: " + e.getMessage());
			return signErrorResult(response, e);
		}
	}

	private void sendChargeMsg2Mq(ContractActiveSourceDocumentMapper mapper) {
		try {
			logger.info("#api提交主动还款凭证后，发送充值消息，contractUuid["+mapper.getContractUuid()+"],sourceDocumentUuid["+mapper.getSourceDocumentUuid()+"],cashFlowUuid["+mapper.getCashFlowUuid()+"]");
			cashFlowChargeProxy.charge_cash_into_virtual_account_for_rpc(mapper.getCashFlowUuid(), mapper.getContractUuid(), Priority.High.getPriority());
		} catch (Exception e) {
			logger.error("#api提交主动还款凭证后，发送充值消息，contractUuid["+mapper.getContractUuid()+"],sourceDocumentUuid["+mapper.getSourceDocumentUuid()+"],cashFlowUuid["+mapper.getCashFlowUuid()+"]，with exception stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
	}

	private void sendRecoverMsg2Mq(ContractActiveSourceDocumentMapper mapper){
		try {
			logger.info("#api提交主动还款凭证后，发送待核销主动还款凭证消息，contractUuid["+mapper.getContractUuid()+"],sourceDocumentUuid["+mapper.getSourceDocumentUuid()+"]");
			activePaymentVoucherProxy.recover_active_payment_voucher(mapper.getContractUuid(), mapper.getSourceDocumentUuid(), Priority.Medium.getPriority());
		} catch (Exception e) {
			logger.error("#api提交主动还款凭证后，发送待核销主动还款凭证消息，contractUuid["+mapper.getContractUuid()+"],sourceDocumentUuid["+mapper.getSourceDocumentUuid()+"],with exception stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
	}

	/**
	 * 主动付款凭证指令-撤销
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + COMMAND_ACTIVE_PAYMENT_VOUCHER, "transactionType=1"}, method = RequestMethod.POST)
	public @ResponseBody String undoActivePaymentVoucher(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ActivePaymentVoucherCommandModel model) {
		try {
			if(model.undoParamsError()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
            activePaymentVoucherHandler.checkRequestNoAndSaveLog(model, IpUtil.getIpAddress(request));
            FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
			String bankTransactionNo = model.getBankTransactionNo();
			activePaymentVoucherHandler.undoActivePaymentVoucher(financialContract, bankTransactionNo);
			return signSucResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#undoBusinessPaymentVoucher occur error ]: " + e.getMessage());
			return signErrorResult(response, e);
		}
	}
	/**
	 * 第三方付款凭证指令
	 */
	@RequestMapping(value = "", params={PARAMS_FN_KEY_WITH_COMBINATORS + COMMAND_THIRD_PART_PAY_VOUCHER}, method = RequestMethod.POST )
	public @ResponseBody String  thirdPartVoucherCommandModel(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute ThirdPartVoucherModel model ){
		try {
			//必填选填校验
			if(model.validateThirdPartVoucherRequest(model) == false) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
			/*//如果日期是元年改为空
			model.modifyYearOneToBlank();
			*/
			//逻辑校验
			thirdPartVoucherCommandHandler.commandRequestLogicValidate(model);
			//生成对应的请求log
            Map<String, Object> errMap = thirdPartVoucherCommandHandler.generateThirdPartVoucherCommandLog(model, IpUtil.getIpAddress(request));
            if (errMap.size()>0) {
				return signErrorResultMap(response, ApiResponseCode.STATUS_CANNOT_INSERT, errMap);
			}
			return signSucResult(response);
		}catch (Exception e) {
			e.printStackTrace();
			boolean isDuplicatedKey=BaseExceptionUtils.isDuplicate(e);
			if(isDuplicatedKey==true)
			{
				return signErrorResult(response,ApiResponseCode.REPEATE_VOUCHER_NO);
			}

			logger.error("#thirdPartVoucherCommandModel occur error ]: " + e.getMessage());
			return signErrorResult(response, e);
		}
	}
}
