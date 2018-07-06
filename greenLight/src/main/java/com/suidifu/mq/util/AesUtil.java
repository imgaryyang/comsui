/**
 * 
 */
package com.suidifu.mq.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author wukai
 *
 */
public abstract class AesUtil {
	
	private static final String KEY_ALGORITHM = "AES";
	
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	private static final String CHARSET_NAME = "UTF8";
	
	public static final String DEFAULT_KEY = "5FijY/Ff86SKwuw5UimlCg==";
	
	public static  byte[] initSecretKey() throws NoSuchAlgorithmException{
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
		
		keyGenerator.init(128);
		
		SecretKey sceretKey = keyGenerator.generateKey();
		
		return sceretKey.getEncoded();
	}
	
	private static Key toKey(byte[] key){
		return new SecretKeySpec(key,KEY_ALGORITHM);
	}
	
	public static byte[] decrypt(byte[] key,byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		
		cipher.init(Cipher.DECRYPT_MODE, toKey(key));
		
		return cipher.doFinal(input);
	}
	public static  byte[] encrypt(byte[] key,byte[] input) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		
		cipher.init(Cipher.ENCRYPT_MODE, toKey(key));
		
		return cipher.doFinal(input);
	}
	
	public static String decrypt(String key,String input){
		
		try {
			return new String(decrypt(decodeToByte(key),decodeToByte(input)),Charset.forName(CHARSET_NAME));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static String decrypt(String input){
		return decrypt(DEFAULT_KEY,input);
	}
	
	public static String encrypt(String key,String input){
		
		try {
			return encodeToString(encrypt(decodeToByte(key),input.getBytes(CHARSET_NAME)));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decodeToByte(String input){
		return Base64.getDecoder().decode(input);
	}
	public static String encodeToString(byte[] input){
		return Base64.getEncoder().encodeToString(input);
	}
	public static String encodeToWithBase64(String input){
		
		try {
			return encodeToString(input.getBytes(CHARSET_NAME));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		
		try {
			if(args.length == 0 || args[0].contains("help") || args[0].contains("h")) {
				
				String[] content = {
						
						"##随地付公司的加密、解密工具！##",
						"通过java命令行的使用，参数说明：",
						" -e 原始值1 原始值2 ...		[加密]",
						" -d 加密值1 加密值2 ...		[解密]",
						" -h|-help 		     		[帮助]",
						"例子：",
						"加密方式: java -jar AesUtil.jar -e 1221332",
						"解密方式: java -jar AesUtil.jar -d Vvi9Cv213+0Q6P5Gpqps4Q==",
				};
				
				for (String c : content) {
				
					System.out.println(c);
				}
				
				System.exit(0);
			}
			
			if(args[0].matches("-e|--e")){
				
				for (int i = 1; i<args.length;i++) {
					
					System.out.println("原始值["+args[i]+"],加密后["+AesUtil.encrypt(DEFAULT_KEY, args[i])+"]");
				}
			}
			
			if(args[0].matches("-d|--d")){
				
				for (int i = 1; i<args.length;i++) {
					
					System.out.println("加密值["+args[i]+"],解密后["+AesUtil.decrypt(DEFAULT_KEY, args[i])+"]");
				}
			}
		} catch (Exception e) {
		
			System.err.println("您输入的有误，请检查,错误栈如下：");
			
			e.printStackTrace();
		}
		
	}
	
}
