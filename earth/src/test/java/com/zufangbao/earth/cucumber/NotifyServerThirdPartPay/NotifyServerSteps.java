package com.zufangbao.earth.cucumber.NotifyServerThirdPartPay;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import com.zufangbao.sun.yunxin.service.ThirdVoucherService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by dzz on 17-4-24.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class NotifyServerSteps extends commandUrl {

    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private ThirdVoucherService thirdVoucherService;


    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号

    @Given("^根据交易请求号从宝付网关获取交易流水$")
    public void 根据交易请求号从宝付网关获取交易流水() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^根据交易流水记录制造第三方凭证$")
    public void 根据交易流水记录制造第三方凭证() throws Throwable {
        System.out.println("开始放款,合同编号："+uniqueId);
        baseTestMethod.makeLoan("G31700", uniqueId,"1500",OuterInternat_command);
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            Thread.sleep(20000);
        }
        //开始导入资产包,对应有三期还款计划
        System.out.println("开始导入资产包");
        baseTestMethod.importAssetPackage2(OuterInternat_modifyUrl,"1500","G31700",uniqueId,"0","500","2017-4-30","2017-5-23","2017-6-23");
        System.out.println("3、根据流水记录制造第三方凭证：");
        baseTestMethod.MakeVOucher2(NewOuterInternat_command,"mSDK4LmZu6yil7NZYCK",uniqueId,"","","");

    }

    @Then("^校验应该自动进行,并且都能核销$")
    public void 校验应该自动进行_并且都能核销() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

}
