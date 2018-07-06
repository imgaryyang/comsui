package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.OrderItemEditHandler;
import com.suidifu.microservice.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.handler.RepaymentOrderPlacingHandler;
import com.suidifu.owlman.microservice.model.RepaymentOrderParameters;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.DetailAliveStatus;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.ReceivableInAdvanceStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import com.zufangbao.sun.yunxin.entity.repayment.FeeType;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hjl
 */
@Component("orderItemEditHandler")
public class OrderItemEditHandlerImpl implements OrderItemEditHandler {
    private static Log logger = LogFactory.getLog(OrderItemEditHandlerImpl.class);
    @Autowired
    private RepaymentOrderItemChargeService repaymentOrderItemChargeService;
    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;
    @Autowired
    private RepaymentOrderService repaymentOrderService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private LedgerBookService ledgerBookService;
    @Autowired
    private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;
    @Autowired
    private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
    @Autowired
    private RepaymentOrderPlacingHandler repaymentOrderPlacingHandler;
    @Autowired
    private JournalVoucherService journalVoucherService;
    @Autowired
    private LedgerBookHandler ledgerBookHandler;
    @Autowired
    private FastHandler fastHandler;
    @Autowired
    private SourceDocumentService sourceDocumentService;
    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;

    @Override
    public void lapseAndCreateNewItemHandler(String contractUuid, String orderUuid, String itemUuidToBeLapsed,
                                             List<RepaymentOrderItemModel> repaymentOrderItemModels, int priority)
            throws GiottoException {
        RepaymentOrderItem repaymentOrderItem = repaymentOrderItemService.getRepaymentOrderItemByUuid(itemUuidToBeLapsed);
        FinancialContract financialContract = financialContractService.getFinancialContractBy(repaymentOrderItem.getFinancialContractUuid());
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);
        Contract contract = contractService.getContract(contractUuid);

        // 校验
        if (!repaymentOrder.canBeEdit()) {
            throw new ApiException("还款订单的状态不可编辑");
        }
        if (!repaymentOrderItem.canBeEdit()) {
            throw new ApiException("还款订单明细状态不可编辑");
        }

        logger.info("lapsedOrderItem ReceivedInAdvanceStatus : '" + repaymentOrderItem.getReceivableInAdvanceStatus().getKey() + "'");
        handlerJournalVoucherAndSourceDoucument(repaymentOrderItem, ledgerBook);
        handlerItemRefreshAsset(itemUuidToBeLapsed);
        handlerItemsInsert(repaymentOrder, itemUuidToBeLapsed, repaymentOrderItemModels, financialContract,
                ledgerBook, contract);

        // recover asset if single_contract_model
        PaymentOrder paymentOrder = null;
        if (repaymentOrder.transferToRepaymentStatus() == RepaymentStatus.PAYMENT_END && repaymentOrder.isDeductRepayWay()) {
            paymentOrder = getPaymentOrder(repaymentOrder);
            thirdPartVoucherCommandLogService.createThirdPartyVoucherCommandLogAfterDeduct(paymentOrder, paymentOrder.getPaymentGateWay(), true);
        }

        if (repaymentOrder.transferToRepaymentStatus() == RepaymentStatus.PAYMENT_END && repaymentOrderItem.isReceivableInAdvance()) {
            if (paymentOrder == null) {
                return;
            }
            //尝试去核销
            thirdPartyVoucherRepaymentOrderWithReconciliationNoSession.thirdPartVoucherWithReconciliation(repaymentOrderItem.getOrderUuid(), paymentOrder.getUuid());
        }
    }

    private void lapseSourceDocAndJournalVoucher(String orderUuid) {
        PaymentOrder paymentOrder = paymentOrderService.getOneSucPaymentOrder(orderUuid);
        if (paymentOrder == null) {//状态为效验成功是可能还没有扣款
            return;
        }
        String sourceDocumentUuid = sourceDocumentService.getUnWriteOffSourceDocumentUuidBy(paymentOrder.getOutlierDocumentUuid());
        SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
        if (sourceDocument == null) {
            return;
        }
        sourceDocument.setSourceDocumentStatus(SourceDocumentStatus.INVALID);
        //作废jv
        journalVoucherService.lapseJournalVoucherBy(sourceDocumentUuid);
        //作废sourdtl
        sourceDocumentDetailService.lapseSourceDocumentDetailBy(sourceDocumentUuid);
        //作废sour
        sourceDocumentService.update(sourceDocument);
    }

    private PaymentOrder getPaymentOrder(RepaymentOrder repaymentOrder) {
        return paymentOrderService.getSuccessPaymentOrderOrderUuid(repaymentOrder.getOrderUuid());
    }

    private void handlerReceivableInAdvance(String orderUuid, String orderItemUuid, LedgerBook ledgerBook) {
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);
        logger.info("repaymentOrder ReceivedInAdvanceStatus : '" + repaymentOrder.getReceivableInAdvanceStatus() + "'");
        if (!repaymentOrder.hasReceivedInAdvance()) {
            throw new ApiException("还款订单与订单明细预收状态不一致");
        }
        repaymentOrder.setReceivableInAdvanceStatus(ReceivableInAdvanceStatus.UNRECEIVED);
        List<JournalVoucher> journalVouchers = journalVoucherService.getJournalVoucherBy(orderUuid, orderItemUuid);
        if (CollectionUtils.isEmpty(journalVouchers) || journalVouchers.size() >= 2) {
            throw new ApiException("JournalVoucher凭证异常,repaymentOrderItemUuid为： " + orderItemUuid);
        }
        JournalVoucher journalVoucher = journalVouchers.get(0);
        journalVoucher.setStatus(JournalVoucherStatus.VOUCHER_LAPSE);
        repaymentOrderService.update(repaymentOrder);
        journalVoucherService.update(journalVoucher);
        // ledgerBook的作废
        ledgerBookHandler.lapse_ledgers_by_voucher(journalVoucher.getJournalVoucherUuid(), "", "", ledgerBook);
    }

    //刷新journalVourcher和sourceDocument
    private void handlerJournalVoucherAndSourceDoucument(RepaymentOrderItem repaymentOrderItem, LedgerBook ledgerBook) {
        if (repaymentOrderItem.hasReceivedInAdvance()) {
            // laspe recived handler
            handlerReceivableInAdvance(repaymentOrderItem.getOrderUuid(), repaymentOrderItem.getOrderDetailUuid(), ledgerBook);
            // laspe item handler
        } else {
            //非预收情况，考虑线上扣款，作废所有凭证（sourceDocument,sourceDocumentDetail,JV）
            if (repaymentOrderItem.repaymentWayIsOnlineDeduct()) {
                lapseSourceDocAndJournalVoucher(repaymentOrderItem.getOrderUuid());
            }
        }
    }

    private void handlerItemRefreshAsset(String orderItemUuid) throws GiottoException {
        // sql
        List<String> repaymentItemUuids = new ArrayList<>();
        repaymentItemUuids.add(orderItemUuid);
        repaymentOrderItemService.updateStatusRepaymentOrderItemByUuids(repaymentItemUuids, DetailAliveStatus.INVALID);
        fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID, orderItemUuid, FastRepaymentOrderItem.class, false);
        RepaymentOrderItem repaymentOrderItem = repaymentOrderItemService.getRepaymentOrderItemByUuid(orderItemUuid);
        if (!repaymentOrderItem.isReceivableInAdvance()) {
            String assetSetUuid = repaymentOrderItem.getRepaymentBusinessUuid();
            logger.info("OrderItem assetSetUuid is :'" + assetSetUuid + "'");
            if (StringUtils.isEmpty(assetSetUuid)) {
                throw new ApiException("原还款明细的还款计划Uuid为空");
            }
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
            if (assetSet == null) {
                throw new ApiException("原还款明细的还款计划不存在");
            }
            FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, assetSetUuid,
                    FastAssetSet.class, false);
            if (fastAssetSet == null) {
                throw new ApiException("缓存中未找到原还款明细的还款计划，还款计划编号为: " + assetSetUuid);
            }
            repaymentPlanHandler.updateOrderPaymentStatusAndExtraData(fastAssetSet);
        }

    }

    private void handlerItemsInsert(RepaymentOrder repaymentOrder,
                                    String orderItemUuid, List<RepaymentOrderItemModel> repaymentOrderItemModels,
                                    FinancialContract financialContract, LedgerBook ledgerBook, Contract contract) throws GiottoException {
        String orderUuid = repaymentOrder.getOrderUuid();
        RepaymentOrderItem repaymentOrderItemToBeLapsed = repaymentOrderItemService.getRepaymentOrderItemByUuid(orderItemUuid);
        BigDecimal itemChargePrincipal = repaymentOrderItemChargeService.getRepaymentOrderChargeMapByItemUuid(repaymentOrderItemToBeLapsed.getOrderDetailUuid())
                .get(FeeType.PRINCIPAL.getLedgerAccount());
        if (repaymentOrderItemToBeLapsed.getAmount().compareTo(getSumAmount(repaymentOrderItemModels)) != 0) {
            throw new ApiException("还款订单明细总金额与新建还款订单明细金额总和不一致");
        }
        if (itemChargePrincipal.compareTo(getSumPrincipal(repaymentOrderItemModels)) != 0) {
            throw new ApiException("还款订单明细本金与新建还款订单明细本金总和不一致");
        }

        int itemNumOneOrderUuid = repaymentOrderItemService.getRepaymentOrderItemByOrderUuid(orderUuid).size();
        for (RepaymentOrderItemModel repaymentOrderItemModel : repaymentOrderItemModels) {
            RepaymentOrderDetail repaymentOrderDetail = new RepaymentOrderDetail(repaymentOrderItemModel, contract,
                    repaymentOrderItemModel.getTotalAmount(), repaymentOrderItemToBeLapsed.getRepaymentWay());
            RepaymentOrderParameters repaymentOrderParameters = new RepaymentOrderParameters(repaymentOrder,
                    repaymentOrderItemModel, repaymentOrderDetail, contract, repaymentOrderItemToBeLapsed.getMerId(),
                    financialContract, ledgerBook, itemNumOneOrderUuid + repaymentOrderItemModels.size(),
                    repaymentOrderItemToBeLapsed.getRepaymentWay());
            boolean result = repaymentOrderPlacingHandler.checkAndSaveResult(repaymentOrderParameters, false);
            if (!result) {
                throw new ApiException("新增还款明细检测异常");
            }
        }
        logger.info("lapseAndCreateNewItem of orderUuid :" + orderUuid);
        updateRepaymentOrderDetailsNumbers(repaymentOrder);
    }

    //刷新还款订单的明细总数（detailsNumber）
    private void updateRepaymentOrderDetailsNumbers(RepaymentOrder repaymentOrder) {
        int repaymentOrderCountForSuccess = repaymentOrderItemService.getRepaymentOrderCountForSuccess(repaymentOrder.getOrderUuid());
        logger.info("updateRepaymentOrderDetailsNumbers repaymentOrderItem Success numbers :" + repaymentOrderCountForSuccess);
        int repaymentOrderCountForFailure = repaymentOrderItemService.getRepaymentOrderCountForFailure(repaymentOrder.getOrderUuid());
        logger.info("updateRepaymentOrderDetailsNumbers repaymentOrderItem Failure numbers :" + repaymentOrderCountForFailure);
        repaymentOrder.setDetailsNumber(repaymentOrderCountForSuccess + repaymentOrderCountForFailure);

        repaymentOrderService.update(repaymentOrder);
    }

    private BigDecimal getSumAmount(List<RepaymentOrderItemModel> repaymentOrderItemModels) {
        BigDecimal sum = BigDecimal.ZERO;
        for (RepaymentOrderItemModel repaymentOrderItemModel : repaymentOrderItemModels) {
            sum = sum.add(repaymentOrderItemModel.getTotalAmount());
        }
        return sum;
    }

    private BigDecimal getSumPrincipal(List<RepaymentOrderItemModel> repaymentOrderItemModels) {
        BigDecimal sum = BigDecimal.ZERO;
        for (RepaymentOrderItemModel repaymentOrderItemModel : repaymentOrderItemModels) {
            sum = sum.add(repaymentOrderItemModel.getPrincipal());
        }
        return sum;
    }
}