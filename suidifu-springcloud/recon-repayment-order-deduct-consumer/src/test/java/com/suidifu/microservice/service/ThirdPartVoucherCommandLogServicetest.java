package com.suidifu.microservice.service;

import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThirdPartVoucherCommandLogServicetest {

	@Autowired
	private ThirdPartyVoucherCommandLogService thirdPartyVoucherCommandLogService;

	@Test
	@Sql("classpath:test/yunxin/thirdPartVoucher/testUpdateVoucherStatus.sql")
	public void testUpdateVoucherStatus(){
		
		
		thirdPartyVoucherCommandLogService.updateTransactionCommandVoucherLogIssueStatus(VoucherLogIssueStatus.HAS_ISSUED, "ppdtest001");
		ThirdPartyVoucherCommandLog thirdPartyVoucherCommandLog = thirdPartyVoucherCommandLogService.getThirdPartyVoucherCommandLogByVoucherUuid("ppdtest001uuid");
		
		Assert.assertEquals(VoucherLogIssueStatus.HAS_ISSUED, thirdPartyVoucherCommandLog.getVoucherLogIssueStatus());
	}
	
}
