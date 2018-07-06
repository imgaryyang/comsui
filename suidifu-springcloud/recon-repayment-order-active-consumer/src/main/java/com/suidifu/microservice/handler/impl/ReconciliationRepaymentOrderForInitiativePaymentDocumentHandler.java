package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.model.JournalVoucher;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.repayment.order.DetailPayStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("reconciliationRepaymentOrderForInitiativePaymentDocumentHandler")
public class ReconciliationRepaymentOrderForInitiativePaymentDocumentHandler extends
    ReconciliationRepaymentOrderForSourceDocument {
    @Autowired
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Autowired
    private JournalVoucherService journalVoucherService;
    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;
    @Autowired
    private FastHandler fastHandler;

    @Override
    public ReconciliationRepaymentContext preAccountReconciliation(
            Map<String, Object> inputParams) throws AlreadyProcessedException {

        ReconciliationRepaymentContext context = super.preAccountReconciliation(inputParams);
        super.extractReconciliationContextFromRepaymentPlanNo(context, context.getRepaymentOrderItem().getRepaymentBusinessNo());

        String oldVersion = context.getRemittanceBorrowerVirtuaAccount().getVersion();
        context.setVirutalAccountVersionLock(oldVersion);
        context.setRecoverType(AssetRecoverType.LOAN_ASSET);
        context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_INITIATIVE);
        context.resovleJournalVoucher(AccountSide.DEBIT, CounterAccountType.VirtualAccount, context.getAssetSet().getAssetUuid());
        if (context.getRepaymentOrderItem().getRepaymentWay() == RepaymentWay.ACTIVE_PAY
                || context.getRepaymentOrderItem().getRepaymentWay() == RepaymentWay.OTHER_PAY) {
            context.setReconciliationType(ReconciliationType.RECONCILIATION_INITIATIVE);
        }
        return context;

    }

    @Override
    public void extract_related_balnace_from_ledger_book(ReconciliationRepaymentContext context) {

        String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
        Customer borrower_customer = context.getBorrower_customer();

        AssetSet assetSet = context.getAssetSet();
        BigDecimal receivalbeAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrower_customer.getCustomerUuid());
        BigDecimal unearnedAmount = ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, assetSet.getAssetUuid());
        context.setReceivalbeAmount(receivalbeAmount);
        context.setUnearnedAmount(unearnedAmount);

        BigDecimal borrower_customer_balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, borrower_customer.getCustomerUuid());
        context.setBorrowerCustomerBalance(borrower_customer_balance);
    }

    @Override
    public void refreshVirtualAccount(ReconciliationRepaymentContext context) {
        if (is_recovered_asset(context.getUnearnedAmount(), context.getReceivalbeAmount()) == true)
            return;
        super.refreshVirtualAccount(context);
    }

    private boolean is_recovered_asset(BigDecimal unearned_amount,
                                       BigDecimal receivable_amount) {
        return BigDecimal.ZERO.compareTo(unearned_amount) == 0 && BigDecimal.ZERO.compareTo(receivable_amount) == 0;
    }

    @Override
    public void ledger_book_reconciliation(ReconciliationRepaymentContext context) {
        if (is_recovered_asset(context.getUnearnedAmount(), context.getReceivalbeAmount()) == true)
            return;
        String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
        String customerUuid = context.getRemittanceBorrowerVirtuaAccount().getOwnerUuid();
        BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, customerUuid);
        BigDecimal bookingAmount = context.getRepaymentOrderItem().getAmount();

        if (context.getReceivalbeAmount().add(context.getUnearnedAmount()).compareTo(BigDecimal.ZERO) <= 0 || balance.compareTo(bookingAmount) < 0) {
            throw new ReconciliationException("balance less than bookingAmount or ReceivalbeAmount equal 0");
        }

        super.ledger_book_reconciliation(context);

    }

    @Override
    public void relatedDocumentsProcessing(ReconciliationRepaymentContext context) {

        if (is_recovered_asset(context.getUnearnedAmount(), context.getReceivalbeAmount()) == true)
            return;

        super.relatedDocumentsProcessing(context);
    }

    @Override
    public void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException

    {
        if (is_recovered_asset(context.getUnearnedAmount(), context.getReceivalbeAmount()) == true)
            return;
        super.issueJournalVoucher(context);
    }

    @Override
    public void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException {
        //1. 余额自动还款后，刷新资产的还款时间；2. 余额未还款，刷新资产状态
        super.refreshAsset(context);
    }

    @Override
    public void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException {

        List<JournalVoucher> journalVoucherList = journalVoucherService.getJournalVoucherListByTypeAndAssetSetUuid(context.getAssetSet().getAssetUuid());
        boolean exists_jv_except_virtual_auto_issue = !CollectionUtils.isEmpty(journalVoucherList);
        if (exists_jv_except_virtual_auto_issue && context.getAssetSet().getAssetStatus() == AssetClearStatus.CLEAR) {

            throw new ReconciliationException("repeat write off");

        }
        super.validateReconciliationContext(context);
    }

    @Override
    public boolean accountReconciliation(Map<String, Object> params) {
        return super.accountReconciliation(params);
    }


    @Override
    public void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {

        if (is_recovered_asset(context.getUnearnedAmount(), context.getReceivalbeAmount()) == false) {
            super.postAccountReconciliation(context);
            return;
        } else {
            fill_compensate_active_payment_info_and_update_detail(context);
        }
    }

    private void fill_compensate_active_payment_info_and_update_detail(ReconciliationRepaymentContext context) throws GiottoException {

        RepaymentOrderItem repaymentOrderItem = context.getRepaymentOrderItem();
        journalVoucherService.transfer_and_fill_compensate_active_payment_info_after_auto_remittance_repayment_order(context.getCompanyId(), context.getAssetSet().getAssetUuid(), context.getJournalVoucherType(), context.getJournalVoucherResovler(), context.getActualRecycleTime(), context.getSourceDocument() == null ? "" : context.getSourceDocument().getOutlierMemo());
        repaymentOrderItemService.update_pay_status_by(repaymentOrderItem.getOrderDetailUuid(), DetailPayStatus.PAID);
        fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID, repaymentOrderItem.getOrderDetailUuid(), FastRepaymentOrderItem.class, false);

    }

}
