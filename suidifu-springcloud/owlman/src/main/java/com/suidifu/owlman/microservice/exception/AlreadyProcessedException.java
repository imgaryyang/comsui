package com.suidifu.owlman.microservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AlreadyProcessedException extends Exception {
    private static final long serialVersionUID = 6615376258319545415L;
    private String errorMessage;

    public AlreadyProcessedException(String msg) {
        super();
        this.errorMessage = msg;
    }
}
