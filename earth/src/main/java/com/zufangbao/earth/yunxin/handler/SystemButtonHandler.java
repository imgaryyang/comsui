package com.zufangbao.earth.yunxin.handler;

import java.util.List;

import com.zufangbao.sun.entity.security.SystemButton;

/**
 * Created by chengll on 17-4-12.
 */
public interface SystemButtonHandler {

    public List<Long> getSystemButtonIdsByPrivilege(List<Long> linkPrivilegeButton);

    public List<Long> getLinkPrivilegeButtonId(Long principalId);

    public List<SystemButton> checkAccessButtonsByMenuId(List<Long> buttonIds, Long menuId);

    public List<String> getAuthByButtonId(Long buttonId);
    
    public List<Long> getLinkPrivilegeButtonIdList(Long roleId);
    
}
