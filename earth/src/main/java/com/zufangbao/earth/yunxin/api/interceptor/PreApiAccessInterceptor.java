package com.zufangbao.earth.yunxin.api.interceptor;

import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前置接口访问拦截器
 * @author louguanyang on 2017/5/3.
 *
 */
public class PreApiAccessInterceptor extends ApiAccessInterceptor{

	private static final Log logger = LogFactory.getLog(PreApiAccessInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		try {
            logger.info("#" + DateUtils.getCurrentTimeMillis() + " : " + IpUtil.getIpAddress(request) + "#PARAMS : " + request.getParameterMap());
//			String requestURI = request.getRequestURI();

			verifySign(request);

		} catch (RuntimeException e) {
			return processRuntimeException(response, e);
		} catch (Exception e) {
			writeJsonResult(response, ApiResponseCode.SYSTEM_ERROR);
			e.printStackTrace();
			return false;
		}

		return true;
	}


}