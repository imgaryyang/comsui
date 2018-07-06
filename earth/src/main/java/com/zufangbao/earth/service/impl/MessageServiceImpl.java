package com.zufangbao.earth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.demo2do.core.utils.StringUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.service.MessageService;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.yunxin.entity.Message;
import com.zufangbao.sun.yunxin.entity.model.MessageQueryModel;
import com.zufangbao.sun.yunxin.entity.model.MessageShowModel;

@Service("messageService")
public class MessageServiceImpl extends GenericServiceImpl<Message> implements MessageService{

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private CompanyService companyService;

	@Override
	public List<MessageShowModel> queryMessageModelList(MessageQueryModel queryModel, Page page) {
		List<Message> messages = queryMessage( queryModel,  page);
		List<MessageShowModel> messageModelLists = new  ArrayList<>();
		for(Message message:messages){
			Company company = companyService.getCompanyById(message.getCompanyId());
			MessageShowModel messageModel = new MessageShowModel(message,company);
			messageModelLists.add(messageModel);
		}
		return messageModelLists;

	}
	@Override
	public List<Message> queryMessage(MessageQueryModel queryModel, Page page) {
		StringBuffer querySB = new StringBuffer();
		Map<String, Object> paramters = new HashMap<String, Object>();

		generateQueryHql( querySB , queryModel,paramters);
		
		if (page == null) {
			return this.genericDaoSupport.searchForList(querySB.toString(), paramters);
		} else {
			return this.genericDaoSupport.searchForList(querySB.toString(), paramters, page.getBeginIndex(),
				page.getEveryPage());
		}

	}
	
	@Override
	public int countMessage(MessageQueryModel queryModel) {
		StringBuffer querySB = new StringBuffer("select count(id) ");
		Map<String, Object> paramters = new HashMap<String, Object>();
		generateQueryHql( querySB , queryModel, paramters);
		return this.genericDaoSupport.searchForInt(querySB.toString(), paramters);

	}
	
	
	private void generateQueryHql(StringBuffer querySB , MessageQueryModel queryModel,Map<String, Object> paramters){
		querySB.append(" From Message where 1=1 ");
        if (!StringUtils.isEmpty(queryModel.getMessageUuid())){
            querySB.append(" And messageUuid = :messageUuid " );
            paramters.put("messageUuid", queryModel.getMessageUuid());
        }
        if (queryModel.getSource()!= null){
            querySB.append(" And source = :source" );
            paramters.put("source", queryModel.getSource());
        }
        if (queryModel.getStatus()!= null){
            querySB.append(" And status = :status" );
            paramters.put("status", queryModel.getStatus());
        }
        if (queryModel.getStartCreateTime()!=null){
            querySB.append(" And Date(createTime) >= :startCreateTime");
            paramters.put("startCreateTime", queryModel.getStartCreateTime());
        }
        if (queryModel.getEndCreateTime()!=null){
            querySB.append(" And Date(createTime) <= :endCreateTime");
            paramters.put("endCreateTime",queryModel.getEndCreateTime());
        }
        if (queryModel.getName()!= null){
            querySB.append(" And name = :name " );
            paramters.put("name", queryModel.getName());
        }
        if (queryModel.getCompanyName() != null){
            Company company = companyService.getCompanyByname(queryModel.getCompanyName());
            querySB.append(" And companyId = :companyId " );
			paramters.put("companyId", company == null ? -1 : company.getId());
		}
	}
	
	
	@Override
	public Message getMessageByUuid(String messageUuid) {
		if(StringUtils.isEmpty(messageUuid)){
			return null;
		}
		String sql = "select * from t_message where message_uuid =:messageUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("messageUuid", messageUuid);
		List<Message> Messages = this.genericDaoSupport.queryForList(sql,params, Message.class);
		if (CollectionUtils.isEmpty(Messages)) {
			return null;
		}
		return Messages.get(0);
	}
	
	@Override
	public Message getMessageBySourceUuid(String sourceUuid) {
		String sql = "select * from t_message where source_uuid =:sourceUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("sourceUuid", sourceUuid);
		List<Message> Messages = this.genericDaoSupport.queryForList(sql,params, Message.class);
		if (CollectionUtils.isEmpty(Messages)) {
			return null;
		}
		return Messages.get(0);
	}
	
	@Override
	public boolean isMessagePostedBy(String sourceUuid) {
		String sql = "select count(id) from t_message where source_uuid =:sourceUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("sourceUuid", sourceUuid);
		int n = this.genericDaoSupport.queryForInt(sql, params);
		return n > 0;
	}

}
