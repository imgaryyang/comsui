package com.zufangbao.earth.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.model.PrincipalInfoModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;

import java.util.List;
import java.util.Map;

public interface PrincipalHandler {

	public String createPrincipal(PrincipalInfoModel principalInfoModel, Long creatorId);
	
	public void updatePrincipal(PrincipalInfoModel principalInfoModel, Long operatorId);

	public List<FinancialContract> get_can_access_financialContract_list(Principal principal);

	public List<FinancialContract> get_can_access_financialContract_list(Long principalId);

	public Map<String, Object> queryFinancialContractListByPrincipal(FinancialContractQueryModel queryModel, Page page);

	List<FinancialContract> get_unbind_financialContract_list(Long principalId);

	public String bindAllFinancialContract(Long principalId);

	public String unbindAllFinancialContract(Long principalId);

	TUser getTUser(Principal principal);


	void bindFinancialContract(Long principalId, List<Long> financialContractIds);

}
