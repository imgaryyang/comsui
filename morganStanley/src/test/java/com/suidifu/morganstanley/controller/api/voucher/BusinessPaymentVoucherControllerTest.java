package com.suidifu.morganstanley.controller.api.voucher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;
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

import java.math.BigDecimal;
import java.util.*;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/9 <br>
 * Time:上午11:05 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
@Sql("classpath:test/api/businesspaymentvoucher/testBusinessPaymentVoucher.sql")
public class BusinessPaymentVoucherControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private HttpHeaders headers;
    private MultiValueMap<String, String> requestBody;
    private Map<String, String> mapParam;
    private List<BusinessPaymentVoucherDetail> details;
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
        mapParam = null;
    }

    @Test
    public void submit() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    @Test
    public void submit_REQUEST_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoRequestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("请求唯一编号［requestNo］，不能为空"));
    }

    //改成自定义注解,只显示一种校验失败提示:商户付款凭证类型［voucherType］错误,具体信息打在注解log里
    @Test
    public void submit_VOUCHER_TYPE_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoVoucherTypeData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("商户付款凭证类型［voucherType］错误"));
    }

    @Test
    public void submit_ILLEGAL_VALUE_VOUCHER_TYPE() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initIllegalValueVoucherTypeData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("商户付款凭证类型［voucherType］错误"));
    }

    @Test
    public void submit_ILLEGAL_VALUE_VOUCHER_AMOUNT() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initIllegalValueVoucherAmountData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("凭证金额［voucherAmount］，必需大于0.00"));
    }

    @Test
    public void submit_FINANCIAL_CONTRACT_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoFinancialContractNoData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("信托产品代码［financialContractNo］，不能为空"));
    }

    @Test
    public void submit_PAYMENT_ACCOUNT_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoPaymentAccountNoTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款账户号［paymentAccountNo］，不能为空"));
    }

    @Test
    public void submit_PAYMENT_NAME_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoPaymentNameNoTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款银行帐户名称［paymentName］，不能为空"));
    }

    @Test
    public void submit_PAYMENT_BANK_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoPaymentBankTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款银行名称［paymentBank］，不能为空"));
    }

    @Test
    public void submit_BANK_TRANSACTION_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoBankTransactionNoTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款账户流水号［bankTransactionNo］，不能为空"));
    }

    @Test
    public void submit_DETAIL_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initNoDetailTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("明细［detail］，不能为空"));
    }

    @Test
    public void submit_DETAIL_UNIQUEID_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initDetailNoUniqueIdTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款合同唯一标识［uniqueId］，不能为空"));
    }

    @Test
    public void submit_DETAIL_FORMAT_ERROR() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initErrorDetailTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("凭证明细内容错误［detail］字段格式"));
    }

    @Test
    public void submit_RequiredOnlyOneFieldNotEmpty() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initRequiredOnlyOneFieldNotEmptyDetailTestData(),
                BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("商户还款计划编号[repayScheduleNo] 还款计划编号[repaymentPlanNo] 期数[currentPeriod]  至少一个不为空"));
    }

    @Test
    public void submit_RepeatedScheduleNo() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initRepeatedScheduleNoDetailTestData(), BaseResponse
                .class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("商户还款计划编号[repayScheduleNo] 不能重复"));
    }

    @Test
    public void submit_RepeatedCurrentPeriod() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initRepeatedCurrentPeriodDetailTestData(),
                BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("明细中的期数不可重复相同[currentPeriod]"));
    }

    @Test
    public void submit_RepeatedRepaymentPlanNo() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initRepeatedRepaymentPlanNoDetailTestData(),
                BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("还款计划编号重复[repaymentPlanNo]"));
    }

    @Test
    public void submit_RepurchaseDetail() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initRepurchaseDetailTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("回购凭证明细总金额与明细其他金额总和不匹配"));
    }

    @Test
    public void submit_ErrorsOfTransactionDate() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initErrorsOfTransactionDateTestData(), BaseResponse
                .class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("设定还款日期 格式有误"));
    }

    @Test
    public void submit_MatchOfVoucherAndDetailAmount() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/submit", initMatchOfVoucherAndDetailAmountTestData(),
                BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("凭证金额与明细总金额不匹配"));
    }

    @Test
    public void undo() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    @Test
    public void undo_REQUEST_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoRequestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("请求唯一编号［requestNo］，不能为空"));
    }

    //改用自定义校验,提示信息只有一种,具体打在注解log里
    @Test
    public void undo_VOUCHER_TYPE_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoVoucherTypeData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("商户付款凭证类型［voucherType］错误"));
    }

    @Test
    public void undo_ILLEGAL_VALUE_VOUCHER_TYPE() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initIllegalValueVoucherTypeData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("商户付款凭证类型［voucherType］错误"));
    }

    @Test
    public void undo_ILLEGAL_VALUE_VOUCHER_AMOUNT() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initIllegalValueVoucherAmountData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("凭证金额［voucherAmount］，必需大于0.00"));
    }

    @Test
    public void undo_FINANCIAL_CONTRACT_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoFinancialContractNoData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("信托产品代码［financialContractNo］，不能为空"));
    }

    @Test
    public void undo_PAYMENT_ACCOUNT_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoPaymentAccountNoTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款账户号［paymentAccountNo］，不能为空"));
    }

    @Test
    public void undo_PAYMENT_NAME_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoPaymentNameNoTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款银行帐户名称［paymentName］，不能为空"));
    }

    @Test
    public void undo_PAYMENT_BANK_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoPaymentBankTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款银行名称［paymentBank］，不能为空"));
    }

    @Test
    public void undo_BANK_TRANSACTION_NO_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoBankTransactionNoTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("付款账户流水号［bankTransactionNo］，不能为空"));
    }

    @Test
    public void undo_DETAIL_NULL_OR_EMPTY() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/business-payment-vouchers/undo", initNoDetailTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("明细［detail］，不能为空"));
    }

    private HttpEntity<MultiValueMap<String, String>> initTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("uniqueId", "f6129d01-b063-11e6-bedc-00163e002839");
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("uniqueId", mapParam.get("uniqueId"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initErrorDetailTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setPrincipal(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setInterest(new BigDecimal("-0.20"));
        //businessPaymentVoucherDetail.setInterest(new BigDecimal("0.10"));
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initRequiredOnlyOneFieldNotEmptyDetailTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");

        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initRepeatedScheduleNoDetailTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setRepayScheduleNo("1");
        details.add(businessPaymentVoucherDetail);

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail1 = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail1.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail1.setPayer(0);
        businessPaymentVoucherDetail1.setTransactionTime("");
        businessPaymentVoucherDetail1.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail1.setRepayScheduleNo("1");
        details.add(businessPaymentVoucherDetail1);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initRepeatedCurrentPeriodDetailTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setRepayScheduleNo("1");
        businessPaymentVoucherDetail.setCurrentPeriod(1);
        details.add(businessPaymentVoucherDetail);

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail1 = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail1.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail1.setPayer(0);
        businessPaymentVoucherDetail1.setTransactionTime("");
        businessPaymentVoucherDetail1.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail1.setRepayScheduleNo("2");
        businessPaymentVoucherDetail1.setCurrentPeriod(1);
        details.add(businessPaymentVoucherDetail1);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }


    private HttpEntity<MultiValueMap<String, String>> initRepeatedRepaymentPlanNoDetailTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        details.add(businessPaymentVoucherDetail);

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail1 = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail1.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail1.setPayer(0);
        businessPaymentVoucherDetail1.setTransactionTime("");
        businessPaymentVoucherDetail1.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setRepaymentPlanNo("plan_no1");
        details.add(businessPaymentVoucherDetail1);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initRepurchaseDetailTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setRepayScheduleNo("1");
        businessPaymentVoucherDetail.setCurrentPeriod(1);
        businessPaymentVoucherDetail.setInterest(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setPrincipal(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setPenaltyFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setOtherCharge(new BigDecimal("0.10"));
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "2");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initErrorsOfTransactionDateTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime(new Date().toString());
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setRepayScheduleNo("1");
        businessPaymentVoucherDetail.setCurrentPeriod(1);
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }


    private HttpEntity<MultiValueMap<String, String>> initMatchOfVoucherAndDetailAmountTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setCurrentPeriod(1);
        businessPaymentVoucherDetail.setRepaymentPlanNo("1");
        details.add(businessPaymentVoucherDetail);

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail1 = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail1.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail1.setPayer(0);
        businessPaymentVoucherDetail1.setTransactionTime("");
        businessPaymentVoucherDetail1.setLateFee(new BigDecimal("0.10"));
        businessPaymentVoucherDetail1.setCurrentPeriod(2);
        businessPaymentVoucherDetail1.setRepaymentPlanNo("2");
        details.add(businessPaymentVoucherDetail1);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.30");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initDetailNoUniqueIdTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoDetailTestData() throws JsonProcessingException {
        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoBankTransactionNoTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoPaymentBankTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoPaymentNameNoTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoPaymentAccountNoTestData() throws JsonProcessingException {
        details = new ArrayList<>();

        BusinessPaymentVoucherDetail businessPaymentVoucherDetail = new BusinessPaymentVoucherDetail();
        businessPaymentVoucherDetail.setAmount(new BigDecimal("0.10"));
        businessPaymentVoucherDetail.setUniqueId("e96bdb77-c051-4db0-b046-daca68baa203");
        businessPaymentVoucherDetail.setRepaymentPlanNo("plan_no1");
        businessPaymentVoucherDetail.setPayer(0);
        businessPaymentVoucherDetail.setTransactionTime("");
        businessPaymentVoucherDetail.setLateFee(new BigDecimal("0.10"));
        details.add(businessPaymentVoucherDetail);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoRequestData() throws JsonProcessingException {
        mapParam = new HashMap<>();
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoVoucherTypeData() throws JsonProcessingException {
        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }


    private HttpEntity<MultiValueMap<String, String>> initIllegalValueVoucherTypeData() throws JsonProcessingException {
        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "8");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initIllegalValueVoucherAmountData() throws JsonProcessingException {
        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "3");
        mapParam.put("voucherAmount", "-0.10");
        mapParam.put("financialContractNo", "YX_AMT_001");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> initNoFinancialContractNoData() throws JsonProcessingException {
        mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("transactionType", "0");
        mapParam.put("voucherType", "0");
        mapParam.put("voucherAmount", "0.10");
        mapParam.put("receivableAccountNo", "955103657777777");
        mapParam.put("paymentAccountNo", "serial_no_2");
        mapParam.put("paymentName", "张建明");
        mapParam.put("paymentBank", "招商银行");
        mapParam.put("bankTransactionNo", "1122049");
        mapParam.put("detail", OBJECT_MAPPER.writeValueAsString(details));

        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("transactionType", mapParam.get("transactionType"));
        requestBody.add("voucherType", mapParam.get("voucherType"));
        requestBody.add("voucherAmount", mapParam.get("voucherAmount"));
        requestBody.add("receivableAccountNo", mapParam.get("receivableAccountNo"));
        requestBody.add("paymentAccountNo", mapParam.get("paymentAccountNo"));
        requestBody.add("paymentName", mapParam.get("paymentName"));
        requestBody.add("paymentBank", mapParam.get("paymentBank"));
        requestBody.add("bankTransactionNo", mapParam.get("bankTransactionNo"));
        requestBody.add("detail", mapParam.get("detail"));

        return new HttpEntity<>(requestBody, headers);
    }
}