package com.suidifu.bridgewater.api.model;

import com.demo2do.core.utils.StringUtils;
import com.suidifu.bridgewater.api.entity.BusinessErrorReason;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsh2014 on 17-5-8.
 */
public class DeductErrorCodeMapSpec {
    public static final Map<String,BusinessErrorReason> BaoFuErrorCodeAndErrorReasonMap = new HashMap<String,BusinessErrorReason>(){{
        //持卡人信息有误
        put("BF00101",BusinessErrorReason.OTHERS);
        //银行卡已过有效期，请联系发卡行
        put("BF00102",BusinessErrorReason.OTHERS);
        //账户余额不足
        put("BF00103",BusinessErrorReason.INSUFFICIENTBALANCE);
        //交易金额超限
        put("BF00104",BusinessErrorReason.OTHERS);
        //短信验证码错误
        put("BF00105",BusinessErrorReason.OTHERS);
        //短信验证码失效
        put("BF00106",BusinessErrorReason.OTHERS);
        //当前银行卡不支持该业务，请联系发卡行
        put("BF00107",BusinessErrorReason.OTHERS);
        //交易失败，请联系发卡行
        put("BF00108",BusinessErrorReason.OTHERS);
        //交易金额低于限额
        put("BF00109",BusinessErrorReason.OTHERS);
        //该卡暂不支持此交易
        put("BF00110",BusinessErrorReason.OTHERS);

        //交易失败
        put("BF00111",BusinessErrorReason.OTHERS);
        //该终端号不存在
        put("BF00116",BusinessErrorReason.OTHERS);
        //报文中密文解析失败
        put("BF00118",BusinessErrorReason.OTHERS);

        //报文交易要素缺失
        put("BF00120",BusinessErrorReason.OTHERS);
        //报文交易要素格式错误
        put("BF00121",BusinessErrorReason.OTHERS);
        //卡号和支付通道不匹配
        put("BF00122",BusinessErrorReason.OTHERS);
        //商户不存在或状态不正常，请联系宝付
        put("BF00123",BusinessErrorReason.OTHERS);
        //商户与终端号不匹配
        put("BF00124",BusinessErrorReason.OTHERS);
        //商户该终端下未开通此类型交易
        put("BF00125",BusinessErrorReason.OTHERS);
        //该笔订单已存在
        put("BF00126",BusinessErrorReason.OTHERS);
        //不支持该支付通道的交易
        put("BF00127",BusinessErrorReason.OTHERS);
        //该笔订单不存在
        put("BF00128",BusinessErrorReason.OTHERS);
        //密文和明文中参数【%s】不一致,请确认是否被篡改！
        put("BF00129",BusinessErrorReason.OTHERS);

        //请确认是否发送短信,当前交易必须通过短信验证！
        put("BF00130",BusinessErrorReason.OTHERS);
        //当前交易信息与短信交易信息不一致,请核对信息
        put("BF00131",BusinessErrorReason.OTHERS);
        //短信验证超时，请稍后再试
        put("BF00132",BusinessErrorReason.OTHERS);
        //短信验证失败
        put("BF00133",BusinessErrorReason.OTHERS);
        //绑定关系不存在
        put("BF00134",BusinessErrorReason.OTHERS);
        //交易金额不正确
        put("BF00135",BusinessErrorReason.OTHERS);
        //订单创建失败
        put("BF00136",BusinessErrorReason.OTHERS);

        //该卡已被注销
        put("BF00140",BusinessErrorReason.OTHERS);
        //该卡已挂失
        put("BF00141",BusinessErrorReason.OTHERS);
        //订单金额超过单笔限额
        put("BF00146",BusinessErrorReason.OTHERS);
        //该银行卡不支持此交易
        put("BF00147",BusinessErrorReason.OTHERS);

        //非法的交易
        put("BF00177",BusinessErrorReason.OTHERS);
        //获取短信验证码失败
        put("BF00180",BusinessErrorReason.OTHERS);
        //您输入的银行卡号有误，请重新输入
        put("BF00182",BusinessErrorReason.CARDNOERROR);
        //暂不支持信用卡的绑定
        put("BF00187",BusinessErrorReason.OTHERS);
        //绑卡失败
        put("BF00188",BusinessErrorReason.OTHERS);

        //商户流水号不能重复
        put("BF00190",BusinessErrorReason.OTHERS);
        //订单日期格式不正确
        put("BF00199",BusinessErrorReason.OTHERS);

        //发送短信和支付时商户订单号不一致
        put("BF00200",BusinessErrorReason.OTHERS);
        //发送短信和支付交易时金额不相等
        put("BF00201",BusinessErrorReason.OTHERS);
        //退款交易已受理
        put("BF00203",BusinessErrorReason.OTHERS);
        //确认绑卡时与预绑卡时的商户订单号不一致
        put("BF00204",BusinessErrorReason.OTHERS);

        //银行卡未开通认证支付
        put("BF00232",BusinessErrorReason.OTHERS);
        //密码输入次数超限，请联系发卡行
        put("BF00233",BusinessErrorReason.OTHERS);
        //单日交易金额超限
        put("BF00234",BusinessErrorReason.OTHERS);
        //单笔交易金额超限
        put("BF00235",BusinessErrorReason.OTHERS);
        //卡号无效，请确认后输入
        put("BF00236",BusinessErrorReason.CARDNOERROR);
        //该卡已冻结，请联系发卡行
        put("BF00237",BusinessErrorReason.CARDNOERROR);
        //订单已过期，请使用新的订单号发起交易
        put("BF00249",BusinessErrorReason.OTHERS);

        //订单未支付
        put("BF00251",BusinessErrorReason.OTHERS);
        //交易拒绝
        put("BF00253",BusinessErrorReason.OTHERS);
        //发送短信验证码失败
        put("BF00255",BusinessErrorReason.OTHERS);
        //请重新获取验证码
        put("BF00256",BusinessErrorReason.OTHERS);
        //手机号码校验失败
        put("BF00258",BusinessErrorReason.OTHERS);

        //短信验证码已过期，请重新发送
        put("BF00260",BusinessErrorReason.OTHERS);
        //短信验证码错误次数超限，请重新获取
        put("BF00261",BusinessErrorReason.OTHERS);
        //交易金额与扣款成功金额不一致，请联系宝付
        put("BF00262",BusinessErrorReason.OTHERS);

        //卡类型和biz_type值不匹配
        put("BF00311",BusinessErrorReason.OTHERS);
        //卡号校验失败
        put("BF00312",BusinessErrorReason.OTHERS);
        //商户未开通此产品
        put("BF00313",BusinessErrorReason.OTHERS);
        //手机号码为空，请重新输入
        put("BF00315",BusinessErrorReason.OTHERS);
        //ip未绑定，请联系宝付
        put("BF00316",BusinessErrorReason.OTHERS);
        //短信验证码已失效，请重新获取
        put("BF00317",BusinessErrorReason.OTHERS);

        //身份证号不合法
        put("BF00321",BusinessErrorReason.OTHERS);
        //卡类型和卡号不匹配
        put("BF00322",BusinessErrorReason.OTHERS);
        //商户未开通交易模版
        put("BF00323",BusinessErrorReason.OTHERS);
        //暂不支持此银行卡支付，请更换其他银行卡或咨询商户客服
        put("BF00324",BusinessErrorReason.OTHERS);
        //非常抱歉！目前该银行正在维护中，请更换其他银行卡支付
        put("BF00325",BusinessErrorReason.SYSTEMMAINTAINING);
        //请联系银行核实您的卡状态是否正常
        put("BF00327",BusinessErrorReason.OTHERS);

        //卡号校验失败
        put("BF00331",BusinessErrorReason.OTHERS);
        //交易失败，请重新支付
        put("BF00332",BusinessErrorReason.OTHERS);
        //该卡有风险，发卡行限制交易
        put("BF00333",BusinessErrorReason.OTHERS);

        //该卡有风险，请持卡人联系银联客服[95516]
        put("BF00341",BusinessErrorReason.OTHERS);
        //单卡单日余额不足次数超限
        put("BF00342",BusinessErrorReason.OTHERS);
        //验证失败（手机号有误）
        put("BF00343",BusinessErrorReason.OTHERS);
        //验证失败（卡号有误）
        put("BF00344",BusinessErrorReason.OTHERS);
        //验证失败（姓名有误）
        put("BF00345",BusinessErrorReason.OTHERS);
        //验证失败（身份证号有误）
        put("BF00346",BusinessErrorReason.OTHERS);
        //交易次数频繁，请稍后重试
        put("BF00347",BusinessErrorReason.OTHERS);

        //该卡本次可支付***元，请更换其他银行卡！
        put("BF08701",BusinessErrorReason.OTHERS);
        //该商户本次可支付***元，请更换其他银行卡或咨询商户客服！
        put("BF08702",BusinessErrorReason.OTHERS);
        //支付金额不能低于最低限额...元！
        put("BF08703",BusinessErrorReason.OTHERS);
        //单笔金额超限，该银行单笔可支付xxx元！
        put("BF08704",BusinessErrorReason.OTHERS);

        //请勿重复提交
        put("BF00373",BusinessErrorReason.OTHERS);
        //暂不支持信用卡交易
        put("BF00372",BusinessErrorReason.OTHERS);

        //验证失败（卡状态异常）
        put("BF00355",BusinessErrorReason.OTHERS);
        //验证失败（该卡当日失败次数超限，请次日再试）
        put("BF00356",BusinessErrorReason.OTHERS);
        //该卡当日交易笔数超过限制，请次日再试！
        put("BF00351",BusinessErrorReason.OTHERS);

        //该卡当日失败次数已超过3次，请次日再试！
        put("BF00350",BusinessErrorReason.OTHERS);
        //交易超时，请重新提交
        put("BF00206",BusinessErrorReason.OTHERS);


    }};
    
    public static final Map<String,BusinessErrorReason> ZhongJinErrorCodeAndErrorReasonMap = new HashMap<String,BusinessErrorReason>(){{
    	put("000004",BusinessErrorReason.INSUFFICIENTBALANCE);
    	put("CS51",BusinessErrorReason.INSUFFICIENTBALANCE);
    	put("000006",BusinessErrorReason.INSUFFICIENTBALANCE);
    	put("51",BusinessErrorReason.INSUFFICIENTBALANCE);
    	put("2051",BusinessErrorReason.INSUFFICIENTBALANCE);
    	put("DK20219",BusinessErrorReason.INSUFFICIENTBALANCE);
    	put("3000",BusinessErrorReason.INSUFFICIENTBALANCE);
        put("1000051",BusinessErrorReason.INSUFFICIENTBALANCE);
        put("CT1O2008",BusinessErrorReason.INSUFFICIENTBALANCE);
        put("YCEA01823113",BusinessErrorReason.INSUFFICIENTBALANCE);
        put("YBLA01928725",BusinessErrorReason.INSUFFICIENTBALANCE);
    }};
    
    public static final Map<String,BusinessErrorReason> GZUionErrorCodeAndErrorReasonMap = new HashMap<String,BusinessErrorReason>(){{
    	put("3008",BusinessErrorReason.INSUFFICIENTBALANCE);//余额不足
    	put("3004",BusinessErrorReason.CARDNOERROR);//无效卡号
    	put("3006",BusinessErrorReason.CARDNOERROR);//已挂失卡
    	put("3009",BusinessErrorReason.CARDNOERROR);//无此账户
    	put("3017",BusinessErrorReason.CARDNOERROR);//账户已冻结
    	put("3028",BusinessErrorReason.SYSTEMMAINTAINING);
    }};

    public static final Map<String, BusinessErrorReason> EPayErrorCodeAndErrorReasonMap = new HashMap<>(1);
    static {
        EPayErrorCodeAndErrorReasonMap.put("4020", BusinessErrorReason.INSUFFICIENTBALANCE); // 余额不足
    }

    public static final Map<PaymentInstitutionName,Map<String,BusinessErrorReason>> PaymentGatewayAndErrorCodeMap = new HashMap<PaymentInstitutionName,Map<String,BusinessErrorReason>>(){{
        put(PaymentInstitutionName.UNIONPAYGZ,GZUionErrorCodeAndErrorReasonMap);
        put(PaymentInstitutionName.BAOFU,BaoFuErrorCodeAndErrorReasonMap);
        put(PaymentInstitutionName.ZHONG_JIN_PAY,ZhongJinErrorCodeAndErrorReasonMap);
        put(PaymentInstitutionName.E_PAY, EPayErrorCodeAndErrorReasonMap);
    }};


    public static BusinessErrorReason getBusinessErrorReason(PaymentInstitutionName PaymentGateway,String errorCode){
        if(null == PaymentGateway || StringUtils.isEmpty(errorCode)){
            return null;
        }
        Map<String,BusinessErrorReason> errorCodeAndErrorReason = PaymentGatewayAndErrorCodeMap.get(PaymentGateway);
        if(null == errorCodeAndErrorReason){
            return null;
        }
        return errorCodeAndErrorReason.get(errorCode);

    }


}
