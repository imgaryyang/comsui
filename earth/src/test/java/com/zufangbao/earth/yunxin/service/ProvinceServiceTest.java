package com.zufangbao.earth.yunxin.service;

import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.utils.EHcacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
})
@TransactionConfiguration()
public class ProvinceServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    ProvinceService provinceService;

    @Before
    public void before() {
        provinceService.evictCachedProvinces();
    }

    @After
    public void after() {
        provinceService.evictCachedProvinces();
    }

    /**
     * code为null或为空
     */
    @Test
    @Sql("classpath:test/yunxin/geography/province.sql")
    public void testGetProvinceByCodeNullOrEmpty() {
        String code1 = "";
        Province province1 = provinceService.getProvinceByCode(code1);
        Assert.assertNull(province1);

        Province province2 = provinceService.getProvinceByCode(null);
        Assert.assertNull(province2);
    }

    @Test
    @Sql("classpath:test/yunxin/geography/province.sql")
    public void testGetProvinceByCodeFromCache() {
        List<Province> all = provinceService.loadAll(Province.class);
        Assert.assertTrue(CollectionUtils.isNotEmpty(all));
        Assert.assertEquals(31L, all.size());

        Map<String, Province> provinceMap = provinceService.getCacheProvinces();
        Assert.assertTrue(MapUtils.isNotEmpty(provinceMap));

        for (Province province : all) {
            Assert.assertNotNull(province);
            provinceService.delete(province);
        }

        List<Province> allAfterDel = provinceService.loadAll(Province.class);
        Assert.assertTrue(CollectionUtils.isEmpty(allAfterDel));

        for (Map.Entry<String, Province> entry : provinceMap.entrySet()) {
            String provinceCode = entry.getKey();
            Province province = entry.getValue();
            Province provinceInCache = provinceService.getProvinceByCode(provinceCode);
            Assert.assertEquals(province, provinceInCache);
        }
    }

    @Test
    @Sql("classpath:test/yunxin/geography/province.sql")
    public void testGetProvinceByCodeFromDB() {
        EHcacheUtil.getInstance().put(CACHE_NAME, CACHE_NAME, Collections.emptyList());

        List<Province> all = provinceService.loadAll(Province.class);
        Assert.assertTrue(CollectionUtils.isNotEmpty(all));
        Assert.assertEquals(31L, all.size());

        for (Province province : all) {
            Assert.assertNotNull(province);
            String provinceCode = province.getCode();

            Province provinceInDB = provinceService.getProvinceByCode(provinceCode);
            Assert.assertNotNull(provinceInDB);
            Assert.assertEquals(province, provinceInDB);
            Map<String, Province> map = provinceService.getCacheProvinces();
            Assert.assertEquals(31, map.size());
        }
    }

    private static final String CACHE_NAME = "provinces";
    private static final String CACHE_KEY = "provinceMap";

    /**
     * 数据库中没有code对应的省份
     */
    @Test
    @Sql("classpath:test/yunxin/geography/province.sql")
    public void testGetProvinceByCodeNoResult() {
        String code = "003";

        // 往缓存塞数据
        Map<String, Province> map = new HashMap<>(2);
        Province province = new Province(code, "浙江省");
        province.setId(100L);
        map.put(code, province);
        EHcacheUtil.getInstance().put(CACHE_NAME, CACHE_KEY, map);

        // 从缓存读取数据
        Province provinceInCache = provinceService.getProvinceByCode(code);
        Assert.assertNotNull(provinceInCache);
        Assert.assertEquals(province, provinceInCache);

        // 清空缓存 直接从DB 获取
        provinceService.evictCachedProvinces();
        Province provinceInDB = provinceService.getProvinceByCode(code);
        Assert.assertNull(provinceInDB);

        provinceService.delete(province);
    }
}
