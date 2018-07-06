package com.suidifu.bridgewater.controller.pre.v2;

import static com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.VALID_REMITTANCD_NOT_CORRECT_PARAMS;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.suidifu.bridgewater.controller.base.v2.BaseApiController;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceNotifyJobSender;
import com.suidifu.matryoshka.prePosition.PrePositionHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SEND_RECEIVE_CODE_FOR_REMITTANCE;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 * 放款接口
 */
@Controller
@RequestMapping(RemittanceApplicationPreController.PRE_API)
public class RemittanceApplicationPreController extends BaseApiController {

	private static final Log logger = LogFactory.getLog(RemittanceApplicationPreController.class);

	public final static String PRE_API = "/pre-api/acception/";

	@Autowired
	@Qualifier("normalPrePositionHandlerNoTransaction")
	private PrePositionHandler prePositionHandler;

	@Autowired
	private RemittanceNotifyJobSender remittanceNotifyJobSender;

	@Autowired
	@Qualifier("normalPreSandboxHandler")
	private com.suidifu.matryoshka.handler.SandboxDataSetHandler sandboxDataSetHandler;

	@Value("#{config['notifyserver.notifyType']}")
	private String projectName;
	
	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;

    private static final Log st_logger = LogFactory.getLog("stLogger");

    
	@RequestMapping(value = "{system-code}/{function-code}/{product-code}", method = RequestMethod.POST)
	@ApiOperation(value = "放款前置接口", notes = "放款交易前置接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "system-code", value = "渠道代码", paramType = "path", dataType = "String", defaultValue = "outlier"),
			@ApiImplicitParam(name = "function-code", value = "渠道代码", paramType = "path", dataType = "String", defaultValue = "validation"),
			@ApiImplicitParam(name = "product-code", value = "服务代码", paramType = "path", dataType = "String", defaultValue = "yunxin"),
			@ApiImplicitParam(name = "fn", value = "功能代码(M)", paramType = "query", dataType = "String", defaultValue = "300002"),
			@ApiImplicitParam(name = "requestNo", value = "请求唯一标识(M)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "remittanceId", value = "放款订单唯一编号(M)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "productCode", value = "信托产品代码(M)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "uniqueId", value = "贷款合同唯一编号(M)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "contractNo", value = "贷款合同编号(M)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "contractAmount", value = "贷款合同本金(O)", paramType = "query", dataType = "BigDecimal"),
			@ApiImplicitParam(name = "plannedRemittanceAmount", value = "计划放款金额(M)", paramType = "query", dataType = "BigDecimal"),
			@ApiImplicitParam(name = "clearingAccount", value = "委托代扣清算号(O)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "auditorName", value = "审核人(O)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "auditTime", value = "审核时间(O)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "remark", value = "放款备注(O)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "remittanceStrategy", value = "放款策略(M)", paramType = "query", dataType = "int"),
			@ApiImplicitParam(name = "notifyUrl", value = "回调地址(M)", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "remittanceDetails", value = "放款明细列表(M)", paramType = "query", dataType = "String")})
	public @ResponseBody
	String remittanceApplicationPreProcessing(HttpServletRequest request,
	                                                   HttpServletResponse response) {

		SystemTraceLog systemTraceLog = null;
		String callName = getMerchantId(request);
		String ip = IpUtil.getIpAddress(request);
		Map<String, String> delayPostRequestParams = new HashMap<>();
		RemittanceCommandModel commandModel = new RemittanceCommandModel();
		commandModel.setProjectName(projectName);
		String remittanceApplicationUuid = commandModel.getRemittanceApplicationUuid();
		String checkRequestNo = commandModel.getCheckRequestNo();
		String eventKey = "checkRequestNo:" + checkRequestNo + "&remittanceApplicationUuid:" + remittanceApplicationUuid;
		try {
			String preProcessUrl = request.getRequestURL().toString().split(RemittanceApplicationPreController.PRE_API)[1];

			Map<String, String> allParameters = getAllParameters(request);

			if (MapUtils.isEmpty(allParameters)) {
				throw new ApiException("系统错误");
			}
			systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_OUTLIER_REQUEST_START, eventKey,
					null, null, SystemTraceLog.INFO, ip,
					SYSTEM_NAME.OUTLIER_SYSTEM, SYSTEM_NAME.REMITTANCE_SYSTEM);
			logger.info(systemTraceLog);
			
			st_logger.debug(systemTraceLog.getSql(EVENT_NAME.REMITTANCE_ACCEPT_OUTLIER_REQUEST_START,remittanceApplicationUuid,new Date()));

			int errorCode = prePositionHandler.prePositionDefaultTaskHandler(preProcessUrl, allParameters,
					delayPostRequestParams, logger);

			systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_DEALING_OUTLIER_REQUEST_START);
			logger.info(systemTraceLog);

			if (errorCode != ApiResponseCode.SUCCESS) {

				String errorMsg = delayPostRequestParams.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.ERROR_MSG);

				systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_DEALING_OUTLIER_REQUEST_END, eventKey,
						null, "validate parameter failed with message[" + errorMsg + "]", SystemTraceLog.ERROR, ip,
						SYSTEM_NAME.OUTLIER_SYSTEM, SYSTEM_NAME.REMITTANCE_SYSTEM);

				logger.info(systemTraceLog);

				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, errorMsg);
			}
			
			BeanUtils.populate(commandModel, delayPostRequestParams);
			

			try {
				logger.info(JSON.toJSONString(commandModel));
				iRemittanceApplicationHandler.saveRemittanceInfo(commandModel, ip, callName);
			} catch (ConstraintViolationException e) {

				e.printStackTrace();

				String errorMsg = VALID_REMITTANCD_NOT_CORRECT_PARAMS.getOrDefault(
						VALID_REMITTANCD_NOT_CORRECT_PARAMS.keySet().stream()
								.filter(param -> e.getCause().toString().contains(param)).findFirst().get(),
						"system error");

				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, errorMsg);

			}

			remittanceNotifyJobSender.pushJobToCitigroupForBusinessValidation(commandModel);

			systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ACCEPT_OUTLIER_REQUEST_END);
			logger.info(systemTraceLog);

			st_logger.debug(systemTraceLog.getSql(EVENT_NAME.REMITTANCE_ACCEPT_OUTLIER_REQUEST_END,remittanceApplicationUuid,new Date()));

			return signSucResult(response);
		} catch (Exception e) {

			systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_OUTLIER_REQUEST_ERROR,
					eventKey, null, e.getMessage(),
					SystemTraceLog.ERROR, ip, SYSTEM_NAME.OUTLIER_SYSTEM, SYSTEM_NAME.REMITTANCE_SYSTEM);

			logger.error(systemTraceLog);

			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}
}




