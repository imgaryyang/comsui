/**
 *
 */
package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.entity.account.AccountSide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wukai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalAccount {
    private AccountSide accountSide;

    public static JournalAccount generateDebitJournalAccount() {
        return new JournalAccount(AccountSide.DEBIT);
    }

    public static JournalAccount generateCreditJournalAccount() {
        return new JournalAccount(AccountSide.CREDIT);
    }
}