package com.suidifu.bridgewater.controller;

import com.zufangbao.gluon.api.jpmorgan.model.BusinessStatus;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wsh
 */
public class RemittanceInnerCallbackControllerTest {
	@Test   //1.dbplan已成功
	public void testSingleAnalysisRemittanceResult() {
		QueryStatusResult queryStatusResult = new QueryStatusResult();
		queryStatusResult.setBusinessStatus(BusinessStatus.Success);
		queryStatusResult.setChannelAccountName("operator");
		queryStatusResult.setChannelAccountNo("001053110000001");
		//queryStatusResult.setCommunicationLastSentTime()
		//queryStatusResult.setBusinessSuccessTime("2017-08-22 17:07:20");
		queryStatusResult.setBusinessSuccessTime(new Date());
		queryStatusResult.setTransactionAmount(new BigDecimal("1000.00"));
		queryStatusResult.setBusinessResultMsg("交易?成功");
		queryStatusResult.setChannelSequenceNo(null);
		queryStatusResult.setSourceMessageUuid("70eca269-8a76-4407-9959-604258c9c297");
		queryStatusResult.setTradeUuid("97418423379677184");
	}

	@Test   //1.dbplan处于处理状态
	public void testSingleAnalysisRemittanceResultV2() {
		QueryStatusResult queryStatusResult = new QueryStatusResult();
		queryStatusResult.setBusinessStatus(BusinessStatus.Success);
		queryStatusResult.setChannelAccountName("operator");
		queryStatusResult.setChannelAccountNo("001053110000001");
		//queryStatusResult.setCommunicationLastSentTime()
		//queryStatusResult.setBusinessSuccessTime("2017-08-22 17:07:20");
		queryStatusResult.setTransactionAmount(new BigDecimal("1000.00"));
		queryStatusResult.setBusinessResultMsg("交易?成功");
		queryStatusResult.setChannelSequenceNo(null);
		queryStatusResult.setSourceMessageUuid("70eca269-8a76-4407-9959-604258c9c297");
		queryStatusResult.setTradeUuid("97418423379677184");
	}
}













