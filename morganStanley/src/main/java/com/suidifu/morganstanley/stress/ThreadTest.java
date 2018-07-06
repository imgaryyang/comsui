package com.suidifu.morganstanley.stress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.suidifu.morganstanley.utils.PostTestUtils;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;

@Log4j2
public class ThreadTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

    public static void main(String[] args) throws Exception {
        stressTest(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]));

        log.info(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    private static void stressTest(String url, int threadNum, int execCount) throws InterruptedException {
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
                } catch (JsonProcessingException e) {
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

    private static void execute(String url, int execCount) throws JsonProcessingException {
        for (int i = 0; i < execCount; i++) {
            Map<String, String> mapParam = new HashMap<>();
            mapParam.put("requestNo", UUID.randomUUID().toString());
            mapParam.put("importAssetPackageContent", OBJECT_MAPPER.writeValueAsString(buildImportAssetPackageContent()));

            Map<String, String> headers = new HashMap<>();
            headers.put("merId", "t_test_zfb");
            headers.put("secret", "123456");
            String signContent = ApiSignUtils.getSignCheckContent(mapParam);
            String sign = ApiSignUtils.rsaSign(signContent, PRIVATE_KEY);
            headers.put("sign", sign);

            log.info("result[{}]: {}", i, PostTestUtils.sendPost(url, mapParam, headers));
        }
    }

    private static ImportAssetPackageContent buildImportAssetPackageContent() {
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
        return importAssetPackageContent;
    }
}