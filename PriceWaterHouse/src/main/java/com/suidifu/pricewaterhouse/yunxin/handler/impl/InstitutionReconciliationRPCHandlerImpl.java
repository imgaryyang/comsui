package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.mq.rpc.request.Request;
import com.suidifu.pricewaterhouse.mq.adapter.ConsistentHashMessageProducer;
import com.suidifu.pricewaterhouse.yunxin.handler.InstitutionReconciliationRPCHandler;
import com.zufangbao.gluon.api.jpmorgan.enums.AccountSide;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.CommonLocalTransactionCommand;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.InstitutionReconciliationPart;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.InstitutionReconciliationRpcModel;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.InstitutionReconciliationType;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.yunxin.entity.audit.TransactionCommandExecutionStatus;
import com.zufangbao.sun.yunxin.entity.audit.TransactionCommandType;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhenghangbo on 28/03/2017.
 */
@Component("institutionReconciliationRPCHandler")
public class InstitutionReconciliationRPCHandlerImpl implements InstitutionReconciliationRPCHandler {

    @Autowired
    private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;

    @Autowired
    private ContractService contractService;

    @Value("${missed.third.party.command.log.handle.bean}")
    private String bean;

    @Value("${missed.third.party.command.log.handle.method}")
    private String method;

    @Resource(name ="messageProducer")
    private ConsistentHashMessageProducer messageProducer;

    private static Log logger = LogFactory.getLog(InstitutionReconciliationRPCHandlerImpl.class);

    @Override
    public void  loopProduceMQInformation(){
        List<ThirdPartyVoucherCommandLog> thirdPartVoucherCommands  = thirdPartVoucherCommandLogService.queryMissedThirdPartyVoucherCommandLog();

        logger.error("pending handle log size:"+thirdPartVoucherCommands.size());
        for(ThirdPartyVoucherCommandLog commandLog:thirdPartVoucherCommands){

            Contract contract = contractService.getContractByUniqueId(commandLog.getContractUniqueId());

            ThirdPartVoucherDetailModel detailModel = commandLog.getparseJsonThirdPartVoucherDetailModel();
            CommonLocalTransactionCommand transCmd = new CommonLocalTransactionCommand(commandLog.getVoucherUuid() ,commandLog.getVoucherNo(),commandLog.getBankTransactionNo(),
                    commandLog.getTradeUuid(),commandLog.getTransactionTime(), commandLog.getFinancialContractUuid(), commandLog.getFinancialContractNo(),
                    "", "", "",
                    AccountSide.DEBIT.ordinal(), TransactionCommandExecutionStatus.SUCCESS.ordinal(), TransactionCommandType.THIRD_PART_VOUCHER_COMMAND_LOG.ordinal(),
                    commandLog.getTranscationGateway().getOrdinal(), detailModel.getCurrency(), detailModel.getAmount(), commandLog.getTranscationCompleteTime(),
                    detailModel.getContractUniqueId(), contract.getUuid(), contract.getContractNo(),
                    detailModel.getReceivableAccountNo(), detailModel.getPaymentBank(), detailModel.getPaymentName(),
                    detailModel.getPaymentAccountNo(), detailModel.getCustomerName(), detailModel.getCustomerIdNo(), detailModel.getComment(), JsonUtils.toJsonString(detailModel.getRepayDetailList()),
                    commandLog.getRepaymentNoJsonList(),commandLog.getCreateTime(), commandLog.getBatchNo());

            if(StringUtils.isEmpty(transCmd.getTradeUuid())){
                logger.error("tradeUuid is empty.thirdPartVoucherCommandLogUuid["+commandLog.getVoucherNo()+"]tradeUuid["+commandLog.getTradeUuid()+"].");
                return;
            }

            InstitutionReconciliationRpcModel model = new InstitutionReconciliationRpcModel(transCmd.getTradeUuid(), InstitutionReconciliationType.THIRD_PARTY_DEDUCT_VOUCHER.ordinal(),
                    InstitutionReconciliationPart.LOCAL.ordinal(), JsonUtils.toJsonString(transCmd));
            Request request = newRequest();
            sendMessage(request,commandLog.getTradeUuid(),commandLog.getTradeUuid(),model);

            logger.info("institutionRecon sendMsg:thirdPartVoucherCommandLogUuid["+transCmd.getTransactionVoucherNo()+"]tradeUuid["+transCmd.getTradeUuid()+"].");
        }

    }



    private void sendMessage(Request request,String uuid,String  businessId,InstitutionReconciliationRpcModel model) {
        request.setUuid(uuid);
        request.setBusinessId(businessId);
        request.setParams(new Object[] {JsonUtils.toJsonString(model)});
        messageProducer.rpc().sendAsync(request,6);
        logger.info(request.getUuid());
    }

    private Request newRequest() {
        Request request = new Request();
        request.setBean(bean);
        request.setMethod(method);
        return request;
    }
}
