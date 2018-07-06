package com.suidifu.morganstanley.controller.api.voucher;

import com.suidifu.matryoshka.productCategory.ProductCategoryCacheSpec;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_ACTIVE_PAYMENT_VOUCHERS_UNDO;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.FINANCIAL_CONTRACT_NO;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.PARAMS_MER_ID;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.PARAMS_SECRET;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.PARAMS_SIGN;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.REQUEST_NO;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/9 <br>
 * Time:上午11:04 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class ActivePaymentVoucherControllerTest {
    private String privateKey;

    @Autowired
    private TestRestTemplate template;

    private MultiValueMap<String, Object> requestBody;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ResourceLoader loader;

    @Before
    public void setUp() throws Exception {
        privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
        requestBody = new LinkedMultiValueMap<>();
        Resource resource = loader.getResource("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit.pdf");
        requestBody.add("file", resource);
    }

    @After
    public void tearDown() throws Exception {
        requestBody = null;
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit.sql")
    public void submit() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\"," +
                        "\"amount\":\"490\"}]");
        //BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, formEntity, BaseResponse.class);
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(0, result.getCode());
        Assert.assertTrue(result.getMessage().contains("成功"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noBankTransactionNo.sql")
    public void submit_noBankTransactionNo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("付款账户流水号［bankTransactionNo］，不能为空！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noVoucherType.sql")
    public void submit_noVoucherType() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");

        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证类型［voucherType］，不能为空！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_invalidVoucherType.sql")
    public void submit_invalidVoucherType() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "1",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证类型［voucherType］错误"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_invalidVoucherAmount.sql")
    public void submit_invalidVoucherAmount() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","0","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证金额［voucherAmount］，必需大于0.00！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noPaymentAccountNo.sql")
    public void submit_noPaymentAccountNo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("付款账户号［paymentAccountNo］，不能为空！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noPaymentName.sql")
    public void submit_noPaymentName() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("付款银行帐户名称［paymentName］，不能为空！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noPaymentBank.sql")
    public void submit_noPaymentBank() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("付款银行名称［paymentBank］，不能为空！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noFinancialContractNo.sql")
    public void submit_noFinancialContractNo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("信托产品代码［financialContractNo］，不能为空！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noDetail.sql")
    public void submit_noDetail() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("明细［detail］，不能为空！"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_isNotEmptyRepaymentPlanNoAndRepayScheduleNoAndrepaymentNumber.sql")
    public void submit_isNotEmptyRepaymentPlanNoAndRepayScheduleNoAndrepaymentNumber() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("至少选填一种编号[repayScheduleNo，repaymentPlanNo,currentPeriod]"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_repeatRepayScheduleNo.sql")
    public void submit_repeatRepayScheduleNo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"2\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\"," +
                        "\"amount\":\"10\"},{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"480\"}]"
        );
        //BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, formEntity, BaseResponse.class);
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("商户还款计划编号[repayScheduleNo] 不能重复!!!"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_repeatCurrentPeriod.sql")
    public void submit_repeatCurrentPeriod() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ1\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\"," +
                        "\"amount\":\"10\"},{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"480\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("明细中存在相同的期数[currentPeriod]!!!"));
    }

    /*@Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_invlaidDetail.sql")
    public void submit_invlaidDetail() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity("", UUID.randomUUID().toString(),"0","5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700","[\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]","","[]");
        addFile();
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20002, result.getCode());
        Assert.assertTrue(result.getMessage().contains("请求参数解析错误"));
    }*/

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_errorDetail.sql")
    public void submit_errorDetail() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\"," +
                        "\"amount\":\"0\"},{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ1\",\"currentPeriod\":\"2\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证明细内容错误［detail］(凭证明细金额与凭证金额不匹配|凭证明细金额格式错误|amount<=0)"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit.sql")
    public void submit_repeatRepaymentPlanNoInRepaymentPlanNoList() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ1\",\"currentPeriod\":\"2\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\"," +
                        "\"amount\":\"10\",\"repaymentPlanNo\":\"123\"},{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"480\",\"repaymentPlanNo\":\"123\"}]"
        );
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("还款计划编号重复"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit.sql")
    public void submit_noMatchAmount() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ1\",\"currentPeriod\":\"2\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\"," +
                        "\"amount\":\"490\",\"repaymentPlanNo\":\"1234\"},{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\",\"repaymentPlanNo\":\"123\"}]"
        );
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证金额与明细总金额不匹配"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit.sql")
    public void submit_noContractAndUniqueId() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("请选填其中一种编号[uniqueId，contractNo]"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_repeatRequestNo.sql")
    public void submit_repeatRequestNo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity("e821c6b0-02ae-4f60-8873-ee2d6c47fabf", "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(21002, result.getCode());
        Assert.assertTrue(result.getMessage().contains("请求编号重复"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_FINANCIAL_CONTRACT_NOT_EXIST.sql")
    public void submit_FINANCIAL_CONTRACT_NOT_EXIST() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
        cache.clear();
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(30003, result.getCode());
        Assert.assertTrue(result.getMessage().contains("信托合同不存在"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_NO_SUCH_RECEIVABLE_ACCOUNT_NO.sql")
    public void submit_NO_SUCH_RECEIVABLE_ACCOUNT_NO() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(34002, result.getCode());
        Assert.assertTrue(result.getMessage().contains("收款账户错误，收款账户不是贷款合同的回款账户"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_CONTRACT_NOT_EXIST.sql")
    public void submit_CONTRACT_NOT_EXIST() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(21001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("贷款合同不存在"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_CUSTOMER_NOT_UNIQUE.sql")
    public void submit_CUSTOMER_NOT_UNIQUE() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"400\"},{\"repayScheduleNo\":\"TESTDZZ0822_270DZ0\",\"currentPeriod\":\"2\",\"contractNo\":\"2016-78-DK(ZQ2016000000001)\",\"amount\":\"90\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(34007, result.getCode());
        Assert.assertTrue(result.getMessage().contains("贷款合同不在同一贷款人名下"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_noAssetSet.sql")
    public void submit_noAssetSet() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(22203, result.getCode());
        Assert.assertTrue(result.getMessage().contains("不存在该有效还款计划或者还款计划不在贷款合同内"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_REPAYMENT_CODE_NOT_IN_CONTRACT.sql")
    public void submit_REPAYMENT_CODE_NOT_IN_CONTRACT() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(22203, result.getCode());
        Assert.assertTrue(result.getMessage().contains("不存在该有效还款计划或者还款计划不在贷款合同内"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_VOUCHER_DETAIL_AMOUNT_TOO_LARGE.sql")
    public void submit_VOUCHER_DETAIL_AMOUNT_TOO_LARGE() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(34004, result.getCode());
        Assert.assertTrue(result.getMessage().contains("主动付款凭证金额错误，明细金额应不大于还款计划应还未还金额"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_VOUCHER_DETAIL_AMOUNT_IS_NULL.sql")
    public void submit_VOUCHER_DETAIL_AMOUNT_IS_NULL() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(34005, result.getCode());
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_VOUCHER_DETAIL_AND_AMOUNT_NOT_MATCH.sql")
    public void submit_VOUCHER_DETAIL_AND_AMOUNT_NOT_MATCH() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\",\"interest\":\"1\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证明细内容错误［detail］(凭证明细金额与凭证金额不匹配|凭证明细金额格式错误|amount<=0)"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_VOUCHER_ERROR_OF_DETAIL_FORMAT.sql")
    public void submit_VOUCHER_ERROR_OF_DETAIL_FORMAT() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\",\"interest\":\"-1\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证明细内容错误［detail］(凭证明细金额与凭证金额不匹配|凭证明细金额格式错误|amount<=0)"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_BankTransactionNoExistException.sql")
    public void submit_BankTransactionNoExistException() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("打款流水号已关联凭证"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_CashFlowNotExistException.sql")
    public void submit_CashFlowNotExistException() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(20001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证对应流水不存在或已提交"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_NO_SUCH_CASH_FLOW.sql")
    public void submit_NO_SUCH_CASH_FLOW() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(33003, result.getCode());
        Assert.assertTrue(result.getMessage().contains("凭证对应流水不存在或已提交"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit_UNSUPPORTED_FILE_TYPE.sql")
    public void submit_UNSUPPORTED_FILE_TYPE() throws Exception {
        requestBody.set("file", loader.getResource("classpath:test/activePaymentVoucher/activePaymentVoucherSubmit"));
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "600000000001","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.exchange(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_SUBMIT, HttpMethod.POST, formEntity, BaseResponse.class).getBody();

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(34001, result.getCode());
        Assert.assertTrue(result.getMessage().contains("不支持的文件类型"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherUndo.sql")
    public void undo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "6214855712106521","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_UNDO, formEntity, BaseResponse.class);

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertEquals(0, result.getCode());
        Assert.assertTrue(result.getMessage().contains("成功"));
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherUndo_noBankTransactionNo.sql")
    public void undo_noBankTransactionNo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "6214855712106521","中国农业银行","","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_UNDO, formEntity, BaseResponse.class);

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertTrue(result.getMessage().contains("付款账户流水号［bankTransactionNo］，不能为空！"));
        Assert.assertEquals(20001, result.getCode());
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherUndo_repeatRequestNo.sql")
    public void undo_repeatRequestNo() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity("e821c6b0-02ae-4f60-8873-ee2d6c47fabf", "0", "5",
                "6214855712106521","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_UNDO, formEntity, BaseResponse.class);

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertTrue(result.getMessage().contains("请求编号重复"));
        Assert.assertEquals(21002, result.getCode());
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherUndo_FINANCIAL_CONTRACT_NOT_EXIST.sql")
    public void undo_FINANCIAL_CONTRACT_NOT_EXIST() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "6214855712106521","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G317000", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_UNDO, formEntity, BaseResponse.class);

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertTrue(result.getMessage().contains("信托合同不存在"));
        Assert.assertEquals(30003, result.getCode());
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherUndo_noVoucher.sql")
    public void undo_noVoucher() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "6214855712106521","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_UNDO, formEntity, BaseResponse.class);

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertTrue(result.getMessage().contains("凭证对应流水不存在"));
        Assert.assertEquals(33007, result.getCode());
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherUndo_noCashflow.sql")
    public void undo_noCashflow() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "6214855712106521","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_UNDO, formEntity, BaseResponse.class);

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertTrue(result.getMessage().contains("凭证对应流水不存在"));
        Assert.assertEquals(33007, result.getCode());
    }

    @Test
    @Sql("classpath:test/activePaymentVoucher/activePaymentVoucherUndo_invalidCashflow.sql")
    public void undo_invalidCashflow() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> formEntity = getMultiValueMapHttpEntity(UUID.randomUUID().toString(), "0", "5",
                "6214855712106521","中国农业银行","722455d9-30e8-49a1-9976-f981958bf72f","490","高渐离", "6214855712117980",
                "G31700", "[{\"repayScheduleNo\":\"TESTDZZ0822_270DZZ0\",\"currentPeriod\":\"3\",\"contractNo\":\"2016-236-DK(hk771837350822830067)号\",\"amount\":\"490\"}]");
        BaseResponse result = template.postForObject(URL_API_V3 + URL_ACTIVE_PAYMENT_VOUCHERS_UNDO, formEntity, BaseResponse.class);

        log.info("log ActivePaymentVoucherControllerTest : {}", result);
        Assert.assertTrue(result.getMessage().contains("当前凭证无法撤销"));
        Assert.assertEquals(33008, result.getCode());
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(PARAMS_MER_ID, "t_test_zfb");
        headers.add(PARAMS_SECRET, "123456");
        return headers;
    }


    private HttpEntity<MultiValueMap<String, Object>> getMultiValueMapHttpEntity(String requestNo, String transactionType, String voucherType,
                                                                                 String receivableAccountNo, String paymentBank, String bankTransactionNo, String voucherAmount,
                                                                                 String paymentName, String paymentAccountNo, String financialContractNo, String detail) {
        HttpHeaders headers = buildHttpHeaders();
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put(REQUEST_NO, requestNo);
        mapParam.put("transactionType", transactionType);
        mapParam.put("voucherType", voucherType);
        mapParam.put("receivableAccountNo", receivableAccountNo);
        mapParam.put("paymentBank", paymentBank);
        mapParam.put("bankTransactionNo", bankTransactionNo);
        mapParam.put("voucherAmount", voucherAmount);
        mapParam.put("paymentName", paymentName);
        mapParam.put("paymentAccountNo", paymentAccountNo);
        mapParam.put(FINANCIAL_CONTRACT_NO, financialContractNo);
        mapParam.put("detail", detail);
        mapParam.put("file", "");

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        log.info("signContent:{}", signContent);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add(PARAMS_SIGN, sign);

        requestBody.add(REQUEST_NO, mapParam.getOrDefault(REQUEST_NO, ""));
        requestBody.add("transactionType", mapParam.getOrDefault("transactionType", ""));
        requestBody.add("voucherType", mapParam.getOrDefault("voucherType", ""));
        requestBody.add("receivableAccountNo", mapParam.getOrDefault("receivableAccountNo", ""));
        requestBody.add("paymentBank", mapParam.getOrDefault("paymentBank", ""));
        requestBody.add("bankTransactionNo", mapParam.getOrDefault("bankTransactionNo", ""));
        requestBody.add("voucherAmount", mapParam.getOrDefault("voucherAmount", ""));
        requestBody.add("paymentName", mapParam.getOrDefault("paymentName", ""));
        requestBody.add("paymentAccountNo", mapParam.getOrDefault("paymentAccountNo", ""));
        requestBody.add(FINANCIAL_CONTRACT_NO, mapParam.getOrDefault(FINANCIAL_CONTRACT_NO, ""));
        requestBody.add("detail", mapParam.getOrDefault("detail", ""));
        return new HttpEntity<MultiValueMap<String, Object>>(requestBody, headers);
    }
}