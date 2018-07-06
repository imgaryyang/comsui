package com.suidifu.jpmorgan.entity;

public enum GatewayWorkingStatus {

	//working,suspending,stop
	Working,
	
	Suspending,
	
	Stop;
	public static GatewayWorkingStatus fromOrdinal(Integer ordinal) {
		if (null == ordinal) {
			return null;
		}

		for (GatewayWorkingStatus item : GatewayWorkingStatus.values()) {
			if (item.ordinal() == ordinal) {
				return item;
			}
		}
		return null;
	}
}
