package com.suidifu.owlman.microservice.model;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商户付款凭证指令模型
 *
 * @author louguanyang
 */
@Data
@ApiModel(value = "商户付款凭证")
@DetailFormatError(message = "凭证明细内容错误［detail］字段格式错误")
@RepeatedRepayScheduleNo(message = "商户还款计划编号[repayScheduleNo] 不能重复")
@RepeatedCurrentPeriod(message = "明细中的期数不可重复相同[currentPeriod]")
@RepurchaseDetailError(message = "回购凭证明细总金额与明细其他金额总和不匹配")
@RepeatedRepaymentPlanNo(message = "还款计划编号重复[repaymentPlanNo]")
@MatchOfVoucherAndDetailAmount(message = "凭证金额与明细总金额不匹配")
public class BusinessPaymentVoucher {
    /**
     * 请求唯一标识（必填）
     */
    @NotEmpty(message = "请求唯一编号［requestNo］，不能为空")
    @NotNull(message = "请求唯一编号［requestNo］，不能为null")
    @ApiModelProperty(value = "请求唯一编号", required = true)
    private String requestNo;
    /**
     * 交易类型(0:提交，1:撤销)（必填）
     */
    @Min(0)
    @Max(1)
    @ApiModelProperty(value = "交易类型(0:提交，1:撤销)", required = true, allowableValues = "0,1")
    private Integer transactionType;
    /**
     * 凭证类型(0:委托转付，1:担保补足，2:回购，3:差额划拨, 7:代偿)（必填）
     */
    @CheckVoucherType(type = {0, 1, 2, 3, 7}, message = "商户付款凭证类型［voucherType］错误")
    @ApiModelProperty(value = "凭证类型(0:委托转付,1:担保补足,2:回购,3:差额划拨,7:代偿)", required = true, allowableValues = "0,1,2,3,7")
    private Integer voucherType;
    /**
     * 凭证金额（必填）
     */
    @ApiModelProperty(value = "凭证金额", required = true)
    @DecimalMin(value = "0.00", message = "凭证金额［voucherAmount］，必需大于0.00")
    private BigDecimal voucherAmount = BigDecimal.ZERO;
    /**
     * 信托产品代码（必填）
     */
    @NotEmpty(message = "信托产品代码［financialContractNo］，不能为空")
    @NotNull(message = "信托产品代码［financialContractNo］，不能为null")
    @ApiModelProperty(value = "信托产品代码", required = true)
    private String financialContractNo;
    /**
     * 收款账户号（选填）
     */
    @ApiModelProperty(value = "收款账户号")
    private String receivableAccountNo;
    /**
     * 付款账户号（必填）
     */
    @NotEmpty(message = "付款账户号［paymentAccountNo］，不能为空")
    @NotNull(message = "付款账户号［paymentAccountNo］，不能为null")
    @ApiModelProperty(value = "付款账户号", required = true)
    private String paymentAccountNo;
    /**
     * 付款银行帐户名称（必填）
     */
    @NotEmpty(message = "付款银行帐户名称［paymentName］，不能为空")
    @NotNull(message = "付款银行帐户名称［paymentName］，不能为null")
    @ApiModelProperty(value = "付款银行帐户名称", required = true)
    private String paymentName;
    /**
     * 付款银行名称（必填）
     */
    @NotEmpty(message = "付款银行名称［paymentBank］，不能为空")
    @NotNull(message = "付款银行名称［paymentBank］，不能为null")
    @ApiModelProperty(value = "付款银行名称", required = true)
    private String paymentBank;
    /**
     * 银行流水号（必填）
     */
    @NotEmpty(message = "付款账户流水号［bankTransactionNo］，不能为空")
    @NotNull(message = "付款账户流水号［bankTransactionNo］，不能为null")
    @ApiModelProperty(value = "付款账户流水号", required = true)
    private String bankTransactionNo;
    /**
     * 明细（必填）
     */
    @NotEmpty(message = "明细［detail］，不能为空")
    @NotNull(message = "明细［detail］，不能为null")
    @ApiModelProperty(value = "明细", required = true)
    private String detail;

    @Valid
    public List<BusinessPaymentVoucherDetail> getBusinessPaymentVoucherDetails() {
        return JsonUtils.parseArray(getDetail(), BusinessPaymentVoucherDetail.class);
    }
}