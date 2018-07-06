package com.zufangbao.earth.handler.impl;

import com.zufangbao.earth.handler.MessageHandler;
import com.zufangbao.earth.model.report.UserInfoModel;
import com.zufangbao.earth.service.MessageService;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.util.Md5Util;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.Message;
import com.zufangbao.sun.yunxin.entity.MessageResult;
import com.zufangbao.sun.yunxin.entity.MessageSource;
import com.zufangbao.sun.yunxin.entity.MessageStatus;
import com.zufangbao.sun.yunxin.service.TUserService;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("messageHandler")
public class MessageHandlerImpl implements MessageHandler{
	@Autowired
	private CompanyService companyService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private TUserService tUserService;
	@Autowired
	private PrincipalService principalService;
	
	
	public void generateMessage(UserInfoModel userInfoModel,String publicKey,String sourceUuid){
		Message message = new Message();
		message.setName(userInfoModel.getUsername());
		message.setJobNumber(userInfoModel.getJobNumber());
		message.setIdNumber(userInfoModel.getIdNumber());
		message.setPhone(userInfoModel.getPhone());
		message.setDeptName(userInfoModel.getDeptName());
		message.setCreateTime(new Date());
		message.setPostscript(userInfoModel.getPostscript());
		message.setPublicKey(publicKey);
		message.setEmail(userInfoModel.getEmail());
		message.setSourceUuid(sourceUuid);
		message.setSource( MessageSource.CLIENT.ordinal());
		
		Long companyId = userInfoModel.getCompanyId();
		if(companyId != null && companyId > 0) {
			Company company = companyService.getCompanyById(userInfoModel.getCompanyId());
			message.setCompanyId(company.getId());
		}
		
		messageService.save(message);
		
	}
	@Override
	public Long updateMessageBackPrincipalId(Message message, Integer result, String feedback) {
		message.setResult(result);
		message.setFeedback(feedback);
		message.setStatus(MessageStatus.TREATED.ordinal());
		messageService.update(message);
        Principal principal = createPrincipal( message, result);
        return principal == null ? null : principal.getId();
	}
	
	@Override
	public Principal createPrincipal(Message message,Integer result) {
		if (Objects.equals(EnumUtil.fromOrdinal(MessageResult.class, result), MessageResult.REFUSE)) {
			return null;
		}
		TUser tUser = new TUser();
		tUser.setName(message.getName());
		tUser.setDeptName(message.getDeptName());
		tUser.setPhone(message.getPhone());
		tUser.setEmail(message.getEmail());
		tUser.setIdNumber(message.getIdNumber());
		tUser.setJobNumber(message.getJobNumber());
		Company company = companyService.getCompanyById(message.getCompanyId());
		tUser.setCompany(company);
		//先存储用户基础信息
		Serializable tUserId = tUserService.save(tUser);
		tUser = tUserService.load(TUser.class, tUserId);
		//用户账号信息
		Principal principal = new Principal();
		principal.setAuthority("初始用户");
		principal.setName(message.getName());
		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(6);
		principal.setPassword(Md5Util.encode(randomAlphanumeric));
		principal.setCreatedTime(new Date());
		principal.settUser(tUser);
		principal.setSourceUuid(message.getSourceUuid());
				
		//存储用户账号信息
		principalService.save(principal);

		return principal;
	}

}
