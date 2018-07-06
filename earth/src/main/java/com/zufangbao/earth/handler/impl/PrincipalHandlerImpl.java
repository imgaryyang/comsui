package com.zufangbao.earth.handler.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.PrincipalInfoModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.util.Md5Util;
import com.zufangbao.earth.yunxin.handler.FinancialContractHandler;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.security.UserGroup;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractShowModel;
import com.zufangbao.sun.yunxin.service.TUserService;
import com.zufangbao.sun.yunxin.service.UserGroupService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Component("principalHandler")
public class PrincipalHandlerImpl implements PrincipalHandler{
	
	@Autowired
	private PrincipalService principalService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private TUserService tUserService;
	
	@Autowired
	public FinancialContractService financialContractService;
	
	@Autowired
	public FinancialContractHandler financialContractHandler;

	// TODO for check
	@Override
	public String createPrincipal(PrincipalInfoModel principalInfoModel, Long creatorId) {
		
		List<Long> financialContractIdList = principalInfoModel.getAddIdList();
		
		//用户基础信息
		TUser tUser = new TUser();
		tUser.setName(principalInfoModel.getRealname());
		tUser.setEmail(principalInfoModel.getEmail());
		tUser.setPhone(principalInfoModel.getPhone());
		tUser.setDeptName(principalInfoModel.getDeptName());
		tUser.setPositionName(principalInfoModel.getPositionName());
		tUser.setRemark(principalInfoModel.getRemark());
		tUser.setIdNumber(principalInfoModel.getIdNumber());
		tUser.setJobNumber(principalInfoModel.getJobNumber());

		Long companyId = principalInfoModel.getCompanyId();
		if(companyId != null && companyId > 0) {
			Company company = companyService.getCompanyById(principalInfoModel.getCompanyId());
			tUser.setCompany(company);
		}
		
		Long groupId = principalInfoModel.getGroupId();
		if (groupId != null && groupId >0) {
			UserGroup userGroup = userGroupService.getUserGroupBy(groupId);
			tUser.setUserGroup(userGroup);
			tUser.setDeptName(userGroup.getGroupName());
		}

		if(!CollectionUtils.isEmpty(financialContractIdList)) {
			List<Long> newList = financialContractIdList.stream().distinct().collect(Collectors.toList());
			String idJson = JsonUtils.toJSONString(newList);
			tUser.setFinancialContractIds(idJson);
		}
		//先存储用户基础信息
		Serializable tUserId = tUserService.save(tUser);
		tUser = tUserService.load(TUser.class, tUserId);
		
		//用户账号信息
		Principal principal = new Principal();
		principal.setAuthority("初始用户");
		principal.setName(principalInfoModel.getUsername());
		
		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(6);
		principal.setPassword(Md5Util.encode(randomAlphanumeric));
		principal.setCreatedTime(new Date());
		principal.setCreatorId(creatorId);
		
		principal.settUser(tUser);
		
		//存储用户账号信息
		principalService.save(principal);
		return randomAlphanumeric;
	}

	@Override
	public void updatePrincipal(PrincipalInfoModel principalInfoModel,
			Long operatorId) {
		Long principalId = principalInfoModel.getPrincipalId();
		Principal principal = principalService.load(Principal.class, principalId);
		
		TUser tUser = principal.gettUser() == null ? new TUser() : principal.gettUser();
		tUser.setName(principalInfoModel.getRealname());
		tUser.setEmail(principalInfoModel.getEmail());
		tUser.setPhone(principalInfoModel.getPhone());
		tUser.setDeptName(principalInfoModel.getDeptName());
		tUser.setPositionName(principalInfoModel.getPositionName());
		tUser.setRemark(principalInfoModel.getRemark());
		tUser.setIdNumber(principalInfoModel.getIdNumber());
		tUser.setJobNumber(principalInfoModel.getJobNumber());

		Long companyId = principalInfoModel.getCompanyId();
		if(companyId != null && companyId > 0) {
			Company company = companyService.getCompanyById(principalInfoModel.getCompanyId());
			tUser.setCompany(company);
		}
		
		Long groupId = principalInfoModel.getGroupId();
		if (groupId != null && groupId >0) {
			UserGroup userGroup = userGroupService.getUserGroupBy(groupId);
			tUser.setUserGroup(userGroup);
			tUser.setDeptName(userGroup.getGroupName());
		}
		//用户基础信息不存在则创建，有则更新
		Serializable tUserId;
		if(principal.gettUser() == null) {
			tUserId = tUserService.save(tUser);
		}else {
			
			tUserService.update(tUser);
			tUserId = tUser.getId();
		}
		tUser = tUserService.load(TUser.class, tUserId);
		principal.settUser(tUser);
		principalService.update(principal);
	}
	
	@Override
	public List<FinancialContract> get_can_access_financialContract_list(Principal principal) {
		if(principal == null) {
			return Collections.emptyList();
		}
		if(principal.is_super_user_role()) {
			return financialContractService.loadAll(FinancialContract.class);
		}
		try {
			principal = principalService.load(Principal.class, principal.getId());
			TUser gettUser = principal.gettUser();
			if (gettUser.getBindAll()) {
				return financialContractService.loadAll(FinancialContract.class);
			}
			List<Long> financialContractIdList = gettUser.getFinancialContractIdList();
			return financialContractService.getFinancialContractsByIds(financialContractIdList);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<FinancialContract> get_can_access_financialContract_list(Long principalId) {
		if(principalId == null) {
			return Collections.emptyList();
		}
		Principal principal = principalService.load(Principal.class, principalId);
		return get_can_access_financialContract_list(principal);
	}

	@Override
	public Map<String, Object> queryFinancialContractListByPrincipal(FinancialContractQueryModel queryModel, Page page) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(queryModel == null) {
			result.put("size", 0);
			result.put("list", Collections.emptyList());
			return result;
		}
		List<FinancialContract> financialContractList = getQueryList(queryModel);
		List<FinancialContract> list = financialContractService.getFinancialContractList(queryModel, page, financialContractList);
		List<FinancialContract> all = financialContractService.getFinancialContractList(queryModel, null, financialContractList);
		result.put("size", all.size());
		result.put("list", list);
		return result;
	}

	public List<FinancialContract> getQueryList(FinancialContractQueryModel queryModel) {
		Long principalId = queryModel.getPrincipalId();
		List<FinancialContract> financialContractList = new ArrayList<>();
		int bindState = queryModel.getBindState();
		switch (bindState) {
		case -1:
			financialContractList = financialContractService.loadAll(FinancialContract.class);
			break;
		case 0://未绑定
			financialContractList = get_unbind_financialContract_list(principalId);
			break;
		case 1:
			financialContractList = get_can_access_financialContract_list(principalId);
			break;
		default:
			financialContractList = Collections.emptyList();
			break;
		}
		return financialContractList;
	}

	@Override
	public List<FinancialContract> get_unbind_financialContract_list(Long principalId) {
		List<FinancialContract> all = financialContractService.loadAll(FinancialContract.class);
		if (principalId == null) {
			return all;
		}
		List<FinancialContract> can_access_list = get_can_access_financialContract_list(principalId);
		for (FinancialContract financialContract : can_access_list) {
			if(all.contains(financialContract)) {
				all.remove(financialContract);
			}
		}
		return all;
	}

	public List<FinancialContractShowModel> buildSuperUserShowList() {
		try {
			List<FinancialContract> all = financialContractService.loadAll(FinancialContract.class);
			List<FinancialContractShowModel> result = new ArrayList<>();
			for (FinancialContract financialContract : all) {
				FinancialContractShowModel model = new FinancialContractShowModel(financialContract);
				model.setBindState(1);//1已绑定
				result.add(model);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public String bindAllFinancialContract(Long principalId) {
		if (principalId == null) {
			return "参数错误！";
		}
		Principal principal = principalService.load(Principal.class, principalId);
		if (principal == null) {
			return "参数错误！";
		}
		TUser tUser = principal.gettUser();
		tUser.setBindAll(true);
		tUserService.save(tUser);
		return null;
	}

	@Override
	public String unbindAllFinancialContract(Long principalId) {
		if (principalId == null) {
			return "参数错误！";
		}
		Principal principal = principalService.load(Principal.class, principalId);
		if (principal == null) {
			return "参数错误！";
		}
		TUser tUser = principal.gettUser();
		tUser.setBindAll(false);
		tUser.setFinancialContractIds(null);
		tUserService.save(tUser);
		return null;
	}

	@Override
	public TUser getTUser(Principal principal) {
		if(principal == null){
			return null;
		}
		Principal principals = principalService.getPrincipalById(principal.getId());
		return principals.gettUser();
	}

	@Override
	public void bindFinancialContract(Long principalId, List<Long> financialContractIds) {
		Principal principal = principalService.load(Principal.class, principalId);
		TUser tUser = principal.gettUser();
		List<Long> exsitIdList = tUser.getFinancialContractIdList();
		exsitIdList.addAll(financialContractIds);
		List<Long> newList = exsitIdList.stream().distinct().collect(Collectors.toList());
		String newIdJson = com.zufangbao.sun.utils.JsonUtils.toJSONString(newList);
		tUser.setFinancialContractIds(newIdJson);
		tUserService.save(tUser);
	}

}
