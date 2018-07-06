package com.zufangbao.earth.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class AccessDeniedHandlerBySDF implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		boolean isAjax = isAjaxRequest(request);
		if (isAjax) {
			String msg = "{\"code\" : -1, \"message\" : \"操作权限不足！\"}";
			response.setContentType("json");
			OutputStream out = response.getOutputStream();
			out.write(msg.getBytes());
			out.flush();
		}else {
			request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	/**
	 * 判断是否为AJAX请求
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
	
}
