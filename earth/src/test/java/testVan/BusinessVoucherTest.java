package testVan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.cucumber.method.model.BusinessAmountModel;
import com.zufangbao.cucumber.method.model.RepaymentDetailsModel;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ActivePaymentVoucherDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by FanT on 2017/5/12.
 */

public class BusinessVoucherTest extends BaseApiTestPost {
    @Autowired
    private GenericDaoSupport genericDaoSupport;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    //放款
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
                requestParams.put("productCode", "G31700");
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


    //导入资产包
    class ImportPackage implements Runnable {
        private int num;
        public ImportPackage(int num){
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 100;
            System.out.println("id " + id);
            for (int index = 0; index < 100; index++) {
                String a = id + index + "";
                String uniqueId = "firstTest" + a;
                ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
                importAssetPackageContent.setThisBatchContractsTotalNumber(1);
                importAssetPackageContent.setThisBatchContractsTotalAmount("6");
                importAssetPackageContent.setFinancialProductCode("CS0001");

                List<ContractDetail> contracts = new ArrayList<ContractDetail>();
                ContractDetail contractDetail = new ContractDetail();
                contractDetail.setUniqueId(uniqueId);
                contractDetail.setLoanContractNo(uniqueId);

                contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
                contractDetail.setLoanCustomerName("高渐");
                contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
                contractDetail.setIDCardNo("320301198502169143");
                contractDetail.setBankCode("C10105");
                contractDetail.setBankOfTheProvince("110000");
                contractDetail.setBankOfTheCity("110100");
                contractDetail.setRepaymentAccountNo("6214855712105070");
                contractDetail.setLoanTotalAmount("6.00");
                contractDetail.setLoanPeriods(1);
                contractDetail.setEffectDate("2017-01-01");
                contractDetail.setExpiryDate("2017-12-20");
                contractDetail.setLoanRates("0.156");
                contractDetail.setInterestRateCycle(1);
                contractDetail.setPenalty("0.0005");
                contractDetail.setRepaymentWay(2);

                List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

                ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
                repaymentPlanDetail1.setRepaymentPrincipal("6.00");
                repaymentPlanDetail1.setRepaymentInterest("0.00");
                repaymentPlanDetail1.setRepaymentDate("2018-01-17");
                repaymentPlanDetail1.setOtheFee("0.00");
                repaymentPlanDetail1.setTechMaintenanceFee("0.00");
                repaymentPlanDetail1.setLoanServiceFee("0.00");
                repaymentPlanDetails.add(repaymentPlanDetail1);



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
                    String sr = PostTestUtil.sendPost(IMPORT_PACKAGE, params, headers);
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
        for(int i=0; i<200; i++){
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




    class ImportPackage1 implements Runnable {
        private int num;
        public ImportPackage1(int num){
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 100;
            System.out.println("id " + id);
            for (int index = 0; index < 100; index++) {
                String a = id + index + "";
                String uniqueId = "secondTest" + a;
                ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
                importAssetPackageContent.setThisBatchContractsTotalNumber(1);
                importAssetPackageContent.setThisBatchContractsTotalAmount("6");
                importAssetPackageContent.setFinancialProductCode("CS0001");

                List<ContractDetail> contracts = new ArrayList<ContractDetail>();
                ContractDetail contractDetail = new ContractDetail();
                contractDetail.setUniqueId(uniqueId);
                contractDetail.setLoanContractNo(uniqueId);

                contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
                contractDetail.setLoanCustomerName("高渐");
                contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
                contractDetail.setIDCardNo("320301198502169143");
                contractDetail.setBankCode("C10105");
                contractDetail.setBankOfTheProvince("110000");
                contractDetail.setBankOfTheCity("110100");
                contractDetail.setRepaymentAccountNo("6214855712105070");
                contractDetail.setLoanTotalAmount("6.00");
                contractDetail.setLoanPeriods(1);
                contractDetail.setEffectDate("2017-01-01");
                contractDetail.setExpiryDate("2017-12-20");
                contractDetail.setLoanRates("0.156");
                contractDetail.setInterestRateCycle(1);
                contractDetail.setPenalty("0.0005");
                contractDetail.setRepaymentWay(2);

                List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

                ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
                repaymentPlanDetail1.setRepaymentPrincipal("6.00");
                repaymentPlanDetail1.setRepaymentInterest("0.00");
                repaymentPlanDetail1.setRepaymentDate("2018-01-17");
                repaymentPlanDetail1.setOtheFee("0.00");
                repaymentPlanDetail1.setTechMaintenanceFee("0.00");
                repaymentPlanDetail1.setLoanServiceFee("0.00");
                repaymentPlanDetails.add(repaymentPlanDetail1);


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
                    String sr = PostTestUtil.sendPost(IMPORT_PACKAGE, params, headers);
                    System.out.println("===========" + index + sr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("已导入" + (index + 1) + "笔资产包");
            }
        }
    }
    @Test
    public void testImportPackage1(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<200; i++){
            Thread myThread = new Thread(new ImportPackage1(i));
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

    class ImportPackage2 implements Runnable {
        private int num;
        public ImportPackage2(int num){
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 100;
            System.out.println("id " + id);
            for (int index = 0; index < 100; index++) {
                String a = id + index + "";
                String uniqueId = "thirdTest" + a;
                ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
                importAssetPackageContent.setThisBatchContractsTotalNumber(1);
                importAssetPackageContent.setThisBatchContractsTotalAmount("6");
                importAssetPackageContent.setFinancialProductCode("CS0001");

                List<ContractDetail> contracts = new ArrayList<ContractDetail>();
                ContractDetail contractDetail = new ContractDetail();
                contractDetail.setUniqueId(uniqueId);
                contractDetail.setLoanContractNo(uniqueId);

                contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
                contractDetail.setLoanCustomerName("高渐");
                contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
                contractDetail.setIDCardNo("320301198502169143");
                contractDetail.setBankCode("C10105");
                contractDetail.setBankOfTheProvince("110000");
                contractDetail.setBankOfTheCity("110100");
                contractDetail.setRepaymentAccountNo("6214855712105070");
                contractDetail.setLoanTotalAmount("6.00");
                contractDetail.setLoanPeriods(1);
                contractDetail.setEffectDate("2017-01-01");
                contractDetail.setExpiryDate("2017-12-20");
                contractDetail.setLoanRates("0.156");
                contractDetail.setInterestRateCycle(1);
                contractDetail.setPenalty("0.0005");
                contractDetail.setRepaymentWay(2);

                List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

                ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
                repaymentPlanDetail1.setRepaymentPrincipal("6.00");
                repaymentPlanDetail1.setRepaymentInterest("0.00");
                repaymentPlanDetail1.setRepaymentDate("2018-01-17");
                repaymentPlanDetail1.setOtheFee("0.00");
                repaymentPlanDetail1.setTechMaintenanceFee("0.00");
                repaymentPlanDetail1.setLoanServiceFee("0.00");
                repaymentPlanDetails.add(repaymentPlanDetail1);

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
                    String sr = PostTestUtil.sendPost(IMPORT_PACKAGE, params, headers);
                    System.out.println("===========" + index + sr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("已导入" + (index + 1) + "笔资产包");
            }
        }
    }
    @Test
    public void testImportPackage2(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<200; i++){
            Thread myThread = new Thread(new ImportPackage2(i));
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


    class ImportPackage3 implements Runnable {
        private int num;
        public ImportPackage3(int num){
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 100;
            System.out.println("id " + id);
            for (int index = 0; index < 100; index++) {
                String a = id + index + "";
                String uniqueId = "fourthTest" + a;
                ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
                importAssetPackageContent.setThisBatchContractsTotalNumber(1);
                importAssetPackageContent.setThisBatchContractsTotalAmount("6");
                importAssetPackageContent.setFinancialProductCode("CS0001");

                List<ContractDetail> contracts = new ArrayList<ContractDetail>();
                ContractDetail contractDetail = new ContractDetail();
                contractDetail.setUniqueId(uniqueId);
                contractDetail.setLoanContractNo(uniqueId);

                contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
                contractDetail.setLoanCustomerName("高渐");
                contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
                contractDetail.setIDCardNo("320301198502169143");
                contractDetail.setBankCode("C10105");
                contractDetail.setBankOfTheProvince("110000");
                contractDetail.setBankOfTheCity("110100");
                contractDetail.setRepaymentAccountNo("6214855712105070");
                contractDetail.setLoanTotalAmount("6.00");
                contractDetail.setLoanPeriods(1);
                contractDetail.setEffectDate("2017-01-01");
                contractDetail.setExpiryDate("2017-12-20");
                contractDetail.setLoanRates("0.156");
                contractDetail.setInterestRateCycle(1);
                contractDetail.setPenalty("0.0005");
                contractDetail.setRepaymentWay(2);

                List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

                ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
                repaymentPlanDetail1.setRepaymentPrincipal("6.00");
                repaymentPlanDetail1.setRepaymentInterest("0.00");
                repaymentPlanDetail1.setRepaymentDate("2018-01-17");
                repaymentPlanDetail1.setOtheFee("0.00");
                repaymentPlanDetail1.setTechMaintenanceFee("0.00");
                repaymentPlanDetail1.setLoanServiceFee("0.00");
                repaymentPlanDetails.add(repaymentPlanDetail1);

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
                    String sr = PostTestUtil.sendPost(IMPORT_PACKAGE, params, headers);
                    System.out.println("===========" + index + sr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("已导入" + (index + 1) + "笔资产包");
            }
        }
    }
    @Test
    public void testImportPackage3(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<200; i++){
            Thread myThread = new Thread(new ImportPackage3(i));
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



    //商户付款凭证
        class Task implements Runnable {
//            private int num;
//
//                public Task(int num) {
//                    super();
//                    this.num = num;
//                }

            @Override

            public void run() {
//                long id = num * 10000;
//                System.out.println("id " + id);

                    List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
                    for (int i = 0; i < 20000; i++) {
                        String uniqueId = "eighthTest" + i;
                        String repaymentNumber = "";
                        Map<String, String> requestParams1 = new HashMap<String, String>();
                        requestParams1.put("fn", "100001");
                        requestParams1.put("contractNo", "");
                        requestParams1.put("requestNo", UUID.randomUUID().toString());
                        requestParams1.put("uniqueId", uniqueId);
                        try {
                            String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
                            Result result = JsonUtils.parse(sr, Result.class);
                            Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
                            JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
                            JSONObject jo = jsonArray.getJSONObject(0);
                            repaymentNumber = (String) jo.get("repaymentNumber");
                            System.out.println("\n" + repaymentNumber + "\t" + (i + 1));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
                        detail.setAmount(new BigDecimal("6"));
                        detail.setUniqueId(uniqueId);
                        detail.setRepaymentPlanNo(repaymentNumber);
                        detail.setPrincipal(new BigDecimal("6"));
                        detail.setInterest(new BigDecimal("0"));
                        detail.setServiceCharge(new BigDecimal("0"));
                        detail.setMaintenanceCharge(new BigDecimal("0"));
                        detail.setOtherCharge(new BigDecimal("0"));
                        detail.setPayer(1);
                        details.add(detail);

                    }
                    String jsonString = JsonUtils.toJsonString(details);
                    String amount = "120000";
                    Map<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("fn", "300003");
                    requestParams.put("requestNo", UUID.randomUUID().toString());
                    requestParams.put("transactionType", "0");
                    requestParams.put("voucherType", "0");
                    requestParams.put("voucherAmount", amount);
                    requestParams.put("financialContractNo", "CS0001");
                    requestParams.put("receivableAccountNo", "1220127571120");
                    requestParams.put("paymentAccountNo", "6217857600016839339");
                    requestParams.put("bankTransactionNo", UUID.randomUUID().toString());
                    requestParams.put("paymentName", "秦萎超");
                    requestParams.put("paymentBank", "bank");

                    requestParams.put("detail", jsonString);

                    ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
                    CloseableHttpClient client = HttpClientBuilder.create().build();
//		HttpPost httppost = new HttpPost("http://192.168.1.33:8080/earth-yunxin-0.1.0/api/command");
//		HttpPost httppost = new HttpPost("http://192.168.1.145:9090/api/command");
//		HttpPost httppost = new HttpPost("http://localhost:9090/api/command");
//        HttpPost httppost = new HttpPost("http://192.168.0.204/api/command");
                    HttpPost httppost = new HttpPost(VOUCHER);
                    MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                    reqEntity.setCharset(Charset.forName("UTF-8"));
                    for (Map.Entry<String, String> e : requestParams.entrySet()) {
                        StringBody stringBody = new StringBody(e.getValue(), contentType);
                        reqEntity.addPart(e.getKey(), stringBody);
                    }
                    httppost.setEntity(reqEntity.build());
                    String signContent = ApiSignUtils.getSignCheckContent(requestParams);
                    String sign = ApiSignUtils.rsaSign(signContent, privateKey);
                    httppost.addHeader("merId", TEST_MERID);
                    httppost.addHeader("secret", TEST_SECRET);
                    httppost.addHeader("sign", sign);
                    try {
                        HttpResponse response = client.execute(httppost);
                        HttpEntity entity = response.getEntity();
                        System.out.println(EntityUtils.toString(entity));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


    @Test
    public void testBusinessVoucher(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<1; i++){
            Thread myThread = new Thread(new Task());
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


    class Task1 implements Runnable {
//

        @Override

        public void run() {

            List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
            for (int i = 0; i < 20000; i++) {
                String uniqueId = "secondTest" + i;
                String repaymentNumber = "";
                Map<String, String> requestParams1 = new HashMap<String, String>();
                requestParams1.put("fn", "100001");
                requestParams1.put("contractNo", "");
                requestParams1.put("requestNo", UUID.randomUUID().toString());
                requestParams1.put("uniqueId", uniqueId);
                try {
                    String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
                    Result result = JsonUtils.parse(sr, Result.class);
                    Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
                    JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
                    JSONObject jo = jsonArray.getJSONObject(0);
                    repaymentNumber = (String) jo.get("repaymentNumber");
                    System.out.println("\n" + repaymentNumber + "\t" + (i + 1));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
                detail.setAmount(new BigDecimal("6"));
                detail.setUniqueId(uniqueId);
                detail.setRepaymentPlanNo(repaymentNumber);
                detail.setPrincipal(new BigDecimal("6"));
                detail.setInterest(new BigDecimal("0"));
                detail.setServiceCharge(new BigDecimal("0"));
                detail.setMaintenanceCharge(new BigDecimal("0"));
                detail.setOtherCharge(new BigDecimal("0"));
                detail.setPayer(1);
                details.add(detail);

            }
            String jsonString = JsonUtils.toJsonString(details);
            String amount = "60000";
            Map<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("fn", "300003");
            requestParams.put("requestNo", UUID.randomUUID().toString());
            requestParams.put("transactionType", "0");
            requestParams.put("voucherType", "0");
            requestParams.put("voucherAmount", amount);
            requestParams.put("financialContractNo", "CS0001");
            requestParams.put("receivableAccountNo", "1220127571120");
            requestParams.put("paymentAccountNo", "6217857600016839346");
            requestParams.put("bankTransactionNo", UUID.randomUUID().toString());
            requestParams.put("paymentName", "萎超");
            requestParams.put("paymentBank", "bank");

            requestParams.put("detail", jsonString);

            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost(VOUCHER);
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setCharset(Charset.forName("UTF-8"));
            for (Map.Entry<String, String> e : requestParams.entrySet()) {
                StringBody stringBody = new StringBody(e.getValue(), contentType);
                reqEntity.addPart(e.getKey(), stringBody);
            }
            httppost.setEntity(reqEntity.build());
            String signContent = ApiSignUtils.getSignCheckContent(requestParams);
            String sign = ApiSignUtils.rsaSign(signContent, privateKey);
            httppost.addHeader("merId", TEST_MERID);
            httppost.addHeader("secret", TEST_SECRET);
            httppost.addHeader("sign", sign);
            try {
                HttpResponse response = client.execute(httppost);
                HttpEntity entity = response.getEntity();
                System.out.println(EntityUtils.toString(entity));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testBusinessVoucher1(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<1; i++){
            Thread myThread = new Thread(new Task1());
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



    class Task2 implements Runnable {
//

        @Override

        public void run() {

            List<BusinessPaymentVoucherDetail> details = new ArrayList<>();
            for (int i = 0; i < 20000; i++) {
                String uniqueId = "thirdTest" + i;
                String repaymentNumber = "";
                Map<String, String> requestParams1 = new HashMap<String, String>();
                requestParams1.put("fn", "100001");
                requestParams1.put("contractNo", "");
                requestParams1.put("requestNo", UUID.randomUUID().toString());
                requestParams1.put("uniqueId", uniqueId);
                try {
                    String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
                    Result result = JsonUtils.parse(sr, Result.class);
                    Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
                    JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
                    JSONObject jo = jsonArray.getJSONObject(0);
                    repaymentNumber = (String) jo.get("repaymentNumber");
                    System.out.println("\n" + repaymentNumber + "\t" + (i + 1));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                BusinessPaymentVoucherDetail detail = new BusinessPaymentVoucherDetail();
                detail.setAmount(new BigDecimal("6"));
                detail.setUniqueId(uniqueId);
                detail.setRepaymentPlanNo(repaymentNumber);
                detail.setPrincipal(new BigDecimal("6"));
                detail.setInterest(new BigDecimal("0"));
                detail.setServiceCharge(new BigDecimal("0"));
                detail.setMaintenanceCharge(new BigDecimal("0"));
                detail.setOtherCharge(new BigDecimal("0"));
                detail.setPayer(1);
                details.add(detail);

            }
            String jsonString = JsonUtils.toJsonString(details);
            String amount = "60000";
            Map<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("fn", "300003");
            requestParams.put("requestNo", UUID.randomUUID().toString());
            requestParams.put("transactionType", "0");
            requestParams.put("voucherType", "0");
            requestParams.put("voucherAmount", amount);
            requestParams.put("financialContractNo", "CS0001");
            requestParams.put("receivableAccountNo", "1220127571120");
            requestParams.put("paymentAccountNo", "6217857600016839349");
            requestParams.put("bankTransactionNo", UUID.randomUUID().toString());
            requestParams.put("paymentName", "秦萎");
            requestParams.put("paymentBank", "bank");

            requestParams.put("detail", jsonString);

            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost(VOUCHER);
            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setCharset(Charset.forName("UTF-8"));
            for (Map.Entry<String, String> e : requestParams.entrySet()) {
                StringBody stringBody = new StringBody(e.getValue(), contentType);
                reqEntity.addPart(e.getKey(), stringBody);
            }
            httppost.setEntity(reqEntity.build());
            String signContent = ApiSignUtils.getSignCheckContent(requestParams);
            String sign = ApiSignUtils.rsaSign(signContent, privateKey);
            httppost.addHeader("merId", TEST_MERID);
            httppost.addHeader("secret", TEST_SECRET);
            httppost.addHeader("sign", sign);
            try {
                HttpResponse response = client.execute(httppost);
                HttpEntity entity = response.getEntity();
                System.out.println(EntityUtils.toString(entity));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testBusinessVoucher2(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<1; i++){
            Thread myThread = new Thread(new Task2());
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
//        }
//

    //扣款
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
                    for (int index = 0; index < 200; index++) {
                        String uniqueId = "造数据100" + (0 + index);
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
                        requestParams.put("amount",  "6.00");
                        requestParams.put("repaymentType", "1");
                        requestParams.put("mobile", "13777847783");
                        requestParams.put("repaymentDetails", "[{'loanFee':0.00,'otherFee':0.00,'repaymentAmount':6,'repaymentInterest':0.00,'repaymentPlanNo':'"+repaymentNumber+"','repaymentPrincipal':6.00,'techFee':0.00,'overDueFeeDetail':{"
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


    //放款黑名单调试
    class Remittance1 implements Runnable{
        @Override
        public void run(){
            String uniqueId = "testdz22222";
            Map<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("fn", "300002");
            requestParams.put("requestNo", UUID.randomUUID().toString());
            requestParams.put("remittanceStrategy", "0");
            requestParams.put("productCode", "G31700");
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
                String sr = PostTestUtil.sendPost("http://remittance.5veda.net/api/command", requestParams, getIdentityInfoMap(requestParams));
                System.out.println(sr);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class CancelRemittance implements Runnable{
        @Override
        public void run(){
                Map<String, String> requestParams = new HashMap<String, String>();
                requestParams.put("fn", "300005");
                requestParams.put("requestNo", UUID.randomUUID().toString());
                requestParams.put("productCode", "G31700");
		        requestParams.put("uniqueId", "testdz22222");
//                 requestParams.put("contractNo", "testwf123455");
                try {
                    String sr = PostTestUtil.sendPost("http://remittance.5veda.net/api/command", requestParams, getIdentityInfoMap(requestParams));
                    System.out.println(sr);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }


        @Test
        public void test1() throws InterruptedException {
            List<Thread> threadList = new ArrayList<>();
            Thread thread1 = new Thread(new Remittance1());
            Thread thread2 = new Thread(new CancelRemittance());
            threadList.add(thread2);
            threadList.add(thread1);
//
//            for(Thread thread : threadList){
//                thread.start();
//                thread.join();
//            }
            threadList.parallelStream().forEach(t -> {
            try {
                t.start();
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        }




        //还款订单
    class RepaymentOrder implements Runnable {
        private int num;

        public RepaymentOrder(int num) {
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 100;


            for (int index = 0; index < 100; index++) {
                List<RepaymentDetailsModel> repaymentDetailsModelList = new ArrayList<>();
                List<BusinessAmountModel> businessAmountModelList = new ArrayList<>();
                String uniqueId = "fifthTest" + (id + index);
                String repaymentNumber = "";
                Map<String, String> requestParams1 = new HashMap<String, String>();
                requestParams1.put("fn", "100001");
                requestParams1.put("contractNo", "");
                requestParams1.put("requestNo", UUID.randomUUID().toString());
                requestParams1.put("uniqueId", uniqueId);
                try {
                    String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, getIdentityInfoMap(requestParams1));
                    Result result = JsonUtils.parse(sr, Result.class);
                    Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
                    JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
                    JSONObject jo = jsonArray.getJSONObject(0);
                    repaymentNumber = (String) jo.get("repaymentNumber");
                    System.out.println("\n" + repaymentNumber + "\t" + (index + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RepaymentDetailsModel repaymentDetailsModel1 = new RepaymentDetailsModel();
                repaymentDetailsModel1.setContractNo("");
                repaymentDetailsModel1.setContractUniqueId(uniqueId);
                repaymentDetailsModel1.setRepaymentWay(5001l);
                repaymentDetailsModel1.setRepaymentBusinessNo(repaymentNumber);
                repaymentDetailsModel1.setPlannedDate("");
                repaymentDetailsModel1.setDetailsTotalAmount(new BigDecimal("6"));
                BusinessAmountModel businessAmountModel1 = new BusinessAmountModel();
                businessAmountModel1.setFeeType(1000l);
                businessAmountModel1.setActualAmount(new BigDecimal("6"));

                businessAmountModelList.add(businessAmountModel1);

                repaymentDetailsModel1.setDetailsAmountJsonList(businessAmountModelList);
                repaymentDetailsModelList.add(repaymentDetailsModel1);
                System.out.println("已加入明细" + index + "笔");

                String s = JSON.toJSONString(repaymentDetailsModelList);
                Map<String, String> requestParams = new HashMap<String, String>();
                requestParams.put("orderRequestNo", UUID.randomUUID().toString());
                requestParams.put("orderUniqueId",  UUID.randomUUID().toString());
                requestParams.put("transType", "0");
                requestParams.put("financialContractNo", "CS0001");
                requestParams.put("orderAmount", "6");
                requestParams.put("repaymentOrderDetail", s);
                try {
                    System.out.println(s);
                    String sr = PostTestUtil.sendPost(REPAYMENTORDER, requestParams, getIdentityInfoMap(requestParams));
                    System.out.println(sr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testRepaymentOrder(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<200; i++){
            Thread myThread = new Thread(new RepaymentOrder(i));
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



    //浮动费用
        class MutableFee implements Runnable {
//                private int num;
//
//                public Deduct(int num) {
//                    super();
//                    this.num = num;
//                }

            public void run() {
                for (int i = 0; i < 100; i++) {
                    Map<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("requestNo", UUID.randomUUID().toString());
                    requestParams.put("financialProductCode", "G31700");
                    requestParams.put("contractNo", "overFT195");

                    requestParams.put("repaymentPlanNo", "ZC140567477150597120");
//		requestParams.put("repayScheduleNo","fanteng1");
                    requestParams.put("reasonCode", "0");
                    requestParams.put("approver", "FXF");
                    requestParams.put("approvedTime", "2017-11-22");
                    requestParams.put("comment", "TestInterface");

                    MutableFeeDetail detail = new MutableFeeDetail(1, new BigDecimal("100"));
                    MutableFeeDetail detail1 = new MutableFeeDetail(2, new BigDecimal("100"));
                    MutableFeeDetail detail2 = new MutableFeeDetail(3, new BigDecimal("100"));
                    MutableFeeDetail detail3 = new MutableFeeDetail(4, new BigDecimal("100"));

                    List<MutableFeeDetail> details = new ArrayList<>();
                    details.add(detail);
                    details.add(detail1);
                    details.add(detail2);
                    details.add(detail3);
                    net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(details);
                    requestParams.put("details", jsonArray.toString());

                    try

                    {
                        String sr = PostTestUtil.sendPost(MUTABLE_FEE, requestParams,
                                getIdentityInfoMap(requestParams));
                        System.out.println(sr);
                    } catch (
                            Exception e)

                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

    }


    @Test
    public void testMutalbeFee(){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<300; i++){
            Thread myThread = new Thread(new MutableFee());
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


    //变更还款计划
    class ModifyRepaymentPlan implements Runnable {
        private int num;

        public ModifyRepaymentPlan(int num) {
            super();
            this.num = num;
        }

        public void run() {
            long id = num * 100;
            System.out.println("id " + id);
            for (int i = 0; i < 100; i++) {
//
                    String uniqueId = "FGG"+(id+i);
                    Map<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("fn", "200001");
//                requestParams.put("financialProductCode", "overWrite3");
                    requestParams.put("uniqueId", uniqueId);
//			requestParams.put("contractNo","云信信2017-1496-DK(20171128000000006326)号");
                    requestParams.put("requestReason", "3");
                    requestParams.put("requestNo", UUID.randomUUID().toString());


                    List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
                    RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
                    model.setAssetInterest(new BigDecimal("0"));
                    model.setAssetPrincipal(new BigDecimal("6"));
                    model.setMaintenanceCharge(new BigDecimal("0"));
                    model.setServiceCharge(new BigDecimal("0"));
                    model.setOtherCharge(new BigDecimal("0"));
                    model.setRepayScheduleNo("");
                    model.setAssetType(1);
                    model.setAssetRecycleDate("2018-01-23");

                    requestDataList.add(model);

                    requestParams.put("requestData", JsonUtils.toJsonString(requestDataList));
                    try {
                        String sr = PostTestUtil.sendPost(MODIFY_REPAYMENT, requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://contra.5veda.net/api/v3/modifyRepaymentPlan", requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://yunxin.5veda.net/api/modify", requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://192.168.0.128/api/modify", requestParams, getIdentityInfoMap(requestParams));
                        System.out.println(Thread.currentThread().getName() + "---------->" + sr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Test
        public void testModifyRepaymentPlan() {
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Thread myThread = new Thread(new ModifyRepaymentPlan(i));
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



        //修改逾期费用
        class ModifyOverDueFee implements Runnable {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Map<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("fn", "200005");
//			requestParams.put("financialProductCode","G31700");
                    requestParams.put("requestNo", UUID.randomUUID().toString());
                    /*requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST002\",\"repaymentPlanNo\":\"ZC14102608024244224\",\"overDueFeeCalcDate\":\"2017-01-17\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"test124\",\"repaymentPlanNo\":\"ZC18440915338375168\",\"overDueFeeCalcDate\":\"2017-01-17\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");*/
                    //requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST00000500\",\"repaymentPlanNo\":\"ZC626151806080905216\",\"overDueFeeCalcDate\":\"2017-01-22\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");

                    requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"overW31105\",\"repaymentPlanNo\":\"ZC135778041744900096\",\"overDueFeeCalcDate\":\"2017-12-19\",\"penaltyFee\":40,\"latePenalty\":40,\"lateFee\":30,\"lateOtherCost\":30,\"totalOverdueFee\":140}]");
//			requestParams.put("modifyOverDueFeeDetails", "[{\"financialProductCode\":\"CS0001\",\"contractUniqueId\":\"overWrite13\",\"repaymentPlanNo\":\"ZC132670904422309888\",\"overDueFeeCalcDate\":\"2017-11-27\",\"penaltyFee\":30,\"latePenalty\":30,\"lateFee\":30,\"lateOtherCost\":30,\"totalOverdueFee\":120}]");
                    //requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST002\",\"repaymentPlanNo\":\"ZC14102608024244224\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"TEST001\",\"repaymentPlanNo\":\"ZC14096339980193792\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},"
                    //+ "{\"contractUniqueId\":\"test124\",\"repaymentPlanNo\":\"ZC18440915338375168\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"zshtest3213456\",\"repaymentPlanNo\":\"ZC14093931451453440\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");

                    //requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"wwtest--contract-1003-1003\",\"repaymentPlanNo\":\"ZC843795461589327872\",\"overDueFeeCalcDate\":\"2017-02-08\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"TEST131\",\"repaymentPlanNo\":\"ZC832136444735537152\",\"overDueFeeCalcDate\":\"2017-02-08\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");

                    //requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST002\",\"repaymentPlanNo\":\"ZC14102608024244224\",\"overDueFeeCalcDate\":\"2017-02-07\",\"penaltyFee\":1,\"latePenalty\":2,\"lateFee\":3,\"lateOtherCost\":4,\"totalOverdueFee\":10}]");
                    try {
                        String sr = PostTestUtil.sendPost(MODIFY_OVERDUE_FEE, requestParams, getIdentityInfoMap(requestParams));
                        System.out.println(sr);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }

        @Test
        public void testModifyOverDueFee() {
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 300; i++) {
                Thread myThread = new Thread(new ModifyOverDueFee());
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


        /*
        提前还款
         */
        class ModifyPrepaymentPlan implements Runnable {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Map<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("fn", "200002");
                    requestParams.put("financialProductCode", "G31700");
                    requestParams.put("uniqueId", "overWrite82");
//		requestParams.put("contractNo", "overWrite2");
//		requestParams.put("repayScheduleNo","");
                    requestParams.put("requestNo", UUID.randomUUID().toString());
                    requestParams.put("assetRecycleDate", "2017-12-10");
                    requestParams.put("assetInitialValue", "200000");
                    requestParams.put("assetPrincipal", "200000");
                    requestParams.put("assetInterest", "0");
                    requestParams.put("serviceCharge", "0");
                    requestParams.put("maintenanceCharge", "0");
                    requestParams.put("otherCharge", "0");
                    requestParams.put("type", "0");
                    requestParams.put("payWay", "0");
                    //requestParams.put("hasDeducted", "-1");

                    try {
                        String sr = PostTestUtil.sendPost(PREPAYMENT, requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
                        System.out.println(sr);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        @Test
        public void testModifyPrepaymentPlan() {
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 300; i++) {
                Thread myThread = new Thread(new ModifyPrepaymentPlan());
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


        /*
        主动付款凭证
         */
        class ActiveVoucher implements Runnable {

            public void run() {
                for (int i = 0; i < 10; i++) {
                    Map<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("fn", "300004");
                    requestParams.put("transactionType", "0");
                    requestParams.put("requestNo", UUID.randomUUID().toString());
                    requestParams.put("voucherType", "5");//5,6
//		requestParams.put("uniqueId", "妹妹你大胆的往前走28");
//		requestParams.put("receivableAccountNo", "600000000001");
                    requestParams.put("paymentBank", "中国工商银行");
                    requestParams.put("bankTransactionNo", UUID.randomUUID().toString());
                    requestParams.put("voucherAmount", "2940");
                    requestParams.put("financialContractNo", "G31700");

                    ActivePaymentVoucherDetail detail = new ActivePaymentVoucherDetail();
                    detail.setUniqueId("妹妹你大胆的往前走815");
                    detail.setRepaymentPlanNo("ZC130812252658515968");
//		detail.setRepayScheduleNo("TESTDZZ0821_107_DZZ");
                    detail.setAmount(new BigDecimal("980"));
                    detail.setPrincipal(new BigDecimal("900"));
                    detail.setInterest(new BigDecimal("20"));
                    detail.setServiceCharge(new BigDecimal("20"));
                    detail.setMaintenanceCharge(new BigDecimal("20"));
                    detail.setOtherCharge(new BigDecimal("20"));
//		requestParams.put("contractNo", "");

                    ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
                    detail2.setUniqueId("妹妹你大胆的往前走818");
                    detail2.setRepaymentPlanNo("ZC130812277111308288");
//		detail.setRepayScheduleNo("TESTDZZ0821_107_DZZ");
                    detail2.setAmount(new BigDecimal("980"));
                    detail2.setPrincipal(new BigDecimal("900"));
                    detail2.setInterest(new BigDecimal("20"));
                    detail2.setServiceCharge(new BigDecimal("20"));
                    detail2.setMaintenanceCharge(new BigDecimal("20"));
                    detail2.setOtherCharge(new BigDecimal("20"));

                    ActivePaymentVoucherDetail detail3 = new ActivePaymentVoucherDetail();
                    detail3.setUniqueId("妹妹你大胆的往前走819");
                    detail3.setRepaymentPlanNo("ZC130812309642330112");
//		detail.setRepayScheduleNo("TESTDZZ0821_107_DZZ");
                    detail3.setAmount(new BigDecimal("980"));
                    detail3.setPrincipal(new BigDecimal("900"));
                    detail3.setInterest(new BigDecimal("20"));
                    detail3.setServiceCharge(new BigDecimal("20"));
                    detail3.setMaintenanceCharge(new BigDecimal("20"));
                    detail3.setOtherCharge(new BigDecimal("20"));

                    requestParams.put("detail", JsonUtils.toJsonString(Arrays.asList(detail, detail2, detail3)));
                    requestParams.put("paymentName", "高渐离啊");
                    requestParams.put("paymentAccountNo", "6214855712117670");

                    CloseableHttpClient client = HttpClientBuilder.create().build();
//		HttpPost httppost = new HttpPost(COMMAND_URL_TEST);

//		File file = FileUtils.getFile("/Users/apple/1.png");
//		File file2 = FileUtils.getFile("/Users/apple/2.png");
//		File file5 = FileUtils.getFile("/Users/apple/3.png");

//		CloseableHttpClient client = HttpClientBuilder.create().build();
//		HttpPost httppost = new HttpPost("http://127.0.0.1:9090/api/command");
//		HttpPost httppost = new HttpPost("http://yunxin.zufangbao.cn/api/command");
//		HttpPost httppost = new HttpPost("http://localhost:9090/api/command");
                    HttpPost httppost = new HttpPost(AVTIVE_VOUCHER);
//		HttpPost httppost = new HttpPost("http://yunxin.5veda.net/api/command");

//		File file = FileUtils.getFile("/Users/louguanyang/Desktop/2.png");
//		File file2 = FileUtils.getFile("/Users/louguanyang/Desktop/gamersky_01origin_01_201412922358DD.jpg");
//		File file3 = FileUtils.getFile("/Users/louguanyang/Desktop/gamersky_21origin_41_201411812152E3.jpg");
//		File file4 = FileUtils.getFile("/Users/louguanyang/Desktop/gamersky_001origin_001_2015117171327B.jpg");
//		File file5 = FileUtils.getFile("/Users/louguanyang/Desktop/pdf.pdf");

//		File file = FileUtils.getFile("C:/Users/Public/Pictures/Sample Pictures/1.jpg");
//		File file2 = FileUtils.getFile("C:/Users/Public/Pictures/Sample Pictures/2.jpg");
//		File file5 = FileUtils.getFile("C:/Users/Public/Pictures/Sample Pictures/3.jpg");
//
//		File file = FileUtils.getFile("D:/Users/louguanyang/Desktop/2016-10-14/1.png");
//		File file2 = FileUtils.getFile("D:/Users/louguanyang/Desktop/2016-10-14/2.png");
//		File file5 = FileUtils.getFile("D:/Users/louguanyang/Desktop/2016-10-14/3.png");


//		FileBody bin = new FileBody(file);
//		FileBody bin2 = new FileBody(file2);
//		FileBody bin3 = new FileBody(file3);
//		FileBody bin4 = new FileBody(file4);
//		FileBody bin5 = new FileBody(file5);

                    ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

                    MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                    reqEntity.setCharset(Charset.forName(HTTP.UTF_8));

//        reqEntity.addPart("file", bin);
//        reqEntity.addPart("file", bin2);
//        reqEntity.addPart("file3", bin3);
//        reqEntity.addPart("file4", bin4);
//        reqEntity.addPart("file5", bin5);
                    for (Map.Entry<String, String> e : requestParams.entrySet()) {
                        StringBody stringBody = new StringBody(e.getValue(), contentType);
                        reqEntity.addPart(e.getKey(), stringBody);
                    }
                    httppost.setEntity(reqEntity.build());
                    String signContent = ApiSignUtils.getSignCheckContent(requestParams);
                    String sign = ApiSignUtils.rsaSign(signContent, privateKey);
                    httppost.addHeader("merId", TEST_MERID);
                    httppost.addHeader("secret", TEST_SECRET);
                    httppost.addHeader("sign", sign);
                    System.out.println(sign);
                    HttpResponse response = null;
                    try {
                        response = client.execute(httppost);
                        HttpEntity entity = response.getEntity();
                        System.out.println(EntityUtils.toString(entity));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        @Test
        public void testActiveVoucher() {
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 300; i++) {
                Thread myThread = new Thread(new ActiveVoucher());
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



        @Test
        public void testpaymentorder(){
            for (int i =0; i<10000;i++){
                String orderUniqueId = "FGG" + i;
                Map<String,String> requestParams = new HashMap<>();
                requestParams.put("requestNo", UUID.randomUUID().toString()); //请求编号
                requestParams.put("paymentNo",UUID.randomUUID().toString()); //商户支付编号
                requestParams.put("orderUniqueId",orderUniqueId); //商户订单号
//        requestParams.put("orderUuid","c17b095e-742b-49fa-8c11-fa39dfe44238"); //五维订单号
                requestParams.put("financialContractNo","CS0001"); //信托产品代码
                requestParams.put("payWay","2"); //支付方式 0：线下转账  2：线上代扣  3：商户代扣
                requestParams.put("paymentAccountNo","6217857600016839337"); //付款银行帐户号
                requestParams.put("paymentAccountName","秦萎"); //付款银行帐户名称
                requestParams.put("paymentBankCode","C10104"); //付款银行名称编码
                requestParams.put("paymentProvinceCode","620000"); //付款方所在地省编码
                requestParams.put("paymentCityCode","141100"); //付款方所在地市编码
                requestParams.put("idCardNum","421182199204114115"); //付款方身份证
                requestParams.put("mobile","17682481004"); //付款方手机号
                requestParams.put("paymentGateWay","2"); //支付通道
                requestParams.put("tradeUuid",UUID.randomUUID().toString()); //通道交易号
                requestParams.put("amount","6"); //交易金额
                requestParams.put("transactionTime","2017-09-21 16:48:11"); //交易时间
                requestParams.put("receivableAccountNo","6001"); //收款方账号
                requestParams.put("receivableAccountName","sdf"); //收款方户名
                requestParams.put("receivableBankCode","C10103");//收款方开户行
//        requestParams.put("notifyUrl","http://www.qq.com"); //回调地址
                try {
                    String sr = PostTestUtil.sendPost(PAYMENTORDERONLINE, requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
                    System.out.println(sr);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            }
        }

