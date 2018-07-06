package com.suidifu.jpmorgan.handler;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.suidifu.jpmorgan.entity.unionpay.PaymentDetailInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.RealTimePaymentInfoModel;
import com.suidifu.jpmorgan.entity.unionpay.TransactionResultQueryInfoModel;
import com.suidifu.jpmorgan.handler.impl.UnionPayPacketFactory;

public class UnionPayPacketFactoryTest {
	
	
	@Test
	public void testgenerateRealTimePaymentPacket() {
		RealTimePaymentInfoModel model = new RealTimePaymentInfoModel();
		model.setUserName("testUserName");
		model.setReqNo("testReqNo");
		model.setMerchantId("testMerchantId");
		model.setTotalItem(1);
		model.setTotalSum(new BigDecimal("0.1"));
		
		PaymentDetailInfoModel detailInfo = new PaymentDetailInfoModel();
		detailInfo.setSn("testSn1");
		detailInfo.setBankCode("testBankCode1");
		detailInfo.setAccountName("testAccountName1");
		detailInfo.setIdNum("testIdNum");
		detailInfo.setAccountNo("testAccountNo1");
		detailInfo.setRemark("testRemark1");
		
		
		model.setDetailInfo(detailInfo);
		
		String xmlPacket = UnionPayPacketFactory.generateRealTimePaymentPacket(model);
		
		System.out.println(xmlPacket);
		
		assertTrue(xmlPacket.contains("<TRX_CODE>100005</TRX_CODE>"));
		assertTrue(xmlPacket.contains("<LEVEL>5</LEVEL>"));
		assertTrue(xmlPacket.contains("<USER_NAME>testUserName</USER_NAME>"));
		assertTrue(xmlPacket.contains("<USER_PASS></USER_PASS>"));
		assertTrue(xmlPacket.contains("<REQ_SN>testReqNo</REQ_SN>"));
		assertTrue(xmlPacket.contains("<BUSINESS_CODE></BUSINESS_CODE>"));
		assertTrue(xmlPacket.contains("<MERCHANT_ID>testMerchantId</MERCHANT_ID>"));
		assertTrue(xmlPacket.contains("<TOTAL_ITEM>1</TOTAL_ITEM>"));
		assertTrue(xmlPacket.contains("<TOTAL_SUM>10</TOTAL_SUM>"));
		assertTrue(xmlPacket.contains("<SN>testSn1</SN>"));
		assertTrue(xmlPacket.contains("<BANK_CODE>testBankCode1</BANK_CODE>"));
		assertTrue(xmlPacket.contains("<ACCOUNT_NO>testAccountNo1</ACCOUNT_NO>"));
		assertTrue(xmlPacket.contains("<ACCOUNT_NAME>testAccountName1</ACCOUNT_NAME>"));
		assertTrue(xmlPacket.contains("<AMOUNT>0</AMOUNT>"));
		assertTrue(xmlPacket.contains("<REMARK>testRemark1</REMARK>"));
		assertTrue(xmlPacket.contains("<ID_TYPE></ID_TYPE>"));
	}
	
	
	@Test
	public void testGenTransactionResultQueryPacket() {
		TransactionResultQueryInfoModel model = new TransactionResultQueryInfoModel();
		model.setUserName("testUserName");
		model.setReqNo("testReqNo");
		model.setQueryReqNo("testQueryReqNo");
		
		String xmlPacket = UnionPayPacketFactory.genTransactionResultQueryPacket(model);
		assertTrue(xmlPacket.contains("<TRX_CODE>200001</TRX_CODE>"));
		assertTrue(xmlPacket.contains("<USER_NAME>testUserName</USER_NAME>"));
		assertTrue(xmlPacket.contains("<USER_PASS></USER_PASS>"));
		assertTrue(xmlPacket.contains("<REQ_SN>testReqNo</REQ_SN>"));
		assertTrue(xmlPacket.contains("<QUERY_SN>testQueryReqNo</QUERY_SN>"));
	}

}
