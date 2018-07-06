package com.zufangbao.earth.cache.accessor;

import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.handler.FinancialContracCacheHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.wellsfargo.greypool.document.entity.busidocument.dictionary.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("persistAccessor")
public class PersistAccessor {

	@Autowired
	private FinancialContracCacheHandler financialContracCacheHandler;

	@Autowired
	private PrincipalService principalService;
	
	public String getRealnameBy(Long principalId){
		Principal principal = principalService.load(Principal.class, principalId);
		TUser tUser = principal.gettUser();
		return tUser == null ? "" : tUser.getName();
	}

	public Currency getCurrencyBy(String name){
		return Currency.fromName(name);
	}
	
	public List<FinancialContract> getCanAccessFinancialContractList(Long princialId){
		return financialContracCacheHandler.getAvailableFinancialContractList(princialId);
	}

}
