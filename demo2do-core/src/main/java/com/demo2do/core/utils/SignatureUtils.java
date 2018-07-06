/**
 * 
 */
package com.demo2do.core.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Downpour
 */
public class SignatureUtils {
	
	/**
	 * Make signature according to input parameters, the value sequence is corresponding to the key order
	 * 
	 * @param parameters
	 * @return
	 */
	public static String makeMD5Signature(Map<String,String> parameters) {
		
		List<String> keys = new ArrayList<String>(parameters.keySet());
		
		// sort key first
		Collections.sort(keys);
		
		// connect values
		StringBuffer buffer = new StringBuffer();
		for(String key : keys){
			buffer.append(parameters.get(key));
		}
		
		// do md5 encrypt and hex
		byte[] md5encryptBytes = encryptMD5(buffer.toString().getBytes(Charset.forName("utf-8")));
		
		if(md5encryptBytes != null) {
			return toHexValue(md5encryptBytes);
		}

		return null;
		
	}
	
	/**
	 * Make signature according to input parameters
	 * 
	 * @param parameters
	 * @return
	 */
	public static String makeMD5Signature(String... parameters) {
		
		String plainText = org.apache.commons.lang.StringUtils.join(parameters);
		
		byte[] md5encryptBytes = encryptMD5(plainText.getBytes(Charset.forName("utf-8")));
		
		if(md5encryptBytes != null) {
			return toHexValue(md5encryptBytes);
		}
		
		return null;
	}
	
	
	/**
	 * Make MD5 encrypt, return null if there are some problems here
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptMD5(byte[] data) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(data);
			return md5.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Convert to Hex value
	 * 
	 * @param messageDigest
	 * @return
	 */
	private static String toHexValue(byte[] messageDigest) {
		
		if (messageDigest != null) {
			
			StringBuilder hexValue = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				int val = 0xFF & aMessageDigest;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
			
		}
		
		return null;
	}

}
