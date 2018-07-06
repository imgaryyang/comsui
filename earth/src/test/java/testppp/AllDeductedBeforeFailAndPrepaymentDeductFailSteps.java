package testppp;

import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
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
public class AllDeductedBeforeFailAndPrepaymentDeductFailSteps {

    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
    String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
    String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
    String repaymentPlanCountBeforeDeduct = "";
    String repaymentPlanCountAfterDeduct = "";
    String result = "";
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

    @Given("^有三期未到期的还款计划F$")
    public void 有三期未到期的还款计划f() throws Throwable {
        prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "7", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
        throw new PendingException();
    }

    @Given("^对第一期未到期的还款计划做全额提前划拨且扣款失败F$")
    public void 对第一期未到期的还款计划做全额提前划拨且扣款失败f() throws Throwable {
        String repaymentNumber = prepaymentCucumberMethod.queryFirstRepaymentPlan(uniqueId);
        prepaymentCucumberMethod.deductRepaymentPlan(repaymentNumber, uniqueId, productCode, amount, "0");
        while (true) {
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentNumber);
            ExecutingStatus excutingStatus = assetSet.getExecutingStatus();
            if (ExecutingStatus.UNEXECUTED == excutingStatus) {
                break;
            }
            Thread.sleep(10000);
        }
        throw new PendingException();
    }

    @When("^对所有的还款计划做提前还款F$")
    public void 对所有的还款计划做提前还款f() throws Throwable {
        result = prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId, totalAmount, totalAmount, DateUtils.format(new Date()));
        repaymentPlanCountBeforeDeduct = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
        throw new PendingException();
    }

    @When("^对此提前还款进行扣款且扣款失败F$")
    public void 对此提前还款进行扣款且扣款失败f() throws Throwable {
        String prepaymentNumber = prepaymentApplicationService.getPrepaymentNumber(uniqueId);
        prepaymentCucumberMethod.deductRepaymentPlan(prepaymentNumber, uniqueId, productCode, totalAmount, "1");
        while (true) {
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(prepaymentNumber);
            AssetSetActiveStatus activeStatus = assetSet.getActiveStatus();
            if (AssetSetActiveStatus.INVALID == activeStatus) {
                break;
            }
            Thread.sleep(10000);
        }
        throw new PendingException();
    }

    @Then("^这个贷款合同下可查询出\"([^\"]*)\"条还款计划F$")
    public void 这个贷款合同下可查询出_条还款计划F(String repaymentPlanCount) throws Throwable {
        repaymentPlanCountAfterDeduct = prepaymentCucumberMethod.queryRepaymentPlanCount(uniqueId);
        repaymentPlanCount = repaymentPlanCountAfterDeduct;
        Assert.assertEquals("1", repaymentPlanCountBeforeDeduct);
        Assert.assertEquals("3", repaymentPlanCount);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功!\"}", result);
        throw new PendingException();
    }


}
