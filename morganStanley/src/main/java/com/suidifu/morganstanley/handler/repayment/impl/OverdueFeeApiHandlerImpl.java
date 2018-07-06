package com.suidifu.morganstanley.handler.repayment.impl;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.OVERDUE_MODIFY_AMOUNT_LESS_THAN_PAIN_IN_AMOUNT_GREATER;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_CODE_NOT_IN_CONTRACT;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PLAN_IS_NOT_RECEIVABLE;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PLAN_IS_PAID_OFF;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PLAN_LOCKED;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PLAN_NOT_OPEN;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPEAT_REQUEST_NO;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.SINGLE_LOAN_CONTRACT_NO_ERROR;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.morganstanley.configuration.bean.weifang.WeiFangProperties;
import com.suidifu.morganstanley.handler.repayment.OverdueFeeApiHandler;
import com.suidifu.morganstanley.model.request.MutableFee;
import com.suidifu.morganstanley.model.request.repayment.OverdueFeeDetail;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.api.ModifyOverdueFeeLog;
import com.zufangbao.sun.yunxin.entity.model.OverdueChargesModifyModel;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.handler.RepaymentOrderItemHandler;
import com.zufangbao.sun.yunxin.service.ModifyOverDueFeeLogService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * @author louguanyang at 2017/10/13 22:43
 */
@Component("OverdueFeeApiHandler")
@Log4j2
public class OverdueFeeApiHandlerImpl implements OverdueFeeApiHandler {

    @Resource
    private ModifyOverDueFeeLogService modifyOverDueFeeLogService;
    @Resource
    private ContractApiHandler contractApiHandler;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private RepaymentOrderItemHandler repaymentOrderItemHandler;
//    @Resource
//    private ModifyOverdueFeeNewHandler modifyOverdueFeeNewHandler;
    @Resource
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Resource
    private SandboxDataSetHandler sandboxDataSetHandler;
    @Resource
    private WeiFangProperties weiFangProperties;
    @Resource
    private ProductCategoryCacheHandler productCategoryCacheHandler;
    @Resource
    private ModifyOverdueFeeMorganstanleySyncProxy modifyOverdueFeeMorganstanleySyncProxy;
    
    @Override
    public void checkRequestNo(@NotNull String requestNo, @NotNull String ip) {
        ModifyOverdueFeeLog modifyOverdueFeeLog = modifyOverDueFeeLogService.getLogByRequestNo(requestNo);
        if (modifyOverdueFeeLog != null) {
            throw new ApiException(REPEAT_REQUEST_NO);
        }
    }

    @Override
    public void saveLog(String requestNo, String requestData, String ip) {
        ModifyOverdueFeeLog modifyLog = new ModifyOverdueFeeLog(requestNo, requestData, ip);
        modifyOverDueFeeLogService.save(modifyLog);
    }

    @Override
    public List<Map<String, String>> modifyOverdueFee(List<ModifyOverdueParams> paramsList) {
        List<Map<String, String>> updateFailList = new ArrayList<>();
        long start = 0;
        String contractUuid;
        Map<String, String> errorMap;
        String key;
        for (ModifyOverdueParams parameter : paramsList) {
            try {
                start = System.currentTimeMillis();
                contractUuid = parameter.getContractUuid();
                modifyOverdueFeeMorganstanleySyncProxy.modifyOverdueFeeSaveLog(contractUuid, parameter, Priority.High.getPriority());
            } catch (Exception e) {
                key = parameter.getRepaymentNo();
                errorMap = new HashMap<>();
                errorMap.put("uuid", UUIDUtil.randomUUID());
                errorMap.put("repaymentPlanUniqueNo", key);
                updateFailList.add(errorMap);
                log.error("更新逾期费用明细接口 occur error,repaymentPlanUniqueNo:" + key + ",error stack full trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
            } finally {
                log.debug("更新逾期费用明细接口, modifyOverdueFee success, use:" + (System.currentTimeMillis() - start) + "ms");
            }
        }
        return updateFailList;
    }

    /**
     * 检查信托合同产品代码是否存在
     *
     * @param overdueFeeDetails 变更明细
     */
    @Override
    public List<ModifyOverdueParams> verifyAndReturnParamsList(List<OverdueFeeDetail> overdueFeeDetails) {
        if (CollectionUtils.isEmpty(overdueFeeDetails)) {
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), "具体变更内容[modifyOverDueFeeDetails]格式错误");
        }
        List<BigDecimal> principals = new ArrayList<>();
        List<String> penaltys = new ArrayList<>();
        List<ModifyOverdueParams> paramsList = new ArrayList<>();
        int days = 0;
        String rate = "";
        for (OverdueFeeDetail overdueFeeDetail : overdueFeeDetails) {
            String financialContractNo = overdueFeeDetail.getFinancialProductCode();
            String uniqueId = overdueFeeDetail.getContractUniqueId();
            String contractNo = overdueFeeDetail.getContractNo();
            Date overDueFeeCalcDate = overdueFeeDetail.getCalcDate();

            Contract contract = contractApiHandler.getContractBy(uniqueId, contractNo);
            FinancialContract financialContract = contractApiHandler.checkAndReturnFinancialContract(financialContractNo, contract);
            overdueFeeDetail.setFinancialProductCode(financialContract.getContractNo());
            String contractUuid = contract.getUuid();
            AssetSet repaymentPlan = checkAndReturnRepaymentPlan(overdueFeeDetail, contractUuid);
            penaltys.add(overdueFeeDetail.getPenaltyFee());
            principals.add(repaymentPlan.getAssetPrincipalValue());
            days = repaymentPlan.calculateOverdueDay(DateUtils.asDay(DateUtils.today()), financialContract.getLoanOverdueStartDay());
            rate = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContract.getUuid(), FinancialContractConfigurationCode.WEIFANG_INTEREST_RATE.getCode());
            String repaymentPlanAssetUuid = repaymentPlan.getAssetUuid();
            checkOverDueFeeCalcDateAfterAssetRecycleDate(financialContract, repaymentPlan, overDueFeeCalcDate);
            checkLedgerBookAmount(financialContract.getLedgerBookNo(), repaymentPlanAssetUuid, overdueFeeDetail);

            ModifyOverdueParams parameter = overdueFeeDetail.convertParameter(contractUuid, repaymentPlanAssetUuid);
            paramsList.add(parameter);
        }
        if(weiFangProperties.isEnable()) {
            Map<String, String> preRequest = new HashMap<>();
            preRequest.put("r", rate);//潍坊利率
            preRequest.put("overdueDay", com.zufangbao.sun.utils.JsonUtils.toJSONString(days));
            preRequest.put("penalty", com.zufangbao.sun.utils.JsonUtils.toJSONString(penaltys));
            preRequest.put("principal", com.zufangbao.sun.utils.JsonUtils.toJSONString(principals));
            ProductCategory productCategory = productCategoryCacheHandler.get("importAssetPackage/weifang/10002", true);
            CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
            boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), LogFactory.getLog(MutableFee.class));
            if(!result){
                throw new ApiException(ApiResponseCode.CUSTOMER_FEE_CHECK_FAIL);
            }
        }
        return paramsList;
    }

    private void checkLedgerBookAmount(String ledgerBookNo, String assetSetUuid, OverdueFeeDetail overdueFeeDetail) {
        Map<String, BigDecimal> amountDetailInPaying = repaymentOrderItemHandler.get_paying_amount_detail(assetSetUuid);
        OverdueChargesModifyModel overdueChargesModifyModel = new OverdueChargesModifyModel(new BigDecimal(overdueFeeDetail.getPenaltyFee()), new BigDecimal
                (overdueFeeDetail.getLatePenalty()),
                new BigDecimal(overdueFeeDetail.getLateFee()), new BigDecimal(overdueFeeDetail.getLateOtherCost()));

        BigDecimal penalty = amountDetailInPaying.getOrDefault(ExtraChargeSpec.PENALTY_KEY, BigDecimal.ZERO);
        BigDecimal overdueFeeObligation = amountDetailInPaying.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, BigDecimal.ZERO);
        BigDecimal overdueFeeServiceFee = amountDetailInPaying.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, BigDecimal.ZERO);
        BigDecimal overdueFeeOtherFee = amountDetailInPaying.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, BigDecimal.ZERO);

        checkout_modify_amount_is_greater_than_pain_in_amount(overdueChargesModifyModel.getOverdueFeePenalty(),
                ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec.PENALTY_KEY).add(penalty));
        checkout_modify_amount_is_greater_than_pain_in_amount(overdueChargesModifyModel.getOverdueFeeObligation(),
                ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY).add
                        (overdueFeeObligation));
        checkout_modify_amount_is_greater_than_pain_in_amount(overdueChargesModifyModel.getOverdueFeeService(),
                ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY).add
                        (overdueFeeServiceFee));
        checkout_modify_amount_is_greater_than_pain_in_amount(overdueChargesModifyModel.getOverdueFeeOther(),
                ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY).add(overdueFeeOtherFee));
    }

    private void checkout_modify_amount_is_greater_than_pain_in_amount(BigDecimal modifyAmount, BigDecimal paidInAmount) {
        if (modifyAmount.compareTo(paidInAmount) < 0) {
            throw new ApiException(OVERDUE_MODIFY_AMOUNT_LESS_THAN_PAIN_IN_AMOUNT_GREATER);
        }
    }

    /**
     * 校验并返回还款计划
     *
     * @param overdueFeeDetail 变更逾期费用Model详情
     * @param contractUuid     贷款合同UUID
     * @return 还款计划
     */
    private AssetSet checkAndReturnRepaymentPlan(OverdueFeeDetail overdueFeeDetail, String contractUuid) {
        String repayScheduleNo = overdueFeeDetail.getRepayScheduleNo();
        String financialContractNo = overdueFeeDetail.getFinancialProductCode();

        String repayScheduleNo4MD5 = repaymentPlanService.getRepayScheduleNoMD5(financialContractNo, repayScheduleNo, StringUtils.EMPTY);
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByConditions(repayScheduleNo4MD5,
                overdueFeeDetail.getRepaymentPlanNo(), contractUuid,
                overdueFeeDetail.getCurrentPeriod());
        if (repaymentPlan == null) {
            throw new ApiException(SINGLE_LOAN_CONTRACT_NO_ERROR);
        }
        if (!contractUuid.equals(repaymentPlan.getContractUuid())) {
            throw new ApiException(REPAYMENT_CODE_NOT_IN_CONTRACT);
        }
        if (repaymentPlan.getActiveStatus() != AssetSetActiveStatus.OPEN) {
            throw new ApiException(REPAYMENT_PLAN_NOT_OPEN);
        }
        if (repaymentPlan.isPaidOff()) {
            throw new ApiException(REPAYMENT_PLAN_IS_PAID_OFF);
        }
        String activeDeductApplicationUuid = repaymentPlan.getActiveDeductApplicationUuid();
        if (!AssetSet.EMPTY_UUID.equals(activeDeductApplicationUuid)) {
            throw new ApiException(REPAYMENT_PLAN_LOCKED);
        }
        return repaymentPlan;
    }

    private void checkOverDueFeeCalcDateAfterAssetRecycleDate(FinancialContract financialContract, AssetSet repaymentPlan, Date overDueFeeCalcDate) {
        if (repaymentPlan.getAssetRecycleDate() == null) {
            return;
        }
        if (!overDueFeeCalcDateAfterAssetRecycleDate(overDueFeeCalcDate, repaymentPlan.getAssetRecycleDate())) {
            return;
        }
        BigDecimal unearned_amount = ledgerBookStatHandler.get_unearned_amount(financialContract.getLedgerBookNo(), repaymentPlan.getAssetUuid());
        if (unearned_amount.compareTo(BigDecimal.ZERO) != 0) {
            throw new ApiException(REPAYMENT_PLAN_IS_NOT_RECEIVABLE);
        }
    }

    private boolean overDueFeeCalcDateAfterAssetRecycleDate(Date overDueFeeCalcDate, Date assetRecycleDate) {
        return DateUtils.compareTwoDatesOnDay(overDueFeeCalcDate, assetRecycleDate) > 0;
    }


}
