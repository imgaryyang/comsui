package com.zufangbao.earth.model;

import com.demo2do.core.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrincipalInfoModel {

	private Long principalId;
	
	private String username;
	
	private String role;
	
	private String realname;
	
	private String email;
	
	private String phone;
	
	private Long companyId;
	
	private Long groupId;
	
	private String deptName; 
	
	private String positionName;
	
	private String remark;
	
	private String jobNumber;
	
	private String idNumber;
	
	/**
	 * 新增Ids
	 */
	private String addIds;
	
	/**
	 * 解绑Ids
	 */
	private String removeIds;
	
	
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Long getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(Long principalId) {
		this.principalId = principalId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = StringUtils.trim(username);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = StringUtils.trim(realname);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = StringUtils.trim(email);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = StringUtils.trim(phone);
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = StringUtils.trim(deptName);
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = StringUtils.trim(positionName);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public boolean isValidUsername() {
		String user_name = this.username;
		return isValidUserName(user_name);
	}

	public static boolean isValidUserName(String user_name) {
		if(StringUtils.isEmpty(user_name)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{5,15}$");
		Matcher m = pattern.matcher(user_name);
		return m.matches();
	}
	
	public PrincipalInfoModel() {
		super();
	}
	
	public String getAddIds() {
		return addIds;
	}

	public void setAddIds(String addIds) {
		this.addIds = addIds;
	}

	public String getRemoveIds() {
		return removeIds;
	}

	public void setRemoveIds(String removeIds) {
		this.removeIds = removeIds;
	}

	public List<Long> getAddIdList() {
		List<Long> financialContractList = JsonUtils.parseArray(addIds, Long.class);
		if (financialContractList == null) {
			return Collections.emptyList();
		}
		return financialContractList;
	}

	public List<Long> getRemoveIdList() {
		List<Long> financialContractList = JsonUtils.parseArray(removeIds, Long.class);
		if (financialContractList == null) {
			return Collections.emptyList();
		}
		return financialContractList;
	}
}
