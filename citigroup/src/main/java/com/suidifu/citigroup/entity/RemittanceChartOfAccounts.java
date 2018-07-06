package com.suidifu.citigroup.entity;

import java.util.HashMap;

import com.zufangbao.sun.ledgerbook.AccountSide;

/**
 * 放款会计科目表
 * 1.都为三级科目
 * 
 */
public class RemittanceChartOfAccounts {

	
	// bankSavingFreeze && bankSavingLoan
    public static final String FST_BANK_SAVING = "FST_BANK_SAVING";
    public static final String FST_BANK_SAVING_CODE = "" + 60000;
    public static final String SND_BANK_SAVING_REMITTANCE = "SND_BANK_SAVING_REMITTANCE";
    public static final String SND_BANK_SAVING_REMITTANCE_CODE = FST_BANK_SAVING_CODE + ".01";
    
    
    // bankSavingLoan
    public static final String TRD_BANK_SAVING_REMITTANCE_BALANCE = "TRD_BANK_SAVING_REMITTANCE_BALANCE"; 
    public static final String TRD_BANK_SAVING_REMITTANCE_BALANCE_CODE = SND_BANK_SAVING_REMITTANCE_CODE + ".01";
    
    // bankSavingFreeze
    public static final String TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND = "TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND";
    public static final String TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND_CODE = SND_BANK_SAVING_REMITTANCE_CODE + ".02";
    
    
    //payAble
    public static final String FST_PAYABLE_LIABILITY = "FST_PAYABLE_LIABILITY"; 
    public static final String FST_PAYABLE_LIABILITY_CODE = "" + 30000;
    public static final String SND_PAYABLE_LIABILITY_REMITTANCE = "SND_PAYABLE_LIABILITY_REMITTANCE";
    public static final String SND_PAYABLE_LIABILITY_REMITTANCE_CODE = FST_PAYABLE_LIABILITY_CODE + ".01";
    public static final String TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT = "TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT";
    public static final String TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT_CODE = SND_PAYABLE_LIABILITY_REMITTANCE_CODE + ".01";

    
    public static String FREEZING = "0";
    public static String UNFREEZING = "1";
    public static String RECHARGE = "2";
    public static String UNRECHARGE = "3";
    
    
    public static String MODIFY_DATA = "4";
    
    private static HashMap<String, RemittanceAccountInfo> entryBook ;
    
    static {
	EntryBook();
    }
    
    public static HashMap<String, RemittanceAccountInfo> EntryBook() {

    	if (entryBook == null) {
    	    entryBook = new HashMap<String, RemittanceAccountInfo>();
    	    
    	    entryBook.put(TRD_BANK_SAVING_REMITTANCE_BALANCE, new RemittanceAccountInfo(FST_BANK_SAVING,FST_BANK_SAVING_CODE,SND_BANK_SAVING_REMITTANCE,SND_BANK_SAVING_REMITTANCE_CODE,TRD_BANK_SAVING_REMITTANCE_BALANCE,TRD_BANK_SAVING_REMITTANCE_BALANCE_CODE,AccountSide.DEBIT.ordinal()));
    	    
    	    entryBook.put(TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND, new RemittanceAccountInfo(FST_BANK_SAVING,FST_BANK_SAVING_CODE,SND_BANK_SAVING_REMITTANCE,SND_BANK_SAVING_REMITTANCE_CODE,TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND,TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND_CODE,AccountSide.DEBIT.ordinal()));
    	    
    	    entryBook.put(TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT, new RemittanceAccountInfo(FST_PAYABLE_LIABILITY,FST_PAYABLE_LIABILITY_CODE,SND_PAYABLE_LIABILITY_REMITTANCE,SND_PAYABLE_LIABILITY_REMITTANCE_CODE,TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT,TRD_PAYABLE_LIABILITY_REMITTANCE_DEPOSIT_CODE,AccountSide.CREDIT.ordinal()));
    	    
    	    }
    	return entryBook;
    }
    
}
