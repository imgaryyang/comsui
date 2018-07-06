package com.suidifu.jpmorgan.service.impl;

import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.OrderNoRegister;
import com.suidifu.jpmorgan.service.OrderNoRegisterService;

@Service("orderNoRegisterService")
public class OrderNoRegisterServiceImpl extends
		GenericServiceImpl<OrderNoRegister> implements OrderNoRegisterService {

	@Override
	public void logOffRegister(String outlierTransactionUuid) {
		try {
			genericDaoSupport.executeSQL("delete from order_no_register where outlier_transaction_uuid =:outlierTransactionUuid", "outlierTransactionUuid", outlierTransactionUuid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
