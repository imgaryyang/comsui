package com.suidifu.microservice.handler.impl;

import com.demo2do.core.BusinessException;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.microservice.handler.TransactionApiHandler;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.sun.entity.financial.PaymentChannelInformation;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component("transactionApiHandler")
public class TransactionApiHandlerImpl implements TransactionApiHandler {
    @Resource
    private PaymentChannelInformationService paymentChannelInformationService;

    @Value("${barclaysHost}")
    private String barclaysHost;

    @Override
    public void queryCreditModelByNotifyServer(String tradeUuid,
                                               Integer paymentGateWayInt,
                                               String financialContractUuid,
                                               String batchNo) {
        QueryCreditModel queryCreditModel = new QueryCreditModel();
        queryCreditModel.setTransactionVoucherNo(tradeUuid);
        queryCreditModel.setRequestSequenceNo(UUIDUtil.snowFlakeIdString());

        //支付通道
        PaymentInstitutionName paymentType = EnumUtil.fromOrdinal(PaymentInstitutionName.class,
                paymentGateWayInt);

        PaymentChannelInformation paymentChannelInformation =
                paymentChannelInformationService.getByFinancialContractUUidAndGateway(financialContractUuid, paymentType);
        if (null == paymentChannelInformation) {
            throw new BusinessException("---[" + tradeUuid + "] 无法获取信托合同通道配置信息---");
        }
        queryCreditModel.setAccountNo(paymentChannelInformation.getOutlierChannelName());
        queryCreditModel.setPaymentGateWayName(paymentType.getBarclaysName());
        queryCreditModel.setBatchNo(batchNo);

        String requestUrl = barclaysHost.concat("paymentAsync/asyncQueryStatus");

        String queryParamsJson = JsonUtils.toJsonString(queryCreditModel);
        Map<String, String> paramsMap = new HashMap<>(1);
        paramsMap.put("queryCreditModelJson", queryParamsJson);
        paramsMap.put("financialContractUuid", financialContractUuid);
        Result result = HttpClientUtils.executePostRequest(requestUrl, paramsMap, null);
        log.info("---[" + tradeUuid + "] call barclays async response : " + result.getCode() + "---");
    }
}