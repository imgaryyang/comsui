package com.suidifu.datasync.canal.rowprocesser.redis.queue.hash.hashfunction;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.suidifu.datasync.canal.rowprocesser.redis.queue.hash.HashFunction;

public class MurmurHashFunction implements HashFunction {

	@Override
	public int hash(String key) {
		return hash(ByteBuffer.wrap(key.getBytes()), 0x1234ABCD);
	}

	/**
	 * Hashes the bytes in a buffer from the current position to the limit.
	 * 
	 * @param buf The bytes to hash.
	 * @param seed The seed for the hash.
	 * @return The 32 bit murmur hash of the bytes in the buffer.
	 */
	public static int hash(ByteBuffer buf, int seed) {
		// save byte order for later restoration
		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);
		int m = 0x5bd1e995;
		int r = 24;
		int h = seed ^ buf.remaining();
		int k;
		while (buf.remaining() >= 4) {
			k = buf.getInt();
			k *= m;
			k ^= k >>> r;
			k *= m;
			h *= m;
			h ^= k;
		}
		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, use this first:
			// finish.position(4-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getInt();
			h *= m;
		}
		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;
		buf.order(byteOrder);
		return h;
	}

	// 64位
	public static long hash64A(ByteBuffer buf, int seed) {
		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);
		long m = 0xc6a4a7935bd1e995L;
		int r = 47;
		long h = seed ^ (buf.remaining() * m);
		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();
			k *= m;
			k ^= k >>> r;
			k *= m;
			h ^= k;
			h *= m;
		}
		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, do this first:
			// finish.position(8-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;
		buf.order(byteOrder);
		return h;
	}
}
