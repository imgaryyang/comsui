package com.zufangbao.earth.web.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

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

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.web.controller.RepurchaseController;
import com.zufangbao.earth.yunxin.web.controller.VirtualAccountController;
import com.zufangbao.sun.entity.account.BankAccountAdapter;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback=true)
@WebAppConfiguration(value="webapp")
public class VirtualAccountControllerTest{
	
	@Autowired
	private VirtualAccountController virtualAccountController;
	
	@Autowired
	private PrincipalService principalService;
	
	@Autowired
	private ContractAccountService contractAccountService;
	
	@Test
	@Sql("classpath:/test/yunxin/web/virtual_account_controller_update.sql")
	public void testEditContractAccount(){
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		Principal principal = principalService.load(Principal.class, 1l);
		String virtualAccountUuid = "4b6e315a-7f95-4203-b081-efc0d3b28f9e";
		String uuid = "d45f1f4260954e13aa4e6197a7c9a88a";
		
		ContractAccount account = contractAccountService.getContractAccountByUuid(uuid);
		
		String bankAccountAdapterJson = "{\"bankCode\":\"C10302\",\"accountNo\":\"aa123dhww\",\"provinceCode\":\"530000\",\"province\":\"云南省\",\"cityCode\":\"530100\",\"city\":\"保山市\",\"accountName\":\"dcc\",\"idCardNo\":\"341225199207070128\",\"bankCardStatus\":\"BINDING\",\"bankCardStatusName\":\"绑定\",\"bankName\":\"中信银行\",\"default\":false,\"type\":0,\"uuid\":\"d45f1f4260954e13aa4e6197a7c9a88a\"}";
		
		virtualAccountController.updateAfterBankCard(principal, bankAccountAdapterJson, virtualAccountUuid, uuid, request);
		
		ContractAccount newAccount = contractAccountService.getContractAccountByUuid(uuid);
		Assert.assertEquals("aa123dhww", newAccount.getPayAcNo());
		Assert.assertEquals("保山市", newAccount.getCity());
		Assert.assertEquals("dcc", newAccount.getPayerName());
		
	}
		
	
	@Test
	@Sql("classpath:/test/yunxin/web/virtual_account_controller_update2.sql")
	public void testEditContractAccount2(){
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		Principal principal = principalService.load(Principal.class, 1l);
		String virtualAccountUuid = "4b6e315a-7f95-4203-b081-efc0d3b28f9e";
		String uuid = "d45f1f4260954e13aa4e6197a7c9a88a";
		
		ContractAccount account = contractAccountService.getContractAccountByUuid(uuid);
		
		String bankAccountAdapterJson = "{\"bankCode\":\"C10302\",\"accountNo\":\"aa123dhww\",\"provinceCode\":\"530000\",\"province\":\"云南省\",\"cityCode\":\"530100\",\"city\":\"保山市\",\"accountName\":\"dcc\",\"idCardNo\":\"341225199207070128\",\"bankCardStatus\":\"BINDING\",\"bankCardStatusName\":\"绑定\",\"bankName\":\"中信银行\",\"default\":false,\"type\":0,\"uuid\":\"d45f1f4260954e13aa4e6197a7c9a88a\"}";
		
		String resultString = virtualAccountController.updateAfterBankCard(principal, bankAccountAdapterJson, virtualAccountUuid, uuid, request);
		Result result = JsonUtils.parse(resultString, Result.class);
		assertEquals("编辑的账户号已存在！", result.getMessage());
	}
		
	
	@Test
	@Sql("classpath:/test/yunxin/web/virtual_account_controller_add.sql")
	public void testAddContractAccount(){
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		Principal principal = principalService.load(Principal.class, 1l);
		String virtualAccountUuid = "4b6e315a-7f95-4203-b081-efc0d3b28f9e";
		String uuid = "d45f1f4260954e13aa4e6197a7c9a88a";
		Long contractId = new Long("39662");
		String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
		
		List<ContractAccount> accountList = contractAccountService.loadAll(ContractAccount.class);
		assertEquals(1, accountList.size());
		
		String bankAccountAdapterJson = "{\"bankCode\":\"C10302\",\"accountNo\":\"aa123dhww\",\"provinceCode\":\"530000\",\"province\":\"云南省\",\"cityCode\":\"530500\",\"city\":\"保山市\",\"accountName\":\"1\",\"idCardNo\":\"341225199207070128\",\"bankName\":\"中信银行\"}";
		
		String resultString = virtualAccountController.insertBankCard(principal, request, bankAccountAdapterJson, virtualAccountUuid, contractId, financialContractUuid);
		
		List<ContractAccount> accountList2 = contractAccountService.loadAll(ContractAccount.class);
		assertEquals(2, accountList2.size());
		ContractAccount newAccount = accountList2.get(1);
		Assert.assertEquals("aa123dhww", newAccount.getPayAcNo());
		Assert.assertEquals("保山市", newAccount.getCity());
		Assert.assertEquals("中信银行", newAccount.getBank());
		Assert.assertEquals("C10302", newAccount.getBankCode());
		Assert.assertEquals("530500", newAccount.getCityCode());
		Assert.assertEquals("云南省", newAccount.getProvince());
		Assert.assertEquals("530000", newAccount.getProvinceCode());
		Assert.assertEquals("341225199207070128", newAccount.getIdCardNum());
		Assert.assertEquals("1", newAccount.getPayerName());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/web/virtual_account_controller_add2.sql")
	public void testAddContractAccount2(){
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		Principal principal = principalService.load(Principal.class, 1l);
		String virtualAccountUuid = "4b6e315a-7f95-4203-b081-efc0d3b28f9e";
		String uuid = "d45f1f4260954e13aa4e6197a7c9a88a";
		Long contractId = new Long("39662");
		String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
		
		List<ContractAccount> accountList = contractAccountService.loadAll(ContractAccount.class);
		assertEquals(1, accountList.size());
		
		String bankAccountAdapterJson = "{\"bankCode\":\"C10302\",\"accountNo\":\"aa123dhww\",\"provinceCode\":\"530000\",\"province\":\"云南省\",\"cityCode\":\"530500\",\"city\":\"保山市\",\"accountName\":\"1\",\"idCardNo\":\"341225199207070128\",\"bankName\":\"中信银行\"}";
		
		String resultString = virtualAccountController.insertBankCard(principal, request, bankAccountAdapterJson, virtualAccountUuid, contractId, financialContractUuid);
		
		Result result = JsonUtils.parse(resultString, Result.class);
		assertEquals("新增的账户号已存在！", result.getMessage());
	}
}
