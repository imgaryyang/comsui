package com.suidifu.pricewaterhouse.yunxin.task;

import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderDelayTask;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.pricewaterhouse.yunxin.handler.ClearingVoucherTaskHandler;
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderActiveVoucherTaskHandler;
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderCancelNoTransaction;
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderTaskHandler;
import com.suidifu.pricewaterhouse.yunxin.handler.RepaymentOrderTaskNoTransaction;
import com.suidifu.pricewaterhouse.yunxin.handler.TmpDepositTaskNoTransaction;
import com.suidifu.pricewaterhouse.yunxin.handler.VoucherTaskHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;

@Component("voucherTaskV2_0")
public class VoucherTaskV2_0 {

	@Autowired
	private VoucherTaskHandler voucherTaskHandler;

	@Autowired
	private RepaymentOrderTaskNoTransaction repaymentOrderTaskNoTransaction;

	@Autowired
	private RepaymentOrderCancelNoTransaction repaymentOrderCancelNoTransaction;
	
	@Autowired
	private RepaymentOrderTaskHandler repaymentOrderTaskHandler;
	
	@Autowired
	private RepaymentOrderActiveVoucherTaskHandler repaymentOrderActiveVoucherTaskHandler;
	
	@Autowired
	private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
	
	@Autowired
	private ClearingVoucherTaskHandler clearingVoucherTaskHandler;
	
	@Autowired
	private TmpDepositTaskNoTransaction tmpDepositTaskNoTransaction;

	@Autowired
	private RepaymentOrderDelayTask repaymentOrderDelayTask;
	
	private static Log logger = LogFactory.getLog(VoucherTaskV2_0.class);

	public void voucher_and_auto_deposit() {
		compensatory_loan_asset_recover(); // 商户付款凭证的核销
		repayment_order_placing();	//还款订单落盘
		repayment_order_cancel();	//取消订单
		repayment_order_recover();  //还款订单核销
		clearing_voucher_recover();//清算凭证核销
		tmp_deposit_compensatory_loan_asset_recover();//滞留单核销
		bq_repayment_order_data_sync();//佰仟三期还款文件同步
		repayment_order_modify_placing();  //还款订单变更落盘
		
	}




	private void repayment_order_recover(){
		
		long start = System.currentTimeMillis();

		try {

			logger.info("begin to inter_account_transfer_for_repayment_order!");

			repaymentOrderTaskHandler.handler_repayment_order_recover();

		} catch (Exception e) {
			logger.error("occur error inter_account_transfer_for_repayment_order. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to inter_account_transfer_for_repayment_order! use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	
	private void clearing_voucher_recover(){
		
		long start = System.currentTimeMillis();

		try {
			
			logger.info("begin to handleCreateClearingDeductPlanJvAndLedger!");

			clearingVoucherTaskHandler.handleCreateClearingDeductPlanJvAndLedger();
			
			logger.info("end to handleCreateClearingDeductPlanJvAndLedger!");
			
		} catch (Exception e) {
			logger.error("occur error handleCreateClearingDeductPlanJvAndLedger. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		
		logger.info("end to handleCreateClearingDeductPlanJvAndLedger! use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	
	private void compensatory_loan_asset_recover() {

		long start = System.currentTimeMillis();

		try {

			logger.info("begin to inter_account_transfer_for_business_payment_voucher!");

			voucherTaskHandler.handler_compensatory_loan_asset_recover();
			
			logger.info("end to inter_account_transfer_for_business_payment_voucher!");
			
		} catch (Exception e) {
			logger.error("occur error inter_account_transfer_for_business_payment_voucher. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to inter_account_transfer_for_business_payment_voucher! use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}

	private void repayment_order_placing() {

		long start = System.currentTimeMillis();

		try {

			logger.info("begin to repayment_order_placing!");
			repaymentOrderTaskNoTransaction.check_and_save_repayment_order_items();
		} catch (Exception e) {
			logger.error("occur error repayment_order_placing. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to repayment_order_placing! use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	private void repayment_order_cancel() {

		long start = System.currentTimeMillis();

		try {

			logger.info("begin to cacel repayment_order_cacel!");
			repaymentOrderCancelNoTransaction.lapse_repayment_order();
		} catch (Exception e) {
			logger.error("occur error repayment_order_cacel. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to repayment_order_cacel! use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	
	public void active_voucher_repayment_order_recover(){
		
		long start = System.currentTimeMillis();

		try {

			logger.info("begin to inter_account_transfer_for_active_voucher_repayment_order!");

			repaymentOrderActiveVoucherTaskHandler.handler_repayment_order_active_voucher_recover();

		} catch (Exception e) {
			logger.error("occur error inter_account_transfer_for_active_voucher_repayment_order. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to inter_account_transfer_for_active_voucher_repayment_order! use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	
	/**
	 * 单合同类型 核销 补漏
	 */
	public void repayment_order_for_single_contrct_recover_trap(){
		
		long start = System.currentTimeMillis();
		
		try {
			logger.info("begin to repayment_order_for_single_contrct_recover_trap!");
			repaymentOrderTaskNoTransaction.repaymentOrderGenerateThirdPartVoucherWithReconciliationTrap();
			
		} catch (Exception e) {
			
			logger.error("occur error repayment_order_for_single_contrct_recover_trap. FullStackTrace:"+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to repayment_order_for_single_contrct_recover_trap use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	
	/**
	 * 滞留单商户核销
	 */
	private void tmp_deposit_compensatory_loan_asset_recover(){
		
		long start = System.currentTimeMillis();
		
		try {
			logger.info("begin to tmp_deposit_compensatory_loan_asset_recover!");
			tmpDepositTaskNoTransaction.handler_compensatory_loan_asset_recover();
		} catch (Exception e) {
			logger.error("occur error tmp_deposit_compensatory_loan_asset_recover. FullStackTrace:"+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to tmp_deposit_compensatory_loan_asset_recover use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	private void bq_repayment_order_data_sync() {
		long start = System.currentTimeMillis();

		try {
			logger.info("begin to bq_repayment_order_data_sync!");
			repaymentOrderDelayTask.repaymentOrderDataSync();
		} catch (Exception e) {
			logger.error("occur error bq_repayment_order_data_sync. FullStackTrace:"+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to bq_repayment_order_data_sync use times["
			+ (System.currentTimeMillis() - start) + "]ms");
	}
	
	/**
	 *  还款订单 变更 落盘
	 */
	 private void repayment_order_modify_placing() {

		long start = System.currentTimeMillis();

		try {

			logger.info("begin to repayment_order_modify_placing!");
			
			repaymentOrderTaskNoTransaction.modify_order_check_and_save_repayment_order_items();
			
		} catch (Exception e) {
			logger.error("occur error repayment_order_modify_placing. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}
		logger.info("end to repayment_order_modify_placing! use times["
				+ (System.currentTimeMillis() - start) + "]ms");
	}
	
}
