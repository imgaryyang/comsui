package com.zufangbao.earth.yunxin.handler.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.earth.yunxin.handler.reportform.InterestReportFormHandler;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestShowModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class InterestReportFormHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired 
	private InterestReportFormHandler handler;
	
	
	@Test
	public void test_query_null(){
		InterestQueryModel queryModel = null;
		List<InterestShowModel> list = handler.query(queryModel, null);
		Assert.assertEquals(0, list.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/loansReportForm/test_query.sql")
	public void test_query_nullStartDate(){
		InterestQueryModel queryModel = new InterestQueryModel();
		String financialContractUuids = "[\"2d380fe1-7157-490d-9474-12c5a9901e29\",\"92846f20-87e3-49f4-8f90-fe04a72c0484\",\"d84e2927-839e-4162-9af1-e648e15bbf59\",\"930f1d3d-8158-420e-89bd-6f3922395eae\"]";                                                                   
		queryModel.setFinancialContractUuids(financialContractUuids);
		String startDateString = null;
		queryModel.setStartDateString(startDateString);
		try{
		List<InterestShowModel> list = handler.query(queryModel, null);
		}catch(RuntimeException e){
			Assert.assertEquals("查询起始日期格式错误！", e.getMessage());
		}
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/loansReportForm/test_query.sql")
	public void test_query(){
		InterestQueryModel queryModel = new InterestQueryModel();
		String financialContractUuids = "[\"2d380fe1-7157-490d-9474-12c5a9901e29\",\"92846f20-87e3-49f4-8f90-fe04a72c0484\",\"d84e2927-839e-4162-9af1-e648e15bbf59\",\"930f1d3d-8158-420e-89bd-6f3922395eae\"]";                                                                   
		queryModel.setFinancialContractUuids(financialContractUuids);
		String startDateString = "2016-04-01";
		queryModel.setStartDateString(startDateString);
		List<InterestShowModel> list = handler.query(queryModel, null);

		Assert.assertEquals(4, list.size());
	}

}
