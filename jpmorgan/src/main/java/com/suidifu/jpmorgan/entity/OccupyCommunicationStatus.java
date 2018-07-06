package com.suidifu.jpmorgan.entity;

public enum OccupyCommunicationStatus {

	Ready,

	Processing,

	Sending,

	Done;

	public static OccupyCommunicationStatus fromOrdinal(Integer ordinal) {
		if (null == ordinal) {
			return null;
		}

		for (OccupyCommunicationStatus item : OccupyCommunicationStatus
				.values()) {
			if (item.ordinal() == ordinal) {
				return item;
			}
		}
		return null;
	}
}
