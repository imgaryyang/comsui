package com.zufangbao.earth.yunxin.api;


import com.zufangbao.sun.yunxin.entity.datasync.DataSyncLogModel;
import com.zufangbao.sun.yunxin.entity.datasync.RepayStatus;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanExtraChargeHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DataSyncHandler;
import com.zufangbao.wellsfargo.yunxin.data.sync.model.DataSyncMapSpec;
import com.zufangbao.wellsfargo.yunxin.data.sync.model.DataSyncMapSpec.DataSyncDeductType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class DataSyncHandlerTest {

//extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	public RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;
	
	@Autowired
	public RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private JournalVoucherService jorunalVoucherService;
	
	@Autowired
	private DataSyncHandler dataSyncHandler;
	@Test
	@Sql("classpath:test/yunxin/api/getCastType.sql")
	public  void getCastTypeWAITFORCONFIRM(){
		
		
		JournalVoucher journalVoucher = jorunalVoucherService.getJournalVoucherByVoucherUUID("1cc5912c-2d67-4995-b4d1-78d64af02cd1");
		String repayKind =dataSyncHandler.fetch_repay_kind_by(journalVoucher);
		
		assertEquals(DataSyncMapSpec.DataSyncRepayKind.OVERDUE_WAIT_FOR_SURE, repayKind);
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/getCastTypeOVERDUE.sql")
	public  void getCastTypeOVERDUE(){
		
		JournalVoucher journalVoucher = jorunalVoucherService.getJournalVoucherByVoucherUUID("48d58b2a-f2ac-4797-b08a-5704280dd423");
		String repayKind =dataSyncHandler.fetch_repay_kind_by(journalVoucher);
		
		assertEquals(DataSyncMapSpec.DataSyncRepayKind.OVERDUE_REPAYMENT, repayKind);
		
	}
	@Test
	@Sql("classpath:test/yunxin/api/getCastTypeUNKNOW.sql")
	public  void getCastTypeUNKNOW(){
		
		JournalVoucher journalVoucher = jorunalVoucherService.getJournalVoucherByVoucherUUID("48d58b2a-f2ac-4797-b08a-5704280dd423");
		String repayKind =dataSyncHandler.fetch_repay_kind_by(journalVoucher);
		
		assertEquals(DataSyncMapSpec.DataSyncRepayKind.UN_KNOWN, repayKind);
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/getCastTypeRepo.sql")
	public  void getCastTypeRepo(){
		
		JournalVoucher journalVoucher = jorunalVoucherService.getJournalVoucherByVoucherUUID("85196442-79e3-45e7-9460-d02345719fc7");
		String repayKind =dataSyncHandler.fetch_repay_kind_by(journalVoucher);
		
		assertEquals(DataSyncMapSpec.DataSyncRepayKind.REPO, repayKind);
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/generateDataSyncLogNormal.sql")
	public  void generateDataSyncLogNormalThirdPartDeduct() throws Exception{
		
		JournalVoucher journalVoucher = jorunalVoucherService.getJournalVoucherByVoucherUUID("463126e4-e6c6-40c1-a4d3-3cb751108dc3");
		DataSyncLogModel dataSyncLog =dataSyncHandler.generateDataSyncLog(journalVoucher);
		
		
//		assertEquals("1e59c5d1-89ea-459b-a094-636fb1fbe17b", dataSyncLog.getAssetSetUuid());
		assertEquals(new String("2016-12-28 15:18:13.0"), dataSyncLog.getOccurDate().toString());
		assertEquals(1, dataSyncLog.getContractFlag());
		assertEquals("2016-236-DK(143275553729444822ht)号", dataSyncLog.getContractNo());
		assertEquals("8a056722-456e-4760-bd49-4f9ffb241bcc", dataSyncLog.getUniqueId());
//		SyncDataDecimalModel decimalDetail = dataSyncLog.getDataSyncBigDecimalDetailsJson();
		assertEquals(new BigDecimal("3333.00"), dataSyncLog.getPreRepayPrincipal());
		assertEquals(new BigDecimal("1.00"), dataSyncLog.getPreRepayProfit());
		assertEquals(new BigDecimal("1.00"), dataSyncLog.getPreRepayPennalty());
		assertEquals(new BigDecimal("0.00"), dataSyncLog.getRepayProfit());
		assertEquals(new BigDecimal("0.00"), dataSyncLog.getRepayPennalty());
		assertEquals(new BigDecimal("0.00"), dataSyncLog.getRepayPrincipal());
		assertEquals(new BigDecimal("9.00"), dataSyncLog.getTotalAmount());
		assertEquals(new BigDecimal("0.00"), dataSyncLog.getTechMaintenanceFee());
		assertEquals(new BigDecimal("3.00"), dataSyncLog.getLateServiceFee());
		assertEquals(new BigDecimal("2.00"), dataSyncLog.getLatePenalty());
		assertEquals(new BigDecimal("4.00"), dataSyncLog.getLateOtherCosts());


//		assertEquals(LogDataSyncStatus.NO_SYNC, dataSyncLog.getDataSyncStatus());
		assertEquals(new Integer(DataSyncDeductType.DEDUCT), new Integer(dataSyncLog.getDeductType()));
//		List<DeductWay> deductWays = dataSyncLog.getDeductWaysJson();
//		assertEquals(1, deductWays.get(0).getDeductWay());
//		assertEquals(new BigDecimal("9.00"),	deductWays.get(0).getDeductAmount());
		assertEquals("463126e4-e6c6-40c1-a4d3-3cb751108dc3", dataSyncLog.getVoucherUuid());
		assertEquals(DataSyncMapSpec.DataSyncRepayKind.UN_KNOWN, dataSyncLog.getRepayKind());
		assertEquals(RepayStatus.UN_CLEAR, dataSyncLog.getRepayStatus());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/generateDataSyncLogRepo.sql")
	public  void generateDataSyncLogRepo() throws Exception{
		
		JournalVoucher journalVoucher = jorunalVoucherService.getJournalVoucherByVoucherUUID("26371945-e2be-4d23-b3fe-727e3c8c48c7");
		DataSyncLogModel	dataSyncLog = dataSyncHandler.generateDataSyncLog(journalVoucher);
		
		
//		assertEquals(null, dataSyncLog.getAssetSetUuid());
		assertEquals(new String("2016-12-20 10:47:47.0"), dataSyncLog.getOccurDate().toString());
		assertEquals(2, dataSyncLog.getContractFlag());
		assertEquals("3aba87bc-9163-4976-8163-df262c0dcb97", dataSyncLog.getBuyBackId());
		assertEquals("2016-236-DK(12731659)号", dataSyncLog.getContractNo());
		assertEquals("96e30227-5da4-49a0-b1d4-345daec2fea4", dataSyncLog.getUniqueId());
//		SyncDataDecimalModel decimalDetail = dataSyncLog.getDataSyncBigDecimalDetailsJson();

		assertEquals(new BigDecimal("1500.00"), dataSyncLog.getPreRepayPrincipal());//
		assertEquals(new BigDecimal("60.00"), dataSyncLog.getPreRepayProfit());//
		assertEquals(BigDecimal.ZERO, dataSyncLog.getPreRepayPennalty());//
		assertEquals(new BigDecimal("2653.86"), dataSyncLog.getTotalAmount());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getTechMaintenanceFee());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getLateServiceFee());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getLatePenalty());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getLateOtherCosts());


//		assertEquals(LogDataSyncStatus.NO_SYNC, dataSyncLog.getDataSyncStatus());
		assertEquals(new Integer(DataSyncDeductType.MERCHANT_PAY), new Integer(dataSyncLog.getDeductType()));
		assertEquals("26371945-e2be-4d23-b3fe-727e3c8c48c7", dataSyncLog.getVoucherUuid());
		assertEquals(DataSyncMapSpec.DataSyncRepayKind.REPO, dataSyncLog.getRepayKind());
		assertEquals(RepayStatus.UN_CLEAR, dataSyncLog.getRepayStatus());
		
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/api/generateDataSyncLoggGuarantee.sql")
	public  void generateDataSyncLoggGuarantee() throws Exception{
		
		JournalVoucher journalVoucher = jorunalVoucherService.getJournalVoucherByVoucherUUID("d8d618ac-b9c3-4226-8268-11a460e329e9");
		DataSyncLogModel dataSyncLog =dataSyncHandler.generateDataSyncLog(journalVoucher);

		
//		assertEquals("792b77cd-4a56-4f71-a0d2-41aa6a6842e1", dataSyncLog.getAssetSetUuid());
		assertEquals(new String("2016-12-21 21:01:44.0"), dataSyncLog.getOccurDate().toString());
		assertEquals(1, dataSyncLog.getContractFlag());
		assertEquals(null, dataSyncLog.getBuyBackId());
		assertEquals("wwtest1--fa10f36a-d442-44cc-b914-79224ce1b1970", dataSyncLog.getContractNo());
		assertEquals("a964da76-a333-4635-b94c-661d0f7d9340", dataSyncLog.getUniqueId());
//		SyncDataDecimalModel decimalDetail = dataSyncLog.getDataSyncBigDecimalDetailsJson();

//		assertEquals(new BigDecimal("1500.00"), dataSyncLog.getPreRepayPrincipal());//
//		assertEquals(new BigDecimal("60.00"), dataSyncLog.getPreRepayProfit());//
//		assertEquals(BigDecimal.ZERO, dataSyncLog.getPreRepayPennalty());//
		assertEquals(new BigDecimal("2653.86"), dataSyncLog.getTotalAmount());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getTechMaintenanceFee());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getLateServiceFee());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getLatePenalty());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getLateOtherCosts());

		assertEquals(new BigDecimal("250.00"), dataSyncLog.getPreRepayPrincipal());
		assertEquals(new BigDecimal("0.00"), dataSyncLog.getPreRepayProfit());
		assertEquals(new BigDecimal("3.38"), dataSyncLog.getPreRepayPennalty());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getRepayProfit());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getRepayPennalty());
		assertEquals(BigDecimal.ZERO, dataSyncLog.getRepayPrincipal());
		assertEquals( new BigDecimal("200.00"), dataSyncLog.getTotalAmount());
		assertEquals( BigDecimal.ZERO, dataSyncLog.getTechMaintenanceFee());
		assertEquals( BigDecimal.ZERO, dataSyncLog.getLateServiceFee());
		assertEquals( BigDecimal.ZERO, dataSyncLog.getLatePenalty());
		assertEquals( BigDecimal.ZERO, dataSyncLog.getLateOtherCosts());
//		assertEquals(LogDataSyncStatus.NO_SYNC, dataSyncLog.getDataSyncStatus());
		assertEquals(new Integer(DataSyncDeductType.INITIATIVE_PAY), new Integer(dataSyncLog.getDeductType()));
		assertEquals("d8d618ac-b9c3-4226-8268-11a460e329e9", dataSyncLog.getVoucherUuid());
		assertEquals(DataSyncMapSpec.DataSyncRepayKind.HAS_GUARANTEE, dataSyncLog.getRepayKind());
		assertEquals(RepayStatus.UN_CLEAR, dataSyncLog.getRepayStatus());
	}

	@Test
	@Sql("classpath:test/yunxin/api/test_generateDataSyncLog.sql")
	public void test_generateDataSyncLog() throws Exception{

		List<JournalVoucher> journalVouchers = jorunalVoucherService.loadAll(JournalVoucher.class);
		for (JournalVoucher jv: journalVouchers) {
			DataSyncLogModel dataSyncLog =dataSyncHandler.generateDataSyncLog(jv);
		}
	}
}
