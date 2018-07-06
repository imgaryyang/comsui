package com.suidifu.microservice.enume;

import com.zufangbao.sun.BasicEnum;

/**
 * 凭证明细核销状态
 * @author louguanyang
 *
 */
public enum SourceDocumentDetailStatus implements BasicEnum {
	/** 未核销 */
	UNSUCCESS("enum.source-document-detail-status.unsuccess"),
	/** 已核销 */
	SUCCESS("enum.source-document-detail-status.success"),
	/** 作废 */
	INVALID("enum.source-document-detail-status.invalid");
	
	private String key;
	SourceDocumentDetailStatus(String key) {
		
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	
	public static SourceDocumentDetailStatus fromKey(String key) {
		for (SourceDocumentDetailStatus item : SourceDocumentDetailStatus.values()) {
			if(item.getKey().equals(key)) {
				return item;
			}
		}
		return null;
	}
	
	public static SourceDocumentDetailStatus fromValue(int value) {
		for (SourceDocumentDetailStatus item : SourceDocumentDetailStatus.values()) {
			if(item.ordinal() == value) {
				return item;
			}
		}
		return null;
	}
}
