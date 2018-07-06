package com.suidifu.morganstanley.controller.api.repayment;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.productCategory.ProductCategoryCacheSpec;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeType;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.*;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.*;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MUTABLE_FEE;

/**
 * Created by hwr on 17-9-25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class MutableFeeControllerTest {
    private String privateKey;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
    }

    @After
    public void tearDown() throws Exception {
    }

    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(String requestNo, String dateTime, String financialProductCode, String repaymentPlanNo, String reasonCode, String uniqueId, String repayScheduleNo, List<MutableFeeDetail> mutableFeeDetailList) {
        List<MutableFeeDetail> mutableFeeDetail = mutableFeeDetailList;
        HttpHeaders headers = buildHttpHeaders();
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put(REQUEST_NO, requestNo);
        mapParam.put(APPROVED_TIME, dateTime);
        mapParam.put(APPROVER, "");
        mapParam.put(COMMENT, "test");
        mapParam.put(CONTRACT_NO, "");
        mapParam.put(FINANCIAL_PRODUCT_CODE, financialProductCode);
        mapParam.put("currentPeriod", "1");
        mapParam.put(REPAYMENT_PLAN_NO, repaymentPlanNo);
        mapParam.put(DETAILS, JsonUtils.toJsonString(mutableFeeDetail));
        mapParam.put(REASON_CODE, reasonCode);
        mapParam.put(UNIQUE_ID, uniqueId);
        mapParam.put("checkFailedMsg", "");
        mapParam.put("repayScheduleNo", repayScheduleNo);
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add(PARAMS_SIGN, sign);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(REQUEST_NO, mapParam.get(REQUEST_NO));
        requestBody.add(APPROVED_TIME, mapParam.get(APPROVED_TIME));
        requestBody.add(APPROVER, mapParam.get(APPROVER));
        requestBody.add(COMMENT, mapParam.get(COMMENT));
        requestBody.add(CONTRACT_NO, mapParam.get(CONTRACT_NO));
        requestBody.add(FINANCIAL_PRODUCT_CODE, mapParam.get(FINANCIAL_PRODUCT_CODE));
        requestBody.add("currentPeriod", mapParam.get("currentPeriod"));
        requestBody.add(REPAYMENT_PLAN_NO, mapParam.get(REPAYMENT_PLAN_NO));
        requestBody.add(DETAILS, mapParam.get(DETAILS));
        requestBody.add(REASON_CODE, mapParam.get(REASON_CODE));
        requestBody.add(UNIQUE_ID, mapParam.get(UNIQUE_ID));
        requestBody.add("checkFailedMsg", mapParam.get("checkFailedMsg"));
        requestBody.add("repayScheduleNo", mapParam.get("repayScheduleNo"));
        return new HttpEntity<>(requestBody, headers);
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi.sql")
    public void mutableFee() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //0
        Assert.assertTrue(result.getMessage().contains("成功"));
        Assert.assertEquals(0, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_noRequestNo.sql")
    public void mutableFee_noRequestNo() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity("",
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //10001
        Assert.assertTrue(result.getMessage().contains("请求唯一编号[requestNo]不能为空"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_noUniqueId.sql")
    public void mutableFee_noUniqueId() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));

        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //20001
        Assert.assertTrue(result.getMessage().contains("请选填其中一种编号［uniqueId，contractNo］"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_noFinancialProductCode.sql")
    public void mutableFee_noFinancialProductCode() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));

        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //10001
        Assert.assertTrue(result.getMessage().contains("信托产品代码[financialProductCode]不能为空"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_noReasonCode.sql")
    public void mutableFee_noReasonCode() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "111",
                "ZC75396719055290368", "",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));

        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //10001
        Assert.assertTrue(result.getMessage().contains("更新原因[reasonCode]不能为空"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_noDetails.sql")
    public void mutableFee_noDetails() throws Exception {
        HttpHeaders headers = buildHttpHeaders();
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put(REQUEST_NO, UUID.randomUUID().toString());
        mapParam.put(APPROVED_TIME, "2017-06-05");
        mapParam.put(APPROVER, "");
        mapParam.put(COMMENT, "test");
        mapParam.put(CONTRACT_NO, "");
        mapParam.put(FINANCIAL_PRODUCT_CODE, "111");
        mapParam.put("currentPeriod", "1");
        mapParam.put(REPAYMENT_PLAN_NO, "ZC75396719055290368");
        mapParam.put(DETAILS, "");
        mapParam.put(REASON_CODE, "0");
        mapParam.put(UNIQUE_ID, "QINWEICHAO80");
        mapParam.put("checkFailedMsg", "");
        mapParam.put("repayScheduleNo", "outerId1");
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add(PARAMS_SIGN, sign);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(REQUEST_NO, mapParam.get(REQUEST_NO));
        requestBody.add(APPROVED_TIME, mapParam.get(APPROVED_TIME));
        requestBody.add(APPROVER, mapParam.get(APPROVER));
        requestBody.add(COMMENT, mapParam.get(COMMENT));
        requestBody.add(CONTRACT_NO, mapParam.get(CONTRACT_NO));
        requestBody.add(FINANCIAL_PRODUCT_CODE, mapParam.get(FINANCIAL_PRODUCT_CODE));
        requestBody.add("currentPeriod", mapParam.get("currentPeriod"));
        requestBody.add(REPAYMENT_PLAN_NO, mapParam.get(REPAYMENT_PLAN_NO));
        requestBody.add(DETAILS, mapParam.get(DETAILS));
        requestBody.add(REASON_CODE, mapParam.get(REASON_CODE));
        requestBody.add(UNIQUE_ID, mapParam.get(UNIQUE_ID));
        requestBody.add("checkFailedMsg", mapParam.get("checkFailedMsg"));
        requestBody.add("repayScheduleNo", mapParam.get("repayScheduleNo"));
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
                //formEntity.getBody().set(DETAILS, null);
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //10001
        Assert.assertTrue(result.getMessage().contains("费用明细jsonlist不能为空"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_noRepaymentPlanNoAndRepayScheduleNo.sql")
    public void mutableFee_noRepaymentPlanNoAndRepayScheduleNo() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "", "0",
                "QINWEICHAO80", "",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));

        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //20001
        Assert.assertTrue(result.getMessage().contains("请选填其中一种编号［repayScheduleNo，repaymentPlanNo］"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_invalidReason.sql")
    public void mutableFee_invalidReason() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "7",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));

        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //10001
        Assert.assertTrue(result.getMessage().contains("更新原因[reasonCode]内容不合法"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_FIXME_detail.sql")
    public void mutableFee_FIXME_detail () throws Exception {
        List<MutableFeeDetail> mutableFeeDetail = new ArrayList<MutableFeeDetail>();
            mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_SERVICE_FEE.ordinal(), BigDecimal.TEN));
            mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN));
            mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_OTHER_FEE.ordinal(), BigDecimal.TEN));
            mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_PRINCIPAL.ordinal(), BigDecimal.TEN));
            mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_TECH_FEE.ordinal(), BigDecimal.TEN));
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                mutableFeeDetail);
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //20001
        Assert.assertTrue(result.getMessage().contains("[details］格式异常！"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_InvalidDetail.sql")
    public void mutableFee_InvalidDetail() throws Exception {
        List<MutableFeeDetail> mutableFeeDetail = new ArrayList<MutableFeeDetail>();
        mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_SERVICE_FEE.ordinal(), BigDecimal.TEN));
        mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN));
        mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_OTHER_FEE.ordinal(), BigDecimal.TEN));
        mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_PRINCIPAL.ordinal(), BigDecimal.TEN));
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                mutableFeeDetail);
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //20001
        Assert.assertTrue(result.getMessage().contains("[details.feeType］无效费用类型！"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/testMutableFeeApi_RepeatDetail.sql")
    public void mutableFee_RepeatDetail() throws Exception {
        List<MutableFeeDetail> mutableFeeDetail = new ArrayList<MutableFeeDetail>();
        mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_SERVICE_FEE.ordinal(), BigDecimal.TEN));
        mutableFeeDetail.add(new MutableFeeDetail(MutableFeeType.LOAN_SERVICE_FEE.ordinal(), BigDecimal.TEN));
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                mutableFeeDetail);
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //20001
        Assert.assertTrue(result.getMessage().contains("[details］费用重复！"));
        Assert.assertEquals(20001, result.getCode());
    }

    public HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(PARAMS_MER_ID, "t_test_zfb");
        headers.add(PARAMS_SECRET, "123456");
        return headers;
    }

    @Test
    @Sql("classpath:test/mutableFee_ApiException.sql")
    public void mutableFee_ApiException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity("3332",
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //21002
        Assert.assertTrue(result.getMessage().contains("请求编号重复"));
        Assert.assertEquals(21002, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_DateFormatException.sql")
    public void mutableFee_DateFormatException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05-", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //24005
        Assert.assertTrue(result.getMessage().contains("审核日期格式错误"));
        Assert.assertEquals(24005, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_FinancialContractNotExistException.sql")
    public void mutableFee_FinancialContractNotExistException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));

        Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
        cache.clear();
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //30003
        Assert.assertTrue(result.getMessage().contains("信托合同不存在"));
        Assert.assertEquals(30003, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_FinancialContractNotPAYGException.sql")
    public void mutableFee_FinancialContractNotPAYGException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //24001
        Assert.assertTrue(result.getMessage().contains("信托合同不支持随借随还"));
        Assert.assertEquals(24001, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_ContractNotExistException.sql")
    public void mutableFee_ContractNotExistException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //21001
        Assert.assertTrue(result.getMessage().contains("贷款合同不存在"));
        Assert.assertEquals(21001, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_FinancialContractNotIncludedContractException.sql")
    public void mutableFee_FinancialContractNotIncludedContractException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //33006
        Assert.assertTrue(result.getMessage().contains("信托计划与贷款合同不匹配"));
        Assert.assertEquals(33006, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_RepaymentPlanNotExistException.sql")
    public void mutableFee_RepaymentPlanNotExistException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //22203
        Assert.assertTrue(result.getMessage().contains("不存在该有效还款计划或者还款计划不在贷款合同内"));
        Assert.assertEquals(22203, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_ContractNotIncludedRepaymentPlanException.sql")
    public void mutableFee_ContractNotIncludedRepaymentPlanException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //33005
        Assert.assertTrue(result.getMessage().contains("还款计划与贷款合同不匹配"));
        Assert.assertEquals(33005, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_RepaymentPlanSuccessException.sql")
    public void mutableFee_RepaymentPlanSuccessException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //23106
        Assert.assertTrue(result.getMessage().contains("还款计划已还款成功"));
        Assert.assertEquals(23106, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_RepaymentPlanDeductLockedException.sql")
    public void mutableFee_RepaymentPlanDeductLockedException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //23105
        Assert.assertTrue(result.getMessage().contains("还款计划被锁定"));
        Assert.assertEquals(23105, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_RepaymentPlanCanBeRollbackException.sql")
    public void mutableFee_RepaymentPlanCanBeRollbackException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), BigDecimal.TEN))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //24004
        Assert.assertTrue(result.getMessage().contains("还款计划已申请提前还款"));
        Assert.assertEquals(24004, result.getCode());
    }

    @Test
    @Sql("classpath:test/mutableFee_MutableFeeAmountInvaildException.sql")
    public void mutableFee_MutableFeeAmountInvaildException() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), new BigDecimal(-1)))));

        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //24003
        Assert.assertTrue(result.getMessage().contains("非法还息金额"));
        Assert.assertEquals(24003, result.getCode());
    }

    /**
     *   计划还款利息 > 计划还款本金 * 24%
     */
    @Test
    @Sql({"classpath:test/testMutableFeeApi.sql"})
    public void mutableFee_CUSTOMER_FEE_CHECK_FAIL() throws Exception {
        HttpEntity<MultiValueMap<String, String>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(),
                "2017-06-05", "G31700",
                "ZC75396719055290368", "0",
                "QINWEICHAO80", "outerId1",
                new ArrayList<>(Arrays.asList(new MutableFeeDetail(MutableFeeType.LOAN_ASSET_INTEREST.ordinal(), new BigDecimal("2000.00")))));
        BaseResponse result = template.postForObject(URL_API_V3 + URL_MUTABLE_FEE, formEntity, BaseResponse.class);

        log.info("log mutablFee : {}", result);
        //0
        Assert.assertTrue(result.getMessage().contains("自定义金额校验失败"));
        Assert.assertEquals(23108, result.getCode());
    }
}