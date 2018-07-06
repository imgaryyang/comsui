package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.financial.FinancialContract;

import java.math.BigDecimal;

public interface BusinessPaymentVoucherTaskHandler {

    void recover_each_frozen_capital_amount(String ledgerBookNo, FinancialContract financialContract, String companyCustomerUuid, String jvUuid, String sdUuid, BigDecimal bookingAmount, String tmpDepositDocUuid, String sndSecondNo);

}