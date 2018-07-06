package com.zufangbao.earth.api.test.post;

import com.zufangbao.sun.utils.JsonUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FanT on 2017/5/26.
 */
public class commandSignUpApiPost extends BaseApiTestPost {
    @Test
    public void testSignUp() {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("tranType","27");
        requestParams.put("opType","1");
        requestParams.put("proNo", "fantengwar157");
        requestParams.put("productCode","AVICTC2017X0143");
        requestParams.put("bankAliasName","ABC");
//        requestParams.put("bankFullName","中国工商银行股份有限公司北京中石化大厦支行");
        requestParams.put("payType","B2C");
//        requestParams.put("paymentInstitution","10");//10:建行   11:通联
        requestParams.put("accName","高渐3");
        requestParams.put("accNo","6214855712122200");
        requestParams.put("certType","1");
        requestParams.put("certId","410482198601099182");
        requestParams.put("phoneNo","18512234564");
        requestParams.put("proBeg","20170612");//05201712
        requestParams.put("proEnd","20180612");//20191019
        requestParams.put("tranMaxAmt","50000");
        requestParams.put("remark","123321");
        requestParams.put("signData","2019ss");//20191019
        requestParams.put("merId","systemdeduct");
        requestParams.put("secret","628c8b28bb6fdf5c5add6f18da47f1a6");

        System.out.println(JsonUtils.toJsonString(requestParams));


        try {
            String sr = PostTestUtil.sendPost("http://avictctest.5veda.net/pre/api/zhonghang/zhonghang/sign-up", requestParams, getIdentityInfoMap(requestParams)); //http://avictctest.5veda.net:8888/pre/api/sign-up
           // avictctest.5veda.net
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void querySignUp() {
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("productCode","11111111");
        requestParams.put("accNo","6214855712117980");
//        requestParams.put("proNo","fantengwar220");



        System.out.println(JsonUtils.toJsonString(requestParams));


        try {
            String sr = PostTestUtil.sendPost("http://avictctest.5veda.net/pre/api/sign_up/query", requestParams, getIdentityInfoMap(requestParams)); //http://avictctest.5veda.net:8888/pre/api/sign-up
            // avictctest.5veda.net
            System.out.println(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
