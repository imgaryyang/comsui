package com.suidifu.morganstanley.model.request.pre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/12 <br>
 * Time:下午6:24 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Data
@ApiModel(value = "文件上传")
public class UploadFile {
    @NotEmpty(message = "请求唯一编号［requestNo］，不能为空")
    @NotNull(message = "请求唯一编号［requestNo］，不能为null")
    @ApiModelProperty(value = "请求编号", required = true)
    private String requestNo;
    @ApiModelProperty(value = "交易时间")
    private String tradeTime;
}