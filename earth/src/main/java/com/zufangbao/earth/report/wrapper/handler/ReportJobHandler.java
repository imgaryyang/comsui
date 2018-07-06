package com.zufangbao.earth.report.wrapper.handler;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ReportJobHandler {

    String createReportJob(String reportId, List<String> financialContractUuids, JSONObject reportParams, Long userId);
}
