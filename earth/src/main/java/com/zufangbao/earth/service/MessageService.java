package com.zufangbao.earth.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.demo2do.core.service.GenericService;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.Message;
import com.zufangbao.sun.yunxin.entity.model.MessageQueryModel;
import com.zufangbao.sun.yunxin.entity.model.MessageShowModel;

public interface MessageService extends GenericService<Message>{
	List<Message> queryMessage(MessageQueryModel queryModel, Page page);

	int countMessage(MessageQueryModel queryModel);

	Message getMessageByUuid(String messageUuid);

	Message getMessageBySourceUuid(String sourceUuid);

	boolean isMessagePostedBy(String sourceUuid);

	List<MessageShowModel> queryMessageModelList(MessageQueryModel queryModel, Page page);


}
