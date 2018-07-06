package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.FirstOverdueRateService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.firstOverdueRate.FirstOverdueRate;
import com.zufangbao.sun.entity.firstOverdueRate.FirstOverdueRateShowModel;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by zxj on 2018/3/19.
 */
@Controller
@RequestMapping("/firstOverdueRate")
@MenuSetting("menu-report")
public class FirstOverdueRateController extends BaseController {
    private static final Log logger = LogFactory.getLog(FirstOverdueRateController.class);
    @Autowired
    private FirstOverdueRateService firstOverdueRateService;
    @Autowired
    private PrincipalHandler principalHandler;
    @Autowired
    private FinancialContractService financialContractService;

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public @ResponseBody String getOptions(@Secure Principal principal) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

            result.put("queryAppModels", queryAppModels);
            return jsonViewResolver.sucJsonResult(result);
        }catch (Exception e) {
            logger.error("##FirstOverdueRateController-getOptions## error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(QUERY_ERROR);
        }
    }
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody String query(@RequestParam("financialContractUuids") String financialContractUuids, @RequestParam(value = "date", required = false) String date, Page page, @Secure Principal principal) {
        try {
            Date assetDate = date == null ? null : DateUtils.parseDate(date,"yyyy-MM-dd");
            if (assetDate == null) {
                return jsonViewResolver.errorJsonResult("请选择日期");
            }
            int dataSize = firstOverdueRateService.countList(financialContractUuids,assetDate);
            List<FirstOverdueRate> dataList = firstOverdueRateService.queryList(financialContractUuids,assetDate,page);
            List<FirstOverdueRateShowModel> showModels = new ArrayList<>();
            dataList.forEach(data -> showModels.add(new FirstOverdueRateShowModel(data)));
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("size", dataSize);
            resultData.put("list", showModels);
            return jsonViewResolver.sucJsonResult(resultData);
        }catch (Exception e) {
            logger.error("##FirstOverdueRateController-query## error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(QUERY_ERROR);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public @ResponseBody String update(@RequestParam("financialContractUuid") String financialContractUuid,@RequestParam("date") String date,
                                       @Secure Principal principal) {
        try {
            Date assetDate = date == null ? null : DateUtils.parseDate(date,"yyyy-MM-dd");

            firstOverdueRateService.update(financialContractUuid, assetDate, principal.getName());

            FirstOverdueRate firstOverdueRate = firstOverdueRateService.queryFirstOverdueRate(financialContractUuid, assetDate);
            if (firstOverdueRate == null) return jsonViewResolver.sucJsonResult();
            FirstOverdueRateShowModel showModel = new FirstOverdueRateShowModel(firstOverdueRate);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("row", showModel);
            return jsonViewResolver.sucJsonResult(resultData);
        }catch (Exception e) {
            logger.error("##FirstOverdueRateController-update## error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "/showHistory", method = RequestMethod.GET)
    public @ResponseBody String showHistory(@RequestParam("financialContractUuid") String financialContractUuid,@RequestParam("date") String date) {
        try {
            Date assetDate = date == null ? null : DateUtils.parseDate(date,"yyyy-MM-dd");
            List<FirstOverdueRate> dataList = firstOverdueRateService.showHistory(financialContractUuid, assetDate);
            List<FirstOverdueRateShowModel> showModels = new ArrayList<>();
            dataList.stream().forEach(data -> showModels.add(new FirstOverdueRateShowModel(data)));
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("list", showModels);
            return jsonViewResolver.sucJsonResult(resultData);
        }catch (Exception e) {
            logger.error("##FirstOverdueRateController-showHistory## error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(QUERY_ERROR);
        }
    }
}
