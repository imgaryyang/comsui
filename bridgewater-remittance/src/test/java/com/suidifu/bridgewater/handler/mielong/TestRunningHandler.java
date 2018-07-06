package com.suidifu.bridgewater.handler.mielong;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.suidifu.bridgewater.RemittanceContext;
import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;

public interface TestRunningHandler {

	/**
	 * 放款计划发送前，保存放款信息
	 * @param commandModel 放款指令模型
	 * @param ip ip地址
	 * @param operator 操作人员
	 */
	public List<TradeSchedule> saveRemittanceInfoBeforeProcessing(RemittanceCommandModel commandModel, String ip, String operator);

	List<TradeSchedule> revokeFailRemittancePlanThenSaveInfo(ModifyRemittanceApplicationModel model, String ip, String operator);
	
	/**
	 * 放款计划发送处理后，更新放款信息
	 * @param tradeSchedules 交易日程
	 * @param remittanceApplicationUuid 放款申请uuid
	 * @param requestNo 放款请求唯一编号
	 */
	public void processingAndUpdateRemittanceInfo_NoRollback(List<TradeSchedule> tradeSchedules, String remittanceApplicationUuid, String requestNo);
	
	/**
	 * 单笔查询，并处理放款申请
	 * @param remittanceApplicationUuid 放款申请uuid
	 */
	public void singleQueryAndProcessingRemittanceResult(String remittanceApplicationUuid);
	
	public String getRemittanceResultForNotification(RemittanceApplication remittanceApplication, String remittanceApplicationUuid);
	
	/**
	 * 查询（对端）处理中的放款申请uuids
	 * @param queryStartDate 查询起始日
	 * @param limit TODO
	 * @return
	 */
	public List<String> getRemittanceApplicationUuidsInOppositeProcessing(Date queryStartDate, int limit);
	
	
	/**
	 * @param limit TODO
	 * 
	 */
	public List<RemittanceApplication> getRemittanceApplicationInNotVerify(int limit);
	
	
	/**
	 * 查询放款申请下的planUuid
	 * @param remittanceApplicationUuid 放款申请uuid
	 * @return applicationUuid列表
	 */
	public List<String> getRemittancePlanUuidsInProcessingBy(String remittanceApplicationUuid);
	
	/**
	 * 获取等待通知的放款申请
	 */
	public List<String> getWaitingNoticeRemittanceApplication();
	
	/**
	 * 根据信托合同uuid，获取该信托合同下等待通知的放款申请
	 * @param financialContractUuid 信托合同uuid
	 * @param pageSize 回调批次条数
	 * @param queryStartDate 查询起始日
	 */
	public List<String> getWaitingNoticeRemittanceApplicationBy(String financialContractUuid, int pageSize, Date queryStartDate);
	
	public List<RemittanceApplication> getWaitingNoticeRemittanceApplicationBy(int pageSize, Date queryStartDate);
	
	/**
	 * 执行放款回调
	 * @param remittanceApplicationUuid 放款申请uuid
	 */
	public void execSingleNotifyForRemittanceApplication(String remittanceApplicationUuid);

	/**
	 * 执行放款回调（批量）
	 * @param remittanceApplicationUuids 放款申请uuids
	 * @param noticeBatchNo TODO
	 */
//	public void execBatchNotifyForRemittanceApplication(List<String> remittanceApplicationUuids, String noticeBatchNo);
	
	/**
	 * 执行放款回调（批量）v2
	 * @param remittanceApplicationUuids 放款申请uuids
	 * @param noticeBatchNo TODO
	 */
	public void execBatchNotifyForRemittanceApplicationV2(List<String> remittanceApplicationUuids, String noticeBatchNo);
	
	/**
	 * 获取放款申请单
	 * @param contractUniqueId 合同uniqueId
	 * @param contractNo 合同编号
	 */
	public List<RemittanceApplication> getRemittanceApplicationsBy(String contractUniqueId, String contractNo);

	String getRemittanceResultsForBatchNotice(List<RemittanceSqlModel> remittanceSqlModes,
			List<String> remittanceApplicationUuids);

	Map<String, String> buildHeaderParamsForNotifyRemittanceResult(String content);

	RemittanceApplication getWaitingNoticeRemittanceApplicationBy(String remittanceApplicationUuid);

	RemittanceApplication getRemittanceApplicationUuidsInOppositeProcessing(String remittanceApplicationUuid);

	RemittanceContext singleAnalysisRemittanceResult(RemittanceContext remittanceContext);

	void deleteAllObjectAndRelationCache(RemittancePlan remittancePlan);
	
	default void deleteAllObjectAndRelationCache(String remittancePlanUuid){};

	List<String> getWaitingNoticeRemittanceApplicationV2(int pageSize,Date queryStartDate);

	String saveRemittanceInfo(RemittanceCommandModel commandModel, String ip,
			String creatorName);

	void saveRemittanceInfo(RemittanceCommandModel commandModel, List<TradeSchedule> tradeSchedules, FinancialContract financialContract);

	String receiveCitiGroupCallback_NoRollback(HttpServletRequest request);

	RemittanceContext buildContext(QueryStatusResult queryStatusResult );

	List<String> getRemittancePlanUuidsNotInProcessingBy(String remittanceApplicationUuid);

}
