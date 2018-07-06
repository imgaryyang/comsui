package com.suidifu.morganstanley.configuration.bean.zhonghang;

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
 * @author louguanyang at 2017/11/1 15:01
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class ZhongHangPropertiesTest {
    @Resource
    private ZhongHangProperties properties;

    @Test
    public void testReadProperties() {
        String signKey = "8888888888888";
        String merId = "systemdeduct";
        String secret = "628c8b28bb6fdf5c5add6f18da47f1a6";
        String signMethod = "MD5";
        String requestUrl = "https://www.ezf123.com/jspt/payment/backTransReq.action";
        String cachedJobQueueSize = "5";
        String serverIdentity = "querySignUpNotifyJobServer";
        String persistenceMode = "1";
        String notifyUrlToSignUp = "http://127.0.0.1:9090/pre/api/zhonghang/zhonghang/sign-up";
        String signTransType = "27";
        String queryTransType = "25";
        String signUpGroupName = "group4";
        String morganstanleyUrl = "http://127.0.0.1:7778";

        Assert.assertEquals(morganstanleyUrl, properties.getMorganstanleyUrl());
        Assert.assertEquals(signKey, properties.getSignKey());
        Assert.assertEquals(merId, properties.getMerId());
        Assert.assertEquals(secret, properties.getSecret());
        Assert.assertEquals(signMethod, properties.getSignMethod());
        Assert.assertEquals(requestUrl, properties.getRequestUrl());
        Assert.assertEquals(cachedJobQueueSize, properties.getCachedJobQueueSize());
        Assert.assertEquals(serverIdentity, properties.getServerIdentity());
        Assert.assertEquals(persistenceMode, properties.getPersistenceMode());
        Assert.assertEquals(notifyUrlToSignUp, properties.getNotifyUrlToSignUp());
        Assert.assertEquals(signTransType, properties.getSignTransType());
        Assert.assertEquals(queryTransType, properties.getQueryTransType());
        Assert.assertEquals(signUpGroupName, properties.getSignUpGroupName());
    }
}