package com.suidifu.bridgewater.controller.api.deduct.batch.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.handler.DeductApplicationDetailHandler;
import com.suidifu.bridgewater.handler.single.v2.BatchDeductApplicationBusinessHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductApplicationDetailInfoModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductRequestLogService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationSqlModel;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductRequestLog;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.DeductPlanApplicationCheckLog;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.ProcessDeductResultFailType;
import com.suidifu.bridgewater.api.service.batch.v2.BatchDeductApplicationService;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanApplicationCheckLogService;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanCacheService;
import com.suidifu.bridgewater.api.util.BatchDeductItemHelper;
import com.suidifu.bridgewater.api.util.DeductPlanApplicationCheckLogHelper;
import com.suidifu.bridgewater.handler.impl.single.v2.DeliverDeductCommandHandler;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductPlanModel;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_standard_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.handler.DeductPlanCoreOperationHandler;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;

@Controller
@RequestMapping("/citigroup/callBack")
public class CitigroupCallBackController {
	
	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	
	private static Log logger = LogFactory.getLog(CitigroupCallBackController.class);

	@Autowired
	@Qualifier("standardDeliverDeductCommandHandler")
	private DeliverDeductCommandHandler deliverDeductCommandProcessor;
	
	@Autowired
	private DeductApplicationService DeductApplicationService;
	
	@Autowired 
	private DeductPlanApplicationCheckLogService deductPlanApplicationCheckLogService;
	
	@Autowired 
	private BatchDeductApplicationService BatchDeductApplicationService;
	
	@Autowired
	private DeductPlanCoreOperationHandler deductPlanCoreOperationHandler;
	
	@Autowired
	private DeductPlanCacheService deductPlanCacheService;

	@Autowired
	BatchDeductApplicationBusinessHandler batchDeductApplicationBusinessHandler;

	@Autowired
	DeductApplicationBusinessHandler deductApplicationBusinessHandler;

	@Autowired
	DeductRequestLogService deductRequestLogService;

	@Autowired
	DeductApplicationDetailHandler deductAppDetailHandler;

	@Autowired
	private DeductApplicationService deductApplicationService;
	
	@RequestMapping(value = "/deduct", method=RequestMethod.POST)
	@ResponseBody
	public String doReturnInfo(HttpServletRequest request, HttpServletResponse response) {
		SystemTraceLog systemTraceLog = new SystemTraceLog();
		String deductApplicationUuid = null;
		try {
			long start = System.currentTimeMillis();
			String responsePackage = request.getParameter(HttpClientUtils.DATA_RESPONSE_PACKET);

			Map<String, String> paramMap = JSON.parseObject(responsePackage,new TypeReference<Map<String, String>>(){});

			if (!MapUtils.isEmpty(paramMap)) {
				String failMessage = (String)paramMap.get(ZhonghangResponseMapSpec.FAIL_MESSAGE);
				String batchDeductApplicationUuid = (String)paramMap.get(ZhonghangResponseMapSpec.BATCH_DEDUCT_APPLICATION_UUID);
				deductApplicationUuid = (String)paramMap.get(ZhonghangResponseMapSpec.DEDUCT_APPLICATION_UUID);
				String checkResponseNo = (String)paramMap.get(ZhonghangResponseMapSpec.CHECK_RESPONSE_NO);
				String repaymentCodes = (String)paramMap.get(ZhonghangResponseMapSpec.REPAYMENT_CODES);
				String financialContractUuid = (String)paramMap.get(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT_UUID)==null?"not financialContractUuid":(String)paramMap.get(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT_UUID);
				String requestNo = paramMap.get(ZhonghangResponseMapSpec.REQUEST_NO);
				String contractNo = paramMap.get(ZhonghangResponseMapSpec.CONTRACT_NO);
				String uniqueId = paramMap.get(ZhonghangResponseMapSpec.UNIQUE_ID);
				String customerName = paramMap.get(ZhonghangResponseMapSpec.CUSTOMER_NAME);

				String eventKey = "requestNo:"+ requestNo +"#deductApplicationUuid:"+deductApplicationUuid;
				systemTraceLog = new SystemTraceLog(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_CITIGROUP, eventKey,
						responsePackage, failMessage, SystemTraceLog.INFO, null, ZhonghangResponseMapSpec.SYSTEM_NAME.CITIGROUP, ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT);
				logger.info(systemTraceLog);

				DeductApplicationSqlModel sqlModel = deductApplicationService.getDeductApplicationSqlModelByDeducApplicationUuid(deductApplicationUuid);
				if (sqlModel == null || !checkResponseNo.equals(sqlModel.getCheckResponseNo()) || sqlModel.isReceiveSuccessOrFail()){
					systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_CITIGROUP_ERROR);
					systemTraceLog.setErrorMsg("扣款并发错误！");
					logger.info(systemTraceLog);
					return jsonViewResolver.errorJsonResult(ApiResponseCode.SYSTEM_ERROR,"扣款并发错误！ [" + responsePackage + "]");
				}

				int actualDeductPlanSize = Integer.parseInt(paramMap.get(ZhonghangResponseMapSpec.DEDUCT_PLAN_SIZE));
				Boolean isSuccess = JsonUtils.parse(paramMap.get(ZhonghangResponseMapSpec.IS_SUCCESS),Boolean.class);
				systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_CITIGROUP_START);
				systemTraceLog.setUpStreamSystem(ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT);
				systemTraceLog.setDownStreamSystem(ZhonghangResponseMapSpec.SYSTEM_NAME.JPMORGAN);
				logger.info(systemTraceLog);
				if (!isSuccess) {
//					
//					BatchDeductItem batchDeductItem = new BatchDeductItem();
//					
//					BatchDeductApplication batchDeductApplication = BatchDeductApplicationService.getBatchDeductApplicationBy(batchDeductApplicationUuid);
//					DeductRequestLog deductRequestLog = deductRequestLogService.getDeductRequestLogByRequestNo(requestNo);
//					if (null!=deductRequestLog) {
//						batchDeductItem = BatchDeductItemHelper.buildBatchDeductItemByModel(deductRequestLog, deductApplicationUuid);
//					}
//					
//					if (null != batchDeductApplication) {
//				
////					DeductPlanApplicationCheckLog deductPlanApplicationCheckLog = 	DeductPlanApplicationCheckLogHelper.buildErrorDeductPlanApplicationCheckLog(batchDeductApplication, batchDeductItem, failMessage, ProcessDeductResultFailType.VALIDATE_FAIL);
//
////					batchDeductApplicationBusinessHandler.updateBatchDeductApplicationSetCountFail(batchDeductApplication.getBatchDeductApplicationUuid(), 1);
//					
////					deductPlanApplicationCheckLogService.save(deductPlanApplicationCheckLog);
//					
//					}				
					deductApplicationBusinessHandler.updateDeductApplicationByStatus(deductApplicationUuid, DeductApplicationExecutionStatus.FAIL,ProcessDeductResultFailType.VALIDATE_FAIL.getChineseMessage()+"["+failMessage+"]",
							DeductApplicationReceiveStatus.FAIL, financialContractUuid, TransactionRecipient.LOCAL, new ArrayList<>(), repaymentCodes, contractNo, uniqueId, customerName);
				
					return jsonViewResolver.sucJsonResult();
				
				}

				logger.info("updateBatchDeductApplicationByUuidactualDeductPlanSize["+actualDeductPlanSize+"]");
				
				batchDeductApplicationBusinessHandler.updateBatchDeductApplicationByUuid(batchDeductApplicationUuid, actualDeductPlanSize);

				Map<String, DeductApplicationDetailInfoModel> deductApplicationDetailInfoMap = JSON.parseObject(paramMap.get(ZhonghangResponseMapSpec.DEDUCT_APPLICATION_DETAIL_INFO_MAP), new TypeReference<Map<String, DeductApplicationDetailInfoModel>>(){});
				deductAppDetailHandler.updateDeductApplicationDetailByAsseSet(deductApplicationDetailInfoMap);

				List<DeductPlanModel> deductPlanModels = JSON.parseArray(paramMap.get(ZhonghangResponseMapSpec.DEDUCT_PLAN_MODEL_LIST),DeductPlanModel.class);

				if (!CollectionUtils.isEmpty(deductPlanModels)) {
				
					deductPlanCoreOperationHandler.saveDeductPlanByDeductPlanModelList(deductPlanModels);
					
					List<String> deductPlanUuids = deductPlanModels.stream().map(a->a.getDeductPlanUuid()).collect(Collectors.toList());

					if(StringUtils.isNotEmpty(batchDeductApplicationUuid)) {
						deductPlanCacheService.pushDeductPlanUuidsToCache(batchDeductApplicationUuid, deductPlanUuids);
					}
					DeductPlanModel model = deductPlanModels.get(0);

					String localKeyWord="BRIDGEWATER[requestNo="+model.getRequestNo()+";deductApplicationUuid="+deductApplicationUuid+
							"]";

					List<TradeSchedule> tradeSchedules = JSON.parseArray(paramMap.get(ZhonghangResponseMapSpec.TRADE_SCHEDEULE_LIST),TradeSchedule.class);
			
					if (!CollectionUtils.isEmpty(tradeSchedules)) {

						try {
							deliverDeductCommandProcessor.deliverDeductCommandAndUpdateStatus(tradeSchedules, model.getDeductApplicationUuid(), model.getRequestNo(), financialContractUuid, repaymentCodes);

							deductApplicationBusinessHandler.updateDeductApplicationByStatus(deductApplicationUuid, DeductApplicationExecutionStatus.PROCESSING, "",
									DeductApplicationReceiveStatus.RECEIVESUCCESS, financialContractUuid, TransactionRecipient.OPPOSITE, deductPlanUuids, repaymentCodes, contractNo, uniqueId, customerName);
						}catch(ApiException e){
							e.printStackTrace();
							logger.error(GloableLogSpec.AuditLogHeaderSpec() +GloableLogSpec.bridgewater_deduct_standard_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+"扣款请求受理失败!");
							deductApplicationBusinessHandler.updateDeductApplicationByStatus(deductApplicationUuid, DeductApplicationExecutionStatus.FAIL, e.getCode()+","+e.getMsg(),
									DeductApplicationReceiveStatus.RECEIVESUCCESS, financialContractUuid, TransactionRecipient.LOCAL, deductPlanUuids, repaymentCodes, contractNo, uniqueId, customerName);
							return jsonViewResolver.errorJsonResult(ApiResponseCode.SYSTEM_ERROR,e.getCode()+","+e.getMsg()+"[responsePackage:" + responsePackage + "]");
						}catch (Exception e) {
							e.printStackTrace();
							logger.error(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_deduct_standard_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN + localKeyWord+"扣款请求受理失败!");
							deductApplicationBusinessHandler.updateDeductApplicationByStatus(deductApplicationUuid, DeductApplicationExecutionStatus.FAIL, "扣款发送对端失败！",
									DeductApplicationReceiveStatus.RECEIVESUCCESS, financialContractUuid, TransactionRecipient.LOCAL, deductPlanUuids, repaymentCodes, contractNo, uniqueId, customerName);
							return jsonViewResolver.errorJsonResult(ApiResponseCode.SYSTEM_ERROR,"扣款发送对端失败！[" + responsePackage + "]");
						}finally {
							deductApplicationBusinessHandler.pushJobToCitigroupModifyAsseset(deductApplicationUuid);
						}
					}
					
				}

				systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_CITIGROUP_END);
				logger.info(systemTraceLog);

				long end = System.currentTimeMillis();
				logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.RECV_TRADE_REQUEST_FROM_CITIGROUP + "deductApplicationUuid,["+deductApplicationUuid+"],[SUCCESS,use:"+(end-start)+"ms]");

				return jsonViewResolver.sucJsonResult();
			}
			
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.RECV_TRADE_REQUEST_FROM_CITIGROUP + "deductApplicationUuid["+deductApplicationUuid+"][SUCCESS] with no data");
			
			return jsonViewResolver.errorJsonResult(ApiResponseCode.WRONG_FORMAT,"json parse error jsonString [" + responsePackage + "]");

		} catch (Exception e) {
			e.printStackTrace();
			
			systemTraceLog.setErrorMsg("System error!");
			systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_CITIGROUP_ERROR);
			logger.error(systemTraceLog);

			return jsonViewResolver.errorJsonResult(ApiResponseCode.SYSTEM_ERROR, "system error!");

		}
	}

}
