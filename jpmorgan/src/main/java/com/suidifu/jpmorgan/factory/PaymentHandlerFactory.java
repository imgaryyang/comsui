package com.suidifu.jpmorgan.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.util.SpringContextUtil;

public class PaymentHandlerFactory {


    public static String GATEWAY_TYPE_DIRECT_BANK_CMB = "GATEWAY_TYPE_DIRECT_BANK_CMB";
    public static String GATEWAY_TYPE_DIRECT_BANK_PAB = "GATEWAY_TYPE_DIRECT_BANK_PAB";
    public static String GATEWAY_TYPE_DIRECT_BANK_SPDB = "GATEWAY_TYPE_DIRECT_BANK_SPDB";
    public static String GATEWAY_TYPE_UNIONPAY_GZ = "GATEWAY_TYPE_UNIONPAY_GZ";
    public static String GATEWAY_TYPE_ElECPAY_BAOFU = "GATEWAY_TYPE_ElECPAY_BAOFU";
    public static String GATEWAY_TYPE_FAST_PAY_PAB = "GATEWAY_TYPE_FAST_PAY_PAB";
    public static String GATEWAY_TYPE_ElECPAY_CPCN = "GATEWAY_TYPE_ElECPAY_CPCN";
    public static String GATEWAY_TYPE_DIRECT_BANK_CCB = "GATEWAY_TYPE_DIRECT_BANK_CCB";
    public static String GATEWAY_TYPE_CCBPAY_SX = "GATEWAY_TYPE_CCBPAY_SX";
    public static String GATEWAY_TYPE_ElECPAY_TONGLIAN = "GATEWAY_TYPE_ElECPAY_TONGLIAN";
    public static String GATEWAY_TYEP_YEEPAY = "GATEWAY_TYEP_YEEPAY";
    public static String GATEWAY_TYPE_DIRECT_BANK_HUAXING = "GATEWAY_TYPE_DIRECT_BANK_HUAXING";
    public static String GATEWAY_TYPE_DIRECT_BANK_CMBC = "GATEWAY_TYPE_DIRECT_BANK_CMBC";
    public static String GATEWAY_TYPE_DIRECT_BANK_BOSC = "GATEWAY_TYPE_DIRECT_BANK_BOSC";
    public static String GATEWAY_TYPE_DIRECT_BANK_NJBC = "GATEWAY_TYPE_DIRECT_BANK_NJBC";
    public static String GATEWAY_TYPE_ElECPAY_LDYS = "GATEWAY_TYPE_ElECPAY_LDYS";
    public static String GATEWAY_TYPE_DIRECT_BANK_WFCCB = "GATEWAY_TYPE_DIRECT_BANK_WFCCB";
    public static String GATEWAY_TYPE_EPAY = "GATEWAY_TYPE_EPAY";



    public static String GATEWAY_TYPE_SUPER_BANK_TEST = "GATEWAY_TYPE_SUPER_BANK_TEST";
    public static String GATEWAY_TYPE_UNIONPAY_TEST = "GATEWAY_TYPE_UNIONPAY_TEST";
    public static String GATEWAY_TYPE_DIRECTBANK_TEST = "GATEWAY_TYPE_DIRECTBANK_TEST";


    private final static Map<String, String> bankCodeSuperBankHandlerBeanNameMapper = new HashMap<String, String>() {

        {

            put(GATEWAY_TYPE_SUPER_BANK_TEST, "TestPaymentGateWaySuperBankHandler");
            put(GATEWAY_TYPE_UNIONPAY_TEST, "TestPaymentGateWayUnionpayHandler");


            put(GATEWAY_TYPE_DIRECT_BANK_CMB, "cmbDirectBankPaymentHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_PAB, "pabDirectBankPaymentHandler");
            put(GATEWAY_TYPE_UNIONPAY_GZ, "gzUnionPaymentHandlerImpl");
            put(GATEWAY_TYPE_ElECPAY_BAOFU, "baofuPaymentHandler");
            put(GATEWAY_TYPE_FAST_PAY_PAB, "pabFastPayPaymentHandler");
            put(GATEWAY_TYPE_ElECPAY_CPCN, "cpcnPaymentHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_CCB, "ccbDirectBankPaymentHandler");
            put(GATEWAY_TYPE_CCBPAY_SX, "sxCcbPaymentHandler");
            put(GATEWAY_TYPE_ElECPAY_TONGLIAN, "tongLianPaymentHandler");
            put(GATEWAY_TYEP_YEEPAY, "yeepayHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_SPDB, "spdbUniversalPaymentHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_HUAXING, "huaxingDirectBankPaymentHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_CMBC, "cmbcDirectBankPaymentHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_BOSC, "boscPaymentHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_NJBC, "njbcDirectBankPaymentHandler");
            put(GATEWAY_TYPE_ElECPAY_LDYS, "ldysPaymentHandler");
            put(GATEWAY_TYPE_DIRECT_BANK_WFCCB, "weiFangBankDirectBankPaymentHandler");
            put(GATEWAY_TYPE_EPAY, "epayHandler");
        }
    };

    public static <T extends PaymentHandler> T newInstance(String gatewayName) {
        String beanName = bankCodeSuperBankHandlerBeanNameMapper.getOrDefault(
                gatewayName, "");
        if (StringUtils.isEmpty(beanName)) return null;
        return SpringContextUtil.getBean(beanName);
    }
}
