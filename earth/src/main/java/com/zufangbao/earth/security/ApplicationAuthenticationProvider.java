/**
 * 
 */
package com.zufangbao.earth.security;

import com.demo2do.core.cache.CompositeCacheAccessor;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.sun.entity.security.Principal;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

/**
 * 
 * @author downpour
 */
public class ApplicationAuthenticationProvider implements UserDetailsService {

	private static final Log logger = LogFactory.getLog(ApplicationAuthenticationProvider.class);
	
	private CompositeCacheAccessor cacheAccessor;
	
	private PrincipalService principalService;
	
	/**
	 * @param cacheAccessor the cacheAccessor to set
	 */
	public void setCacheAccessor(CompositeCacheAccessor cacheAccessor) {
		this.cacheAccessor = cacheAccessor;
	}
	
	/**
	 * @param principalService the principalService to set
	 */
	public void setPrincipalService(PrincipalService principalService) {
		this.principalService = principalService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Principal principal = principalService.getPrincipal(username);

		if (principal == null) {
			throw new UsernameNotFoundException("ApplicationAuthenticationProvider#loadUserByUsername() - user not found.");
		}

		logger.info("ApplicationAuthenticationProvider#loadUserByUsername() - user " + principal.getName() + "[" + principal.getAuthority() + "] is found to login. ");
		
		// ======= initialize other user properties =======
		
		// 1. initialize role based resources 
		// Map<String, List<String>> resources = (Map<String, List<String>>) cacheAccessor.evaluate("resources['" + principal.getAuthority() + "']");
		
		// 2. initialize role based on mysqldb by principal
		Map<String, List<String>> resources = new HashMap<String, List<String>>(); 
		String authString = principal.getAuthority();
		String[] auths = authString.split(",");
		for (String auth:auths) {
			Map<String, List<String>> resource = (Map<String, List<String>>) cacheAccessor.evaluate("resources['" + auth + "']");
			if (MapUtils.isNotEmpty(resource)) {
				AppendToResources(resource, resources);
			}
		}
		
		if(MapUtils.isNotEmpty(resources)) {
			principal.initResources(resources);
		}
		return principal;
	}
	
	private void AppendToResources(Map<String, List<String>> resource, Map<String, List<String>> resources) {
		List<String> menus = new ArrayList<String>();
		List<String> submenus = new ArrayList<>();
		List<String> urls = new ArrayList<>();
		
		if (resources.get("menus") != null) 
			menus = resources.get("menus");
		if (resources.get("submenus") != null)
			submenus = resources.get("submenus");
		if (resources.get("urls") != null)
			urls = resources.get("urls");
		
		if (resource.get("menus") != null)
			menus.addAll(resource.get("menus"));
		if (resource.get("submenus") != null)
			submenus.addAll(resource.get("submenus"));
		if (resource.get("urls") != null)
			urls.addAll(resource.get("urls"));
		
		resources.put("menus", new ArrayList<String>(new HashSet<String>(menus)));
		resources.put("submenus", new ArrayList<String>(new HashSet<String>(submenus)));
		resources.put("urls", new ArrayList<String>(new HashSet<String>(urls)));
	}

}
