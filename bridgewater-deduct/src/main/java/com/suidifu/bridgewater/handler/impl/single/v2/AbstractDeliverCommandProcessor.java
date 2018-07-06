package com.suidifu.bridgewater.handler.impl.single.v2;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.ApiMessageUtil;

/**
 * Created by zhenghangbo on 10/05/2017.
 */
public class AbstractDeliverCommandProcessor{


    @Autowired
    private JpmorganApiHelper jpmorganApiHelper;

    private static final Log logger = LogFactory.getLog(AbstractDeliverCommandProcessor.class);


    public void deliverDeductCommand_NoRollback(List<TradeSchedule> tradeScheduleList, String deductApplicationUuid, String requestNo, String localKeyWord, String remoteKeyWord) {


        String requestBody = JSON.toJSONString(tradeScheduleList  , SerializerFeature.DisableCircularReferenceDetect);

        logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN +localKeyWord+remoteKeyWord+GloableLogSpec.RawData(requestBody));

        Result result = jpmorganApiHelper.sendBatchTradeSchedules(tradeScheduleList, requestBody);

        if (!result.isValid()) {
            logger.error("扣款请求受理失败，递交对端失败!");
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+ localKeyWord+ "[扣款请求受理失败，递交对端失败!]"+GloableLogSpec.RawData(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect)));
            throw new ApiException(ApiResponseCode.SYSTEM_BUSY,ApiMessageUtil.getMessage(ApiResponseCode.SYSTEM_BUSY));
        }

        Result responseResult = parseResult(result);
        if (responseResult != null && !responseResult.isValid()) {
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+ "[扣款请求受理失败，对端响应"+responseResult.getMessage()+"]"+GloableLogSpec.RawData(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect)));
            logger.error("扣款请求受理失败，对端响应（" + responseResult.getMessage() + "）!");
            throw new ApiException(ApiResponseCode.SYSTEM_ERROR, "扣款请求受理失败!原因为["+responseResult.getMessage()+"]");
        }

    }


    private Result parseResult(Result result) {
        String queryResString = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
        Result responseResult = JsonUtils.parse(queryResString, Result.class);
        return responseResult;
    }
}
