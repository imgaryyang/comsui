package com.zufangbao.earth.cucumber.ThirdPartPayVoucher;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import com.zufangbao.sun.entity.account.InstitutionReconciliationAuditStatus;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dzz on 17-3-29.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class ThirdPartPayVoucherSteps6_firstRequestOuttime extends commandUrl {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private ThirdVoucherService thirdVoucherService;

    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号
    String transactionRequestNo=uniqueId+1;//交易请求号
    String productCode="G31700";
    @Given("^有一个放款计划，对应有三期还款计划six$")
    public void 有一个放款计划_对应有三期还款计划() throws Throwable {
        // 开始放款
        System.out.println("开始放款:");
        baseTestMethod.makeLoan("G31700", uniqueId,"1500",OuterInternat_command);
        while(true){
            Thread.sleep(20000);
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
        }
        //开始导入资产包,对应有三期还款计划
        System.out.println("开始导入资产包:");
        baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,"1500","G31700",uniqueId,"1","1500","2017-4-30","2017-5-23","2017-6-23");

    }

    @Given("^有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证,but outtimesix$")
    public void 有一期还款计划到期了_还款人去第三方机构还款_并且产生一个第三方凭证_but_outtime() throws Throwable {
        // 查询还款编号
        System.out.println("查询还款编号:");
        String RepaymentPlanNo=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        // 开始制造第三方凭证
        System.out.println("开始制造第三方凭证:");
        baseTestMethod.MakeVOucher(NewOuterInternat_command,transactionRequestNo,uniqueId,RepaymentPlanNo,"1500",productCode);
    }

    @When("^ten mintues,again产生一个第三方凭证six$")
    public void ten_mintues_again产生一个第三方凭证() throws Throwable {
        //sleep 10 mintues
        Thread.sleep(15*60*1000);
        // 查询还款编号
        System.out.println("again查询还款编号:");
        String RepaymentPlanNo=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        // 开始制造第三方凭证
        System.out.println("again开始制造第三方凭证:");
        baseTestMethod.MakeVOucher(NewOuterInternat_command,transactionRequestNo,uniqueId,RepaymentPlanNo,"1500",productCode);
        // 插入第三方交易记录的流水记录
        System.out.println("插入第三方交易记录的流水记录:");
        ThirdPartyTransactionRecord thirdPartyTransactionRecord=new ThirdPartyTransactionRecord();
        thirdPartyTransactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
        thirdPartyTransactionRecord.setBusinessProcessStatus(BusinessProcessStatus.SUCCESS);
        thirdPartyTransactionRecord.setFinancialContractUuid("G31700");
        thirdPartyTransactionRecord.setAccountSide(com.zufangbao.sun.ledgerbook.AccountSide.DEBIT);
        thirdPartyTransactionRecord.setMerchantOrderNo(transactionRequestNo);//凭证交易请求号
        thirdPartyTransactionRecord.setTransactionAmount(new BigDecimal("1500"));//amount
        thirdPartyTransactionRecord.setTransactionTime(new Date());
        thirdPartyTransactionRecord.setAuditStatus(InstitutionReconciliationAuditStatus.CREATE);
        thirdPartyTransactionRecord.setPayTime(new Date());
        thirdVoucherService.save(thirdPartyTransactionRecord);
        String param =  JsonUtils.toJsonString(thirdPartyTransactionRecord);
        System.out.println(param);
    }

    @Then("^校验应该自动进行，并且返回记录six$")
    public void 校验应该自动进行_并且返回记录() throws Throwable {
        List<Map<String, Object>> list=thirdVoucherService.queryErrorMessage(transactionRequestNo);
        String mess=(String) list.get(0).get("error_message");
        //Assert.assertEquals("交易凭证金额与流水金额不相等",mess);
    }

}
