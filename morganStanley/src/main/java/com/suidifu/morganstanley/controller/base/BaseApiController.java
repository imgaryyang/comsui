package com.suidifu.morganstanley.controller.base;

import com.demo2do.core.entity.Result;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.AnnotationUtils;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
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
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.HTTP_ROOT;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.HTTP_URL;
import static com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec.PRE_API;

@Log4j2
public class BaseApiController {
	private static final String SYSTEM_MER_ID = "systemdeduct";
	private static final long TIMEOUT = 1000 * 60 * 60 * 24L;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private ApiJsonViewResolver apiJsonViewResolver;
    @Resource
    private DictionaryService dictionaryService;
    @Resource
    private TMerConfigService tMerConfigService;

    protected String getMerchantId(HttpServletRequest request) {
        return request.getHeader(ApiConstant.PARAMS_MER_ID);
    }

    protected String signSucResult(HttpServletResponse response) {
        return signSucResult(response, null, null);
    }

    protected String signSucResult(HttpServletResponse response, String key, Object data) {
        return signSucResult(response, null, key, data);
    }

    protected String signSucResult(HttpServletResponse response, String message, String key, Object data) {
        String result = apiJsonViewResolver.sucJsonResult(message, key, data);
        return getPriKeyAndSign(response, result);
    }

    protected String signSucResult(HttpServletResponse response, Map<String, Object> map) {
        String result = apiJsonViewResolver.sucJsonResult(null, map);
        return getPriKeyAndSign(response, result);
    }

    protected String signErrorResult(HttpServletResponse response, Exception e) {
        String result = apiJsonViewResolver.errorJsonResult(e);
        return getPriKeyAndSign(response, result);
    }

    /**
     * 获取BaseResponse响应实体类，并且将sign放入Header
     *
     * @param response   http响应对象
     * @param apiMessage api枚举类
     * @return http响应返回的错误信息实体类
     */
    protected BaseResponse wrapHttpServletResponse(HttpServletResponse response, ApiMessage apiMessage) {
        return wrapHttpServletResponse(response, apiMessage, null);
    }

    /**
     * 获取BaseResponse响应实体类，并且将sign放入Header(如果枚举类是成功，则需放入返回数据)
     *
     * @param response   http响应对象
     * @param apiMessage api枚举类
     * @param data       返回成功是需要带入的数据
     * @return http响应返回的错误信息实体类
     */
    protected BaseResponse wrapHttpServletResponse(HttpServletResponse response, ApiMessage apiMessage, Object data) {
        return getBaseResponse(response, apiMessage.getCode(), apiMessage.getMessage(), data);
    }

    /**
     * 获取BaseResponse响应实体类，并且将sign放入Header
     *
     * @param response  http响应对象
     * @param errorCode 错误代码
     * @param errorMsg  错误消息
     * @return http响应返回的错误信息实体类
     */
    protected BaseResponse wrapHttpServletResponse(HttpServletResponse response, int errorCode, String errorMsg) {
        return getBaseResponse(response, errorCode, errorMsg, null);
    }

    /**
     * 获取BaseResponse响应实体类，并且将sign放入Header
     *
     * @param response  http响应对象
     * @param errorCode 错误代码
     * @param errorMsg  错误消息
     * @param data      返回成功是需要带入的数据
     * @return http响应返回的错误信息实体类
     */
    private BaseResponse getBaseResponse(HttpServletResponse response, int errorCode, String errorMsg, Object data) {
        BaseResponse baseResponse = new BaseResponse(errorCode, errorMsg);
        if (ObjectUtils.notEqual(null, data)) {
            baseResponse.setData(data);
        }

        String result = JsonUtils.toJSONString(baseResponse);
        String privateKey = dictionaryService.getPlatformPrivateKey();
        ApiSignUtils.sign2Header(response, result, privateKey);
        return baseResponse;
    }

    protected String signErrorResult(HttpServletResponse response, int errorCode) {
        return signErrorResult(response, errorCode, null);
    }

    protected String signErrorResult(HttpServletResponse response, int errorCode, String errorMsg) {
        String result = apiJsonViewResolver.errorJsonResult(errorCode, errorMsg);
        return getPriKeyAndSign(response, result);
    }

    protected String signErrorResultMap(HttpServletResponse response, int errorCode, Map<String, Object> errorMap) {
        String result = apiJsonViewResolver.errorJsonResult(errorCode, errorMap);
        return getPriKeyAndSign(response, result);
    }

    private String getPriKeyAndSign(HttpServletResponse response, String result) {
        String privateKey = dictionaryService.getPlatformPrivateKey();
        return ApiSignUtils.signAndReturnResult(response, result, privateKey);
    }

    protected Map<String, String> getAllParameters(HttpServletRequest request) {
        HashMap<String, String> parameters = new HashMap<>();
        if (request == null) {
            return parameters;
        }
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValues = request.getParameter(paramName);
            parameters.put(paramName, paramValues);
        }
        return parameters;
    }

    public Result post(Map<String, String> postRequest, String location) {
        try {
            Map<String, String> authorizationInfo = new HashMap<>();
            String content = ApiSignUtils.getSignCheckContent(postRequest);
            String sign = ApiSignUtils.rsaSign(content, getPrivateKey());
            authorizationInfo.put(ApiConstant.PARAMS_MER_ID, SYSTEM_MER_ID);
            authorizationInfo.put(ApiConstant.PARAMS_SECRET, getSecret(SYSTEM_MER_ID));
            authorizationInfo.put(ApiConstant.PARAMS_SIGN, sign);
            Result postResult = HttpClientUtils.executePostRequest(location, postRequest, authorizationInfo);
            String responsePacket = postResult.getData().get(HttpClientUtils.DATA_RESPONSE_PACKET).toString();
            return JsonUtils.parse(responsePacket, Result.class);
        } catch (Exception e) {
            log.error("#Post 转发失败,{}", ExceptionUtils.getStackTrace(e));
            return new Result(Integer.toString(ApiResponseCode.SYSTEM_BUSY));
        }
    }

    private String getSecret(String merId) {
        Cache cache = cacheManager.getCache(TMerConfigCacheSpec.CACHE_KEY);
        TMerConfigCache merConfigCache = cache.get(merId, TMerConfigCache.class);
        if (null != merConfigCache && !merConfigCache.needUpdate(TIMEOUT)) {
            log.info("#get secret from cache");
            return merConfigCache.gettMerConfig().getSecret();
        }
        TMerConfig merConfig;
        try {
            merConfig = tMerConfigService.getTMerConfigByMerId(merId);
            cache.put(merId, new TMerConfigCache(merConfig));
            log.info("#get secret from table t_merchant_config and fresh cache");
        } catch (Exception e) {
            log.error("#get secret fail {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
        return merConfig.getSecret();
    }

    private String getPrivateKey() {
        Cache cache = cacheManager.getCache(DictionaryCacheSpec.CACHE_KEY);
        DictionaryCache dictionaryCache = cache.get(DictionaryCode.PLATFORM_PRI_KEY.getCode(), DictionaryCache.class);
        if (null != dictionaryCache && !dictionaryCache.needUpdate(TIMEOUT)) {
            log.info("#get private key from cache");
            return dictionaryCache.getDictionary().getContent();
        }
        Dictionary priKeyDictionary;
        try {
            priKeyDictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
            cache.put(DictionaryCode.PLATFORM_PRI_KEY.getCode(), new DictionaryCache(priKeyDictionary));
            log.info("#get private key from table dictionary and fresh cache");
        } catch (DictionaryNotExsitException e) {
            log.error("#get private key fail,{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
        return priKeyDictionary.getContent();
    }

    protected BaseResponse getValidatedResult(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();

        List<ObjectError> objectErrors = bindingResult.getGlobalErrors();
        for (ObjectError objectError : objectErrors) {
            stringBuilder.append(objectError.getObjectName()).append(":").append(objectError.getDefaultMessage()).append(",");
        }

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            stringBuilder.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        String formattedMessage = MessageFormat.format(ApiMessage.INVALID_PARAMS.getMessage(), stringBuilder);
        return new BaseResponse(ApiMessage.INVALID_PARAMS.getCode(), formattedMessage);
    }

    protected BaseResponse getCheckedValue(Object o) {
        String checkValue = AnnotationUtils.checkValue(o);
        if (StringUtils.isEmpty(checkValue)) {
            return null;
        }

        BaseResponse baseResponse = new BaseResponse();
        String formattedMessage = MessageFormat.format(ApiMessage.INVALID_PARAMS.getMessage(), checkValue);
        baseResponse.setCode(ApiMessage.INVALID_PARAMS.getCode());
        baseResponse.setMessage(formattedMessage);
        return baseResponse;
    }

	protected Map<String, String> getHttpRootUrlMap(HttpServletRequest request) {
		try {
			String[] split = request.getRequestURL().toString().split(PRE_API);
			Map<String, String> map = new HashMap<>(2);
			map.put(HTTP_ROOT, split[0]);
			map.put(HTTP_URL, split[1]);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	/**
	 * @param request Http请求
	 * @param key     key
	 * @return Http请求根地址或URL
	 */
	protected String getRequestURL(HttpServletRequest request, String key) {
		try {
			Map<String, String> rootAndUrl = getHttpRootUrlMap(request);
			return rootAndUrl.getOrDefault(key, StringUtils.EMPTY);
		} catch (Exception e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
	}
}