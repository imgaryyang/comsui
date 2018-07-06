package com.suidifu.microservice.entity;

import java.util.HashMap;
import java.util.Map;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.zufangbao.sun.utils.SpringContextUtil;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;

public interface Reconciliation {

	public abstract void validateReconciliationContext(ReconciliationContext context) throws AlreadyProcessedException;

	public abstract void refreshVirtualAccount(ReconciliationContext context);

	public abstract void issueJournalVoucher(ReconciliationContext context)  throws AlreadyProcessedException;

	public abstract void postAccountReconciliation(ReconciliationContext context);
	
	public abstract void refreshAsset(ReconciliationContext context);

	public abstract ReconciliationContext preAccountReconciliation(Map<String,Object> inputParams) throws AlreadyProcessedException;

	public abstract boolean accountReconciliation(Map<String,Object> params) ;
	
	
	
	public static HashMap<String,String>   reconciliationVoucherTypeTable=
			new HashMap<String,String>(){{
				put(VoucherType.REPURCHASE.getKey(), "reconciliationForRepurchaseDocumentHandler");
				put(VoucherType.PAY.getKey(), "reconciliationForSubrogationDocumentHandler");
				put(VoucherType.MERCHANT_REFUND.getKey(), "reconciliationForSubrogationDocumentHandler");
				put(VoucherType.ADVANCE.getKey(), "reconciliationForSubrogationDocumentHandler");
				put(VoucherType.ACTIVE_PAY.getKey(), "reconciliationForInitiativePaymentDocumentHandler");
				put(VoucherType.OTHER_PAY.getKey(), "reconciliationForInitiativePaymentDocumentHandler");
				put(ReconciliationType.RECONCILIATION_AUTO_RECOVER_SETTLEMENT_SHEET.getKey(), "reconciliationForReconciliationSettlementSheetDocumentHandler");
//				put(ReconciliationType.RECONCILIATION_AUTO_RECOVER_GUARANTEE.getKey(), "reconciliationForReconciliationGuaranteeDocumentHandler");
				put(VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), "reconciliationForDeductApiDocumentHandler");
				put(VoucherType.GUARANTEE.getKey(), "reconciliationForGuaranteeVoucherHandler");
				put(ReconciliationType.RECONCILIATION_DEDUCT_REFUND.getKey(), "reconciliationFoDeductApiRefundHandler");
				put(ReconciliationType.RECONCILIATION_Clearing_Voucher.getKey(), "reconciliationForClearingVoucherHandler");
			}};
	
	public static Reconciliation reconciliationFactory(String voucherType)
	{
		String BeanName=reconciliationVoucherTypeTable.get(voucherType);
		Reconciliation accountReconciliation=SpringContextUtil.getBean(BeanName);
		return accountReconciliation;
	}

}
