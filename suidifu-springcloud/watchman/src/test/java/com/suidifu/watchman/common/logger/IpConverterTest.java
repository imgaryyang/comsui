package com.suidifu.watchman.common.logger;

import com.suidifu.watchman.WatchManTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author louguanyang at 2018/1/10 19:23
 * @mail louguanyang@hzsuidifu.com
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "junit")
@SpringBootTest(classes = WatchManTests.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class IpConverterTest {

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Test
    public void testLog() {
        log.trace("this is a trace log");
        log.debug("this is a debug log");
        log.warn("this is a warn log");
        log.info("this is a info log");
        log.error("this is a error log");

        MDC.put("haha", "hello");

        LOGGER.trace("this is a trace log");
        LOGGER.debug("this is a debug log");
        LOGGER.warn("this is a warn log");
        LOGGER.info("this is a info log");
        LOGGER.error("this is a error log");
    }
}