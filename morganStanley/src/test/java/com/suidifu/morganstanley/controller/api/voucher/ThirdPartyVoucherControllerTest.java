package com.suidifu.morganstanley.controller.api.voucher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.request.voucher.ThirdPartVoucherDetail;
import com.suidifu.morganstanley.model.request.voucher.ThirdPartVoucherRepayDetail;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
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
import java.util.*;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/22 <br>
 * Time:上午1:11 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
@Sql("classpath:test/api/thirdPartVoucher/testThirdPartVoucher.sql")
public class ThirdPartyVoucherControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private HttpHeaders headers;
    private MultiValueMap<String, String> requestBody;
    private Map<String, String> mapParam;
    private List<ThirdPartVoucherDetail> details;
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
    public void thirdPartVoucherCommandModel() throws Exception {
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/third-part-vouchers/submit", initTestData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }


    @Test
    public void testNullRequestNo() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit",initNullRequestNoData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("请求唯一标识[requestNo]不能为空"));
    }

    @Test
    public void testNullFinancialContractNo() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullFinancialContractNoData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("信托产品代码[financialContractNo]不能为空"));
    }

    @Test
    public void testNullThirdPartVoucherDetail()throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullThirdPartVoucherDetail(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("第三方凭证明细[detailList]不能为空"));
    }

    @Test
    public void testNullVoucherNo() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullVoucherNoData(),BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("凭证编号[voucherNo]不能为空"));
    }

    @Test
    public void testNullTransactionRequestNo() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullTransactionRequestNoData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("交易请求号[transactionRequestNo]不能为空"));
    }

    @Test
    public void testNullTransactionTime()throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullTransactionTimeData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("交易发起时间[transactionTime]不能为空"));
    }

    @Test
    public void testNullContractUniqueIdData() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullContractUniqueIdData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());


        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款合同唯一编号[contractUniqueId]不能为空"));
    }

    @Test
    public void testNullRepayDetailList() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullRepayDetailList(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("还款明细[repayDetailList]不能为空"));
    }

    @Test
    public void testErrorTransactionTime() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initErrorTransactionTime(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("交易发起时间,格式有误"));
    }

    @Test
    public void testNullParamsThirdPartVoucherRepayDetail() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullParamsThirdPartVoucherRepayDetail(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("[repaymentPlanNo],[repayScheduleNo],[currentPeriod]至少一个不为空"));
    }

    @Test
    public void testNullAmountData() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullAmountData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("交易金额［amount]不能为空"));
    }

    @Test
    public void testNullInterestData() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initNullInterestParamData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("[interest]不能为空"));
    }

    @Test
    public void testRepeatedRepayScheduleNo() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initRepeatedRepayScheduleNo(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("商户还款计划编号[repayScheduleNo]不可重复"));
    }

    @Test
    public void testRepeatedCurrentPeriod() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initRepeatedCurrentPeriod(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("存在相同[currentPeriod]期数"));
    }

    @Test
    public void testFinancialContractNotExist() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initFinancialContractNotExistData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getMessage()));

    }

    /**
     * 重复的voucherNo会在注解里面被校验,所以只存在同一批次的重复交易请求号会被校验
     */
    @Test
    public void testRepeatVoucherNoOrTransactionRequestNo()throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initRepeatVoucherNoOrTransactionRequestNoData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.ONE_BATCH_REPEATE_VOUCHER_NO_OR_TRANSCATION_REQUEST_NO.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.ONE_BATCH_REPEATE_VOUCHER_NO_OR_TRANSCATION_REQUEST_NO.getMessage()));
    }

    @Test
    public void testRepeatedVoucherNo()throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3+"/third-part-vouchers/submit", initRepeatedVoucherNo(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getMessage(), containsString("保存commandlog错误"));
    }

    @Test
    @Ignore("很难让update的时候报错")
    public void testErrorUpdateLog() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + "/third-part-vouchers/submit", initErrorUpdateLogData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getMessage(), containsString("更新log错误"));
    }

    private HttpEntity<MultiValueMap<String,String>> initTestData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", "ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo", "G31700");
        mapParam.put("detailList", OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("detailList", mapParam.get("detailList"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initErrorUpdateLogData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("dzzz");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("827aee18-049e-4a60-adc2-843bb41f7f7f");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", "ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo", "G31700");
        mapParam.put("detailList", OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("detailList", mapParam.get("detailList"));

        return new HttpEntity<>(requestBody, headers);
    }
    private HttpEntity<MultiValueMap<String,String>> initNullRequestNoData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel = new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullFinancialContractNoData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullThirdPartVoucherDetail() throws JsonProcessingException{
        details = null;

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",null);
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullVoucherNoData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));
        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initRepeatedVoucherNo() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("827aee18-049e-4a60-adc2-843bb41f7f7f");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullTransactionRequestNoData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();
        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullTransactionTimeData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullAmountData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

//        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", "ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo", "G31700");
        mapParam.put("detailList", OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("detailList", mapParam.get("detailList"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullInterestParamData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(-100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullContractUniqueIdData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullRepayDetailList() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        voucherDetailModel.setRepayDetailList(null);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initRepeatVoucherNoOrTransactionRequestNoData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");

        ThirdPartVoucherDetail voucherDetailModel1=  new ThirdPartVoucherDetail();

        voucherDetailModel1.setAmount(new BigDecimal(200000));
        voucherDetailModel1.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel1.setComment("213456");
        voucherDetailModel1.setCompleteTime(new Date().toString());
        voucherDetailModel1.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel1.setCreateTime(new Date());
        voucherDetailModel1.setCurrency(0);
        voucherDetailModel1.setCustomerIdNo("1234567890-");
        voucherDetailModel1.setCustomerName("dzz2");
        voucherDetailModel1.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel1.setPaymentBank("dzz9");
        voucherDetailModel1.setPaymentName("dzz2");
        voucherDetailModel1.setReceivableAccountNo("12345tyui");
        voucherDetailModel1.setTransactionGateway(0);
        voucherDetailModel1.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel1.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel1.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");

        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);

        List<ThirdPartVoucherRepayDetail> objects1 = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel1 = new ThirdPartVoucherRepayDetail();
        detailsModel1.setAmount(new BigDecimal(100000));
        detailsModel1.setCurrentPeriod(2);
        detailsModel1.setInterest(new BigDecimal(0));
        detailsModel1.setLateFee(new BigDecimal(0));
        detailsModel1.setLateOtherCost(new BigDecimal(0));
        detailsModel1.setLatePenalty(new BigDecimal(0));
        detailsModel1.setMaintenanceCharge(new BigDecimal(0));
        detailsModel1.setOtherCharge(new BigDecimal(0));
        detailsModel1.setPenaltyFee(new BigDecimal(0));
        detailsModel1.setPrincipal(new BigDecimal(100000));
        detailsModel1.setRepayScheduleNo("6666666666");
        detailsModel1.setServiceCharge(new BigDecimal(0));
        objects1.add(detailsModel1);
        voucherDetailModel1.setRepayDetailList(objects1);
        details.add(voucherDetailModel);
        details.add(voucherDetailModel1);

        log.info("details size is {}",details.size());
        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G31700");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initFinancialContractNotExistData() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo","ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo","G11000");
        mapParam.put("detailList",OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));
        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initErrorTransactionTime()throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(1);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", "ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo", "G31700");
        mapParam.put("detailList", OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo",mapParam.get("requestNo"));
        requestBody.add("financialContractNo",mapParam.get("financialContractNo"));
        requestBody.add("detailList",mapParam.get("detailList"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initRepeatedRepayScheduleNo() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel1 = new ThirdPartVoucherRepayDetail();
        detailsModel1.setAmount(new BigDecimal(100000));
        detailsModel1.setCurrentPeriod(1);
        detailsModel1.setInterest(new BigDecimal(0));
        detailsModel1.setLateFee(new BigDecimal(0));
        detailsModel1.setLateOtherCost(new BigDecimal(0));
        detailsModel1.setLatePenalty(new BigDecimal(0));
        detailsModel1.setMaintenanceCharge(new BigDecimal(0));
        detailsModel1.setOtherCharge(new BigDecimal(0));
        detailsModel1.setPenaltyFee(new BigDecimal(0));
        detailsModel1.setPrincipal(new BigDecimal(100000));
        detailsModel1.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel1.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel1);
        ThirdPartVoucherRepayDetail detailsModel2 = new ThirdPartVoucherRepayDetail();
        detailsModel2.setAmount(new BigDecimal(100000));
        detailsModel2.setCurrentPeriod(1);
        detailsModel2.setInterest(new BigDecimal(0));
        detailsModel2.setLateFee(new BigDecimal(0));
        detailsModel2.setLateOtherCost(new BigDecimal(0));
        detailsModel2.setLatePenalty(new BigDecimal(0));
        detailsModel2.setMaintenanceCharge(new BigDecimal(0));
        detailsModel2.setOtherCharge(new BigDecimal(0));
        detailsModel2.setPenaltyFee(new BigDecimal(0));
        detailsModel2.setPrincipal(new BigDecimal(100000));
        detailsModel2.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel2.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel2);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", "ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo", "G31700");
        mapParam.put("detailList", OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("detailList", mapParam.get("detailList"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initRepeatedCurrentPeriod() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel1 = new ThirdPartVoucherRepayDetail();
        detailsModel1.setAmount(new BigDecimal(100000));
        detailsModel1.setCurrentPeriod(1);
        detailsModel1.setInterest(new BigDecimal(0));
        detailsModel1.setLateFee(new BigDecimal(0));
        detailsModel1.setLateOtherCost(new BigDecimal(0));
        detailsModel1.setLatePenalty(new BigDecimal(0));
        detailsModel1.setMaintenanceCharge(new BigDecimal(0));
        detailsModel1.setOtherCharge(new BigDecimal(0));
        detailsModel1.setPenaltyFee(new BigDecimal(0));
        detailsModel1.setPrincipal(new BigDecimal(100000));
        detailsModel1.setRepayScheduleNo("TESTDZZ0821_97DZZ1");
        detailsModel1.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel1);
        ThirdPartVoucherRepayDetail detailsModel2 = new ThirdPartVoucherRepayDetail();
        detailsModel2.setAmount(new BigDecimal(100000));
        detailsModel2.setCurrentPeriod(1);
        detailsModel2.setInterest(new BigDecimal(0));
        detailsModel2.setLateFee(new BigDecimal(0));
        detailsModel2.setLateOtherCost(new BigDecimal(0));
        detailsModel2.setLatePenalty(new BigDecimal(0));
        detailsModel2.setMaintenanceCharge(new BigDecimal(0));
        detailsModel2.setOtherCharge(new BigDecimal(0));
        detailsModel2.setPenaltyFee(new BigDecimal(0));
        detailsModel2.setPrincipal(new BigDecimal(100000));
        detailsModel2.setRepayScheduleNo("6666666666666666");
        detailsModel2.setServiceCharge(new BigDecimal(0));
        objects.add(detailsModel2);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", "ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo", "G31700");
        mapParam.put("detailList", OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("detailList", mapParam.get("detailList"));

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullParamsThirdPartVoucherRepayDetail() throws JsonProcessingException{
        details = new ArrayList<>();
        ThirdPartVoucherDetail voucherDetailModel=  new ThirdPartVoucherDetail();

        voucherDetailModel.setAmount(new BigDecimal(200000));
        voucherDetailModel.setBankTransactionNo("7f44af31-0368-45fa-8a02-c1119e7a19ee");
        voucherDetailModel.setComment("213456");
        voucherDetailModel.setCompleteTime(new Date().toString());
        voucherDetailModel.setContractUniqueId("TESTDZZ0821_97");
        voucherDetailModel.setCreateTime(new Date());
        voucherDetailModel.setCurrency(0);
        voucherDetailModel.setCustomerIdNo("1234567890-");
        voucherDetailModel.setCustomerName("dzz2");
        voucherDetailModel.setPaymentAccountNo("23r4t5y6i8o9");
        voucherDetailModel.setPaymentBank("dzz9");
        voucherDetailModel.setPaymentName("dzz2");
        voucherDetailModel.setReceivableAccountNo("12345tyui");
        voucherDetailModel.setTransactionGateway(0);
        voucherDetailModel.setTransactionRequestNo("JIAOYIQINGQIU23");
        voucherDetailModel.setTransactionTime("2017-08-29 00:00:00");
        voucherDetailModel.setVoucherNo("ff660cbd-3f1d-4619-afc1-d95216380fb4");
        List<ThirdPartVoucherRepayDetail> objects = new ArrayList<>();
        ThirdPartVoucherRepayDetail detailsModel = new ThirdPartVoucherRepayDetail();
        detailsModel.setAmount(new BigDecimal(100000));
        detailsModel.setCurrentPeriod(null);
        detailsModel.setInterest(new BigDecimal(0));
        detailsModel.setLateFee(new BigDecimal(0));
        detailsModel.setLateOtherCost(new BigDecimal(0));
        detailsModel.setLatePenalty(new BigDecimal(0));
        detailsModel.setMaintenanceCharge(new BigDecimal(0));
        detailsModel.setOtherCharge(new BigDecimal(0));
        detailsModel.setPenaltyFee(new BigDecimal(0));
        detailsModel.setPrincipal(new BigDecimal(100000));
        detailsModel.setRepayScheduleNo("");
        detailsModel.setServiceCharge(new BigDecimal(0));
        detailsModel.setRepaymentPlanNo("");
        objects.add(detailsModel);
        voucherDetailModel.setRepayDetailList(objects);
        details.add(voucherDetailModel);

        mapParam = new HashMap<>();
        mapParam.put("requestNo", "ad27d1f3-f455-4f75-a22a-d315a28c44df");
        mapParam.put("financialContractNo", "G31700");
        mapParam.put("detailList", OBJECT_MAPPER.writeValueAsString(details));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("financialContractNo", mapParam.get("financialContractNo"));
        requestBody.add("detailList", mapParam.get("detailList"));

        return new HttpEntity<>(requestBody, headers);
    }
}
