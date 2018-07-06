package com.suidifu.bridgewater.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.api.ApiStatus;
import com.zufangbao.sun.yunxin.entity.api.TApiConfig;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TApiConfigService;

/**
 * 前置接口访问拦截器
 * Created by louguanyang on 2017/5/3.
 *
 */
public class PreApiAccessInterceptor extends ApiAccessInterceptor{

	@Autowired
	private TApiConfigService tApiConfigService;

	@Autowired
	private ApiJsonViewResolver jsonViewResolver;

	@Autowired
	private DictionaryService dictionaryService;

	private static final Log logger = LogFactory.getLog(PreApiAccessInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		try {
			String requestURI = request.getRequestURI();
			String apiUrl = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;
			String fnCode = request.getParameter(ApiConstant.PARAMS_FN_KEY);
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#" + DateUtils.getCurrentTimeMillis() + " : " + IpUtil
				.getIpAddress(request) + " CALL #API : " + apiUrl + " #FN : " + fnCode + " #PARAMS : " + JsonUtils
				.toJsonString(request.getParameterMap()));

			//校验数据库里是否已配置该接口
			List<TApiConfig> tApiConfigs = tApiConfigService.getApiConfigListBy(apiUrl);
			if (CollectionUtils.isEmpty(tApiConfigs)) {
				writeJsonResult(response, ApiResponseCode.API_NOT_FOUND);
				return false;
			}

			boolean check_fn_code = tApiConfigs.stream().filter(tApiConfig -> StringUtils.isNotEmpty(tApiConfig
				.getFnCode()))
				.count() > 0;

			TApiConfig tApiConfig = null;

			if (check_fn_code) {
				//校验接口功能代码是否存在
				Optional<TApiConfig> optionalTApiConfig = tApiConfigs.stream()
					.filter(tac -> StringUtils.equals(tac.getFnCode(), fnCode))
					.findFirst();
				if (fnCode == null || !optionalTApiConfig.isPresent()) {
					writeJsonResult(response, ApiResponseCode.INVALID_FN_CODE);
					return false;
				}
				tApiConfig = optionalTApiConfig.get();
			} else {
				tApiConfig = tApiConfigs.get(0);
			}

			//接口启用状态校验
			if (tApiConfig.getApiStatus() == ApiStatus.CLOSED) {
				writeJsonResult(response, ApiResponseCode.API_UNAVAILABLE);
				return false;
			}
			verifySign(request);
//			verifyPreApiSign(request);
		} catch (RuntimeException e) {
			return processRuntimeException(response, e);
		} catch (Exception e) {
			writeJsonResult(response, ApiResponseCode.SYSTEM_ERROR);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public  boolean processRuntimeException(HttpServletResponse response, RuntimeException e) throws IOException {
		e.printStackTrace();
		if( e instanceof ApiException) {
			int error_code = ((ApiException) e).getCode();
			switch (error_code) {
				case ApiResponseCode.SIGN_MER_CONFIG_ERROR:
					writeJsonResult(response, ApiResponseCode.SIGN_MER_CONFIG_ERROR);
					return false;
				case ApiResponseCode.SIGN_VERIFY_FAIL:
					writeJsonResult(response, ApiResponseCode.SIGN_VERIFY_FAIL);
					return false;
				default:
					break;
			}
		}
		writeJsonResult(response, ApiResponseCode.SYSTEM_ERROR);
		return false;
	}

	@Override
	public void writeJsonResult(HttpServletResponse response, int code) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String jsonStr = signErrorResult(response, code);
		out.write(jsonStr);
		out.close();
	}

	public String signErrorResult(HttpServletResponse response, int error_code) {
		String result = jsonViewResolver.errorJsonResult(error_code);
		Dictionary dictionary;
		try {
			dictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
		} catch (DictionaryNotExsitException e) {
			logger.error("#get private key fail");
			e.printStackTrace();
			return result;
		}
		return ApiSignUtils.sign_and_return_result(response, result, dictionary.getContent());
	}


}