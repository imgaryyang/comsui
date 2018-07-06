package com.zufangbao.earth.factory;

import static com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory.CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN;
import static com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory.CONST_FILE_INFO_BATCH_NO;
import static com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory.CONST_FILE_INFO_PRODUCT_CODE;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.trade.ExternalTradeBatchDetailModel;
import com.zufangbao.sun.handler.trade.ExternalTradeBatchHandler;
import com.zufangbao.sun.service.FinancialContractService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml"})
@Transactional
public class ExternalTradeFileAnalyzeFactoryTest {

	@Autowired
	private ExternalTradeBatchHandler externalTradeBatchHandler;
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ExternalTradeFileAnalyzeFactory analyzeFactory;

	private final String productNoForJinDan = "900000004";
	private final String batchNoForJinDan = "10001";

	@Test
	@Sql("classpath:test/ExternalTradeBatchDetail.sql")
	public void testimportDataForExternalTradeBatch() {
		File file = new File("src/test/resources/fileFolder/dsout-900000004-10001-20161227.data");
		String productNo = "900000004";
		String batchNo = "10001";
		String tradeDate = "20160211";
		String operatorName = "zlf";
		String ipAddress = "127.0.0.1";
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productNo);
		List<ExternalTradeBatchDetailModel> list = new ArrayList<>();
		try {
			InputStream ins = new FileInputStream(file);
			list = analyzeFactory.analyzeDetail(ins, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		externalTradeBatchHandler.importDataForExternalTradeBatch(list, productNo, batchNo, operatorName,
				ipAddress, tradeDate);
	}

	@Test
	public void testSaveFile() {
		File file = new File("src/test/resources/fileFolder/dsout-900000004-10001-20161227.data");
		String localPath = "src/test/resources/saveFilePath";
		try {
			analyzeFactory.saveFile(file, localPath);
		} catch (CommonException e) {
			e.printStackTrace();
		}
	}

	/*@Test
	public void testSaveFileForNull() throws CommonException {
		File file = new File("src/test/resources/fileFolder/dsout-900000004-10001-20161227.data");
		try {
			analyzeFactory.saveFile(file, null);
			Assert.fail();
		} catch (CommonException e) {
			assertEquals(GlobalCodeSpec.CODE_FAILURE, e.getErrorCode());
		}
	}*/

	@Test
	public void testAnalyzeFileName() {
		File file = new File("src/test/resources/fileFolder/dsout-900000004-10001-20161227.data");
		String fileName = file.getName();
		Map<String, String> fileInfos = new HashMap<>();
		try {
			fileInfos = analyzeFactory.analyzeFileName(fileName, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
		} catch (CommonException e) {
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
		String productNo = fileInfos.get(CONST_FILE_INFO_PRODUCT_CODE);
		String batchNo = fileInfos.get(CONST_FILE_INFO_BATCH_NO);
		assertEquals(productNoForJinDan, productNo);
		assertEquals(batchNoForJinDan, batchNo);
	}

	/*@Test
	public void testAnalyzeFileNameForNull() {
		Map<String, String> fileInfos = new HashMap<>();
		try {
			fileInfos = analyzeFactory.analyzeFileName(null, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
			Assert.fail();
		} catch (CommonException e) {
			assertEquals(GlobalCodeSpec.CODE_FAILURE, e.getErrorCode());
		}
	}*/

	@Test
	public void testAnalyzeFileNameFoErrorType() {
		File file = new File("src/test/resources/fileFolder/dsout-900000004-10001-20161227.data");
		String fileName = file.getName();
		Map<String, String> fileInfos = new HashMap<>();
		try {
			fileInfos = analyzeFactory.analyzeFileName(fileName, "Error");
			Assert.fail();
		} catch (CommonException e) {
			assertEquals(GlobalCodeSpec.CODE_FAILURE, e.getErrorCode());
		}
	}

	@Test
	public void testAnalyzeDetail() {
		File file = new File("src/test/resources/fileFolder/dsout-900000004-10001-20161227.data");
		List<ExternalTradeBatchDetailModel> list = new ArrayList<>();
		try {
			InputStream ins = new FileInputStream(file);
			list = analyzeFactory.analyzeDetail(ins, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Test
	public void testAnalyzeDetailForNull() {
		List<ExternalTradeBatchDetailModel> list = new ArrayList<>();
		try {
			list = analyzeFactory.analyzeDetail(null, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
			Assert.fail();
		} catch (CommonException e) {
			assertEquals(GlobalCodeSpec.CODE_FAILURE, e.getErrorCode());
		}
	}

	@Test
	public void testAnalyzeDetailForErrorType() {
		List<ExternalTradeBatchDetailModel> list = new ArrayList<>();
		try {
			list = analyzeFactory.analyzeDetail(null, "error");
			Assert.fail();
		} catch (CommonException e) {
			assertEquals(GlobalCodeSpec.CODE_FAILURE, e.getErrorCode());
		}
	}

}
