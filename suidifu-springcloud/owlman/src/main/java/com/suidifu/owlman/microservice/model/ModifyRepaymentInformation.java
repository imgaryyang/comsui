package com.suidifu.owlman.microservice.model;

import com.suidifu.owlman.microservice.annotation.RequiredOnlyOneFieldNotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "变更还款信息")
@RequiredOnlyOneFieldNotEmpty(message = "请选填其中一种编号[uniqueId，contractNo]")
public class ModifyRepaymentInformation {
    @NotEmpty(message = "请求唯一编号［requestNo］，不能为空")
    @NotNull(message = "请求唯一编号［requestNo］，不能为null")
    @ApiModelProperty(value = "请求唯一编号", required = true)
    private String requestNo;

    @ApiModelProperty(value = "贷款合同唯一标识（二选一）")
    private String uniqueId;

    @ApiModelProperty(value = "贷款合同编号（二选一）")
    private String contractNo;

    @ApiModelProperty(value = "银行账户名")
    private String payerName;

    @NotEmpty(message = "请必填编号［bankCode］，不能为空")
    @NotNull(message = "请必填编号［bankCode］，不能为null")
    @ApiModelProperty(value = "银行代码", required = true)
    private String bankCode;

    @NotEmpty(message = "请必填编号［bankAccount］，不能为空")
    @NotNull(message = "请必填编号［bankAccount］，不能为null")
    @ApiModelProperty(value = "银行账号", required = true)
    private String bankAccount;

    @NotEmpty(message = "请必填编号［bankName］，不能为空")
    @NotNull(message = "请必填编号［bankName］，不能为null")
    @ApiModelProperty(value = "开户行名称", required = true)
    private String bankName;

    @NotEmpty(message = "请必填编号［bankProvince］，不能为空")
    @NotNull(message = "请必填编号［bankProvince］，不能为null")
    @ApiModelProperty(value = "开户行所在省", required = true)
    private String bankProvince;

    @NotEmpty(message = "请必填编号［bankCity］，不能为空")
    @NotNull(message = "请必填编号［bankCity］，不能为null")
    @ApiModelProperty(value = "开户行所在市", required = true)
    private String bankCity;

    @ApiModelProperty(value = "还款通道")
    private String repaymentChannel;

    @ApiModelProperty(hidden = true)
    private String idCardNum;

    @ApiModelProperty(hidden = true)
    private String mobile;
}