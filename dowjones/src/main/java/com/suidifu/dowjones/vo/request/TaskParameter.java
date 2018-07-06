package com.suidifu.dowjones.vo.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

@Data
@ApiModel(value = "输入的指纹表参数")
public class TaskParameter implements Serializable {

    @NotEmpty(message = "信托合同UUID不能为空")
    @ApiModelProperty(value = "信托合同UUID", required = true)
    private String financialContractUuid;

    @NotEmpty(message = "任务类型")
    @ApiModelProperty(value = "任务ID", required = true)
    private String taskType;

    @NotEmpty(message = "日期不能为空")
    @ApiModelProperty(value = "统计的日期", required = true)
    private String date;

}
