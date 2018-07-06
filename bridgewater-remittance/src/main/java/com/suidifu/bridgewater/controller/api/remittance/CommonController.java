package com.suidifu.bridgewater.controller.api.remittance;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.controller.api.remittance.v2.exception.NonFinalStateException;
import com.suidifu.bridgewater.controller.base.v2.BaseApiController;
import com.suidifu.bridgewater.handler.RemittanceNotifyHandler;
import com.suidifu.bridgewater.handler.RemittanceTaskNoSession;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;

@RestController
@RequestMapping("/common")
public class CommonController extends BaseApiController{
	
	@Autowired
	private RemittanceNotifyHandler remittanceNotifyHandler;
	
	@Autowired 
	private RemittanceTaskNoSession remittanceTaskNoSession;
	
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	
	@Autowired
	private JsonViewResolver jsonViewResolver;
	
	@RequestMapping(value = "bussinessValidation")
	public @ResponseBody String bussinessValidation(HttpServletResponse response,
			@RequestParam(value = "remittanceApplicationUuids") String remittanceApplicationUuids) {
		
		List<String> uuids = getUuidList(remittanceApplicationUuids);

		if(CollectionUtils.isEmpty(uuids)) {
			return jsonViewResolver.errorJsonResult("参数有误");
		}
		
		Map<String, Object> errorMessage = loopBussinessValidation(uuids);
		
		if(MapUtils.isEmpty(errorMessage)) {
			return jsonViewResolver.sucJsonResult();
		}else {
			return jsonViewResolver.errorJsonResult(errorMessage);
		}

	}
	
	
	@RequestMapping(value = "queryStatus")
	public @ResponseBody String queryStatus(HttpServletResponse response,
			@RequestParam(value = "remittanceApplicationUuids") String remittanceApplicationUuids) {
		
		List<String> uuids = getUuidList(remittanceApplicationUuids);

		if(CollectionUtils.isEmpty(uuids)) {
			return jsonViewResolver.errorJsonResult("参数有误");
		}
		
		Map<String, Object> message = remittanceTaskNoSession.callBackQueryStatusForRemittance(uuids);
		
		return jsonViewResolver.sucJsonResult(message);
	

	}
	
	@RequestMapping(value = "notifyOutlier")
	public @ResponseBody String notifyOutlier(HttpServletResponse response,
			@RequestParam(value = "remittanceApplicationUuids") String remittanceApplicationUuids) {
		
		List<String> uuids = getUuidList(remittanceApplicationUuids);

		if(CollectionUtils.isEmpty(uuids)) {
			return jsonViewResolver.errorJsonResult("参数有误");
		}
		
		Map<String, Object> errorMessage = loopNotifyOutlier(uuids);
		
		if(MapUtils.isEmpty(errorMessage)) {
			return jsonViewResolver.sucJsonResult();
		}else {
			return jsonViewResolver.errorJsonResult(errorMessage);
		}

	}
	
	private List<String> getUuidList(String remittanceApplicationUuids) {
		List<String> uuids;
		try {
			uuids = JsonUtils.parseArray(remittanceApplicationUuids, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return uuids;
	}
	
	private Map<String, Object> loopNotifyOutlier(List<String> uuids) {
		Map<String, Object> errorMessage = new HashMap<>();
		for (String uuid : uuids) {
			notifyOutlier(uuid,errorMessage);
		}
		return errorMessage;
	}
	
	private void notifyOutlier(String uuid,Map<String, Object> errorMessage) {
		try {
			remittanceNotifyHandler.processingRemittanceCallback(uuid);
			
		} catch (NonFinalStateException e) {
			e.printStackTrace();
			errorMessage.put(uuid,"扣款单应为终态");
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage.put(uuid,"回调外部异常");
		}
	}
	
	private Map<String, Object> loopBussinessValidation(List<String> uuids) {
		Map<String, Object> errorMessage = new HashMap<>();
		for (String uuid : uuids) {
			bussinessValidation(uuid,errorMessage);
		}
		return errorMessage;
	}
	
	private void bussinessValidation(String uuid,Map<String, Object> errorMessage) {
		try {
			RemittanceApplication application = iRemittanceApplicationService.getRemittanceApplicationBy(uuid);
			remittanceTaskNoSession.sendToCitigroupForBussinessValidation(application);

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage.put(uuid,e.getMessage());
		}
	}
	
	
//	@RequestMapping(value = "xxxx")
//	public @ResponseBody String xxxx(@RequestBody String message,HttpServletRequest request,HttpServletResponse response) {
//
//		return jsonViewResolver.sucJsonResult();
//	}
}
