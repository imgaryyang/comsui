package com.suidifu.morganstanley.controller.api.transfer;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.TransferBillProcessHandler;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBill;
import com.zufangbao.sun.yunxin.service.TransferBillService;
import com.zufangbao.wellsfargo.yunxin.handler.TransferBillHandler;

@Controller
@RequestMapping("/notify")
public class CommonApiNotifyController extends BaseApiController{

	private static final Log logger = LogFactory.getLog(CommonApiNotifyController.class);
	
	@Autowired
	private TransferBillProcessHandler transferBillProcessHandler;
	@Autowired
	private TransferBillHandler transferBillHandler;
	@Autowired
	private TransferBillService transferBillService;
	@Autowired
	private JsonViewResolver jsonViewResolver;
	
	
	@RequestMapping(value = "/transfer" , method = RequestMethod.POST)
	public @ResponseBody String receiveTransferBillCallback(@RequestBody String messsage, HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> allParameters = JsonUtils.parse(messsage);
			 if (MapUtils.isEmpty(allParameters)) {
				 return jsonViewResolver.errorJsonResult(ApiResponseCode.INVALID_PARAMS);
			}
			String orderUuid = transferBillProcessHandler.processNotifyInfo(allParameters);
			transferBillProcessHandler.processingTransferBillCallback(orderUuid);
			return jsonViewResolver.sucJsonResult();
		} catch (ApiException apie){
			return jsonViewResolver.errorJsonResult(apie.getMessage());
		} catch (Exception e) {
			logger.error("#receiveTransferBillCallback# occur exception with stack trace["
					+ ExceptionUtils.getFullStackTrace(e) + "]");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(ApiResponseCode.SYSTEM_ERROR);
		}
	}
	
	
}
