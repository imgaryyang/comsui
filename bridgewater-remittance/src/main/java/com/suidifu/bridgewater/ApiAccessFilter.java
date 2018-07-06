package com.suidifu.bridgewater;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.gluon.util.RequestUtil;
import com.zufangbao.gluon.util.SecurityUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.api.ApiStatus;
import com.zufangbao.sun.yunxin.entity.api.EncryptionMode;
import com.zufangbao.sun.yunxin.entity.api.TApiConfig;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TApiConfigService;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 接口访问过滤器
 *
 * @author
 */
@Component("apiAccessFilter")
public class ApiAccessFilter implements Filter {

	private static final Log logger = LogFactory.getLog(ApiAccessFilter.class);

	@Autowired
	private TApiConfigService tApiConfigService;
	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private TMerConfigService tMerConfigService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
	                     FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;

			String requestURI = request.getRequestURI();
			String apiUrl = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;

			//校验接口是否存在
			List<TApiConfig> tApiConfigs = tApiConfigService.getApiConfigListBy(apiUrl);
			if (CollectionUtils.isEmpty(tApiConfigs)) {
				writeJsonResult(response, ApiResponseCode.API_NOT_FOUND);
				return;
			}

			TMerConfig merConfig = verifyMerconfig(request);
			//非加密模式
			if (null == merConfig.getEncryptionMode() || EncryptionMode.NONE.equals(merConfig.getEncryptionMode())) {

				String fnCode = request.getParameter(ApiConstant.PARAMS_FN_KEY);

				logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#" + DateUtils.getCurrentTimeMillis() + " : " + IpUtil.getIpAddress(request) + " CALL #API : " +
						apiUrl + " #FN : " + fnCode + " #PARAMS : " + JsonUtils.toJsonString(request.getParameterMap()));

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
						return;
					}

					tApiConfig = optionalTApiConfig.get();
				} else {
					tApiConfig = tApiConfigs.get(0);
				}

				//接口启用状态校验
				if (tApiConfig.getApiStatus() == ApiStatus.CLOSED) {
					writeJsonResult(response, ApiResponseCode.API_UNAVAILABLE);
					return;
				}

				//验签
				if (!ApiSignUtils.verifySign(request, merConfig)) {
					throw new ApiException(ApiResponseCode.SIGN_VERIFY_FAIL);
				}

				chain.doFilter(request, response);

			} else {//加密模式

				//解密
				String requestBody = RequestUtil.getRequestBody(request);

				logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#" + IpUtil.getIpAddress(request) + " CALL #API : " + apiUrl);

				String decryptString = StringUtils.EMPTY;
				EncryptionMode encryptionMode = merConfig.getEncryptionMode();
				switch (encryptionMode) {
					case THREE_DES:
						try {
							decryptString = SecurityUtil.threeDesDecrypt(requestBody, merConfig.getEncryptionKey());
							logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#decrypt[SUCC],decryptString:" + decryptString);
						} catch (Exception e) {
							logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#decrypt[ERR],reason:解密失败！");
							e.printStackTrace();
							writeJsonResult(response, ApiResponseCode.DECRYPTION_VERIFY_ERROR);
							return;
						}
						break;
					default:
						logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#decrypt[ERR],reason:解密失败，不支持该加密模式！");
						writeJsonResult(response, ApiResponseCode.DECRYPTION_VERIFY_ERROR);
						return;
				}

				//检查fn 启用状态等等
				Map<String, Object> requestParams = JsonUtils.parse(decryptString);
				if (null == requestParams) {
					logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#decrypt parse[ERR],reason:解密内容格式错误！");
					writeJsonResult(response, ApiResponseCode.DECRYPTION_CONTENT_FORMAT_ERROR);
					return;
				}

				String fnCode = requestParams.getOrDefault(ApiConstant.PARAMS_FN_KEY, StringUtils.EMPTY).toString();

				//校验接口功能代码是否存在
				Optional<TApiConfig> optionalTApiConfig = tApiConfigs.stream()
						.filter(tac -> StringUtils.equals(tac.getFnCode(), fnCode))
						.findFirst();
				if (fnCode == null || !optionalTApiConfig.isPresent()) {
					writeJsonResult(response, ApiResponseCode.INVALID_FN_CODE);
					return;
				}

				//接口启用状态校验
				TApiConfig tApiConfig = optionalTApiConfig.get();
				if (tApiConfig.getApiStatus() == ApiStatus.CLOSED) {
					writeJsonResult(response, ApiResponseCode.API_UNAVAILABLE);
					return;
				}

				//验签
				boolean rsaCheckContent = verifySignWithEncryption(request, requestBody, merConfig);

				if (!rsaCheckContent) {
					writeJsonResult(response, ApiResponseCode.SIGN_VERIFY_FAIL);
					return;
				}

				//存request
				ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(request);
				requestWrapper.addAllParameters(requestParams);

				chain.doFilter(requestWrapper, response);
			}

		} catch (RuntimeException e) {
			processRuntimeException((HttpServletResponse) servletResponse, e);
			return;
		} catch (Exception e) {
			logger.info("***ApiAccessFilter***");
			e.printStackTrace();
			writeJsonResult((HttpServletResponse) servletResponse, ApiResponseCode.SYSTEM_ERROR);
			return;
		}
	}

	private void writeJsonResult(HttpServletResponse response, int code) throws IOException {
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

	private TMerConfig verifyMerconfig(HttpServletRequest request) throws Exception {
		String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);
		String secret = request.getHeader(ApiConstant.PARAMS_SECRET);
		vaildateHeader(merId, secret);
		TMerConfig merConfig = tMerConfigService.getTMerConfig(merId, secret);
		if (null == merConfig) {
			throw new ApiException(ApiResponseCode.NOT_EXIST_MER_ERROR);
		}
		return merConfig;
	}

	private void vaildateHeader(String merId, String secret) {
		if (StringUtils.isEmpty(merId) || StringUtils.isEmpty(secret))
			throw new ApiException(ApiResponseCode.SIGN_MER_CONFIG_ERROR);
	}

	private void verifySign(HttpServletRequest request) throws Exception {
		String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);
		String secret = request.getHeader(ApiConstant.PARAMS_SECRET);
		vaildateHeader(merId, secret);
		TMerConfig merConfig = tMerConfigService.getTMerConfig(merId, secret);
		if (!ApiSignUtils.verifySign(request, merConfig))
			throw new ApiException(ApiResponseCode.SIGN_VERIFY_FAIL);
	}

	private boolean verifySignWithEncryption(HttpServletRequest request, String content, TMerConfig merConfig) {
		String sign = request.getHeader(ApiConstant.PARAMS_SIGN);
		String publicKey = merConfig.getPubKey();
		logger.info(System.currentTimeMillis() + ", ip［" + IpUtil.getIpAddress(request) + "］, 内容验签: content［" + content + "］" + ",sign［" + sign + "］");

		return ApiSignUtils.rsaCheckContent(content, sign, publicKey);
	}

	private boolean processRuntimeException(HttpServletResponse response, RuntimeException e) throws IOException {
		e.printStackTrace();
		if (e instanceof ApiException) {
			int error_code = ((ApiException) e).getCode();
			switch (error_code) {
				case ApiResponseCode.SIGN_MER_CONFIG_ERROR:
					writeJsonResult(response, ApiResponseCode.SIGN_MER_CONFIG_ERROR);
					return false;
				case ApiResponseCode.NOT_EXIST_MER_ERROR:
					writeJsonResult(response, ApiResponseCode.NOT_EXIST_MER_ERROR);
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
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
