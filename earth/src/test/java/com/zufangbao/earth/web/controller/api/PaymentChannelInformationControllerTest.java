package com.zufangbao.earth.web.controller.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.web.controller.channel.PaymentChannelInformationController;
import com.zufangbao.sun.entity.financial.BankTransactionLimitSheetUpdateModel;
import com.zufangbao.sun.yunxin.entity.model.TransactionLimitQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@WebAppConfiguration(value="webapp")
public class PaymentChannelInformationControllerTest{

	@Autowired
	private PaymentChannelInformationController paymentChannelInformationController;
	
	@Test
	@Sql("classpath:/test/yunxin/paymentChannelInformation/test4PaymentChannelInformationController.sql")
	public void testBank_Limit_Preview(){
		int paymentInstitutionOrdinal = 2;
		String outlierChannelName = null;
		int accountSide = 0;
		Page page = null;
		String resultStr = paymentChannelInformationController.bankLimitPreview(paymentInstitutionOrdinal, outlierChannelName, accountSide, page);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("请求参数错误，请重试", result.getMessage());
		
		paymentInstitutionOrdinal = -1;
		outlierChannelName = "19014526016004";
		accountSide = 0;
		resultStr = paymentChannelInformationController.bankLimitPreview(paymentInstitutionOrdinal, outlierChannelName, accountSide, page);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("请求参数错误，请重试", result.getMessage());
		
		paymentInstitutionOrdinal = 2;
		outlierChannelName = "19014526016004";
		accountSide = -1;
		resultStr = paymentChannelInformationController.bankLimitPreview(paymentInstitutionOrdinal, outlierChannelName, accountSide, page);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("请求参数错误，请重试", result.getMessage());
		
		paymentInstitutionOrdinal = 2;
		outlierChannelName = "19014526016003";
		accountSide = 0;
		resultStr = paymentChannelInformationController.bankLimitPreview(paymentInstitutionOrdinal, outlierChannelName, accountSide, page);
		result = JsonUtils.parse(resultStr, Result.class);
		List<Map<String, Object>> rtnList = JsonUtils.parseArray(result.get("list").toString());
		Assert.assertEquals(0,rtnList.size());

		paymentInstitutionOrdinal = 2;
		outlierChannelName = "19014526016005";
		accountSide = 0;
		resultStr = paymentChannelInformationController.bankLimitPreview(paymentInstitutionOrdinal, outlierChannelName, accountSide, page);
		result = JsonUtils.parse(resultStr, Result.class);
		rtnList = JsonUtils.parseArray(result.get("list").toString());
		Assert.assertEquals(1,rtnList.size());
	}
	
	
	
	@Test
	@Sql("classpath:/test/yunxin/paymentChannelInformation/test4PaymentChannelInformationController.sql")
	public void testSearch_Bank_Transaction_Limit(){
		TransactionLimitQueryModel queryModel = new TransactionLimitQueryModel();
//		queryModel.setGateway(-1);
//		queryModel.setOutlierChannelName(null);
//		queryModel.setAccountSide(-1);
//		queryModel.setKeyWord(null);
		String resultStr = paymentChannelInformationController.searchBankTransactionLimit(queryModel, null);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Map<String, Object> resultMap = result.getData();
		Assert.assertEquals(5,resultMap.get("size"));
		
		queryModel.setGateway(2);
		queryModel.setOutlierChannelName(null);
		queryModel.setAccountSide(-1);
		queryModel.setKeyWord(null);
		resultStr = paymentChannelInformationController.searchBankTransactionLimit(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals(4,resultMap.get("size"));
	
		queryModel.setGateway(2);
		queryModel.setOutlierChannelName("19014526016004");
		queryModel.setAccountSide(-1);
		queryModel.setKeyWord(null);
		resultStr = paymentChannelInformationController.searchBankTransactionLimit(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals(3,resultMap.get("size"));
		
		queryModel.setGateway(2);
		queryModel.setOutlierChannelName("19014526016004");
		queryModel.setAccountSide(1);
		queryModel.setKeyWord(null);
		resultStr = paymentChannelInformationController.searchBankTransactionLimit(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals(2,resultMap.get("size"));
		
		queryModel.setGateway(2);
		queryModel.setOutlierChannelName("19014526016004");
		queryModel.setAccountSide(1);
		queryModel.setKeyWord("招商银行");
		resultStr = paymentChannelInformationController.searchBankTransactionLimit(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals(1,resultMap.get("size"));
		
		queryModel.setGateway(2);
		queryModel.setOutlierChannelName("19014526016004");
		queryModel.setAccountSide(1);
		queryModel.setKeyWord("中国银行");
		resultStr = paymentChannelInformationController.searchBankTransactionLimit(queryModel, null);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals(0,resultMap.get("size"));
		
	}
	
	@Test
	@Sql("classpath:/test/yunxin/paymentChannelInformation/test4PaymentChannelInformationController.sql")
	public void testUpdate_Bank_Transaction_Limit(){
		
		BankTransactionLimitSheetUpdateModel listModel = new BankTransactionLimitSheetUpdateModel();
		String resultStr = paymentChannelInformationController.updateBankTransactionLimit(listModel);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("-1",result.getCode());
		
		listModel = new BankTransactionLimitSheetUpdateModel();
		listModel.setBankTransactionLimitSheetUuid("bd3eb3d7-9166-4f33-88e4-ae5587f639cb");
		listModel.setTranscationLimitPerDay(new BigDecimal("2000.00"));
		listModel.setTransactionLimitPerMonth(new BigDecimal("20000.00"));
		listModel.setTransactionLimitPerTranscation(new BigDecimal("200000.00"));
		resultStr = paymentChannelInformationController.updateBankTransactionLimit(listModel);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("0",result.getCode());
		
	}
	
	
	//唯一编号解析
	@Test
	public void analyzeUniqueNoTest() throws UnsupportedEncodingException {
		String uniqueNo = null;
		String resultStr = paymentChannelInformationController.analyzeUniqueNo(uniqueNo);
		Result result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("请输入唯一编号！！", result.getMessage());
		
		final Base64.Encoder encoder = Base64.getEncoder();
		uniqueNo = "12334;;zhangsan;";
		byte[] textByte = uniqueNo.getBytes("UTF-8");
		//编码
		String encodedText = encoder.encodeToString(textByte);
		resultStr = paymentChannelInformationController.analyzeUniqueNo(encodedText);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("唯一编号不合法！！", result.getMessage());
		
		uniqueNo = "12345;;zhangsan;20;";
		textByte = uniqueNo.getBytes("UTF-8");
		//编码
		encodedText = encoder.encodeToString(textByte);
		resultStr = paymentChannelInformationController.analyzeUniqueNo(encodedText);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("唯一编号不合法！！", result.getMessage());
		
		uniqueNo = "12345;;zhangsan;ll;";
		textByte = uniqueNo.getBytes("UTF-8");
		//编码
		encodedText = encoder.encodeToString(textByte);
		resultStr = paymentChannelInformationController.analyzeUniqueNo(encodedText);
		result = JsonUtils.parse(resultStr, Result.class);
		Assert.assertEquals("唯一编号解析失败！请仔细核对！", result.getMessage());
		
		uniqueNo = "098765;;zhangsan;1;";
		textByte = uniqueNo.getBytes("UTF-8");
		//编码
		encodedText = encoder.encodeToString(textByte);
		resultStr = paymentChannelInformationController.analyzeUniqueNo(encodedText);
		result = JsonUtils.parse(resultStr, Result.class);
		Map<String, Object> resultMap = result.getData();
		Assert.assertEquals("超级网银",resultMap.get("paymentInstitutionName"));
		
		
		uniqueNo = ";098765;zhangsan;1;";
		textByte = uniqueNo.getBytes();
		//编码
		encodedText = encoder.encodeToString(textByte);
		resultStr = paymentChannelInformationController.analyzeUniqueNo(encodedText);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals("超级网银",resultMap.get("paymentInstitutionName"));
		
		uniqueNo = "56788;098765;zhangsan;1;";
		textByte = uniqueNo.getBytes();
		//编码
		encodedText = encoder.encodeToString(textByte);
		resultStr = paymentChannelInformationController.analyzeUniqueNo(encodedText);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals("超级网银",resultMap.get("paymentInstitutionName"));
		
		uniqueNo = ";098765;zhangsan;1;lll";
		textByte = uniqueNo.getBytes();
		//编码
		encodedText = encoder.encodeToString(textByte);
		resultStr = paymentChannelInformationController.analyzeUniqueNo(encodedText);
		result = JsonUtils.parse(resultStr, Result.class);
		resultMap = result.getData();
		Assert.assertEquals("超级网银",resultMap.get("paymentInstitutionName"));
		
	}
}
