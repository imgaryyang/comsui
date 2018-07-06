package com.zufangbao.earth.report.wrapper;

import com.zufangbao.earth.report.factory.SqlCacheManager;
import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.sun.utils.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 报表包装基类
 * @author zhanghongbing
 *
 */
public class ReportBaseWrapper {
	
	public final static String UTF_8 = "UTF-8";
	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	
	/**
	 * 将多个HSSFWorkBook写入zip文件
	 * @param zipPathName
	 * @param workbookMap
	 * @throws IOException
	 */
	public void writeZipFileToDisk(String zipPathName, Map<String, HSSFWorkbook> workbookMap) throws IOException {
		File zipFile = new File(zipPathName);
		FileOutputStream out = FileUtils.openOutputStream(zipFile, false);
		ZipOutputStream zip = new ZipOutputStream(out);
		for (Entry<String, HSSFWorkbook> entry : workbookMap.entrySet()) {
			String zipEntryFileName = entry.getKey();
			HSSFWorkbook wb = entry.getValue();
			ZipEntry zipEntry = new ZipEntry(zipEntryFileName);
			zip.putNextEntry(zipEntry);
			wb.write(zip);
			wb.close();
			zip.flush();
		}
		zip.flush();
		zip.close();
	}
	
	public void exportExcelToClient(HttpServletResponse response, String fileName, String charset, HSSFWorkbook workBook) throws IOException {
		String encodeFileName = getEncodeFileName(fileName, charset);
		response.setContentType(GlobalSpec.Response.EXCEL_CONTENT_TYPE);
		response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + "; filename*="+ charset +"''"+ encodeFileName);
		
		OutputStream out = response.getOutputStream();
		out.flush();
		workBook.write(out);
		workBook.close();
		out.close();
	}

	public ZipOutputStream openZipOutputStream(HttpServletResponse response, String fileName) throws IOException {
		String encodeFileName = getEncodeFileName(fileName, UTF_8);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + "; filename*="+ UTF_8 +"''"+ encodeFileName);
		OutputStream out = response.getOutputStream();
		return new ZipOutputStream(out);
	}
	
	public PrintWriter putNextZipEntry(ZipOutputStream zip, String csvName) throws IOException {
		ZipEntry zipEntry = new ZipEntry(csvName + ".csv");
		zip.putNextEntry(zipEntry);	
		
		BufferedOutputStream bufStream = new BufferedOutputStream(zip);
		PrintWriter printWriter = new PrintWriter(bufStream);
		
		bufStream.flush();
		bufStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }); //避免csv乱码
		return printWriter;
	}
	
	public void closeZipOutputStream(ZipOutputStream zip, HttpServletResponse response) throws IOException {
		zip.flush();
		zip.close();
		response.flushBuffer();
	}
	
	private static String getEncodeFileName(String zipFileName, String charset) throws UnsupportedEncodingException {
		String encodeFileName = java.net.URLEncoder.encode(zipFileName, charset);
		encodeFileName = encodeFileName.replaceAll("\\+", "%20");
		return encodeFileName;
	}
	
	public String getCachedSql(String sqlId, Map<String, Object> params)
			throws Exception {
		Map<String, String> cachedSqlMap = sqlCacheManager.getCachedSqlMap();
		if(!cachedSqlMap.containsKey(sqlId)) {
			return null;
		}
		String templateSource = cachedSqlMap.get(sqlId);
		return FreemarkerUtil.process(templateSource, params);
	}
	
	public BigDecimal add(BigDecimal... amounts) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (BigDecimal amount : amounts) {
			if(amount != null) {
				totalAmount = totalAmount.add(amount);
			}
		}
		return totalAmount;
	}
	
	public BigDecimal add(String... amountStrs) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (String amountStr : amountStrs) {
			BigDecimal amount = new BigDecimal(amountStr);
			if(amount != null) {
				totalAmount = totalAmount.add(amount);
			}
		}
		return totalAmount;
	}
	
}
