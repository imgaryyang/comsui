package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.service.CashFingerPrinterServiceV2;
import com.suidifu.dowjones.service.FileGenerationService;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 21:47 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class CashFingerPrinterServiceImplV2Test {
    @Resource
    private CashFingerPrinterServiceV2 cashFingerPrinterServiceV2;
    @Autowired
    private FileGenerationService fileGenerationService;

    @Test
    public void test4DoCashFingerPrinter() throws ParseException {
        String financialContractUuid = "9495f5f2-d306-461a-8b03-5896923dc1b3";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date time  = df.parse("2018-03-27");
        cashFingerPrinterServiceV2.doCashFingerPrinter_DebitCashFlow(financialContractUuid, time);
        cashFingerPrinterServiceV2.doCashFingerPrinter_RepaymentOrderSummary(financialContractUuid, time);
        cashFingerPrinterServiceV2.doCashFingerPrinter_OnlinePaymentDetailsInTransit(financialContractUuid, time);
    }

    @Test
    public void test4GenerateFileReport() throws ParseException, IOException {
        String financialContractUuid = "9495f5f2-d306-461a-8b03-5896923dc1b3";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date time = df.parse("2018-02-15");
        fileGenerationService.generateFileReport(financialContractUuid, time);
    }

    @Test
    public void test4CashFingerPrinterServiceV2() throws ParseException {
        String financialContractUuid = "9495f5f2-d306-461a-8b03-5896923dc1b3";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date time  = df.parse("2018-02-15");
        cashFingerPrinterServiceV2.doCashFingerPrinter_BankCorporateCashFlow(financialContractUuid, time);
    }

}