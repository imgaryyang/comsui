package com.zufangbao.earth.cucumber.ThirdPartPayVoucher;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import com.zufangbao.sun.entity.account.InstitutionReconciliationAuditStatus;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.barclays.BusinessProcessStatus;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.service.ThirdVoucherService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dzz on 17-3-23.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class ThirdPartPayVoucherSteps1 extends commandUrl {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private ThirdVoucherService thirdVoucherService;


    BaseTestMethod baseTestMethod=new BaseTestMethod();
        String uniqueId=UUID.randomUUID().toString();//合同编号
        String transactionRequestNo="S170606000005";//交易请求号
        String productCode="G31700";
        String totalamount="10";
        String amount="10000";
//        String productCode="G31700";

    @Given("^有一个放款计划，对应有三期还款计划one$")
    public void 有一个放款计划_对应有三期还款计划() throws Throwable {
        //G31700
        System.out.println("开始放款");
        baseTestMethod.makeLoan(productCode, uniqueId,amount,OuterInternat_command);
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            Thread.sleep(20000);
        }
        //开始导入资产包,对应有三期还款计划
        System.out.println("开始导入资产包");
        baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,amount,productCode,uniqueId,"0",amount, DateUtils.format(new Date(),"yyyy-MM-dd"),"2017-8-23","2017-9-23");
    }

    @Given("^有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证one$")
    public void 有一期还款计划到期了_还款人去第三方机构还款_并且产生一个第三方凭证() throws Throwable {
        // 查询还款编号
        System.out.println("开始查询还款计划");
        String RepaymentPlanNo=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
      // 开始制造第三方凭证
        System.out.println("开始制造第三方凭证");

        baseTestMethod.MakeVOucher(NewOuterInternat_command,transactionRequestNo,uniqueId,RepaymentPlanNo,amount,productCode);
    }

    @When("^获取第三方交易机构的交易记录,第三方凭证和交易记录进行校验one$")
    public void 获取第三方交易机构的交易记录_第三方凭证和交易记录进行校验() throws Throwable {
        // 插入第三方交易记录的流水记录
        System.out.println(" 插入第三方交易记录的流水记录");
        ThirdPartyTransactionRecord thirdPartyTransactionRecord=new ThirdPartyTransactionRecord();
        thirdPartyTransactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
        thirdPartyTransactionRecord.setBusinessProcessStatus(BusinessProcessStatus.SUCCESS);
        thirdPartyTransactionRecord.setFinancialContractUuid(productCode);
        thirdPartyTransactionRecord.setAccountSide(com.zufangbao.sun.ledgerbook.AccountSide.DEBIT);
        thirdPartyTransactionRecord.setMerchantOrderNo(transactionRequestNo);//凭证交易请求号
        thirdPartyTransactionRecord.setTransactionAmount(new BigDecimal(totalamount));//amount
        thirdPartyTransactionRecord.setTransactionTime(new Date());
        thirdPartyTransactionRecord.setAuditStatus(InstitutionReconciliationAuditStatus.CREATE);
        thirdPartyTransactionRecord.setPayTime(new Date());
        //thirdPartyTransactionRecord.setPaymentGateway(PaymentInstitutionName.UNIONPAYGZ);//0银联
        thirdVoucherService.save(thirdPartyTransactionRecord);
    }

    @Then("^校验应该自动进行，并且返回记录one$")
    public void 校验应该自动进行_并且返回记录() throws Throwable {
//        int auto_result_status=thirdVoucherService.queryAuto_result_Status(transactionRequestNo);
//        Assert.assertEquals(2,auto_result_status);//ping zhang
    }





}
