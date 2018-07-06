package com.suidifu.microservice.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.microservice.handler.DeductApplicationDetailHandler;
import com.suidifu.microservice.handler.DeductPlanAndScheduleHandler;
import com.suidifu.microservice.model.DeductDataContext;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJobServer;
import com.suidifu.swift.notifyserver.notifyserver.PriorityEnum;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductApplicationDetailInfoModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductPlanModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.api.jpmorgan.enums.AccountSide;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_standard_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.entity.financial.FinancialContractConfig;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.financial.PaymentStrategyMode;
import com.zufangbao.sun.service.FinancialContractConfigService;
import com.zufangbao.sun.utils.Md5Util;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductMode;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.remittance.CertificateType;
import com.zufangbao.sun.yunxin.service.UnionpayBankConfigService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 */
@Log4j2
@Component("deductPlanAndScheduleHandler")
public class DeductPlanAndScheduleHandlerImpl implements DeductPlanAndScheduleHandler {
    @Resource
    private UnionpayBankConfigService unionpayBankConfigService;
    @Resource
    private DeductApplicationDetailHandler deductApplicationDetailHandler;
    @Resource
    private FinancialContractConfigService financialContractConfigService;

    private void buildTradeSchedules(DeductDataContext deductDataContext) {
        DeductMode deductMode = deductDataContext.getDeductMode();

        List<TradeSchedule> tradeSchedules;

        if (deductMode == DeductMode.SPLIT_MODE) {
            tradeSchedules = buildTradeScheduleForSplitMode(deductDataContext);
        } else {
            tradeSchedules = buildTradeScheduleForMultiChannelMode(deductDataContext);
        }

        deductDataContext.setTradeSchedules(tradeSchedules);
    }

    private List<TradeSchedule> buildTradeScheduleForMultiChannelMode(DeductDataContext deductDataContext) {
        List<DeductPlanModel> deductPlanModels = deductDataContext.getDeductPlanModels();

        DeductRequestModel deductRequestModel = deductDataContext.getDeductRequestModel();

        String outlierTransactionUuid = buildOutlierTransactionUuidForMultiChannel(
                deductPlanModels, deductRequestModel.getDeductApplicationUuid());

        List<TradeSchedule> tradeSchedules = buildTradeScheduleList(outlierTransactionUuid,
                deductDataContext.getBatchDeductApplicationUuid(), deductRequestModel, deductPlanModels);

        fillOutlierTransactionUuidIntoDeductPlanModel(outlierTransactionUuid, deductPlanModels);

        return tradeSchedules;
    }

    private void fillOutlierTransactionUuidIntoDeductPlanModel(
            String outlierTransactionUuid,
            List<DeductPlanModel> deductPlanModels) {

        for (DeductPlanModel deductPlanModel : deductPlanModels) {
            deductPlanModel.setTransactionUuid(outlierTransactionUuid);
        }
    }

    private String buildOutlierTransactionUuidForMultiChannel(List<DeductPlanModel> deductPlanModels,
                                                              String deductApplicationUuid) {
        List<String> result = new ArrayList<>();

        List<String> deductPlanModelUuidList = deductPlanModels.stream()
                .map(DeductPlanModel::getDeductPlanUuid).collect(Collectors.toList());

        result.add(deductApplicationUuid);
        result.addAll(deductPlanModelUuidList);

        return Md5Util.encode(StringUtils.join(result, "_"));
    }

    private List<TradeSchedule> buildTradeScheduleForSplitMode(DeductDataContext deductDataContext) {
        List<TradeSchedule> tradeSchedules = new ArrayList<>();

        List<DeductPlanModel> deductPlanModels = deductDataContext.getDeductPlanModels();

        DeductRequestModel deductRequestModel = deductDataContext.getDeductRequestModel();

        for (DeductPlanModel deductPlanModel : deductPlanModels) {
            List<DeductPlanModel> splitModeDeductPlanModeList = new ArrayList<>();

            splitModeDeductPlanModeList.add(deductPlanModel);

            String outlierTransactionUuid = buildOutlierTransactionUuidForMultiChannel(
                    splitModeDeductPlanModeList,
                    deductRequestModel.getDeductApplicationUuid());

            List<TradeSchedule> list = buildTradeScheduleList(outlierTransactionUuid,
                    deductDataContext.getBatchDeductApplicationUuid(), deductRequestModel,
                    splitModeDeductPlanModeList);

            tradeSchedules.addAll(list);

            fillOutlierTransactionUuidIntoDeductPlanModel(
                    outlierTransactionUuid, splitModeDeductPlanModeList);
        }

        return tradeSchedules;
    }

    // 多通道模式的生成DeductPlanModelList
    private List<DeductPlanModel> buildDeductPlanModelsForMultiChannelMode(DeductDataContext deductDataContext) {
        List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfos = deductDataContext
                .getPaymentChannelAndSignUpInfoForMultiChannels();

        List<DeductPlanModel> deductPlanModels = new ArrayList<>();

        DeductRequestModel deductRequestModel = deductDataContext
                .getDeductRequestModel();
        List<Date> effectiveTimeList = deductDataContext.getExecutimeIntervalsWithMulitPaymentChannelSize();
        for (int i = 0; i < paymentChannelAndSignUpInfos.size(); i++) {
            PaymentChannelAndSignUpInfo paymentChannelAndSignUpInfo = paymentChannelAndSignUpInfos.get(i);

            BigDecimal plannedTotalAmount = new BigDecimal(
                    deductRequestModel.getDeductAmount());

            DeductPlanModel deductPlanModel = buildSingleDeductPlanModel(
                    deductRequestModel,
                    paymentChannelAndSignUpInfo.getPaymentChannelSummaryInfo(),
                    plannedTotalAmount);

            deductPlanModel.setProtocolNo(paymentChannelAndSignUpInfo.getProNo());
            deductPlanModel.setCpBankCode(paymentChannelAndSignUpInfo.getStdBankCode());
            deductPlanModel.setBankCode(paymentChannelAndSignUpInfo.getBankCode());
            deductPlanModel.setCpBankName(paymentChannelAndSignUpInfo.getBankName());
            deductPlanModel.setCpBankProvince(paymentChannelAndSignUpInfo.getProvinceCode());
            deductPlanModel.setCpBankCity(paymentChannelAndSignUpInfo.getCityCode());
            deductPlanModel.setDeductMode(0);
            deductPlanModel.setEffectiveAbsoluteTime(i < effectiveTimeList.size() ? effectiveTimeList.get(i) : null);

            deductPlanModels.add(deductPlanModel);
        }

        return deductPlanModels;
    }

    private List<TradeSchedule> buildTradeScheduleList(String outlierTransactionUuid,
                                                       String batchDeductApplicationUuid,
                                                       DeductRequestModel deductRequestModel,
                                                       List<DeductPlanModel> deductPlans) {
        if (CollectionUtils.isEmpty(deductPlans)) {
            return Collections.emptyList();
        }

        String urlForJpmorganCallback = deductRequestModel
                .getUrlForJpmorganCallback();

        DeductPlanModel firstDeductPlan = getDeductPlanByOrder(deductPlans,
                Order.First);

        String batchUuid = firstDeductPlan.getDeductApplicationUuid();

        String destinationAccountName = firstDeductPlan
                .getCpBankAccountHolder();
        String destinationAccountNo = firstDeductPlan.getCpBankCardNo();

        String destinationAccountAppendix = assembleDestinationAccountAppendixMap(
                firstDeductPlan.getCpIdNumber(), firstDeductPlan.getMobile());
        String destinationBankInfo = assembleDestinationBankInfoMap(
                firstDeductPlan.getCpBankCode(),
                firstDeductPlan.getCpBankProvince(),
                firstDeductPlan.getCpBankCity(),
                firstDeductPlan.getCpBankName());
        String fstPaymentChannelUuid = firstDeductPlan.getPaymentChannelUuid();

        String fstGatewayRouterInfo = getGateWayRouterInfo(firstDeductPlan.getBankCode(),
                firstDeductPlan.getCpBankCode(),
                firstDeductPlan.getPgClearingAccount(), firstDeductPlan.getProtocolNo());
        Date fstEffectiveAbsoluteTime = firstDeductPlan.getEffectiveAbsoluteTime();

        BigDecimal fstTransactionAmount = firstDeductPlan
                .getPlannedTotalAmount();
        String fstSlotUuid = firstDeductPlan.getTradeScheduleSlotUuid();

        String sndPaymentChannelUuid = null;
        String sndGatewayRouterInfo = null;
        Date sndEffectiveAbsoluteTime = null;
        BigDecimal sndTransactionAmount = null;
        String sndSlotUuid = null;

        String trdPaymentChannelUuid = null;
        String trdGatewayRouterInfo = null;
        Date trdEffectiveAbsoluteTime = null;
        BigDecimal trdTransactionAmount = null;
        String trdSlotUuid = null;

        String fourthPaymentChannelUuid = null;
        String fourthGatewayRouterInfo = null;
        Date fourthEffectiveAbsoluteTime = null;
        BigDecimal fourthTransactionAmount = null;
        String fourthSlotUuid = null;

        String fifthPaymentChannelUuid = null;
        String fifthGatewayRouterInfo = null;
        Date fifthEffectiveAbsoluteTime = null;
        BigDecimal fifthTransactionAmount = null;
        String fifthSlotUuid = null;

        DeductPlanModel sndDeductPlan = getDeductPlanByOrder(deductPlans,
                Order.Second);
        DeductPlanModel trdDeductPlan = getDeductPlanByOrder(deductPlans,
                Order.Third);
        DeductPlanModel fourthDeductPlan = getDeductPlanByOrder(deductPlans,
                Order.Fourth);
        DeductPlanModel fifthDeductPlan = getDeductPlanByOrder(deductPlans,
                Order.Fifth);

        if (null != sndDeductPlan) {
            sndPaymentChannelUuid = sndDeductPlan.getPaymentChannelUuid();
            sndGatewayRouterInfo = getGateWayRouterInfo(null,
                    sndDeductPlan.getCpBankCode(),
                    sndDeductPlan.getPgClearingAccount(), sndDeductPlan.getProtocolNo());
            sndTransactionAmount = sndDeductPlan.getPlannedTotalAmount();
            sndSlotUuid = sndDeductPlan.getTradeScheduleSlotUuid();
            sndEffectiveAbsoluteTime = sndDeductPlan.getEffectiveAbsoluteTime();
        }

        if (null != trdDeductPlan) {
            trdPaymentChannelUuid = trdDeductPlan.getPaymentChannelUuid();
            trdGatewayRouterInfo = getGateWayRouterInfo(null,
                    trdDeductPlan.getCpBankCode(),
                    trdDeductPlan.getPgClearingAccount(), trdDeductPlan.getProtocolNo());
            trdTransactionAmount = trdDeductPlan.getPlannedTotalAmount();
            trdSlotUuid = trdDeductPlan.getTradeScheduleSlotUuid();
            trdEffectiveAbsoluteTime = trdDeductPlan.getEffectiveAbsoluteTime();
        }

        if (null != fourthDeductPlan) {
            fourthPaymentChannelUuid = fourthDeductPlan.getPaymentChannelUuid();
            fourthGatewayRouterInfo = getGateWayRouterInfo(null,
                    fourthDeductPlan.getCpBankCode(),
                    fourthDeductPlan.getPgClearingAccount(), fourthDeductPlan.getProtocolNo());
            fourthTransactionAmount = fourthDeductPlan.getPlannedTotalAmount();
            fourthSlotUuid = fourthDeductPlan.getTradeScheduleSlotUuid();
            fourthEffectiveAbsoluteTime = fourthDeductPlan.getEffectiveAbsoluteTime();
        }

        if (null != fifthDeductPlan) {
            fifthPaymentChannelUuid = fifthDeductPlan.getPaymentChannelUuid();
            fifthGatewayRouterInfo = getGateWayRouterInfo(null,
                    fifthDeductPlan.getCpBankCode(),
                    fifthDeductPlan.getPgClearingAccount(), fifthDeductPlan.getProtocolNo());
            fifthTransactionAmount = fifthDeductPlan.getPlannedTotalAmount();
            fifthSlotUuid = fifthDeductPlan.getTradeScheduleSlotUuid();
            fifthEffectiveAbsoluteTime = fifthDeductPlan.getEffectiveAbsoluteTime();
        }

        TradeSchedule tradeSchedule = new TradeSchedule(
                com.zufangbao.gluon.api.jpmorgan.enums.AccountSide.DEBIT,
                destinationAccountName, destinationAccountNo,
                destinationAccountAppendix, destinationBankInfo, "",
                outlierTransactionUuid, UUID.randomUUID().toString(),
                fstPaymentChannelUuid, fstEffectiveAbsoluteTime,
                fstTransactionAmount, batchUuid, batchDeductApplicationUuid, null, null, null,
                fstGatewayRouterInfo, urlForJpmorganCallback,
                sndPaymentChannelUuid, sndGatewayRouterInfo,
                sndEffectiveAbsoluteTime, sndTransactionAmount,
                trdPaymentChannelUuid, trdGatewayRouterInfo,
                trdEffectiveAbsoluteTime, trdTransactionAmount,
                fourthPaymentChannelUuid, fourthGatewayRouterInfo,
                fourthEffectiveAbsoluteTime, fourthTransactionAmount,
                fifthPaymentChannelUuid, fifthGatewayRouterInfo,
                fifthEffectiveAbsoluteTime, fifthTransactionAmount,
                fstSlotUuid, sndSlotUuid, trdSlotUuid, fourthSlotUuid,
                fifthSlotUuid);

        List<TradeSchedule> tradeScheduleList = new ArrayList<>();

        tradeScheduleList.add(tradeSchedule);

        return tradeScheduleList;
    }

    private DeductPlanModel getDeductPlanByOrder(List<DeductPlanModel> deductPlans, Order order) {
        if (deductPlans.size() < order.ordinal() + 1) {
            return null;
        }
        return deductPlans.get(order.ordinal());
    }

    private String assembleDestinationBankInfoMap(String standardBankCode,
                                                  String provinceCode, String cityCode, String bankName) {
        Map<String, String> destinationBankInfoMap = new HashMap<>();
        destinationBankInfoMap.put("bankCode", standardBankCode);
        destinationBankInfoMap.put("bankProvince", provinceCode);
        destinationBankInfoMap.put("bankCity", cityCode);
        destinationBankInfoMap.put("bankName", bankName);
        return JSON.toJSONString(destinationBankInfoMap,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    private String getGateWayRouterInfo(String bankCode,
                                        String standardBankCode,
                                        String clearingNo,
                                        String protocolNo) {
        String debitMode = unionpayBankConfigService.isUseBatchDeduct(bankCode,
                standardBankCode) ? "batchMode" : "realTimeMode";

        Map<String, String> fstGateWayRouterInfoMap = new HashMap<>();
        fstGateWayRouterInfoMap.put("debitMode", debitMode);
        if (StringUtils.isNotEmpty(clearingNo)) {
            fstGateWayRouterInfoMap.put("reckonAccount", clearingNo);
        }

        if (StringUtils.isNotEmpty(protocolNo)) {
            fstGateWayRouterInfoMap.put("protocolNo", protocolNo);
        }

        return JSON.toJSONString(fstGateWayRouterInfoMap);
    }

    private String assembleDestinationAccountAppendixMap(String idCardNum,
                                                         String mobile) {
        Map<String, String> destinationAccountAppendixMap = new HashMap<>();
        destinationAccountAppendixMap.put("idNumber", idCardNum);
        destinationAccountAppendixMap.put("mobile", mobile);
        return JSON.toJSONString(
                destinationAccountAppendixMap,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    private DeductPlanModel buildSingleDeductPlanModel(
            DeductRequestModel deductModel,
            PaymentChannelSummaryInfo paymentChannelSummaryInfo,
            BigDecimal plannedTotalAmount) {
        String requestNo = deductModel.getRequestNo();
        String creatorName = deductModel.getCreatorName();
        String deductApplicationUuid = deductModel.getDeductApplicationUuid();
        String financialContractUuid = deductModel.getFinancialContractUuid();
        String standardBankCode = deductModel.getStandardBankCode();

        String paymentChannelUuid = paymentChannelSummaryInfo
                .getChannelServiceUuid();
        int transactionType = AccountSide.DEBIT.ordinal();
        String cpBankCardNo = deductModel.getDeductAccountNo();
        String cpBankAccountHolder = deductModel.getAccountHolderName();
        String cpIdNumber = deductModel.getIdCardNo();
        String cpBankProvince = deductModel.getCpBankProvince();
        String cpBankCity = deductModel.getCpBankCity();
        String cpBankName = deductModel.getAccountBankName();

        List<RepaymentDetail> repaymentDetailInfoList = JsonUtils.parseArray(deductModel.getRepayDetailInfo(), RepaymentDetail.class);
        String deductApplicationDetailUuid = null;
        if (CollectionUtils.isNotEmpty(repaymentDetailInfoList)) {
            deductApplicationDetailUuid = repaymentDetailInfoList.get(0).getRepaymentDetailUuid();
        }

        PaymentInstitutionName gateway = PaymentInstitutionName
                .fromValue(paymentChannelSummaryInfo.getPaymentGateway());

        DeductPlanModel deductPlan = new DeductPlanModel(UUID.randomUUID()
                .toString(), deductApplicationUuid, deductApplicationDetailUuid,
                financialContractUuid, deductModel.getUniqueId(), deductModel.getContractNo(), gateway.ordinal(),
                paymentChannelUuid, paymentChannelSummaryInfo.getMerchantId(), paymentChannelSummaryInfo.getClearingNo(), transactionType, null,
                standardBankCode, cpBankCardNo, cpBankAccountHolder,
                CertificateType.ID_CARD.ordinal(), cpIdNumber, cpBankProvince,
                cpBankCity, cpBankName, null, plannedTotalAmount, null,
                creatorName, deductModel.getMobile(), deductModel.getRepaymentType(),
                SourceType.INTERFACEONLINEDEDUCT.ordinal(),
                deductModel.getBatchDeductApplicationUuid(),
                deductModel.getBatchDeductId(), requestNo);

        deductPlan.setTradeScheduleSlotUuid(UUIDUtil.snowFlakeIdString());

        return deductPlan;
    }

    @Override
    public void processDeductPlanModelAndTradeSchedule(NotifyJobServer notifyJobServer, DeductDataContext deductDataContext, String groupName, int deductPlanModelListSize) {
        boolean isSuccess = true;

        String failMsg = "";

        DeductRequestModel deductRequestModel = deductDataContext.getDeductRequestModel();

        log.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.RECE_DEDUCT_INFO_FROM_BRIDGEWATER_DEDUCT_SYSTEM + "begin with deductRequestModel[" + JsonUtils.toJsonString(deductRequestModel) + "],deductDataContext[" + JsonUtils.toJsonString(deductDataContext) + "]");

        try {
            prepareDeductPlanModelAndTradeSchedule(deductDataContext);

            deductPlanModelListSize = deductDataContext.getDeductPlanModels().size();
            if (deductPlanModelListSize == 0) {
                deductPlanModelListSize = 1;
                isSuccess = false;
                log.info("#handleDeductBusiness# set deductPlanModelListSize=1 when 0 with deductRequestModel["
                        + JsonUtils.toJsonString(deductRequestModel) + "],deductDataContext[" + JsonUtils.toJsonString(deductDataContext) + "]");
            }
        } catch (Exception e) {
            deductPlanModelListSize = 1;

            failMsg = "系统异常：" + e.getMessage();

            log.error("#handleDeductBusiness# occur exception with deductRequestModel["
                    + JsonUtils.toJsonString(deductRequestModel)
                    + "],deductDataContext[" + JsonUtils.toJsonString(deductDataContext) + "],stack full exception["
                    + ExceptionUtils.getFullStackTrace(e) + "]");

            isSuccess = false;
        } finally {
            NotifyApplication notifyApplication = sendDeductPlanModelAndTradeScheduleToDeduct(notifyJobServer, deductDataContext, failMsg, deductPlanModelListSize, isSuccess, groupName);

            log.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.RECE_DEDUCT_INFO_FROM_BRIDGEWATER_DEDUCT_SYSTEM + "end with notifyApplication[" + JsonUtils.toJsonString(notifyApplication) + "],isSuccess[" + isSuccess + "]"
                    + "deductPlanModelSize[" + deductDataContext.getDeductPlanModels().size() + "],tradeSchedule Size[" + deductDataContext.getTradeSchedules().size() + "]"
                    + ",result will callback to deduct system");
        }
    }

    @Override
    public void prepareDeductPlanModelAndTradeSchedule(DeductDataContext deductDataContext) {
        // 检测扣扣模式 拆单或多通道
        detectDeductMode(deductDataContext);

        buildDeductPlanModels(deductDataContext);

        buildTradeSchedules(deductDataContext);

        //获取ApplicationDetail中资产信息
        deductApplicationDetailInfo(deductDataContext);

        //按最大金额排序
        sortDeductPlanModelsAndTradeSchedules(deductDataContext);
    }

    @Override
    public NotifyApplication sendDeductPlanModelAndTradeScheduleToDeduct(NotifyJobServer notifyJobServer,
                                                                         DeductDataContext deductDataContext,
                                                                         String failedMsg,
                                                                         int deductPlanModeSize,
                                                                         boolean isSuccess,
                                                                         String groupName) {

        NotifyApplication notifyApplication = buildNotifyApplication(
                deductDataContext.getCallbackUrl(),
                deductDataContext.getDeductApplicaitonUuid(),
                deductDataContext.getBatchDeductApplicationUuid(),
                deductDataContext.getRequestNo(),
                deductDataContext.getDeductRequestModel(),
                deductDataContext.getFinancialContractUuid(),
                deductDataContext.getDeductApplicationDetailInfoMap(),
                deductDataContext.getDeductPlanModels(),
                deductDataContext.getTradeSchedules(),
                failedMsg, deductPlanModeSize, isSuccess, groupName);

        notifyJobServer.pushJob(notifyApplication);

        return notifyApplication;
    }

    private NotifyApplication buildNotifyApplication(String requestUrl,
                                                     String deductApplicationUuid,
                                                     String batchDdeductApplicationUuid,
                                                     String requestNo,
                                                     DeductRequestModel deductRequestModel,
                                                     String financialContractUuid,
                                                     Map<String, DeductApplicationDetailInfoModel> deductApplicationDetailInfoMap,
                                                     List<DeductPlanModel> deductPlanModelList,
                                                     List<TradeSchedule> tradeScheduleList,
                                                     String errormsg,
                                                     int deductPlanModelListSize,
                                                     boolean isSuccess,
                                                     String groupName) {
        HashMap<String, Object> requestParams = new HashMap<>();

        List<RepaymentDetail> repaymentDetailInfoList = JsonUtils.parseArray(deductRequestModel.getRepayDetailInfo(),
                RepaymentDetail.class);
        List<String> repaymentCodes = repaymentDetailInfoList.stream().
                map(RepaymentDetail::getRepaymentPlanNo).collect(Collectors.toList());

        requestParams.put(ZhonghangResponseMapSpec.TRADE_SCHEDEULE_LIST, (tradeScheduleList));
        requestParams.put(ZhonghangResponseMapSpec.DEDUCT_PLAN_MODEL_LIST, (deductPlanModelList));
        requestParams.put(ZhonghangResponseMapSpec.FAIL_MESSAGE, errormsg);
        requestParams.put(ZhonghangResponseMapSpec.BATCH_DEDUCT_APPLICATION_UUID, batchDdeductApplicationUuid);
        requestParams.put(ZhonghangResponseMapSpec.DEDUCT_APPLICATION_UUID, deductApplicationUuid);
        requestParams.put(ZhonghangResponseMapSpec.DEDUCT_APPLICATION_DETAIL_INFO_MAP, deductApplicationDetailInfoMap);
        requestParams.put(ZhonghangResponseMapSpec.DEDUCT_PLAN_SIZE, deductPlanModelListSize);
        requestParams.put(ZhonghangResponseMapSpec.IS_SUCCESS, isSuccess);
        requestParams.put(ZhonghangResponseMapSpec.REQUEST_NO, requestNo);
        requestParams.put(ZhonghangResponseMapSpec.CHECK_RESPONSE_NO, deductRequestModel.getCheckResponseNo());
        requestParams.put(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT_UUID, financialContractUuid);
        requestParams.put(ZhonghangResponseMapSpec.REPAYMENT_CODES, repaymentCodes);
        requestParams.put(ZhonghangResponseMapSpec.CONTRACT_NO, deductRequestModel.getContractNo());
        requestParams.put(ZhonghangResponseMapSpec.UNIQUE_ID, deductRequestModel.getUniqueId());
        requestParams.put(ZhonghangResponseMapSpec.CUSTOMER_NAME, deductRequestModel.getCustomerName());
        HashMap<String, String> dataResponsePackage = new HashMap<>();

        dataResponsePackage.put(HttpClientUtils.DATA_RESPONSE_PACKET,
                JsonUtils.toJsonString(requestParams));

        NotifyApplication notifyApplication = new NotifyApplication();
        notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());
        notifyApplication.setBusinessId(requestNo);
        notifyApplication.setRequestParameters(dataResponsePackage);
        notifyApplication.setRequestUrl(requestUrl);
        notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);
        notifyApplication.setDelaySecond(0);
        notifyApplication.setRetryTimes(0);
        notifyApplication.setPriority(PriorityEnum.FIRST);
        notifyApplication.setGroupName(groupName);
        return notifyApplication;
    }

    private void detectDeductMode(DeductDataContext deductDataContext) {
        DeductRequestModel deductRequestModel = deductDataContext
                .getDeductRequestModel();

        BigDecimal deductAmount = new BigDecimal(
                deductRequestModel.getDeductAmount());

        List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfoForMultiChannel;

        List<PaymentChannelAndSignUpInfo> sortPaymentChannelAndSignUpInfos = calculateMaxLimitedAmount(deductDataContext
                .getPaymentChannelAndSignUpInfos());

        PaymentChannelAndSignUpInfo maxLimitedAmountPaymentChannelSummaryInfo = sortPaymentChannelAndSignUpInfos.get(0);

        DeductMode deductMode = deductDataContext.getDeductMode();

        FinancialContractConfig financialContractConfig = financialContractConfigService.getFinancialContractConfigBy(deductDataContext.getFinancialContractUuid(), BusinessType.SELF);

        if (null == financialContractConfig) {
            throw new ApiException("信托对应扣款通道未配置 FinancialContractUuid[" + deductDataContext.getFinancialContractUuid() + "]");
        }

        if (deductMode == null) {

            BigDecimal maxLimitedAmount = maxLimitedAmountPaymentChannelSummaryInfo
                    .getMinLimitedAmount();

            deductMode = deductAmount
                    .compareTo(maxLimitedAmount) > 0 ? DeductMode.SPLIT_MODE
                    : DeductMode.MULTI_CHANNEL_MODE;

            deductDataContext.setDeductMode(deductMode);
        }


        if (deductMode == DeductMode.SPLIT_MODE) {

            deductDataContext
                    .setPaymentChannelAndSignUpInfoForSplitMode(maxLimitedAmountPaymentChannelSummaryInfo);
        } else {

            //筛选出所有限额>扣款金额的的通道
            if (PaymentStrategyMode.QUOTAPRIORITY.equals(financialContractConfig.getDebitPaymentChannelMode())) {
                paymentChannelAndSignUpInfoForMultiChannel = sortPaymentChannelAndSignUpInfos.stream().filter(a -> a.getMinLimitedAmount().compareTo(deductAmount) >= 0).collect(Collectors.toList());
            } else {
                paymentChannelAndSignUpInfoForMultiChannel = deductDataContext.getPaymentChannelAndSignUpInfos().stream().filter(a -> a.getMinLimitedAmount().compareTo(deductAmount) >= 0).collect(Collectors.toList());
            }

            deductDataContext.setPaymentChannelAndSignUpInfoForMultiChannels(paymentChannelAndSignUpInfoForMultiChannel);
        }
    }

    private List calculateMaxLimitedAmount(
            List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfos) {

        List<PaymentChannelAndSignUpInfo> maxModePaymentChannelAndSignUpInfos = new ArrayList<>(paymentChannelAndSignUpInfos);

        Comparator<PaymentChannelAndSignUpInfo> maxPaymentChannelAndSignUpInfoComparator = (o1, o2) ->
                o2.getMinLimitedAmount().compareTo(o1.getMinLimitedAmount());
        maxModePaymentChannelAndSignUpInfos.sort(maxPaymentChannelAndSignUpInfoComparator);

        if (CollectionUtils.isEmpty(maxModePaymentChannelAndSignUpInfos)) {
            return ListUtils.EMPTY_LIST;
        }

        return maxModePaymentChannelAndSignUpInfos;
    }


    private void buildDeductPlanModels(DeductDataContext deductDataContext) {
        DeductMode deductMode = deductDataContext.getDeductMode();

        List<DeductPlanModel> deductPlanModelList;

        if (deductMode == DeductMode.SPLIT_MODE) {
            deductPlanModelList = buildDeductPlanModelsForSplitMode(deductDataContext);
        } else {
            deductPlanModelList = buildDeductPlanModelsForMultiChannelMode(deductDataContext);
        }
        deductDataContext.setDeductPlanModels(deductPlanModelList);
    }

    // 拆单模式生成DeductPlanModelList
    private List<DeductPlanModel> buildDeductPlanModelsForSplitMode(DeductDataContext deductDataContext) {
        DeductRequestModel deductRequestModel = deductDataContext
                .getDeductRequestModel();

        List<DeductPlanModel> deductPlanModelList = new ArrayList<>();

        BigDecimal deductAmount = new BigDecimal(
                deductRequestModel.getDeductAmount());

        PaymentChannelAndSignUpInfo paymentChannelAndSignUpInfo = deductDataContext
                .getPaymentChannelAndSignUpInfoForSplitMode();

        BigDecimal splitAmountStep = paymentChannelAndSignUpInfo
                .getMinLimitedAmount();

        List<BigDecimal> splitedAmountList = splitAmountBy(deductAmount,
                splitAmountStep);

        PaymentChannelSummaryInfo paymentChannelSummaryInfo = paymentChannelAndSignUpInfo
                .getPaymentChannelSummaryInfo();

        for (BigDecimal plannedTotalAmount : splitedAmountList) {

            DeductPlanModel buildSingleDeductPlanModel = buildSingleDeductPlanModel(
                    deductRequestModel, paymentChannelSummaryInfo,
                    plannedTotalAmount);

            buildSingleDeductPlanModel.setProtocolNo(paymentChannelAndSignUpInfo.getProNo());
            buildSingleDeductPlanModel.setCpBankCode(paymentChannelAndSignUpInfo.getStdBankCode());
            buildSingleDeductPlanModel.setPgClearingAccount(paymentChannelAndSignUpInfo.getPaymentChannelSummaryInfo().getClearingNo());


            buildSingleDeductPlanModel.setDeductMode(1);
            deductPlanModelList.add(buildSingleDeductPlanModel);
        }
        return deductPlanModelList;
    }

    private void deductApplicationDetailInfo(DeductDataContext deductDataContext) {
        deductApplicationDetailHandler.getDeductApplicationDetailInfoModel(deductDataContext);
    }

    private void sortDeductPlanModelsAndTradeSchedules(DeductDataContext deductDataContext) {
        List<DeductPlanModel> deductPlanModelList = deductDataContext.getDeductPlanModels();

        List<TradeSchedule> tradeScheduleList = deductDataContext.getTradeSchedules();

        Comparator<DeductPlanModel> deductPlanModelComparator = (o1, o2) ->
                o2.getPlannedTotalAmount().compareTo(o1.getPlannedTotalAmount());

        Comparator<TradeSchedule> tradeScheduleComparator = (o1, o2) ->
                o2.getFstTransactionAmount().compareTo(o1.getFstTransactionAmount());

        deductPlanModelList.sort(deductPlanModelComparator);
        tradeScheduleList.sort(tradeScheduleComparator);

        deductDataContext.setTradeSchedules(tradeScheduleList);
        deductDataContext.setDeductPlanModels(deductPlanModelList);
    }

    private List<BigDecimal> splitAmountBy(BigDecimal amount, BigDecimal step) {
        List<BigDecimal> amountList = new ArrayList<>();

        while (amount.compareTo(step) >= 0) {
            amountList.add(step);
            amount = amount.subtract(step);
        }
        if (amount.compareTo(step) < 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
            amountList.add(amount);
        }
        return amountList;
    }

    enum Order {
        First, Second, Third, Fourth, Fifth
    }
}