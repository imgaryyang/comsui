package com.suidifu.microservice.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastContractKeyEnum;
import com.suidifu.giotto.keyenum.FastCustomerKeyEnum;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.model.FastContract;
import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.microservice.handler.RepaymentBusinessCheckHandler;
import com.suidifu.microservice.handler.RepaymentOrderHandler;
import com.suidifu.owlman.microservice.model.RepaymentOrderCheck;
import com.suidifu.owlman.microservice.model.RepaymentOrderParameters;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderCheckException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetailInfoModel;
import com.zufangbao.sun.entity.repayment.order.DetailAliveStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItemCharge;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItemCheckFailLog;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.repayment.FeeType;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import com.zufangbao.sun.yunxin.handler.RepaymentOrderItemChargeHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemCheckFailLogService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component("repaymentOrderHandler")
public class RepaymentOrderHandlerImpl implements RepaymentOrderHandler {
    @Resource
    private RepaymentOrderItemChargeHandler repaymentOrderItemChargeHandler;
    @Resource
    private FastHandler fastHandler;
    @Resource
    private RepaymentOrderItemCheckFailLogService repaymentOrderItemCheckFailLogService;
    @Resource
    private RepaymentOrderItemChargeService repaymentOrderItemChargeService;
    @Resource
    private RepaymentPlanHandler repaymentPlanHandler;
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private LedgerItemService ledgerItemService;

    @Autowired
    private RepaymentOrderService repaymentOrderService;
    @Autowired
    private RepaymentOrderItemService repaymentOrderItemService;

    @Override
    public RepaymentOrderCheck check(RepaymentOrderParameters repaymentOrderParameters) {
        //1. 校验合同编号和unique有效
        //2. 还款方式互斥;仅开发类型3，4；类型4如果包含多个贷款合同，贷款合同指向同一个贷款人
        //3. 订单生成时间-设定还款时间<=7
        //4  业务编号与还款方式匹配（回购与还款计划）
        //5. 业务编号不处于不允许提交状态：还款计划不为“扣款中”、“作废”、“还款成功”、“已回购”、“违约”；回购单不为“已回购”、“违约”
        //6. 明细总金额与单据明细总金额一致
        //7. 明细业务编号类型与费用类型匹配(还款计划（计划本金、计划利息、贷款服务费、技术维护费、其他费用、逾期罚息、逾期违约金、逾期服务费、逾期其他费用).回购单（回购本金、回购利息、回购罚息、回购其他费用）)
        //8. 业务明细金额更新值是否小于等于当前应收未收-支付中金额
        String financialContractUuid = repaymentOrderParameters.getFinancialContractUuid();
        RepaymentWay repaymentWayOfFirstDetail = repaymentOrderParameters.getRepaymentWay();
        String customerSource = repaymentOrderParameters.getCustomerSource();
        Date orderCreateTime = repaymentOrderParameters.getOrderCreateTime();
        int repaymentCheckDays = repaymentOrderParameters.getRepaymentCheckDays();
        RepaymentOrderDetail repaymentOrderDetail = repaymentOrderParameters.getRepaymentOrderDetail();
        int repaymentOrderDetailSize = repaymentOrderParameters.getRepaymentOrderDetailSize();
        LedgerBook ledgerBook = repaymentOrderParameters.getLedgerBook();

        //校验合同和信托合同是否有效
        String contractUniqueId = repaymentOrderDetail.getContractUniqueId();
        String contractNo = repaymentOrderDetail.getContractNo();

        String contractNoOfFirstDetail = repaymentOrderParameters.getContractNo();
        String contractUniqueIdOfFirstDetail = repaymentOrderParameters.getContractUniqueId();

        RepaymentOrderCheck checkModel = new RepaymentOrderCheck();
        checkModel.setMerId(repaymentOrderParameters.getMerId());
        checkModel.setFinancialContractUuid(financialContractUuid);
        checkModel.setFinancialContractNo(repaymentOrderParameters.getFinancialContractNo());
        try {

            RepaymentWay repaymentWayOfCurrentDetail = repaymentOrderDetail.getRepaymentWayEnum();
            checkValidContract(contractUniqueId, contractNo, financialContractUuid, checkModel, contractNoOfFirstDetail, contractUniqueIdOfFirstDetail, repaymentWayOfCurrentDetail);
            checkRepaymentWayAndCustomerSource(repaymentWayOfFirstDetail, repaymentWayOfCurrentDetail,
                    customerSource, checkModel.getCustomerUuid());

            checkPlanDate(orderCreateTime, repaymentCheckDays, repaymentOrderDetail);

            checkModel.setRepaymentBusinessType(repaymentWayOfCurrentDetail.getRepaymentBusinessType());
            RepaymentBusinessCheckHandler repaymentBusinessCheckHandler = RepaymentBusinessCheckHandler.repaymentBusinessCheckFactory(repaymentWayOfCurrentDetail.getRepaymentBusinessType().getKey());
            // 校验业务编号
            repaymentBusinessCheckHandler.checkRepaymentNo(repaymentOrderDetail, checkModel);
            checkAmount(repaymentOrderDetail, repaymentBusinessCheckHandler, checkModel);

            checkReceivableInAdvance(repaymentOrderDetail, checkModel, repaymentOrderDetailSize, ledgerBook);
        } catch (RepaymentOrderCheckException roce) {
            checkModel.fail(roce.getMessage());
            log.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.FAIL_CHECK
                    + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                    + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid()
                    + ",with full stack trace [" + ExceptionUtils.getFullStackTrace(roce) + "]");
        } catch (Exception e) {
            checkModel.fail("系统错误");
            log.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.FAIL_CHECK
                    + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                    + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid()
                    + ",with full stack trace [" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
        return checkModel;
    }

    private void checkReceivableInAdvance(RepaymentOrderDetail detail,
                                          RepaymentOrderCheck checkModel,
                                          int repaymentOrderDetailSize,
                                          LedgerBook ledgerBook) throws RepaymentOrderCheckException {
        if (!detail.isReceivableInAdvance()) {
            return;
        }
        if (repaymentOrderDetailSize != 1) {
            throw new RepaymentOrderCheckException("进预收的还款订单应只有一个明细");
        }
        RepaymentWay repaymentWay = detail.getRepaymentWayEnum();
        if (repaymentWay != RepaymentWay.ON_LINE_DEDUCT && repaymentWay != RepaymentWay.MERCHANT_DEDUCT_EASY_PAY) {
            throw new RepaymentOrderCheckException("进预收的还款订单的支付方式应为线上代扣或者快捷支付");
        }
        if (detail.isContractOnlyMode()) {
            checkAmountForContractOnlyMode(detail, ledgerBook, checkModel.getFastContractUuid());
        }
    }

    private void checkAmountForContractOnlyMode(RepaymentOrderDetail detail, LedgerBook ledgerBook, String contractUuid) throws RepaymentOrderCheckException {
        //明细本金+预收本金<=合同应收本金
        Map<String, BigDecimal> unrecoveredMap = ledgerBookStatHandler.unrecovered_contract_snapshot(ledgerBook.getLedgerBookNo(), contractUuid, true);
        BigDecimal unrecoveredPrinciple = unrecoveredMap.getOrDefault(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, BigDecimal.ZERO);
        BigDecimal detailPrinciple = BigDecimal.ZERO;
        List<RepaymentOrderDetailInfoModel> models = detail.getFormatRepaymentOrderDetailInfoList();
        for (RepaymentOrderDetailInfoModel model : models) {
            FeeType feeType = model.getFeeTypeEnum();
            if (feeType == FeeType.PRINCIPAL) {
                detailPrinciple = detailPrinciple.add(model.getActualAmount());
            }
        }
        BigDecimal receivableInAdvancePrinciple = ledgerItemService.getBalancedAmount(ledgerBook.getLedgerBookNo(), ExtraChargeSpec.RIA_LOAN_ASSET_PRINCIPAL_KEY, "", "", "", contractUuid, "", "", "");
        receivableInAdvancePrinciple = receivableInAdvancePrinciple.negate();
        BigDecimal isAboutToReceivedInAdvancePrincipal = repaymentOrderItemChargeService.getIsAboutToReceivedInAdvancePrincipal(contractUuid);

        if (AmountUtils.addAll(detailPrinciple, receivableInAdvancePrinciple, isAboutToReceivedInAdvancePrincipal).compareTo(unrecoveredPrinciple) > 0) {
            throw new RepaymentOrderCheckException("进预收时，还款订单明细的本金加上已进预收的本金应小于等于合同的应收本金");
        }
    }

    @Override
    public void saveAfterCheck(RepaymentOrderParameters repaymentOrderParameters,
                               RepaymentOrderCheck checkModel,
                               boolean isSaveItemCheckFail) throws GiottoException {
        boolean check = checkModel.isValid();
        FastContract fastContract = checkModel.getFastContract();
        String repaymentBusinessUuid = checkModel.getRepaymentBusinessUuid();
        String errorMsg = checkModel.getErrorMsg();
        if (check) {
            saveDetailAndUpdateOrderAmountCountWhenSuc(repaymentOrderParameters, fastContract,
                    repaymentBusinessUuid);
            return;
        }
        if (!isSaveItemCheckFail) {
            throw new ApiException("还款明细检测异常," + errorMsg);
        }
        //校验失败后保存RepaymentOrderItemCheckFailLog
        saveWhenCheckFail(repaymentOrderParameters, fastContract, errorMsg);
    }

    private void saveWhenCheckFail(RepaymentOrderParameters repaymentOrderParameters, FastContract fastContract,
                                   String errorMsg) {
        log.info(GloableLogSpec.AuditLogHeaderSpec()
                + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.START_SAVE_DETAIL_CHECK_FAIL_LOG
                + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid());

        List<RepaymentOrderItemCheckFailLog> logsInDb = repaymentOrderItemCheckFailLogService
                .get(repaymentOrderParameters.getRepaymentOrderDetailUuid());
        if (CollectionUtils.isNotEmpty(logsInDb)) {
            log.info(GloableLogSpec.AuditLogHeaderSpec()
                    + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.ALREADY_IN_DB_DETAIL_CHECK_FAIL_LOG
                    + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                    + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid());
            return;
        }

        RepaymentOrderItemCheckFailLog checkFailLog = repaymentOrderParameters
                .toRepaymentOrderItemCheckFailLog(errorMsg, fastContract);
        repaymentOrderItemCheckFailLogService.save(checkFailLog);

        log.info(GloableLogSpec.AuditLogHeaderSpec()
                + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.END_SAVE_DETAIL_CHECK_FAIL_LOG
                + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid());
    }

    private void saveDetailAndUpdateOrderAmountCountWhenSuc(RepaymentOrderParameters repaymentOrderParameters,
                                                            FastContract fastContract, String repaymentBusinessUuid) throws GiottoException {
        log.info(GloableLogSpec.AuditLogHeaderSpec()
                + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.START_SAVE_DETAIL_AND_UPDATE_ORDER_AMOUNT
                + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid());

        FastRepaymentOrderItem itemInCache = fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
                repaymentOrderParameters.getRepaymentOrderDetailUuid(), FastRepaymentOrderItem.class, true);
        if (null != itemInCache) {
            log.info(GloableLogSpec.AuditLogHeaderSpec()
                    + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.ALREADY_IN_DB_DETAIL + "RepaymentOrderUuid:["
                    + repaymentOrderParameters.getRepaymentOrderUuid() + "].RepaymentOrderDetailUuid:["
                    + repaymentOrderParameters.getRepaymentOrderDetailUuid());
            return;
        }
        FastRepaymentOrderItem fastRepaymentOrderItem = repaymentOrderParameters.toFastRepaymentOrderItem(fastContract,
                repaymentBusinessUuid);
        fastHandler.add(fastRepaymentOrderItem, false);

        RepaymentOrderDetail repaymentOrderDetail = repaymentOrderParameters.getRepaymentOrderDetail();
        List<RepaymentOrderDetailInfoModel> detailInfoModelList = repaymentOrderDetail
                .getFormatRepaymentOrderDetailInfoList();
        for (RepaymentOrderDetailInfoModel repaymentOrderDetailInfoModel : detailInfoModelList) {
            FeeType feeType = repaymentOrderDetailInfoModel.getFeeTypeEnum();
            BigDecimal amount = repaymentOrderDetailInfoModel.getActualAmount();
            if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                RepaymentOrderItemCharge detailInfo = new RepaymentOrderItemCharge(
                        fastRepaymentOrderItem.getOrderDetailUuid(), fastRepaymentOrderItem.getRepaymentBusinessUuid(),
                        fastRepaymentOrderItem.getRepaymentBusinessNo(), fastRepaymentOrderItem.getContractUuid(),
                        amount, feeType.getLedgerAccount(), fastRepaymentOrderItem.getOrderUuid());
                repaymentOrderItemChargeService.save(detailInfo);
            }

        }

        updateOrderAmountAndCount(repaymentOrderParameters.getRepaymentOrderDetail());

        log.info(GloableLogSpec.AuditLogHeaderSpec()
                + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.END_SAVE_DETAIL_AND_UPDATE_ORDER_AMOUNT
                + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid());
    }

    private void updateOrderAmountAndCount(RepaymentOrderDetail repaymentOrderDetail) throws GiottoException {
        String assetSetNo = repaymentOrderDetail.getRepaymentBusinessNo();

        if (repaymentOrderDetail.getRepaymentWayEnum() == null
                || repaymentOrderDetail.getRepaymentWayEnum() == RepaymentWay.REPURCHASE) {
            return;
        }

        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO, assetSetNo,
                FastAssetSet.class, true);

        if (fastAssetSet != null) {
            repaymentPlanHandler.updateOrderPaymentStatusAndExtraData(fastAssetSet);
        }
    }

    private void checkAmount(RepaymentOrderDetail repaymentOrderDetail,
                             RepaymentBusinessCheckHandler repaymentBusinessCheckHandler, RepaymentOrderCheck checkModel)
            throws RepaymentOrderCheckException, GiottoException {
        BigDecimal currentTotalAmount = repaymentOrderDetail.getFormatDetailsTotalAmount();
        if (currentTotalAmount == null) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_ORDER_DETAIL_AMOUNT_MSG);
        }

        repaymentOrderDetail.check_fee_type_and_amount(checkModel.getRepaymentBusinessType(),
                checkModel.isAllowDetailEmpty());

        boolean isNotNeedCheckDetail = CollectionUtils.isEmpty(repaymentOrderDetail.getFormatRepaymentOrderDetailInfoList());
        BigDecimal totalDetailAmount = repaymentOrderDetail.getTotalFeeDetailAmount();
        Map<String, BigDecimal> requestDetailAmount = repaymentOrderDetail.getDetailAmountMap();
        if (!isNotNeedCheckDetail) {
            //业务明细金额和
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_ORDER_DETAIL_AMOUNT_AND_FEE_AMOUNT_MSG, "明细总金额[" + currentTotalAmount + "],明细金额累加[" + totalDetailAmount + "]");
        }
        if (repaymentOrderDetail.isReceivableInAdvance()) {
            //进预收 随后进行预收校验
            return;
        }

        String repaymentBusinessUuid = checkModel.getRepaymentBusinessUuid();
        List<FastRepaymentOrderItem> itemsInCache = fastHandler.getByKeyList(FastRepaymentOrderItemKeyEnum.REPAYMENT_BUSINESS_UUID,
                repaymentBusinessUuid, FastRepaymentOrderItem.class, true);
        //去除作废的 item
        List<FastRepaymentOrderItem> invalidItems = new ArrayList<>();
        for (FastRepaymentOrderItem fastRepaymentOrderItem : itemsInCache) {
            if (fastRepaymentOrderItem.getDetailAliveStatus() == DetailAliveStatus.INVALID.ordinal()) {
                invalidItems.add(fastRepaymentOrderItem);
            }
        }

        itemsInCache.removeAll(invalidItems);
        BigDecimal repaymentBusinessAmount = repaymentBusinessCheckHandler.getTotalRepaymentBusinessAmount(checkModel);
        Map<String, BigDecimal> totalCharges = repaymentBusinessCheckHandler.getTotalCharges(checkModel);

        checkTotalAmount(itemsInCache, currentTotalAmount, repaymentBusinessAmount);
        checkDetailAmount(totalCharges, requestDetailAmount, repaymentBusinessUuid);
    }

    private void checkTotalAmount(List<FastRepaymentOrderItem> itemsInCache, BigDecimal currentAmount,
                                  BigDecimal totalReceivableAmount) throws RepaymentOrderCheckException {
        BigDecimal preTotalAmount = BigDecimal.ZERO;
        List<String> orderUuids = new ArrayList<>();
        for (FastRepaymentOrderItem item : itemsInCache) {
            preTotalAmount = preTotalAmount.add(item.getAmount());
            if (!orderUuids.contains(item.getOrderUuid())) {
                orderUuids.add(item.getOrderUuid());
            }
        }

        if (preTotalAmount.add(currentAmount).compareTo(totalReceivableAmount) > 0) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_ORDER_TOTAL_AMOUNT_CHECK_MSG, "已下订单金额[" + preTotalAmount + "]", "当前订单金额[" + currentAmount + "]", "总应还金额[" + totalReceivableAmount + "]", "已下单订到: " + JsonUtils.toJsonString(orderUuids));
        }
    }

    private void checkDetailAmount(Map<String, BigDecimal> totalCharges, Map<String, BigDecimal> currentDetailAmount,
                                   String repaymentBusinessUuid) throws RepaymentOrderCheckException {

        Map<String, BigDecimal> repaymentOrderItemChargeInDb = repaymentOrderItemChargeHandler
                .getRepaymentOrderCharge(repaymentBusinessUuid);
        Set<String> keySet = new HashSet<>();
        keySet.addAll(repaymentOrderItemChargeInDb.keySet());
        keySet.addAll(currentDetailAmount.keySet());

        for (String charge_key : keySet) {
            BigDecimal dbAmount = repaymentOrderItemChargeInDb.getOrDefault(charge_key, BigDecimal.ZERO);
            BigDecimal current_detail_amount = currentDetailAmount.getOrDefault(charge_key, BigDecimal.ZERO);
            BigDecimal totalAmount = totalCharges.getOrDefault(charge_key, BigDecimal.ZERO);
            if (dbAmount.add(current_detail_amount).compareTo(totalAmount) > 0) {
                throw new RepaymentOrderCheckException(
                        GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_ORDER_DETAIL_AMOUNT_CHECK_MSG, "明细[" + ExtraChargeSpec.chiness_name.get(charge_key) + "]", "已下订单金额[" + dbAmount + "]", "此次还款金额[" + current_detail_amount + "]", "总还款金额[" + totalAmount + "]");
            }
        }
    }

    private void checkPlanDate(Date orderCreateTime, int repaymentCheckDays, RepaymentOrderDetail repaymentOrderDetail)
            throws RepaymentOrderCheckException {
        Date parsedPlannedDate = repaymentOrderDetail.getParsedPlannedDate();
        if (StringUtils.isNotEmpty(repaymentOrderDetail.getPlannedDate()) && parsedPlannedDate == null && !StringUtils.equalsIngoreNull(repaymentOrderDetail
                .getPlannedDate(), DateUtils.DATE_FORMAT_0001_01_01)) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_PLAN_DATE_CHECK_MSG);
        }
        if (orderCreateTime == null) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_CREATE_TIME_MSG);
        }
        if (parsedPlannedDate != null && repaymentCheckDays >= 0) {
            int distanceDays = DateUtils.compareTwoDatesOnDay(orderCreateTime, parsedPlannedDate);
            if (distanceDays > repaymentCheckDays) {
                throw new RepaymentOrderCheckException(
                        GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_PLAN_DATE_CHECK_MSG);
            }
        }
    }

    private void checkRepaymentWayAndCustomerSource(RepaymentWay expectedRepaymentWay,
                                                    RepaymentWay repaymentWayOfDetail, String customerSource, String customerUuid)
            throws RepaymentOrderCheckException, GiottoException {

        if (expectedRepaymentWay == null || repaymentWayOfDetail == null) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_WAY_CODE_MSG);
        }

        if (!RepaymentWay.isInSameGroup(expectedRepaymentWay, repaymentWayOfDetail)) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_WAY_GROUP_MSG);
        }
        if (repaymentWayOfDetail.getRepaymentWayGroupType() != RepaymentWayGroupType.OWNER_OFFLINE_REPAYMENT_ORDER_TYPE
                && repaymentWayOfDetail
                .getRepaymentWayGroupType() != RepaymentWayGroupType.ALTER_OFFLINE_REPAYMENT_ORDER_TYPE
                && repaymentWayOfDetail.getRepaymentWayGroupType() != RepaymentWayGroupType.ONLINE_DEDUCT_REPAYMENT_ORDER_EASY_PAY_TYPE
                && repaymentWayOfDetail.getRepaymentWayGroupType() != RepaymentWayGroupType.ONLINE_DEDUCT_REPAYMENT_ORDER_TYPE
                && repaymentWayOfDetail.getRepaymentWayGroupType() != RepaymentWayGroupType.ONLINE_DEDUCT_REPAYMENT_ORDER_ONLINE_PAYMENT_TYPE
                && repaymentWayOfDetail.getRepaymentWayGroupType() != RepaymentWayGroupType.BUSINESS_DEDUCT_REPAYMENT_ORDER_TYPE
                ) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_WAY_MSG);
        }
        if (repaymentWayOfDetail == RepaymentWay.MERCHANT_GUARANTEE) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_WAY_MSG);
        }
        // 在repaymentOrder时校验：百仟项目目前只开发还款类型3（委托转付、商户代偿、差额划拨、回购）4（主动付款、他人代偿）
        // 主动付款、他人代偿：如果包含多个贷款合同，贷款合同指向同一个贷款人
        if (expectedRepaymentWay.isOfflineRepayment()) {
            FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, customerUuid,
                    FastCustomer.class, true);

            if (fastCustomer == null || StringUtils.isEmpty(fastCustomer.getSource())
                    || !StringUtils.equalsIngoreNull(customerSource, fastCustomer.getSource())) {
                throw new RepaymentOrderCheckException(
                        GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_CUSTOMER_SOURCE_IN_MULTI_CONTRACT);
            }
        }

    }

    private void checkValidContract(String contractUniqueId, String contractNo, String financialContractUuid,
                                    RepaymentOrderCheck checkModel, String contractNoOfFirstDetail,
                                    String contractUniqueIdOfFirstDetail, RepaymentWay repaymentWayOfCurrentDetail)
            throws RepaymentOrderCheckException, GiottoException {
        FastContract fastContract;

        //校验还款订单是否包含多个贷款合同
        if (repaymentWayOfCurrentDetail != null && repaymentWayOfCurrentDetail.getRepaymentWayGroupType().singleContractForRepaymentWayGroupType()) {
            checkIsSameContract(contractNoOfFirstDetail, contractNo);

            checkIsSameContract(contractUniqueIdOfFirstDetail, contractUniqueId);
        }

        if (StringUtils.isEmpty(contractUniqueId) && StringUtils.isEmpty(contractNo)) {
            throw new RepaymentOrderCheckException(GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_CONTRACT_MSG, "contractUniqueId[" + contractUniqueId + "],contractNo[" + contractNo + "]");
        }
        if (!StringUtils.isEmpty(contractUniqueId)) {
            fastContract = fastHandler.getByKey(FastContractKeyEnum.UNIQUE_ID, contractUniqueId, FastContract.class, true);
        } else {
            fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, contractNo, FastContract.class, true);
        }
        if (fastContract == null) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_CONTRACT_MSG, "找不到合同");
        }
        if (!StringUtils.equalsIngoreNull(fastContract.getUniqueId(), contractUniqueId) &&
                !StringUtils.equalsIngoreNull(fastContract.getContractNo(), contractNo)) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_CONTRACT_MSG, "找不到合同");
        }
        if (!StringUtils.equalsIngoreNull(fastContract.getFinancialContractUuid(), financialContractUuid)) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_FINANCIAL_CONTRACT_MSG);
        }
        checkModel.setFastContract(fastContract);
    }

    private void checkIsSameContract(String firstDetailValue, String orderDetailValue)
            throws RepaymentOrderCheckException {
        if (!StringUtils.isEmpty(firstDetailValue) && !StringUtils.isEmpty(orderDetailValue)) {
            if (!firstDetailValue.equals(orderDetailValue)) {
                throw new RepaymentOrderCheckException(
                        GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_NOT_SAME_CONTRACT_MSG, "[" + firstDetailValue + "],[" + orderDetailValue + "]");
            }
        }
    }

    @Override
    public void lapseOrderItemRollBackRepaymentPlan(String repaymentOrderUuid) throws GiottoException {
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

        List<String> repaymentItemUuids = repaymentOrderItemService.getRepaymentOrderItemByOrderUuid(repaymentOrder.getOrderUuid());

        //items 状态作废
        log.info("lapse_order_item_roll_back_repaymentPlan update item invalid  begin, orderUuid[" + repaymentOrderUuid + "]." + "itemSize:" + repaymentItemUuids.size());
        repaymentOrderItemService.updateStatusRepaymentOrderItemByUuids(repaymentItemUuids, DetailAliveStatus.INVALID);
        log.info("lapse_order_item_roll_back_repaymentPlan update item invalid  end, orderUuid[" + repaymentOrderUuid + "].");

        //刷新缓存 items
        fastHandler.getByKeyList(FastRepaymentOrderItemKeyEnum.ORDER_UUID, repaymentOrderUuid, FastRepaymentOrderItem.class, false);

        for (String repaymentItemUuid : repaymentItemUuids) {

            RepaymentOrderItem item = repaymentOrderItemService.getRepaymentOrderItemByUuid(repaymentItemUuid);

            FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO, item.getRepaymentBusinessNo(),
                    FastAssetSet.class, false);

            if (fastAssetSet != null) {
                log.info("lapse_order_item_roll_back_repaymentPlan update orderPaymentStatus and extraData, orderUuid[" + repaymentOrderUuid + "]." +
                        "repaymentItemUuid:" + repaymentItemUuid + ",fastAssetSetUuid" + fastAssetSet.getAssetUuid());

                repaymentPlanHandler.updateOrderPaymentStatusAndExtraData(fastAssetSet);
            }
        }
    }
}