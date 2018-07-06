package com.suidifu.owlman.microservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.validation.DateCheck;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户还款凭证明细
 *
 * @author louguanyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@RequiredOnlyOneFieldNotEmpty(message = "商户还款计划编号[repayScheduleNo] 还款计划编号[repaymentPlanNo] 期数[currentPeriod]  至少一个不为空")
public class BusinessPaymentVoucherDetail extends RepaymentPlanDetailAmount {
    /**
     * 贷款合同唯一标识（必填）
     */
    @NotEmpty(message = "贷款合同唯一标识［uniqueId］，不能为空")
    @NotNull(message = "贷款合同唯一标识［uniqueId］，不能为null")
    private String uniqueId;
    /**
     * 还款计划编号
     */
    private String repaymentPlanNo;
    /**
     * 还款金额（必填）
     */
    @DecimalMin(value = "0.01", message = "还款金额［amount］，必需大于0.00")
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 付款人（0:贷款人，1:商户垫付）
     */
    private Integer payer = 0;

    /**
     * 设定还款日期
     */
    @DateCheck(format = DateUtils.LONG_DATE_FORMAT, allowNull = true, message = "设定还款日期 格式有误")
//    @TransactionDateError(message = "设定还款日期 格式有误")
    private String transactionTime;

    /**
     * 商户还款编号  (选填)
     */
    private String repayScheduleNo;
    /**
     * 期数(选填)
     */
    private Integer currentPeriod;

    public Date getTransactionDate() {
        if (StringUtils.equalsIngoreNull(transactionTime, DateUtils.DATE_FORMAT_0001_01_01)) {
            return null;
        }
        if (StringUtils.isNotEmpty(transactionTime)) {
            return DateUtils.parseDate(transactionTime, DateUtils.LONG_DATE_FORMAT);
        }
        return null;
    }

    @JSONField(serialize = false)
    public boolean isValid(boolean repaymentPlanNotNullFlag) {
        return super.isValid() && StringUtils.isNotEmpty(this.getUniqueId());
    }

    @JSONField(serialize = false)
    public VoucherPayer getVoucherPayer() {
        VoucherPayer voucherPayer = VoucherPayer.fromValue(this.getPayer());
        return voucherPayer != null ? voucherPayer : VoucherPayer.LOANER;
    }

    @JSONField(serialize = false)
    public boolean isNotEmptyrepayScheduleNoAndrepaymentPlanNoAndrepaymentNumber() {
        return StringUtils.isEmpty(this.repayScheduleNo) && StringUtils.isEmpty(this.repaymentPlanNo) && (currentPeriod == null);
    }

    @Override
    public String toString() {
        return "BusinessPaymentVoucherDetail [uniqueId=" + uniqueId + ", repaymentPlanNo="
                + repaymentPlanNo
                + ", amount=" + amount + ", payer=" + payer + ", transactionTime=" + transactionTime
                + ", repayScheduleNo=" + repayScheduleNo + ", currentPeriod=" + currentPeriod + "]";
    }
}