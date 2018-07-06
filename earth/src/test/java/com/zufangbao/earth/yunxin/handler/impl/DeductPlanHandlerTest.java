package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.interfacc.payment.model.QueryDeductPlanShowModel;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.deduct.InterfacePaymentQueryModel;
import com.zufangbao.sun.yunxin.handler.DeductPlanCoreOperationHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class DeductPlanHandlerTest {

	@Autowired
	private DeductPlanCoreOperationHandler deductPlanHandler;

	@Test
	@Sql("classpath:test/yunxin/deductQuery/testGetInterfacePaymentShowModel.sql")
	public void testGetInterfacePaymentShowModel() {
		InterfacePaymentQueryModel interfacePaymentQueryModel = new InterfacePaymentQueryModel();
		Page page = new Page();
		//数据满足vaildInterfaceQueryModel(queryModel) ==false
		List<QueryDeductPlanShowModel> interfacePaymentQueryModels = deductPlanHandler.getInterfacePaymentShowModel(interfacePaymentQueryModel, page);
		assertEquals(Collections.emptyList(),interfacePaymentQueryModels);
		int count = deductPlanHandler.countDeductPlanSize(interfacePaymentQueryModel);
		assertEquals(0,count);
		List<QueryDeductPlanShowModel> interfacePaymentQueryModels1 = deductPlanHandler.getInterfacePaymentShowModel(interfacePaymentQueryModel, null);
		assertEquals(0, interfacePaymentQueryModels1.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/deductQuery/testGetInterfacePaymentShowModel.sql")
	public void testGetInterfacePaymentShowModel4PageIsNUll() {
		InterfacePaymentQueryModel interfacePaymentQueryModel = new InterfacePaymentQueryModel();
		Page page = null;
		interfacePaymentQueryModel.setFinancialContractIds("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		interfacePaymentQueryModel.setExecutionStatus("[2]");
		List<QueryDeductPlanShowModel> result = deductPlanHandler.getInterfacePaymentShowModel(interfacePaymentQueryModel, page);
		assertEquals(1, result.size());
		int count = deductPlanHandler.countDeductPlanSize(interfacePaymentQueryModel);
		assertEquals(1, count);
	}
	
	@Test
	@Sql("classpath:test/yunxin/deductQuery/testGetInterfacePaymentShowModel.sql")
	public void testGetInterfacePaymentShowModel2() {
		InterfacePaymentQueryModel interfacePaymentQueryModel = new InterfacePaymentQueryModel();
		Page page = new Page();
		interfacePaymentQueryModel.setFinancialContractIds("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		interfacePaymentQueryModel.setExecutionStatus("[2]");
		List<QueryDeductPlanShowModel> result = deductPlanHandler.getInterfacePaymentShowModel(interfacePaymentQueryModel, page);
		assertEquals(1, result.size());
		int count = deductPlanHandler.countDeductPlanSize(interfacePaymentQueryModel);
		assertEquals(1, count);
	}
	
	@Test
	@Sql("classpath:test/yunxin/deductQuery/testGetDeductPlanByDeductApplicaiton.sql")
	public void testGetDeductPlanByDeductApplicaiton(){
		Map<String, List<DeductPlan>> deductPlanMap = deductPlanHandler.getDeductPlanByDeductApplicaiton(null);
		assertEquals(0,deductPlanMap.size());
		
		List<DeductApplication> deductApplicationList = new ArrayList<DeductApplication>();
		DeductApplication deductApplication = new DeductApplication();
		deductApplicationList.add(deductApplication);
		deductPlanMap = deductPlanHandler.getDeductPlanByDeductApplicaiton(deductApplicationList);
		assertEquals(0,deductPlanMap.size());
	}
	
	
	
}
