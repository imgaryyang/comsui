package com.suidifu.morganstanley.servers;

import com.suidifu.morganstanley.TestMorganStanley;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.ProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class ProvinceServiceTest {

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
    }

    @Test
    @Sql("classpath:test/yunxin/geography/province.sql")
    public void testGetProvinceByCodeFromDB() {
        List<Province> all = provinceService.loadAll(Province.class);
        Assert.assertTrue(CollectionUtils.isNotEmpty(all));
        Assert.assertEquals(31L, all.size());

        for (Province province : all) {
            Assert.assertNotNull(province);
            String provinceCode = province.getCode();

            Province provinceInDB = provinceService.getProvinceByCode(provinceCode);
            log.info("provinceService.getProvinceByCode(provinceCode)");
            Assert.assertNotNull(provinceInDB);
            Assert.assertEquals(province, provinceInDB);
            Map<String, Province> map = provinceService.getCacheProvinces();
            log.info("provinceService.getCacheProvinces()");
            Assert.assertEquals(31, map.size());
        }
    }

    /**
     * 数据库中没有code对应的省份
     */
    @Test
    @Sql("classpath:test/yunxin/geography/province.sql")
    public void testGetProvinceByCodeNoResult() {
        String code = "003";
        Province provinceInDB = provinceService.getProvinceByCode(code);
        Assert.assertNull(provinceInDB);
    }
}
