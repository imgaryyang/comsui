package com.suidifu.bridgewater.notify.server;

import com.suidifu.bridgewater.handler.IRemittanceAsyncNotifyHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJobServer;
import com.suidifu.swift.notifyserver.notifyserver.PostEntityType;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author wsh
 */
public abstract class RemittanceAsyncNotifyHandler implements IRemittanceAsyncNotifyHandler {

	private static final Log LOGGER = LogFactory.getLog(RemittanceAsyncNotifyHandler.class);

	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;

	@Autowired
	private RemittanceNotifyJobServer remittanceNotifyJobServer;

	@Autowired
	private ConfigHandler configHandler;

	@Value("#{config['notifyserver.notifyType']}")
	private String notifyType;

	public abstract String buildContent(RemittanceSqlModel remittanceSqlMode, String remittanceApplicationUuid);

	public abstract HashMap<String, String> buildHeaderParamsForNotifyRemittanceResult(String content, String financialContractUuid);

	@Override
	public void pushApplicationsToOutlier(RemittanceSqlModel remittanceSqlMode, String financialContractUuid) {
		String groupName = configHandler.getNotifyGroupNameBy(financialContractUuid);
		processingRemittanceCallback(remittanceNotifyJobServer, remittanceSqlMode, groupName);
	}

	private void processingRemittanceCallback(NotifyJobServer notifyJobServer, RemittanceSqlModel remittanceSqlMode, String groupNameForRemittance) {

		NotifyApplication notifyApplication = null;
		try {
			String remittanceApplicationUuid = remittanceSqlMode.getRemittanceApplicationUuid();

			String content = buildContent(remittanceSqlMode, remittanceApplicationUuid);

			HashMap<String, String> headerParams = buildHeaderParamsForNotifyRemittanceResult(content, remittanceSqlMode.getFinancialContractUuid());

			notifyApplication = buildNotifyApplication(remittanceApplicationUuid, groupNameForRemittance, content, headerParams);
		} catch (Exception e) {
			LOGGER.error("构建notifyserver回调上下文失败");
			e.printStackTrace();
		}
		notifyJobServer.pushJob(notifyApplication);
	}

	private NotifyApplication buildNotifyApplication(String remittanceApplicationUuid, String groupNameForRemittance, String content, HashMap<String, String>
			headParameters) throws Exception {
		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
		if (iRemittanceApplicationService == null) {
			return null;
		}
		String notifyUrl = remittanceApplication.getNotifyUrl();

		NotifyApplication notifyApplication = new NotifyApplication();
		if (notifyUrl == null || "".equals(notifyUrl)) {
			throw new Exception("send async query credit error : can not get request url.");
		}

		HashMap<String, String> workParams = new HashMap<String, String>();
		workParams.put("remittanceApplicationUuid", remittanceApplicationUuid);

		HashMap<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("message", content);

		notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);
		notifyApplication.setPostEntityType(PostEntityType.STRING);

		notifyApplication.setRequestUrl(notifyUrl);
		notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());
		notifyApplication.setHeadParameters(headParameters);
		notifyApplication.setRedundanceMap(workParams);
		notifyApplication.setRequestParameters(requestParams);
		notifyApplication.setGroupName(groupNameForRemittance);

		return notifyApplication;
	}

}






