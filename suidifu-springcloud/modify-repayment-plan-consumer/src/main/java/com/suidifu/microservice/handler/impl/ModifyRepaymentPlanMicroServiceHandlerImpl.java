package com.suidifu.microservice.handler.impl;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.ASSET_RECYCLE_DATE_TOO_EARLY;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.ASSET_RECYCLE_DATE_TOO_LATE;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EXSIT_PAYING_REPAYMENT_PLAN;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EXSIT_PROCESSING_OR_SUCCESS_REPAYMENT_PLAN;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.FAIL_TO_MODIFY;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.INVALID_PRINCIPAL_AMOUNT;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.WRONG_ASSET_RECYCLE_DATE;

import com.suidifu.microservice.handler.RepaymentPlanRecoverHandler;
import com.suidifu.owlman.microservice.handler.ModifyRepaymentPlanMicroServiceHandler;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec.ModifyAssetErrorCode;
import com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.ModifyAssetErrorMsgSpec;
import com.zufangbao.sun.api.model.repayment.RepaymentPlanModifyDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.contract.RepaymentPlanOperateLog;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractModifyFlag;
import com.zufangbao.sun.ledgerbook.InvalidWriteOffException;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.WriteOffReason;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.exception.DuplicatedDeductionException;
import com.zufangbao.sun.yunxin.handler.ContractHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 变更还款计划 微服务实现
 *
 * @author louguanyang at 2018/3/13 18:08
 * @mail louguanyang@hzsuidifu.com
 */
@Log4j2
@Component("modifyRepaymentPlanMicroServiceHandler")
public class ModifyRepaymentPlanMicroServiceHandlerImpl implements ModifyRepaymentPlanMicroServiceHandler {

    private static final String CODE_ERROR_MSG = "code:{}, errorMsg:{}";
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private ContractService contractService;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private RepaymentPlanHandler repaymentPlanHandler;
    @Resource
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
    @Resource
    private ContractHandler contractHandler;
    @Resource
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Resource
    private RepaymentPlanRecoverHandler repaymentPlanRecoverHandler;

    @Override
    public List<RepaymentPlanModifyDetail> modifyRepaymentPlan(
        List<RepaymentPlanModifyRequestDataModel> requestDataList, String contractUuid, int priority,
        Integer oldActiveVersionNo, Integer modifyReasonCode, String ip)
        throws GlobalRuntimeException, DuplicatedDeductionException, InvalidWriteOffException {
        log.info("modifyRepaymentPlanMicroServiceHandler#modifyRepaymentPlan start, contractUuid:{}", contractUuid);

        Contract contract = contractService.getContract(contractUuid);
        if (null == contract) {
            log.error("modifyRepaymentPlan fail, contract is null, contractUuid:{}", contractUuid);
            return Collections.emptyList();
        }

        FinancialContract financialContract = getFinancialContract(contract);
        // 剩余未还的还款计划（还款计划分为几期，这里表示后面几期还没有偿还的还款计划）
        List<AssetSet> candidateAssetList = checkParamsReturnCandidateAssetList(financialContract, contract,
            requestDataList);

        // 变更还款计划时如与原计划一致，不再报错而是跳出执行返回成功
        if (checkoutAssetFingerPrint(requestDataList, candidateAssetList)) {
            log.warn("###变更还款计划时如与原计划一致###");
            return unchangedRepaymentPlanModifyDetails(requestDataList, candidateAssetList);
        }

        // 作废变更之前的还款计划
        writeOffOldRepaymentPlan(financialContract.getContractNo(), candidateAssetList);

        int newVersion = updateContractActiveVersionNo(contractUuid, oldActiveVersionNo);
        contract.setActiveVersionNo(newVersion);

        int currentPeriod = getCurrentPeriod(candidateAssetList);
        List<AssetSet> open = saveRequestAssetList(contract, financialContract.getContractNo(), requestDataList,
            currentPeriod, modifyReasonCode, newVersion);

        saveToLedgerBook(contract, financialContract, open);
        syncDataToYnTrust(contract, open, oldActiveVersionNo, modifyReasonCode);
        saveContractLog(contract, candidateAssetList, open, ip);
        //预收转实收
        repaymentPlanRecoverHandler.recover_received_in_advance(open, financialContract.getFinancialContractUuid());
        return RepaymentPlanModifyDetail.covert(requestDataList);
    }

    /**
     * 作废变更之前的还款计划
     * @param financialContractNo   信托产品代码
     * @param candidateAssetList    允许变更的还款计划
     */
    private void writeOffOldRepaymentPlan(String financialContractNo, List<AssetSet> candidateAssetList) {
        WriteOffReason modifyRepaymentPlan = WriteOffReason.MODIFY_REPAYMENT_PLAN;
        repaymentPlanService.wirteOffRepaymentPlan(candidateAssetList, modifyRepaymentPlan, financialContractNo);
    }

    /**
     * 新增还款计划，并返回新的还款计划集合
     * @param contract 贷款合同
     * @param financialContractNo 信托产品代码
     * @param requestDataList   请求变更数据
     * @param currentPeriod 当前最新期数
     * @param modifyReasonCode  变更原因
     * @param newVersion    最新版本号
     * @return
     */
    private List<AssetSet> saveRequestAssetList(Contract contract, String financialContractNo,
        List<RepaymentPlanModifyRequestDataModel> requestDataList, int currentPeriod, Integer modifyReasonCode,
        int newVersion) {
        return repaymentPlanHandler.saveRequestAssetList(requestDataList, contract, currentPeriod, newVersion, modifyReasonCode, financialContractNo);
    }

    /**
     * 获取还款计划最新的开始期数
     *
     * @param candidateAssetList 允许变更的还款计划
     * @return 最新的开始期数
     */
    private int getCurrentPeriod(List<AssetSet> candidateAssetList) {
        return candidateAssetList.get(0).getCurrentPeriod();
    }

    private void saveToLedgerBook(Contract contract, FinancialContract financialContract, List<AssetSet> open) {
        try {
            // 将变更的信息存入ledgerbook中
            repaymentPlanHandler.recordLedgerBookAndValueAssetAndCreateOrder(financialContract, contract, open);
        } catch (Exception e) {
            log.error(" book loan asset occur error. error msg ", e);
        }
    }

    /**
     * 获取信托合同
     *
     * @param contract 贷款合同
     * @return 信托合同
     */
    private FinancialContract getFinancialContract(Contract contract) {
        String financialContractUuid = contract.getFinancialContractUuid();
        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
        if (financialContract != null) {
            return financialContract;
        }
        int code = ModifyAssetErrorCode.FINANCIAL_CONTRACT_NOT_EXIST;
        String errorMsg = ModifyAssetErrorMsgSpec.FINANCIAL_CONTRACT_NOT_EXIST;
        log.error(CODE_ERROR_MSG, code, errorMsg);
        throw new GlobalRuntimeException(code, errorMsg);
    }

    /**
     * 保存贷款合同日志
     * @param contract 贷款合同
     * @param candidateAssetList 允许变更的还款计划
     * @param open  已变更的还款计划
     * @param ip    请求ip
     */
    private void saveContractLog(Contract contract, List<AssetSet> candidateAssetList, List<AssetSet> open, String ip) {
        contractHandler.addRepaymentPlanOperateLog(contract, RepaymentPlanOperateLog.MODIFY_REPAYMENT_PLAN, open,
            candidateAssetList, null, ip, null);
    }

    /**
     * 同步数据到云信数据中心
     * @param contract 贷款合同
     * @param open  已变更的还款计划
     * @param oldActiveVersionNo    还款计划原先的版本号
     * @param modifyReasonCode  变更原因
     */
    private void syncDataToYnTrust(Contract contract, List<AssetSet> open, Integer oldActiveVersionNo, Integer modifyReasonCode) {
        BusinessLogsModel businessLogsModel = new BusinessLogsModel(OperateMode.CHANGE, modifyReasonCode, null,
            oldActiveVersionNo);
        repaymentPlanHandler.saveBusinessLogs(contract, open, businessLogsModel);
    }

    /**
     * 获取允许变更的最早日期
     *
     * @param financialContract 信托合同
     * @return 允许变更的最早日期，默认明天
     */
    private Date getStartDate(FinancialContract financialContract) {
        String financialContractUuid = financialContract.getUuid();
        String code = FinancialContractConfigurationCode.ALLOW_MODIFY_REPAYMENT_PLAN.getCode();
        String modifyFlagContent = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, code);

        Date today = DateUtils.getToday();
        if (StringUtils.isBlank(modifyFlagContent)) {
            if (financialContract.isUnusualModifyFlag()) {
                return today;
            }
            return DateUtils.addDays(today, 1);
        }
        if (modifyFlagContent.equals(Integer.toString(FinancialContractModifyFlag.ABNORMAL.ordinal()))) {
            //该贷款合同允许变更当日的还款计划
            return today;
        }
        if (modifyFlagContent.equals(Integer.toString(FinancialContractModifyFlag.GRACE_DAY.ordinal()))) {
            //该贷款合同允许变更宽限日中的还款计划
            return DateUtils.addDays(today, financialContract.getAdvaRepaymentTerm() * (-1));
        }
        return DateUtils.addDays(today, 1);
    }

    /**
     * 校验参数 并 返回 剩余未还的还款计划
     * @param financialContract 信托合同
     * @param contract 贷款合同
     * @param requestDataList 请求数据
     */
    private List<AssetSet> checkParamsReturnCandidateAssetList(FinancialContract financialContract,
        Contract contract, List<RepaymentPlanModifyRequestDataModel> requestDataList) {

        Date startDate = getStartDate(financialContract);
        checkPlanBeginEndDate(contract, requestDataList, startDate);

        checkPrincipal(contract, requestDataList, startDate);

        String contractUuid = contract.getUuid();
        List<AssetSet> candidateAssetList = repaymentPlanService.getAllAssetListBy(contractUuid, startDate);

        checkCandidateAssetList(contractUuid, candidateAssetList);

        return candidateAssetList;
    }

    /**
     * 校验 允许变更的剩余未还的还款计划
     * @param contractUuid  贷款合同UUID
     * @param candidateAssetList   允许变更的剩余未还的还款计划
     */
    private void checkCandidateAssetList(String contractUuid, List<AssetSet> candidateAssetList) {
        if (CollectionUtils.isEmpty(candidateAssetList)) {
            int code = FAIL_TO_MODIFY.getCode();
            String errorMsg = FAIL_TO_MODIFY.getMessage();
            log.error(CODE_ERROR_MSG, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
        Integer maxClearPeriod = repaymentPlanService.getMaxClearPeriod(contractUuid);

        AssetSet repaymentPlan;
        for (int index = 0, size = candidateAssetList.size(); index < size; index++) {
            repaymentPlan = candidateAssetList.get(0);
            if (repaymentPlan == null) {
                continue;
            }
            checkHasDeducting(repaymentPlan);
            checkExistCrossPeriodClear(repaymentPlan, maxClearPeriod);
        }
    }

    /**
     * 校验是否存在跨期核销
     * @param repaymentPlan 还款计划
     * @param maxClearPeriod 最近还款期数号
     */
    private void checkExistCrossPeriodClear(AssetSet repaymentPlan, Integer maxClearPeriod) {
        if (repaymentPlan == null) {
            throw new IllegalArgumentException("repaymentPlan is null");
        }
        if (0 == maxClearPeriod) {
            return;
        }
        if (maxClearPeriod > repaymentPlan.getCurrentPeriod()) {
            int code = ModifyAssetErrorCode.EXIST_INTERTEMPORAL;
            String errorMsg = ModifyAssetErrorMsgSpec.EXIST_INTERTEMPORAL;
            log.error(CODE_ERROR_MSG, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
    }

    /**
     * 校验还款计划是否正在处理中
     * @param repaymentPlan  还款计划
     */
    private void checkHasDeducting(AssetSet repaymentPlan) {
        if (repaymentPlan == null) {
            throw new IllegalArgumentException("repaymentPlan is null");
        }
        if (!StringUtils.equals(AssetSet.EMPTY_UUID, repaymentPlan.getActiveDeductApplicationUuid())) {
            int code = EXSIT_PROCESSING_OR_SUCCESS_REPAYMENT_PLAN.getCode();
            String errorMsg = EXSIT_PROCESSING_OR_SUCCESS_REPAYMENT_PLAN.getMessage();
            log.error(CODE_ERROR_MSG, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
        boolean inPayingStatus = repaymentPlanExtraDataService.isInPayingStatus(repaymentPlan.getAssetUuid());
        if (inPayingStatus) {
            int code = EXSIT_PAYING_REPAYMENT_PLAN.getCode();
            String errorMsg = EXSIT_PAYING_REPAYMENT_PLAN.getMessage();
            log.error(CODE_ERROR_MSG, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
    }

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 校验本金是否正确
     */
    private void checkPrincipal(Contract contract, List<RepaymentPlanModifyRequestDataModel> requestDataList,
        Date startDate) {
        BigDecimal planPrincipalAmount = requestDataList.stream().filter(Objects::nonNull)
            .map(RepaymentPlanModifyRequestDataModel::getAssetPrincipal).reduce(ZERO, BigDecimal::add);
        //查询修改日期及以后的剩余未还本金总额
        BigDecimal principalAmountOfContract = repaymentPlanService
            .get_the_outstanding_principal_amount_of_contract(contract, startDate);
        //变更的还款计划的本金总额与剩余本金总额是否相同，若不同，则抛出异常
        if (planPrincipalAmount.compareTo(principalAmountOfContract) != 0) {
            int code = INVALID_PRINCIPAL_AMOUNT.getCode();
            String errorMsg = INVALID_PRINCIPAL_AMOUNT.getMessage();
            log.error("code:{}, errorMsg:{}, 请求本金总额:{}, 剩余未还本金总额:{}", code, errorMsg, planPrincipalAmount,
                principalAmountOfContract);
            throw new GlobalRuntimeException(code, errorMsg);
        }
    }

    /**
     * 幂等校验 校验还款计划变更前后是否相同
     */
    private boolean checkoutAssetFingerPrint(List<RepaymentPlanModifyRequestDataModel> requestDataList,
        List<AssetSet> candidateAssetList) {
        int requestSize = requestDataList.size();
        if (requestSize != candidateAssetList.size()) {
            return false;
        }
        AssetSet assetSet;
        RepaymentPlanModifyRequestDataModel model;
        for (int index = 0; index < requestSize; index++) {
            model = requestDataList.get(index);
            assetSet = candidateAssetList.get(index);
            if (!StringUtils.equals(model.assetFingerPrint(), assetSet.getAssetFingerPrint())) {
                return false;
            }
            if (!StringUtils.equals(model.assetExtraFeeFingerPrint(), assetSet.getAssetExtraFeeFingerPrint())) {
                return false;
            }
        }
        log.warn(ModifyAssetErrorCode.REPEATED_SUBMIT + ":" + ModifyAssetErrorMsgSpec.REPEATED_SUBMIT);
        return true;
    }

    /**
     * 校验还款计划的最小日期和最大日期
     */
    private void checkPlanBeginEndDate(Contract contract, List<RepaymentPlanModifyRequestDataModel> requestDataList,
        Date startDate) {
        Date contractBeginDate = contract.getBeginDate();
        Date customerContractEndDate = getCustomerContractEndDate(contract);

        RepaymentPlanModifyRequestDataModel model;
        Date planDate;
        for (int index = 0, size = requestDataList.size() ; index < size; index++) {
            model = requestDataList.get(index);
            planDate = model.getDate();

            checkPlanBeginDate(contractBeginDate, planDate);
            checkPlanEndDate(customerContractEndDate, planDate);
            checkStartDate(startDate, planDate);
        }
    }

    /**
     * 校验还款计划日 跟允许变更起始日
     * @param startDate 允许变更起始日
     * @param planDate  还款计划日
     */
    private void checkStartDate(Date startDate, Date planDate) {
        if (startDate.after(planDate)) {
            int code = WRONG_ASSET_RECYCLE_DATE.getCode();
            String errorMsg = WRONG_ASSET_RECYCLE_DATE.getMessage();
            log.error(CODE_ERROR_MSG, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
    }

    /**
     * 校验还款计划日 跟合同截止日
     *
     * 变更计划还款日期在贷款合同终止日期两个月内，否则抛出异常
     *
     * @param customerEndDay 自定义合同截止日
     * @param planDate  还款计划日
     */
    private void checkPlanEndDate(Date customerEndDay, Date planDate) {
        if (customerEndDay == null) {
            return;
        }
        if (planDate.after(customerEndDay)) {
            int code = ASSET_RECYCLE_DATE_TOO_LATE.getCode();
            String errorMsg = ASSET_RECYCLE_DATE_TOO_LATE.getMessage();
            log.error(CODE_ERROR_MSG, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
    }

    private void checkPlanBeginDate(Date contractBeginDate, Date planDate) {
        // 变更计划还款日期不能早于贷款合同开始日期，否则抛出异常
        if (planDate.before(contractBeginDate)) {
            int code = ASSET_RECYCLE_DATE_TOO_EARLY.getCode();
            String errorMsg = ASSET_RECYCLE_DATE_TOO_EARLY.getMessage();
            log.error(CODE_ERROR_MSG, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
    }

    private Date getCustomerContractEndDate(Contract contract) {
        Date contractEndDate = contract.getEndDate();
        if (contractEndDate == null) {
            log.warn("contractEndDate is null, contractUuid:{}", contract.getUuid());
            return null;
        }
        //2017-05-04 放开限制，允许最后一期还款计划应还日晚于贷款合同截止日
        return DateUtils.addDays(contractEndDate, 108);
    }

    private List<RepaymentPlanModifyDetail> unchangedRepaymentPlanModifyDetails(
        List<RepaymentPlanModifyRequestDataModel> requestDataList, List<AssetSet> candidateAssetList) {

        List<RepaymentPlanModifyDetail> detailList = new ArrayList<>();
        RepaymentPlanModifyDetail modifyDetail;
        AssetSet assetSet;
        RepaymentPlanModifyRequestDataModel reqData;
        for (int index = 0, size = requestDataList.size() ; index < size; index++) {
            reqData = requestDataList.get(index);
            assetSet = candidateAssetList.get(index);
            modifyDetail = new RepaymentPlanModifyDetail();
            modifyDetail.setPlanRepaymentDate(reqData.getAssetRecycleDate());
            modifyDetail.setAssetPrincipal(reqData.getAssetPrincipal());
            modifyDetail.setAssetInterest(reqData.getAssetInterest());
            modifyDetail.setServiceCharge(reqData.getServiceCharge());
            modifyDetail.setMaintenanceCharge(reqData.getMaintenanceCharge());
            modifyDetail.setOtherCharge(reqData.getOtherCharge());
            modifyDetail.setCurrentPeriod(assetSet.getCurrentPeriod());
            modifyDetail.setRepaymentNumber(assetSet.getSingleLoanContractNo());
            modifyDetail.setRepayScheduleNo(assetSet.getRepayScheduleNo());
            detailList.add(modifyDetail);
        }
        return detailList;
    }

    /**
     * 更新贷款最新合同版本号，若其他接口已调用过该贷款合同，则贷款合同会更新失败并抛出异常
     * @param contractUuid  贷款合同UUID
     * @param oldActiveVersionNo 贷款合同原版本号
     * @return 最新版本号
     */
    private int updateContractActiveVersionNo(String contractUuid, Integer oldActiveVersionNo) {
        // 产生新的版本号
        Integer newVersionNo = java.util.UUID.randomUUID().hashCode();
        // 更新贷款合同版本号
        contractService.updateContractActiveVersionNo(contractUuid, oldActiveVersionNo, newVersionNo);
        // 更新后的版本号
        Integer updatedVersionNo = contractService.getActiveVersionNo(contractUuid);
        // 更新后的版本号与预定的新版本号是否相同，若不同，则更新失败并抛出异常
        if (updatedVersionNo == null || !newVersionNo.equals(updatedVersionNo)) {
            int code = ModifyAssetErrorCode.REQUEST_FREQUENT;
            String errorMsg = ModifyAssetErrorMsgSpec.REQUEST_FREQUENT;
            log.error("contractUuid:{} change version failed, code:{}, errorMsg:{}", contractUuid, code, errorMsg);
            throw new GlobalRuntimeException(code, errorMsg);
        }
        return updatedVersionNo;
    }

}
