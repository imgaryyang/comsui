package com.zufangbao.earth.yunxin.task;

import com.zufangbao.sun.yunxin.handler.AuditJobNoTransaction;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VirtualAccountTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 资产评估Task
 * 
 * @author louguanyang
 *
 */
@Component
public class AssetTask {

	@Autowired
	private VirtualAccountTask virtualAccountTask;
	@Autowired
	private AuditJobNoTransaction auditJobNoTransaction;
	private static Log logger = LogFactory.getLog(AssetTask.class);
	
	/**
	 * 结束昨日的工作，开始今日的工作
	 */
	public void endYesterdayWorkAndStartTodayWork() {
		//同步超过提前还款日，且未完成的提前还款还款计划 
		//已迁移至消息队列 @link{AssetTaskV2_0}
//		try {
//			processingPrepaymentApplicationAfterFail();
//		} catch (Exception e) {
//			logger.error("#processingPrepaymentApplicationAfterFail occur error.");
//			e.printStackTrace();
//		}
		
		//资产评估生成结算单 
		//已迁移至消息队列 @link{AssetTaskV2_0}
//		assetValuationAndCreateNormalOrder();
		try {
			auditJobNoTransaction.create_yesterday_audit_job_and_finish_past_audit_job();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//账户状态变更
		try {
			virtualAccountTask.updateVirtualAccountStatus();
		} catch (Exception e) {
			logger.error("#updateVirtualAccountStatus occur error.");
			e.printStackTrace();
		}
		
	}
	
//	@Deprecated
//	private void processingPrepaymentApplicationAfterFail() {
//		List<String> repaymentPlanUuids = repaymentPlanHandler.getPrepaymentFailRepaymentPlanUuids();
//		prepaymentHandler.processingPrepaymentApplicationAfterFail(repaymentPlanUuids);
//	}
//	
//	/**
//	 * 资产评估生成结算单
//	 *    1、还款计划状态满足：应收、未清
//	 *    2、还款计划类型满足：开启、提前还款待处理
//	 *    3、还款计划下，当日未生成结算单
//	 *    4、还款计划下，不存在未处理完的线上支付单
//	 */
//	@Deprecated
//	private void assetValuationAndCreateNormalOrder(){
//		logger.info("asset valuation and create normal order begin!");
//		long start = System.currentTimeMillis();
//		
//		Date currentDate = new Date();
//		try {
//			//获取可以生成结算单的还款计划
//			List<Long> assetSetIds = repaymentPlanHandler.get_all_allow_valuation_and_create_normal_order_asset_set_list(currentDate);
//			logger.info(DateUtils.format(currentDate)+" 需要进行评估的还款计划总计（"+ assetSetIds.size() +"）条 ! －ids－" + assetSetIds);
//			
//			//轮询处理单条资产，评估并生成结算单［单事务］
//			loopValuateAndCreateNormalOrder(currentDate, assetSetIds);
//		} catch (Exception e1) {
//			logger.error("获取可生成结算单的还款计划列表，发生异常！");
//			e1.printStackTrace();
//		}
//		
//		long end = System.currentTimeMillis();
//		logger.info("asset valuation and create normal order  end! used ["+(end-start)+"]ms");
//	}
//
//	@Deprecated
//	private void loopValuateAndCreateNormalOrder(Date currentDate, List<Long> assetSetIds) {
//		for (Long assetSetId : assetSetIds) {
//			try {
//				orderHandler.assetValuationAndCreateOrder(assetSetId, currentDate);
//			} catch (Exception e) {
//				logger.error("资产id［"+ assetSetId +"］，评估｜生成结算单失败，发生异常！");
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void debitAuditJobCount() {
		try {
			auditJobNoTransaction.redoDebitAuditJob();;
		} catch(Exception e){
			e.printStackTrace();
			logger.info("debitAuditJobCount# redo yesterday TotalReceivableBills count error");
		}
	}
	
}
