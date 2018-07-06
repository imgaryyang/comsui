package testVan;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;

import java.util.*;

public class ImportPackage extends BaseApiTestPost {



    class ImportPackages implements Runnable {
        private int num;
        public ImportPackages(int num){
            super();
            this.num = num;
        }


        public void run() {
            long id = num * 200;
            System.out.println("id " + id);
            for (int index = 0; index < 100; index++) {
                String a = id + index + "";
                String uniqueId = "firstBusinessVoucherTest" + a;
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

    public static void main(String [] args){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<200; i++){
            Thread myThread = new Thread(new ImportPackage().new ImportPackages(i));
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
}
