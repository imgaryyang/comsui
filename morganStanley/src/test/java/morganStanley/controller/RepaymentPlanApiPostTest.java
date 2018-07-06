package morganStanley.controller;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.request.repayment.ContractDetail;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackage;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackageContent;
import com.suidifu.morganstanley.model.request.repayment.ImportRepaymentPlanDetail;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageRequestModel;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeRequestModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

import static com.suidifu.morganstanley.utils.PostTestUtils.sendPost;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class RepaymentPlanApiPostTest {
    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG" +
            "/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL" +
            "+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD" +
            "/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU" +
            "+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV" +
            "+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx" +
            "/eMcITaLq8l1qzZ907UXY+Mfs=";

    @Before
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackage_temp_interface_for_async.sql")
    public void setUp() throws Exception {
    }

    @Test
    @Sql("classpath:test/testImportAssetPackageApi.sql")
    public void test() {

        com.suidifu.morganstanley.model.request.repayment.ImportAssetPackageContent importAssetPackageContent = new com.suidifu.morganstanley.model.request.repayment.ImportAssetPackageContent();
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
        // repaymentPlanDetail2.setRepayScheduleNo("outerRepaymentPlanNo2");
        repaymentPlanDetails.add(repaymentPlanDetail2);
        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        ImportAssetPackageRequestModel requestModel = new ImportAssetPackageRequestModel();
//		requestModel.setFn("200004");
        requestModel.setRequestNo("13456789");
        requestModel.setImportAssetPackageContent(JsonUtils.toJsonString(importAssetPackageContent));

        String param = JsonUtils.toJsonString(importAssetPackageContent);
        // System.out.println(param);
        Map<String, String> params = new HashMap<>();
//		params.put("fn", "200004");
        params.put("requestNo", UUID.randomUUID().toString());
        params.put("importAssetPackageContent", param);

        Map<String, String> headers = new HashMap<>();
        headers.put("merId", "t_test_zfb");
        headers.put("secret", "123456");
        String signContent = ApiSignUtils.getSignCheckContent(params);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.put("sign", sign);
        String result;
        try {
            result = sendPost("http://127.0.0.1:7778/api/v3/importAssetPackage", params, headers);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {

        ModifyOverDueFeeRequestModel model1 = new ModifyOverDueFeeRequestModel();
        model1.setRequestNo(UUID.randomUUID().toString());
        String modifyOverDueFeeDetails6 = "[{\"contractUniqueId\":\"f370083f0eb54e21aab6b8a8840f0c0d\",\"repaymentPlanNo\":\"ZC79614965091745792\"," +
                "\"overDueFeeCalcDate\":\"2017-08-24\",\"penaltyFee\":5.0,\"latePenalty\":0.0,\"lateFee\":0.0,\"lateOtherCost\":0.0,\"totalOverdueFee\":5.0," +
                "\"repayScheduleNo\":null},{\"contractUniqueId\":\"f370083f0eb54e21aab6b8a8840f0c0d\",\"repaymentPlanNo\":\"ZC79614965116911616\"," +
                "\"overDueFeeCalcDate\":\"2017-08-24\",\"penaltyFee\":13.0,\"latePenalty\":0.0,\"lateFee\":0.0,\"lateOtherCost\":0.0,\"totalOverdueFee\":13.0," +
                "\"repayScheduleNo\":null},{\"contractUniqueId\":\"f370083f0eb54e21aab6b8a8840f0c0d\",\"repaymentPlanNo\":\"ZC79614965121105920\"," +
                "\"overDueFeeCalcDate\":\"2017-08-24\",\"penaltyFee\":26.0,\"latePenalty\":0.0,\"lateFee\":0.0,\"lateOtherCost\":0.0,\"totalOverdueFee\":26.0," +
                "\"repayScheduleNo\":null}]";
        model1.setModifyOverDueFeeDetails(modifyOverDueFeeDetails6);

        // System.out.println(param);
        Map<String, String> params = new HashMap<>();
        params.put("fn", "200005");
        params.put("requestNo", UUID.randomUUID().toString());
        params.put("modifyOverDueFeeDetails", modifyOverDueFeeDetails6);

        Map<String, String> headers = new HashMap<>();
        headers.put("merId", "t_test_zfb");
        headers.put("secret", "123456");
        String signContent = ApiSignUtils.getSignCheckContent(params);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        headers.put("sign", sign);
        String result;
        try {
            result = sendPost("http://yunxin.5veda.net/api/modify", params, headers);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private FinancialContractService financialContractService;

    @Test
    @Sql("classpath:test/api/importAssetPackage/testImportAssetPackageApi.sql")
    public void test3() {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy("G0000000");

        ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
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
        repaymentPlanDetail1.setRepaymentDate("2020-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNo1");
        repaymentPlanDetails.add(repaymentPlanDetail1);

        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail2.setRepaymentInterest("20.00");
        repaymentPlanDetail2.setRepaymentDate("2020-10-04");
        repaymentPlanDetail2.setOtheFee("0.00");
        repaymentPlanDetail2.setLoanServiceFee("0.00");
        repaymentPlanDetail2.setTechMaintenanceFee("0.00");
        // repaymentPlanDetail2.setRepayScheduleNo("outerRepaymentPlanNo2");
        repaymentPlanDetails.add(repaymentPlanDetail2);
        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);

        ImportAssetPackage requestModel = new ImportAssetPackage();
        String requestNo = UUID.randomUUID().toString();
        requestModel.setRequestNo(requestNo);
        requestModel.setImportAssetPackageContent(JsonUtils.toJsonString(importAssetPackageContent));
        requestModel.setCallbackUrl("1234");
//        requestModel.setResponseMode(ImportAssetPackage.RSP_ASYNC_MOD);

        String param = JsonUtils.toJsonString(importAssetPackageContent);
        Map<String, String> params = new HashMap<>();
        params.put("requestNo", requestNo);
        params.put("importAssetPackageContent", param);
        params.put("callbackUrl", "1234");
//        params.put("responseMode", ImportAssetPackage.RSP_ASYNC_MOD);

        String signContent = ApiSignUtils.getSignCheckContent(params);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);
        try {
            HttpHeaders headers = new BaseUnitTest().initHttpHeaders();
            headers.add("merId", "t_test_zfb");
            headers.add("secret", "123456");
            headers.add("sign", sign);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("requestNo", requestNo);
            requestBody.add("importAssetPackageContent", param);
            requestBody.add("callbackUrl", "1234");
//            requestBody.add("responseMode", ImportAssetPackage.RSP_ASYNC_MOD);
            HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
            template.postForObject("http://127.0.0.1:7778/api/v3/importAssetPackage", formEntity, BaseResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
