package com.suidifu.morganstanley.servers;

import com.suidifu.morganstanley.TestMorganStanley;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.service.CityService;
import lombok.extern.slf4j.Slf4j;
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


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class CityServiceTest {
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
        for (int index = 0; index < 10; index++) {
            String cityCode = "330100";
            City city = cityService.getCityByCityCode(cityCode);
            log.info("cityService.getCityByCityCode(cityCode)");
            Assert.assertNotNull(city);
        }
    }

    /**
     * 数据库中没有code对应的省份
     */

    @Test
    @Sql("classpath:test/yunxin/geography/city.sql")
    public void testGetCityByCityCodeNoResult() {
        for (int index = 0; index < 10; index++) {
            String cityCode = "00003";
            City city = cityService.getCityByCityCode(cityCode);
            log.info("cityService.getCityByCityCode(cityCode)");
            Assert.assertNull(city);
        }
    }

}
