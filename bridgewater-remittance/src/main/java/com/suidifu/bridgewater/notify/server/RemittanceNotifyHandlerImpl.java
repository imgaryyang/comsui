package com.suidifu.bridgewater.notify.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.controller.api.remittance.v2.exception.NonFinalStateException;
import com.suidifu.bridgewater.handler.IRemittanceAsyncNotifyHandler;
import com.suidifu.bridgewater.handler.RemittanceNotifyHandler;
import com.zufangbao.sun.api.model.remittance.RemittanceProjectName;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.RemittanceBlackListService;

@Component("RemittanceNotifyHandlerNoSession")
public class RemittanceNotifyHandlerImpl implements RemittanceNotifyHandler{
	
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	
	@Autowired
	private RemittanceBlackListService remittanceBlackListService;
	
	@Value("#{config['notifyserver.notifyType']}")
	private String projectName;
	
	@Override
	public void processingRemittanceCallback(String remittanceApplicationUuid) {
		RemittanceSqlModel remittanceSqlMode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		pushApplicationsToOutlier(remittanceSqlMode,remittanceSqlMode.getFinancialContractUuid());
	}
	
	@Override
	public void pushApplicationsToOutlier(RemittanceSqlModel remittanceSqlMode,String financialContractUuidOrGroupName) {
		
		RemittanceProjectName notifyTypeEnum = RemittanceProjectName.fromKey(projectName);
		
		valideApplicationExecutionStatus(remittanceSqlMode, notifyTypeEnum);

		IRemittanceAsyncNotifyHandler iRemittanceAsyncNotifyHandler = IRemittanceAsyncNotifyHandler.getNotifyHandler(notifyTypeEnum);

		iRemittanceAsyncNotifyHandler.pushApplicationsToOutlier(remittanceSqlMode,
				financialContractUuidOrGroupName);
	}
	
	/**
	 * 在放款前置校验时已经检验过了，但和放款黑名单功能存在并发问题，于是在发给jp前再校验一次
	 */
	@Override
	public boolean validateRemittanceBlackList(boolean isSuccess, String contractUniqueId) {
		RemittanceProjectName notifyTypeEnum = RemittanceProjectName.fromKey(projectName);
		if (notifyTypeEnum == RemittanceProjectName.YUN_XIN || notifyTypeEnum == RemittanceProjectName.HUA_RUI) {
			if (isSuccess && remittanceBlackListService.existUniqueId(contractUniqueId)) {
				return false;
			}
		}
		return true; 
	}

	private void valideApplicationExecutionStatus(RemittanceSqlModel remittanceSqlMode,
			RemittanceProjectName notifyTypeEnum) {
		ExecutionStatus executionStatus = EnumUtil.fromOrdinal(ExecutionStatus.class,
				remittanceSqlMode.getExecutionStatus());
		if (notifyTypeEnum == RemittanceProjectName.YUN_XIN) {
			if (executionStatus != ExecutionStatus.SUCCESS && executionStatus != ExecutionStatus.FAIL
					&& executionStatus != ExecutionStatus.ABANDON && executionStatus != ExecutionStatus.ABNORMAL) {
				throw new NonFinalStateException();
			}
		} else if (notifyTypeEnum == RemittanceProjectName.ZHONG_HANG) {
			if (executionStatus != ExecutionStatus.SUCCESS && executionStatus != ExecutionStatus.FAIL
					&& executionStatus != ExecutionStatus.ABANDON) {
				throw new NonFinalStateException();
			}
		} else if (notifyTypeEnum == RemittanceProjectName.HUA_RUI) {
			if (executionStatus != ExecutionStatus.SUCCESS && executionStatus != ExecutionStatus.FAIL
					&& executionStatus != ExecutionStatus.ABANDON && executionStatus != ExecutionStatus.ABNORMAL) {
				throw new NonFinalStateException();
			}
		} else {
			throw new RuntimeException("RemittanceNotifyType_transform_error");
		}
	}

	@Override
	public RemittanceProjectName getRemittanceProjectName() {
		return RemittanceProjectName.fromKey(projectName);
	}
}





