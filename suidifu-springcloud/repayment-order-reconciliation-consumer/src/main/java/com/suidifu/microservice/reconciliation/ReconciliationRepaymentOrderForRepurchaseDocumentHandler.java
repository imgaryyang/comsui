package com.suidifu.microservice.reconciliation;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.microservice.model.ReconciliationRepaymentContext;
import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.AssetSetHistory;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import com.zufangbao.sun.yunxin.service.RepaymentPlanHistoryService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("reconciliationRepaymentOrderForRepurchaseDocumentHandler")
public class ReconciliationRepaymentOrderForRepurchaseDocumentHandler extends ReconciliationRepaymentOrderForSourceDocument {
    @Resource
    private RepurchaseService repurchaseService;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private ContractService contractService;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private RepaymentPlanHistoryService repaymentPlanHistoryService;

    @Override
    public ReconciliationRepaymentContext preAccountReconciliation(
            Map<String, Object> inputParams) throws AlreadyProcessedException {
        ReconciliationRepaymentContext context = super.preAccountReconciliation(inputParams);
        String repurchaseDocByUuid = context.getRepaymentOrderItem().getRepaymentBusinessUuid();
        RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocByUuid);
        Contract contract = contractService.getContract(repurchaseDoc.getContractId());

        super.extractReconciliationContextFromContractUuidWithoutAsset(context, contract);
        context.setRepurchaseDoc(repurchaseDoc);
        context.setRecoverType(AssetRecoverType.REPURCHASE_ASSET);
        context.setJournalVoucherType(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE);

        if (context.getRepaymentOrderItem().getRepaymentWay().equals(RepaymentWay.REPURCHASE)) {
            context.setReconciliationType(ReconciliationType.RECONCILIATION_REPURCHASE);
        }

        context.resolveJournalVoucher(AccountSide.DEBIT,
                CounterAccountType.VirtualAccount,
                context.getRepurchaseDoc().getUuid());

        return context;
    }

    @Override
    public void extractRelatedBalnaceFromLedgerBook(ReconciliationRepaymentContext context) {
        String ledgerBookNo = context.getFinancialContract().getLedgerBookNo();
        Contract contract = context.getContract();
        Customer borrowerCustomer = context.getBorrowerCustomer();

        AssetSet assetSet = context.getAssetSet();
        if (assetSet != null) {
            BigDecimal receivableAmount = ledgerBookStatHandler.get_receivable_amount(ledgerBookNo, assetSet.getAssetUuid(), borrowerCustomer.getCustomerUuid());
            BigDecimal unearnedAmount = ledgerBookStatHandler.get_unearned_amount(ledgerBookNo, assetSet.getAssetUuid());
            context.setReceivableAmount(receivableAmount);
            context.setUnearnedAmount(unearnedAmount);
        }

        BigDecimal repurchaseAmount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contract.getUuid());
        context.setRepurchaseAmount(repurchaseAmount);
    }


    @Override
    public void refreshVirtualAccount(ReconciliationRepaymentContext context) {
        super.refreshVirtualAccount(context);
    }

    @Override
    public void refreshAsset(ReconciliationRepaymentContext context) {
        //回购不刷新资产
    }

    @Override
    public void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {
        if (BigDecimal.ZERO.compareTo(ledgerBookStatHandler.get_repurchase_amount(context.getLedgerBookNo(), context.getContract().getUuid())) == 0) {
            RepurchaseDoc repurchaseDoc = context.getRepurchaseDoc();
            Contract contract = context.getContract();
            repurchaseDoc.setRepurchaseStatus(RepurchaseStatus.REPURCHASED);
            repurchaseDoc.setLastModifedTime(new Date());
            repurchaseDoc.setVerificationTime(new Date());
            List<String> assetSetUuids = repurchaseDoc.getAssetSetUuidList();
            for (String assetSetUuid : assetSetUuids) {
                AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
                assetSet.repurchase();
                if (Objects.equals(assetSet.getActiveStatus(), AssetSetActiveStatus.INVALID)) {
                    AssetSetHistory history = new AssetSetHistory().init(assetSet);
                    repaymentPlanService.delete(assetSet);
                    repaymentPlanHistoryService.save(history);
                } else {
                    repaymentPlanService.saveOrUpdate(assetSet);
                }
            }

            contract.setState(ContractState.REPURCHASED);
            contractService.saveOrUpdate(contract);
            repurchaseService.saveOrUpdate(repurchaseDoc);
        }
        super.postAccountReconciliation(context);
    }

    @Override
    public void validateReconciliationContext(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
        super.validateReconciliationContext(context);
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(context.getLedgerBookNo());
        if (ledgerBook == null)
            throw new ReconciliationException("empty ledgerBook");
        if (context.getRepurchaseDoc() == null)
            throw new ReconciliationException("empty repurchaseDoc");
    }

    public void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
        super.issueJournalVoucher(context);
    }

    @Override
    public void relatedDocumentsProcessing(ReconciliationRepaymentContext context) {
    }

    public boolean accountReconciliation(Map<String, Object> params) {
        return super.accountReconciliation(params);
    }

    public void ledgerBookReconciliation(ReconciliationRepaymentContext context) {
        super.recoverEachFrozenCapitalAmount(context);
        super.ledgerBookReconciliation(context);
    }
}