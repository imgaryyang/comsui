package com.suidifu.microservice.model;

import com.suidifu.microservice.enume.CounterAccountType;
import com.zufangbao.sun.entity.account.AccountSide;

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
	
	
		public String getRelatedBillUuid() {
		return relatedBillUuid;
	}
	public void setRelatedBillUuid(String relatedBillUuid) {
		this.relatedBillUuid = relatedBillUuid;
	}
	public AccountSide getAccountSide() {
		return accountSide;
	}
	public void setAccountSide(AccountSide accountSide) {
		this.accountSide = accountSide;
	}
	public String getCounterPartyAccount() {
		return counterPartyAccount;
	}
	public void setCounterPartyAccount(String counterPartyAccount) {
		this.counterPartyAccount = counterPartyAccount;
	}
	public String getCounterPartyAccountName() {
		return counterPartyAccountName;
	}
	public void setCounterPartyAccountName(String counterPartyAccountName) {
		this.counterPartyAccountName = counterPartyAccountName;
	}
	public String getLocalPartyAccount() {
		return localPartyAccount;
	}
	public void setLocalPartyAccount(String localPartyAccount) {
		this.localPartyAccount = localPartyAccount;
	}
	public String getLocalPartyAccountName() {
		return localPartyAccountName;
	}
	public void setLocalPartyAccountName(String localPartyAccountName) {
		this.localPartyAccountName = localPartyAccountName;
	}
	public String getSourceDocumentCounterPartyAccount() {
		return sourceDocumentCounterPartyAccount;
	}
	public void setSourceDocumentCounterPartyAccount(String sourceDocumentCounterPartyAccount) {
		this.sourceDocumentCounterPartyAccount = sourceDocumentCounterPartyAccount;
	}
	public String getSourceDocumentCounterPartyName() {
		return sourceDocumentCounterPartyName;
	}
	public void setSourceDocumentCounterPartyName(String sourceDocumentCounterPartyName) {
		this.sourceDocumentCounterPartyName = sourceDocumentCounterPartyName;
	}
	
	public CounterAccountType getFromAccountType() {
		return fromAccountType;
	}
	public void setFromAccountType(CounterAccountType fromAccountType) {
		this.fromAccountType = fromAccountType;
	}
	public String getSourceDocumentLocalPartyAccount() {
		return sourceDocumentLocalPartyAccount;
	}
	public void setSourceDocumentLocalPartyAccount(String sourceDocumentLocalPartyAccount) {
		this.sourceDocumentLocalPartyAccount = sourceDocumentLocalPartyAccount;
	}
	public String getSourceDocumentLocalPartyName() {
		return sourceDocumentLocalPartyName;
	}
	public void setSourceDocumentLocalPartyName(String sourceDocumentLocalPartyName) {
		this.sourceDocumentLocalPartyName = sourceDocumentLocalPartyName;
	}
	
	
	
	
	
}
