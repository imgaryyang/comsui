package com.suidifu.owlman.microservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeductApplicationWriteOffException extends RuntimeException {
    private static final long serialVersionUID = -8955801661089237072L;
    private int code;
    private String message;

    public DeductApplicationWriteOffException(String message) {
        this.message = message;
    }
}
