package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.microservice.handler.RepaymentBusinessCheckHandler;
import com.suidifu.owlman.microservice.model.RepaymentOrderCheck;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderCheckException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.repayment.order.IdentificationMode;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanType;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhusy on 2017/7/13.
 */
@Component("repaymentPlanCheckHandler")
public class RepaymentPlanCheckHandlerImpl implements RepaymentBusinessCheckHandler {
    private static Log logger = LogFactory.getLog(RepaymentPlanCheckHandlerImpl.class);
    @Autowired
    private FastHandler fastHandler;
    @Autowired
    private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Override
    public void checkRepaymentNo(RepaymentOrderDetail repaymentOrderDetail, RepaymentOrderCheck checkModel) throws RepaymentOrderCheckException, GiottoException {
        String contractUuid = checkModel.getFastContractUuid();

        String financialContractNo = checkModel.getFinancialContractNo();
        String financialContractUuid = checkModel.getFinancialContractUuid();
        // 1. 校验还款计划是否在本合同内,还款计划必须有效(不为扣款中，作废，还款成功，已回购，违约)

        FastAssetSet fastAssetSet = checkAndGetFastAssetSet(repaymentOrderDetail, contractUuid, financialContractNo, financialContractUuid);
        if (fastAssetSet == null) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec()
                    + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.CHECK_REPAYMENT_NO_EMPTY
                    + "RepaymentOrderDetailUuid:[" + repaymentOrderDetail.getRepaymentOrderDetailUuid() + "]."
                    + "repaymentPlanNo:[" + repaymentOrderDetail.getRepaymentBusinessNo() + "].contractUuid:[" + contractUuid + "].currentPeriod:[" + repaymentOrderDetail.getCurrentPeriod() + "].");
            return;

        }
        if (!StringUtils.equalsIngoreNull(contractUuid, fastAssetSet.getContractUuid())) {
            throw new RepaymentOrderCheckException(GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_PLAN_CONTRACT_MSG);
        }
        if (ExecutingStatus.SUCCESSFUL.ordinal() == fastAssetSet.getExecutingStatus() ||
                ExecutingStatus.REPURCHASING.ordinal() == fastAssetSet.getExecutingStatus() ||
                ExecutingStatus.REPURCHASED.ordinal() == fastAssetSet.getExecutingStatus() ||
                ExecutingStatus.DEFAULT.ordinal() == fastAssetSet.getExecutingStatus() ||
                ExecutingStatus.STOPPED.ordinal() == fastAssetSet.getExecutingStatus()) {
            ExecutingStatus executionStatus = EnumUtil.fromOrdinal(ExecutingStatus.class, fastAssetSet.getExecutingStatus());
            throw new RepaymentOrderCheckException(GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_PLAN_STATE_MSG, "执行状态[" + executionStatus == null ? "" : executionStatus.getChinessName() + "]");
        }
        if (AssetSetActiveStatus.INVALID.ordinal() == fastAssetSet.getActiveStatus()) {
            throw new RepaymentOrderCheckException(GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_PLAN_STATE_MSG, "还款计划状态[作废]");
        }
        //非提前还款的冻结单
        if (AssetSetActiveStatus.FROZEN.ordinal() == fastAssetSet.getActiveStatus()
                &&
                false == (RepaymentPlanType.PREPAYMENT.ordinal() == fastAssetSet.getRepaymentPlanType() && new Integer(1).intValue() == fastAssetSet.getCanBeRollbacked())) {
            throw new RepaymentOrderCheckException(GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_PLAN_STATE_MSG, "还款计划状态[关闭]");


        }
        // 2. 校验还款计划是否已经存在处理中的扣款申请 check_and_save deuct lock
        if (AssetSet.isAssetEmptyLocked(fastAssetSet.getActiveDeductApplicationUuid()) == false) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_PLAN_IN_DEDUCT_MSG);
        }
        checkModel.setRepaymentBusinessUuid(fastAssetSet.getAssetUuid());
        checkModel.setFastAssetSet(fastAssetSet);
        repaymentOrderDetail.setRepaymentBusinessNo(fastAssetSet.getSingleLoanContractNo());
        repaymentOrderDetail.setRepayScheduleNo(fastAssetSet.getOuterRepaymentPlanNo());
    }

    private FastAssetSet checkAndGetFastAssetSet(RepaymentOrderDetail repaymentOrderDetail, String contractUuid, String financialContractNo, String financialContractUuid)
            throws RepaymentOrderCheckException, GiottoException {
        if (isNonEssentialAssetNo(financialContractUuid)) {
            return nonEssentialAssetNo(repaymentOrderDetail, contractUuid, financialContractNo);
        } else {
            return essentialAssetNo(repaymentOrderDetail, financialContractNo);
        }
    }

    private FastAssetSet essentialAssetNo(RepaymentOrderDetail repaymentOrderDetail,
                                          String financialContractNo) throws RepaymentOrderCheckException, GiottoException {
        String repayScheduleNo = repaymentOrderDetail.getRepayScheduleNo();
        String repaymentPlanNo = repaymentOrderDetail.getRepaymentBusinessNo();
        if (StringUtils.isNotEmpty(repayScheduleNo)) {
            return mode_repay_schedule_no(repaymentOrderDetail, repayScheduleNo, financialContractNo);
        } else if (StringUtils.isNotEmpty(repaymentPlanNo)) {
            return mode_repayment_plan_no(repaymentOrderDetail);
        } else {
            throw new RepaymentOrderCheckException("还款业务编号［repaymentBusinessNo],商户还款编号[repayScheduleNo],至少有一个不为空！");
        }
    }

    private FastAssetSet nonEssentialAssetNo(RepaymentOrderDetail repaymentOrderDetail, String contractUuid,
                                             String financialContractNo) throws RepaymentOrderCheckException, GiottoException {
        //优先级:方式1:商户还款编号+合同编号 方式2:合同编号+五维还款编号 方式3:期数+合同编号 方式4:合同编号
        String repayScheduleNo = repaymentOrderDetail.getRepayScheduleNo();
        String repaymentPlanNo = repaymentOrderDetail.getRepaymentBusinessNo();
        if (StringUtils.isNotEmpty(repayScheduleNo)) {
            return mode_repay_schedule_no(repaymentOrderDetail, repayScheduleNo, financialContractNo);
        }

        if (StringUtils.isNotEmpty(repaymentPlanNo)) {
            return mode_repayment_plan_no(repaymentOrderDetail);
        }
        int currentPeriod = repaymentOrderDetail.getCurrentPeriod();
        if (currentPeriod > 0) {
            repaymentOrderDetail.setIdentificationMode(IdentificationMode.CONTRACT_PERIOD.getOrdinal());
            repaymentOrderDetail.setCurrentPeriod(currentPeriod);
            return null;
        } else {
            repaymentOrderDetail.setIdentificationMode(IdentificationMode.CONTRACT_ONLY.getOrdinal());
            currentPeriod = repaymentPlanService.get_current_unclear_asset_set_period(contractUuid);
            if (currentPeriod < 1) {
                throw new RepaymentOrderCheckException("找不到未结清的还款计划");
            }
            repaymentOrderDetail.setCurrentPeriod(currentPeriod);
            return null;
        }
    }

    private boolean isNonEssentialAssetNo(String financialContractUuid) {
        return financialContractConfigurationService.isFinancialContractConfigCodeConfiged(financialContractUuid, FinancialContractConfigurationCode.IS_REPAYMENT_ORDER_RECEIVED_IN_ADVANCE.getCode());
    }

    private FastAssetSet mode_repayment_plan_no(RepaymentOrderDetail repaymentOrderDetail)
            throws RepaymentOrderCheckException, GiottoException {
        String repaymentPlanNo = repaymentOrderDetail.getRepaymentBusinessNo();
        if (StringUtils.isEmpty(repaymentPlanNo)) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec()
                    + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.CHECK_REPAYMENT_NO
                    + "RepaymentOrderDetailUuid:[" + repaymentOrderDetail.getRepaymentOrderDetailUuid() + "],repaymentPlanNo:[" + repaymentPlanNo + "].");
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_PLAN_NO_MSG);
        }
        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.SINGLE_LOAN_CONTRACT_NO,
                repaymentPlanNo, FastAssetSet.class, true);
        if (null == fastAssetSet) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec()
                    + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.CHECK_REPAYMENT_NO
                    + "RepaymentOrderDetailUuid:[" + repaymentOrderDetail.getRepaymentOrderDetailUuid() + "],repaymentPlanNo:[" + repaymentPlanNo + "].");
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAYMENT_PLAN_NO_MSG, "无该还款计划");
        }
        repaymentOrderDetail.setIdentificationMode(IdentificationMode.REPAYMENT_PLAN_NO.getOrdinal());
        repaymentOrderDetail.setCurrentPeriod(fastAssetSet.getCurrentPeriod());
        return fastAssetSet;
    }

    private FastAssetSet mode_repay_schedule_no(RepaymentOrderDetail repaymentOrderDetail, String repayScheduleNo, String financialContractNo)
            throws RepaymentOrderCheckException, GiottoException {
        if (StringUtils.isEmpty(financialContractNo)) {
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_MER_ID_MSG);
        }
        String repayScheduleNo4MD5 = repaymentPlanService.getRepayScheduleNoMD5(financialContractNo, repayScheduleNo, StringUtils.EMPTY);
        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.REPAY_SCHEDULE_NO,
                repayScheduleNo4MD5, FastAssetSet.class, true);
        if (null == fastAssetSet) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec()
                    + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.CHECK_REPAYMENT_NO
                    + "RepaymentOrderDetailUuid:[" + repaymentOrderDetail.getRepaymentOrderDetailUuid() + "],financialContractNo:[" + financialContractNo + "],repayScheduleNo:[" + repayScheduleNo + "],repayScheduleNo4MD5:[" + repayScheduleNo4MD5 + "].");
            throw new RepaymentOrderCheckException(
                    GlobalErrorMsgSpec.RepaymentOrderErrorMsgSpec.ERROR_OF_REPAY_SCHEDULE_NO_MSG);
        }

        repaymentOrderDetail.setIdentificationMode(IdentificationMode.REPAY_SCHEDULE_NO.getOrdinal());
        repaymentOrderDetail.setCurrentPeriod(fastAssetSet.getCurrentPeriod());
        return fastAssetSet;
    }

    @Override
    public BigDecimal getTotalRepaymentBusinessAmount(RepaymentOrderCheck checkModel) {
        FastAssetSet fastAssetSet = checkModel.getFastAssetSet();
        return fastAssetSet.getAssetFairValue();
    }

    @Override
    public Map<String, BigDecimal> getTotalCharges(RepaymentOrderCheck checkModel) {
        FastAssetSet fastAssetSet = checkModel.getFastAssetSet();
        Map<String, BigDecimal> totalCharges = repaymentPlanExtraChargeService
                .getAssetSetExtraChargeModels(checkModel.getRepaymentBusinessUuid());
        totalCharges.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, fastAssetSet.getAssetPrincipalValue());
        totalCharges.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, fastAssetSet.getAssetInterestValue());
        return totalCharges;
    }
}