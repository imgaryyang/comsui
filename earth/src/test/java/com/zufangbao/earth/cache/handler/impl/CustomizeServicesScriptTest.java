package com.zufangbao.earth.cache.handler.impl;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanxiaofan on 2017/4/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class CustomizeServicesScriptTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;
    private Log logger = LogFactory.getLog(CustomizeServicesScriptTest.class);

	@Autowired
	private SandboxDataSetHandler sandboxDataSetHandler;
	@Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_SXHDeferredPaymentServices.sql")
    public void test_script_SXHDeferredPaymentServices() {
    	stringRedisTemplate.delete("sr:modify-repaymentPlan/zhongan/SXH-YQHK");
		Map<String, String> parameters = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0.00"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-05-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-06-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		parameters.put("requestData", requestData);
		parameters.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");

        ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/SXH-YQHK",
                false);

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
        boolean evaluate = services.evaluate(sandboxDataSetHandler, parameters, new HashMap<>(), logger);
        Assert.assertTrue(evaluate);
    }

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_SXHChangeRepaymentDateServices.sql")
	public void test_script_SXHChangeRepaymentDateServices() {
    	stringRedisTemplate.delete("sr:modify-repaymentPlan/zhongan/SXH-BGHKJH");
    	Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-21");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-21");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");

		ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/SXH-BGHKJH",
                false);

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
        boolean evaluate = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(evaluate);
	}

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_SXHPrepaymentServices.sql")
	public void test_script_SXHPrepaymentServices() {
    	stringRedisTemplate.delete("sr:modify-repaymentPlan/zhongan/SXH-YHTQHK");
    	Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");

		ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/SXH-YHTQHK",
                false);

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
        if(services==null){
        	Assert.fail();
        }
        boolean evaluate = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(evaluate);
	}
  
    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_SXHCancelServices.sql")
	public void test_script_SXHCancelServices() {
    	stringRedisTemplate.delete("sr:modify-repaymentPlan/zhongan/SXH-QXSXH");
    	Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");


		ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/SXH-QXSXH",
                false);

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
        boolean evaluate = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(evaluate);
	}

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_PrepaymentServices.sql")
	public void test_script_PrepaymentServices() {
    	stringRedisTemplate.delete("sr:modify-repaymentPlan/zhongan/TQJQ");
    	Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");

		ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/TQJQ",
                false);

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
        boolean evaluate = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(evaluate);
	
    }

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_ChargeChangeServices.sql")
	public void test_script_ChargeChangeServices() {
    	stringRedisTemplate.delete("sr:modify-repaymentPlan/zhongan/FYBG");
    	Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("1000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
		model2.setAssetInterest(new BigDecimal("100"));
		model2.setAssetPrincipal(new BigDecimal("1000"));
		model2.setMaintenanceCharge(new BigDecimal("0"));
		model2.setServiceCharge(new BigDecimal("0"));
		model2.setOtherCharge(new BigDecimal("0"));
		model2.setAssetType(0);
		model2.setAssetRecycleDate("2099-05-20");

		requestDataList.add(model2);
		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);

		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");
		ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/FYBG",
                false);

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
        boolean evaluate = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(evaluate);
	}

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_script_RefundsServices.sql")
	public void test_script_RefundsServices() {
    	stringRedisTemplate.delete("sr:modify-repaymentPlan/zhongan/TUIHUO");
    	Map<String, String> preRequest = new HashMap<>();
		List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
		RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
		model.setAssetInterest(new BigDecimal("100"));
		model.setAssetPrincipal(new BigDecimal("2000"));
		model.setMaintenanceCharge(new BigDecimal("0"));
		model.setServiceCharge(new BigDecimal("0"));
		model.setOtherCharge(new BigDecimal("0"));
		model.setAssetType(0);
		model.setAssetRecycleDate("2099-04-20");
		requestDataList.add(model);

		String requestData = JsonUtils.toJSONString(requestDataList);
		preRequest.put("requestData", requestData);
		preRequest.put("uniqueId", "ad6b3053-8625-4eb6-a78a-dcabc6132b5d");

		ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/TUIHUO",
                false);

        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
        boolean evaluate = 
        		services.evaluate(
        				sandboxDataSetHandler, preRequest, new HashMap<>(), logger);
        Assert.assertTrue(evaluate);
	
    }

}