package com.zufangbao.earth.yunxin.handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartyVoucherBatchHandler;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.ThirdPartyVoucherBatchQueryModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherBatchShowModel;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ThirdPartyVoucherBatchHandlerTest {

	@Autowired
	ThirdPartyVoucherBatchHandler thirdPartyVoucherBatchHandler;

	private final Page page = new Page(1);

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/wwwww.sql")
	public void getThirdPartVoucherBatchByUuidTest() {
		int size = thirdPartyVoucherBatchHandler.countThirdPartVoucherBatchNum(generateQueryModel());
		assertEquals(2, size);
	}

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/wwwww.sql")
	public void getThirdPartVoucherBatchShowModelListTest() {
		List<ThirdPartVoucherBatchShowModel> showModels = thirdPartyVoucherBatchHandler
				.getThirdPartVoucherBatchShowModelList(generateQueryModel(), page);
		assertEquals(2, showModels.size());
	}

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/wwwww.sql")
	public void getThirdPartVoucherCommandLogShowModelTest() {
		String batchUuid = "a78d48ab-0950-11e7-9677-c83a35ce563d";
		ThirdPartVoucherBatchShowModel showModel = thirdPartyVoucherBatchHandler
				.getThirdPartVoucherCommandLogShowModel(batchUuid);
		assertNotNull(showModel);
		assertEquals("a78d48ab-0950-11e7-9677-c83a35ce563d", showModel.getBatchUuid());
		assertEquals(DateUtils.parseDate("2017-03-15 15:32:52", "yyyy-MM-dd HH:mm:ss"), showModel.getCreateTime());
		assertEquals("a78d48d0-0950-11e7-9677-c83a35ce563d", showModel.getFinancialContractNo());
		assertEquals("666214124124", showModel.getRequestNo());
		assertEquals(10, showModel.getSize());
	}

	private ThirdPartyVoucherBatchQueryModel generateQueryModel() {
		ThirdPartyVoucherBatchQueryModel queryModel = new ThirdPartyVoucherBatchQueryModel();
		queryModel.setStartTime("2017-03-14 15:32:52");
		queryModel.setEndTime("2017-03-16 15:32:52");
		queryModel.setFinancialContractUuids("[\"111cfcfb-0951-11e7-9677-c83a35ce563d\"]");
		return queryModel;
	}
	
	@Test
	public void enumUtilsTest(){
		//ordinal和中文释义組成map
		List<Map<String, Object>> list=EnumUtil.getKVList(PaymentInstitutionName.class);
		assertEquals(8, list.size());
		assertEquals(0, list.get(0).get("key"));
		assertEquals("银联广州", list.get(0).get("value"));
		assertEquals(2, list.get(0).entrySet().size());

	}
}







