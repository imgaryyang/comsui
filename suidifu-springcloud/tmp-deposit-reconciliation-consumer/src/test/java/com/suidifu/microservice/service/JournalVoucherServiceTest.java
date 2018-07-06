package com.suidifu.microservice.service;

import com.suidifu.microservice.ConsumerTest;
import lombok.extern.log4j.Log4j2;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author louguanyang at 2018/3/6 20:48
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@ActiveProfiles(value = "test")
public class JournalVoucherServiceTest {



}