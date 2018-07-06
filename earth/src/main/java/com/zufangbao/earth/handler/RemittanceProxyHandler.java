/**
 * 
 */
package com.zufangbao.earth.handler;

import com.zufangbao.sun.entity.security.Principal;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dell
 * 放款代理类，转发请求到remittance系统
 */
public interface RemittanceProxyHandler {

	String processingRevokeRemittancePlan(Principal principal, HttpServletRequest request, String remittancePlanUuid, String comment);

	String processingRevokeUpdateRemittanceInfo(String remittancePlanUuid); 
	
	String notifyOutlier(String remittanceApplicationUuid); 
}
