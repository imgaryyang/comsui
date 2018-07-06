package com.suidifu.dowjones.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/21 <br>
 * @time: 下午9:47 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@ApiModel(value = "传入参数类")
public class InputParameter {
    @NotEmpty(message = "信托合同UUID不能为空")
    @ApiModelProperty(value = "信托合同UUID", required = true)
    private String financialContractUuid;

    @NotEmpty(message = "必须选择至少一个逾期阶段")
    @ApiModelProperty(value = "逾期阶段(可多选)\n" +
            "M1  逾期30天以内的还款计划\n" +
            "M2  逾期30-60天的还款计划\n" +
            "M3  逾期60-90天的还款计划\n" +
            "M3+ 逾期90天以上的还款计划\n", required = true)
    private String overdueStage;

    @ApiModelProperty(value = "逾期状态(待确认是否计入逾期)", required = true)
    private boolean includeUnconfirmed;

    @ApiModelProperty(value = "回购状态(已回购是否计入已还,true为不计入，false为计入)", required = true)
    private boolean includeRepurchase;

    @ApiModelProperty(value = "逾期阶段每阶段天数", required = true)
    @Min(1)
    private int periodDays;

    @ApiModelProperty(value = "计算类型：0剩余本金1剩余本息", required = true)
    @Min(0)
    @Max(1)
    private int computeTypeFlag;
}