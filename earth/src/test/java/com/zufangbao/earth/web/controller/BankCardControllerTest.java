package com.zufangbao.earth.web.controller;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.web.controller.bankCard.BankCardController;
import com.zufangbao.sun.entity.bankCard.BankCard;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.BankCardService;
import com.zufangbao.sun.yunxin.entity.model.BankCardModel;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
@Transactional
public class BankCardControllerTest {
	@Autowired
	private BankCardController bankCardController;
	@Autowired
	private BankCardService bankCardService;

	@Test
	@Sql("classpath:/test/yunxin/bankCard/test4BankCardController.sql")
	public void test_addBankCard(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
	    String resultStr = "";
	    Result result = null;
	    String outerIdentifier = "outerIdentifier";
	    int identityOrdinal = 0 ;
	    BankCardModel bankCardModel = new BankCardModel();
	    bankCardModel.setAccountName("陈彩非");
	    bankCardModel.setAccountNo("6666667777");
	    bankCardModel.setBankName("中国银行");
	    bankCardModel.setCity("杭州市");
	    bankCardModel.setProvince("浙江省");
	    
	    resultStr = bankCardController.addBankCard(outerIdentifier, identityOrdinal, bankCardModel, principal, request);
	    result = JsonUtils.parse(resultStr, Result.class);
	    Assert.assertEquals("0",  result.getCode());
	    BankCard bankCard = bankCardService.getFirstBankCard(outerIdentifier);
	    Assert.assertEquals("陈彩非",  bankCard.getAccountName());
	    Assert.assertEquals("浙江省",  bankCard.getProvince());
	    
	    identityOrdinal = 1;
	    resultStr = bankCardController.addBankCard(outerIdentifier, identityOrdinal, bankCardModel, principal, request);
	    result = JsonUtils.parse(resultStr, Result.class);
	    Assert.assertEquals("该银行卡已存在！",  result.getMessage());
	    Assert.assertEquals("1",  result.getCode());
	    
	    identityOrdinal = 10;
	    resultStr = bankCardController.addBankCard(outerIdentifier, identityOrdinal, bankCardModel, principal, request);
	    result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		outerIdentifier = "";
		identityOrdinal = 0;
		resultStr = bankCardController.addBankCard(outerIdentifier, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		outerIdentifier = "outerIdentifier";
		identityOrdinal = 0;
		bankCardModel = new BankCardModel();
		resultStr = bankCardController.addBankCard(outerIdentifier, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		resultStr = bankCardController.addBankCard(outerIdentifier, identityOrdinal, null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/bankCard/test4BankCardController.sql")
	public void test_modifyBankCard(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		String uuid = "02dc514a-3260-417e-a3e8-8ceecf31b310";
		int identityOrdinal = 1 ;
		BankCardModel bankCardModel = new BankCardModel();
		bankCardModel.setAccountName("张三");
		bankCardModel.setAccountNo("123456");
		bankCardModel.setBankName("中国银行");
		bankCardModel.setCity("杭州市");
		bankCardModel.setProvince("浙江省");
		
		resultStr = bankCardController.modifyBankCard(uuid, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		BankCard bankCard = bankCardService.getBankCardByUuid(uuid);
		Assert.assertEquals("张三",  bankCard.getAccountName());
		Assert.assertEquals("123456",  bankCard.getAccountNo());
		
		uuid = "111111111";
		identityOrdinal = 1;
		bankCardModel.setAccountName("李四");
		bankCardModel.setAccountNo("123456789");
		resultStr = bankCardController.modifyBankCard(uuid, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("数据错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		uuid = "02dc514a-3260-417e-a3e8-8ceecf31b310";
		identityOrdinal = 1;
		bankCardModel.setAccountName("张三");
		bankCardModel.setAccountNo("123456789");
		resultStr = bankCardController.modifyBankCard(uuid, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("该银行卡已存在！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		uuid = "02dc514a-3260-417e-a3e8-8ceecf31b310";
		identityOrdinal = 10;
		resultStr = bankCardController.modifyBankCard(uuid, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		uuid = "";
		identityOrdinal = 0;
		resultStr = bankCardController.modifyBankCard(uuid, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		uuid = "lllll";
		identityOrdinal = 0;
		bankCardModel = new BankCardModel();
		resultStr = bankCardController.modifyBankCard(uuid, identityOrdinal, bankCardModel, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		resultStr = bankCardController.modifyBankCard(uuid, identityOrdinal, null, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
	}
	
	@Test
	@Sql("classpath:/test/yunxin/bankCard/test4BankCardController.sql")
	public void test_deleteBankCard(){
		Principal principal = new Principal();
		MockHttpServletRequest request = new MockHttpServletRequest();
		String resultStr = "";
		Result result = null;
		String uuid = "qqqqwwwww";
		
		resultStr = bankCardController.deleteBankCard(uuid, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("参数错误！",  result.getMessage());
		Assert.assertEquals("1",  result.getCode());
		
		uuid = "02dc514a-3260-417e-a3e8-8ceecf31b310";
		resultStr = bankCardController.deleteBankCard(uuid, principal, request);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",  result.getCode());
		BankCard bankCard = bankCardService.getBankCardByUuid(uuid);
		Assert.assertNull(bankCard);
	}
}
