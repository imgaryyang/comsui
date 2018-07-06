package com.zufangbao.earth.yunxin.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zufangbao.sun.yunxin.entity.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.handler.MessageHandler;
import com.zufangbao.earth.service.MessageService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.Message;
import com.zufangbao.sun.yunxin.entity.MessageSource;
import com.zufangbao.sun.yunxin.entity.MessageStatus;
import com.zufangbao.sun.yunxin.entity.model.MessageDetailShowModel;
import com.zufangbao.sun.yunxin.entity.model.MessageQueryModel;
import com.zufangbao.sun.yunxin.entity.model.MessageShowModel;

@RestController
@RequestMapping("/messages")
public class MessageController extends BaseController{

	@Autowired
	private MessageHandler messageHandler;

	@Autowired
	private MessageService messageService;

    @Autowired
    private CompanyService companyService;

	@RequestMapping(value="",method=RequestMethod.GET)
	public String queryMessage(@ModelAttribute MessageQueryModel queryModel,Page page) {
		try {
			Map<String, Object> data = new HashMap<>();
			List<MessageShowModel> messagesModelList = messageService.queryMessageModelList(queryModel, page);
			int size = messageService.countMessage(queryModel);
			data.put("size", size);
			data.put("list", messagesModelList);
			return jsonViewResolver.sucJsonResult(data);
		}  catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public String getMessageOptions() {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("source",
					EnumUtil.getKVList(MessageSource.class));
			result.put("status",
					EnumUtil.getKVList(MessageStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	@RequestMapping(value="/{messageUuid}",method=RequestMethod.GET)
	public String getMessageDetail (@PathVariable(value = "messageUuid") String messageUuid){
		try {
			Message message =  messageService.getMessageByUuid(messageUuid);
			if(message == null){
				return jsonViewResolver.errorJsonResult("消息不存在！！！！！");
			}
			Company company = companyService.getCompanyById(message.getCompanyId());
			MessageDetailShowModel detailModel = new MessageDetailShowModel(message,company);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("detailModel", detailModel);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！！！");
		}
	}

	@RequestMapping(value = "/{messageUuid}", method = RequestMethod.POST)
	public String ProcessingMessages(@PathVariable("messageUuid") String messageUuid,
			 								   @RequestParam("result") Integer result,@RequestParam("feedback") String feedback) {
		try {
			Message message =  messageService.getMessageByUuid(messageUuid);
            Long principalId = messageHandler.updateMessageBackPrincipalId(message, result, feedback);
            return jsonViewResolver.sucJsonResult("principalId", principalId);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}			
	}

}
