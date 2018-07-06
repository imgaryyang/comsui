package com.suidifu.mq.hash.hashfunction;

import java.security.MessageDigest;

import com.suidifu.mq.hash.HashFunction;

public class Md5HashFunction implements HashFunction {

	@Override
	public int hash(String key) {
		return md5(key).hashCode();
	}

	private String md5(String key) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] bytesOfValue = key.getBytes("UTF-8");
			byte[] digest = messageDigest.digest(bytesOfValue);
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < digest.length; i++) {
				stringBuffer.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
			}
			return stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
