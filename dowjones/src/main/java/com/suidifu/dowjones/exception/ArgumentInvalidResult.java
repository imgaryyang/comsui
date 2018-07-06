package com.suidifu.dowjones.exception;

import lombok.Data;

/**
 * User:frankwoo(吴峻申) <br>
 * Date:2016-11-3 <br>
 * Time:13:41 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Data
public class ArgumentInvalidResult {
    private String field;
    private Object rejectedValue;
    private String defaultMessage;
}