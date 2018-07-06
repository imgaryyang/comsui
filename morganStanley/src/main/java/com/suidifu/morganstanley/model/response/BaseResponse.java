package com.suidifu.morganstanley.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * User:Administrator(吴峻申)
 * Date:2015-11-30
 * Time:11:12
 * Mail:frank_wjs@hotmail.com
 */
@Data
@ApiModel(value = "响应消息基础类")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseResponse {
    @NonNull
    @ApiModelProperty(value = "响应码", required = true)
    private int code;
    @NonNull
    @ApiModelProperty(value = "响应消息", required = true)
    private String message;
    @ApiModelProperty(value = "返回数据")
    private Object data;
}