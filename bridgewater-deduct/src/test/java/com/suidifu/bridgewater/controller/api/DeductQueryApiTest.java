package com.suidifu.bridgewater.controller.api;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.suidifu.bridgewater.controller.api.deduct.QueryApiDeductController;
import com.zufangbao.gluon.api.earth.v3.model.ApiResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@Transactional
public class DeductQueryApiTest {

	
	@Autowired
	private  QueryApiDeductController queryApiController;
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryOverdueDeductResult(){
		
		OverdueDeductResultQueryModel queryModel = new OverdueDeductResultQueryModel();
		queryModel.setContractNo("contractNo1");
		queryModel.setRequestNo(null);
		queryModel.setUniqueId("100");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		String RTNContent = queryApiController.queryOverdueDeductResult(queryModel,response,request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
		Assert.assertEquals(20001, apiResult.getCode());
		 
	}
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryOverdueDeductResult1(){
		
		OverdueDeductResultQueryModel queryModel = new OverdueDeductResultQueryModel();
		queryModel.setContractNo("contractNo1");
		queryModel.setRequestNo("");
		queryModel.setUniqueId("101");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		String RTNContent = queryApiController.queryOverdueDeductResult(queryModel,response,request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
		Assert.assertEquals(20001, apiResult.getCode());

	}
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryOverdueDeductResult2(){
		
		OverdueDeductResultQueryModel queryModel = new OverdueDeductResultQueryModel();
		queryModel.setContractNo("contractNo1");
		queryModel.setRequestNo("102");
		queryModel.setUniqueId(null);
		queryModel.setDeductId(null);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		String RTNContent = queryApiController.queryOverdueDeductResult(queryModel,response,request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
		Assert.assertEquals(20001, apiResult.getCode());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryBatchDeductStatus(){
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();	
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		queryModel.setDeductIdList("['9d4e6627-6a14-410b-9c8f-b076cbd12347','ee32b181-45a8-459a-95a6-b0fe93c33715','5b9ee8ab-38d4-4729-af33-63dee1c7bf97']");
		queryModel.setRequestNo("107");
		String RTNContent = queryApiController.queryBatchDeductStatus(queryModel, response, request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
	    Assert.assertEquals("成功!", apiResult.getMessage());
	    System.out.println(RTNContent);
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryBatchDeductStatus1(){
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();	
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		queryModel.setDeductIdList("['9d4e6627-6a14-410b-9c8f-b076cbd12347','ee32b181-45a8-459a-95a6-b0fe93c33715','5b9ee8ab-38d4-4729-af33-63dee1c7bf97']");
		String RTNContent = queryApiController.queryBatchDeductStatus(queryModel, response, request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
	    Assert.assertEquals("请求唯一标识［requestNo］，不能为空！", apiResult.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryBatchDeductStatus2(){	
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();	
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();	

		queryModel.setRequestNo("123");
		String RTNContent = queryApiController.queryBatchDeductStatus(queryModel, response, request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
	    Assert.assertEquals("请选填其中一种编号［deductIdList,repaymentPlanCodeList］！", apiResult.getMessage());
	    
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryBatchDeductStatus4(){
		
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();	
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
	    queryModel.setDeductIdList("['12233']");
	    queryModel.setRequestNo("105");
		String RTNContent = queryApiController.queryBatchDeductStatus(queryModel, response, request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
	    Assert.assertEquals("请求编号重复!", apiResult.getMessage());	    
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testQueryBatchDeductStatus5(){
		
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();	
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
	    queryModel.setRepaymentPlanCodeList("['ZC27438B14F806E86C','ZC2748D80AE8F7CBA3','ZC274966BC441E07F6']");
	    
	    queryModel.setRequestNo("106");
	    String RTNContent = queryApiController.queryBatchDeductStatus(queryModel, response, request);
		ApiResult apiResult =JsonUtils.parse(RTNContent,ApiResult.class);
		System.out.println(RTNContent);
	    Assert.assertEquals("成功!", apiResult.getMessage());
	}
	
}
