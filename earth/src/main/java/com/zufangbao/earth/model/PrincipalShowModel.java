package com.zufangbao.earth.model;

import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.sun.utils.StringUtils;

public class PrincipalShowModel {
	private String id;
	private String role;
	private String accountName;
	private String userName;
	private String email;
	private String number;
	private String company;
	private String status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public PrincipalShowModel(Principal principal) {
		this.id = principal.getId().toString();
		String authority = principal.getAuthority();
		if(authority.equals("ROLE_SUPER_USER")) {
			this.role = "系统管理员";
		} else if (authority.equals("ROLE_TRUST_OBSERVER") || StringUtils.isEmpty(authority)){
			this.role = "普通用户";
		} else {
			this.role = authority;
		}
		this.accountName = principal.getName();
		TUser tUser = principal.gettUser();
		if(tUser != null) {
			this.userName = tUser.getName();
			this.email = tUser.getEmail();
			this.number = tUser.getPhone();
			Company company = tUser.getCompany();
			if(company !=null) {
				this.company = company.getFullName();
			}
		}
		if(principal.getThruDate() == null) {
			this.status = "正常";
		} else {
			this.status = "冻结";
		}
	}
	
}
