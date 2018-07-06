	package com.zufangbao.earth.yunxin.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.handler.ActivePaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.handler.BusinessPaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.voucher.BankTransactionNoExistException;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.OrderBusinessPaymentVoucherDetailDTO;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.voucher.CreateBusinessPaymentVoucherModel;
import com.zufangbao.sun.entity.voucher.TemplatesForAll;
import com.zufangbao.sun.entity.voucher.TemplatesForGuarantee;
import com.zufangbao.sun.entity.voucher.TemplatesForPay;
import com.zufangbao.sun.entity.voucher.TemplatesForRepurchase;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseShowModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.ActiveVoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.AppAccountModelForVoucherController;
import com.zufangbao.sun.yunxin.entity.model.voucher.AssetInfoQueryModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.ContractInfoQueryModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.FinancialContractInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAccountInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAssetInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateBaseModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateCashFlowInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateContractInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateRequestModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateSubmitModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherDetailBusinessModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherQueryModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.RecordLogCore;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailCheckState;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentExcuteResult;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowChargeProxy;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.SourceDocumentDetailHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.VoucherAmountStatisticsModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.VoucherShowModel;
import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherProxy;
import com.zufangbao.wellsfargo.yunxin.handler.vouchertask.ActivePaymentVoucherNoSession;

/**
 * 商户付款凭证页
 *
 * @author louguanyang
 */
@Controller("voucherController")
@RequestMapping("/voucher")
@MenuSetting("menu-capital")
public class VoucherController extends BaseController {

    private static final Log logger = LogFactory.getLog(VoucherController.class);
    public final static String UTF_8 = "UTF-8";
    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Autowired
    private SourceDocumentService sourceDocumentService;
    @Autowired
    private SourceDocumentDetailHandler sourceDocumentDetailHandler;
    @Autowired
    private BusinessPaymentVoucherSession businessPaymentVoucherSession;
    @Autowired
    private BusinessPaymentVoucherHandler businessPaymentVoucherHandler;
    @Autowired
    private ActivePaymentVoucherHandler activePaymentVoucherHandler;
    @Autowired
    private ActivePaymentVoucherProxy activePaymentVoucherProxyHandler;
    @Autowired
    private VoucherHandler voucherHandler;
    @Autowired
    private PrincipalHandler principalHandler;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private CashFlowHandler cashFlowHandler;
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Autowired
    private CashFlowChargeProxy cashFlowChargeProxy;

    @Autowired
    private RecordLogCore recordLogCore;
    @Autowired
    private SystemOperateLogHandler systemOperateLogHandler;
    @Autowired
	 private ActivePaymentVoucherNoSession activePaymentVoucherNoSession;

    private final String BankTransactionNoExistExceptionMsg = "打款流水号已关联凭证！";
    private final String SheetNameFormatter = "模板";

    @RequestMapping(value = "/business", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public ModelAndView showBusinessVoucherPage(
            @ModelAttribute VoucherQueryModel voucherQueryModel,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");

        return modelAndView;
    }

    @RequestMapping(value = "/business/optionData", method = RequestMethod.GET)
    public @ResponseBody
    String getBusinessVoucherOptions(
            @Secure Principal principal) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<FinancialContract> financialContracts = principalHandler
                .get_can_access_financialContract_list(principal);

        List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
        result.put("queryAppModels", queryAppModels);

        List<Map<String, Object>> voucherTypeList = genBusinessPaymentVoucherType();
        result.put("voucherTypeList", voucherTypeList);
        result.put("voucherStatusList",
                EnumUtil.getKVList(SourceDocumentDetailStatus.class));
        result.put("financialContracts", financialContracts);

        return jsonViewResolver.sucJsonResult(result);
    }

    private List<Map<String, Object>> genBusinessPaymentVoucherType() {
        List<Map<String, Object>> voucherTypeList = new ArrayList<>();
        for (VoucherType voucherType : VoucherType.BUSINESS_PAYMENT_VOUCHER_TYPE) {
            Map<String, Object> voucherTypeMap = new HashMap<>();
            voucherTypeMap.put("key", voucherType.ordinal());
            voucherTypeMap.put("value", voucherType.getChineseMessage());
            voucherTypeList.add(voucherTypeMap);
        }
        return voucherTypeList;
    }

    /**
     * 主动付款凭证-列表页
     *
     * @param voucherQueryModel
     * @param principal
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public ModelAndView showActiveVoucherPage(
            @ModelAttribute VoucherQueryModel voucherQueryModel,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");

        return modelAndView;
    }

    @RequestMapping(value = "/active/optionData", method = RequestMethod.GET)
    public @ResponseBody
    String getActiveVoucherOptions(
            @Secure Principal principal) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            List<FinancialContract> financialContracts = principalHandler
                    .get_can_access_financialContract_list(principal);

            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
            result.put("queryAppModels", queryAppModels);

            List<Map<String, Object>> voucherTypeList = new ArrayList<>();
            for (VoucherType voucherType : VoucherType.activePaymentVoucherType) {
                Map<String, Object> voucherTypeMap = new HashMap<>();
                voucherTypeMap.put("key", voucherType.ordinal());
                voucherTypeMap.put("value", voucherType.getChineseMessage());
                voucherTypeList.add(voucherTypeMap);
            }
            result.put("voucherTypeList", voucherTypeList);
            result.put("voucherStatusList", Voucher.getVoucherStatusList());

            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getActiveVoucherOptions occur error.");
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "/business/query", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String queryBusinessVoucher(
            @ModelAttribute VoucherQueryModel voucherQueryModel,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            int size = voucherHandler.countBusinessVouchers(voucherQueryModel);
            List<VoucherShowModel> showList = voucherHandler
                    .getBusinessVoucherList(voucherQueryModel, page);
            Map<String, Object> data = new HashMap<>();
            data.put("list", showList);
            data.put("size", size);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "/statistics/voucherAmount", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody String statisticsVoucherAmountBySecondType(@RequestParam String voucherUuid, @RequestParam String secondType){
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            VoucherAmountStatisticsModel voucherAmountStatisticsModel =sourceDocumentDetailService.statisticsVoucherAmountBySecondType(voucherUuid,secondType);
            result.put("voucherAmountStatisticsModel", voucherAmountStatisticsModel);
            return jsonViewResolver.sucJsonResult(result);
        }catch (Exception e) {
            logger.error("#statisticsVoucherAmountBySecondType occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }


    /**
     * 主动付款凭证查询
     *
     * @param voucherQueryModel
     * @param principal
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/active/query", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String queryActiveVoucher(
            @ModelAttribute VoucherQueryModel voucherQueryModel,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            int size = voucherHandler.countActiveVouchers(voucherQueryModel);
            List<Voucher> showList = voucherHandler.getActiveVoucherList(
                    voucherQueryModel, page);
            Map<String, Object> data = new HashMap<>();
            data.put("list", showList);
            data.put("size", size);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "/business/detail/{voucherId}/data", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String getBusinessVoucherDetailData(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            VoucherDetailModel detail = voucherHandler.getBusinessVoucherDetailModel(voucherId);
            if (detail == null) {
                return jsonViewResolver
                        .errorJsonResult("当前凭证不是商户付款凭证，无法进入详情页!");
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("detail", detail);
            result.put("voucherId", voucherId);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getBusinessVoucherDetailData occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 主动付款凭证 - 详情页数据
    @RequestMapping(value = "/active/detail/{voucherNo}/data", method = RequestMethod.GET)
    public @ResponseBody
    String getActiveVoucherDetailOptions(
            @PathVariable("voucherNo") String voucherNo,
            @Secure Principal principal) {
        try {
            ActiveVoucherDetailModel detail = voucherHandler.getActiveVoucherDetail(voucherNo);
            if (detail == null) {
                return jsonViewResolver.errorJsonResult("当前凭证不是主动付款凭证，无法进入详情页!");
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("detail", detail);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getActiveVoucherDetailOptions occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 商户付款凭证 - 单笔核销
     *
     * @param detailId
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(value = "/business/detail/hexiao/{detailId}", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String hexiaoSingleBusinessVoucher(
            @PathVariable("detailId") Long detailId,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            SourceDocumentDetail detail = sourceDocumentDetailService.load(
                    SourceDocumentDetail.class, detailId);
            //TODO
            SourceDocument sourceDocument = sourceDocumentService
                    .getSourceDocumentBy(detail.getSourceDocumentUuid());
            businessPaymentVoucherSession
                    .single_compensatory_recover_loan_asset(
                            Arrays.asList(detail.getUuid()), sourceDocument);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 凭证-业务单据 代偿
     *
     * @param voucherId
     * @param principal
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/business/detail/query", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String queryBusinessVoucherDetails(
            @RequestParam("voucherId") Long voucherId,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            if (voucher == null || !voucher.is_business_payment_voucher()) {
                return jsonViewResolver.errorJsonResult("凭证不存在!");
            }
            return queryVoucherDetails(voucher, page);
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 凭证-业务单据 代偿 导出
     */
    @RequestMapping(value = "/business/detail/query/export")
    public void exportBusinessVoucherDetails(
            @RequestParam("voucherId") Long voucherId, HttpServletRequest request, HttpServletResponse response, @Secure Principal principal) {
        ExportEventLogModel exportEventLogModel = new ExportEventLogModel("10", principal);
        try {
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            if (voucher == null || !voucher.is_business_payment_voucher()) {
                throw new Exception("凭证不存在!");
            }
            exportEventLogModel.recordStartLoadDataTime();

            List<String> csvData = exportVoucherDetails(voucher);

            exportEventLogModel.recordAfterLoadDataComplete(csvData.size());

            Map<String, List<String>> csvs = new HashMap<String, List<String>>();
            csvs.put("凭证业务单据", csvData);
            exportZipToClient(response, "凭证业务单据" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ".zip", GlobalSpec.UTF_8, csvs);

            exportEventLogModel.recordEndWriteOutTime();

            SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtil.getIpAddress(request), LogFunctionType.EXPORTSOURCEDOCUMENTDETAIL, LogOperateType.EXPORT);
            log.setRecordContent("导出商户付款凭证业务单据，凭证编号：【" + voucher.getVoucherNo() + "】，导出记录" + (csvData.size() - 1) + "条。");
            systemOperateLogService.save(log);
        } catch (Exception e) {
            logger.error("#exportBusinessVoucherDetails# occur error.");
            e.printStackTrace();
            exportEventLogModel.setErrorMsg(e.getMessage());
        } finally {
            logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
        }
    }

    /**
     * 商户 回购 凭证-业务单据
     *
     * @param voucherId
     * @param principal
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/business/repurchase/detail/query", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String queryBusinessVoucherDetailsOf(
            @RequestParam("voucherId") Long voucherId,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            if (voucher == null || !voucher.is_business_payment_voucher()) {
                return jsonViewResolver.errorJsonResult("凭证不存在!");
            }
            return queryVoucherDetailsOfRepurchase(voucher, page);
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 商户 担保 凭证-业务单据
     */
    @RequestMapping(value = "/business/guarantee/detail/query", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String queryBusinessVoucherDetailsForGuarantee(
            @RequestParam("voucherId") Long voucherId,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            if (voucher == null || !voucher.is_business_payment_voucher()) {
                return jsonViewResolver.errorJsonResult("凭证不存在!");
            }
            return queryVoucherDetailsOfGuarantee(voucher, page);
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 主动付款凭证-业务单据
     *
     * @param voucherId
     * @param principal
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/active/detail/query", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String queryActiveVoucherDetails(
            @RequestParam("voucherId") Long voucherId,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            if (voucher == null || !voucher.is_active_payment_voucher()) {
                return jsonViewResolver.errorJsonResult("凭证不存在!");
            }
            return queryVoucherDetails(voucher, page);
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    private String queryVoucherDetails(Voucher voucher, Page page) {
        String voucherUuid = voucher.getUuid();
        int size = sourceDocumentDetailService.countSourceDocumentDetailListOfVoucher(voucherUuid, voucher.getFirstNo());
        List<VoucherDetailBusinessModel> showList = sourceDocumentDetailHandler.queryVoucherDetails(voucherUuid, voucher.getFirstNo(), page );
        Map<String, Object> data = new HashMap<>();
        data.put("list", showList);
        data.put("size", size);
        return jsonViewResolver.sucJsonResult(data);
    }

    private List<String> exportVoucherDetails(Voucher voucher) {
        List<VoucherDetailBusinessModel> showList = sourceDocumentDetailHandler.queryVoucherDetails(voucher.getUuid(), voucher.getFirstNo(), null);
        if (CollectionUtils.isNotEmpty(showList)) {
            showList.forEach(this::parseComment);
        }
        ExcelUtil<VoucherDetailBusinessModel> excelUtil = new ExcelUtil<VoucherDetailBusinessModel>(VoucherDetailBusinessModel.class);
        return excelUtil.exportDatasToCSV(showList);
    }

    private void parseComment(VoucherDetailBusinessModel m) {
        try {
            String originComment = m.getComment();
            if (originComment == null || originComment.isEmpty()) {
                return;
            }
            if (originComment.startsWith("[")) {
                List<Result> rs = JSON.parseArray(originComment, Result.class);
                String comment = "";
                for (Result r : rs) {
                    comment += r.getMessage();
                }
                if (!comment.equals("")) {
                    m.setComment(comment);
                }
            } else if (originComment.startsWith("{")) {
                Result r = JSON.parseObject(originComment, Result.class);
                if (r == null) {
                    return;
                }
                m.setComment(r.getMessage());
            }
        } catch (Exception ignored) {
        }
    }

    private String queryVoucherDetailsOfRepurchase(Voucher voucher, Page page) {
        String voucherUuid = voucher.getUuid();
        int size = sourceDocumentDetailService.countSourceDocumentDetailListOfVoucher(voucherUuid, voucher.getFirstNo());
        List<RepurchaseShowModel> showList = sourceDocumentDetailHandler.queryVoucherDetailsOfRepurchase(voucherUuid, voucher.getFirstNo(), page);
        Map<String, Object> data = new HashMap<>();
        data.put("list", showList);
        data.put("size", size);
        return jsonViewResolver.sucJsonResult(data);
    }

	private String queryVoucherDetailsOfGuarantee(Voucher voucher, Page page) {
		String voucherUuid = voucher.getUuid();
		int size = sourceDocumentDetailService.countSourceDocumentDetailListOfVoucher(voucherUuid,voucher.getFirstNo());
		List<OrderBusinessPaymentVoucherDetailDTO> showList= sourceDocumentDetailHandler.queryVoucherDetailsOfGuarantee(voucherUuid,voucher.getFirstNo() , page);
		Map<String, Object> data = new HashMap<>();
		data.put("list", showList);
		data.put("size", size);
		return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
	}

    /***
     * 商戶付款凭证作废
     * @param voucherId
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(value = "/business/invalid/{voucherId}", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String invalidBusinessVoucher(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            businessPaymentVoucherHandler.invalidSourceDocument(voucherId);
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            String recordContent = "作废商戶付款，凭证编号为【 " + voucher.getVoucherNo() + " 】凭证状态为【" + voucher.getStatus().getChineseName() + "】";
            systemOperateLogHandler.operateLog(principal, IpUtil.getIpAddress(request), LogFunctionType.BUSINESS_PAYMENT_VOUCHER_INVALIDATE,
                    LogOperateType.INVALIDATE, voucher, voucher.getUuid(), recordContent);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#invalidBusinessVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 主动付款凭证作废
     *
     * @param voucherId
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(value = "/active/invalid/{voucherId}", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String invalidActiveVoucher(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal, HttpServletRequest request) {
        try {
        	  boolean flag= businessPaymentVoucherHandler.checkVoucher(voucherId);
        	  if(flag == false){
        		  jsonViewResolver.errorJsonResult("凭证已核销或存在凭证明细已核销，不可作废");
        	  }
            businessPaymentVoucherHandler.invalidSourceDocument(voucherId);
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            String recordContent = "作废主动付款凭证，凭证编号为【 " + voucher.getVoucherNo() + " 】";
            systemOperateLogHandler.operateLog(principal, IpUtil.getIpAddress(request), LogFunctionType.ACTIVE_PAYMENT_VOUCHER_INVALIDATE,
                    LogOperateType.INVALIDATE, voucher, voucher.getUuid(), recordContent);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#invalidActiveVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 主动付款凭证 - 凭证类型
    @RequestMapping(value = "/active/voucherTypes", method = RequestMethod.GET)
    public @ResponseBody
    String getVoucherTypes() {
        try {
            List<Map<String, Object>> voucherTypeList = new ArrayList<>();
            for (VoucherType voucherType : VoucherType.activePaymentVoucherType) {
                Map<String, Object> voucherTypeMap = new HashMap<>();
                voucherTypeMap.put("key", voucherType.ordinal());
                voucherTypeMap.put("value", voucherType.getChineseMessage());
                voucherTypeList.add(voucherTypeMap);
            }
            return jsonViewResolver.sucJsonResult("voucherTypes",
                    voucherTypeList);
        } catch (Exception e) {
            logger.error("#getVoucherTypes occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取凭证类型失败！！！");
        }
    }

    /**
     * 主动付款凭证-新增-根据贷款合同编号匹配相关信息
     *
     * @param principal
     * @param request
     * @param contractNo 贷款合同编号
     * @return
     */
    @RequestMapping(value = "/active/create/search-contract", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchAccountInfo(
            @Secure Principal principal,
            HttpServletRequest request,
            @RequestParam(value = "contractNo", required = true, defaultValue = "") String contractNo) {
        try {
            VoucherCreateBaseModel accountInfo = activePaymentVoucherHandler
                    .searchAccountInfoByContractNo(contractNo);
            return jsonViewResolver.sucJsonResult("accountInfo", accountInfo);
        } catch (Exception e) {
            logger.error("#searchAccountInfo occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "/active/create/search-name", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchAccountInfoByName(
            @Secure Principal principal,
            HttpServletRequest request,
            @RequestParam(value = "name", required = true, defaultValue = "") String name) {
        try {
            List<ContractAccount> accountInfos = activePaymentVoucherHandler
                    .searchAccountInfoByName(name);
            return jsonViewResolver.sucJsonResult("contractAccountList",
                    accountInfos);
        } catch (Exception e) {
            logger.error("#searchAccountInfo occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 新增主动付款凭证上传资源文件返回资源文件uuid
     *
     * @param principal
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/active/create/upload-file", method = RequestMethod.POST)
    @MenuSetting("submenu-voucher-active")
    public
    @ResponseBody
    String uploadSingleFile(@Secure Principal principal,
                            MultipartFile file, HttpServletRequest request) {
        try {
            logger.info("新增主动付款凭证，上传资源文件,【操作人】" + principal.getName()
                    + "【IP地址】" + IpUtil.getIpAddress(request));
            if (file == null) {
                return jsonViewResolver.errorJsonResult("请选择要上传的文件");
            }
            String uuid = activePaymentVoucherHandler
                    .uploadSingleFileReturnUUID(file);
            return jsonViewResolver.sucJsonResult("uuid", uuid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#save active voucher occur error ]: "
                    + e.getMessage());
            return jsonViewResolver.errorJsonResult("系统错误," + e.getMessage());
        }
    }

    //修改备注
    @RequestMapping(value = "/active/detail/update-comment/{voucherId}", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String updateActiveVoucherComment(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal,
            HttpServletRequest request,
            @RequestParam(value = "comment", required = true, defaultValue = "") String comment) {
        try {
            activePaymentVoucherHandler.updateActiveVoucherComment(voucherId,
                    comment, principal, IpUtil.getIpAddress(request));
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#update active voucher comment occur error ]: "
                    + e.getMessage());
            return jsonViewResolver.errorJsonResult("系统错误," + e.getMessage());
        }
    }

    //下载凭证
    @RequestMapping(value = "/active/detail/resource/{voucherId}", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String getActiveVoucherResource(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            List<String> resources = activePaymentVoucherHandler
                    .getActiveVoucherResource(voucherId);
            if (CollectionUtils.isNotEmpty(resources)) {
                String zipFileName = DateUtils.today() + "_" + voucherId + "_"
                        + UUID.randomUUID().hashCode() + ".zip";
                zipFilesToClient(response, zipFileName, "UTF-8", resources);
            }

            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            StringBuffer recordContentBuffer = new StringBuffer();
            recordContentBuffer.append("下载原始凭证");
            //操作日志
            try {
                systemOperateLogHandler.operateLog(principal, IpUtil.getIpAddress(request), LogFunctionType.ACTIVE_PAYMENT_VOUCHER_DOWNLOAD,
                        LogOperateType.ADD, voucher, voucher.getUuid(), recordContentBuffer.toString());
            } catch (Exception e) {
                logger.info("error produce log");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#update active voucher comment occur error ]: "
                    + e.getMessage());
            return jsonViewResolver.errorJsonResult("系统错误," + e.getMessage());
        }

        return jsonViewResolver.sucJsonResult();
    }

    /**
     * 流水匹配 商户付款凭证
     *
     * @param voucherId
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(value = "/business/detail/match-cash-flow/{voucherId}", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String matchCashFlow(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            List<CashFlow> cashFlows = businessPaymentVoucherHandler.matchCashflow(voucherId);
            return jsonViewResolver.sucJsonResult("cashFlows", cashFlows);
        } catch (BankTransactionNoExistException e) {
            logger.error("#match cash flow occur error ]: " + BankTransactionNoExistExceptionMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + BankTransactionNoExistExceptionMsg);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#match cash flow occur error ]: " + e.getMessage());
            return jsonViewResolver.errorJsonResult("系统错误," + e.getMessage());
            // TODO: handle exception
        }
    }

    /**
     * 流水匹配 主动付款凭证
     *
     * @param voucherId
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(value = "/business/detail/connection-cash-flow/{voucherId}", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String connectionCashFlow(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal,
            HttpServletRequest request,
            @RequestParam(value = "cashFlowUuid", required = true, defaultValue = "") String cashFlowUuid) {
        try {
            ContractActiveSourceDocumentMapper mapper = businessPaymentVoucherHandler.connectionCashFlow(voucherId,
                    cashFlowUuid);
            if (mapper != null) {
                sendChargeMsg2Mq(mapper);
                sendRecoverMsg2Mq(mapper);
            }
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            String recordContent = "选择流水号【 " + cashFlowUuid + " 】进行匹配";
            systemOperateLogHandler.operateLog(principal, IpUtil.getIpAddress(request), LogFunctionType.ACTIVE_PAYMENT_VOUCHER_CASH_FLOW_MATCH,
                    LogOperateType.ASSOCIATE, voucher, voucher.getUuid(), recordContent);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#connection cash flow occur error ]: "
                    + e.getMessage());
            return jsonViewResolver.errorJsonResult("系统错误," + e.getMessage());
        }
    }


    /**
     * 根据输入内容模糊查询贷款合同
     */
    @RequestMapping(value = "/active/search-contract", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchContract(@Secure Principal principal,
                          @ModelAttribute ContractInfoQueryModel model) {
        try {
            List<VoucherCreateContractInfoModel> modelList = activePaymentVoucherHandler.getContractInfoModelList(model);
            return jsonViewResolver.sucJsonResult("contractlList", modelList);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 根据所选贷款合同查询相应的还款计划
     */
    @RequestMapping(value = "/active/search-asset", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchAsset(@Secure Principal principal, @ModelAttribute AssetInfoQueryModel model) {
        try {
            List<VoucherCreateAssetInfoModel> modelList = activePaymentVoucherHandler.getAssetInfoModelList(model);
            return jsonViewResolver.sucJsonResult("assetList", modelList);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 根据输入内容模糊查询前5个还款方账号
     */
    @RequestMapping(value = "/active/search-account", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchContractAccount(
            @Secure Principal principal,
            @RequestParam("paymentAccountNo") String paymentAccountNo) {
        try {
            List<VoucherCreateAccountInfoModel> models = activePaymentVoucherHandler
                    .getContractAccountInfoModelListBy(paymentAccountNo);
            return jsonViewResolver.sucJsonResult("models", models);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 根据输入内容查询对应的流水
     */
    @RequestMapping(value = "/active/search-cashflow", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchCashFlow(@Secure Principal principal,
                          @ModelAttribute VoucherCreateRequestModel model) {
        try {
            List<VoucherCreateCashFlowInfoModel> cashFlowList = cashFlowHandler
                    .getCashFlowModelList(model.getPaymentAccountNo(),
                            model.getPaymentName(),
                            model.getBankTransactionNo());
            return jsonViewResolver.sucJsonResult("cashFlowList", cashFlowList);
        } catch (BankTransactionNoExistException e) {
            return jsonViewResolver.errorJsonResult(BankTransactionNoExistExceptionMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 根据输入内容查询前五个信托合同 (用到:主动付款凭证，商户付款凭证)
     */
    @RequestMapping(value = "/active/search-financial", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchFinancialContract(@Secure Principal principal,
                                   @RequestParam(value = "financialContractNo", required = true, defaultValue = "") String financialContractNo) {
        try {
            List<FinancialContractInfoModel> models = financialContractService.getFirstFiveFinancialContractInfo(financialContractNo);
            return jsonViewResolver.sucJsonResult("fcList", models);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 主动付款凭证提交
     */
    @RequestMapping(value = "/active/submit", method = RequestMethod.POST)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String submit(@Secure Principal principal,
                  @ModelAttribute VoucherCreateSubmitModel model,
                  HttpServletRequest request) {
        try {
            logger.info("请求提交主动付款凭证:【操作人】" + principal.getName() + "【IP地址】" + IpUtil.getIpAddress(request));

            List<ContractActiveSourceDocumentMapper> mappers = activePaymentVoucherHandler.submit(principal, model, IpUtil.getIpAddress(request));
            for (ContractActiveSourceDocumentMapper mapper : mappers) {
                sendChargeMsg2Mq(mapper);
                sendRecoverMsg2Mq(mapper);
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (e instanceof ApiException) {
                errorMsg = ApiMessageUtil.getMessage(((ApiException) e)
                        .getCode());
            }
            logger.error("#submit active voucher occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    private void sendChargeMsg2Mq(ContractActiveSourceDocumentMapper mapper) {
        try {
            logger.info("#页面上提交主动还款凭证后，发送充值消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "],cashFlowUuid[" + mapper.getCashFlowUuid() + "]");
            cashFlowChargeProxy.charge_cash_into_virtual_account_for_rpc(mapper.getCashFlowUuid(), mapper.getContractUuid(), Priority.High.getPriority());
        } catch (Exception e) {
            logger.error("#页面上提交主动还款凭证后，发送充值消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "],cashFlowUuid[" + mapper.getCashFlowUuid() + "]，with exception stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
    }

    private void sendRecoverMsg2Mq(ContractActiveSourceDocumentMapper mapper) {
        try {
            logger.info("#页面上提交主动还款凭证后，发送待核销主动还款凭证消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "]");
            activePaymentVoucherProxyHandler.recover_active_payment_voucher(mapper.getContractUuid(), mapper.getSourceDocumentUuid(), Priority.Medium.getPriority());
        } catch (Exception e) {
            logger.error("#页面上提交主动还款凭证后，发送待核销主动还款凭证消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "],with exception stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
    }

    /**
     * 主动付款凭证保存
     */
    @RequestMapping(value = "/active/save", method = RequestMethod.POST)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String save(@Secure Principal principal,
                @ModelAttribute VoucherCreateSubmitModel model,
                HttpServletRequest request) {
        try {
            logger.info("请求保存主动付款凭证:【操作人】" + principal.getName() + "【IP地址】"
                    + IpUtil.getIpAddress(request));

            activePaymentVoucherHandler.save(model, principal,
                    IpUtil.getIpAddress(request));
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (e instanceof ApiException) {
                errorMsg = ApiMessageUtil.getMessage(((ApiException) e)
                        .getCode());
            }
            logger.error("#save active voucher occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    /**
     * 根据输入内容查询前五个信托合同 (用到:主动付款凭证，商户付款凭证)
     */
    @RequestMapping(value = "/business/search-financial", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-active")
    public @ResponseBody
    String searchFinancialContractForBusinessPaymentVoucher(@Secure Principal principal,
                                                            @RequestParam(value = "financialContractNo", required = true, defaultValue = "") String keyWord) {
        try {
            List<FinancialContractInfoModel> models = financialContractService.getFinancialContractsByFuzzySearch(keyWord);
            return jsonViewResolver.sucJsonResult("fcList", models);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 商户付款凭证 -- 获取商户付款凭证类型
    @RequestMapping(value = "/business/voucher-type", method = RequestMethod.GET)
    public @ResponseBody
    String getVoucherTypeList() {
        try {
            List<Map<String, Object>> voucherTypeList = genBusinessPaymentVoucherType();
            return jsonViewResolver.sucJsonResult("voucherTypeList", voucherTypeList, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            logger.error("#getVoucherTypeList occur error==" + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    // 商户付款凭证 -- 根据输入内容模糊查询还款方账号
    @RequestMapping(value = "/business/search-account", method = RequestMethod.GET)
    public @ResponseBody
    String getAppAccount(@ModelAttribute AppAccountModelForVoucherController appAccountModel) {
        try {
            List<VoucherCreateAccountInfoModel> accountInfos = businessPaymentVoucherHandler.getAppAccountInfoClasses(appAccountModel);
            return jsonViewResolver.sucJsonResult("data", accountInfos, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            logger.error("#getAccount occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    // 商户付款凭证 -- 付款流水号查重
    @RequestMapping(value = "/business/check", method = RequestMethod.GET)
    public @ResponseBody
    String checkCreditSerialNo(@RequestParam(value = "creditSerialNo") String creditSerialNo) {
        try {
            if (voucherService.countVoucherBySecondNo(creditSerialNo) != 0) {
                return jsonViewResolver.errorJsonResult("付款流水号已存在");
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            logger.error("#checkCreditSerialNo occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    /**
     * 商户付款凭证--根据输入内容查询对应的流水
     */
    @RequestMapping(value = "/business/search-cashflow", method = RequestMethod.GET)
    public @ResponseBody
    String searchCashFlow(
            @RequestParam(value = "financialContractUuid") String financialContractUuid,
            @RequestParam(value = "accountNo") String counterAccountNo) {
        try {
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if (financialContract == null) return jsonViewResolver.errorJsonResult("信托合同不存在");
            Account account = financialContract.getCapitalAccount();
            if (account == null) return jsonViewResolver.errorJsonResult("系统数据错误");
            List<VoucherCreateCashFlowInfoModel> cashFlowList = cashFlowHandler.getCashFlowModelListForBusinessPaymentVoucher(account.getAccountNo(), counterAccountNo);
            return jsonViewResolver.sucJsonResult("cashFlowList", cashFlowList);
        } catch (BankTransactionNoExistException e) {
            return jsonViewResolver.errorJsonResult(BankTransactionNoExistExceptionMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

	/**
	 * 商户付款凭证--新增提交
	 */
	@RequestMapping(value = "business/add", method = RequestMethod.POST)
	public @ResponseBody String addBusinessPaymentVoucher(@ModelAttribute CreateBusinessPaymentVoucherModel voucherModel,@Secure Principal principal,HttpServletRequest request){
		try{
			
			if (!voucherModel.checkSubmitParams()) {
				
				return jsonViewResolver.errorJsonResult(voucherModel.getCheckFailedMsg());
				}


			if(voucherModel.getVoucherAmount().compareTo(voucherModel.getCashFlowAmount())!=0){
				return jsonViewResolver.errorJsonResult("凭证金额与流水金额不等！");}

			logger.info("CreateBusinessPaymentVoucherModel of requestNo :"+voucherModel.getRequestNo()+" 的金额明细(amountDetail) :"+voucherModel.getAmountDetail());
			if(voucherModel.checkDetailAmount()==false){
				return jsonViewResolver.errorJsonResult(voucherModel.getCheckFailedMsg());
			}

			businessPaymentVoucherHandler.createBusinessPaymentVoucher(voucherModel);

            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            logger.error("#addBusinessPaymentVoucher occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    /**
     * 商户付款凭证--导入文件
     */
    @RequestMapping(value = "business/import", method = RequestMethod.POST)
    public @ResponseBody
    String importBusinessPaymentVoucher(@Secure Principal principal,
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(value = "voucherType", required = true) Integer voucherType) {
        try {
            VoucherType type = VoucherType.createBusinessPaymentVoucherType(voucherType);
            switch (type) {
                case PAY:
                case MERCHANT_REFUND:
                case ADVANCE:
                    return jsonViewResolver.sucJsonResult(genVoucherDetails(TemplatesForPay.class, request), SerializerFeature.DisableCircularReferenceDetect);
                case GUARANTEE:
                    return jsonViewResolver.sucJsonResult(genVoucherDetails(TemplatesForGuarantee.class, request), SerializerFeature.DisableCircularReferenceDetect);
                case REPURCHASE:
                    return jsonViewResolver.sucJsonResult(genVoucherDetails(TemplatesForRepurchase.class, request), SerializerFeature.DisableCircularReferenceDetect);
                default:
                    return jsonViewResolver.errorJsonResult("请先选择正确的类型！");
            }
        } catch (ApiException e) {

            e.printStackTrace();
            String errMsg = e.getMessage();
            logger.error(errMsg);
            return jsonViewResolver.errorJsonResult(errMsg);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            logger.error("#importBusinessPaymentVoucher occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("文档内容错误");
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            logger.error("#importBusinessPaymentVoucher occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    private <T extends TemplatesForAll> Map<String, Object> genVoucherDetails(Class<T> template, HttpServletRequest request) throws Exception {
        List<T> dataList = new ArrayList<T>();
        List<String> names = new ArrayList<>();
        BigDecimal amountDetail = new BigDecimal(0);
        MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = fileRequest.getFileNames();
        ExcelUtil<T> excelUtil = new ExcelUtil<T>(template);
        Map<String, String> headers = excelUtil.buildCsvHeader();
        while (fileNames.hasNext()) {
            List<MultipartFile> files = fileRequest.getFiles(fileNames.next());
            for (MultipartFile multipartFile : files) {
                List<T> modelList = excelUtil.importExcelWithinLimit(0, multipartFile.getInputStream(), 1000);
                if (CollectionUtils.isEmpty(modelList))
                    throw new Exception("文档无内容或内容超限(1000行)");
                for (T t : modelList) {
                    amountDetail = amountDetail.add(t.getAmount());
                }
                names.add(multipartFile.getOriginalFilename());
                dataList.addAll(modelList);
            }
        }

        validateParam(dataList);

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("nameList", names);

        dataMap.put("headers", headers);

        dataMap.put("dataList", dataList);

        dataMap.put("voucherAmount", amountDetail);

        return dataMap;
    }

    /**
     * 商户付款凭证--导出模板
     */
    @RequestMapping(value = "business/export", method = RequestMethod.GET)
    public @ResponseBody
    String exportBusinessPaymentVoucher(@Secure Principal principal, HttpServletRequest request, HttpServletResponse response, Integer voucherType) {
        try {
            String fileName = "模板文件.xls";
            genResponse(response, fileName);
            VoucherType type = VoucherType.createBusinessPaymentVoucherType(voucherType);
            if (type == null)
                return jsonViewResolver.errorJsonResult("请先选择正确的类型！");
            switch (type) {
                case PAY:
                case MERCHANT_REFUND:
                case ADVANCE:
                    this.exportVoucher(TemplatesForPay.class, SheetNameFormatter, response);
                    break;
                case GUARANTEE:
                    this.exportVoucher(TemplatesForGuarantee.class, SheetNameFormatter, response);
                    break;
                case REPURCHASE:
                    this.exportVoucher(TemplatesForRepurchase.class, SheetNameFormatter, response);
                    break;
                default:
                    return jsonViewResolver.errorJsonResult("请先选择正确的类型！");
            }

            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            logger.error("#exportBusinessPaymentVoucher occur error ]: " + errorMsg);
            return jsonViewResolver.errorJsonResult("系统错误," + errorMsg);
        }
    }

    @SuppressWarnings({"serial"})
    private <T> void exportVoucher(Class<T> template, String sheetName, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IOException {
        ExcelUtil<T> excelUtil = new ExcelUtil<T>(template);
        excelUtil.exportExcel(new ArrayList<T>() {{
            template.newInstance();
        }}, sheetName, response.getOutputStream());
    }


    private <T extends TemplatesForAll> void validateParam(List<T> dataList) {

        List<String> repeatrepaymentNoList = dataList.stream().filter(data -> data.getRepaymentNo() != null)
                .collect(Collectors.groupingBy(data -> data.getRepaymentNo(), Collectors.counting())).entrySet()
                .stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(repeatrepaymentNoList)) {

            throw new ApiException("还款编号重复 重复的编号为[" + JSON.toJSONString(repeatrepaymentNoList) + "]");

        }

    }

    private void genResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName + "; filename*=" + UTF_8 + "''" + encodeFileName);
    }


    @RequestMapping(value = "/business/detail/delete-job", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-business")
    public @ResponseBody
    String deleteJob(
            @Secure Principal principal,
            HttpServletRequest request,
            @RequestParam(value = "voucherUuid", required = true, defaultValue = "") String voucherUuid) {
        try {
            logger.info("请求对商户付款凭证进行重新核销:【操作人】" + principal.getName() + "【IP地址】" + IpUtil.getIpAddress(request));

            businessPaymentVoucherHandler.deleteJob(voucherUuid, principal, IpUtil.getIpAddress(request));

            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#redo cancel after verification occur error ]: "
                    + e.getMessage());
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }
    
    //主动付款凭证-重新核销
    @RequestMapping(value = "/active/{voucherNo}/re_write_off", method = RequestMethod.POST)
    public @ResponseBody String reWriteOff(@PathVariable("voucherNo") String voucherNo,@Secure Principal principal,String remark) {
        try {
        	  String message = checkVoucherOfReWriteOff(voucherNo, remark);
        	  if(StringUtils.isNotEmpty(message)){
        		  throw new ApiException(message);
        	  }
        	  Voucher voucher= voucherService.getVoucherByVoucherNo(voucherNo);
        	  sourceDocumentService.updateSourceDocumentExcuteStatusByVoucherUuid(voucher.getUuid());
        	  
        	  //触发,主动核销
        	  activePaymentVoucherNoSession.recover_active_by_voucher_uuid(voucher.getUuid());
        	  
           return jsonViewResolver.sucJsonResult();
            
        	} catch (Exception e) {
        		ExceptionUtils.getMessage(e);
        		return jsonViewResolver.errorJsonResult(e,"系统错误");
        	}
    }
    
    private String checkVoucherOfReWriteOff(String voucherNo,String remark){
    	String message = "";
    	if(StringUtils.isEmpty(voucherNo)){
    		return message = "传入凭证为空";
    	}
    	if(StringUtils.isEmpty(remark)){
    		return message = "备注不能为空";
    	}
    	Voucher voucher= voucherService.getVoucherByVoucherNo(voucherNo);
    	if(voucher == null || VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey().equals(voucher.getFirstType())==false){
    		return message = "主动付款凭证不存在";
    	}
    	if(!voucher.getStatus().equals(SourceDocumentDetailStatus.UNSUCCESS) || !voucher.getCheckState().equals(SourceDocumentDetailCheckState.CHECK_SUCCESS)){
    		return message = "请检查凭证,该主动付款凭证不可重新核销";
    	}
    	List<SourceDocument> sourceDocuments = sourceDocumentService.querySourceDocumentByVoucherUuid(voucher.getUuid());
    	List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.getSourceDocumentDetailList(voucher.getUuid(), null, null);
    	if(CollectionUtils.isEmpty(sourceDocuments) || CollectionUtils.isEmpty(sourceDocumentDetails)){
    		return message = "如果没充值,请先充值;如果已充值,则充值失败";
    	}
    	int sucSourceDocumentDetailNum = 0;
    	for(SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetails){
    		if(sourceDocumentDetail.getStatus().equals(SourceDocumentDetailStatus.SUCCESS)){
    			sucSourceDocumentDetailNum++;
    		}
    	}
    	if(sucSourceDocumentDetailNum == sourceDocumentDetails.size()){
    		return message = "凭证明细都已经核销";
    	}
    	return message;
    }
}
