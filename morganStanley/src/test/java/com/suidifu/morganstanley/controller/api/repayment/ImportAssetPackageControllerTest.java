package com.suidifu.morganstanley.controller.api.repayment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.request.repayment.ContractDetail;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackageContent;
import com.suidifu.morganstanley.model.request.repayment.ImportRepaymentPlanDetail;
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
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_IMPORT_ASSET_PACKAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/25 <br>
 * Time:下午5:54 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class ImportAssetPackageControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private HttpHeaders headers;
    private MultiValueMap<String, String> requestBody;
    @Autowired
    private TestRestTemplate template;
    private String privateKey;

    private ImportAssetPackageContent importAssetPackageContent;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
        importAssetPackageContent = new ImportAssetPackageContent();
        cacheManager.getCache("citys").clear();
    }

    @After
    public void tearDown() throws Exception {
        requestBody = null;
        headers = null;
        privateKey = null;
        importAssetPackageContent = null;
    }

    /**
     * 正常同步测试 成功
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage() throws Exception {
        importAssetPackageContentData();

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    /**
     * 新增异步测试 成功
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testAsyImportAssetPackageSuccess() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initAsyImportAssetPackageData("1","callBackUrl"), BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));

        //todo
    }

    /**
     * 商户还款计划编号重复
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi_repeat_RepayScheduleNo.sql")
    public void testImportAssetPackage_repeat_RepayScheduleNo() throws Exception {
        importAssetPackageContentData();

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPEAT_OUTER_REPAYMENT_PLAN_NO.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPEAT_OUTER_REPAYMENT_PLAN_NO.getMessage()));
    }

    /**
     * 信托产品代码错误
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_financialProductCode_error.sql")
    public void testImportAssetPackage_financialProductCode_error() throws Exception {
        importAssetPackageContentData();

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FINANCIAL_PRODUCT_CODE_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FINANCIAL_PRODUCT_CODE_ERROR.getMessage()));
    }

    /**
     * 未查询到合同对应放款
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_no_match_remittance.sql")
    public void testImportAssetPackage_no_match_remittance() throws Exception {
        importAssetPackageContentData();

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.NO_MATCH_REMITTANCE.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.NO_MATCH_REMITTANCE.getMessage()));
    }

    /**
     * 批次合同总条数有误
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_total_contracts_number_not_match() throws Exception {
        importAssetPackageContentData(2,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.TOTAL_CONTRACTS_NUMBER_NOT_MATCH.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.TOTAL_CONTRACTS_NUMBER_NOT_MATCH.getMessage()));
    }

    /**
     * 信托合同商户未设置
     */
    @Test
    @Ignore("hibernate objectNotFoundException : cannot evaluate com.zufangbao.sun.entity.company.corp.App 待优化?懒加载")
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_not_set_app_in_financial_contract.sql")
    public void testImportAssetPackage_not_set_app_in_financial_contract() throws Exception{
        importAssetPackageContentData();

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(),equalTo(ApiMessage.NOT_SET_APP_IN_FINANCIAL_CONTRACT.getCode()));
        assertThat(resultResponse.getMessage(),equalTo(ApiMessage.NOT_SET_APP_IN_FINANCIAL_CONTRACT.getMessage()));
    }

    /**
     * 批次合同总条数不能小于等于0
     */
    @Test
    public void testImportAssetPackage_total_number_error() throws Exception{
        importAssetPackageContentData(0,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("批次合同总条数[thisBatchContractsTotalNumber]不能小于等于0"));
    }

    /**
     * 合同还款本金总额格式错误 (金额为负)
     */
    @Test
    public void testImportAssetPackage_total_amount_error() throws Exception{
        importAssetPackageContentData(1,"-1.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(),equalTo(ApiMessage.TOTAL_AMOUNT_ERROR.getCode()));
        assertThat(resultResponse.getMessage(),equalTo(ApiMessage.TOTAL_AMOUNT_ERROR.getMessage()));
    }

    /**
     * 异步导入资产包回调地址为空
     */
    @Test
    @Ignore("现在可以为空")
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testNullCallBackUrl() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initAsyImportAssetPackageData("1",null), BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("异步响应方式下回调地址不能为空"));
    }

    /**
     * 批次合同本金总额为空
     */
    @Test
    public void testNullTotalAmountError() throws Exception{
        importAssetPackageContentData(1,"","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(),equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("批次合同本金总额[thisBatchContractsTotalAmount]不能为空"));
    }

    /**
     * 贷款合同唯一识别编号[uniqueId]为空
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_loan_contract_no_or_uniqueid_is_empty() throws Exception {
        importAssetPackageContentData(1,"4000.00","G0000000","", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(),equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款合同唯一识别编号[uniqueId]不能为空"));
    }

    /**
     * 未知银行代码
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_no_match_bank() throws Exception {
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "000000",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(),equalTo(ApiMessage.NO_MATCH_BANK.getCode()));
        assertThat(resultResponse.getMessage(),equalTo(ApiMessage.NO_MATCH_BANK.getMessage()));
    }

    /**
     * 批次合同本金总额有误
     */
    @Test
    public void testImportAssetPackage_total_contracts_amount_not_match() throws Exception{
        importAssetPackageContentData(1,"1000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.TOTAL_CONTRACTS_AMOUNT_NOT_MATCH.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.TOTAL_CONTRACTS_AMOUNT_NOT_MATCH.getMessage()));
    }

    /**
     * 还款计划总条数错误
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_repayment_plan_total_periods_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",9,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPAYMENT_PLAN_TOTAL_PERIODS_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPAYMENT_PLAN_TOTAL_PERIODS_ERROR.getMessage()));
    }

    /**
     * 贷款客户姓名为空
     */
    @Test
    public void testImportAssetPackage_loan_customer_name_null() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款客户姓名[loanCustomerName]不能为空"));
    }

    /**
     * 贷款客户编号为空
     */
    @Test
    public void testImportAssetPackage_loan_customer_no_null() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款客户编号[loanCustomerNo]不能为空"));
    }

    /**
     * 贷款客户身份证号码为空
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_id_card_null() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));


        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款客户身份证号码[iDCardNo]不能为空"));
    }

    /**
     *贷款本金总额格式错误 (金额为空)
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_loan_total_amount_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));


        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.LOAN_TOTAL_AMOUNT_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.LOAN_TOTAL_AMOUNT_ERROR.getMessage()));
    }

    /**
     * 贷款期数小于等于0
     */
    @Test
    public void testImportAssetPackage_loan_periods_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",-1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));


        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款期数[loanPeriods]不能小于等于0"));
    }

    /**
     * 还款账户号为空
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_repayment_account_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("还款账户号[repaymentAccountNo]不能为空"));
    }

    /**
     * 设定生效日期格式错误
     */
    @Test
    public void testImportAssetPackage_effect_date_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("设定生效日期[effectDate]格式错误"));
    }

    /**
     * 设定到期日期格式错误
     */
    @Test
    public void testImportAssetPackage_expire_date_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","1","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("设定到期日期[expiryDate]格式错误"));
    }

    /**
     * 贷款利率格式错误 (利率为负数)
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_loan_rates_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","-1", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(), containsString("贷款利率[loanRates]格式错误"));
    }

    /**
     * 未知回款方式
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_no_match_repayment_way() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 10,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.NO_MATCH_REPAYMENT_WAY.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.NO_MATCH_REPAYMENT_WAY.getMessage()));
    }

    /**
     * 还款日期格式错误
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_repayment_plan_repayment_date_error() throws Exception{

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initRepaymentPlanRepaymentDateErrorData(), BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPAYMENT_PLAN_REPAYMENT_DATE_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPAYMENT_PLAN_REPAYMENT_DATE_ERROR.getMessage()));
    }

    /**
     * 还款本金格式错误 (为负金额)
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_repayment_principal_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("-4000.00","20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPAYMENT_PRINCIPAL_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPAYMENT_PRINCIPAL_ERROR.getMessage()));
    }

    /**
     * 还款利息格式错误 (为负金额)
     */
    @Test
    public void testImportAssetPackage_repayment_interest_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","-20.00","2018-09-04","0.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPAYMENT_INTEREST_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPAYMENT_INTEREST_ERROR.getMessage()));
    }

    /**
     * 技术服务费格式错误 (为负金额)
     */
    @Test
    public void testImportAssetPackage_tech_maintenance_fee_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","-10.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.TECH_MAINTENANCE_FEE_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.TECH_MAINTENANCE_FEE_ERROR.getMessage()));
    }

    /**
     * 贷款服务费格式错误 (为负金额)
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_loan_service_fee_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","-10.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.LOAN_SERVICE_FEE_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.LOAN_SERVICE_FEE_ERROR.getMessage()));
    }

    /**
     * 其他金额格式错误 (为负金额)
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_other_fee_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","-10.00","0.00","0.00"));

        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.OTHER_FEE_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.OTHER_FEE_ERROR.getMessage()));
    }

    /**
     * 贷款合同编号重复
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_exist_loan_contract_no.sql")
    public void testImportAssetPackage_exist_loan_contract_no() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo_repeat", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.EXIST_LOAN_CONTRACT_NO.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.EXIST_LOAN_CONTRACT_NO.getMessage()));
    }

    /**
     * 贷款合同唯一编号重复
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_exist_loan_contract_unique_id.sql")
    public void testImportAssetPackage_exist_loan_contract_unique_id() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","unique_id_repeat", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.EXIST_LOAN_CONTRACT_UNIQUE_ID.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.EXIST_LOAN_CONTRACT_UNIQUE_ID.getMessage()));
    }

    /**
     * 还款计划总金额错误
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testImportAssetPackage_repayment_plan_total_amount_error() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("1000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR.getMessage()));
    }

    /**
     * 首期还款还款日期错误
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_first_repayment_date_not_correct.sql")
    public void testImportAssetPackage_first_repayment_date_not_correct() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initFirstRepaymentDateNotCorrectData(), BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FIRST_REPAYMENT_DATE_NOT_CORRECT.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FIRST_REPAYMENT_DATE_NOT_CORRECT.getMessage()));
    }

    /**
     * 标的已存在
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_exist_the_subject_matter.sql")
    public void testImportAssetPackage_exist_the_subject_matter() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.EXIST_THE_SUBJECT_MATTER.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.EXIST_THE_SUBJECT_MATTER.getMessage()));
    }

    /**
     * 城市与省份代码不匹配
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_city_and_province_code_does_not_match.sql")
    public void testImportAssetPackage_city_and_province_code_does_not_match() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.CITY_AND_PROVINCE_CODE_DOES_NOT_MATCH.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.CITY_AND_PROVINCE_CODE_DOES_NOT_MATCH.getMessage()));
    }

    /**
     * 信托代码与对应放款中信托代码不匹配
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_trust_code_not_agree_with_trust_code_of_remittance.sql")
    public void testImportAssetPackage_trust_code_not_agree_with_trust_code_of_remittance() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.TRUST_CODE_NOT_AGREE_WITH_TRUST_CODE_OF_REMITTANCE.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.TRUST_CODE_NOT_AGREE_WITH_TRUST_CODE_OF_REMITTANCE.getMessage()));
    }

    /**
     * 本金和放款金额不等
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_loan_total_amount_not_match_remittance_amount.sql")
    public void testImportAssetPackage_loan_total_amount_not_match_remittance_amount() throws Exception{
        importAssetPackageContentData(1,"4000.00","G0000000","34567890", "contractNo1", "customerNo1", "郑航波", "330683199403062411", "C10102",
                "23456787654323456", "4000.00",1,
                "2016-8-1","2099-01-01","0.156", "0.0005", 1,
                getRepaymentPlanDetailsList("4000.00","20.00","2018-09-04","0.00","0.00","0.00"));
        HttpEntity<MultiValueMap<String,String>> formEntity = new HttpEntity<>(requestBody,headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.LOAN_TOTAL_AMOUNT_NOT_MATCH_REMITTANCE_AMOUNT.getCode()));
        assertThat(resultResponse.getMessage(),containsString("本金和放款成功金额不等"));
    }

    /**
     * 导入资产包请求内容为空
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testNullImportAssetPackageContentData() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initNullImportAssetPackageContent(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(),containsString("导入资产包请求内容[importAssetPackageContent]不能为空"));
    }

    /**
     * 贷款合同详情为空
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testNullContractDetailData() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initNullContractDetailData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(),containsString("贷款合同详情[contractDetails]不能为空"));
    }

    /**
     * 还款日期为空
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testNullRepaymentDate() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initNullRepaymentDate(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(),containsString("还款日期[repaymentDate]不能为空"));
    }

    /**
     * 还款计划详情为空
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testNullRepaymentPlanDetailData() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initNullRepaymentPlanDetailData(), BaseResponse.class);

        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.INVALID_PARAMS.getCode()));
        assertThat(resultResponse.getMessage(),containsString("还款计划详情[repaymentPlanDetails]不能为空"));
    }

    /**
     * 潍坊自定义检验失败(热编译)
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void testCustomer_fee_check_fail() throws Exception{
        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, initCustomerFeeCheckFail(), BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getMessage()));
    }

    /**
     * 首期还款日不能早于或者等于当前日
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi_first_period_date_before_today.sql")
    public void testImportAssetPackage_first_period_date_after_today() throws Exception {
        importAssetPackageContentData_first_period_before_today();

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.FIRST_REPAYMENT_DATE_NOT_CORRECT.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.FIRST_REPAYMENT_DATE_NOT_CORRECT.getMessage()));
    }

    /**
     * 无放款模式
     */
    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi_noRemittance.sql")
    public void testImportAssetPackage_noRemittance() throws Exception {
        importAssetPackageContentData_first_period_before_today();

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        BaseResponse resultResponse = template.postForObject(URL_API_V3 + URL_IMPORT_ASSET_PACKAGE, formEntity, BaseResponse.class);
        log.info("message is {}", resultResponse.getMessage());

        assertThat(resultResponse.getCode(), equalTo(ApiMessage.SUCCESS.getCode()));
        assertThat(resultResponse.getMessage(), equalTo(ApiMessage.SUCCESS.getMessage()));
    }

    private void importAssetPackageContentData() throws JsonProcessingException {
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        repaymentPlanDetail1.setRepaymentDate("2018-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("20.00");
        repaymentPlanDetail2.setRepaymentDate("2018-10-04");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        repaymentPlanDetails.add(repaymentPlanDetail2);

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));
    }

    private void importAssetPackageContentData(int totalNumber, String totalAmount, String financialProductCode, String uniqueId,
                                               String loanContractNo, String loanCustomerNo, String loanCustomerName, String IDCardNo, String bankCode,
                                               String repaymentAccountNo, String loanTotalAmount, int loanPeriods,
                                               String effectDate, String expiryDate, String loanRates,
                                               String penalty, int repaymentWay, List<ImportRepaymentPlanDetail> detailList) throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(totalNumber);
        importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);
        importAssetPackageContent.setFinancialProductCode(financialProductCode);

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId(uniqueId);
        contractDetail.setLoanContractNo(loanContractNo);
        contractDetail.setLoanCustomerNo(loanCustomerNo);
        contractDetail.setLoanCustomerName(loanCustomerName);
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo(IDCardNo);
        contractDetail.setBankCode(bankCode);
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo(repaymentAccountNo);
        contractDetail.setLoanTotalAmount(loanTotalAmount);
        contractDetail.setLoanPeriods(loanPeriods);
        contractDetail.setEffectDate(effectDate);
        contractDetail.setExpiryDate(expiryDate);
        contractDetail.setLoanRates(loanRates);
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty(penalty);
        contractDetail.setRepaymentWay(repaymentWay);
        contractDetail.setRepaymentPlanDetails(detailList);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));
    }

    private List<ImportRepaymentPlanDetail> getRepaymentPlanDetailsList(String repaymentPrincipal, String repaymentInterest, String repaymentDate,
                                                                        String otheFee, String loanServiceFee, String techMaintenanceFee) {
        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();
        ImportRepaymentPlanDetail repaymentPlanDetail = new ImportRepaymentPlanDetail();
        repaymentPlanDetail.setRepaymentPrincipal(repaymentPrincipal);
        repaymentPlanDetail.setRepaymentInterest(repaymentInterest);
        repaymentPlanDetail.setRepaymentDate(repaymentDate);
        repaymentPlanDetail.setOtheFee(otheFee);
        repaymentPlanDetail.setLoanServiceFee(loanServiceFee);
        repaymentPlanDetail.setTechMaintenanceFee(techMaintenanceFee);
        repaymentPlanDetail.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail);
        return repaymentPlanDetails;
    }

    private HttpEntity<MultiValueMap<String,String>> initNullImportAssetPackageContent() throws JsonProcessingException{
        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", null);
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullContractDetailData() throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initAsyImportAssetPackageData(String responseMode, String callbackUrl) throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        repaymentPlanDetail1.setRepaymentDate("2018-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("20.00");
        repaymentPlanDetail2.setRepaymentDate("2018-10-04");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        repaymentPlanDetails.add(repaymentPlanDetail2);

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("responseMode",responseMode);
        mapParam.put("callbackUrl",callbackUrl);
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("responseMode", mapParam.get("responseMode"));
        requestBody.add("callbackUrl", mapParam.get("callbackUrl"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullRepaymentPlanDetailData() throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initFirstRepaymentDateNotCorrectData() throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        repaymentPlanDetail1.setRepaymentDate("2016-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("20.00");
        repaymentPlanDetail2.setRepaymentDate("2016-10-04");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        repaymentPlanDetails.add(repaymentPlanDetail2);

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initRepaymentPlanRepaymentDateErrorData() throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        repaymentPlanDetail1.setRepaymentDate("2018-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("20.00");
        repaymentPlanDetail2.setRepaymentDate("2016-10-04");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        repaymentPlanDetails.add(repaymentPlanDetail2);

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initNullRepaymentDate() throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("20.00");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        repaymentPlanDetails.add(repaymentPlanDetail2);

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private HttpEntity<MultiValueMap<String,String>> initCustomerFeeCheckFail() throws JsonProcessingException{
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("600.00");
        repaymentPlanDetail1.setRepaymentDate("2018-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("580.00");
        repaymentPlanDetail2.setRepaymentDate("2018-10-04");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        repaymentPlanDetails.add(repaymentPlanDetail2);

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));

        return new HttpEntity<>(requestBody,headers);
    }

    private void importAssetPackageContentData_first_period_before_today() throws JsonProcessingException {
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G0000000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId("34567890");
        contractDetail.setLoanContractNo("contractNo1");
        contractDetail.setLoanCustomerNo("customerNo1");
        contractDetail.setLoanCustomerName("郑航波");
        contractDetail.setSubjectMatterassetNo("234567");
        contractDetail.setIDCardNo("330683199403062411");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        repaymentPlanDetail1.setRepaymentDate("2017-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("20.00");
        repaymentPlanDetail2.setRepaymentDate("2018-10-04");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        repaymentPlanDetails.add(repaymentPlanDetail2);

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        headers = new BaseUnitTest().initHttpHeaders();

        Map<String, String> mapParam = new HashMap<>();
        mapParam.put("requestNo", UUID.randomUUID().toString());
        mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(importAssetPackageContent));
        String signContent = ApiSignUtils.getSignCheckContent(mapParam);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.add("sign", sign);

        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("requestNo", mapParam.get("requestNo"));
        requestBody.add("importAssetPackageContent", mapParam.get("importAssetPackageContent"));
    }
}