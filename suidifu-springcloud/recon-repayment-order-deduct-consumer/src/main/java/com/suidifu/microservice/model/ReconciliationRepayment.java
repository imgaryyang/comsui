package com.suidifu.microservice.model;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.zufangbao.sun.utils.SpringContextUtil;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import java.util.HashMap;
import java.util.Map;

public interface ReconciliationRepayment {

	public abstract void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException;

	public abstract void refreshVirtualAccount(ReconciliationRepaymentContext context);

	public abstract void issueJournalVoucher(ReconciliationRepaymentContext context)  throws AlreadyProcessedException;

	public abstract void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException;
	
	public abstract void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException;

	public abstract ReconciliationRepaymentContext preAccountReconciliation(
      Map<String, Object> inputParams) throws AlreadyProcessedException;

	public abstract boolean accountReconciliation(Map<String, Object> params) ;
	
	
	
	public static HashMap<String,String>   reconciliationVoucherTypeTable=
			new HashMap<String,String>(){{
				put(RepaymentWay.MERCHANT_PAY.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
				put(RepaymentWay.MERCHANT_TRANSFER.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
				put(RepaymentWay.MERCHANT_TRANSFER_BALANCE.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
				put(RepaymentWay.ACTIVE_PAY.getKey(), "reconciliationRepaymentOrderForInitiativePaymentDocumentHandler");
				put(RepaymentWay.OTHER_PAY.getKey(), "reconciliationRepaymentOrderForInitiativePaymentDocumentHandler");

				put(RepaymentWay.REPURCHASE.getKey(), "reconciliationRepaymentOrderForRepurchaseDocumentHandler");
				put(RepaymentWay.MERCHANT_DEDUCT_EASY_PAY.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
				put(RepaymentWay.ON_LINE_DEDUCT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
				put(RepaymentWay.MERCHANT_DEDUCT_ONLINE_PAYMENT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
				put(RepaymentWay.MERCHANT_DEDUCT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
				
			}};
	
	public static ReconciliationRepayment receivableInAdvanceBean()
	{
			return SpringContextUtil.getBean("reconciliationRepaymentOrderForReceivableInAdvanceHandler");
	}
	
	public static ReconciliationRepayment reconciliationFactory(String voucherType)
	{

		String BeanName=reconciliationVoucherTypeTable.get(voucherType);
		ReconciliationRepayment accountReconciliation=SpringContextUtil.getBean(BeanName);
		return accountReconciliation;
	}

	public static ReconciliationRepayment recover_reconciliation()
	{
		String BeanName="reconciliationRepaymentOrderForRecoverHandler";
		ReconciliationRepayment accountReconciliation=SpringContextUtil.getBean(BeanName);
		return accountReconciliation;
	}
}
