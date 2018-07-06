/**
 *
 */
package com.zufangbao.earth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.GenericJdbcSupport;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.demo2do.core.utils.StringUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.RoleSpec;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.security.SystemMenu;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.PrincipalDetailShowModel;
import com.zufangbao.sun.yunxin.entity.model.PrincipalShowModel;
import com.zufangbao.sun.yunxin.entity.model.principal.PrincipalQueryModel;
import com.zufangbao.sun.yunxin.service.SystemMenuService;
import com.zufangbao.sun.yunxin.service.TUserService;

/**
 *
 * @author Downpour
 */
@Service("principalService")
public class PrincipalServiceImpl extends GenericServiceImpl<Principal> implements PrincipalService {

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private AppService appService;
	@Autowired
	private GenericJdbcSupport genericJdbcSupport;
	@Autowired
	private SystemMenuService systemMenuService;
	@Autowired
	private TUserService tUserService;
	@Autowired
	private FinancialContractService financialContractService;
	/* (non-Javadoc)
	 * @see com.demo2do.alaska.service.UserService#getPrincipal(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Principal getPrincipal(String name) {
		List<Principal> principals = genericDaoSupport.searchForList("FROM Principal principal WHERE principal.name = :name ", "name", name);
		return CollectionUtils.isNotEmpty(principals) ? principals.get(0) : null;
	}

	@Override
	public Principal getPrincipalById(Long id) {
		try {
			Principal principal = genericDaoSupport.get(Principal.class, id);
			return principal;
		} catch (Exception e) {
			return null;
		}
	}

	// edit by fenglei 密码加密传到后台
	@Override
	public String updatePassword(Principal principal, String oldPassword,
		String newPassword) {
//		if(!Md5Util.encode(oldPassword).equals(principal.getPassword())){
//			return "原密码输入有误";
//		}
//		if(principal.getPassword().equals(Md5Util.encode(newPassword))){
//			return "新密码不允许与原密码一致";
//		}
		if (!oldPassword.equals(principal.getPassword())) {
			return "原密码输入有误";
		}
		if(principal.getPassword().equals(newPassword)){
			return "新密码不允许与原密码一致";
		}

		String sql = "update principal set password = :password, modify_password_time = :modify_password_time where id = :id";
		HashMap<String,Object> params = new HashMap<>();
//		params.put("password", Md5Util.encode(newPassword));
		params.put("password", newPassword);
		params.put("modify_password_time", principal.getModifyPasswordTime()+1);
		params.put("id", principal.getId());
		try {
			genericJdbcSupport.executeSQL(sql, params);
			return "修改成功";
		} catch (Exception e) {
			return "修改失败请重试";
		}
	}

	@Override
	public String updatePrincipal(TUser tUser,PrincipalDetailShowModel principalDetailShowModel) {
		String sql = "update t_user set name = :name, email =:email ,phone =:phone,job_number =:jobNumber, id_number =:idNumber  where id = :id";
		HashMap<String,Object> params = new HashMap<>();
		params.put("name", principalDetailShowModel.getName());
		params.put("phone", principalDetailShowModel.getPhone());
		params.put("email", principalDetailShowModel.getEmail());
		params.put("jobNumber", principalDetailShowModel.getJobNumber());
		params.put("idNumber", principalDetailShowModel.getIdNumber());
		params.put("id", tUser.getId());
		try {
			genericJdbcSupport.executeSQL(sql, params);
			return "修改成功";
		} catch (Exception e) {
			return "修改失败请重试";
		}
	}



	@Override
	public void save(Principal newPrincipal) {
		newPrincipal.setStartDate(new Date());
		this.genericDaoSupport.save(newPrincipal);
	}

	@Override
	public void deleteUser(Principal newPrincipal){
		newPrincipal.setThruDate(new Date());
		this.genericDaoSupport.update(newPrincipal);
	}

	@Override
	//FIXME 根据 principal 查询 app
	public List<App> get_can_access_app_list(Principal principal) {
		if (principal.is_super_user_role() || RoleSpec.ROLE_TRUST_OBSERVER.equals(principal.getAuthority())){
			return getSuperRoleAppList();
		}
		principal = this.load(Principal.class, principal.getId());
		TUser tUser = principal.gettUser();
		// 绑定了所有的信托，返回所有的商户
		if (tUser.getBindAll()){
			return getSuperRoleAppList();
		}
		List<Long> exsitIdList = tUser.getFinancialContractIdList();
		List<App> getAppList = new 	ArrayList<App>();
		for(Long financialContractId : exsitIdList){
			FinancialContract financialContract = financialContractService.load(FinancialContract.class, financialContractId);
			App app = financialContract.getApp();
			if (app != null && !getAppList.contains(app)) {
				getAppList.add(app);
			}
		}
		return getAppList;
	}

	private List<App> getSuperRoleAppList() {
		return appService.getAllApp();
	}

	@Override
	public Map<String, String> getQueriesByRequest(HttpServletRequest request) {
		String queryString = request.getQueryString();
		Map<String, String> queries = StringUtils
			.parseQueryString(queryString);
		if (queries.containsKey("page")) {
			queries.remove("page");
		}
		return queries;

	}

	@Override
	public Long getSystemPrincipalId() {
		Filter filter = new Filter();
		filter.addEquals("name", RoleSpec.ROLE_PRINCIPAL_NAME_OF_SYSTEM);
		List<Principal> principalList = this.list(Principal.class, filter);
		if(CollectionUtils.isEmpty(principalList)){
			return null;
		}
		return principalList.get(0).getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Principal> queryPrincipal(PrincipalQueryModel queryModel, Page page) {
		StringBuffer querySB = new StringBuffer();
		Map<String ,Object> parameters = new HashMap<String, Object>();
		getQuerySentence(queryModel, querySB, parameters);
		querySB.append(" order by id DESC ");
		if (page == null) {
			return  this.genericDaoSupport.searchForList(querySB.toString(), parameters);
		} else {
			return this.genericDaoSupport.searchForList(querySB.toString(), parameters, page.getBeginIndex(), page.getEveryPage());
		}
	}

	@Override
	public List<Map<String, Object>> queryPrincipalWithoutGroup(PrincipalQueryModel queryModel, Page page) {
		if (!StringUtils.isEmpty(queryModel.getRoleId())) return new ArrayList<Map<String, Object>>();

		StringBuffer querySB = new StringBuffer(
			" select p.id id, p.authority role, p.name accountName,  " +
				" b.name userName, b.email email, b.phone number,b.job_number jobNumber,b.id_number idNumber,b.dept_name deptName, b.full_name company, b.cid, b.group_name groupName, b.ugid, " +
				" case when p.thru_date is null then '正常' else '冻结' end status " +
				" from principal p " +
				" left join ( " +
				" 	select a.id, a.name, a.email, a.phone,a.job_number,a.id_number, a.full_name,a.dept_name, a.cid, ug.group_name, ug.id ugid from " +
				" 		(select tu.id, tu.user_group_id, tu.name, tu.email, tu.phone,tu.job_number,tu.id_number,tu.dept_name,c.full_name, c.id cid " +
				" 			from t_user tu  " +
				" 			left join company c on tu.company_id=c.id) a " + // t_user left join company
				" 		left join user_group ug on ug.id=a.user_group_id " + // then left join user_group
				" ) b on p.t_user_id = b.id " +
				" where 1=1 ");

		if (!StringUtils.isEmpty(queryModel.getId()))
			querySB.append(" and p.id = " + queryModel.getId());
		if (!StringUtils.isEmpty(queryModel.getAccountName()))
			querySB.append(" and p.name like '%" + queryModel.getAccountName() + "%'");
		if (!StringUtils.isEmpty(queryModel.getCompanyId()))
			querySB.append(" and b.cid = " + queryModel.getCompanyId());
		if (!StringUtils.isEmpty(queryModel.getGroupId()))
			querySB.append(" and b.ugid = " + queryModel.getGroupId());

		querySB.append(" limit " + page.getBeginIndex() + "," + page.getEveryPage());

		List<Map<String, Object>> results = new ArrayList<>();
		try {
			results = genericJdbcSupport.queryForList(querySB.toString());

			return results;
		} catch (Exception e) {
			return new ArrayList<Map<String, Object>>();
		}

	}

	@Override
	public int countPrincipalWithoutGroup(PrincipalQueryModel queryModel) {

		StringBuffer querySB = new StringBuffer(
			" select p.id id, p.authority role, p.name accountName,  " +
				" b.name userName, b.email email, b.phone number ,b.job_number jobNumber,b.id_number idNumber,b.dept_name deptName,b.full_name company, b.cid, b.group_name groupName, b.ugid, " +
				" case when p.thru_date is null then '正常' else '冻结' end status " +
				" from principal p " +
				" left join ( " +
				" 	select a.id, a.name, a.email, a.phone,a.job_number,a.id_number,a.dept_name, a.full_name, a.cid, ug.group_name, ug.id ugid from " +
				" 		(select tu.id, tu.user_group_id, tu.name, tu.email, tu.phone,tu.job_number,tu.id_number,tu.dept_name, c.full_name, c.id cid " +
				" 			from t_user tu  " +
				" 			left join company c on tu.company_id=c.id) a " + // t_user left join company
				" 		left join user_group ug on ug.id=a.user_group_id " + // then left join user_group
				" ) b on p.t_user_id = b.id " +
				" where 1=1 ");

		if (!StringUtils.isEmpty(queryModel.getId()))
			querySB.append(" and p.id = " + queryModel.getId());
		if (!StringUtils.isEmpty(queryModel.getAccountName()))
			querySB.append(" and p.name like '%" + queryModel.getAccountName() + "%'");
		if (!StringUtils.isEmpty(queryModel.getCompanyId()))
			querySB.append(" and b.cid like '" + queryModel.getCompanyId() + "'");
		if (!StringUtils.isEmpty(queryModel.getGroupId()))
			querySB.append(" and b.ugid = " + queryModel.getGroupId());

		List<Map<String, Object>> results = new ArrayList<>();
		try {
			results = genericJdbcSupport.queryForList(querySB.toString());

			return results.size();
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public List<Map<String, Object>> queryPrincipalWithGroup(PrincipalQueryModel queryModel, Page page) {
		if (StringUtils.isEmpty(queryModel.getRoleId())) return new ArrayList<Map<String, Object>>();

		StringBuffer querySB = new StringBuffer(
			" select distinct p.id, p.role, p.accountName, p.userName, p.email, p.number,p.jobNumber,p.idNumber,p.deptName, p.company, p.cid, p.groupName, p.ugid, p.status " +
				" from " +
				" (select p.id id, p.authority role, p.name accountName, b.name userName, b.email email, b.phone number,b.job_number jobNumber,b.id_number idNumber,b.dept_name deptNamem b.full_name company, b.cid, b.group_name groupName, b.ugid, " +
				" 	case when p.thru_date is null then '正常' else '冻结' end status " +
				" 	from principal p  " +
				" 	left join ( " +
				" 		select a.id, a.name, a.email, a.phone,a.job_number,a.id_number,a.dept_name, a.full_name, a.cid, ug.group_name, ug.id ugid from " +
				" 			(select tu.id, tu.user_group_id, tu.name, tu.email, tu.phone,tu.job_number,tu.id_number, tu.dept_ame,c.full_name, c.id cid" +
				" 				from t_user tu  " +
				" 				left join company c on tu.company_id=c.id) a " + // t_user left join company
				" 			left join user_group ug on ug.id=a.user_group_id " + // then left join user_group
				" 	) b on p.t_user_id = b.id " +
				" 	where 1=1) p, " + // subquery, same as queryPrincipalWithoutGroup,
				" ( " +
				" select lrp.principal_id id, lrp.role_id roleId, sr.role_name roleName " +
				" 	from link_role_principal lrp " +
				" 	left join system_role sr on sr.id=lrp.role_id ) r " +
				" where p.id=r.id ");

		if (!StringUtils.isEmpty(queryModel.getId()))
			querySB.append(" and p.id = " + queryModel.getId());
		if (!StringUtils.isEmpty(queryModel.getAccountName()))
			querySB.append(" and p.name like '%" + queryModel.getAccountName() + "%'");
		if (!StringUtils.isEmpty(queryModel.getCompanyId()))
			querySB.append(" and p.cid like '" + queryModel.getCompanyId() + "'");
		if (!StringUtils.isEmpty(queryModel.getGroupId()))
			querySB.append(" and p.ugid = " + queryModel.getGroupId());
		if (!StringUtils.isEmpty(queryModel.getRoleId()))
			querySB.append(" and r.roleId = " + queryModel.getRoleId());

		querySB.append(" limit " + page.getBeginIndex() + "," + page.getEveryPage());

		List<Map<String, Object>> results = new ArrayList<>();
		try {
			results = genericJdbcSupport.queryForList(querySB.toString());

			return results;
		} catch (Exception e) {
			return new ArrayList<Map<String, Object>>();
		}

	}

	@Override
	public int countPrincipalWithGroup(PrincipalQueryModel queryModel) {

		StringBuffer querySB = new StringBuffer(
			" select distinct p.id, p.role, p.accountName, p.userName, p.email, p.number,p.jobNumber,p.idNumber,p.deptName,  p.company, p.cid, p.groupName, p.ugid, p.status " +
				" from " +
				" (select p.id id, p.authority role, p.name accountName, b.name userName, b.email email, b.phone number,b.job_number jobNumber,b.id_number idNumber,b.dept_name deptName, b.full_name company, b.cid, b.group_name groupName, b.ugid, " +
				" 	case when p.thru_date is null then '正常' else '冻结' end status " +
				" 	from principal p  " +
				" 	left join ( " +
				" 		select a.id, a.name, a.email, a.phone,a.job_number,a.id_number,a.dept_name, a.full_name, a.cid, ug.group_name, ug.id ugid from " +
				" 			(select tu.id, tu.user_group_id, tu.name, tu.email, tu.phone,tu.job_number,tu.id_number,tu.dept_name, c.full_name, c.id cid" +
				" 				from t_user tu  " +
				" 				left join company c on tu.company_id=c.id) a " + // t_user left join company
				" 			left join user_group ug on ug.id=a.user_group_id " + // then left join user_group
				" 	) b on p.t_user_id = b.id " +
				" 	where 1=1) p, " + // subquery, same as queryPrincipalWithoutGroup,
				" ( " +
				" select lrp.principal_id id, lrp.role_id roleId, sr.role_name roleName " +
				" 	from link_role_principal lrp " +
				" 	left join system_role sr on sr.id=lrp.role_id ) r " +
				" where p.id=r.id ");

		if (!StringUtils.isEmpty(queryModel.getId()))
			querySB.append(" and p.id = " + queryModel.getId());
		if (!StringUtils.isEmpty(queryModel.getAccountName()))
			querySB.append(" and p.name like '%" + queryModel.getAccountName() + "%'");
		if (!StringUtils.isEmpty(queryModel.getCompanyId()))
			querySB.append(" and p.cid like '" + queryModel.getCompanyId() + "'");
		if (!StringUtils.isEmpty(queryModel.getGroupId()))
			querySB.append(" and p.ugid = " + queryModel.getGroupId());
		if (!StringUtils.isEmpty(queryModel.getRoleId()))
			querySB.append(" and r.roleId = " + queryModel.getRoleId());

		List<Map<String, Object>> results = new ArrayList<>();
		try {
			results = genericJdbcSupport.queryForList(querySB.toString());

			return results.size();
		} catch (Exception e) {
			return 0;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public int countPrincipal(PrincipalQueryModel queryModel) {
		if(CollectionUtils.isEmpty(queryModel.getAuthorityList())) {
			return 0;
		}
		StringBuffer querySB = new StringBuffer("SELECT count(id) ");
		Map<String ,Object> parameters = new HashMap<String, Object>();
		getQuerySentence(queryModel, querySB, parameters);
		List<Long> countList = this.genericDaoSupport.searchForList(querySB.toString(), parameters);
		if(countList != null && countList.get(0) != null) {
			return countList.get(0).intValue();
		} else {
			return 0;
		}
	}

	private void getQuerySentence(PrincipalQueryModel queryModel,StringBuffer querySB,Map<String, Object> parameters) {
		querySB.append("FROM Principal WHERE 1=1");
//		List<String> companyUuids = queryModel.getCompanyUuidList();
		List<String> authorityList = queryModel.getAuthorityList();
		if(!StringUtils.isEmpty(queryModel.getId())) {
			Long id = Long.parseLong(queryModel.getId());
			querySB.append(" AND id = :id");
			parameters.put("id", id);
		}
		if(!StringUtils.isEmpty(queryModel.getAccountName())) {
			querySB.append(" AND name LIKE :name");
			parameters.put("name", "%" + queryModel.getAccountName() + "%");
		}

		if(CollectionUtils.isEmpty(authorityList)) {
			authorityList.add("");
		}
		querySB.append(" AND authority IN (:authorityList)");
		parameters.put("authorityList", authorityList);

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMenuBy(Long principalId) {
		if (this.getPrincipalById(principalId) == null) return ListUtils.EMPTY_LIST;

		StringBuffer sb = new StringBuffer("select distinct lrm.menu_id from link_role_principal lrp " +
			" left join link_role_menu lrm " +
			" on lrp.role_id=lrm.role_id " +
			" where principal_id = " + principalId);
		List<Map<String, Object>> results = new ArrayList<>();
		try {
			results = genericJdbcSupport.queryForList(sb.toString());

			return results;
		} catch (Exception e) {
			return ListUtils.EMPTY_LIST;
		}
	}

	@Override
	public List<SystemMenu> getMenusBy(Long principalId) {
		List<SystemMenu> systemMenus = systemMenuService.loadAllMenus();
		List<SystemMenu> filteredMenus = new ArrayList<>();
		List<Map<String, Object>> availableMenusIds = this.getMenuBy(principalId);
		List<Long> availableMenuIds = new ArrayList<>();
		availableMenusIds.forEach((m)->{
			availableMenuIds.add(Long.parseLong(m.get("menu_id").toString()));
		});

		for (SystemMenu sytemMenu:systemMenus) {
			if (availableMenuIds.contains(sytemMenu.getId()))
				filteredMenus.add(sytemMenu);
		}
		return filteredMenus;
	}

	@Override
	public void bindFinancialContract(Long financialContractId,Long principalId) {
		if (financialContractId == null || principalId == null) {
			return;
		}
		List<Long> financialContractIdList = new ArrayList<>();
		financialContractIdList.add(financialContractId);
		Principal principal = load(Principal.class, principalId);
		TUser tUser = principal.gettUser();
		List<Long> exsitIdList = tUser.getFinancialContractIdList();
		exsitIdList.addAll(financialContractIdList);
		List<Long> newList = exsitIdList.stream().distinct().collect(Collectors.toList());
		String newIdJson = com.zufangbao.sun.utils.JsonUtils.toJSONString(newList);
		tUser.setFinancialContractIds(newIdJson);
		tUserService.save(tUser);
	}

	@Override
	public Long getPrincipalIdBysourceUuid(String sourceUuid) {
		String sql = "select * from Principal where sourceUuid =:sourceUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("sourceUuid", sourceUuid);
		List<Principal> principal = this.genericDaoSupport.queryForList(sql,params, Principal.class);
		if (CollectionUtils.isEmpty(principal)) {
			return null;
		}
		return principal.get(0).getId();
	}
	
	@Override
	public  List<PrincipalShowModel>getPrincipalShowList(List<Principal> principalList) {
		if(principalList == null){
			return null;
		}
		List<PrincipalShowModel>list = new ArrayList<>();
		for(Principal principal:principalList){
			PrincipalShowModel showModel = new PrincipalShowModel(principal);
			list.add(showModel);
		}
		return list;
	}
	
	

}
