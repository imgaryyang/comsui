/**
 * 
 */
package com.demo2do.core.security.expression;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import com.demo2do.core.security.details.SecurityUserDetails;

/**
 * @author Downpour
 */
public class PrincipalExpressionRoot extends WebSecurityExpressionRoot {

	/**
	 * @param authentication
	 * @param filterInvocation
	 */
	public PrincipalExpressionRoot(Authentication authentication, FilterInvocation filterInvocation) {
		super(authentication, filterInvocation);
	}

	/**
	 * 
	 * @param roles
	 * @return
	 */
	public boolean hasAnyPrincipalRole(String... roles) {
		Object principal = super.getPrincipal();
		if(principal != null && (principal instanceof SecurityUserDetails)) {
			return ((SecurityUserDetails) principal).hasAnyPrincipalRole(roles);
		}
		return false;
	}
	
	/**
	 * 
	 * @param type
	 * @param key
	 * @return
	 */
	public boolean hasResource(String type, String key) {
		Object principal = super.getPrincipal();
		if(principal != null && (principal instanceof SecurityUserDetails)) {
			return ((SecurityUserDetails) principal).hasResource(type, key);
		}
		return false;
	}
	
}
