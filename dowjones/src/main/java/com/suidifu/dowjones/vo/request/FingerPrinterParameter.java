package com.suidifu.dowjones.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 13:17 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@ApiModel(value = "输入的指纹表参数")
public class FingerPrinterParameter implements Serializable {
    @NotEmpty(message = "信托合同UUID不能为空")
    @ApiModelProperty(value = "信托合同UUID", required = true)
    private String financialContractUuid;

    @NotEmpty(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID", required = true)
    private String taskId;

    @NotEmpty(message = "文件流Uuid不能为空")
    @ApiModelProperty(value = "文件流Uuid", required = true)
    private String dataStreamUuid;

    @NotEmpty(message = "csv存放路径不能为空")
    @ApiModelProperty(value = "csv存放路径", required = true)
    private String path;
}