package com.zufangbao.earth.yunxin.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zufangbao.earth.handler.MessageHandler;
import com.zufangbao.earth.model.report.UserInfoModel;
import com.zufangbao.earth.service.MessageService;
import com.zufangbao.earth.service.RequestLogService;
import com.zufangbao.earth.util.IpUtil;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.yunxin.entity.Message;
import com.zufangbao.sun.yunxin.entity.MessageSource;

@RestController
@RequestMapping(value="/api/client",method=RequestMethod.POST)
public class ApiClientController extends BaseController{
	
	@Autowired
	private MessageHandler messageHandler;
	@Autowired
	private MessageService messageService;
	@Autowired
	private RequestLogService requestLogService;
	
	
	private static final Log logger = LogFactory.getLog(ApiClientController.class);

	@RequestMapping(value = "/n/users", method = RequestMethod.POST)
	public String registerUserRole(@ModelAttribute UserInfoModel userInfoModel, String publicKey, String sourceUuid,HttpServletRequest request) {
		logger.info("Receive a register message from " + sourceUuid);
		try {
			String requestURI = request.getRequestURI();
			String apiUrl = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;
			String ip = IpUtil.getIpAddress(request);
			
			if (!userInfoModel.checkParams()) {
				return jsonViewResolver.errorJsonResult("请输入格式有效的用户名！");
			}
			if (messageService.isMessagePostedBy(sourceUuid)) {
				return jsonViewResolver.jsonResult("重复注册申请");
			}
			messageHandler.generateMessage(userInfoModel, publicKey,sourceUuid);
			requestLogService.saveRequestLog(sourceUuid,null, MessageSource.CLIENT.ordinal(), ip, apiUrl);
			return jsonViewResolver.jsonResult("注册申请成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Register error [" + sourceUuid + "]");
			return jsonViewResolver.errorJsonResult("注册申请失败！");
		}

	}
	
	@RequestMapping(value="/n/messages",method=RequestMethod.GET)
	public String queryRegisterApplicationResult(@RequestParam(value = "sourceUuid") String sourceUuid,HttpServletRequest request) {
		try {
			String requestURI = request.getRequestURI();
			String apiUrl = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;
			String ip = IpUtil.getIpAddress(request);
			
			logger.info("Receive a query message from " + sourceUuid);
			// IP 限定3min/次
			if(requestLogService.queryTimesIn3Mins(sourceUuid,ip, apiUrl)){
				return jsonViewResolver.errorJsonResult("查询频繁");
			}
			Message message =  messageService.getMessageBySourceUuid(sourceUuid);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result",  message.getResult());
			result.put("feedback", message.getFeedback());
			requestLogService.saveRequestLog(sourceUuid,null, MessageSource.CLIENT.ordinal(), ip, apiUrl);
			return jsonViewResolver.sucJsonResult(result);
		}  catch (Exception e) {
			e.printStackTrace();
			logger.info("Query message occur error [" + sourceUuid + "]");
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	
	
	
}
