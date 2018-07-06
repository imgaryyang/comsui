package com.zufangbao.earth.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.handler.SystemButtonHandler;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.security.SystemButton;
import com.zufangbao.sun.yunxin.service.SystemRoleService;
import com.zufangbao.sun.yunxin.service.privilege.SystemButtonService;

/**
 * author: victory_always mail: wukai@hzsuidifu.com time: 2018-01-04 12:17 权限url拦截器
 */
public class AuthenticationUrlFilter implements Filter {

	private static Log LOGGER = LogFactory.getLog(AuthenticationUrlFilter.class);

	@Autowired
	private SystemRoleService systemRoleService;

	private PathMatcher antPathMatcher = new AntPathMatcher();

	private Set<String> excludeUrlPathRegexSet = null;
	
	@Autowired
	private SystemButtonHandler systemButtonHandler;
	
	@Autowired
	private SystemButtonService systemButtonService;

	private boolean open;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		long startTime = System.currentTimeMillis();

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		try {

			if (!open) {

				chain.doFilter(request, response);
				
				return;
			}

			String path = httpServletRequest.getServletPath();

			for (String urlRegex : excludeUrlPathRegexSet) {

				if (antPathMatcher.match(urlRegex, path)) {

					chain.doFilter(httpServletRequest, response);

					return;
				}
			}

			Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (null == principal) {

				LOGGER.error("#AuthenticationUrlFilter# user not found");

				wrapErrorResponse(httpServletRequest, httpServletResponse, -1, "用户不存在，无权限操作！");

				return;
			}

			String reqURL = path.split("\\?")[0].replace("#", "");

			if (!principalCanVisit(principal, resourcemap(), reqURL)) {

				LOGGER.error("#AuthenticationUrlFilter# user can't access url[" + reqURL + "]");

				wrapErrorResponse(httpServletRequest, httpServletResponse, -1, "用户无权限操作该url["+reqURL+"]！");

				return;
			}
			
			chain.doFilter(httpServletRequest, httpServletResponse);

		} catch (Exception e) {

			e.printStackTrace();

			LOGGER.error(
					"#AuthenticationUrlFilter# occur error, but can't continue,use time["
							+ (System.currentTimeMillis() - startTime) + "]ms)");

			wrapErrorResponse(httpServletRequest, httpServletResponse, -1, "用户权限有问题不能继续访问！");
		}
	}
	private void wrapErrorResponse(HttpServletRequest request, HttpServletResponse response, int code, String message)
			throws IOException {

		if (isAjaxQuery(request)) {

			Map<String, Object> errMap = new HashMap<>();

			errMap.put("code", code);
			errMap.put("message", message);

			wrapJsonesponse(response, errMap);

		} else {

			response.sendRedirect(request.getContextPath() + "/403");
		}

	}

	private void wrapJsonesponse(HttpServletResponse response, Map<String, Object> messageMap) throws IOException {

		response.setContentType("application/json;charset=utf-8");

		OutputStream out = response.getOutputStream();

		out.write(JsonUtils.toJsonString(messageMap).getBytes());

		out.flush();

		out.close();
	}

	public Set<String> getExcludeUrlPathRegexSet() {
		return excludeUrlPathRegexSet;
	}

	public void setExcludeUrlPathRegexSet(Set<String> excludeUrlPathRegexSet) {
		this.excludeUrlPathRegexSet = excludeUrlPathRegexSet;
	}
	private Map<String, Object> resourcemap() {

		Map<String, Object> resmap = new HashMap<String, Object>();

		Map<String, Map<String, List<String>>> resources = new HashMap<String, Map<String, List<String>>>();

		List<Map<String, Object>> dbmaps = systemRoleService.getSystemMenuByRole4Cache();

		for (Map<String, Object> dbmap : dbmaps) {

			Map<String, List<String>> resource = new HashMap<String, List<String>>();

			String roleName = dbmap.get("role_name").toString();

			if (resources.get(roleName) != null) {

				resource = resources.get(roleName);
			}
			insertResources(dbmap, resource);

			resources.put(roleName, resource);
		}

		resmap.putAll(resources);

		return resmap;
	}

	private boolean principalCanVisit(Principal principal, Map<String, Object> resmap, String url) {
		
		String[] authorities = principal.getAuthority().split(",");
		
		for (String auth : authorities) {
			if (resmap.get(auth) != null) {
				Map<String, List<String>> map = (Map<String, List<String>>) resmap.get(auth);
				if (map.get("urls").contains(url)) {
					return true;
				}
			}
		}
		
		List<String> ajaxUrlList = extractAjaxUrlFromSystemButtonBy(principal);
		
		for (String ajaxUrlRegex : ajaxUrlList) {
			
			if(Pattern.matches(ajaxUrlRegex, url)) 
			{
				return true;
			}
		}
		return false;
	}

	private void insertResources(Map<String, Object> dbmap, Map<String, List<String>> Resource) {
		List<String> menus = new ArrayList<>();
		List<String> submenus = new ArrayList<>();
		List<String> urls = new ArrayList<>();
		if (Resource.get("menus") != null) {
			menus = Resource.get("menus");
		}
		if (Resource.get("submenus") != null) {
			submenus = Resource.get("submenus");
		}
		if (Resource.get("urls") != null) {
			urls = Resource.get("urls");
		} else {
			urls.add("/welcome");
			urls.add("/get-auth-menus");
			urls.add("/get-auth-buttons");
		}

		Integer lvl = Integer.parseInt(dbmap.get("lvl").toString());
		if (lvl == 0)
			menus.add(dbmap.get("mkey").toString());
		else
			submenus.add(dbmap.get("mkey").toString());

		String url = dbmap.get("url").toString();

		if (!org.springframework.util.StringUtils.isEmpty(url)) {

			if (url.indexOf("#") > -1) {

				urls.add(url.split("\\#")[1]);
			}

		}
		Resource.put("menus", menus);
		Resource.put("submenus", submenus);
		Resource.put("urls", urls);
	}

	private boolean isAjaxQuery(HttpServletRequest request) {
		return !com.demo2do.core.utils.StringUtils.isEmpty(request.getHeader("X-Requested-With"));
	}
	
	private List<String> extractAjaxUrlFromSystemButtonBy(Principal principal){
      //根据用户信息获取该用户的有权限的按钮模块的id集合
      List<Long> linkPrivilegeButtonIds = systemButtonHandler
          .getLinkPrivilegeButtonId(principal.getId());
      //通过有权限的按钮模块集合获取相应的可访问按钮id集合
      List<Long> userOwnButtonIds = systemButtonHandler
          .getSystemButtonIdsByPrivilege(linkPrivilegeButtonIds);
      //对访问的按钮id集合进行过滤(通过父级菜单来过滤)
      List<SystemButton> allButtons = systemButtonService.findSystemButtonListByIds(userOwnButtonIds);
      
    	  return allButtons.stream().filter(a->{return StringUtils.isNotEmpty(a.getUrl());}).map(a->{return a.getUrl();}).collect(Collectors.toList());
	}
	

	public boolean getOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public void destroy() {

	}
}
