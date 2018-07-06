package com.zufangbao.testAPIWuBo;

import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.ThirdVoucherService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Wubo on 17-3-24.
 */

@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml" })

public class testApiYunXin extends commandUrl {

    //String productCode = "G32000";
    String productCode = "G31700";
    String productCode1 = "11111111";
    String productCode2 = "WB123";
    String repayScheduleNo="deduct9019";
    String deductId = UUID.randomUUID().toString();
    String uniqueId = UUID.randomUUID().toString();
//    String uniqueId = "176942c8-52ed-48a6-b42e-c456438ad3rt";
    String batchContractsTotalAmount = "100000000";
    String totalAmount = "3000.00";
    String amount = "1000.00";
    String repaymentPlanNo = "";
    String result = "";
    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 0));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), -1));

    //    @Autowired
//    private DeductApplicationDetailService deductApplicationDetailService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    ThirdVoucherService thirdVoucherService;

    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    QueryDeuctInfoTestMethod queryDeductInfoTestMethod = new QueryDeuctInfoTestMethod();
    TestMethod testMethod = new TestMethod();
    testMethod111 testMethod111 = new testMethod111();

    //UUID生成
    @Test
    public void testUUid(){
        System.out.println(UUID.randomUUID().toString());
    }

//    @Before
    @Test
    public void makeLoan() throws Throwable {
//        广银联放款
       prepaymentCucumberMethod.makeLoan1("WB123", uniqueId, "3000");
//        while(true){
//            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
//
//            if(executionStatus == 2){
//                break;
//            }
//            Thread.sleep(10000);
//        }
//        导入资产包
//        prepaymentCucumberMethod.importAssetPackage1("","60000.00", "WB123", uniqueId, "0", "20000.00", firstPlanDate, secondPlanDate, thirdPlanDate);
        System.out.println("贷款合同编号："+uniqueId);
    }
//    @Before
    @Test
    //中金
    public void makeLoan1() throws Throwable {
        //放款
//        prepaymentCucumberMethod.makeLoan2("WB123", uniqueId, totalAmount);
//        while(true){
//            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
//
//            if(executionStatus == 2){
//                break;
//            }
//            Thread.sleep(10000);
//        }
        //Thread.sleep(50000);

        //导入资产包
        System.out.println("贷款合同编号："+uniqueId);
        prepaymentCucumberMethod.importAssetPackage2(totalAmount,"WB123",uniqueId,"remhhhhh","0","1000",firstPlanDate,secondPlanDate,thirdPlanDate);
    }
//    @Before
    @Test
    //宝付
    public void makeLoan2() throws Throwable {
        //放款
        //prepaymentCucumberMethod.makeLoan3(productCode, "wb9988760192", amount);
//        while(true){
//            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
//
//            if(executionStatus == 2){
//                break;
//            }
//            Thread.sleep(10000);
//        }
        //Thread.sleep(50000);

        //导入资产包
        prepaymentCucumberMethod.importAssetPackage3(totalAmount, "WB123", uniqueId, "3", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
    }
    //通联扣款
//    @Before
    @Test
    public void makeLoan8() throws Throwable {
//        prepaymentCucumberMethod.makeLoan1("WB123", uniqueId, "3000");
//        while(true){
//            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
//
//            if(executionStatus == 2){
//                break;
//            }
//            Thread.sleep(10000);
//        }
//        导入资产包
        prepaymentCucumberMethod.importAssetPackage1("","3000.00", "WB123", uniqueId, "3", "1000.00", firstPlanDate, secondPlanDate, thirdPlanDate);
        System.out.println("贷款合同编号："+uniqueId);
    }
    @Test
    //浦发放款
    public void makeLoan3(){
        //prepaymentCucumberMethod.makeLoan4("G08202",uniqueId,totalAmount);
        prepaymentCucumberMethod.makeLoan4(productCode,uniqueId,"5.01");
    }
    //民生银行放款
    @Test
    public void makeLoan6(){
        prepaymentCucumberMethod.makeLoan6(productCode2,uniqueId,"1");
    }

    //上海银行银企直联
    @Test
    public void makeLoan7(){
        prepaymentCucumberMethod.makeLoan7("SXAAA1",uniqueId,"1000");
    }

    //南京银行银企直联
    @Test
    public void makeLoan9(){
        for (int i = 0; i < 1; i++ ) {
            int money = (int)(Math.random() * 10000)  + 10000;
            prepaymentCucumberMethod.makeLoan9("WB123",uniqueId,"73000");
        }

    }
    @Test
    //平安或建行银企直联放款
    public void makeLoan4(){
        for (int i = 0; i < 1; i++) {
            prepaymentCucumberMethod.makeLoan5(productCode,"wb998876021784"+i,amount);
        }
    }
    @Test
    public void deduct111() throws Throwable {
        //扣款
        repaymentPlanNo = queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId, 0);//查询第i+1笔还款计划
        System.out.println(repaymentPlanNo);
        prepaymentCucumberMethod.deductRepaymentPlanNEW(repaymentPlanNo,"", uniqueId, "WB123", "1000", "1000", "1");
    }
    @Test
    public void TestQueryDeductInfo(){
        queryDeductInfoTestMethod.deductInfoList1("5e314789-81f8-4254-b8c5-362e42032e21","c077ce64-2750-473e-9067-7a6ca245e020");
    }
    //申请提前还款
    @Test

    public void prepaymentTest111() {
        prepaymentCucumberMethod.applyPrepaymentPlan("22828793-616b-4a85-a9b8-f164776fd6f7", "","3000", "3000", DateUtils.format(new Date()));
    }
    //变更还款计划接口
    @Test    public void changeRepaymentPlan() {
        testMethod.changeRepaymentPlan("", "4794d49e-bc35-4fc1-afc1-0be3a2999112", "9", "1");
    }

    //费用浮动接口
    @Test
    public void floatingCharges() throws InterruptedException {
        repaymentPlanNo = queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId, 0);
        System.out.println(repaymentPlanNo);
        Thread.sleep(6000);
        testMethod.floatingCharges("1500", uniqueId, productCode2, repaymentPlanNo);
    }

    /**
     * 制造第三方凭证,已测通
     * 注意：此方法只在内网可用，外网找有权限的人插SQL
     */
    @Test
    public void TestmakeThirdVoucher() {
        String transactionRequestNo = "wb111452341279" + "wb";//交易请求号
        String repaymentNumber = queryDeductInfoTestMethod.query_i_RepaymentPlan("wb111452341279", 0); // 0：第一期 1：第二期 2：第三期
        System.out.println("还款编号:" + repaymentNumber);
        System.out.println("第三方凭证接口：");
        testMethod.MakeVoucher(NewOuterInternat_command, transactionRequestNo, "wb111452341279", repaymentNumber, amount, productCode);
//        System.out.println("插入流水文件：");
//        System.out.println(" 插入第三方交易记录的流水记录");
//        ThirdPartyTransactionRecord thirdPartyTransactionRecord=new ThirdPartyTransactionRecord();
//        thirdPartyTransactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
//        thirdPartyTransactionRecord.setBusinessProcessStatus(BusinessProcessStatus.SUCCESS);
//        thirdPartyTransactionRecord.setFinancialContractUuid(productCode);
//        thirdPartyTransactionRecord.setAccountSide(com.zufangbao.sun.ledgerbook.AccountSide.DEBIT);
//        thirdPartyTransactionRecord.setMerchantOrderNo(transactionRequestNo);//凭证交易请求号
//        thirdPartyTransactionRecord.setTransactionAmount(new BigDecimal(amount));//amount
//        thirdPartyTransactionRecord.setTransactionTime(new Date());
//        thirdPartyTransactionRecord.setAuditStatus(InstitutionReconciliationAuditStatus.CREATE);
//        thirdPartyTransactionRecord.setPayTime(new Date());
//        thirdPartyTransactionRecord.setPaymentGateway(PaymentInstitutionName.UNIONPAYGZ);//0银联
//        thirdVoucherService.save(thirdPartyTransactionRecord);
    }

    //变更还款信息接口
    @Test
    public void TestchangeRepaymentInfo() {
        testMethod.changeRepaymentInfo(BaseApiTestPost.MODIFY_URL_TEST, uniqueId, "666662518000519914");
    }

    //商户付款凭证 三个还款计划
    @Test
    public void testMerchantVoucher() {
        String in_url = "http://192.168.0.128:7778/api/v3/business-payment-vouchers/submit";
        String out_url = "http://yunxin.5veda.net/api/v3/business-payment-vouchers/submit";
        testMethod.merchantVoucher1(in_url,uniqueId,"3000","1000");
    }
    //商户付款凭证 一个还款计划
    @Test
    public void testmerchantVoucher(){
//        String in_url = "http://192.168.0.128/api/command";
        String in_url = "http://192.168.0.128:7778/api/v3/business-payment-vouchers/submit";
        String out_url = "http://contra.5veda.net/api/v3/business-payment-vouchers/submit";
        System.out.println("开始查询还款计划");
        String repaymentPlanNo1 = queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId,0);
        System.out.println("还款计划为："+repaymentPlanNo1);
        System.out.println("开始上传商户付款凭证");
        testMethod.merchantVoucher(in_url,"SXAAA1",uniqueId,repaymentPlanNo1,"","1000.00");
    }
    @Test
    //商户付款凭证撤销接口
    public void testmerchantVoucherUndo(){

        String in_url = "http://192.168.0.128:7778/api/v3/business-payment-vouchers/undo";
        String out_url = "http://contra.5veda.net/api/v3/business-payment-vouchers/undo";
        testMethod.merchantVoucherUndo(out_url,"5d02fdee-0ffb-4330-84a7-18bd82ce621b","ZC140596336218652672",amount);
    }

    //主动付款凭证 一个还款计划
    @Test
    public void testActivePaymentVoucher() {
        String in_url = "http://192.168.0.128/api/command";
        String out_url = "http://yunxin.5veda.net/api/command";
        testMethod.testActivePaymentVoucher(out_url, UUID.randomUUID().toString(), productCode2, "1000", uniqueId, amount);
    }

    //第三方支付凭证
    @Test
    public void testThirdVoucher() {
        String in_url = "http://192.168.0.128/api/command";
        String out_url = "http://yunxin.5veda.net/api/command";
        String repaymentPlanNo = queryDeductInfoTestMethod.query_i_RepaymentPlan("wb9988760192", 0);
//        ThirdPartyTransactionRecord thirdPartyTransactionRecord =  new ThirdPartyTransactionRecord();
//        thirdPartyTransactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
//        thirdPartyTransactionRecord.setBusinessProcessStatus(BusinessProcessStatus.SUCCESS);
//        thirdPartyTransactionRecord.setFinancialContractUuid(UUID.randomUUID().toString());
//        thirdPartyTransactionRecord.setPaymentGateway(PaymentInstitutionName.BAOFU);
//        thirdPartyTransactionRecord.setMerchantNo("001053110000001");
//        thirdPartyTransactionRecord.setAccountSide(AccountSide.DEBIT);
//        thirdPartyTransactionRecord.setMerchantOrderNo("");//交易请求号
//        thirdPartyTransactionRecord.setSettleDate(new Date());
//        thirdPartyTransactionRecord.setTransactionAmount(new BigDecimal("1000"));
//        thirdPartyTransactionRecord.setServiceFee(new BigDecimal("0"));
//        thirdPartyTransactionRecord.setTransactionTime(new Date());
//        thirdPartyTransactionRecord.setPayTime(new Date());
//        testMethod.testThirdVoucher(in_url,UUID.randomUUID().toString(),"44d0b9fa-eb48-4e6c-8890-8d46a66634c31",amount,"1000",1,repaymentPlanNo);
        testMethod111.MakeVOucher(in_url, "44d0b9fa-eb48-4e6c-8890-8d46a99634c31", "wb9988760192", repaymentPlanNo);
    }
    //回购申请测试-单条
    @Test
    public void testRepurchase(){
        String in_url = "http://192.168.0.128/api/v2/repurchase";
        String out_url = "http://yunxin.5veda.net/api/v2/repurchase";
        String requestNo =UUID.randomUUID().toString();
        String batchNo = UUID.randomUUID().toString();
        System.out.println("batchNo:"+batchNo);
        testMethod.testRepurchase(out_url,requestNo,batchNo,productCode,"wb9988760315",totalAmount,totalAmount,"0");//提交
//        testMethod.testRepurchase(out_url,requestNo,"",productCode,"wb9988760315",totalAmount,totalAmount,"1");// 撤销 batchNo和repurchaseDetail选填其中一个就行
    }

    //回购申请测试-多条
    @Test
    public void testRepurchase1(){
        String in_url = "http://192.168.0.128/api/v2/repurchase";
        String out_url = "http://yunxin.5veda.net/api/v2/repurchase";
        String requestNo =UUID.randomUUID().toString();
        String batchNo = UUID.randomUUID().toString();
        System.out.println("batchNo:"+batchNo);
//        testMethod.testRepurchase1(out_url,requestNo,batchNo,productCode,"wb9988760315",totalAmount,totalAmount,"0");//提交
        testMethod.testRepurchase1(out_url,requestNo,"",productCode,"wb9988760315",totalAmount,totalAmount,"1");// 撤销 batchNo和repurchaseDetail选填其中一个就行
    }

    //逾期费用明细变更接口测试
    @Test
    public void testUpdateOverdueDetails(){
        String in_url = "http://192.168.0.128/api/modify";
        String out_url = "http://contra.5veda.net/api/v3/modifyOverDueFee";
        repaymentPlanNo = queryDeductInfoTestMethod.query_i_RepaymentPlan("overFXZ20",0);
        testMethod.updateOverdueDetails(out_url,"d4cb29b7-2740-42e6-a1cf-78ad51fee5b7","",DateUtils.format(new Date()));
    }
    //专户账户余额查询测试
    @Test
    public void testAccountRealTimeZH(){
        String in_url = "http://192.168.0.212/api/query";
        String out_url = "http://yunxin.5veda.net/api/query";
        testMethod.accountRealTime(out_url,"G31700","600000000001");
    }
    //专户流水查询-cash-flow
    @Test
    public void testQueryCashFlow(){
        String in_url = "http://192.168.0.128/api/query";
        String out_url = "http://yunxin.5veda.net/api/query";
        String huarui_out_url ="http://pf.5veda.net/api/query";
        testMethod.queryCashFlow(out_url,"WB123","831952389159132","1");
    }
    //专户流水查询-third_party_audit_bill
    @Test
    public void testQueryCashFlow1(){
        String in_url = "http://192.168.0.128/api/query";
        String out_url = "http://yunxin.5veda.net/api/query";
        String huarui_out_url ="http://pf.5veda.net/api/query";
        testMethod.queryCashFlow(in_url,"WB123","31600700009000356","1");
    }
    //还款订单测试
    @Test
    public void testRepaymentOder(){
//        String repaymentPlanNo1 = queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId,0);
//        String repaymentPlanNo2 = queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId,1);
        testMethod111.repaymentOderTest(uniqueId,"344","","wubo338");
    }
    //订单支付测试
    @Test
    public void testPaymentOrder(){
        testMethod111.testPaymentOrder("HI7000","1521876665940","24233.33");
    }

    //还款订单查询
    @Test
    public void queryRepaymentOrder(){
        String in_url = "http://192.168.0.128/api/v2/query-repaymentOrder";
        String out_url = "http://yunxin.5veda.net/api/v2/query-repaymentOrder";
        testMethod.queryRepaymentOrder(out_url,productCode,"20171206-ynxt-1512524822916","");
    }
    //订单支付查询
    @Test
    public void queryPaymentOrder(){
        String in_url = "";
        String out_url = "http://yunxin.5veda.net/api/v3/query-paymentOrder";
        testMethod111.testPaymentOrderQuery(out_url,"HI7000");
    }

    @Test
    public void query_voucher(){
        testMethod.query_voucher("bcca3e3b-bdd7-433f-95c5-920372ead2b7","");
    }

}
