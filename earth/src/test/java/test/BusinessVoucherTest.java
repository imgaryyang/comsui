package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by FanT on 2017/5/12.
 */

public class BusinessVoucherTest extends BaseApiTestPost {
    @Autowired
    private GenericDaoSupport genericDaoSupport;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    class Remittance implements Runnable {
        private int num;

        public Remittance(int num) {
            super();
            this.num = num;
        }
        public void run() {
            long id = num * 1000;
            System.out.println("id " + id);
            for (int i = 0; i < 1000; i++) {
                String a = id + i + "";
                String uniqueId = "ZHANGLONGFEI" + a;
                Map<String, String> requestParams = new HashMap<String, String>();
                requestParams.put("fn", "300002");
                requestParams.put("requestNo", UUID.randomUUID().toString());
                requestParams.put("remittanceStrategy", "0");
                requestParams.put("productCode", "G32000");
                requestParams.put("uniqueId", uniqueId);
                requestParams.put("contractNo", uniqueId);
                String amount = "6";
                requestParams.put("plannedRemittanceAmount", amount);
                requestParams.put("auditorName", "auditorName1");
                requestParams.put("auditTime", "2016-08-20 00:00:00");
                requestParams.put("remark", "交易备注");
                String bankCardNo = "6214855712106520";
                requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'" + amount + "','plannedDate':'','bankCode':'C10102','bankCardNo':'" + bankCardNo + "','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
                try {
                    String sr = PostTestUtil.sendPost(REMITTANCECOMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
                    System.out.println(sr);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("已放款" + (i + 1) + "笔");
            }
        }
    }

    @Test
    public void testRemittance(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<10; i++){
            Thread myThread = new Thread(new Remittance(i));
            threads.add(myThread);
        }
        threads.parallelStream().forEach(t -> {

            try {
                t.start();
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });

    }

    class ImportPackage implements Runnable {
        private int num;
        public ImportPackage(int num){
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 1000;
            System.out.println("id " + id);
            for (int index = 0; index < 1000; index++) {
                String a = id + index + "";
                String uniqueId = "ZHANGLONGFEI" + a;
                ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
                importAssetPackageContent.setThisBatchContractsTotalNumber(1);
                importAssetPackageContent.setThisBatchContractsTotalAmount("6");
                importAssetPackageContent.setFinancialProductCode("G32000");

                List<ContractDetail> contracts = new ArrayList<ContractDetail>();
                ContractDetail contractDetail = new ContractDetail();
                contractDetail.setUniqueId(uniqueId);
                contractDetail.setLoanContractNo(uniqueId);

                contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
                contractDetail.setLoanCustomerName("悟空");
                contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
                contractDetail.setIDCardNo("320301198502169142");
                contractDetail.setBankCode("C10105");
                contractDetail.setBankOfTheProvince("110000");
                contractDetail.setBankOfTheCity("110100");
                contractDetail.setRepaymentAccountNo("6214855712106520");
                contractDetail.setLoanTotalAmount("6.00");
                contractDetail.setLoanPeriods(3);
                contractDetail.setEffectDate("2017-01-01");
                contractDetail.setExpiryDate("2017-12-20");
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
                System.out.println("已导入" + (index + 1) + "笔资产包");
            }
        }
    }
    @Test
    public void testImportPackage(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<10; i++){
            Thread myThread = new Thread(new ImportPackage(i));
            threads.add(myThread);
        }
        threads.parallelStream().forEach(t -> {
            try {
                t.start();
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



//        class Task implements Runnable {
//            private int num;
//
//            public Task(int num) {
//                super();
//                this.num = num;
//            }
//
//            @Override
//
//            public void run() {
//                long id = num * 1000;
//                System.out.println("id " + id);
//                List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
//                for (int i = 0; i < 10000; i++) {
//                    String uniqueId = "YUZHEYU" + (0 + i);
//                    String repaymentNumber = "";
//                    Map<String, String> requestParams1 = new HashMap<String, String>();
//                    requestParams1.put("fn", "100001");
//                    requestParams1.put("contractNo", "");
//                    requestParams1.put("requestNo", UUID.randomUUID().toString());
//                    requestParams1.put("uniqueId", uniqueId);
//                    try {
//                        String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
//                        Result result = JsonUtils.parse(sr, Result.class);
//                        JSONArray repaymentPlanDetails = (JSONArray) result.get("repaymentPlanDetails");
//                        JSONObject jo = repaymentPlanDetails.getJSONObject(0);
//                        repaymentNumber = (String) jo.get("repaymentNumber");
//                        System.out.println("\n" + repaymentNumber + "\t" + (i + 1));
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
//                    detail.setAmount(new BigDecimal("10"));
//                    detail.setUniqueId(uniqueId);
//                    detail.setRepaymentPlanNo(repaymentNumber);
//                    detail.setPrincipal(new BigDecimal("6"));
//                    detail.setInterest(new BigDecimal("1"));
//                    detail.setServiceCharge(new BigDecimal("1"));
//                    detail.setMaintenanceCharge(new BigDecimal("1"));
//                    detail.setOtherCharge(new BigDecimal("1"));
//                    detail.setPayer(1);
//                    details.add(detail);
//                }
//                String amount = "100000";
//                String jsonString = JsonUtils.toJsonString(details);
//                Map<String, String> requestParams = new HashMap<String, String>();
//                requestParams.put("fn", "300003");
//                requestParams.put("requestNo", UUID.randomUUID().toString());
//                requestParams.put("transactionType", "0");
//                requestParams.put("voucherType", "0");
//                requestParams.put("voucherAmount", amount);
//                requestParams.put("financialContractNo", "G31700");
//                requestParams.put("receivableAccountNo", "600000000001");
//                requestParams.put("paymentAccountNo", "1001133419006708197");
//                requestParams.put("bankTransactionNo", UUID.randomUUID().toString());
//                requestParams.put("paymentName", "上海拍拍贷金融信息服务有限公司");
//                requestParams.put("paymentBank", "bank");
//
//                requestParams.put("detail", jsonString);
//
//                ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
//                CloseableHttpClient client = HttpClientBuilder.create().build();
////		HttpPost httppost = new HttpPost("http://192.168.1.33:8080/earth-yunxin-0.1.0/api/command");
////		HttpPost httppost = new HttpPost("http://192.168.1.145:9090/api/command");
////		HttpPost httppost = new HttpPost("http://localhost:9090/api/command");
////        HttpPost httppost = new HttpPost("http://192.168.0.204/api/command");
//                HttpPost httppost = new HttpPost("http://yunxin.5veda.net/api/command");
//                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
//                reqEntity.setCharset(Charset.forName("UTF-8"));
//                for (Map.Entry<String, String> e : requestParams.entrySet()) {
//                    StringBody stringBody = new StringBody(e.getValue(), contentType);
//                    reqEntity.addPart(e.getKey(), stringBody);
//                }
//                httppost.setEntity(reqEntity.build());
//                String signContent = ApiSignUtils.getSignCheckContent(requestParams);
//                String sign = ApiSignUtils.rsaSign(signContent, privateKey);
//                httppost.addHeader("merId", TEST_MERID);
//                httppost.addHeader("secret", TEST_SECRET);
//                httppost.addHeader("sign", sign);
//                try {
//                    HttpResponse response = client.execute(httppost);
//                    HttpEntity entity = response.getEntity();
//                    System.out.println(EntityUtils.toString(entity));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
////}
//
////    @Test
////    public void testBusinessVoucher(){
////        List<Thread> threads = new ArrayList<>();
////        for(int i=0; i<10; i++){
////            Thread myThread = new Thread(new Task(i));
////            threads.add(myThread);
////        }
////        threads.parallelStream().forEach(t -> {
////            try {
////                t.start();
////                t.join();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        });
////    }
//        }
//
//            class Deduct implements Runnable {
//                private int num;
//
//                public Deduct(int num) {
//                    super();
//                    this.num = num;
//                }
//                @Override

                @Test
                public void run() {
//                    long id = num * 1000;
//                    System.out.println("id " + id);
                    for (int index = 0; index < 10000; index++) {
                        String uniqueId = "ZHENGHANGBO" + (0 + index);
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
                            JSONObject jo = repaymentPlanDetails.getJSONObject(0);
                            repaymentNumber = (String) jo.get("repaymentNumber");
                            System.out.println("\n" + repaymentNumber + "\t" + (index + 1));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Map<String, String> requestParams = new HashMap<String, String>();

                        requestParams.put("fn", "300001");
                        requestParams.put("requestNo", UUID.randomUUID().toString());
                        requestParams.put("deductId",  UUID.randomUUID().toString());
                        requestParams.put("financialProductCode", "G31700");
                        requestParams.put("uniqueId", uniqueId);
                        requestParams.put("apiCalledTime", DateUtils.format(new Date()));
                        requestParams.put("amount",  "10.00");
                        requestParams.put("repaymentType", "1");
                        requestParams.put("mobile", "13777847783");
                        requestParams.put("repaymentDetails", "[{'loanFee':1.00,'otherFee':1.00,'repaymentAmount':10,'repaymentInterest':1.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':6.00,'techFee':1.00,'overDueFeeDetail':{"
                                //	+ "'penaltyFee':1.00,'latePenalty':1.00,'lateFee':1.00,'lateOtherCost':1.00,'totalOverdueFee':4.00}}]");
                                + "'totalOverdueFee':0.00}}]");
                        try {
                            String sr = PostTestUtil.sendPost(COMMAND_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
                            System.out.println(sr);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

//            }

//    @Test
//    public void testDeduct(){
//        List<Thread> threads = new ArrayList<>();
//        for(int i=0; i<10; i++){
//            Thread myThread = new Thread(new Deduct(i));
//            threads.add(myThread);
//        }
//        threads.parallelStream().forEach(t -> {
//            try {
//                t.start();
//                t.join();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

}