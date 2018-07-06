package com.suidifu.dowjones.service;

import com.suidifu.dowjones.exception.DowjonesException;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;

import java.io.IOException;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/21 <br>
 * @time: 下午1:55 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface OverdueAnalyzeService {
    List<StatisticsReport> getDynamicOverdueRate(InputParameter inputParameter) throws IOException, DowjonesException;

    List<StatisticsReport> getStaticOverdueRate(StaticOverdueRateInputParameter inputParameter) throws IOException, DowjonesException;
}