package com.zufangbao.earth.yunxin.service;

import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.service.CityService;
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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
})
@TransactionConfiguration()
public class CityServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    CityService cityService;

    @Before
    public void before() {
        cityService.evictCachedCitys();
    }

    @After
    public void after() {
        cityService.evictCachedCitys();
    }

    /**
     * cityCode为null或空
     */
    @Test
    @Sql("classpath:test/yunxin/geography/city.sql")
    public void testGetCityByCityCodeParamsEmptyOrNull() {
        City city1 = cityService.getCityByCityCode(null);
        Assert.assertNull(city1);

        String cityCode2 = "";
        City city2 = cityService.getCityByCityCode(cityCode2);
        Assert.assertNull(city2);
    }

    @Test
    @Sql("classpath:test/yunxin/geography/city.sql")
    public void testGetCityByCityCode() {
        String cityCode = "330100";
        City city = cityService.getCityByCityCode(cityCode);
        Assert.assertNotNull(city);
    }

    /**
     * 数据库中没有code对应的省份
     */

    @Test
    @Sql("classpath:test/yunxin/geography/city.sql")
    public void testGetCityByCityCodeNoResult() {
        String cityCode = "00003";
        City city = cityService.getCityByCityCode(cityCode);
        Assert.assertNull(city);
    }

}
