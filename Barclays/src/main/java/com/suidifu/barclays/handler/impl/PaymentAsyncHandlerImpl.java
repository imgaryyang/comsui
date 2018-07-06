package com.suidifu.barclays.handler.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.suidifu.barclays.entity.PaymentAsyncJobServer;
import com.suidifu.barclays.factory.PaymentHandlerFactory;
import com.suidifu.barclays.handler.PaymentAsyncHandler;
import com.suidifu.barclays.service.GatewayConfigService;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.handler.ThirdPartyPayPacketHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.zufangbao.sun.entity.account.InstitutionReconciliationAuditStatus;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyTransactionRecordService;

/*
 * @author zfj
 * @date 17/6/20
 */

@Component(value = "paymentAsyncHandler")
public class PaymentAsyncHandlerImpl implements PaymentAsyncHandler {

    private static final Log LOGGER = LogFactory.getLog(PaymentAsyncHandlerImpl.class);

    @Autowired
    private ThirdPartyTransactionRecordService thirdPartyTransactionRecordService;
	
    @Autowired
    private PaymentAsyncJobServer paymentAsyncJobServer;
    
    @Autowired
    private GatewayConfigService gatewayConfigService;
    
	@Override
	public void onResult(NotifyJob result) {
        String redundanceMap= result.getRedundanceMap();
        HashMap<String,String> workingParams = JSON.parseObject(redundanceMap, new TypeReference<HashMap<String,String> >(){});
        String tradeUuid = workingParams.get(ThirdPartyPayPacketHandler.TRD_PARTY_TRADE_UUID);

        if (!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())) {
            LOGGER.info("---"+ result.getRequestMethod() +"request url: "
                    + result.getRequestUrl() + "error, back status code : "
                    + result.getLastTimeHttpResponseCode() + ". back error info : "
                    + result.getResponseJson() + ", trade uuid : " + tradeUuid + "---");
            return;
        }

        String isBatchQueryStr = workingParams.get(ThirdPartyPayPacketHandler.TRD_PARTY_QUERY_TYPE);
        String gateWayName = workingParams.get(ThirdPartyPayPacketHandler.TRD_PARTY_GATEWAY_NAME);
        String accountNo = workingParams.get(ThirdPartyPayPacketHandler.TRD_PARTY_ACCOUNT_NO);
        try {
            Boolean isBatchQuery=Boolean.parseBoolean(isBatchQueryStr);

            ThirdPartyPayPacketHandler packetHandler=PaymentHandlerFactory.createThirdPartyPayPacket(PaymentHandlerFactory.getPaymentNameSpace(gateWayName));
            LOGGER.info("-- gateway name : " + gateWayName + " , account no : " + accountNo);
            Map<String, String> gatewayConfig = gatewayConfigService.getByChannelIdentityAndMerchantNo(gateWayName,accountNo);

            LOGGER.info("-- gateway name : " + gatewayConfig);


            QueryCreditResult queryCreditResult = null;

            if(isBatchQuery) {
                queryCreditResult = packetHandler.parseQueryBatchDebitPacket(
                        JsonUtils.parse(result.getResponseJson(), String.class), gatewayConfig);
            } else {
                queryCreditResult = packetHandler.parseQueryDebitPacket(
                        JsonUtils.parse(result.getResponseJson(), String.class), gatewayConfig);
            }
            LOGGER.info("---queryCreditResult---" + JsonUtils.toJsonString(queryCreditResult));
            if (!BusinessProcessStatus.SUCCESS.equals(queryCreditResult.getProcessStatus())) {
                LOGGER.info("---返回查询扣款交易流水记录不存在或正在处理中, tradeUuid: " + tradeUuid);
                return;
            }
            if(thirdPartyTransactionRecordService.getThirdPartyTransactionRecordBytradeUuid(tradeUuid) != null){
                LOGGER.info("---返回查询扣款交易流水记录已经存在, tradeUuid: " + tradeUuid);
                return;
            }
                  
            saveTransactionRecord(workingParams.get(ThirdPartyPayPacketHandler.TRD_PARTY_FINANCIAL_CONTRACT_UUID),
                    queryCreditResult);
        } catch (Exception e) {
            LOGGER.error("[" + tradeUuid + "] handler coffer callback result error : " + e.getMessage());
        }
	}
	
	//生成第三方记录
    private void saveTransactionRecord(
            String financialContractUuid, QueryCreditResult queryCreditResult) {
        ThirdPartyTransactionRecord thirdPartyTransactionRecord = new ThirdPartyTransactionRecord();
        thirdPartyTransactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
        thirdPartyTransactionRecord.setMerchantOrderNo(queryCreditResult.getTransactionVoucherNo());
        thirdPartyTransactionRecord.setChannelSequenceNo(queryCreditResult.getChannelSequenceNo());
        thirdPartyTransactionRecord.setBusinessProcessStatus(com.zufangbao.sun.yunxin.entity.barclays.BusinessProcessStatus.SUCCESS);
        thirdPartyTransactionRecord.setCreateTime(new Date());
        thirdPartyTransactionRecord.setTransactionTime(queryCreditResult.getBusinessSuccessTime());
        thirdPartyTransactionRecord.setAuditStatus(InstitutionReconciliationAuditStatus.CREATE);
        thirdPartyTransactionRecord.setFinancialContractUuid(financialContractUuid);
        thirdPartyTransactionRecord.setTransactionTime(queryCreditResult.getBusinessSuccessTime());
        BigDecimal amount = null;
        if (StringUtils.isNotBlank(queryCreditResult.getTransactionAmount())) {
            amount = new BigDecimal(queryCreditResult.getTransactionAmount());
        }
        thirdPartyTransactionRecord.setTransactionAmount(amount);
        thirdPartyTransactionRecord.setBatchNo(queryCreditResult.getBatchNo());
        thirdPartyTransactionRecordService.save(thirdPartyTransactionRecord);
    }

	@Override
	public QueryCreditResult queryDebit(QueryCreditModel queryCreditModel, String financialContractUuid) {
        try {
            String NameSpace=queryCreditModel.getPaymentGateWayName();
//            ThirdPartyPayHandler payHandler =
//                    PaymentHandlerFactory.createThirdPartyPay(PaymentHandlerFactory.getPaymentNameSpace(NameSpace));

            //广银联、新浪直接查交易记录表
            if (PaymentGateWayNameSpace.GATEWAY_TYPE_UNIONPAYGZ.equals(NameSpace) ||
                    PaymentGateWayNameSpace.GATEWAY_TYPE_SINA_PAY.equals(NameSpace)) {
                return new QueryCreditResult(BusinessRequestStatus.FINISH);
            }
            ThirdPartyPayPacketHandler payPacketHandler =
                    PaymentHandlerFactory.createThirdPartyPayPacket(PaymentHandlerFactory.getPaymentNameSpace(NameSpace));
            Map<String, String> workingParams = gatewayConfigService.getByChannelIdentityAndMerchantNo(
                    NameSpace,queryCreditModel.getAccountNo());

            // send async request
            Map<String, String> requestData = null;
            queryCreditModel.setRequestDate(new Date());
            if (StringUtils.isEmpty(queryCreditModel.getBatchNo())) {
                requestData = payPacketHandler.generateQueryDebitPacket(queryCreditModel, workingParams);
            } else {
                requestData = payPacketHandler.generateBatchQueryDebitPacket(queryCreditModel, workingParams);
            }
            LOGGER.info("---request data ---" + JsonUtils.toJsonString(requestData));
            return sendAsyncRequest(queryCreditModel, workingParams, requestData, financialContractUuid);
        } catch (Exception e) {
            LOGGER.error("[" + queryCreditModel.getTransactionVoucherNo() + "] query transaction record error : "
                    + ExceptionUtils.getFullStackTrace(e));
            return new QueryCreditResult(GlobalSpec.ErrorMsg.ERR_SYSTEM_EXCEPTION);
        }
	}


	private QueryCreditResult sendAsyncRequest(QueryCreditModel queryCreditModel
            , Map<String, String> workParms, Map<String, String> requestData
            , String financialContractUuid) throws Exception {

        NotifyApplication notifyApplication = new NotifyApplication();
        notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());
        String reqUrl = workParms.get("requestUrl");
        LOGGER.info("---- request url : " + reqUrl);
        if (PaymentGateWayNameSpace.GATEWAY_TYPE_YIJIFU.equals(
                queryCreditModel.getPaymentGateWayName())) {
            reqUrl = requestData.get("url");
        } else {
            notifyApplication.setRequestParameters((HashMap<String, String>) requestData);
            if (StringUtils.isBlank(reqUrl)) {
                reqUrl = workParms.get("apiUrl");
            }
            if (StringUtils.isBlank(reqUrl)) {
                reqUrl = workParms.get("url");
            }
            if (StringUtils.isBlank(reqUrl)) {
                throw new Exception("send async query credit error : can not get request url.");
            }
        }

        notifyApplication.setRequestUrl(reqUrl);

        notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);
        notifyApplication.setRedundanceMap(new HashMap<String, String>(){{
//            put(ThirdPartyPayPacketHandler.TRD_PARTY_CALLBACK_URL, queryCreditModel.getQueryResultCallBackUrl());
            put(ThirdPartyPayPacketHandler.TRD_PARTY_FINANCIAL_CONTRACT_UUID, financialContractUuid);
            put(ThirdPartyPayPacketHandler.TRD_PARTY_GATEWAY_NAME, queryCreditModel.getPaymentGateWayName());
            put(ThirdPartyPayPacketHandler.TRD_PARTY_QUERY_TYPE, queryCreditModel.isBatchQuery().toString());
            put(ThirdPartyPayPacketHandler.TRD_PARTY_TRADE_UUID, queryCreditModel.getTransactionVoucherNo());
            put(ThirdPartyPayPacketHandler.TRD_PARTY_BATCH_NO, queryCreditModel.getBatchNo());
            put(ThirdPartyPayPacketHandler.TRD_PARTY_ACCOUNT_NO, queryCreditModel.getAccountNo());
        }});
        paymentAsyncJobServer.pushJob(notifyApplication);
        return new QueryCreditResult(BusinessRequestStatus.INPROCESS);
    }


}
