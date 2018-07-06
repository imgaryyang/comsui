package com.zufangbao.earth.yunxin.handler.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.sun.entity.notice.Notice;
import com.zufangbao.sun.entity.notice.NoticeLog;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.log.SystemOperateLogVO;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class SystemOperateLogHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	
	
	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired 
	private SystemOperateLogService systemOperateLogService;
	
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private FinancialContractService financialContractService;
	
	@Test
	@Sql("classpath:test/yunxin/systemOperateLog/testGetLogVOListByUuid.sql")
	public void testGetLogVOListByUuid(){
		List<SystemOperateLogVO> SystemOperateLogVOList = systemOperateLogHandler.getLogVOListByUuid(null);
		Assert.assertEquals(0, SystemOperateLogVOList.size());
		
		SystemOperateLogVOList = systemOperateLogHandler.getLogVOListByUuid("");
		Assert.assertEquals(0, SystemOperateLogVOList.size());
		
		SystemOperateLogVOList = systemOperateLogHandler.getLogVOListByUuid("6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f");		
		Assert.assertEquals(7, SystemOperateLogVOList.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/systemOperateLog/getSystemOperateLogVOListBy.sql")
	public void testGetSystemOperateLogVOListBy(){
		List<SystemOperateLogVO> SystemOperateLogVOList =  systemOperateLogHandler.getSystemOperateLogVOListBy("",null);
		Assert.assertEquals(0, SystemOperateLogVOList.size());
		
		SystemOperateLogVOList =  systemOperateLogHandler.getSystemOperateLogVOListBy(null,null);
		Assert.assertEquals(0, SystemOperateLogVOList.size());
	
		Page page = new Page();
		page.setEveryPage(7);
		SystemOperateLogVOList = systemOperateLogHandler.getSystemOperateLogVOListBy("6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f",page);		
		Assert.assertEquals(7, SystemOperateLogVOList.size());
		
		page.setEveryPage(5);
		SystemOperateLogVOList = systemOperateLogHandler.getSystemOperateLogVOListBy("6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f",page);		
		Assert.assertEquals(5, SystemOperateLogVOList.size());
	}
	
	@SuppressWarnings("unused")
	@Test(expected = NullPointerException.class)
	@Sql("classpath:test/yunxin/systemOperateLog/getSystemOperateLogVOListBy.sql")
	public void testGetSystemOperateLogVOListByForNullPointerException(){
		List<SystemOperateLogVO> SystemOperateLogVOList = systemOperateLogHandler.getSystemOperateLogVOListBy("6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f",null);		
	}
	
	@Test
	@Sql("classpath:test/yunxin/systemOperateLog/generateAssociateSystemLog.sql")
	public void testGenerateAssociateSystemLog() throws Exception{
		Principal principal = new Principal();
		String ip = "127.0.0.1";
		String offlineBillUuid = "0da4d513e11b43bd866b1bb031939ff4";
		Map<String,Object> map = new HashMap<String,Object>();
		systemOperateLogHandler.generateAssociateSystemLog(principal, ip, offlineBillUuid, map);
		List<SystemOperateLog> log = systemOperateLogService.getLogsByUuid(offlineBillUuid);
		Assert.assertEquals(LogFunctionType.OFFLINEBILLASSOCIATE,log.get(0).getLogFunctionType());
	}
	
	@Test
	@Sql("classpath:test/yunxin/systemOperateLog/testGenerateCashFLowAuditSystemLog.sql")
	public void testGenerateCashFLowAuditSystemLog(){
		//systemOperateLogHandler.generateCashFLowAuditSystemLog(voucherOperationInfo, appArriveRecord);
	}
	
	@Test
	@Sql("classpath:test/yunxin/systemOperateLog/testGenerateCashFlowSystemLogOfRevoke.sql")
	public void testGenerateCashFlowSystemLogOfRevoke(){
		//systemOperateLogHandler.generateCashFlowSystemLogOfRevoke(principal, ip, cashFlow, sourceDocument);
	}
	
	@Test
	@Sql("classpath:test/yunxin/systemOperateLog/testGenerateCashFlowSystemLogOfRecharge.sql")
	public void testGenerateCashFlowSystemLogOfRecharge(){
		//systemOperateLogHandler.generateCashFlowSystemLogOfRecharge(principal, ip, cashFlow, sourceDocument);
	}
	
	@Test
	@Sql("classpath:test/yunxin/systemOperateLog/testGenerateSystemOperateLog.sql")
	public void testGenerateSystemOperateLog() throws Exception{
		//Update notice
		Long principalId = 14l;
		String ipAddress2 = "127.0.0.1";
		String keyContent = "";
		
		
		LogFunctionType logFunctionType = LogFunctionType.EDITNOTICE;
		LogOperateType logOperateType =  LogOperateType.UPDATE;
		NoticeLog oldObject = new NoticeLog("uuid001","标题1","内容1","2016-12-14","2016-12-14","1");
		NoticeLog editObject = new NoticeLog("uuid001","标题1","内容1","2016-12-14","2016-12-14","1");
		SystemOperateLogRequestParam  systemOperateLogRequestParam = new SystemOperateLogRequestParam(principalId, ipAddress2, keyContent, logFunctionType, logOperateType,Notice.class, oldObject, editObject,null);
		systemOperateLogHandler.generateSystemOperateLog(systemOperateLogRequestParam);
		List<SystemOperateLog> log = systemOperateLogService.getLogsByUuid("uuid001");
		Assert.assertEquals("uuid001",log.get(0).getObjectUuid());
		
		
		//Export
		LogFunctionType logFunctionType1 = LogFunctionType.EXPORTREPAYEMNTPLAN;
		LogOperateType logOperateType1 =   LogOperateType.EXPORT;
		List<String> uuidList = Arrays.asList("001","002","003","004");
		SystemOperateLogRequestParam  systemOperateLogRequestParam1 = new SystemOperateLogRequestParam(principalId, ipAddress2, keyContent, logFunctionType1, logOperateType1,null, null, null, uuidList);
		systemOperateLogHandler.generateSystemOperateLog(systemOperateLogRequestParam1);
		List<SystemOperateLog> log1 = systemOperateLogService.list(SystemOperateLog.class,new Filter().addLike("recordContentDetail", "001,002"));
		Assert.assertEquals(LogOperateType.EXPORT,log1.get(0).getLogOperateType());
		
		//Default
		LogFunctionType logFunctionType2 = LogFunctionType.ADDNOTICE;
		LogOperateType logOperateType2 =  LogOperateType.ADD;
		NoticeLog oldObject2 = new NoticeLog("uuid002","标题1","内容1","2016-12-14","2016-12-14","1");
		SystemOperateLogRequestParam  systemOperateLogRequestParam2 = new SystemOperateLogRequestParam(principalId, ipAddress2, keyContent, logFunctionType2, logOperateType2,Notice.class, oldObject2,null,null);
		systemOperateLogHandler.generateSystemOperateLog(systemOperateLogRequestParam2);
		List<SystemOperateLog> log2 = systemOperateLogService.getLogsByUuid("uuid002");
		systemOperateLogHandler.generateSystemOperateLog(systemOperateLogRequestParam2);
		Assert.assertEquals("uuid002",log2.get(0).getObjectUuid());
	}
}
