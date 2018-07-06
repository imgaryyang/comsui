package com.suidifu.owlman.microservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * @author louguanyang
 */
@Data
@ApiModel(value = "变更还款计划明细")
public class RequestData {
    /**
     * 计划还款日期 yyyy-MM-dd
     */
    private String assetRecycleDate;
    /**
     * 计划本金date
     */
    @ApiModelProperty(value = "计划本金")
    @DecimalMin(value = "0.0", message = "无效的计划本金总额")
    private BigDecimal assetPrincipal = BigDecimal.ZERO;
    /**
     * 计划利息
     */
    @ApiModelProperty(value = "计划利息")
    @DecimalMin(value = "0.0", message = "无效的计划利息总额")
    private BigDecimal assetInterest = BigDecimal.ZERO;
    /**
     * 服务费
     */
    private BigDecimal serviceCharge = BigDecimal.ZERO;
    /**
     * 维护费
     */
    private BigDecimal maintenanceCharge = BigDecimal.ZERO;
    /**
     * 其它费用
     */
    private BigDecimal otherCharge = BigDecimal.ZERO;

    private Integer assetType = null;

    private String repaymentPlanNo;

    private Integer currentPeriod;

    private String repayScheduleNo;
}