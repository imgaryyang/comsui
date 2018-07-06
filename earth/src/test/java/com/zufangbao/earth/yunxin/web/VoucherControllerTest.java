package com.zufangbao.earth.yunxin.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.yunxin.web.controller.VoucherController;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherQueryModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.VoucherShowModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional()
@WebAppConfiguration(value="webapp")
public class VoucherControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	VoucherController voucherController;
	
	@Test
	@Sql("classpath:test/yunxin/web/voucher/test_queryBusinessVoucher.sql")
	public void test_queryBusinessVoucher() {
		VoucherQueryModel voucherQueryModel = new VoucherQueryModel();
    	
    	voucherQueryModel.setFinancialContractUuids("[\"db36ecc9-d80c-4350-bd0d-59b1139d550d\"]");
    	
    	voucherQueryModel.setVoucherStatus(1);
    	
    	Principal principal = new Principal();
    	
    	MockHttpServletRequest request = new MockHttpServletRequest();
		
		String sucMsg = voucherController.queryBusinessVoucher(voucherQueryModel, principal, null, request);
		
		Result result = JsonUtils.parse(sucMsg, Result.class);
		
		int size = (int) result.getData().get("size");
		
		List<JSONObject> list = (List<JSONObject>) result.getData().get("list");
		
		Assert.assertEquals(1, size);
		
		VoucherShowModel model = JsonUtils.parse(list.get(0).toJSONString(), VoucherShowModel.class);
		
		Assert.assertEquals("0", result.getCode());
		
		Assert.assertEquals("CZ2743ED6CF5506163", model.getVoucherNo());
		
		Assert.assertEquals(new BigDecimal("11000.00"), model.getAmount());
	}
}
