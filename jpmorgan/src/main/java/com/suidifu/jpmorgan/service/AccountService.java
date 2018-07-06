package com.suidifu.jpmorgan.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.Account;

/**
 * 
 * @author zjm
 *
 */
public interface AccountService extends GenericService<Account> {

	Account getAccountBy(String accountNo);
}
