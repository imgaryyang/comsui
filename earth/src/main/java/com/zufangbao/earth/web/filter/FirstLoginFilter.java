package com.zufangbao.earth.web.filter;

import com.demo2do.core.utils.StringUtils;
import com.zufangbao.earth.cache.handler.impl.CashFlowCacheHandlerImpl;
import com.zufangbao.sun.entity.security.Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class FirstLoginFilter implements Filter {
	@Autowired
	private static Log logger = LogFactory.getLog(CashFlowCacheHandlerImpl.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletReqeust, ServletResponse serveletResponse, FilterChain chain)
			throws IOException, ServletException {
		// 获取用户信息
		Principal userDetails = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = (HttpServletResponse) serveletResponse;
		HttpServletRequest request = (HttpServletRequest) servletReqeust;
		if(userDetails.has_modified_password() || is_password_updation_operation(request)){
			chain.doFilter(request, response);
			return;
		}
		if(!isAjaxQuery(request)){
			logger.info("#execute FirstLoginFilter ->  redirect post-update-password");
			response.sendRedirect(request.getContextPath() + "/post-update-password#/system/update-password");
			return;
		}
		if(request.getRequestURL().indexOf("/get-auth-menus")!=-1 || request.getRequestURL().indexOf("/get-auth-buttons")!=-1){
			chain.doFilter(request, response);
			return;
		}
		ajaxQeuryHandler(response);
		
	}
	
	
	private void ajaxQeuryHandler(HttpServletResponse response) throws IOException{
		String msg = "{\"code\" : -1, \"message\" : \"操作权限不足！\"}";
		response.setContentType("application/json;charset=utf-8");
		OutputStream out = response.getOutputStream();
		out.write(msg.getBytes());
		out.flush();
		out.close();
	}

	private boolean is_password_updation_operation(HttpServletRequest request) {
		return request.getRequestURL().indexOf("/post-update-password#/system/update-password") != -1
				|| request.getRequestURL().indexOf("update-password") != -1;
	}

	private boolean isAjaxQuery(HttpServletRequest request) {
		return !StringUtils.isEmpty(request.getHeader("X-Requested-With"));
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}