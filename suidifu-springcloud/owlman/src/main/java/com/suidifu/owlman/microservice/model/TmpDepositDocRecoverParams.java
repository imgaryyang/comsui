package com.suidifu.owlman.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmpDepositDocRecoverParams implements Serializable {
    private boolean isFromTmpDepost = false;
    private String tmpDepositDocUuid;
    private String secondNo;

    public String getTmpDepositDocUuidFromTmpDepositRecover() {
        return isFromTmpDepost ? tmpDepositDocUuid : "";
    }

    public String getSecondNoFromTmpDepositRecover() {
        return isFromTmpDepost ? secondNo : "";
    }
}
