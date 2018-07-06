package com.zufangbao.earth.yunxin.api.interceptor;

import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.gluon.util.RequestUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.api.ApiStatus;
import com.zufangbao.sun.yunxin.entity.api.TApiConfig;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TApiConfigService;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.zufangbao.earth.yunxin.api.constant.ApiConstant.*;

/**
 * 接口访问拦截器
 * @author zhanghongbing
 *
 */
public class ApiAccessInterceptor extends HandlerInterceptorAdapter{

	private static final Log logger = LogFactory.getLog(ApiAccessInterceptor.class);

	private static final String TEST_MER_ID = "t_test_zfb";
	private static final String TEST_SECRET = "123456";

	@Autowired
	private TApiConfigService tApiConfigService;
	
	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	
	@Autowired
	private TMerConfigService tMerConfigService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		try {
			
			String requestURI = request.getRequestURI();
			String apiUrl = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;
			String fnCode = request.getParameter(PARAMS_FN_KEY);
			
			logger.info("#" + DateUtils.getCurrentTimeMillis() + " : " + IpUtil.getIpAddress(request) + " CALL #API : "+ apiUrl + " #FN : " + fnCode + " #PARAMS : " + request.getParameterMap());
			
			//校验接口是否存在
			List<TApiConfig> tApiConfigs = tApiConfigService.getApiConfigListBy(apiUrl);
			if(CollectionUtils.isEmpty(tApiConfigs)) {
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
				if(fnCode == null || !optionalTApiConfig.isPresent()) {
					writeJsonResult(response, ApiResponseCode.INVALID_FN_CODE);
					return false;
				}
				//接口启用状态校验
				tApiConfig = optionalTApiConfig.get();
			} else {
				tApiConfig = tApiConfigs.get(0);
			}

			if(tApiConfig.getApiStatus() == ApiStatus.CLOSED) {
				writeJsonResult(response, ApiResponseCode.API_UNAVAILABLE);
				return false;
			}

			verifySign(request);
			
		} catch (RuntimeException e) { 
			return processRuntimeException(response, e);
		} catch (Exception e) {
			writeJsonResult(response, ApiResponseCode.SYSTEM_ERROR);
			e.printStackTrace();
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}

	public boolean processRuntimeException(HttpServletResponse response, RuntimeException e) throws IOException {
		e.printStackTrace();
		if( e instanceof ApiException ) { 
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

	public void verifySign(HttpServletRequest request) throws Exception {
		String merId = request.getHeader(PARAMS_MER_ID);
		String secret = request.getHeader(PARAMS_SECRET);
		vaildateHeader(merId, secret);
		TMerConfig merConfig = tMerConfigService.getTMerConfig(merId, secret);
		if (merConfig != null && TEST_MER_ID.equals(merId) && TEST_SECRET.equals(secret)) {
			// 测试商户不做验签操作
			return;
		}
		if(!ApiSignUtils.verifySign(request, merConfig)) {
			throw new ApiException(ApiResponseCode.SIGN_VERIFY_FAIL);
		}
	}

	public void vaildateHeader(String merId, String secret) {
		if (StringUtils.isEmpty(merId) || StringUtils.isEmpty(secret)) {
			throw new ApiException(ApiResponseCode.SIGN_MER_CONFIG_ERROR);
		}
	}

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
		String privateKey = dictionaryService.getPlatformPrivateKey();
		return ApiSignUtils.sign_and_return_result(response, result, privateKey);
	}
	
}