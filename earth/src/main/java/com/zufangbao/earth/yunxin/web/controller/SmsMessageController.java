package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.assetset.SmsMessageQueryModel;
import com.zufangbao.sun.yunxin.entity.sms.SmsQuene;
import com.zufangbao.sun.yunxin.entity.sms.SmsTemplateEnum;
import com.zufangbao.sun.yunxin.entity.sms.model.SmsQueneQueryModel;
import com.zufangbao.sun.yunxin.handler.SmsQueneHandler;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.SmsQueneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("message")
@MenuSetting("menu-message")
public class SmsMessageController {

	@Autowired
	private SmsQueneService smsQueneService;
	@Autowired
	private SmsQueneHandler smsQueneHandler;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	public JsonViewResolver jsonViewResolver;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@MenuSetting("submenu-message-activate")
	public ModelAndView getMessagePage(HttpServletRequest request, Page page) {
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}
	
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public @ResponseBody String getMessagePageOptions(HttpServletRequest request, Page page) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean allowedSendFlag = dictionaryService.getSmsAllowSendFlag();
		result.put("allowedSendFlag", allowedSendFlag);
		result.put("smsTemplateEnumList", EnumUtil.getKVList(SmsTemplateEnum.class));
		return jsonViewResolver.sucJsonResult(result);
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public @ResponseBody String queryMessage(HttpServletRequest request, Page page,
			@ModelAttribute SmsMessageQueryModel smsMessageQueryModel){
		int allowedSendStatus=smsMessageQueryModel.getAllowedSendStatus(); 
		String clientId=smsMessageQueryModel.getClientId(); 
		int smsTemplateEnum=smsMessageQueryModel.getSmsTemplateEnum();
		String startDate=smsMessageQueryModel.getStartDate();
		String endDate=smsMessageQueryModel.getEndDate();
		
		List<SmsQueneQueryModel> smsQueneQueryModelList = smsQueneHandler.querySmsQueneQueryModelList(allowedSendStatus, clientId, smsTemplateEnum, page, startDate, endDate);
		Long size = smsQueneService.countSmsQuene(allowedSendStatus, clientId, smsTemplateEnum, page,startDate,endDate);
		Map<String, Object> data = new HashMap<String, Object>();
		data.putIfAbsent("list", smsQueneQueryModelList);
		data.putIfAbsent("size", size);
		return jsonViewResolver.sucJsonResult(data);
	}

	@RequestMapping(value = "/activate", method = RequestMethod.POST)
	@MenuSetting("submenu-message-activate")
	public @ResponseBody String activateMessage(Page page, Long smsQueneId) {

		Result result = new Result();
		try {
			SmsQuene smsQueue = smsQueneService
					.load(SmsQuene.class, smsQueneId);
			smsQueneHandler.activateSmsQuene(smsQueue);
			result.success().message("激活允许发送成功！！");
		} catch (Exception e) {
			e.printStackTrace();
			result.fail().message("激活失败！！");
			return JsonUtils.toJsonString(result);
		}
		return JsonUtils.toJsonString(result);
	}

	@RequestMapping(value = "/reSend", method = RequestMethod.POST)
	@MenuSetting("submenu-message-activate")
	public @ResponseBody String reSendMessage(Long smsQueneId) {
		Result result = new Result();
		try {
			SmsQuene smsQueue = smsQueneService.load(SmsQuene.class, smsQueneId);
			smsQueneHandler.reSendMessage(smsQueue);
			result.success().message("已添加到短信发送队列！");
		} catch (Exception e) {
			e.printStackTrace();
			result.fail().message("重新发送失败！");
			return JsonUtils.toJsonString(result);
		}
		return JsonUtils.toJsonString(result);
	}

	@RequestMapping(value = "/changeAllowSend", method = RequestMethod.POST)
	@MenuSetting("submenu-message-activate")
	public @ResponseBody String changeAllowSend(boolean allowedSendFlag) {
		Result result = new Result();
		try {
			dictionaryService.updateAllowedSendStatus(allowedSendFlag);
			result.success().message("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.fail().message("操作失败！" + e.getMessage());
			return JsonUtils.toJsonString(result);
		}
		return JsonUtils.toJsonString(result);
	}
	
	@RequestMapping(value = "/sendSuccSms", method = RequestMethod.POST)
	@MenuSetting("submenu-message-activate")
	public @ResponseBody String sendSuccSms() {
		Result result = new Result();
		try {
			smsQueneHandler.sendSuccSms();
			result.success().message("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.fail().message("操作失败！" + e.getMessage());
			return JsonUtils.toJsonString(result);
		}
		return JsonUtils.toJsonString(result);
	}

	@RequestMapping(value = "/deleteNotSuccSms", method = RequestMethod.POST)
	@MenuSetting("submenu-message-activate")
	public @ResponseBody String deleteNotSuccSms() {
		Result result = new Result();
		try {
			smsQueneHandler.deleteNotSuccSms();
			result.success().message("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.fail().message("操作失败！" + e.getMessage());
			return JsonUtils.toJsonString(result);
		}
		return JsonUtils.toJsonString(result);
	}
	
}
