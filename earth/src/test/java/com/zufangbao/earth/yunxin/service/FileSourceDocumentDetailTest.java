package com.zufangbao.earth.yunxin.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.csv.CsvUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"})
public class FileSourceDocumentDetailTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	
	@Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;
	
	
	@Sql("classpath:test/yunxin/sourceDocementDetailAddField/sourceDocumentDetail.sql")
    @Test
    public void sourceDocumentDetailFile() throws IOException{
		List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.getDetailsBySourceDocumentUuid("7a134112-549e-46e0-80b5-ea189d857f5a", "75daa729-68d5-44cd-b042-15cca6fa037a");
		String path = "src/test/resources/fileFolder/sourceDocumentDetail.csv";
		ExcelUtil<SourceDocumentDetail> excelUtil = new ExcelUtil<>(SourceDocumentDetail.class);
		List<String> CsvStr = excelUtil.exportDatasToCSV(sourceDocumentDetails);
		CsvUtils.createCSVFile(CsvStr, path);
		
		File file = new ClassPathResource("fileFolder/sourceDocumentDetail.csv").getFile();
		List<SourceDocumentDetail> sDetails = CsvUtils.readFromFile(file, SourceDocumentDetail.class);
		SourceDocumentDetail sDetail = sDetails.get(0);
		Assert.assertEquals(new BigDecimal(1.01), sDetail.getAmount());
		Assert.assertEquals(DateUtils.parseDate("2017-11-17 22:04:11", "yyyy-MM-dd HH:mm:ss"), sDetail.getActualPaymentTime());
		Assert.assertEquals("69c02558-412c-4c76-9811-e92c2f2b55bc", sDetail.getContractUniqueId());
		//Assert.assertEquals("", sDetail.getDetailAmountMap());
		
		
		
		
	}
	
	@Sql("classpath:test/yunxin/sourceDocementDetailAddField/sourceDocumentDetail.sql")
    @Test
	public void test() throws IOException{
		List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.getDetailsBySourceDocumentUuid("7a134112-549e-46e0-80b5-ea189d857f5a", "75daa729-68d5-44cd-b042-15cca6fa037a");
		String path = "src/test/resources/fileFolder/sourceDocumentDetail.csv";
		List<String> sourceDocumentDetailsString = new ArrayList<>();
		sourceDocumentDetailsString.add(JsonUtils.toJsonString(sourceDocumentDetails.get(0)));
		sourceDocumentDetailsString.add(JsonUtils.toJsonString(sourceDocumentDetails.get(0)));
		CsvUtils.createCSVFile(sourceDocumentDetailsString, path);
		
		List<SourceDocumentDetail> sDetails = FileUtils.readJsonList(path, SourceDocumentDetail.class);
		SourceDocumentDetail sDetail = sDetails.get(0);
		Assert.assertEquals(new BigDecimal("1.01"), sDetail.getAmount());
		Assert.assertEquals(DateUtils.parseDate("2017-11-17 22:04:11", "yyyy-MM-dd HH:mm:ss"), sDetail.getActualPaymentTime());
		Assert.assertEquals("69c02558-412c-4c76-9811-e92c2f2b55bc", sDetail.getContractUniqueId());
	}
	
	
}
