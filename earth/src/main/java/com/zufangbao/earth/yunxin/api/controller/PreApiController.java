package com.zufangbao.earth.yunxin.api.controller;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.matryoshka.service.ProductCategoryService;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.repurchase.RepurchaseDetails;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.utils.csv.CsvUtils;
import com.zufangbao.sun.yunxin.entity.files.FileType;
import com.zufangbao.sun.yunxin.exception.FileUnsupportedException;
import com.zufangbao.sun.yunxin.service.files.FileRepositoryService;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.suidifu.matryoshka.snapshot.SandboxDataSetSpec.*;
import static com.zufangbao.earth.yunxin.api.controller.PreApiControllerSpec.*;
import static com.zufangbao.earth.yunxin.api.swagger.SwaggerSpec.*;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FILE_FORMAT_ERROR;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FILE_UNSUPPORTED;

//import com.zufangbao.sun.entity.perInterface.snapshots.PaymentPlanSnapshot;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSet;

/**
 * 前置接口API
 * @author louguanyang on 2017/4/24.
 */
@RequestMapping(PRE_API)
@Controller
@Api(value = "五维金融贷后接口V3.0", description = "五维金融贷后接口V3.0")
public class PreApiController extends BaseApiController {

    private static final Log LOGGER = LogFactory.getLog(Api_V2_Controller.class);

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    @Qualifier("productCategoryCacheHandler")
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    @Qualifier("delayTaskConfigCacheHandler")
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    @Qualifier("delayProcessingTaskCacheHandler")
    private DelayProcessingTaskCacheHandler delayProcessingTaskHandler;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    private FileRepositoryService fileRepositoryService;
    @Value("#{config['uploadFilePath']}")
    private String uploadFilePath = "";

    @Autowired
    private ContractApiHandler contractApiHandler;

    @RequestMapping(value = PRE_URL_MODIFY_REPAYMENT_PLAN, method = RequestMethod.POST)
    @ApiOperation(value = "变更还款计划前置接口", notes = "变更还款计划前置接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CHANNEL_CODE, value = CHANNEL_CODE_VALUE, paramType = PARAM_TYPE_PATH, dataType = DATA_TYPE_STRING,
                    defaultValue = DEFAULT_CHANNEL_CODE),
            @ApiImplicitParam(name = SERVICE_CODE, value = SERVICE_CODE_VALUE, paramType = PARAM_TYPE_PATH, dataType = DATA_TYPE_STRING,
                    defaultValue = SXH_DEFERRED_PAYMENT, allowableValues = SXH_ALLOWABLE_VALUES),
            @ApiImplicitParam(name = REQUEST_NO, value = REQUEST_NO_VALUE, required = true, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = UNIQUE_ID, value = UNIQUE_ID_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = CONTRACT_NO, value = CONTRACT_NO_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = FINANCIAL_CONTRACT_NO, value = FINANCIAL_CONTRACT_NO_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = REQUEST_DATA, value = REQUEST_DATA_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = MODIFY_REPAYMENT_PLAN_TYPE, value = MODIFY_REPAYMENT_PLAN_TYPE_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_INTEGER)
    })
    public @ResponseBody
    String modifyRepaymentPlan(HttpServletRequest request, HttpServletResponse response) {
        try {
            HashMap<String, String> allParameters = getAllParameters(request);
            String request_url = request.getRequestURL().toString();
            String[] split = request_url.split(PRE_API);
            String url = split[1];
            ProductCategory productCategory = productCategoryCacheHandler.get(url, true);
            if (null == productCategory) {
                return signErrorResult(response, new ApiException(ApiResponseCode.API_NOT_FOUND));
            }
            CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);

            if (services == null) {
                LOGGER.warn("CustomizeServices not found ");
                return signErrorResult(response, new ApiException(ApiResponseCode.API_NOT_FOUND));
            }
            String uniqueId = allParameters.getOrDefault(UNIQUE_ID, "");
            String contractNo = allParameters.getOrDefault(CONTRACT_NO, "");
            String financialContractNo = allParameters.getOrDefault(FINANCIAL_CONTRACT_NO, "");

            /*
            * 请求参数新增“信托产品代码”选填字段，当该字段为非空时，做如下校验：
            * a.校验信托产品代码是否正确存在，若错误或不存在，返回失败+失败原因；
            * b.校验贷款合同唯一编号/贷款合同与产品代码是否存在映射关系，若不存在映射关系，返回失败+失败原因
            */
            if (StringUtils.isNotEmpty(financialContractNo)) {
                Contract contract = contractApiHandler.getContractBy(uniqueId, contractNo);
                contractApiHandler.checkAndReturnFinancialContract(financialContractNo, contract);
            }
            SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(uniqueId, contractNo);
            if (sandboxDataSet == null) {
                LOGGER.warn("modifyRepaymentPlan, sandboxDataSet is null!");
                return signErrorResult(response, new ApiException(ApiResponseCode.INVALID_PARAMS));
            }
            if (null == sandboxDataSet.getContractSnapshot()) {
                LOGGER.warn("modifyRepaymentPlan, getContractSnapshot is null!");
                return signErrorResult(response, new ApiException(ApiResponseCode.CONTRACT_NOT_EXIST));
            }

            HashMap<String, String> postRequest = new HashMap<>();

            boolean evaluate = services.evaluate(sandboxDataSetHandler, allParameters, postRequest, LOGGER);

            if (!evaluate) {
                String errorMsg = postRequest.getOrDefault("errorMsg", "");
                LOGGER.warn("modifyRepaymentPlan, 热编译代码执行失败, " + errorMsg);
                return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, errorMsg);
            }

            String location = split[0] + productCategory.getPostProcessInterfaceUrl();
            Result post_result = post(response, postRequest, location);
            Integer resultCode = Integer.valueOf(post_result.getCode());
            if (resultCode != ApiResponseCode.SUCCESS) {
                String errorMsg = post_result.getMessage();
                LOGGER.warn("modifyRepaymentPlan fail, " + errorMsg);
                return signErrorResult(response, resultCode, errorMsg);
            }
            Map<String, Object> data = post_result.getData();
            String delayTaskConfigUuid = productCategory.getDelayTaskConfigUuid();
            DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler
                    .getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
            if (null != delayTaskServices) {
                HashMap<String, Object> inputMap = getInputMap(sandboxDataSet, data);
                inputMap.put(CONFING_UUID, delayTaskConfigUuid);
                Map<String, Object> resultMap = new HashMap<>();
                boolean evaluate_result = delayTaskServices.evaluate(post_result, delayProcessingTaskHandler, sandboxDataSetHandler, inputMap, resultMap, LOGGER);
            } else {
                LOGGER.warn("modifyRepaymentPlan, delayTaskServices is null");
            }
            return signSucResult(response, data);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("#modifyRepaymentPlan occur error !", e);
            // 返回结果信息为失败，出现异常
            return signErrorResult(response, e);
        }
    }

    private HashMap<String, Object> getInputMap(SandboxDataSet sandboxDataSet, Map<String, Object> data) {
        HashMap<String, Object> inputMap = new HashMap<>();
        List<String> repaymentPlanNoList = (List<String>) data.getOrDefault(REPAYMENT_PLAN_NO_LIST, Collections.emptyList());
        String financialContractUuid = sandboxDataSet.getFinancialContractUuid();
        String contractUuid = sandboxDataSet.getContractUuid();
        inputMap.put(FINANCIAL_CONTRACT_UUID, financialContractUuid);
        inputMap.put(CONTRACT_UUID, contractUuid);
        inputMap.put(REPAYMENT_PLAN_NO_LIST, repaymentPlanNoList);
        List<String> originalRepaymentPlanNoList = sandboxDataSet.getPaymentPlanSnapshotList().stream().map(PaymentPlanSnapshot::getSingleLoanContractNo).collect
                (Collectors.toList());
        inputMap.put(ORIGINAL_REPAYMENT_PLAN_NO_LIST, originalRepaymentPlanNoList);

        return inputMap;
    }

    @RequestMapping(value = PRE_URL_REPURCHASE, method = RequestMethod.POST)
    @ApiOperation(value = "申请回购前置接口", notes = "申请回购前置接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CHANNEL_CODE, value = CHANNEL_CODE_VALUE, paramType = PARAM_TYPE_PATH, dataType =
                    DATA_TYPE_STRING,
                    defaultValue = DEFAULT_CHANNEL_CODE),
            @ApiImplicitParam(name = SERVICE_CODE, value = SERVICE_CODE_VALUE, paramType = PARAM_TYPE_PATH, dataType = DATA_TYPE_STRING,
                    defaultValue = "repurchase",
                    allowableValues = "repurchase"),
            @ApiImplicitParam(name = REQUEST_NO, value = REQUEST_NO_VALUE, required = true, paramType = PARAM_TYPE_QUERY, dataType =
                    DATA_TYPE_STRING),
            @ApiImplicitParam(name = "batchNo", value = "批次号", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = "transactionType", value = "交易类型", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_INTEGER),
            @ApiImplicitParam(name = "financialContractNo", value = "信托合同代码", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = "repurchaseDetail", value = "回购详情", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = "reviewer", value = "审核人", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = "reviewTimeString", value = "审核时间", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING)
    })
    public @ResponseBody
    String applyForRepurchase(HttpServletRequest request, HttpServletResponse response) {
        try {
            HashMap<String, String> allParameters = getAllParameters(request);
            String request_url = request.getRequestURL().toString();
            String[] split = request_url.split(PRE_API);
            String url = split[1];
            ProductCategory productCategory = productCategoryCacheHandler.get(url, true);
            if (null == productCategory) {
                return signErrorResult(response, new ApiException(ApiResponseCode.API_NOT_FOUND));
            }
            CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
            if (services == null) {
                LOGGER.warn("CustomizeServices not found ");
                return signErrorResult(response, new ApiException(ApiResponseCode.API_NOT_FOUND));
            }

            // 转发请求
            String location = split[0] + productCategory.getPostProcessInterfaceUrl();
            Result post_result = post(response, allParameters, location);

            Integer resultCode = Integer.valueOf(post_result.getCode());
            if (resultCode != ApiResponseCode.SUCCESS) {
                return signErrorResult(response, resultCode, post_result.getMessage());
            }

            // 后置任务

            Integer transactionType = Integer.valueOf(allParameters.get("transactionType"));
            String delayTaskConfigUuid = productCategory.getDelayTaskConfigUuid();
            DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
            if (null != delayTaskServices && transactionType == RepurchaseCommandModel.SUBMIT) {
                String repurchaseDetail = allParameters.get("transactionType");
                List<RepurchaseDetails> repurchaseDetailModels = JsonUtils.parseArray(repurchaseDetail, RepurchaseDetails.class);
                if (repurchaseDetailModels == null) {
                    return signErrorResult(response, new ApiException(ApiResponseCode.INVALID_PARAMS));
                }
                for (RepurchaseDetails detail : repurchaseDetailModels) {
                    String uniqueId = detail.getUniqueId();
                    String contractNo = detail.getContractNo();
                    Map<String, Object> inputMap = new HashMap<String, Object>();
                    inputMap.put(UNIQUE_ID, uniqueId);
                    inputMap.put(CONTRACT_NO, contractNo);
                    inputMap.put("taskConfigUuid", delayTaskConfigUuid);
                    Map<String, Object> resultMap = new HashMap<>();
                    boolean evaluate_result = delayTaskServices.evaluate(post_result, delayProcessingTaskHandler, sandboxDataSetHandler, inputMap, resultMap, LOGGER);
                }
            }
            return signSucResult(response);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("#applyForRepurchase occur error !", e);
            // 返回结果信息为失败，出现异常
            return signErrorResult(response, e);
        }
    }

    @RequestMapping(value = "/update-cache", method = RequestMethod.POST)
    public @ResponseBody
    String updateCache(HttpServletResponse response, String productCategoryUuid) {
        try {
            ProductCategory productCategory = productCategoryService.get_by_uuid(productCategoryUuid);
            if (productCategory != null) {
                productCategoryCacheHandler.clearByUrl(productCategory.getPreProcessInterfaceUrl());
            }
            return signSucResult(response);
        } catch (Exception e) {
            e.printStackTrace();
            return signErrorResult(response, e);
        }
    }

    @RequestMapping(value = PRE_URL_UPLOAD_FILE, method = RequestMethod.POST)
    @ApiOperation(value = "文件上传前置接口", notes = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = PRODUCT_CODE, value = PRODUCT_CODE_VALUE, paramType = PARAM_TYPE_PATH, dataType = DATA_TYPE_STRING,
                    defaultValue = HA0100),
            @ApiImplicitParam(name = SERVICE_CODE, value = SERVICE_CODE_VALUE, paramType = PARAM_TYPE_PATH, dataType = DATA_TYPE_STRING,
                    defaultValue = "10001", allowableValues = "10001,10002"),
            @ApiImplicitParam(name = REQUEST_NO, value = REQUEST_NO_VALUE, required = true, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = TRADE_TIME, value = TRADE_TIME_VALUE, paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING)
    })
    public @ResponseBody
    String upload(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
            HashMap<String, String> allParameters = getAllParameters(request);
            Date tradeTime = getTradeTime(allParameters);

            String request_url = request.getRequestURL().toString();
            String[] split = request_url.split(PRE_API);
            String url = split[1];
            ProductCategory productCategory = productCategoryCacheHandler.get(url, true);
            if (null == productCategory) {
                return signErrorResult(response, new ApiException(ApiResponseCode.API_NOT_FOUND));
            }
            String fileTypeCode = productCategory.getProductLv3Code();
            FileType fileType = FileType.fromTypeCode(fileTypeCode);
            if (fileType == null) {
                LOGGER.warn("ProductCategory 表 ProductLv3Code 配置错误！");
                return signErrorResult(response, new ApiException(ApiResponseCode.UNSUPPORTED_FILE_TYPE));
            }

            //信托产品代码
            String productCode = productCategory.getProductLv1Code();

            List<String> filePathList = new ArrayList<>();
            Iterator<String> fileNames = fileRequest.getFileNames();
            while (fileNames.hasNext()) {
                String fileName = fileNames.next();
                List<MultipartFile> files = fileRequest.getFiles(fileName);
                for (MultipartFile multipartFile : files) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    if (!FilenameUtils.isExtension(originalFilename, fileType.getSupportExtensions())) {
                        LOGGER.error("FileUnsupportedException, ");
                        throw new FileUnsupportedException();
                    }

                    File temp = FileUtils.saveUploadFile(uploadFilePath, multipartFile);
                    if (temp == null) {
                        LOGGER.warn("saveUploadFile fail, temp file is null");
                        continue;
                    }

                    //csv file check first line
                    if (FilenameUtils.isExtension(originalFilename, FilenameUtils.CSV_EXTENSIONS)) {
                        boolean isSuccess = CsvUtils.checkFirstLine(temp, fileType);
                        if (!isSuccess) {
                            throw new ApiException(FILE_FORMAT_ERROR);
                        }
                    }

                    filePathList.add(temp.getPath());
                    LOGGER.info("save file to service success, file path:" + temp.getPath());
                }
            }
            fileRepositoryService.saveFileRepository(filePathList, productCode, fileTypeCode, tradeTime);

            return signSucResult(response);
        } catch (FileUnsupportedException e) {
            e.printStackTrace();
            LOGGER.error("upload fail, 文件类型错误" + e.getMessage());
            return signErrorResult(response, new ApiException(FILE_UNSUPPORTED));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("upload fail, message:" + e.getMessage());
            return signErrorResult(response, e);
        }
    }

    private Date getTradeTime(HashMap<String, String> allParameters) {
        String now_time = DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT);
        String tradeTimeStr = allParameters.getOrDefault("tradeTime", now_time);
        return DateUtils.parseDate(tradeTimeStr, DateUtils.LONG_DATE_FORMAT);
    }

}
