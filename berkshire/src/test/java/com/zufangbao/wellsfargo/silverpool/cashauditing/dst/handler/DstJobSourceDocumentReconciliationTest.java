package com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler;

import com.suidifu.berkshire.BaseTestContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DstJobSourceDocumentReconciliationTest extends BaseTestContext{
	
	@Autowired
	@Qualifier("dstJobSourceDocumentReconciliation")
	private DstJobSourceDocumentReconciliation dstJobSourceDocumentReconciliation;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	/**
	 * 测试dstJobSourceDocumentReconciliation的方法名和参数类型是否改过
	 * @throws Exception
	 */
	@Test
	public void testIsTargetMethodNameAndParameterTypeExist() throws Exception {
		
		List<String> methodNames = new ArrayList<String>();
		
		methodNames.add("criticalMarker");
		methodNames.add("validateSourceDocumentDetailList");
		methodNames.add("fetch_virtual_account_and_business_payment_voucher_transfer");
		methodNames.add("source_document_recover_details");
		
		int methodNameExistCounter = 0;
		
		Method[] methods = dstJobSourceDocumentReconciliation.getClass().getMethods();
		
		for (Method method : methods) {
		
			if(methodNames.contains(method.getName())){
				methodNameExistCounter++;
			}
		}
		assertEquals(4,methodNameExistCounter);
		
	}
//	@Test
//	@Sql("file:src/test/resources/test/yunxin/voucher/testGetContractUuidAndSourceDocumentDetailUuidListBy.sql")
//	public void testCriticalMarker() {
//		
//		List<String> sourceDocumentDetailUuidList = new ArrayList<>();
//		
//		sourceDocumentDetailUuidList.add("eff22cef-2b28-4548-93b9-9e7ecd5b097f");
//		sourceDocumentDetailUuidList.add("225d4e01-bf37-4d0a-8c33-ddaa5c28f062");
//		
//		Map<String,String> map = dstJobSourceDocumentReconciliation.criticalMarker(sourceDocumentDetailUuidList);
//		
//		assertEquals(1,map.keySet().size());
//		
//		assertTrue(map.containsKey("225d4e01-bf37-4d0a-8c33-ddaa5c28f062"));
//		
//		assertEquals("1217",map.get("225d4e01-bf37-4d0a-8c33-ddaa5c28f062"));
//		
//		map = dstJobSourceDocumentReconciliation.criticalMarker(null);
//		
//		assertEquals(0,map.keySet().size());
//		
//		map = dstJobSourceDocumentReconciliation.criticalMarker(Collections.emptyList());;
//		
//		assertEquals(0,map.keySet().size());
//	}
	
//	@Test(expected=IllegalStateException.class)
//	@Sql("classpath:test/yunxin/voucher/testGetContractUuidAndSourceDocumentDetailUuidListBy.sql")
//	public void testCriticalMarkerWithExeption() {
//		
//		List<String> sourceDocumentDetailUuidList = new ArrayList<>();
//		
//		sourceDocumentDetailUuidList.add("06016b5f-0cb6-40b0-9242-0ccec7441a62");
//		sourceDocumentDetailUuidList.add("eff22cef-2b28-4548-93b9-9e7ecd5b097f");
//		sourceDocumentDetailUuidList.add("225d4e01-bf37-4d0a-8c33-ddaa5c28f062");
//		
//		dstJobSourceDocumentReconciliation.criticalMarker(sourceDocumentDetailUuidList);
//		
//	}
//	@Test
//	public void testValidateSourceDocumentDetailList() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testFetch_virtual_account_and_business_payment_voucher_transfer() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSource_document_recover_details() {
//		fail("Not yet implemented");
//	}

}
