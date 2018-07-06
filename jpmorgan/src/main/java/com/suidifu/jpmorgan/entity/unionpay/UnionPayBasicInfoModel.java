package com.suidifu.jpmorgan.entity.unionpay;

public class UnionPayBasicInfoModel implements IUnionPayApiParams{
	
	private String reqNo; //交易流水号
	
	private String userName; //用户名
	
	private String userPwd; //用户密码
	
	private String apiUrl; //银联接口地址
	
	private String cerFilePath; //公钥文件地址
		
	private String pfxFilePath; //私钥文件地址
		
	private String pfxFileKey; //私钥key

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Override
	public String getApiUrl() {
		return apiUrl;
	}

	@Override
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	@Override
	public String getCerFilePath() {
		return cerFilePath;
	}

	@Override
	public void setCerFilePath(String cerFilePath) {
		this.cerFilePath = cerFilePath;
	}

	@Override
	public String getPfxFilePath() {
		return pfxFilePath;
	}

	@Override
	public void setPfxFilePath(String pfxFilePath) {
		this.pfxFilePath = pfxFilePath;
	}

	@Override
	public String getPfxFileKey() {
		return pfxFileKey;
	}

	@Override
	public void setPfxFileKey(String pfxFileKey) {
		this.pfxFileKey = pfxFileKey;
	}

	public UnionPayBasicInfoModel() {
		super();
	}
	
}
