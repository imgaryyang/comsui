package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.utils.validation.DateCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 第三方凭证明细
 *
 * @author wangjianhua
 */
@Data
@NoArgsConstructor
public class ThirdPartVoucherDetail {
    /**
     * 凭证编号（必填）
     */
    @NotEmpty(message = "凭证编号[voucherNo]不能为空")
    private String voucherNo;

    /**
     * 交易流水号（必填）
     */
    private String bankTransactionNo;

    /**
     * 交易请求号（必填）
     */
    @NotEmpty(message = "交易请求号[transactionRequestNo]不能为空")
    private String transactionRequestNo;

    // 批次号，由transactionRequestNo中;前的部分获取(如果有)
    private String batchNo;

    /**
     * 交易发起时间（必填）
     */
    @NotEmpty(message = "交易发起时间[transactionTime]不能为空")
    @DateCheck(format = "yyyy-MM-dd HH:mm:ss", message = "交易发起时间,格式有误")
    private String transactionTime;

    /**
     * 交易网关（必填）
     * 0广银联
     * 1宝付
     * 2民生代扣
     * 3新浪支付
     * 4中金支付
     */
    @Min(value = 0, message = "交易网关［voucherType］错误")
    @Max(value = 4, message = "交易网关［voucherType］错误")
    @NotNull(message = "交易网关[transactionGateway]不能为空")
    @ApiModelProperty(value = "交易网关(0:广银联,1:宝付,2:民生代扣,3:新浪支付,4:中金支付)", required = true, allowableValues = "0,1,2,3,4")
    private int transactionGateway;

    /**
     * 交易币种（选填）
     */
    private int currency = 0;

    /**
     * 交易金额 必填()
     */
    @NotNull(message = "交易金额［amount]不能为空")
    @DecimalMin(value = "0.00", message = "交易金额［amount］，必需大于等于0.00")
    private BigDecimal amount;

    /**
     * 交易完成时间（选填）
     */
    private String completeTime;

    /**
     * 贷款合同唯一编号（必填）
     */
    @NotEmpty(message = "贷款合同唯一编号[contractUniqueId]不能为空")
    private String contractUniqueId;

    /**
     * 收款银行帐户号（选填）
     */
    private String receivableAccountNo;

    /**
     * 来往机构（选填）
     */
    private String paymentBank;

    /**
     * 付款帐户人名称（选填）
     */
    private String paymentName;

    /**
     * 付款帐户号（选填）
     */
    private String paymentAccountNo;

    /**
     * 客户姓名（选填）
     */
    private String customerName;

    /**
     * 身份证号码（选填）
     */
    private String customerIdNo;

    /**
     * 备注（选填）
     */
    private String comment;

    /**
     * 还款明细（必填）
     */
    @Valid
    @NotEmpty(message = "第三方付款凭证还款明细[repayDetailList]不能为空")
    private List<ThirdPartVoucherRepayDetail> repayDetailList;

    /**
     * 创建时间
     *
     * @return
     */
    private Date createTime = new Date();

    private String versionNo;

    public void setTransactionRequestNo(String transactionRequestNo) {
        if (StringUtils.isNotBlank(transactionRequestNo)) {
            String[] transactionRequestNoArr = transactionRequestNo.split(";");
            if (transactionRequestNoArr.length >= 2) {
                this.batchNo = transactionRequestNoArr[0];
                this.transactionRequestNo = transactionRequestNoArr[1];
                return;
            }
        }
        this.transactionRequestNo = transactionRequestNo;
    }
}