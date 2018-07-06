package com.suidifu.morganstanley.interceptor;

import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web访问拦截器
 *
 * @author louguanyang at 2017/11/1 21:12
 * @mail louguanyang@hzsuidifu.com
 */
@Log4j2
public class WebAccessInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private DictionaryService dictionaryService;

    /**
     * JSON web token: 1.header，2.payload，3.signature
     */
    private static final String AUTHORIZATION = "authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqUid = UUIDUtil.random32UUID();
        String requestURI = request.getRequestURI();
        String apiUrl = requestURI.endsWith("/") ? requestURI.substring(0, requestURI.length() - 1) : requestURI;
        log.info("#Web Request UUID:{}, #IP:{}", reqUid, IpUtil.getIpAddress(request));
        log.info("#Web Request UUID:{}, #API:{}", reqUid, apiUrl);
        log.info("#Web Request UUID:{}, #PARAMS:{}", reqUid, request.getParameterMap());

        String jwt = request.getHeader(AUTHORIZATION);
        dictionaryService.getPlatformPrivateKey();

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        super.postHandle(request, response, handler, modelAndView);
    }
}
