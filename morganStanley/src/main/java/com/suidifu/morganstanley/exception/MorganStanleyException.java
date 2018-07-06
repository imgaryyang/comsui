package com.suidifu.morganstanley.exception;

import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import lombok.*;

/**
 * @author louguanyang on 2017/5/24.
 */
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class MorganStanleyException extends Exception {
    private Integer code;
    @NonNull
    private String message;

    public MorganStanleyException(ApiMessage apiMessage) {
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
    }
}