package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.thirdparty.baofu.SecurityUtil;
import com.zufangbao.cucumber.method.BaseTestMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dzz on 17-5-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"
})
public class HuaRuiApiTest {
    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号
    String productCode="G31700";//信托代码
    String totalAmount="1500";//放款总金额
    String amount="1500";//资产包每期还款计划的钱
    /**
     * 华瑞接口测试
     * */
    @Test
    public void test1(){
        System.out.println("开始放款：");
        //header
        Map<String, String> headers = new HashMap<String, String>();
//		headers.put("merId", "t_test_zfb");
//		headers.put("secret", "123456");
        headers.put("merId", "hr_shoujidai");
        headers.put("secret", "e6bf65978f8ed1d55fc0ef66498861ea");

        Map<String, String> bodyContent = new HashMap<String, String>();
        bodyContent.put("fn", "300002");
        bodyContent.put("requestNo", UUID.randomUUID().toString());
        bodyContent.put("uniqueId", "hr_shoujidai-手机贷测试合同号5");
        bodyContent.put("remittanceStrategy", "0");
        bodyContent.put("productCode", "TEST001");
        bodyContent.put("contractNo", "手机贷测试合同号5");
        bodyContent.put("plannedRemittanceAmount", "0.03");
        bodyContent.put("auditorName", "auditorName1");
        bodyContent.put("auditTime", "2017-03-21 00:00:00");
        bodyContent.put("remark", "交易备注");
        bodyContent.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'0.02','plannedDate':'2016-08-20 00:00:00','bankCode':'C10102','bankCardNo':'5685968545868856','bankAccountHolder':'无邪','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'},{'detailNo':'detailNo2','recordSn':'2','amount':'0.01','plannedDate':'2016-08-20 00:00:00','bankCode':'C10102','bankCardNo':'1234567890','bankAccountHolder':'无邪2','bankProvince':'bankProvince2','bankCity':'bankCity2','bankName':'bankName2','idNumber':'idNumber2'}]");

        String requestBody = JsonUtils.toJsonString(bodyContent);
        System.out.println(requestBody);
        String requestContent = null;
        try {
            requestContent = SecurityUtil.threeDesEncrypt(requestBody, "C4VRT4DBf5jTDoWM00kvQ+oBuauYm7wO");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKiOVgMAgYCawI3ubsNdlj+bmo8TqVdfBokslymjYr4anVE6mk+2W5fpsUI+NHDh99Ca6pPHAnFaGiwaCMxKyVRLLj8Dv1ZakjF9ofRO1qdIzFsd5Ho34yEzfXvAPFEGjQ98eHekl3W0ibuy8/cqWdImgK2/HebW2lCmwKXIL2T7AgMBAAECgYAvus6EXxpMzoWEK2ZWECRBstBbf5fOU+xH38aWVdvaNTMjE1MrC1p4dAZr2a2D4ZkJCzUtz4bTjk4m+uBO6UaFEB3880bRMxpoV/0NgX7Mw9P4ZtyIvWBA6gss5mP0lLUW+LOmkuG9RHIacgwd5hsragmb48irLebElLo2B4ldcQJBAOBnZfnbldrxmR+nWdm+fHYiACpeTPzylyJuul+zduPNdnHpE/xa9fmWuWSqgBJbXw4ObISabri6QMVCAkQkXs0CQQDASejGTKOyUGasbFxKmlRWAeR/23zotcxGzNTxnUEzJBUAl4KwCiyZ/SZHr3aRA0/Y8xHWtz2l+Z+nrzZ8oULnAkEAwSqAb6oDvypFIyhNgdAlFxOBjPcL0pmIW41xRTwY1VNh6AA4wfwSzLxf7jWOdT7N/i2QYD0HnmBFrQd7J+ke6QJAA6xlygQEzI9Ept6bFMHGAq6ekuK6jCeM831ORB9g830o+Y3rbTmEiJyRcqUjerm5eMeL+7Icd2NBbY6nV9Is2QJACKPst7u2nVuQ88KvgRwFMDLmuw+CutLTT5D0816eRlfGsibkrmtcm5E+l8MIDcFKqP1hASAR2qxol8M+3F9gaw==";
//        String sign = ApiSignUtils.rsaSign(requestContent, privateKey);
//        headers.put("sign", sign);
//
//        System.out.println(requestContent);
//        System.out.println(sign);

        try {
            //Result result = PostTestUtil.sendPostWithEncryption(HUARUI_COMMAND_URL_TEST, headers, requestContent);
           // System.out.println(JsonUtils.toJsonString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
