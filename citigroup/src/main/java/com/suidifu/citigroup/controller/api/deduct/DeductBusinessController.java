package com.suidifu.citigroup.controller.api.deduct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.suidifu.citigroup.handler.DeductBusinessHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wukai
 * 扣款业务处理
 */
@Controller
@RequestMapping(value=DeductBusinessController.BUSINESS_ACCEPTANCE)
public class DeductBusinessController {

	@Autowired
	@Qualifier("deductBusinessHandler")
	private DeductBusinessHandler deductBusinessHandler;
	
	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	
	private static Log LOG = LogFactory.getLog(DeductBusinessController.class);

	public static final String BUSINESS_ACCEPTANCE="/inner-api/";
	
	@RequestMapping(value = "{pre_channel-code}/{channel-code}/{service-code}",method=RequestMethod.POST)
	@ApiOperation(value = "扣款业务接口", notes = "扣款业务接口")
    @ApiImplicitParams({
    		 @ApiImplicitParam(name = "pre_channel-code", value = "渠道代码", paramType = "path", dataType = "String",defaultValue = "zhonghang"),
            @ApiImplicitParam(name = "channel-code", value = "渠道代码", paramType = "path", dataType = "String",defaultValue = "zhonghang"),
            @ApiImplicitParam(name = "service-code", value = "服务代码", paramType = "path", dataType = "String",defaultValue = "deduct-business-acceptance"),         
    }
    )
	public @ResponseBody String handleDeductBusiness(HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> data = new HashMap<>();
		long start = System.currentTimeMillis();
		SystemTraceLog systemTraceLog = new SystemTraceLog();
		String requestNo = null;
		String eventKey = null;
		try{
			DeductRequestModel deductRequestModel = new  DeductRequestModel();
			String deductRequestModelJson = request.getParameter(ZhonghangResponseMapSpec.DEDUCTREQUESTMODEL);
			
			Map<String,String> deductRequestModelMap = JSON.parseObject(deductRequestModelJson,new TypeReference<Map<String,String>>(){});
			BeanUtils.populate(deductRequestModel, deductRequestModelMap);

			requestNo = deductRequestModel.getRequestNo();
			eventKey = "requestNo:" + requestNo + "deductApplicationUuid:" + deductRequestModel.getDeductApplicationUuid();
			systemTraceLog = new SystemTraceLog(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_TRADE_FROM_DEDUCT, eventKey,
					deductRequestModelJson, null, SystemTraceLog.INFO, null, ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT, ZhonghangResponseMapSpec.SYSTEM_NAME.CITIGROUP);
			LOG.info(systemTraceLog);

			String request_url = request.getRequestURL().toString();
			String[] split = request_url.split(BUSINESS_ACCEPTANCE);
			String preProcessUrl = split[1];
		
			deductBusinessHandler.handleDeductBusiness(preProcessUrl,deductRequestModel);
			data.put(ZhonghangResponseMapSpec.RECEIVESTATUS,true);

			systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_TRADE_FROM_DEDUCT_END);
			systemTraceLog.setUpStreamSystem(ZhonghangResponseMapSpec.SYSTEM_NAME.CITIGROUP);
			systemTraceLog.setDownStreamSystem(ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT);
			LOG.info(systemTraceLog);

			return jsonViewResolver.sucJsonResult(data);
		}catch(Exception e){
			e.printStackTrace();
			data.put(ZhonghangResponseMapSpec.RECEIVESTATUS,false);
			systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_TRADE_FROM_DEDUCT_ERROR);
			LOG.error(systemTraceLog);

			return jsonViewResolver.errorJsonResult(e);
		}
	}

}