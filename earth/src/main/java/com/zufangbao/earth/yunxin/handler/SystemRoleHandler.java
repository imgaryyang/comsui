package com.zufangbao.earth.yunxin.handler;

/**
 * Created by chengll on 17-4-13.
 */
public interface SystemRoleHandler {

     public boolean createSystemRole(String roleName, String roleRemark, String menuIds, String buttonIds);

     public boolean editSystemRole(Long roleId, String roleName, String roleRemark, String menuIds, String buttonIds);

     public boolean  delSystemRoleRelation(Long roleId);
     public void initLinkPrivilegeWithButtons();

}
