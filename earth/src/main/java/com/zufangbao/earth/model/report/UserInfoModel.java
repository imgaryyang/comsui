package com.zufangbao.earth.model.report;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class UserInfoModel {
	private Long principalId;
	
	private String username;
	
	private Long companyId;
	
	private String deptName; 
	
	private String jobNumber;
	
	private String idNumber;
	
	private String phone;
	
	private String email;
	
	private String postscript;

	public UserInfoModel() {
		super();
	}
	
	public boolean checkParams(){
		return isValidUserName(this.getUsername());
	}
	
	public static boolean isValidUserName(String user_name) {
		if(StringUtils.isEmpty(user_name)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{5,15}$");
		Matcher m = pattern.matcher(user_name);
		return m.matches();
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
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
		this.username = username;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

}
