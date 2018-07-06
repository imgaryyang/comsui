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
 * @author louguanyang at 2017/11/1 14:54
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class YntrustFileTaskTest {
    @Resource
    private YntrustFileTask yntrustFileTask;

    @Test
    public void testReadFileTaskProperties() {
        String merId = "systemdeduct";
        String secret = "628c8b28bb6fdf5c5add6f18da47f1a6";
        String scanPath = "/tmp";

        Assert.assertNotNull(yntrustFileTask);
        Assert.assertEquals(merId, yntrustFileTask.getMerId());
        Assert.assertEquals(secret, yntrustFileTask.getSecret());
        Assert.assertEquals(scanPath, yntrustFileTask.getScanPath());
    }
}