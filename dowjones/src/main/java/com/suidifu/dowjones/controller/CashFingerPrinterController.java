package com.suidifu.dowjones.controller;

import com.suidifu.dowjones.exception.ResponseStatus;
import com.suidifu.dowjones.service.CashFingerPrinterService;
import com.suidifu.dowjones.utils.AsyncFtp;
import com.suidifu.dowjones.utils.AsyncRepeatedFTP;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import com.suidifu.dowjones.vo.request.ReRunParameter;
import com.suidifu.dowjones.vo.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 22:34 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RestController
@Api(value = "/")
@Slf4j
public class CashFingerPrinterController extends BaseController {
    @Resource
    private CashFingerPrinterService cashFingerPrinterService;

    /**
     * @param fingerPrinterParameter 输入的指纹表参数
     * @param bindingResult          传入参数对象实例
     * @return 成功或失败信息，json格式封装
     */
    @PostMapping(value = "/api/LedgerBook/CashFingerPrinter",
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "指纹表", httpMethod = "POST",
            notes = "指纹表",
            response = BaseResponse.class
    )
    public BaseResponse getCashFingerPrinters(@Valid
                                              @ApiParam(value = "输入的指纹表参数", required = true)
                                              @RequestBody FingerPrinterParameter fingerPrinterParameter, BindingResult bindingResult) {
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        cashFingerPrinterService.save(fingerPrinterParameter);

//        try {
//            Long startTime = new Date().getTime();
//            List<String> fileNameList = cashFingerPrinterService.operateFile(fingerPrinterParameter);
//            Long endTime = new Date().getTime();
//            log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
//
//            cashFingerPrinterService.post(fileNameList, fingerPrinterParameter.getDataStreamUuid());
//        } catch (IOException e) {
//            log.error("IOException message is: {}", ExceptionUtils.getStackTrace(e));
//        }
        new Thread(new AsyncFtp(cashFingerPrinterService, fingerPrinterParameter)).start();

        baseResponse = new BaseResponse();
        baseResponse.setCode(ResponseStatus.OK.getCode());
        baseResponse.setMessage(ResponseStatus.OK.getMessage());

        return baseResponse;
    }

    @PostMapping(value = "/api/LedgerBook/CashFingerPrintersRepeatedly",
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "指纹表", httpMethod = "POST",
            notes = "指纹表",
            response = BaseResponse.class
    )
    public BaseResponse getCashFingerPrintersRepeatedly(@Valid
                                                        @ApiParam(value = "输入的可重复运行spark任务的指纹表参数", required = true)
                                                        @RequestBody ReRunParameter runParameter, BindingResult bindingResult) {
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }
        log.info("\ndate is :{},financialContractUuid is:{},",
                runParameter.getDate(),
                runParameter.getFinancialContractUuid());

        new Thread(new AsyncRepeatedFTP(cashFingerPrinterService, runParameter)).start();

//        try {
//            FingerPrinterParameter[] fingerPrinterParameters = cashFingerPrinterService.
//                    loadScheduleJob(runParameter.getFinancialContractUuid());
//
//            for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
//                log.info("\ndataStreamUuid is :{},financialContractUuid is:{}," +
//                                "taskId is:{},path is:{}\n",
//                        fingerPrinterParameter.getDataStreamUuid(),
//                        fingerPrinterParameter.getFinancialContractUuid(),
//                        fingerPrinterParameter.getTaskId(),
//                        fingerPrinterParameter.getPath());
//            }
//
//            for (FingerPrinterParameter fingerPrinterParameter : fingerPrinterParameters) {
//                Long startTime = new Date().getTime();
//                List<String> fileNameList = cashFingerPrinterService.
//                        operateFile(fingerPrinterParameter, runParameter.getDate());
//                Long endTime = new Date().getTime();
//                log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
//
//                cashFingerPrinterService.post(fileNameList,
//                        fingerPrinterParameter.getDataStreamUuid());
//            }
//        } catch (IOException e) {
//            log.error("IOException message is: {}", ExceptionUtils.getStackTrace(e));
//        }

        baseResponse = new BaseResponse();
        baseResponse.setCode(ResponseStatus.OK.getCode());
        baseResponse.setMessage(ResponseStatus.OK.getMessage());

        return baseResponse;
    }
}