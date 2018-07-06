package com.suidifu.microservice.enume;

import java.util.Arrays;
import java.util.List;

import com.zufangbao.sun.BasicEnum;
import com.zufangbao.sun.utils.ApplicationMessageUtils;

public enum JournalVoucherStatus implements BasicEnum  {
	

	/** 已建 */
	VOUCHER_CREATED("enum.journal-voucher-status.voucher_created"),
	/** 已制证; 成功  */
	VOUCHER_ISSUED("enum.journal-voucher-status.voucher_issued"),
	/** 凭证作废; 已退款  */
	VOUCHER_LAPSE("enum.journal-voucher-status.voucher_lapse");

	
	private String key;
	

	
	/**
	 * @param key
	 */
    JournalVoucherStatus(String key) {
		this.key = key;
	
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	public int getOrdinal(){
		
		return this.ordinal();
	}
		
	/**
	 * Get alias of order status
	 * 
	 * @return
	 */
	public String getAlias() {
		return this.key.substring(this.key.lastIndexOf(".") + 1);
	}
	
	public static JournalVoucherStatus fromOrdinal(int ordinal){
		
		for(JournalVoucherStatus item : JournalVoucherStatus.values()){
			
			if(ordinal == item.ordinal()){
				
				return item;
			}
		}
		return null;
	}
	
	public  String getChineseMessage() {
		return ApplicationMessageUtils.getChineseMessage(this.getKey());
	}
	
	public static List<JournalVoucherStatus> getRefundStatus(){
		
		return Arrays.asList(JournalVoucherStatus.VOUCHER_ISSUED);
	}
}
