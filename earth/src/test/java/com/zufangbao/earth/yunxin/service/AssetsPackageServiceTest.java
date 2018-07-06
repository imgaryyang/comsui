package com.zufangbao.earth.yunxin.service;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.StringUtils;
import com.zufangbao.earth.yunxin.handler.AssetPackageHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.NFQLoanInformation;
import com.zufangbao.sun.yunxin.entity.NFQRepaymentPlan;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
public class AssetsPackageServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private AssetPackageHandler assetPackageHandler;
	@Test
	@Sql("classpath:test/yunxin/assetPackage/Import_Fail_No_FinancialContractNo.sql")
	public void test_Import_Fail_No_FinancialContractNo() {
		Long financialContractId = 2L;
		FinancialContract financialContract = financialContractService.load(FinancialContract.class,financialContractId);
		Assert.assertTrue(StringUtils.isEmpty(financialContract.getContractNo()));
		String fileName = "./src/test/resources/test/yunxin/excel/2016-04-29-NFQ.xls";
		Result result = new Result();
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(input);

			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			result = assetPackageHandler.importAssetPackagesViaExcel(loanInformationList,repaymentPlanList,
					financialContractId, "test", "127.0.0.1");
			Assert.assertEquals("信托合同编号为空", result.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	@Sql("classpath:test/yunxin/assetPackage/Import_Fail_No_AssetPackageFormat.sql")
	public void test_Import_Fail_No_AssetPackageFormat() {
		Long financialContractId = 2L;
		FinancialContract financialContract = financialContractService.load(FinancialContract.class,financialContractId);
		Assert.assertNull(financialContract.getAssetPackageFormat());
		String fileName = "./src/test/resources/test/yunxin/excel/2016-04-29-NFQ.xls";
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(input);

			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			Result result = assetPackageHandler.importAssetPackagesViaExcel(loanInformationList,repaymentPlanList,
					financialContractId, "test", "127.0.0.1");
			Assert.assertEquals("信托合同中未设置资产包格式", result.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/assetPackage/Import_Fail_Exist_Contract.sql")
	public void test_Import_Fail_Exist_Contract() {
		Long financialContractId = 2L;
		String fileName = "./src/test/resources/test/yunxin/excel/2016-04-29-NFQ.xls";
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(input);

			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			assetPackageHandler.importAssetPackagesViaExcel(loanInformationList,repaymentPlanList,
					financialContractId, "test", "127.0.0.1");
		} catch (RuntimeException e) {
			Assert.assertEquals("贷款合同已存在，编号: 云信信2016-78-DK(ZQ2016042522479)", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/assetPackage/Import_Succ_NFQ.sql")
	public void test_Import_Fail_Wrong_Periods() {
		Long financialContractId = 2L;
		String fileName = "./src/test/resources/test/yunxin/excel/2016-04-29-NFQ-Wrong-Periods.xlsx";
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(input);

			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			assetPackageHandler.importAssetPackagesViaExcel(loanInformationList,repaymentPlanList,
					financialContractId, "test", "127.0.0.1");
		} catch (RuntimeException e) {
			Assert.assertEquals("还款计划期数错误, 编号: 云信信2016-78-DK(ZQ2016042522479)", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/assetPackage/Import_Succ_NFQ.sql")
	public void test_Import_NFQ_Fail_Wrong_Excel() {
		Long financialContractId = 2L;
		String fileName = "./src/test/resources/test/yunxin/excel/2016-04-29-NFQ-Wrong-Excel.xlsx";
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(input);

			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			assetPackageHandler.importAssetPackagesViaExcel(loanInformationList,repaymentPlanList,
					financialContractId, "test", "127.0.0.1");
		}  catch (RuntimeException e) {
			Assert.assertEquals("资产包格式错误，请检查", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Test
	@Sql("classpath:test/yunxin/assetPackage/Import_Succ_NFQ.sql")
	public void test_Import_NFQ_Succ_XLSX() {
		Long financialContractId = 2L;
		String fileName = "./src/test/resources/test/yunxin/excel/2016-04-29-NFQ.xlsx";
		Result result = new Result();
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(input);

			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			result = assetPackageHandler.importAssetPackagesViaExcel(loanInformationList,repaymentPlanList,
					financialContractId, "test", "127.0.0.1");
			Assert.assertEquals("导入成功", result.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	@Sql("classpath:test/yunxin/assetPackage/Import_Succ_NFQ.sql")
	public void test_Import_NFQ_Succ_XLS() {
		Long financialContractId = 2L;
		String fileName = "./src/test/resources/test/yunxin/excel/2016-04-29-NFQ.xls";
		Result result = new Result();
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(input);

			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			result = assetPackageHandler.importAssetPackagesViaExcel(loanInformationList,repaymentPlanList,
					financialContractId, "test", "127.0.0.1");
		Assert.assertEquals("导入成功", result.getMessage());
		}
		 catch (Exception e) {
			e.printStackTrace();
		}

	}




}
