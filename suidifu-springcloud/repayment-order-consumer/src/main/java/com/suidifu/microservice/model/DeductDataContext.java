package com.suidifu.microservice.model;

import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductApplicationDetailInfoModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductPlanModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扣款数据上下文
 *
 * @author wukai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductDataContext {
    private String requestNo;

    private String callbackUrl;

    private String batchDeductApplicationUuid;

    private String deductApplicaitonUuid;

    private DeductRequestModel deductRequestModel;

    private List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfos = new LinkedList<>();

    // 拆单的最佳通道信息
    private PaymentChannelAndSignUpInfo paymentChannelAndSignUpInfoForSplitMode = null;

    private List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfoForMultiChannels = new LinkedList<>();

    private List<DeductPlanModel> deductPlanModels = new ArrayList<>();

    private List<TradeSchedule> tradeSchedules = new ArrayList<>();

    private DeductMode deductMode;

    private String assetSetUuid;

    private Map<String, DeductApplicationDetailInfoModel> deductApplicationDetailInfoMap;

    private String financialContractUuid;

    private List<Date> exeTimeIntervals;

    public DeductDataContext(DeductRequestModel deductRequestModel) {
        super();
        this.setDeductRequestModel(deductRequestModel);
    }

    public void setDeductRequestModel(DeductRequestModel deductRequestModel) {
        this.deductRequestModel = deductRequestModel;
        this.callbackUrl = deductRequestModel.getUrlForMorganStanletCallback();
        this.requestNo = deductRequestModel.getRequestNo();
        this.batchDeductApplicationUuid = deductRequestModel.getBatchDeductApplicationUuid();
        this.deductApplicaitonUuid = deductRequestModel.getDeductApplicationUuid();
    }

    public List<Date> getExecutimeIntervalsWithMulitPaymentChannelSize() {
        List<Date> effective_time_list = new ArrayList<>(getPaymentChannelAndSignUpInfoForMultiChannels().size());
        if (getExeTimeIntervals() != null) {
            effective_time_list.addAll(getExeTimeIntervals());
        }
        return effective_time_list;
    }
}