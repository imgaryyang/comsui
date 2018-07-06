package com.suidifu.microservice.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.ReconciliationForSourceDocument;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.MisMatchedConditionException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("reconciliationForSubrogationDocumentHandler")
public class ReconciliationForSubrogationDocumentHandlerImpl extends ReconciliationForSourceDocument {
    @Resource
    private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;

    @Resource
    private OrderService orderService;

    @Resource
    private LedgerBookService ledgerBookService;

    @Resource
    private VirtualAccountHandler virtualAccountHandler;

    @Resource
    private VirtualAccountFlowService virtualAccountFlowService;

    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Resource
    private BusinessPaymentVoucherTaskHandler businessPaymentVoucherHandler;
    @Resource
    private LedgerBookHandler ledgerBookHandler;

    @Override
    public ReconciliationContext preAccountReconciliation(Map<String, Object> inputParams)
            throws AlreadyProcessedException, JsonProcessingException {
        long start = System.currentTimeMillis();
        ReconciliationContext context = super.preAccountReconciliation(inputParams);
        long end = System.currentTimeMillis();
        log.info("\n#preAccountReconciliation#super preAccountReconciliation use [{}]ms\n",
                start - end);

        start = System.currentTimeMillis();
        super.extractReconciliationContextFromRepaymentPlanNo(context, context.getSourceDocumentDetail().getRepaymentPlanNo());
        end = System.currentTimeMillis();
        log.info("\n#preAccountReconciliation#super extractReconciliationContextFromRepaymentPlanNo use [{}]ms\n",
                start - end);

        start = System.currentTimeMillis();
//        log.info("\ncontext is:{}\n", new ObjectMapper().writeValueAsString(context));
        if (context.getSourceDocumentDetail().getSecondType().equals(VoucherType.PAY.getKey())) {
            context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER);
            context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
        } else if (context.getSourceDocumentDetail().getSecondType().equals(VoucherType.ADVANCE.getKey())) {
            context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE);
            context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
        } else if (context.getSourceDocumentDetail().getSecondType().equals(VoucherType.MERCHANT_REFUND.getKey())) {
            context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE);
            context.setReconciliationType(ReconciliationType.RECONCILIATION_SUBROGATION);
        }
        context.setRecoverType(AssetRecoverType.LOAN_ASSET);
        context.resolveJournalVoucher(AccountSide.DEBIT, CounterAccountType.VirtualAccount, context.getAssetSet().getAssetUuid());
        fillVoucherNo(context, context.getSourceDocument());

        if (context.getSourceDocumentDetail().getActualPaymentTime() != null && context.getFinancialContract().isRepaymentDayCheck()) {
            context.setActualRecycleTime(context.getSourceDocumentDetail().getActualPaymentTime());
        }

        end = System.currentTimeMillis();
        log.info("\n#preAccountReconciliation#fillVoucherNo use [{}]ms\n", start - end);

        //部分核销 在父类中 按明细 （构建Map）
        return context;
    }

    @Override
    public void refreshVirtualAccount(ReconciliationContext context) {
        super.refreshVirtualAccount(context);
    }

    @Override
    public void refreshAsset(ReconciliationContext context) {
        super.refreshAsset(context);
    }

    @Override
    public void postAccountReconciliation(ReconciliationContext context) {
        super.postAccountReconciliation(context);
    }

    public boolean accountReconciliation(Map<String, Object> params) throws JsonProcessingException {
        return super.accountReconciliation(params);
    }

    @Override
    public void issueJournalVoucher(ReconciliationContext context) throws AlreadyProcessedException {
        super.issueJournalVoucher(context);
    }

    @Override
    public void validateReconciliationContext(ReconciliationContext context) throws AlreadyProcessedException {
        super.validateReconciliationContext(context);
        AssetSet assetSet = context.getAssetSet();
        if (assetSet == null || assetSet.isClearAssetSet()) {
            throw new MisMatchedConditionException("assetSet is null or clear");
        }

        SourceDocumentDetail detail = context.getSourceDocumentDetail();
        if (detail != null && detail.getActualPaymentTime() != null && context.getFinancialContract().isRepaymentDayCheck()) {
            boolean result = detail.checkTransactionTime(context.getSourceDocument().getOutlierTradeTime(),
                    context.getFinancialContract().getRepaymentCheckDays(), context.getContract().getBeginDate());
            if (!result) {
                throw new ReconciliationException("distanceDays greater than seven");
            }
        }
    }

    @Override
    public void extractRelatedBalanceFromLedgerBook(ReconciliationContext context) {
        String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
        Customer borrowerCustomer = context.getBorrowerCustomer();

        AssetSet assetSet = context.getAssetSet();
        long start = System.currentTimeMillis();
        BigDecimal receivableAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrowerCustomer.getCustomerUuid());
        long end = System.currentTimeMillis();
        log.info("#abcdefgh#extract_related_balnace_from_ledger_book use [" + (end - start) + "]ms");

        context.setReceivableAmount(receivableAmount);
    }
}