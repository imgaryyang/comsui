package com.suidifu.matryoshaka.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.matryoshka.productCategory.Product3lvl;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.service.ProductCategoryService;
import com.zufangbao.sun.entity.perInterface.ProductLv2Code;
import com.zufangbao.sun.utils.Md5Util;

/**
 * Created by hwr on 17-7-5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ProductCategoryServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    @Sql("classpath:test/test_open.sql")
    public void test_open() {
        ProductCategory productCategory = productCategoryService.getByPreUrl("api/query/test1");
        Assert.assertNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/test3");
        Assert.assertNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/test5");
        Assert.assertNull(productCategory);

        productCategoryService.open("fe410a76-b8cd-4858-b019-3d9b1c1f3238");
        productCategory = productCategoryService.getByPreUrl("api/query/test1");
        Assert.assertNotNull(productCategory);
        productCategoryService.open(Arrays.asList(new String[]{"2b5e7e02-11b4-4d4f-b970-c3c8fd6476d9", "1ede9903-35ab-4f3b-bd76-a5ea6b35d630"}));
        productCategory = productCategoryService.getByPreUrl("api/query/test3");
        Assert.assertNotNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/test5");
        Assert.assertNotNull(productCategory);

    }

    @Test
    @Sql("classpath:test/test_close.sql")
    public void test_close() {
        ProductCategory productCategory = productCategoryService.getByPreUrl("api/query/test0");
        Assert.assertNotNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/test2");
        Assert.assertNotNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/test4");
        Assert.assertNotNull(productCategory);

        productCategoryService.close("36b7d036-4ec4-48c5-9c03-0cbfe35f99b5");
        productCategory = productCategoryService.getByPreUrl("api/query/test0");
        Assert.assertNull(productCategory);
        productCategoryService.close(Arrays.asList(new String[]{"3f056394-b7bc-41ea-bc20-f16e99ebdc7d", "9399c469-8ccc-430f-ba54-5b90b71ee324"}));
        productCategory = productCategoryService.getByPreUrl("api/query/test2");
        Assert.assertNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/test4");
        Assert.assertNull(productCategory);

    }

    @Test
    @Sql("classpath:test/test_get_by_pre_url.sql")
    public void test_get_by_pre_url() {
        ProductCategory productCategory = productCategoryService.getByPreUrl("api/query/test0");
        Assert.assertNotNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/");
        assertNull(productCategory);
        productCategory = productCategoryService.getByPreUrl("api/query/test1");
        assertNull(productCategory);
    }

    public void test_get_by_all_lv_code() {
        Product3lvl product3lvl = new Product3lvl("modify-repaymentPlan", "query", "test0");
        ProductCategory productCategory = productCategoryService.getByAllLvCode(product3lvl);
        Assert.assertNotNull(productCategory);

        product3lvl = new Product3lvl("api", "query", "test1");
        productCategory = productCategoryService.getByAllLvCode(product3lvl);
        assertNull(productCategory);

        product3lvl = new Product3lvl("api", "query", "");
        productCategory = productCategoryService.getByAllLvCode(product3lvl);
        assertNull(productCategory);

        product3lvl = new Product3lvl("", "", "");
        productCategory = productCategoryService.getByAllLvCode(product3lvl);

    }

    @Test
    @Sql("classpath:test/yunxin/test_get_all_by_lv_code.sql")
    public void test_get_all_by_lv_code() {
        Product3lvl product3lvl = new Product3lvl("", ProductLv2Code.UPLOAD.getCode(), "");
        List<ProductCategory> categoryList = productCategoryService.get_all_by_lv_code(product3lvl);
        Assert.assertEquals(1L, categoryList.size());

        Product3lvl product3lvl2 = new Product3lvl("", ProductLv2Code.MODIFY_REPAYMENT_PLAN.getCode(), "");
        List<ProductCategory> categoryList2 = productCategoryService.get_all_by_lv_code(product3lvl2);
        Assert.assertEquals(6L, categoryList2.size());
    }

    /**测试通过用这个
     *
     */
    @Test
    @Sql("classpath:test/test_getAll.sql")
    public void test_getAll() {
        List<ProductCategory> list = productCategoryService.getAll();
        assertTrue(CollectionUtils.isNotEmpty(list));
        assertEquals(6, list.size());
    }

    /**
     * 测试不用过用这个
     */
    @Test
    @Sql("classpath:test/test_getAll2.sql")
    public void test_getAll_2() {
        List<ProductCategory> list = productCategoryService.getAll();
        assertTrue(CollectionUtils.isEmpty(list));
    }

    @Test
    @Sql("classpath:test/test_get_by_uuid.sql")
    public void test_get_by_uuid() {
        ProductCategory productCategory = productCategoryService.get_by_uuid("36b7d036-4ec4-48c5-9c03-0cbfe35f99b5");
        Assert.assertNotNull(productCategory);
        productCategory = productCategoryService.get_by_uuid("76433213456789");
        assertNull(productCategory);
    }

    @Test
    @Sql("classpath:test/test_get_by_script_version.sql")
    public void test_get_by_script_version() {
        ProductCategory productCategory = productCategoryService.get_by_script_version("bda500743d6a6be9fec1969984ed8d12");
        Assert.assertNotNull(productCategory);
        productCategory = productCategoryService.get_by_script_version("17892d90955c133e9e232989da09d57f");
        assertNull(productCategory);
        productCategory = productCategoryService.get_by_script_version("76433213456789");
        assertNull(productCategory);
        productCategory = productCategoryService.get_by_script_version(null);
        assertNull(productCategory);
    }

    @Test
    @Sql("classpath:test/test_checkScriptVersion.sql")
    public void test_checkScriptVersion() {
        String script = productCategoryService.get_by_uuid("36b7d036-4ec4-48c5-9c03-0cbfe35f99b5")
                .getPreProcessScript();
        String currentVersion = Md5Util.encode(script);
        boolean result = productCategoryService.checkScriptVersion("36b7d036-4ec4-48c5-9c03-0cbfe35f99b5",
                currentVersion);
        Assert.assertTrue(result);

        script = productCategoryService.get_by_uuid("3f056394-b7bc-41ea-bc20-f16e99ebdc7d").getPreProcessScript();
        currentVersion = Md5Util.encode(script);
        result = productCategoryService.checkScriptVersion("3f056394-b7bc-41ea-bc20-f16e99ebdc7d", currentVersion);
        Assert.assertFalse(result);

        script = productCategoryService.get_by_uuid("3f056394-b7bc-41ea-bc20-f16e99ebdc7d").getPreProcessScript();
        currentVersion = Md5Util.encode(script);
        result = productCategoryService.checkScriptVersion("643545443876543212345", currentVersion);
        Assert.assertFalse(result);

        result = productCategoryService.checkScriptVersion(null, currentVersion);
        Assert.assertFalse(result);
        result = productCategoryService.checkScriptVersion("643545443876543212345", null);
        Assert.assertFalse(result);
    }

}
