package com.suidifu.bridgewater.controller.api.remittance;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.RemittanceBlackListCommandModel;
import com.suidifu.bridgewater.controller.BaseApiController;
import com.suidifu.bridgewater.handler.RemittanceBlackListHandler;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.earth.v3.CommandOpsFunctionCodes;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/command")
public class CommandApiRemittanceController extends BaseApiController{

	private static final Log logger = LogFactory.getLog(CommandApiRemittanceController.class);
	
	@Autowired
	private RemittanceBlackListHandler remittanceBlackListHandler;

	@Value("#{config['remittance_pre_api']}")
	private String REMITTANCE_PRE_API = "";
	
	@Value("#{config['transfer_pre_api']}")
	private String TRANSFER_PRE_API = "";
	/**
	 * 执行放款（汇款）指令
	 * @return
	 */
	@RequestMapping(value = "", params = {ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS + CommandOpsFunctionCodes.COMMAND_REMITTANCE}, method = RequestMethod.POST)
	public String commandRemittanceV2(
			HttpServletRequest request, HttpServletResponse response, @ModelAttribute RemittanceCommandModel commandModel) {

		return "forward:/" + REMITTANCE_PRE_API;
	}
	
	
	/**
	 * 取消贷款合同接口（放款黑名单）
	 */
	@RequestMapping(value = "", params = {ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS + CommandOpsFunctionCodes.COMMAND_REMITTANCE_BLACK_LIST }, method = RequestMethod.POST)
	public @ResponseBody String recordRemittanceBlackList(
			@ModelAttribute RemittanceBlackListCommandModel model,
			HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			String ipAddress = IpUtil.getIpAddress(request);
			logger.info("开始调用放款黑名单接口, 时间:" + DateUtils.getCurrentTimeMillis() + "content:"+JsonUtils.toJsonString(model) + ", 请求 ip:" + ipAddress + "]");
			
			if(!model.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getCheckFailedMsg());
			}
			
			/*休眠20s 降低与放款接口并发的可能 软顺序*/
			Thread.sleep(20000);
			
			String creatorName = getMerchantId(request);
			remittanceBlackListHandler.recordRemittanceBlackList(model, ipAddress, creatorName);
			return signSucResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#recordRemittanceBlackList occur error [requestNo : "+ model.getRequestNo() +" ]!");
			return signErrorResult(response, e);
		}
	}
	
	
	
	/**
	 * 执行转账指令
	 * @return
	 */
	@RequestMapping(value = "", params = {ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS + CommandOpsFunctionCodes.COMMAND_TRANSFER_COMMAND}, method = RequestMethod.POST)
	public String transferAccounts(
			HttpServletRequest request, HttpServletResponse response, @ModelAttribute RemittanceCommandModel commandModel) {

		return "forward:/" + TRANSFER_PRE_API;
	}
	
}
