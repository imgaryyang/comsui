package com.suidifu.bridgewater.handler.remittance.v2;

import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;

import java.util.Date;
import java.util.List;

/**
 * 放款申请处理器
 * @author zhanghongbing
 *
 */
public interface IRemittanceApplicationHandler {

	/**
	 * 单笔查询，并处理放款申请
	 * @param remittanceApplicationUuid 放款申请uuid
	 */
	void singleQueryAndProcessingRemittanceResult(String remittanceApplicationUuid);

	String getRemittanceResultForNotification(RemittanceApplication remittanceApplication,
	                                          String remittanceApplicationUuid);

	/**
	 * 查询（对端）处理中的放款申请uuids
	 * @param queryStartDate 查询起始日
	 * @return
	 */
	List<String> getRemittanceApplicationUuidsInOppositeProcessing(Date queryStartDate);

	/**
	 * 查询放款申请下的planUuid
	 * @param remittanceApplicationUuid 放款申请uuid
	 * @return applicationUuid列表
	 */
	List<String> getRemittancePlanUuidsInProcessingBy(String remittanceApplicationUuid);

	/**
	 * 获取等待通知的放款申请
	 */
	List<String> getWaitingNoticeRemittanceApplication();

	/**
	 * 根据信托合同uuid，获取该信托合同下等待通知的放款申请
	 * @param financialContractUuid 信托合同uuid
	 * @param pageSize 回调批次条数
	 * @param queryStartDate 查询起始日
	 */
	List<String> getWaitingNoticeRemittanceApplicationBy(String financialContractUuid, int pageSize,
	                                                     Date queryStartDate);
	/**
	 * 获取放款申请单
	 * @param contractUniqueId 合同uniqueId
	 * @param contractNo 合同编号
	 */
	List<RemittanceApplication> getRemittanceApplicationsBy(String contractUniqueId, String contractNo);

	FinancialContract checkRemittanceInfoByLogic(RemittanceCommandModel commandModel, String ip, String operator);

    List<TradeSchedule> revokeFailRemittancePlanThenSaveInfo(ModifyRemittanceApplicationModel model, String ip,
        String operator);

	List<String> getWaitingNoticeRemittanceApplicationV2(int limit, Date queryStartDate, Date notifyOutlierDelay);
}
