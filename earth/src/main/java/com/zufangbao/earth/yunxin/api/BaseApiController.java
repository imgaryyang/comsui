package com.zufangbao.earth.yunxin.api;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.resolver.PageViewResolver;
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
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.yunxin.cache.DictionaryCache;
import com.zufangbao.wellsfargo.yunxin.cache.TMerConfigCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.yunxin.api.constant.ApiConstant.PARAMS_MER_ID;

public class BaseApiController {

    public static final String SYSTEM_MER_ID = "systemdeduct";

    private static final Log logger = LogFactory.getLog(BaseApiController.class);

    @Autowired
    public PageViewResolver pageViewResolver;

    @Autowired
    private ApiJsonViewResolver jsonViewResolver;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    protected CacheManager cacheManager;

    @Autowired
    private TMerConfigService tMerConfigService;

    public String getMerchantId(HttpServletRequest request) {
        return request.getHeader(PARAMS_MER_ID);
    }

    public String signSucResult(HttpServletResponse response) {
        return signSucResult(response, null, null);
    }

    public String signResult(HttpServletResponse response, String result) {
        return getPriKeyAndSign(response, result);
    }

    public String signSucResult(HttpServletResponse response, String key, Object data, SerializerFeature... features) {
        return signSucResult(response, null, key, data, features);
    }

    public String signSucResult(HttpServletResponse response, String message, String key, Object data, SerializerFeature... features) {
        String result = jsonViewResolver.sucJsonResult(message, key, data, features);
        jsonViewResolver.sucJsonResult(message);
        return getPriKeyAndSign(response, result);
    }

    public String signSucResult(HttpServletResponse response, String message) {
        String result = jsonViewResolver.sucJsonResult(message);
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

    public String signErrorResultMap(HttpServletResponse response, int error_code, Map<String, Object> errmap) {
        String result = jsonViewResolver.errorJsonResult(error_code, errmap);
        return getPriKeyAndSign(response, result);
    }

    private String getPriKeyAndSign(HttpServletResponse response, String result) {
        String privateKey = getPrivateKey();
        return ApiSignUtils.sign_and_return_result(response, result, privateKey);
    }

    public String signErrorResult(HttpServletResponse response, int error_code, String error_msg, String key, Object data) {
        String result = jsonViewResolver.errorJsonResult(error_code, error_msg, key, data);
        return getPriKeyAndSign(response, result);
    }

    public String signSucResult(HttpServletResponse response, Map<String, Object> map) {
        String result = jsonViewResolver.sucJsonResult(null, map);
        return getPriKeyAndSign(response, result);
    }

    protected static long TIMEOUT = 1000 * 60 * 60 * 24 * 5;

    private String getPrivateKey() {
        Cache cache = cacheManager.getCache(DictionaryCacheSpec.CACHE_KEY);
        DictionaryCache dictionaryCache = cache.get(DictionaryCode.PLATFORM_PRI_KEY.getCode(), DictionaryCache.class);
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

    public HashMap<String, String> getAllParameters(HttpServletRequest request) {
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

    protected void validatedRequestModel(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return;
        }
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError error : errors) {
            stringBuilder.append(error.getField()).append(":").append(error.getDefaultMessage()).append(" ,");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        String formattedMessage = MessageFormat.format(ApiMessage.INVALID_PARAMS.getMessage(), stringBuilder);
        throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), formattedMessage);
    }
}
