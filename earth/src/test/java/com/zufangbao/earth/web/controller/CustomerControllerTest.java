package com.zufangbao.earth.web.controller;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.web.controller.customer.CustomerController;
import com.zufangbao.sun.entity.bankCard.BankCard;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerEnterprise;
import com.zufangbao.sun.entity.customer.CustomerPerson;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.CustomerService;
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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
@Transactional
public class CustomerControllerTest {
	@Autowired
	private CustomerController customerController;
	@Autowired
	private CustomerService customerService;
	
	@Test
	@Sql("classpath:/test/yunxin/customer/test4CustomerController.sql")
	public void test_search(){
		String resultStr = customerController.search(null, null);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		CustomerQueryModel queryModel = new CustomerQueryModel();
		resultStr = customerController.search(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(6, result.get("size"));
		
		Page page = new Page(0,5);
		resultStr = customerController.search(queryModel, page);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(6, result.get("size"));
		List<CustomerShowModel> customerShowModels= JSON.parseArray(result.get("list").toString(), CustomerShowModel.class);
		Assert.assertEquals(5, customerShowModels.size());
		
		CustomerQueryModel queryModel1 = new CustomerQueryModel();
		queryModel1.setCustomerName("CustomerName1");
		queryModel1.setCustomerUuid("CustomerUuid1");
		queryModel1.setCustomerStatus(true);
		queryModel1.setCustomerStyleOrdinal(0);
		queryModel1.setIdTypeOrdinal(0);
		resultStr = customerController.search(queryModel1, null);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Assert.assertEquals(1, result.get("size"));
		customerShowModels= JSON.parseArray(result.get("list").toString(), CustomerShowModel.class);
		Assert.assertEquals("3412221997", customerShowModels.get(0).getIdNumber());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/customer/test4CustomerController.sql")
	public void test_personDetail(){
		String resultStr = "";
		Result result = null;
		String customerUuid = "";
		resultStr = customerController.personDetail(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("个人客户信息详情获取失败！！！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		customerUuid = "bucunzaide";
		resultStr = customerController.personDetail(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(Collections.emptyMap(),  result.getData());
		Assert.assertEquals("0",  result.getCode());
		
		customerUuid = "CustomerUuid11";
		resultStr = customerController.personDetail(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Customer customer = JSON.parseObject(result.get("customer").toString(), Customer.class);
		Assert.assertEquals(new Long(1), customer.getId());
		CustomerPerson customerPerson = JSON.parseObject(result.get("customerPerson").toString(), CustomerPerson.class);
		Assert.assertEquals("陈彩非", customerPerson.getName());
		List<BankCard> bankCards = JSON.parseArray(result.get("bankCards").toString(), BankCard.class);
		Assert.assertEquals(1, bankCards.size());
		Assert.assertEquals("6666667777", bankCards.get(0).getAccountNo());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/customer/test4CustomerController.sql")
	public void test_enterpriseDetail(){
		String resultStr = "";
		Result result = null;
		String customerUuid = "";
		resultStr = customerController.enterpriseDetail(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("企业客户信息详情获取失败！！！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		customerUuid = "bucunzaide";
		resultStr = customerController.enterpriseDetail(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals(Collections.emptyMap(),  result.getData());
		Assert.assertEquals("0",  result.getCode());
		
		customerUuid = "CustomerUuid12";
		resultStr = customerController.enterpriseDetail(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		Customer customer = JSON.parseObject(result.get("customer").toString(), Customer.class);
		Assert.assertEquals(new Long(2), customer.getId());
		CustomerEnterprise customerEnterprise = JSON.parseObject(result.get("customerEnterprise").toString(), CustomerEnterprise.class);
		Assert.assertEquals("测试用户321", customerEnterprise.getName());
		List<BankCard> bankCards = JSON.parseArray(result.get("bankCards").toString(), BankCard.class);
		Assert.assertEquals(0, bankCards.size());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/customer/test4CustomerController.sql")
	public void test_getRelatedContract(){
		String resultStr = "";
		Result result = null;
		String customerUuid = "";
		resultStr = customerController.getRelatedContract(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		customerUuid = "bucunzaide";
		resultStr = customerController.getRelatedContract(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		customerUuid = "CustomerUuid21";
		resultStr = customerController.getRelatedContract(customerUuid);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		List<RelatedContractShowModel> showModels = JSON.parseArray(result.get("list").toString(), RelatedContractShowModel.class);
		Assert.assertEquals(2, showModels.size());
		Assert.assertEquals("VACC27438CADB442A6A1", showModels.get(0).getVirtualAccountNo());
		Assert.assertEquals("2016-78-DK(ZQ2016042522471)", showModels.get(0).getContractNo());
		Assert.assertEquals(new BigDecimal("2400.00"), result.get("totalPrincipal"));
	}
	
	@Test
	@Sql("classpath:/test/yunxin/customer/test4CustomerController.sql")
	public void test_modifyPerson(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		
		resultStr = customerController.modifyPerson(null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		ModifyPersonModel queryModel = new ModifyPersonModel();
		queryModel.setName("ModifyPersonName");
		resultStr = customerController.modifyPerson(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());

		ModifyPersonModel queryModel1 = new ModifyPersonModel();
		queryModel1.setCustomerUuid("bucunzai");
		queryModel1.setName("ModifyPersonName");
		queryModel1.setIdNumber("123654852");
		queryModel1.setIdTypeOrdinal(0);
		resultStr = customerController.modifyPerson(queryModel1, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("该客户不存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
			
		ModifyPersonModel queryModel2 = new ModifyPersonModel();
		queryModel2.setCustomerUuid("CustomerUuid11");
		queryModel2.setName("ModifyPersonName");
		queryModel2.setIdNumber("3412221998");
		queryModel2.setIdTypeOrdinal(0);
		resultStr = customerController.modifyPerson(queryModel2, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("系统内已存在此证件号，请核对后提交！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		ModifyPersonModel queryModel3 = new ModifyPersonModel();
		queryModel3.setCustomerUuid("CustomerUuid11");
		queryModel3.setName("ModifyPersonName");
		queryModel3.setIdNumber("23654852");
		queryModel3.setIdTypeOrdinal(0);
		resultStr = customerController.modifyPerson(queryModel3, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("成功",  result.getMessage());
		Assert.assertEquals("0",  result.getCode());
		Customer customer = customerService.getCustomer("CustomerUuid11");
		Assert.assertEquals("ModifyPersonName", customer.getName());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/customer/test4CustomerController.sql")
	public void test_modifyEnterprise(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		
		resultStr = customerController.modifyEnterprise(null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		ModifyEnterpriseModel queryModel = new ModifyEnterpriseModel();
		queryModel.setName("ModifyName");
		resultStr = customerController.modifyEnterprise(queryModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		ModifyEnterpriseModel queryModel1 = new ModifyEnterpriseModel();
		queryModel1.setCustomerUuid("bucunzai");
		queryModel1.setName("ModifyName");
		queryModel1.setIdNumber("123654852");
		queryModel1.setIdTypeOrdinal(0);
		resultStr = customerController.modifyEnterprise(queryModel1, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("该客户不存在！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		ModifyEnterpriseModel queryModel2 = new ModifyEnterpriseModel();
		queryModel2.setCustomerUuid("CustomerUuid12");
		queryModel2.setName("CustomerName11");
		queryModel2.setIdNumber("3412221991");
		queryModel2.setIdTypeOrdinal(0);
		resultStr = customerController.modifyEnterprise(queryModel2, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("此公司名已在系统内存在，请核对后提交！",  result.getMessage());
		Assert.assertEquals("-1",  result.getCode());
		
		ModifyEnterpriseModel queryModel3 = new ModifyEnterpriseModel();
		queryModel3.setCustomerUuid("CustomerUuid12");
		queryModel3.setName("ModifyName");
		queryModel3.setIdNumber("23654852");
		queryModel3.setIdTypeOrdinal(0);
		resultStr = customerController.modifyEnterprise(queryModel3, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("成功",  result.getMessage());
		Assert.assertEquals("0",  result.getCode());
		Customer customer = customerService.getCustomer("CustomerUuid12");
		Assert.assertEquals("ModifyName", customer.getName());
	}
}
