package com.suidifu.jpmorgan.entity;

public enum ValidGateway {
	
	SuperBank("enum.gate-way.super-bank","0001"),
	UnionPay("enum.gate-way.union-pay","0002");

	private String key;
	
	private String code;
	
	private ValidGateway(String key,String code) {
		this.key = key;
		this.code = code;
	}
	
	public String getAlias() {
		return this.key.substring(this.key.lastIndexOf(".") + 1);
	}

	public String getKey(){
		return key;
	}
	
	public String getCode(){
		return code;
	}
	
	public int getOrdinal() {
		return this.ordinal();
	}
	
	public static ValidGateway fromValue(int value) {
		for (ValidGateway item : ValidGateway.values()) {
			if (item.ordinal() == value) {
				return item;
			}
		}
		return null;
	}
	
	public static ValidGateway fromCode(String code) {
		for (ValidGateway item : ValidGateway.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}


}
