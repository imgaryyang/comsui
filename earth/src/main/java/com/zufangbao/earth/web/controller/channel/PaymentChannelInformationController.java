package com.zufangbao.earth.web.controller.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.util.DownloadUtils;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.reapymentInfo.BankCoreCodeMapSpec;
import com.zufangbao.earth.yunxin.handler.BankTransactionLimitSheetHandler;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.financial.BankTransactionConfigure;
import com.zufangbao.sun.entity.financial.BankTransactionLimitSheet;
import com.zufangbao.sun.entity.financial.BankTransactionLimitSheetUpdateModel;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.entity.financial.ChannelWorkingStatus;
import com.zufangbao.sun.entity.financial.ChargeExcutionMode;
import com.zufangbao.sun.entity.financial.ChargeRateMode;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfig;
import com.zufangbao.sun.entity.financial.PaymentChannelConfigure;
import com.zufangbao.sun.entity.financial.PaymentChannelCreateModel;
import com.zufangbao.sun.entity.financial.PaymentChannelInformation;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.financial.PaymentStrategyMode;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.handler.FinancialContractConfigHandler;
import com.zufangbao.sun.service.BankTransactionLimitSheetService;
import com.zufangbao.sun.service.FinancialContractConfigService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.service.TransferApplicationService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.model.ChannelPollingRequestModel;
import com.zufangbao.sun.yunxin.entity.model.PaymentChannelQueryModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.StrategySwitchResultSubmitModel;
import com.zufangbao.sun.yunxin.entity.model.TimingStrategySwitchResultSubmitModel;
import com.zufangbao.sun.yunxin.entity.model.TransactionLimitQueryModel;
import com.zufangbao.sun.yunxin.entity.model.TransferApplicationQueryModel4TransactionDetail;
import com.zufangbao.sun.yunxin.entity.model.TransferApplicationResultModel4TransactionDetail;
import com.zufangbao.sun.yunxin.entity.model.TransferApplicationStatisticsModel;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import com.zufangbao.wellsfargo.yunxin.handler.TransferApplicationHandler;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/paymentchannel")
public class PaymentChannelInformationController extends BaseController {

    @Autowired
    private PaymentChannelInformationHandler paymentChannelInformationHandler;

    @Autowired
    private BankTransactionLimitSheetHandler bankTransactionLimitSheetHandler;

    @Autowired
    private TransferApplicationHandler transferApplicationHandler;

    @Autowired
    private FinancialContractConfigHandler financialContractConfigHandler;

    @Autowired
    private FinancialContractConfigService financialContractConfigService;

    @Autowired
    private PaymentChannelInformationService paymentChannelInformationService;

    @Autowired
    private TransferApplicationService transferApplicationService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private BankTransactionLimitSheetService bankTransactionLimitSheetService;

    private static final Log logger = LogFactory.getLog(PaymentChannelInformationController.class);

    private static String savePath;

    @Value("#{config['uploadPath']}")
    private void setSavePath(String uploadPath) {
        if (StringUtils.isEmpty(uploadPath)) {
            PaymentChannelInformationController.savePath = getClass().getResource(".").getFile() + "paymentChannel/";
        } else if (uploadPath.endsWith(File.separator)) {
            PaymentChannelInformationController.savePath = uploadPath + "paymentChannel" + File.separator;
        } else {
            PaymentChannelInformationController.savePath =
                uploadPath + File.separator + "paymentChannel" + File.separator;
        }
    }

    private static final int EVERY_PAGE = 8;

    // 获取下拉框选项
    @RequestMapping("/optionData")
    public String showAllData(@Secure Principal principal) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("creditStatusList", EnumUtil.getKVMap(ChannelWorkingStatus.class));
            data.put("debitStatusList", EnumUtil.getKVMap(ChannelWorkingStatus.class));
//            data.put("gatewayList", EnumUtil.getKVMap(PaymentInstitutionName.class));
            data.put("gatewayList", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
            data.put("businessTypeList", EnumUtil.getKVList(BusinessType.class));
            data.put("channelWorkingStatus", EnumUtil.getKVEnList(ChannelWorkingStatus.class));
            data.put("chargeRateMode", EnumUtil.getKVEnList(ChargeRateMode.class));
            data.put("chargeExcutionMode", EnumUtil.getKVEnList(ChargeExcutionMode.class));

            List<FinancialContract> financialContractList = financialContractService.loadAll(FinancialContract.class);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContractList);
            data.put("queryAppModels", queryAppModels);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("##PaymentChannelInformationController-showAllData## get option data error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(QUERY_ERROR);
        }
    }

    // 通道配置-列表 查询
    @RequestMapping(value = "/config/search", method = RequestMethod.GET)
    public String searchChannel(
        @ModelAttribute PaymentChannelQueryModel queryModel, Page page) {
        try {
            Map<String, Object> dataMap = paymentChannelInformationHandler.searchPaymentChannelBy(queryModel, page);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#searchChannel#  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 通道配置-新增通道
    @RequestMapping(value = "/config/create", method = RequestMethod.POST)
    public String createPaymentChannelInformation(
        @RequestParam(value = "paymentChannelCreateModelJson") String PaymentChannelCreateModelJson) {
        try {
            PaymentChannelCreateModel paymentChannelCreateModel = JsonUtils
                .parse(PaymentChannelCreateModelJson, PaymentChannelCreateModel.class);
            PaymentChannelInformation paymentChannelInformation = paymentChannelInformationHandler
                .createPaymentChannelInformation(paymentChannelCreateModel);
            if (paymentChannelInformation == null) {
                return jsonViewResolver.errorJsonResult("新增通道失败！请检查信息！");
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("##createPaymentChannel## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("新增通道失败！请检查信息！");
        }
    }

    // 通道配置-新增通道-唯一编号解析
    @RequestMapping(value = "/config/analyze", method = RequestMethod.GET)
    public String analyzeUniqueNo(
        @RequestParam(value = "uniqueNo") String uniqueNo) {
        try {
            if (StringUtils.isBlank(uniqueNo)) {
                return jsonViewResolver.errorJsonResult("请输入唯一编号！！");
            }
            Map<String, Object> analyzeResult = paymentChannelInformationHandler.analyzeUniqueNo(uniqueNo);
            if (analyzeResult == null) {
                return jsonViewResolver.errorJsonResult("唯一编号不合法！！");
            }
            return jsonViewResolver.sucJsonResult(analyzeResult, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#analyzeUniqueNo# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("唯一编号解析失败！请仔细核对！");
        }
    }

    // 通道配置-列表 详情操作
    @RequestMapping(value = "/config/detail", method = RequestMethod.GET)
    public String viewDetails(
        @RequestParam(value = "paymentChannelUuid", required = false) String paymentChannelUuid) {
        try {
            Map<String, Object> data = paymentChannelInformationHandler.getPaymentChannelDetails(paymentChannelUuid);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullStringAsEmpty);
        } catch (Exception e) {
            logger.error("#viewDetails# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误，请重试");
        }
    }

    // 通道配置-配置 页面
    @RequestMapping(value = "/config/{paymentChannelUuid}", method = RequestMethod.GET)
    public String editPaymentChannel(
        @PathVariable(value = "paymentChannelUuid") String paymentChannelUuid) {
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            genPaymentChannelRtnDataMap(dataMap, paymentChannelUuid);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#editPaymentChannel# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("通道配置页面信息获取失败");
        }
    }

    private void genPaymentChannelRtnDataMap(Map<String, Object> dataMap, String paymentChannelUuid) {
        dataMap.put("chargeRateMode", EnumUtil.getKVEnList(ChargeRateMode.class));
        dataMap.put("channelWorkingStatus", ChannelWorkingStatus.getChineseOrdinalListExceptNotLink());
        dataMap.put("chargeExcutionMode", EnumUtil.getKVEnList(ChargeExcutionMode.class));
        FinancialContract fc = paymentChannelInformationHandler.getFinancialContractBy(paymentChannelUuid);
        if (fc != null && fc.getCapitalAccount() != null) {
            Account captitalAccount = fc.getCapitalAccount();
            String unionStr = captitalAccount.getBankName() + "(" + captitalAccount.getMarkedAccountNo() + ")";
            dataMap.put("captitalAccountNameAndNo", unionStr);
        }
        PaymentChannelInformation pci = paymentChannelInformationService
            .getPaymentChannelInformationBy(paymentChannelUuid);
        if (pci != null) {
            String configDataJsonStr = pci.getPaymentConfigureData();
            PaymentChannelConfigure extractConfigData = pci.extractConfigData();
            dataMap.put("paymentChannelConfigData", configDataJsonStr);
            dataMap.put("relatedFinancialContractName", pci.getRelatedFinancialContractName());
            dataMap.put("relatedFinancialContractUuid", pci.getRelatedFinancialContractUuid());
            dataMap.put("paymentChannelName", pci.getPaymentChannelName());
            dataMap.put("outlierChannelName", pci.getOutlierChannelName());
            dataMap.put("paymentInstitutionOrdinal",
                pci.getPaymentInstitutionName() == null ? -1 : pci.getPaymentInstitutionName().ordinal());
            dataMap.put("paymentInstitutionName",
                pci.getPaymentInstitutionName() == null ? "" : pci.getPaymentInstitutionName().getChineseMessage());
            dataMap.put("businessTypeName",
                pci.getBusinessType() == null ? "" : pci.getBusinessType().getChineseMessage());
            dataMap.put("clearingNo", pci.getClearingNo());
            dataMap.put("creditChannelWorkingStatus", pci.getCreditChannelWorkingStatus().getOrdinal());
            dataMap.put("debitChannelWorkingStatus", pci.getDebitChannelWorkingStatus().getOrdinal());
        }
    }

    // 通道配置-配置页面 判断通道名是否唯一
    @RequestMapping(value = "/config/edit/check", method = RequestMethod.GET)
    public String checkParams(
        @RequestParam(value = "paymentChannelUuid") String paymentChannelUuid,
        @RequestParam(value = "paymentChannelName") String paymentChannelName) {
        try {
            boolean isTrue = paymentChannelInformationService.isUnique(paymentChannelName, paymentChannelUuid);
            return jsonViewResolver.jsonResult(isTrue);
        } catch (Exception e) {
            logger.error("#checkParams# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误,请重试");
        }
    }

    // 通道配置-配置页面 下载模板
    @Deprecated
    @RequestMapping(value = "/config/edit/downloadBankLimitXls", method = RequestMethod.GET)
    public String downloadBankLimitXls(HttpServletRequest request, HttpServletResponse response) {
        try {
            ExcelUtil<BankTransactionConfigure> excelUtil = new ExcelUtil<BankTransactionConfigure>(
                BankTransactionConfigure.class);
            List<BankTransactionConfigure> list = new ArrayList<BankTransactionConfigure>();
            list.add(new BankTransactionConfigure());
            HSSFWorkbook workBook = excelUtil.exportDataToHSSFWork(list, "银行限额表");
            exportExcelToClient(response, "银行限额模板.xls", GlobalSpec.UTF_8, workBook);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#downloadBankLimitXls# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误,请重试");
        }
    }

    // 通道配置 - 配置页面 银行限额预览
    @RequestMapping(value = "/config/edit/bankLimitPreview", method = RequestMethod.GET)
    public String bankLimitPreview(
        @RequestParam(value = "paymentInstitutionOrdinal") int paymentInstitutionOrdinal,
        @RequestParam(value = "outlierChannelName") String outlierChannelName,
//			@RequestParam(value = "paymentChannelUuid") String paymentChannelUuid,
        @RequestParam(value = "accountSide", defaultValue = "-1") int accountSide, Page page) {
        try {
            AccountSide accountSideEnum = EnumUtil.fromOrdinal(AccountSide.class, accountSide);
            PaymentInstitutionName paymentInstitutionName = EnumUtil
                .fromOrdinal(PaymentInstitutionName.class, paymentInstitutionOrdinal);
            if (StringUtils.isBlank(outlierChannelName) || accountSideEnum == null || paymentInstitutionName == null) {
                return jsonViewResolver.errorJsonResult("请求参数错误，请重试");
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("list", bankTransactionLimitSheetHandler
                .getBankLimitPreview(paymentInstitutionName, accountSideEnum, outlierChannelName, null));
//			resultMap.put("size", bankTransactionLimitSheetService.getBankTransactionLimitSheetCountBy(paymentChannelUuid, accountSideEnum));
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#bankLimitPreview# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误,请重试");
        }
    }

    // 通道配置-配置页面 保存配置操作
    @RequestMapping(value = "/config/{paymentChannelUuid}/save", method = RequestMethod.POST)
    public String saveConfigure(
        @PathVariable(value = "paymentChannelUuid") String paymentChannelUuid,
        @RequestParam(value = "paymentChannelName") String paymentChannelName,
        @RequestParam(value = "data") String jsonData) {
        try {
            PaymentChannelConfigure paymentChannelConfigure = JSON.parseObject(jsonData, PaymentChannelConfigure.class);
            boolean isOk = paymentChannelInformationHandler
                .savePaymentChannelConfigure(paymentChannelUuid, paymentChannelName, paymentChannelConfigure);
            return jsonViewResolver.jsonResult(isOk);
        } catch (Exception e) {
            logger.error("#saveConfigure# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("保存失败，请检查数据！");
        }
    }

    // 通道配置-交易明细 页面
    @RequestMapping(value = "/config/transactiondetail/{paymentChannelUuid}", method = RequestMethod.GET)
    public String getTransactionDetail(@PathVariable(value = "paymentChannelUuid") String paymentChannelUuid) {
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            genTransactionDetailDataMap(dataMap, paymentChannelUuid);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#getTransactionDetail# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("交易明细查询失败");
        }
    }

    private void genTransactionDetailDataMap(Map<String, Object> dataMap, String paymentChannelUuid) {
        BigDecimal totalCreditAmount = transferApplicationHandler.getTotalCreditAmount(paymentChannelUuid);
        BigDecimal totalDebitAmount = transferApplicationHandler.getTotalDebitAmount(paymentChannelUuid);
        BigDecimal tradingSuccessRateIn24Hours = transferApplicationService
            .getTradingSuccessRateIn24Hours(paymentChannelUuid);
        PaymentChannelInformation paymentChannelInformation = paymentChannelInformationService
            .getPaymentChannelInformationBy(paymentChannelUuid);
        String paymentChannelName = paymentChannelInformation.getPaymentChannelName();
        Date createTime = paymentChannelInformation.getCreateTime();
        dataMap.put("totalCreditAmount", totalCreditAmount);
        dataMap.put("totalDebitAmount", totalDebitAmount);
        dataMap.put("tradingSuccessRateIn24Hours", tradingSuccessRateIn24Hours);
        dataMap.put("paymentChannelName", paymentChannelName);
        dataMap.put("createTime", createTime);
        dataMap.put("paymentChannelUuid", paymentChannelUuid);
        dataMap.put("everyPage", EVERY_PAGE);
    }

    // 通道配置-交易明细 查询交易额趋势 7天 6月
    @RequestMapping(value = "/config/transactionDetail/searchTradingVolume", method = RequestMethod.GET)
    public String searchTradingVolume(
        @RequestParam(value = "time") String time,
        @RequestParam(value = "paymentChannelUuid") String paymentChannelUuid) {
        try {
            Map<String, Object> resultMap = transferApplicationHandler
                .getCreditAndDebitAmountListBy(time, paymentChannelUuid);
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#searchTradingVolume# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询失败");
        }
    }

    // 通道配置-交易明细 交易记录查询
    @RequestMapping(value = "/config/transactionDetail/search")
    public String searchTransferApplication(
        @ModelAttribute TransferApplicationQueryModel4TransactionDetail financialContractQueryModel,
        Page page) {
        try {
            page = new Page(page.getCurrentPage(), EVERY_PAGE);
            Map<String, Object> resultData = transferApplicationHandler
                .queryTransferApplicationForTransactionDetail(financialContractQueryModel, page);
            return jsonViewResolver.sucJsonResult(resultData, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#searchTransferApplication# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询失败");
        }
    }

    //通道配置-交易明细 导出交易记录查询结果
    @RequestMapping(value = "/config/transactionDetail/export")
    public String exportTransferApplicationResult(
        @ModelAttribute TransferApplicationQueryModel4TransactionDetail financialContractQueryModel,
        HttpServletResponse response) {
        try {
            Map<String, Object> resultData = transferApplicationHandler
                .queryTransferApplicationForTransactionDetail(financialContractQueryModel, null);
            List<TransferApplicationResultModel4TransactionDetail> list = (List<TransferApplicationResultModel4TransactionDetail>) resultData
                .get("list");
            ExcelUtil<TransferApplicationResultModel4TransactionDetail> excelUtil = new ExcelUtil<TransferApplicationResultModel4TransactionDetail>(
                TransferApplicationResultModel4TransactionDetail.class);
            List<String> csvData = excelUtil.exportDatasToCSV(list);
            Map<String, List<String>> csvs = new HashMap<String, List<String>>();
            csvs.put("交易记录", csvData);
            exportZipToClient(response, "交易记录.zip", GlobalSpec.UTF_8, csvs);
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#exportTransferApplicationResult# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("导出失败");
        }
    }

    //通道配置-交易明细  交易记录导出预览
    @RequestMapping(value = "/config/transactionDetail/preview-export")
    public String previewExportTransferApplicationResult(
        @ModelAttribute TransferApplicationQueryModel4TransactionDetail financialContractQueryModel,
        HttpServletResponse response) {
        try {
            Page page = new Page(0, 10);
            Map<String, Object> resultData = transferApplicationHandler
                .queryTransferApplicationForTransactionDetail(financialContractQueryModel, page);
            List<TransferApplicationResultModel4TransactionDetail> list = (List<TransferApplicationResultModel4TransactionDetail>) resultData
                .get("list");

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("list", list);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#previewExportTransferApplicationResult  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("交易记录导出预览失败");
        }
    }


    @RequestMapping(value = "/switch/list/option", method = RequestMethod.GET)
    public String getSwitchListOption() {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("creditStrategyMode", EnumUtil.getKVList(PaymentStrategyMode.class));
            result.put("debitStrategyMode", EnumUtil.getKVList(PaymentStrategyMode.class));
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getSwitchListPage# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 通道切换-列表页 查询操作
    @RequestMapping(value = "/switch/search", method = RequestMethod.GET)
    public String switchSearch(
        @RequestParam(value = "creditStrategyMode", required = false, defaultValue = "-1") int creditStrategyMode,
        @RequestParam(value = "debitStrategyMode", required = false, defaultValue = "-1") int debitStrategyMode,
        @RequestParam(value = "contractName", required = false) String contractName,
        @RequestParam(value = "contractNo", required = false) String contractNo, Page page) {
        try {
            Map<String, Object> dataMap = financialContractConfigHandler
                .queryBy(debitStrategyMode, creditStrategyMode, contractName, contractNo, page);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#switchSearch# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询失败");
        }
    }


    // 通道切换-详情页-获取数据
    @RequestMapping(value = "/switch/getDetailData/{financialContractUuid}", method = RequestMethod.GET)
    public String getSwitchDetail(
        @PathVariable(value = "financialContractUuid") String financialContractUuid) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            FinancialContract fc = financialContractService.getFinancialContractBy(financialContractUuid);
            if (fc == null) {
                return jsonViewResolver.errorJsonResult("系统错误");
            }
            result.put("contractName", fc.getContractName());
            result.put("contractNo", fc.getContractNo());
            Account account = fc.getCapitalAccount();
            result.put("bankNameUnionAccountNo", account.getBankName() + "(" + account.getMarkedAccountNo() + ")");
            result.put("switchDetailInfo", financialContractConfigHandler.getSwitchDetailInfo(financialContractUuid));
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#switchDetailPage# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 通道切换-各银行通道顺序预览
    @RequestMapping(value = "/switch/paymentChannelOrder", method = RequestMethod.GET)
    public String previewPaymentChannelOrderForBanks(
        @RequestParam(value = "financialContractUuid") String financialContractUuid,
        @RequestParam(value = "businessType", defaultValue = "-1") int businessType,
        @RequestParam(value = "accountSide", defaultValue = "-1") int accountSide) {
        try {
            BusinessType type = EnumUtil.fromOrdinal(BusinessType.class, businessType);
            AccountSide Side = EnumUtil.fromOrdinal(AccountSide.class, accountSide);
            List<Map<String, Object>> rtnMaps = financialContractConfigHandler
                .previewPaymentChannelOrderForBanks(financialContractUuid, type, Side);
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("list", rtnMaps);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#previewPaymentChannelOrderForBanks# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误，请重试");
        }
    }

    // 通道切换-设置策略 第二步
    @RequestMapping(value = "/switch/strategy/step/2")
    public String getPaymentChannelList(
        @RequestParam(value = "financialContractUuid") String financialContractUuid,
        @RequestParam(value = "businessType") int businessType,
        @RequestParam(value = "accountSide") int accountSide) {
        try {
            BusinessType businessTypeEnum = EnumUtil.fromOrdinal(BusinessType.class, businessType);
            AccountSide accountSideEnum = EnumUtil.fromOrdinal(AccountSide.class, accountSide);
            if (StringUtils.isEmpty(financialContractUuid) || businessTypeEnum == null || accountSideEnum == null) {
                return jsonViewResolver.errorJsonResult("参数错误，请重试");
            }
            FinancialContractConfig financialContractConfig = financialContractConfigService
                .getFinancialContractConfigBy(financialContractUuid, businessTypeEnum);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("list", financialContractConfigHandler
                .extractAvailableBriefPaymentChannelInfo(accountSideEnum, financialContractConfig));
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#getPaymentChannelList# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取通道列表出错，请重试");
        }
    }

    // 通道切换-设置策略 第三步
    @RequestMapping(value = "/switch/strategy/step/3")
    public String getAllBank(
        @RequestParam(value = "financialContractUuid") String financialContractUuid,
        @RequestParam(value = "businessType") int businessType,
        @RequestParam(value = "accountSide") int accountSide) {
        try {
            BusinessType businessTypeEnum = EnumUtil.fromOrdinal(BusinessType.class, businessType);
            AccountSide accountSideEnum = EnumUtil.fromOrdinal(AccountSide.class, accountSide);
            if (StringUtils.isEmpty(financialContractUuid) || businessTypeEnum == null || accountSideEnum == null) {
                return jsonViewResolver.errorJsonResult("参数错误，请重试");
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("list",
                bankTransactionLimitSheetHandler.getAllBanks(financialContractUuid, businessTypeEnum, accountSideEnum));
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#getPaymentChannelList# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取通道列表出错，请重试");
        }
    }

    // 通道切换-设置策略 银行通道限额预览
    @RequestMapping(value = "/switch/strategy/bankChannelLimitPreview", method = RequestMethod.GET)
    public String bankChannelLimitPreview(
        @RequestParam(value = "paymentChannelUuid") String paymentChannelUuid,
        @RequestParam(value = "accountSide", defaultValue = "-1") int accountSide,
        @RequestParam(value = "bankCode") String bankCode) {
        try {
            AccountSide accountSideEnum = EnumUtil.fromOrdinal(AccountSide.class, accountSide);
            PaymentChannelInformation pci = paymentChannelInformationService
                .getPaymentChannelInformationBy(paymentChannelUuid);
            if (pci == null) {
                return jsonViewResolver.errorJsonResult("请求参数错误，请重试");
            }
            PaymentInstitutionName paymentInstitutionName = pci.getPaymentInstitutionName();
            String outlierChannelName = pci.getOutlierChannelName();
            if (StringUtils.isBlank(outlierChannelName) || accountSideEnum == null || paymentInstitutionName == null) {
                return jsonViewResolver.errorJsonResult("请求参数错误，请重试");
            }
            Map<String, Object> resultMap = bankTransactionLimitSheetHandler
                .getBankChannelLimitPreview(paymentInstitutionName, accountSideEnum, outlierChannelName, bankCode);
            resultMap.put("paymentChannelName", pci.getPaymentChannelName());
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#bankChannelLimitPreview# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误,请重试");
        }
    }

    // 通道切换-切换策略 收款策略列表
    @Deprecated
    @RequestMapping(value = "/switch/strategy/debit", method = RequestMethod.GET)
    public String getDebitStrategyList(
        @RequestParam(value = "financialContractUuid") String financialContractUuid,
        @RequestParam(value = "paymentStrategyMode", required = false, defaultValue = "-1") int paymentStrategyMode,
        @RequestParam(value = "businessType", required = false, defaultValue = "0") int businessType) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            List<Map<String, Object>> dataList = financialContractConfigHandler
                .getDebitStrategyList(financialContractUuid, businessType);
            resultMap.put("list", dataList);
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#getDebitStrategyList# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询收款策略失败");
        }
    }

    // 通道切换-切换策略 付款策略列表
    @Deprecated
    @RequestMapping(value = "/switch/strategy/credit", method = RequestMethod.GET)
    public String getCreditStrategyList(
        @RequestParam(value = "financialContractUuid") String financialContractUuid,
        @RequestParam(value = "paymentStrategyMode", required = false, defaultValue = "-1") int paymentStrategyMode,
        @RequestParam(value = "businessType", required = false, defaultValue = "0") int businessType) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            List<Map<String, Object>> dataList = financialContractConfigHandler
                .getCreditStrategyList(financialContractUuid, businessType);
            resultMap.put("list", dataList);
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#getCreditStrategyList# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询付款策略失败");
        }
    }

    // 通道切换-切换策略 提交按钮  单一策略
    @RequestMapping(value = "/switch/strategy/saveResult", method = RequestMethod.POST)
    public String saveStrtegySwitchResult(@RequestBody String data) {
        // @@RequestBody StrategySwitchResultSubmitModel submitModel
        try {
            StrategySwitchResultSubmitModel submitModel = JSON.parseObject(data, StrategySwitchResultSubmitModel.class);
            boolean isSuc = financialContractConfigHandler.updateConfig(submitModel);
            return jsonViewResolver.jsonResult(isSuc);
        } catch (Exception e) {
            logger.error("#saveStrtegySwitchResult# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("保存失败");
        }
    }

    // 通道切换-切换策略 提交按钮 定时策略
    @RequestMapping(value = "/switch/strategy/timing/saveResult", method = RequestMethod.POST)
    public String saveTimingStrategySwitchResult(@RequestBody String data) {
        try {
            TimingStrategySwitchResultSubmitModel submitModel = JSON
                .parseObject(data, TimingStrategySwitchResultSubmitModel.class);
            boolean isSuc = financialContractConfigHandler.updateTimingStrategyConfig(submitModel);
            return jsonViewResolver.jsonResult(isSuc);
        } catch (Exception e) {
            logger.error("#saveTimingStrategySwitchResult# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("保存失败");
        }
    }

    /**
     * 通道切换 提交通道轮询设置
     */
    @RequestMapping(value = "/switch/strategy/polling", method = RequestMethod.POST)
    public String savePollingConfig(@ModelAttribute ChannelPollingRequestModel model) {
        try {
            if (!model.checkData()){
                return jsonViewResolver.errorJsonResult("请求数据错误");
            }
            boolean isSuc = financialContractConfigHandler.saveChannelPollingConfiguration(model);
            if (!isSuc){
                return jsonViewResolver.errorJsonResult("该业务类型下未配置通道");
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#savePollingConfig# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("保存失败");
        }
    }



    // 效率分析 页面 数据
    @RequestMapping(value = "/efficentanalysis/data", method = RequestMethod.GET)
    public String getEfficentAnalysisPageData() {
        try {
            Map<String, Object> dataMap = transferApplicationHandler.getDataForEfficentAnalysis();
            return jsonViewResolver.sucJsonResult(dataMap);
        } catch (Exception e) {
            logger.error("#getEfficentAnalysisPage# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取效率分析页面数据错误");
        }
    }

    // 效率分析-交易额趋势查询
    @RequestMapping(value = "/efficentanalysis/tradingVolumeTrend")
    public String getTradingVolumeTrend(
        @RequestParam(value = "time") String time) {
        try {
            Map<String, Object> dataMap = transferApplicationHandler.getTradingVolumeTrendIn(time);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#getTradingVolumeTrend# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询失败");
        }
    }

    // 效率分析-24小时通道交易查询
    @RequestMapping(value = "/efficentanalysis/statistics")
    public String getStatistics(@RequestParam(value = "type") int type) {
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<TransferApplicationStatisticsModel> transferApplicationStatisticsModels = transferApplicationHandler
                .getStatisticsDataIn24Hours(type);
            dataMap.put("list", transferApplicationStatisticsModels);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#getStatistics# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询失败");
        }
    }

    // 第三方限额-列表 页面 配置数据
    @RequestMapping(value = "/limitSheet/list/options")
    public String getThirdPartyTransactionLimitListPageOptions() {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("outlierChannelNames", paymentChannelInformationService.getAllOutlierChannelNames());
            result.put("accountSide", EnumUtil.getKVList(AccountSide.class));
//            result.put("gatewayList", EnumUtil.getKVList(PaymentInstitutionName.class));
            result.put("gatewayList", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getThirdPartyTransactionLimitListPageOptions  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取第三方限额配置数据错误");
        }
    }

    // 第三方限额-列表 查询
    @RequestMapping(value = "/limitSheet/search", method = RequestMethod.GET)
    public String searchBankTransactionLimit(
        @ModelAttribute TransactionLimitQueryModel queryModel, Page page) {
        try {
            Map<String, Object> dataMap = bankTransactionLimitSheetHandler
                .searchBankTransactionLimitBy(queryModel, page);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#searchBankTransactionLimit# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 第三方限额-编辑限额表
    @RequestMapping(value = "/limitSheet/update", method = RequestMethod.POST)
    public String updateBankTransactionLimit(
        @ModelAttribute BankTransactionLimitSheetUpdateModel updateModel) {
        try {
            boolean isSuc = bankTransactionLimitSheetService.modifyTransactionLimit(updateModel);
            return jsonViewResolver.jsonResult(isSuc);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("##updateBankTransactionLimit### occur error.");
            return jsonViewResolver.errorJsonResult("更新失败，请重试");
        }
    }

    // 文件上传 操作
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public String uploadFile(
        @RequestParam(value = "paymentInstitutionName", defaultValue = "-1") int paymentInstitutionName,
        @RequestParam(value = "outlierChannelName", required = false) String outlierChannelName,
        @RequestParam(value = "accountSide", defaultValue = "-1") int accountSide,
        HttpServletRequest request) {
        try {
            PaymentInstitutionName paymentInstitutionNameEnum = EnumUtil
                .fromOrdinal(PaymentInstitutionName.class, paymentInstitutionName);
            AccountSide accountSideEnum = EnumUtil.fromOrdinal(AccountSide.class, accountSide);
//			PaymentChannelInformation information = paymentChannelInformationService.getPaymentChannelInformationBy(paymentInstitutionNameEnum, outlierChannelName);
            if (paymentInstitutionNameEnum == null || accountSideEnum == null || outlierChannelName == null) {
                return jsonViewResolver.errorJsonResult("参数错误,请重试");
            }
            if (!ServletFileUpload.isMultipartContent(request)) {// 按照传统方式获取数据
                return jsonViewResolver.errorJsonResult("请选择文件进行上传");
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            if (file == null) {
                return jsonViewResolver.errorJsonResult("请选择文件进行上传");
            }
            String originFilename = file.getOriginalFilename(); // 获得原始文件名  c:\a\b\1.txt OR 1.txt
            String filename = originFilename.substring(originFilename.lastIndexOf(File.separator) + 1);// 获得文件名1.txt
            if (StringUtils.isEmpty(filename)) {
                return jsonViewResolver.errorJsonResult("请选择文件");
            }
            if (!checkFilename(filename)) {
                return jsonViewResolver.errorJsonResult("不支持该文件类型");
            }
            String result = checkAndSaveBanktranscationLimitSheet(file, paymentInstitutionNameEnum, outlierChannelName,
                accountSideEnum);
            if (StringUtils.isNotBlank(result)) {
                return jsonViewResolver.errorJsonResult(result);
            }
            Map<String, Object> resultMap = storeFile(file, savePath, filename);
            if (MapUtils.isEmpty(resultMap)) {
                return jsonViewResolver.errorJsonResult("保存文件出错");
            }
            return jsonViewResolver.sucJsonResult(resultMap);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }
    }

    private String checkAndSaveBanktranscationLimitSheet(MultipartFile file,
        PaymentInstitutionName paymentInstitutionName, String outlierChannelName, AccountSide side) {
        try {
            // 文件解析并检查
            InputStream inputStream = file.getInputStream();
            List<BankTransactionConfigure> configList = new ExcelUtil<>(BankTransactionConfigure.class)
                .importExcel(0, inputStream);
            inputStream.close();
            if (CollectionUtils.isEmpty(configList)) {
                return "银行限额表格式错误，请检查";
            }
            List<String> bcList = new ArrayList<String>();
            for (BankTransactionConfigure configure : configList) {
                String bankCode = configure.getBankCode();
                String bankName = configure.getBankName();
                if (StringUtils.isBlank(bankCode) || StringUtils.isBlank(bankName)) {
                    return "数据缺失";
                }
                if (bcList.contains(bankCode)) {
                    return "银行编号[" + bankCode + "]重复";
                }
                bcList.add(bankCode);
                if (!BankCoreCodeMapSpec.coreBankMap.containsKey(bankCode)) {
                    return "银行编号[" + bankCode + "]不存在，请检查";
                }
                if (!bankName.equals(BankCoreCodeMapSpec.coreBankMap.get(bankCode))) {
                    return "银行编号[" + bankCode + "]与银行名称[" + bankName + "]不对应，请检查";
                }
                if (isAmountNegative(configure.getTransactionLimitPerTranscation())
                    || isAmountNegative(configure.getTransactionLimitPerMonth())
                    || isAmountNegative(configure.getTranscationLimitPerDay())) {
                    return "限额数据不能小于零";
                }
            }

            if (!bankTransactionLimitSheetService.modifyinvalidTime(paymentInstitutionName, outlierChannelName, side)) {
                return "参数错误错误，请重试";
            }

            for (BankTransactionConfigure configure : configList) {
                BankTransactionLimitSheet limitSheet = new BankTransactionLimitSheet();
                limitSheet.initBy(configure);
                limitSheet.setPaymentInstitutionName(paymentInstitutionName);
                limitSheet.setOutlierChannelName(outlierChannelName);
                limitSheet.setAccountSide(side);
//				limitSheet.setPaymentChannelInformationUuid(information.getPaymentChannelUuid());
                bankTransactionLimitSheetService.save(limitSheet);
            }
            return null;
        } catch (Exception e) {
            logger.error("###checkAndSaveBanktranscationLimitSheet### occur error.");
            e.printStackTrace();
            return "未知错误，请重试";
        }
    }

    private boolean isAmountNegative(String amountStr) {
        return amountStr != null && amountStr.startsWith("-");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> storeFile(MultipartFile file, String path, String filename) {
        try {

            String saveFilename = genSaveFilename(filename);
            File fileTmp = new File(savePath);
            if (!fileTmp.exists() && !fileTmp.isDirectory()) { // 目录不存在，需要创建
                fileTmp.mkdir();  //创建目录
            }
            File source = new File((path + saveFilename));
            file.transferTo(source);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("fileName", filename);
            resultMap.put("fileKey", saveFilename);
            return resultMap;
        } catch (Exception e) {
            logger.error("###storeFile### occur error.");
            e.printStackTrace();
            return MapUtils.EMPTY_MAP;
        }
    }

    private String genSaveFilename(String filename) {
        return UUID.randomUUID().toString().replace("-", "") + "_" + filename;
    }

    private boolean checkFilename(String filename) {
        List<String> valaidFileformat = Arrays.asList("xls", "xlsx");
        //获得扩展名
        int dotFlag = filename.lastIndexOf(".");
        String fileExtName = dotFlag == -1 ? null : filename.substring(dotFlag + 1).toLowerCase();
        return valaidFileformat.contains(fileExtName);
    }


    // 文件删除 操作
    @RequestMapping(value = "/file/delete", method = RequestMethod.POST)
    public String deleteFile(
        @RequestParam(value = "paymentInstitutionName", defaultValue = "-1") int paymentInstitutionName,
        @RequestParam(value = "outlierChannelName") String outlierChannelName,
        @RequestParam(value = "accountSide", defaultValue = "-1") int accountSide) {
        try {
            PaymentInstitutionName paymentInstitutionNameEnum = EnumUtil
                .fromOrdinal(PaymentInstitutionName.class, paymentInstitutionName);
            AccountSide accountSideEnum = EnumUtil.fromOrdinal(AccountSide.class, accountSide);
            if (paymentInstitutionNameEnum == null || accountSideEnum == null || outlierChannelName == null) {
                return jsonViewResolver.errorJsonResult("参数错误,请重试");
            }
            if (!bankTransactionLimitSheetService
                .modifyinvalidTime(paymentInstitutionNameEnum, outlierChannelName, accountSideEnum)) {
                return jsonViewResolver.errorJsonResult("参数错误,请重试");
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("###deleteFile### occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("参数错误,请重试");
        }
    }

    // 文件下载 操作
    @RequestMapping(value = "/file/download", method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value = "fileKey", defaultValue = "0") String fileKey,
        HttpServletRequest request, HttpServletResponse response) {
        try {
            if ("1".equals(fileKey)) {
                fileKey = "16d753ab90114b00bb2a275f775f863f_银行限额模板.xls";
            } else if ("2".equals(fileKey)) {
                fileKey = "银行编号列表.xls";
            }
            File file = new File(savePath + fileKey);
            if (!file.exists()) {
                logger.error("Downloading file error. FilePath: " + savePath + fileKey);
                return;
            }
            //处理文件名
            String realname = fileKey.substring(fileKey.indexOf("_") + 1);
            DownloadUtils.flushFileIntoHttp(realname, fileKey, savePath, response);
        } catch (Exception e) {
            logger.error("#downloadFile# occur error.");
            e.printStackTrace();
        }
    }

    // 文件下载 操作
    @Deprecated
    @RequestMapping(value = "/file/download/bank", method = RequestMethod.GET)
    public void downloadBankList(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = "银行编号列表.xls";
            File file = new File(savePath + fileName);
            if (!file.exists()) {
                logger.error("Downloading file error. FilePath: " + savePath + fileName);
                return;
            }
            DownloadUtils.flushFileIntoHttp(fileName, fileName, savePath, response);
        } catch (Exception e) {
            logger.error("#downloadFile# occur error.");
            e.printStackTrace();
        }
    }
}
