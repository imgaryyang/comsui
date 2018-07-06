package com.suidifu.morganstanley.controller.api.repayment;

import com.demo2do.core.entity.Result;
import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.controller.api.BaseControllerTest;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.model.api.PrepaymentModifyModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_PREPAYMENT;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * 提前全额还款接口测试类
 * @author louguanyang at 2017/9/27 15:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class PrepaymentControllerTest extends BaseControllerTest {

    private PrepaymentModifyModel model;

    @Before
    public void setUp() throws Exception {
        post_test_url = URL_API_V3 + URL_PREPAYMENT;
        model = new PrepaymentModifyModel();
    }

    @After
    public void tearDown() throws Exception {
        requestBody = null;
        headers = null;
    }

    /**
     * 提前还款测试
     * 1)提前还款成功
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepayment() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.SUCCESS.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.SUCCESS.getMessage());

        buildSuccessModel();
        Result result = assertPostResultTest(expectedCode, expectedMatcher);
        Map<String, Object> data = result.getData();
        Assert.assertNotNull(data);
        String repaymentPlanNo = (String) data.getOrDefault("repaymentPlanNo", null);
        Assert.assertNotNull(repaymentPlanNo);
        String repayScheduleNo = (String) data.getOrDefault("repayScheduleNo", null);
        Assert.assertNotNull(repayScheduleNo);
        Assert.assertEquals(model.getRepayScheduleNo(), repayScheduleNo);
    }

    /**
     * 提前还款测试
     * 还款日期 格式错误 yyyy-MM-dd HH:mm:ss
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentRecycleDateErrorFormat() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("assetRecycleDate:计划还款日期格式错误,日期格式[yyyy-MM-dd]");

        buildSuccessModel();
        model.setAssetRecycleDate("2017-10-18 00:00:00");
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 还款日期 格式错误
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentErrorRecycleDate() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("assetRecycleDate:计划还款日期格式错误,日期格式[yyyy-MM-dd]");

        buildSuccessModel();
        model.setAssetRecycleDate("ABC123445");
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 还款日期 格式错误 昨天
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentRecycleDateYesterday() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("assetRecycleDate:日期不应早于当日");

        buildSuccessModel();
        model.setAssetRecycleDate(DateUtils.format(DateUtils.getYesterday()));
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 还款日期 格式错误 很久以前
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentRecycleDatePast() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("assetRecycleDate:日期不应早于当日:" + DateUtils.today());

        buildSuccessModel();
        model.setAssetRecycleDate("2007-10-18");
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 还款金额 为 负数
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentInvalidRequestData() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("assetInitialValue:必须大于或等于0.00");

        buildSuccessModel();
        model.setRequestNo("");
        model.setAssetRecycleDate("");
        model.setAssetInitialValue("-100");
        model.setAssetPrincipal("-100");
        model.setAssetInterest("-100");
        model.setServiceCharge("-100");
        model.setMaintenanceCharge("-100");
        model.setOtherCharge("-100");
        model.setType(1);
        model.setPayWay(-1);
        model.setFinancialProductCode("");

        Result result = assertPostResultTest(expectedCode, expectedMatcher);

        assertThat(result.getMessage(), containsString("requestNo:请求唯一编号不能为空"));
        assertThat(result.getMessage(), containsString("assetRecycleDate:计划还款日期不能为空"));
        assertThat(result.getMessage(), containsString("assetInitialValue:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("assetPrincipal:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("assetInterest:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("serviceCharge:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("maintenanceCharge:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("otherCharge:必须大于或等于0.00"));
        assertThat(result.getMessage(), containsString("type:目前仅支持:0-[全部提前还款]模式"));
        assertThat(result.getMessage(), containsString("payWay:还款方式只支持以下4种类型:0-[代扣],1-[主动还款],2-[委托转付],3-[代偿]"));
        assertThat(result.getMessage(), containsString("financialProductCode:信托产品代码不能为空"));
    }

    /**
     * 提前还款测试
     * 还款金额 格式错误
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentInvalidAmount() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("assetInitialValue:金额精度错误,需小于或等于小数点后2位");

        buildSuccessModel();
        model.setAssetInitialValue("0.001");
        model.setAssetPrincipal("0.00");
        model.setType(-1);
        model.setPayWay(4);

        Result result = assertPostResultTest(expectedCode, expectedMatcher);
        assertThat(result.getMessage(), containsString("assetInitialValue:必须大于或等于0.01"));
        assertThat(result.getMessage(), containsString("assetPrincipal:必须大于或等于0.01"));
        assertThat(result.getMessage(), containsString("type:目前仅支持:0-[全部提前还款]模式"));
        assertThat(result.getMessage(), containsString("payWay:还款方式只支持以下4种类型:0-[代扣],1-[主动还款],2-[委托转付],3-[代偿]"));
    }

    /**
     * 提前还款测试
     * 申请当日处理中还款计划扣款状态 0.本端未扣款;1.对端已扣款
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentInvalidHasDeducted() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("hasDeducted:错误,取值范围:0-[本端未扣款],1-[对端已扣款]");

        buildSuccessModel();
        model.setHasDeducted(-1);

        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 申请当日处理中还款计划扣款状态 0.本端未扣款;1.对端已扣款
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentInvalidHasDeducted2() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("hasDeducted:错误,取值范围:0-[本端未扣款],1-[对端已扣款]");

        buildSuccessModel();
        model.setHasDeducted(2);

        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 申请当日处理中还款计划扣款状态 0.本端未扣款;1.对端已扣款
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentAmountNotEquals() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.INVALID_PARAMS.getCode());
        Matcher<String> expectedMatcher = containsString("提前还款明细金额之和与总金额[assetInitialValue]不匹配");

        buildSuccessModel();
        model.setAssetInitialValue("100");
        model.setAssetPrincipal("100.00");
        model.setAssetInterest("10.00");

        assertPostResultTest(expectedCode, expectedMatcher);
    }

    @Autowired
    private ContractService contractService;

    /**
     * 提前还款测试
     * 贷款合同不存在
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentContractNotExist() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.CONTRACT_NOT_EXIST.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.CONTRACT_NOT_EXIST.getMessage());
        String contractNo = StringUtils.EMPTY;
        String uniqueId = StringUtils.EMPTY;
        buildSuccessModel();
        model.setContractNo(contractNo);
        model.setUniqueId(uniqueId);
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 贷款合同不存在 错误的contactUniqueId
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentContractNotExist2() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.CONTRACT_NOT_EXIST.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.CONTRACT_NOT_EXIST.getMessage());
        buildSuccessModel();
        String uniqueId = UUIDUtil.random32UUID();
        Contract contract = contractService.getContractByUniqueId(uniqueId);
        Assert.assertNull(contract);
        model.setUniqueId(uniqueId);
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 贷款合同不存在 错误的contractNo
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentContractNotExist3() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.CONTRACT_NOT_EXIST.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.CONTRACT_NOT_EXIST.getMessage());
        buildSuccessModel();
        String uniqueId = StringUtils.EMPTY;
        String contractNo = "贷款合同编号123456";
        Contract contract = contractService.getContractByContractNo(contractNo);
        Assert.assertNull(contract);
        model.setUniqueId(uniqueId);
        model.setContractNo(contractNo);
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    @Resource
    private FinancialContractService financialContractService;

    /**
     * 提前还款测试
     * 信托合同不存在
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentFinancialContractNotExist() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST.getMessage());
        buildSuccessModel();
        String financialProductCode = "G00001";
        model.setFinancialProductCode(financialProductCode);

        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialProductCode);
        Assert.assertNull(financialContract);
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 提前还款测试
     * 信托产品代码 与 贷款合同不匹配
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentFinancialContractNotEquals() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT.getMessage());
        buildSuccessModel();
        String financialProductCode = "G00002";
        model.setFinancialProductCode(financialProductCode);

        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialProductCode);
        Assert.assertNotNull(financialContract);
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    @Resource
    private PrepaymentApplicationService prepaymentApplicationService;

    /**
     * 提前还款测试
     * 请求编号重复
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentRepeatRequestNo() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.REPEAT_REQUEST_NO.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.REPEAT_REQUEST_NO.getMessage());
        buildSuccessModel();
        String requestNo = "123456";
        model.setRequestNo(requestNo);
        List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.checkRequestNo(requestNo);
        Assert.assertTrue(CollectionUtils.isNotEmpty(prepaymentApplications));
        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 已存在提前还款申请
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_fail_exist_prepayment_plan.sql")
    public void prepaymentExistPrepaymentPlan() throws Exception {
        String expectedCode = String.valueOf(ApiMessage.PREPAYMENT_ASSETSET_EXSITED.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.PREPAYMENT_ASSETSET_EXSITED.getMessage());
        buildSuccessModel();

        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 没有可变更的还款计划
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_fail_no_available_asset_set.sql")
    public void prepaymentNoAvailableAssetSet() {
        String expectedCode = String.valueOf(ApiMessage.NO_AVAILABLE_ASSET_SET.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.NO_AVAILABLE_ASSET_SET.getMessage());
        buildSuccessModel();
        model.setUniqueId("unique_id");

        assertPostResultTest(expectedCode, expectedMatcher);
    }

    /**
     * 潍坊自定义金额校验失败
     */
    @Test
    @Sql("classpath:test/api/prepayment/test_prepayment_success.sql")
    public void prepaymentCustomerFeeCheckFail() {
        String expectedCode = String.valueOf(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getCode());
        Matcher<String> expectedMatcher = containsString(ApiMessage.CUSTOMER_FEE_CHECK_FAIL.getMessage());
        buildSuccessModel();
        model.setAssetPrincipal("30000.00");
        model.setAssetInitialValue("30010.00");
        model.setAssetInterest("10.00");

        assertPostResultTest(expectedCode, expectedMatcher);
    }

    public Result assertPostResultTest(String expectedCode, Matcher<String> expectedMatcher) {
        return sendPostTest(expectedCode, expectedMatcher);
    }

    private void buildSuccessModel() {
        model = new PrepaymentModifyModel();
        model.setFinancialProductCode("test_financial_contract_no");
        model.setUniqueId("");
        model.setContractNo("test_contract_no");
        model.setRequestNo("testRequestNo");
        model.setAssetRecycleDate(DateUtils.today());
        model.setAssetInitialValue("30000.00");
        model.setType(0);
        model.setPayWay(0);
        model.setAssetPrincipal("30000.00");
        model.setHasDeducted(0);
        model.setRepayScheduleNo("ABC123456");
    }

    @Override
    public void buildRequestParamsMap() {
        requestParams.clear();
        requestParams.put(UNIQUE_ID, model.getUniqueId());
        requestParams.put(CONTRACT_NO, model.getContractNo());
        requestParams.put(REQUEST_NO, model.getRequestNo());
        requestParams.put(FINANCIAL_PRODUCT_CODE, model.getFinancialProductCode());
        requestParams.put("assetRecycleDate", model.getAssetRecycleDate());
        requestParams.put("assetInitialValue", model.getAssetInitialValue());
        requestParams.put("repayScheduleNo", "" + model.getRepayScheduleNo());
        requestParams.put("type", "" + model.getType());
        requestParams.put("payWay", "" + model.getPayWay());
        requestParams.put("assetPrincipal", model.getAssetPrincipal());
        requestParams.put("assetInterest", model.getAssetInterest());
        requestParams.put("serviceCharge", model.getServiceCharge());
        requestParams.put("maintenanceCharge", model.getMaintenanceCharge());
        requestParams.put("otherCharge", model.getOtherCharge());
        requestParams.put("hasDeducted", "" + model.getHasDeducted());
    }
}