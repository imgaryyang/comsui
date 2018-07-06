package com.suidifu.bridgewater.handler.test;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.zufangbao.gluon.api.jpmorgan.model.BusinessStatus;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class DeductTaskHandlerTest {

	@Autowired
     private DeductApplicationBusinessHandler deductApplicationBusinessHandler;
     
	
	@Autowired
	private DeductPlanService  deductPlanService;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
     
     
	@Autowired
	private SessionFactory  sessionFactory;
     @Test
     public void testHandleSingleQueryResultStringResultIsNotVaild(){
    	 
    	 
    	 String  deductApplicationUuid = "";
    	 Result result = new Result();
    	 result.fail();
    	 
    	 deductApplicationBusinessHandler.handleSingleQueryResultString(deductApplicationUuid, result);
    	 
     }
     

     @Test
     @Sql("classpath:test/yunxin/api/testHandleSingleQueryResultStringResultIsVaildNotFoundResults.sql")
     public void testHandleSingleQueryResultStringResultIsVaildNotFoundResults(){
    	 
    	
    	 
    	 List<QueryStatusResult> queryStatusResults = new ArrayList<QueryStatusResult>();
    	 
    	 Result  responseResult = new Result();
    	 String queryStatusResultStr=JSON.toJSONString(queryStatusResults);
    	 responseResult.success().data("queryResult", queryStatusResultStr);
    	 
    	 String responseResultStr = JSON.toJSONString(responseResult);
    	 Result result = new Result();
    	 result.success().data("responsePacket", responseResultStr);
    	 
    	 String  deductApplicationUuid = "00bcd5a0-30a5-43d3-9d15-a8886222f49e";
    	 
    	 /*deductApplicationBusinessHandler.handleSingleQueryResultString(deductApplicationUuid, result);
    	 
    	 
    	 String  queryString = "select execution_status from t_deduct_plan where deduct_plan_uuid = '78a70c4f-6e95-419e-b3ed-cee5b50a8c91'";
    	 List<Integer> statusList = this.genericDaoSupport.queryForSingleColumnList(queryString, "uuid", "78a70c4f-6e95-419e-b3ed-cee5b50a8c91", Integer.class);
    	 Assert.assertEquals((Integer)3, statusList.get(0));
    	 
    	 String  queryString2 = "select execution_status from t_deduct_application where deduct_application_uuid = '00bcd5a0-30a5-43d3-9d15-a8886222f49e'";
    	 List<Integer> statusList2 = this.genericDaoSupport.queryForSingleColumnList(queryString2, "uuid", "00bcd5a0-30a5-43d3-9d15-a8886222f49e", Integer.class);
    	 Assert.assertEquals((Integer)3, statusList2.get(0));*/
     }
     
     @Test
     @Sql("classpath:test/yunxin/api/testHandleSingleQueryResultStringResultIsVaildNotFoundResults.sql")
     public void testHandleSingleQueryResultStringResultIsVaild(){
    	 
    	 
    	 List<QueryStatusResult> queryStatusResults = new ArrayList<QueryStatusResult>();
    	 QueryStatusResult  queryStatusResult = new QueryStatusResult();
    	 queryStatusResult.setTransactionUuid("78a70c4f-6e95-419e-b3ed-cee5b50a8c91");
    	 queryStatusResult.setTransactionAmount(new BigDecimal("1"));
    	 queryStatusResult.setBusinessStatus(BusinessStatus.Success);
    	 queryStatusResults.add(queryStatusResult);
    	 
    	 Result  responseResult = new Result();
    	 String queryStatusResultStr=JSON.toJSONString(queryStatusResults);
    	 responseResult.success().data("queryResult", queryStatusResultStr);
    	 
    	 String responseResultStr = JSON.toJSONString(responseResult);
    	 Result result = new Result();
    	 result.success().data("responsePacket", responseResultStr);
    	 
    	 String  deductApplicationUuid = "00bcd5a0-30a5-43d3-9d15-a8886222f49e";
    	 
    	 deductApplicationBusinessHandler.handleSingleQueryResultString(deductApplicationUuid, result);
    	 
    	 DeductPlan deductPlan = deductPlanService.getDeductPlanByUUid(new String("78a70c4f-6e95-419e-b3ed-cee5b50a8c91"));
    	 
    	 Assert.assertEquals(2, deductPlan.getExecutionStatus().ordinal());
    	 
    	 String  queryString2 = "select execution_status from t_deduct_application where deduct_application_uuid = '00bcd5a0-30a5-43d3-9d15-a8886222f49e'";
    	 List<Integer> statusList2 = this.genericDaoSupport.queryForSingleColumnList(queryString2, "uuid", "00bcd5a0-30a5-43d3-9d15-a8886222f49e", Integer.class);
    	 Assert.assertEquals((Integer)2, statusList2.get(0));
     }
     
     
     @Test
     @Ignore("暂时忽略")
     @Sql("classpath:test/yunxin/api/testHandleSingleQueryResultStringResultIsVaildForTwoNumber.sql")
     public void testHandleSingleQueryResultStringResultIsVaildForTwoNumber(){
    	 
    	 
    	 List<QueryStatusResult> queryStatusResults = new ArrayList<QueryStatusResult>();
    	 QueryStatusResult  queryStatusResult = new QueryStatusResult();
    	 queryStatusResult.setTransactionUuid("78a70c4f-6e95-419e-b3ed-cee5b50a8c91");
    	 queryStatusResult.setTransactionAmount(new BigDecimal("1"));
    	 queryStatusResult.setBusinessStatus(BusinessStatus.Success);
    	 queryStatusResults.add(queryStatusResult);
    	 
    	 QueryStatusResult  queryStatusResult2  = new QueryStatusResult();
    	 queryStatusResult2.setTransactionUuid("78a70c4f-6e95-419e-b3ed-cee5b50a8c88");
    	 queryStatusResult2.setTransactionAmount(new BigDecimal("1"));
    	 queryStatusResult2.setBusinessStatus(BusinessStatus.Failed);
    	 queryStatusResults.add(queryStatusResult);
    	 
    	 
    	 Result  responseResult = new Result();
    	 String queryStatusResultStr=JSON.toJSONString(queryStatusResults);
    	 responseResult.success().data("queryResult", queryStatusResultStr);
    	 
    	 String responseResultStr = JSON.toJSONString(responseResult);
    	 Result result = new Result();
    	 result.success().data("responsePacket", responseResultStr);
    	 
    	 String  deductApplicationUuid = "00bcd5a0-30a5-43d3-9d15-a8886222f49e";
    	 
    	 deductApplicationBusinessHandler.handleSingleQueryResultString(deductApplicationUuid, result);
    	 
    	 DeductPlan deductPlan = deductPlanService.getDeductPlanByUUid(new String("78a70c4f-6e95-419e-b3ed-cee5b50a8c91"));
    	 
    	 Assert.assertEquals(2, deductPlan.getExecutionStatus().ordinal());
    	 
    	 DeductPlan deductPlan2 = deductPlanService.getDeductPlanByUUid(new String("78a70c4f-6e95-419e-b3ed-cee5b50a8c88"));
    	 
    	 Assert.assertEquals(3, deductPlan2.getExecutionStatus().ordinal());
    	 
    	 String  queryString2 = "select execution_status from t_deduct_application where deduct_application_uuid = '00bcd5a0-30a5-43d3-9d15-a8886222f49e'";
    	 List<Integer> statusList2 = this.genericDaoSupport.queryForSingleColumnList(queryString2, "uuid", "00bcd5a0-30a5-43d3-9d15-a8886222f49e", Integer.class);
    	 Assert.assertEquals((Integer)3, statusList2.get(0));
     }
     
     
     
     @Test
     @Sql("classpath:test/yunxin/api/testHandleSingleQueryResultStringIsEmptyActiveDeductApplicationUuid.sql")
     public void testHandleSingleQueryResultStringIsEmptyActiveDeductApplicationUuid(){
    	 
    	 
    	 List<QueryStatusResult> queryStatusResults = new ArrayList<QueryStatusResult>();
    	 QueryStatusResult  queryStatusResult = new QueryStatusResult();
    	 queryStatusResult.setTransactionUuid("78a70c4f-6e95-419e-b3ed-cee5b50a8c91");
    	 queryStatusResult.setTransactionAmount(new BigDecimal("1"));
    	 queryStatusResult.setBusinessStatus(BusinessStatus.Success);
    	 queryStatusResults.add(queryStatusResult);
    	 
    	 Result  responseResult = new Result();
    	 String queryStatusResultStr=JSON.toJSONString(queryStatusResults);
    	 responseResult.success().data("queryResult", queryStatusResultStr);
    	 
    	 String responseResultStr = JSON.toJSONString(responseResult);
    	 Result result = new Result();
    	 result.success().data("responsePacket", responseResultStr);
    	 
    	 String  deductApplicationUuid = "00bcd5a0-30a5-43d3-9d15-a8886222f49e";
    	 
    	 deductApplicationBusinessHandler.handleSingleQueryResultString(deductApplicationUuid, result);
    	 
    	 String activeDeductApplicationUuid = repaymentPlanService.queryActiveDeductApplicationUuidBySql("88450378-e6fd-4857-9562-22971b05b932");
    	 //Assert.assertEquals("empty_deduct_uuid", activeDeductApplicationUuid);
     }
}
