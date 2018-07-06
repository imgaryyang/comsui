package com.zufangbao.earth.zhonghang.api.controller.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.handler.deduct.v2.DeductBusinessHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
		
		try{
			
			DeductRequestModel deductRequestModel = new  DeductRequestModel();
			
			String deductRequestModelJson = request.getParameter(ZhonghangResponseMapSpec.DEDUCTREQUESTMODEL);
			
			Map<String,String> deductRequestModelMap = JSON.parseObject(deductRequestModelJson,new TypeReference<Map<String,String>>(){});
			
			BeanUtils.populate(deductRequestModel, deductRequestModelMap);
			
			String request_url = request.getRequestURL().toString();
			
			String[] split = request_url.split(BUSINESS_ACCEPTANCE);
			
			String preProcessUrl = split[1];
		
			LOG.info("#handleDeductBusiness# begin to handleDeductBusiness deductRequestModel["+JsonUtils.toJsonString(deductRequestModel)+"]");
			
			deductBusinessHandler.handleDeductBusiness(preProcessUrl,deductRequestModel);
			
			LOG.info("#handleDeductBusiness# end to handleDeductBusiness deductRequestModel["+JsonUtils.toJsonString(deductRequestModel)+"]");
			
			data.put(ZhonghangResponseMapSpec.RECEIVESTATUS,true);
			
			
		}catch(Exception e){
			
			LOG.error("#handleDeductBusiness# occur exception with deductRequestModel["+ request.getParameterMap()+"],stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
			
			data.put(ZhonghangResponseMapSpec.RECEIVESTATUS,false);
			
		}finally{
			
			return jsonViewResolver.sucJsonResult(data);
		}
	}

}