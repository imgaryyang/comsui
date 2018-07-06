package com.zufangbao.earth.yunxin.web;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.web.controller.BankReconciliationController;
import com.zufangbao.wellsfargo.silverpool.cashauditing.webform.CashFlowDetailShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.webform.RelatedVoucherShowModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
public class BankReconciliationControllerTest  extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private BankReconciliationController bankReconciliationController;
	
	@Test
	@Sql("classpath:test/yunxin/test_getCashFlowDetail.sql")
	public void test_getCashFlowDetail(){
		String cashFlowUuid = "";
		String resultStr = bankReconciliationController.getCashFlowDetail(cashFlowUuid);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("流水信息错误！", result.getMessage());
		
		cashFlowUuid = "2b0b4455-34c5-11e7-bf99-00163e002839";
		resultStr = bankReconciliationController.getCashFlowDetail(cashFlowUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("信托合同不存在！", result.getMessage());
		
		cashFlowUuid = "3b0b4455-34c5-11e7-bf99-00163e002839";
		resultStr = bankReconciliationController.getCashFlowDetail(cashFlowUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertNotNull(result.getData().get("detail"));
		Assert.assertNull(result.getData().get("voucher"));
		
		CashFlowDetailShowModel showModel = JsonUtils.parse(JSON.toJSONString(result.getData().get("detail")) ,CashFlowDetailShowModel.class);
		Assert.assertEquals("3a0b44b2-34c5-11e7-bf99-00163e002839", showModel.getBankSequenceNo());
		
		cashFlowUuid = "1b0b4455-34c5-11e7-bf99-00163e002839";
		resultStr = bankReconciliationController.getCashFlowDetail(cashFlowUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertNotNull(result.getData().get("detail"));
		Assert.assertNotNull(result.getData().get("voucher"));
		
		showModel = JsonUtils.parse(JSON.toJSONString(result.getData().get("detail")) ,CashFlowDetailShowModel.class);
		RelatedVoucherShowModel voucher = JsonUtils.parse(JSON.toJSONString(result.getData().get("voucher")), RelatedVoucherShowModel.class);
		Assert.assertEquals("1a0b44b2-34c5-11e7-bf99-00163e002839", showModel.getBankSequenceNo());
		Assert.assertEquals("V1902447263644053501", voucher.getVoucherNo());
		
		
	}
	
	
}
