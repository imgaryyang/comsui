package com.zufangbao.earth.web.controller.api;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.controller.QueryApiController;
import com.zufangbao.earth.yunxin.api.model.ApiResult;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetailsQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetailsResultModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml",
		"classpath:/local/DispatcherServlet.xml"})
@WebAppConfiguration(value="webapp")
public class QueryRepaymentPlanDetailsApiTest {

	@Autowired
	private QueryApiController queryApiController;

	//查询还款详情接口测试
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_no_queryModel() {
		
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(null, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		Assert.assertEquals(20002, apiResult.getCode());
		Assert.assertEquals("请求参数解析错误!", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_no_requestNo() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setContractNo("G31700");
		queryModel.setSingleLoanContractNo("ZC1332811198381084672");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		Assert.assertEquals(20001, apiResult.getCode());
		Assert.assertEquals("无效参数!", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_no_contractNo() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setRequestNo(UUID.randomUUID().toString());
		queryModel.setSingleLoanContractNo("ZC1332811198381084672");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		Assert.assertEquals(20001, apiResult.getCode());
		Assert.assertEquals("无效参数!", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_no_singleLoanContractNo() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setRequestNo(UUID.randomUUID().toString());
		queryModel.setContractNo("G31700");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		Assert.assertEquals(20001, apiResult.getCode());
		Assert.assertEquals("无效参数!", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_contractNo_error() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setRequestNo(UUID.randomUUID().toString());
		queryModel.setContractNo("bucunzai");
		queryModel.setSingleLoanContractNo("ZC1332811198381084672");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		Assert.assertEquals(23031, apiResult.getCode());
		Assert.assertEquals("信托产品代码错误", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_singleLoanContractNo_error() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setRequestNo(UUID.randomUUID().toString());
		queryModel.setContractNo("G31700");
		queryModel.setSingleLoanContractNo("bucunzai");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		Assert.assertEquals(21023, apiResult.getCode());
		Assert.assertEquals("还款计划编号错误", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_contractNoNotAgreeWithSingleLoanContractNo() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setRequestNo(UUID.randomUUID().toString());
		queryModel.setContractNo("G08200");
		queryModel.setSingleLoanContractNo("ZC1332811198381084672");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request,response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		Assert.assertEquals(23045, apiResult.getCode());
		Assert.assertEquals("信托代码与还款计划中信托代码不匹配", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_unexpire.sql")
	public void testQueryRepaymentPlanDetailsApi_unexpire() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setRequestNo(UUID.randomUUID().toString());
		queryModel.setContractNo("G31700");
		queryModel.setSingleLoanContractNo("ZC1332811198381084672");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		String repaymentPlanDetailsStr = apiResult.getData().get("repaymentPlanDetails").toString();
		System.out.println(repaymentPlanDetailsStr);
		
		RepaymentPlanDetailsResultModel details = JsonUtils.parse(repaymentPlanDetailsStr, RepaymentPlanDetailsResultModel.class);
		Assert.assertEquals(0, apiResult.getCode());
		Assert.assertEquals("成功!", apiResult.getMessage());
		Assert.assertEquals(new Integer(0), details.getPaymentStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentPlanDetailsApi_success_active.sql")
	public void testQueryRepaymentPlanDetailsApi_success_active() {
		RepaymentPlanDetailsQueryModel queryModel = new RepaymentPlanDetailsQueryModel();
		queryModel.setRequestNo(UUID.randomUUID().toString());
		queryModel.setContractNo("G31700");
		queryModel.setSingleLoanContractNo("ZC1200665466820567040");
		// FIXME add response
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String RTNContent = queryApiController.queryRepaymentPlanDetails(queryModel, request, response);
		
		ApiResult apiResult = JsonUtils.parse(RTNContent, ApiResult.class);
		String repaymentPlanDetailsStr = apiResult.getData().get("repaymentPlanDetails").toString();
		System.out.println(repaymentPlanDetailsStr);
		
		RepaymentPlanDetailsResultModel details = JsonUtils.parse(repaymentPlanDetailsStr, RepaymentPlanDetailsResultModel.class);
		Assert.assertEquals(0, apiResult.getCode());
		Assert.assertEquals("成功!", apiResult.getMessage());
		Assert.assertEquals(null, details.getRepaymentType());
		Assert.assertEquals(new Integer(2), details.getPaymentStatus());
		Assert.assertEquals(1, details.getPaymentRecords().size());
		Assert.assertEquals(new Integer(2), details.getPaymentRecords().get(0).getVoucherSource());
	}
	
}
