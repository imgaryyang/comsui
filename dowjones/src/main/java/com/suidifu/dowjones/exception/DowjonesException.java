package com.suidifu.dowjones.exception;

import lombok.*;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/4 <br>
 * @time: 14:00 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class DowjonesException extends Exception {
    private Integer code;
    @NonNull
    private String message;
}