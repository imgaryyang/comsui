package com.suidifu.morganstanley.handler.impl;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V2;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_FILESUB_REPAYMENTORDER;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_OVER_DUE_FEE;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_REPAYMENT_PLAN;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MUTABLE_FEE;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorCode4File.ERROR_CODE_BATCH_NO_EMPTY;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorCode4File.ERROR_CODE_BUILD_BACKUP_FILE_FAIL;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorCode4File.ERROR_CODE_FILE_NAME_RULES;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorCode4File.ERROR_CODE_SIGNAL_FILE_ERROR;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorCode4File.ERROR_CODE_WAITING_BATCH;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorMsg4File.ERROR_MSG_BUILD_BACKUP_FILE_FAIL;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorMsg4File.ERROR_MSG_FILE_NAME_RULES;
import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorMsg4File.ERROR_MSG_SIGNAL_FILE_ERROR;
import static com.zufangbao.sun.entity.perInterface.ProductLv1Code.HA0100;
import static com.zufangbao.sun.entity.perInterface.ProductLv1Code.HG4100;
import static com.zufangbao.sun.entity.perInterface.ProductLv1Code.I05600;
import static com.zufangbao.sun.entity.perInterface.ProductLv1Code.ZHONG_HANG;
import static com.zufangbao.sun.utils.FilenameUtils.SIGNAL_FILE_EXTENSIONS;
import static com.zufangbao.sun.utils.FilenameUtils.SIGNAL_FILE_SPLIT;
import static com.zufangbao.sun.yunxin.entity.files.FileExecuteStatus.EXECUTED;
import static com.zufangbao.sun.yunxin.entity.files.FileExecuteStatus.UNEXECUTE;
import static com.zufangbao.sun.yunxin.entity.files.FileProcessStatus.PROCESSED;
import static com.zufangbao.sun.yunxin.entity.files.FileSendStatus.ABANDON;
import static com.zufangbao.sun.yunxin.entity.files.FileSendStatus.SEND;
import static com.zufangbao.sun.yunxin.entity.files.FileSendStatus.UNSEND;
import static com.zufangbao.wellsfargo.api.util.ApiConstant.PARAMS_MER_ID;
import static com.zufangbao.wellsfargo.api.util.ApiConstant.PARAMS_SECRET;
import static com.zufangbao.wellsfargo.api.util.ApiConstant.PARAMS_SIGN;
import static java.io.File.separator;

import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.productCategory.Product3lvl;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTask;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustProperties;
import com.suidifu.morganstanley.configuration.bean.zhonghang.ZhongHangProperties;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.exception.MorganStanleyRuntimeException;
import com.suidifu.morganstanley.handler.BatchProcessHandler;
import com.suidifu.morganstanley.handler.FileProcessHandler;
import com.suidifu.morganstanley.servers.FileRepositoryRedisPersistence;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.morganstanley.PreApiControllerSpec;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeDetail;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.perInterface.ProductLv2Code;
import com.zufangbao.sun.entity.perInterface.ProductLv3Code;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.SpringContextUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCacheSpec;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeDetail;
import com.zufangbao.sun.yunxin.entity.files.FileExecuteStatus;
import com.zufangbao.sun.yunxin.entity.files.FileNameRules;
import com.zufangbao.sun.yunxin.entity.files.FileProcessStatus;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileSendStatus;
import com.zufangbao.sun.yunxin.entity.files.FileSignal;
import com.zufangbao.sun.yunxin.entity.files.FileStatus;
import com.zufangbao.sun.yunxin.entity.files.FileSubRepaymentOrder;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_ModifyRepaymentPlan;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_ModifyRepaymentPlan_CSV;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_ModifyRepaymentPlan_ZHXT;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_MutableFee_CSV;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_OverdueFee;
import com.zufangbao.sun.yunxin.entity.files.FileType;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.entity.model.api.modify.SPDBankRepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.entity.repayment.Base_RepaymentPlanDetail;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentPlanDetail_SPDBank;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.files.FileRepositoryService;
import com.zufangbao.wellsfargo.yunxin.cache.DictionaryCache;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


/**
 * 文件处理Handler
 * @author louguanyang on 2017/5/23.
 */
@Component("FileProcessHandler")
public class FileProcessHandlerImpl implements FileProcessHandler {

	private static final String BUSINESS_ID = "businessId";
	private static final String BUSINESS_TYPE = "businessType";
	private static final String JOB_DIR = "jobDir";

	private static final ArrayList<String> NO_NEED_DEAL_PRODUCT_CODE_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -3558491157582273197L;
        {
            clear();
            add(HA0100.getCode());
            add(ZHONG_HANG.getCode());
            add(HG4100.getCode());
            add(I05600.getCode());
        }
    };

	private static final Map<String, String> BATCH_PROCESS_HANDLER_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = -3558491157582273197L;
        {
            putIfAbsent(FileType.MODIFY_REPAYMENT.getTypeCode(), "ModifyRepaymentPlanBatchProcessHandlerImpl");
            putIfAbsent(FileType.OVERDUE_FEE.getTypeCode(), "OverdueFeeBatchProcessHandlerImpl");
            putIfAbsent(FileType.REPAYMENT_ORDER.getTypeCode(), "repaymentOrderFileSubHandlerImpl");
        }
    };
    private final static Log LOGGER = LogFactory.getLog(FileProcessHandlerImpl.class);
	@Resource
    private CacheManager cacheManager;
    @Resource
    private ContractService contractService;
    @Resource
	private ProductCategoryCacheHandler productCategoryCacheHandler;
	@Resource
	private YntrustProperties yntrustProperties;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private FileRepositoryService fileRepositoryService;
	@Resource
	@Qualifier("FileRepositoryRedisPersistence")
    private FileRepositoryRedisPersistence fileRepositoryRedisPersistence;

    @Override
    public NotifyApplication build_BQJR_modify_repaymentPlan_notifyApplication(String businessType, FileRepository file, String bizId,
                                                                               List<FileTmp_ModifyRepaymentPlan_CSV> sameTradeNoList,
                                                                               String privateKey, String groupNameForModifyPlan) {
        try {
            List<FileTmp_ModifyRepaymentPlan_CSV> sortedList = sortedList(sameTradeNoList);
	        String requestUrl = getModifyRepaymentUrl(sortedList);
	        HashMap<String, String> requestParameters = buildModifyRequestMap(sortedList);
	        HashMap<String, String> headers = buildHeaders(requestParameters, privateKey);
	        HashMap<String, String> businessDataMap = buildBusinessDataMap(file.getPath(), businessType, bizId);
            return getNotifyApplication(requestUrl, headers, requestParameters, businessDataMap, groupNameForModifyPlan);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("build_BQJR_modify_repaymentPlan_notifyApplication fail, tradeNo:" + bizId + ", error msg:" + e.getMessage());
            return null;
        }
    }

    private String getConsistenceHashPolicy(HashMap<String, String> requestParameters) {
        String uniqueId = requestParameters.getOrDefault(ApiConstant.UNIQUE_ID, StringUtils.EMPTY);
        String contractNo = requestParameters.getOrDefault(ApiConstant.CONTRACT_NO, StringUtils.EMPTY);
        Contract contract = contractService.getContractBy(uniqueId, contractNo);
        if (contract == null) {
            return StringUtils.EMPTY;
        }
        return contract.getUuid();
    }

    @Override
    public NotifyApplication build_mutable_NotifyApplication(String businessType, FileRepository file, String bizId, List<FileTmp_MutableFee_CSV> sameTradeNoList,
                                                             String privateKey, String groupNameForModifyPlan) {
        try {
            String requestUrl = buildYntrustMorganStanleyUrl(URL_MUTABLE_FEE, URL_API_V3);
            HashMap<String, String> requestParameters = buildMutableRequestMap(sameTradeNoList);
	        HashMap<String, String> headers = buildHeaders(requestParameters, privateKey);
	        HashMap<String, String> businessDataMap = buildBusinessDataMap(file.getPath(), businessType, bizId);
            return getNotifyApplication(requestUrl, headers, requestParameters, businessDataMap, groupNameForModifyPlan);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("build_mutable_NotifyApplication fail, tradeNo:" + bizId + ", error msg:" + e.getMessage());
            return null;
        }
    }

    @Override
    public NotifyApplication build_ZHXT_notifyApplication(String businessType, FileRepository file, String bizId, FileTmp_ModifyRepaymentPlan_ZHXT fileTmp,
                                                          String privateKey, String groupNameForModifyPlan) {
        try {
            String requestUrl = getZhonghangModifyRepaymentUrl(fileTmp);
            HashMap<String, String> requestParameters = buildZHXTModifyRequestMap(fileTmp);
	        HashMap<String, String> headers = buildHeaders(requestParameters, privateKey);
	        HashMap<String, String> businessDataMap = buildBusinessDataMap(file.getPath(), businessType, bizId);
            return getNotifyApplication(requestUrl, headers, requestParameters, businessDataMap, groupNameForModifyPlan);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("build_ZHXT_notifyApplication fail, tradeNo:" + bizId + ", error msg:" + e.getMessage());
            return null;
        }
    }

    /**
     * 变更还款计划根据变更日期重新排序
     *
     * @param sameTradeNoList   同一批次还款计划
     * @return
     */
    private List<FileTmp_ModifyRepaymentPlan_CSV> sortedList(List<FileTmp_ModifyRepaymentPlan_CSV> sameTradeNoList) {
        return sameTradeNoList.stream().sorted(Comparator.comparing(FileTmp_ModifyRepaymentPlan_CSV::getAssetRecycleDateValue)).collect(Collectors.toList());
    }

    /**
     * 创建浮动费用请求参数
     *
     * @param sameTradeNoList   同一批次还款计划
     * @return
     */
    private HashMap<String, String> buildMutableRequestMap(List<FileTmp_MutableFee_CSV> sameTradeNoList) {
        if (CollectionUtils.isEmpty(sameTradeNoList)) {
	        return new HashMap<>(0);
        }
	    HashMap<String, String> requestParameters = new HashMap<>();
	    FileTmp_MutableFee_CSV firstLine = sameTradeNoList.get(0);
	    requestParameters.put(ApiConstant.REQUEST_NO, firstLine.getTradeNo());
	    requestParameters.put(ApiConstant.FINANCIAL_PRODUCT_CODE, firstLine.getProductCode());
	    requestParameters.put(ApiConstant.UNIQUE_ID, firstLine.getUniqueId());
	    requestParameters.put(ApiConstant.CONTRACT_NO, firstLine.getContractNo());
	    requestParameters.put(ApiConstant.REPAYMENT_PLAN_NO, firstLine.getRepaymentPlanNo());

        List<MutableFeeDetail> modelList = new ArrayList<>();
        for (FileTmp_MutableFee_CSV line : sameTradeNoList) {
            MutableFeeDetail model = line.transfer();
            if (model != null) {
                modelList.add(model);
            }
        }
	    requestParameters.put(ApiConstant.DETAILS, JsonUtils.toJSONString(modelList));

	    requestParameters.put(ApiConstant.REASON_CODE, firstLine.getReasonCode());
	    requestParameters.put(ApiConstant.APPROVER, firstLine.getApprover());
	    requestParameters.put(ApiConstant.APPROVED_TIME, firstLine.getApprovedTime());
	    requestParameters.put(ApiConstant.COMMENT, firstLine.getComment());
	    return requestParameters;
    }

	private String getModifyRepaymentUrl(List<FileTmp_ModifyRepaymentPlan_CSV> sameTradeNoList) {
		if (CollectionUtils.isEmpty(sameTradeNoList)) {
            return null;
        }
        try {
            FileTmp_ModifyRepaymentPlan_CSV firstLine = sameTradeNoList.get(0);
            String requestReason = firstLine.getRequestReason();
            ProductCategory productCategory = productCategoryCacheHandler.get(ProductLv2Code.MODIFY_REPAYMENT_PLAN.getCode(), requestReason);
            String preProcessInterfaceUrl = productCategory.getPreProcessInterfaceUrl();
            return buildYntrustMorganStanleyUrl(preProcessInterfaceUrl, PreApiControllerSpec.PRE_API);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getZhonghangModifyRepaymentUrl(FileTmp_ModifyRepaymentPlan_ZHXT fileTmp) {
        try {
            if (fileTmp == null) {
                return null;
            }
            Product3lvl product3lvl = new Product3lvl(ZHONG_HANG.getCode(), ProductLv2Code.MODIFY_REPAYMENT_PLAN.getCode(), ProductLv3Code.STANDARD.getCode());
            ProductCategory productCategory = productCategoryCacheHandler.get(product3lvl);
            String preProcessInterfaceUrl = productCategory.getPreProcessInterfaceUrl();
            return buildZhonghangMorganStanleyUrl(preProcessInterfaceUrl, PreApiControllerSpec.PRE_API);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Resource
    private YntrustFileTask yntrustFileTask;

	private HashMap<String, String> buildHeaders(HashMap<String, String> requestParameters, String privateKey) {
		HashMap<String, String> headers = new HashMap<>(4);
        headers.put(PARAMS_MER_ID, yntrustFileTask.getMerId());
        headers.put(PARAMS_SECRET, yntrustFileTask.getSecret());
        String content = ApiSignUtils.getSignCheckContent(requestParameters);
        String sign = ApiSignUtils.rsaSign(content, privateKey);
        headers.put(PARAMS_SIGN, sign);
        return headers;
    }

    private HashMap<String, String> buildBusinessDataMap(String filePath, String businessType, String businessId) {
        HashMap<String, String> businessDataMap = new HashMap<>();
        businessDataMap.put(BUSINESS_ID, businessId);
        businessDataMap.put(BUSINESS_TYPE, businessType);
        businessDataMap.put(JOB_DIR, filePath);
        return businessDataMap;
    }

    private HashMap<String, String> buildBusinessDataMap(FileRepository fileRepository, String tradeNo) {
        HashMap<String, String> businessDataMap = new HashMap<>();
        businessDataMap.put(BUSINESS_ID, fileRepository.buildBizId(tradeNo));
        businessDataMap.put(BUSINESS_TYPE, fileRepository.getBusinessType());
        businessDataMap.put(JOB_DIR, fileRepository.getPath());
        return businessDataMap;
    }

	private HashMap<String, String> buildModifyRequestMap(List<FileTmp_ModifyRepaymentPlan_CSV> sameTradeNoList) {
		if (CollectionUtils.isEmpty(sameTradeNoList)) {
	        return new HashMap<>(0);
		}
		HashMap<String, String> requestParameters = new HashMap<>();
		FileTmp_ModifyRepaymentPlan_CSV firstLine = sameTradeNoList.get(0);
		requestParameters.put(ApiConstant.REQUEST_NO, firstLine.getTradeNo());
		requestParameters.put(ApiConstant.UNIQUE_ID, firstLine.getUniqueId());
		requestParameters.put(ApiConstant.CONTRACT_NO, firstLine.getContractNo());
		List<RepaymentPlanModifyRequestDataModel> modelList = new ArrayList<>();
        for (FileTmp_ModifyRepaymentPlan_CSV line : sameTradeNoList) {
            RepaymentPlanModifyRequestDataModel model = line.transfer();
            if (model != null) {
                modelList.add(model);
            }
        }
		requestParameters.put(ApiConstant.REQUEST_DATA, JsonUtils.toJSONString(modelList));
		return requestParameters;
	}

    private HashMap<String, String> buildZHXTModifyRequestMap(FileTmp_ModifyRepaymentPlan_ZHXT fileTmp) {
        if (null == fileTmp) {
            return new HashMap<>();
        }
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(ApiConstant.REQUEST_NO, fileTmp.getTradeNo());
        requestParameters.put(ApiConstant.UNIQUE_ID, fileTmp.getUniqueId());
        requestParameters.put(ApiConstant.CONTRACT_NO, fileTmp.getContractNo());
        requestParameters.put(ApiConstant.REQUEST_REASON, fileTmp.getRequestReason());
        String assetType = fileTmp.getAssetType();
        List<Base_RepaymentPlanDetail> repaymentPlanDetails = fileTmp.getRepaymentPlanDetails();
        List<RepaymentPlanModifyRequestDataModel> models = new ArrayList<>();
        for (Base_RepaymentPlanDetail planDetail : repaymentPlanDetails) {
            RepaymentPlanModifyRequestDataModel model = planDetail.parse(assetType);
            if (model != null) {
                models.add(model);
            }
        }
        requestParameters.put(ApiConstant.REQUEST_DATA, JsonUtils.toJSONString(models));
        return requestParameters;
    }

    private NotifyApplication getNotifyApplication(String requestUrl, HashMap<String, String> headers, HashMap<String, String> requestParameters, HashMap<String,
            String> businessDataMap, String groupNameForModifyPlan) {
        if (StringUtils.isEmpty(requestUrl)) {
            return null;
        }
        try {
            NotifyApplication notifyApplication = new NotifyApplication();
            notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());
            notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);
            notifyApplication.setRequestUrl(requestUrl);
            notifyApplication.setRequestParameters(requestParameters);
            notifyApplication.setRetryTimes(2);
            notifyApplication.setTimeOutSetting(60000);
            notifyApplication.setGroupName(groupNameForModifyPlan);
            HashMap<Integer, Long> retryIntervals = new HashMap<>();
            retryIntervals.put(1, 120000L);
            retryIntervals.put(2, 120000L);
            notifyApplication.setRetryIntervals(retryIntervals);

            String businessId = businessDataMap.getOrDefault(BUSINESS_ID, StringUtils.EMPTY);
            String businessType = businessDataMap.getOrDefault(BUSINESS_TYPE, StringUtils.EMPTY);
            String jobDir = businessDataMap.getOrDefault(JOB_DIR, StringUtils.EMPTY);

            notifyApplication.setBusinessId(businessId);
            notifyApplication.setBusinessType(businessType);
            notifyApplication.setJobDir(jobDir);
            notifyApplication.setHeadParameters(headers);

            // 一致性Hash分组
            String consistenceHashPolicy = getConsistenceHashPolicy(requestParameters);
            notifyApplication.setConsistenceHashPolicy(consistenceHashPolicy);
            if (StringUtils.isNotEmpty(consistenceHashPolicy)) {
            }

            return notifyApplication;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("#getNotifyApplication has error, error msg:" + e.getMessage());
            return null;
        }
    }

    @Override
    public String getPrivateKey() {
        Cache cache = cacheManager.getCache(DictionaryCacheSpec.CACHE_KEY);
        DictionaryCache dictionaryCache = cache.get(DictionaryCode.PLATFORM_PRI_KEY.getCode(), DictionaryCache.class);
        if (null != dictionaryCache) {
            LOGGER.info("#get private key from cache");
            return dictionaryCache.getDictionary().getContent();
        }
        try {
            Dictionary priKeyDictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
            cache.put(DictionaryCode.PLATFORM_PRI_KEY.getCode(), new DictionaryCache(priKeyDictionary));
            LOGGER.info("#get private key from table dictionary and fresh cache");
            return priKeyDictionary.getContent();
        } catch (DictionaryNotExsitException e) {
            LOGGER.error("#get private key fail");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public NotifyApplication buildSPDBankNotifyApplication(FileRepository file, FileTmp_ModifyRepaymentPlan fileTmp, String privateKey, String
            groupNameForModifyPlan) {
        try {
            String requestUrl = buildYntrustMorganStanleyUrl(URL_MODIFY_REPAYMENT_PLAN, URL_API_V3);
            if (StringUtils.isEmpty(requestUrl)) {
                throw new MorganStanleyException("requestUrl is empty.");
            }
            HashMap<String, String> requestParameters = buildSPDBankModifyRequestMap(file, fileTmp);
	        HashMap<String, String> headers = buildHeaders(requestParameters, privateKey);
	        HashMap<String, String> businessDataMap = buildBusinessDataMap(file, fileTmp.getTradeNo());
            return getNotifyApplication(requestUrl, headers, requestParameters, businessDataMap, groupNameForModifyPlan);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("buildSPDBankNotifyApplication fail, tradeNo:" + fileTmp.getTradeNo() + ", error msg:" + e.getMessage());
            return null;
        }
    }

    @Override
    public NotifyApplication buildRepaymentOrderFileSubNotifyApplication(String businessType, FileRepository file, String bizId,
                                                                         FileSubRepaymentOrder fileTmp, String privateKey, String groupNameForModifyPlan) {
        try {
            String requestUrl = buildYntrustMorganStanleyUrl(URL_FILESUB_REPAYMENTORDER, URL_API_V2);
            if (StringUtils.isEmpty(requestUrl)) {
                throw new MorganStanleyException("requestUrl is empty.");
            }
            HashMap<String, String> requestParameters = buildRepaymentOrderFileSubRequestMap(fileTmp, file);
	        HashMap<String, String> headers = buildHeaders(requestParameters, privateKey);
	        HashMap<String, String> businessDataMap = buildBusinessDataMap(file.getPath(), businessType, bizId);
	        return getNotifyApplication(requestUrl, headers, requestParameters, businessDataMap, groupNameForModifyPlan);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("buildNotifyApplication fail, tradeNo:" + bizId + ", error msg:" + e.getMessage());
            return null;
        }
    }

    /**
     * 创建云信MorganStanley服务请求地址
     *
     * @param methodUrl 方法名
     * @param apiUrl    接口版本
     * @return MorganStanley服务请求地址
     */
    private String buildYntrustMorganStanleyUrl(String methodUrl, String apiUrl) {
        try {
            return buildMorganstanleyUrl(yntrustProperties.getMorganstanleyUrl(), apiUrl, methodUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    @Resource
    private ZhongHangProperties zhongHangProperties;

    /**
     * 创建中航MorganStanley服务请求地址
     *
     * @param methodUrl 方法名
     * @param apiUrl    接口版本
     * @return MorganStanley服务请求地址
     */
    private String buildZhonghangMorganStanleyUrl(String methodUrl, String apiUrl) {
        try {
            String zhongHangMorganstanleyUrl = zhongHangProperties.getMorganstanleyUrl();
            return buildMorganstanleyUrl(zhongHangMorganstanleyUrl, apiUrl, methodUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    private String buildMorganstanleyUrl(String morganstanleyUrl, String versionName, String methodName) {
        if (StringUtils.isEmpty(morganstanleyUrl)) {
            return StringUtils.EMPTY;
        }
        return morganstanleyUrl + versionName + methodName;
    }

    private HashMap<String, String> buildSPDBankModifyRequestMap(FileRepository fileRepository, FileTmp_ModifyRepaymentPlan fileTmp) {
        if (null == fileTmp) {
	        return new HashMap<>(0);
        }
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(ApiConstant.REQUEST_NO, fileRepository.buildTradeNo(fileTmp.getTradeNo()));
        requestParameters.put(ApiConstant.UNIQUE_ID, fileTmp.getUniqueId());
        requestParameters.put(ApiConstant.CONTRACT_NO, fileTmp.getContractNo());
        String financialProductCode = fileTmp.getFinancialProductCode();
        if (StringUtils.isEmpty(financialProductCode)) {
            LOGGER.warn("financialProductCode not found in biz file, use fileRepository.getProductCode()");
            financialProductCode = fileRepository.getProductCode();
        }
        requestParameters.put(ApiConstant.FINANCIAL_PRODUCT_CODE, financialProductCode);
        requestParameters.put(ApiConstant.REQUEST_REASON, fileTmp.getRequestReason());
        String assetType = fileTmp.getAssetType();
	    List<RepaymentPlanDetail_SPDBank> repaymentPlanDetailSpdBanks = fileTmp.getRepaymentPlanDetails();
	    List<RepaymentPlanModifyRequestDataModel> models = new ArrayList<>();
	    for (RepaymentPlanDetail_SPDBank planDetail : repaymentPlanDetailSpdBanks) {
		    SPDBankRepaymentPlanModifyRequestDataModel model = planDetail.parse(assetType);
            if (model != null) {
                models.add(model);
            }
        }
        requestParameters.put(ApiConstant.REQUEST_DATA, JsonUtils.toJSONString(models));
        return requestParameters;
    }

    private HashMap<String, String> buildRepaymentOrderFileSubRequestMap(FileSubRepaymentOrder fileTmp, FileRepository file) {
        if (null == fileTmp) {
	        return new HashMap<>(0);
        }

        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.put(ApiConstant.ORDER_REQUEST_NO, fileTmp.getOrderRequestNo());
        requestParameters.put(ApiConstant.ORDER_UNIQUE_ID, fileTmp.getOrderUniqueId());
        requestParameters.put(ApiConstant.TRANS_TYPE, fileTmp.getTransTypeStr());
        //信号文件信托产品代码转到业务文件
        String financialProductCode = fileTmp.getFinancialProductCode();
        if (StringUtils.isEmpty(financialProductCode)) {
            LOGGER.warn("financialProductCode not found in biz file, use fileRepository.getProductCode()");
            financialProductCode = file.getProductCode();
        }
        requestParameters.put(ApiConstant.FINANCIAL_CONTRACT_NO, financialProductCode);
        requestParameters.put(ApiConstant.ORDER_AMOUNT, fileTmp.getOrderAmount() + "");
        requestParameters.put(ApiConstant.FINANCIAL_PRODUCT_CODE, financialProductCode);
        requestParameters.put(ApiConstant.REPAYMENT_ORDER_DETAIL, fileTmp.getRepaymentOrderDetail());
        requestParameters.put(ApiConstant.ORDER_NOTIFY_URL, fileTmp.getNotifyUrl());

        List<RepaymentOrderDetail> repaymentOrderDetails = JsonUtils.parseArray(fileTmp.getRepaymentOrderDetail(), RepaymentOrderDetail.class);
        if (CollectionUtils.isNotEmpty(repaymentOrderDetails)) {
            RepaymentOrderDetail firstRepaymentOrderDetail = repaymentOrderDetails.get(0);
            if (firstRepaymentOrderDetail != null) {
                requestParameters.put(ApiConstant.UNIQUE_ID, firstRepaymentOrderDetail.getContractUniqueId());
                requestParameters.put(ApiConstant.CONTRACT_NO, firstRepaymentOrderDetail.getContractNo());
            }
        }
        return requestParameters;
    }

    @Override
    public void scan_signal_file(String scanPath, String tradeDate) {
        File scanDirectory = new File(scanPath);
        if (!scanDirectory.exists()) {
            LOGGER.warn("scan_signal_file, time:" + DateUtils.getCurrentTimeMillis() + ",scanPath not exists, scanPath:" + scanPath);
            return;
        }
        Iterator itFile = FileUtils.iterateFiles(scanDirectory, SIGNAL_FILE_EXTENSIONS, false);
        while (itFile.hasNext()) {
	        File signalFile = (File) itFile.next();
	        try {
	            String signalFilePath = signalFile.getAbsolutePath();
	            LOGGER.info("scan_signal_file start, signalFilePath:" + signalFilePath);
	            File bizFile = checkBizFileExist(signalFilePath);
	            if (bizFile == null) {
		            LOGGER.warn("scan_signal_file warn, " + " bizFile not exist, waiting... " + ", signalFilePath:" + signalFilePath);
		            continue;
                }
	            FileNameRules fileNameRules = verifyFileNameGetFileNameRules(signalFile);
              String productCode = getProductCodeFromFile(signalFile, fileNameRules.getMerId());

	            File signalBackupFile = buildBackupFile(scanPath, productCode, tradeDate, signalFile.getName());
              File bizBackupFile = buildBackupFile(scanPath, productCode, tradeDate, bizFile.getName());
              if (bizBackupFile.exists() || signalBackupFile.exists()) {
                  throw new MorganStanleyRuntimeException(ERROR_CODE_BUILD_BACKUP_FILE_FAIL,
                      ERROR_MSG_BUILD_BACKUP_FILE_FAIL + "备份文件已存在, 文件名重复, 文件路径地址----->" +
                          signalFilePath);
              }
              FileUtils.moveFile(bizFile, bizBackupFile);
              FileUtils.moveFile(signalFile, signalBackupFile);

                // 存贮备份后的业务文件路径
                fileNameRules.setFilePath(bizBackupFile.getAbsolutePath());

                fileRepositoryService.saveFileRepository(fileNameRules,productCode);
            } catch (GlobalRuntimeException | MorganStanleyRuntimeException e) {
                LOGGER.error("scan_signal_file fail, occur error, time:" + DateUtils.getCurrentTimeMillis() + ", throw IOException, error msg:" + e.getMessage());
                // TODO 处理异常

            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("scan_signal_file fail, occur error, error msg:" + e.getMessage());
                //TODO: IO 异常
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("scan_signal_file fail, occur error, error msg:" + e.getMessage());
            }
        }
    }

    private String getProductCodeFromFile(File signalFile, String merId) throws IOException {
        //信号文件信息
        FileSignal fileSignal = FileUtils.readJsonObject(signalFile, FileSignal.class);
        String financialProductCode = fileSignal.getFinancialProductCode();
        if (StringUtils.isNotEmpty(financialProductCode)) {
            return financialProductCode;
        }
        return merId;
    }

    /**
     * 创建备份文件
     *
     * @param scanPath    文件备份根目录
     * @param merId 信托产品代码
     * @param tradeDate   交易时间
     * @param fileName    备份文件名称
     * @return 备份文件对象
     */
    private File buildBackupFile(String scanPath, String merId, String tradeDate, String fileName) {
        try {
            String backupFilePath = scanPath + separator + merId + separator + tradeDate + separator + fileName;
            LOGGER.info("buildBackupFile start, 开始创建备份文件, backupFilePath:" + backupFilePath);
            return new File(backupFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MorganStanleyRuntimeException(ERROR_CODE_BUILD_BACKUP_FILE_FAIL, ERROR_MSG_BUILD_BACKUP_FILE_FAIL + "创建备份文件失败, error msg:" + e.getMessage());
        }
    }

	private FileNameRules verifyFileNameGetFileNameRules(File signalFile) {
      checkFromSignalFile(signalFile);
      FileNameRules fileNameRules = FileNameRules.buildByFileName(signalFile);
      if (fileNameRules == null) {
          // 文件名称 格式错误 抛异常 该批次 所有文件 不处理
          throw new MorganStanleyRuntimeException(ERROR_CODE_FILE_NAME_RULES,
              ERROR_MSG_FILE_NAME_RULES + signalFile.getAbsolutePath());
      }
      if (!fileNameRules.isLegalIntValue()) {
          // 优先级-总量数值不合法
          throw new MorganStanleyRuntimeException(ERROR_CODE_FILE_NAME_RULES,
              ERROR_MSG_FILE_NAME_RULES + signalFile.getAbsolutePath());
      }
      if (fileNameRules.getTradeTimeDate() == null) {
          // 交易日期格式错误
          throw new MorganStanleyRuntimeException(ERROR_CODE_FILE_NAME_RULES,
              ERROR_MSG_FILE_NAME_RULES + signalFile.getAbsolutePath());
      }
      String merIdBatchNo = fileNameRules.get_MerId_BatchNo();
      if (StringUtils.isEmpty(merIdBatchNo)) {
          // 文件名称 格式错误 抛异常 该批次 所有文件 不处理
          throw new MorganStanleyRuntimeException(ERROR_CODE_BATCH_NO_EMPTY);
      }

      FileType fileType = fileNameRules.getFileTypeEnum();
      if (fileType == null) {
          throw new MorganStanleyRuntimeException(ERROR_CODE_SIGNAL_FILE_ERROR,
              ERROR_MSG_SIGNAL_FILE_ERROR + ", 获取文件类型[fileType]失败.");
      }
      return fileNameRules;
    }

    /**
     * 从信号文件获取信托合同编号
     *
     * @param signalFile 信号文件
     * @return 信托合同编号
     */
    private void checkFromSignalFile(File signalFile) {
        try {
	        FileSignal fileSignal = FileUtils.readJsonObject(signalFile, FileSignal.class);
	        if (fileSignal == null) {
                throw new MorganStanleyRuntimeException(ERROR_CODE_SIGNAL_FILE_ERROR, ERROR_MSG_SIGNAL_FILE_ERROR);
            }
            /*String productCode = fileSignal.getFinancialProductCode();
            if (StringUtils.isEmpty(productCode)) {
                throw new MorganStanleyRuntimeException(ERROR_CODE_SIGNAL_FILE_ERROR, ERROR_MSG_SIGNAL_FILE_ERROR + ", " + "未找到信托合同代码.");
            }
            return productCode;*/
        } catch (MorganStanleyRuntimeException e) {
            e.printStackTrace();
            throw new MorganStanleyRuntimeException(ERROR_CODE_SIGNAL_FILE_ERROR, ERROR_MSG_SIGNAL_FILE_ERROR);
        }
    }

    /**
     * 判断业务文件是否存在
     * 若不存在, 跳过本次操作
     *
     * @param signalFilePath 信号文件路径
     * @return 业务文件
     */
    private File checkBizFileExist(String signalFilePath) {
        try {
	        String bizFilePath = signalFilePath.replace(SIGNAL_FILE_SPLIT, StringUtils.EMPTY);
	        File bizFile = new File(bizFilePath);
	        if (!bizFile.exists()) {
		        LOGGER.warn("checkBizFileExist, time:" + DateUtils.getNowFullDateTime() + ", bizFile not exists, signalFilePath:" + signalFilePath);
		        return null;
            }
	        return bizFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public NotifyApplication buildSPDBankOverDueFeeNotifyApplication(FileRepository file, FileTmp_OverdueFee fileTmp, String privateKey, String
            groupNameForModifyPlan) {
        try {
            String requestUrl = buildYntrustMorganStanleyUrl(URL_MODIFY_OVER_DUE_FEE, URL_API_V3);
            if (StringUtils.isEmpty(requestUrl)) {
                throw new MorganStanleyException("requestUrl is empty.");
            }
	        HashMap<String, String> requestParameters = buildModifyOverdueRequestMap(file, fileTmp);
	        HashMap<String, String> headers = buildHeaders(requestParameters, privateKey);
	        HashMap<String, String> businessDataMap = buildBusinessDataMap(file, fileTmp.getTradeNo());
            return getNotifyApplication(requestUrl, headers, requestParameters, businessDataMap, groupNameForModifyPlan);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("buildSPDBankOverDueFeeNotifyApplication fail, tradeNo:" + fileTmp.getTradeNo() + ", error msg:" + e.getMessage());
            return null;
        }
    }

    @Override
    public void sendToNotifyServer(String fileRepositoryUuid, List<String> continueNoList) throws MorganStanleyException {
        FileRepository fileRepository = fileRepositoryService.get_by_uuid(fileRepositoryUuid);
        if (fileRepository == null) {
            return;
        }
        if (NO_NEED_DEAL_PRODUCT_CODE_LIST.contains(fileRepository.getProductCode())) {
            LOGGER.warn("sendToNotifyServer error, fileRepository.getProductCode() not support, productCode:" + fileRepository.getProductCode());
            return;
        }
	    checkSameBatchAllExist(fileRepository, continueNoList);
	    String beanName = BATCH_PROCESS_HANDLER_MAP.getOrDefault(fileRepository.getFileTypeCode(), null);
        BaseBatchProcessHandlerImpl batchProcessHandler = SpringContextUtil.getBean(beanName);
        if (batchProcessHandler == null) {
            LOGGER.error("sendToNotifyServer error, fileTypeCode not support, set FileSendStatus to ABANDON, fileTypeCode:" + fileRepository.getFileTypeCode());
            update_fileRepository_abandon(fileRepository);
            return;
        }
        sendFileContentToNotifyServer(fileRepository, batchProcessHandler);
    }

	private void checkSameBatchAllExist(FileRepository fileRepository, List<String> continueNoList) throws MorganStanleyException {
		if (fileRepository.needCheckAllExist()) {
            String merIdBatchNo = fileRepository.getMerIdBatchNo();
            if (continueNoList.contains(merIdBatchNo)) {
                throw new MorganStanleyException(ERROR_CODE_WAITING_BATCH, merIdBatchNo); // 批次号下文件未齐 跳过处理
            }
            Integer batchCountInDB = fileRepositoryService.get_same_merIdBatchNo_count(merIdBatchNo);
            Integer batchCount = fileRepository.getBatchCount();
            if (batchCount > batchCountInDB) {
                throw new MorganStanleyException(ERROR_CODE_WAITING_BATCH, merIdBatchNo); // 批次号下文件未齐 跳过处理
            }
        }
    }

    public static final Map<String, String> batchProcessHandlerMap = new HashMap<String, String>() {
        private static final long serialVersionUID = -3558491157582273197L;

        {
            putIfAbsent(FileType.MODIFY_REPAYMENT.getTypeCode(), "ModifyRepaymentPlanBatchProcessHandlerImpl");
            putIfAbsent(FileType.OVERDUE_FEE.getTypeCode(), "OverdueFeeBatchProcessHandlerImpl");
            putIfAbsent(FileType.REPAYMENT_ORDER.getTypeCode(), "repaymentOrderFileSubHandlerImpl");
        }
    };


    @Override
    public void sendFileContentToNotifyServer(FileRepository fileRepository, BatchProcessHandler batchProcessHandler) throws MorganStanleyException {
        if (null == fileRepository) {
            throw new MorganStanleyException("fileRepository is null.");
        }
        if (null == batchProcessHandler) {
            throw new MorganStanleyException("batchProcessHandler is null.");
        }
        try {
            batchProcessHandler.verifySignUpdateProcessStatus(fileRepository);
        } catch (MorganStanleyException e) {
            e.printStackTrace();
            LOGGER.error("sendFileContentToNotifyServer save Redis fail, FileRepository uuid:" + fileRepository.getUuid() + ", error message:" + e.getMessage());
        }
        batchProcessHandler.sendToNotifyServer(fileRepository);
    }

    private void updateFileRepository(FileRepository fileRepository, FileProcessStatus processStatus, FileSendStatus sendStatus, FileExecuteStatus executeStatus,
                                      Integer tradeSize, List<String> bizIdList, Integer processSize, FileStatus fileStatus) throws MorganStanleyException {
        if (fileRepository == null) {
            throw new MorganStanleyException("fileRepository is null.");
        }
        fileRepository.update_status(processStatus, sendStatus, executeStatus, tradeSize, processSize, fileStatus);

        fileRepositoryService.saveOrUpdate(fileRepository);
        if (CollectionUtils.isEmpty(bizIdList) || sendStatus == null) {
            return;
        }
        if (!SEND.getCode().equals(sendStatus.getCode())) {
            return;
        }

        try {
            String fileRepositoryUuid = fileRepository.getUuid();
            fileRepositoryRedisPersistence.pushToTail(fileRepositoryUuid, bizIdList);
        } catch (MorganStanleyException e) {
            e.printStackTrace();
            LOGGER.error("fileRepositoryRedisPersistence pushToTail occur error, error message:" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void update_fileRepository_abandon(FileRepository fileRepository) throws MorganStanleyException {
        updateFileRepository(fileRepository, PROCESSED, ABANDON, UNEXECUTE, null, null, null, FileStatus.ABANDON);
    }

    @Override
    public void update_fileRepository_processed(FileRepository fileRepository, Integer tradeSize) throws MorganStanleyException {
        updateFileRepository(fileRepository, PROCESSED, UNSEND, UNEXECUTE, tradeSize, null, null, FileStatus.PROCESS);
    }

    @Override
    public void update_fileRepository_send(FileRepository fileRepository, List<String> bizIdList) throws MorganStanleyException {
        updateFileRepository(fileRepository, PROCESSED, SEND, UNEXECUTE, null, bizIdList, null, FileStatus.SEND);
    }

    @Override
    public void update_fileRepository_executed(FileRepository fileRepository, Integer processSize) throws MorganStanleyException {
        updateFileRepository(fileRepository, PROCESSED, SEND, EXECUTED, null, null, processSize, FileStatus.DONE);
    }

    @Override
    public void queryNotifyServerResult(String fileRepositoryUuid) throws MorganStanleyException {
        FileRepository fileRepository = fileRepositoryService.get_by_uuid(fileRepositoryUuid);
        if (fileRepository == null) {
            throw new MorganStanleyException("fileRepository is null.");
        }
        List<String> bizIdList = fileRepositoryRedisPersistence.peekJobsFromHead(fileRepositoryUuid);
        if (CollectionUtils.isEmpty(bizIdList)) {
            throw new MorganStanleyException("get bizIdList from redis fail, no result.");
        }
        List<String> processedIdList = new ArrayList<>();
        for (String bizId : bizIdList) {
            try {
                String result = fileRepositoryRedisPersistence.get(bizId);
                if (result == null) {
                    LOGGER.info("queryNotifyServerResult, bizId:" + bizId + ", NotifyServer 执行中.");
                    continue;
                }
                Boolean resultInRedis = Boolean.valueOf(result);
                if (resultInRedis) {
                    processedIdList.add(bizId);
                }
            } catch (MorganStanleyException e) {
                e.printStackTrace();
                LOGGER.error("queryNotifyServerResult error, read from redis has error, error message:" + e.getMessage());
            }
        }
        if (processedIdList.size() == bizIdList.size()) {
            LOGGER.info("queryNotifyServerResult, 文件全部处理完成, fileRepositoryUuid:" + fileRepositoryUuid);
            update_fileRepository_executed(fileRepository, processedIdList.size());
        }
    }

	private HashMap<String, String> buildModifyOverdueRequestMap(FileRepository fileRepository, FileTmp_OverdueFee fileTmp) {
		if (null == fileTmp || null == fileRepository) {
	        return new HashMap<>(0);
		}
        HashMap<String, String> requestParameters = new HashMap<>();
        List<ModifyOverDueFeeDetail> models = fileTmp.parse();
		requestParameters.put(ApiConstant.REQUEST_NO, fileRepository.buildTradeNo(fileTmp.getTradeNo()));
		requestParameters.put(ApiConstant.UNIQUE_ID, fileTmp.getUniqueId());
        requestParameters.put(ApiConstant.CONTRACT_NO, fileTmp.getContractNo());
      String financialProductCode = fileTmp.getFinancialProductCode();
      if (StringUtils.isEmpty(financialProductCode)) {
          LOGGER.warn("financialProductCode not found in biz file, use fileRepository.getProductCode()");
          financialProductCode = fileRepository.getProductCode();
      }
      requestParameters.put(ApiConstant.FINANCIAL_PRODUCT_CODE, financialProductCode);
        requestParameters.put(ApiConstant.MODIFY_OVERDUEFEE_DETAILS, JsonUtils.toJSONString(models));
        return requestParameters;
    }
}
