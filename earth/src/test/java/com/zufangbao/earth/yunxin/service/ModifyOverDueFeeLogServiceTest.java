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
import com.zufangbao.sun.yunxin.entity.api.ModifyOverdueFeeLog;
import com.zufangbao.sun.yunxin.service.ModifyOverDueFeeLogService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

		"classpath:/local/applicationContext-*.xml",
})
@Transactional()
public class ModifyOverDueFeeLogServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private ModifyOverDueFeeLogService modifyOverDueFeeLogService;
	
	
	@Test
	@Sql("classpath:test/yunxin/api/service/testGetLogByRequestNo.sql")
	public void testGetLogByRequestNo() throws Exception {
		ModifyOverdueFeeLog log = modifyOverDueFeeLogService.getLogByRequestNo("db8a3496-dc02-4245-a679-2144ec9f5207");
		Assert.assertEquals("db8a3496-dc02-4245-a679-2144ec9f5207",log.getRequestNo());
		
		log = modifyOverDueFeeLogService.getLogByRequestNo(null);
		Assert.assertEquals("db8a3496-dc02-4245-a679-2144ec9f5207",log.getRequestNo());
		
		log = modifyOverDueFeeLogService.getLogByRequestNo("");
		Assert.assertEquals("db8a3496-dc02-4245-a679-2144ec9f5207",log.getRequestNo());
		
		log = modifyOverDueFeeLogService.getLogByRequestNo("12222222222222");
		Assert.assertNull(log);
	}

}
