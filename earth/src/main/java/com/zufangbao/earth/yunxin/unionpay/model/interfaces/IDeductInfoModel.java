package com.zufangbao.earth.yunxin.unionpay.model.interfaces;

import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.PaymentChannel;

import java.math.BigDecimal;

public interface IDeductInfoModel {

    void setReqNo(String reqNo);

    void setAmount(BigDecimal amount);

    void setContractAccount(ContractAccount contractAccount);

    void setPaymentChannel(PaymentChannel paymentChannel);

    void setBusinessCode(String businessCode);

    void setBankCode(String bankCode);

    String getReqNo();

    BigDecimal getAmount();

    ContractAccount getContractAccount();

    PaymentChannel getPaymentChannel();

    String getBusinessCode();

    String getBankCode();

}
