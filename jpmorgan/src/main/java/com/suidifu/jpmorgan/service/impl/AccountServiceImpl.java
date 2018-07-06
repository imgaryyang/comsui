package com.suidifu.jpmorgan.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.jpmorgan.entity.Account;
import com.suidifu.jpmorgan.service.AccountService;

/**
 * 
 * @author zjm
 *
 */
@Service("accountService")
public class AccountServiceImpl extends GenericServiceImpl<Account> implements
		AccountService {

	@Override
	public Account getAccountBy(String accountNo) {
		
		if(StringUtils.isEmpty(accountNo)) {
			return null;
		}
		
		List<Account> accountList = genericDaoSupport.searchForList("FROM Account WHERE accountNo =:accountNo", "accountNo", accountNo);
		
		if(CollectionUtils.isEmpty(accountList)) {
			return null;
		}
		return accountList.get(0);
	}

}
