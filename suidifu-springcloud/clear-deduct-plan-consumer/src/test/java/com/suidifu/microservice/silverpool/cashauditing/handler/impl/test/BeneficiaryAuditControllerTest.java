package com.suidifu.microservice.silverpool.cashauditing.handler.impl.test;

import static org.junit.Assert.assertEquals;

import com.suidifu.microservice.handler.TotalReceivableBillsHandler;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucherStatus;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeneficiaryAuditControllerTest {

	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;
	@Autowired
	private TotalReceivableBillsHandler totalReceivableBillsHandler;


	@Test
	@Sql("classpath:test/yunxin/showBasicInfoTrans.sql")
	public void queryClearingVoucher(){
		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.getTotalReceivableBillsByUuid("ed055704-25c1-46fe-ab56-d733d9af0bb0");
		ClearingVoucher clearingVoucher = totalReceivableBillsHandler.queryClearingVoucherByTotalReceivableBills(totalReceivableBills, ClearingVoucherStatus.DONE);
		assertEquals("001053110000001",clearingVoucher.getMerchantNo());
		assertEquals(3, clearingVoucher.getPaymentInstitution().ordinal());
		assertEquals("", clearingVoucher.getPgClearingAccount());
		assertEquals("52bd00cd-6633-4e35-8a06-7c7eabfe7522", clearingVoucher.getBatchUuid());
	}
}
