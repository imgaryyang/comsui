package com.zufangbao.earth.yunxin.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/local/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional()
public class PrepaymentApplicationServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private PrepaymentApplicationService prepaymentApplicationService;
	
//	@Test
//	@Sql("classpath:test/yunxin/prepaymentApplication/test_prepaymentApplicationRequest.sql")
//	public void test_checkByRequestNo(){
//		String requestNo = "123456";
//		try{
//	        prepaymentApplicationService.checkByRequestNo(requestNo);
//		}catch(ApiException e){
//			Assert.assertEquals("请求编号重复!",  ApiMessageUtil.getMessage(e.getCode()));
//		}
//		
//	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getUniquePrepaymentApplicationByRepaymentPlanUuid.sql")
	public void test_getUniquePrepaymentApplicationByRepaymentPlanUuid(){
		String repaymentPlanUuid = "ce51a10c-67b1-43cd-ac7d-983071531e5c";
		PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(repaymentPlanUuid);
		Assert.assertNotNull(prepaymentApplication);
		Assert.assertEquals("1", prepaymentApplication.getId().toString());
		Assert.assertEquals("8050", prepaymentApplication.getContractId().toString());
		Assert.assertEquals("18183",prepaymentApplication.getAssetSetId().toString() );
		Assert.assertEquals("f522e269-8281-4bf2-b922-7c691881ef6a", prepaymentApplication.getUniqueId());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getUniquePrepaymentApplicationByRepaymentPlanUuid.sql")
	public void test_getUniquePrepaymentApplicationByRepaymentPlanUuid_nullUuid(){
		String repaymentPlanUuid = "";
		PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(repaymentPlanUuid);
		Assert.assertNull(prepaymentApplication);
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getUniquePrepaymentApplicationByRepaymentPlanUuid.sql")
	public void test_getUniquePrepaymentApplicationByRepaymentPlanUuid_wrongUuid(){
		String repaymentPlanUuid = "1234567";
		PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(repaymentPlanUuid);
		Assert.assertNull(prepaymentApplication);
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getUniquePrepaymentApplicationByRepaymentPlanUuid.sql")
	public void test_getUniquePrepaymentApplicationByAssetSetId(){
		Long assetSetId = 25001L;
		PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByAssetSetId(assetSetId);
		Assert.assertNotNull(prepaymentApplication);
		Assert.assertEquals("2", prepaymentApplication.getId().toString());
		Assert.assertEquals("10281", prepaymentApplication.getContractId().toString());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getUniquePrepaymentApplicationByRepaymentPlanUuid.sql")
	public void test_getUniquePrepaymentApplicationByAssetSetId_nullAssetSetId(){
		Long assetSetId = null;
		PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByAssetSetId(assetSetId);
		Assert.assertNull(prepaymentApplication);
	}

	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getUniquePrepaymentApplicationByRepaymentPlanUuid.sql")
	public void test_getUniquePrepaymentApplicationByAssetSetId_wrongAssetSetId(){
		Long assetSetId = 123L;
		PrepaymentApplication prepaymentApplication = prepaymentApplicationService.getUniquePrepaymentApplicationByAssetSetId(assetSetId);
		Assert.assertNull(prepaymentApplication);
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getPrepaymentApplicationByRecycleDateAndStatus.sql")
	public void test_getPrepaymentApplicationByRecycleDateAndStatus() {
		List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.getPrepaymentApplicationByRecycleDateAndStatus(DateUtils.getToday(), PrepaymentStatus.UNFINISHED);
		Assert.assertTrue(CollectionUtils.isNotEmpty(prepaymentApplications));
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getPrepaymentApplicationByRecycleDateAndStatus.sql")
	public void test_getPrepaymentApplicationByRecycleDateAndStatus_nullParams() {
		List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.getPrepaymentApplicationByRecycleDateAndStatus(null, null);
		Assert.assertTrue(CollectionUtils.isEmpty(prepaymentApplications));
	}
	
	@Test
	@Sql("classpath:test/yunxin/prepaymentApplication/test_getPrepaymentApplicationByRecycleDateAndStatus.sql")
	public void test_getPrepaymentApplicationByRecycleDateAndStatus_noResult() {
		List<PrepaymentApplication> prepaymentApplications = prepaymentApplicationService.getPrepaymentApplicationByRecycleDateAndStatus(DateUtils.getToday(), PrepaymentStatus.SUCCESS);
		Assert.assertTrue(CollectionUtils.isEmpty(prepaymentApplications));
	}
}




















