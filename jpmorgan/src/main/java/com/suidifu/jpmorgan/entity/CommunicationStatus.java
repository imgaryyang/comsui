package com.suidifu.jpmorgan.entity;

public enum CommunicationStatus {

	Inqueue,

	Process,

	Success,

	Failed,

	Abandon;

	public static CommunicationStatus fromOrdinal(Integer ordinal) {
		if (null == ordinal) {
			return null;
		}

		for (CommunicationStatus item : CommunicationStatus.values()) {
			if (item.ordinal() == ordinal) {
				return item;
			}
		}
		return null;
	}
}
