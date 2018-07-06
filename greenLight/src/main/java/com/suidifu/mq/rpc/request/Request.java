package com.suidifu.mq.rpc.request;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class Request implements Serializable {
	private static final long serialVersionUID = -4770571847682450725L;
	private String uuid = UUID.randomUUID().toString();
	private String businessId;// 业务Id
	private String bean; // 模块标识
	private String method; // 远程方法
	private Object[] params; // 参数列表
	private String[] paramTypes;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public String[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(String[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public Request bean(String bean) {
		this.bean = bean;
		return this;
	}

	public Request method(String method) {
		this.method = method;
		return this;
	}

	public Request params(Object... params) {
		this.params = params;
		return this;
	}

	public Request paramTypes(Class<?>... types) {
		if (types == null)
			return this;
		this.paramTypes = new String[types.length];
		for (int i = 0; i < types.length; i++)
			this.paramTypes[i] = types[i].getCanonicalName();
		return this;
	}

	public static void normalize(Request req) {
		if (req.bean == null)
			req.bean = "";
		if (req.params == null)
			req.params = new Object[0];
	}

	@Override
	public String toString() {
		return "Request [uuid=" + uuid + ", businessId=" + businessId + ", bean=" + bean + ", method=" + method + ", params=" + Arrays.toString(params) + ", paramTypes=" + Arrays.toString(paramTypes) + "]";
	}

}