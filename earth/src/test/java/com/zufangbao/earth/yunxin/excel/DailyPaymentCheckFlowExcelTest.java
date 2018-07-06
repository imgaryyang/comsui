package com.zufangbao.earth.yunxin.excel;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zufangbao.earth.util.MockUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml", })
public class DailyPaymentCheckFlowExcelTest extends
		AbstractTransactionalJUnit4SpringContextTests {


	@BeforeClass
	public static void init(){
		MockUtils.init();
	}
	
	@AfterClass
	public static void end(){
		MockUtils.end();
	}
	
/*	@Test
	@Sql("classpath:test/testDailyRetuenListExcelForBankAndRemark.sql")
	public void testDailyRetuenListExcelForBankAndRemark(){
		String queryDate = "2016-06-03";
		Long financialContractId = 1l;
		Principal principal = principalService.load(Principal.class, 1l);
		HSSFWorkbook  workBook =  dailyYunxinPaymentFlowCheckExcel.dailyReturnListExcel(principal,"0.00.0.0",queryDate, financialContractId);
		Sheet sheet = workBook.getSheetAt(0);
		Assert.assertEquals(2, sheet.getPhysicalNumberOfRows());
		Row firstRow = sheet.getRow(0);
		Row secondRow = sheet.getRow(1);
	    Assert.assertEquals("备注", XSSFUtils.getCellContent(firstRow.getCell(9)));
	    Assert.assertEquals("银行", XSSFUtils.getCellContent(firstRow.getCell(10)));
	    Assert.assertEquals("最新更新时间", XSSFUtils.getCellContent(firstRow.getCell(11)));
	    Assert.assertEquals("贷款合同编号", XSSFUtils.getCellContent(firstRow.getCell(12)));
	    Assert.assertEquals("签名工具类异常！", XSSFUtils.getCellContent(secondRow.getCell(9)));
	    Assert.assertEquals("中国银行滨江支行", XSSFUtils.getCellContent(secondRow.getCell(10)));
	    Assert.assertEquals("2016-06-03 11:39:30", XSSFUtils.getCellContent(secondRow.getCell(11)));
	    Assert.assertEquals("云信信2016-78-DK(ZQ2016042522479)", XSSFUtils.getCellContent(secondRow.getCell(12)));
	} 
	@Test
	@Sql("classpath:test/testDailyRetuenListExcelForContractNoAndLastModifyTime.sql")
	public void testDailyRetuenListExcelForContractNoAndLastModifyTime(){
		String queryDate = "2016-06-03";
		Long financialContractId = 1l;
		Principal principal = principalService.load(Principal.class, 1l);
		HSSFWorkbook  workBook =  dailyYunxinPaymentFlowCheckExcel.dailyReturnListExcel(principal,"0.00.0.0",queryDate, financialContractId);
		Sheet sheet = workBook.getSheetAt(0);
		Assert.assertEquals(2, sheet.getPhysicalNumberOfRows());
		Row firstRow = sheet.getRow(0);
		Row secondRow = sheet.getRow(1);
		Assert.assertEquals("备注", XSSFUtils.getCellContent(firstRow.getCell(9)));
	    Assert.assertEquals("银行", XSSFUtils.getCellContent(firstRow.getCell(10)));
	    Assert.assertEquals("最新更新时间", XSSFUtils.getCellContent(firstRow.getCell(11)));
	    Assert.assertEquals("贷款合同编号", XSSFUtils.getCellContent(firstRow.getCell(12)));
	    Assert.assertEquals("云信信2016-78-DK(ZQ2016042522479)", XSSFUtils.getCellContent(secondRow.getCell(12)));
	    Assert.assertEquals("2016-06-03 11:39:30", XSSFUtils.getCellContent(secondRow.getCell(11)));
	    Assert.assertEquals("0", XSSFUtils.getCellContent(secondRow.getCell(1)));
	    Filter filter = new Filter();
	    TransferApplication transferApplication = transferApplicationService.list(TransferApplication.class, filter).get(0);
	    Assert.assertEquals("1210.20", transferApplication.getAmount().toString());
	} */

	
	
}
