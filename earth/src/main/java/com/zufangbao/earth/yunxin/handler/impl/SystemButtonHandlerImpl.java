package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.GenericJdbcSupport;
import com.demo2do.core.utils.StringUtils;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.handler.SystemButtonHandler;
import com.zufangbao.sun.entity.security.PrivilegeStrategy;
import com.zufangbao.sun.entity.security.SystemButton;
import com.zufangbao.sun.yunxin.service.SystemMenuService;
import com.zufangbao.sun.yunxin.service.SystemRoleService;
import com.zufangbao.sun.yunxin.service.privilege.SystemButtonService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengll on 17-4-12.
 */
@Component("systemButtonHandler")
public class SystemButtonHandlerImpl implements SystemButtonHandler {

    @Autowired
    private SystemButtonService systemButtonService;

    @Autowired
    private SystemMenuService systemMenuService;

    @Autowired
    private GenericDaoSupport genericDaoSupport;

    @Autowired
    private GenericJdbcSupport genericJdbcSupport;

    @Autowired
    private PrincipalService principalService;
    @Autowired
    private SystemRoleService systemRoleService;

    @Override
    public List<Long> getSystemButtonIdsByPrivilege(List<Long> linkPrivilegeButtonIds) {
        if(CollectionUtils.isEmpty(linkPrivilegeButtonIds)) {
            return ListUtils.EMPTY_LIST;
        }
        String sql = "SELECT distinct privilege_button.button_id " +
                "FROM link_privilege_button privilege_button " +
                "LEFT JOIN system_privilege privilege " +
                "ON privilege_button.privilege_id = privilege.id " +
                "WHERE privilege_button.id IN (:linkPrivilegeButtonIds) " +
                "AND privilege.privilege_strategy = :privilege_strategy ";
        HashMap<String,Object> params = new HashMap<>();
        params.put("linkPrivilegeButtonIds", linkPrivilegeButtonIds);
        params.put("privilege_strategy", PrivilegeStrategy.HAS_ACCESS);
        List<Long> buttonIds = new ArrayList<Long>();
        try {
            List<Map<String, Object>> maps = genericJdbcSupport.queryForList(sql, params);
            maps.forEach((m)->{
                buttonIds.add(Long.parseLong(m.get("button_id").toString()));
            });
        } catch (Exception e) {
            e.printStackTrace();
            return ListUtils.EMPTY_LIST;
        }

        return buttonIds;
    }

    @Override
    public List<Long> getLinkPrivilegeButtonId(Long principalId) {

        if(this.principalService.getPrincipalById(principalId) == null) {
            return ListUtils.EMPTY_LIST;
        }
        String sql = "SELECT distinct role_privilege.link_privilege_button_id " +
                "FROM link_role_privilege role_privilege " +
                "LEFT JOIN link_role_principal role_principal " +
                "ON role_principal.role_id = role_privilege.role_id " +
                "WHERE role_principal.principal_id = :principalId ";

        HashMap<String,Object> params = new HashMap<>();
        params.put("principalId", principalId);
        List<Long> linkPrivilegeButtonIds = new ArrayList<Long>();
        try {
            List<Map<String, Object>> maps = genericJdbcSupport.queryForList(sql, params);
            if(CollectionUtils.isEmpty(maps)) {
                return  ListUtils.EMPTY_LIST;
            }
            maps.forEach((m)->{
                if(!StringUtils.isEmpty(m.get("link_privilege_button_id").toString())){
                    linkPrivilegeButtonIds.add(Long.parseLong(m.get("link_privilege_button_id").toString()));
                }

            });
        } catch (Exception e) {
            //e.printStackTrace();
            return ListUtils.EMPTY_LIST;
        }

        return linkPrivilegeButtonIds;
    }

    @Override
    public List<SystemButton> checkAccessButtonsByMenuId(List<Long> buttonIds, Long menuId) {
        if(CollectionUtils.isEmpty(buttonIds) || null == systemMenuService.findMenuById(menuId)) {
            return ListUtils.EMPTY_LIST;
        }
        List<SystemButton> buttonsFromMenu = systemButtonService.loadSystemButtonByMenuKey(systemMenuService.findMenuById(menuId).getMkey());
        List<SystemButton> allButtons = systemButtonService.findSystemButtonListByIds(buttonIds);
        //取交集
        List<SystemButton> realAccessButtons = new ArrayList<SystemButton>();
        realAccessButtons.addAll(buttonsFromMenu);
        realAccessButtons.retainAll(allButtons);
        if(CollectionUtils.isEmpty(realAccessButtons)) {

            return ListUtils.EMPTY_LIST;
        }
        return realAccessButtons;
    }

    @Override
    public List<String> getAuthByButtonId(Long buttonId) {
        if(this.systemButtonService.findSystemButtonById(buttonId) == null) {
            return ListUtils.EMPTY_LIST;
        }
        List<String> roleNameList = new ArrayList<String>();
        String sql = "SELECT systemRole.role_name " +
                "FROM system_role systemRole " +
                "LEFT JOIN link_role_privilege lrp " +
                "ON systemRole.id = lrp.role_id " +
                "LEFT JOIN link_privilege_button lpb " +
                "ON lrp.link_privilege_button_id = lpb.id " +
                "WHERE lpb.button_id =:button_id ";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("button_id", buttonId);
        List<Map<String, Object>> maps = this.genericJdbcSupport.queryForList(sql, params);
        maps.forEach((m->{
            roleNameList.add(m.get("role_name").toString());
        }));
        return CollectionUtils.isEmpty(roleNameList)? null : roleNameList;
    }

	@Override
	public List<Long> getLinkPrivilegeButtonIdList(Long roleId) {
		
		if(null == roleId) {
			return Collections.emptyList();
		}
		
        String sql = "SELECT distinct role_privilege.link_privilege_button_id " +
                "FROM link_role_privilege role_privilege " +
                "LEFT JOIN link_role_principal role_principal " +
                "ON role_principal.role_id = role_privilege.role_id " +
                "WHERE role_principal.role_Id = :roleId ";

        HashMap<String,Object> params = new HashMap<>();
        params.put("roleId", roleId);
        List<Long> linkPrivilegeButtonIds = new ArrayList<Long>();
        try {
            List<Map<String, Object>> maps = genericJdbcSupport.queryForList(sql, params);
            if(CollectionUtils.isEmpty(maps)) {
                return Collections.emptyList();
            }
            maps.forEach((m)->{
                if(!StringUtils.isEmpty(m.get("link_privilege_button_id").toString())){
                    linkPrivilegeButtonIds.add(Long.parseLong(m.get("link_privilege_button_id").toString()));
                }
            });
        } catch (Exception e) {
        }
        return linkPrivilegeButtonIds;
	}
}
