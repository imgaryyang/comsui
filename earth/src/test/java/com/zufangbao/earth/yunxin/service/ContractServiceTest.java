package com.zufangbao.earth.yunxin.service;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.yunxin.entity.model.ContractQueryModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
//
		"classpath:/local/applicationContext-*.xml",
//		"classpath:/DispatcherServlet.xml"
})
@TransactionConfiguration(defaultRollback = true)
@Transactional()
public class ContractServiceTest {
	
	@Autowired
	private ContractService contractService; 
	
	@Test
	@Sql("classpath:test/yunxin/test_contract.sql")
	public void testGetContract(){
		Contract contract = contractService.getContract(1l);
		assertEquals("2016-78-DK(ZQ2016042522479)", contract.getContractNo());
		Assert.assertNotEquals(null, contract.getActiveVersionNo());
	}
	
	@Test
	@Sql("classpath:test/yunxin/test_contract.sql")
	public void testGetContractByUniqueId(){
		Contract contract = contractService.getContractByUniqueId("unique_id0001");
		Assert.assertEquals("unique_id0001",contract.getUniqueId());
		
		contract = contractService.getContractByUniqueId(null);
		Assert.assertNull(contract);
		
		contract = contractService.getContractByUniqueId("");
		Assert.assertNull(contract);
		
		contract = contractService.getContractByUniqueId("122132131418");
		Assert.assertNull(contract);
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
		int size = contractService.queryContractCountBy(contractQueryModel);
		Assert.assertEquals(3, size);
	}
	
}
