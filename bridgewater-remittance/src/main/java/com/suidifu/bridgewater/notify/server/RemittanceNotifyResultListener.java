package com.suidifu.bridgewater.notify.server;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import com.zufangbao.sun.utils.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 放款回调结果监听
 *
 * @author wsh
 */
@Component("remittanceNotifyResultListener")
public class RemittanceNotifyResultListener implements NotifyResultListener {
	/**
	 * 实际回调结果处理接口
	 */
	private NotifyResultDelegateHandler notifyResultDelegateHandler;

	private static Log logger = LogFactory.getLog(RemittanceNotifyResultListener.class);

	@Autowired
	private ConfigHandler configHandler;

	private Map<String, String> groupNameAndResultHandlerMapping = null;

	@PostConstruct
	public void init() {
		groupNameAndResultHandlerMapping = new HashMap<String, String>();
		Map<String, Integer> gorupMap = configHandler.getNotifyServerGroupConfig();
		for (Map.Entry<String, Integer> group : gorupMap.entrySet()) {
			String groupName = group.getKey();
			if (groupName.endsWith("PushToOutlier")) {
				groupNameAndResultHandlerMapping.put(groupName, "remittanceOnResultOutlierDelegateNoSession");
			}
		}

	}

	@Override
	public void onResult(NotifyJob result) {
		String groupName = result.getGroupName();
		logger.info("groupName:" + groupName);
		String resultDelegateHandlerBeanName = groupNameAndResultHandlerMapping.get(groupName);
		logger.info("resultDelegateHandlerBeanName:" + resultDelegateHandlerBeanName + ",groupNameAndResultHandlerMapping in json" + JsonUtils.toJsonString
				(groupNameAndResultHandlerMapping));
		notifyResultDelegateHandler = SpringContextUtil.getBean(resultDelegateHandlerBeanName);
		notifyResultDelegateHandler.onResult(result);
	}

}









