package com.zufangbao.earth.cucumber.QueryThirdPartPayVoucher;

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
import java.util.UUID;

/**
 * Created by dzz on 17-3-30.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class queryThirdPartPayVoucher1_haveBatchRequest extends commandUrl {

    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private ThirdVoucherService thirdVoucherService;


    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号
    String transactionRequestNo=uniqueId+1;//jiao yi qing qiu bian hao

    @Given("^有一个放款计划，对应有三期还款计划one$")
    public void 有一个放款计划_对应有三期还款计划one() throws Throwable {
        // 开始放款
        baseTestMethod.makeLoan("G31700", uniqueId,"1500",OuterInternat_command);
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            Thread.sleep(20000);
        }
        //开始导入资产包,对应有三期还款计划
        baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,"1500","G31700",uniqueId,"0","1500","2017-4-30","2017-5-23","2017-6-23");

    }

    @Given("^有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证one$")
    public void 有一期还款计划到期了_还款人去第三方机构还款_并且产生一个第三方凭证one() throws Throwable {
        // 查询还款编号
        String RepaymentPlanNo=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        // 开始制造第三方凭证
        baseTestMethod.MakeVOucher(NewOuterInternat_command,transactionRequestNo,uniqueId,RepaymentPlanNo,"1500","");

    }

    @Given("^获取第三方交易机构的交易记录,第三方凭证和交易记录进行校验one$")
    public void 获取第三方交易机构的交易记录_第三方凭证和交易记录进行校验one() throws Throwable {
        // 插入第三方交易记录的流水记录
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
    }

    @When("^give BatchRequest to query one$")
    public void give_BatchRequest_to_query_one() throws Throwable {
        //交易请求编号查询
        System.out.println("交易请求编号查询:");
//        baseTestMethod.queryThirdVoucherBybatchRequest(OuterInternat_queryUrl,transactionRequestNo,"");
    }

    @Then("^校验应该自动进行，并且返回记录one$")
    public void 校验应该自动进行_并且返回记录one() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

}
