package com.suidifu.morganstanley.utils;

import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 接口签名工具类
 *
 * @author louguanyang
 */
@Log4j2
public class ApiSignUtils {
    /**
     * MD5withRSA 签名方式
     */
    private static final String MD5_WITH_RSA = "MD5withRSA";
    /**
     * RSA
     */
    private static final String RSA = "RSA";
    /**
     * 字符 “&”
     */
    private static final String AND = "&";
    /**
     * 字符 “=”
     */
    private static final String EQUAL = "=";
    /**
     * 字符 “”
     */
    private static final String BLANK = StringUtils.EMPTY;
    /**
     * 文件
     */
    private static final String FILE = "file";

    /**
     * 私有构造方法
     */
    private ApiSignUtils() {
    }

    /**
     * 请求参数签名验证
     *
     * @param requestParams 请求参数
     * @param request       Http请求
     * @param merConfig     api请求商户配置表
     * @return 验证结果
     */
    public static boolean verifySign(Map<String, String> requestParams, HttpServletRequest request, TMerConfig merConfig) {
        String content = getSignCheckContent(requestParams);
        if (StringUtils.isEmpty(content)) {
            log.error("验签失败，sign签名为空，或者商户未上传公钥！");
            return false;
        }
        return verifySign(content, request, merConfig);
    }

    /**
     * 字符串签名验证
     *
     * @param content   待验签字符串
     * @param request   Http请求
     * @param merConfig api请求商户配置表
     * @return 验证结果
     */
    private static boolean verifySign(String content, HttpServletRequest request, TMerConfig merConfig) {
        log.info("ip［{}］", IpUtil.getIpAddress(request));
        log.info("内容验签: content［{}］", content);
        if (merConfig == null) {
            log.error("验签失败，未找到到merId所关联商户！");
            return false;
        }
        String publicKey = merConfig.getPubKey();
        String sign = request.getHeader(ApiConstant.PARAMS_SIGN);
        return rsaCheckContent(content, sign, publicKey);
    }

    /**
     * MD5_WITH_RSA数字签名验证
     *
     * @param content   验签内容
     * @param sign      签名
     * @param publicKey 公钥
     * @return 验证结果
     */
    public static boolean rsaCheckContent(String content, String sign, String publicKey) {
        try {
            if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(publicKey)) {
                log.error("验签失败，sign签名为空，或者商户未上传公钥！");
                return false;
            }
            log.info("sign［{}］", sign);
            PublicKey pubKey = getPublicKey(publicKey);
            Signature signature = Signature.getInstance(MD5_WITH_RSA);
            signature.initVerify(pubKey);

            byte[] contentBytes = content.getBytes();
            if (ArrayUtils.isEmpty(contentBytes)) {
                log.error("内容: content［无法转换为byte数组］");
                return false;
            }

            signature.update(content.getBytes());
            return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥
     * @return 公钥类
     */
    private static PublicKey getPublicKey(String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("exception message is {}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static String signAndReturnResult(HttpServletResponse response, String result, String privateKey) {
        String sign = rsaSign(result, privateKey);
        if (!StringUtils.isEmpty(sign)) {
            response.addHeader(ApiConstant.PARAMS_SIGN, sign);
        }
        return result;
    }

    /**
     * 将RSA算法生成的sign放入response的Header
     *
     * @param response   http响应对象
     * @param result     http响应返回的json字符串
     * @param privateKey 生成sign所需的私钥
     */
    public static void sign2Header(HttpServletResponse response, String result, String privateKey) {
        String sign = rsaSign(result, privateKey);
        if (!StringUtils.isEmpty(sign)) {
            response.addHeader(ApiConstant.PARAMS_SIGN, sign);
        }
    }

    /**
     * RSA 签名
     *
     * @param content    验签内容
     * @param privateKey 私钥
     * @return RSA签名字符串
     */
    public static String rsaSign(String content, String privateKey) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(RSA, privateKey);
            Signature signature = Signature.getInstance(MD5_WITH_RSA);

            signature.initSign(priKey);
            signature.update(content.getBytes());

            byte[] signed = signature.sign();
            return new String(Base64.getEncoder().encode(signed));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.error("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥！");
        }

        return null;
    }

    /**
     * 获取PKCS8格式私钥
     *
     * @param algorithm  加密算法
     * @param privateKey 私钥
     * @return 私钥类
     */
    private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, String privateKey) {
        try {
            return KeyFactory.getInstance(algorithm).generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥！");
        }
        return null;
    }

    /**
     * 从请求参数中获取待签名字符串
     *
     * @param params 请求参数
     * @return 待签名字符串
     */
    public static String getSignCheckContent(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            log.warn("params is null or empty.");
            return BLANK;
        }

        List<String> keys = new ArrayList<>(params.keySet());
        if (CollectionUtils.isEmpty(keys)) {
            log.warn("params keySet list is empty.");
            return BLANK;
        }

        StringBuilder content = new StringBuilder();
        Collections.sort(keys);
        for (String key : keys) {
            if (key.startsWith(FILE)) {
                log.warn("key contains file, skip.");
                continue;
            }
            String value = params.get(key);
            value = StringUtils.isEmpty(value) ? BLANK : value;
            content.append(AND).append(key).append(EQUAL).append(value);
        }
        String result = content.toString();
        return result.substring(1, result.length());
    }
}