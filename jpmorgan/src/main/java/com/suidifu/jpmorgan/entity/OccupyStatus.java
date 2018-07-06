package com.suidifu.jpmorgan.entity;

public enum OccupyStatus {

	Free,

	Occupied;

	public static OccupyStatus fromOrdinal(Integer ordinal) {
		if (null == ordinal) {
			return null;
		}

		for (OccupyStatus item : OccupyStatus.values()) {
			if (item.ordinal() == ordinal) {
				return item;
			}
		}
		return null;
	}
}
