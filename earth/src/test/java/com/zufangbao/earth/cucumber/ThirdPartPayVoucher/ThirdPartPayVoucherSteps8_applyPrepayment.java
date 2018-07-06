package com.zufangbao.earth.cucumber.ThirdPartPayVoucher;

import com.demo2do.core.utils.JsonUtils;
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
 * Created by dzz on 17-4-10.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class ThirdPartPayVoucherSteps8_applyPrepayment extends commandUrl {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private ThirdVoucherService thirdVoucherService;

    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号
    String transactionRequestNo="72320644167000064";//交易请求号
    String productCode="G31700";
    String totalamount="1500";
    String amount="1500";
    @Given("^有一个放款计划，对应有三期还款计划eight$")
    public void 有一个放款计划_对应有三期还款计划eight() throws Throwable {
        // 开始放款
        System.out.println("开始放款:");
        baseTestMethod.makeLoan(productCode, uniqueId,totalamount,OuterInternat_command);
        while(true){
            Thread.sleep(20000);
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
        }
        //开始导入资产包,对应有3期还款计划
        System.out.println("开始导入资产包:");
        baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,totalamount,productCode,uniqueId,"0",amount, DateUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),2),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),3),"yyyy-MM-dd"));
        //申请提前全额还款
        System.out.println("申请提前还款:");
        baseTestMethod.applyPrepaymentPlan(uniqueId,totalamount,totalamount,DateUtils.format(DateUtils.addDays(new Date(),0),"yyyy-MM-dd"));
    }

    @Given("^有一期还款计划申请提前还款,还款人去第三方机构还款，并且产生一个第三方凭证eight$")
    public void 有一期还款计划申请提前还款_还款人去第三方机构还款_并且产生一个第三方凭证eight() throws Throwable {
        // 开始制造第三方凭证
        System.out.println("开始制造第三方凭证:");
        baseTestMethod.MakeVOucher2(NewOuterInternat_command,transactionRequestNo,uniqueId,productCode,totalamount,amount);
    }

    @When("^获取第三方交易机构的交易记录,第三方凭证和交易记录进行校验eight$")
    public void 获取第三方交易机构的交易记录_第三方凭证和交易记录进行校验eight() throws Throwable {
        // 插入第三方交易记录的流水记录
            System.out.println("插入第三方交易记录的流水记录:");
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
            thirdVoucherService.save(thirdPartyTransactionRecord);
            String param =  JsonUtils.toJsonString(thirdPartyTransactionRecord);
            System.out.println(param);
    }

    @Then("^校验应该自动进行，检验是否能执行eight$")
    public void 校验应该自动进行_检验是否能执行eight() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

}
