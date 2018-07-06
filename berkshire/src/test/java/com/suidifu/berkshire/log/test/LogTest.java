package com.suidifu.berkshire.log.test;


import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.api.syncdata.model.BusinessLogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.OutputCapture;

import java.util.ArrayList;
import java.util.List;

public class LogTest {

	private static Log logger = LogFactory.getLog(LogTest.class);
	
	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void testSysout() {

		System.out.println("test stout");
		
		Assert.assertTrue(outputCapture.toString().contains("test stout"));

		
	}
	@Test
	public void testLog() throws Exception {
		
		logger.debug("test debug");
		
		Assert.assertFalse(outputCapture.toString().contains("test debug"));
		
		logger.error("test error...");
		
	}

	@Test
	public void testLog4J2() {
		List<String> idList = new ArrayList<>();
		long startTime = System.currentTimeMillis();
		for (int index = 0; index < 13; index++) {
			idList.add("" + index);
		}
        idList.forEach(this::writeLog);
//		idList.parallelStream().forEach(this::writeLog);
//		try {
			int sleepTime = 60000;
			System.out.println("\n\n\n\n\nuse:" + (System.currentTimeMillis() - startTime) + "ms. sleep:" + sleepTime + "ms.");
//			Thread.sleep(sleepTime);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

//		idList.forEach(this::writeLog);
	}

	private void writeLog(String id) {
		long startTime = System.currentTimeMillis();
		BusinessLogFactory.writeBusinessLog("index:" + id + ", snow flake id:" + UUIDUtil.snowFlakeIdString() + ",[{\"changeReason\"\"\n" +
						"    :2,\"contractNo\":\"云信信2016-241-DK(428522112675736881)\",\"createTime\":\"2017-04-27 21:17:18\",\"preOccurDate\":\"2017-05-02 " +
						"00:00:00\",\"preRepayPrinn\n" +
						"    cipal\":35000.00,\"preRepayProfit\":0,\"preTotalAmount\":35000.00,\"repayScheduleNo\":\"ZC1762624160098160640\",\"status\":0," +
						"\"uniqueId\":\"e568793f-a44c-44\n" +
						"    362-9e78-0ce433131f3e\"},{\"changeReason\":2,\"contractNo\":\"云信信2016-241-DK(428522112675736881)\",\"createTime\":\"2017-04-27 " +
						"21:17:18\",\"preOccurDaa\n" +
						"    te\":\"2017-05-12 00:00:00\",\"preRepayPrincipal\":25000.00,\"preRepayProfit\":0,\"preTotalAmount\":25000.00," +
						"\"repayScheduleNo\":\"ZC1762624160366596096\"\"\n" +
						"    ,\"status\":0,\"uniqueId\":\"e568793f-a44c-4362-9e78-0ce433131f3e\"},{\"changeReason\":2,\"contractNo\":\"云信信2016-241-DK" +
						"(428522112675736881)\",\"createe\n" +
						"    Time\":\"2017-04-27 21:17:19\",\"preOccurDate\":\"2017-05-22 00:00:00\",\"preRepayPrincipal\":20000.00,\"preRepayProfit\":0," +
						"\"preTotalAmount\":20000.00,\"rr\n" +
						"    epayScheduleNo\":\"ZC1762624161574555648\",\"status\":0,\"uniqueId\":\"e568793f-a44c-4362-9e78-0ce433131f3e\"}]",
				"syncLog");
//		try {
//			System.out.println("index:" + id + "use:" + (System.currentTimeMillis() - startTime) + "ms. start sleep 5000ms");
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
