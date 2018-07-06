package com.suidifu.microservice.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.coffer.entity.cmb.UnionPayBankCodeMap;
import com.suidifu.microservice.handler.DeductApplicationDetailHandler;
import com.suidifu.microservice.handler.DeductPlanAndScheduleHandler;
import com.suidifu.microservice.handler.RepaymentOrderReconciliationNoSession;
import com.suidifu.microservice.model.DeductDataContext;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverResult;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductRequestLogService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductMode;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductRequestLog;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.deduct.DeductGatewayMapSpec;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class RepaymentOrderReconciliationNoSessionImpl implements RepaymentOrderReconciliationNoSession {
    @Autowired
    FinancialContractService financialContractService;
    @Autowired
    private DeductApplicationService deductApplicationService;
    @Autowired
    private RepaymentOrderService repaymentOrderService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private DeductPlanAndScheduleHandler deductPlanAndScheduleHandler;
    @Autowired
    private PaymentChannelInformationHandler paymentChannelInformationHandler;
    @Autowired
    private RepaymentOrdeForEasyPayrNotifyServer repaymentOrdeForEasyPayrNotifyServer;
    @Autowired
    private RepaymentOrderItemChargeService repaymentOrderItemChargeService;
    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Autowired
    private DeductApplicationDetailHandler deductApplicationDetailHandler;
    @Autowired
    private DeductApplicationDetailService deductApplicationDetailService;

    @Autowired
    private DeductRequestLogService deductRequestLogService;

    @Value("#{config['notifyserver.groupCacheJobQueueMap_group3']}")
    private String groupName;

    @Value("${urlForJpmorganCallback}")
    private String urlForJpmorganCallback;

    @Value("${urlForDeductCallbackToMorganStanleyAfterDeduct}")
    private String urlForDeductCallbackToMorganStanleyAfterDeduct;

    @Value("${urlForCallbackToDeduct}")
    private String urlForCallbackToDeduct;

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public void repayment_order_for_split_mode_and_deduct(String contractUuid, String orderUuid, String paymentOrderUuid, int priority) {
        logger.info("begin to  build deduct_request_mode repaymentOrderUuid: [" + orderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "]");
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);
        if (null == repaymentOrder || OrderRecoverResult.ALL.equals(repaymentOrder.getOrderRecoverResult()))
            return;
        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        if (paymentOrder == null) {
            logger.info("paymentOrder is null, orderUuid[" + orderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "]");
            return;
        }
        if (paymentOrder.hasPayResult()) {
            logger.info("paymentOrder has pay result, orderUuid[" + orderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "]");
            return;
        }
        Contract contract = contractService.getContract(repaymentOrder.getFirstContractUuid());
        String deductRequestNo = paymentOrder.getDeductRequestNo();

        List<RepaymentDetail> repaymentDetails = getRepaymentDetailForRepaymentOrderItems(repaymentOrder.getOrderUuid());
        String repaymentDetailsJson = JsonUtils.toJsonString(repaymentDetails);
        String gateWay = "";
        if (paymentOrder.getPaymentGateWay() != null) {
            gateWay = paymentOrder.getPaymentGateWay().ordinal() + "";
        }
        List<String> repaymentPlanNoList = repaymentDetails.stream().filter(e -> !StringUtils.isEmpty(e.getRepaymentPlanNo())).map(RepaymentDetail::getRepaymentPlanNo).collect(Collectors.toList());
        DeductRequestModel model = new DeductRequestModel(deductRequestNo, deductRequestNo, repaymentOrder.getFinancialContractNo(), DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT), contract.getUniqueId(),
                contract.getContractNo(), paymentOrder.getAmount().toString(), paymentOrder.getMobileInAppendix(), paymentOrder.getCounterAccountName(), paymentOrder.getCounterAccountNo(), paymentOrder.getBankCode(), paymentOrder.getIdCardNumInAppendix(),
                paymentOrder.getProvinceCode(), paymentOrder.getCityCode(), paymentOrder.getCounterAccountBankName(), gateWay, urlForDeductCallbackToMorganStanleyAfterDeduct, repaymentOrder.getFinancialContractUuid(), repaymentOrder.getRepaymentOrderDetailJson(),
                repaymentOrder.getMerId(), urlForJpmorganCallback, repaymentDetailsJson, urlForCallbackToDeduct);

        int planNotifyNumber = 1;

        DeductApplication deductApplication = new DeductApplication(UUID.randomUUID().toString(), model.getApiCalledTime(),
                model.getRequestNo(), model.getDeductId(), model.getDeductAmount(), model.getMobile(),
                DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP.getOrDefault(model.getGateway(), null), repaymentOrder.getMerId(), repaymentPlanNoList, repaymentOrder.getIp(), model.getFinancialProductCode(), model.getFinancialContractUuid(),
                model.getBatchDeductApplicationUuid(), model.getBatchDeductId(), model.getNotifyUrl(), null, DeductApplicationReceiveStatus.SENDSUCCESS, planNotifyNumber, 1, contract.getUniqueId(), contract.getContractNo(), model.getRepaymentType());

        model.setDeductApplicationUuid(deductApplication.getDeductApplicationUuid());
        model.setCheckResponseNo(deductApplication.getCheckResponseNo());
        deductApplication = deductApplicationService.fillContractInfoAndSourceType(contract.getUuid(), SourceType.REPAYMENTORDER, deductApplication);
        saveDeductDeductRequestLog(model);
        List<DeductApplicationDetail> deductApplicationDetailList = deductApplicationDetailHandler.generateByRepaymentDetailListV(model, deductApplication, repaymentOrder.getFirstContractUuid(), paymentOrder.getFinancialContractUuid());
        saveDeductApplicationDetailList(deductApplicationDetailList);

        paymentOrder.setOutlierDocumentUuid(deductApplication.getDeductApplicationUuid());
        paymentOrderService.saveOrUpdate(paymentOrder);
        List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfoList = getPaymentChannelInfos(repaymentOrder.getFinancialContractUuid(), paymentOrder.getPaymentGateWay(), model.getStandardBankCode(), paymentOrder.getAmount());

        List<Date> executimeInterval = getExecutimeInterval(paymentOrder.getCreateTime(), paymentOrder.getFinancialContractUuid());
        adjust_payment_rule(executimeInterval, paymentChannelAndSignUpInfoList);

        DeductDataContext deductDataContext = new DeductDataContext(model);
        deductDataContext.setFinancialContractUuid(paymentOrder.getFinancialContractUuid());
        deductDataContext.setPaymentChannelAndSignUpInfos(paymentChannelAndSignUpInfoList);
        deductDataContext.setDeductMode(DeductMode.MULTI_CHANNEL_MODE);
        deductDataContext.setExeTimeIntervals(executimeInterval);
        int deductPlanModelListSize = 0;
        logger.info("begin to  build deduct_request_mode repaymentOrderUuid: [" + orderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "],deductApplicationUuid[" + deductApplication.getDeductApplicationUuid() + "]");

        deductPlanAndScheduleHandler.processDeductPlanModelAndTradeSchedule(repaymentOrdeForEasyPayrNotifyServer,
                deductDataContext, groupName, deductPlanModelListSize);

        deductApplication.setTotalCount(deductPlanModelListSize);
        deductApplicationService.update(deductApplication);
        logger.info("end to  build deduct_request_mode repaymentOrderUuid: [" + orderUuid + "],paymentOrderUuid[" + paymentOrderUuid + "],deductApplicationUuid[" + deductApplication.getDeductApplicationUuid() + "]");

    }

    private void saveDeductDeductRequestLog(DeductRequestModel model) {
        DeductRequestLog deductRequestLog = new DeductRequestLog(model, "");
        deductRequestLogService.save(deductRequestLog);
    }

    private void saveDeductApplicationDetailList(List<DeductApplicationDetail> deductApplicationDetailList) {
        for (DeductApplicationDetail deductApplicationDetail : deductApplicationDetailList) {
            deductApplicationDetailService.saveOrUpdate(deductApplicationDetail);
        }
    }

    private List<Date> getExecutimeInterval(Date day, String financialContractUuid) {
        List<Date> time_inteval = financialContractConfigurationService.getDeductTimeInterval(day, financialContractUuid);
        Date now = new Date();
        Set<Date> results = new HashSet<>();
        for (Date t : time_inteval) {
            if (t == null) {
                continue;
            }
            results.add(t.compareTo(now) > 0 ? t : now);
        }
        return new ArrayList<>(results);
    }

    private void adjust_payment_rule(List<Date> executimeInterval,
                                     List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfoList) {

        int maxNum = 0;
        if (CollectionUtils.isEmpty(executimeInterval)) {
            maxNum = paymentChannelAndSignUpInfoList.size();
        } else {
            maxNum = executimeInterval.size();
        }
//		int maxNum = Integer.max(paymentChannelAndSignUpInfoList.size(), executimeInterval.size());
        paymentChannelShuffle(paymentChannelAndSignUpInfoList, maxNum);
        paymentTimeSchedule(executimeInterval, maxNum);

    }

    private void paymentTimeSchedule(List<Date> executimeInterval, int maxNum) {
        int subSize = executimeInterval.size();
        Date now = new Date();
        for (int i = 0; i < maxNum - subSize; i++) {
            executimeInterval.add(0, now);
        }
        executimeInterval.sort(Comparator.naturalOrder());
    }

    private <T> void paymentChannelShuffle(List<T> paymentChannelAndSignUpInfoList, int maxNum) {
        List<T> newArray = new ArrayList<>();
        int subSize = paymentChannelAndSignUpInfoList.size();
        for (int i = 0; i < maxNum; i++) {
            newArray.add(paymentChannelAndSignUpInfoList.get(i % subSize));
        }
        paymentChannelAndSignUpInfoList.clear();
        paymentChannelAndSignUpInfoList.addAll(newArray);
    }

    private List<RepaymentDetail> getRepaymentDetailForRepaymentOrderItems(String orderUuid) {

        List<RepaymentDetail> repaymentDetailList = new ArrayList<RepaymentDetail>();

        List<RepaymentOrderItem> repaymentOrderItems = repaymentOrderItemService.getRepaymentOrderItems(orderUuid);

        if (CollectionUtils.isNotEmpty(repaymentOrderItems)) {
            for (RepaymentOrderItem repaymentOrderItem : repaymentOrderItems) {

                Map<String, BigDecimal> repaymentOrderItemAmountMap = repaymentOrderItemChargeService.getRepaymentOrderChargeMapByItemUuid(repaymentOrderItem.getOrderDetailUuid());
                RepaymentDetail RepaymentDetail = new RepaymentDetail(repaymentOrderItemAmountMap, repaymentOrderItem.getAmount(), repaymentOrderItem.getRepaymentBusinessNo(), repaymentOrderItem.getCurrentPeriod());
                repaymentDetailList.add(RepaymentDetail);
            }
        }

        return repaymentDetailList;
    }

    private List<PaymentChannelAndSignUpInfo> getPaymentChannelInfos(String financialContractUuid, PaymentInstitutionName gateway, String standardBankCode, BigDecimal deductAmount) {


        List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfoList = new ArrayList<>();

        List<PaymentChannelSummaryInfo> paymentChannelInformations = paymentChannelInformationHandler.getPaymentChannelServiceUuidsBy(financialContractUuid, BusinessType.SELF, AccountSide.DEBIT, standardBankCode, BigDecimal.ZERO);

        List<PaymentChannelSummaryInfo> resultPaymentChannelSummaryInfo = new ArrayList<>();

        if (gateway != null) {

            for (PaymentChannelSummaryInfo paymentChannelSummaryInfo : paymentChannelInformations) {
                if (paymentChannelSummaryInfo.getPaymentGateway() == gateway.ordinal()) {

                    resultPaymentChannelSummaryInfo.add(paymentChannelSummaryInfo);
                }
            }
        }

        if (CollectionUtils.isEmpty(resultPaymentChannelSummaryInfo)) {
            resultPaymentChannelSummaryInfo = paymentChannelInformations;
        }

        if (CollectionUtils.isNotEmpty(resultPaymentChannelSummaryInfo)) {

            for (PaymentChannelSummaryInfo paymentChannelSummaryInfo : resultPaymentChannelSummaryInfo) {
                BigDecimal transactionLimitPerTransaction = null == paymentChannelSummaryInfo.getTrasncationLimitPerTransaction() ? deductAmount.divide(new BigDecimal(10000)) : paymentChannelSummaryInfo.getTrasncationLimitPerTransaction();
                paymentChannelSummaryInfo.setTrasncationLimitPerTransaction(transactionLimitPerTransaction);
                PaymentChannelAndSignUpInfo info = new PaymentChannelAndSignUpInfo();
                info.setPaymentChannelSummaryInfo(paymentChannelSummaryInfo);
                //即cpBankCode，组装deductPlan
                info.setStdBankCode(standardBankCode);
                info.setBankCode(UnionPayBankCodeMap.BANK_CODE_MAP.get(standardBankCode));
                paymentChannelAndSignUpInfoList.add(info);
            }
        }

        return paymentChannelAndSignUpInfoList;
    }

}
