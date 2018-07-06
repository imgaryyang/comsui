package com.suidifu.citigroup.servers.notify;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import com.zufangbao.sun.utils.SpringContextUtil;

/**
 * @author wukai
 *
 */
/**
 * 扣款和签约的监听器
 * @author wukai
 *
 */
@Component("morganStanleyNotifyResultListener")
public class MorganStanleyNotifyResultListener implements NotifyResultListener {

	private static final Log logger = LogFactory.getLog(MorganStanleyNotifyResultListener.class);
	
	private NotifyResultDelegateHandler notifyResultDelegateHandler;
	
	@Value("${notifyserver.groupCacheJobQueueMap_group0}")
	private String groupNameForModifyPlan;
	
	@Value("${notifyserver.groupCacheJobQueueMap_group2}")
	private String groupNameForSignup;

	@Value("${notifyserver.groupCacheJobQueueMap_callbackDeduct}")
	private String groupNameForDeductBusiness;
	
	private Map<String,String> groupNameAndResultHandlerMapping = null;
	
	@PostConstruct
	public void init(){
		
		groupNameAndResultHandlerMapping = new HashMap<String,String>(){
			{
				put(groupNameForModifyPlan,"modifyPlanNotifyResultDelegateHandler");
				put(groupNameForSignup,"channelsSignUpDelegateHandler");
				put(groupNameForDeductBusiness, "deductNotifyResultDelegateHandler");
			}
		};
		
	}
	@Override
	public void onResult(NotifyJob result) {
		
		String groupName = result.getGroupName();
		
		logger.info("MorganStanleyNotifyResultListener groupName:["+groupName+"] 对应的名字为：remittanceNotifyResultDelegateHandler");
		
		String resultDelegateHandlerBeanName = groupNameAndResultHandlerMapping.get(groupName);
		
		notifyResultDelegateHandler = SpringContextUtil.getBean(resultDelegateHandlerBeanName);
		
		notifyResultDelegateHandler.onResult(result);
		
	}
}
