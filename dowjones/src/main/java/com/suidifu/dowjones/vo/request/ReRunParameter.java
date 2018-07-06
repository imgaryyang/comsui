package com.suidifu.dowjones.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/19 <br>
 * @time: 22:47 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@ApiModel(value = "输入的可重复运行spark任务的指纹表参数")
public class ReRunParameter implements Serializable {
    @NotEmpty(message = "信托合同UUID不能为空")
    @ApiModelProperty(value = "信托合同UUID", required = true)
    private String financialContractUuid;

    @NotEmpty(message = "日期参数不能为空")
    @ApiModelProperty(value = "要运行的日期参数，以yyyy-MM-dd格式展现", required = true)
    private String date;
}