package com.zufangbao.earth.web.controller;


import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.web.controller.financial.FlowController;
import com.zufangbao.earth.yunxin.handler.remittance.RemittancetPlanHandler;
import com.zufangbao.gluon.exception.CommonException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
@Transactional
public class FlowControllerTest {
	
	@Autowired
	FlowController flowController;
	@Autowired
	private RemittancetPlanHandler remittancetPlanHandler;
	

	@Test
	public void check_opposite_execution_status_test1(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = flowController.checkOppositeExecutionStatus(principal, "", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的代付单", result.getMessage());
		
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	@Ignore
	public void check_opposite_execution_status_test2(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = flowController.checkOppositeExecutionStatus(principal, "e04d7c18-11c0-4019-88a0-9ed617c82699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("通讯失败", result.getMessage());
		
	}
	
	/*@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test3(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		
		HttpServletRequest request = null;
		String response = flowController.checkOppositeExecutionStatus(principal, "e04d7c18-11c0-4019-88a0-9ed617c82699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("结果解析失败", result.getMessage());
		
	}*/
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test4(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		
		String response = flowController.checkOppositeExecutionStatus(principal, "e04d7c18-11c0-4019-88a0-9ed617c82699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("查询失败", result.getMessage());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test5(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = flowController.checkOppositeExecutionStatus(principal, "f04r7c18-11c0-4019-88a0-8ed617c82699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的放款单", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test6(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		
		String response = flowController.checkOppositeExecutionStatus(principal, "g04d7c18-11c0-4019-88a0-9ed617c82699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的计划明细单", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test7(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = flowController.checkOppositeExecutionStatus(principal, "r04d7c18-11c0-4019-84a0-9ed61e482699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的计划单", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test8(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = flowController.checkOppositeExecutionStatus(principal, "af04d7c18-11c0-4019-88a0-9ed617c82699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("核单成功", result.getMessage());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	@Ignore
	public void check_opposite_execution_status_test9(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = flowController.checkOppositeExecutionStatus(principal, "04d7c18-11c0-4019-88a0-9ed617c82699", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("核单成功", result.getMessage());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/editReverseStatus.sql")
	public void editReverseStatus_test1(){
		String response1 = flowController.editReverseStatus("", 2, "executionRemark");
		String response2 = flowController.editReverseStatus("b89948cb9-4796-44e7-8d96-0104fa5ed098", -1, "executionRemark");
		String response3 = flowController.editReverseStatus("b89948cb9-4796-44e7-8d96-0104fa5ed098", 2, "");
		
		Result result1 = JsonUtils.parse(response1, Result.class);
		Result result2 = JsonUtils.parse(response2, Result.class);
		Result result3 = JsonUtils.parse(response3, Result.class);
		
		Assert.assertEquals("参数不能为空", result1.getMessage());
		Assert.assertEquals("参数不能为空", result2.getMessage());
		Assert.assertEquals("参数不能为空", result3.getMessage());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/editReverseStatus.sql")
	public void editReverseStatus_test2(){
		String response = flowController.editReverseStatus("48cb9-4796-44e7-8d96-0104fa5ed098", 2, "executionRemark");
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的代付单", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/editReverseStatus.sql")
	public void editReverseStatus_test3(){
		String response1 = flowController.editReverseStatus("b89948cb9-4796-44e7-8d96-0104fa5ed098", 2, "executionRemark");
		Result result1 = JsonUtils.parse(response1, Result.class);
		
		//String response2 = flowController.editReverseStatus("b89948cb9-4796-44e7-8d96-0104fa5ed098", 3, "executionRemark");
		//Result result2 = JsonUtils.parse(response2, Result.class);
		
		Assert.assertEquals("只允许未冲账改为已冲账", result1.getMessage());
		//Assert.assertEquals("只允许未发生改为已退票", result2.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/editReverseStatus.sql")
	public void editReverseStatus_test4(){
		String response = flowController.editReverseStatus("cc9948cb9-4796-44e7-8d96-0104fa5ed098", 2, "executionRemark");
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的贷记流水", result.getMessage());
	}
	
	/*@Test
	@Sql("classpath:test/yunxin/flowController/editReverseStatus.sql")
	public void editReverseStatus_test5(){
		String response = flowController.editReverseStatus("67ee8c06-76a9-4419-b8b7-0e26323b5326", 3, "未发生改为已退票");
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("修改成功", result.getMessage());
	}*/
	
	@Test
	@Sql("classpath:test/yunxin/flowController/editReverseStatus.sql")
	public void editReverseStatus_test6(){
		String response = flowController.editReverseStatus("8c06-76a9-4419-b8c7-0e26323brfr26", 2, "未冲账改为已冲账");
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("修改成功", result.getMessage());
	}
	
//	@Test
//	public void searchCashFlow_test1(){
//		String response = flowController.searchCashFlow("", "", "", "");
//		Result result = JsonUtils.parse(response, Result.class);
//		Map<String, Object> data = new HashMap<>();
//		Assert.assertEquals(data, result.getData());
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/flowController/searchCashFlow.sql")
//	public void searchCashFlow_test2(){//根据银行流水号查询
//		String response = flowController.searchCashFlow("207e3cd6-7gff0-11e46-a206e-98744232231898123111d", "", "", "");
//		Result result = JsonUtils.parse(response, Result.class);
//		Assert.assertNotNull(result.getData().get("list"));
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/flowController/searchCashFlow.sql")
//	public void searchCashFlow_test3(){//根据账户号查询
//		String response = flowController.searchCashFlow("", "600000000001", "", "");
//		Result result = JsonUtils.parse(response, Result.class);
//		Assert.assertNotNull(result.getData().get("list"));
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/flowController/searchCashFlow.sql")
//	public void searchCashFlow_test4(){//根据账户名查询
//		String response = flowController.searchCashFlow("", "", "云南国际信托有限公司", "");
//		Result result = JsonUtils.parse(response, Result.class);
//		Assert.assertNotNull(result.getData().get("list"));
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/flowController/searchCashFlow.sql")
//	public void searchCashFlow_test5(){//根据流水备注查询
//		String response = flowController.searchCashFlow("", "", "", "测试银行流水备注");
//		Result result = JsonUtils.parse(response, Result.class);
//		Assert.assertNotNull(result.getData().get("list"));
//	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/attachCashFlow.sql")
	//@Sql("classpath:test/yunxin/flowController/editReverseStatus.sql")
	public void attachCashFlow_test1(){
		String response1 = flowController.attachCashFlow("", "35a70d5d-b211-11e6-bedc-00163e002839", "");
		Result result1 = JsonUtils.parse(response1, Result.class);
		
		String response2 = flowController.attachCashFlow("28c06-76a9-4419-b8c7-0e26323fr26", "35a70d5d-b211-11e6-bedc-00163e002839", "");
		Result result2 = JsonUtils.parse(response2, Result.class);
		
		String response3 = flowController.attachCashFlow("33c06-76a9-4419-b8c7-0e26323fr26", "", "");
		Result result3 = JsonUtils.parse(response3, Result.class);
		
		String response4 = flowController.attachCashFlow("43c06-76a9-4419-b8c7-0ees323fr26", "35a70d5d-b211-11e6-bedc-00163e002839", "");
		Result result4= JsonUtils.parse(response4, Result.class);
		
		String response5 = flowController.attachCashFlow("98c06-76a9-4419-b8c7-0ees323f6", "35a70d5d-b211-11e6-bedc-00163e002839", "");
		Result result5= JsonUtils.parse(response5, Result.class);
		
		Assert.assertEquals("未找到对应的代付单", result1.getMessage());
		Assert.assertEquals("只允许未发生改为已退票", result2.getMessage());
		Assert.assertEquals("未找到添加的借记流水", result3.getMessage());
		Assert.assertEquals("流水金额与代付金额不等", result4.getMessage());
		Assert.assertEquals("0", result5.getCode());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/reRemittanceForFailedPlan.sql")
	@Ignore
	public void reRemittanceForFailedPlan_test6(){//执行状态成功 冲账状态，已退票
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		String response = flowController.reRemittanceForFailedPlan("64092-dffc-4f9f-bb46-d53bfref0", "放款失败，重新放款！", principal, request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("0", result.getCode());
		
	}

	@Test
	@Sql("classpath:test/yunxin/flowController/refundCombine.sql")
	public void testRefundCombine(){
		List<String> list = new ArrayList<>();
		
		String execReqNos = JsonUtils.toJsonString(list);
		String cashFlowUuid = "bb290571-ad98-11e7-b26b-525400dbb013";
		String executionRemark = "executionRemark";
		String response = flowController.refundCombine(execReqNos, cashFlowUuid, executionRemark);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的线上代付单", result.getMessage());
		
		list.add("8f5202f1-87ce-4e6d-be55-623c4fcb93cb");
		execReqNos = JsonUtils.toJsonString(list);
		response = flowController.refundCombine(execReqNos, "", executionRemark);
		Assert.assertEquals("未找到添加的借记流水", result.getMessage());
		
		execReqNos = JsonUtils.toJsonString(list);
		response = flowController.refundCombine(execReqNos, cashFlowUuid, executionRemark);
		Assert.assertEquals("流水金额与代付总金额不等", result.getMessage());
		
		list.add("50a66131-8c8f-4725-8cf4-c11cfe638a2a");
		execReqNos = JsonUtils.toJsonString(list);
		response = flowController.refundCombine(execReqNos, cashFlowUuid, executionRemark);
		Assert.assertEquals("0", result.getCode());
	}

}