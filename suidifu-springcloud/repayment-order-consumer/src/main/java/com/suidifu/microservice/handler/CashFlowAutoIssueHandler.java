package com.suidifu.microservice.handler;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.model.SourceDocumentVirtualAccountResolver;
import com.suidifu.owlman.microservice.enumation.PaymentCondition;
import com.suidifu.owlman.microservice.exception.BankRechargeAmountException;
import com.zufangbao.sun.entity.account.ConflictBankAccountException;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import java.math.BigDecimal;

public interface CashFlowAutoIssueHandler {
    void charge_cash_into_virtual_account(CashFlow cashFlow, String fst_snd_contractUuid);

    SourceDocumentVirtualAccountResolver charge_cash_into_virtual_account(CashFlow cashFlow, Customer customer, BigDecimal bookingAmount, FinancialContract financialContract, AssetCategory assetCategory, String remark, SourceDocument sourceDocument, Contract contract, boolean binding) throws BankRechargeAmountException, ConflictBankAccountException;

//    SourceDocument deposit_cancel(String sourceDocumentUuid, String cashFlowUuid, BigDecimal bookingAmount, String remark, String ContractUuidOrFinacialContractUuid) throws BankCancelPaymentException, GlobalRuntimeException;
//
//    SourceDocument charge_cash_into_virtual_account(CashFlow cashFlow, Customer customer,
//                                                    BigDecimal bookingAmount, FinancialContract financialContract, AssetCategory assetCategory, String remark,
//                                                    SourceDocument sourceDocument) throws BankRechargeAmountException;
//
//    void recover_assets_online_deduct_by_interface_each_deduct_applicationV2(String sourceDocumentUuid);
//
//    void auto_recover_assets_by_contract_uuid(String contractUuid, int priority);

    void isExistSameBankCard(CashFlow cashFlow, Customer customer, VirtualAccount virtualAccount, Contract contract, String financialContractUuid) throws
            ConflictBankAccountException;

    PaymentCondition paymentOrderRecharge(CashFlow cashFlow, RepaymentOrder order, String contractUuid, FinancialContract financialContract);

//    //*******此方法不要修改名字*******
//    void cashFlowAutoChargeForRepaymentOrder(String businessId, String paymentOrderUuid);
//
//    void bookCashFlowInPendingSpecialAccount(String cashFlowUuid, String financialContractUuid);
//
//    void book_cash_flow_pending(String financialContractUuid, BigDecimal amount);
}