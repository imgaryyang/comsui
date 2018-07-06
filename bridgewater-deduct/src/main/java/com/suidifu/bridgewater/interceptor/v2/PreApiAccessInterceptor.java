package com.suidifu.bridgewater.interceptor.v2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.suidifu.bridgewater.ApiAccessInterceptor;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;

/**
 * 前置接口访问拦截器
 * Created by louguanyang on 2017/5/3.
 *
 */
public class PreApiAccessInterceptor extends ApiAccessInterceptor {

	private static final Log logger = LogFactory.getLog(PreApiAccessInterceptor.class);



	@Autowired
	private ApiJsonViewResolver jsonViewResolver;


	@Autowired
	private DictionaryService dictionaryService;


	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		try {

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


	public  boolean processRuntimeException(HttpServletResponse response, RuntimeException e) throws IOException {
		e.printStackTrace();
		if( e instanceof ApiException ) {
			int error_code = ((ApiException) e).getCode();
			switch (error_code) {
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


	public  void writeJsonResult(HttpServletResponse response, int code) throws IOException {
		response.setContentType("application/json");
 		response.setCharacterEncoding("utf-8");
 		PrintWriter out = response.getWriter();
 		String jsonStr = signErrorResult(response, code);
 		out.write(jsonStr);
 		out.close();
	}

	@Override
    public String signErrorResult(HttpServletResponse response, int error_code) {
		String result = jsonViewResolver.errorJsonResult(error_code);
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
}
