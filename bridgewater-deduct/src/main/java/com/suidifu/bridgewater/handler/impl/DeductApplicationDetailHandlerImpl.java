package com.suidifu.bridgewater.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.DeductApplicationDetailHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductApplicationDetailInfoModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.api.model.deduct.IsTotal;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by qinweichao on 2017/9/18.
 */

@Component("deductAppDetailHandler")
public class DeductApplicationDetailHandlerImpl implements DeductApplicationDetailHandler {

    @Autowired
    DeductApplicationDetailService deductApplicationDetailService;



    @Override
    public void generateByRepaymentDetailList(DeductRequestModel deductRequestModel, DeductApplication deductApplication){

        List<DeductApplicationDetail> deductApplicationDetailList = new ArrayList<>();

        List<RepaymentDetail> repaymentDetailInfoList = JsonUtils.parseArray(deductRequestModel.getRepayDetailInfo(),RepaymentDetail.class);
        List<RepaymentDetail> repaymentDetails = new ArrayList<RepaymentDetail>();
        for (RepaymentDetail repaymentDetail : repaymentDetailInfoList) {
            repaymentDetail.setRepaymentDetailUuid(UUID.randomUUID().toString());
            repaymentDetails.add(repaymentDetail);
            createDeductApplicationDetail(deductApplicationDetailList, deductApplication, repaymentDetail, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE, IsTotal.DETAIL,
                    repaymentDetail.getRepaymentPrincipal(), null);

            createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_INTEREST, IsTotal.DETAIL,
                    repaymentDetail.getRepaymentInterest(), null);

            createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE, IsTotal.DETAIL,
                    repaymentDetail.getTechFee(), null);

            createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE, IsTotal.DETAIL,
                    repaymentDetail.getLoanFee(), null);

            createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE, IsTotal.DETAIL,
                    repaymentDetail.getOtherFee(), null);

            if(repaymentDetail.getOverDueFeeDetail() != null && repaymentDetail.getOverDueFeeDetail().overDueFeeDetailRule() == 1){

                createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
                        ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, IsTotal.DETAIL,
                        repaymentDetail.getOverDueFeeDetail().getPenaltyFee(), null);
                createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
                        ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, IsTotal.DETAIL,
                        repaymentDetail.getOverDueFeeDetail().getLatePenalty(), null);
                createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
                        ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, IsTotal.DETAIL,
                        repaymentDetail.getOverDueFeeDetail().getLateFee(), null);
                createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
                        ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, IsTotal.DETAIL,
                        repaymentDetail.getOverDueFeeDetail().getLateOtherCost(), null);
            }

            createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail, "", IsTotal.DETAIL,
                    repaymentDetail.getOverDueFeeDetail().getTotalOverdueFee(), ExtraChargeSpec.TOTAL_OVERDUE_FEE);

            createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
                    "",IsTotal.TOTAL, null,ExtraChargeSpec.TOTAL_RECEIVABEL_AMOUNT);

            createDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail, "", IsTotal.DETAIL,
                    repaymentDetail.getTotalAmount(),ExtraChargeSpec.TOTAL_AMOUNT);
        }

        for (DeductApplicationDetail deductApplicationDetail: deductApplicationDetailList) {
            deductApplicationDetailService.save(deductApplicationDetail);
        }

        deductRequestModel.setRepayDetailInfo(JsonUtils.toJsonString(repaymentDetails));
    }

    @Override
    public void updateDeductApplicationDetailByAsseSet(Map<String, DeductApplicationDetailInfoModel> deductApplicationDetailInfoMap) {
        if (MapUtils.isEmpty(deductApplicationDetailInfoMap)){
            return;
        }

        for (String deductApplicationDetailUuid: deductApplicationDetailInfoMap.keySet()) {
            DeductApplicationDetailInfoModel deductApplicationDetailInfoModel = deductApplicationDetailInfoMap.get(deductApplicationDetailUuid);
            List<DeductApplicationDetail> deductApplicationDetails = deductApplicationDetailService.getDeductApplicationDetailByDeductApplicationDetailUuid(deductApplicationDetailUuid);
            if (CollectionUtils.isEmpty(deductApplicationDetails) || deductApplicationDetailInfoModel == null){
                continue;
            }
            for (DeductApplicationDetail deductApplicationDetail :deductApplicationDetails) {

                deductApplicationDetail.setAssetSetUuid(deductApplicationDetailInfoModel.getAssetSetUuid());
                deductApplicationDetail.setContractUniqueId(deductApplicationDetailInfoModel.getContractUniqueId());
                deductApplicationDetail.setFinancialContractUuid(deductApplicationDetailInfoModel.getFinancialContractUuid());
                deductApplicationDetail.setRepaymentPlanCode(deductApplicationDetailInfoModel.getRepaymentPlanCode());
                if (deductApplicationDetail.getIsTotal() == IsTotal.TOTAL){
                    deductApplicationDetail.setAccountAmount(deductApplicationDetailInfoModel.getCaclAccountReceivableAmount());
                }
                deductApplicationDetailService.saveOrUpdate(deductApplicationDetail);
            }

        }

    }

    private void createDeductApplicationDetail(List<DeductApplicationDetail> deductApplicationDetailList,DeductApplication deductApplication,
                                                     RepaymentDetail repaymentDetail, String chartString, IsTotal isTotal, BigDecimal amount, String firstAccountName) {
        //金额小于等于零不生成明细记录
        if(isTotal == IsTotal.DETAIL && (null == amount || amount.compareTo(BigDecimal.ZERO) < 1)){
            return;
        }
        DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,
                repaymentDetail, isTotal, amount, repaymentDetail.getCurrentPeriod());

        if (StringUtils.isNotEmpty(chartString)){
            deductApplicationDetail.copyTAccount(ChartOfAccount.EntryBook().get(chartString));
        }

        if(StringUtils.isNotEmpty(firstAccountName)) {
            deductApplicationDetail.setFirstAccountName(firstAccountName);
        }

        deductApplicationDetailList.add(deductApplicationDetail);
    }

}
