package com.suidifu.jpmorgan.entity.unionpay;

public interface IUnionPayApiParams {
	
	public abstract String getPfxFileKey();

	public abstract String getPfxFilePath();

	public abstract String getCerFilePath();

	public abstract String getApiUrl();

	public abstract void setPfxFileKey(String pfxFileKey);

	public abstract void setPfxFilePath(String pfxFilePath);

	public abstract void setCerFilePath(String cerFilePath);

	public abstract void setApiUrl(String apiUrl);

}
