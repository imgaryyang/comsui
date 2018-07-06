package com.zufangbao.earth.web.controller.report;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.earth.util.DownloadUtils;
import com.zufangbao.earth.util.IpUtil;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.reportJob.ZHLoanRepayment;
import com.zufangbao.sun.entity.reportJob.ZHPaymentInfo;
import com.zufangbao.sun.entity.reportJob.ZHReportJob;
import com.zufangbao.sun.entity.reportJob.ZHReportJobShowModel;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.ZHReportJobStatus;
import com.zufangbao.sun.yunxin.entity.ZHReportJobType;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.ReportJobQueryModel;
import com.zufangbao.sun.yunxin.entity.model.ZHReportJobCreateModel;
import com.zufangbao.sun.yunxin.handler.ZHReportJobHandler;
import com.zufangbao.sun.yunxin.service.ZHReportJobService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 报表任务--中航
 * Created by zxj on 2017/11/8.
 */
@Controller
@RequestMapping("/zh/report/jobs")
public class ZHReportJobController extends BaseController {

    @Autowired
    private PrincipalHandler principalHandler;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private ZHReportJobService zhReportJobService;

    @Autowired
    private ZHReportJobHandler zhReportJobHandler;

    private static String savePath;

    private final static String QUERY_COUNT="query_count";

    private final static int FIRST_TIME = 0;

    private static final Log logger = LogFactory.getLog(ZHReportJobController.class);


    @Value("#{config['uploadPath']}")
    private void setSavePath(String uploadPath){
        ZHReportJobController.savePath = (StringUtils.isEmpty(uploadPath) ? getClass().getResource(".").getFile()
                : (uploadPath.endsWith(File.separator) ? uploadPath : uploadPath+ File.separator))
                + "zhReportJob" + File.separator;
    }

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public @ResponseBody String getOption(@Secure Principal principal) {
        try {
            HashMap<String, Object> result = new HashMap<>();

            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

            result.put("queryAppModels", queryAppModels);
            result.put("reportJobStatusList", EnumUtil.getKVList(ZHReportJobStatus.class));
            result.put("requestId", getRequestId());

            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#getOption occured error" + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    /**
     * 为了与
     * @link com.zufangbao.earth.web.controller.report.ReportJobController.java
     * 统一
     * @return
     */
    private Map<String, String> getRequestId() {
        Map<String, String> requestIds = new HashMap<>();
        requestIds.put(ZHReportJobType.LOAN_REPAYMENT.getCode() + "", ZHReportJobType.LOAN_REPAYMENT.getChineseMessage());
        requestIds.put(ZHReportJobType.PAYMENT_INFO.getCode() + "", ZHReportJobType.PAYMENT_INFO.getChineseMessage());
        return requestIds;
    }

    /**
     * query 列表
     * @param principal
     * @param reportJobModel
     * @param page
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody String queryZHReportJob(@Secure Principal principal, @ModelAttribute ReportJobQueryModel reportJobModel, Page page) {
        try {
            HashMap<String, Object> result = new HashMap<String, Object>();

            int size = zhReportJobService.countZHReportJobBy(reportJobModel);
            List<ZHReportJob> reportJobList = zhReportJobService.getZHReportJobBy(reportJobModel, page);
            List<ZHReportJobShowModel> reportJobQueryList = reportJobList.stream().map(ZHReportJobShowModel::new)
                    .collect(Collectors.toList());
            result.put("list", reportJobQueryList);
            result.put("size", size);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#queryZHReportJob  error" + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    /**
     * 新建查询任务
     * @param principal
     * @param reportJobCreateModel
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @MenuSetting("submenu-report-job")
    public @ResponseBody String add(@Secure Principal principal, @ModelAttribute ZHReportJobCreateModel reportJobCreateModel) {
        try {
            if (reportJobCreateModel == null) {
                return jsonViewResolver.errorJsonResult("系统错误");
            }
            checkCreateModel(reportJobCreateModel);
            zhReportJobService.createZHReportJob(reportJobCreateModel, principal.getId());
            return jsonViewResolver.sucJsonResult();
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("#add" + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    private void checkCreateModel(ZHReportJobCreateModel reportJobCreateModel) throws ExportException {
        Date startDate = reportJobCreateModel.getQueryStartDateValue();
        Date endDate = reportJobCreateModel.getQueryEndDateValue();
        if(startDate == null || endDate == null) {
            throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "查询时间不能为空！");
        }

        if(startDate.compareTo(endDate) > 0) {
            throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "查询起始日期不得晚于截止日期！");
        }

        if(StringUtils.isEmpty(reportJobCreateModel.getFinancialContractUuids())) {
            throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "查询项目不能为空！");
        }
    }

    /**
     * 模板下载
     * @param type
     * @param response
     * @return
     */
    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    @MenuSetting("submenu-report-job")
    public @ResponseBody String downloadTemplate(@RequestParam(value = "type") int type , HttpServletResponse response) {
        try {
            ZHReportJobType zhReportJobType =  EnumUtil.fromOrdinal(ZHReportJobType.class, type);
            if (zhReportJobType == null){
                return jsonViewResolver.errorJsonResult("报表任务不存在！");
            }
            String fileKey = "报表任务" + zhReportJobType.getChineseMessage() + "模板." + ZHReportJobHandler.SUFFIX_EXECL;
            switch (zhReportJobType){
                case PAYMENT_INFO:
                    exportTemplate(response, fileKey,ZHPaymentInfo.class);
                    break;
                case LOAN_REPAYMENT:
                    exportTemplate(response, fileKey, ZHLoanRepayment.class);
                    break;
                default:
                    break;
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#downloadTemplate error" + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    private <T> void exportTemplate(HttpServletResponse response,String fileName, Class<T> template) throws IOException, IllegalAccessException, InstantiationException {
        String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + "; filename*="+ "UTF-8" +"''"+ encodeFileName);
        ExcelUtil<T> excelUtil = new ExcelUtil<>(template);
        excelUtil.exportExcel(new ArrayList<T>(){{template.newInstance();}}, "Sheet1",response.getOutputStream());
    }

    /**
     * 上传文件
     * @param type
     * @param uuid
     * @param file
     * @param request
     * @param principal
     * @return
     */
    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public @ResponseBody String importZHReportJobFile(@RequestParam(value = "type") int type, @RequestParam(value = "uuid") String uuid, MultipartFile file,HttpServletRequest request, @Secure Principal principal) {
        try {
            logger.info("报表任务，上传资源文件,【操作人】" + principal.getName()
                    + "【IP地址】" + IpUtil.getIpAddress(request));
            if(file == null) {
                return jsonViewResolver.errorJsonResult("请选择要上传的文件");
            }
            if (StringUtils.isEmpty(uuid)) {
                return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
            }
            InputStream in = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(in);
            if (Objects.equals(type, ZHReportJobType.LOAN_REPAYMENT.getCode())) {
                List<ZHLoanRepayment> loanRepaymentList = new ExcelUtil<>(ZHLoanRepayment.class).importExcelHighVersion(0,workbook);
                if (CollectionUtils.isEmpty(loanRepaymentList)) {
                    return jsonViewResolver.errorJsonResult("放款还款数据为空");
                }
            }

            if (Objects.equals(type, ZHReportJobType.PAYMENT_INFO.getCode())) {
                List<ZHPaymentInfo> paymentInfoList = new ExcelUtil<>(ZHPaymentInfo.class).importExcelHighVersion(0,workbook);
                if (CollectionUtils.isEmpty(paymentInfoList)) {
                    return jsonViewResolver.errorJsonResult("专户流水数据为空");
                }
            }
            zhReportJobHandler.importExcel(file, savePath, uuid);
            in.close();
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#importZHReportJobFile error" + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    /**
     * 文件预览-查看
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public @ResponseBody String getPreviewExportExcel(@RequestParam("uuid") String uuid) {
        try {
            if (StringUtils.isEmpty(uuid)) {
                return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
            }
            HashMap<String, Object> result = new HashMap<String, Object>();
            ZHReportJob zhReportJob = zhReportJobService.getByUuid(uuid);
            String filePath = zhReportJob.getFilePath();

            File file = new File(filePath);
            Workbook workbook = WorkbookFactory.create(file);

            if (Objects.equals(zhReportJob.getReportType(), ZHReportJobType.LOAN_REPAYMENT.getCode())) {
                List<ZHLoanRepayment> loanRepaymentList = new ExcelUtil<>(ZHLoanRepayment.class).importExcelHighVersion(0,workbook);
                result.put("list", loanRepaymentList);
                result.put("size", loanRepaymentList.size());
            }

            if (Objects.equals(zhReportJob.getReportType(), ZHReportJobType.PAYMENT_INFO.getCode())) {
                List<ZHPaymentInfo> paymentInfoList = new ExcelUtil<>(ZHPaymentInfo.class).importExcelHighVersion(0,workbook);
                result.put("list", paymentInfoList);
                result.put("size", paymentInfoList.size());
            }
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#getPreviewExportExcel error" + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param uuid
     * @param response
     * @return
     */
    @RequestMapping(value = "/files/download", method = RequestMethod.GET)
    public @ResponseBody String downloadExcel(@RequestParam(value = "uuid") String uuid , HttpServletResponse response) {
        try {
            if (StringUtils.isEmpty(uuid)) {
                return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
            }
            ZHReportJob zhReportJob = zhReportJobService.getByUuid(uuid);
            if (zhReportJob == null) {
                return jsonViewResolver.errorJsonResult("不存在该报表任务");
            }
            String filePath = zhReportJob.getFilePath();

            File file = new File(filePath);
            if (!file.exists()) {
                logger.error("downloadExcel error. FilePath: " + filePath);
                return jsonViewResolver.errorJsonResult("文件不存在！");
            }
            String realName = zhReportJob.getFinancialProductCode()+ "_" + EnumUtil.fromOrdinal(ZHReportJobType.class, zhReportJob.getReportType()).getChineseMessage()+"_" + zhReportJob.getReportParamsStr() + "." + ZHReportJobHandler.SUFFIX_EXECL;
            DownloadUtils.flushFileIntoHttp(realName, filePath, "", response);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#downloadExcel error" + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    /**
     * 查看已创建
     * @param request
     * @return
     */
    @RequestMapping(value = "/views/create", method = RequestMethod.GET)
    public @ResponseBody String querySizeOfCreateStatus(HttpServletRequest request) {
        try{
            HashMap<String, Object> result = new HashMap<String, Object>();
            HttpSession session = request.getSession();
            Object queryValue = session.getAttribute(QUERY_COUNT);
            Integer queryTimes =  queryValue == null ? FIRST_TIME : (Integer) queryValue;
            if (Objects.equals(queryTimes, FIRST_TIME)) {
                int totalOfCreate = zhReportJobService.totalOfCreateStatus();
                result.put("totalOfCreate", totalOfCreate);
            }
            session.setAttribute(QUERY_COUNT, queryTimes + 1);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#querySizeOfCreateStatus error " + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

}
