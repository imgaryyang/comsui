package com.suidifu.microservice.handler.impl;


import com.suidifu.microservice.handler.PrepaymentHandler;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.WriteOffReason;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentStatus;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanValuationHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("prepaymentHandler")
public class PrepaymentHandlerImpl implements PrepaymentHandler {
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private PrepaymentApplicationService prepaymentApplicationService;
    @Resource
    private ContractService contractService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private DeductApplicationService deductApplicationService;
    @Resource
    private RepaymentPlanValuationHandler repaymentPlanValuationHandler;
    @Resource
    private LedgerBookHandler ledgerBookHandler;
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;

    private boolean checkPrepaymentPlan(AssetSet prepaymentPlan) {
        return prepaymentPlan != null &&
                AssetSet.EMPTY_UUID.equals(prepaymentPlan.getActiveDeductApplicationUuid()) &&
                !prepaymentPlan.isClearAssetSet() &&
                prepaymentPlan.getActiveStatus() != AssetSetActiveStatus.INVALID &&
                !isExistPartDeduct(prepaymentPlan);
    }

    @Override
    public void processingOnePrepaymentPlan(String contractUuid, int priority, Long prepaymentApplicationId) {
        PrepaymentApplication application = prepaymentApplicationService.getByPrepaymentApplicationId(prepaymentApplicationId);
        if (null == application) {
            return;
        }
        AssetSet prepaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(application.getAssetSetUuid());
        if (!checkPrepaymentPlan(prepaymentPlan)) {
            return;
        }
        boolean flag = prepaymentPlanRollBack(application, prepaymentPlan);
        if (!flag) {
            undoFrozenPrepaymentPlan(prepaymentPlan);
        }
    }

    /**
     * 提前还款回滚
     */
    private boolean prepaymentPlanRollBack(PrepaymentApplication application, AssetSet prepaymentPlan) {
        List<DeductApplication> deductApplicationList = deductApplicationService.getDeductApplicationByRepaymentPlanCode(prepaymentPlan.getAssetUuid());
        List<DeductApplication> failDeductApplicationList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deductApplicationList)) {
            failDeductApplicationList = deductApplicationList.stream().filter(d -> d.getExecutionStatus() == DeductApplicationExecutionStatus.FAIL).collect
                    (Collectors.toList());
        }
        //若提前还款出现扣款失败或逾期的情况，立刻回滚
        if (CollectionUtils.isNotEmpty(failDeductApplicationList) || DateUtils.getToday().after(prepaymentPlan.getAssetRecycleDate())) {
            String errMsg = invalidPrepaymentAndUndoFrozenBePredRepaymentPlan(application, prepaymentPlan);
            return StringUtils.isBlank(errMsg);
        }
        return false;
    }

    /**
     * 解冻提前还款
     */
    private void undoFrozenPrepaymentPlan(AssetSet prepaymentPlan) {
        //查询提前还款之前的还款计划是否都已结清
        Integer count = repaymentPlanService.count_open_and_unclear_asset_list_before_prepayment_plan(prepaymentPlan.getContractUuid(), prepaymentPlan
                .getCurrentPeriod());
        if (prepaymentPlan.getActiveStatus() != AssetSetActiveStatus.FROZEN || count != 0) {
            return;
        }
        //将提前还款状态设为开启
        prepaymentPlan.setActiveStatus(AssetSetActiveStatus.OPEN);
        repaymentPlanService.saveOrUpdate(prepaymentPlan);
        log.info("#提前还款解冻， 提前还款计划uuid = (" + prepaymentPlan.getAssetUuid() + ")");
        //为解冻的提前还款计划评估并生成结算单
        try {
            if (prepaymentPlan.isAssetRecycleDate(DateUtils.getToday())) {
                repaymentPlanValuationHandler.valuate_repayment_plan_and_system_create_order(prepaymentPlan.getAssetUuid(), DateUtils.getToday());
            }
        } catch (Exception e) {
            log.error("#processingPrepaymentPlan occur error, prepayment plan uuid = [" + prepaymentPlan.getAssetUuid() + "].");
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在部分扣款
     */
    private boolean isExistPartDeduct(AssetSet prepaymentPlan) {
        FinancialContract financialContract = financialContractService.getCacheableFinancialContracBy(prepaymentPlan.getFinancialContractUuid());
        String ledgerBookNo = financialContract.getLedgerBookNo();
        BigDecimal ledgerBookAmount = ledgerBookStatHandler.unrecovered_asset_snapshot(ledgerBookNo, prepaymentPlan.getAssetUuid(), prepaymentPlan.getCustomerUuid
                (), true);
        return ledgerBookAmount.compareTo(BigDecimal.ZERO) > 0 && ledgerBookAmount.compareTo(prepaymentPlan.getAssetFairValue()) < 0;
    }

    /**
     * 作废提前还款并解锁之前的还款计划
     */
    @Override
    public String invalidPrepaymentAndUndoFrozenBePredRepaymentPlan(PrepaymentApplication application,
                                                                    AssetSet prepaymentPlan) {
        try {
            if (application == null || prepaymentPlan == null) {
                return "请求参数有误！";
            }
            //提前还款不存在部分还款
            //作废提前还款
            log.info("#将提前还款作废并将贷款合同回滚至之前的版本， 提前还款计划uuid = (" + prepaymentPlan.getAssetUuid() + ")");
            repaymentPlanService.wirteOffRepaymentPlan(Arrays.asList(prepaymentPlan), WriteOffReason.PRE_REPAYEMNT_PLAN, com.zufangbao.sun.utils.StringUtils.EMPTY);
            //获得提前还款之后的还款计划uuid
            List<String> repaymentPlanUuidList = application.getBePredRepaymentPlanUuidListByJson();
            //解冻还款计划,回滚版本号，评估并重新生成结算单
            int rollbackVersionNo = -1;
            //解冻还款计划生成结算单
            FinancialContract financialContract = financialContractService.getFinancialContractBy(prepaymentPlan.getFinancialContractUuid());
            LedgerTradeParty party = new LedgerTradeParty(financialContract.getCompany().getUuid(), prepaymentPlan.getCustomerUuid());
            List<AssetSet> assetSetToImportLedgers = new ArrayList<>();
            for (String repaymentPlanUuid : repaymentPlanUuidList) {
                AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(repaymentPlanUuid);
                assetSetToImportLedgers.add(assetSet);
            }
            if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNo
                    (financialContract.getLedgerBookNo())) {
                log.info("begin to PrepaymentHandlerImpl" +
                        "#invalidPrepaymentAndUndoFrozenBePredRepaymentPlan make#is book_loan_assets_v2_pre !!!");
                ledgerBookV2Handler.book_loan_assets_v2_pre(financialContract
                        .getLedgerBookNo(), assetSetToImportLedgers, party);
                log.info("end !!!");
            }
            if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(financialContract.getLedgerBookNo())) {
                ledgerBookHandler.book_loan_assets(financialContract.getLedgerBookNo(), assetSetToImportLedgers, party);
            }
            for (AssetSet assetSet : assetSetToImportLedgers) {
                assetSet.setActiveStatus(AssetSetActiveStatus.OPEN);
                repaymentPlanService.update(assetSet);
                if (assetSet.isAssetRecycleDate(DateUtils.getToday())) {
                    repaymentPlanValuationHandler.valuate_repayment_plan_and_system_create_order(assetSet.getAssetUuid(), DateUtils.getToday());
                }
                rollbackVersionNo = assetSet.getVersionNo();

            }
            //回滚贷款合同版本号
            Contract contract = prepaymentPlan.getContract();
            assetSetToImportLedgers.sort(Comparator.comparingInt(AssetSet::getCurrentPeriod));
            if (rollbackVersionNo != -1) {
                saveContractNewInfo(contract, assetSetToImportLedgers.get(assetSetToImportLedgers.size() - 1).getCurrentPeriod(), rollbackVersionNo);
            }
            application.setPrepaymentStatus(PrepaymentStatus.FAIL);
            prepaymentApplicationService.saveOrUpdate(application);
            return "";
        } catch (Exception e) {
            log.error("#processingPrepaymentPlan occur error, prepayment plan uuid = [" + prepaymentPlan.getAssetUuid() + "].");
            return "提前还款作废失败！";
        }
    }

    /**
     * 贷款合同保存新的期数和新的版本号
     *
     * @param contract     贷款合同
     * @param newPeriod    新的期数
     * @param newVersionNo 新的版本号
     */
    private void saveContractNewInfo(Contract contract, int newPeriod, int newVersionNo) {
        contract.setActiveVersionNo(newVersionNo);
        contract.setPeriods(newPeriod);
        contractService.update(contract);
    }
}