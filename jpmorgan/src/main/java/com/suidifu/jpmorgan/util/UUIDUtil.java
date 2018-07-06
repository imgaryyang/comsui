package com.suidifu.jpmorgan.util;

import java.util.UUID;
/**
 * 
 * @author zhangjianming
 *
 */
public class UUIDUtil {

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Numbers.toString(hi | (val & (hi - 1)), Numbers.MAX_RADIX)
				.substring(1);
	}

	/**
	 * 以62进制（字母加数字）生成19位UUID，最短的UUID
	 * 
	 * @return
	 */
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
		sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
		sb.append(digits(uuid.getMostSignificantBits(), 4));
		sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
		sb.append(digits(uuid.getLeastSignificantBits(), 12));
		return sb.toString();
	}

	/**
	 * snowFlake UUID
	 * 
	 * @return
	 */
	public static String snowFlakeIdString() {
		return snowFlakeId() + "";
	}

	public static long snowFlakeId() {
		long datacenterId = SnowFlake.getDefaultDatacenterId();
		long machineId = SnowFlake.getDefaultMachineId();
		return SnowFlake.getSnowFlake(datacenterId, machineId).nextId();
	}
}
