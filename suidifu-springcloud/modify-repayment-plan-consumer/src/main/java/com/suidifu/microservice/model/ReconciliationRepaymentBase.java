package com.suidifu.microservice.model;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.matryoshka.customize.ReconciliationDelayTaskProcess;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.handler.JournalVoucherHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.exception.AlreadyProcessedException;
import com.suidifu.owlman.microservice.exception.AssetSetLockException;
import com.suidifu.owlman.microservice.exception.MisMatchedConditionException;
import com.suidifu.owlman.microservice.exception.ReconciliationException;
import com.suidifu.owlman.microservice.model.ContractCategory;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderPaymentStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ReconciliationRepaymentBase implements ReconciliationRepayment {

    @Resource
    private JournalVoucherHandler journalVoucherHandler;
    @Resource
    private VirtualAccountFlowService virtualAccountFlowService;
    @Resource
    private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
    @Resource
    private VirtualAccountHandler virtualAccountHandler;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private JournalVoucherService journalVoucherService;
    @Resource
    private RepaymentPlanHandler repaymentPlanHandler;
    @Resource
    private SettlementOrderService settlementOrderService;
    @Resource
    private OrderService orderService;
    @Resource
    private ReconciliationDelayTaskProcess reconciliationDelayTaskProcess;
    @Resource
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Resource
    private FastHandler fastHandler;
    @Resource
    private RepaymentOrderItemService repaymentOrderItemService;
    @Resource
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
    @Resource
    private ClearingExecLogService clearingExecLogService;
    private static final Log logger = LogFactory.getLog(ReconciliationRepaymentBase.class);


    @Override
    public boolean accountReconciliation(Map<String, Object> params) {

        try {
            ReconciliationRepaymentContext context = preAccountReconciliation(params);

            validateReconciliationContext(context);

            relatedDocumentsProcessing(context);

            issueJournalVoucher(context);

            ledgerBookReconciliation(context);

            refreshVirtualAccount(context);

            refreshAsset(context);

            postAccountReconciliation(context);

            return true;

        } catch (MisMatchedConditionException | GiottoException e) {

            logger.info("#accountReconciliation# MisMatchedConditionException occur exception,error message :" + e);

            return false;
        } catch (AlreadyProcessedException e) {

            logger.info("#accountReconciliation# AlreadyProcessedException occur exception,error message :" + e);

            return true;
        }

    }


    public abstract void relatedDocumentsProcessing(ReconciliationRepaymentContext context);


    private void ledgerBookReconciliation(ReconciliationRepaymentContext context) {
        String configureContext = financialContractConfigurationService
            .getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                FinancialContractConfigurationCode.CROSS_PERIOD_CANCEL_AFTER_VERIFICATION.getCode());
        if (StringUtils.isBlank(configureContext) && context.getRecoverType().isLoanAsset()) {
            //进预收也不需要验证

            boolean isDeblock = repaymentPlanHandler.repaymentPlanDeblocking(context.getAssetSet());

            if (!isDeblock) {
                throw new AssetSetLockException();
            }
        }
        journalVoucherHandler.recover_asset_by_reconciliation_context_repayment_order(context);
    }

    private ContractCategory extractContractCategory(ReconciliationRepaymentContext context) {
        ContractCategory contractCategory = new ContractCategory();
        contractCategory.setRelatedBillContractInfoLv1(context.getFinancialContract().getUuid());
        contractCategory.setRelatedBillContractInfoLv2(context.getContract().getUuid());

        contractCategory.setRelatedBillContractNoLv1(context.getFinancialContract().getContractName());
        contractCategory.setRelatedBillContractNoLv2(context.getContract().getContractNo());

        if (context.getReconciliationType() == ReconciliationType.RECONCILIATION_REPURCHASE) {
            contractCategory.setRelatedBillContractNoLv4(context.getRepurchaseDoc().getRepurchaseDocUuid());
            contractCategory.setRelatedBillContractInfoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
            contractCategory.setRelatedBillContractNoLv3(context.getRepurchaseDoc().getRepurchaseDocUuid());
        } else {
            contractCategory
                .setRelatedBillContractNoLv4(context.getOrder() == null ? "" : context.getOrder().getOrderNo());
            contractCategory.setRelatedBillContractInfoLv3(
                context.getAssetSet() == null ? "" : context.getAssetSet().getAssetUuid());
            contractCategory.setRelatedBillContractNoLv3(
                context.getAssetSet() == null ? "" : context.getAssetSet().getSingleLoanContractNo());
        }
        return contractCategory;
    }


    @Override
    public abstract ReconciliationRepaymentContext preAccountReconciliation(Map<String, Object> inputParams)
        throws AlreadyProcessedException;

    @Override
    public void issueJournalVoucher(ReconciliationRepaymentContext context) throws AlreadyProcessedException {
        JournalVoucher journalVoucher = context.getIssuedJournalVoucher();
        ContractCategory contractCategory = extractContractCategory(context);
        journalVoucher.fillBillContractInfo(contractCategory);
        journalVoucherService.saveOrUpdate(journalVoucher);
        return;

    }

    @Override
    public void refreshVirtualAccount(ReconciliationRepaymentContext context) {
        FinancialContract financialContract = context.getFinancialContract();
        if (context.getRemittanceVirtualAccount() == null) {
            throw new ReconciliationException("remittanceVirtualAccount is null.");
        }
        BigDecimal currentBalance = null;
        VirtualAccount virtualAccount = context.getRemittanceVirtualAccount();
        if (context.getReconciliationType().isRecoveredByVirtualAccountSelf()) {
            VirtualAccount refreshedVirtualAccount = virtualAccountHandler
                .refreshVirtualAccountBalance(financialContract.getLedgerBookNo(),
                    context.getRemittanceVirtualAccount().getOwnerUuid(), financialContract.getFinancialContractUuid(),
                    context.getVirutalAccountVersionLock());
            currentBalance = refreshedVirtualAccount.getTotalBalance();
        }
        long start = System.currentTimeMillis();
        //新增账户流水 (瞬时余额为null) ，但不刷新 账户余额
        virtualAccountFlowService
            .addAccountFlow(context.getIssuedJournalVoucher().getJournalVoucherNo(), virtualAccount,
                context.getBookingAmount(), AccountSide.CREDIT, VirtualAccountTransactionType.INTER_ACCOUNT_REMITTANCE,
                context.getIssuedJournalVoucher().getJournalVoucherUuid(), currentBalance);
        long end = System.currentTimeMillis();
        logger.debug("#abcdefgh#addAccountFlow use [" + (end - start) + "]ms");
    }

    @Override
    public abstract void validateReconciliationContext(ReconciliationRepaymentContext context)
        throws AlreadyProcessedException;


    @Override
    public void refreshAsset(ReconciliationRepaymentContext context) throws GiottoException {

        RepaymentType repaymentType = RepaymentType.NORMAL;
        if (context.getDeductApplication() != null) {
            repaymentType = context.getDeductApplication().getRepaymentType();
        }
        updateAssetStatusLedgerBookHandler
            .updateAssetsFromLedgerBook(context.getLedgerBookNo(), Arrays.asList(context.getAssetSet().getAssetUuid()),
                context.getActualRecycleTime(), repaymentType, context.getAssetSet().getExecutingStatus());

        BigDecimal payingTotalAmount = repaymentOrderItemService
            .get_total_paying_amount(context.getAssetSet().getAssetUuid(), context.getRepaymentOrderItem()
                .getOrderDetailUuid());
        AssetSet assetSet = context.getAssetSet();
        OrderPaymentStatus expectedPaymentStatus = null;
        if (payingTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
            expectedPaymentStatus = OrderPaymentStatus.UNEXECUTINGORDER;
        } else {
            expectedPaymentStatus = OrderPaymentStatus.ORDERPAYMENTING;
        }
        if (expectedPaymentStatus != assetSet.getOrderPaymentStatus()) {
            //无处理订单
            assetSet.updateOrderPaymentStatus(expectedPaymentStatus);
            repaymentPlanService.update(assetSet);
        }

        //刷新缓存
        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO,
            context.getRepaymentOrderItem().getRepaymentBusinessNo(), FastAssetSet.class, false);

        if (fastAssetSet != null) {

            String assetSetUuid = fastAssetSet.getAssetUuid();

            RepaymentPlanExtraData extraData = repaymentPlanExtraDataService.get(assetSetUuid);
            extraData.setOrderPayingAmount(payingTotalAmount);
            repaymentPlanExtraDataService.saveOrUpdate(extraData);
        }

    }

    @Override
    public void postAccountReconciliation(ReconciliationRepaymentContext context) throws GiottoException {
        AssetRecoverType recoverType = context.getRecoverType();

        if (recoverType != null && recoverType.isLoanAssetOrGuranteeAsset()) {
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(context.getAssetSet().getAssetUuid());
            settlementOrderService.createSettlementOrder(assetSet);
        }

        Order order = context.getOrder();
        if (recoverType != null && recoverType.isLoanAsset() && order != null && StringUtils
            .isBlank(order.getChargesDetail())) {
            RepaymentChargesDetail chargesDetail = getChargesDetailByLoanAsset(context);
            order.setChargesDetail(chargesDetail.toJsonString());
            orderService.save(order);
        }
        createReconciliationDelayTask(recoverType, context);
        if (JournalVoucherType
            .JournalVoucherTypeUsedSpecialAccount().contains(context.getJournalVoucherType().ordinal())) {
            if (financialContractService
                .isSpecialAccountConfigured(context.getFinancialContract().getFinancialContractUuid())) {
                clearingExecLogService
                    .createClearingExecLog(context.getAssetSet(), getChargesDetailMapByLoanAsset(context),
                        context.getIssuedJournalVoucher().getUuid(),
                        context.getJournalVoucherType().ordinal(), context.getBookingAmount(),
                        context.getContract().getId(), "");
            }
        }
    }

    private void createReconciliationDelayTask(AssetRecoverType recoverType,
        ReconciliationRepaymentContext context) {
        try {

//			if(context.getRecoverType().isReceivableInAdvance())
//				return;

            logger.info("create_reconciliation_delay_task start:recoverType[" + recoverType + "],assetsetuuid[" + (
                context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid()) + "],"
                + "repurchaseUuid[" + (context.getRepurchaseDoc() == null ? null
                : context.getRepurchaseDoc().getRepurchaseDocUuid()) + "]");
            if (recoverType == null || !recoverType.isLoanAssetOrRepurchaseAsset()) {
                logger.info("create_reconciliation_delay_task recoverType is not loanRepurAssset");
                return;
            }
            RepaymentChargesDetail chargesDetail = null;
            if (recoverType.isLoanAsset()) {
                chargesDetail = getChargesDetailByLoanAsset(context);
            } else if (recoverType.isRepurchaseAsset()) {
                chargesDetail = getChargesDetailByRepurchaseDoc(context);
            }

            //正常核销
            String taskUuidForRecover = financialContractConfigurationService
                .getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                    FinancialContractConfigurationCode.RECOVER_DELAY_TASK_UUID.getCode());
            if (StringUtils.isNotBlank(taskUuidForRecover)) {
                logger.info("taskUuid:[" + taskUuidForRecover + "]");
                reconciliationDelayTaskProcess
                    .processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail,
                        context.getAssetSet(),
                        context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid(),
                        recoverType, taskUuidForRecover);
            }

            //逾期核销
            String taskUuidForOverRecover = financialContractConfigurationService
                .getFinancialContractConfigContentContent(context.getFinancialContract().getUuid(),
                    FinancialContractConfigurationCode.BQ_OVER_RECOVER_DELAY_TASK_UUID.getCode());
            if (StringUtils.isNotBlank(taskUuidForOverRecover)) {
                logger.info("taskUuid:[" + taskUuidForOverRecover + "]");
                reconciliationDelayTaskProcess
                    .processingDelayTask(context.getActualRecycleTime(), context.getContract(), chargesDetail,
                        context.getAssetSet(),
                        context.getRepurchaseDoc() == null ? null : context.getRepurchaseDoc().getRepurchaseDocUuid(),
                        recoverType, taskUuidForOverRecover);
            }

            logger.info("create_reconciliation_delay_task end:recoverType[" + recoverType + "],assetsetuuid[" + (
                context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid()) + "],"
                + "repurchaseUuid[" + (context.getRepurchaseDoc() == null ? null
                : context.getRepurchaseDoc().getRepurchaseDocUuid()) + "]");
        } catch (Exception e) {
            logger.error("create_reconciliation_delay_task error:recoverType[" + recoverType + "],assetsetuuid[" + (
                context.getAssetSet() == null ? null : context.getAssetSet().getAssetUuid()) + "],"
                + "repurchaseUuid[" + (context.getRepurchaseDoc() == null ? null
                : context.getRepurchaseDoc().getRepurchaseDocUuid()) + "],msg[" + ExceptionUtils.getFullStackTrace(e)
                + "].");
        }
    }

    private RepaymentChargesDetail getChargesDetailByLoanAsset(ReconciliationRepaymentContext context) {
        Map<String, BigDecimal> detailAmountMap = context.getBookingDetailAmount();
        if (MapUtils.isEmpty(detailAmountMap)) {
            detailAmountMap = ledgerBookStatHandler
                .get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(context.getLedgerBookNo(),
                    context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getAssetSet().getAssetUuid());
        }
        return new RepaymentChargesDetail(detailAmountMap);
    }

    private Map<String, BigDecimal> getChargesDetailMapByLoanAsset(ReconciliationRepaymentContext context) {
        Map<String, BigDecimal> detailAmountMap = context.getBookingDetailAmount();
        if (MapUtils.isEmpty(detailAmountMap)) {
            detailAmountMap = ledgerBookStatHandler
                .get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(context.getLedgerBookNo(),
                    context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getAssetSet().getAssetUuid());
        }
        return detailAmountMap;
    }

    private RepaymentChargesDetail getChargesDetailByRepurchaseDoc(ReconciliationRepaymentContext context) {
        RepurchaseDoc repurchaseDoc = context.getRepurchaseDoc();
        Map<String, BigDecimal> detailAmount = ledgerBookStatHandler
            .get_jv_repurchase_detail_amount(context.getLedgerBookNo(),
                context.getIssuedJournalVoucher().getJournalVoucherUuid(), context.getContract().getUuid());
        RepaymentChargesDetail repurchaseDetail = new RepaymentChargesDetail();
        if (repurchaseDoc != null) {
            repurchaseDetail.fillRepurchaseAmount(detailAmount);
        }
        return repurchaseDetail;
    }

}




