package com.suidifu.morganstanley.controller.api.repayment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.request.repayment.RequestData;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.sun.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_REPAYMENT_PLAN;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/25 <br>
 * Time:下午6:12 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
@Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlan.sql")
public class ModifyRepaymentPlanControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private List<RequestData> requestDataModels = new ArrayList<>();
    private HttpHeaders headers;
    private MultiValueMap<String, String> requestBody;
    private String privateKey;
    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

        RequestData RequestData1 = new RequestData();
        RequestData RequestData2 = new RequestData();
        RequestData RequestData3 = new RequestData();

        RequestData1
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
        RequestData2
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
        RequestData3
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

        RequestData1.setAssetPrincipal(new BigDecimal("70000.00"));
        RequestData2.setAssetPrincipal(new BigDecimal("50000.00"));
        RequestData3.setAssetPrincipal(new BigDecimal("40000.00"));

        requestDataModels = new ArrayList<>();
        requestDataModels.add(RequestData1);
        requestDataModels.add(RequestData2);
        requestDataModels.add(RequestData3);

        headers = new BaseUnitTest().initHttpHeaders();
    }

    @After
    public void tearDown() throws Exception {
        requestDataModels = null;
        requestBody = null;
        headers = null;
    }

    @Test
    public void testModifyRepaymentPlan() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    /**
     * uniqueId与contract 为空 或 为null 或 两者都存在
     */
    @Test
    public void testModifyRepaymentPlan_uniqueId_contractNo_empty_or_null() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("requestReason", "2");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), containsString("请选填其中一种编号[uniqueId，contractNo"));
    }

    /**
     * requestNo为空或null
     */
    @Test
    public void testModifyRepaymentPlan_requestNo_empty_or_null() throws Exception {
        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "2");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), containsString("请求唯一编号"));
    }

    /**
     * requestReason为空或null
     */
    @Test
    public void testModifyRepaymentPlan_requestReason_empty_or_null() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        mapParam.put("financialProductCode", "G32003");
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestData", mapParam.get("requestData"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), containsString("请求原因[requestReason]不能为空"));
    }

    /**
     * 请求原因不合法（<0 或 >2)
     */
    @Test
    public void testModifyRepaymentPlan_requestReason_illegal() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "11");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), containsString("请求原因[requestReason]内容不合法"));
    }

    /**
     * 请求原因不是数字
     */
    @Test
    public void testModifyRepaymentPlan_requestReason_is_not_number() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "abc");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), containsString("请求原因[requestReason]内容不合法"));
    }

    /**
     * requestData为null或空
     */
    @Test
    public void testModifyRepaymentPlan_requestData_empty_or_null() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "2");
        mapParam.put("financialProductCode", "G32003");
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), containsString("具体变更内容[requestData]不能为空"));
    }

    /**
     * 计划本金非法
     */
    @Test
    public void testModifyRepaymentPlan_INVALID_PRINCIPAL_AMOUNT() throws Exception {
        RequestData requestDataModel = new RequestData();
        requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
        requestDataModel.setAssetPrincipal(new BigDecimal("-1999.00"));
        requestDataModel.setAssetInterest(new BigDecimal("20.00"));

        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(requestDataModel);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PRINCIPAL_AMOUNT.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.INVALID_PRINCIPAL_AMOUNT.getMessage()));
    }

    /**
     * 计划利息非法
     */
    @Test
    public void testModifyRepaymentPlan_INVALID_INTEREST_AMOUNT() throws Exception {
        RequestData requestDataModel = new RequestData();
        requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
        requestDataModel.setAssetPrincipal(new BigDecimal("1999.00"));
        requestDataModel.setAssetInterest(new BigDecimal("-20.00"));

        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(requestDataModel);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("无效的计划利息总额"));
    }

    /**
     * 计划还款日期错误
     */
    @Test
    public void testModifyRepaymentPlan_WRONG_ASSET_RECYCLE_DATE() throws Exception {
        RequestData requestDataModel = new RequestData();
        requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), -5)));
        requestDataModel.setAssetPrincipal(new BigDecimal("1999.00"));
        requestDataModel.setAssetInterest(new BigDecimal("20.00"));

        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(requestDataModel);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.WRONG_ASSET_RECYCLE_DATE.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.WRONG_ASSET_RECYCLE_DATE.getMessage()));
    }

    /**
     * 计划变更原因为提前结清或提前部分还款时，还款计划类型应为提前
     */
    @Test
    public void testModifyRepaymentPlan_ASSET_TYPE_SHOULD_BEFORE() throws Exception {
        RequestData requestDataModel = new RequestData();
        requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));
        requestDataModel.setAssetPrincipal(new BigDecimal("160000.00"));

        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(requestDataModel);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "1");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.ASSET_TYPE_SHOULD_BEFORE.getCode()));
        assertThat(resultResponse.getMessage(), containsString("计划变更原因为提前结清或提前部分还款时，还款计划类型应为提前"));
    }

    /**
     * 计划变更原因不是提前结清和提前部分还款时，还款计划类型不能为提前
     */
    @Test
    public void testModifyRepaymentPlan_ASSET_TYPE_SHOULD_NOT_BEFORE() throws Exception {
        RequestData requestDataModel = new RequestData();
        requestDataModel.setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));
        requestDataModel.setAssetPrincipal(new BigDecimal("160000.00"));
        requestDataModel.setAssetType(0);//设置还款计划类型为提前
        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(requestDataModel);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.ASSET_TYPE_SHOULD_NOT_BEFORE.getCode()));
        assertThat(resultResponse.getMessage(), containsString("计划变更原因不是提前结清和提前部分还款时，还款计划类型不能为提前"));
    }

    /**
     * 提前结清时未偿还款计划只能有一条
     */
    @Test
    public void testModifyRepaymentPlan_ONLY_ONE_ASSETSET_WHEN_PRE_CLEAR() throws Exception {
        RequestData RequestData1 = new RequestData();
        RequestData RequestData2 = new RequestData();
        RequestData RequestData3 = new RequestData();

        RequestData1
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
        RequestData2
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
        RequestData3
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

        RequestData1.setAssetPrincipal(new BigDecimal("70000.00"));
        RequestData2.setAssetPrincipal(new BigDecimal("50000.00"));
        RequestData3.setAssetPrincipal(new BigDecimal("40000.00"));
        RequestData1.setAssetInterest(new BigDecimal("16800.00"));
        RequestData2.setAssetInterest(new BigDecimal("12000.00"));
        RequestData3.setAssetInterest(new BigDecimal("9600.00"));

        RequestData1.setAssetType(0);
        RequestData2.setAssetType(0);
        RequestData3.setAssetType(0);

        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(RequestData1);
        requestDataModels.add(RequestData2);
        requestDataModels.add(RequestData3);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "1");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.ONLY_ONE_ASSETSET_WHEN_PRE_CLEAR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.ONLY_ONE_ASSETSET_WHEN_PRE_CLEAR.getMessage()));
    }

    /**
     * 计划还款日期不能晚于贷款合同终止日期后108天
     */
    @Test
    @Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlanCustomerCheckFail.sql")
    public void testModifyRepaymentPlan_ASSET_RECYCLE_DATE_TOO_LATE() throws Exception {
        RequestData RequestData = new RequestData();
        Date contractEndDate = DateUtils.parseDate("2017-12-09", "yy-MM-dd");
        RequestData
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(contractEndDate,109)));
        RequestData.setAssetPrincipal(new BigDecimal("160000.00"));
        RequestData.setAssetInterest(new BigDecimal("21226.67"));
        RequestData.setAssetType(0);

        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(RequestData);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "1");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.ASSET_RECYCLE_DATE_TOO_LATE.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.ASSET_RECYCLE_DATE_TOO_LATE.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlan_repeat_RepayScheduleNo.sql")
    public void testModifyRepaymentPlan_REPEAT_OUTER_REPAYMENT_PLAN_NO() throws Exception {
        RequestData RequestData1 = new RequestData();

        RequestData1
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.asDay("2016-12-10"),358)));

        RequestData1.setAssetPrincipal(new BigDecimal("160000.00"));
        RequestData1.setAssetInterest(new BigDecimal("38186.67"));

        RequestData1.setAssetType(0);

        RequestData1.setRepayScheduleNo("repayScheduleNo1");

        List<RequestData> requestDataModels = new ArrayList<>();
        requestDataModels.add(RequestData1);


        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "1");
        mapParam.put("financialProductCode", "G32000");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPEAT_OUTER_REPAYMENT_PLAN_NO.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPEAT_OUTER_REPAYMENT_PLAN_NO.getMessage()));
    }

    @Test
    public void testModifyRepaymentPlan_FinancialContract() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32000");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    @Ignore
    @Test
    public void testModifyRepaymentPlan_CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "2");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT.getMessage()));
    }

    @Test
    @Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlan_financial_contract_not_exist.sql")
    public void testModifyRepaymentPlan_FINANCIAL_CONTRACT_NOT_EXIST() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getMessage()));
    }

    @Test
    public void testModifyRepaymentPlan_Repeat() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32000");



        RequestData RequestData1 = new RequestData();

        RequestData1
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));

        RequestData1.setAssetPrincipal(new BigDecimal("160000.00"));
        RequestData1.setAssetInterest(new BigDecimal("10000.00"));
        RequestData1.setAssetRecycleDate("2018-11-10");
        RequestData1.setOtherCharge(new BigDecimal("3.60"));
//        RequestData1.set

        requestDataModels = new ArrayList<>();
        requestDataModels.add(RequestData1);


        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    /**
     * 服务费为负数
     */
    @Test
    @Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlan.sql")
    public void testModifyRepaymentPlanServiceChargeError() throws JsonProcessingException{
        RequestData RequestData1 = new RequestData();
        RequestData RequestData2 = new RequestData();
        RequestData RequestData3 = new RequestData();

        RequestData1
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));
        RequestData2
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 15)));
        RequestData3
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 25)));

        RequestData1.setAssetPrincipal(new BigDecimal("70000.00"));
        RequestData2.setAssetPrincipal(new BigDecimal("50000.00"));
        RequestData3.setAssetPrincipal(new BigDecimal("40000.00"));

        RequestData1.setServiceCharge(new BigDecimal("-10"));
        RequestData2.setServiceCharge(new BigDecimal("-100"));
        RequestData3.setServiceCharge(new BigDecimal("-1000"));

        List<RequestData> requestDataModels = new ArrayList<>();

        requestDataModels.add(RequestData1);
        requestDataModels.add(RequestData2);
        requestDataModels.add(RequestData3);

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("必须大于或等于0.00"));
    }

    /**
     * 自定义校验失败 ( 利息: 计划还款利息 > 计划还款本金 * 24%)
     */
    @Test
    @Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlan.sql")
    public void testModifyRepaymentPlan_CUSTOMER_FEE_CHECK_FAIL_1() throws Exception{
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "3");
        mapParam.put("financialProductCode", "G32003");
        RequestData RequestData1 = new RequestData();

        RequestData1
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));

        RequestData1.setAssetPrincipal(new BigDecimal("160000.00"));
        RequestData1.setAssetInterest(new BigDecimal("160000.00"));
        RequestData1.setAssetRecycleDate("2018-11-10");

        requestDataModels = new ArrayList<>();
        requestDataModels.add(RequestData1);


        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getMessage()));
    }

    /**
     * 自定义金额校验失败-提前还款
     */
    @Test
    @Sql("classpath:test/api/modifyRepaymentPlan/testModifyRepaymentPlan.sql")
    public void testModifyRepaymentPlan_CUSTOMER_FEE_CHECK_FAIL_2() throws Exception {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "e568793f-a44c-4362-9e78-0ce433131f3e");
        mapParam.put("requestReason", "1");
        mapParam.put("financialProductCode", "G32003");
        RequestData RequestData1 = new RequestData();

        RequestData1
                .setAssetRecycleDate(DateUtils.format(DateUtils.addDays(DateUtils.getToday(), 5)));

        RequestData1.setAssetPrincipal(new BigDecimal("160000.00"));
        RequestData1.setAssetInterest(new BigDecimal("3000.00"));
        RequestData1.setAssetType(0);

        requestDataModels = new ArrayList<>();
        requestDataModels.add(RequestData1);


        mapParam.put("requestData", OBJECT_MAPPER.writeValueAsString(requestDataModels));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("requestReason", mapParam.get("requestReason"));
        requestBody.add("financialProductCode", mapParam.get("financialProductCode"));
        requestBody.add("requestData", mapParam.get("requestData"));

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_MODIFY_REPAYMENT_PLAN, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());
        assertThat(resultResponse.getCode(), equalTo(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getCode()));
        assertThat(resultResponse.getMessage(), equalTo("自定义金额校验失败-提前还款"));
    }
}