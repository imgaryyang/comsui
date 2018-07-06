package com.suidifu.jpmorgan.entity;

/**
 * 
 * @author zjm
 *
 */
public enum GatewayType {

	/** 银企直联 */
	DirectBank,

	/** 超级网银 */
	SuperBank,

	/** 银联 */
	UnionPay;

	public static GatewayType fromOrdinal(Integer ordinal) {
		if (null == ordinal) {
			return null;
		}

		for (GatewayType item : GatewayType.values()) {
			if (item.ordinal() == ordinal) {
				return item;
			}
		}
		return null;
	}
}
