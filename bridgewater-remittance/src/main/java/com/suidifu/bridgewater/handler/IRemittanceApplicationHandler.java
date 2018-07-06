package com.suidifu.bridgewater.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.suidifu.bridgewater.RemittanceContext;
import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;


/**
 * 放款申请处理器
 * @author zhanghongbing
 *
 */
public interface IRemittanceApplicationHandler{

	List<TradeSchedule> revokeFailRemittancePlanThenSaveInfo(ModifyRemittanceApplicationModel model, String ip, String operator);
	
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
	 * @param queryStatusDelay TODO
	 * @return
	 */
	public List<String> getRemittanceApplicationUuidsInOppositeProcessing(Date queryStartDate, int limit, Date queryStatusDelay);
	
	
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
	
	//public List<String> getWaitingNoticeRemittanceApplicationBy(String financialContractUuid, int pageSize, Date queryStartDate);
	
	public List<RemittanceApplication> getWaitingNoticeRemittanceApplicationBy(int pageSize, Date queryStartDate);
	
	/**
	 * 执行放款回调
	 * @param remittanceApplicationUuid 放款申请uuid
	 */
	public void execSingleNotifyForRemittanceApplication(String remittanceApplicationUuid);

	/**
	 * 获取放款申请单
	 * @param contractUniqueId 合同uniqueId
	 * @param contractNo 合同编号
	 */
	public List<RemittanceApplication> getRemittanceApplicationsBy(String contractUniqueId, String contractNo);

	String getRemittanceResultsForBatchNotice(RemittanceSqlModel remittanceSqlMode,
			List<String> remittanceApplicationUuids);

	Map<String, String> buildHeaderParamsForNotifyRemittanceResult(String content);

	RemittanceApplication getWaitingNoticeRemittanceApplicationBy(String remittanceApplicationUuid);

	RemittanceApplication getRemittanceApplicationUuidsInOppositeProcessing(String remittanceApplicationUuid);

	RemittanceContext singleAnalysisRemittanceResult(RemittanceContext remittanceContext);

	void deleteAllObjectAndRelationCache(RemittancePlan remittancePlan);
	
	default void deleteAllObjectAndRelationCache(String remittancePlanUuid){};

	List<String> getWaitingNoticeRemittanceApplicationV2(int pageSize,Date queryStartDate, Date notifyOutlierDelay);

	String saveRemittanceInfo(RemittanceCommandModel commandModel, String ip,
			String creatorName);

	List<RemittancePlan> saveRemittanceInfo(RemittanceCommandModel commandModel, List<TradeSchedule> tradeSchedules, FinancialContract financialContract);

	RemittanceContext buildContext(QueryStatusResult queryStatusResult );

	List<String> getRemittancePlanUuidsNotInProcessingBy(String remittanceApplicationUuid);

	/**
	 * 根据信托合同uuid，获取该信托合同下等待通知的放款申请
	 * @param pageSize 回调批次条数
	 * @param queryStartDate 查询起始日
	 * @param notifyOutlierDelay TODO
	 */
	List<String> getWaitingNoticeRemittanceApplicationV3(int pageSize,Date queryStartDate, Date notifyOutlierDelay);

	String checkAndUpdateApplication(String remittanceApplicationUuid, boolean is_success,
			String checkRequestNo,boolean isBusinessValidation, Boolean isNeedDoBalance);

	void updateRemittanceInfoAfterSendFailBy(String remittanceApplicationUuid, String executionRemark);

	void updateRemittanceInfoAfterSendSuccessBy(String remittanceApplicationUuid);

	void deleteAllObjectAndRelationCacheByApplicationUuid(String applicationUuid);

	void updateRemittanceExecuteIfFailed(String remittanceApplicationUuid,boolean notifyStatus);

	void updateRemittanceExecuteIfSuccess(String remittanceApplicationUuid);

	void fillFinancialInfoIntoRemittance(String remittanceApplicationUuid, FinancialContract financialContract);

	void updateRemittanceInfoAfterSendFailForSecondRemittance(String remittanceApplicationUuid, List<String> planUuids, String executionRemark);

}
