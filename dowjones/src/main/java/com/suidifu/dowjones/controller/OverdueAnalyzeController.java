package com.suidifu.dowjones.controller;

import com.suidifu.dowjones.exception.DowjonesException;
import com.suidifu.dowjones.exception.ResponseStatus;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.service.OverdueAnalyzeService;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import com.suidifu.dowjones.vo.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * User:frankwoo(吴峻申) <br>
 * Date:2016-10-17 <br>
 * Time:17:21 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RestController
@Api(value = "/")
@Slf4j
public class OverdueAnalyzeController extends BaseController {
    @Resource
    private OverdueAnalyzeService overdueAnalyzeService;

    /**
     * @param inputParameter 传入参数对象实例
     * @return 成功或失败信息，json格式封装
     */
    @CrossOrigin
    @PostMapping(value = "/api/OverdueAnalyze/DynamicOverdueRate",
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "动态池逾期率", httpMethod = "POST",
            notes = "动态池逾期率",
            response = BaseResponse.class
    )
    public BaseResponse getDynamicOverdueRate(@Valid
                                              @ApiParam(value = "输入的计算参数", required = true)
                                              @RequestBody InputParameter inputParameter, BindingResult bindingResult) throws IOException,DowjonesException {
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        baseResponse = new BaseResponse();
        Long startTime = new Date().getTime();
        List<StatisticsReport> reports = overdueAnalyzeService.getDynamicOverdueRate(inputParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);
        baseResponse.setCode(ResponseStatus.OK.getCode());
        baseResponse.setMessage(ResponseStatus.OK.getMessage());
        baseResponse.setData(reports);

        return baseResponse;
    }

    /**
     * @param inputParameter 传入参数对象实例
     * @return 成功或失败信息，json格式封装
     */
    @CrossOrigin
    @PostMapping(value = "/api/OverdueAnalyze/StaticOverdueRateOfMonth",
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "月度静态池逾期率", httpMethod = "POST",
            notes = "月度静态池逾期率",
            response = BaseResponse.class
    )
    public BaseResponse getStaticOverdueRate(@Valid
                                             @ApiParam(value = "输入的计算参数", required = true)
                                             @RequestBody StaticOverdueRateInputParameter inputParameter,
                                             BindingResult bindingResult) throws IOException, DowjonesException {
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        baseResponse = new BaseResponse();
        Long startTime = new Date().getTime();
        List<StatisticsReport> reports = overdueAnalyzeService.getStaticOverdueRate(inputParameter);
        Long endTime = new Date().getTime();
        log.info("\n\n\n\n\nduration is :{}ms\n\n\n\n\n", endTime - startTime);

        baseResponse.setCode(ResponseStatus.OK.getCode());
        baseResponse.setMessage(ResponseStatus.OK.getMessage());
        baseResponse.setData(reports);

        return baseResponse;
    }
}