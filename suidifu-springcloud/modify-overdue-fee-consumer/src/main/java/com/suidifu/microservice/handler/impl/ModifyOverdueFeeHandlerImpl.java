package com.suidifu.microservice.handler.impl;

import com.suidifu.owlman.microservice.handler.ModifyOverdueFeeHandler;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.handler.*;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by MieLongJun on 18-1-26. from com.zufangbao.wellsfargo.yunxin.handler.impl.ModifyOverdueFeeNewHandlerImpl
 */
@Component("modifyOverdueFeeNewHandler")
@Log4j2
public class ModifyOverdueFeeHandlerImpl implements ModifyOverdueFeeHandler {

    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private RepaymentPlanHandler repaymentPlanHandler;
    @Resource
    private RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private RepaymentPlanValuationHandler repaymentPlanValuationHandler;
    @Resource
    private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
    @Resource
    private ExtraChargeSnapShotHandler overdueFeeLogHandler;
    @Resource
    private ContractService contractService;

    @Override
    public void modifyOverdueFeeSaveLog(String contractUuid, ModifyOverdueParams parameter, int priority) {
        try {
            long startTime = 0;
            long end;

            if (log.isDebugEnabled()) {
                startTime = System.currentTimeMillis();
            }
            String planUuid = parameter.getRepaymentPlanUuid();
            AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(planUuid);

            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.info(
                        "modify-overdue-fee-consumer#modifyOverdueFeeSaveLog#getUniqueRepaymentPlanByUuid usage:{}ms, currentTimeMillis:{}, repaymentPlanUuid:{}",
                        (end - startTime), end, planUuid);
                startTime = end;
            }
            if (repaymentPlan == null) {
                log.error("modify-overdue-fee-consumer#modifyOverdueFeeSaveLog fail, repaymentPlan is null, parameter:{}", parameter.toString());
                return;
            }

            Date overDueFeeCalcDate = parameter.getCalcDate();
            if (!repaymentPlan.isOverdueDate(overDueFeeCalcDate)) {
                log.warn("repaymentPlan is not overdue, skip.");
                return;
            }

            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.info(
                        "modify-overdue-fee-consumer#modifyOverdueFeeSaveLog#getCalcDate-checkOverdueStatus usage:{}ms, currentTimeMillis:{}, repaymentPlanUuid:{}",
                        (end - startTime), end, planUuid);
                startTime = end;
            }

            modifyOverdueCharges(repaymentPlan, parameter);

            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.info(
                        "modify-overdue-fee-consumer#modifyOverdueFeeSaveLog#modifyOverdueCharges usage:{}ms, currentTimeMillis:{}, repaymentPlanUuid:{}",
                        (end - startTime), end, planUuid);
                startTime = end;
            }

            Contract contract = contractService.getContract(contractUuid);

            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.info(
                        "modify-overdue-fee-consumer#modifyOverdueFeeSaveLog#getContract usage:{}ms, currentTimeMillis:{}, repaymentPlanUuid:{}",
                        (end - startTime), end, planUuid);
                startTime = end;
            }

            BusinessLogsModel model = new BusinessLogsModel(OperateMode.UPDATE, null, overDueFeeCalcDate, null);
            repaymentPlanHandler.saveBusinessLogs(contract, Collections.singletonList(repaymentPlan), model);

            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.info(
                        "modify-overdue-fee-consumer#modifyOverdueFeeSaveLog#saveBusinessLogs usage:{}ms, currentTimeMillis:{}, repaymentPlanUuid:{}",
                        (end - startTime), end, planUuid);
            }

        } catch (Exception e) {
            log.error(
                    "ModifyOverdueParams:[" + parameter.toString() + "], FullStackTrace:" + ExceptionUtils.getFullStackTrace(e));
            throw e;
        }
    }

    /**
     * 更新逾期费用
     *
     * @param repaymentPlan 还款计划
     * @param overdueParams 逾期费用明细
     */
    private void modifyOverdueCharges(AssetSet repaymentPlan, ModifyOverdueParams overdueParams) {
        long start = System.currentTimeMillis();
        try {
            Map<String, BigDecimal> accountAmountMap = buildModifyOverdueMap(overdueParams);
            modifyFeeInAssetSetExtraCharge(repaymentPlan, accountAmountMap);
            FinancialContract financialContract = financialContractService
                    .getFinancialContractBy(repaymentPlan.getFinancialContractUuid());
            modifyOverDueFeeInLedgerBook(financialContract, repaymentPlan, accountAmountMap);
            updateAssetFairAmount(repaymentPlan);
            String ledgerBookNo = financialContract.getLedgerBookNo();
            updateAssetStatusIfClear(repaymentPlan, ledgerBookNo);
            overdueFeeLogHandler.saveOverdueFeeLog(repaymentPlan);
        } finally {
            log.debug("#modifyOverdueCharges end, use: " + (System.currentTimeMillis() - start) + " ms");
        }
    }

    /**
     * 遍历修改还款计划其他费用表
     *
     * @param repaymentPlan    还款计划
     * @param accountAmountMap 逾期费用明细Map
     */
    private void modifyFeeInAssetSetExtraCharge(AssetSet repaymentPlan, Map<String, BigDecimal> accountAmountMap) {
        long start = System.currentTimeMillis();
        try {
            for (Map.Entry<String, BigDecimal> entry : accountAmountMap.entrySet()) {
                repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, entry.getValue(), entry.getKey());
            }
        } finally {
            log.debug("#modifyFeeInAssetSetExtraCharge end, use: " + (System.currentTimeMillis() - start) + " ms");
        }
    }

    /**
     * 创建逾期费用明细Map
     *
     * @param overdueParams 逾期费用参数Model
     * @return 逾期费用明细Map
     */
    private Map<String, BigDecimal> buildModifyOverdueMap(ModifyOverdueParams overdueParams) {
        long start = System.currentTimeMillis();
        try {
            BigDecimal feePenalty = overdueParams.getOverdueFeePenalty();
            BigDecimal feeObligation = overdueParams.getOverdueFeeObligation();
            BigDecimal feeService = overdueParams.getOverdueFeeService();
            BigDecimal feeOther = overdueParams.getOverdueFeeOther();

            Map<String, BigDecimal> accountAmountMap = new HashMap<>();
            accountAmountMap.put(ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, feePenalty);
            accountAmountMap.put(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, feeObligation);
            accountAmountMap.put(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, feeService);
            accountAmountMap.put(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, feeOther);
            return accountAmountMap;
        } finally {
            log.debug("#buildModifyOverdueMap end, use: " + (System.currentTimeMillis() - start) + " ms");
        }
    }

    /**
     * 修改账本中的逾期费用金额
     *
     * @param financialContract     信托合同
     * @param repaymentPlan         还款计划
     * @param accountDeltaAmountMap 逾期费用明细Map
     */
    private void modifyOverDueFeeInLedgerBook(FinancialContract financialContract, AssetSet repaymentPlan,
                                              Map<String, BigDecimal> accountDeltaAmountMap) {
        long start = System.currentTimeMillis();
        try {
            LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
            repaymentPlanValuationHandler
                    .modifyOverDueFeeInLedgerBook(repaymentPlan, financialContract, ledgerBook, accountDeltaAmountMap);
        } finally {
            log.debug("#buildModifyOverdueMap end, use: " + (System.currentTimeMillis() - start) + " ms");
        }
    }

    /**
     * 更新已结清的还款计划状态
     *
     * @param repaymentPlan 还款计划
     * @param ledgerBookNo  账本编号
     */
    private void updateAssetStatusIfClear(AssetSet repaymentPlan, String ledgerBookNo) {
        String assetUuid = repaymentPlan.getAssetUuid();
        Date actualRecycleDate = repaymentPlan.getActualRecycleDate();
        ExecutingStatus executingStatus = repaymentPlan.getExecutingStatus();
        List<String> assetSetUUIDs = Collections.singletonList(assetUuid);
        updateAssetStatusLedgerBookHandler
                .updateAssetsFromLedgerBook(ledgerBookNo, assetSetUUIDs, actualRecycleDate, RepaymentType.NORMAL,
                        executingStatus);
    }

    /**
     * 更新还款计划公允值
     *
     * @param repaymentPlan 还款计划
     */
    private void updateAssetFairAmount(AssetSet repaymentPlan) {
        BigDecimal assetSetFairAmount = repaymentPlanService.get_principal_interest_and_extra_amount(repaymentPlan);
        repaymentPlan.update_asset_fair_value_and_valution_time(assetSetFairAmount);
        repaymentPlanService.update(repaymentPlan);
    }
}