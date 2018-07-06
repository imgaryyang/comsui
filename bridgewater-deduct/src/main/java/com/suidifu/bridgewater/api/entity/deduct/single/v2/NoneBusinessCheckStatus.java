package com.suidifu.bridgewater.api.entity.deduct.single.v2;


public enum NoneBusinessCheckStatus {

	Waiting,
	
	Processing,
	
	Done;
	
	public static NoneBusinessCheckStatus fromValue(int value) {

		for (NoneBusinessCheckStatus item : NoneBusinessCheckStatus.values()) {
			if (item.ordinal() == value) {
				return item;
			}
		}
		return null;
	}
}
