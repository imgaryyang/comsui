package com.suidifu.pricewaterhouse.yunxin.task;


import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.pricewaterhouse.yunxin.handler.ClearingVoucherToAcountOnlineNoTransaction;
import com.suidifu.pricewaterhouse.yunxin.handler.SpecialAccountTaskHandlerNoTransaction;
import com.zufangbao.wellsfargo.yunxin.handler.specialaccount.SpecialAccountTaskNoSession;


@Component("specialAccountTask")
public class SpecialAccountTask {
	
	private static Log logger = LogFactory.getLog(SpecialAccountTask.class);
	
	@Autowired
	private SpecialAccountTaskNoSession specialAccountTaskNoSession;
	
	@Autowired
	private SpecialAccountTaskHandlerNoTransaction specialAccountTaskHandlerNoTransaction;	
	
	@Autowired
	private ClearingVoucherToAcountOnlineNoTransaction clearingVoucherToAcountOnlineNoTransaction;

	
	public void special_account_task_total_method() {
		special_account_for_customer_type_update_amount();
		special_acount_recover();//专户记账
		clearing_voucher_to_account_online();
		
	}
	
	/**
	 *  客户账户金额更新
	 */
	private void special_account_for_customer_type_update_amount(){
		try {
			logger.info("special_account_for_customer_update_amount_by_virtual_account start");
			
			specialAccountTaskNoSession.handlerSpecialAccountForCustomerType();
			
		} catch(Exception e){
			logger.error("special_account_for_customer_update_amount_by_virtual_account error");
			e.printStackTrace();
		}
		logger.info("special_account_for_customer_update_amount_by_virtual_account end");
	}
	/**
	 * 专户记账
	 */
	private void special_acount_recover(){
		long start = System.currentTimeMillis();
		try {
			logger.info("begin to special_acount_recover!");
			
			specialAccountTaskHandlerNoTransaction.handleSpecialAccountBookAccounts();
		} catch (Exception e) {
			logger.error("occur error special_acount_recover. FullStackTrace:"+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to special_acount_recover use times["+ (System.currentTimeMillis() - start) + "]ms");
	}
	

	private void clearing_voucher_to_account_online(){
		long start = System.currentTimeMillis();
		
		try {
			logger.info("begin to Clearing_Voucher_To_Acount_Online!");
			clearingVoucherToAcountOnlineNoTransaction.handlerClearingVoucherToAcountOnline();;
		} catch (Exception e) {
			logger.error("occur error Clearing_Voucher_To_Acount_Online. FullStackTrace:"+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to Clearing_Voucher_To_Acount_Online use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
}
