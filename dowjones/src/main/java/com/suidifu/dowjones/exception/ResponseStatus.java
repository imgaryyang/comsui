package com.suidifu.dowjones.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User:frankwoo(吴峻申) <br>
 * Date:2016-11-3 <br>
 * Time:13:48 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@AllArgsConstructor
@Getter
public enum ResponseStatus {
    OK(0, "成功"),
    SYSTEM_ERROR(111, "系统错误"),
    DATA_CREATE_ERROR(100, "新增数据失败"),
    DATA_REQUERY_ERROR(101, "查询数据失败"),
    DATA_UPDATED_ERROR(102, "更新数据失败"),
    DATA_DELETED_ERROR(103, "删除数据失败"),
    DATA_INPUT_ERROR(104, "数据未输入"),
    PARAMETER_VALIDATION(105, "参数验证失败-{0}"),
    PARAMETER_ERROR(106, "参数错误"),
    INVALID_CLIENT_ID(300, "clientID无效"),
    INVALID_USER_NAME(301, "用户名错误"),
    INVALID_PASSWORD(302, "密码错误"),
    INVALID_TOKEN(303, "access_token无效"),
    NO_AUTHORIZATION(304, "无Authorization传入"),
    EXCEPTION_INFO(500, "发生异常，信息如下:"),
    FALL_BACK(501, "无法访问服务，该服务可能由于某种未知原因被关闭。请重启服务！");

    // 成员变量
    private int code;
    private String message;
}