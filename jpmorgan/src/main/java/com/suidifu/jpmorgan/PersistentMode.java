package com.suidifu.jpmorgan;

import java.util.HashMap;
import java.util.Map;

public enum PersistentMode {

	DB,

	CACHE_DB;

	private static Map<Integer, PersistentMode> map = new HashMap<>();

	static {
		for (PersistentMode mode : PersistentMode.values()) {
			map.put(mode.ordinal(), mode);
		}
	}

	public static PersistentMode valueOf(int mode) {
		return map.get(mode);
	}

}
