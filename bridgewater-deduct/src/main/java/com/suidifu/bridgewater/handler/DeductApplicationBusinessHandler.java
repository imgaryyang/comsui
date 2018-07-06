package com.suidifu.bridgewater.handler;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.demo2do.core.entity.Result;
import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.suidifu.bridgewater.api.model.ReDeductDataPackage;
import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.model.v2.NotifyModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.api.model.deduct.DeductCommandRequestModel;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface DeductApplicationBusinessHandler {

	List<TradeSchedule> checkAndsaveDeductInfoBeforePorcessing(DeductCommandRequestModel commandModel, String ipAddress, String merchantId);

	void processingAndUpdateDeducInfo_NoRollback(List<TradeSchedule> tradeSchedules, String deductApplicationUuid, String requestNo);

	DeductApplication getDeductApplicationAndcheckData(OverdueDeductResultQueryModel queryModel, HttpServletRequest request);

	List<QueryStatusResult> requestAndParsingResponse(String deductApplicationUuid);

	List<String>  getDeductApplicationUuidInProcessing();

	List<String>  getDeductApplicationUuidInProcessingAndSourceTypeNotRepaymentOrder();

	BigDecimal calcDeductSuccessAmount(List<QueryStatusResult> results);

	void updateDeductApplication(String deductApplicationUuid, DeductApplicationExecutionStatus deductApplicationExecutionStatus,
                                 BigDecimal successAmount, String executionRemark);
	
	List<TradeSchedule> handleSingleQueryResult(String deductApplicationUuid);

	ReDeductDataPackage handleSingleQueryResultString(String deductApplicationUuid,
                                                      Result result);

	DeductApplicationExecutionStatus evaluatedDeductApplicationStatusBy(
            List<DeductPlan> deductPlans, ReDeductDataPackage reDeductPackage);

	List<TradeSchedule> saveDeductInfoBeforeProcessing(DeductCommandRequestModel commandModel, String ipAddress,
			String merchantId, Contract contract, FinancialContract financialContract);
	void checkConcurrent(DeductApplication deductApplication, RepaymentDetail repaymentDetail);
	
	
//	新增for v2
	void unlockRepaymentPlans(String deductApplicationUuid);

	void checkRepaymentDetailAmount(DeductCommandRequestModel commandModel, List<RepaymentDetail> repaymentDetails);
	
	void handleSingleQueryResultForProducts(String deductApplicationUuid);
	
	void handleSingleQueryResultStringForProjects(String deductApplicationUuid,
                                                  Result result);
	
	boolean dealCacheDeductPlansAndPushNotifyJob(DeductApplication deductApplication, DeductNotifyJobServer deductSingleCallBackNotifyJobServer, String groupName);
	
	List<String> getWaitingNoticeDeductApplication(String financialContractUuid, int pageSize, Date queryStartDate);

	void updateDeductApplicationNotInporcessingForProjects(String deductApplicationUuid,
                                                           List<QueryStatusResult> queryStatusResults, List<DeductPlan> deductPlans,
                                                           DeductApplicationExecutionStatus deductApplicationExecutionStatus);
	
	DeductApplicationExecutionStatus evaluateDeductApplicationStatusBy(List<DeductPlan> deductPlans);
	
	void doFillInfoByDeductApplicationUuid(String deductApplicationUuid);
	
	boolean sendMessageToEarth(Object deductModel);
	
	public DeductApplication creatDeductAppilcationByDeductRequestModel(DeductRequestModel model, String creatorName, String IpAddress, int planNotifyNumber, int planPushMorganStanleyNumber);
	
	public void pushJobToCitigroup(DeductRequestModel model, DeductNotifyJobServer deductSendEarthNotifyJobServer, String requestUrl, String groupName);
	
	public DeductApplicationExecutionStatus updateDeductApplicationAccordingPartDeductPlan(String deductApplicationUuid);
	
	NotifyModel genNotifyModel(DeductApplication deductApplication);
	
	List<String> getWaitingNoticeDeductApplication(int pageSize, Date queryStartDate);
	
	boolean fillInfoIntoDeductCommandRequestModel(DeductCommandRequestModel commandModel);

	DeductApplicationExecutionStatus doEvaluateFinalStatus(List<DeductApplicationExecutionStatus> statusList);

	void execSingleNotifyFordeductApplication(String deductApplicationUuid);

	void pushJobToCitigroupModifyAsseset(String deductApplicationUuid);

	List<String> getDeductApplicationUuidWaittingToCitigroup();

	void updateDeductApplicationByStatus(String deductApplicationUuid, DeductApplicationExecutionStatus executionStatus, String message,
												DeductApplicationReceiveStatus receiveStatus, String financialContractUuid, TransactionRecipient transactionRecipient,
												List<String> outlierTransactionUuid, String repaymentCodes,String contractNo, String uniqueId, String customerName);

	void updatDeductApplicationToCitigroup(DeductApplication deductApplication, int receiveStatus, String newCheckResponseNo);

	void updatDeductApplicationCheckResponseNo(String deductApplicationUuid, String newCheckResponseNo, String oldCheckResponseNo);

	void saveDeductInfo(DeductRequestModel model, String creatorName, String IpAddress, String postProcessUrl);


}
