package com.suidifu.bridgewater.handler.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryResult;
import com.suidifu.bridgewater.api.model.DeductQueryResult;
import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.suidifu.bridgewater.handler.DeductQueryApiHandler;
import com.zufangbao.gluon.exception.ApiException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class DeductQueryApiHandlerImplTest{

	@Autowired
	private DeductQueryApiHandler deductQueryApiHandler;

	@Test
	@Sql("classpath:test/yunxin/deductQuery/testDeductQueryNoContract.sql")
	public void testApideductQuery() {
		OverdueDeductResultQueryModel queryModel = new OverdueDeductResultQueryModel();
		MockHttpServletRequest request = new MockHttpServletRequest();

		queryModel.setDeductId("12345");
		queryModel.setContractNo("contractNo1");
		queryModel.setRequestNo("1234");
		queryModel.setUniqueId("123");

		try {
			deductQueryApiHandler.apideductQuery(queryModel, request);
		} catch (ApiException e) {
			e.printStackTrace();
			Assert.assertEquals(22206, e.getCode());
		}

	} 
	@Test
	@Sql("classpath:test/yunxin/deductQuery/testDeductQueryNoContract1.sql")
	public void testApideductQuery1() {
		OverdueDeductResultQueryModel queryModel = new OverdueDeductResultQueryModel();
		MockHttpServletRequest request = new MockHttpServletRequest();

		queryModel.setDeductId("1087e301-bd27-45b4-8368-3137b9e6e12c");
		queryModel.setContractNo("contractNo1");
		queryModel.setRequestNo("1234");
		queryModel.setUniqueId("123");
        
		DeductQueryResult deductQueryResult = deductQueryApiHandler.apideductQuery(queryModel, request);
		Assert.assertEquals("contractNo1",deductQueryResult.getContractNo());
		Assert.assertEquals("123",deductQueryResult.getContractUniqueId());
		Assert.assertEquals("1234",deductQueryResult.getQueryRequestNo());
		Assert.assertEquals(1,deductQueryResult.getDeductInfos().size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testApiBatchDeductStatusQuery() {
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();
		MockHttpServletRequest request = new MockHttpServletRequest();
		queryModel.setDeductIdList("['9d4e6627-6a14-410b-9c8f-b076cbd12347','ee32b181-45a8-459a-95a6-b0fe93c33715']");
		List<BatchDeductStatusQueryResult>  list = deductQueryApiHandler.apiBatchDeductStatusQuery(queryModel, request);
		Assert.assertEquals(2,list.size());
		
		queryModel = new BatchDeductStatusQueryModel();
		queryModel.setDeductIdList("['9d4e6627-6a14-410b-9c8f-b076cbd12347','ee32b181-45a8-459a-95a6-b0fe93c33715','6934251e-ae58-4838-aadb-cdba2b57f73f']");
		list = deductQueryApiHandler.apiBatchDeductStatusQuery(queryModel, request);
		Assert.assertEquals(3,list.size());
		
		
	}
	 
	 
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testGetDeductPlanByDeductApplicaiton(){
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();
		MockHttpServletRequest request = new MockHttpServletRequest();
		queryModel.setRepaymentPlanCodeList("['ZC27438B14F806E86C','ZC2748A7ECBDC97EAC','ddfdfs']");
		queryModel.setRequestNo("11");
		List<BatchDeductStatusQueryResult>  list = deductQueryApiHandler.apiBatchDeductStatusQuery(queryModel, request);
		Assert.assertEquals(2,list.size());
	}
	
	
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryBatchDeductStatus4.sql")
	public void testTimeSort() {
		BatchDeductStatusQueryModel queryModel = new BatchDeductStatusQueryModel();
		MockHttpServletRequest request = new MockHttpServletRequest();
		queryModel.setDeductIdList("['9d4e6627-6a14-410b-9c8f-b076cbd12347']");
		List<BatchDeductStatusQueryResult>  list = deductQueryApiHandler.apiBatchDeductStatusQuery(queryModel, request);
		Assert.assertEquals("流水2",list.get(0).getPaymentFlowNo());
		
	}
	
}
