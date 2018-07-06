package com.suidifu.bridgewater.controller;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.controller.api.deduct.CommandApiDeductController;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;



@Controller
@RequestMapping("/api/command/")
public class DeductApplicationController extends BaseApiController {

	
	private static final Log logger = LogFactory.getLog(DeductApplicationController.class);
	
	@Autowired
	private DeductApplicationService deductApplicationService;
	
	
	@RequestMapping(value = "containProcessingRepaymentplan", method = RequestMethod.POST)
	public  @ResponseBody String getProcessingOrSuccessList(HttpServletRequest request, HttpServletResponse response,String assetUuidsJson){
		try {
			Result result =new Result();
			
			synchronized(CommandApiDeductController.gloableLock)
			{
			List<String>  assetUuidList = JsonUtils.parseArray(assetUuidsJson, String.class);
			if(CollectionUtils.isEmpty(assetUuidList)){
				 result.setCode("2");
				 result.setMessage("参数解析错误！！");
				 return JSON.toJSONString(result);
			}
			for(String assetSetUuid :assetUuidList){
			   List<DeductApplication> deductApplications =   deductApplicationService.get_processing_or_success_list(assetSetUuid);
			   if(!CollectionUtils.isEmpty(deductApplications)){
				   return JSON.toJSONString(result.success());
			   }
			}
			return JSON.toJSONString(result.fail());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Result result =new Result();
			result.setCode("2");
			result.setMessage(e.getMessage());
			logger.error("#getProcessingOrSuccessList occur error ]: " + e.getMessage());
			return JSON.toJSONString(result);
		}
	}
	
	
}
