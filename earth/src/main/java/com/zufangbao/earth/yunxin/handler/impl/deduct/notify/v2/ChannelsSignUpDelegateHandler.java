package com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.yunxin.entity.v2.SignUpCheckLog;
import com.zufangbao.sun.yunxin.service.v2.SignUpCheckLogService;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.yunxin.entity.v2.SignUpCheckLog;
import com.zufangbao.sun.yunxin.service.v2.SignUpCheckLogService;

@Component("channelsSignUpDelegateHandler")
public class ChannelsSignUpDelegateHandler implements NotifyResultDelegateHandler {

	private static final Log logger = LogFactory.getLog(ChannelsSignUpDelegateHandler.class);
	
	@Autowired
	private SignUpCheckLogService signUpCheckLogService;
	
	@Override
	public void onResult(NotifyJob result) {
		
		SignUpCheckLog signUpCheckLog = null; 
		
		Map<String, String> param = new HashMap<>();
		
		try {
			if(!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())) {
				logger.error(".#ChannelsSignUpDelegateHandler 多通道签约失败，贷款合同对应的银行卡号为["+result.getBusinessId()+"] 对应的请求参数为["+JsonUtils.toJsonString(result.getRedundanceMap())+"]");
				
				Map<String, String> params = JsonUtils.parse(result.getRedundanceMap(),Map.class);
				
				if (MapUtils.isNotEmpty(params)) {
					
					param.putAll(params);
					
					param.put("failReason", "签约接口请求不通！ 接口为["+result.getRequestUrl()+"]");
					
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			
			logger.error(".#ChannelsSignUpDelegateHandler occurs error reason is ["+e.getMessage()+"]");
			
		}finally {
			
			if (StringUtils.isNotEmpty(param.get("failReason"))) {
				
				signUpCheckLog = new SignUpCheckLog(param);
				
				signUpCheckLogService.saveOrUpdate(signUpCheckLog);
				
			}
					
		}
		
		
	}

}
