package com.zufangbao.earth.yunxin.web;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.assets.ContractController;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.ContractQueryModel;
import org.junit.Assert;
import org.junit.Before;
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

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional()
@WebAppConfiguration(value="webapp")
public class ContractControllerTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private ContractController contractController;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private FinancialContractService financialContractService;
	
	private Map<String, FinancialContract> financialContractsMap = new HashMap<String, FinancialContract>();

	@Before
	public void setUp() {
		financialContractsMap.put("d7b3b325-719a-42af-a129-0ac861f18ebe", financialContractService.load(FinancialContract.class, 1L));
	}
	
//	@Test
//	@Sql("classpath:test/yunxin/testContractList.sql")
//	public void testContractList() {
//
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setQueryString("wqerqw");
//		String contractNo = "DKHD-001";
//		String carNo = "V001";
//		String startDate = "2016-03-08";
//		String endDate = "2016-03-09";
//		String customerName = "name_1";
//		String contractStates = "[0, 1, 2, 3]";
//		Page page = new Page(1, 6);
//		ContractQueryModel contractQueryModel = new ContractQueryModel( carNo,
//				contractNo, startDate, endDate, customerName, contractStates);
//		contractQueryModel.setFinancialContractsMap(financialContractsMap);
//		String resultStr = contractController.searchContracts(contractQueryModel, page);
//		Result result = JsonUtils.parse(resultStr, Result.class);
//		List<Contract> contractList = JsonUtils.parseArray(result.get("list").toString(), Contract.class);
//		Contract contract = contractList.get(0);
//		Assert.assertEquals(new String("DKHD-001"), contract.getContractNo());
//		Assert.assertEquals(20, contract.getPeriods());
//		Assert.assertEquals(1, contract.getPaymentFrequency());
//		Assert.assertEquals(new String("安美途"), contract.getApp().getName());
//		Assert.assertEquals(0, contract.getAssetType().ordinal());
//		Assert.assertEquals(new BigDecimal("15976.15"), contract.getMonthFee());
//		Assert.assertEquals(DateUtils.parseDate(startDate, "yyyy-MM-dd"),
//				contract.getBeginDate());
//
//	}
	
	@Test
	@Sql("classpath:test/yunxin/testContractList.sql")
	public void testContractList_empty() {
		//test empty
		Page page = new Page(1, 6);
		ContractQueryModel contractQueryModel = new ContractQueryModel();
		String resultStr = contractController.searchContracts(contractQueryModel, page);
		Result result = JsonUtils.parse(resultStr, Result.class);
		List<Contract> contractList = JsonUtils.parseArray(result.get("list").toString(), Contract.class);
		assertEquals(8,contractList.size());
	}

/*	@Test
	@Sql("classpath:test/yunxin/testContractList_StartDateAndEndDate.sql")
	public void testContractList_StartDateAndEndDate() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("wqerqw");
		Principal principal = principalService.load(Principal.class, 1l);

		String app = "1";
		String contractNo = "DKHD-001";
		String carNo = "V001";
		String startDate = "2016-03-08";
		String endDate = "2016-03-09";
		String customerName = "";
		Page page = new Page(1, 6);
		ContractQueryModel contractQueryModel = new ContractQueryModel("[5]" carNo,
				contractNo, startDate, endDate, customerName);
		contractQueryModel.setContractStateOrdinals("[0,1,2,3]");
		contractQueryModel.setFinancialContractsMap(financialContractsMap);
		String resultStr = contractController.searchContracts(contractQueryModel, page);
		Result result = JsonUtils.parse(resultStr, Result.class);
		List<Contract> contractList = JsonUtils.parseArray(result.get("list").toString(), Contract.class);
		Assert.assertEquals(2, contractList.size());
	}*/

	@Test
	@Sql("classpath:test/yunxin/testContractList_StartDateAndEndDate.sql")
	public void testContractList_endDateEarlierThanStartDate() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("wqerqw");
		Principal principal = principalService.load(Principal.class, 1l);

		String app = "1";
		String contractNo = "DKHD-001";
		String carNo = "V001";
		String startDate = "2016-03-09";
		String endDate = "2017-03-08";
		String customerName = "";
		Page page = new Page(1, 6);
		ContractQueryModel contractQueryModel = new ContractQueryModel(carNo, contractNo, startDate, endDate, customerName);
		String resultStr = contractController.searchContracts(contractQueryModel, page);
		Result result = JsonUtils.parse(resultStr, Result.class);
		List<Contract> contractList = JsonUtils.parseArray(result.get("list").toString(), Contract.class);
		Assert.assertEquals(0, contractList.size());
	}

	@Test
	@Sql("classpath:test/yunxin/testContractList_StateView.sql")
	public void testContractList_StateView(){

		String contractNo = "2016-16-T(test-contract2016062402)";
		String startDate = "2016-03-09";
		String endDate = "2017-03-08";
		String customerName = "";
		Page page = new Page(1, 6);
		String contractStateOrdinals = "[\"0\",\"1\",\"2\",\"3\",\"4\",\"6\",\"7\",\"8\"]";
		ContractQueryModel contractQueryModel = new ContractQueryModel(contractNo, startDate, endDate, customerName,contractStateOrdinals);
		String resultStr = contractController.searchContracts(contractQueryModel, page);
		Result result = JsonUtils.parse(resultStr, Result.class);
		List<Contract> contractList = JsonUtils.parseArray(result.get("list").toString(), Contract.class);
		Assert.assertEquals(2, contractList.size());
	}

}
