/**
 *
 */
package com.zufangbao.earth.web.controller;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.controller.Api_V2_Controller;
import com.zufangbao.sun.api.model.repayment.QueryOrderRepaymentItemModel;
import com.zufangbao.sun.api.model.repayment.QueryRepaymentOrderModel;
import com.zufangbao.sun.entity.payment.order.QueryRepaymentOrderRequest;
import com.zufangbao.sun.entity.repayment.order.CheckStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author hjl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml"})
@WebAppConfiguration(value = "webapp")
@Transactional
@Rollback(true)
public class QueryRepaymentOrderControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    protected GenericDaoSupport genericDaoSupport;
    @Autowired
    private Api_V2_Controller api_v2_controller;
    @Autowired
    private RepaymentOrderHandler repaymentOrderHandler;

    @Test//RequestNo为空
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestfailRequestNo() {

        List<String> orderuuidlist = new LinkedList<>();
        for (int i = 1; i < 12; i++) {
            String orderuuid = "order_unique_id_";
            orderuuid += i;
            orderuuidlist.add(orderuuid);
        }
        String orderuuidlistJson = JSON.toJSON(orderuuidlist).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo("");
        queryOrderModel.setOrderUniqueIds(orderuuidlistJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REQUEST_NO_IS_EMPTY + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test//orderUuids orderUniqueIds 都不为空
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestfail() {

        List<String> orderUuidList = new LinkedList<>();
        for (int i = 1; i < 12; i++) {
            String orderUuid = "order_unique_id_";
            orderUuid += i;
            orderUuidList.add(orderUuid);
        }
        List<String> orderUniqueIdsList = new LinkedList<>();
        orderUniqueIdsList.add("order_unique_id_1");

        String orderUuidListJson = JSON.toJSON(orderUuidList).toString();
        String orderUniqueIdsJson = JSON.toJSON(orderUniqueIdsList).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds(orderUuidListJson);
        queryOrderModel.setOrderUuids(orderUniqueIdsJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ORDERUNIQUEIDS_OR_ORDERUUIDS_ISNULL + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test//信托产品代码为空
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestfailfinancialProductCode() {

        List<String> orderuuidlist = new LinkedList<>();
        for (int i = 1; i < 12; i++) {
            String orderuuid = "order_unique_id_";
            orderuuid += i;
            orderuuidlist.add(orderuuid);
        }
        String orderuuidlistJson = JSON.toJSON(orderuuidlist).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds(orderuuidlistJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_PRODUCT_CODE_IS_EMPTY + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test//不存在信托产品代码
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestfailfinancialProductCode2() {

        List<String> orderuuidlist = new LinkedList<>();
        for (int i = 1; i < 12; i++) {
            String orderuuid = "order_unique_id_";
            orderuuid += i;
            orderuuidlist.add(orderuuid);
        }
        String orderuuidlistJson = JSON.toJSON(orderuuidlist).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G1111");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds(orderuuidlistJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test//缺少商户号merId或商户密钥secret
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestFailMerId() {

        List<String> orderuuidlist = new LinkedList<>();
        orderuuidlist.add("order_uuid");

        String orderuuidlistJson = JSON.toJSON(orderuuidlist).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds(orderuuidlistJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
//		request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.SIGN_MER_CONFIG_ERROR + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test//五维订单号列表和商户订单号列表 都为空
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestFailOrderUuidAndOrderUniqueIdNull() {

        List<String> orderuuidlist = new LinkedList<>();
        orderuuidlist.add("order_uuid");

        String orderuuidlistJson = JSON.toJSON(orderuuidlist).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds("");
        queryOrderModel.setOrderUuids("");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ORDERUNIQUEIDS_OR_ORDERUUIDS_ISNULL + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test//五维订单号列表不符合条件
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestOrderUuidsI() {

        List<String> orderUuidlist = new LinkedList<>();
        for (int i = 1; i < 12; i++) {
            String orderUuid = "order_uuid_";
            orderUuid += i;
            orderUuidlist.add(orderUuid);
        }
        String orderUuidListJson = JSON.toJSON(orderUuidlist).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUuids(orderUuidListJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ORDERUUIDS_NOT_CONTENTMENT + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test//商户订单号列表不符合条件
    @Sql("classpath:test/yunxin/repaymentOrder/QueryRepaymentOrderControllerTest1.sql")
    public void QueryRepaymentOrderCtTestOrderUniqueIds() {

        List<String> orderUniqueIdList = new LinkedList<>();
        for (int i = 1; i < 12; i++) {
            String orderUniqueId = "order_unique_id_";
            orderUniqueId += i;
            orderUniqueIdList.add(orderUniqueId);
        }
        String orderUniqueIdListJson = JSON.toJSON(orderUniqueIdList).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds(orderUniqueIdListJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {
            String resultString = api_v2_controller.CommandqueryRepaymentOrder(request, response, queryOrderModel);
            Result result = JsonUtils.parse(resultString, Result.class);
            assertEquals(com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ORDERUNIQUEIDS_NOT_CONTENTMENT + "", result.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询单条
    @Test
    @Sql("classpath:test/yunxin/repaymentOrder/queryRepaymentOrderControllerTest3.sql")
    public void QueryRepaymentOrderSingle() {

        List<String> orderUniqueIdList = new LinkedList<>();
        orderUniqueIdList.add("order_unique_id_1");

        String orderUniqueIdListJson = JSON.toJSON(orderUniqueIdList).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds(orderUniqueIdListJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {

            List<QueryRepaymentOrderModel> queryRepaymentOrderModelList = repaymentOrderHandler.queryRepaymentOrderModelList(queryOrderModel);
            Assert.assertEquals(1, queryRepaymentOrderModelList.size());

            QueryRepaymentOrderModel model = queryRepaymentOrderModelList.get(0);
            Assert.assertEquals("order_uuid_1", model.getOrderUuid());
            Assert.assertEquals("order_unique_id_1", model.getOrderUniqueId());
            Assert.assertEquals("G31700", model.getFinancialProductCode());
            Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS.ordinal(), model.getOrderStatus());
            Assert.assertEquals("aaa", model.getRemark());

            List<QueryOrderRepaymentItemModel> repaymentItemModels = model.getRepaymentDetailList();
            Assert.assertEquals(2, repaymentItemModels.size());

            QueryOrderRepaymentItemModel model1 = repaymentItemModels.get(0);
            Assert.assertEquals(new BigDecimal("400.00"), model1.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_SUCCESS.ordinal(), model1.getCheckStatus());
            Assert.assertEquals("contract_no_1", model1.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model1.getContractUniqueId());
            Assert.assertEquals(0, model1.getRecoverStatus());
            Assert.assertEquals("remark_1", model1.getRemark());
            Assert.assertEquals("repayment_plan_no_1", model1.getRepaymentBusinessNo());
            Assert.assertEquals(null, model1.getRepayScheduleNo());

            QueryOrderRepaymentItemModel model2 = repaymentItemModels.get(1);
            Assert.assertEquals(new BigDecimal("500.00"), model2.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_FAIL.ordinal(), model2.getCheckStatus());
            Assert.assertEquals("contract_no_1", model2.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model2.getContractUniqueId());
            Assert.assertEquals(0, model2.getRecoverStatus());
            Assert.assertEquals("remark_2", model2.getRemark());
            Assert.assertEquals("repayment_plan_no_2", model2.getRepaymentBusinessNo());
            Assert.assertEquals(null, model2.getRepayScheduleNo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询多条  orderUuids
    @Test
    @Sql("classpath:test/yunxin/repaymentOrder/queryRepaymentOrderControllerTest3.sql")
    public void QueryRepaymentOrderByOrderUuids() {

        List<String> orderUuidList = new LinkedList<>();
        orderUuidList.add("order_uuid_1");
        orderUuidList.add("order_uuid_2");

        String orderUuidIdListJson = JSON.toJSON(orderUuidList).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUuids(orderUuidIdListJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {

            List<QueryRepaymentOrderModel> queryRepaymentOrderModelList = repaymentOrderHandler.queryRepaymentOrderModelList(queryOrderModel);
            Assert.assertEquals(2, queryRepaymentOrderModelList.size());

            QueryRepaymentOrderModel model = queryRepaymentOrderModelList.get(0);
            Assert.assertEquals("order_uuid_1", model.getOrderUuid());
            Assert.assertEquals("order_unique_id_1", model.getOrderUniqueId());
            Assert.assertEquals("G31700", model.getFinancialProductCode());
            Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS.ordinal(), model.getOrderStatus());
            Assert.assertEquals("aaa", model.getRemark());

            List<QueryOrderRepaymentItemModel> repaymentItemModels = model.getRepaymentDetailList();
            Assert.assertEquals(2, repaymentItemModels.size());

            QueryOrderRepaymentItemModel model1 = repaymentItemModels.get(0);
            Assert.assertEquals(new BigDecimal("400"), model1.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_SUCCESS.ordinal(), model1.getCheckStatus());
            Assert.assertEquals("contract_no_1", model1.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model1.getContractUniqueId());
            Assert.assertEquals(0, model1.getRecoverStatus());
            Assert.assertEquals("remark_1", model1.getRemark());
            Assert.assertEquals("repayment_plan_no_1", model1.getRepaymentBusinessNo());
            Assert.assertEquals("", model1.getRepayScheduleNo());

            QueryOrderRepaymentItemModel model2 = repaymentItemModels.get(1);
            Assert.assertEquals(new BigDecimal("500"), model2.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_FAIL.ordinal(), model2.getCheckStatus());
            Assert.assertEquals("contract_no_1", model2.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model2.getContractUniqueId());
            Assert.assertEquals(0, model2.getRecoverStatus());
            Assert.assertEquals("remark_2", model2.getRemark());
            Assert.assertEquals("repayment_plan_no_2", model2.getRepaymentBusinessNo());
            Assert.assertEquals("", model2.getRepayScheduleNo());


            QueryRepaymentOrderModel orderModel = queryRepaymentOrderModelList.get(1);

            Assert.assertEquals("order_uuid_2", orderModel.getOrderUuid());
            Assert.assertEquals("order_unique_id_2", orderModel.getOrderUniqueId());
            Assert.assertEquals("G31700", orderModel.getFinancialProductCode());
            Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS.ordinal(), orderModel.getOrderStatus());
            Assert.assertEquals("bbb", orderModel.getRemark());

            List<QueryOrderRepaymentItemModel> repaymentItemModels2 = orderModel.getRepaymentDetailList();
            Assert.assertEquals(2, repaymentItemModels2.size());

            QueryOrderRepaymentItemModel model3 = repaymentItemModels2.get(0);
            Assert.assertEquals(new BigDecimal("100"), model3.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_SUCCESS.ordinal(), model3.getCheckStatus());
            Assert.assertEquals("contract_no_1", model3.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model3.getContractUniqueId());
            Assert.assertEquals(0, model3.getRecoverStatus());
            Assert.assertEquals("remark_1", model3.getRemark());
            Assert.assertEquals("repayment_plan_no_1", model3.getRepaymentBusinessNo());
            Assert.assertEquals("", model3.getRepayScheduleNo());

            QueryOrderRepaymentItemModel model4 = repaymentItemModels2.get(1);
            Assert.assertEquals(new BigDecimal("600"), model4.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_FAIL.ordinal(), model4.getCheckStatus());
            Assert.assertEquals("contract_no_1", model4.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model4.getContractUniqueId());
            Assert.assertEquals(0, model4.getRecoverStatus());
            Assert.assertEquals("remark_2", model4.getRemark());
            Assert.assertEquals("repayment_plan_no_2", model4.getRepaymentBusinessNo());
            Assert.assertEquals("", model4.getRepayScheduleNo());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询多条  orderUniqueIds
    @Test
    @Sql("classpath:test/yunxin/repaymentOrder/queryRepaymentOrderControllerTest3.sql")
    public void QueryRepaymentOrderByOrderUniqueIds() {

        List<String> orderUniqueIdList = new LinkedList<>();
        orderUniqueIdList.add("order_unique_id_1");
        orderUniqueIdList.add("order_unique_id_2");

        String orderUniqueIdListJson = JSON.toJSON(orderUniqueIdList).toString();
        QueryRepaymentOrderRequest queryOrderModel = new QueryRepaymentOrderRequest();
        queryOrderModel.setFinancialProductCode("G31700");
        queryOrderModel.setRequestNo(UUID.randomUUID().toString());
        queryOrderModel.setOrderUniqueIds(orderUniqueIdListJson);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("merId", "mer_id_1");
        try {

            List<QueryRepaymentOrderModel> queryRepaymentOrderModelList = repaymentOrderHandler.queryRepaymentOrderModelList(queryOrderModel);
            Assert.assertEquals(2, queryRepaymentOrderModelList.size());

            QueryRepaymentOrderModel model = queryRepaymentOrderModelList.get(0);
            Assert.assertEquals("order_uuid_1", model.getOrderUuid());
            Assert.assertEquals("order_unique_id_1", model.getOrderUniqueId());
            Assert.assertEquals("G31700", model.getFinancialProductCode());
            Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS.ordinal(), model.getOrderStatus());
            Assert.assertEquals("aaa", model.getRemark());

            List<QueryOrderRepaymentItemModel> repaymentItemModels = model.getRepaymentDetailList();
            Assert.assertEquals(2, repaymentItemModels.size());

            QueryOrderRepaymentItemModel model1 = repaymentItemModels.get(0);
            Assert.assertEquals(new BigDecimal("400"), model1.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_SUCCESS.ordinal(), model1.getCheckStatus());
            Assert.assertEquals("contract_no_1", model1.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model1.getContractUniqueId());
            Assert.assertEquals(0, model1.getRecoverStatus());
            Assert.assertEquals("remark_1", model1.getRemark());
            Assert.assertEquals("repayment_plan_no_1", model1.getRepaymentBusinessNo());
            Assert.assertEquals("", model1.getRepayScheduleNo());

            QueryOrderRepaymentItemModel model2 = repaymentItemModels.get(1);
            Assert.assertEquals(new BigDecimal("500"), model2.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_FAIL.ordinal(), model2.getCheckStatus());
            Assert.assertEquals("contract_no_1", model2.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model2.getContractUniqueId());
            Assert.assertEquals(0, model2.getRecoverStatus());
            Assert.assertEquals("remark_2", model2.getRemark());
            Assert.assertEquals("repayment_plan_no_2", model2.getRepaymentBusinessNo());
            Assert.assertEquals("", model2.getRepayScheduleNo());


            QueryRepaymentOrderModel orderModel = queryRepaymentOrderModelList.get(1);

            Assert.assertEquals("order_uuid_2", orderModel.getOrderUuid());
            Assert.assertEquals("order_unique_id_2", orderModel.getOrderUniqueId());
            Assert.assertEquals("G31700", orderModel.getFinancialProductCode());
            Assert.assertEquals(RepaymentStatus.VERIFICATION_SUCCESS.ordinal(), orderModel.getOrderStatus());
            Assert.assertEquals("bbb", orderModel.getRemark());

            List<QueryOrderRepaymentItemModel> repaymentItemModels2 = orderModel.getRepaymentDetailList();
            Assert.assertEquals(2, repaymentItemModels2.size());

            QueryOrderRepaymentItemModel model3 = repaymentItemModels2.get(0);
            Assert.assertEquals(new BigDecimal("100"), model3.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_SUCCESS.ordinal(), model3.getCheckStatus());
            Assert.assertEquals("contract_no_1", model3.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model3.getContractUniqueId());
            Assert.assertEquals(0, model3.getRecoverStatus());
            Assert.assertEquals("remark_1", model3.getRemark());
            Assert.assertEquals("repayment_plan_no_1", model3.getRepaymentBusinessNo());
            Assert.assertEquals("", model3.getRepayScheduleNo());

            QueryOrderRepaymentItemModel model4 = repaymentItemModels2.get(1);
            Assert.assertEquals(new BigDecimal("600"), model4.getAmount());
            Assert.assertEquals(CheckStatus.CHECK_STATUS_FAIL.ordinal(), model4.getCheckStatus());
            Assert.assertEquals("contract_no_1", model4.getContractNo());
            Assert.assertEquals("contract_unique_id_1", model4.getContractUniqueId());
            Assert.assertEquals(0, model4.getRecoverStatus());
            Assert.assertEquals("remark_2", model4.getRemark());
            Assert.assertEquals("repayment_plan_no_2", model4.getRepaymentBusinessNo());
            Assert.assertEquals("", model4.getRepayScheduleNo());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//	@Test
//	@Sql("classpath:test/yunxin/repaymentOrder/deleterepaymentorder1000000sql.sql")
//	public void testinsertsql() {
//
//		for(int i=1;i<=1000000;i++){
//			StringBuffer stringBuffer=new StringBuffer();
//			Map<String, Object> parms = new HashMap<String, Object>();
//			parms.put("id", i);
//			parms.put("order_uuid", "order_uuid_"+i);
//			parms.put("order_unique_id", "order_unique_id"+i);
//			parms.put("mer_id", "mer_id_1");
//			parms.put("order_request_no", "e573be60-f16c-4918-9e8c-b220561b9282");
//			parms.put("first_repayment_way_group", 5);
//			parms.put("order_alive_status", 0);
//			parms.put("order_check_status", 1);
//			parms.put("order_db_status", 0);
//			parms.put("order_pay_status", 2);
//			parms.put("order_recover_result", 0);
//			parms.put("order_recover_status", 0);
//			parms.put("paid_amount", 0.00);
//			parms.put("order_amount", 500.00);
//			parms.put("order_create_time", "2017-06-12 17:40:39");
//			parms.put("order_last_modified_time", "2017-06-12 17:40:39");
//			parms.put("order_notify_url", "");
//			parms.put("ip", "127.0.0.1");
//			parms.put("financial_contract_uuid", "financial_contract_uuid_1");
//			parms.put("financial_contract_no", "G31700");
//			parms.put("financial_contract_name", "拍拍贷测试");
//			parms.put("order_detail_file_path", "/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv");
//			parms.put("active_order_uuid", "empty_order_uuid");
//			parms.put("remark", "");
//			stringBuffer.append("(:id, :order_uuid, :order_unique_id, :mer_id, :order_request_no, :first_repayment_way_group, :order_alive_status, :order_check_status, :order_db_status, :order_pay_status, :order_recover_result, :order_recover_status, :paid_amount, :order_amount, :order_create_time, :order_last_modified_time, :order_notify_url, :ip, :financial_contract_uuid, :financial_contract_no, :financial_contract_name, :order_detail_file_path, :active_order_uuid, :remark);");
//			String sqlStr="INSERT INTO repayment_order (id, order_uuid, order_unique_id, mer_id, order_request_no, first_repayment_way_group, order_alive_status, order_check_status, order_db_status, order_pay_status, order_recover_result, order_recover_status, paid_amount, order_amount, order_create_time, order_last_modified_time, order_notify_url, ip, financial_contract_uuid, financial_contract_no, financial_contract_name, order_detail_file_path, active_order_uuid, remark)"
//					+" VALUES ";
//			sqlStr+=stringBuffer.toString();
//			genericDaoSupport.executeSQL(sqlStr, parms);
//		}
//		}
//
//	@Test
//	//@Sql("classpath:test/yunxin/repaymentOrder/deleterepaymentorderitem1000000sql.sql")
//	public void testinsertitemsql() {
//
//		for(int i=1600001;i<=2000000;i++){
//			StringBuffer stringBuffer=new StringBuffer();
//			Map<String, Object> parms = new HashMap<String, Object>();
//			parms.put("id", i);
//			parms.put("order_detail_uuid", "order_detail_uuid_"+i);
//			parms.put("contract_unique_id", "contract_unique_id_1");
//			parms.put("contract_no", "contract_no_1");
//			parms.put("contract_uuid", "contract_uuid_1");
//			parms.put("amount", 250);
//			parms.put("detail_alive_status", 0);
//			parms.put("detail_pay_status", 0);
//			parms.put("mer_id", "mer_id_1");
//			parms.put("financial_contract_uuid", "financial_contract_uuid_1");
//			parms.put("repayment_way", 8);
//			parms.put("repayment_business_uuid", "asset_uuid_1");
//			parms.put("repayment_business_no", "repayment_plan_no_1");
//			parms.put("repayment_business_type", 0);
//			parms.put("repayment_plan_time", "2017-06-15 10:10:10");
//			parms.put("order_uuid", "order_uuid_"+((i+1)/2));
//			parms.put("order_unique_id", "oder_unique_id_"+((i+1)/2));
//			parms.put("remark", "");
//
//			stringBuffer.append("(:id, :order_detail_uuid, :contract_unique_id, :contract_no, :contract_uuid, :amount, :detail_alive_status, :detail_pay_status, :mer_id, :financial_contract_uuid, :repayment_way, :repayment_business_uuid, :repayment_business_no, :repayment_business_type, :repayment_plan_time, :order_uuid, :order_unique_id, :remark);");
//			String sqlStr="INSERT INTO repayment_order_item (id, order_detail_uuid, contract_unique_id, contract_no, contract_uuid, amount, detail_alive_status, detail_pay_status, mer_id, financial_contract_uuid, repayment_way, repayment_business_uuid, repayment_business_no, repayment_business_type, repayment_plan_time, order_uuid, order_unique_id, remark)"
//					+" VALUES ";
//			sqlStr+=stringBuffer.toString();
//			genericDaoSupport.executeSQL(sqlStr, parms);
//			}
//		}

}
