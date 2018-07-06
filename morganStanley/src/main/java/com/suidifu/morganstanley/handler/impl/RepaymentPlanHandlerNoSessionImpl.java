package com.suidifu.morganstanley.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.RepaymentPlanSnapshotService;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.suidifu.morganstanley.handler.RepaymentPlanHandlerSession;
import com.suidifu.morganstanley.handler.RepaymentPlanHandlerV2;
import com.suidifu.morganstanley.model.request.MutableFee;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSetSpec;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hwr on 17-10-23.
 */
@Slf4j
@Component("RepaymentPlanHandlerNoSession")
public class RepaymentPlanHandlerNoSessionImpl implements RepaymentPlanHandlerSession {
    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Autowired
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskHandler;

    @Autowired
    private RepaymentPlanSnapshotService repaymentPlanSnapshotService;

    @Autowired
    @Qualifier("RepaymentPlanHandlerV2")
    private RepaymentPlanHandlerV2 repaymentPlanHandlerV2;

    @Override
    public void mutableFeeV2(MutableFee mutableFee, String ip, Long userId, int priority) {
        if (null == mutableFee) {
            log.info("mutableFeeModel is null");
            return;
        }

        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(mutableFee.getUniqueId(), mutableFee
                .getContractNo());

        log.info("ip:["+ip+"],userId:["+userId+"],"+mutableFee.toString());

        setRepaySchedule4Q(mutableFee, mutableFee.getFinancialProductCode());

        repaymentPlanHandlerV2.mutableFee(mutableFee, ip, userId, priority);

        sandboxData(sandboxDataSet, mutableFee);
    }

    private HashMap<String, Object> getInputMap(SandboxDataSet sandboxDataSet, MutableFee mutableFee) {
        HashMap<String, Object> inputMap = new HashMap<>();
        String financialContractUuid = sandboxDataSet.getFinancialContractUuid();
        String contractUuid = sandboxDataSet.getContractUuid();
        inputMap.put(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, financialContractUuid);
        inputMap.put(SandboxDataSetSpec.CONTRACT_UUID, contractUuid);
        String repaymentPlanNo = mutableFee.getRepaymentPlanNo();
        List<String> repaymentPlanNoList = Collections.singletonList(repaymentPlanNo);
        inputMap.put(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);

        Map<String, Object> extraParam = new HashMap<>();

        String reasonCode = mutableFee.getReasonCode();
        extraParam.put(SandboxDataSetSpec.ExtraItem.REASON_CODE, reasonCode);

        List<PaymentPlanSnapshot> repaymentPlanSnapshotSrcList = sandboxDataSet.getPaymentPlanSnapshotList();
        List<PaymentPlanSnapshot> repaymentPlanSnapshotTarList = repaymentPlanSnapshotService
                .get_all_assetSetSnapshot_list(contractUuid);

        Map<String, PaymentPlanSnapshot> srcMap = repaymentPlanSnapshotSrcList.stream()
                .collect(Collectors.toMap(PaymentPlanSnapshot::getSingleLoanContractNo, sd -> sd));

        Map<String, PaymentPlanSnapshot> tarMap = repaymentPlanSnapshotTarList.stream()
                .collect(Collectors.toMap(PaymentPlanSnapshot::getSingleLoanContractNo, sd -> sd));

        BigDecimal originalPrincipal = srcMap.get(repaymentPlanNo).getAssetPrincipalValue();
        BigDecimal currentPrincipal = tarMap.get(repaymentPlanNo).getAssetPrincipalValue();
        BigDecimal principal = currentPrincipal.subtract(originalPrincipal);
        extraParam.put(SandboxDataSetSpec.ExtraItem.PRINCIPAL, principal);

        BigDecimal originalInterest = srcMap.get(repaymentPlanNo).getAssetInterestValue();
        BigDecimal currentInterest = tarMap.get(repaymentPlanNo).getAssetInterestValue();
        BigDecimal interest = currentInterest.subtract(originalInterest);
        extraParam.put(SandboxDataSetSpec.ExtraItem.INTEREST, interest);

        BigDecimal originalRepayCharge = srcMap.get(repaymentPlanNo).getRepayCharge();
        BigDecimal currentRepayCharge = tarMap.get(repaymentPlanNo).getRepayCharge();
        BigDecimal repayCharge = currentRepayCharge.subtract(originalRepayCharge);
        extraParam.put(SandboxDataSetSpec.ExtraItem.REPAY_CHARGE, repayCharge);

        JSONObject jsonObject = new JSONObject(extraParam);
        String extraItem = JsonUtils.toJSONString(jsonObject);

        inputMap.put(SandboxDataSetSpec.EXTRA_ITEM, extraItem);

        return inputMap;
    }

    private void sandboxData(SandboxDataSet sandboxDataSet, MutableFee mutableFee) {
        String financialProductCode = mutableFee.getFinancialProductCode();
        FinancialContract financialContract = financialContractService
                .getUniqueFinancialContractBy(financialProductCode);
        String delayTaskConfigUuid = financialContractConfigurationService.getFinancialContractConfigContentContent(
                financialContract.getFinancialContractUuid(),
                FinancialContractConfigurationCode.MUTABLE_FEE_DELAY_TASK_UUID.getCode());
        DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler
                .getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);

        if (null != delayTaskServices) {
            HashMap<String, Object> inputMap = getInputMap(sandboxDataSet, mutableFee);
            inputMap.put(SandboxDataSetSpec.CONFING_UUID, delayTaskConfigUuid);
            Map<String, Object> resultMap = new HashMap<>();
            @SuppressWarnings("unused")
            boolean evaluate_result = delayTaskServices.evaluate(null, delayProcessingTaskHandler,
                    sandboxDataSetHandler, inputMap, resultMap, LogFactory.getLog(RepaymentPlanHandlerNoSessionImpl.class));
        }
    }

    private void setRepaySchedule4Q(MutableFee mutableFee, String financialProductCode) {
        String repayScheduleNoMD5 = repaymentPlanService.getRepayScheduleNoMD5(financialProductCode, mutableFee.getRepayScheduleNo(), StringUtils.EMPTY);
        mutableFee.setRepayScheduleNo(repayScheduleNoMD5);
    }
}
