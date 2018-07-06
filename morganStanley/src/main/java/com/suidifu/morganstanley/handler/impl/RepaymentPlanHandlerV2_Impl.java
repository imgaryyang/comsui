package com.suidifu.morganstanley.handler.impl;

import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.morganstanley.configuration.bean.weifang.WeiFangProperties;
import com.suidifu.morganstanley.handler.RepaymentPlanHandlerV2;
import com.suidifu.morganstanley.model.request.CheckMutableFee;
import com.suidifu.morganstanley.model.request.MutableFee;
import com.suidifu.morganstanley.service.SystemOperateLogService;
import com.zufangbao.gluon.exception.ContractNotIncludedRepaymentPlanException;
import com.zufangbao.gluon.exception.DateFormatException;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.exception.RepaymentPlanNotExistException;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentPlanHandlerV2_0;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by hwr on 17-10-23.
 */
@Component("RepaymentPlanHandlerV2")
public class RepaymentPlanHandlerV2_Impl implements RepaymentPlanHandlerV2 {

    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;

    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private RepaymentPlanHandlerV2_0 repaymentPlanHandlerV2_0;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractApiHandler contractApiHandler;

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    private WeiFangProperties weiFangProperties;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;
    @Override
    public void mutableFee(MutableFee mutableFee, String ip, Long userId, int priority) {
        Contract contract = contractService.getContractBy(mutableFee.getUniqueId(), mutableFee.getContractNo());
        FinancialContract financialContract = contractApiHandler.checkAndReturnFinancialContract(mutableFee.getFinancialProductCode(), contract);
        repaymentPlanHandlerV2_0.checkSupportPayForGo(financialContract);
        String contractUuid = contract.getUuid();
        AssetSet repaymentPlan = mutableFeeCheckRepaymentPlan(contractUuid, mutableFee.getRepayScheduleNo(), mutableFee.getRepaymentPlanNo(),
                mutableFee.getCurrentPeriod());
        CheckMutableFee.checkInterestAndPrincipal(mutableFee, weiFangProperties.isEnable(), sandboxDataSetHandler,
                productCategoryCacheHandler, repaymentPlan.getAssetPrincipalValue());
        // 回传数据保证逻辑完整性
        mutableFee.setRepaymentPlanNo(repaymentPlan.getSingleLoanContractNo());
        mutableFee.setRepayScheduleNo(repaymentPlan.getOuterRepaymentPlanNo());
        String assetUuid = repaymentPlan.getAssetUuid();
        String ledgerBookNo = financialContract.getLedgerBookNo();
        List<MutableFeeDetail> feeDetailList = mutableFee.getDetailList();
        Map<String, String> mutableFeeBaseInfo = getBaseInfoMsg(mutableFee);
        repaymentPlanHandlerV2_0.mutableFeeMq( contractUuid, priority, mutableFeeBaseInfo, feeDetailList, ledgerBookNo, assetUuid,ip
        );

        BusinessLogsModel model = new BusinessLogsModel(OperateMode.UPDATE, null, null , null);
        repaymentPlanHandler.saveBusinessLogs(contract, Collections.singletonList(repaymentPlan),model);
        systemOperateLogService.saveMutableFeeSystemLog(assetUuid, mutableFee, ip, userId);
    }

    private AssetSet mutableFeeCheckRepaymentPlan(String contractUuid, String repayScheduleNo, String repaymentPlanNo, Integer currentPeriod) {
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByConditions(repayScheduleNo, repaymentPlanNo, contractUuid, currentPeriod);
        if (null == assetSet || assetSet.getActiveStatus() != AssetSetActiveStatus.OPEN) {
            throw new RepaymentPlanNotExistException();
        }

        String cUuid = assetSet.getContractUuid();
        if (!contractUuid.equals(cUuid)) {
            throw new ContractNotIncludedRepaymentPlanException();
        }
        return assetSet;
    }

    private Map<String, String> getBaseInfoMsg(MutableFee mutableFee) {
        Map<String, String> result = new HashMap<>();

        String approvedTime = null == mutableFee.getApprovedTime() ? StringUtils.EMPTY
                : mutableFee.getApprovedTime();

        if (!approvedTime.isEmpty() && !isValidTime(approvedTime)) {
            throw new DateFormatException();
        }

        String approver = null == mutableFee.getApprover() ? StringUtils.EMPTY : mutableFee.getApprover();
        String comment = null == mutableFee.getComment() ? StringUtils.EMPTY : mutableFee.getComment();
        result.put("reasonCode", mutableFee.getReasonCode());
        result.put("approvedTime", approvedTime);
        result.put("approver", approver);
        result.put("comment", comment);
        return result;
    }

    private boolean isValidTime(String time) {
        Date date = DateUtils.parseDate(time, DateUtils.DATE_FORMAT);
        if (null == date) {
            return false;
        }
        Date today = DateUtils.asDay(new Date());
        return !date.after(today);
    }
}
