package com.zufangbao.earth.yunxin.api.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zufangbao.earth.yunxin.api.model.ApiResult;
import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.repurchase.RepurchaseDetails;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.RepurchaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/local/applicationContext-*.xml", "classpath:/local/DispatcherServlet.xml"})
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class Api_V2_ControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private Api_V2_Controller api_v2_controller;

    @Autowired
    private RepurchaseService repurchaseService;

    @Autowired
    private ContractService contractService;


    // 回购申请 接受 网络 url拼写 参数缺失。。。
    // 数据校验 必填选填，数据格式校验
    // 系统校验 重复性校验 可行性校验
    // 处理：可行性校验
    // 返回结果：签名

    //
    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchase.sql")
    public void test_repurchase_application_suc_single() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setBatchNo(UUID.randomUUID().toString());
        model.setTransactionType(0);
        model.setFinancialContractNo("ceshi003");

        List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();

        RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
        repurchaseDetails01.setUniqueId("cuid222");
        repurchaseDetails01.setPrincipal(new BigDecimal("800.00"));
        repurchaseDetails01.setInterest(new BigDecimal("800.00"));
        repurchaseDetails01.setAmount(new BigDecimal("1600.00"));

        repurchaseDetailsList.add(repurchaseDetails01);

        model.setRepurchaseDetail(JSON.toJSONString(repurchaseDetailsList, SerializerFeature.DisableCircularReferenceDetect));
        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        ApiResult apiResult = JSON.parseObject(result_json, ApiResult.class);
        assertEquals(apiResult.getCode(), 0);
        RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(234567L);
        assertEquals(new Long(234567), doc.getContractId());
        assertEquals(0, new BigDecimal("1600").compareTo(doc.getAmount()));
    }

    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchase.sql")
    public void test_repurchase_application_suc_batch() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setBatchNo(UUID.randomUUID().toString());
        model.setTransactionType(0);
        model.setFinancialContractNo("ceshi003");

        List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();

        RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
        repurchaseDetails01.setUniqueId("cuid111");
        repurchaseDetails01.setPrincipal(new BigDecimal("1600.00"));
        repurchaseDetails01.setInterest(new BigDecimal("1600.00"));
        repurchaseDetails01.setAmount(new BigDecimal("3200.00"));
        repurchaseDetailsList.add(repurchaseDetails01);

        RepurchaseDetails repurchaseDetails02 = new RepurchaseDetails();
        repurchaseDetails02.setUniqueId("cuid222");
        repurchaseDetails02.setPrincipal(new BigDecimal("800.00"));
        repurchaseDetails02.setInterest(new BigDecimal("800.00"));
        repurchaseDetails02.setAmount(new BigDecimal("1600.00"));
        repurchaseDetailsList.add(repurchaseDetails02);

        model.setRepurchaseDetail(JSON.toJSONString(repurchaseDetailsList, SerializerFeature.DisableCircularReferenceDetect));
        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        ApiResult apiResult = JSON.parseObject(result_json, ApiResult.class);

        RepurchaseDoc doc01 = repurchaseService.getRepurchaseDocBy(234567L);
        assertEquals(0, doc01.getAmount().compareTo(new BigDecimal("1600.00")));

        RepurchaseDoc doc02 = repurchaseService.getRepurchaseDocBy(123456L);
        assertEquals(0, doc02.getAmount().compareTo(new BigDecimal("3200.00")));

    }

    // 数据格式
    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchase.sql")
    public void test_repurchase_application_data_format_check() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setBatchNo(UUID.randomUUID().toString());
        model.setTransactionType(0);
        model.setFinancialContractNo("ceshi003");
        List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();

        RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
        repurchaseDetails01.setUniqueId("cuid222");
        repurchaseDetails01.setContractNo("G00003(zht765714537113774061)");
        repurchaseDetails01.setPrincipal(new BigDecimal("11"));
        repurchaseDetails01.setInterest(new BigDecimal("12"));
        repurchaseDetails01.setAmount(new BigDecimal("23"));
        repurchaseDetailsList.add(repurchaseDetails01);
        model.setRepurchaseDetail(JSON.toJSONString(repurchaseDetailsList, SerializerFeature.DisableCircularReferenceDetect));
        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        ApiResult apiResult = JSON.parseObject(result_json, ApiResult.class);
        assertEquals(apiResult.getCode(), ApiResponseCode.INVALID_PARAMS);
    }


    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchase.sql")
    public void test_repurchase_application_one_error() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setBatchNo(UUID.randomUUID().toString());
        model.setTransactionType(0);
        model.setFinancialContractNo("ceshi003");

        List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();

        RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
        repurchaseDetails01.setUniqueId("cuid111");
        repurchaseDetails01.setPrincipal(new BigDecimal("1600.00"));
        repurchaseDetails01.setInterest(new BigDecimal("1600.00"));
        repurchaseDetails01.setAmount(new BigDecimal("3200.00"));
        repurchaseDetailsList.add(repurchaseDetails01);

        RepurchaseDetails repurchaseDetails02 = new RepurchaseDetails();
        repurchaseDetails02.setUniqueId("cuid222");
        repurchaseDetails02.setPrincipal(new BigDecimal("500.00"));
        repurchaseDetails02.setInterest(new BigDecimal("500.00"));
        repurchaseDetails02.setAmount(new BigDecimal("1000.00"));
        repurchaseDetailsList.add(repurchaseDetails02);

        model.setRepurchaseDetail(JSON.toJSONString(repurchaseDetailsList, SerializerFeature.DisableCircularReferenceDetect));
        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        ApiResult apiResult = JSON.parseObject(result_json, ApiResult.class);
        assertEquals(apiResult.getCode(), ApiResponseCode.ERROR_REPURCHASE_PRINCIPAL);
//        RepurchaseDoc doc01 = repurchaseService.getRepurchaseDocBy(123456L);
//        assertNull(doc01);
//        RepurchaseDoc doc02 = repurchaseService.getRepurchaseDocBy(234567L);
//        assertNull(doc02);
    }


    // 重复性
    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchase.sql")
    public void test_repurchase_application_repeatability_check() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo("327cf084-232f-11e7-a992-dfe1c742f3fb");
        model.setBatchNo(UUID.randomUUID().toString());
        model.setTransactionType(0);
        model.setFinancialContractNo("ceshi003");

        List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();

        RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
        repurchaseDetails01.setUniqueId("cuid222");
        repurchaseDetails01.setPrincipal(new BigDecimal("800.00"));
        repurchaseDetails01.setInterest(new BigDecimal("800.00"));
        repurchaseDetails01.setAmount(new BigDecimal("1600.00"));

        repurchaseDetailsList.add(repurchaseDetails01);

        model.setRepurchaseDetail(JSON.toJSONString(repurchaseDetailsList, SerializerFeature.DisableCircularReferenceDetect));
        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        ApiResult apiResult = JSON.parseObject(result_json, ApiResult.class);
        assertEquals(apiResult.getCode(), ApiResponseCode.REPEAT_REQUEST_NO);
    }

    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchase.sql")
    public void test_undoRepurchase_batch() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setBatchNo("60aa5f6e-232f-11e7-a992-dfe1c742f3fb");
        model.setTransactionType(1);
        model.setFinancialContractNo("ceshi003");
        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        Contract contract = contractService.getContractByUniqueId("cuid333");
//        assertEquals(contract.getState(),ContractState.EFFECTIVE);

    }

    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchase.sql")
    public void test_undoRepurchase_single() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
//        model.setBatchNo("60aa5f6e-232f-11e7-a992-dfe1c742f3fb");
        model.setTransactionType(1);
        model.setFinancialContractNo("ceshi003");
        List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();
        RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
        repurchaseDetails01.setUniqueId("cuid333");
        repurchaseDetailsList.add(repurchaseDetails01);
        model.setRepurchaseDetail(JSON.toJSONString(repurchaseDetailsList, SerializerFeature.DisableCircularReferenceDetect));

        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        ApiResult apiResult = JSON.parseObject(result_json, ApiResult.class);
        assertEquals(apiResult.getCode(), 0);
        Contract contract = contractService.getContractByUniqueId("cuid333");
        assertNotNull(contract);
//        assertEquals(contract.getState(), ContractState.EFFECTIVE);
    }
    
	public static void main(String[] args){
		 List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();
	     RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
	     repurchaseDetails01.setContractNo("984c6227-995e-48b8-a5d4-1234e5bf45c4");
	     repurchaseDetails01.setPrincipal(new BigDecimal("1500"));
	     repurchaseDetails01.setInterest(new BigDecimal("500"));
	     repurchaseDetails01.setAmount(new BigDecimal("3000"));
	     repurchaseDetailsList.add(repurchaseDetails01);
	     System.out.println(JSON.toJSONString(repurchaseDetailsList));
	}
	

    @Test
    @Sql("classpath:test/yunxin/api/test4submitRepurchaseExistPaymentingAssetset.sql")
    public void test_repurchase_application_one_exist_paymenting_assetset_error() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RepurchaseCommandModel model = new RepurchaseCommandModel();
        model.setRequestNo(UUID.randomUUID().toString());
        model.setBatchNo(UUID.randomUUID().toString());
        model.setTransactionType(0);
        model.setFinancialContractNo("ceshi003");

        List<RepurchaseDetails> repurchaseDetailsList = new ArrayList<RepurchaseDetails>();

        RepurchaseDetails repurchaseDetails01 = new RepurchaseDetails();
        repurchaseDetails01.setUniqueId("cuid111");
        repurchaseDetails01.setPrincipal(new BigDecimal("1600.00"));
        repurchaseDetails01.setInterest(new BigDecimal("1600.00"));
        repurchaseDetails01.setAmount(new BigDecimal("3200.00"));
        repurchaseDetailsList.add(repurchaseDetails01);

        RepurchaseDetails repurchaseDetails02 = new RepurchaseDetails();
        repurchaseDetails02.setUniqueId("cuid222");
        repurchaseDetails02.setPrincipal(new BigDecimal("500.00"));
        repurchaseDetails02.setInterest(new BigDecimal("500.00"));
        repurchaseDetails02.setAmount(new BigDecimal("1000.00"));
        repurchaseDetailsList.add(repurchaseDetails02);

        model.setRepurchaseDetail(JSON.toJSONString(repurchaseDetailsList, SerializerFeature.DisableCircularReferenceDetect));
        String result_json = api_v2_controller.submitRepurchase(request, response, model);
        ApiResult apiResult = JSON.parseObject(result_json, ApiResult.class);
        assertEquals(apiResult.getCode(), ApiResponseCode.REPAYMENT_PLAN_IN_PAYING);
    }
}
