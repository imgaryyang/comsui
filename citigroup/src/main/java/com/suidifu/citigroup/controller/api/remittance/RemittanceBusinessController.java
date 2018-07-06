package com.suidifu.citigroup.controller.api.remittance;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.suidifu.citigroup.handler.RemittanceBusinessHandler;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

//@Controller
//@RequestMapping(value = RemittanceBusinessController.REMITTANCE_INNER_CONTROLLER)
@Component("citigroupReceiveRemittance")
public class RemittanceBusinessController {

	
	@Autowired
	private RemittanceBusinessHandler remittanceBusinessHandler;
	
	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	
	public  static final  String REMITTANCE_INNER_CONTROLLER="/inner-api/acception/";
	
	private static Log LOG = LogFactory.getLog(RemittanceBusinessController.class);
	
	/*@RequestMapping(value = "{system-code}/{function-code}/{product-code}",method=RequestMethod.POST)
	@ApiOperation(value = "放款业务接口", notes = "放款业务接口")
    @ApiImplicitParams({
    		 @ApiImplicitParam(name = "system-code", value = "渠道代码", paramType = "path", dataType = "String",defaultValue = "remittance"),
            @ApiImplicitParam(name = "function-code", value = "渠道代码", paramType = "path", dataType = "String",defaultValue = "business-validation"),
            @ApiImplicitParam(name = "product-code", value = "服务代码", paramType = "path", dataType = "String",defaultValue = "yunxin"),         
    }
    )
	@ResponseBody*/
	
	public  String  handleRemittanceBusiness(String paramsJson){
		
		Map<String,Object> data = new HashMap<>();
		
		String ip = null;
		String eventKey = null;
		
		SystemTraceLog systemTraceLog =new SystemTraceLog();
		try {
			
			Map<String, String> receiveParams = JSONObject.parseObject(paramsJson,
					new TypeReference<Map<String, String>>() {
					});

			RemittanceResponsePacket packet = JsonUtils.parse(
					receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);
			
			
//			String remittanctRequestJson = receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL);
			RemittanceCommandModel remittanceCommandModel  = packet.getRemittanceCommandModel();
//			String request_url = receiveParams.get("validateUrl");
//			
//			String[] split = request_url.split(REMITTANCE_INNER_CONTROLLER);
			
			String preProcessUrl = packet.getBusinessType();
			
			eventKey = "checkRequestNo:"+remittanceCommandModel.getCheckRequestNo() + "&remittanceApplicationUuid:" + remittanceCommandModel.getRemittanceApplicationUuid();
			
			systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_START,
					eventKey, null, null,
					SystemTraceLog.INFO, ip, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			
			LOG.info(systemTraceLog);
			
			remittanceBusinessHandler.handleRemittanceBusiness(preProcessUrl,remittanceCommandModel, ip, packet.getCallBackUrl());
			
			systemTraceLog.setEventName(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_END);
			
			LOG.info(systemTraceLog);
			
			data.put(ZhonghangResponseMapSpec.RECEIVESTATUS,true);
			
			
			
		} catch (Exception e) {
			
			systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST,
					eventKey, null, e.getMessage(),
					SystemTraceLog.ERROR, ip, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			
			LOG.error(systemTraceLog);
			
			data.put(ZhonghangResponseMapSpec.RECEIVESTATUS,false);
			
		}finally {
			
			return jsonViewResolver.sucJsonResult(data);
			
		}
		
	}
}
