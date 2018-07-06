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
 * Created by dzz on 17-3-27.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class ThirdPartyPayVoucherStep3_outtime extends commandUrl {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private ThirdVoucherService thirdVoucherService;

    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号
    String transactionRequestNo=uniqueId+1;
    String productCode="G31700";
    String totalamount="10";
    String amount="10000";

    @Given("^有一个放款计划，对应有三期还款计划three$")
    public void 有一个放款计划_对应有三期还款计划() throws Throwable {
        // 开始放款
        baseTestMethod.makeLoan(productCode, uniqueId,totalamount,OuterInternat_command);//HA0300
        while(true){
            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
            if(executionStatus == 2){
                break;
            }
            Thread.sleep(20000);
        }
        //开始导入资产包,对应有三期还款计划
        baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,totalamount,productCode,uniqueId,"0",amount, DateUtils.format(new Date(),"yyyy-MM-dd"),"2017-8-23","2017-9-23");

    }

    @Given("^有一期还款计划到期了,还款人去第三方机构还款，并且产生一个第三方凭证three$")
    public void 有一期还款计划到期了_还款人去第三方机构还款_并且产生一个第三方凭证() throws Throwable {
        // 查询还款编号
        String RepaymentPlanNo=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
        // 开始制造第三方凭证
        baseTestMethod.MakeVOucher(NewOuterInternat_command,transactionRequestNo,uniqueId,RepaymentPlanNo,totalamount,productCode);

    }

    @When("^第三方交易机构的交易记录outtimethree$")
    public void 第三方交易机构的交易记录outtime() throws Throwable {
        // outtime插入第三方交易记录的流水记录
        //Thread.sleep(1000*60*10);//10m
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
        thirdVoucherService.save(thirdPartyTransactionRecord);
    }

    @Then("^凭证应该outtime，并且返回记录three$")
    public void 凭证应该outtime_并且返回记录() throws Throwable {
//        List<Map<String, Object>> list=thirdVoucherService.queryErrorMessage(transactionRequestNo);
//        String mess=(String) list.get(0).get("error_message");
//        Assert.assertEquals("第三方扣款凭证指令超时，请校验凭证数据",mess);
    }

}
