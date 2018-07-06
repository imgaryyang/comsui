package com.suidifu.morganstanley.handler.repayment;

import com.suidifu.morganstanley.model.request.repayment.ModifyRepaymentInformation;
import com.zufangbao.sun.entity.contract.Contract;

public interface RepaymentInformationApiHandler {
    void modifyRepaymentInformation(ModifyRepaymentInformation modifyModel, Contract contract);

    void saveLog(ModifyRepaymentInformation information, Contract contract, String ip);

    void checkRequestNo(String requestNo);
}