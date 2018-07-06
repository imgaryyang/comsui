package com.suidifu.barclays.controller;

import java.util.Map;

import com.demo2do.core.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.suidifu.barclays.handler.PaymentAsyncHandler;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.zufangbao.gluon.resolver.JsonViewResolver;

/*
 * @author zfj
 * @date 17/6/20
 */
@Controller
@RequestMapping(value = "/paymentAsync")
public class PaymentAsynController {
	@Autowired
	JsonViewResolver jsonViewResolver;
	
	@Autowired
	private PaymentAsyncHandler paymentAsyncHandler;
	
    private static final Log LOGGER = LogFactory.getLog(PaymentAsynController.class);

    /*
     * QueryCreditModel
     *  查询贷记状态model
     *  
     * QueryCreditResult
     *  查询贷记状态结果
     */
    
	@RequestMapping(value = "/asyncQueryStatus", method = RequestMethod.POST)
	public @ResponseBody String asyncQueryStatus(@RequestParam String queryCreditModelJson,
												 @RequestParam String financialContractUuid) {
		if (StringUtils.isBlank(queryCreditModelJson)) {
			return jsonViewResolver.errorJsonResult("queryCreditModel is null.");
		}
		if (StringUtils.isBlank(financialContractUuid)) {
			return jsonViewResolver.errorJsonResult("financialContractUuid is null.");
		}
		try {
			QueryCreditModel queryCreditModel = JsonUtils.parse(queryCreditModelJson, QueryCreditModel.class);
			if (null == queryCreditModel) {
				return jsonViewResolver.errorJsonResult("queryCreditModelJson convert to QueryCreditModel fail.");
			}
			if (StringUtils.isBlank(queryCreditModel.getPaymentGateWayName())) {
				return jsonViewResolver.errorJsonResult("payment gate way name is empty.");
			}
			if (StringUtils.isBlank(queryCreditModel.getAccountNo())) {
				return jsonViewResolver.errorJsonResult("account no is empty.");
			}
			QueryCreditResult creditResult = paymentAsyncHandler.queryDebit(queryCreditModel, financialContractUuid);
			return jsonViewResolver.sucJsonResult("queryResult", creditResult, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			LOGGER.error("asyncQueryStatus error : " + e.getMessage());
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
	}
}
