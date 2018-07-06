package com.suidifu.jpmorgan.entity;

public enum BusinessStatus {

	Inqueue,

	Processing,

	OppositeProcessing,

	Success,

	Failed,

	Abandon;

	public static BusinessStatus fromOrdinal(Integer ordinal) {
		if (null == ordinal) {
			return null;
		}

		for (BusinessStatus item : BusinessStatus.values()) {
			if (item.ordinal() == ordinal) {
				return item;
			}
		}
		return null;
	}

}
