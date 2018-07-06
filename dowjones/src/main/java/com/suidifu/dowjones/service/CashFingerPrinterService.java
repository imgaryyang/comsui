package com.suidifu.dowjones.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;

import java.io.IOException;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 21:34 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface CashFingerPrinterService {
    void save(FingerPrinterParameter fingerPrinterParameter);

    FingerPrinterParameter[] loadScheduleJob();

    FingerPrinterParameter[] loadScheduleJob(String financialContractUuid);

    List<String> operateFile(FingerPrinterParameter fingerPrinterParameter) throws IOException;

    List<String> operateFile(FingerPrinterParameter fingerPrinterParameter, String date) throws IOException;

    String post(List<String> fileNameList, String dataStreamUuid) throws JsonProcessingException;
}