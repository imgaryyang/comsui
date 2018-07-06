package com.suidifu.berkshire.mq.receiver.adapter;

import com.suidifu.berkshire.BaseTestContext;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.mq.rpc.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.*;

public class MqMessageReceiverTest extends BaseTestContext {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Autowired
	private MqMessageReceiver  mqMessageReceiver;
	
	@Test
	public void testHandleSyncMessageRequestForPrintString() {
		
		Request mqRequest = new Request();
		
		String requestUuid = UUID.randomUUID().toString();
		
		mqRequest.setUuid(requestUuid);
		mqRequest.setBean("mqRpcConsumerSideInterface");
		mqRequest.setMethod("printString");
		mqRequest.setParams(new Object[]{"test"});
		
		String queue = "";
		
		Response response = mqMessageReceiver.handleSyncMessage(mqRequest, queue);
		
		assertEquals("str=test",response.getResult());
		assertNull(response.getStackTrace());
		assertNull(response.getExceptionType());
	}
	@Test
	public void testHandleSyncMessageRequestForVoid() {
		
		Request mqRequest = new Request();
		
		String requestUuid = UUID.randomUUID().toString();
		
		mqRequest.setUuid(requestUuid);
		mqRequest.setBean("mqRpcConsumerSideInterface");
		mqRequest.setMethod("testVoid");
		
		String queue = "";
		
		Response response = mqMessageReceiver.handleSyncMessage(mqRequest, queue);
		
		assertNull(response.getResult());
		assertNull(response.getStackTrace());
		assertNull(response.getExceptionType());
	}
	@Test
	public void testHandleSyncMessageRequestForThrowNullPointException() {
		
		Request mqRequest = new Request();
		
		String requestUuid = UUID.randomUUID().toString();
		
		mqRequest.setUuid(requestUuid);
		mqRequest.setBean("mqRpcConsumerSideInterface");
		mqRequest.setMethod("throwException");
		
		String queue = "";
		
		Response response = mqMessageReceiver.handleSyncMessage(mqRequest, queue);
		
		assertNull(response.getResult());
		
//		assertEquals("stackTrace :",response.getStackTrace());
		
		assertTrue(response.getStackTrace().contains("java.lang.NullPointerException: null point exception"));
		assertEquals("java.lang.NullPointerException",response.getExceptionType());
	}
}
