package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_REPAYMENT_INFORMATION;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/25 <br>
 * Time:下午6:27 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
@Sql("classpath:test/api/modifyRepaymentInfo/testModifyRepaymentInformation.sql")
public class ModifyRepaymentInformationControllerTest {
    private HttpHeaders headers;
    private MultiValueMap<String, String> requestBody;
    private String privateKey;
    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

        headers = new BaseUnitTest().initHttpHeaders();
    }

    @After
    public void tearDown() throws Exception {
        privateKey = null;
        requestBody = null;
        headers = null;
    }

    @Test
    public void testModifyRepaymentInformation() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10102");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "中国工商银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
        mapParam.put("requestNo", UUID.randomUUID().toString());

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    @Test
    public void testModifyRepaymentInformation_INVALID_PARAMS() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
        mapParam.put("requestNo", UUID.randomUUID().toString());

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("请必填编号"));
    }

    @Test
    public void testModifyRepaymentInformation_Required_UniqueId_Or_ContractNo() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10641");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "奥地利中央合作银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("requestNo", UUID.randomUUID().toString());

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("请选填其中一种编号[uniqueId，contractNo]"));
    }

    @Test
    public void testModifyRepaymentInformation_Required_RequestNo() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10641");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "奥地利中央合作银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("请求唯一编号"));
    }

    @Test
    @Sql("classpath:test/api/modifyRepaymentInfo/testModifyRepaymentInformation_repeat_RequestNo.sql")
    public void testModifyRepaymentInformation_REPEAT_REQUEST_NO() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10641");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "奥地利中央合作银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
        mapParam.put("requestNo", "123456789");

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPEAT_REQUEST_NO.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPEAT_REQUEST_NO.getMessage()));
    }

    @Test
    public void testModifyRepaymentInformation_NO_MATCH_BANK() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "中国建设银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
        mapParam.put("requestNo", "123456789");

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.NO_MATCH_BANK.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.NO_MATCH_BANK.getMessage()));
    }

    @Test
    public void testModifyRepaymentInformation_NO_MATCH_PROVINCE() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10102");
        mapParam.put("bankProvince", "550000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "中国工商银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
        mapParam.put("requestNo", UUID.randomUUID().toString());

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.NO_MATCH_PROVINCE.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.NO_MATCH_PROVINCE.getMessage()));
    }

    @Test
    public void testModifyRepaymentInformation_NO_MATCH_CITY() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10102");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141200");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "中国工商银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
        mapParam.put("requestNo", UUID.randomUUID().toString());

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.NO_MATCH_CITY.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.NO_MATCH_CITY.getMessage()));
    }

    @Test
    public void testModifyRepaymentInformation_CONTRACT_NOT_EXIST() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10640");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "中国建设银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("requestNo", "123456789");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522480)");

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.CONTRACT_NOT_EXIST.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.CONTRACT_NOT_EXIST.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/modifyRepaymentInfo/testModifyRepaymentInformation_fail_to_modify.sql")
    public void testModifyRepaymentInformation_FAIL_TO_MODIFY() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("bankCode", "C10102");
        mapParam.put("bankProvince", "500000");
        mapParam.put("bankCity", "141000");
        mapParam.put("bankAccount", "sdfghjkl");
        mapParam.put("bankName", "中国工商银行");
        mapParam.put("payerName", "测试用户1");
        mapParam.put("contractNo", "云信信2016-78-DK(ZQ2016042522479)");
        mapParam.put("requestNo", UUID.randomUUID().toString());

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("bankCode", mapParam.get("bankCode"));
        requestBody.add("bankProvince", mapParam.get("bankProvince"));
        requestBody.add("bankCity", mapParam.get("bankCity"));
        requestBody.add("bankAccount", mapParam.get("bankAccount"));
        requestBody.add("bankName", mapParam.get("bankName"));
        requestBody.add("payerName", mapParam.get("payerName"));
        requestBody.add("contractNo", mapParam.get("contractNo"));
        requestBody.add("requestNo", mapParam.get("requestNo"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_INFORMATION, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FAIL_TO_MODIFY.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FAIL_TO_MODIFY.getMessage()));
    }
}