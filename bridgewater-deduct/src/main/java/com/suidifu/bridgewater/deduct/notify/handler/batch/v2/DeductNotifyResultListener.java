package com.suidifu.bridgewater.deduct.notify.handler.batch.v2;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import com.zufangbao.sun.utils.SpringContextUtil;

/**
 * @author wukai
 *
 */
/**
 * 单扣回调监听
 * @author wukai
 *
 */
@Component("deductNotifyResultListener")
public class DeductNotifyResultListener implements NotifyResultListener {
	
	private NotifyResultDelegateHandler notifyResultDelegateHandler;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group0']}")
	private String groupNameForBatchDeduct;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group1']}")
	private String groupNameForDeductBusinessAcceptance;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMap_group2']}")
	private String groupNameForDeductSingleCallBackNotify;

	@Value("#{config['notifyserver.groupCacheJobQueueMap_group3']}")
	private String toCitigroupModifyAsseset;
	
	private  Map<String,String> groupNameAndResultHandlerMapping = null;
	
	@PostConstruct
	public void init(){
		
		groupNameAndResultHandlerMapping=new HashMap<String,String>(){
			{
				put(groupNameForBatchDeduct,"batchNotifyResultDelegateHandler");
				put(groupNameForDeductBusinessAcceptance,"sendCitigroupNotifyResultDelegateHandler");
				put(toCitigroupModifyAsseset, "sendCitigroupModifyAsseset");
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
