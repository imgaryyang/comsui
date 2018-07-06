package com.suidifu.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.ReceivableInAdvanceStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("reconciliationRepaymentOrderForReceivableInAdvanceHandler")
public class ReconciliationRepaymentOrderForReceivableInAdvanceHandler
	extends ReconciliationRepaymentOrderForSourceDocument {

	@Autowired
	private ContractService contractService;
	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	@Autowired
	private FastHandler fastHandler;
	@Autowired
	private DeductPlanService deductPlanService;
	@Autowired
	private BankAccountCache bankAccountCache;

	@Override
	public void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
		super.validateReconciliationContext(context);
		RepaymentOrderItem repaymentOrderItem = context.getRepaymentOrderItem();
		if(ReceivableInAdvanceStatus.RECEIVED==repaymentOrderItem.getReceivableInAdvanceStatus()){
			throw new ReconciliationException("还款订单明细已经进预收");
		}
	}

	@Override
	public void relatedDocumentsProcessing(ReconciliationRepaymentContext context){
		//预收不操作结算单
	}

	@Override
	public void refreshVirtualAccount(ReconciliationRepaymentContext context) {
		//预收不操作虚户
	}

	@Override
	public void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
		super.issueJournalVoucher(context);

	}

	@Override
	public void ledger_book_reconciliation(ReconciliationRepaymentContext context){
		super.ledger_book_reconciliation(context);
	}

	@Override
	public void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {
		RepaymentOrderItem repaymentOrderItem = context.getRepaymentOrderItem();
		// 更新预收状态
		repaymentOrderItemService.update_receivable_in_advance_status_by(repaymentOrderItem.getOrderDetailUuid(),
			ReceivableInAdvanceStatus.RECEIVED);
		fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID, repaymentOrderItem.getOrderDetailUuid(),
			FastRepaymentOrderItem.class, false);
	}

	@Override
	public void refreshAsset(ReconciliationRepaymentContext context) {
		//预收不操作资产
	}


	@Override
	public ReconciliationRepaymentContext preAccountReconciliation(Map<String, Object> inputParams)
		throws AlreadyProcessedException {

		ReconciliationRepaymentContext context = super.preAccountReconciliation(inputParams);
		PaymentOrder paymentOrder=(PaymentOrder)inputParams.get(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER);
		context.setPaymentOrder(paymentOrder);
		CounterAccountType counterAccountType = null;
		ReconciliationType reconciliationType = null;
		if (context.getRepaymentOrderItem().getRepaymentWay().isDeductType()) {
			reconciliationType = ReconciliationType.RECONCILIATION_THIRDPARTY_DEDUCT;
			counterAccountType = CounterAccountType.BankAccount;
		} else {
			throw new ReconciliationException("付款方式不支持");
		}

		context.setReconciliationType(reconciliationType);

		RepaymentOrderItem item = context.getRepaymentOrderItem();

		context.setRecoverType(AssetRecoverType.RECEIVABLE_IN_ADVANCE);
		context.setCurrentPeriod(item.getCurrentPeriod());
		context.setJournalVoucherType(JournalVoucherType.RECEIVABLE_IN_ADVANCE);
		Contract contract = contractService.getContract(context.getRepaymentOrderItem().getContractUuid());
		super.extractReconciliationContextFromContractUuidWithoutAsset(context, contract);
		context.setReceivableJournalVoucher(true);
		context.resovleJournalVoucher(AccountSide.DEBIT, counterAccountType, getJournalVoucherBillingPlanUuid(item));
		if(context.isRepaymentPlanTimeInvalid(context.getPlanRepaymentTimeLock())){
			Date paymentSusTime = deductPlanService.getMaxPaymentSucTime(paymentOrder.getOutlierDocumentUuid());
			context.setActualRecycleTime(paymentSusTime);
		}
		String merIdClearingNo = deductPlanService.getMerIdClearingNoKey(paymentOrder.getOutlierDocumentUuid());
		DepositeAccountInfo unionDepositAccount = bankAccountCache.extractThirdPartyPaymentChannelAccountFrom(context.getFinancialContract(), merIdClearingNo);
		context.setFinancialContractAccountForLedgerBook(unionDepositAccount);

		return context;
	}

	private String getJournalVoucherBillingPlanUuid(RepaymentOrderItem item) {
		return new StringBuilder(item.getContractUuid()).append(":").append(item.getCurrentPeriod()).toString();
	}

	@Override
	public boolean accountReconciliation(Map<String, Object> params) {
		return super.accountReconciliation(params);

	}

}
