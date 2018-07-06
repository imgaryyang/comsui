package com.zufangbao.earth.yunxin.task;

import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.AutoRecoverAssesNoSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;
import com.zufangbao.wellsfargo.yunxin.handler.SourceDocumentHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class VoucherTask {
	@Autowired
	private JournalVoucherHandler journalVoucherHandler;
	
	@Autowired
	private SourceDocumentHandler sourceDocumentHandler;
	@Autowired
	private AutoRecoverAssesNoSession autoRecoverAssesNoSession;
	@Autowired
	@Qualifier("dataStatisticsCacheHandler")
	private BankAccountCache bankAccountCacheHandler;
	@Autowired
	private BusinessPaymentVoucherSession businessPaymentVoucherSession;
	private static Log logger = LogFactory.getLog(VoucherTask.class);
	
	public void voucher_and_auto_deposit(){
		createOnlineCashFlowSourceDocument();//系统主动银联扣款成功后 生成原始凭证
		updateOnLineCashFlowStatement();//根据原始凭证生成jv和bv
		//TODO银行流水 充值移至  短信TASK
		//ps 商户付款凭证的核销已经移动到PricewaterHouse中{@see com.suidifu.pricewaterhouse.yunxin.task.VoucherTaskV2_0#voucher_and_auto_deposit}
		//compensatory_loan_asset_recover(); //商户付款凭证的核销
		//已迁移至PricewaterHouse{@see CashflowChargeAndRecoverV2_0#scan_cashflow_charge_virtual_account_auto_recover}
//		active_payment_voucher_recover(); //主动付款凭证的核销
		//已经迁移走了，@see com.suidifu.pricewaterhouse.yunxin.task.SystemDeductTaskV2_0#active_payment_voucher_recover
//		virtual_account_auto_recover(); //余额系统自动核销
	}

	public void createOnlineCashFlowSourceDocument(){
		try {
			logger.info("begin to createOnlineSouceDocument!");
			sourceDocumentHandler.createOnlineSouceDocumentByDate(new Date());
		} catch(Exception e){
			logger.error("occur exception when createOnlineSouceDocument.");
			e.printStackTrace();
		}
		logger.info("end to createOnlineSouceDocument!");
	}
	
	public void updateOnLineCashFlowStatement(){
		try {
			logger.info("begin to createJVFromUnSignedOnlineSourceDocuments!");
			journalVoucherHandler.createJVFromUnSignedOnlineSourceDocuments(bankAccountCacheHandler);
		} catch(Exception e){
			logger.error("occur exception when createJVFromUnSignedOnlineSourceDocuments.");
			e.printStackTrace();
		}
		logger.info("end to createJVFromUnSignedOnlineSourceDocuments!");
	}
	
//	public void compensatory_loan_asset_recover(){
//		long start = System.currentTimeMillis();
//		try {
//			logger.info("begin to inter_account_transfer_for_business_payment_voucher!");
//			businessPaymentVoucherSession.handler_recover_asset_by_source_document();
//		} catch(Exception e){
//			logger.error("occur error inter_account_transfer_for_business_payment_voucher.");
//			e.printStackTrace();
//		}
//		logger.info("end to inter_account_transfer_for_business_payment_voucher! use times["+(System.currentTimeMillis()-start)+"]ms");
//	}
	
	
}
