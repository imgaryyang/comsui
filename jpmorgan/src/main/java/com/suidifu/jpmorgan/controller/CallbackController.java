package com.suidifu.jpmorgan.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.PaymentWorkerContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.handler.PaymentWorkerContextHanlder;
import com.suidifu.jpmorgan.service.PaymentOrderService;
import com.suidifu.jpmorgan.spec.PaymentTaskSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.util.RequestUtil;

@Controller
@RequestMapping("/callback")
public class CallbackController {

	private static final Log logger = LogFactory.getLog(CallbackController.class);
	
	@Autowired
	JsonViewResolver jsonViewResolver;
	@Autowired
	private PaymentWorkerContextHanlder paymentWorkerContextHanlder;
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@RequestMapping(value = "sxccb", method = RequestMethod.POST)
	public @ResponseBody String sxccbCallBack(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> requestParams = RequestUtil.getRequestParams(request);
			
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_CALLBACK_FROM_OPPOSITE + "requestParams[" + requestParams + "]");

			String workerUuid = requestParams.getOrDefault("merReserved1", StringUtils.EMPTY);
			
			if(StringUtils.isEmpty(workerUuid)) {
				logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_CALLBACK_FROM_OPPOSITE + "ERR[workerUuid is empty]");
				return jsonViewResolver.errorJsonResult("失败！");
			}
			
			PaymentWorkerContext workercontext = paymentWorkerContextHanlder.buildWorkerContext(workerUuid);
			String queueTableName = workercontext.getQueueTableName();
			String logTableName = workercontext.getLogTableName();
			PaymentHandler paymentHandler = workercontext.getWorkingHanlder();
			
			QueryCreditResult queryCreditResult = paymentHandler.handleCallback(requestParams);
			
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_CALLBACK_FROM_OPPOSITE + "orderNo=" + queryCreditResult.getTransactionVoucherNo() + "commStatus=" + queryCreditResult.getCommCode() + "requestStatus=" + queryCreditResult.getRequestStatus() + "businessStatus=" + queryCreditResult.getProcessStatus());

			if(null == queryCreditResult || queryCreditResult.commFailed()) {
				logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_CALLBACK_FROM_OPPOSITE + "ERR[callback内容解析失败！]");
				return jsonViewResolver.errorJsonResult("callback内容解析失败！");
			}
			
			String transactionVoucherNo = queryCreditResult.getTransactionVoucherNo();
			if(StringUtils.isEmpty(transactionVoucherNo)) {
				logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_CALLBACK_FROM_OPPOSITE + "ERR[商户订单号解析失败！]");
				return jsonViewResolver.errorJsonResult("商户订单号解析失败！");
			}
			
			PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderBy(transactionVoucherNo, queueTableName);
			
			if(queryCreditResult.isBusinessSuccess() || queryCreditResult.isBusinessFailed()) {
				boolean updateSuccess = paymentOrderService.updateBusinessStatus(paymentOrder, queryCreditResult, PaymentTaskSpec.UPDATE_TRY_TIMES, queueTableName);
				if(updateSuccess) {
					paymentOrderService.transferToPaymentOrderLog(paymentOrder, queueTableName, logTableName);
				}
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误!");
		}
		
	}
	
}
