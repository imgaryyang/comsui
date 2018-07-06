package com.suidifu.bridgewater.handler;

import com.zufangbao.sun.api.model.remittance.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;
import com.zufangbao.wellsfargo.deduct.handler.IPaymentChannelHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class RemittancePaymentChannelHandlerTest {
	
	@Autowired
	IPaymentChannelHandler remittancePaymentChannelHandler;
	
	@Test
	@Sql("classpath:test/remittance/test_qutoaPriority.sql")
	public void testGetFirstPaymentChannelSummaryInfo() {
		String financialContractUuid = "ace7522b-0b94-4f4f-8d6f-2e4b6744ef6b";
		BusinessType businessType = BusinessType.SELF;
		AccountSide accountSide = AccountSide.CREDIT;
		String bankCode = "";
		BigDecimal plannedAmount = new BigDecimal(5000);
		PaymentChannelSummaryInfo pcsi = remittancePaymentChannelHandler.getFirstPaymentChannelSummaryInfoBy(financialContractUuid, businessType, accountSide, bankCode, plannedAmount);
		Assert.assertEquals("df49620c-5b21-4c07-82af-4b8079655a32", pcsi.getChannelServiceUuid());
		Assert.assertEquals("G40900招行银企直连", pcsi.getPaymentChannelName());
	}
	
	@Test
	@Sql("classpath:test/remittance/test_qutoaPriority.sql")
	public void error_testGetFirstPaymentChannelSummaryInfo(){//超过限额
		String financialContractUuid = "ace7522b-0b94-4f4f-8d6f-2e4b6744ef6b";
		BusinessType businessType = BusinessType.SELF;
		AccountSide accountSide = AccountSide.CREDIT;
		String bankCode = "";
		BigDecimal plannedAmount = new BigDecimal(5000.01);
		PaymentChannelSummaryInfo pcsi = remittancePaymentChannelHandler.getFirstPaymentChannelSummaryInfoBy(financialContractUuid, businessType, accountSide, bankCode, plannedAmount);
		Assert.assertEquals("df49620c-5b21-4c07-82af-4b8079655a32", pcsi.getChannelServiceUuid());
		Assert.assertEquals("G40900招行银企直连", pcsi.getPaymentChannelName());
		
	}
	

}
