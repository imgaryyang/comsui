//package com.zufangbao.earth.yunxin.web;
//
//import javax.transaction.Transactional;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.zufangbao.earth.yunxin.web.controller.OperationDataController;
//import com.zufangbao.sun.yunxin.entity.model.reportform.OperationDataQueryModel;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//
//		"classpath:/local/applicationContext-*.xml",
//		"classpath:/DispatcherServlet.xml" })
//
//@Transactional()
//@TransactionConfiguration(defaultRollback = true)
//@WebAppConfiguration
//public class OperationDataControllerTest {
//
//	@Autowired
//	OperationDataController operationDataController;
//
//	@Autowired
//	private WebApplicationContext wac;
//
//	private MockMvc mockMvc;
//
////	@Before
////	public void setUP() {
////		// TODO Auto-generated method stub
////		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
////	}
//
//	@Test
//	public void test_export() throws Exception {
//		OperationDataQueryModel queryModel = new OperationDataQueryModel();
//		//List<String> financialContractUuids = new ArrayList<>();
//		String financialContractUuid1 = "[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]";
//		//String financialContractUuid2 = "d2812bc5-5057-4a91-b3fd-9019506f0499";
//		//financialContractUuids.add(financialContractUuid1);
//		//financialContractUuids.add(financialContractUuid2);
//		queryModel.setFinancialContractUuids(financialContractUuid1);
//		//queryModel.setQueryDate("2017-03-30");
//		queryModel.setQueryDate("2017-04-06");
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		MockHttpServletResponse response = new MockHttpServletResponse();
//		operationDataController.export(queryModel, request, response);
//
////		MultiValueMap<String, String> parameters = new  LinkedMultiValueMap<>();
////
////		parameters.add("financialContractUuids", financialContractUuid1);
////		parameters.add("queryDate", "2017-03-30");
////
////		mockMvc.perform(get("/operation-data/export").params(parameters).accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk());
//
//	}
//}
