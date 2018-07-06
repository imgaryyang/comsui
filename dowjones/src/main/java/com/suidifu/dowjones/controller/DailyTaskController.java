package com.suidifu.dowjones.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.suidifu.dowjones.exception.ResponseStatus;
import com.suidifu.dowjones.service.*;
import com.suidifu.dowjones.vo.request.TaskParameter;
import com.suidifu.dowjones.vo.response.BaseResponse;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/daily-task")
@Slf4j
public class DailyTaskController extends BaseController {

    @Autowired
    private DailyActualRepaymentService dailyActualRepaymentService;

    @Autowired
    private DailyRepurchaseService dailyRepurchaseService;

    @Autowired
    private DailyGuaranteeService dailyGuaranteeService;

    @Autowired
    private DailyPlanRepaymentService dailyPlanRepaymentService;

    @Autowired
    private DailyRemittanceService dailyRemittanceService;

    @Autowired
    private FileGenerationService fileGenerationService;

    @Autowired
    private BaiDu_RepaymentAndAssetFileService repaymentAndAssetFileService;

    @Autowired
    private ABS_FileService absFileService;

    @Autowired
    private DailyFirstOverdueRateService firstOverdueRateService;

    @Autowired
    private CashFingerPrinterServiceV2 cashFingerPrinterServiceV2;

    private Executor executor;

    {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("task-pool-%d").build();
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            namedThreadFactory);
    }


    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.POST,
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
    )
    public BaseResponse startDailyTask(@Valid @RequestBody TaskParameter taskParameter, BindingResult bindingResult) {

        try {
            BaseResponse baseResponse = getValidatedResult(bindingResult);
            if (baseResponse != null) {
                return baseResponse;
            }

            baseResponse = new BaseResponse();



            Date dateValue = DateUtils.parseDate(taskParameter.getDate(), "yyyy-MM-dd");

            executor.execute(
                new DailyTask(dailyActualRepaymentService, dailyRepurchaseService, dailyGuaranteeService,
                    dailyPlanRepaymentService, dailyRemittanceService, fileGenerationService,
                    repaymentAndAssetFileService,absFileService, firstOverdueRateService,cashFingerPrinterServiceV2,
                    taskParameter.getFinancialContractUuid(), dateValue, taskParameter.getTaskType()));

            baseResponse.setCode(ResponseStatus.OK.getCode());
            baseResponse.setMessage(ResponseStatus.OK.getMessage());
            baseResponse.setData("接收成功");

            return baseResponse;
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setCode(ResponseStatus.SYSTEM_ERROR.getCode());
            baseResponse.setMessage(ResponseStatus.SYSTEM_ERROR.getMessage());
            baseResponse.setData("接收失败");
            return baseResponse;
        }
    }

}
