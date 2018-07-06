package com.zufangbao.earth.yunxin.task;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.handler.deduct.SystemDeductHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.BridgewaterDeductHelper;
import com.zufangbao.gluon.api.bridgewater.deduct.model.DeductInfoModel;
import com.zufangbao.gluon.api.bridgewater.model.HeaderInfoModel;
import com.zufangbao.sun.entity.log.SystemDeductLog;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.SystemDeductLogService;
import com.zufangbao.sun.yunxin.entity.OrderSource;
import com.zufangbao.sun.yunxin.exception.RepaymentPlanDeductLockedException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.yunxin.handler.PrepaymentHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;




/**
 * 系统扣款task
 * @author zhanghongbing
 *
 */
@Component("systemDeductTask")
public class SystemDeductTask {
	
	@Autowired
	private SystemDeductLogService systemDeductLogService;
	
	@Autowired
	private SystemDeductHandler systemDeductHandler;
	
	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private BridgewaterDeductHelper bridgewaterDeductHelper;
	
	@Autowired
	private PrepaymentHandler prepaymentHandler;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private RepaymentOrderService repaymentOrderService;
	
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	
	private static Log logger = LogFactory.getLog(SystemDeductTask.class);

	@Value("#{config['sys.deduct.merId']}")
	private String MER_ID = "";
	
	@Value("#{config['sys.deduct.secret']}")
	private String SECRET = "";
	
	/**
	 * 系统扣款—计划还款日，结算单由系统生成
	 */
	public void sysDeductOnPlannedRepaymentDate() {
		String deductPrivateKey = dictionaryService.getPlatformDeductPrivateKey();
		if(StringUtils.isEmpty(deductPrivateKey)) {
			logger.info("#sysDeductOnPlannedRepaymentDate not found deduct private key!");
			return;
		}
		List<Long> orderIds = systemDeductHandler.getTodayWaitingDeductOrderOnPlannedRepaymentDate(OrderSource.SYSTEM);
		logger.info("#sysDeductOnPlannedRepaymentDate waiting process size("+orderIds.size()+")");
		loopSendDeductApplication("#sysDeductOnPlannedRepaymentDate", orderIds, deductPrivateKey);
	}
	
	/**
	 * 系统扣款－计划还款日后（逾期），结算单由系统生成
	 */
	public void sysDeductAfterPlannedRepaymentDate() {
		String deductPrivateKey = dictionaryService.getPlatformDeductPrivateKey();
		if(StringUtils.isEmpty(deductPrivateKey)) {
			logger.info("#sysDeductAfterPlannedRepaymentDate not found deduct private key!");
			return;
		}
		List<Long> orderIds = systemDeductHandler.getTodayWaitingDeductOrderAfterPlannedRepaymentDate(OrderSource.SYSTEM);
		logger.info("#sysDeductAfterPlannedRepaymentDate waiting process size("+orderIds.size()+")");
		loopSendDeductApplication("#sysDeductAfterPlannedRepaymentDate", orderIds, deductPrivateKey);
	}
	
	/**
	 * 系统扣款－提前还款（全额），结算单由系统生成
	 */
	public void sysDeductForPrepaymentApplication() {
		String deductPrivateKey = dictionaryService.getPlatformDeductPrivateKey();
		if(StringUtils.isEmpty(deductPrivateKey)) {
			logger.info("#sysDeductForPrepaymentApplication not found deduct private key!");
			return;
		}
		List<Long> orderIds = systemDeductHandler.getTodayWaitingDeductOrderForPrepaymentApplication(OrderSource.SYSTEM);
		logger.info("#sysDeductForPrepaymentApplication waiting process size("+orderIds.size()+")");
		loopSendDeductApplication("#sysDeductForPrepaymentApplication", orderIds, deductPrivateKey);
	}
	
	/**
	 * 系统扣款－手工拆分的结算单
	 */
	public void sysDeductForManualNormalOrder() {
		String deductPrivateKey = dictionaryService.getPlatformDeductPrivateKey();
		if(StringUtils.isEmpty(deductPrivateKey)) {
			logger.info("#sysDeductForManualNormalOrder not found deduct private key!");
			return;
		}
		List<Long> totalManualOrderIds = systemDeductHandler.getTodayEffectiveUnexecutedManualNormalOrder();
		logger.info("#sysDeductForManualNormalOrder waiting process size("+totalManualOrderIds.size()+")");
		loopSendDeductApplication("#sysDeductForManualNormalOrder", totalManualOrderIds, deductPrivateKey);
	}
	
	private void loopSendDeductApplication(String logPrefix, List<Long> orderIds, String deductPrivateKey) {
		for (Long orderId : orderIds) {
			try {
				long start = System.currentTimeMillis();
				//存在成功或处理中的扣款，即跳过
				boolean isExistDeduct = systemDeductHandler.existSuccessOrProcessingDeductApplication(orderId);
				if(isExistDeduct) {
					logger.info(logPrefix + " singleSendDeductApplication continue, reason[exist success or processing deduct application], orderId["+orderId+"].");
					continue;
				}
				
				DeductInfoModel deductInfoModel = systemDeductHandler.getDeductInfoModelBy(orderId);
				if(deductInfoModel == null) {
					logger.info(logPrefix + " singleSendDeductApplication continue, reason[order already close OR invalid repayment plan], orderId["+orderId+"].");
				}
				
				String content = ApiSignUtils.getSignCheckContent(deductInfoModel.getDeductInfosMap());
				String sign = ApiSignUtils.rsaSign(content, deductPrivateKey);
				
				//通信密钥设定
				HeaderInfoModel headerInfoModel = new HeaderInfoModel(MER_ID, SECRET, sign);
				
				String deductRequestNo = deductInfoModel.getRequestNo();
				String deductId = deductInfoModel.getDeductId();
				
				SystemDeductLog systemDeductLog = new SystemDeductLog(orderId, deductRequestNo, deductId, new Date());
				Serializable logId = systemDeductLogService.save(systemDeductLog);
				
				//扣款交易发起
				Result result = bridgewaterDeductHelper.sendDeductApplication(deductInfoModel, headerInfoModel);
				
				systemDeductHandler.updateDeductResultAfterDeductProcessing(orderId, logId, result);
				
				logger.info(logPrefix + " singleSendDeductApplication success, orderId["+orderId+"], requestNo["+deductInfoModel.getRequestNo()+"], result["+JsonUtils.toJsonString(result)+"], usedTime["+(System.currentTimeMillis() - start)+"].");
			} catch (RepaymentPlanDeductLockedException e1) {
				logger.error(logPrefix + " singleSendDeductApplication continue, reason[repayment plan already locked], orderId["+orderId+"].");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(logPrefix + " singleSendDeductApplication occur error, orderId["+orderId+"].");
			}
		}
	}

	/**
	 * 同步系统扣款状态－结算单
	 */
	public void syncSysDeductStatus() {
		//同步处理中的结算单状态（系统扣款用结算单）
		syncProcessingNormalOrdersForSystemDeduct();
		
		//处理还款成功的提前还款申请
		processingPrepaymentApplicationsAfterSuccess();
		
		//处理提前还款的还款计划
		prepaymentHandler.processingPrepaymentPlan();
	}
	
	/**
	 * 同步处理中的结算单状态
	 */
	private void syncProcessingNormalOrdersForSystemDeduct() {
		List<Long> orderIds = systemDeductHandler.getUnclearAndProcessingNormalOrderListForSystemDeduct();
		logger.info("#syncProcessingNormalOrdersForSystemDeduct waiting process size("+orderIds.size()+")");
		
		for (Long orderId : orderIds) {
			try {
				systemDeductHandler.syncProcessingNormalOrderForSystemDeduct(orderId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("#syncProcessingNormalOrders occur error, orderId["+orderId+"].");
			}
		}
	}
	
	/**
	 * 处理还款成功的提前还款申请
	 */
	private void processingPrepaymentApplicationsAfterSuccess() {
		List<String> repaymentPlanUuids = repaymentPlanService.getPrepaymentSuccessRepaymentPlanUuids();
		logger.info("#processingPrepaymentApplicationsAfterSuccess waiting process size("+repaymentPlanUuids.size()+")");

		for (String repaymentPlanUuid : repaymentPlanUuids) {
			try {
				prepaymentHandler.processingPrepaymentApplicationAfterSuccess(repaymentPlanUuid);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("#processingPrepaymentApplicationsAfterSuccess occur error, repaymentPlanUuid["+repaymentPlanUuid+"].");
			}
		}
	}
	
	/**
	 * 第三方扣款核销
	 */
	public void thirdPartyDeductionAssetRecover() {
//		try {
//			thirdPartyDeductNoSession.loopGenerateVoucherInformation();
//		} catch (Exception e) {
//			logger.error("occur exception when gengerate third part journal voucher");
//			e.printStackTrace();
//		}
//		try {
//			thirdPartyDeductNoSession.recover_assets_online_deduct_by_interface();
//		} catch(Exception e){
//			logger.error("occur exception when third_party_deduction_asset_recover.");
//			e.printStackTrace();
//		}
//		logger.info("end to third_party_deduction_asset_recover!");
	}
	
}
