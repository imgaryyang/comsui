package com.suidifu.owlman.microservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherException extends Exception {
    private static final long serialVersionUID = 5795418806169905873L;
    private int errorCode;
    private String errorMsg;
}
