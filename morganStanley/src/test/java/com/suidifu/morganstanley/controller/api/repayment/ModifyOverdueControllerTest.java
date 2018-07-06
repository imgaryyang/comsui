package com.suidifu.morganstanley.controller.api.repayment;

import com.demo2do.core.entity.Result;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.api.BaseControllerTest;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeDetail;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeRequestModel;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_OVER_DUE_FEE;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.REQUEST_NO;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * 更新逾期费用明细接口测试类
 * @author louguanyang at 2017/9/25 17:48
 * jqncxhhkfamybcgd
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
@Sql("classpath:test/api/overdueFee/HotCompilation.sql")
public class ModifyOverdueControllerTest extends BaseControllerTest {
    private static final String MODIFY_OVER_DUE_FEE_DETAILS = "modifyOverDueFeeDetails";
    private ModifyOverDueFeeRequestModel model;
    @Resource
    private RepaymentPlanService repaymentPlanService;

    @Before
    public void setUp() throws Exception {
        post_test_url = URL_API_V3 + URL_MODIFY_OVER_DUE_FEE;
        model = new ModifyOverDueFeeRequestModel();
    }

    @After
    public void tearDown() throws Exception {
        requestBody = null;
        headers = null;
        model = null;
    }

    /**
     * 构建请求参数Map
     */
    @Override
    public void buildRequestParamsMap() {
        requestParams.clear();
        requestParams.put(REQUEST_NO, model.getRequestNo());
        requestParams.put(MODIFY_OVER_DUE_FEE_DETAILS, model.getModifyOverDueFeeDetails());
    }

    /**
     * 商户还款计划编号 变更逾期费用 成功
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeSuccessRepayScheduleNo() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.SUCCESS.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.SUCCESS.getMessage());

        String date = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"outer1\",\"repaymentPlanNo\":\"\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 五维还款计划编号 变更逾期费用 成功
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeSuccessRepaymentPlanNo() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.SUCCESS.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.SUCCESS.getMessage());

        String today = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ today +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 参数校验失败 空的请求
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeEmptyRequest() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("请求唯一编号[requestNo]不能为空");

        model.setRequestNo(StringUtils.EMPTY);
        model.setModifyOverDueFeeDetails(StringUtils.EMPTY);
        Result result = sendPostTest(expectedCode, expectedMatcher);
        assertThat(result.getMessage(), containsString("具体变更内容[modifyOverDueFeeDetails]不能为空"));
    }

    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeInvalidDetail() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("penaltyFee:金额格式错误");

        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\"," +
                "\"lateFee\":\"-300.0\",\"lateOtherCost\":\"200.002\",\"latePenalty\":\"\",\"overDueFeeCalcDate\":\"2017-10-16\",\"penaltyFee\":\"a1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC2730FAE4092E0A6E\",\"totalOverdueFee\":\"0.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);

        Result result = sendPostTest(expectedCode, expectedMatcher);
        // 金额不合法
        assertThat(result.getMessage(), containsString("latePenalty:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("lateFee:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("penaltyFee:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("penaltyFee:金额格式错误"));
        assertThat(result.getMessage(), containsString("latePenalty:不能为空"));
        // 总金额大于0
        assertThat(result.getMessage(), containsString("totalOverdueFee:必须大于或等于0.01"));
        // 小数点后三位
        assertThat(result.getMessage(), containsString("lateOtherCost:金额精度错误,需小于或等于小数点后2位"));
    }

    /**
     * 还款计划编号, 商户还款计划编号不能都为空
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeEmptyRepaymentPlan() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("还款计划编号[repaymentPlanNo], 商户还款计划编号[repayScheduleNo]不能都为空");

        String today = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ today +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 逾期罚息计算日[overDueFeeCalcDate]格式错误, 应为[yyyy-MM-dd]
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeInvalidDetailDate() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("逾期罚息计算日[overDueFeeCalcDate]格式错误,日期格式[yyyy-MM-dd]");

        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\"2017-10-10 00:00:00\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 逾期罚息计算日[overDueFeeCalcDate]日期有误 早于当日
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeErrorOverdueDate() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("逾期罚息计算日[overDueFeeCalcDate]日期有误");

        String date = DateUtils.format(DateUtils.addDays(new Date(), -2));
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);

        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 逾期罚息计算日[overDueFeeCalcDate]日期有误 晚于当日
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate.sql")
    public void modifyOverdueFeeInvalidOverdueDate() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("逾期罚息计算日[overDueFeeCalcDate]日期有误");

        String date = DateUtils.format(DateUtils.addDays(new Date(), 2));
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);

        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 修改逾期费用金额明细总额与总金额不相等
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate.sql")
    public void modifyOverdueFeeInvalidTotalAmount() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("修改逾期费用金额明细总额与总金额不相等");

        String date = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"1000.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);

        sendPostTest(expectedCode, expectedMatcher);
    }

    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeRepeatRequestNo() {
        String expectedCode = String.valueOf(ApiMessage.REPEAT_REQUEST_NO.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.REPEAT_REQUEST_NO.getMessage());

        String date = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo("1");
        model.setModifyOverDueFeeDetails(details);

        sendPostTest(expectedCode, expectedMatcher);
    }

    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeContractNotExist() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.CONTRACT_NOT_EXIST.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.CONTRACT_NOT_EXIST.getMessage());

        String date = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);

        sendPostTest(expectedCode, expectedMatcher);
    }

    @Test
    @Sql("classpath:test/api/overdueFee/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate2.sql")
    public void modifyOverdueFeeInvalidFinancialProductCode() {
        String expectedCode = String.valueOf(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getMessage());

        String date = DateUtils.format(DateUtils.addDays(new Date(), 1));
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"123456\",\"currentPeriod\":0,\"financialProductCode\":\"G00002\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC2730FAE4092E0A6E\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);

        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 商户还款计划编号错误
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate2.sql")
    public void modifyOverdueFeeRepayPlanNotFind_1() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.SINGLE_LOAN_CONTRACT_NO_ERROR.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.SINGLE_LOAN_CONTRACT_NO_ERROR.getMessage());

        String financialProductCode = "G00001";
        String repayScheduleNo = "1223333";
        String repayScheduleNoMD5 = repaymentPlanService.getRepayScheduleNoMD5(financialProductCode, repayScheduleNo, "");
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByConditions(repayScheduleNoMD5, "", "", null);
        Assert.assertNull(assetSet);

        String date = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"123456\",\"currentPeriod\":0,\"financialProductCode\":\"" + financialProductCode + "\"," +
                "\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ date +"\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"" + repayScheduleNo + "\",\"repaymentPlanNo\":\"\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 五维还款计划编号错误
     *
     * @throws Exception 异常
     */
    @Test
    @Sql("classpath:test/api/overdueFee/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate2.sql")
    public void modifyOverdueFeeRepayPlanNotFind_2() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.SINGLE_LOAN_CONTRACT_NO_ERROR.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.SINGLE_LOAN_CONTRACT_NO_ERROR.getMessage());

        String repaymentPlanNo = "ABCD1234";
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByConditions("", repaymentPlanNo, "", null);

        Assert.assertNull(assetSet);

        String date = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"123456\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\"" + date + "\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"" + repaymentPlanNo + "\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 贷款合同 + 期数 错误
     *
     * @throws Exception 异常
     */
    @Ignore
    @Test
    @Sql("classpath:test/api/overdueFee/testModifyOverDueFee_overDueFeeCalcAfterAssetRecycleDate2.sql")
    public void modifyOverdueFeeRepayPlanNotFind_3() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.SINGLE_LOAN_CONTRACT_NO_ERROR.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.SINGLE_LOAN_CONTRACT_NO_ERROR.getMessage());

        Integer currentPeriod = 100;
        AssetSet assetSet = repaymentPlanService.getRepaymentPlanByConditions("", "", "1234567890", currentPeriod);
        Assert.assertNull(assetSet);

        String date = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"123456\",\"currentPeriod\":" + currentPeriod + ",\"financialProductCode\":\"G00001\"," +
                "\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\"" + date + "\",\"penaltyFee\":\"1090.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"\",\"totalOverdueFee\":\"1690.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    // TODO add test REPAYMENT_CODE_NOT_IN_CONTRACT
    // TODO add test REPAYMENT_PLAN_NOT_OPEN
    // TODO add test REPAYMENT_PLAN_IS_PAID_OFF
    // TODO add test REPAYMENT_PLAN_LOCKED

    @Test
    @Sql("classpath:test/api/overdueFee/modifyOverdueFee_OVERDUE_MODIFY_AMOUNT_LESS_THAN_PAIN_IN_AMOUNT_GREATER.sql")
    public void modifyOverdueAmountLessThanPainInAmountGreater() {
        String expectedCode = String.valueOf(ApiMessage.OVERDUE_MODIFY_AMOUNT_LESS_THAN_PAIN_IN_AMOUNT_GREATER.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.OVERDUE_MODIFY_AMOUNT_LESS_THAN_PAIN_IN_AMOUNT_GREATER.getMessage());

        buildOverdueModifyAmountLessThanPainInAmountGreaterModel();

        sendPostTest(expectedCode, expectedMatcher);
    }

    /**
     * 逾期罚息 > 计划还款本金 * 24% * （1+r ） * 逾期天数/360  （r根据项目而定）（逾期天数指宽限期过后开始计算，同系统现有逻辑）
     */
    @Test
    @Sql("classpath:test/api/overdueFee/test_modifyOverDueFee.sql")
    public void modifyOverdueFeeCustomerFeeCheckFail() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getMessage());

        String today = DateUtils.today();
        String details = "[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\"," +
                "\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\""+ today +"\",\"penaltyFee\":\"109000.00\"," +
                "\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"109600.00\"}]";
        model.setRequestNo(UUIDUtil.random32UUID());
        model.setModifyOverDueFeeDetails(details);
        sendPostTest(expectedCode, expectedMatcher);
    }

    private void buildOverdueModifyAmountLessThanPainInAmountGreaterModel() {
        model.setRequestNo(UUIDUtil.random32UUID());
        ModifyOverDueFeeDetail detail = new ModifyOverDueFeeDetail();
        detail.setContractUniqueId("123456");
        detail.setLateFee("100.00");
        detail.setLateOtherCost("0.00");
        detail.setLatePenalty("100.00");
        detail.setOverDueFeeCalcDate(DateUtils.format(DateUtils.addDays(new Date(), 1)));
        detail.setPenaltyFee("90.00");
        detail.setRepaymentPlanNo("ZC2730FAE4092E0A6E");
        detail.setRepayScheduleNo("");
        detail.setTotalOverdueFee("290.00");
        detail.setFinancialProductCode("G00001");
        String modifyOverDueFeeDetails = JsonUtils.toJSONString(Collections.singletonList(detail));
        model.setModifyOverDueFeeDetails(modifyOverDueFeeDetails);
    }
}