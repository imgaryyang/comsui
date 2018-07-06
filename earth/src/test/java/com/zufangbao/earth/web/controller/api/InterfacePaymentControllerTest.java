package com.zufangbao.earth.web.controller.api;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.web.controller.InterfacePaymentController;
import com.zufangbao.gluon.resolver.PageViewResolver;
import com.zufangbao.sun.yunxin.entity.deduct.InterfacePaymentQueryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@Transactional()
@WebAppConfiguration(value="webapp")
public class InterfacePaymentControllerTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private InterfacePaymentController interfacePaymentController;
	@Autowired
	public PageViewResolver pageViewResolver;
	
	@Test
	@Sql("classpath:test/yunxin/deductQuery/testGetInterfacePaymentShowModel.sql")
	public void testInterfacePaymentController4queryInterafacePayment() {
		InterfacePaymentQueryModel interfacePaymentQueryModel = new InterfacePaymentQueryModel();
		Page page = new Page();
		interfacePaymentQueryModel.setFinancialContractIds("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		interfacePaymentQueryModel.setExecutionStatus("[2]");
		String resultStr = interfacePaymentController.queryInterafacePayment(interfacePaymentQueryModel, page);
		
		Result result = JSON.parseObject(resultStr, Result.class);
		Map<String, Object> dataMap = result.getData();
		
		assertEquals("{\"code\":\"0\",\"data\":{\"deductApplicationQeuryModel\":{\"executionStatus\":\"[2]\",\"executionStatusEnumList\":[\"SUCCESS\"],\"financialContractIdList\":[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"],\"financialContractIds\":\"[\\\"d2812bc5-5057-4a91-b3fd-9019506f0499\\\"]\",\"paymentGatewayList\":[]},\"list\":[{\"accountCustomerName\":\"王宝\",\"bankAccountNo\":\"6228480444455553333\",\"bankName\":\"中国农业银行 \",\"deductAmount\":910.00,\"deductApplicationNo\":\"eb3d66fb-649f-4cc7-9a7c-15d202638691\",\"deductOccurDate\":\"2016-10-25 10:51:56\",\"deductPlanId\":291,\"deductPlanNo\":\"ae5c6232-54b3-4041-a67a-1fc9e0b591d4\",\"paymentGateway\":\"宝付\",\"remark\":\"交易成功\",\"status\":\"成功\"}],\"size\":1}}", resultStr);
	}
//	@Test
//	@Sql("classpath:test/yunxin/deductQuery/testGetInterfacePaymentShowModel.sql")
//	public void testInterfacePaymentController4showInterfacePaymentDetail() {
//		Principal principal = new Principal();
//		ModelAndView result = interfacePaymentController.showInterfacePaymentDetail(287L, principal);
//		assertEquals("[接口支付单不存在！！！！！]", result.getModel().values().toString());
//	}
//
//	@Test
//	@Sql("classpath:test/yunxin/deductQuery/testGetInterfacePaymentShowModel.sql")
//	public void testInterfacePaymentController4showInterfacePaymentDetail1() {
//		Principal principal = new Principal();
//		ModelAndView result = interfacePaymentController.showInterfacePaymentDetail(290L, principal);
//		DeductPlanDetailShowModel detailModel = (DeductPlanDetailShowModel)result.getModelMap().get("detailModel");
//		assertEquals("be2c6dd0-4514-4a3a-8dc8-ea4cee299102", detailModel.getDeductPlanNo());
//		assertEquals("1510.00", detailModel.getOccurAmount());
//		assertEquals("王宝", detailModel.getPayerName());
//		assertEquals("342c1ff6-0684-4422-a58e-e135aefe468e", detailModel.getPaymentInterfaceNo());
//
//	}
//
//	@Test//需要重新写
//	@Sql("classpath:test/yunxin/deductQuery/testGetInterfacePaymentShowModel.sql")
//	public void testInterfacePaymentController4showInterfacePayment() {
//		Principal principal = new Principal();
//		ModelAndView result = interfacePaymentController.showInterfacePayment(principal, null);
//		List<FinancialContract> financialContracts = (List<FinancialContract>)result.getModelMap().get("financialContracts");
//		assertEquals(0, financialContracts.size());
//
//	}
}
