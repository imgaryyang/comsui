package com.zufangbao.earth.security;

import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.security.SystemMenu;
import com.zufangbao.sun.yunxin.service.SystemMenuService;
import com.zufangbao.sun.yunxin.service.SystemRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.StringUtils;

import java.util.*;

public class FilterInvocationSecurityMetadataSourceBySDF implements
		FilterInvocationSecurityMetadataSource, InitializingBean {

	private final static Map<String, Collection<ConfigAttribute>> dbStoreRoleResource=new HashMap<String, Collection<ConfigAttribute>>();
	
	@Autowired
	private PrincipalService principalService;
	
	@Autowired
	private SystemMenuService systemMenuService;
	@Autowired
	private SystemRoleService systemRoleService;
	
	private Map<String, Object> url2submenu() {
		Map<String, Object> map = new HashMap<>();
		
		List<SystemMenu> systemMenus = systemMenuService.loadAll(SystemMenu.class);
		systemMenus.forEach((m)->{
			if (!StringUtils.isEmpty(m.getUrl())) {
				String url = "/" + m.getUrl().split("\\#")[0];
				map.put(url, m.getMkey());
			}
		});
		
		return map;
	}
	
	private Map<String, Object> resourcemap() {
		Map<String, Object> resmap = new HashMap<String, Object>();
		Map<String, Map<String, List<String>>> resources = new HashMap<String, Map<String, List<String>>>();
		List<Map<String, Object>> dbmaps = systemRoleService.getSystemMenuByRole4Cache();
		for (Map<String, Object> dbmap:dbmaps) {
			Map<String, List<String>> resource = new HashMap<String, List<String>>();
			if (resources.get(dbmap.get("role_name").toString()) != null) {
				resource = resources.get(dbmap.get("role_name").toString());
			}
			insertResources(dbmap, resource);
			resources.put(dbmap.get("role_name").toString(), resource);
		}
		
		resmap.putAll(resources);
		return resmap;
	}
	
	// TODO filter url with roles
	@SuppressWarnings("unchecked")
	@Override
	public Collection<ConfigAttribute> getAttributes(Object fi)
			throws IllegalArgumentException {
		// loadResourceDefine();
		
		String requestUrl = ((FilterInvocation) fi).getRequestUrl();
		String reqURL=requestUrl.split("\\?")[0].replace("#", "");
		
		Map<String, Object> map = url2submenu();
		Map<String, Object> resmap = resourcemap();
		
		Collection<ConfigAttribute> roleList = new HashSet<ConfigAttribute>();
		
		List<Principal> principals = principalService.loadAll(Principal.class);
		// 全局链接, 所有角色可访问
		if (map.get(reqURL) == null || ("/post-update-password#/system/update-password").equals(reqURL)) {
			for (Principal principal:principals) {
				if (principal.getAuthority() != null) {
					ConfigAttribute roleCa = new SecurityConfig(principal.getAuthority());
					roleList.add(roleCa);
				}
			}
			return roleList;
		} else {
			for (Principal principal:principals) {
				if (principalCanVisit(principal, resmap, reqURL)) {
					ConfigAttribute roleCa = new SecurityConfig(principal.getAuthority());
					roleList.add(roleCa);
				}
			}
			if (CollectionUtils.isEmpty(roleList)) {
				ConfigAttribute roleCa = new SecurityConfig("forbidden");
				roleList.add(roleCa);
			}
			return roleList;
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean principalCanVisit(Principal principal, Map<String, Object> resmap, String url) {
		String[] authorities = principal.getAuthority().split(",");
		for (String auth:authorities) {
			if (resmap.get(auth) != null) {
				Map<String, List<String>> map = (Map<String, List<String>>) resmap.get(auth);
				if (map.get("urls").contains(url)) {
					return true;
				}
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
			// remained urls
//			urls.add("data");
			urls.add("/welcome");
		}
		
		Integer lvl = Integer.parseInt(dbmap.get("lvl").toString());
		if (lvl == 0)
			menus.add(dbmap.get("mkey").toString());
		else
			submenus.add(dbmap.get("mkey").toString());
		if (!StringUtils.isEmpty(dbmap.get("url").toString()))
			urls.add("/" + dbmap.get("url").toString().split("\\#")[0]);
		
		Resource.put("menus", menus);
		Resource.put("submenus", submenus);
		Resource.put("urls", urls);
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {}

}
