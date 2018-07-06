/**
 * 
 */
package com.zufangbao.earth.service;

import com.demo2do.core.service.GenericService;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.security.SystemMenu;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.sun.yunxin.entity.model.PrincipalDetailShowModel;
import com.zufangbao.sun.yunxin.entity.model.PrincipalShowModel;
import com.zufangbao.sun.yunxin.entity.model.principal.PrincipalQueryModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @author Downpour
 */
public interface PrincipalService extends GenericService<Principal>{

    Principal getPrincipal(String name);

    Principal getPrincipalById(Long id);

    String updatePassword(Principal principal,String oldPassword, String newPassword);

    void save(Principal newPrincipal);

    List<App> get_can_access_app_list(Principal principal);

    Map<String, String> getQueriesByRequest(HttpServletRequest request);

    void deleteUser(Principal newPrincipal);

    Long getSystemPrincipalId();

    List<Principal> queryPrincipal(PrincipalQueryModel queryModel, Page page);

    List<Map<String, Object>> queryPrincipalWithoutGroup(PrincipalQueryModel queryModel, Page page);

    int countPrincipalWithoutGroup(PrincipalQueryModel queryModel);

    List<Map<String, Object>> queryPrincipalWithGroup(PrincipalQueryModel queryModel, Page page);

    int countPrincipalWithGroup(PrincipalQueryModel queryModel);

    int countPrincipal(PrincipalQueryModel queryModel);

    List<SystemMenu> getMenusBy(Long principalId);

	void bindFinancialContract(Long financialContractId, Long principalId);

	String updatePrincipal(TUser tUser, PrincipalDetailShowModel principalDetailShowModel);

	Long getPrincipalIdBysourceUuid(String sourceUuid);

	List<PrincipalShowModel> getPrincipalShowList(List<Principal> principalList);
}
