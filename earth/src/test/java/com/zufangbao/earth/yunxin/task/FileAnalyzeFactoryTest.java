package com.zufangbao.earth.yunxin.task;



import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.earth.handler.impl.trade.FileAnalyzaFactory;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class FileAnalyzeFactoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	private String uploadPath="/Users/linzhechi/Downloads/Mr.Donkey.2016.国语中字.720p.WEBRip.mp4/";
	private String doubtPath="/home/zhanglongfei/桌面/New Folder/doubtFolder/";
	private String localPath="/home/zhanglongfei/桌面/New Folder/storageFolder/";
	private String ftpFileBackStorageFolder="/home/zhanglongfei/桌面/New Folder/ftpFileBackStorageFolder/";
	@Autowired
	private FileAnalyzaFactory fileAnalyzaFactory;
	
	@Test
	@Sql("classpath:test/ExternalTradeBatchDetail.sql")
	@Ignore("#FileAnalyzeFactoryTest#中localPath是开发者本地")
	public  void testDoDheckAndSaveJinDanFiles(){
		fileAnalyzaFactory.doCheckAndSaveJinDanFiles(localPath, uploadPath, doubtPath);
	}
	
	
	@Test
	//@Sql("classpath:test/ExternalTradeBatchDetail.sql")
	public  void testDodoReturnFtpFile(){
		String externalTradeBatchUuid ="4ad53e7b-ccd7-4d68-acee-50ae49c18a99" ;
		String contractNo ="900000004";
		String externalBatchNo="1110000";
				
		fileAnalyzaFactory.doReturnFtpFile(externalTradeBatchUuid, ftpFileBackStorageFolder, contractNo,externalBatchNo);
	
}
	
	
}
