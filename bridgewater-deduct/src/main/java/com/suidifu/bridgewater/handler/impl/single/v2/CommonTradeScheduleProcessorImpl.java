package com.suidifu.bridgewater.handler.impl.single.v2;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.single.v2.TradeScheduleProcessor;
import com.suidifu.bridgewater.model.v2.ContractAccountModel;
import com.suidifu.bridgewater.model.v2.StandardDeductModel;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;

/**
 * Created by zhenghangbo on 02/05/2017.
 */
@Component("commonTradeScheduleProcessor")
public class CommonTradeScheduleProcessorImpl extends AbstractTradeScheduleProcessor implements TradeScheduleProcessor {

	@Value("#{config['urlForJpmorganCallback']}")
	private String urlForJpmorganCallback;

    @Override
    public TradeSchedule castTradeScheduleByDeductPlan(DeductPlan deductPlan) {

            BigDecimal plannedAmount = deductPlan.getPlannedTotalAmount();
            String destinationAccountName = deductPlan.getCpBankAccountHolder();
            String destinationAccountNo = deductPlan.getCpBankCardNo();

            String destinationAccountAppendix = assembleDestinationAccountAppendixMap(deductPlan.getCpIdNumber(), deductPlan.getMobile());
            String destinationBankInfo = assembleDestinationBankInfoMap(deductPlan.getCpBankCode(), deductPlan.getCpBankProvince(), deductPlan.getCpBankCity(), deductPlan.getCpBankName());

            String outlierTransactionUuid = deductPlan.getDeductPlanUuid();
            String fstPaymentChannelUuid = deductPlan.getPaymentChannelUuid();

            String batchUuid = deductPlan.getDeductApplicationUuid();

            String fstGatewayRouterInfo = getFstGateWayRouterInfo(null,deductPlan.getCpBankCode(), deductPlan.getPgClearingAccount(), null);

            return new TradeSchedule(com.zufangbao.gluon.api.jpmorgan.enums.AccountSide.DEBIT, destinationAccountName, destinationAccountNo, destinationAccountAppendix, destinationBankInfo, "", outlierTransactionUuid, "", fstPaymentChannelUuid, null, plannedAmount, batchUuid, null, null, null, fstGatewayRouterInfo, urlForJpmorganCallback);
    }


    @Override
    public TradeSchedule castCommonTradeSchedule(String batchUuid, PaymentChannelSummaryInfo deductChannelInfo,
                                                 ContractAccountModel contractAccountModel, FinancialContract financialContract, BigDecimal plannedAmount, String mobile) {


        String fstGatewayRouterInfo = getFstGateWayRouterInfo(contractAccountModel.getBankCode(),contractAccountModel.getStandardBankCode(),deductChannelInfo.getClearingNo(), null);

        String destinationAccountAppendix = assembleDestinationAccountAppendixMap(contractAccountModel.getIdCardNum(),mobile);

        String destinationBankInfo = assembleDestinationBankInfoMap(contractAccountModel.getStandardBankCode(),contractAccountModel.getProvinceCode(),contractAccountModel.getCityCode(),contractAccountModel.getBank());

        return new TradeSchedule(com.zufangbao.gluon.api.jpmorgan.enums.AccountSide.DEBIT, contractAccountModel.getPayerName(),
                contractAccountModel.getPayAcNo(), destinationAccountAppendix, destinationBankInfo, "",
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), deductChannelInfo.getChannelServiceUuid(), null, plannedAmount,
                batchUuid, null, null, null, 0,  deductChannelInfo.getPaymentGateway(),
                deductChannelInfo.getPaymentChannelName(), fstGatewayRouterInfo);
    }


	@Override
	public TradeSchedule castTradeScheduleByDeductPlan(DeductPlan deductPlan,
			StandardDeductModel deductModel) {

        BigDecimal plannedAmount = deductPlan.getPlannedTotalAmount();
        String destinationAccountName = deductPlan.getCpBankAccountHolder();
        String destinationAccountNo = deductPlan.getCpBankCardNo();

        String destinationAccountAppendix = assembleDestinationAccountAppendixMap(deductPlan.getCpIdNumber(), deductPlan.getMobile());
        String destinationBankInfo = assembleDestinationBankInfoMap(deductPlan.getCpBankCode(), deductPlan.getCpBankProvince(), deductPlan.getCpBankCity(), deductPlan.getCpBankName());

        String outlierTransactionUuid = deductPlan.getDeductPlanUuid();
        String fstPaymentChannelUuid = deductPlan.getPaymentChannelUuid();

        String batchUuid = deductPlan.getDeductApplicationUuid();

        String fstGatewayRouterInfo = getFstGateWayRouterInfo(null,deductPlan.getCpBankCode(), deductPlan.getPgClearingAccount(), deductModel.getProtocolNo());

        return new TradeSchedule(com.zufangbao.gluon.api.jpmorgan.enums.AccountSide.DEBIT, destinationAccountName, destinationAccountNo, destinationAccountAppendix, destinationBankInfo, "", outlierTransactionUuid, "", fstPaymentChannelUuid, null, plannedAmount, batchUuid, null, null, null, fstGatewayRouterInfo, urlForJpmorganCallback);
	}
}
