package com.zufangbao.earth.web.controller;

import com.zufangbao.earth.RoleSpec;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;

import java.util.HashMap;

public class HomeControllerSpec {
	/*
	 * yechenhui
	 * 这里定义的URL已经失效了，因为都统一为/v#/...了
	 * 以后要改为前端做跳转
	 */
	public static final String DATA="/data";
	public static final String APP_DATA="/app-data";
	public static final String FINANCE="/finance";
	public static final String MESSAGE="/message";
	public static final String SYSTEM ="/system";
	public static final String CAPITAL = URL.CAPITAL_NAME;
	public static final String ROLE_DEFAULT = "ROLE_DEFAULT";
	
	public class role_super_user{
		public static final String DATA_URL="redirect:/contracts";
		public static final String FINANCE_URL="redirect:/v#/finance/assets";
		public static final String SYSTEM_URL="redirect:/post-update-password";
		public static final String CAPITAL_URL="redirect:/v#/capital/directbank";
	}
	
	private final static HashMap<String, HashMap<String, String>> homeEntryMappingByRole=new HashMap<String,HashMap<String,String> >()
			{{
				put(HomeControllerSpec.DATA, new  HashMap<String, String>(){{
					put(RoleSpec.ROLE_SUPER_USER,HomeControllerSpec.role_super_user.DATA_URL);
					put(HomeControllerSpec.ROLE_DEFAULT,HomeControllerSpec.role_super_user.DATA_URL);
					}});
				put(HomeControllerSpec.FINANCE, new  HashMap<String, String>(){
					{
					put(RoleSpec.ROLE_SUPER_USER,HomeControllerSpec.role_super_user.FINANCE_URL);
					put(HomeControllerSpec.ROLE_DEFAULT,HomeControllerSpec.role_super_user.FINANCE_URL);
					}
				});
				put(HomeControllerSpec.SYSTEM, new  HashMap<String, String>(){{
					put(RoleSpec.ROLE_SUPER_USER,HomeControllerSpec.role_super_user.SYSTEM_URL);
					put(HomeControllerSpec.ROLE_DEFAULT,HomeControllerSpec.role_super_user.SYSTEM_URL);
					}});
				put(HomeControllerSpec.CAPITAL, new  HashMap<String, String>(){{
					put(RoleSpec.ROLE_SUPER_USER,HomeControllerSpec.role_super_user.CAPITAL_URL);
					put(HomeControllerSpec.ROLE_DEFAULT,HomeControllerSpec.role_super_user.CAPITAL_URL);
					}});
				
			}
			};

	public static HashMap<String, HashMap<String, String>> HomeEntryMappingByRole() {
		return homeEntryMappingByRole;
	}

			
			
}
			

