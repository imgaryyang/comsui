package com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2;

import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import com.zufangbao.sun.utils.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wukai
 *
 */
/**
 * 扣款和签约的监听器
 * @author wukai
 *
 */
@Component("deductNotifyResultListener")
public class DeductNotifyResultListener implements NotifyResultListener {

	private static final Log logger = LogFactory.getLog(DeductNotifyResultListener.class);
	
	private NotifyResultDelegateHandler notifyResultDelegateHandler;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group1']}")
    private String groupNameForDeductBusinessAcceptance;

    @Value("#{config['notifyserver.groupCacheJobQueueMap_group0']}")
    private String groupNameForSignup;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group2']}")
	private String groupNameForChannelsSignUp;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group6']}")
	private String groupNameForRemittanceBusiness;
	
	private Map<String,String> groupNameAndResultHandlerMapping = null;
	
	@PostConstruct
	public void init(){
		
		groupNameAndResultHandlerMapping = new HashMap<String,String>(){
			{
				put(groupNameForSignup,"querySignUpNotifyResultDelegateHandler");
				put(groupNameForDeductBusinessAcceptance,"deductBusinessAcceptanceDelegateHandler");
				put(groupNameForChannelsSignUp,"channelsSignUpDelegateHandler");
				put(groupNameForRemittanceBusiness, "remittanceBusinessAcceptanceDelerfateHandler");
			}
		};
		
	}
	@Override
	public void onResult(NotifyJob result) {
		
		String groupName = result.getGroupName();
		
		String resultDelegateHandlerBeanName = groupNameAndResultHandlerMapping.get(groupName);
		
		notifyResultDelegateHandler = SpringContextUtil.getBean(resultDelegateHandlerBeanName);
		
		notifyResultDelegateHandler.onResult(result);
		
	}
}
