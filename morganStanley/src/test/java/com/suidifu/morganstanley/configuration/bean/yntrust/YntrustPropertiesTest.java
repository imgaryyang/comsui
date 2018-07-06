package com.suidifu.morganstanley.configuration.bean.yntrust;

import com.suidifu.morganstanley.TestMorganStanley;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 云信信托相关配置 测试
 *
 * @author louguanyang at 2017/10/26 11:23
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class YntrustPropertiesTest {

	@Resource
	private YntrustProperties yntrustProperties;

	@Test
	public void testProperties() {
		String uploadPath = "/tmp";
		String morganstanleyUrl = "http://127.0.0.1:7778";

		Assert.assertNotNull(yntrustProperties);
		Assert.assertEquals(morganstanleyUrl, yntrustProperties.getMorganstanleyUrl());
		Assert.assertEquals(uploadPath, yntrustProperties.getUploadPath());
	}

}