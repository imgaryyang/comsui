package com.suidifu.owlman.microservice.model;

import com.suidifu.giotto.model.FastContract;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetailInfoModel;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.DetailAliveStatus;
import com.zufangbao.sun.entity.repayment.order.DetailPayStatus;
import com.zufangbao.sun.entity.repayment.order.ReceivableInAdvanceStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItemCheckFailLog;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentOrderParameters {
    private String repaymentOrderUuid;
    private String repaymentOrderUniqueId;
    private String merId;
    private RepaymentOrderDetail repaymentOrderDetail;
    private int repaymentOrderDetailSize;
    private String financialContractUuid;
    //firstRepaymentWay
    private RepaymentWay repaymentWay;
    private String customerSource;
    private Date orderCreateTime;
    private int repaymentCheckDays;
    private LedgerBook ledgerBook;
    private String contractUniqueId;
    private String contractNo;
    private String financialContractNo;

    public RepaymentOrderParameters(String repaymentOrderUuid, String repaymentOrderUniqueId, String merId,
                                    RepaymentOrderDetail repaymentOrderDetail, String financialContractUuid, RepaymentWay repaymentWay,
                                    String customerSource, Date orderCreateTime, int repaymentCheckDays, LedgerBook ledgerBook, String contractNo, String contractUniqueId, int size, String financialContractNo) {
        super();
        this.repaymentOrderUuid = repaymentOrderUuid;
        this.repaymentOrderUniqueId = repaymentOrderUniqueId;
        this.merId = merId;
        this.repaymentOrderDetail = repaymentOrderDetail;
        this.financialContractUuid = financialContractUuid;

        this.repaymentWay = repaymentWay;
        this.customerSource = customerSource;
        this.orderCreateTime = orderCreateTime;
        this.repaymentCheckDays = repaymentCheckDays;
        this.ledgerBook = ledgerBook;
        this.contractNo = contractNo;
        this.contractUniqueId = contractUniqueId;
        this.repaymentOrderDetailSize = size;
        this.financialContractNo = financialContractNo;
    }

    public RepaymentOrderParameters(RepaymentOrder repaymentOrder, RepaymentOrderDetail repaymentOrderDetail,
                                    RepaymentWay repaymentWay, String customerSource, int repaymentCheckDays, LedgerBook ledgerBook, String contractNo, String contractUniqueId, int size) {
        this(repaymentOrder.getOrderUuid(), repaymentOrder.getOrderUniqueId(), repaymentOrder.getMerId(),
                repaymentOrderDetail, repaymentOrder.getFinancialContractUuid(), repaymentWay, customerSource,
                repaymentOrder.getOrderCreateTime(), repaymentCheckDays, ledgerBook, contractNo, contractUniqueId, size, repaymentOrder.getFinancialContractNo());
    }

    public RepaymentOrderParameters(RepaymentOrder repaymentOrder, RepaymentOrderItemModel RepaymentOrderItemModel, RepaymentOrderDetail repaymentOrderDetail, Contract contract, String merId, FinancialContract financialContract, LedgerBook ledgerBook, int detailSize, RepaymentWay repaymentWay) {
        this(repaymentOrder.getOrderUuid(), repaymentOrder.getOrderUniqueId(), repaymentOrder.getMerId(),
                repaymentOrderDetail, repaymentOrder.getFinancialContractUuid(), repaymentWay, repaymentOrder.getFirstCustomerSource(),
                repaymentOrder.getOrderCreateTime(), financialContract.getRepaymentCheckDays(), ledgerBook, RepaymentOrderItemModel.getContractNo(), contract.getUniqueId(), detailSize, repaymentOrder.getFinancialContractNo());
    }

    public String getRepaymentOrderDetailUuid() {
        return repaymentOrderDetail == null ? "" : repaymentOrderDetail.getRepaymentOrderDetailUuid();
    }

    public FastRepaymentOrderItem toFastRepaymentOrderItem(FastContract fastContract, String repaymentBusinessUuid) {
        FastRepaymentOrderItem fastRepaymentOrderItem = new FastRepaymentOrderItem(null, repaymentOrderDetail.getRepaymentOrderDetailUuid(),
                fastContract.getUniqueId(), fastContract.getContractNo(), fastContract.getUuid(), repaymentOrderDetail.getFormatDetailsTotalAmount(), DetailAliveStatus.CREATED.ordinal()
                , DetailPayStatus.UNPAID.ordinal(), repaymentOrderDetail.getRepaymentWayEnum() == null ? -1 : repaymentOrderDetail.getRepaymentWayEnum().ordinal()
                , repaymentOrderDetail.getRepaymentBusinessNo(), repaymentOrderDetail.getRepaymentBusinessType() == null ? -1 : repaymentOrderDetail.getRepaymentBusinessType().ordinal(), repaymentOrderDetail.getParsedPlannedDate()
                , repaymentOrderUuid, repaymentOrderUniqueId, "", null
                , null, null, null, null
                , null, null, null
                , null, merId, repaymentBusinessUuid, financialContractUuid, new Date(), new Date(), repaymentOrderDetail.getCurrentPeriod(), repaymentOrderDetail.getRepayScheduleNo(), repaymentOrderDetail.getIdentificationMode(), ReceivableInAdvanceStatus.UNRECEIVED.ordinal(), RepaymentOrderDetailInfoModel.parseChargeDetailJsonByDetailMap(repaymentOrderDetail.getFormatRepaymentOrderDetailInfoList()));
        return fastRepaymentOrderItem;
    }

    public RepaymentOrderItemCheckFailLog toRepaymentOrderItemCheckFailLog(String errorMsg, FastContract fastContract) {
        return new RepaymentOrderItemCheckFailLog(repaymentOrderDetail, repaymentOrderUuid, repaymentOrderUniqueId,
                merId, errorMsg, repaymentOrderDetail.getDetailAmountJosnAfterParsed(), financialContractUuid, fastContract, repaymentOrderDetail.getFormatDetailsTotalAmount()
                , repaymentOrderDetail.getCurrentPeriod(), repaymentOrderDetail.getRepayScheduleNo());
    }
}