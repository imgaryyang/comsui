package com.zufangbao.earth.web.controller.system;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.web.controller.AuthenticationController;
import com.zufangbao.sun.entity.log.LogType;
import com.zufangbao.sun.entity.log.SystemLog;
import com.zufangbao.sun.entity.security.Principal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

		"classpath:/local/applicationContext-*.xml",
		
		"classpath:/local/DispatcherServlet.xml"
})
public class SystemLogServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private GenericDaoSupport genericDaoSurpport;
	@Autowired
	private AuthenticationController authenticationController;
	@Autowired
	private LogsController logsController;
	@Test
	@Sql("classpath:test/createLoginLogTestData.sql")
	public void createLoginLogTest(){
		String ip = "127.0.0.1";
		Principal principal = this.genericDaoSurpport.load(Principal.class, 2L);
		this.authenticationController.generateUserLoginLog(principal, ip);;
		List<SystemLog> logs = this.genericDaoSurpport.searchForList("from SystemLog where ip=:ip", "ip",ip);
		Assert.assertEquals(ip, logs.get(0).getIp());
		Assert.assertEquals(LogType.LOGIN, logs.get(0).getLogType());
	}
	
//	@Test
//	@Sql("classpath:test/getLoginLogsTestData.sql")
//	public void getUserLoginLogsTest(){
//		Page page = new Page();
//		page.setCurrentPage(1);
//		page.setEveryPage(12);
//		boolean isAsc = false;
//		List<SystemLog> resultSet = this.logsController.searchLogsByCreateTime(page, isAsc);
//		Assert.assertEquals(2, resultSet.size());
//		Assert.assertEquals(resultSet.get(0).getContent(), "退出登陆");
//		Assert.assertEquals(resultSet.get(1).getContent(), "登陆");
//
//	}
	
}
