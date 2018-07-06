package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.coffer.entity.QueryBalanceModel;
import com.suidifu.coffer.entity.QueryBalanceResult;
import com.suidifu.coffer.factory.DirectBankHandlerFactory;
import com.suidifu.coffer.handler.DirectBankHandler;
import com.zufangbao.earth.yunxin.exception.QueryBalanceException;
import com.zufangbao.earth.yunxin.handler.CapitalHandler;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.directbank.USBKey;
import com.zufangbao.sun.service.USBKeyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("capitalHandler")
public class CapitalHandlerImpl implements CapitalHandler {
	
	@Autowired
	USBKeyService usbKeyService;

	@Override
	public BigDecimal queryAccountBalance(Account account) throws QueryBalanceException {
		if(null == account) {
			throw new QueryBalanceException();
		}
		String bankCode = account.getBankCode();
		if(StringUtils.isEmpty(bankCode)) {
			throw new QueryBalanceException();
		}
		String usbUuid = account.getUsbUuid();
		if(StringUtils.isEmpty(usbUuid)) {
			throw new QueryBalanceException();
		}
		USBKey usbKey = usbKeyService.getUSBKeyByUUID(usbUuid);
		if(null == usbKey) {
			throw new QueryBalanceException();
		}
		Map<String, String> workParms = usbKey.getConfig();
		DirectBankHandler directBankHandler = DirectBankHandlerFactory.newInstance(bankCode);
		if(null == directBankHandler) {
			throw new QueryBalanceException();
		}
		QueryBalanceModel queryBalanceModel = new QueryBalanceModel(account.getAccountNo(), new Date(), DateUtils.format(new Date(), "yyyyMMddHHmmss"));
		
		Map<String, String> bankAppendix = new HashMap<String, String>();
		bankAppendix.put("bankBranchNo", account.getAttr().getOrDefault("bankBranchNo", StringUtils.EMPTY).toString());
		queryBalanceModel.appendQueryAccountBankAppendix(bankAppendix);
		QueryBalanceResult queryAccountBalance = directBankHandler.queryAccountBalance(queryBalanceModel, workParms);
		
		if(queryAccountBalance.commFailed()) {
			throw new QueryBalanceException();
		}
		return new BigDecimal(queryAccountBalance.getBalance());
	}

}
