package com.suidifu.owlman.microservice.model;

import com.suidifu.owlman.microservice.enumation.CounterAccountType;
import com.zufangbao.sun.entity.account.AccountSide;
import lombok.Data;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/14 <br>
 * @time: 22:26 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
public class JournalVoucherResovler {
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