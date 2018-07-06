package com.suidifu.bridgewater.controller.base.v2;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.demo2do.core.entity.Result;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.resolver.PageViewResolver;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCacheSpec;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.entity.api.TMerConfigCacheSpec;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import com.zufangbao.wellsfargo.yunxin.cache.DictionaryCache;
import com.zufangbao.wellsfargo.yunxin.cache.TMerConfigCache;

public class BaseApiController {


	public static final String SYSTEM_MER_ID = "systemdeduct";

	private static final Log logger = LogFactory.getLog(BaseApiController.class);

	protected static long TIMEOUT = 1000 * 60 * 60 * 24;

	@Autowired
	protected CacheManager cacheManager;

	@Autowired
	public PageViewResolver pageViewResolver;

	@Autowired
	private ApiJsonViewResolver jsonViewResolver;
	
	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private TMerConfigService tMerConfigService;
	
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

	public String signSucResult(HttpServletResponse response,Map<String,Object> map) {
		String result = jsonViewResolver.sucJsonResult(null,map);
		return getPriKeyAndSign(response, result);
	}


	public  HashMap<String, String> getAllParameters(HttpServletRequest request) {
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

	public Result post(HttpServletResponse response, HashMap<String, String> postRequest, String location) {
		try {
			Map<String, String> authorizationInfo = new HashMap<String, String>();
			String content = ApiSignUtils.getSignCheckContent(postRequest);
			String sign = ApiSignUtils.rsaSign(content, getPrivateKey());
			authorizationInfo.put("merId", SYSTEM_MER_ID);
			authorizationInfo.put("secret", getSecret(SYSTEM_MER_ID));
			authorizationInfo.put("sign", sign);
			Result postResult = HttpClientUtils.executePostRequest(location, postRequest, authorizationInfo);
			String responsePacket = postResult.getData().get(HttpClientUtils.DATA_RESPONSE_PACKET).toString();
			Result result = JsonUtils.parse(responsePacket, Result.class);
			return result;
		} catch (Exception e) {
			logger.error("#Post 转发失败");
			e.printStackTrace();
			return new Result(ApiResponseCode.SYSTEM_BUSY + "");
		}
	}
	private String getSecret(String merId) {
		Cache cache = cacheManager.getCache(TMerConfigCacheSpec.CACHE_KEY);
		TMerConfigCache merConfigCache = cache.get(merId, TMerConfigCache.class);
		if (null != merConfigCache && !merConfigCache.needUpdate(TIMEOUT)) {
			logger.info("#get secret from cache");
			return merConfigCache.gettMerConfig().getSecret();
		}
		TMerConfig merConfig;
		try {
			merConfig = tMerConfigService.getTMerConfigByMerId(merId);
			cache.put(merId, new TMerConfigCache(merConfig));
			logger.info("#get secret from table t_merchant_config and fresh cache");
		} catch (Exception e) {
			logger.error("#get secret fail");
			e.printStackTrace();
			return null;
		}
		return merConfig.getSecret();
	}

	private String getPrivateKey(){
		Cache cache = cacheManager.getCache(DictionaryCacheSpec.CACHE_KEY);
		DictionaryCache dictionaryCache=cache.get(DictionaryCode.PLATFORM_PRI_KEY.getCode(), DictionaryCache.class);
		if (null != dictionaryCache && !dictionaryCache.needUpdate(TIMEOUT)) {
			logger.info("#get private key from cache");
			return dictionaryCache.getDictionary().getContent();
		}
		Dictionary priKeyDictionary;
		try {
			priKeyDictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
			cache.put(DictionaryCode.PLATFORM_PRI_KEY.getCode(), new DictionaryCache(priKeyDictionary));
			logger.info("#get private key from table dictionary and fresh cache");
		} catch (DictionaryNotExsitException e) {
			logger.error("#get private key fail");
			e.printStackTrace();
			return null;
		}
		return priKeyDictionary.getContent();
	}


}
