package com.zufangbao.earth.yunxin.handler;

import com.zufangbao.earth.yunxin.exception.QueryBalanceException;
import com.zufangbao.sun.entity.account.Account;

import java.math.BigDecimal;

public interface CapitalHandler {

	BigDecimal queryAccountBalance(Account account) throws QueryBalanceException;
}
