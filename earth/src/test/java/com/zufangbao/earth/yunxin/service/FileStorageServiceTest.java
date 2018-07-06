package com.zufangbao.earth.yunxin.service;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.sun.service.FileStorageService;
import com.zufangbao.sun.yunxin.entity.FileStorage;
import com.zufangbao.sun.yunxin.entity.FlieApplicationType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/local/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional()
public class FileStorageServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private FileStorageService fileStorageService;
	
	@Test
	@Sql("classpath:test/yunxin/testFileStorage.sql")
	public void testSaveFileStorageRecord() {
		fileStorageService.saveFileStorageRecord("v83aa72d-da72-4e0f-be80-322dcac6ba62","中止合同.xls", "C:\\upload\\contracts\\", null, FlieApplicationType.NotSetting);
		Assert.assertEquals(2, fileStorageService.loadAll(FileStorage.class).size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/testFileStorage.sql")
	public void testInvalidateFile() {
		fileStorageService.invalidateFile("e83aa72d-da72-4e0f-be80-322dcac6ba62", FlieApplicationType.InvalidateContractAttachment);
		Assert.assertNotNull(fileStorageService.load(FileStorage.class, 1L).getExpiryTime());
	}
	
	@Test
	@Sql("classpath:test/yunxin/testFileStorage.sql")
	public void testGetFileStorageByUuid() {
		Assert.assertEquals("asset_set.sql", fileStorageService.getFileStorageByUuid("6eb9511f-30e6-40dc-b17a-9613857f36ae").getOriginFileName());
	}
	
	@Test
	@Sql("classpath:test/yunxin/testFileStorage.sql")
	public void testGetValidFileStorageByRelatedUuid() {
		FileStorage fileStorage = fileStorageService.getValidFileStorageByRelatedUuid("e83aa72d-da72-4e0f-be80-322dcac6ba62", FlieApplicationType.InvalidateContractAttachment);
		Assert.assertEquals(FlieApplicationType.InvalidateContractAttachment, fileStorage.getApplicationType());
	}
}
