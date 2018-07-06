package com.zufangbao.testAPIWuBo;
import com.zufangbao.sun.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Cool on 2017/7/15.
 */
public class testApiZH extends BaseApiTestPost {
    TestMethod testMethod = new TestMethod();
    String productCode = "11111111";
    String uniqueId = UUID.randomUUID().toString();
//    String uniqueId = "f37206c1-0795-49cb-a0f1-77c8d763147f";
    String amount = "2";
    String accountNo="6214855712105555";
    String accountName = "WUBO";
    String deductId = "78786500-5c70-464a-bc20-771cc95e8a98扣款";
    String remittanceId = UUID.randomUUID().toString();

    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 0));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));


    @Test
    public void testDate(){
        System.out.println(firstPlanDate);
    }
    @Test
            public void test1111(){
        System.out.println(thirdPlanDate);
    }

    PrepaymentCucumberMethod prepaymentCucumberMethod =new PrepaymentCucumberMethod();
    TestMethodsZH testMethodsZH = new TestMethodsZH();
    QueryDeuctInfoTestMethod queryDeuctInfoTestMethod = new QueryDeuctInfoTestMethod();
    /**
     * 签约接口测试
     */
    @Test
    public void testSignUp(){
        String proNo = "qianyue035";//每次请求都需要变，不能用UUID太长了
        testMethod.testSignUp(proNo,"1","62284843789576900001","250","ABC");
    }

    //签约查询
    @Test
    public void testQuerySignUp(){
        String proNo = "qianyue033";//每次请求都需要变，不能用UUID太长了
        testMethod.testQuerySignUp("proNo","62284843789576934589");
    }
    @Test
    //中行广银联放款测试
    public void makeLoan0() throws Throwable {
        String requestNo = UUID.randomUUID().toString();
        String remittanceId = "wubo1006";
        prepaymentCucumberMethod.testApiZHCommandRemittance(requestNo,remittanceId, uniqueId, "3000", productCode);
    }
    /**
     * 中航放款测试-不可用
     */
//    @Before
    @Test
    public void testMakeLoanZH(){
        String in_url = "http://192.168.0.128:38084/pre/api/remittance/zhonghang/remittance-application";
//        String out_url = "http://avictctest.5veda.net:8886/pre/api/remittance/zhonghang/update";
        String out_url = "http://avictctest.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application";
        testMethod.makeLoanZH(productCode,uniqueId,"3000",out_url,remittanceId);
    }
    //中航放款测试-可用-新
    @Test
    public void testMakeLoanZH1(){
        String in_url = "http://192.168.0.128:38084/pre/api/remittance/zhonghang/remittance-application";
//        String out_url = "http://avictctest.5veda.net:8886/pre/api/remittance/zhonghang/update";
        String out_url = "http://avictctest.5veda.net:8886/pre/api/remittance/zhonghang/remittance-application";
        testMethod.makeLoanZH1(productCode,uniqueId,"3000",out_url,remittanceId);
    }
    @Test
    /**
     * 中航扣款测试-可用
     */
    public void testDeductRepaymenPlantZH(){
        String in_url = "http://192.168.0.128:38083/pre/api/DEDUCT/zhonghang/ZH-SingleDeduct";
        String out_url = "http://avictctest.5veda.net:8887/pre/api/DEDUCT/zhonghang/ZH-SingleDeduct";
        String repaymentNumber=testMethod.query_i_RepaymentPlan("b4808fd1-3105-46fc-a463-ba8031323583",0);
        System.out.println("还款计划编号为："+repaymentNumber);
        testMethod.deductRepaymentPlanZH(out_url,"11111111","b4808fd1-3105-46fc-a463-ba8031323583",repaymentNumber,"100","王宝","6228480444455553333");
}


    //通联扣款
    @Test
    public void tongliankoukuanceshi(){
        String out_url = "http://avictctest.5veda.net/api/modify";
        testMethodsZH.importAssetPackage2(out_url,"3000", "WB123", uniqueId, "0", "1000", firstPlanDate, secondPlanDate, thirdPlanDate);
        out_url = "http://avictctest.5veda.net:8887/pre/api/DEDUCT/zhonghang/ZH-SingleDeduct";
        String repaymentNumber=testMethod.query_i_RepaymentPlan(uniqueId,0);
        System.out.println("还款计划编号为："+repaymentNumber);
        testMethod.deductRepaymentPlanZH1(out_url,"WB123",uniqueId,repaymentNumber,"0.01","章zzz光","6228481558454183274");
    }

    /**
     * 中航-云信 查询扣款接口
     */
    @Test
    public void testQueryDeductZH(){
        String in_url = "http://192.168.0.128:38083/api/query/deduct_status";
        String out_url = "http://avictctest.5veda.net:8887/api/query/deduct_status";
        testMethod.queryDeductZH(out_url,"866d102d-a717-4c6e-af95-aef5745f31a8","f03c1122-afea-4bdc-a164-2af2debb3b6f扣款");
    }

    /**
     * 中航-云信专户实时余额查询接口
     */
    @Test
    public void testAccountRealTimeZH(){
        String in_url = "http://192.168.0.128:38081/api/query";
        String out_url = "http://avictctest.5veda.net/api/query";
        testMethod.accountRealTime(out_url,"WB123","9838394753");
    }

    /**
     * 批量扣款查询接口
     */
    @Test
    public void testAssetPackageBatchQueryZH(){
        String in_url = "http://192.168.0.128:38083/api/query/deduct_status_list";
        String out_url = "http://avictctest.5veda.net:8887/api/query/deduct_status_list";
        List<String> lst = new ArrayList<>();
        lst.add(deductId);
        testMethod.assetPackageBatchQueryZH(out_url,lst);
    }

    /**
     * 中航-云信批量放款状态查询接口
     */
    @Test
    public void testBatchLoanStatusQuery(){
        String in_url = "http://192.168.0.128:38084/api/query/remittance_status_list";
        String out_url = "http://avictctest.5veda.net:8886/api/query/remittance_status_list";
        String oriRequestNo = "101719f0-860a-4db5-97b4-117f2de048ed";
        testMethod.batchLoanStatusQuery(out_url,oriRequestNo,uniqueId);
    }

    //中航-导入资产包-可用
//    @Before
    @Test
    public void testImportAssetPackage(){
        String in_url ="http://192.168.0.128:38081/api/modify";
        String out_url = "http://avictctest.5veda.net/api/modify";
        String local_url = "http://192.168.0.116:7778/api/v3/importAssetPackage";
        System.out.println(uniqueId);
        testMethodsZH.importAssetPackage1(out_url,"4500", "WB123", uniqueId, "0", "1500", firstPlanDate, secondPlanDate, thirdPlanDate);
    }
//    //扣款
//    @Test
//    public void testDeduct(){
//        String int_queryUrl="http://192.168.0.128:38081/api/query";//查询
//        String out_queryUrl="http://avictctest.5veda.net/api/query";
//        String deductUrl = "http://192.168.0.128:38083/pre/api/DEDUCT/zhonghang/ZH-SingleDeduct";
//        String repaymentNumber = testMethodsZH.query_i_RepaymentPlan(out_queryUrl,"7616f677-f545-416b-9aa1-29d7222e74f9",0);
//        System.out.println("还款计划编号："+repaymentNumber);
//        testMethodsZH.deductRepaymentPlanNEW(deductUrl,repaymentNumber,"7616f677-f545-416b-9aa1-29d7222e74f9",productCode,"1000","1000","1");
//    }

    //变更还款信息接口
    @Test
    public void testChangeReapaymentInfo(){
        String local_url = "http://192.168.1.77:9090/api/modify";
        String in_url = "http://192.168.0.128/api/modify";
        String out_url = "http://avictctest.5veda.net/api/modify";
        testMethodsZH.changeRepaymentInfo(out_url,"f37206c1-0795-49cb-a0f1-77c8d763147f","622588121251757643");
    }
    //专户流水查询
    @Test
    public void testQueryCashFlow(){
        String in_url = "http://192.168.0.128:38081/api/query";
        String out_url = "http://avictctest.5veda.net/api/query";
        testMethod.queryCashFlow(out_url,"TL321","","1");
    }

}
