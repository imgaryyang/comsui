package com.suidifu.jpmorgan.entity;

public enum GatewayOnlineStatus {

	//online,offline
	
	Online,
	
	OffLine;
	public static GatewayOnlineStatus fromOrdinal(Integer ordinal) {
		if (null == ordinal) {
			return null;
		}

		for (GatewayOnlineStatus item : GatewayOnlineStatus.values()) {
			if (item.ordinal() == ordinal) {
				return item;
			}
		}
		return null;
	}

}
