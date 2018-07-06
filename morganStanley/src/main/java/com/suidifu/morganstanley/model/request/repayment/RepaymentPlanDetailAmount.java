package com.suidifu.morganstanley.model.request.repayment;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.utils.CheckFormatUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.HashMap;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.VOUCHER_DETAIL_AND_AMOUNT_NOT_MATCH;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.VOUCHER_ERROR_OF_DETAIL_FORMAT;

/**
 * 还款计划明细值相关字段
 * 商户付款凭证，主动付款凭证
 * Created by louguanyang on 2017/2/28.
 */
@Data
@Slf4j
public class RepaymentPlanDetailAmount {
    /**
     * 还款本金
     */
    @DecimalMin(value = "0.00", message = "还款本金［principal］，必需大于0.00")
    private BigDecimal principal = BigDecimal.ZERO;
    /**
     * 还款利息
     */
    @DecimalMin(value = "0.00", message = "还款利息［interest］，必需大于0.00")
    private BigDecimal interest = BigDecimal.ZERO;
    /**
     * 贷款服务费
     */
    @DecimalMin(value = "0.00", message = "贷款服务费［serviceCharge］，必需大于0.00")
    private BigDecimal serviceCharge = BigDecimal.ZERO;
    /**
     * 技术维护费
     */
    @DecimalMin(value = "0.00", message = "技术维护费［maintenanceCharge］，必需大于0.00")
    private BigDecimal maintenanceCharge = BigDecimal.ZERO;
    /**
     * 其他费用
     */
    @DecimalMin(value = "0.00", message = "其他费用［otherCharge］，必需大于0.00")
    private BigDecimal otherCharge = BigDecimal.ZERO;
    /**
     * 罚息
     */
    @DecimalMin(value = "0.00", message = "罚息［penaltyFee］，必需大于0.00")
    private BigDecimal penaltyFee = BigDecimal.ZERO;
    /**
     * 逾期违约金
     */
    @DecimalMin(value = "0.00", message = "逾期违约金［latePenalty］，必需大于0.00")
    private BigDecimal latePenalty = BigDecimal.ZERO;
    /**
     * 逾期服务费
     */
    @DecimalMin(value = "0.00", message = "逾期服务费［lateFee］，必需大于0.00")
    private BigDecimal lateFee = BigDecimal.ZERO;
    /**
     * 逾期其他费用
     */
    @DecimalMin(value = "0.00", message = "逾期其他费用［lateOtherCost］，必需大于0.00")
    private BigDecimal lateOtherCost = BigDecimal.ZERO;

    /**
     * 明细总金额
     */
    @DecimalMin(value = "0.00", message = "明细总金额［amount］，必需大于0.00")
    private BigDecimal amount = BigDecimal.ZERO;

    @JSONField(serialize = false)
    public boolean isValid() {
        if (BigDecimal.ZERO.compareTo(getAmount()) >= 0) {
            return false;
        }
        if (detailAmount().compareTo(BigDecimal.ZERO) > 0 && getAmount().compareTo(detailAmount()) != 0) {
            log.error("#凭证明细金额与凭证金额不匹配");
            throw new ApiException(VOUCHER_DETAIL_AND_AMOUNT_NOT_MATCH);
        }
        if (!checkDetailAmount()) {
            log.error("#凭证明细金额格式错误");
            throw new ApiException(VOUCHER_ERROR_OF_DETAIL_FORMAT);
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