package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.utils.StringUtils;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.handler.SystemButtonHandler;
import com.zufangbao.earth.yunxin.handler.SystemRoleHandler;
import com.zufangbao.sun.entity.security.*;
import com.zufangbao.sun.yunxin.service.*;
import com.zufangbao.sun.yunxin.service.impl.LinkRolePrincipalServiceImpl;
import com.zufangbao.sun.yunxin.service.privilege.SystemButtonService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chengll on 17-4-13.
 */
@Component("systemRoleHandler")
public class SystemRoleHandlerImpl implements SystemRoleHandler {


    private static Log logger = LogFactory.getLog(SystemRoleHandlerImpl.class);

    @Autowired
    private SystemRoleService systemRoleService;

    @Autowired
    private SystemMenuService systemMenuService;

    @Autowired
    private LinkRoleMenuService linkRoleMenuService;

    @Autowired
    private SystemButtonService systemButtonService;

    @Autowired
    private SystemButtonHandler systemButtonHandler;
    @Autowired
    private LinkPrivilegeButtonService linkPrivilegeButtonService;
    @Autowired
    private SystemPrivilegeService systemPrivilegeService;
    @Autowired
    private LinkRolePrivilegeService linkRolePrivilegeService;
    @Autowired
    private LinkRolePrincipalService linkRolePrincipalService;
    @Autowired
    private PrincipalService pincipalService;


    @Override
    public boolean createSystemRole(String roleName, String roleRemark, String menuIds, String buttonIds) {
        try {
            if (StringUtils.isEmpty(roleName)) return false;

            //保存用户角色
            if(!systemRoleService.saveSystemRole(roleName, roleRemark)){
                return false;
            }
            SystemRole systemRole = systemRoleService.getSystemRoleBy(roleName);
            if(systemRole == null) {
                return false;
            }
            Long roleId = systemRole.getId();
            this.addSystemRoleWithResourceRelation(roleId,menuIds, buttonIds,PrivilegeStrategy.HAS_ACCESS);
            return true;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editSystemRole(Long roleId, String roleName, String roleRemark, String menuIds, String buttonIds) {

        try {
            SystemRole systemRole = systemRoleService.getSystemRoleBy(roleId);
            if(null == systemRole){
                return false;
            }
            if(StringUtils.isEmpty(roleName)){
                return false;
            }
            String oldRoleName = systemRole.getRoleName();
            systemRole.setRoleName(roleName);
            systemRole.setRoleRemark(roleRemark);
            systemRoleService.update(systemRole);

            //先删除,在更新
            this.delSystemRoleRelation(roleId);
            //更新新的关系
           this.updateSystemRoleWithResourceRelation(roleId,menuIds,buttonIds,PrivilegeStrategy.HAS_ACCESS);
            //更新principal表
            updatePrincipalByRoleName(roleId,oldRoleName,roleName);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void updatePrincipalByRoleName(Long roleId,String oldRoleName,String newRoleName){
    	
    	    if(org.apache.commons.lang.StringUtils.equals(oldRoleName, newRoleName)) {
    	    		return;
    	    }

        List<LinkRolePrincipal> linkRolePrincipals = linkRolePrincipalService.getPrincipalsBy(roleId);

        List<Principal> principals = linkRolePrincipals.stream().map(a->{return  pincipalService.getPrincipalById(a.getPrincipalId());}).filter(a->{return null !=a;}).collect(Collectors.toList());

        for (Principal principal:principals
             ) {
            List<String> roleNameList = Arrays.asList(principal.getAuthority().split(",")).stream().filter(a->!oldRoleName.equals(a)).collect(Collectors.toList());

            roleNameList.add(newRoleName);

            principal.setAuthority(org.apache.commons.lang.StringUtils.join(roleNameList,","));

            pincipalService.update(principal);
        }
    }

    /**
     * 通过roleId删除相应的关联关系
     *,linkroleprivilege,linkrolemenu
     * @param roleId
     * @return
     */
    @Override
    public boolean delSystemRoleRelation(Long roleId) {
        if(null == roleId || roleId <= 0){
            return false;
        }
        if(!linkRoleMenuService.delLinkRole(roleId)) {
            return false;
        }
        return linkRolePrivilegeService.delLinkRolePrivilege(roleId);
    }


    private List<Long> splitIdStrToListByRegex(String idStr, String regex) {

        if(StringUtils.isEmpty(idStr) || StringUtils.isEmpty(regex)){
            return ListUtils.EMPTY_LIST;
        }
        List<Long> idList = new ArrayList<Long>();
        try {
            String[] idStrList = idStr.split(regex);
            if(idStrList.length <= 0){
               return ListUtils.EMPTY_LIST;
            }
            for(String id : idStrList) {
                if(!StringUtils.isEmpty(id)){
                    idList.add(Long.parseLong(id));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ListUtils.EMPTY_LIST;
        }
        return CollectionUtils.isEmpty(idList)? ListUtils.EMPTY_LIST : idList;
    }

    /**
     *添加角色关联资源关系
     * @param roleId
     * @param menuIds
     * @param buttonIds
     * @param privilegeStrategy
     */
    private void  addSystemRoleWithResourceRelation(Long roleId, String menuIds, String buttonIds,Enum<PrivilegeStrategy> privilegeStrategy){
        /**
         * 1.获取选中的菜单
         * 2.处理选中button按钮,上级菜单默认选中的情况
         * 3.获取子菜单的父级菜单
         */
        List<Long> unionMenuIdList = new ArrayList<>();
        List<Long> menuList = this.splitIdStrToListByRegex(menuIds, ",");
        List<Long> buttonIdList = this.splitIdStrToListByRegex(buttonIds, ",");
        List<String> menuKeys = systemButtonService.findMenuKeyListByButtonIds(buttonIdList);
        List<Long> buttonParentMenuIdList = new ArrayList<>();
        for(String mKey : menuKeys) {
            SystemMenu menuByMkey = systemMenuService.findMenuByMkey(mKey);
            buttonParentMenuIdList.add(menuByMkey.getId());
        }


        //先合并一遍
        HashSet<Long> set = new HashSet<Long>();
        set.addAll(menuList);
        set.addAll(buttonParentMenuIdList);
        unionMenuIdList.addAll(set);
        //查找相关的父级菜单进行合并去重复,并保留当前菜单
        List<Long> allMenuIds = systemMenuService.findAllConnectionMenuIdsBy(unionMenuIdList);
        //保存菜单关联关系:菜单与权限关联
        for(Long menuId : allMenuIds) {
            linkRoleMenuService.newLinkRoleMenu(menuId, roleId);
        }

        /**
         * 存储link_role_privilege
         */
        SystemPrivilege systemPrivilege = systemPrivilegeService.findSystemPrivilegeByStrategy(privilegeStrategy);
        if(systemPrivilege != null) {
            List<Long> linkPrivilegeButtonIdList = linkPrivilegeButtonService.getLinkPrivilegeButtonIdListBy(buttonIdList, systemPrivilege.getId());
            List<Long> linkPrivilegeButtonIds = new ArrayList<>();
            if(!CollectionUtils.isEmpty(linkPrivilegeButtonIdList)){
                for(Long privilegeButtonId : linkPrivilegeButtonIdList) {
                        linkPrivilegeButtonIds.add(privilegeButtonId);
                }
                linkRolePrivilegeService.batchSaveLinkRolePrivilege(roleId, linkPrivilegeButtonIds);
            }
        }
    }

    private void  updateSystemRoleWithResourceRelation(Long roleId, String menuIds, String buttonIds,Enum<PrivilegeStrategy> privilegeStrategy){
        /**
         * 1.获取选中的菜单
         * 2.处理选中button按钮,上级菜单默认选中的情况
         * 3.获取子菜单的父级菜单
         */
        List<Long> unionMenuIdList = new ArrayList<>();
        List<Long> menuList = this.splitIdStrToListByRegex(menuIds, ",");
        List<Long> buttonIdList = this.splitIdStrToListByRegex(buttonIds, ",");
        List<String> menuKeys = systemButtonService.findMenuKeyListByButtonIds(buttonIdList);
        List<Long> buttonParentMenuIdList = new ArrayList<>();
        for(String mKey : menuKeys) {
            SystemMenu menuByMkey = systemMenuService.findMenuByMkey(mKey);
           if(menuByMkey != null){
               buttonParentMenuIdList.add(menuByMkey.getId());
           }

        }

        //先合并一遍
        HashSet<Long> set = new HashSet<Long>();
        set.addAll(menuList);
        set.addAll(buttonParentMenuIdList);
        unionMenuIdList.addAll(set);
        //查找相关的父级菜单进行合并去重复,并保留当前菜单
        List<Long> allMenuIds = systemMenuService.findAllConnectionMenuIdsBy(unionMenuIdList);
        //保存菜单关联关系:菜单与权限关联
        for(Long menuId : allMenuIds) {
            linkRoleMenuService.newLinkRoleMenu(menuId, roleId);
        }
        
        linkRolePrivilegeService.delLinkRolePrivilege(roleId);
        
        List<Long> oldButtonIds = extractSystemButtonId(roleId);

        linkPrivilegeButtonService.deleteLinkPrivilegeButton(oldButtonIds);
        
        /**
         * 存储link_role_privilege
         */
        SystemPrivilege systemPrivilege = systemPrivilegeService.findSystemPrivilegeByStrategy(privilegeStrategy);
        if(systemPrivilege != null) {
            List<Long> linkPrivilegeButtonIdList = buildLinkPrivilegeButtonList(systemPrivilege,buttonIdList);
            List<Long> linkPrivilegeButtonIds = new ArrayList<>();
            if(!CollectionUtils.isEmpty(linkPrivilegeButtonIdList)){
                for(Long privilegeButtonId : linkPrivilegeButtonIdList) {
                        linkPrivilegeButtonIds.add(privilegeButtonId);
                }
                linkRolePrivilegeService.batchSaveLinkRolePrivilege(roleId, linkPrivilegeButtonIds);
            }
        }
    }
    
    private List<Long> buildLinkPrivilegeButtonList(SystemPrivilege systemPrivilege,List<Long> buttonIds){
    		
    		List<Long> linkPrivilegeIdList = new ArrayList<>();
    	
    		for (Long buttonId : buttonIds) {
				
    			linkPrivilegeIdList.add((Long)linkPrivilegeButtonService.save(new LinkPrivilegeButton(buttonId,systemPrivilege.getId())));
			
    		}
    		return linkPrivilegeIdList;
    }
    

    private List<Long> extractSystemButtonId(Long roleId){
    	
    	  List<Long> linkPrivilegeButtonIds = systemButtonHandler
    		          .getLinkPrivilegeButtonIdList(roleId);
    		      //通过有权限的按钮模块集合获取相应的可访问按钮id集合
      List<Long> userOwnButtonIds = systemButtonHandler
          .getSystemButtonIdsByPrivilege(linkPrivilegeButtonIds);
      //对访问的按钮id集合进行过滤(通过父级菜单来过滤)
      List<SystemButton> allButtons = systemButtonService.findSystemButtonListByIds(userOwnButtonIds);
    		      
    	  return allButtons.stream().map(a->{return a.getId();}).collect(Collectors.toList());
    }
    
    //初始化菜单权限
    @Override
    public void initLinkPrivilegeWithButtons() {
       List<Long> buttonIdList = new ArrayList<Long>();
        List<SystemButton> systemButtons = systemButtonService.loadAllButtons();
        systemButtons.forEach((sbs)->{
            buttonIdList.add(sbs.getId());
        });
        SystemPrivilege systemPrivilege = systemPrivilegeService.findSystemPrivilegeByStrategy(PrivilegeStrategy.HAS_ACCESS);
        if(null != systemPrivilege) {

            linkPrivilegeButtonService.batchSaveLinkPrivilegeButton(buttonIdList,systemPrivilege.getId());
        }

    }


}
