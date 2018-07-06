package com.zufangbao.testAPIWuBo;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.testAPIWuBo.testAPI.models.ContractDetail;
import com.zufangbao.testAPIWuBo.testAPI.models.ImportAssetPackageContent;
import com.zufangbao.testAPIWuBo.testAPI.models.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

import java.util.*;

/**
 * Created by Cool on 2017/7/18.
 */
public class RunnableMakeLoanImportAssetpackage extends BaseApiTestPost{
    /**
     * Created by liwei on 16/7/19.
     * 使用匿名内部类的格式:
     *
     * *        new 类名或者接口名() {
     *          重写方法;
     *      };
     *      本质：是该类或者接口的子类对象。
     */
    String uniqueId = "";
    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

    static List<String> list = new ArrayList<>();
    public static void main(String[] args) {
            // 继承 Thread 类来实现多线程
            new Thread("线程1"){
                @Override
                public void run(){
                    for (int i=0;i<1;i++){
                        String uniqueId = UUID.randomUUID().toString();
                        list.add(uniqueId);
                    }
                        for (String e:list) {
                            //makeLoan2("G31700", e, "20000");
                            System.out.println(Thread.currentThread().getName()+"--"+e);
                        }
//                        try {
//                            Thread.sleep(50000);
//                        }catch (Exception e){
//                        e.printStackTrace();
//                        }
                    importAssetPackage2("20000","20000","G31700",list,"0","1000","","","");
                }
            }.start();
            System.out.println("------ 无聊的分割线 ------");
        }
    public static void makeLoan2(String productCode,String uniqueId,String amount){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn", "300002");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("remittanceStrategy", "0");
        requestParams.put("productCode", productCode);
        requestParams.put("uniqueId", uniqueId);
        requestParams.put("contractNo", uniqueId);
        requestParams.put("plannedRemittanceAmount", amount);
        requestParams.put("auditorName", "auditorName1");
        requestParams.put("auditTime", "2016-08-20 00:00:00");
        requestParams.put("remark", "交易备注");
        String bankCardNo = "6222020200002430";
        requestParams.put("remittanceDetails", "[{'detailNo':'detailNo1','recordSn':'1','amount':'"+amount+"','plannedDate':'','bankCode':'C10102','bankCardNo':'"+bankCardNo+"','bankAccountHolder':'测试用户1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
        try {
            //String sr = PostTestUtil.sendPost("http://192.168.0.128:8084/api/command", requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));
            String sr = PostTestUtil.sendPost("http://remittance.5veda.net/api/command", requestParams,new BaseApiTestPost().getIdentityInfoMap(requestParams));

            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /*
	中金-导入资产包
	 */
    public static void importAssetPackage2(String batchContractsTotalAmount,String totalAmount,String productCode,List<String> uniqueIdList,String repaymentAccountNo,String amount,String firstRepaymentDate,String secondRepaymentDate,String thirdRepaymentDate){
        for (int index = 0; index < 1; index++) {
            ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
            importAssetPackageContent.setThisBatchContractsTotalNumber(1);//贷款合同个数
            importAssetPackageContent.setThisBatchContractsTotalAmount(batchContractsTotalAmount);
            importAssetPackageContent.setFinancialProductCode(productCode);
            List<ContractDetail> contracts = new ArrayList<ContractDetail>();
            for (int k = 0; k <1 ; k++) {
                ContractDetail contractDetail = new ContractDetail();
                contractDetail.setUniqueId(uniqueIdList.get(k));
                contractDetail.setLoanContractNo(uniqueIdList.get(k));
                contractDetail.setLoanCustomerNo(UUID.randomUUID().toString());
                contractDetail.setLoanCustomerName("WUBO");
                contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
                contractDetail.setIDCardNo("320301198502169142");
                contractDetail.setBankCode("C10105");
                contractDetail.setBankOfTheProvince("110000");
                contractDetail.setBankOfTheCity("110100");
                //contractDetail.setRepaymentAccountNo("621485571210652"+repaymentAccountNo);
                contractDetail.setRepaymentAccountNo("6222020200002432");
                contractDetail.setLoanTotalAmount(totalAmount);
                contractDetail.setLoanPeriods(20);//还款计划期数
                contractDetail.setEffectDate(DateUtils.format(new Date()));
                contractDetail.setExpiryDate("2099-01-01");
                contractDetail.setLoanRates("0.156");
                contractDetail.setInterestRateCycle(1);
                contractDetail.setPenalty("0.0005");
                contractDetail.setRepaymentWay(2);
                List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();
                for(int i=0;i<20;i++) {
                    String PlanDate = DateUtils.format(DateUtils.addMonths(new Date(), i));
                    ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
                    repaymentPlanDetail1.setRepaymentPrincipal(amount);
                    repaymentPlanDetail1.setRepaymentInterest("0.00");
                    repaymentPlanDetail1.setRepaymentDate(PlanDate);
                    repaymentPlanDetail1.setOtheFee("0.00");
                    repaymentPlanDetail1.setTechMaintenanceFee("0.00");
                    repaymentPlanDetail1.setLoanServiceFee("0.00");
                    repaymentPlanDetails.add(repaymentPlanDetail1);
                }
                contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);
                contracts.add(contractDetail);
            }
            importAssetPackageContent.setContractDetails(contracts);
            String param =  JsonUtils.toJsonString(importAssetPackageContent);
            System.out.println(param);
            Map<String,String> params =new HashMap<String,String>();
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
                String sr = PostTestUtil.sendPost("http://192.168.0.128/api/modify", params, headers);
                //String sr = PostTestUtil.sendPost("http://yunxin.5veda.net/api/modify", params, headers);

                System.out.println( "===========" + index + sr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
