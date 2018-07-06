package com.zufangbao.earth.api.test.post;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author dafuchen
 *         2018/3/14
 */
public class KeyUtil {
    private static final String ALGORITHM_FOR_SIGNATURE = "MD5withRSA";
    private static final String PROVIDER = "BC";
    private static final String ALGORITHM_FOR_KEY_FACTORY = "RSA";
    private static final String ALGORITHM_FOR_KEY_AES = "AES";

    /**
     * 得到公钥信息
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key)
            throws Exception {
        byte[] decodeKeyBytes = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec (decodeKeyBytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM_FOR_KEY_FACTORY);
        return kf.generatePublic(spec);
    }

    /**
     * 得到私钥信息
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivate(String key)
            throws Exception {

        byte[] decodeKeyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(decodeKeyBytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM_FOR_KEY_FACTORY);
        return kf.generatePrivate(spec);
    }

    /**
     * 验证签名信息
     * @param content
     * @param sign
     * @param rsaPublicKey
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static Boolean verify (String content, String sign, PublicKey rsaPublicKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException
            , SignatureException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Signature signature = Signature.getInstance(ALGORITHM_FOR_SIGNATURE, PROVIDER);
        signature.initVerify(rsaPublicKey);

        byte[] messageByte = content.getBytes();
        signature.update(messageByte);

        return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
    }

    /**
     * 签名
     * @param content
     * @param privateKey
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String sign(String content, PrivateKey privateKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Signature signature = Signature.getInstance(ALGORITHM_FOR_SIGNATURE, PROVIDER);
        signature.initSign(privateKey);
        signature.update(content.getBytes());
        byte[] signed = signature.sign();
        return Base64.getEncoder().encodeToString(signed);
    }

    /**
     * rsa 加密
     * @param data
     * @param publicKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String rsaEncrypt(String data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_FOR_KEY_FACTORY, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * rsa 解密
     * @param data
     * @param privateKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String rsaDecrpt(String data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_FOR_KEY_FACTORY, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

    /**
     * AES 加密
     * @param data
     * @param secret
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static String aesEncrypt(String data, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key key = new SecretKeySpec(secret.getBytes(), ALGORITHM_FOR_KEY_AES);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_FOR_KEY_AES, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * AES 解密
     * @param data
     * @param secret
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String aesDescrypt(String data, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key key = new SecretKeySpec(secret.getBytes(), ALGORITHM_FOR_KEY_AES);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_FOR_KEY_AES, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

}
