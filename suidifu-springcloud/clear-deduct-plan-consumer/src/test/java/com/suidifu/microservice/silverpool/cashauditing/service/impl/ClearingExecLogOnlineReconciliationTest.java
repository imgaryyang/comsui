package com.suidifu.microservice.silverpool.cashauditing.service.impl;

import static org.junit.Assert.assertEquals;

import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherType;
import com.zufangbao.sun.entity.account.special.account.ClearingExecLog;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.account.ClearingExecLogService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClearingExecLogOnlineReconciliationTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	ClearingExecLogService clearingExecLogService;
	@Autowired
	JournalVoucherService journalVoucherService;

	@Autowired
	LedgerBookStatHandler ledgerBookStatHandler;
	@Autowired
	RepaymentPlanService repaymentPlanService;

	@Test
	@Sql("classpath:test/yunxin/clearing2SpecialAccountonline.sql")
	public void createClearingExecLog(){
		String assetUuid = "6cfd2caf-801d-4339-97b2-4f2748226542";
		String sourceDoucumentDetailUuid = "19478f46-27d5-4719-9505-2952d8341c6d";
		String sourceDocumentUuid = "24884c68-4b57-4688-a4f3-c72c1d75c70d";
		String ledgerBookNo = "192bd38f-b088-479b-8b89-b5bc5e53ad52";
		JournalVoucher journalVoucher = journalVoucherService.getJournalVoucherBySourceDocumentUuidAndType(sourceDocumentUuid, sourceDoucumentDetailUuid,assetUuid);
		AssetSet uniqueRepaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
		if(journalVoucher != null){
			Map<String,BigDecimal> detailAmountMap = ledgerBookStatHandler.get_jv_asset_detail_amount_of_banksaving_and_independent_accounts(ledgerBookNo, journalVoucher.getJournalVoucherUuid(), assetUuid);
			clearingExecLogService.createClearingExecLog(uniqueRepaymentPlan, detailAmountMap, journalVoucher.getUuid(), journalVoucher.getJournalVoucherType().ordinal(), journalVoucher.getBookingAmount(), 12453L, "1212112");
		}
		List<ClearingExecLog> clearingExecLogOfUnclears = clearingExecLogService.queryNeedToRecordOfUnclear(
				Arrays.asList(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER.ordinal()), "2d380fe1-7157-490d-9474-12c5a9901e29", "1212112");
		List<ClearingExecLog> all = clearingExecLogService.loadAll(ClearingExecLog.class);
		assertEquals(1, clearingExecLogOfUnclears.size());

	}

}
