package com.zufangbao.earth.yunxin.web.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.util.DownloadUtils;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.LoanBatchHandler;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanExcelVO;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.loanbatch.LoanBatchQueryModel;
import com.zufangbao.sun.yunxin.entity.model.loanbatch.LoanBatchShowModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.service.LoanBatchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("loan-batch")
@MenuSetting("menu-data")
public class LoanBatchController extends BaseController {

    private static final Log logger = LogFactory.getLog(LoanBatchController.class);
    @Autowired
    private LoanBatchService loanBatchService;
    @Autowired
    private LoanBatchHandler loanBatchHandler;
    @Autowired
    private PrincipalHandler principalHandler;
    @Autowired
    private PrincipalService principalService;
    @Autowired
    public FinancialContractService financialContractService;
    private static String savePath;

    @Value("#{config['uploadPath']}")
    private void setSavePath(String uploadPath) {
        if (StringUtils.isEmpty(uploadPath)) {
            LoanBatchController.savePath = getClass().getResource(".").getFile().toString() + "assetsPackage/";
        } else if (uploadPath.endsWith(File.separator)) {
            LoanBatchController.savePath = uploadPath + "assetsPackage" + File.separator;
        } else {
            LoanBatchController.savePath = uploadPath + File.separator + "assetsPackage" + File.separator;
        }
    }

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public @ResponseBody
    String getLoanBatchListOptions(@Secure Principal principal) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

            result.put("queryAppModels", queryAppModels);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("列表选项获取错误");
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @MenuSetting("submenu-assets-package-loan_batch")
    public @ResponseBody
    String query(@Secure Principal principal, Page page, HttpServletRequest request,
                 @ModelAttribute LoanBatchQueryModel loanBatchQueryModel) {
        try {
            int size = loanBatchService.countLoanBatchList(loanBatchQueryModel);
            List<LoanBatchShowModel> loanBatchShowModelList = loanBatchHandler.generateLoanBatchShowModelList(loanBatchQueryModel, page);
            Map<String, Object> data = new HashMap<>();
            data.put("list", loanBatchShowModelList);
            data.put("size", size);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    //	@RequestMapping(value = "/export-repayment-plan/{loanBatchId}")
//	@MenuSetting("submenu-assets-package-loan_batch")
    @Deprecated
    public @ResponseBody
    String exportRepaymentPlan(HttpServletRequest request, Page page,
                               @PathVariable Long loanBatchId, HttpServletResponse response,
                               @Secure Principal principal) {
        ExportEventLogModel exportEventLogModel = new ExportEventLogModel("2", principal);
        try {
            exportEventLogModel.recordStartLoadDataTime();

            Map<String, List<String>> csvs = loanBatchHandler.createExcelList(loanBatchId);

            List<Integer> sizes = csvs.values().stream().map(csv -> csv.size()).collect(Collectors.toList());
            exportEventLogModel.recordAfterLoadDataComplete(sizes.toArray(new Integer[csvs.size()]));

            loanBatchHandler.generateLoanBacthSystemLog(principal, IpUtil.getIpAddress(request), LogFunctionType.EXPORTREPAYEMNTPLAN, LogOperateType.EXPORT,
                    loanBatchId);

            exportZipToClient(response, create_download_file_name(DateUtils.asDay(new Date()), loanBatchId), GlobalSpec.UTF_8, csvs);

            exportEventLogModel.recordEndWriteOutTime();
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            exportEventLogModel.setErrorMsg(e.getMessage());
            return jsonViewResolver.errorJsonResult("导出失败");
        } finally {
            logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
        }
    }

    //导出预览
    @RequestMapping(value = "/preview-export-repayment-plan/{loanBatchId}")
    @MenuSetting("submenu-assets-package-loan_batch")
    public @ResponseBody
    String previewExportRepaymentPlan(HttpServletRequest request,
                                      @PathVariable Long loanBatchId, HttpServletResponse response,
                                      @Secure Principal principal) {
        try {
            List<RepaymentPlanExcelVO> repaymentPlanExcelVOList = loanBatchHandler.previewRepaymentPlanExcelVO(loanBatchId, 10);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("list", repaymentPlanExcelVOList);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("导出预览失败");
        }
    }

    @RequestMapping(value = "/delete-loan-batch", method = RequestMethod.POST)
    @MenuSetting("submenu-assets-package-loan_batch")
    public @ResponseBody
    String deleteLoanBatch(
            @RequestParam(required = true) Long loanBatchId,
            @Secure Principal principal, HttpServletRequest request,
            HttpServletResponse response) {

        Result result = new Result();
        try {
            loanBatchHandler.deleteLoanBatchData(principal, IpUtil.getIpAddress(request), loanBatchId);
            result.success().setMessage("成功删除批次数据！！");
            return JsonUtils.toJSONString(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.fail().setMessage(e.getMessage());
            return JsonUtils.toJSONString(result);
        }
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @MenuSetting("submenu-assets-package-loan_batch")
    public @ResponseBody
    String activateLoanBatch(
            @RequestParam Long loanBatchId, @Secure Principal principal,
            HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        try {
            loanBatchHandler.activateLoanBatch(loanBatchId);
            loanBatchHandler.generateLoanBacthSystemLog(principal, IpUtil.getIpAddress(request), LogFunctionType.ACTIVELOANBATCH, LogOperateType.ACTIVE, loanBatchId);
        } catch (Exception e) {
            e.printStackTrace();
            result.fail().message("激活失败！！！");
            return JsonUtils.toJSONString(result);
        }
        result.success().message("激活成功！！");
        return JsonUtils.toJSONString(result);
    }

    private String create_download_file_name(Date create_date, Long loanBatchId) {
        LoanBatch loanBatch = loanBatchService.load(LoanBatch.class,
                loanBatchId);
        return loanBatch.getCode() + "批次" + "详细还款计划"
                + DateUtils.format(create_date, "yyyy_MM_dd") + ".zip";
    }


    /**
     * 导入资产包--下载模板
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @MenuSetting("submenu-assets-package-loan_batch")
    public @ResponseBody
    String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileKey = "信托项目资产包线下导入模板.xlsx";
            File file = new File(savePath + fileKey);
            if (!file.exists()) {
                logger.error("Downloading file error. FilePath: " + savePath + fileKey);
                return jsonViewResolver.errorJsonResult("文件不存在！");
            }
            //处理文件名
            String realname = fileKey.substring(fileKey.indexOf("_") + 1);
            DownloadUtils.flushFileIntoHttp(realname, fileKey, savePath, response);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#downloadFile# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("文件下载失败！");
        }
    }
}
