package com.suidifu.microservice.reconciliation;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.MisMatchedConditionException;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("reconciliationRepaymentOrderForSubrogationDocumentHandler")
public class ReconciliationRepaymentOrderForSubrogationDocumentHandler extends
        ReconciliationRepaymentOrderForSourceDocument {
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;

    @Override
    public ReconciliationRepaymentContext preAccountReconciliation(
            Map<String, Object> inputParams) throws AlreadyProcessedException {
        ReconciliationRepaymentContext context = super.preAccountReconciliation(inputParams);
        super.extractReconciliationContextFromRepaymentPlanNo(context, context.getRepaymentOrderItem().getRepaymentBusinessNo());

        if (context.getRepaymentOrderItem().getRepaymentWay().equals(RepaymentWay.MERCHANT_PAY)) {
            context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE);
            context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
        } else if (context.getRepaymentOrderItem().getRepaymentWay().equals(RepaymentWay.MERCHANT_TRANSFER)) {
            context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER);
            context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
        } else if (context.getRepaymentOrderItem().getRepaymentWay().equals(RepaymentWay.MERCHANT_TRANSFER_BALANCE)) {
            context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE);
            context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
        }
        context.setRecoverType(AssetRecoverType.LOAN_ASSET);
        context.resolveJournalVoucher(AccountSide.DEBIT, CounterAccountType.VirtualAccount, context.getAssetSet().getAssetUuid());

        return context;
    }

    @Override
    public void extractRelatedBalnaceFromLedgerBook(ReconciliationRepaymentContext context) {
        String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
        Customer borrowerCustomer = context.getBorrowerCustomer();

        AssetSet assetSet = context.getAssetSet();
        long start = System.currentTimeMillis();
        BigDecimal receivableAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrowerCustomer.getCustomerUuid());
        long end = System.currentTimeMillis();
        log.info("\n#extractRelatedBalnaceFromLedgerBook use [{}]ms",
                end - start);

        context.setReceivableAmount(receivableAmount);
    }

    @Override
    public void ledgerBookReconciliation(ReconciliationRepaymentContext context) {
        long start = System.currentTimeMillis();
        super.recoverEachFrozenCapitalAmount(context);
        long end = System.currentTimeMillis();

        log.info("#abcdefgh#recoverEachFrozenCapitalAmount use [{}]ms",
                end - start);
        super.ledgerBookReconciliation(context);
    }

    @Override
    public void refreshVirtualAccount(ReconciliationRepaymentContext context) {
        super.refreshVirtualAccount(context);
    }

    @Override
    public void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException {
        super.refreshAsset(context);
    }

    @Override
    public void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {
        super.postAccountReconciliation(context);
    }

    @Override
    public boolean accountReconciliation(Map<String, Object> params) {
        return super.accountReconciliation(params);
    }

    @Override
    public void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
        super.issueJournalVoucher(context);
    }

    @Override
    public void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
        super.validateReconciliationContext(context);
        AssetSet assetSet = context.getAssetSet();
        if (assetSet == null || assetSet.getAssetStatus() == AssetClearStatus.CLEAR) {
            throw new MisMatchedConditionException("assetSet is null or clear");
        }
    }
}