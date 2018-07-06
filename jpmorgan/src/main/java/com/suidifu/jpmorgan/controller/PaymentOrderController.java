package com.suidifu.jpmorgan.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.jpmorgan.entity.DistributeWorkingContext;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.exception.paymentorder.PaymentOrderListOfNullException;
import com.suidifu.jpmorgan.exception.paymentorder.PaymentOrderOfNullException;
import com.suidifu.jpmorgan.handler.PaymentOrderHandler;
import com.suidifu.jpmorgan.spec.TradeScheduleHandlerSpec.ErrorMsg;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;
import com.zufangbao.gluon.resolver.JsonViewResolver;

@Controller
@RequestMapping("/paymentOrder")
public class PaymentOrderController {

	@Autowired
	PaymentOrderHandler paymentOrderHandler;
	@Autowired
	JsonViewResolver jsonViewResolver;
	
	private static final Log logger = LogFactory.getLog(PaymentOrderController.class);
	
	@RequestMapping(value = "distribute", method = RequestMethod.POST)
	public @ResponseBody String receivePaymentOrder(@ModelAttribute DistributeWorkingContext workingContext) {
		String remoteKeyWord = null;
		try {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_PAYMENTORDER_REQUEST_FROM_TRADESCHEDULE + GloableLogSpec.RawData(workingContext.getTradeInfo()));
			
			PaymentOrder paymentOrder = JsonUtils.parse(workingContext.getTradeInfo(), PaymentOrder.class);
			
			remoteKeyWord ="[" + paymentOrder.getUuid() + "]";
		
			paymentOrderHandler.paymentOrderListInitAndSave(paymentOrder, workingContext);
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_PAYMENTORDER_REQUEST_FROM_TRADESCHEDULE + remoteKeyWord  + "[SUCC]");
			return jsonViewResolver.sucJsonResult();
		} catch (PaymentOrderListOfNullException e) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_PAYMENTORDER_REQUEST_FROM_TRADESCHEDULE +remoteKeyWord+ GloableLogSpec.RawData(workingContext.getTradeInfo()) + "ERR:" +e.getMessage());
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch(PaymentOrderOfNullException e) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_PAYMENTORDER_REQUEST_FROM_TRADESCHEDULE + remoteKeyWord+GloableLogSpec.RawData(workingContext.getTradeInfo())  + "ERR:"+ e.getMessage());
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (Exception e) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_PAYMENTORDER_REQUEST_FROM_TRADESCHEDULE + remoteKeyWord+GloableLogSpec.RawData(workingContext.getTradeInfo())  + "ERR:"+ e.getMessage());
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "queryStatus", method = RequestMethod.POST)
	public @ResponseBody String queryPaymentOrder(@RequestParam("uuid") String uuid, @RequestParam("tableName") String tableName, @RequestParam("logTableName") String logTableName) {
		try {
			GatewaySlot realtimeInfo = paymentOrderHandler.getRealtimeInfo(uuid, tableName, logTableName);
			
			if(null == realtimeInfo) {
				return jsonViewResolver.errorJsonResult(ErrorMsg.MSG_NO_INFO_ERROR);
			}
			return jsonViewResolver.sucJsonResult("gatewaySlot", realtimeInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
	}
}
