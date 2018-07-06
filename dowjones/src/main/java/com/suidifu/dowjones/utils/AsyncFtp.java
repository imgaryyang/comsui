package com.suidifu.dowjones.utils;

import com.suidifu.dowjones.service.CashFingerPrinterService;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/10 <br>
 * @time: 23:07 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
public class AsyncFtp implements Runnable {
    private CashFingerPrinterService cashFingerPrinterService;
    private FingerPrinterParameter fingerPrinterParameter;

    public AsyncFtp(CashFingerPrinterService cashFingerPrinterService, FingerPrinterParameter fingerPrinterParameter) {
        this.cashFingerPrinterService = cashFingerPrinterService;
        this.fingerPrinterParameter = fingerPrinterParameter;
    }

    @Override
    public void run() {
        try {
            Long startTime = new Date().getTime();
            List<String> fileNameList = cashFingerPrinterService.operateFile(fingerPrinterParameter);
            Long endTime = new Date().getTime();
            log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

            cashFingerPrinterService.post(fileNameList, fingerPrinterParameter.getDataStreamUuid());
        } catch (IOException e) {
            log.error("IOException message is: {}", ExceptionUtils.getStackTrace(e));
        }
    }
}