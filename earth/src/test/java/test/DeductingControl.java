package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.PaymentStatus;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.audit.DeductPlanStatCacheService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by FanT on 2017/4/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml" })
public class DeductingControl extends BaseApiTestPost {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private DeductPlanStatCacheService deductPlanStatCacheService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;



    String uniqueId = UUID.randomUUID().toString();
    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

    @Test
    public void test1() throws Exception {

        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("remittanceStrategy", "0");
        requestParams.put("productCode", "G31700");
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", uniqueId);
        String amount = "1500";
        requestParams.put("plannedRemittanceAmount", amount);
        requestParams.put("auditorName", "auditorName1");
        requestParams.put("auditTime", "2016-08-20 00:00:00");
        requestParams.put("remark", "交易备注");
        String bankCardNo = "6214855712106520";
        requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','plannedDate':'2016-08-20 00:00:00','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
        try {
            String sr = PostTestUtil.sendPost(REMITTANCECOMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true) {
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if (executionStatus == 2) {
                break;
            }
            Thread.sleep(10000);
        }
        for (int index = 0; index < 1; index++) {
            ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
            importAssetPackageContent.setThisBatchContractsTotalNumber(1);
            importAssetPackageContent.setThisBatchContractsTotalAmount("1500");
            importAssetPackageContent.setFinancialProductCode("G31700");

            List<ContractDetail> contracts = new ArrayList<ContractDetail>();
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setUniqueId(uniqueId);
            contractDetail.setLoanContractNo(uniqueId);

            int periods = 10;
            contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
            contractDetail.setLoanCustomerName("夏侯你惇哥");
            contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
            contractDetail.setIDCardNo("320301198502169142");
            contractDetail.setBankCode("C10105");
            contractDetail.setBankOfTheProvince("110000");
            contractDetail.setBankOfTheCity("110100");
            contractDetail.setRepaymentAccountNo("6214855712106520");
            contractDetail.setLoanTotalAmount("1500");
            contractDetail.setLoanPeriods(periods);
            contractDetail.setEffectDate("2017-01-01");
            contractDetail.setExpiryDate("2017-12-20");
            contractDetail.setLoanRates("0.156");
            contractDetail.setInterestRateCycle(1);
            contractDetail.setPenalty("0.0005");
            contractDetail.setRepaymentWay(2);

            List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();


            ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail1.setRepaymentPrincipal("150");
            repaymentPlanDetail1.setRepaymentInterest("0.00");
            repaymentPlanDetail1.setRepaymentDate("2017-04-01");
            repaymentPlanDetail1.setOtheFee("0.00");
            repaymentPlanDetail1.setTechMaintenanceFee("0.00");
            repaymentPlanDetail1.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail1);

            ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail2.setRepaymentPrincipal("150");
            repaymentPlanDetail2.setRepaymentInterest("0.00");
            repaymentPlanDetail2.setRepaymentDate("2017-04-01");
            repaymentPlanDetail2.setOtheFee("0.00");
            repaymentPlanDetail2.setTechMaintenanceFee("0.00");
            repaymentPlanDetail2.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail2);

            ImportRepaymentPlanDetail repaymentPlanDetail3 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail3.setRepaymentPrincipal("150");
            repaymentPlanDetail3.setRepaymentInterest("0.00");
            repaymentPlanDetail3.setRepaymentDate("2017-04-01");
            repaymentPlanDetail3.setOtheFee("0.00");
            repaymentPlanDetail3.setTechMaintenanceFee("0.00");
            repaymentPlanDetail3.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail3);

            ImportRepaymentPlanDetail repaymentPlanDetail4 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail4.setRepaymentPrincipal("150");
            repaymentPlanDetail4.setRepaymentInterest("0.00");
            repaymentPlanDetail4.setRepaymentDate("2017-04-01");
            repaymentPlanDetail4.setOtheFee("0.00");
            repaymentPlanDetail4.setTechMaintenanceFee("0.00");
            repaymentPlanDetail4.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail4);

            ImportRepaymentPlanDetail repaymentPlanDetail5 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail5.setRepaymentPrincipal("150");
            repaymentPlanDetail5.setRepaymentInterest("0.00");
            repaymentPlanDetail5.setRepaymentDate("2017-04-01");
            repaymentPlanDetail5.setOtheFee("0.00");
            repaymentPlanDetail5.setTechMaintenanceFee("0.00");
            repaymentPlanDetail5.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail5);

            ImportRepaymentPlanDetail repaymentPlanDetail6 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail6.setRepaymentPrincipal("150");
            repaymentPlanDetail6.setRepaymentInterest("0.00");
            repaymentPlanDetail6.setRepaymentDate("2017-04-01");
            repaymentPlanDetail6.setOtheFee("0.00");
            repaymentPlanDetail6.setTechMaintenanceFee("0.00");
            repaymentPlanDetail6.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail6);

            ImportRepaymentPlanDetail repaymentPlanDetail7 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail7.setRepaymentPrincipal("150");
            repaymentPlanDetail7.setRepaymentInterest("0.00");
            repaymentPlanDetail7.setRepaymentDate("2017-04-01");
            repaymentPlanDetail7.setOtheFee("0.00");
            repaymentPlanDetail7.setTechMaintenanceFee("0.00");
            repaymentPlanDetail7.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail7);

            ImportRepaymentPlanDetail repaymentPlanDetail8 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail8.setRepaymentPrincipal("150");
            repaymentPlanDetail8.setRepaymentInterest("0.00");
            repaymentPlanDetail8.setRepaymentDate("2017-04-01");
            repaymentPlanDetail8.setOtheFee("0.00");
            repaymentPlanDetail8.setTechMaintenanceFee("0.00");
            repaymentPlanDetail8.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail8);

            ImportRepaymentPlanDetail repaymentPlanDetail9 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail9.setRepaymentPrincipal("150");
            repaymentPlanDetail9.setRepaymentInterest("0.00");
            repaymentPlanDetail9.setRepaymentDate("2017-04-01");
            repaymentPlanDetail9.setOtheFee("0.00");
            repaymentPlanDetail9.setTechMaintenanceFee("0.00");
            repaymentPlanDetail9.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail9);

            ImportRepaymentPlanDetail repaymentPlanDetail10 = new ImportRepaymentPlanDetail();
            repaymentPlanDetail10.setRepaymentPrincipal("150");
            repaymentPlanDetail10.setRepaymentInterest("0.00");
            repaymentPlanDetail10.setRepaymentDate("2017-04-01");
            repaymentPlanDetail10.setOtheFee("0.00");
            repaymentPlanDetail10.setTechMaintenanceFee("0.00");
            repaymentPlanDetail10.setLoanServiceFee("0.00");
            repaymentPlanDetails.add(repaymentPlanDetail10);


            contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

            contracts.add(contractDetail);
            importAssetPackageContent.setContractDetails(contracts);

            String param = JsonUtils.toJsonString(importAssetPackageContent);
            System.out.println(param);
            Map<String, String> params = new HashMap<String, String>();
            params.put("fn", "200004");
            params.put("requestNo", UUID.randomUUID().toString());
            params.put("importAssetPackageContent", param);

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("merId", "t_test_zfb");
            headers.put("secret", "123456");
            String signContent = ApiSignUtils.getSignCheckContent(params);
            String sign = ApiSignUtils.rsaSign(signContent, privateKey);
            System.out.println(sign);
            headers.put("sign", sign);

            try {
                String sr = PostTestUtil.sendPost(MODIFY_URL_TEST, params, headers);
                System.out.println("===========" + index + sr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int j = 0; j < 10; j++) {
            String repaymentNumber = "";
            Map<String, String> requestParams1 = new HashMap<String, String>();
            requestParams1.put("fn", "100001");
            requestParams1.put("contractNo", "");
            requestParams1.put("requestNo", UUID.randomUUID().toString());
            requestParams1.put("uniqueId", uniqueId);
            try {
                String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
                Result result = JsonUtils.parse(sr, Result.class);
                JSONArray repaymentPlanDetails = (JSONArray) result.get("repaymentPlanDetails");
                JSONObject jo = repaymentPlanDetails.getJSONObject(j);
                repaymentNumber = (String) jo.get("repaymentNumber");
                System.out.println(sr + 1);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println(repaymentNumber);

            Map<String, String> requestParams2 = new HashMap<String, String>();

            requestParams2.put("fn", "300001");
            requestParams2.put("requestNo", UUID.randomUUID().toString());
            requestParams2.put("deductId", UUID.randomUUID().toString());
            requestParams2.put("financialProductCode", "G31700");
            requestParams2.put("uniqueId", uniqueId);
            requestParams2.put("apiCalledTime", "2017-04-01");
            requestParams2.put("amount", "150.00");
            requestParams2.put("repaymentType", "1");
            requestParams2.put("mobile", "13777847783");
            requestParams2.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':150,'repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':150,'techFee':0.00,'overDueFeeDetail':{"
//				+ "'penaltyFee':50.00,'latePenalty':50.00,'lateFee':50.00,'lateOtherCost':50.00,'totalOverdueFee':200.00}}]");
                    + "'totalOverdueFee':0.00}}]");
            try {
                String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams2, getIdentityInfoMap(requestParams2));
                System.out.println(sr);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            while(true){
                AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentNumber);
                PaymentStatus paymentStatus = asset.getPaymentStatus();
                if(PaymentStatus.SUCCESS == paymentStatus){
                    break;
                }
                Thread.sleep(10000);
            }

//            DeductPlanStatCache deductPlanStatCache = deductPlanStatCacheService.searchSucNoAndSucAmountByFinancialContractUuid("d2812bc5-5057-4a91-b3fd-9019506f0499", DateUtils.parseDate("2017-04-01 17:00:00","yyyy-MM-dd HH:mm:ss"),DateUtils.parseDate("2017-04-01 18:00:00","yyyy-MM-dd HH:mm:ss"));
//            long sucNo = deductPlanStatCache.getSucNum();
//            BigDecimal sucAmount = deductPlanStatCache.getSucAmount();
//            System.out.println(sucNo);
//            System.out.println(sucAmount);

            Thread.sleep(10000);



        }
    }

}
