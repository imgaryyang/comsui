package com.zufangbao.earth.yunxin.handler.impl;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.handler.YunxinOfflinePaymentHandler;
import com.zufangbao.sun.yunxin.entity.OfflineBill;
import com.zufangbao.sun.yunxin.entity.model.OfflineBillQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml", })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class YunxinOfflinePaymentHandlerTest {

	@Autowired
	private YunxinOfflinePaymentHandler yunxinPaymentHandler;
	
	
	@Test
	@Sql("classpath:test/yunxin/offlineBill/testQueryAllOfflineBillCorrespondingSourceDocument.sql")
	public void testQueryAllOfflineBillCorrespondingSourceDocument(){
		Page page = new Page(0,12);
		OfflineBillQueryModel offlineBillQueryModel = new OfflineBillQueryModel("payer_account_name","acc");
		offlineBillQueryModel.setFinancialContractUuids("[\"ae07d211-2c10-43ed-9924-3d7988db6fe4\"]");
		Map<String, Object> resultMap = yunxinPaymentHandler.queryAllOfflineBillCorrespondingSourceDocumentByAuditStatus(offlineBillQueryModel, page);
		List<OfflineBill> offlineBillList = (List<OfflineBill>) resultMap.get("list");
		Assert.assertEquals(1, offlineBillList.size());
	}
	@Test
	@Sql("classpath:test/yunxin/offlineBill/testQueryAllOfflineBillCorrespondingSourceDocument.sql")
	public void testQueryAllOfflineBillCorrespondingSourceDocument_like_payment_account_name(){
		Page page = new Page(0,12);
		OfflineBillQueryModel offlineBillQueryModel = new OfflineBillQueryModel(null,null);
		offlineBillQueryModel.setFinancialContractUuids("[\"ae07d211-2c10-43ed-9924-3d7988db6fe4\"]");
		Map<String, Object> resultMap = yunxinPaymentHandler.queryAllOfflineBillCorrespondingSourceDocumentByAuditStatus(offlineBillQueryModel, page);
		List<OfflineBill> offlineBillList = (List<OfflineBill>) resultMap.get("list");
		Assert.assertEquals(1, offlineBillList.size());
	}

	
}
