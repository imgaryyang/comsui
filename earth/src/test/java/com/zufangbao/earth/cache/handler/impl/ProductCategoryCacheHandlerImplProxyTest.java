package com.zufangbao.earth.cache.handler.impl;

import com.suidifu.matryoshka.cache.ProductCategoryCache;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.customize.CustomizeServicesBuilder;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.productCategory.ProductCategoryCacheSpec;
import com.zufangbao.gluon.exception.apis.ApiNotExistException;
import org.codehaus.commons.compiler.CompileException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author louguanyang on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
@Transactional
public class ProductCategoryCacheHandlerImplProxyTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    private CacheManager cacheManager;


    @Before
    public void init_cache() {
        // 清空Cache
        productCategoryCacheHandler.clearAll();
    }

    @Test
    public void test_clearAll_Cache() {
        String url = "asset-modify/zhongan/yanqihuankuan";
        ProductCategory category = new ProductCategory();
        category.setPreProcessInterfaceUrl(url);
        Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
        cache.put(url, category);

        ProductCategory category_in_cache = cache.get(url, ProductCategory.class);
        assertNotNull(category_in_cache);

        String scriptMD5Version = "17892d90955c133e9e232989da09d57f";
        // FIXME 转对象
        cache.put(scriptMD5Version, category);

        category_in_cache = cache.get(scriptMD5Version, ProductCategory.class);
        assertNotNull(category_in_cache);

        productCategoryCacheHandler.clearAll();

        ProductCategory category_in_cache_after_clear = cache.get(url, ProductCategory.class);
        assertNull(category_in_cache_after_clear);

        // FIXME 转对象
        category_in_cache_after_clear = cache.get(scriptMD5Version, ProductCategory.class);
        assertNull(category_in_cache_after_clear);
        
        
        
    }

    @Test
    public void test_clearByUrl() {
        String url = "asset-modify/zhongan/yanqihuankuan";
        ProductCategory category = new ProductCategory();
        category.setPreProcessInterfaceUrl(url);
        Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
        cache.put(url, category);

        ProductCategory category_in_cache = cache.get(url, ProductCategory.class);
        assertNotNull(category_in_cache);

        productCategoryCacheHandler.clearByUrl(url);

        ProductCategory category_in_cache_after_clear = cache.get(url, ProductCategory.class);
        assertNull(category_in_cache_after_clear);
    }
    
    @Test
    public void test_clearByScriptMD5Version() {
        String scriptMD5Version = "17892d90955c133e9e232989da09d57f";
        ProductCategory category = new ProductCategory();
        category.setScriptMd5Version(scriptMD5Version);
        Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
        // FIXME 转对象
        cache.put(scriptMD5Version, category);

        ProductCategory category_in_cache = cache.get(scriptMD5Version, ProductCategory.class);
        assertNotNull(category_in_cache);

        productCategoryCacheHandler.clearByScriptMD5Version(scriptMD5Version);

        // FIXME 转对象
        ProductCategory category_in_cache_after_clear = cache.get(scriptMD5Version, ProductCategory.class);
        assertNull(category_in_cache_after_clear);
    }

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_get_ProductCategory_null_param_and_no_result.sql")
    public void test_get_ProductCategory_null_param_and_no_result() {
    	try {
        	@SuppressWarnings("unused")
			ProductCategory return_category1 = productCategoryCacheHandler.get(null, true);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ApiNotExistException);
        }
    	try {
        	@SuppressWarnings("unused")
			ProductCategory return_category2 = productCategoryCacheHandler.get("", true);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ApiNotExistException);
        }
    	try {
        	@SuppressWarnings("unused")
			ProductCategory return_category3 = productCategoryCacheHandler.get("36b7d036-4ec4-48c5-9c03-0cbfe35f99b5", true);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ApiNotExistException);
        }

    }
    
    @Test
    @Sql("classpath:test/yunxin/productCategory/test_get_ProductCategory_NOT_Recursively.sql")
    public void test_get_ProductCategory_NOT_Recursively() {

        String url = "asset-modify/zhongan/yanqihuankuan";
        String url_just_productlv = "asset-modify/zhongan/tuihuo";
        
        productCategoryCacheHandler.clearAll();
        ProductCategory return_category = productCategoryCacheHandler.get(url, false);
        assertNotNull(return_category);
        
        try {
        	@SuppressWarnings("unused")
			ProductCategory return_category2 = productCategoryCacheHandler.get(url_just_productlv, false);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ApiNotExistException);
        }

        Cache cache = cacheManager.getCache(ProductCategoryCacheSpec.CACHE_KEY);
        assertNotNull(cache);
        ProductCategoryCache cachedData = cache.get(url, ProductCategoryCache.class);
        assertNotNull(cachedData);

        assertEquals(url, cachedData.getProductCategory().getPreProcessInterfaceUrl());
        assertEquals(url, return_category.getPreProcessInterfaceUrl());
    }

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_get_ProductCategory_Recursively.sql")
	public void test_get_ProductCategory_Recursively() {
    	String simpleUrl = "/modify-repaymentPlan";// 最简接口
		String standardUrl = "/modify-repaymentPlan/zhongan";// 标准接口
		String product_url = "/modify-repaymentPlan/zhongan/yanqihuankuan";// 定制接口
		String extension_url = "/modify-repaymentPlan/zhongan/yanqihuankuan/youhui";// 扩展接口

		ProductCategory return_category = productCategoryCacheHandler.get(product_url, true);
		assertNotNull(return_category);
		assertEquals(return_category.getUuid(), "36b7d036-4ec4-48c5-9c03-0cbfe35f99b5");

		ProductCategory return_category2 = productCategoryCacheHandler.get(standardUrl, true);
		assertNotNull(return_category2);
		assertEquals(return_category2.getUuid(), "fe410a76-b8cd-4858-b019-3d9b1c1f3238");

		ProductCategory return_category3 = productCategoryCacheHandler.get(simpleUrl, true);
		assertNotNull(return_category3);
		assertEquals(return_category3.getUuid(), "3f056394-b7bc-41ea-bc20-f16e99ebdc7d");
		
		ProductCategory return_category4 = productCategoryCacheHandler.get(extension_url, true);
		assertNotNull(return_category4);
		assertEquals(return_category4.getUuid(), "2b5e7e02-11b4-4d4f-b970-c3c8fd6476d9");

		try {
			@SuppressWarnings("unused")
			ProductCategory return_category5 = productCategoryCacheHandler.get(standardUrl + 1, true);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ApiNotExistException);
		}
		try {
			productCategoryCacheHandler.clearByUrl(standardUrl);
			@SuppressWarnings("unused")
			ProductCategory return_category6 = productCategoryCacheHandler.get(standardUrl, false);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ApiNotExistException);
		}

	}
    
    @Test
    @Sql("classpath:test/yunxin/productCategory/test_get_ProductCategory_by_cache.sql")
    public void test_get_ProductCategory_by_cache() {
		String product_url = "/modify-repaymentPlan/zhongan/yanqihuankuan";

		try {
			@SuppressWarnings("unused")
			ProductCategory return_category1 = productCategoryCacheHandler.get(product_url, false);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof ApiNotExistException);
		}
		
		ProductCategory return_category2 = productCategoryCacheHandler.get(product_url, true);
		assertNotNull(return_category2);
		assertEquals(return_category2.getUuid(), "36b7d036-4ec4-48c5-9c03-0cbfe35f99b5");

		ProductCategory return_category3 = productCategoryCacheHandler.get(product_url, false);
		assertNotNull(return_category3);
		assertEquals(return_category3.getUuid(), "36b7d036-4ec4-48c5-9c03-0cbfe35f99b5");


	}

    @Test
    @Sql("classpath:test/yunxin/productCategory/test_get_script.sql")
	public void test_get_script() {
		try {
			String url = "modify-repaymentPlan/zhongan/tiqianjieqing";
			String scriptMd5="0b00f3d395dc80f6382dd021b9ae5f22";
			productCategoryCacheHandler.clearAll();
			ProductCategory return_category = productCategoryCacheHandler.get(url, false);

            CustomizeServices script = new CustomizeServicesBuilder().build(return_category.getPreProcessScript(),"");
            Object scriptObj = productCategoryCacheHandler.getScript(return_category);

            CustomizeServices customizeServices = (CustomizeServices) scriptObj;
            assertNotNull(customizeServices);

            assertEquals(script.getClass().getName(), customizeServices.getClass().getName());
			
			Object scriptObjByCache = productCategoryCacheHandler.getScript(return_category);
			assertEquals(scriptObj,scriptObjByCache);
			
		} catch (CompileException | IllegalAccessException | InstantiationException | IOException e) {
			e.printStackTrace();
            fail();
		}
	}
    
    @Test
    @Sql("classpath:test/yunxin/productCategory/test_get_script_null_param_and_no_result.sql")
	public void test_get_script_null_param_and_no_result() {

		Object scriptObj = productCategoryCacheHandler.getScript(null);
		assertNull(scriptObj);
	}

//
//    @Test
//    @Sql("classpath:test/yunxin/productCategory/test_get_script_2.sql")
//    public void test_get_script_2() {
//        String financialContractUuid = UUID.randomUUID().toString();
//        String uniqueId = UUID.randomUUID().toString();
//        String contractUuid = UUID.randomUUID().toString();
//        String customerUuid = UUID.randomUUID().toString();
//
//        FinancialContractSnapshot financialContractSnapshot = new FinancialContractSnapshot();
//        financialContractSnapshot.setFinancialContractUuid(financialContractUuid);
//        financialContractSnapshot.setAllowFreewheelingRepayment(false);
//        CustomerAccountSnapshot customerAccountSnapshot = new CustomerAccountSnapshot();
//
//        ContractSnapshot contractSnapshot = new ContractSnapshot(uniqueId, contractUuid, financialContractUuid,
//                DateUtils.parseDate("2017-01-01"), DateUtils.parseDate("2027-01-01"), customerUuid);
//
//        contractSnapshot.setCustomerAccountSnapshot(customerAccountSnapshot);
//
//        PaymentPlanSnapshot assetSetSnapshot1 = new PaymentPlanSnapshot("assetUuid", "singleLoanContractNo",
//                new BigDecimal("1100"), new BigDecimal("1000"), new BigDecimal("100"),
//                DateUtils.parseDate("2099-04-20"), 2, financialContractUuid, customerUuid, contractUuid,
//                AssetClearStatus.UNCLEAR, null);
//
//        PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot1 = new PaymentPlanExtraChargeSnapshot();
//        assetSetSnapshot1.setAssetSetExtraChargeSnapshot(assetSetExtraChargeSnapshot1);
//
//        PaymentPlanSnapshot assetSetSnapshot2 = new PaymentPlanSnapshot("assetUuid", "singleLoanContractNo",
//                new BigDecimal("1100"), new BigDecimal("1000"), new BigDecimal("100"),
//                DateUtils.parseDate("2099-05-20"), 3, financialContractUuid, customerUuid, contractUuid,
//                AssetClearStatus.UNCLEAR, null);
//
//        PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot2 = new PaymentPlanExtraChargeSnapshot();
//        assetSetSnapshot2.setAssetSetExtraChargeSnapshot(assetSetExtraChargeSnapshot2);
//
//        List<PaymentPlanSnapshot> assetSetSnapshotList = Arrays.asList(assetSetSnapshot1, assetSetSnapshot2);
//
//        Map<String, String> parameters = new HashMap<>();
//        List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
//        RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
//        model.setAssetInterest(new BigDecimal("100"));
//        model.setAssetPrincipal(new BigDecimal("1000"));
//        model.setMaintenanceCharge(new BigDecimal("0"));
//        model.setServiceCharge(new BigDecimal("0"));
//        model.setOtherCharge(new BigDecimal("0"));
//        model.setAssetType(0);
//        model.setAssetRecycleDate("2099-05-20");
//        requestDataList.add(model);
//
//        RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
//        model2.setAssetInterest(new BigDecimal("100"));
//        model2.setAssetPrincipal(new BigDecimal("1000"));
//        model2.setMaintenanceCharge(new BigDecimal("0"));
//        model2.setServiceCharge(new BigDecimal("0"));
//        model2.setOtherCharge(new BigDecimal("0"));
//        model2.setAssetType(0);
//        model2.setAssetRecycleDate("2099-06-20");
//
//        requestDataList.add(model2);
//        String requestData = JsonUtils.toJSONString(requestDataList);
//        parameters.put("requestData", requestData);
//
//        ProductCategory productCategory = productCategoryCacheHandler.get("modify-repaymentPlan/zhongan/SXH-YQHK",
//                false);
//
//        long start = System.currentTimeMillis();
//        CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
//        boolean evaluate = services.evaluate(financialContractSnapshot, contractSnapshot, assetSetSnapshotList,
//                parameters, null);
//
//        System.out.println("first usage:" + (System.currentTimeMillis() - start));
//
//        Assert.assertFalse(evaluate);
//
//        start = System.currentTimeMillis();
//        CustomizeServices services2 = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
//
//        boolean evaluate2 = services2.evaluate(financialContractSnapshot, contractSnapshot, assetSetSnapshotList,
//                parameters, null);
//        Assert.assertFalse(evaluate2);
//
//        System.out.println("secend usage:" + (System.currentTimeMillis() - start));
//
//    }


}