package com.zufangbao.earth.web.controller;

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

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.yunxin.web.controller.audit.RemittanceAuditController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
@Transactional
public class RemittanceAuditControllerTest {
	
	@Autowired
	RemittanceAuditController remittanceAuditController;
	
	@Test
	public void check_opposite_execution_status_test1(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "", request);
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
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "c6AWgdDFGYxV2uZrWyY", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("通讯失败", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test4(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "c6AWgdDFGYxV2uZrWyY", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("查询失败", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test5(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "e104b7e4d9734baa8623", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的放款单", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test6(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "c0c5c8bb88a7408193fc", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的计划明细单", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	public void check_opposite_execution_status_test7(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "b09b1a65a3dc48fcae31", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("未找到对应的计划单", result.getMessage());
	}
	
	@Test
	@Sql("classpath:test/yunxin/flowController/check_opposite_execution_status.sql")
	@Ignore
	public void check_opposite_execution_status_test8(){
		Principal principal = new Principal();
		principal.setName("zhenghangbo");
		HttpServletRequest request = null;
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "50b2c7833cbf42f08ee7", request);
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
		
		String response = remittanceAuditController.checkOppositeExecutionStatus(principal, "3437c6bd503442fb86fa", request);
		Result result = JsonUtils.parse(response, Result.class);
		Assert.assertEquals("核单成功", result.getMessage());
	}

}
