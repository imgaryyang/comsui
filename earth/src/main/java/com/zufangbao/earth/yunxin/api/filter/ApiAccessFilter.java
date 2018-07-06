package com.zufangbao.earth.yunxin.api.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.service.SecretKeyService;
import com.zufangbao.earth.service.RequestLogService;

import com.zufangbao.earth.util.IpUtil;
import com.zufangbao.earth.yunxin.api.constant.ApiConstant;
import com.zufangbao.earth.yunxin.api.exception.ApiResponseCode;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.util.RequestUtil;
import com.zufangbao.gluon.util.SecurityUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.MessageSource;

import com.zufangbao.sun.yunxin.entity.RequestLog;
import com.zufangbao.sun.yunxin.entity.SecretKey;
import com.zufangbao.sun.yunxin.entity.api.ApiStatus;
import com.zufangbao.sun.yunxin.entity.api.EncryptionMode;
import com.zufangbao.sun.yunxin.entity.api.TApiConfig;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.TApiConfigService;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

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
    @Autowired
    private PrincipalService principalService;
    @Autowired
    private SecretKeyService secretKeyService;
    @Autowired
    private RequestLogService requestLogService;

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

            String clientIp = IpUtil.getIpAddress(request);

            logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#" + DateUtils.getCurrentTimeMillis() + " : " + clientIp
                + " CALL #API : " + apiUrl + " #PARAMS : " + JsonUtils
                .toJsonString(request.getParameterMap()));

            //校验接口是否存在
            List<TApiConfig> tApiConfigs = tApiConfigService.getApiConfigListBy(apiUrl);
            if (CollectionUtils.isEmpty(tApiConfigs)) {
                writeJsonResult(response, ApiResponseCode.API_NOT_FOUND);
                return;
            }

            /**
             * 客户端请求，不需要验签，仅限注册申请和查询授权结果接口
             * @see ApiClientController#registerUserRole()
             * @see ApiClientController#queryRegisterApplicationResult()
             */
            
            boolean isClientRequest = apiUrl.startsWith("/api/client/");
            TMerConfig merConfig = null;
            if(!isClientRequest){
            	merConfig = verifyMerconfig(request.getHeader(ApiConstant.PARAMS_MER_ID),request.getHeader(ApiConstant.PARAMS_SECRET));
            }
            String sign = request.getHeader(ApiConstant.PARAMS_SIGN);
            Map<String, String> requestParams = new HashMap<>();
            String content = "";

            // 非加密模式
            if (isClientRequest || null == merConfig.getEncryptionMode() || EncryptionMode.NONE.equals(merConfig.getEncryptionMode())) {
                requestParams = RequestUtil.getRequestParams(request);
                content = ApiSignUtils.getSignCheckContent(requestParams);
            } else {
                //加密模式
                //解密
                String requestBody = RequestUtil.getRequestBody(request);
                content = requestBody;

                logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#" + clientIp + " CALL #API : " + apiUrl);
                String decryptString;
                EncryptionMode encryptionMode = merConfig.getEncryptionMode();
                switch (encryptionMode) {
                    case THREE_DES:
                        try {
                            decryptString = SecurityUtil.threeDesDecrypt(requestBody, merConfig.getEncryptionKey());
                            logger.info(
                                GloableLogSpec.AuditLogHeaderSpec() + "#decrypt[SUCC],decryptString:" + decryptString);
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
                ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(request);
                requestWrapper.addAllParameters(JsonUtils.parse(decryptString));
                // 要测试！！！
                request = requestWrapper;

                requestParams = JSON.parseObject(decryptString, new TypeReference<Map<String, String>>() {});
            }

            logger.info(GloableLogSpec.AuditLogHeaderSpec() + "#" + DateUtils.getCurrentTimeMillis() + " : " + clientIp
                + " CALL #API : " + apiUrl + " #PARAMS : " + JsonUtils
                .toJsonString(requestParams));

            boolean systemHasFnCode = tApiConfigs.stream()
                .anyMatch(tApiConfig -> StringUtils.isNotEmpty(tApiConfig.getFnCode()));
            TApiConfig tApiConfig = tApiConfigs.get(0);
            if (systemHasFnCode) {
                //校验接口功能代码是否存在
                String fnCode = requestParams.getOrDefault(ApiConstant.PARAMS_FN_KEY, StringUtils.EMPTY);

                logger.info(
                    GloableLogSpec.AuditLogHeaderSpec() + "#" + DateUtils.getCurrentTimeMillis() + " : " + clientIp
                        + " CALL #API : " + apiUrl + " #FN : " + fnCode);

                Optional<TApiConfig> optionalTApiConfig = tApiConfigs.stream()
                    .filter(tac -> StringUtils.equals(tac.getFnCode(), fnCode))
                    .findFirst();
                if (fnCode == null || !optionalTApiConfig.isPresent()) {
                    writeJsonResult(response, ApiResponseCode.INVALID_FN_CODE);
                    return;
                }
                tApiConfig = optionalTApiConfig.get();
            }
            //接口启用状态校验
            if (tApiConfig.getApiStatus() == ApiStatus.CLOSED) {
                writeJsonResult(response, ApiResponseCode.API_UNAVAILABLE);
                return;
            }
            
            
            boolean rsaCheckContent = true;
            if(isClientRequest && !apiUrl.startsWith("/api/client/n/")){
            	String sourceUuid = request.getParameter("sourceUuid");
            	Long principalId = principalService.getPrincipalIdBysourceUuid(sourceUuid);
            	List<SecretKey>secretKeys = secretKeyService.getSecretKeyByPrincipalId(principalId);
            	for(SecretKey secretKey :secretKeys){
            		String publicKey = secretKey.getPublicKey();
                    if(rsaCheckContent = ApiSignUtils.rsaCheckContent(content, sign, publicKey)){
                    	RequestLog requestLog = new RequestLog(clientIp,sourceUuid,secretKey.getPublicKeyUuid(),MessageSource.CLIENT.ordinal(),apiUrl);
                    	requestLogService.save(requestLog);
                    	break;
                    }
            	}
            }else if(!isClientRequest){
            	 String publicKey = merConfig.getPubKey();
                 //验签
                 rsaCheckContent = ApiSignUtils.rsaCheckContent(content, sign, publicKey);
                 logger.info(
                     System.currentTimeMillis() + ", ip［" + clientIp + "］, 内容验签: content［" + content + "］" + ",sign［" + sign
                         + "］");
            }
           
            if (!rsaCheckContent) {
                writeJsonResult(response, ApiResponseCode.SIGN_VERIFY_FAIL);
                return;
            }

            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            processRuntimeException((HttpServletResponse) servletResponse, e);
        } catch (Exception e) {
            logger.info("***ApiAccessFilter***");
            e.printStackTrace();
            writeJsonResult((HttpServletResponse) servletResponse, ApiResponseCode.SYSTEM_ERROR);
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


    private TMerConfig verifyMerconfig(String merId, String secret) throws Exception {
        vaildateHeader(merId, secret);
        TMerConfig merConfig = tMerConfigService.getTMerConfig(merId, secret);
        if (null == merConfig) {
            throw new ApiException(ApiResponseCode.NOT_EXIST_MER_ERROR);
        }
        return merConfig;
    }

    private void vaildateHeader(String merId, String secret) {
        if (StringUtils.isEmpty(merId) || StringUtils.isEmpty(secret)) {
            throw new ApiException(ApiResponseCode.SIGN_MER_CONFIG_ERROR);
        }
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
