package com.suidifu.jpmorgan.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.jpmorgan.entity.AccountSide;
import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.exception.paymentorder.PaymentOrderListOfNullException;
import com.suidifu.jpmorgan.exception.paymentorder.PaymentOrderOfNullException;
import com.suidifu.jpmorgan.handler.PaymentOrderHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
public class PaymentOrderHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	
	@Autowired
	private PaymentOrderHandler paymentOrderHandler;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Test
	@Sql("classpath:test/paymentOrderController.sql")
	public void paymentOrderControllerTest() {
		
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("accessVersion", UUID.randomUUID().toString());
		map.put("sourceAccountName", "");
		map.put("sourceAccountNo", "571907757810703");
		map.put("sourceAccountAppendix", null);
		map.put("sourceBankInfo","CB");
		map.put("destinationAccountName","张建明");
		map.put("destinationAccountNo", "6226227705568300");
		
		map.put("destinationAccountAppendix", null);
		map.put("destinationBankInfo", "305100000012");
		map.put("postscript", "超级网银测试");
		
		map.put("currencyCode", "10");
		map.put("accountSide", AccountSide.CREDIT);
		map.put("transactionUuid", UUID.randomUUID().toString());
		map.put("gatewayType", GatewayType.SuperBank);
		map.put("transactionAmount",new BigDecimal("0.01"));
		
		list.add(map);
		
		String stringTradeScheduleList = JSON.toJSONString(list);
		ArrayList<PaymentOrder> listPaymentOrder = (ArrayList<PaymentOrder>) JsonUtils.parseArray(stringTradeScheduleList, PaymentOrder.class);
		//paymentOrderHandler.paymentOrderListInitAndSave(listPaymentOrder, "");
		List<PaymentOrder> paymentOrderList = genericDaoSupport.searchForList("FROM PaymentOrder");
		
		Assert.assertEquals(1, paymentOrderList.size());
		Assert.assertEquals("超级网银测试", paymentOrderList.get(0).getPostscript());
		Assert.assertEquals("305100000012", paymentOrderList.get(0).getDestinationBankInfo());
		
	}
	
	@Test
	@Sql("classpath:test/paymentOrderController.sql")
	public void paymentOrderControllerTest_1() {
		
		List<Map> list = new ArrayList<Map>();
		for(int i = 0; i<10; i++) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("accessVersion", UUID.randomUUID().toString());
			map.put("sourceAccountName", "");
			map.put("sourceAccountNo", "571907757810703");
			map.put("sourceAccountAppendix", null);
			map.put("sourceBankInfo","CB");
			map.put("destinationAccountName","张建明");
			map.put("destinationAccountNo", "6226227705568300");
			map.put("destinationAccountAppendix", null);
			map.put("destinationBankInfo", "305100000012");
			map.put("postscript", "超级网银测试");
			map.put("currencyCode", "10");
			map.put("accountSide", AccountSide.CREDIT);
			map.put("transactionUuid", UUID.randomUUID().toString());
			map.put("gatewayType", GatewayType.SuperBank);
			map.put("transactionAmount",new BigDecimal("0.01"));

			list.add(map);
		}
		
		String stringTradeScheduleList = JSON.toJSONString(list);
		ArrayList<PaymentOrder> listPaymentOrder = (ArrayList<PaymentOrder>) JsonUtils.parseArray(stringTradeScheduleList, PaymentOrder.class);
		//paymentOrderHandler.paymentOrderListInitAndSave(listPaymentOrder, "");
		
		List<PaymentOrder> paymentOrderList = genericDaoSupport.searchForList("FROM PaymentOrder");
		Assert.assertEquals(10, paymentOrderList.size());
		Assert.assertEquals(GatewayType.SuperBank, paymentOrderList.get(0).getGatewayType());
		Assert.assertEquals(AccountSide.CREDIT, paymentOrderList.get(0).getAccountSide());
		
	}
	
	@Test(expected = PaymentOrderOfNullException.class)
	@Sql("classpath:test/paymentOrderController.sql")
	public void paymentOrderOfNullException() {
		
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("accessVersion", UUID.randomUUID().toString());
		map.put("sourceAccountName", "");
		map.put("sourceAccountNo", "571907757810703");
		map.put("sourceAccountAppendix", null);
		map.put("sourceBankInfo","CB");
		map.put("destinationAccountName","张建明");
		map.put("destinationAccountNo", "6226227705568300");
		
		map.put("destinationAccountAppendix", null);
		map.put("destinationBankInfo", "305100000012");
		map.put("postscript", "超级网银测试");
		
		map.put("currencyCode", "10");
		map.put("accountSide", AccountSide.CREDIT);
		map.put("transactionUuid", UUID.randomUUID().toString());
//		map.put("gatewayType", GatewayType.SuperBank);
//		map.put("transactionAmount",new BigDecimal("0.01"));
	
		list.add(map);
		String stringTradeScheduleList = JSON.toJSONString(list);
		ArrayList<PaymentOrder> listPaymentOrder = (ArrayList<PaymentOrder>) JsonUtils.parseArray(stringTradeScheduleList, PaymentOrder.class);
		//paymentOrderHandler.paymentOrderListInitAndSave(listPaymentOrder, "");
	

	}
	
	@Test(expected = PaymentOrderListOfNullException.class)
	@Sql("classpath:test/paymentOrderController.sql")
	public void paymentOrderListOfNullException() {
		
		List<Map> list = new ArrayList<Map>();
		String stringTradeScheduleList = JSON.toJSONString(list);
		ArrayList<PaymentOrder> listPaymentOrder = (ArrayList<PaymentOrder>) JsonUtils.parseArray(stringTradeScheduleList, PaymentOrder.class);
		//paymentOrderHandler.paymentOrderListInitAndSave(listPaymentOrder, "");
	

	}
	
	
}
