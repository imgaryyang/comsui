package com.zufangbao.earth.yunxin.api.handler.impl;

import com.alibaba.fastjson.JSON;
import com.zufangbao.earth.yunxin.api.model.PagedTradeList;
import com.zufangbao.earth.yunxin.api.model.query.AccountTradeList;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyAuditBillService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by whb on 17-7-5.
 */
@Component("thirdPartyTradeListApiHandler")
public class ThirdPartyTradeListApiHandlerImpl extends TradeListApiHandlerImpl {
    @Autowired
    private ThirdPartyAuditBillService thirdPartyAuditBillService;

   

    @Override
    public PagedTradeList findAccountTradeListBy(String productNo,
                                                 String capitalAccountNo,
                                                 PaymentInstitutionName paymentInstitutionName,
                                                 Date startDate,
                                                 Date endDate,
                                                 Integer pageNumber,
                                                 AccountSide accountSide) {
        int beginIndex = (pageNumber - 1) * getSizeOfPerPage();
        String financialContract = findFinancialContractUuidByContractNo(productNo);
        List<ThirdPartyAuditBill> thirdPartyAuditBills = thirdPartyAuditBillService.getThirdPartyAuditBillBy(
                paymentInstitutionName,
                financialContract,
                startDate,
                endDate,
                beginIndex,
                getSizeOfPerPage() * 2,
                accountSide);

        boolean nextPageFlagABoolean = ifHasNextPage(thirdPartyAuditBills);

        List<AccountTradeList> tradeList= thirdPartyAuditBills.parallelStream().limit(getSizeOfPerPage()).map(
                thirdPartyAuditBill -> {
                    String countAccountAppendix = thirdPartyAuditBill.getCounterAccountAppendix();
                    String bankName = "";
                    if (! StringUtils.isEmpty(bankName)) {
                        try {
                            bankName = JSON.parseObject(countAccountAppendix).getString("bankName");
                        } catch (Exception e) {
                            bankName = "";
                        }
                    }

                    return new AccountTradeList(
                            thirdPartyAuditBill.getChannelSequenceNo(),
                            thirdPartyAuditBill.getAccountSide().getOrdinal(),
                            thirdPartyAuditBill.getCounterAccountNo(),
                            thirdPartyAuditBill.getCounterAccountName(),
                            bankName,
                            thirdPartyAuditBill.getTransactionAmount(),
                            thirdPartyAuditBill.getTransactionTime(),
                            thirdPartyAuditBill.getRemark(),
                            "",
                            thirdPartyAuditBill.getBatchNo(),
                            thirdPartyAuditBill.getMerchantOrderNo()
                    );
                }
        ).collect(Collectors.toList());
        PagedTradeList pagedList=new PagedTradeList();
        
        pagedList.setTradeList(tradeList);
        pagedList.setHasNextPage(nextPageFlagABoolean);
        
        return pagedList;
    }

   
    /**
     * 根据合同代码得到合同的uuid
     * @param contractNo
     * @return
     */
    private String findFinancialContractUuidByContractNo(String contractNo){
        return financialContractService.
                getUniqueFinancialContractBy(contractNo).
                getFinancialContractUuid();
    }
}
