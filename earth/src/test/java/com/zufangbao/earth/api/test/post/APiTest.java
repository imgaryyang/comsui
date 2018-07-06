package com.zufangbao.earth.api.test.post;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.service.ThirdVoucherService;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyAuditBillService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dzz on 17-5-9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class APiTest extends commandUrl {

    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;

    @Autowired
    private ThirdPartyAuditBillService thirdPartyAuditBillService;

    @Autowired
    private DeductPlanService deductPlanService;

    @Autowired
    private ThirdVoucherService thirdVoucherService;

    @Autowired
    CashFlowService cashFlowService;

    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号
    String transactionRequestNo=uniqueId+1;//交易请求号
//   String productCode="G47800";//信托代码
     String productCode="G31700";//信托代码，
      //String productCode="H66666";//信托代码,宝付代扣
      //String productCode="G32000";//信托代码，广银联代扣
    String totalAmount="1500";//放款总金额
    String amount="1500";//资产包每期还款计划的钱
    @Before
    public void init(){
        System.out.println("开始放款：");
        baseTestMethod.makeLoan(productCode, uniqueId,totalAmount,OuterInternat_command);
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //开始导入资产包
        System.out.println("开始导入资产包：");
        //一期还款计划
        baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,totalAmount,productCode,uniqueId,"0",amount, DateUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),2),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),3),"yyyy-MM-dd"));
        //三期还款计划
        //baseTestMethod.importAssetPackage2(OuterInternat_modifyUrl,totalAmount,productCode,uniqueId,"0",amount, DateUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),2),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),3),"yyyy-MM-dd"));

    }
    /**
     * 已测通
     * */
    @Test
    public void queryTest(){
        System.out.println("查询还款计划接口：");
        baseTestMethod.queryLoan(OuterInternat_queryUrl,uniqueId);
    }
    /**
     * 返回信息显示接口不存在，需寻找原因
     * */
    @Test
    public void queryTest1(){
        System.out.println("扣款查询接口：");
        String deductId="f458ec82-19cd-4eca-bdd1-afd80e5a8e57";
        String uniqueId="dzzz0609000";
        baseTestMethod.queryDeduct(OuterInternat_queryUrl,uniqueId,deductId);
    }
    /**
     * 依据查询信托号与时间段，查询还款清单结果，该接口已通
     * */
    @Test
    public void queryTest2(){
        System.out.println("查询还款清单接口：");
        baseTestMethod.queryRepayment(OuterInternat_queryUrl,productCode,DateUtils.format(new Date()),DateUtils.format(new Date()));
    }
    /**
     *
     *接口已经测通，但不清楚uniqueId是什么格式，需寻找原因
     */
    @Test
    public void queryTest3(){
        System.out.println("批量查询还款计划接口:");
        baseTestMethod.queryRepaymentPlan(OuterInternat_queryUrl,uniqueId,productCode,DateUtils.format(new Date()));
    }
    /**
     * 已测通
     *凭证查询接口
     *描述：供贷前系统调用，查询商户付款凭证接口，主动付款凭证接口，第三方扣款凭证接口核销结果。
     * batchRequest对应的是交易请求号，可在页面上查找到，例如：资金管理-第三方支付凭证-凭证详情-交易请求号
     *banktranscation是银行流水号，两个二选一查询
     * */
//    @Test
//    public void queryTest4(){
//        System.out.println("凭证查询接口:");
//        baseTestMethod.queryThirdVoucherBybatchRequest(OuterInternat_queryUrl,"","123");
//
//    }
    /**Thread.sleep(20000);
     * 制造商户付款凭证
     * 注意：商户凭证流水的校验只用金额对应上，还需要去资产管理-商户付款凭证详情里进行流水选择
     * */
    @Test
    public void test1(){
        System.out.println("插入cash_flow流水记录：");
        CashFlow cashFlow=new CashFlow();
        cashFlow.setCashFlowUuid(UUID.randomUUID().toString());
        cashFlow.setCashFlowChannelType(CashFlowChannelType.DirectBank);
        cashFlow.setHostAccountUuid("d0503298-e890-425a-4444444");
        cashFlow.setHostAccountNo("600000000001");
        cashFlow.setHostAccountName("云南国际信托有限公司");
        cashFlow.setCounterAccountNo("1001133419006708197");
        cashFlow.setCounterAccountName("上海拍拍贷金融信息服务有限公司");
        cashFlow.setAccountSide(AccountSide.DEBIT);
        cashFlow.setTransactionTime(new Date());
        cashFlow.setTransactionAmount(new BigDecimal(amount));
        cashFlow.setBankSequenceNo(UUID.randomUUID().toString());
        cashFlow.setRemark("戴智智-凭证重构");
        cashFlow.setIssuedAmount(new BigDecimal("0"));
        cashFlow.setAuditStatus(AuditStatus.CREATE);
        cashFlowService.save(cashFlow);

        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("制造商户凭证：");
        baseTestMethod.merchantVoucher(NewOuterInternat_command,uniqueId,repaymentNumber,totalAmount);
    }
    /**
     * 已测通
     * */
    @Test
    public void test2(){
        System.out.println("变更还款信息接口：");
        baseTestMethod.changeRepaymentInfo(OuterInternat_modifyUrl,uniqueId,"6214855712106520");
    }
    /**
    * 已测通
     * 注意：只有逾期的还款计划才可调用该接口
     * overDueFeeCalcDate：格式未知，需解决
    * */
    @Test
    public void test3(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("更新逾期费用明细接口：");
        baseTestMethod.updateOverdueDetails(OuterInternat_modifyUrl,"云信信2016-176-DK(C2017051916390952353687)号","ZC63066213163941888");
    }
    /**
     * 已测通
     * 描述：通过传入数据，在不变更还款计划的前提下，对还款计划的应收费用明细进行浮动变更
     * 数据显示位置：还款计划详情-费用浮动明细中
     * */
    @Test
    public void test4(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("浮动费用更改：");
        baseTestMethod.floatingCharges(ChangeUrl,amount,uniqueId,productCode,repaymentNumber);
    }

    /**
     * 已测通
     * */
    @Test
    public void test5(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId,uniqueId,totalAmount,repaymentNumber,repaymentNumber,productCode);
    }
    /**
     * 制造机构对账，可在资金管理-代收_第三方机构中查看
     * */
    @Test
    public void test6(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("扣款接口：");
        baseTestMethod.deductRepaymentPlan(deductUrl,repaymentNumber,uniqueId,productCode,amount,"1");
//        String merchantOrderNo=deductPlanService.getTradeUUidByContractNo(uniqueId);//扣款单的交易通道号
//        System.out.println("插入第三方对账文件：");
//        ThirdPartyAuditBill thirdPartyAuditBill=new ThirdPartyAuditBill();
//        thirdPartyAuditBill.setFinancialContractUuid("d2812bc5-5057-4a91-b3fd-9019506f0499");
//        thirdPartyAuditBill.setAuditBillUuid(UUID.randomUUID().toString());
//        thirdPartyAuditBill.setPaymentGateway(PaymentInstitutionName.UNIONPAYGZ);
//        thirdPartyAuditBill.setMerchantNo("001053110000001");
//        thirdPartyAuditBill.setSndLvlMerchantNo("uerber111");
//        thirdPartyAuditBill.setAccountSide(AccountSide.DEBIT);
//        thirdPartyAuditBill.setChannelSequenceNo("c6056ac6-1069-11e7-b8a2-525400dbb013");
//        thirdPartyAuditBill.setMerchantOrderNo(merchantOrderNo);//通道请求号，需要与扣款单相对应
//        thirdPartyAuditBill.setSettleDate(null);//格式可能不对
//        thirdPartyAuditBill.setReckonAccount(null);
//        thirdPartyAuditBill.setTransactionAmount(new BigDecimal("110"));
//        thirdPartyAuditBill.setServiceFee(new BigDecimal("0"));
//        thirdPartyAuditBill.setOrderCreateTime(new Date());
//        thirdPartyAuditBill.setTransactionTime(new Date());
//        thirdPartyAuditBill.setCounterAccountNo("counter_account_no");
//        thirdPartyAuditBill.setCounterAccountName("counter_account_name");
//        thirdPartyAuditBill.setCounterAccountAppendix69967221525733376("counter_account_appendix");
//        thirdPartyAuditBill.setRemark("remark");
//        thirdPartyAuditBill.setCreateTime(new Date());
//        thirdPartyAuditBill.setIssuedAmount(new BigDecimal("0.00"));
//        thirdPartyAuditBill.setAuditStatus(AuditStatus.CREATE);
//        thirdPartyAuditBillService.save(thirdPartyAuditBill);
    }
    /**
     * 已测通
     * 描述：将放款未成功的贷款合同异常中止
     * 注意：已经放款的合同不可取消,可对异常终止的合同进行取消
     * */
    @Test
    public void test7(){
        System.out.println("取消贷款接口：");
        baseTestMethod.cancalLoan(OuterInternat_command,productCode,"云信信2017-245(DM-2017050917425826105)号");
    }
   /**
     * 未测通
    * 描述：调用该接口，向贷后系统发送回购申请请求，支持批量处理。费用
     * */
    @Test
    public void test8(){
        String batchNo="H18900 LB1900069189321445376";
        String uniqueId="云信信2017-245(DM-2017050917425826105)号";
        System.out.println("回购申请接口:");
        baseTestMethod.repurchaseApplication(RepurchaseUrl,productCode,uniqueId,batchNo);
    }
    /**
     * 制造第三方凭证,已测通
     * 注意：此方法只在内网可用，外网找有权限的人插SQL
     * */
    @Test
    public void test9(){
        //String transactionRequestNo=uniqueId+"0"+"dzz";//交易请求号


        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("第三方凭证接口：");
        baseTestMethod.MakeVOucher(NewOuterInternat_command,transactionRequestNo,uniqueId,repaymentNumber,amount,productCode);
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
//        //thirdPartyTransactionRecord.setPaymentGateway(PaymentInstitutionName.UNIONPAYGZ);//0银联
//        thirdVoucherService.save(thirdPartyTransactionRecord);
    }
    /**
     * 已测通
     * */
    @Test
    public void test10(){
        System.out.println("申请提前还款接口：");//有
        baseTestMethod.applyPrepaymentPlan(uniqueId,totalAmount,amount,DateUtils.format(new Date(),"yyyy-MM-dd"));
    }
    /**
     * 已测通
     * */
    @Test
    public void test11(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId,uniqueId,totalAmount,repaymentNumber,repaymentNumber,productCode);
    }
    /**
     *未测通
     * 显示系统错误，需询问
     * 描述：描述：供贷前系统调用，合作方系统调用接口提交或者撤销付款凭证接口。
     * */
    @Test
    public void test12(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        System.out.println("主动付款凭证接口：");
        baseTestMethod.activePaymentVoucher(NewOuterInternat_command,uniqueId,totalAmount,repaymentNumber);
    }
    @Test
    public void test14(){
        System.out.println("申请提前还款接口：");
        baseTestMethod.applyPrepaymentPlan(uniqueId,totalAmount,amount,DateUtils.format(new Date(),"yyyy-MM-dd"));
        System.out.println("扣款接口：");
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        System.out.println("还款编号:"+repaymentNumber);
        baseTestMethod.deductRepaymentPlan(deductUrl,repaymentNumber,uniqueId,productCode,totalAmount,"1");
    }

    /**
     * 显示系统错误，需询问
     * */
    @Test
    public void test15(){
        System.out.println("变更还款计划接口:");
        baseTestMethod.changeRepayment(OuterInternat_modifyUrl,"wb1114523476","1500.00","1");
    }
}
