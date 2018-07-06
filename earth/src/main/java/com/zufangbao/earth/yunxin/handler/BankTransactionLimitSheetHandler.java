package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.yunxin.entity.model.TransactionLimitQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;

import java.util.List;
import java.util.Map;

public interface BankTransactionLimitSheetHandler {

    List<Map<String, Object>> getAllBanks(String financialContractUuid, BusinessType businessType, AccountSide accountSide);

	Map<String, Object> searchTransactionLimitBy(TransactionLimitQueryModel queryModel, Page page);

    List<Map<String, Object>> getBankLimitPreview(PaymentInstitutionName paymentInstitutionName, AccountSide accountSide, String outlierChannelName, Page page);

    Map<String, Object> searchBankTransactionLimitBy(TransactionLimitQueryModel queryModel, Page page);

    Map<String, Object> getBankChannelLimitPreview(PaymentInstitutionName paymentInstitutionName, AccountSide accountSide, String outlierChannelName, String bankCode);
}
