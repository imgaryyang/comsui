package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.modify.RepaymentInformationModifyModel;
import com.zufangbao.sun.entity.security.Principal;

import javax.servlet.http.HttpServletRequest;

public interface RepaymentInformationApiHandler {

    void modifyRepaymentInformation(RepaymentInformationModifyModel modifyModel, HttpServletRequest request, Contract contract) throws Exception;

    void modifyRepaymentInfoByRule(String payerName, String bankCode, String bankAccount, String bankName, String provinceCode,
                                   String cityCode, ContractAccount vaildContractAccount, String ip, Principal principal, String idCardNum, Customer customer,
                                   String mobile) throws Exception;
}
