package com.suidifu.morganstanley.stress;

import com.suidifu.morganstanley.handler.repayment.ImportAssetPackageHandler;
import com.suidifu.morganstanley.model.request.repayment.ContractDetail;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackage;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackageContent;
import com.suidifu.morganstanley.model.request.repayment.ImportRepaymentPlanDetail;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_ASYNC_IMPORT_ASSET_PACKAGE;

/**
 * 资产包压力测试工具
 */
@Log4j2
@Component("asyImportAssetPackageTestTask")
public class AsyImportAssetPackageTest {
    @Autowired
    private ImportAssetPackageHandler importAssetPackageHandler;
//    @Autowired
//    private RestTemplate template;
    private RestTemplate template = new RestTemplate();
    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";
    private static final String REQUEST_URL = "http://127.0.0.1:7778" +  URL_API_V3 + URL_ASYNC_IMPORT_ASSET_PACKAGE;

    private static LongAdder longAdder1 = new LongAdder();
    private static LongAdder longAdder2 = new LongAdder();
    private static LongAdder longAdder3 = new LongAdder();
    private static LongAdder taskNum = new LongAdder();

    public void assetPacket() throws Exception {
        taskNum.increment();
        int taskNumber= taskNum.intValue();
        log.info("-----第"+taskNumber+"task------");

        int startNum1=0;
        int startNum2=0;
        int startNum3=0;
        long start2= System.currentTimeMillis();
        ImportAssetPackageContent importAssetPackageContent;
        for(int i=0;i<20;i++) {
            log.info("第"+i+"次----");
            long start= System.currentTimeMillis();
            //随机期数
            int ran = new Random().nextInt(4) + 1;
            int number;
            if (i<14) {
                //剩余70%
                longAdder3.increment();
                number= longAdder3.intValue();
                importAssetPackageContent = buildImportAssetPackageContentV2(ran,3,number+startNum3);
            } else if (i >= 14 && i < 17) {
                //过期15%，日切
                longAdder1.increment();
                number= longAdder1.intValue();
                importAssetPackageContent = buildImportAssetPackageContentV2(ran,1,number+startNum1);
            } else {
                //当日15%，日切，核销
                longAdder2.increment();
                number= longAdder2.intValue();
                importAssetPackageContent = buildImportAssetPackageContentV2(ran,2,number+startNum2);
            }
            BaseResponse baseResponse = executeStressTest(importAssetPackageContent,REQUEST_URL);
            log.info("返回结果信息:{}", baseResponse.getMessage());
            log.info("import used " + (System.currentTimeMillis()-start)+ "ms");
            log.info("第"+taskNumber+"task "+"的第"+i+"number:"+number);
        }

        log.info("-----第"+taskNumber+"task------import used " + (System.currentTimeMillis()-start2)+ "ms");

    }

    //导入资产包
    private void stressTestV2(ImportAssetPackageContent importAssetPackageContent) throws Exception {
        ImportAssetPackage model = new ImportAssetPackage();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setImportAssetPackageContent(JsonUtils.toJSONString(importAssetPackageContent));
        importAssetPackageHandler.importAssetPackage(model,false);

    }

    /**
     * 导入资产包异步压力测试
     * @return BaseResponse
     */
    private BaseResponse executeStressTest(ImportAssetPackageContent importAssetPackageContent, String url){
        Map<String, String> params = new HashMap<String, String>();
        ImportAssetPackage model = new ImportAssetPackage();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setImportAssetPackageContent(JsonUtils.toJSONString(importAssetPackageContent));

        params.put("requestNo", model.getRequestNo());
        params.put("importAssetPackageContent", model.getImportAssetPackageContent());
        String signContent = ApiSignUtils.getSignCheckContent(params);
        String sign = ApiSignUtils.rsaSign(signContent, privateKey);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("merId", "t_test_zfb");
        headers.add("secret", "123456");
        headers.add("sign", sign);
        requestBody.add("requestNo", model.getRequestNo());
        requestBody.add("importAssetPackageContent", model.getImportAssetPackageContent());
        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(requestBody,headers);
        return template.postForObject(url, httpEntity, BaseResponse.class);
    }

    private static ImportAssetPackageContent buildImportAssetPackageContentV2(int periods, int kind, int number) {

        ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
        importAssetPackageContent.setThisBatchContractsTotalNumber(1);

        String totalAmount = new DecimalFormat("#.00").format(2000*periods);
        importAssetPackageContent.setThisBatchContractsTotalAmount(totalAmount);

        importAssetPackageContent.setFinancialProductCode("WUBO123");

        List<ContractDetail> contracts = new ArrayList<>();
        ContractDetail contractDetail = new ContractDetail();
        if (kind==1) {
            contractDetail.setUniqueId("uniqueIde111"+number);
            contractDetail.setLoanContractNo("contractNoe111"+number);
        } else if (kind == 2) {
            contractDetail.setUniqueId("uniqueIde222" + number);
            contractDetail.setLoanContractNo("contractNoe222" + number);
        } else {
            contractDetail.setUniqueId("uniqueIde333"+number);
            contractDetail.setLoanContractNo("contractNoe333"+number);
        }


        contractDetail.setLoanCustomerNo("customerNo");
        contractDetail.setLoanCustomerName("赵锋杰");
        contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
        contractDetail.setIDCardNo(UUID.randomUUID().toString());
        contractDetail.setBankCode("C10102");
        contractDetail.setBankOfTheProvince("330000");
        contractDetail.setBankOfTheCity("330300");
        contractDetail.setRepaymentAccountNo("23456787654323456");
        contractDetail.setLoanTotalAmount(totalAmount);
        contractDetail.setLoanPeriods(periods);
        contractDetail.setEffectDate("2016-8-1");
        contractDetail.setExpiryDate("2099-01-01");
        contractDetail.setLoanRates("0.156");
        contractDetail.setInterestRateCycle(1);
        contractDetail.setPenalty("0.0005");
        contractDetail.setRepaymentWay(1);

        List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<>();

        ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
        ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
        ImportRepaymentPlanDetail repaymentPlanDetail3 = new ImportRepaymentPlanDetail();
        ImportRepaymentPlanDetail repaymentPlanDetail4 = new ImportRepaymentPlanDetail();


        repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
        repaymentPlanDetail1.setRepaymentInterest("20.00");
        if (kind==1) {
            repaymentPlanDetail1.setRepaymentDate("2017-10-01");
        } else if (kind == 2) {
            String format = DateUtils.format(new Date(), "yyyy-MM-dd");
            repaymentPlanDetail1.setRepaymentDate(format);
        } else {
            repaymentPlanDetail1.setRepaymentDate("2017-11-15");
        }
        repaymentPlanDetail1.setOtheFee("0.00");
        repaymentPlanDetail1.setLoanServiceFee("0.00");
        repaymentPlanDetail1.setTechMaintenanceFee("0.00");
        if (kind==1) {
            repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNoe111"+number + "1");
        } else if (kind == 2) {
            repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNoe222"+number + "1");
        } else {
            repaymentPlanDetail1.setRepayScheduleNo("outerRepaymentPlanNoe333"+number + "1");
        }

        repaymentPlanDetails.add(repaymentPlanDetail1);



        if (periods>1) {
            repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
            repaymentPlanDetail2.setRepaymentInterest("20.00");
            repaymentPlanDetail2.setRepaymentDate("2017-11-16");
            repaymentPlanDetail2.setOtheFee("0.00");
            repaymentPlanDetail2.setLoanServiceFee("0.00");
            repaymentPlanDetail2.setTechMaintenanceFee("0.00");
            if (kind==1) {
                repaymentPlanDetail2.setRepayScheduleNo("outerRepaymentPlanNoe111"+number + "2");
            } else if (kind == 2) {
                repaymentPlanDetail2.setRepayScheduleNo("outerRepaymentPlanNoe222"+number + "2");
            } else {
                repaymentPlanDetail2.setRepayScheduleNo("outerRepaymentPlanNoe333"+number + "2");
            }
            repaymentPlanDetails.add(repaymentPlanDetail2);
        }

        if (periods>2) {
            repaymentPlanDetail3.setRepaymentPrincipal("2000.00");
            repaymentPlanDetail3.setRepaymentInterest("20.00");
            repaymentPlanDetail3.setRepaymentDate("2017-11-17");
            repaymentPlanDetail3.setOtheFee("0.00");
            repaymentPlanDetail3.setLoanServiceFee("0.00");
            repaymentPlanDetail3.setTechMaintenanceFee("0.00");
            if (kind==1) {
                repaymentPlanDetail3.setRepayScheduleNo("outerRepaymentPlanNoe111"+number + "3");
            } else if (kind == 2) {
                repaymentPlanDetail3.setRepayScheduleNo("outerRepaymentPlanNoe222"+number + "3");
            } else {
                repaymentPlanDetail3.setRepayScheduleNo("outerRepaymentPlanNoe333"+number + "3");
            }
            repaymentPlanDetails.add(repaymentPlanDetail3);
        }

        if (periods>3) {
            repaymentPlanDetail4.setRepaymentPrincipal("2000.00");
            repaymentPlanDetail4.setRepaymentInterest("20.00");
            repaymentPlanDetail4.setRepaymentDate("2017-11-18");
            repaymentPlanDetail4.setOtheFee("0.00");
            repaymentPlanDetail4.setLoanServiceFee("0.00");
            repaymentPlanDetail4.setTechMaintenanceFee("0.00");
            if (kind==1) {
                repaymentPlanDetail4.setRepayScheduleNo("outerRepaymentPlanNoe111"+number + "4");
            } else if (kind == 2) {
                repaymentPlanDetail4.setRepayScheduleNo("outerRepaymentPlanNoe222"+number + "4");
            } else {
                repaymentPlanDetail4.setRepayScheduleNo("outerRepaymentPlanNoe333"+number + "4");
            }
            repaymentPlanDetails.add(repaymentPlanDetail4);
        }

        contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

        contracts.add(contractDetail);
        importAssetPackageContent.setContractDetails(contracts);
        return importAssetPackageContent;
    }
}
