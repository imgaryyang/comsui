package com.suidifu.dowjones.utils;

import com.suidifu.dowjones.service.CashFingerPrinterService;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import com.suidifu.dowjones.vo.request.ReRunParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/19 <br>
 * @time: 22:54 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
public class AsyncRepeatedFTP implements Runnable {
    private CashFingerPrinterService cashFingerPrinterService;
    private ReRunParameter reRunParameter;

    public AsyncRepeatedFTP(CashFingerPrinterService cashFingerPrinterService, ReRunParameter reRunParameter) {
        this.cashFingerPrinterService = cashFingerPrinterService;
        this.reRunParameter = reRunParameter;
    }

    @Override
    public void run() {
        try {
            FingerPrinterParameter[] fingerPrinterParameters = cashFingerPrinterService.loadScheduleJob(reRunParameter.getFinancialContractUuid());

            for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
                log.info("\ndataStreamUuid is :{},financialContractUuid is:{}," +
                                "taskId is:{},path is:{}\n",
                        fingerPrinterParameter.getDataStreamUuid(),
                        fingerPrinterParameter.getFinancialContractUuid(),
                        fingerPrinterParameter.getTaskId(),
                        fingerPrinterParameter.getPath());
            }

            for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
                Long startTime = new Date().getTime();
                List<String> fileNameList = cashFingerPrinterService.operateFile(fingerPrinterParameter, reRunParameter.getDate());
                Long endTime = new Date().getTime();
                log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

                cashFingerPrinterService.post(fileNameList,
                        fingerPrinterParameter.getDataStreamUuid());

            }
        } catch (IOException e) {
            log.error("IOException message is: {}", ExceptionUtils.getStackTrace(e));
        }
    }
}