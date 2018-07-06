package com.zufangbao.earth.web.filter;

import com.demo2do.core.utils.StringUtils;
import com.google.code.kaptcha.Constants;
import com.zufangbao.earth.kaptcha.VerificationCodeConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CaptchaVerifierFilter extends OncePerRequestFilter{
	@Autowired
	private VerificationCodeConfigProperties properties;
	
	public VerificationCodeConfigProperties getProperties() {
		return properties;
	}

	public void setProperties(VerificationCodeConfigProperties properties) {
		this.properties = properties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(!StringUtils.isEmpty(properties.getIsOpen())&&properties.getIsOpen().equals("false")){
			filterChain.doFilter(request, response);
			return;
		}
		
		if(!is_login_request(request)){//过滤器只针对登录
			filterChain.doFilter(request, response);
			return;
		}
		
		HttpSession session = request.getSession(); 
		
	    if(!is_through_validation(session,request)){
	    	session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
			session.setAttribute("captchaErrorMessage", "security.authentication.captcha.fail");
			response.sendRedirect(request.getContextPath()+"/login");
			return;
	    }
		session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
		filterChain.doFilter(request, response);
	}

	private boolean is_login_request(HttpServletRequest request) {
		return request.getRequestURI().indexOf("j_spring_security_check")!=-1;
	}

	private boolean is_through_validation(HttpSession session,HttpServletRequest request) {
		String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY); 
	    String captcha = request.getParameter("j_captcha");
		return !StringUtils.isEmpty(code) && !StringUtils.isEmpty(captcha) && code.equalsIgnoreCase(captcha);
	}
	
}
