package com.suidifu.jpmorgan.util;

import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantLock;

public final class SnowFlake {
	/**
	 * 起始的时间戳
	 */
	private final static long START_STMP = 1480166465631L;

	/**
	 * 每一部分占用的位数
	 */
	private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
	private final static long MACHINE_BIT = 5; // 机器标识占用的位数
	private final static long DATACENTER_BIT = 5;// 数据中心占用的位数

	/**
	 * 每一部分的最大值
	 */
	private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
	private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
	private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

	/**
	 * 每一部分向左的位移
	 */
	private final static long MACHINE_LEFT = SEQUENCE_BIT;
	private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
	private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

	private long datacenterId; // 数据中心
	private long machineId; // 机器标识
	private long sequence = 0L; // 序列号
	private long lastStmp = -1L;// 上一次时间戳

	private static final int MACHINE_IDENTIFIER;// 机器
	private static final int PROCESS_IDENTIFIER;// 进程
	private static final int LOW_ORDER_THREE_BYTES = 0x00ffffff;

	private static final ReentrantLock lock = new ReentrantLock();
	private static SnowFlake snowFlake = null;

	private SnowFlake() {
		this(getDefaultDatacenterId(), getDefaultMachineId());
	}

	private SnowFlake(long datacenterId, long machineId) {
		if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0)
			throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
		if (machineId > MAX_MACHINE_NUM || machineId < 0)
			throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
		this.datacenterId = datacenterId;
		this.machineId = machineId;
	}

	public static SnowFlake getSnowFlake(long datacenterId, long machineId) {
		if (snowFlake == null) {
			lock.lock();
			if (snowFlake == null)
				snowFlake = new SnowFlake(datacenterId, machineId);
			lock.unlock();
		}
		return snowFlake;
	}

	public static long getDefaultDatacenterId() {
		return PROCESS_IDENTIFIER % (MAX_DATACENTER_NUM + 1);
	}

	public static long getDefaultMachineId() {
		return MACHINE_IDENTIFIER % (MAX_MACHINE_NUM + 1);
	}

	/**
	 * 产生下一个ID
	 *
	 * @return
	 */
	public synchronized long nextId() {
		long currStmp = getNewstmp();
		if (currStmp < lastStmp)
			throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
		if (currStmp == lastStmp) {
			// 相同毫秒内，序列号自增
			sequence = (sequence + 1) & MAX_SEQUENCE;
			// 同一毫秒的序列数已经达到最大
			if (sequence == 0L)
				currStmp = getNextMill();
		} else {
			// 不同毫秒内，序列号置为0
			sequence = 0L;
		}
		lastStmp = currStmp;
		return (currStmp - START_STMP) << TIMESTMP_LEFT // 时间戳部分
				| datacenterId << DATACENTER_LEFT // 数据中心部分
				| machineId << MACHINE_LEFT // 机器标识部分
				| sequence; // 序列号部分
	}

	private long getNextMill() {
		long mill = getNewstmp();
		while (mill <= lastStmp)
			mill = getNewstmp();
		return mill;
	}

	private long getNewstmp() {
		return System.currentTimeMillis();
	}

	static {
		try {
			MACHINE_IDENTIFIER = Math.abs(createMachineIdentifier());
			PROCESS_IDENTIFIER = Math.abs(createProcessIdentifier());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 机器
	 * 
	 * @return
	 */
	private static int createMachineIdentifier() {
		// build a 2-byte machine piece based on NICs info
		int machinePiece;
		StringBuilder sb = new StringBuilder();
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface ni = e.nextElement();
				sb.append(ni.toString());
				byte[] mac = ni.getHardwareAddress();
				if (mac != null) {
					ByteBuffer bb = ByteBuffer.wrap(mac);
					try {
						sb.append(bb.getChar());
						sb.append(bb.getChar());
						sb.append(bb.getChar());
					} catch (BufferUnderflowException shortHardwareAddressException) { // NOPMD
						// mac with less than 6 bytes. continue
					}
				}
			}
			machinePiece = sb.toString().hashCode();
		} catch (Throwable t) {
			// exception sometimes happens with IBM JVM, use random
			machinePiece = (new SecureRandom().nextInt());
		} finally {
			sb.setLength(0);
		}
		machinePiece = machinePiece & LOW_ORDER_THREE_BYTES;
		return machinePiece;
	}

	/**
	 * 进程
	 * 
	 * @return
	 */
	private static short createProcessIdentifier() {
		short processId;
		try {
			String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
			if (processName.contains("@"))
				processId = (short) Integer.parseInt(processName.substring(0, processName.indexOf('@')));
			else
				processId = (short) java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
		} catch (Throwable t) {
			processId = (short) new SecureRandom().nextInt();
		}
		return processId;
	}
}