package com.suidifu.jpmorgan.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.gluon.api.jpmorgan.enums.AccountSide;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.opensdk.HttpClientUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
public class TradeScheduleControllerTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	
	@Test
	public void mockPaymentRequest() {
		while (true) {
			
			try {
				//模拟用钱宝 fstPaymentChannelUuid = f8bb9956-1952-4893-98c8-66683d25d7ce
				
				String batchUuid = UUID.randomUUID().toString();
				String fstOutlierUuid = UUID.randomUUID().toString();
				String fstPaymentChannelUuid = "f8bb9956-1952-4893-98c8-66683d25d7ce";
				String executionPrecond = "{\"abandon\":[\""+ fstOutlierUuid +"\"],\"start\":[\""+ fstOutlierUuid +"\"]}";
				TradeSchedule fstTradeSchedule = new TradeSchedule(AccountSide.CREDIT, "深圳一号", "11021315964008", "", "{\"bankCity\":\"310100\",\"bankCode\":\"C10104\",\"bankName\":\"中国银行\",\"bankProvince\":\"310000\"}", "for用钱宝测试客户", fstOutlierUuid, UUID.randomUUID().toString(), fstPaymentChannelUuid, null, new BigDecimal("0.07"), batchUuid, null, null, null, null);
				
				TradeSchedule sndTradeSchedule = new TradeSchedule(AccountSide.CREDIT, "超级网银", "88881000200003444", "", "{\"bankCity\":\"310100\",\"bankCode\":\"C10102\",\"bankName\":\"工商银行\",\"bankProvince\":\"310000\"}", "for用钱宝测试商户", UUID.randomUUID().toString(), UUID.randomUUID().toString(), fstPaymentChannelUuid, null, new BigDecimal("0.09"), batchUuid, executionPrecond, null, null, null);
				
				List<TradeSchedule> tradeScheduleList = new ArrayList<TradeSchedule>();
				tradeScheduleList.add(fstTradeSchedule);
				tradeScheduleList.add(sndTradeSchedule);
				
				Result result = HttpClientUtils.executePostRequest("http://127.0.0.1:9091/jpmorgan/tradeSchedule/batchPayment", JsonUtils.toJsonString(tradeScheduleList), null);
				
				System.out.println(JsonUtils.toJsonString(result));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
