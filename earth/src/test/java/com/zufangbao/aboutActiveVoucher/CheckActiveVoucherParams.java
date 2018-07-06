package com.zufangbao.aboutActiveVoucher;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.PaymentStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailCheckState;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class CheckActiveVoucherParams {
    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private VoucherService voucherService;

    RefactorMethod refactorMethod = new RefactorMethod();

    private String uniqueId = "";
    String productCode = "CS0001";
    String totalAmount = "30000";
    String firstRepaymentPlan = "";
    String interest = "";

    @Before
    public void setUp() {
        refactorMethod.deleteAllCashFlow();
        uniqueId = UUID.randomUUID().toString();
        String repaymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操";
        interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount, productCode, uniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        firstRepaymentPlan = refactorMethod.queryRepaymentPlan(productCode,uniqueId,0);
    }

    /**
     * 主动付款凭证接口参数校验-交易类型[transactionType]为空
     * @throws Exception
     */
    @Test
    public void checkParams1() throws Exception{
        String transactionType = "";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("无效参数-transactionType:交易类型[transactionType](0:提交，1:撤销)（必填）");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-交易类型[transactionType]为非数字字符串
     * @throws Exception
     */
    @Test
    public void checkParams2() throws Exception{
        String transactionType = "das";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("无效参数-transactionType");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-凭证类型［voucherType]为空
     * @throws Exception
     */
    @Test
    public void checkParams3() throws Exception{
        String transactionType = "0";
        String voucherType = "";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证类型［voucherType］，不能为空");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-凭证类型［voucherType]为不在5，6之内
     * @throws Exception
     */
    @Test
    public void checkParams4() throws Exception{
        String transactionType = "0";
        String voucherType = "3";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证类型［voucherType］错误");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-交易类型[voucherType]为非数字字符串
     * @throws Exception
     */
    @Test
    public void checkParams5() throws Exception{
        String transactionType = "0";
        String voucherType = "sad";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("无效参数-voucherType");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-付款账户流水号［bankTransactionNo］为空
     * @throws Exception
     */
    @Test
    public void checkParams6() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = "";
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("付款账户流水号［bankTransactionNo］，不能为空");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-打款流水号［bankTransactionNo］重复
     * @throws Exception
     */
    @Test
    public void checkParams7() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        cashFlowService.insertCashFlow(uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        if(!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        String secondRepaymentPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,1);
        String result2 = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, secondRepaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result2.contains("打款流水号已关联凭证");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-信托产品代码［financialContractNo］为空
     * @throws Exception
     */
    @Test
    public void checkParams8() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = "";
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("信托产品代码［financialContractNo］，不能为空");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-信托产品代码［financialContractNo］不存在
     * @throws Exception
     */
    @Test
    public void checkParams9() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode+1;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("信托产品代码错误");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-信托产品代码［financialContractNo］与 uniqueId 不匹配
     * @throws Exception
     */
    @Test
    public void checkParams10() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = "G31700";
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = !result.contains("成功");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-uniqueId 和 contractNo 都不填
     * @throws Exception
     */
    @Test
    public void checkParams11() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = "";
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("请选填其中一种编号[uniqueId，contractNo]");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-uniqueId 不存在
     * @throws Exception
     */
    @Test
    public void checkParams12() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId+1;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("贷款合同不存在");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证，冒烟测试-单条明细-填contractNo不填uniqueId
     * @throws Exception
     */
    @Test
    public void checkParams13() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = "";
        String contractNo = this.uniqueId;
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }

    /**
     * 主动付款凭证，冒烟测试-单条明细-填uniqueId不填contractNo
     * @throws Exception
     */
    @Test
    public void checkParams14() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }


    /**
     * 主动付款凭证接口参数校验-uniqueId 和 contractNo 都填
     * @throws Exception
     */
    @Test
    public void checkParams15() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = this.uniqueId;
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("请选填其中一种编号[uniqueId，contractNo]");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-uniqueId 和 contractNo 都填, uniqueId正确，contractNo错误
     * @throws Exception
     */
    @Test
    public void checkParams16() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = this.uniqueId+1;
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("请选填其中一种编号[uniqueId，contractNo]");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-repayScheduleNo 和 repaymentPlanNo 都不填
     * @throws Exception
     */
    @Test
    public void checkParams17() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = "";
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("至少选填一种编号[repayScheduleNo，repaymentPlanNo,currentPeriod]");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-repaymentPlanNo 错误
     * @throws Exception
     */
    @Test
    public void checkParams18() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = this.firstRepaymentPlan+1;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("不存在该有效还款计划或者还款计划不在贷款合同内");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-repaymentPlanNo 与 repayScheduleNo 都填且正确
     * @throws Exception
     */
    @Test
    public void checkParams19() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = this.firstRepaymentPlan;
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(repayScheduleNo == null || repayScheduleNo == ""){
            throw new Exception("无商户还款计划编号");
        }
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }

    /**
     * 主动付款凭证接口参数校验-只填 repayScheduleNo
     * @throws Exception
     */
    @Test
    public void checkParams20() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstRepaymentPlan);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("无商户还款计划编号");
        }
        cashFlowService.insertCashFlow(uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }

    /**
     * 主动付款凭证接口参数校验-repayScheduleNo 错误
     * @throws Exception
     */
    @Test
    public void checkParams21() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo()+1;
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("不存在该有效还款计划或者还款计划不在贷款合同内");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-repayScheduleNo 与 uniqueId 都填且 repayScheduleNo 错误 repaymentPlanNo 正确
     * @throws Exception
     */
    @Test
    public void checkParams22() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = this.firstRepaymentPlan;
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo()+1;
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result.contains("不存在该有效还款计划或者还款计划不在贷款合同内");
        Assert.assertEquals(true, outcome);
    }

    /**
     * 主动付款凭证接口参数校验-repayScheduleNo 与 uniqueId 都填且 repayScheduleNo 正确 repaymentPlanNo 错误
     * @throws Exception
     */
    @Test
    public void checkParams23() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = this.firstRepaymentPlan+1;
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        if (null == assetSet){
            throw new Exception("还款计划不存在");
        }
        String repayScheduleNo = assetSet.getOuterRepaymentPlanNo();
        if(null == repayScheduleNo || "".equals(repayScheduleNo)){
            throw new Exception("无商户还款计划编号");
        }
        cashFlowService.insertCashFlow(uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }

    /**
     * 主动付款凭证接口参数校验-凭证金额与明细总金额不匹配
     * @throws Exception
     */
    @Test
    public void checkParams24() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = String.valueOf(Double.valueOf(detailAmount)+Double.valueOf("20"));
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证金额与明细总金额不匹配");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-利息大于还款计划利息
     * @throws Exception
     */
    @Test
    public void checkParams25() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = "1000";
        String interest = "30";
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("主动付款凭证明细字段金额有误");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-利息为负数
     * @throws Exception
     */
    @Test
    public void checkParams26() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = "1000";
        String interest = "-30";
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证明细金额格式错误");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-本金为负数
     * @throws Exception
     */
    @Test
    public void checkParams27() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = "-1000";
        String interest = "20";
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证明细金额格式错误");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-本金大于还款计划本金
     * @throws Exception
     */
    @Test
    public void checkParams28() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = "100000";
        String interest = "20";
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("主动付款凭证金额错误");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 主动付款凭证接口参数校验-凭证金额为负数
     * @throws Exception
     */
    @Test
    public void checkParams29() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = "100000";
        String interest = "20";
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = "-"+detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证金额［voucherAmount］，必需大于0.00");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-凭证金额为0
     * @throws Exception
     */
    @Test
    public void checkParams30() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = "0";
        String interest = "0";
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证金额［voucherAmount］，必需大于0.00");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-凭证付款名与流水账户名不一致
     * @throws Exception
     */
    @Test
    public void checkParams31() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String cashFlowPayAccountName = "秦曹";
        cashFlowService.insertCashFlow(uniqueId,productCode,paymentAccountNo,cashFlowPayAccountName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证对应流水不存在或已提交");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-凭证付款号与流水账户号不一致
     * @throws Exception
     */
    @Test
    public void checkParams32() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String cashFlowPayAccountNo = "6217857600016839123";
        cashFlowService.insertCashFlow(uniqueId,productCode,cashFlowPayAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证对应流水不存在或已提交");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-凭证付款金额与流水金额不一致
     * @throws Exception
     */
    @Test
    public void checkParams33() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String cashFlowAmount = String.valueOf(Double.valueOf(detailAmount)+Double.valueOf("200"));
        cashFlowService.insertCashFlow(uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(cashFlowAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("凭证对应流水不存在或已提交");
        Assert.assertEquals(true,outcome);
    }


    /**
     * 主动付款凭证接口参数校验-收款账户不是贷款合同的回款账户
     * @throws Exception
     */
    @Test
    public void checkParams34() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "1234567890";
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        boolean outcome = result.contains("收款账户错误，收款账户不是贷款合同的回款账户");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证接口参数校验-匹配的流水不是统一信托合同的
     * @throws Exception
     */
    @Test
    public void checkParams35() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String cashFlowProductCode = "G32000";
        cashFlowService.insertCashFlow(uniqueId,cashFlowProductCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,contractNo,repaymentPlanNo,repayScheduleNo,detailAmount,principal,
                interest,paymentName,paymentAccountNo,receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus =null;
        for(int i=0;i<3;i++){
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        Assert.assertEquals(SourceDocumentDetailStatus.UNSUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.CREATE,auditStatus);
    }

    /**
     * 主动付款凭证业务校验-一笔还款计划分两笔凭证还款
     * @throws Exception
     */
    @Test
    public void checkParams36() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String firstBankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String firstPrincipal = String.valueOf(Double.valueOf(totalAmount)/6);
        String firstInterest = String.valueOf((Double.valueOf(this.interest)/2));
        String firstDetailAmount = String.valueOf(Double.valueOf(firstPrincipal) + Double.valueOf(firstInterest)*4);
        String firstVoucherAmount = firstDetailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String secondBankTransactionNo = UUID.randomUUID().toString();
        String cashFlowUuid = UUID.randomUUID().toString();
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(firstVoucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, firstBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, firstPrincipal,
                firstInterest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(this.uniqueId);
            AuditStatus auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }

        Voucher voucher = voucherService.get_voucher_by_secondNo(firstBankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailStatus.SUCCESS != voucher.getStatus()){
            throw new Exception("凭证校验状态有误");
        }

        cashFlowService.insertCashFlow(cashFlowUuid,productCode,paymentAccountNo,paymentName,new BigDecimal(firstVoucherAmount));
        String result2 = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, secondBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, firstPrincipal,
                firstInterest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result2.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher2 = voucherService.get_voucher_by_secondNo(secondBankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher2.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid2 = voucher2.getCashFlowUuid();
        if (null == cashFlowUuid2 || "".equals(cashFlowUuid2)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid2);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher2 = voucherService.get_voucher_by_secondNo(secondBankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher2.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }

    /**
     * 主动付款凭证业务校验-凭证明细本金大于应还未还金额
     * @throws Exception
     */
    @Test
    public void checkParams37() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String firstBankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String firstPrincipal = String.valueOf(Double.valueOf(totalAmount)/6);
        String firstInterest = String.valueOf((Double.valueOf(this.interest)/2));
        String firstDetailAmount = String.valueOf(Double.valueOf(firstPrincipal) + Double.valueOf(firstInterest)*4);
        String firstVoucherAmount = firstDetailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(firstVoucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, firstBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, firstPrincipal,
                firstInterest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(this.uniqueId);
            AuditStatus auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        Voucher voucher = voucherService.get_voucher_by_secondNo(firstBankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailStatus.SUCCESS != voucher.getStatus()){
            throw new Exception("凭证校验状态有误");
        }
        String secondBankTransactionNo = UUID.randomUUID().toString();
        String secondPrincipal = "5040";
        String secondInterest = "0";
        String result2 = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, secondBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, secondPrincipal,
                secondInterest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result2.contains("主动付款凭证明细字段金额有误");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证业务校验-凭证明细利息大于应还未还金额
     * @throws Exception
     */
    @Test
    public void checkParams38() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String firstBankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String firstPrincipal = String.valueOf(Double.valueOf(totalAmount)/6);
        String firstInterest = String.valueOf((Double.valueOf(this.interest)/2));
        String firstDetailAmount = String.valueOf(Double.valueOf(firstPrincipal) + Double.valueOf(firstInterest)*4);
        String firstVoucherAmount = firstDetailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(firstVoucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, firstBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, firstPrincipal,
                firstInterest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(this.uniqueId);
            AuditStatus auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        Voucher voucher = voucherService.get_voucher_by_secondNo(firstBankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailStatus.SUCCESS != voucher.getStatus()){
            throw new Exception("凭证校验状态有误");
        }
        String secondBankTransactionNo = UUID.randomUUID().toString();
        String secondPrincipal = "4960";
        String secondInterest = "20";
        String result2 = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, secondBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, secondPrincipal,
                secondInterest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result2.contains("主动付款凭证明细字段金额有误");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证业务校验-凭证金额大于该还款计划应还未还金额
     * @throws Exception
     */
    @Test
    public void checkParams39() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String firstBankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String firstPrincipal = String.valueOf(Double.valueOf(totalAmount)/6);
        String firstInterest = this.interest;
        String firstDetailAmount = String.valueOf(Double.valueOf(firstPrincipal) + Double.valueOf(firstInterest)*4);
        String firstVoucherAmount = firstDetailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(firstVoucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, firstBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, firstPrincipal,
                firstInterest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(this.uniqueId);
            AuditStatus auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(firstBankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailStatus.SUCCESS != voucher.getStatus()){
            throw new Exception("凭证校验状态有误");
        }
        String secondBankTransactionNo = UUID.randomUUID().toString();
        String result2 = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, secondBankTransactionNo, firstVoucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, firstDetailAmount, firstPrincipal,
                firstInterest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result2.contains("主动付款凭证金额错误");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证业务校验-对还款成功的还款计划提交凭证
     * @throws Exception
     */
    @Test
    public void checkParams40() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(this.firstRepaymentPlan);
        if (PaymentStatus.SUCCESS != assetSet.getPaymentStatus() || SourceDocumentDetailStatus.SUCCESS != voucher.getStatus()){
            throw new Exception("凭证未核销");
        }
        String result2 = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result2.contains("明细金额应不大于还款计划应还未还金额");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 主动付款凭证业务校验-对作废的还款计划提交凭证
     * @throws Exception
     */
    @Test
    public void checkParams41() throws Exception{
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String contractNo = "";
        String repaymentPlanNo = firstRepaymentPlan;
        String repayScheduleNo = "";
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = detailAmount;
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String receivableAccountNo = "";
        String secondPlanDate = DateUtils.format(DateUtils.addDays(new Date(),1));
        String result = refactorMethod.modifyRepaymentPlanForOne(uniqueId,totalAmount,interest,secondPlanDate,1,"3",repayScheduleNo,"");
        if(!result.contains("成功")){
            throw new Exception("变更还款计划失败");
        }
        String result2 = refactorMethod.activePaymentVoucherForOneDetail(transactionType, voucherType, bankTransactionNo, voucherAmount, financialContractNo,
                uniqueId, contractNo, repaymentPlanNo, repayScheduleNo, detailAmount, principal,
                interest, paymentName, paymentAccountNo, receivableAccountNo);
        boolean outcome = result2.contains("不存在该有效还款计划或者还款计划不在贷款合同内");
        Assert.assertEquals(true,outcome);
    }

    /**
     * 冒烟测试-一笔凭证包含两条明细（不同贷款合同，同一贷款人）
     * @throws Exception
     */
    @Test
    public void checkParams42() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String firstPlanNo = this.firstRepaymentPlan;
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = String.valueOf(Double.valueOf(detailAmount)*2);
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";

        String secondUniqueId = UUID.randomUUID().toString();
        interest = "20";
        String loanCustomerNo = "11111117";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));

        refactorMethod.importAssetPackage(totalAmount, productCode, secondUniqueId, paymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, paymentName);
        String secondPlanNo = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForTwoDetails(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                                                                        uniqueId,firstPlanNo,detailAmount,principal,interest,
                                                                        secondUniqueId,secondPlanNo,paymentName,paymentAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode(secondPlanNo);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet2.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }

    /**
     * 冒烟测试-一笔凭证包含两条明细（同一贷款合同）
     * @throws Exception
     */
    @Test
    public void checkParams43() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String firstPlanNo = this.firstRepaymentPlan;
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = String.valueOf(Double.valueOf(detailAmount)*2);
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String secondPlanNo = refactorMethod.queryRepaymentPlan(productCode,uniqueId,1);
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForTwoDetails(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,firstPlanNo,detailAmount,principal,interest,
                uniqueId,secondPlanNo,paymentName,paymentAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode(secondPlanNo);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet2.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }


    /**
     * 冒烟测试-一笔凭证包含两条明细（不同贷款合同，loanCustomerName和loanCustomerNo不同，repaymentAccountNo相同）
     * @throws Exception
     */
    @Test
    public void checkParams44() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String firstPlanNo = this.firstRepaymentPlan;
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = String.valueOf(Double.valueOf(detailAmount)*2);
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String loanCustomerName = "秦操曹";
        String secondUniqueId = UUID.randomUUID().toString();
        interest = "20";
        String loanCustomerNo = "11111234";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        refactorMethod.importAssetPackage(totalAmount, productCode, secondUniqueId, paymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        String secondPlanNo = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForTwoDetails(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,firstPlanNo,detailAmount,principal,interest,
                secondUniqueId,secondPlanNo,paymentName,paymentAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode(secondPlanNo);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(PaymentStatus.UNUSUAL, assetSet2.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.UNSUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.CLOSE,auditStatus);
    }

    /**
     * 冒烟测试-一笔凭证包含两条明细（不同贷款合同，loanCustomerName、loanCustomerNo和repaymentAccountNo都不同）
     * @throws Exception
     */
    @Test
    public void checkParams45() throws Exception {
        String transactionType = "0";
        String voucherType = "5";
        String bankTransactionNo = UUID.randomUUID().toString();
        String financialContractNo = productCode;
        String uniqueId = this.uniqueId;
        String firstPlanNo = this.firstRepaymentPlan;
        String principal = String.valueOf(Double.valueOf(totalAmount)/3);
        String interest = this.interest;
        String detailAmount = String.valueOf(Double.valueOf(principal) + Double.valueOf(interest)*4);
        String voucherAmount = String.valueOf(Double.valueOf(detailAmount)*2);
        String paymentName = "秦操";
        String paymentAccountNo = "6217857600016839330";
        String repaymentAccountNo = "6217857600016839321";
        String loanCustomerName = "秦操曹";
        String secondUniqueId = UUID.randomUUID().toString();
        interest = "20";
        String loanCustomerNo = "11111234";
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), -1));
        String secondPlanDate = DateUtils.format(new Date());
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        refactorMethod.importAssetPackage(totalAmount, productCode, secondUniqueId, repaymentAccountNo, interest, loanCustomerNo,
                firstPlanDate, secondPlanDate, thirdPlanDate, loanCustomerName);
        String secondPlanNo = refactorMethod.queryRepaymentPlan(productCode,secondUniqueId,0);
        cashFlowService.insertCashFlow(this.uniqueId,productCode,paymentAccountNo,paymentName,new BigDecimal(voucherAmount));
        String result = refactorMethod.activePaymentVoucherForTwoDetails(transactionType,voucherType,bankTransactionNo,voucherAmount,financialContractNo,
                uniqueId,firstPlanNo,detailAmount,principal,interest,
                secondUniqueId,secondPlanNo,paymentName,paymentAccountNo);
        if (!result.contains("成功")){
            throw new Exception("凭证提交失败");
        }
        Voucher voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        if(null == voucher){
            throw new Exception("凭证不存在");
        }
        if(SourceDocumentDetailCheckState.CHECK_SUCCESS != voucher.getCheckState()){
            throw new Exception("凭证校验状态有误");
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        if (null == cashFlowUuid || "".equals(cashFlowUuid)){
            throw new Exception("凭证中无流水");
        }
        AuditStatus auditStatus;
        while (true) {
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
            auditStatus = cashFlow.getAuditStatus();
            if(AuditStatus.CREATE != auditStatus){
                break;
            }
            Thread.sleep(1000l);
        }
        Thread.sleep(3000l);
        voucher = voucherService.get_voucher_by_secondNo(bankTransactionNo);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(firstPlanNo);
        AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode(secondPlanNo);
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet2.getPaymentStatus());
        Assert.assertEquals(SourceDocumentDetailStatus.SUCCESS,voucher.getStatus());
        Assert.assertEquals(AuditStatus.ISSUED,auditStatus);
    }
}
