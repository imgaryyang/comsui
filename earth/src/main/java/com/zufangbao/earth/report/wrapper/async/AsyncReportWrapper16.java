package com.zufangbao.earth.report.wrapper.async;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.earth.report.model.ReportJobBaseModel;
import com.zufangbao.earth.report.wrapper.ReportBaseWrapper;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class AsyncReportWrapper16 extends ReportBaseWrapper implements IAsyncReportWrapper<ReportJobBaseModel> {

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Override
	public void checkParams(ReportJobBaseModel paramsBean) throws ExportException {
		Date startDateAsDay = paramsBean.getQueryStartDateAsDay();
		Date endDateAsDay = paramsBean.getQueryEndDateAsDay();
		if(startDateAsDay == null || endDateAsDay == null) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "查询时间不能为空！");
		}
		
		if(startDateAsDay.compareTo(endDateAsDay) > 0) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "查询起始日期不得晚于截止日期！");
		}
		
		if(CollectionUtils.isEmpty(paramsBean.getFinancialContractUuidList())) {
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "查询项目不能为空！");
		}
	}
	
	@Override
	public ExportEventLogModel asyncWrap(ReportJobBaseModel paramsBean, String zipPathName, ExportEventLogModel exportEventLogModel)
			throws Exception {
		
		String queryStartDate = paramsBean.getQueryStartDate();
		String queryEndDate = paramsBean.getQueryEndDate();
		
		Map<String, Object> params = buildParams(queryStartDate, queryEndDate);
		List<String> financialContractUuidList = paramsBean.getFinancialContractUuidList();
		
		exportEventLogModel.recordStartLoadDataTime();
		
		List<Integer> recordSizes = new ArrayList<Integer>();
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (String financialContractUuid : financialContractUuidList) {
			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
			if(financialContract == null) {
				continue;
			}
			HSSFSheet sheet = workbook.createSheet(financialContract.getContractNo());
			writeReportHeader(workbook, sheet, queryStartDate, queryEndDate);
			
			params.put("financialContractUuid", financialContractUuid);
			String sql = getCachedSql("reportWrapper16", params);
			
			SqlRowSet rs = genericDaoSupport.queryForRowSet(sql, params);
			
			int recordSize = writeReportDatas(workbook, sheet, rs, financialContract);
			recordSizes.add(recordSize);
		}
		exportEventLogModel.recordAfterLoadDataComplete(recordSizes.toArray(new Integer[recordSizes.size()]));
		
		String fileName = String.format("部分还款报表_%s_%s.xls", queryStartDate, queryEndDate);
		
		Map<String, HSSFWorkbook> workbookMap = new HashMap<String, HSSFWorkbook>();
		workbookMap.put(fileName, workbook);
		
		writeZipFileToDisk(zipPathName, workbookMap);
		return exportEventLogModel;
	}
	
	private Map<String, Object> buildParams(String queryStartDate, String queryEndDate) throws ExportException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		Date startDate = DateUtils.asDay(queryStartDate);
		Date endDate = DateUtils.asDay(queryEndDate);
		
		params.put("startDate", startDate);
		params.put("endDate", DateUtils.addDays(endDate, 1));
		return params;
	}

	private void writeReportHeader(HSSFWorkbook wb, HSSFSheet sheet, String startDate, String endDate) {
		String[] headerColumns = {"信托产品代码","信托产品合同","贷款合同编号","期初余额","本期新增","本期减少","期末余额"};
		HSSFRow row;
		HSSFCell cell;
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//标题行
		row = sheet.createRow(0);
		cell = row.createCell(0);
		String title = String.format("部分还款报表（%s至%s）", startDate, endDate);
		cell.setCellValue(title);
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 6));

		//报表头
		row = sheet.createRow(3);
		for (int i = 0; i < headerColumns.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerColumns[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn(i);
		}
		
	}
	
	private int writeReportDatas(HSSFWorkbook wb, HSSFSheet sheet, SqlRowSet rs, FinancialContract financialContract) {
		int recordSize = 0;
		BigDecimal totalInitialBalance = BigDecimal.ZERO;
		BigDecimal totalAddWithinInterval = BigDecimal.ZERO;
		BigDecimal totalReduceWithinInterval = BigDecimal.ZERO;
		BigDecimal totalClosingBalance = BigDecimal.ZERO;
		
		HSSFRow row;
		HSSFCell cell;
		int startRow = 5;
		while (rs.next()) {
			BigDecimal initialBalance = rs.getBigDecimal("beforeStartDateAmount");
			BigDecimal beforeEndDateAmount = rs.getBigDecimal("beforeEndDateAmount");
			BigDecimal totalAmount = rs.getBigDecimal("totalAmount");
			String contractNo = rs.getString("contractNo");
			
			BigDecimal reduceWithinInterval = BigDecimal.ZERO;
			//累计足额还款时，本期减少=期初余额+本期新增，否则为0
			if(beforeEndDateAmount.compareTo(totalAmount) == 0) {
				reduceWithinInterval = beforeEndDateAmount;
			}
			BigDecimal addWithinInterval = beforeEndDateAmount.subtract(initialBalance);
			BigDecimal closingBalance = beforeEndDateAmount.subtract(reduceWithinInterval);
			
			row = sheet.createRow(startRow++);
			
			cell = row.createCell(2);
			cell.setCellValue(contractNo);
			//期初余额
			cell = row.createCell(3);
			cell.setCellValue(formatSkipZero(initialBalance));
			//本期新增
			cell = row.createCell(4);
			cell.setCellValue(formatSkipZero(addWithinInterval));
			//本期减少
			cell = row.createCell(5);
			cell.setCellValue(formatSkipZero(reduceWithinInterval));
			//期末余额
			cell = row.createCell(6);
			cell.setCellValue(formatSkipZero(closingBalance));
			
			totalInitialBalance = totalInitialBalance.add(initialBalance);
			totalAddWithinInterval = totalAddWithinInterval.add(addWithinInterval);
			totalReduceWithinInterval = totalReduceWithinInterval.add(reduceWithinInterval);
			totalClosingBalance = totalClosingBalance.add(closingBalance);
			recordSize++;
		}
		writeReportSummaryData(sheet, financialContract, totalInitialBalance, totalAddWithinInterval, totalReduceWithinInterval, totalClosingBalance);
		return recordSize;
	}

	private void writeReportSummaryData(HSSFSheet sheet,
			FinancialContract financialContract,
			BigDecimal totalInitialBalance, BigDecimal totalAddWithinInterval,
			BigDecimal totalReduceWithinInterval, BigDecimal totalClosingBalance) {
		HSSFRow row = sheet.createRow(4);
		HSSFCell cell;
		
		cell = row.createCell(0);
		cell.setCellValue(financialContract.getContractNo());
		
		cell = row.createCell(1);
		cell.setCellValue(financialContract.getContractName());
		
		cell = row.createCell(2);
		cell.setCellValue("总计");
		//期初余额
		cell = row.createCell(3);
		cell.setCellValue(formatSkipZero(totalInitialBalance));
		//本期新增
		cell = row.createCell(4);
		cell.setCellValue(formatSkipZero(totalAddWithinInterval));
		//本期减少
		cell = row.createCell(5);
		cell.setCellValue(formatSkipZero(totalReduceWithinInterval));
		//期末余额
		cell = row.createCell(6);
		cell.setCellValue(formatSkipZero(totalClosingBalance));
	}
	
	private String formatSkipZero(BigDecimal bigDecimal) {
		if(bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
			return "-";
		}
		return bigDecimal.toString();
	}
	
}
