package com.suidifu.morganstanley.aspect;

import com.alibaba.fastjson.JSON;
import com.zufangbao.gluon.util.IpUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 接口性能日志和返回报文
 * @author wjh on 17-11-22.
 */
@Aspect
@Order(1)
@Log4j2
@Component
public class WebLogAspect {
    @Pointcut(value = "execution(public * com.suidifu.morganstanley.controller..*.*(..))")
    public void webLog() {}

    @Around(value = "webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //接受请求,记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = proceedingJoinPoint.getSignature();

        log.info("url: {}", request.getRequestURL().toString());
        log.info("http_method: {}", request.getMethod());
        log.info("request_ip: {}", IpUtil.getIpAddress(request));
        log.info("class_method: {}#{}", signature.getDeclaringTypeName(), signature.getName());
        log.info("args: {}", Arrays.toString(proceedingJoinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object rsp = proceedingJoinPoint.proceed();
            log.info("save request log use {} ms, http response: {}", (System.currentTimeMillis() - startTime), JSON.toJSONString(rsp));
            return rsp;
        }catch (Throwable t){
            //只记录发生的异常和耗时时间,业务异常交由上层处理
            log.error("exception method: {}, occurs exception: {}",signature.getName(), ExceptionUtils.getMessage(t));
            log.error("exception use: {} ms", (System.currentTimeMillis() - startTime));
            throw t;
        }
    }
}
