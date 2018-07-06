package com.suidifu.bridgewater.controller.api.deduct.batch.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationSqlModel;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.handler.DeductPlanBusinessHandler;
import com.suidifu.bridgewater.handler.single.v2.BatchDeductApplicationBusinessHandler;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;

import java.util.Date;

/**
 * @author wukai
 * 扣款请求内部回调方法，接收jpmorgan处理请求的回调
 *
 */
@Controller
@RequestMapping("/jpmorgan/callback")
public class JpmorganCallbackController {
	
	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	
	private static Log logger = LogFactory.getLog(JpmorganCallbackController.class);

	private static final Log st_logger = LogFactory.getLog("deduct_st_logger");
	
	@Autowired
	private DeductPlanService deductPlanService;
	
	@Autowired
	private DeductPlanBusinessHandler deductPlanHandler;
	
	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;

	@Autowired
	private BatchDeductApplicationBusinessHandler batchDeductApplicationBusinessHandler;

	@Autowired
	private DeductApplicationService deductApplicationService;

	@RequestMapping(value="/deduct",method=RequestMethod.POST)
	public @ResponseBody String receiveDeductCallback(HttpServletRequest request, HttpServletResponse response){
		SystemTraceLog systemTraceLog = new SystemTraceLog();
		try {
			long start = System.currentTimeMillis();
			String resultJsonString = request.getParameter("transactionResult");

			systemTraceLog = new SystemTraceLog(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_JPMORGAN, null,
					resultJsonString, null, SystemTraceLog.INFO, null, ZhonghangResponseMapSpec.SYSTEM_NAME.JPMORGAN, ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT);
			logger.info(systemTraceLog);

			QueryStatusResult queryStatusResult = JsonUtils.parse(resultJsonString, QueryStatusResult.class);
			
			if (queryStatusResult != null) {

				DeductApplicationSqlModel sqlModel = deductApplicationService.getDeductApplicationSqlModelByDeducApplicationUuid(queryStatusResult.getBatchUuid());
				if(sqlModel == null || sqlModel.isSuccessOrFailedOrAbandon()) {
					systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_JPMORGAN_ERROR);
					systemTraceLog.setErrorMsg("cant't find the deductApplication or deductApplication isfinshed");
					logger.info(systemTraceLog);
					return jsonViewResolver.errorJsonResult(ApiResponseCode.WRONG_FORMAT,"cant't find the deductApplication or deductApplication isfinshed");
				}

				String eventKey = "deductApplicationUuid:" + sqlModel.getDeductApplicationUuid() + "#batchDeductApplicationUuid:" + sqlModel.getBatchDeductApplicationUuid();
				systemTraceLog.setEventKey(eventKey);
				systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_JPMORGAN_START);
				logger.info(systemTraceLog);

				updateStatusInDb(queryStatusResult, sqlModel.getDeductApplicationUuid(),sqlModel.getBatchDeductApplicationUuid());

				String oppositeKeyWord = "[batchDeductApplicationUuid="+sqlModel.getBatchDeductApplicationUuid()+",deductApplicationUuid="+sqlModel.getDeductApplicationUuid()+"]";

				systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_JPMORGAN_END);
				logger.info(systemTraceLog);

				long end = System.currentTimeMillis();
				logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.REC_BATCH_DEDUCT_CALLBACK_FROM_JPMORGAN + oppositeKeyWord + "[END,use:"+(end-start)+"ms" );

				return jsonViewResolver.sucJsonResult();
			}
			
			return jsonViewResolver.errorJsonResult(ApiResponseCode.WRONG_FORMAT,"json parse error,with resultJsonString["+resultJsonString+"]");	
			
			
		} catch (Exception e) {
			e.printStackTrace();

			systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_JPMORGAN_ERROR);
			systemTraceLog.setErrorMsg("System error!");
			logger.error(systemTraceLog);

			return jsonViewResolver.errorJsonResult(e);
		}
	}


	private void  updateStatusInDb(QueryStatusResult queryStatusResult,
								   String deductApplicationUuid,String batchDeductApplicationUuid) {

		batchDeductApplicationBusinessHandler.updateBatchDeductApplicationAccordingPartDeductApplication(batchDeductApplicationUuid);

		deductPlanHandler.updateDeductPlanStatusByQueryResultV2(deductApplicationUuid, queryStatusResult);

		DeductApplicationExecutionStatus executionStatus = deductApplicationBusinessHandler.updateDeductApplicationAccordingPartDeductPlan(deductApplicationUuid);

		if (executionStatus == DeductApplicationExecutionStatus.SUCCESS || executionStatus == DeductApplicationExecutionStatus.FAIL || executionStatus == DeductApplicationExecutionStatus.ABANDON ){

			deductApplicationBusinessHandler.pushJobToCitigroupModifyAsseset(deductApplicationUuid);

			deductApplicationBusinessHandler.execSingleNotifyFordeductApplication(deductApplicationUuid);
		}
	}

}
