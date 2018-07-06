package com.suidifu.citigroup.configuration;

/**
 * Created by louguanyang on 2017/5/23.
 */
public class FileConfig {
    /**
     * 变更还款计划地址
     */
    private String modifyUrl;
    /*
     *浮动费用请求地址
     */
    private String mutableUrl;
    /**
     * 还款订单提交地址
     */
    private String orderUrl;
    /**
     * 商户编号
     */
    private String merId;
    /**
     * 商户密钥
     */
    private String secret;

	/**
     * 文件扫描地址
     */
    private String scanPath;

    /**
     * 接口版本 1.0
     */
    private String apiV1;

    public String getModifyUrl() {
        return modifyUrl;
    }

    public void setModifyUrl(String modifyUrl) {
        this.modifyUrl = modifyUrl;
    }

    public String getMutableUrl() {
        return mutableUrl;
    }

    public void setMutableUrl(String mutableUrl) {
        this.mutableUrl = mutableUrl;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }
    /**
	 * @return the orderurl
	 */
	public String getOrderUrl() {
		return orderUrl;
	}

	/**
	 * @param orderurl the orderurl to set
	 */
	public void setOrderUrl(String orderurl) {
		this.orderUrl = orderurl;
	}

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    public String getApiV1() {
        return apiV1;
    }

    public void setApiV1(String apiV1) {
        this.apiV1 = apiV1;
    }
}