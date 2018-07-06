package com.zufangbao.earth.yunxin.api;


import com.zufangbao.earth.yunxin.api.handler.RepaymentInformationApiHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.modify.RepaymentInformationModifyModel;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class RepaymentInformationApiHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private RepaymentInformationApiHandler repaymentInformationApiHandler;

	@Autowired
	private ContractService contractService;
	@Autowired
	private ContractAccountService contractAccountService;
	@Autowired
	private CityService cityService;
	@Autowired
	private ProvinceService provinceService;
	
	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformationNormal.sql")
	@Ignore("不明原因，等待后续修复")
	public void testModifyRepaymentInformationNormal() throws Exception{
			
		    RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
			MockHttpServletRequest  request = new MockHttpServletRequest();
			modifyModel.setRequestNo("1234567");
			String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
			modifyModel.setContractNo(contractNo);
			modifyModel.setBankCode("C10641");
			modifyModel.setBankProvince("500000");
			modifyModel.setBankCity("141000");
			modifyModel.setBankAccount("sdfghjkl");
			Contract contract = contractService.getContractByContractNo(contractNo);
			repaymentInformationApiHandler.modifyRepaymentInformation(modifyModel, request, contract);

        ContractAccount contractAccout = contractAccountService.getTheEfficientContractAccountBy(contract);
			Assert.assertEquals("641", contractAccout.getBankCode());
			Assert.assertEquals("C10641", contractAccout.getStandardBankCode());
			Assert.assertEquals("sdfghjkl", contractAccout.getPayAcNo());
			Assert.assertEquals("500000", contractAccout.getProvinceCode());
			Assert.assertEquals("141000", contractAccout.getCityCode());
	}
	
	/**
	 * 银行代码错误
	 * @throws Exception 
	 */
	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformation.sql")
	public void testModifyRepaymentInformationWrongBankCode() throws Exception{
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		MockHttpServletRequest  request = new MockHttpServletRequest();
		modifyModel.setRequestNo("1234567");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		String error_bank_code = "C1061";
		modifyModel.setBankCode(error_bank_code);
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankProvince("330000");
		modifyModel.setBankCity("110100");
		Contract contract = contractService.getContractByContractNo(contractNo);
		try {
			repaymentInformationApiHandler.modifyRepaymentInformation(modifyModel, request, contract);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.NO_MATCH_BANK, e.getCode());
		}
	}
	
	/**
	 * 省份代码错误
	 * @throws Exception 
	 */
	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformation.sql")
	@Ignore("不明原因，等待后续修复")
	public void testModifyRepaymentInformationWrongProvinceCode() throws Exception{
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		MockHttpServletRequest  request = new MockHttpServletRequest();
		modifyModel.setRequestNo("1234567");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		modifyModel.setBankCode("C10641");
		modifyModel.setBankAccount("sdfghjkl");
		String error_province_code = "33000";
		modifyModel.setBankProvince(error_province_code);
		modifyModel.setBankCity("110100");
		Contract contract = contractService.getContractByContractNo(contractNo);
		
		try {
			repaymentInformationApiHandler.modifyRepaymentInformation(modifyModel, request, contract);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.NO_MATCH_PROVINCE, e.getCode());
		}
	}
	
	/**
	 * 城市代码错误
	 * @throws Exception 
	 */
	@Test
	@Sql("classpath:test/yunxin/api/testModifyRepaymentInformationNormal.sql")
	@Ignore("不明原因，等待后续修复")
	public void testModifyRepaymentInformationWrongCityCode() throws Exception{
		RepaymentInformationModifyModel modifyModel = new RepaymentInformationModifyModel();
		MockHttpServletRequest request = new MockHttpServletRequest();
		modifyModel.setRequestNo("1234567");
		String contractNo = "云信信2016-78-DK(ZQ2016042522479)";
		modifyModel.setContractNo(contractNo);
		modifyModel.setBankCode("C10641");
		modifyModel.setBankAccount("sdfghjkl");
		modifyModel.setBankProvince("330000");
		String error_city_code = "11011";
		modifyModel.setBankCity(error_city_code);
		Contract contract = contractService.getContractByContractNo(contractNo);
		ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
		String provinceName = contractAccount.getProvince();
		Province province = provinceService.getProvinceByProvinceName(provinceName);
		String provinceCode = province.getCode();
		String bankCode = contractAccount.getBankCode();
		
		
		try {
			repaymentInformationApiHandler.modifyRepaymentInformation(modifyModel, request, contract);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.NO_MATCH_CITY, e.getCode());
		} 
		
		Long id = contractAccount.getId();
		ContractAccount modifyContractAccount = contractAccountService.load(ContractAccount.class, id);
		String actualprovinceName = modifyContractAccount.getProvince();
		String actualprovinceCode = provinceService.getProvinceByProvinceName(actualprovinceName).getCode();
		String actualbankCode = contractAccount.getBankCode();
		Assert.assertEquals(provinceCode, actualprovinceCode);
		Assert.assertEquals(bankCode, actualbankCode);
	}
}
