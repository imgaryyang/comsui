package com.suidifu.morganstanley.interceptor;

import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.gluon.util.RequestUtil;
import com.zufangbao.sun.yunxin.entity.api.ApiStatus;
import com.zufangbao.sun.yunxin.entity.api.TApiConfig;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TApiConfigService;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 接口访问拦截器
 *
 * @author zhanghongbing
 */
@Log4j2
public class ApiAccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private TApiConfigService tApiConfigService;

    @Autowired
    private ApiJsonViewResolver jsonViewResolver;

    @Autowired
    private TMerConfigService tMerConfigService;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String requestURI = request.getRequestURI();
            String apiUrl = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;

            log.info("#IP: {}", IpUtil.getIpAddress(request));
            log.info("#API : {}", apiUrl);
            log.info("#PARAMS : {}", request.getParameterMap());

            if (checkApiConfig(response, apiUrl)) {
                return false;
            }

            verifySign(request);

        } catch (RuntimeException e) {
            return processRuntimeException(response, e);
        } catch (Exception e) {
            writeJsonResult(response, ApiResponseCode.SYSTEM_ERROR);
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    /**
     * 校验接口是否存在
     *
     * @param response
     * @param apiUrl
     * @return
     * @throws IOException
     */
    public boolean checkApiConfig(HttpServletResponse response, String apiUrl) throws IOException {
        boolean isPreApi = apiUrl.contains(PreApiControllerSpec.PRE_API);
        if (isPreApi) {
            return false;
        }

        List<TApiConfig> tApiConfigs = tApiConfigService.getApiConfigListBy(apiUrl);
        if (CollectionUtils.isEmpty(tApiConfigs)) {
            writeJsonResult(response, ApiResponseCode.API_NOT_FOUND);
            return true;
        }

        TApiConfig tApiConfig = tApiConfigs.get(0);
        if (tApiConfig.getApiStatus() == ApiStatus.CLOSED) {
            writeJsonResult(response, ApiResponseCode.API_UNAVAILABLE);
            return true;
        }
        return false;
    }

    private boolean processRuntimeException(HttpServletResponse response, RuntimeException e) throws IOException {
        log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));

        if (e instanceof ApiException) {
            int errorCode = ((ApiException) e).getCode();
            switch (errorCode) {
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

    private void verifySign(HttpServletRequest request) {
        String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);
        String secret = request.getHeader(ApiConstant.PARAMS_SECRET);
        validateHeader(merId, secret);
        TMerConfig merConfig = tMerConfigService.getTMerConfig(merId, secret);
        Map<String, String> requestParams = RequestUtil.getRequestParams(request);
        if (!ApiSignUtils.verifySign(requestParams, request, merConfig)) {
            throw new ApiException(ApiResponseCode.SIGN_VERIFY_FAIL);
        }
    }

    private void validateHeader(String merId, String secret) {
        if (StringUtils.isEmpty(merId) || StringUtils.isEmpty(secret)) {
            throw new ApiException(ApiResponseCode.SIGN_MER_CONFIG_ERROR);
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

    public String signErrorResult(HttpServletResponse response, int errorCode) {
        String result = jsonViewResolver.errorJsonResult(errorCode);
        String privateKey = dictionaryService.getPlatformPrivateKey();
        return ApiSignUtils.signAndReturnResult(response, result, privateKey);
    }
}