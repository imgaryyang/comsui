package com.suidifu.bridgewater.handler.single.v2;

import java.math.BigDecimal;

import com.suidifu.bridgewater.model.v2.ContractAccountModel;
import com.suidifu.bridgewater.model.v2.StandardDeductModel;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;

/**
 * Created by zhenghangbo on 02/05/2017.
 */
public interface TradeScheduleProcessor {

    public  TradeSchedule castTradeScheduleByDeductPlan(DeductPlan reDeductPlan);

    public TradeSchedule castCommonTradeSchedule(String batchUuid, PaymentChannelSummaryInfo deductChannelInfo, ContractAccountModel contractAccountModel, FinancialContract financialContract, BigDecimal plannedAmount, String mobile);
    
    public  TradeSchedule castTradeScheduleByDeductPlan(DeductPlan deductPlan, StandardDeductModel deductModel);
}
