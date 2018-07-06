package com.suidifu.bridgewater.handler.common.impl.v2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.common.v2.ISendHttpToJPMorganHandler;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;

/**
 * Created by zhenghangbo on 23/05/2017.
 */

@Component("iSendHttpToJPMorganHandler")
public class ISendHttpToJPMorganHandlerImpl implements ISendHttpToJPMorganHandler {

    @Autowired
    private JpmorganApiHelper jpmorganApiHelper;


    @Autowired
    private GenericDaoSupport genericDaoSupport;

    private static final Log logger = LogFactory.getLog(ISendHttpToJPMorganHandlerImpl.class);

    @Override
    public void processingAndUpdateRemittanceInfo_NoRollback(
            List<TradeSchedule> tradeSchedules, String remittanceApplicationUuid, String requestNo) {

        String localKeyWord="BRIDGEWATER[requestNo="+requestNo+";remittanceApplicationUuid="+remittanceApplicationUuid+
                "]";
        String remoteKeyWord="==SENTTO==>>JPMORGAN[";
        for(TradeSchedule trade:tradeSchedules)
        {
            remoteKeyWord+="SourceMessageUuid="+trade.getSourceMessageUuid()+";";
        }
        remoteKeyWord+="]}";
        String requestBody = JSON.toJSONString(tradeSchedules, SerializerFeature.DisableCircularReferenceDetect);

        logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN +localKeyWord+remoteKeyWord+ GloableLogSpec
            .RawData(requestBody));

        Result result = jpmorganApiHelper.sendBatchTradeSchedules(tradeSchedules,requestBody);

        if(!result.isValid()) {
            String httpStatus = (String) result.getData().getOrDefault(HttpClientUtils.DATA_RESPONSE_HTTP_STATUS, "");
            logger.error("放款请求受理失败，递交对端失败("+httpStatus+")!");
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+ localKeyWord+ "ERR:[放款请求受理失败，递交对端失败!("+httpStatus+")]"+ GloableLogSpec
                .RawData(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect)));

            updateRemittanceInfoAfterSendFailBy(remittanceApplicationUuid, httpStatus + ApiMessageUtil
                .getMessage(ApiResponseCode.SYSTEM_BUSY));

            throw new ApiException(ApiResponseCode.SYSTEM_BUSY);
        }

        String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
        Result responseResult = JsonUtils.parse(responseStr, Result.class);
        if(responseResult != null && !responseResult.isValid()) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+ "ERR:[放款请求受理失败，对端响应"+responseResult.getMessage()+"]"+ GloableLogSpec
                .RawData(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect)));

            logger.error("放款请求受理失败，对端响应（"+responseResult.getMessage()+"）!");
            updateRemittanceInfoAfterSendFailBy(remittanceApplicationUuid, "放款请求受理失败!");
            throw new ApiException(ApiResponseCode.SYSTEM_ERROR, "放款请求受理失败!");
        }

        //捕捉异常，防止DB异常，返回系统错误
        try {
            updateRemittanceInfoAfterSendSuccessBy(remittanceApplicationUuid);
        } catch (Exception e) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+ "ERR:[落盘对端处理中失败]");
            e.printStackTrace();
        }
        logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+"[SUCC:放款请求受理成功]");
    }

    /**
     * 更新放款状态（交易受理失败）
     */
    private void updateRemittanceInfoAfterSendFailBy(String remittanceApplicationUuid, String executionRemark) {
        Map<String, Object> paramsForFail = new HashMap<String, Object>();
        paramsForFail.put("executionStatus", ExecutionStatus.FAIL);
        paramsForFail.put("executionRemark", executionRemark);
        paramsForFail.put("lastModifiedTime", new Date());
        paramsForFail.put("remittanceApplicationUuid", remittanceApplicationUuid);

        executeHqlForRemittanceFail("RemittanceApplication", paramsForFail);
        executeHqlForRemittanceFail("RemittanceApplicationDetail", paramsForFail);
        executeHqlForRemittanceFail("RemittancePlan", paramsForFail);
        executeHqlForRemittanceFail("RemittancePlanExecLog", paramsForFail);
    }

    private int executeHqlForRemittanceFail(String tableName, Map<String, Object> paramsForFail) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE ");
        buffer.append(tableName);
        buffer.append(" SET executionStatus =:executionStatus, executionRemark =:executionRemark, lastModifiedTime =:lastModifiedTime WHERE remittanceApplicationUuid =:remittanceApplicationUuid");
        return genericDaoSupport.executeHQL(buffer.toString(), paramsForFail);
    }

    /**
     * 更新交易接收方状态，为对端（交易受理成功）
     * @param remittanceApplicationUuid
     */
    private void updateRemittanceInfoAfterSendSuccessBy(
            String remittanceApplicationUuid) {
        if(StringUtils.isEmpty(remittanceApplicationUuid)) {
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transactionRecipient", TransactionRecipient.OPPOSITE.ordinal());
        params.put("remittanceApplicationUuid", remittanceApplicationUuid);
        genericDaoSupport.executeSQL(
                "UPDATE t_remittance_application "
                        + "SET transaction_recipient =:transactionRecipient "
                        + "WHERE remittance_application_uuid =:remittanceApplicationUuid", params);
    }

}

