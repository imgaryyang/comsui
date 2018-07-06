package testppp;

import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.PaymentStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml"})
public class AllDeductedBeforeSuccessAndPrepaymentSteps {

    String result = "";
    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private PrepaymentApplicationService prepaymentApplicationService;
    private String productCode = "G31700";
    private String uniqueId = "FANT8888";
    private String totalAmount = "1500";
    private String amount = "500";

    @Given("^有三期未到期的还款计划A$")
    public void 有三期未到期的还款计划a() throws Throwable {


        System.out.println("导入资产包");
        //导入资产包
        //repaymentAccountNo 0:扣款成功 7:扣款失败 3:处理中
        prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);


    }

    @When("^对第一期未到期的还款计划做全额提前划拨A$")
    public void 对第一期未到期的还款计划做全额提前划拨a() throws Throwable {
        /*repaymentAccountNo:
		     0:提前划拨
		     1:正常扣款
		     2:逾期扣款
		*/
        String repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
        prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, amount, "0");
        while (true) {
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentNumber);
            PaymentStatus paymentStatus = assetSet.getPaymentStatus();
            if (PaymentStatus.SUCCESS == paymentStatus) {
                break;
            }
            Thread.sleep(10000);
        }
    }


    @When("^然后对后面的还款计划做提前还款A$")
    public void 然后对后面的还款计划做提前还款a() throws Throwable {
        result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", DateUtils.format(new Date()));

    }

    @Then("^这个贷款合同下可查询出\"([^\"]*)\"条还款计划A$")
    public void 这个贷款合同下可查询出_条还款计划A(String repaymentPlanCount) throws Throwable {
        repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
        String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
        AssetSetActiveStatus activeStatus = assetSet.getActiveStatus();
        Assert.assertEquals(AssetSetActiveStatus.OPEN, activeStatus);
        Assert.assertEquals("2", repaymentPlanCount);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}", result);
    }


}
