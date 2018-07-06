package com.zufangbao.earth.api.test.post;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by FanT on 2017/8/16.
 */
public class CommandPaymentOrderPayOnlineApiPost extends BaseApiTestPost{
    @Test
    public void paymentOrderPayOnline(){
        Map<String,String> requestParams = new HashMap<>();
        requestParams.put("requestNo", UUID.randomUUID().toString()); //请求编号
        requestParams.put("paymentNo",UUID.randomUUID().toString()); //商户支付编号
        requestParams.put("orderUniqueId","pufa0000000018"); //商户订单号
//        requestParams.put("orderUuid","2609a704-efce-4cfe-98da-786b7f1e44d0"); //五维订单号
        requestParams.put("financialContractNo","G31700"); //信托产品代码
        requestParams.put("payWay","0"); //支付方式 0：线下转账  2：线上代扣  3：商户代扣
        requestParams.put("paymentAccountNo","6217857600016839339"); //付款银行帐户号
        requestParams.put("paymentAccountName","秦萎超"); //付款银行帐户名称
        requestParams.put("paymentBankCode","C10104"); //付款银行名称编码
        requestParams.put("paymentProvinceCode","620000"); //付款方所在地省编码
        requestParams.put("paymentCityCode","141100"); //付款方所在地市编码
        requestParams.put("idCardNum","421182199204114115"); //付款方身份证
        requestParams.put("mobile","17682481004"); //付款方手机号
        requestParams.put("paymentGateWay","0"); //支付通道
        requestParams.put("tradeUuid","159038420787503214"); //通道交易号
        requestParams.put("amount","100080"); //交易金额
        requestParams.put("transactionTime","2017-09-21 16:48:11"); //交易时间
//        requestParams.put("receivableAccountNo","6001"); //收款方账号
//        requestParams.put("receivableAccountName","sdf"); //收款方户名
//        requestParams.put("receivableBankCode","C10103");//收款方开户行
//        requestParams.put("notifyUrl","http://www.qq.com"); //回调地址






        try {
            String sr = PostTestUtil.sendPost(PAYMENTORDERONLINE, requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
