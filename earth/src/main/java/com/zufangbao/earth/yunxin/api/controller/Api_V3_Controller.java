package com.zufangbao.earth.yunxin.api.controller;

import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.earth.yunxin.api.handler.PaymentOrderApiHandler;
import com.zufangbao.earth.yunxin.api.handler.RepurchaseCommandHandler;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.repayment.QueryPaymentOrderRequestModel;
import com.zufangbao.sun.service.MutableFeeLogService;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentPlanHandlerNoSession;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(ApiConstant.ApiUrlConstant.URL_API_V3)
@Api(value = "五维金融贷后接口V3.0", description = "五维金融贷后接口V3.0")
public class Api_V3_Controller extends BaseApiController {

	@Autowired
	private MutableFeeLogService mutableFeeLogService;
	
	@Autowired
	private RepurchaseCommandHandler repurchaseCommandHandler;

	@Autowired
	private RepaymentPlanHandlerNoSession repaymentPlanHandlerNoSession;
	
	@Autowired
	private PaymentOrderApiHandler paymentOrderApiHandler;
	
	private static final Log logger = LogFactory.getLog(Api_V3_Controller.class);

	/**
	 * 支付查询接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ApiConstant.ApiUrlConstant.QUERY_PAYMENT_ORDER, method = RequestMethod.POST)
	public @ResponseBody String queryPaymentOrder(
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute QueryPaymentOrderRequestModel commandModel) {
		
		try {
			long start = System.currentTimeMillis();
			if (commandModel == null) {
				return signErrorResult(response, ApiResponseCode.WRONG_FORMAT);
			}

			//判断为空校验
			if(!commandModel.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, commandModel.getCheckFailedMsg());
			}
			
			List<Map<String, Object>> queryPaymentOrderResultList = paymentOrderApiHandler.queryPaymentOrderResponeData(commandModel);
			long end = System.currentTimeMillis();
			logger.info("#queryPaymentOrderResultList success, orderUniqueId size : " + queryPaymentOrderResultList.size() + ". used ["+(end-start)+"]ms");
			return signSucResult(response, "queryPaymentOrderResultList", queryPaymentOrderResultList);
		} catch (Exception e) {
			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}

}
