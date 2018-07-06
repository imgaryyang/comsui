package com.suidifu.morganstanley.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * User:frankwoo(吴峻申) <br>
 * Date:2016-11-3 <br>
 * Time:13:42 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@ControllerAdvice//如果返回的为json数据或其它对象，添加该注解
@ResponseBody
@Log4j2
public class GlobalExceptionHandler {//添加全局异常处理流程，根据需要设置需要处理的异常
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Resource
    private DictionaryService dictionaryService;

    /**
     * 获取BaseResponse响应实体类，并且将sign放入Header
     *
     * @param request      http请求对象
     * @param response     http响应对象
     * @param apiException 自定义异常类
     * @return http响应返回的错误信息实体类
     * @throws JsonProcessingException 返回的JsonProcessingException
     */
    @ExceptionHandler(value = {ApiException.class})
    public Object methodArgumentNotValidHandler(HttpServletRequest request,
                                                HttpServletResponse response,
                                                ApiException apiException) throws JsonProcessingException {
        log.error("#occur ApiException [requestNo={},ip={}]!", request.getParameter("requestNo"), IpUtil.getIpAddress(request));

        BaseResponse baseResponse = new BaseResponse();
        if (apiException != null) {
            int code = apiException.getCode();
            String msg = apiException.getMessage();
            baseResponse.setCode(code);
            baseResponse.setMessage(StringUtils.isEmpty(msg) ? ApiMessageUtil.getMessage(code) : msg);
        }

        String result = OBJECT_MAPPER.writeValueAsString(baseResponse);
        String privateKey = dictionaryService.getPlatformPrivateKey();

        ApiSignUtils.sign2Header(response, result, privateKey);
        return baseResponse;
    }


    /**
     * 获取BaseResponse响应实体类，并且将sign放入Header
     *
     * @param request                http请求对象
     * @param response               http响应对象
     * @param globalRuntimeException 自定义异常类
     * @return http响应返回的错误信息实体类
     * @throws JsonProcessingException 返回的JsonProcessingException
     */
    @ExceptionHandler(value = {GlobalRuntimeException.class})
    public Object methodArgumentNotValidHandler(HttpServletRequest request,
                                                HttpServletResponse response,
                                                GlobalRuntimeException globalRuntimeException) throws JsonProcessingException {
        log.error("#occur GlobalRuntimeException [requestNo={},ip={}]!", request.getParameter("requestNo"), IpUtil.getIpAddress(request));

        BaseResponse baseResponse = new BaseResponse();

        if (globalRuntimeException != null) {
            int code = globalRuntimeException.getCode();
            String msg = globalRuntimeException.getMessage();
            baseResponse.setCode(code);
            baseResponse.setMessage(StringUtils.isEmpty(msg) ? ApiMessageUtil.getMessage(code) : msg);
        }

        String result = OBJECT_MAPPER.writeValueAsString(baseResponse);
        String privateKey = dictionaryService.getPlatformPrivateKey();

        ApiSignUtils.sign2Header(response, result, privateKey);
        return baseResponse;
    }

    /**
     * 获取BaseResponse响应实体类，并且将sign放入Header
     *
     * @param request   http请求对象
     * @param response  http响应对象
     * @param exception Exception异常类
     * @return http响应返回的错误信息实体类
     * @throws JsonProcessingException 返回的JsonProcessingException
     */
    @ExceptionHandler(value = {Exception.class})
    public Object methodArgumentNotValidHandler(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Exception exception) throws JsonProcessingException {
        log.error("#occur Exception [requestNo={},ip={},getFullStackTrace={}]!", request.getParameter("requestNo"), IpUtil.getIpAddress(request), ExceptionUtils.getFullStackTrace(exception));

        BaseResponse baseResponse = new BaseResponse();
        if (exception != null){
            baseResponse.setCode(ApiMessage.SYSTEM_ERROR.getCode());
        }

        String result = OBJECT_MAPPER.writeValueAsString(baseResponse);
        String privateKey = dictionaryService.getPlatformPrivateKey();

        ApiSignUtils.sign2Header(response, result, privateKey);
        return baseResponse;
    }
}