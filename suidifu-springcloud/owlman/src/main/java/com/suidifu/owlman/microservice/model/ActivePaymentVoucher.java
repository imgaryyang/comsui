package com.suidifu.owlman.microservice.model;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 主动付款凭证指令模型
 *
 * @author louguanyang
 */
@Slf4j
@Data
@ApiModel(value = "主动付款凭证指令")
@DetailFormatError(message = "凭证明细内容错误［detail］(凭证明细金额与凭证金额不匹配|凭证明细金额格式错误|amount<=0)")
@MatchOfVoucherAndDetailAmount(message = "凭证金额与明细总金额不匹配")
@RepeatedRepayScheduleNo(message = "商户还款计划编号[repayScheduleNo] 不能重复!!!")
@RequiredOnlyOneFieldNotEmpty(message = "请选填其中一种编号[uniqueId，contractNo]")
@RepeatedCurrentPeriod(message = "明细中存在相同的期数[currentPeriod]!!!")
@RepeatedRepaymentPlanNo(message = "还款计划编号重复")
public class ActivePaymentVoucher {
    /**
     * 请求唯一标识（必填）
     */
    @NotEmpty(message = "请求唯一编号[requestNo]不能为空")
    @NotNull(message = "请求唯一编号[requestNo]不能为null")
    @ApiModelProperty(value = "请求唯一编号", required = true)
    private String requestNo;
    /**
     * 交易类型(0:提交，1:撤销)（必填）
     */
    @NotNull(message = "交易类型[transactionType](0:提交，1:撤销)（必填）")
    @ApiModelProperty(value = "交易类型(0:提交，1:撤销)", required = true, allowableValues = "0,1")
    private Integer transactionType;
    /**
     * 凭证类型(5:主动付款，6:他人代付)（必填）
     */
    @NotNull(message = "凭证类型［voucherType］，不能为空！")
    @Min(value = 5, message = "凭证类型［voucherType］错误")
    @Max(value = 6, message = "凭证类型［voucherType］错误")
    @ApiModelProperty(value = "凭证类型(5:主动付款，6:他人代付)", required = true, allowableValues = "5,6")
    private Integer voucherType;
    /**
     * 收款账户号（选填）
     */
    @ApiModelProperty(value = "收款账户号")
    private String receivableAccountNo;
    /**
     * 付款银行名称（必填）
     */
    @NotEmpty(message = "付款银行名称［paymentBank］，不能为空！")
    @NotNull(message = "付款银行名称［paymentBank］，不能为null！")
    @ApiModelProperty(value = "付款银行名称", required = true)
    private String paymentBank;
    /**
     * 银行流水号（必填）
     */
    @NotEmpty(message = "付款账户流水号［bankTransactionNo］，不能为空！）")
    @NotNull(message = "付款账户流水号［bankTransactionNo］，不能为null！")
    @ApiModelProperty(value = "银行流水号", required = true)
    private String bankTransactionNo;
    /**
     * 凭证金额（必填）
     */
    @ApiModelProperty(value = "凭证金额", required = true)
    @DecimalMin(value = "0.01", message = "凭证金额［voucherAmount］，必需大于0.00！")
    private BigDecimal voucherAmount = BigDecimal.ZERO;
    /**
     * 付款银行帐户名称（必填）
     */
    @NotEmpty(message = "付款银行帐户名称［paymentName］，不能为空！")
    @NotNull(message = "付款银行帐户名称［paymentName］，不能为null！")
    @ApiModelProperty(value = "付款银行帐户名称", required = true)
    private String paymentName;
    /**
     * 付款账户号（必填）
     */
    @NotEmpty(message = "付款账户号［paymentAccountNo］，不能为空！")
    @NotNull(message = "付款账户号［paymentAccountNo］，不能为null！")
    @ApiModelProperty(value = "付款账户号", required = true)
    private String paymentAccountNo;
    /**
     * 信托产品代码（必填）
     */
    @NotEmpty(message = "信托产品代码［financialContractNo］，不能为空！")
    @NotNull(message = "信托产品代码［financialContractNo］，不能为null！")
    @ApiModelProperty(value = "信托产品代码", required = true)
    private String financialContractNo;
    /**
     * 明细（必填）
     */
    @NotEmpty(message = "明细［detail］，不能为空！")
    @NotNull(message = "明细［detail］，不能为null！")
    @ApiModelProperty(value = "明细", required = true)
    private String detail;

    @Valid
    public List<ActivePaymentVoucherDetail> getDetailModel() {
        List<ActivePaymentVoucherDetail> details = JsonUtils.parseArray(getDetail(), ActivePaymentVoucherDetail.class);
        if (CollectionUtils.isEmpty(details)) {
            return new ArrayList<>();
        }
        return details;
    }
}