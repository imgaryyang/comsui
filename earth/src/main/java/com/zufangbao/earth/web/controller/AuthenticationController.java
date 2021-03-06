/**
 *
 */
package com.zufangbao.earth.web.controller;

import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.SystemLogService;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.RecordLogCore;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * @author Downpour
 */
@Controller
public class AuthenticationController {

	private static final Log logger = LogFactory.getLog(AuthenticationController.class);

	@Resource
	private RecordLogCore recordLogCore;
	@Resource
	private SystemOperateLogService systemOperateLogService;
	@Resource
	private SystemLogService userLoginLogService;


	@RequestMapping("/403")
	public String RediectUnthorizedPage(){
		return "403";
	}

	/**
	 * Session invalid redirect
	 *
	 * @return
	 */
	@RequestMapping("/timeout")
	public String timeout(RedirectAttributes redirectAttributes) {

		logger.info("Session timeout ... The system will redirect to login page");

		// add a flag into session to display on login page
		redirectAttributes.addFlashAttribute("timeoutErrorMessage", "security.authentication.timeout");

		return "redirect:/login";
	}

	/**
	 *
	 * @param httpSession
	 * @return
	 */
	@RequestMapping(value = { "/", "/login" })
	public String login(HttpServletRequest request, HttpSession httpSession, @ModelAttribute("timeoutErrorMessage") String timeoutErrorMessage) {

		// Trying to get AuthenticationException from HttpSession
		AuthenticationException authenticationException = (AuthenticationException) httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

		// when authentication exception is found, setting up error message to display
		if (authenticationException != null) {

			if (logger.isErrorEnabled()) {
				logger.error("Authentication process exception: " + authenticationException.getMessage());
			}

			request.setAttribute("errorMessage", "security.authentication.fail");

			if (authenticationException.getCause() instanceof LockedException) {
				request.setAttribute("errorMessage", authenticationException.getMessage());
			}

			// remove AuthenticationException from httpSession
			httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

			return "login";
		}

		return "login";
	}

	/**
	 * Execute post login process
	 *
	 * @return
	 */
	@RequestMapping("/post-login")
	public String postLogin(@Secure Principal principal, HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = IpUtil.getIpAddress(request);
        generateUserLoginLog(principal,ipAddress);
		return "redirect:" + principal.getIndexURL();
	}

	public void generateUserLoginLog(Principal principal,String ipAddress) {
		SystemOperateLog log = new SystemOperateLog(principal.getId(),ipAddress,LogFunctionType.LOGIN,LogOperateType.LOGIN);
		log.setRecordContent("用户"+principal.getUsername()+"登陆成功");
		systemOperateLogService.save(log);
	}

//	public void generateUserLoginLog(Principal principal,String ip) {
//		SystemLog userLog = create_user_login_event(principal,ip);
//		this.userLoginLogService.save(userLog);
//	}
//
//	private SystemLog create_user_login_event(Principal principal,String ip) {
//		SystemLog userLog = new SystemLog();
//		userLog.setContent("用户"+principal.getUsername()+"登陆成功");
//		userLog.setEvent("登陆");
//		userLog.setLogType(LogType.LOGIN);
//		userLog.setOperateTime(new Date());
//		userLog.setUserId(principal.getId());
//		userLog.setIp(ip);
//		return userLog;
//	}

}
