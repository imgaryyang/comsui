package com.suidifu.bridgewater.handler.impl.batch.v2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.handler.batch.v2.SingleDeductDelegateHandler;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.PriorityEnum;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.wellsfargo.dictionary.handler.DictionaryHandler;

/**
 * @author wukai
 *
 */
@Component("singleDeductDelegateHandler")
public class SingleDeductDelegateHandlerImpl implements SingleDeductDelegateHandler {
	
	@Autowired
	private DictionaryHandler dictionaryHandler;
	
	@Value("#{config['zhonghang.singlededucturl']}")
	private String requestUrl;

	@Override
	public void pushDeductJob(DeductNotifyJobServer deductNotifyJobServer,BatchDeductApplication batchDeductApplication,BatchDeductItem batchDeductItem, String merId, String secret,String groupName){
		
		NotifyApplication job = buildNotifyApplication(batchDeductApplication,batchDeductItem,batchDeductApplication.getNotifyUrl(),merId,secret,groupName);
		
		deductNotifyJobServer.pushJob(job);
	}
	
	private NotifyApplication buildNotifyApplication(BatchDeductApplication batchDeductApplication,BatchDeductItem batchDeductItem,String notifyUrl,String merId,String secret,String groupName){
		
		HashMap<String,String> workParams  = new HashMap<String,String>();
		
		workParams.put("batchDeductApplicationUuid",batchDeductApplication.getBatchDeductApplicationUuid());
		
		HashMap<String,String> requestParams = buildRequestParameters(batchDeductApplication,batchDeductItem, notifyUrl);
		
		NotifyApplication notifyApplication = new NotifyApplication();
		notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());
		notifyApplication.setBusinessId(batchDeductItem.getDeductId());
		notifyApplication.setRequestParameters(requestParams);
		notifyApplication.setRedundanceMap(workParams);
		notifyApplication.setHeadParameters(buildRequestHeader(merId, secret, requestParams));
		notifyApplication.setRequestUrl(requestUrl);
		notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);
		notifyApplication.setDelaySecond(0);
		notifyApplication.setRetryTimes(0);
		notifyApplication.setPriority(PriorityEnum.FIRST);
		notifyApplication.setGroupName(groupName);
//		notifyApplication.setRetryIntervals(new HashMap<Integer, Long>() {
//			{
//				put(1, 1000L);
//				put(2, 30000L);
//			}
//		});
		return notifyApplication;
	}
	
	private HashMap<String,String> buildRequestParameters(BatchDeductApplication batchDeductApplication,BatchDeductItem batchDeductItem,String notifyUrl){
		
		HashMap<String, String> requestParams = new HashMap<String, String>();
		
		requestParams.put("fn", "300001");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("notifyUrl", notifyUrl);
		requestParams.put("apiCalledTime", DateUtils.format(new Date(), "yyyy-MM-dd"));
		requestParams.put("batchDeductId", batchDeductApplication.getBatchDeductId());
		requestParams.put("batchDeductApplicationUuid", batchDeductApplication.getBatchDeductApplicationUuid());
		
		requestParams.putAll(batchDeductItem.toRequestParameters());
		
		return filterValueIsNull(requestParams);
	}
	/**
	 * 过滤掉value为null的map的key－value，因为使用json转换的时候，会丢弃key，导致验签的时候key不见了，验签不过！
	 * @param requestParams
	 * @return
	 */
	private HashMap<String,String> filterValueIsNull(HashMap<String, String> requestParams){
		
		HashMap<String,String> result = new HashMap<String,String>();
		
		for (String key : requestParams.keySet()) {
			
			if(requestParams.get(key) == null) {
				
				continue;
			}
			result.put(key, requestParams.get(key));
		}
		return result;
	}
	
	private HashMap<String,String> buildRequestHeader(String merId,String secret,HashMap<String, String> requestParams){
		
		HashMap<String,String> header = new HashMap<String,String>();
		
		String privateKey = dictionaryHandler.getPointPrivateKey(DictionaryCode.PLATFORM_PRI_KEY);
		
		header.put("merId", merId);
		header.put("secret", secret);
		
		String content = ApiSignUtils.getSignCheckContent(requestParams);
		
		String sign = ApiSignUtils.rsaSign(content, privateKey);
				
		header.put("sign", sign); 
		
		return header;
	}
	
	

}
