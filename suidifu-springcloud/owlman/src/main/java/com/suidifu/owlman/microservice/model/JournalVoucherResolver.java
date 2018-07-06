package com.suidifu.owlman.microservice.model;

import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.zufangbao.sun.entity.account.AccountSide;
import lombok.Data;

@Data
public class JournalVoucherResolver {
    AccountSide accountSide;
    CounterAccountType fromAccountType;
    String counterPartyAccount;
    String counterPartyAccountName;
    String localPartyAccount;
    String localPartyAccountName;
    String sourceDocumentCounterPartyAccount;
    String sourceDocumentCounterPartyName;
    String sourceDocumentLocalPartyAccount;
    String sourceDocumentLocalPartyName;
    String relatedBillUuid;
}