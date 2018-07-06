/**
 * 
 */
package com.zufangbao.earth.web.controller;

import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.sun.entity.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * @author lute
 *
 */

@Controller
@RequestMapping("")
public class HomeController {

	/**
	 * Redirect to app renters
	 * 
	 * @return
	 */

	/**
	 * 前端做页面路由，3个入口 /v: 普通页面，需登录 /login: 待做 /post-update-password: 新用户登录需修改密码
	 * （ps:
	 * /v#/system/update-password也是修改密码页面，但URL的hash部分无法传给后台，所以新增使用/post-update
	 * -password）
	 */
	@RequestMapping(value = "/v")
	public ModelAndView proxy(@Secure Principal principal) {
		ModelAndView view = new ModelAndView("index");
		return view;
	}

	private String getUrlFromPrincipal(String EntryUrl, Principal principal) {
		HashMap<String, String> mapping = HomeControllerSpec
				.HomeEntryMappingByRole().get(EntryUrl);

		String Url = mapping.getOrDefault(principal.getAuthority(),
				mapping.get(HomeControllerSpec.ROLE_DEFAULT));
		return Url;
	}

	@RequestMapping(HomeControllerSpec.DATA)
	public String data(@Secure Principal principal) {
		System.out.println();
		return getUrlFromPrincipal(HomeControllerSpec.DATA, principal);

	}

	@RequestMapping(HomeControllerSpec.APP_DATA)
	public String appData(@Secure Principal principal) {

		return getUrlFromPrincipal(HomeControllerSpec.APP_DATA, principal);

	}

	@RequestMapping(HomeControllerSpec.FINANCE)
	public String finance(@Secure Principal principal) {
		return getUrlFromPrincipal(HomeControllerSpec.FINANCE, principal);
	}

	@RequestMapping(HomeControllerSpec.CAPITAL)
	public String capital(@Secure Principal principal) {
		return getUrlFromPrincipal(HomeControllerSpec.CAPITAL, principal);
	}

	/**
	 * Redirect to service provider
	 * 
	 * @return
	 */

	@RequestMapping(HomeControllerSpec.SYSTEM)
	public String system(@Secure Principal principal) {
		return getUrlFromPrincipal(HomeControllerSpec.SYSTEM, principal);
	}

}
