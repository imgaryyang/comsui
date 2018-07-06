package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastContractKeyEnum;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.model.FastContract;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.microservice.handler.RepaymentOrderHandler;
import com.suidifu.owlman.microservice.handler.RepaymentOrderPlacingHandler;
import com.suidifu.owlman.microservice.model.RepaymentOrderCheck;
import com.suidifu.owlman.microservice.model.RepaymentOrderParameters;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderCheckException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.OrderCheckStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import com.zufangbao.sun.yunxin.exception.AssetSetOrderPaymentStatusLockFailedException;
import com.zufangbao.sun.yunxin.handler.RepaymentOrderItemHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("dstJobRepaymentOrderPlacing")
public class RepaymentOrderPlacingHandlerImpl implements RepaymentOrderPlacingHandler {
    @Resource
    private RepaymentOrderHandler repaymentOrderHandler;

    @Resource
    private RepaymentPlanService repaymentPlanService;

    @Resource
    private RepaymentOrderItemHandler repaymentOrderItemHandler;

    @Resource
    private FastHandler fastHandler;

    @Resource
    private RepaymentOrderService repaymentOrderService;

    @Resource
    private ContractService contractService;

    @Resource
    private CustomerService customerService;

    @Resource
    private LedgerBookService ledgerBookService;

    @Resource
    private FinancialContractService financialContractService;

    /**
     * 1. 准备数据 返回一个map<detailuuid,FinancialContractUuid>
     *
     * @param repaymentOrderParametersList
     * @return
     * @throws GiottoException
     */
    @Override
    public Map<String, String> criticalMarker(List<RepaymentOrderParameters> repaymentOrderParametersList) throws GiottoException {
        Map<String, String> criticalMarker = new HashMap<>();
        for (RepaymentOrderParameters parameters : repaymentOrderParametersList) {
            RepaymentOrderDetail repaymentOrderDetail = parameters.getRepaymentOrderDetail();
            String marker = getCriticalMarker(repaymentOrderDetail == null ? "" : repaymentOrderDetail.getContractUniqueId(),
                    repaymentOrderDetail == null ? "" : repaymentOrderDetail.getContractNo(), parameters.getFinancialContractUuid());
            criticalMarker.put(parameters.getRepaymentOrderDetailUuid(), marker);
        }
        return criticalMarker;
    }

    private String getCriticalMarker(String uniqueId, String contractNo, String financialContractUuid) throws GiottoException {
        if (!StringUtils.isBlank(uniqueId)) {
            FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.UNIQUE_ID, uniqueId,
                    FastContract.class, true);
            if (fastContract != null && !StringUtils.isBlank(fastContract.getUuid())) {
                return fastContract.getUuid();
            }
        }
        if (!StringUtils.isBlank(contractNo)) {
            FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, contractNo,
                    FastContract.class, true);
            if (fastContract != null && !StringUtils.isBlank(fastContract.getUuid())) {
                return fastContract.getUuid();
            }
        }
        return financialContractUuid;
    }

    /**
     * 2. 校验和虚拟落单
     *
     * @param repaymentOrderParametersList
     * @return
     */
    @Override
    public boolean checkAndSave(List<RepaymentOrderParameters> repaymentOrderParametersList) {
        boolean isValid = true;
        for (RepaymentOrderParameters repaymentOrderParameters : repaymentOrderParametersList) {
            log.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.START_CHECK
                    + "RepaymentOrderUuid:[" + repaymentOrderParameters.getRepaymentOrderUuid()
                    + "].RepaymentOrderDetailUuid:[" + repaymentOrderParameters.getRepaymentOrderDetailUuid() + "].");

            boolean isCurrentValid = checkAndSaveResultTryTimesIfLockException(repaymentOrderParameters);
            isValid = isValid && isCurrentValid;
        }
        return isValid;
    }

    private boolean checkAndSaveResultTryTimesIfLockException(RepaymentOrderParameters repaymentOrderParameters) {
        boolean isCurrentValid = false;
        int try_times = 3;
        while (try_times > 0) {
            try {
                isCurrentValid = checkAndSaveResult(repaymentOrderParameters, true);

                try_times = 0;
                log.info(GloableLogSpec.AuditLogHeaderSpec()
                        + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.END_CHECK + "RepaymentOrderUuid:["
                        + repaymentOrderParameters.getRepaymentOrderUuid() + "].RepaymentOrderDetailUuid:["
                        + repaymentOrderParameters.getRepaymentOrderDetailUuid() + "].isCurrentValid:[" + isCurrentValid + "],try_times[" + try_times + "].");
            } catch (AssetSetOrderPaymentStatusLockFailedException assetLockException) {
                try_times--;
                log.error(GloableLogSpec.AuditLogHeaderSpec()
                        + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.FAIL_CHECK + "RepaymentOrderUuid:["
                        + repaymentOrderParameters.getRepaymentOrderUuid() + "].RepaymentOrderDetailUuid:["
                        + repaymentOrderParameters.getRepaymentOrderDetailUuid() + ",with full stack trace ["
                        + ExceptionUtils.getFullStackTrace(assetLockException) + "],try_times[" + try_times + "].");
            } catch (Exception e) {
                try_times = 0;
                log.error(GloableLogSpec.AuditLogHeaderSpec()
                        + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.FAIL_CHECK + "RepaymentOrderUuid:["
                        + repaymentOrderParameters.getRepaymentOrderUuid() + "].RepaymentOrderDetailUuid:["
                        + repaymentOrderParameters.getRepaymentOrderDetailUuid() + ",with full stack trace ["
                        + ExceptionUtils.getFullStackTrace(e) + "],try_times[" + try_times + "].");
            }
        }
        return isCurrentValid;
    }

    public boolean checkAndSaveResult(RepaymentOrderParameters repaymentOrderParameters, boolean isSaveItemCheckFail) throws GiottoException {
        long start = System.currentTimeMillis();
        refreshCache(repaymentOrderParameters.getRepaymentOrderDetail(), repaymentOrderParameters.getFinancialContractNo());
        long end_refresh_cache = System.currentTimeMillis();
        log.debug("checkAndSaveResult#method[refreshCache] use [" + (end_refresh_cache - start) + "]ms");

        RepaymentOrderCheck checkModel = repaymentOrderHandler.check(repaymentOrderParameters);
        long end_check = System.currentTimeMillis();
        log.debug("checkAndSaveResult#method[check] use [" + (end_check - end_refresh_cache) + "]ms");

        repaymentOrderHandler.saveAfterCheck(repaymentOrderParameters, checkModel, isSaveItemCheckFail);
        long end_save = System.currentTimeMillis();
        log.debug("checkAndSaveResult#method[saveAfterCheck] use [" + (end_save - end_check) + "]ms");

        return checkModel.isValid();
    }

    private void refreshCache(RepaymentOrderDetail repaymentOrderDetail, String financialContractNo) throws GiottoException {
        if (repaymentOrderDetail.getRepaymentWayEnum() == null
                || repaymentOrderDetail.getRepaymentWayEnum() == RepaymentWay.REPURCHASE) {
            return;
        }
        FastAssetSet fastAssetSet = getFastAssetSet(repaymentOrderDetail, financialContractNo);
        if (fastAssetSet == null) {
            return;
        }
        String assetSetUuid = fastAssetSet.getAssetUuid();
        fastHandler.getByKey(FastContractKeyEnum.UUID, fastAssetSet.getContractUuid(), FastContract.class, false);
        fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.REPAYMENT_BUSINESS_UUID, assetSetUuid,
                FastRepaymentOrderItem.class, false);
    }

    private FastAssetSet getFastAssetSet(RepaymentOrderDetail repaymentOrderDetail, String financialContractNo) throws GiottoException {
        String repayScheduleNo = repaymentOrderDetail.getRepayScheduleNo();
        if (StringUtils.isNotEmpty(repayScheduleNo)) {
            String repayScheduleNo4MD5 = repaymentPlanService.getRepayScheduleNoMD5(financialContractNo, repayScheduleNo, com.zufangbao.sun.utils.StringUtils.EMPTY);
            return fastHandler.getByKey(FastAssetSetKeyEnum.REPAY_SCHEDULE_NO, repayScheduleNo4MD5,
                    FastAssetSet.class, false);
        }
        String assetSetNo = getAssetSetNo(repaymentOrderDetail);
        return fastHandler.getByKey(FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO, assetSetNo,
                FastAssetSet.class, false);
    }

    private String getAssetSetNo(RepaymentOrderDetail repaymentOrderDetail) {
        return repaymentOrderDetail.getRepaymentBusinessNo();
    }

    /**
     * 3.rollBack
     *
     * @param parametersList
     * @return
     */
    @Override
    public boolean rollBack(List<RepaymentOrderParameters> parametersList) {
        boolean result = true;
        for (RepaymentOrderParameters repaymentOrderParameters : parametersList) {

            log.info(GloableLogSpec.AuditLogHeaderSpec()
                    + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.START_ROLL_BACK + "RepaymentOrderUuid:["
                    + repaymentOrderParameters.getRepaymentOrderUuid() + "].RepaymentOrderDetailUuid:["
                    + repaymentOrderParameters.getRepaymentOrderDetailUuid() + "].");

            try {
                RepaymentOrderDetail repaymentOrderDetail = repaymentOrderParameters.getRepaymentOrderDetail();
                if (repaymentOrderDetail == null) {
                    result = false;
                    continue;
                }
                boolean currentResult = repaymentOrderItemHandler.lapse_repayment_order_item(false,
                        repaymentOrderParameters.getRepaymentOrderDetailUuid(),
                        repaymentOrderDetail.getRepaymentWayEnum() == null ? null
                                : repaymentOrderDetail.getRepaymentWayEnum().getRepaymentBusinessType());
                result = result && currentResult;
                log.info(GloableLogSpec.AuditLogHeaderSpec()
                        + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.END_ROLL_BACK + "RepaymentOrderUuid:["
                        + repaymentOrderParameters.getRepaymentOrderUuid() + "].RepaymentOrderDetailUuid:["
                        + repaymentOrderParameters.getRepaymentOrderDetailUuid() + "]." + "currentResult:[" + currentResult + "].." + "total_result:[" + result + "].");

            } catch (Exception e) {
                result = false;

                log.info(GloableLogSpec.AuditLogHeaderSpec()
                        + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.FAIL_ROLL_BACK + "RepaymentOrderUuid:["
                        + repaymentOrderParameters.getRepaymentOrderUuid() + "].RepaymentOrderDetailUuid:["
                        + repaymentOrderParameters.getRepaymentOrderDetailUuid() + ",with full stack trace ["
                        + ExceptionUtils.getFullStackTrace(e) + "]");
            }
        }

        return result;
    }

    @Override
    public void check_and_save_for_single_contract_repayment_order(RepaymentOrder repaymentOrder, List<RepaymentOrderDetail> repaymentOrderDetailList) throws RepaymentOrderCheckException {
        RepaymentOrderDetail firstDetail = repaymentOrderDetailList.get(0);

        RepaymentWay repaymentWay = firstDetail.getRepaymentWayEnum();
        String customerSource = getCustomerSource(firstDetail);
        String contractNo = firstDetail.getContractNo();
        String contractUniqueId = firstDetail.getContractUniqueId();

        String financialContractUuid = repaymentOrder.getFinancialContractUuid();
        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);

        int repaymentCheckDays = financialContract.getRepaymentCheckDays();
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());

        List<RepaymentOrderParameters> parameters = repaymentOrderDetailList.parallelStream().map(repaymentDetail ->
                new RepaymentOrderParameters(repaymentOrder, repaymentDetail, repaymentWay, customerSource, repaymentCheckDays, ledgerBook, contractNo, contractUniqueId, repaymentOrderDetailList.size())).collect(Collectors.toList());


        //校验  落盘
        boolean result = checkAndSave(parameters);
        if (!result) {
            rollBack(parameters);
        }
        //更新 还款订单 校验成功
        repaymentOrderService.update_repayment_order_check_status_and_placing_status(repaymentOrder.getOrderUuid(), result ? OrderCheckStatus.VERIFICATION_SUCCESS : OrderCheckStatus.VERIFICATION_FAILURE);


        if (!result) {
            throw new RepaymentOrderCheckException();
        }
    }

    private String getCustomerSource(RepaymentOrderDetail firstDetail) {
        String customerSource = "";
        Contract contract = contractService.getContractBy(firstDetail.getContractUniqueId(), firstDetail.getContractNo());
        if (contract != null && StringUtils.isNotBlank(contract.getCustomerUuid())) {

            Customer customer = customerService.getCustomer(contract.getCustomerUuid());
            if (customer != null) {
                customerSource = customer.getSource();
            }

        }
        return customerSource;
    }
}