package com.suidifu.dowjones.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 21:36 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@ApiModel(value = "指纹表")
public class CashFingerPrinter {
    @ApiModelProperty(value = "银行卡", required = true)
    private String bankCard;
    @ApiModelProperty(value = "指纹", required = true)
    private String fingerPrinter;
    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;
    @ApiModelProperty(value = "总额", required = true)
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "本金", required = true)
    private BigDecimal principal;
    @ApiModelProperty(value = "利息", required = true)
    private BigDecimal interest;
    @ApiModelProperty(value = "贷款服务费", required = true)
    private BigDecimal loanServiceFee;
    @ApiModelProperty(value = "贷款技术费", required = true)
    private BigDecimal loanTechFee;
    @ApiModelProperty(value = "贷款其他费用", required = true)
    private BigDecimal loanOtherFee;
    @ApiModelProperty(value = "罚息", required = true)
    private BigDecimal punishment;
    @ApiModelProperty(value = "逾期违约金", required = true)
    private BigDecimal overdueFee;
    @ApiModelProperty(value = "逾期服务费", required = true)
    private BigDecimal overdueServiceFee;
    @ApiModelProperty(value = "逾期其他费用", required = true)
    private BigDecimal overdueOtherFee;
}