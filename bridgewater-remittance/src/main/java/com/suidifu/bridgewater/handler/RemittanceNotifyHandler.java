package com.suidifu.bridgewater.handler;

import com.zufangbao.sun.api.model.remittance.RemittanceProjectName;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;

public interface RemittanceNotifyHandler {

	void processingRemittanceCallback(String remittanceApplicationUuid);

	void pushApplicationsToOutlier(RemittanceSqlModel remittanceSqlMode,
	                               String financialContractUuidOrGroupName);

	boolean validateRemittanceBlackList(boolean isSuccess, String contractUniqueId);

	RemittanceProjectName getRemittanceProjectName();
}
