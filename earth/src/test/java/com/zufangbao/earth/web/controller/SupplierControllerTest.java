package com.zufangbao.earth.web.controller;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.web.controller.supplier.SupplierController;
import com.zufangbao.sun.entity.bankCard.BankCard;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.supplier.Supplier;
import com.zufangbao.sun.service.SupplierService;
import com.zufangbao.sun.yunxin.entity.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
@Transactional
public class SupplierControllerTest {
	@Autowired
	private SupplierController supplierController;
	@Autowired
	private SupplierService supplierService;

	@Test
	@Sql("classpath:/test/yunxin/supplier/test4SupplierController.sql")
	public void test_searchSupplier(){
		String resultStr = supplierController.searchSupplier(null, null);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		SupplierQueryModel queryModel = new SupplierQueryModel();
		resultStr = supplierController.searchSupplier(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(8, result.get("size"));
		
		Page page = new Page(0,5);
		resultStr = supplierController.searchSupplier(queryModel, page);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(8, result.get("size"));
		List<SupplierShowModel> showModels = JSON.parseArray(result.get("list").toString(), SupplierShowModel.class);
		Assert.assertEquals(5, showModels.size());
		
		SupplierQueryModel queryModel1 = new SupplierQueryModel();
		queryModel1.setSupplierUuid("c496a45d-3d9f-417e-905c-dfd2871d827");
		queryModel1.setSupplierName("supplier");
		resultStr = supplierController.searchSupplier(queryModel1, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(6, result.get("size"));
		showModels = JSON.parseArray(result.get("list").toString(), SupplierShowModel.class);
		Assert.assertEquals("supplier1", showModels.get(showModels.size() - 1).getName());
		Assert.assertEquals("陈彩非", showModels.get(showModels.size() - 1).getAccountName());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/supplier/test4SupplierController.sql")
	public void test_detail(){
		Supplier supplier = null;
		List<BankCard> bankCards = null;
		List<SupplierIdentityShowModel> sisms = null;
		String uuid = "";
		String resultStr = supplierController.detail(uuid);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("供应商信息详情获取失败！！！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		uuid = "c496a45d-3d9f-417e-905c-dfd2871d8271";
		resultStr = supplierController.detail(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		supplier = JSON.parseObject(result.get("supplier").toString(), Supplier.class);
		bankCards = JSON.parseArray(result.get("bankCards").toString(), BankCard.class);
		sisms = JSON.parseArray(result.get("sisms").toString(), SupplierIdentityShowModel.class);
		Assert.assertEquals("supplier1",  supplier.getName());
		Assert.assertEquals(2, bankCards.size());
		Assert.assertEquals("2016-78-DK(ZQ2016042522479)", sisms.get(0).getContractNo());
		
		uuid = "c496a45d-3d9f-417e-905c-dfd2871d8272";
		resultStr = supplierController.detail(uuid);
		result = JsonUtils.parse(resultStr, Result.class);
		supplier = JSON.parseObject(result.get("supplier").toString(), Supplier.class);
		bankCards = JSON.parseArray(result.get("bankCards").toString(), BankCard.class);
		sisms = JSON.parseArray(result.get("sisms").toString(), SupplierIdentityShowModel.class);
		Assert.assertEquals("supplier2",  supplier.getName());
		Assert.assertEquals(Collections.emptyList(),bankCards);
		Assert.assertEquals(Collections.emptyList(),sisms);
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/supplier/test4SupplierController.sql")
	public void test_createSupplier(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		CreateSupplierModel queryModel = null;
		BankCardModel bankCardModel = new BankCardModel();
		String resultStr = "";
		Result result = null;
		
		resultStr = supplierController.createSupplier(null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierName("createSupplier");
		resultStr = supplierController.createSupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		bankCardModel.setAccountName("accountName");
		bankCardModel.setAccountNo("123456789");
		bankCardModel.setBankName("中国银行");
		bankCardModel.setCity("杭州市");
		bankCardModel.setProvince("浙江省");
		List<BankCardModel> bankCardModels = new ArrayList<>();
		bankCardModels.add(bankCardModel);
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierName("supplier1");
		bankCardModels.add(bankCardModel);
		queryModel.setBankInfo(JSON.toJSONString(bankCardModels));
		resultStr = supplierController.createSupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("供应商名称已存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		bankCardModel.setAccountName("陈彩非");
		bankCardModel.setAccountNo("6666667777");
		bankCardModel.setBankName("中国银行");
		bankCardModel.setCity("杭州市");
		bankCardModel.setProvince("浙江省");
		List<BankCardModel> bankCardModels1 = new ArrayList<>();
		bankCardModels1.add(bankCardModel);
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierName("createSupplier");
		queryModel.setBankInfo(JSON.toJSONString(bankCardModels1));
		resultStr = supplierController.createSupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("该银行卡已存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		bankCardModel.setAccountName("accountName");
		bankCardModel.setAccountNo("123456789");
		bankCardModel.setBankName("中国银行");
		bankCardModel.setCity("杭州市");
		bankCardModel.setProvince("浙江省");
		List<BankCardModel> bankCardModels2 = new ArrayList<>();
		bankCardModels2.add(bankCardModel);
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierName("createSupplier1");
		queryModel.setBankInfo(JSON.toJSONString(bankCardModels2));
		resultStr = supplierController.createSupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("成功",  result.getMessage());
		Assert.assertEquals("0",  result.getCode());
		Supplier supplier = supplierService.getSupplierByName("createSupplier");
		Assert.assertNotNull(supplier);
	}
	
	@Test
	@Sql("classpath:/test/yunxin/supplier/test4SupplierController.sql")
	public void test_modifySupplier(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		CreateSupplierModel queryModel = null;
		String resultStr = "";
		Result result = null;
		
		resultStr = supplierController.modifySupplier(null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierName("modifySupplier");
		resultStr = supplierController.modifySupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierUuid("modifySupplier");
		queryModel.setSupplierName("modifySupplier");
		resultStr = supplierController.modifySupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("该供应商不存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierUuid("c496a45d-3d9f-417e-905c-dfd2871d8271");
		queryModel.setSupplierName("supplier2");
		resultStr = supplierController.modifySupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("供应商名称已存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		queryModel = new CreateSupplierModel();
		queryModel.setSupplierUuid("c496a45d-3d9f-417e-905c-dfd2871d8271");
		queryModel.setSupplierName("modifySupplier");
		queryModel.setLegalPerson("lisi");
		resultStr = supplierController.modifySupplier(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("成功",  result.getMessage());
		Assert.assertEquals("0",  result.getCode());
		Supplier supplier = supplierService.getSupplierByUuid("c496a45d-3d9f-417e-905c-dfd2871d8271");
		Assert.assertEquals("modifySupplier", supplier.getName());
		Assert.assertEquals("lisi", supplier.getLegalPerson());
	}
}
