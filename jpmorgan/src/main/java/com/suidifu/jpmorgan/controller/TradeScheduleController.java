package com.suidifu.jpmorgan.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.PaymentReturnModel;
import com.suidifu.jpmorgan.entity.QueryStatusResult;
import com.suidifu.jpmorgan.entity.SlotInfo;
import com.suidifu.jpmorgan.entity.SupplyCallbackResultModel;
import com.suidifu.jpmorgan.exception.AbandonTransactionException;
import com.suidifu.jpmorgan.exception.TradeScheduleParseException;
import com.suidifu.jpmorgan.exception.UpdateSlotException;
import com.suidifu.jpmorgan.handler.TradeScheduleHandler;
import com.suidifu.jpmorgan.spec.TradeScheduleControllerSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;
import com.zufangbao.gluon.resolver.JsonViewResolver;

@Controller
@RequestMapping("/tradeSchedule")
public class TradeScheduleController {

	@Autowired
	TradeScheduleHandler tradeScheduleHandler;
	@Autowired
	JsonViewResolver jsonViewResolver;

	private static final Log logger = LogFactory
			.getLog(TradeScheduleController.class);

	@RequestMapping(value = "singlePayment", method = RequestMethod.POST)
	public @ResponseBody String singlePayment(@RequestBody String reqPacket) {
		
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + GloableLogSpec.RawData(reqPacket));
		
		try {
			PaymentReturnModel paymentReturnModel = tradeScheduleHandler
					.parseAndSaveSinglePayment(reqPacket);
			
			return jsonViewResolver.sucJsonResult("paymentReturnResult", paymentReturnModel);
		} catch (TradeScheduleParseException e) {
			e.printStackTrace();
			logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + GloableLogSpec.RawData(reqPacket)+"ERR:" + e.getMessage());
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + GloableLogSpec.RawData(reqPacket)+"ERR" + e.getMessage());
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
	}

	@RequestMapping(value = "batchPayment", method = RequestMethod.POST)
	public @ResponseBody String batchPayment(@RequestBody String reqPacket) {
		
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + GloableLogSpec.RawData(reqPacket));

		try {
			List<PaymentReturnModel> paymentReturnModelList = tradeScheduleHandler
					.parseAndSaveBatchPayment(reqPacket);
			
			return jsonViewResolver.sucJsonResult("paymentReturnResult", paymentReturnModelList);

		} catch (TradeScheduleParseException e) {
			e.printStackTrace();
			logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + GloableLogSpec.RawData(reqPacket)+"ERR");
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + GloableLogSpec.RawData(reqPacket)+"ERR");
			return jsonViewResolver.errorJsonResult("受理失败");
		}
	}

	@RequestMapping(value = "updateSlot", method = RequestMethod.POST)
	public @ResponseBody String updateSlot(@RequestParam("reqPacket") String reqPacket) {
		try {
			SlotInfo slotInfo = JSON.parseObject(reqPacket, SlotInfo.class);
			boolean updateSuccess = tradeScheduleHandler.updateSlot(slotInfo);
			if (updateSuccess) {
				return jsonViewResolver.sucJsonResult();
			}
		} catch (UpdateSlotException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonViewResolver.errorJsonResult("更新失败！");
	}

	@RequestMapping(value = "abandonTransaction", method = RequestMethod.POST)
	public @ResponseBody String abandonTransaction(
			@RequestParam(value = "transactionUuid", required = false) String transactionUuid,
			@RequestParam(value = "batchUuid", required = false) String batchUuid) {
		try {
			if (StringUtils.isEmpty(transactionUuid) && StringUtils.isEmpty(batchUuid)) {
				throw new AbandonTransactionException(
						TradeScheduleControllerSpec.ERR_EMPTY_REQUEST_NO);
			}
			
		} catch (AbandonTransactionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonViewResolver.errorJsonResult("操作失败！");
	}

	@RequestMapping(value = "queryStatus", method = RequestMethod.POST)
	public @ResponseBody String queryStatus(
			@RequestParam(value = "transactionUuid", required = false) String transactionUuid, 
			@RequestParam(value = "batchUuid", required = false) String batchUuid) {
		try {
			if (StringUtils.isEmpty(transactionUuid) && StringUtils.isEmpty(batchUuid)) {
				return jsonViewResolver.errorJsonResult(TradeScheduleControllerSpec.MSG_EMPTY_UUID_ERROR);
			}

			List<QueryStatusResult> queryResultList = tradeScheduleHandler
					.queryStatus(transactionUuid, batchUuid);
			
			return jsonViewResolver.sucJsonResult("queryResult", queryResultList, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "queryOppositeStatus", method = RequestMethod.POST)
	public @ResponseBody String queryOppositeStatus(
			@RequestParam(value = "paymentChannelUuid") String paymentChannelUuid, 
			@RequestParam(value = "transactionVoucherNo") String transactionVoucherNo) {
		try {
			if (StringUtils.isEmpty(paymentChannelUuid)) {
				return jsonViewResolver.errorJsonResult(TradeScheduleControllerSpec.EMPTY_CHANNEL_UUID);
			}
			if (StringUtils.isEmpty(transactionVoucherNo)) {
				return jsonViewResolver.errorJsonResult(TradeScheduleControllerSpec.EMPTY_TRANSACTION_VOUCHER_NO);
			}
			
			QueryCreditResult queryCreditResult = tradeScheduleHandler.queryOppositeStatus(paymentChannelUuid, transactionVoucherNo);
			
			return jsonViewResolver.sucJsonResult("queryResult", queryCreditResult, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "supplyCallback", method = RequestMethod.POST)
	public @ResponseBody String supplyCallback(
			@RequestParam(value = "transactionUuidListJson", required = false) String transactionUuidListJson) {
		try {
			if (StringUtils.isEmpty(transactionUuidListJson)) {
				return jsonViewResolver.errorJsonResult(TradeScheduleControllerSpec.MSG_EMPTY_UUID_ERROR);
			}
			
			List<String> transactionUuidList = JsonUtils.parseArray(transactionUuidListJson, String.class);
			
			if(null == transactionUuidList) {
				return jsonViewResolver.errorJsonResult(TradeScheduleControllerSpec.MSG_EMPTY_UUID_ERROR);
			}
			
			List<SupplyCallbackResultModel> supplyCallbackResultModelList = tradeScheduleHandler.supplyCallback(transactionUuidList);
			
			return jsonViewResolver.sucJsonResult("supplyCallbackResultModelList", supplyCallbackResultModelList, SerializerFeature.DisableCircularReferenceDetect);

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
		
	}
			

	@RequestMapping(value = "innerCallback", method = RequestMethod.POST)
	public @ResponseBody String queryPaymentOrder(@RequestParam("tradeUuid") String tradeUuid, @RequestParam("nthSlot") int nthSlot, @RequestParam("gatewayName") String gatewayName, @RequestParam("gatewaySlot") String gatewaySlot) {
		String operateKey = null;
		try {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.INNER_CALLBACK_RECEIVE_CONTENT + "tradeUuid:" + tradeUuid + ",nthSlot:" + nthSlot + ",gatewayName:" + gatewayName + ",gatewaySlot:" + gatewaySlot);
			
			if(StringUtils.isEmpty(tradeUuid) || StringUtils.isEmpty(gatewaySlot) || StringUtils.isEmpty(gatewayName)) {
				return jsonViewResolver.errorJsonResult(TradeScheduleControllerSpec.ERR_ILLEGAL_REQUEST_DATA);
			}
			
			GatewaySlot gatewaySlotUpdate = JSON.parseObject(gatewaySlot, GatewaySlot.class);
			
			if(null == gatewaySlotUpdate) {
				logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.INNER_CALLBACK_RECEIVE_CONTENT + "[ERROR]" + "content:" + gatewaySlot + "MESSAGE:cant parse gatewaySlotUpdate!");
			}

			operateKey = gatewaySlotUpdate.getSlotUuid();
			tradeScheduleHandler.handleInnerCallback(tradeUuid, nthSlot, gatewayName, gatewaySlotUpdate);
			
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.INNER_CALLBACK_RECEIVE_CONTENT + "[ERROR]" + "operateKey:" + operateKey + "MESSAGE:" + e.getMessage());
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
		
	}
}
