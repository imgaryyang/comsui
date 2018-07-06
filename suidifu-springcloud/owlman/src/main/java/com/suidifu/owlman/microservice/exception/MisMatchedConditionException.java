package com.suidifu.owlman.microservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MisMatchedConditionException extends RuntimeException {
    private static final long serialVersionUID = -8105370238038521866L;
    private int code;
    private String message;

    public MisMatchedConditionException(String message) {
        this.message = message;
    }
}
