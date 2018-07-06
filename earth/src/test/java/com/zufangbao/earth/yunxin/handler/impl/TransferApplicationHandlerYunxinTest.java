package com.zufangbao.earth.yunxin.handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List; 

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.demo2do.core.persistence.support.Filter;
import com.zufangbao.sun.entity.finance.BatchPayRecord;
import com.zufangbao.sun.entity.financial.TransferApplication;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.service.BatchPayRecordService;
import com.zufangbao.sun.service.TransferApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.DeductStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingDeductStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingSettlingStatus;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.OrderClearStatus;
import com.zufangbao.wellsfargo.yunxin.handler.TransferApplicationHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TransferApplicationHandlerYunxinTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private TransferApplicationHandler transferApplicationHandler;

	@Autowired
	private TransferApplicationService transferApplicationService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private BatchPayRecordService batchPayRecordService;

	@Test
	@Sql("classpath:test/yunxin/transferApplication/testCreateBatchPaymentRecord.sql")
	public void createBatchPayRecordBy(){
		TransferApplication transferApplication = transferApplicationService.load(TransferApplication.class, 4L);
		try {
			BatchPayRecord createBatchPayRecord = transferApplicationHandler.createBatchPayRecordBy(transferApplication);
			assertNotNull(createBatchPayRecord);
			
			List<BatchPayRecord> batchPayRecordList = batchPayRecordService.list(BatchPayRecord.class, new Filter());
			assertEquals(1,batchPayRecordList.size());
			BatchPayRecord batchPayRecord = batchPayRecordList.get(0);
			assertEquals(createBatchPayRecord.getId(),batchPayRecord.getId());
			assertEquals(0,new BigDecimal("2000").compareTo(batchPayRecord.getAmount()));
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/transferApplication/testUpdate_transfer_order_asset.sql")
	public void updateTransferAppAndOrderAndAssetForSuc(){
		TransferApplication transferApplication = transferApplicationService.load(TransferApplication.class, 1L);
		
		transferApplicationHandler.updateTransferAppAndOrderAndAssetBy(transferApplication, true, "ok.", new Date());
		
		transferApplication = transferApplicationService.load(TransferApplication.class, 1L);
		assertEquals(DeductStatus.CLEAR,transferApplication.getDeductStatus());
		assertEquals(ExecutingDeductStatus.SUCCESS,transferApplication.getExecutingDeductStatus());
		assertEquals("ok.",transferApplication.getComment());
		Order order = transferApplication.getOrder();
		assertEquals(OrderClearStatus.CLEAR,order.getClearingStatus());
		assertEquals(ExecutingSettlingStatus.SUCCESS,order.getExecutingSettlingStatus());
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
		assertEquals(AssetClearStatus.CLEAR,assetSet.getAssetStatus());
		assertEquals(OnAccountStatus.WRITE_OFF,assetSet.getOnAccountStatus());
	}
	@Test
	@Sql("classpath:test/yunxin/transferApplication/testUpdate_transfer_order_asset.sql")
	public void updateTransferAppAndOrderAndAssetForFail(){
		TransferApplication transferApplication = transferApplicationService.load(TransferApplication.class, 1L);
		
		transferApplicationHandler.updateTransferAppAndOrderAndAssetBy(transferApplication, false, "fail.", new Date());
		
		transferApplication = transferApplicationService.load(TransferApplication.class, 1L);
		assertEquals(DeductStatus.UNCLEAR,transferApplication.getDeductStatus());
		assertEquals(ExecutingDeductStatus.FAIL,transferApplication.getExecutingDeductStatus());
		assertEquals("fail.",transferApplication.getComment());
		Order order = transferApplication.getOrder();
		assertEquals(OrderClearStatus.UNCLEAR,order.getClearingStatus());
		assertEquals(ExecutingSettlingStatus.DOING,order.getExecutingSettlingStatus());
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
		assertEquals(AssetClearStatus.UNCLEAR,assetSet.getAssetStatus());
		assertEquals(OnAccountStatus.ON_ACCOUNT,assetSet.getOnAccountStatus());
	}
	
}
