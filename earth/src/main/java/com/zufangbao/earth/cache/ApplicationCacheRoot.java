package com.zufangbao.earth.cache;

import com.demo2do.core.security.details.Menu;
import com.demo2do.core.security.details.Role;
import com.demo2do.core.utils.EnumUtils;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.cache.accessor.PersistAccessor;
import com.zufangbao.sun.entity.financial.PaymentChannelJson;
import com.zufangbao.sun.entity.security.SystemMenu;
import com.zufangbao.sun.entity.security.SystemRole;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.service.SystemMenuService;
import com.zufangbao.sun.yunxin.service.SystemRoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;

import java.util.*;

/**
 * 
 * @author Downpour
 */
public class ApplicationCacheRoot {
	
	private static final Log logger = LogFactory.getLog(ApplicationCacheRoot.class);
	private PersistAccessor persistAccessor;

	@Autowired
	private SystemMenuService systemMenuService;

	@Autowired
	private SystemRoleService systemRoleService;
	
	/**
	 * @param persistAccessor the persistAccessor to set
	 */
	public void setPersistAccessor(PersistAccessor persistAccessor) {
		this.persistAccessor = persistAccessor;
	}
	
	
	/**
	 * Get all menus as a list
	 * 
	 * @return
	 */
	@Cacheable("menus")
	public List<Menu> getMenus() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("ApplicationCacheRoot#getMenus() - get menus from menus.json");
		}
		
		// old version to load menus
		// return JsonUtils.parseArray(new ClassPathResource("menus.json"), Menu.class);
		
		// version 2 donot transfer systemMenu to Menu in loadAllLevel1Menus method
		List<SystemMenu> lv1systemMenus = systemMenuService.loadAllLevel1Menus();
		List<Menu> menus = new ArrayList<>();
		lv1systemMenus.forEach((m)->{
			menus.add(m.parse2Menu());
		});
		
		return menus;
	}
	
	/**
	 * Get all submenus as a list
	 * 
	 * @return
	 */
	@Cacheable("submenus")
	public List<Menu> getSubmenus() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("ApplicationCacheRoot#getSubmenus() - get submenus from submenus.json");
		}
		
		// old version to load submenus
		// return JsonUtils.parseArray(new ClassPathResource("submenus.json"), Menu.class);
		
		// version 2
		return systemMenuService.findMenuByLevel(0);
	}
	
	/**
	 * Get all roles group by name and alias
	 * 
	 * @return
	 */
	@Cacheable("roles")
	public Map<String, Role> getRoles() {
		// old version
		// List<Role> roles = JsonUtils.parseArray(new ClassPathResource("roles.json"), Role.class);
		
		Role exRole = new Role();
		exRole.setName("ROLE_SUPER_USER");
		exRole.setAlias("superuser");
		exRole.setDescription("超级管理员");
		List<Role> roles = new ArrayList<>();
		roles.add(exRole);
		
		Map<String, Role> result = new LinkedHashMap<String, Role>(roles.size() * 2);
		for(Role role : roles) {
			result.put(role.getName(), role);
			result.put(role.getAlias(), role);
		}
		
		// contain deleted role
		List<SystemRole> systemRoles = systemRoleService.loadAll(SystemRole.class);
		for(SystemRole systemRole: systemRoles) {
			Role role = new Role();
			role.setName(systemRole.getRoleName());
			role.setAlias(systemRole.getRoleName());
			role.setDescription(systemRole.getRoleRemark());
			result.put(role.getName(), role);
			result.put(role.getAlias(), role);
		}
		
		return result;
	}
	
	/**
	 * 
	 * The actual return type is Map<String, Map<String, List<String>>>
	 * 
	 * @return 
	 */
	@Cacheable("resources")
	public Map<String, Object> getResources() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("ApplicationCacheRoot#getResources() - get resources from resources.json");
		}
		
		// Map<String, Object> map = JsonUtils.parse(new ClassPathResource("resources.json"));
		Map<String, Object> map = new HashMap<String, Object>();
		
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
		
		map.putAll(resources);
		
		return map;
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
			urls.add("welcome");
		}
		
		Integer lvl = Integer.parseInt(dbmap.get("lvl").toString());
		if (lvl == 0)
			menus.add(dbmap.get("mkey").toString());
		else
			submenus.add(dbmap.get("mkey").toString());
		if (!StringUtils.isEmpty(dbmap.get("url").toString()))
			urls.add(dbmap.get("url").toString());
		
		Resource.put("menus", menus);
		Resource.put("submenus", submenus);
		Resource.put("urls", urls);
	}
	
	/**
	 * Get all the enums
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Cacheable("enums")
	public Map<String, Enum[]> getEnums() {
		return EnumUtils.scan("com.zufangbao");
	}
	/**
	 * 
	 * get all config payment channels
	 */
	@Cacheable("paymentChannels")
	public List<PaymentChannelJson>  getPaymentChannels(){
		if(logger.isDebugEnabled()) {
			logger.debug("ApplicationCacheRoot#getPaymentChannels() - get paymentChannels from paymentChannel.json");
		}
		
		return JsonUtils.parseArray(new ClassPathResource("paymentChannel.json"),PaymentChannelJson.class);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public PersistAccessor getData() {
		return this.persistAccessor;
	}

}
