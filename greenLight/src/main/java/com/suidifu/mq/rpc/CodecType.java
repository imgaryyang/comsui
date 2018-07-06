package com.suidifu.mq.rpc;

import com.suidifu.mq.rpc.codec.JsonRpcCodec;
import com.suidifu.mq.rpc.codec.KryoRpcCodec;

/**
 * CodecType
 * 
 * @author lisf
 *
 */
public enum CodecType {
	KYRO("kyro"), JSON("json");
	private String key;

	private CodecType(String key) {
		this.key = key;
	}

	public String get() {
		return key;
	}

	public int getOrdinal() {
		return this.ordinal();
	}

	public RpcCodec getCodec() {
		return getCodec(getOrdinal());
	}

	public static RpcCodec getCodec(int ordinal) {
		switch (ordinal) {
		case 0:
			return new KryoRpcCodec();
		case 1:
			return new JsonRpcCodec();
		}
		return null;
	}
}
