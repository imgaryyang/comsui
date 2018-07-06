package com.suidifu.dowjones.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/23 <br>
 * @time: 下午7:34 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "传入参数类")
public class StaticOverdueRateInputParameter extends InputParameter {
    @ApiModelProperty(value = "年份", required = true)
    private int year;
    @ApiModelProperty(value = "月份", required = true)
    @Min(1)
    @Max(12)
    private int month;
}