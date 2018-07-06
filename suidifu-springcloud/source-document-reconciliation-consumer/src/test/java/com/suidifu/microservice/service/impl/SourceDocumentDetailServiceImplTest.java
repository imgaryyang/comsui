package com.suidifu.microservice.service.impl;

import static com.zufangbao.sun.yunxin.entity.api.VoucherSource.BUSINESS_PAYMENT_VOUCHER;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.suidifu.microservice.SourceDocumentReconciliationApplicationTests;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import java.util.List;
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
 * @time: 13:00 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SourceDocumentReconciliationApplicationTests.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Log4j2
@ActiveProfiles(value = "test")
public class SourceDocumentDetailServiceImplTest {
    @Autowired
    @Qualifier("sourceDocumentDetailService")
    private SourceDocumentDetailService sourceDocumentDetailService;

    private String sourceDocumentDetailUuid;
    private String sourceDocumentUuid;
    private String outlierSerialGlobalIdentity;

    @Before
    public void setUp() {
        sourceDocumentDetailUuid = "cbb69a99-6087-4c63-bf84-faad38aa4d6a";
        sourceDocumentUuid = "c0851f8e-d891-4186-b483-50fda6d96bce";
        outlierSerialGlobalIdentity = "207e3cd6-7ff0-11e46-a206e-98744232231231898";
    }

    @After
    public void tearDown() {
        sourceDocumentDetailUuid = null;
        sourceDocumentUuid = null;
        outlierSerialGlobalIdentity = null;
    }

    @Test
    public void getSourceDocumentDetail() {
        SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.
                getSourceDocumentDetail(sourceDocumentDetailUuid);

        assertThat(sourceDocumentDetail, notNullValue());
        assertThat(sourceDocumentDetail.getId(), equalTo(3L));
    }

    @Test
    public void getValidDeductSourceDocumentDetailsBySourceDocumentUuid() {
        sourceDocumentDetailUuid = "fffca76d-7bb0-4358-9cb8-485d7fa63b30";
        List<SourceDocumentDetail> sourceDocumentDetailList = sourceDocumentDetailService.
                getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocumentDetailUuid);

        assertThat(sourceDocumentDetailList, notNullValue());
        assertThat(sourceDocumentDetailList.size(), equalTo(1));
        assertThat(sourceDocumentDetailList.get(0), notNullValue());
        assertThat(sourceDocumentDetailList.get(0).getId(), equalTo(88242L));
    }

    @Test
    public void existTrue() {
        boolean isBusinessPaymentVoucher = sourceDocumentDetailService.
                exist(sourceDocumentUuid, BUSINESS_PAYMENT_VOUCHER.getKey(),
                        outlierSerialGlobalIdentity);

        assertThat(isBusinessPaymentVoucher, is(true));
    }

    @Test
    public void existFalse() {
        sourceDocumentUuid = "c0851f8e-d891-4186-b483-50fda6d96ce";

        boolean isBusinessPaymentVoucher = sourceDocumentDetailService.
                exist(sourceDocumentUuid, BUSINESS_PAYMENT_VOUCHER.getKey(),
                        outlierSerialGlobalIdentity);

        assertThat(isBusinessPaymentVoucher, is(false));
    }

    @Test
    public void getDetailUuidsBySourceDocumentUuid() {
        sourceDocumentUuid = "7cf60311-c5cd-47f7-8825-c89e0df262af";
        outlierSerialGlobalIdentity = "bank_transaction_no_10001";

        List<String> detailUuids = sourceDocumentDetailService.
                getDetailUuidsBySourceDocumentUuid(sourceDocumentUuid, outlierSerialGlobalIdentity);

        assertThat(detailUuids, notNullValue());
        assertThat(detailUuids.size(), equalTo(10));
    }
}