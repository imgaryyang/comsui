package com.suidifu.morganstanley.exception;

import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import lombok.*;

/**
 * @author louguanyang on 2017/5/24.
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class MorganStanleyRuntimeException extends RuntimeException {
    @NonNull
    private Integer code;
    private String message;

    public MorganStanleyRuntimeException(ApiMessage apiMessage) {
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
    }
}