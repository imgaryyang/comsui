package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.handler.RepaymentPlanRecoverHandler;
import com.suidifu.microservice.handler.SourceDocumentHandler;
import com.suidifu.microservice.model.ReconciliationRepayment;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace;
import com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentForRecover;
import com.suidifu.owlman.microservice.spec.ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams;
import com.zufangbao.sun.api.model.deduct.RecordStatus;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.SimpleOrderItemInfo;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

/**
 * @author louguanyang at 2018/3/14 11:25
 * @mail louguanyang@hzsuidifu.com
 */
@Component("repaymentPlanRecoverHandler")
public class RepaymentPlanRecoverHandlerImpl implements RepaymentPlanRecoverHandler {

    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;
    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private LedgerBookService ledgerBookService;
    @Autowired
    private RepaymentOrderItemChargeService repaymentOrderItemChargeService;
    @Autowired
    private SourceDocumentService sourceDocumentService;
    @Autowired
    private SourceDocumentHandler sourceDocumentHandler;
    @Autowired
    private RepaymentOrderService repaymentOrderService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private DeductApplicationService deductApplicationService;
    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    private static final Log logger = LogFactory.getLog(RepaymentPlanRecoverHandlerImpl.class);
    @Override
    public void recover_received_in_advance(List<AssetSet> assetSets, String financialContractUuid){
        try {
            boolean isReceivedInAdvance = financialContractConfigurationService.isFinancialContractConfigCodeConfiged(financialContractUuid, FinancialContractConfigurationCode.IS_REPAYMENT_ORDER_RECEIVED_IN_ADVANCE.getCode());
            if(!isReceivedInAdvance){
                return;
            }
            logger.info("begin to recover_received_in_advance. assetSets size["+assetSets.size()+"],financialContractUuid["+financialContractUuid+"].");
            for(AssetSet assetSet:assetSets){
                try {
                    logger.info("begin to recover_received_in_advance. assetSetUuid ["+(assetSet==null?null:assetSet.getAssetUuid())+"],financialContractUuid["+financialContractUuid+"].");
                    recover_received_in_advance(assetSet);
                    logger.info("end to recover_received_in_advance. assetSetUuid ["+(assetSet==null?null:assetSet.getAssetUuid())+"],financialContractUuid["+financialContractUuid+"].");

                } catch(Exception e){
                    logger.error("end to recover_received_in_advance. assetSetUuid ["+(assetSet==null?null:assetSet.getAssetUuid())+"],financialContractUuid["+financialContractUuid+"].");
                    e.printStackTrace();
                }
            }
            logger.info("end to recover_received_in_advance. assetSets size["+assetSets.size()+"],financialContractUuid["+financialContractUuid+"].");

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void recover_received_in_advance(AssetSet assetSet) throws GiottoException {
        //1. 判断信托开关
        //2. 还款计划的金额必大于等于预收的金额，且明细校验

        String contractUuid = assetSet.getContractUuid();
        int currentPeriod = assetSet.getCurrentPeriod();
        List<SimpleOrderItemInfo> items = repaymentOrderItemService.get_received_in_advance_items(contractUuid, currentPeriod);
        if(CollectionUtils.isEmpty(items)){
            logger.info("items of recover_received_in_advance is empty. assetSetUuid["+assetSet.getAssetUuid()+"].");
            return;
        }
        List<String> itemUuids = items.stream().map(SimpleOrderItemInfo::getRepaymentOrderItemUuid).collect(Collectors.toList());
        FinancialContract financialContract = financialContractService.getFinancialContractBy(assetSet.getFinancialContractUuid());
        Map<String,BigDecimal> advanceDetailAmount = ledgerBookStatHandler.receivable_in_advance_snapshot(financialContract.getLedgerBookNo(),
            contractUuid, currentPeriod);
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
        Map<String, BigDecimal> restReceivableDetailAmount = ledgerBookStatHandler.get_unrecovered_asset_snapshot(ledgerBook, assetSet.getAssetUuid());
        Map<String,BigDecimal> orderDetailAmount = repaymentOrderItemChargeService.getRepaymentOrderCharge(itemUuids);
        logger.info("recover_received_in_advance check amount restReceivableDetailAmount["+restReceivableDetailAmount+"], assetSetUuid["+assetSet.getAssetUuid()+"].");

        //checkAmount
        for(String extraChargeKey: ExtraChargeSpec.keySet){
            BigDecimal advanceAmount = advanceDetailAmount.getOrDefault(ExtraChargeSpec.received_in_advance_extra_spec_key.get(extraChargeKey),BigDecimal.ZERO);
            BigDecimal restReceivableAmount = restReceivableDetailAmount.getOrDefault(extraChargeKey,BigDecimal.ZERO);
            BigDecimal orderAmount = orderDetailAmount.getOrDefault(extraChargeKey,BigDecimal.ZERO);
            if(advanceAmount.compareTo(orderAmount)!=0 ||advanceAmount.compareTo(restReceivableAmount)>0){
                logger.error("recover_received_in_advance check amount error, assetSetUuid["+assetSet.getAssetUuid()+"].advanceAmount["+advanceAmount+"],orderAmount["+orderAmount+"],restReceivableAmount["+restReceivableAmount+"],extraChargeKey["+extraChargeKey+"]");
                return;
            }
        }

        LinkedMultiValueMap<String,String> orderUuidItems = groupByItemUuid(items);
        for (String orderUuid :orderUuidItems.keySet()){
            logger.info("recover_asset_each_repayment_order begin, orderUuid["+orderUuid+"],assetSetUuid["+assetSet.getAssetUuid()+"].");
            recoverAssetEachRepaymentOrder(assetSet, orderUuid, orderUuidItems.get(orderUuid),financialContract);
            logger.info("recover_asset_each_repayment_order end, orderUuid["+orderUuid+"],assetSetUuid["+assetSet.getAssetUuid()+"].");

        }

    }

    private void recoverAssetEachRepaymentOrder(AssetSet assetSet, String orderUuid, List<String> itemUuids, FinancialContract financialContract) throws GiottoException {

        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);

        PaymentOrder paymentOrder = paymentOrderService.getOneSucPaymentOrder(orderUuid);
        DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid());
        if(deductApplication==null || !deductApplication.isPartSucOrSus()){
            logger.info("recover_assets_repayment_order_deduct start, deductApplication is null or not success, orderUuid["+orderUuid+"].");
            return;
        }
        SourceDocument sourceDocument = sourceDocumentService.getUnWriteOffSourceDocumentByDeductInformation(paymentOrder.getUuid());
        if(sourceDocument==null){
            sourceDocument = sourceDocumentHandler.createUnSignedDeductRepaymentOrderSourceDocument(assetSet.getCustomerUuid(), financialContract, repaymentOrder, paymentOrder);
        }
        logger.info("recover_assets_repayment_order_deduct begin, orderUuid["+orderUuid+"],deductApplicationUuid["+deductApplication.getDeductApplicationUuid()+"],sourceDocumentUuid["+sourceDocument.getSourceDocumentUuid()+"],itemUuids["+itemUuids+"].");

        for(String itemUuid: itemUuids){
            Map<String,Object> params = new HashMap<String,Object>();
            params.put(ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_ITEM_UUID, itemUuid);
            params.put(ReconciliationRepaymentParams.PARAMS_REPAYMENT_ORDER_UUID, repaymentOrder.getOrderUuid());
            params.put(ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentForRecover.PARAMS_ASSET_SET_UUID,assetSet.getAssetUuid());
            params.put(ReconciliationRepaymentParams.PARAMS_REPAYMENT_SOURCE_DOCUMENT,sourceDocument);
            params.put(ReconciliationRepaymentForRecover.PARAMS_DEDUCT_APPLICATION,deductApplication);
            ReconciliationRepayment reconRepayment = ReconciliationRepayment.recover_reconciliation();
            reconRepayment.accountReconciliation(params);
        }
        sourceDocument.updateSourceDocumetStatus(SourceDocumentStatus.SIGNED);
        sourceDocumentService.saveOrUpdate(sourceDocument);
        deductApplication.setRecordStatus(RecordStatus.WRITE_OFF);
        deductApplicationService.saveOrUpdate(deductApplication);
        repaymentOrder.updateRecoverStatusSuc();
        repaymentOrderService.saveOrUpdate(repaymentOrder);
        logger.info("recover_assets_repayment_order_deduct end, orderUuid["+orderUuid+"],deductApplicationUuid["+deductApplication.getDeductApplicationUuid()+"],sourceDocumentUuid["+sourceDocument.getSourceDocumentUuid()+"],itemUuids["+itemUuids+"].");
    }

    private LinkedMultiValueMap<String,String> groupByItemUuid(List<SimpleOrderItemInfo> itemInfos) {
        LinkedMultiValueMap<String,String> orderUuidItems = new LinkedMultiValueMap<String,String>();
        for (SimpleOrderItemInfo itemInfo:itemInfos){
            orderUuidItems.add(itemInfo.getOrderUuid(),itemInfo.getRepaymentOrderItemUuid());
        }
        return orderUuidItems;
    }

}
