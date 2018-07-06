package com.zufangbao.earth.util;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;

public class MessageUtilsTest {
	
	private static Log syncCreateRepaymentPlanLogger = LogFactory.getLog("syncCreateRepaymentPlan");
	
	private static Log syncReapymentPlanExecutionStatusLogger = LogFactory.getLog("syncReapymentPlanExecutionStatus");
	private static Log syncModifyRepaymentPlanLogger = LogFactory.getLog("syncModifyRepaymentPlan");
	private static Log syncUpdateOverdueReapymentPlanLogger = LogFactory.getLog("syncUpdateOverdueReapymentPlan");

	@Test
	public void testReadWriteFile() {
		for (int i = 0; i < 10; i++) {
			syncReapymentPlanExecutionStatusLogger.info("test-" + i + "-" + UUID.randomUUID().toString());
			syncModifyRepaymentPlanLogger.info("test-" + i + "-" + UUID.randomUUID().toString());
			syncUpdateOverdueReapymentPlanLogger.info("test-" + i + "-" + UUID.randomUUID().toString());
		}
	}
}
