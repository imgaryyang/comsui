package com.suidifu.microservice.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.suidifu.microservice.SourceDocumentReconciliationApplicationTests;
import com.suidifu.microservice.entity.Voucher;
import com.suidifu.microservice.service.VoucherService;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/6 <br>
 * @time: 13:49 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SourceDocumentReconciliationApplicationTests.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Log4j2
@ActiveProfiles(value = "test")
public class VoucherServiceImplTest {
    @Autowired
    @Qualifier("voucherService")
    private VoucherService voucherService;

    private String uuid;

    @Before
    public void setUp() {
        uuid = "07f7c925-716a-4dba-9c45-53dccc89a566";
    }

    @After
    public void tearDown() {
        uuid = null;
    }

    @Test
    public void getVoucherByUuid() {
        Voucher voucher = voucherService.getVoucherByUuid(uuid);

        assertThat(voucher, notNullValue());
        assertThat(voucher.getId(), equalTo(594L));
    }
}