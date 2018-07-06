package com.suidifu.watchman.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 Utils
 *
 * @author louguanyang at 2018/1/4 16:48
 * @mail louguanyang@hzsuidifu.com
 */
@Slf4j
public class Md5Utils {

    /**
     * MD5
     */
    private static final String MD5 = "MD5";
    /**
     * UTF-8 编码
     */
    private static final String UTF_8 = "UTF-8";
    /**
     * 十六进制255
     */
    private static final int HEX_255 = 0xff;
    /**
     * 十六进制256
     */
    private static final int HEX_256 = 0x100;
    /**
     * 默认循环次数
     */
    private static final int DEFAULT_HASH_ITERATIONS = 1;

    private Md5Utils() {
        throw new IllegalStateException("util class");
    }

    /**
     * MD5加密(不加盐,循环一次)
     *
     * @param value 待Md5字符串
     * @return Md5后字符串
     */
    public static String md5(String value) {
        return md5(value, DEFAULT_HASH_ITERATIONS);
    }

    /**
     * MD5加密(加盐,循环一次)
     *
     * @param value 待Md5字符串
     * @param salt  盐
     * @return Md5后字符串
     */
    public static String md5(String value, String salt) {
        return md5(value, salt, DEFAULT_HASH_ITERATIONS);
    }

    /**
     * Md5循环加密(不加盐)
     *
     * @param value          待Md5字符串
     * @param hashIterations 循环次数
     * @return Md5后字符串
     */
    public static String md5(String value, int hashIterations) {
        return md5(value, null, hashIterations);
    }

    /**
     * Md5循环加密(加盐)
     *
     * @param value          待Md5字符串
     * @param salt           盐
     * @param hashIterations 循环次数
     * @return Md5后字符串
     */
    public static String md5(String value, String salt, int hashIterations) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("called method [{}] with [{},{},{}]",
                        Thread.currentThread().getStackTrace()[1].getMethodName(), value, salt, hashIterations);
            }
            int iterations = Math.max(DEFAULT_HASH_ITERATIONS, hashIterations);
            byte[] digest = hash(value, salt, iterations);
            StringBuilder sb = new StringBuilder();
            for (byte aDigest : digest) {
                int intValue = (aDigest & HEX_255) + HEX_256;
                String hexString = Integer.toHexString(intValue);
                sb.append(hexString.substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("called method [{}] occur error",
                    Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * hash 处理
     *
     * @param value          待Md5字符串
     * @param salt           盐
     * @param hashIterations 循环次数
     * @return hash串
     * @throws NoSuchAlgorithmException     算法错误
     * @throws UnsupportedEncodingException 编码错误
     */
    private static byte[] hash(String value, String salt, int hashIterations)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (StringUtils.isEmpty(value)) {
            return new byte[0];
        }
        MessageDigest digest = MessageDigest.getInstance(MD5);
        if (StringUtils.isNotEmpty(salt)) {
            digest.reset();
            digest.update(salt.getBytes(UTF_8));
        }

        byte[] hashed = digest.digest(value.getBytes(UTF_8));
        int iterations = hashIterations - 1;

        for (int i = 0; i < iterations; ++i) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return hashed;
    }

}
