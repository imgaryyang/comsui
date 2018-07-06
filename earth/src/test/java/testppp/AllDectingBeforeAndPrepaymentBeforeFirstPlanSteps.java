package testppp;

import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml"})
public class AllDectingBeforeAndPrepaymentBeforeFirstPlanSteps {
    String result = "";
    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), -1));
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    private String productCode = "G31700";
    private String uniqueId = "FANT8888";
    private String totalAmount = "1500";
    private String amount = "500";

    @Given("^有三期未到期的还款计划B$")
    public void 有三期未到期的还款计划b() throws Throwable {
        System.out.println("导入资产包");
        prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
        throw new PendingException();
    }

    @When("^对第一期未到期的还款计划做全额提前划拨B$")
    public void 对第一期未到期的还款计划做全额提前划拨b() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
        prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, amount, "0");
        throw new PendingException();
    }

    @When("^当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之前B$")
    public void 当第一期还在扣款中的时候对后面的还款计划做提前还款且申请日期在第一期之前b() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, "1000", "1000", applyDate);
        throw new PendingException();
    }

    @Then("^返回结果\"([^\"]*)\"且这个贷款合同下可查询出\"([^\"]*)\"条还款计划B$")
    public void 返回结果_且这个贷款合同下可查询出_条还款计划B(String repaymentPlanCount, String result1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        result1 = result;
        repaymentPlanCount = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
        Assert.assertEquals("{\"code\":21014,\"message\":\"提前还款日期错误!\"}", result1);
        Assert.assertEquals("3", repaymentPlanCount);
        throw new PendingException();
    }


}
