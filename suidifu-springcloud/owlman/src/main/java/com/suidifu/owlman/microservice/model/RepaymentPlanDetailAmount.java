package com.suidifu.owlman.microservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec;
import com.zufangbao.sun.utils.CheckFormatUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 还款计划明细值相关字段
 * 商户付款凭证，主动付款凭证
 *
 * @author louguanyang on 2017/2/28.
 */
@Data
@CommonsLog
public class RepaymentPlanDetailAmount {
    /**
     * 还款本金
     */
    private BigDecimal principal = BigDecimal.ZERO;
    /**
     * 还款利息
     */
    private BigDecimal interest = BigDecimal.ZERO;
    /**
     * 贷款服务费
     */
    private BigDecimal serviceCharge = BigDecimal.ZERO;
    /**
     * 技术维护费
     */
    private BigDecimal maintenanceCharge = BigDecimal.ZERO;
    /**
     * 其他费用
     */
    private BigDecimal otherCharge = BigDecimal.ZERO;
    /**
     * 罚息
     */
    private BigDecimal penaltyFee = BigDecimal.ZERO;
    /**
     * 逾期违约金
     */
    private BigDecimal latePenalty = BigDecimal.ZERO;
    /**
     * 逾期服务费
     */
    private BigDecimal lateFee = BigDecimal.ZERO;
    /**
     * 逾期其他费用
     */
    private BigDecimal lateOtherCost = BigDecimal.ZERO;

    /**
     * 明细总金额
     */
    private BigDecimal amount = BigDecimal.ZERO;

    @JSONField(serialize = false)
    public boolean isValid() throws GlobalRuntimeException {
        if (BigDecimal.ZERO.compareTo(getAmount()) >= 0) {
            return false;
        }
        if (detailAmount().compareTo(BigDecimal.ZERO) > 0 && getAmount().compareTo(detailAmount()) != 0) {
            log.error("#凭证明细金额与凭证金额不匹配");
            throw new GlobalRuntimeException(VoucherErrorMsgSpec.VOUCHER_DETAIL_AND_AMOUNT_NOT_MATCH);
        }
        if (!checkDetailAmount()) {
            log.error("#凭证明细金额格式错误");
            throw new GlobalRuntimeException(VoucherErrorMsgSpec.VOUCHER_ERROR_OF_DETAIL_FORMAT);
        }
        //有明细金额，但明细累加值与明细金额不一致，校验失败
        return true;
    }

    @JSONField(serialize = false)
    public BigDecimal detailAmount() {
        try {
            return BigDecimal.ZERO.add(getPrincipal()).add(getInterest()).add(getServiceCharge()).add(getMaintenanceCharge()).add(getOtherCharge())
                    .add(getPenaltyFee()).add(getLatePenalty()).add(getLateFee()).add(getLateOtherCost());
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @JSONField(serialize = false)
    private boolean checkDetailAmount() {
        try {
            return CheckFormatUtils.checkRMBCurrencyBigDecimal(getPrincipal()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(getInterest()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(getServiceCharge()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(getMaintenanceCharge()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(getOtherCharge()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(getPenaltyFee()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(getLatePenalty()) && CheckFormatUtils.checkRMBCurrencyBigDecimal(getLateFee()) &&
                    CheckFormatUtils.checkRMBCurrencyBigDecimal(getLateOtherCost());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @JSONField(serialize = false)
    public HashMap<String, BigDecimal> getDetailAmountMap() {

        HashMap<String, BigDecimal> detailAmountMap = new HashMap<>();
        if (detailAmount().compareTo(BigDecimal.ZERO) == 0) {
            return detailAmountMap;
        }
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, getPrincipal());
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, getInterest());
        detailAmountMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY, getServiceCharge());
        detailAmountMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY, getMaintenanceCharge());
        detailAmountMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY, getOtherCharge());
        detailAmountMap.put(ExtraChargeSpec.PENALTY_KEY, getPenaltyFee());
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, getLatePenalty());
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, getLateFee());
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, getLateOtherCost());

        return detailAmountMap;
    }

    @JSONField(serialize = false)
    private HashMap<String, String> getDetailNameMap() {

        HashMap<String, String> detailAmountMap = new HashMap<>();
        if (detailAmount().compareTo(BigDecimal.ZERO) == 0) {
            return detailAmountMap;
        }
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, "还款本金");
        detailAmountMap.put(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, "还款利息");
        detailAmountMap.put(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY, "贷款服务费");
        detailAmountMap.put(ExtraChargeSpec.LOAN_TECH_FEE_KEY, "技术维护费");
        detailAmountMap.put(ExtraChargeSpec.LOAN_OTHER_FEE_KEY, "其他费用");
        detailAmountMap.put(ExtraChargeSpec.PENALTY_KEY, "罚息");
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, "逾期违约金");
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, "逾期服务费");
        detailAmountMap.put(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, "逾期其他费用");

        return detailAmountMap;
    }

    @JSONField(serialize = false)
    public String getDetailAmountName(String key) {
        try {
            return getDetailNameMap().getOrDefault(key, StringUtils.EMPTY);
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }
}