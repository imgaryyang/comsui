package com.suidifu.morganstanley.controller.api.repayment;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.model.request.repayment.ContractDetail;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackageContent;
import com.suidifu.morganstanley.model.request.repayment.ImportRepaymentPlanDetail;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_ASYNC_IMPORT_ASSET_PACKAGE;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
@Sql("classpath:test/api/asyncImportAssetPackage/testAsyncImportAssetPackage.sql")
public class ImportAssetPackageTest {
    public static final String TEST_HOST = "http://127.0.0.1:7778";
    public static final String ASYNC_IMPORT_ASSET_PACKAGE_URL = TEST_HOST + URL_API_V3+URL_ASYNC_IMPORT_ASSET_PACKAGE;
    @Autowired
    private GenericDaoSupport genericDaoSupport;
    @Autowired
    private TestRestTemplate template;

    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

    class ImportPackage implements Runnable {
        private int num;

        public ImportPackage(int num) {
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 1000;
            int cnt = 0;
            System.out.println("id " + id);
            for (int index = 0; index < 1000; index++) {
                String a = id + index + "";
                String uniqueId = "TestAsyncImport" + a;
                ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
                importAssetPackageContent.setThisBatchContractsTotalNumber(3);
                importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
                importAssetPackageContent.setFinancialProductCode("G32000");

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
                contractDetail.setRepaymentWay(2);

                List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

                ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
                repaymentPlanDetail1.setRepaymentPrincipal("2.00");
                repaymentPlanDetail1.setRepaymentInterest("1.00");
                repaymentPlanDetail1.setRepaymentDate("2017-06-01");
                repaymentPlanDetail1.setOtheFee("1.00");
                repaymentPlanDetail1.setTechMaintenanceFee("1.00");
                repaymentPlanDetail1.setLoanServiceFee("1.00");
                repaymentPlanDetails.add(repaymentPlanDetail1);

                ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
                repaymentPlanDetail2.setRepaymentPrincipal("2.00");
                repaymentPlanDetail2.setRepaymentInterest("1.00");
                repaymentPlanDetail2.setRepaymentDate("2017-07-01");
                repaymentPlanDetail2.setOtheFee("1.00");
                repaymentPlanDetail2.setTechMaintenanceFee("1.00");
                repaymentPlanDetail2.setLoanServiceFee("1.00");
                repaymentPlanDetails.add(repaymentPlanDetail2);


                ImportRepaymentPlanDetail repaymentPlanDetail3 = new ImportRepaymentPlanDetail();
                repaymentPlanDetail3.setRepaymentPrincipal("2.00");
                repaymentPlanDetail3.setRepaymentInterest("1.00");
                repaymentPlanDetail3.setRepaymentDate("2017-08-01");
                repaymentPlanDetail3.setOtheFee("1.00");
                repaymentPlanDetail3.setTechMaintenanceFee("1.00");
                repaymentPlanDetail3.setLoanServiceFee("1.00");
                repaymentPlanDetails.add(repaymentPlanDetail3);

                contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

                contracts.add(contractDetail);
                importAssetPackageContent.setContractDetails(contracts);

                String param = JsonUtils.toJsonString(importAssetPackageContent);
                System.out.println(param);
                Map<String, String> params = new HashMap<String, String>();
                String requestNo = UUID.randomUUID().toString();
                params.put("requestNo", requestNo);
                params.put("importAssetPackageContent", param);
                params.put("callbackUrl", "1234");
                String signContent = ApiSignUtils.getSignCheckContent(params);
                String sign = ApiSignUtils.rsaSign(signContent, privateKey);
                System.out.println(sign);

                try {
                    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
                    HttpHeaders headers = new BaseUnitTest().initHttpHeaders();
                    headers.add("merId", "t_test_zfb");
                    headers.add("secret", "123456");
                    headers.add("sign", sign);
                    requestBody.add("requestNo", requestNo);
                    requestBody.add("importAssetPackageContent", param);
                    requestBody.add("callbackUrl", "1234");
                    HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);
                    BaseResponse baseResponse = template.postForObject(ASYNC_IMPORT_ASSET_PACKAGE_URL, formEntity, BaseResponse.class);
                    cnt++;
//                    String sr = PostTestUtils.sendPost("", params, headers);
                    System.out.println("===========" + index + "====" + baseResponse.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("已导入" + (cnt + 1) + "笔资产包");
        }
    }

    /**
     * 异步导入资产包压力测试工具2 (正确数据)
     */
    @Test
    public void testImportPackage() {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread myThread = new Thread(new ImportPackage(i));
            threads.add(myThread);
        }
        threads.parallelStream().forEach(t -> {
            try {
                t.start();
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("");
            }
        });
    }


    @Test
    public void TestImportAsyImportAssetPackage() throws Exception {
        long startTime =  System.currentTimeMillis();
        log.info("开始执行时间[{}]",startTime);

        stressTest(ASYNC_IMPORT_ASSET_PACKAGE_URL, 20000,1);

        long endTime = System.currentTimeMillis();

        log.info(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        log.info("执行总时间[{}]",endTime-startTime);
    }

    private void stressTest(String url, int threadNum, int execCount) throws InterruptedException {
        log.info("url:[{}]", url);
        log.info(",threadNum:[{}]", threadNum);
        log.info(",execCount:[{}]", execCount);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadNum; i++) {
            final int x = i;
            Thread thread = new Thread(() -> {
                log.info("index is {}", x);
                try {
                    execute(url, execCount);
                } catch (Exception e) {
                    log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        log.info("{}x{}测试结束", threadNum, execCount);
    }


    private void execute(String url, int execCount) {
        for (int i = 0; i < execCount; i++) {
            String param = getImportAssetPackageData();
            System.out.println(param);
            Map<String, String> params = new HashMap<String, String>();
            String requestNo = UUID.randomUUID().toString();
            params.put("requestNo", requestNo);
            params.put("importAssetPackageContent", param);
//            params.put("callbackUrl", "1234");
            String signContent = ApiSignUtils.getSignCheckContent(params);
            String sign = ApiSignUtils.rsaSign(signContent, privateKey);
            System.out.println(sign);


            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//            Map<String, String> headers = new HashMap<>();
            HttpHeaders headers = new HttpHeaders();
            headers.add("merId", "t_test_zfb");
            headers.add("secret", "123456");
            headers.add("sign", sign);
            requestBody.add("requestNo", requestNo);
            requestBody.add("importAssetPackageContent", param);
//            requestBody.add("callbackUrl", "1234");
            HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(requestBody,headers);
            BaseResponse baseResponse = template.postForObject(url, httpEntity, BaseResponse.class);
            log.info("循环执行次数[{}]",i);
            log.info("result:[{}]", baseResponse.getMessage());
        }
    }

    private static String getImportAssetPackageData() {
        String uniqueId = UUIDUtil.randomUUID();
        ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);
        importAssetPackageContent.setThisBatchContractsTotalAmount("4000.00");
        importAssetPackageContent.setFinancialProductCode("G32000");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setUniqueId(uniqueId);
        contractDetail.setLoanContractNo(uniqueId);

        contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
        contractDetail.setLoanCustomerName("悟空");
        contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
        contractDetail.setIDCardNo("320301198502169142");
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("6214855712106520");
        contractDetail.setLoanTotalAmount("4000.00");
        contractDetail.setLoanPeriods(2);
        contractDetail.setEffectDate("2017-01-01");
        contractDetail.setExpiryDate("2017-12-20");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(2);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        repaymentPlanDetail1.setRepaymentDate("2018-09-04");
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
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

        return JsonUtils.toJsonString(importAssetPackageContent);
    }

    private void execute(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(null,null);
        BaseResponse baseResponse = restTemplate.postForObject(url, httpEntity, BaseResponse.class);
    }

}
