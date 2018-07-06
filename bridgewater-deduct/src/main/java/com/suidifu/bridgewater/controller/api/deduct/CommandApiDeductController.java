package com.suidifu.bridgewater.controller.api.deduct;

import com.alibaba.fastjson.JSON;
import com.suidifu.bridgewater.controller.BaseApiController;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.earth.v3.CommandOpsFunctionCodes;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.deduct.DeductCommandRequestModel;
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
import java.util.List;


@Controller
@RequestMapping("/api/command")
public class CommandApiDeductController  extends BaseApiController{

	private static final Log logger = LogFactory.getLog(CommandApiDeductController.class);
	@Autowired 
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;

	public static Object gloableLock=new Object();

	@Value("#{config['yunxin.notifyUrl']}")
	private String notifyUrl;

	@Value("#{config['deduct_pre_api']}")
	private String deduct_pre_api;
	/**
	 * 执行扣款指令
	 */
	@RequestMapping(value = "", params = {ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS +CommandOpsFunctionCodes.COMMAND_OVERDUE_DEDUCT}, method = RequestMethod.POST)
	public String commandDeduct(
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute DeductCommandRequestModel commandModel
			) {
		/*String oppositeKeyWord="[requestNo="+commandModel.getRequestNo()+";";
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +oppositeKeyWord+GloableLogSpec.RawData(JSON.toJSONString(commandModel)));

		try {
			//数据有效性校验
			if((!commandModel.isValid())||(!checkAndFillInfo(commandModel))) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS,commandModel.getCheckFailedMsg());
			}

			String callName = getMerchantId(request);

			List<TradeSchedule> tradeSchedules = deductApplicationBusinessHandler.checkAndsaveDeductInfoBeforePorcessing(commandModel, IpUtil.getIpAddress(request), callName);
			deductApplicationBusinessHandler.processingAndUpdateDeducInfo_NoRollback(tradeSchedules,  commandModel.getDeductApplicationUuid(), commandModel.getRequestNo());

			logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +"SUCC" );
			return signSucResult(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +oppositeKeyWord  +"ERR:" + e.getMessage());
			return signErrorResult(response, e);
		}*/

		return "forward:/" + deduct_pre_api;

	}

	private boolean checkAndFillInfo(DeductCommandRequestModel commandModel) {
		return deductApplicationBusinessHandler.fillInfoIntoDeductCommandRequestModel(commandModel);
	}

}
