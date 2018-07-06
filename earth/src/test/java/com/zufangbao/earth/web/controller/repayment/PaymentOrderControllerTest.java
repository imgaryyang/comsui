package com.zufangbao.earth.web.controller.repayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.yunxin.api.controller.Api_V3_Controller;
import com.zufangbao.earth.yunxin.api.controller.QueryApiController;
import com.zufangbao.earth.yunxin.api.handler.PaymentOrderApiHandler;
import com.zufangbao.earth.yunxin.api.model.ApiResult;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetail;
import com.zufangbao.earth.yunxin.web.controller.PaymentOrderController;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.repayment.QueryPaymentOrderRequestModel;
import com.zufangbao.sun.api.model.repayment.QueryPaymentOrderResults;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.model.paymentOrder.PaymentOrderSubmitModel;

/**
 * Created by zhusy on 2017/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml" })

@WebAppConfiguration(value="webapp")
public class PaymentOrderControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private PaymentOrderController paymentOrderController;
    @Autowired
    private Api_V3_Controller api_V3_Controller;
    
    @Autowired
    private PaymentOrderApiHandler paymentOrderApiHandler;
    
    @Autowired
    private RepaymentOrderHandler repaymentOrderHandler;
    
    private MockHttpServletResponse response = new MockHttpServletResponse();

	private MockHttpServletRequest request = new MockHttpServletRequest();
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders.sql")
    public void queryPaymentOrderForFinancialContractNo(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	
    	model.setOrderUuidList("order_uuid_1");
    	model.setFinancialContractNo("");
    	model.setRequestNo("fdgfdcgd");
    	
    	String result = api_V3_Controller.queryPaymentOrder(request, response, model);
    	Assert.assertEquals("{\"code\":20001,\"message\":\"信托合同编号［financialContractNo],不能为空！\"}",result);
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders.sql")
    public void queryPaymentOrderForOrderUuid(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	
    	model.setOrderUuidList("");
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfdcgd");
    	
    	String result = api_V3_Controller.queryPaymentOrder(request, response, model);
    	Assert.assertEquals("{\"code\":20001,\"message\":\"商户支付编号，五维支付编号，商户订单号，五维订单号 四只能选其一必填！ \"}",result);
    }
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders.sql")
    public void queryPaymentOrderForOrderUuid2(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	List<String> orderUuids = new ArrayList<String>();
    	orderUuids.add("order_uuid_1");
    	
    	List<String> orderUiqueIds = new ArrayList<String>();
    	orderUiqueIds.add("orderUniqueId");
    	
    	model.setOrderUuidList(JsonUtils.toJsonString(orderUuids));
    	model.setOrderUniqueIdList(JsonUtils.toJsonString(orderUiqueIds));
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfdcgd");
    	
    	String result = api_V3_Controller.queryPaymentOrder(request, response, model);
    	Assert.assertEquals("{\"code\":20001,\"message\":\"商户支付编号，五维支付编号，商户订单号，五维订单号 四只能选其一必填！ \"}",result);
    }

    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders.sql")
    public void queryPaymentOrderFinancialContractNo2(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	
    	model.setOrderUuidList("");
    	model.setFinancialContractNo("G082003");
    	model.setRequestNo("fdgfdcgd");
    	
    	try {
			
    		paymentOrderApiHandler.queryPaymentOrderResponeData(model);
		} catch (ApiException e) {
			Assert.assertEquals(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST,e.getCode());
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders_fincianal_contract_no.sql")
    public void queryPaymentOrderFinancialContractNo3(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	List<String> orderUuids = new ArrayList<String>();
    	orderUuids.add("order_uuid_1");
    	
    	model.setOrderUuidList(JsonUtils.toJsonString(orderUuids));
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfdcgd");
    	
    	try {
			
    		paymentOrderApiHandler.queryPaymentOrderResponeData(model);
    		Assert.fail();
		} catch (ApiException e) {
			ApiException exception = (ApiException) e;
			Assert.assertEquals(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EQUAL_FOR_REQUESTMODE_EXIST,((ApiException) e).getCode());
		}
    }
    
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders.sql")
    public void queryPaymentOrder(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	
    	List<String> orderUuids = new ArrayList<String>();
    	orderUuids.add("order_uuid_1");
    	model.setOrderUuidList(JsonUtils.toJsonString(orderUuids));
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfdcgd");
    	
    	try {
    		
    		List<Map<String, Object>> queryPaymentOrderResultLists = paymentOrderApiHandler.queryPaymentOrderResponeData(model);
    		
    		Assert.assertEquals(1, queryPaymentOrderResultLists.size());
    		
    		Map<String, Object> map = queryPaymentOrderResultLists.get(0);
    		String result_uniqueId = (String) map.get("orderUniqueId");
    		Assert.assertEquals("orderUniqueId", result_uniqueId);
    		List<QueryPaymentOrderResults> planOrderResults = (List<QueryPaymentOrderResults>) map.get("queryPaymentOrderResults");
    		Assert.assertEquals(2, planOrderResults.size());

    		
    		QueryPaymentOrderResults result1 = planOrderResults.get(0);
    		QueryPaymentOrderResults result2 = planOrderResults.get(1);

    		Assert.assertEquals(1, result1.getPayStatus());
    		Assert.assertEquals("", result1.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result1.getCompletedTime());
    		Assert.assertEquals("G08200", result1.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_1", result1.getOrderUuid());
    		Assert.assertEquals("", result1.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_1", result1.getPaymentUuid());
    		Assert.assertEquals("bbb", result1.getRemark());
    		
    		
    		Assert.assertEquals(1, result2.getPayStatus());
    		Assert.assertEquals("", result2.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result2.getCompletedTime());
    		Assert.assertEquals("G08200", result2.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_1", result2.getOrderUuid());
    		Assert.assertEquals("", result2.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_2", result2.getPaymentUuid());
    		Assert.assertEquals("aaa", result2.getRemark());
			
		} catch (Exception e) {
			Assert.fail();
		}
		
    }
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders3.sql")
    public void queryPaymentOrder3(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	
    	List<String> paymentNos = new ArrayList<String>();
    	paymentNos.add("payment_no_1");
    	paymentNos.add("payment_no_2");
    	
    	model.setPaymentNoList(JsonUtils.toJsonString(paymentNos));
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfdcgd");
    	
    	try {
    		
    		List<Map<String, Object>> queryPaymentOrderResultLists = paymentOrderApiHandler.queryPaymentOrderResponeData(model);
    		
    		Assert.assertEquals(1, queryPaymentOrderResultLists.size());
    		
    		Map<String, Object> map = queryPaymentOrderResultLists.get(0);
    		String result_uniqueId = (String) map.get("orderUniqueId");
    		Assert.assertEquals("orderUniqueId", result_uniqueId);
    		List<QueryPaymentOrderResults> planOrderResults = (List<QueryPaymentOrderResults>) map.get("queryPaymentOrderResults");
    		Assert.assertEquals(2, planOrderResults.size());

    		
    		QueryPaymentOrderResults result1 = planOrderResults.get(0);
    		QueryPaymentOrderResults result2 = planOrderResults.get(1);

    		Assert.assertEquals(1, result1.getPayStatus());
    		Assert.assertEquals("2017-07-07 17:10:14", result1.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result1.getCompletedTime());
    		Assert.assertEquals("G08200", result1.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_1", result1.getOrderUuid());
    		Assert.assertEquals("payment_no_1", result1.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_1", result1.getPaymentUuid());
    		Assert.assertEquals("bbb", result1.getRemark());
    		
    		
    		Assert.assertEquals(1, result2.getPayStatus());
    		Assert.assertEquals("2017-07-08 17:10:14", result2.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result2.getCompletedTime());
    		Assert.assertEquals("G08200", result2.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_1", result2.getOrderUuid());
    		Assert.assertEquals("payment_no_2", result2.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_2", result2.getPaymentUuid());
    		Assert.assertEquals("aaa", result2.getRemark());
			
		} catch (Exception e) {
			Assert.fail();
		}
		
    }
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders2.sql")
    public void queryPaymentOrder2(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	
    	List<String> orderUuids = new ArrayList<String>();
    	orderUuids.add("order_uuid_1");
    	orderUuids.add("order_uuid_2");
    	model.setOrderUuidList(JsonUtils.toJsonString(orderUuids));
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfsdcgd");
    	
    	try {
    		
    		List<Map<String, Object>> queryPaymentOrderResultLists = paymentOrderApiHandler.queryPaymentOrderResponeData(model);
    		Assert.assertEquals(2, queryPaymentOrderResultLists.size());
    		
    		Map<String, Object> map = queryPaymentOrderResultLists.get(1);
    		Map<String, Object> map2 = queryPaymentOrderResultLists.get(0);
    		String result_uniqueId = (String) map.get("orderUniqueId");
    		String result_uniqueId2 = (String) map2.get("orderUniqueId");
    		Assert.assertEquals("orderUniqueId", result_uniqueId);
    		List<QueryPaymentOrderResults> planOrderResults = (List<QueryPaymentOrderResults>) map.get("queryPaymentOrderResults");
    		Assert.assertEquals(2, planOrderResults.size());

    		
    		QueryPaymentOrderResults result1 = planOrderResults.get(0);
    		QueryPaymentOrderResults result2 = planOrderResults.get(1);

    		Assert.assertEquals(1, result1.getPayStatus());
    		Assert.assertEquals("2017-07-07 17:10:14", result1.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result1.getCompletedTime());
    		Assert.assertEquals("G08200", result1.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_1", result1.getOrderUuid());
    		Assert.assertEquals("", result1.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_1", result1.getPaymentUuid());
    		Assert.assertEquals("bbb", result1.getRemark());
    		
    		
    		Assert.assertEquals(1, result2.getPayStatus());
    		Assert.assertEquals("2017-07-08 17:10:14", result2.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result2.getCompletedTime());
    		Assert.assertEquals("G08200", result2.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_1", result2.getOrderUuid());
    		Assert.assertEquals("", result2.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_2", result2.getPaymentUuid());
    		Assert.assertEquals("aaa", result2.getRemark());
    		
    		
    		Map<String, Object> resultMap = queryPaymentOrderResultLists.get(0);
    		String uniqueId = (String) resultMap.get("orderUniqueId");
    		Assert.assertEquals("orderUniqueId2", uniqueId);
    		List<QueryPaymentOrderResults> orderResults = (List<QueryPaymentOrderResults>) resultMap.get("queryPaymentOrderResults");
    		Assert.assertEquals(2, orderResults.size());
    		
    		QueryPaymentOrderResults result3 = orderResults.get(0);
    		QueryPaymentOrderResults result4 = orderResults.get(1);

    		Assert.assertEquals(1, result3.getPayStatus());
    		Assert.assertEquals("", result3.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:09:57", result3.getCompletedTime());
    		Assert.assertEquals("G08200", result3.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_2", result3.getOrderUuid());
    		Assert.assertEquals("", result3.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_3", result3.getPaymentUuid());
    		Assert.assertEquals("ccc", result3.getRemark());
    		
    		
    		Assert.assertEquals(1, result4.getPayStatus());
    		Assert.assertEquals("", result4.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:09:57", result4.getCompletedTime());
    		Assert.assertEquals("G08200", result4.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_2", result4.getOrderUuid());
    		Assert.assertEquals("", result4.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_4", result4.getPaymentUuid());
    		Assert.assertEquals("ddd", result4.getRemark());
    		
			
		} catch (Exception e) {
			Assert.fail();
		}
		
    }
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders4.sql")
    public void queryPaymentOrder4(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	
    	List<String> paymentNos = new ArrayList<String>();
    	paymentNos.add("payment_no_1");
    	paymentNos.add("payment_no_2");
    	
    	model.setPaymentNoList(JsonUtils.toJsonString(paymentNos));
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfdcgd");
    	
    	try {
    		
    		List<Map<String, Object>> queryPaymentOrderResultLists = paymentOrderApiHandler.queryPaymentOrderResponeData(model);
    		
    		Assert.assertEquals(2, queryPaymentOrderResultLists.size());
    		
    		Map<String, Object> map = queryPaymentOrderResultLists.get(1);
    		Map<String, Object> map2 = queryPaymentOrderResultLists.get(0);
    		String result_uniqueId = (String) map.get("orderUniqueId");
    		Assert.assertEquals("orderUniqueId", result_uniqueId);
    		Assert.assertEquals("orderUniqueId2", (String) map2.get("orderUniqueId"));
    		List<QueryPaymentOrderResults> planOrderResults = (List<QueryPaymentOrderResults>) map.get("queryPaymentOrderResults");
    		Assert.assertEquals(1, planOrderResults.size());

    		
    		QueryPaymentOrderResults result1 = planOrderResults.get(0);

    		Assert.assertEquals(1, result1.getPayStatus());
    		Assert.assertEquals("2017-07-07 17:10:14", result1.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result1.getCompletedTime());
    		Assert.assertEquals("G08200", result1.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_1", result1.getOrderUuid());
    		Assert.assertEquals("payment_no_1", result1.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_1", result1.getPaymentUuid());
    		Assert.assertEquals("bbb", result1.getRemark());
    		
    		List<QueryPaymentOrderResults> planOrderResults2 = (List<QueryPaymentOrderResults>) map2.get("queryPaymentOrderResults");
    		Assert.assertEquals(1, planOrderResults2.size());
    		
    		QueryPaymentOrderResults result2 = planOrderResults2.get(0);
    		
    		Assert.assertEquals(1, result2.getPayStatus());
    		Assert.assertEquals("2017-07-08 17:10:14", result2.getAccountedTime());
    		Assert.assertEquals("2017-08-09 17:10:14", result2.getCompletedTime());
    		Assert.assertEquals("G08200", result2.getFinancialContractNo());
    		Assert.assertEquals("order_uuid_2", result2.getOrderUuid());
    		Assert.assertEquals("payment_no_2", result2.getPaymentNo());
    		Assert.assertEquals("paymentOrderUuid_2", result2.getPaymentUuid());
    		Assert.assertEquals("aaa", result2.getRemark());
			
		} catch (Exception e) {
			Assert.fail();
		}
		
    }
    
    @Test
    @Sql("classpath:/test/yunxin/repaymentOrder/query_payment_orders.sql")
    public void queryPaymentOrderForOrderUuid3(){
        
    	QueryPaymentOrderRequestModel model = new QueryPaymentOrderRequestModel();
    	List<String> orderUuids = new ArrayList<String>();
    	orderUuids.add("order_uuid_1");
    	
    	List<String> orderUiqueIds = new ArrayList<String>();
    	orderUiqueIds.add("null");
    	
    	model.setOrderUuidList(JsonUtils.toJsonString(orderUuids));
    	model.setOrderUniqueIdList("null");
    	model.setPaymentNoList("null");
    	model.setPaymentUuidList("null");
    	model.setFinancialContractNo("G08200");
    	model.setRequestNo("fdgfdcgd");
    	
    	
    	String result = api_V3_Controller.queryPaymentOrder(request, response, model);
    	Result result2 = JsonUtils.parse(result, Result.class);
    	Assert.assertEquals(0+"",result2.getCode());
    }
    
    @Test
	public void test_repaymentOrderGroupTypeToPayWay(){
		
		boolean flag = repaymentOrderHandler.repayementOrderGroupWayToPayWayMappingTable(RepaymentWayGroupType.ALTER_OFFLINE_REPAYMENT_ORDER_TYPE, PayWay.OFFLINE_TRANSFER);
				
		Assert.assertEquals(flag, true);
		
	}

}
