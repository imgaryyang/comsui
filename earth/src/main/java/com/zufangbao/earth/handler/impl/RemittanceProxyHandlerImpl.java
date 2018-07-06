package com.zufangbao.earth.handler.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.handler.RemittanceProxyHandler;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("remittanceProxyHandler")
public class RemittanceProxyHandlerImpl implements RemittanceProxyHandler {
	
	@Value("#{config['remittanceHost']}")
	private String remittanceHost;

	@Autowired
	public JsonViewResolver jsonViewResolver;
	
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	
	/* (non-Javadoc)
	 * @see com.zufangbao.earth.handler.RemittanceProxyHandler#processingRevokeRemittancePlan(java.lang.String, java.lang.String)
	 */
	@Override
	public String processingRevokeRemittancePlan(Principal principal, HttpServletRequest request, String remittancePlanUuid, String comment) {
		
		Map<String, String> parameters = new HashMap<>();
		
		parameters.put("remittancePlanUuid", remittancePlanUuid);
		// TODO Auto-generated method stub
		Result remittanceResult =  HttpClientUtils.executePostRequest(remittanceHost+"/remittance-proxy/resend", parameters , new HashMap<String,String>());
		
		if(StringUtils.equals(String.valueOf(GlobalCodeSpec.CODE_SUC), remittanceResult.getCode())) {
			
			String responsePacket = (String)remittanceResult.get("responsePacket");
			
			Result result = JsonUtils.parse(responsePacket, Result.class);
			
			String execReqNo = (String)result.get("execReqNo");
			
			recordSystemOperateLogForResendRemittance(principal, request, remittancePlanUuid, execReqNo, comment);
			
			//做下判断
			
			return jsonViewResolver.sucJsonResult();
			
		}else {
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}

	/* (non-Javadoc)
	 * @see com.zufangbao.earth.handler.RemittanceProxyHandler#recordSystemOperateLogForResendRemittance(com.zufangbao.earth.entity.security.Principal, javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String)
	 */
	private void recordSystemOperateLogForResendRemittance(Principal principal, HttpServletRequest request,
			String remittancePlanUuid, String execReqNo, String comment) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(remittancePlanUuid)) {
			return;
		}
		SystemOperateLog log = new SystemOperateLog();
		log.setUserId(principal.getId());
		log.setIp(IpUtil.getIpAddress(request));
		log.setObjectUuid(remittancePlanUuid);
		log.setLogFunctionType(LogFunctionType.RESEND_REMITTANCE);
		log.setLogOperateType(LogOperateType.OPERATE);
		log.setKeyContent(remittancePlanUuid);
		log.setRecordContent("重新执行放款单，线上代付单号【" + execReqNo + "】" + "，备注：重新执行放款的原因是：【 " + comment + "】");
		log.setOccurTime(new Date());
		systemOperateLogService.save(log);
	}

	/* (non-Javadoc)
	 * @see com.zufangbao.earth.handler.RemittanceProxyHandler#processingRevokeRemittancePlan(java.lang.String, java.lang.String)
	 */
	@Override
	public String processingRevokeUpdateRemittanceInfo(String remittancePlanUuid) {
		
		Map<String, String> parameters = new HashMap<>();
		
		parameters.put("remittancePlanUuid", remittancePlanUuid);
		
		Result remittanceResult =  HttpClientUtils.executePostRequest(remittanceHost+"/remittance-proxy/refund", parameters , new HashMap<String,String>());
		
		if(StringUtils.equals(String.valueOf(GlobalCodeSpec.CODE_SUC), remittanceResult.getCode())) {
			
			String responsePacket = (String)remittanceResult.get("responsePacket");
			
			Result result = JsonUtils.parse(responsePacket, Result.class);
			
			String successMessage = (String)result.get("successMessage");
			
			return successMessage;
			
		}else {
			throw new RuntimeException("Remittance处理失败");
		}
	}
	
	@Override
	public String notifyOutlier(String remittanceApplicationUuid) {

		Map<String, String> parameters = new HashMap<>();

		parameters.put("remittanceApplicationUuids", JsonUtils.toJsonString(Arrays.asList(remittanceApplicationUuid)));
		// TODO Auto-generated method stub
		Result remittanceResult =  HttpClientUtils.executePostRequest(remittanceHost+"/common/notifyOutlier", parameters , new HashMap<String,String>());

		if(StringUtils.equals(String.valueOf(GlobalCodeSpec.CODE_SUC), remittanceResult.getCode())) {

			return jsonViewResolver.sucJsonResult("message","回调外部任务创建成功");

		}else {
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE,"回调外部失败");
		}
	}
}
