package com.suidifu.bridgewater.controller;

import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.resolver.PageViewResolver;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

public class BaseApiController {

	@Autowired
	public PageViewResolver pageViewResolver;

	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	public String getMerchantId(HttpServletRequest request) {
		return request.getHeader(ApiConstant.PARAMS_MER_ID);
	}

	public String signSucResult(HttpServletResponse response) {
		return signSucResult(response, null, null);
	}
	
	public String signSucResult(HttpServletResponse response, String key, Object data) {
		return signSucResult(response, null, key, data);
	}
	
	public String signSucResult(HttpServletResponse response, String message, String key, Object data) {
		String result = jsonViewResolver.sucJsonResult(message, key, data);
		return getPriKeyAndSign(response, result);
	}

	public String signErrorResult(HttpServletResponse response, Exception e) {
		String result = jsonViewResolver.errorJsonResult(e);
		return getPriKeyAndSign(response, result);
	}
	
	public String signErrorResult(HttpServletResponse response, int error_code) {
		return signErrorResult(response, error_code, null);
	}
	
	public String signErrorResult(HttpServletResponse response, int error_code, String error_msg) {
		String result = jsonViewResolver.errorJsonResult(error_code, error_msg);
		return getPriKeyAndSign(response, result);
	}

	private String getPriKeyAndSign(HttpServletResponse response, String result) {
		String privateKey = dictionaryService.getPlatformPrivateKey();
		return ApiSignUtils.sign_and_return_result(response, result, privateKey);
	}

	protected HashMap<String, String> getAllParameters(HttpServletRequest request) {
		HashMap<String, String> parameters = new HashMap<>();
		if (request == null) return parameters;
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValues = request.getParameter(paramName);
			parameters.put(paramName, paramValues);
		}
		return parameters;
	}
}
