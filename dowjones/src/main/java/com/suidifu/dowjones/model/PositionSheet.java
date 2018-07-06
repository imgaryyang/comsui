package com.suidifu.dowjones.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * 回款汇总表
 *
 * @author veda
 */
@Data
public class PositionSheet {

    // 日期
    private String date;
    // 资金追加
    private String capitalAddition;

    // 当日放款笔数
    private String remittanceCount;
    // 当日放款金额
    private String remittanceAmount;

    // 当日扣款笔数
    private String deductCount;
    // 扣款金额
    private String deductAmount;
    // 扣款本金
    private String deductPrincipal;
    // 扣款利息
    private String deductInterest;
    // 扣款服务费
    private String deductServiceFee;
    // 扣款技术维护费
    private String deductTechFee;
    // 扣款其他费用
    private String deductOtherFee;
    // 扣款罚息
    private String deductOverdueFeePenalty;
    // 扣款逾期违约金
    private String deductOverdueFeeObligation;
    // 扣款逾期服务费
    private String deductOverdueFeeService;
    // 扣款逾期其他费用
    private String deductOverdueFeeOther;

    // 线下还款笔数
    private String offlineRepaymentCount;
    // 线下还款金额
    private String offlineRepaymentAmount;
    // 线下还款本金
    private String offlineRepaymentPrincipal;
    // 线下还款利息
    private String offlineRepaymentInterest;
    // 线下还款服务费
    private String offlineRepaymentServiceFee;
    // 线下还款服务费
    private String offlineRepaymentTechFee;
    // 线下还款其他费用
    private String offlineRepaymentOtherFee;
    // 线下还款罚息
    private String offlineRepaymentOverdueFeePenalty;
    // 线下还款逾期违约金
    private String offlineRepaymentOverdueFeeObligation;
    // 线下还款逾期服务费
    private String offlineRepaymentOverdueFeeService;
    // 线下还款逾期其他费用
    private String offlineRepaymentOverdueFeeOther;

    // 第一通道名称
    private String fstPaymentChannelName;
    // 第一通道笔数
    private String fstPaymentChannelCount;
    // 第一通道金额
    private String fstPaymentChannelAmount;
    // 第一通道本金
    private String fstPaymentChannelPrincipal;
    // 第一通道利息
    private String fstPaymentChannelInterest;
    // 第一通道服务费
    private String fstPaymentChannelServiceFee;
    // 第一通道服务费
    private String fstPaymentChannelTechFee;
    // 第一通道其他费用
    private String fstPaymentChannelOtherFee;
    // 第一通道罚息
    private String fstPaymentChannelOverdueFeePenalty;
    // 第一通道逾期违约金
    private String fstPaymentChannelOverdueFeeObligation;
    // 第一通道逾期服务费
    private String fstPaymentChannelOverdueFeeService;
    // 第一通道逾期其他费用
    private String fstPaymentChannelOverdueFeeOther;

    // 第二通道名称
    private String sndPaymentChannelName;
    // 第二通道笔数
    private String sndPaymentChannelCount;
    // 第二通道金额
    private String sndPaymentChannelAmount;
    // 第二通道本金
    private String sndPaymentChannelPrincipal;
    // 第二通道利息
    private String sndPaymentChannelInterest;
    // 第二通道服务费
    private String sndPaymentChannelServiceFee;
    // 第二通道服务费
    private String sndPaymentChannelTechFee;
    // 第二通道其他费用
    private String sndPaymentChannelOtherFee;
    // 第二通道罚息
    private String sndPaymentChannelOverdueFeePenalty;
    // 第二通道逾期违约金
    private String sndPaymentChannelOverdueFeeObligation;
    // 第二通道逾期服务费
    private String sndPaymentChannelOverdueFeeService;
    // 第二通道逾期其他费用
    private String sndPaymentChannelOverdueFeeOther;

    // 第三通道名称
    private String trdPaymentChannelName;
    // 第三通道笔数
    private String trdPaymentChannelCount;
    // 第三通道金额
    private String trdPaymentChannelAmount;
    // 第三通道本金
    private String trdPaymentChannelPrincipal;
    // 第三通道利息
    private String trdPaymentChannelInterest;
    // 第三通道服务费
    private String trdPaymentChannelServiceFee;
    // 第三通道服务费
    private String trdPaymentChannelTechFee;
    // 第三通道其他费用
    private String trdPaymentChannelOtherFee;
    // 第三通道罚息
    private String trdPaymentChannelOverdueFeePenalty;
    // 第三通道逾期违约金
    private String trdPaymentChannelOverdueFeeObligation;
    // 第三通道逾期服务费
    private String trdPaymentChannelOverdueFeeService;
    // 第三通道逾期其他费用
    private String trdPaymentChannelOverdueFeeOther;


    // 第四通道笔数
    private String fthPaymentChannelName;
    // 第四通道笔数
    private String fthPaymentChannelCount;
    // 第四通道金额
    private String fthPaymentChannelAmount;
    // 第四通道本金
    private String fthPaymentChannelPrincipal;
    // 第四通道利息
    private String fthPaymentChannelInterest;
    // 第四通道服务费
    private String fthPaymentChannelServiceFee;
    // 第四通道服务费
    private String fthPaymentChannelTechFee;
    // 第四通道其他费用
    private String fthPaymentChannelOtherFee;
    // 第四通道罚息
    private String fthPaymentChannelOverdueFeePenalty;
    // 第四通道逾期违约金
    private String fthPaymentChannelOverdueFeeObligation;
    // 第四通道逾期服务费
    private String fthPaymentChannelOverdueFeeService;
    // 第四通道逾期其他费用
    private String fthPaymentChannelOverdueFeeOther;

    // 第五通道名称
    private String fifthPaymentChannelName;
    // 第五通道笔数
    private String fifthPaymentChannelCount;
    // 第五通道金额
    private String fifthPaymentChannelAmount;
    // 第五通道本金
    private String fifthPaymentChannelPrincipal;
    // 第五通道利息
    private String fifthPaymentChannelInterest;
    // 第五通道服务费
    private String fifthPaymentChannelServiceFee;
    // 第五通道服务费
    private String fifthPaymentChannelTechFee;
    // 第五通道其他费用
    private String fifthPaymentChannelOtherFee;
    // 第五通道罚息
    private String fifthPaymentChannelOverdueFeePenalty;
    // 第五通道逾期违约金
    private String fifthPaymentChannelOverdueFeeObligation;
    // 第五通道逾期服务费
    private String fifthPaymentChannelOverdueFeeService;
    // 第五通道逾期其他费用
    private String fifthPaymentChannelOverdueFeeOther;


    public void fillPaymentChannelData(Map<String, Object> item, Integer order) {

        if (Objects.equals(1, order)) {
            String cashFlowChannelType = item.get("cash_flow_channel_type") == null ? null : item.get("cash_flow_channel_type").toString();
            if (cashFlowChannelType != null) {
                this.setFstPaymentChannelName(Const.CHANNEL_NAME_MAP.getOrDefault(cashFlowChannelType, ""));
            }
            this.setFstPaymentChannelCount(item.get("count") == null ? null : item.get("count").toString());
            this.setFstPaymentChannelPrincipal(item.get("loan_asset_principal") == null ? null : item.get("loan_asset_principal").toString());
            this.setFstPaymentChannelInterest(item.get("loan_asset_interest") == null ? null : item.get("loan_asset_interest").toString());
            this.setFstPaymentChannelServiceFee(item.get("loan_service_fee") == null ? null : item.get("loan_service_fee").toString());
            this.setFstPaymentChannelTechFee(item.get("loan_tech_fee") == null ? null : item.get("loan_tech_fee").toString());
            this.setFstPaymentChannelOtherFee(item.get("loan_other_fee") == null ? null : item.get("loan_other_fee").toString());
            this.setFstPaymentChannelOverdueFeePenalty(item.get("overdue_fee_penalty") == null ? null : item.get("overdue_fee_penalty").toString());
            this.setFstPaymentChannelOverdueFeeObligation(item.get("overdue_fee_obligation") == null ? null : item.get("overdue_fee_obligation").toString());
            this.setFstPaymentChannelOverdueFeeService(item.get("overdue_fee_service") == null ? null : item.get("overdue_fee_service").toString());
            this.setFstPaymentChannelOverdueFeeOther(item.get("overdue_fee_other") == null ? null : item.get("overdue_fee_other").toString());
            BigDecimal amount = skipNullGetFstPaymentChannelPrincipal()
                    .add(skipNullGetFstPaymentChannelInterest())
                    .add(skipNullGetFstPaymentChannelServiceFee())
                    .add(skipNullGetFstPaymentChannelTechFee())
                    .add(skipNullGetFstPaymentChannelOtherFee())
                    .add(skipNullGetFstPaymentChannelOverdueFeePenalty())
                    .add(skipNullGetFstPaymentChannelOverdueFeeObligation())
                    .add(skipNullGetFstPaymentChannelOverdueFeeService())
                    .add(skipNullGetFstPaymentChannelOverdueFeeOther());
            this.setFstPaymentChannelAmount(amount.toString());
        } else if (Objects.equals(2, order)) {
            String cashFlowChannelType = item.get("cash_flow_channel_type") == null ? null : item.get("cash_flow_channel_type").toString();
            if (cashFlowChannelType != null) {
                this.setSndPaymentChannelName(Const.CHANNEL_NAME_MAP.getOrDefault(cashFlowChannelType, ""));
            }
            this.setSndPaymentChannelCount(item.get("count") == null ? null : item.get("count").toString());
            this.setSndPaymentChannelPrincipal(item.get("loan_asset_principal") == null ? null : item.get("loan_asset_principal").toString());
            this.setSndPaymentChannelInterest(item.get("loan_asset_interest") == null ? null : item.get("loan_asset_interest").toString());
            this.setSndPaymentChannelServiceFee(item.get("loan_service_fee") == null ? null : item.get("loan_service_fee").toString());
            this.setSndPaymentChannelTechFee(item.get("loan_tech_fee") == null ? null : item.get("loan_tech_fee").toString());
            this.setSndPaymentChannelOtherFee(item.get("loan_other_fee") == null ? null : item.get("loan_other_fee").toString());
            this.setSndPaymentChannelOverdueFeePenalty(item.get("overdue_fee_penalty") == null ? null : item.get("overdue_fee_penalty").toString());
            this.setSndPaymentChannelOverdueFeeObligation(item.get("overdue_fee_obligation") == null ? null : item.get("overdue_fee_obligation").toString());
            this.setSndPaymentChannelOverdueFeeService(item.get("overdue_fee_service") == null ? null : item.get("overdue_fee_service").toString());
            this.setSndPaymentChannelOverdueFeeOther(item.get("overdue_fee_other") == null ? null : item.get("overdue_fee_other").toString());
            BigDecimal amount = skipNullGetSndPaymentChannelPrincipal()
                    .add(skipNullGetSndPaymentChannelInterest())
                    .add(skipNullGetSndPaymentChannelServiceFee())
                    .add(skipNullGetSndPaymentChannelTechFee())
                    .add(skipNullGetSndPaymentChannelOtherFee())
                    .add(skipNullGetSndPaymentChannelOverdueFeePenalty())
                    .add(skipNullGetSndPaymentChannelOverdueFeeObligation())
                    .add(skipNullGetSndPaymentChannelOverdueFeeService())
                    .add(skipNullGetSndPaymentChannelOverdueFeeOther());
            this.setSndPaymentChannelAmount(amount.toString());
        } else if (Objects.equals(3, order)) {
            String cashFlowChannelType = item.get("cash_flow_channel_type") == null ? null : item.get("cash_flow_channel_type").toString();
            if (cashFlowChannelType != null) {
                this.setTrdPaymentChannelName(Const.CHANNEL_NAME_MAP.getOrDefault(cashFlowChannelType, ""));
            }
            this.setTrdPaymentChannelCount(item.get("count") == null ? null : item.get("count").toString());
            this.setTrdPaymentChannelPrincipal(item.get("loan_asset_principal") == null ? null : item.get("loan_asset_principal").toString());
            this.setTrdPaymentChannelInterest(item.get("loan_asset_interest") == null ? null : item.get("loan_asset_interest").toString());
            this.setTrdPaymentChannelServiceFee(item.get("loan_service_fee") == null ? null : item.get("loan_service_fee").toString());
            this.setTrdPaymentChannelTechFee(item.get("loan_tech_fee") == null ? null : item.get("loan_tech_fee").toString());
            this.setTrdPaymentChannelOtherFee(item.get("loan_other_fee") == null ? null : item.get("loan_other_fee").toString());
            this.setTrdPaymentChannelOverdueFeePenalty(item.get("overdue_fee_penalty") == null ? null : item.get("overdue_fee_penalty").toString());
            this.setTrdPaymentChannelOverdueFeeObligation(item.get("overdue_fee_obligation") == null ? null : item.get("overdue_fee_obligation").toString());
            this.setTrdPaymentChannelOverdueFeeService(item.get("overdue_fee_service") == null ? null : item.get("overdue_fee_service").toString());
            this.setTrdPaymentChannelOverdueFeeOther(item.get("overdue_fee_other") == null ? null : item.get("overdue_fee_other").toString());
            BigDecimal amount = skipNullGetTrdPaymentChannelPrincipal()
                    .add(skipNullGetTrdPaymentChannelInterest())
                    .add(skipNullGetTrdPaymentChannelServiceFee())
                    .add(skipNullGetTrdPaymentChannelTechFee())
                    .add(skipNullGetTrdPaymentChannelOtherFee())
                    .add(skipNullGetTrdPaymentChannelOverdueFeePenalty())
                    .add(skipNullGetTrdPaymentChannelOverdueFeeObligation())
                    .add(skipNullGetTrdPaymentChannelOverdueFeeService())
                    .add(skipNullGetTrdPaymentChannelOverdueFeeOther());
            this.setTrdPaymentChannelAmount(amount.toString());
        } else if (Objects.equals(4, order)) {
            String cashFlowChannelType = item.get("cash_flow_channel_type") == null ? null : item.get("cash_flow_channel_type").toString();
            if (cashFlowChannelType != null) {
                this.setFthPaymentChannelName(Const.CHANNEL_NAME_MAP.getOrDefault(cashFlowChannelType, ""));
            }
            this.setFthPaymentChannelCount(item.get("count") == null ? null : item.get("count").toString());
            this.setFthPaymentChannelPrincipal(item.get("loan_asset_principal") == null ? null : item.get("loan_asset_principal").toString());
            this.setFthPaymentChannelInterest(item.get("loan_asset_interest") == null ? null : item.get("loan_asset_interest").toString());
            this.setFthPaymentChannelServiceFee(item.get("loan_service_fee") == null ? null : item.get("loan_service_fee").toString());
            this.setFthPaymentChannelTechFee(item.get("loan_tech_fee") == null ? null : item.get("loan_tech_fee").toString());
            this.setFthPaymentChannelOtherFee(item.get("loan_other_fee") == null ? null : item.get("loan_other_fee").toString());
            this.setFthPaymentChannelOverdueFeePenalty(item.get("overdue_fee_penalty") == null ? null : item.get("overdue_fee_penalty").toString());
            this.setFthPaymentChannelOverdueFeeObligation(item.get("overdue_fee_obligation") == null ? null : item.get("overdue_fee_obligation").toString());
            this.setFthPaymentChannelOverdueFeeService(item.get("overdue_fee_service") == null ? null : item.get("overdue_fee_service").toString());
            this.setFthPaymentChannelOverdueFeeOther(item.get("overdue_fee_other") == null ? null : item.get("overdue_fee_other").toString());
            BigDecimal amount = skipNullGetFthPaymentChannelPrincipal()
                    .add(skipNullGetFthPaymentChannelInterest())
                    .add(skipNullGetFthPaymentChannelServiceFee())
                    .add(skipNullGetFthPaymentChannelTechFee())
                    .add(skipNullGetFthPaymentChannelOtherFee())
                    .add(skipNullGetFthPaymentChannelOverdueFeePenalty())
                    .add(skipNullGetFthPaymentChannelOverdueFeeObligation())
                    .add(skipNullGetFthPaymentChannelOverdueFeeService())
                    .add(skipNullGetFthPaymentChannelOverdueFeeOther());
            this.setFthPaymentChannelAmount(amount.toString());
        } else if (Objects.equals(5, order)) {
            String cashFlowChannelType = item.get("cash_flow_channel_type") == null ? null : item.get("cash_flow_channel_type").toString();
            if (cashFlowChannelType != null) {
                this.setFifthPaymentChannelName(Const.CHANNEL_NAME_MAP.getOrDefault(cashFlowChannelType, ""));
            }
            this.setFifthPaymentChannelCount(item.get("count") == null ? null : item.get("count").toString());
            this.setFifthPaymentChannelPrincipal(item.get("loan_asset_principal") == null ? null : item.get("loan_asset_principal").toString());
            this.setFifthPaymentChannelInterest(item.get("loan_asset_interest") == null ? null : item.get("loan_asset_interest").toString());
            this.setFifthPaymentChannelServiceFee(item.get("loan_service_fee") == null ? null : item.get("loan_service_fee").toString());
            this.setFifthPaymentChannelTechFee(item.get("loan_tech_fee") == null ? null : item.get("loan_tech_fee").toString());
            this.setFifthPaymentChannelOtherFee(item.get("loan_other_fee") == null ? null : item.get("loan_other_fee").toString());
            this.setFifthPaymentChannelOverdueFeePenalty(item.get("overdue_fee_penalty") == null ? null : item.get("overdue_fee_penalty").toString());
            this.setFifthPaymentChannelOverdueFeeObligation(item.get("overdue_fee_obligation") == null ? null : item.get("overdue_fee_obligation").toString());
            this.setFifthPaymentChannelOverdueFeeService(item.get("overdue_fee_service") == null ? null : item.get("overdue_fee_service").toString());
            this.setFifthPaymentChannelOverdueFeeOther(item.get("overdue_fee_other") == null ? null : item.get("overdue_fee_other").toString());
            BigDecimal amount = skipNullGetFifthPaymentChannelPrincipal()
                    .add(skipNullGetFifthPaymentChannelInterest())
                    .add(skipNullGetFifthPaymentChannelServiceFee())
                    .add(skipNullGetFifthPaymentChannelTechFee())
                    .add(skipNullGetFifthPaymentChannelOtherFee())
                    .add(skipNullGetFifthPaymentChannelOverdueFeePenalty())
                    .add(skipNullGetFifthPaymentChannelOverdueFeeObligation())
                    .add(skipNullGetFifthPaymentChannelOverdueFeeService())
                    .add(skipNullGetFifthPaymentChannelOverdueFeeOther());
            this.setFifthPaymentChannelAmount(amount.toString());
        }
    }


    public void fillOfflineRepaymentData(Map<String, Object> item) {
        this.setOfflineRepaymentCount(item.get("count") == null ? null : item.get("count").toString());
        this.setOfflineRepaymentPrincipal(item.get("loan_asset_principal") == null ? null : item.get("loan_asset_principal").toString());
        this.setOfflineRepaymentInterest(item.get("loan_asset_interest") == null ? null : item.get("loan_asset_interest").toString());
        this.setOfflineRepaymentServiceFee(item.get("loan_service_fee") == null ? null : item.get("loan_service_fee").toString());
        this.setOfflineRepaymentTechFee(item.get("loan_tech_fee") == null ? null : item.get("loan_tech_fee").toString());
        this.setOfflineRepaymentOtherFee(item.get("loan_other_fee") == null ? null : item.get("loan_other_fee").toString());
        this.setOfflineRepaymentOverdueFeePenalty(item.get("overdue_fee_penalty") == null ? null : item.get("overdue_fee_penalty").toString());
        this.setOfflineRepaymentOverdueFeeObligation(item.get("overdue_fee_obligation") == null ? null : item.get("overdue_fee_obligation").toString());
        this.setOfflineRepaymentOverdueFeeService(item.get("overdue_fee_service") == null ? null : item.get("overdue_fee_service").toString());
        this.setOfflineRepaymentOverdueFeeOther(item.get("overdue_fee_other") == null ? null : item.get("overdue_fee_other").toString());

        BigDecimal amount = skipNullGetOfflineRepaymentPrincipal().add(skipNullGetOfflineRepaymentInterest())
                .add(skipNullGetOfflineRepaymentServiceFee())
                .add(skipNullGetOfflineRepaymentTechFee())
                .add(skipNullGetOfflineRepaymentOtherFee())
                .add(skipNullGetOfflineRepaymentOverdueFeePenalty())
                .add(skipNullGetOfflineRepaymentOverdueFeeObligation())
                .add(skipNullGetOfflineRepaymentOverdueFeeService())
                .add(skipNullGetOfflineRepaymentOverdueFeeOther());

        this.setOfflineRepaymentAmount(amount.toString());
    }


    public void fillDeductData() {
        Long count = skipNullGetOfflineRepaymentCount() + skipNullGetFstPaymentChannelCount()
                + skipNullGetSndPaymentChannelCount() + skipNullGetTrdPaymentChannelCount()
                + skipNullGetFthPaymentChannelCount() + skipNullGetFifthPaymentChannelCount();
        this.setDeductCount(count.toString());

        BigDecimal amount = skipNullGetOfflineRepaymentAmount().add(skipNullGetFstPaymentChannelAmount())
                .add(skipNullGetSndPaymentChannelAmount()).add(skipNullGetTrdPaymentChannelAmount())
                .add(skipNullGetFthPaymentChannelAmount()).add(skipNullGetFifthPaymentChannelAmount());
        this.setDeductAmount(amount.toString());

        BigDecimal principal = skipNullGetOfflineRepaymentPrincipal().add(skipNullGetFstPaymentChannelPrincipal())
                .add(skipNullGetSndPaymentChannelPrincipal()).add(skipNullGetTrdPaymentChannelPrincipal())
                .add(skipNullGetFthPaymentChannelPrincipal()).add(skipNullGetFifthPaymentChannelPrincipal());
        this.setDeductPrincipal(principal.toString());

        BigDecimal interest = skipNullGetOfflineRepaymentInterest().add(skipNullGetFstPaymentChannelInterest())
                .add(skipNullGetSndPaymentChannelInterest()).add(skipNullGetTrdPaymentChannelInterest())
                .add(skipNullGetFthPaymentChannelInterest()).add(skipNullGetFifthPaymentChannelInterest());
        this.setDeductInterest(interest.toString());

        BigDecimal serviceFee = skipNullGetOfflineRepaymentServiceFee().add(skipNullGetFstPaymentChannelServiceFee())
                .add(skipNullGetSndPaymentChannelServiceFee()).add(skipNullGetTrdPaymentChannelServiceFee())
                .add(skipNullGetFthPaymentChannelServiceFee()).add(skipNullGetFifthPaymentChannelServiceFee());
        this.setDeductServiceFee(serviceFee.toString());

        BigDecimal techFee = skipNullGetOfflineRepaymentTechFee().add(skipNullGetFstPaymentChannelTechFee())
                .add(skipNullGetSndPaymentChannelTechFee()).add(skipNullGetTrdPaymentChannelTechFee())
                .add(skipNullGetFthPaymentChannelTechFee()).add(skipNullGetFifthPaymentChannelTechFee());
        this.setDeductTechFee(techFee.toString());

        BigDecimal otherFee = skipNullGetOfflineRepaymentOtherFee().add(skipNullGetFstPaymentChannelOtherFee())
                .add(skipNullGetSndPaymentChannelOtherFee()).add(skipNullGetTrdPaymentChannelOtherFee())
                .add(skipNullGetFthPaymentChannelOtherFee()).add(skipNullGetFifthPaymentChannelOtherFee());
        this.setDeductOtherFee(otherFee.toString());

        BigDecimal overdueFeePenalty = skipNullGetOfflineRepaymentOverdueFeePenalty()
                .add(skipNullGetFstPaymentChannelOverdueFeePenalty())
                .add(skipNullGetSndPaymentChannelOverdueFeePenalty()).add(skipNullGetTrdPaymentChannelOverdueFeePenalty())
                .add(skipNullGetFthPaymentChannelOverdueFeePenalty())
                .add(skipNullGetFifthPaymentChannelOverdueFeePenalty());
        this.setDeductOverdueFeePenalty(overdueFeePenalty.toString());

        BigDecimal overdueFeeObligation = skipNullGetOfflineRepaymentOverdueFeeObligation()
                .add(skipNullGetFstPaymentChannelOverdueFeeObligation())
                .add(skipNullGetSndPaymentChannelOverdueFeeObligation())
                .add(skipNullGetTrdPaymentChannelOverdueFeeObligation())
                .add(skipNullGetFthPaymentChannelOverdueFeeObligation())
                .add(skipNullGetFifthPaymentChannelOverdueFeeObligation());
        this.setDeductOverdueFeeObligation(overdueFeeObligation.toString());

        BigDecimal overdueFeeService = skipNullGetOfflineRepaymentOverdueFeeService()
                .add(skipNullGetFstPaymentChannelOverdueFeeService())
                .add(skipNullGetSndPaymentChannelOverdueFeeService()).add(skipNullGetTrdPaymentChannelOverdueFeeService())
                .add(skipNullGetFthPaymentChannelOverdueFeeService())
                .add(skipNullGetFifthPaymentChannelOverdueFeeService());
        this.setDeductOverdueFeeService(overdueFeeService.toString());

        BigDecimal overdueFeeOther = skipNullGetOfflineRepaymentOverdueFeeOther()
                .add(skipNullGetFstPaymentChannelOverdueFeeOther())
                .add(skipNullGetSndPaymentChannelOverdueFeeOther()).add(skipNullGetTrdPaymentChannelOverdueFeeOther())
                .add(skipNullGetFthPaymentChannelOverdueFeeOther()).add(skipNullGetFifthPaymentChannelOverdueFeeOther());
        this.setDeductOverdueFeeOther(overdueFeeOther.toString());
    }

    public BigDecimal skipNullGetOfflineRepaymentAmount() {
        return this.getOfflineRepaymentAmount() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentAmount());
    }

    public BigDecimal skipNullGetFstPaymentChannelAmount() {
        return this.getFstPaymentChannelAmount() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFstPaymentChannelAmount());
    }

    public BigDecimal skipNullGetSndPaymentChannelAmount() {
        return this.getSndPaymentChannelAmount() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelAmount());
    }

    public BigDecimal skipNullGetTrdPaymentChannelAmount() {
        return this.getTrdPaymentChannelAmount() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelAmount());
    }

    public BigDecimal skipNullGetFthPaymentChannelAmount() {
        return this.getFthPaymentChannelAmount() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelAmount());
    }

    public BigDecimal skipNullGetFifthPaymentChannelAmount() {
        return this.getFifthPaymentChannelAmount() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelAmount());
    }


    public Long skipNullGetFstPaymentChannelCount() {
        return this.getFstPaymentChannelCount() == null ? 0L : new Long(this.getFstPaymentChannelCount());
    }


    public BigDecimal skipNullGetFstPaymentChannelPrincipal() {
        return this.getFstPaymentChannelPrincipal() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelPrincipal());
    }

    public BigDecimal skipNullGetFstPaymentChannelInterest() {
        return this.getFstPaymentChannelInterest() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelInterest());
    }

    public BigDecimal skipNullGetFstPaymentChannelServiceFee() {
        return this.getFstPaymentChannelServiceFee() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelServiceFee());
    }

    public BigDecimal skipNullGetFstPaymentChannelTechFee() {
        return this.getFstPaymentChannelTechFee() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelTechFee());
    }

    public BigDecimal skipNullGetFstPaymentChannelOtherFee() {
        return this.getFstPaymentChannelOtherFee() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelOtherFee());
    }

    public BigDecimal skipNullGetFstPaymentChannelOverdueFeePenalty() {
        return this.getFstPaymentChannelOverdueFeePenalty() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelOverdueFeePenalty());
    }

    public BigDecimal skipNullGetFstPaymentChannelOverdueFeeObligation() {
        return this.getFstPaymentChannelOverdueFeeObligation() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelOverdueFeeObligation());
    }

    public BigDecimal skipNullGetFstPaymentChannelOverdueFeeService() {
        return this.getFstPaymentChannelOverdueFeeService() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelOverdueFeeService());
    }

    public BigDecimal skipNullGetFstPaymentChannelOverdueFeeOther() {
        return this.getFstPaymentChannelOverdueFeeOther() == null ? BigDecimal.ZERO :
                new BigDecimal(this.getFstPaymentChannelOverdueFeeOther());
    }

    public Long skipNullGetSndPaymentChannelCount() {
        return this.getSndPaymentChannelCount() == null ? 0L
                : new Long(this.getSndPaymentChannelCount());
    }


    public BigDecimal skipNullGetSndPaymentChannelPrincipal() {
        return this.getSndPaymentChannelPrincipal() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelPrincipal());
    }


    public BigDecimal skipNullGetSndPaymentChannelInterest() {
        return this.getSndPaymentChannelInterest() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelInterest());
    }


    public BigDecimal skipNullGetSndPaymentChannelServiceFee() {
        return this.getSndPaymentChannelServiceFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelServiceFee());
    }


    public BigDecimal skipNullGetSndPaymentChannelTechFee() {
        return this.getSndPaymentChannelTechFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelTechFee());
    }


    public BigDecimal skipNullGetSndPaymentChannelOtherFee() {
        return this.getSndPaymentChannelOtherFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelOtherFee());
    }


    public BigDecimal skipNullGetSndPaymentChannelOverdueFeePenalty() {
        return this.getSndPaymentChannelOverdueFeePenalty() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelOverdueFeePenalty());
    }


    public BigDecimal skipNullGetSndPaymentChannelOverdueFeeObligation() {
        return this.getSndPaymentChannelOverdueFeeObligation() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelOverdueFeeObligation());
    }


    public BigDecimal skipNullGetSndPaymentChannelOverdueFeeService() {
        return this.getSndPaymentChannelOverdueFeeService() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelOverdueFeeService());
    }


    public BigDecimal skipNullGetSndPaymentChannelOverdueFeeOther() {
        return this.getSndPaymentChannelOverdueFeeOther() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getSndPaymentChannelOverdueFeeOther());
    }

    public Long skipNullGetTrdPaymentChannelCount() {
        return this.getTrdPaymentChannelCount() == null ? 0L
                : new Long(this.getTrdPaymentChannelCount());
    }

    public BigDecimal skipNullGetTrdPaymentChannelPrincipal() {
        return this.getTrdPaymentChannelPrincipal() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelPrincipal());
    }

    public BigDecimal skipNullGetTrdPaymentChannelInterest() {
        return this.getTrdPaymentChannelInterest() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelInterest());
    }

    public BigDecimal skipNullGetTrdPaymentChannelServiceFee() {
        return this.getTrdPaymentChannelServiceFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelServiceFee());
    }

    public BigDecimal skipNullGetTrdPaymentChannelTechFee() {
        return this.getTrdPaymentChannelTechFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelTechFee());
    }

    public BigDecimal skipNullGetTrdPaymentChannelOtherFee() {
        return this.getTrdPaymentChannelOtherFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelOtherFee());
    }

    public BigDecimal skipNullGetTrdPaymentChannelOverdueFeePenalty() {
        return this.getTrdPaymentChannelOverdueFeePenalty() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelOverdueFeePenalty());
    }

    public BigDecimal skipNullGetTrdPaymentChannelOverdueFeeObligation() {
        return this.getTrdPaymentChannelOverdueFeeObligation() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelOverdueFeeObligation());
    }

    public BigDecimal skipNullGetTrdPaymentChannelOverdueFeeService() {
        return this.getTrdPaymentChannelOverdueFeeService() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelOverdueFeeService());
    }

    public BigDecimal skipNullGetTrdPaymentChannelOverdueFeeOther() {
        return this.getTrdPaymentChannelOverdueFeeOther() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getTrdPaymentChannelOverdueFeeOther());
    }


    public Long skipNullGetFthPaymentChannelCount() {
        return this.getFthPaymentChannelCount() == null ? 0L
                : new Long(this.getFthPaymentChannelCount());
    }

    public BigDecimal skipNullGetFthPaymentChannelPrincipal() {
        return this.getFthPaymentChannelPrincipal() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelPrincipal());
    }

    public BigDecimal skipNullGetFthPaymentChannelInterest() {
        return this.getFthPaymentChannelInterest() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelInterest());
    }

    public BigDecimal skipNullGetFthPaymentChannelServiceFee() {
        return this.getFthPaymentChannelServiceFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelServiceFee());
    }

    public BigDecimal skipNullGetFthPaymentChannelTechFee() {
        return this.getFthPaymentChannelTechFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelTechFee());
    }

    public BigDecimal skipNullGetFthPaymentChannelOtherFee() {
        return this.getFthPaymentChannelOtherFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelOtherFee());
    }

    public BigDecimal skipNullGetFthPaymentChannelOverdueFeePenalty() {
        return this.getFthPaymentChannelOverdueFeePenalty() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelOverdueFeePenalty());
    }

    public BigDecimal skipNullGetFthPaymentChannelOverdueFeeObligation() {
        return this.getFthPaymentChannelOverdueFeeObligation() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelOverdueFeeObligation());
    }

    public BigDecimal skipNullGetFthPaymentChannelOverdueFeeService() {
        return this.getFthPaymentChannelOverdueFeeService() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelOverdueFeeService());
    }

    public BigDecimal skipNullGetFthPaymentChannelOverdueFeeOther() {
        return this.getFthPaymentChannelOverdueFeeOther() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFthPaymentChannelOverdueFeeOther());
    }

    public Long skipNullGetFifthPaymentChannelCount() {
        return this.getFifthPaymentChannelCount() == null ? 0L
                : new Long(this.getFifthPaymentChannelCount());
    }

    public BigDecimal skipNullGetFifthPaymentChannelPrincipal() {
        return this.getFifthPaymentChannelPrincipal() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelPrincipal());
    }

    public BigDecimal skipNullGetFifthPaymentChannelInterest() {
        return this.getFifthPaymentChannelInterest() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelInterest());
    }

    public BigDecimal skipNullGetFifthPaymentChannelServiceFee() {
        return this.getFifthPaymentChannelServiceFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelServiceFee());
    }

    public BigDecimal skipNullGetFifthPaymentChannelTechFee() {
        return this.getFifthPaymentChannelTechFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelTechFee());
    }

    public BigDecimal skipNullGetFifthPaymentChannelOtherFee() {
        return this.getFifthPaymentChannelOtherFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelOtherFee());
    }

    public BigDecimal skipNullGetFifthPaymentChannelOverdueFeePenalty() {
        return this.getFifthPaymentChannelOverdueFeePenalty() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelOverdueFeePenalty());
    }

    public BigDecimal skipNullGetFifthPaymentChannelOverdueFeeObligation() {
        return this.getFifthPaymentChannelOverdueFeeObligation() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelOverdueFeeObligation());
    }

    public BigDecimal skipNullGetFifthPaymentChannelOverdueFeeService() {
        return this.getFifthPaymentChannelOverdueFeeService() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelOverdueFeeService());
    }

    public BigDecimal skipNullGetFifthPaymentChannelOverdueFeeOther() {
        return this.getFifthPaymentChannelOverdueFeeOther() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getFifthPaymentChannelOverdueFeeOther());
    }

    public Long skipNullGetOfflineRepaymentCount() {
        return this.getOfflineRepaymentCount() == null ? 0L
                : new Long(this.getOfflineRepaymentCount());
    }

    public BigDecimal skipNullGetOfflineRepaymentPrincipal() {
        return this.getOfflineRepaymentPrincipal() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentPrincipal());
    }

    public BigDecimal skipNullGetOfflineRepaymentInterest() {
        return this.getOfflineRepaymentInterest() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentInterest());
    }

    public BigDecimal skipNullGetOfflineRepaymentServiceFee() {
        return this.getOfflineRepaymentServiceFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentServiceFee());
    }

    public BigDecimal skipNullGetOfflineRepaymentTechFee() {
        return this.getOfflineRepaymentTechFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentTechFee());
    }

    public BigDecimal skipNullGetOfflineRepaymentOtherFee() {
        return this.getOfflineRepaymentOtherFee() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentOtherFee());
    }

    public BigDecimal skipNullGetOfflineRepaymentOverdueFeePenalty() {
        return this.getOfflineRepaymentOverdueFeePenalty() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentOverdueFeePenalty());
    }

    public BigDecimal skipNullGetOfflineRepaymentOverdueFeeObligation() {
        return this.getOfflineRepaymentOverdueFeeObligation() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentOverdueFeeObligation());
    }

    public BigDecimal skipNullGetOfflineRepaymentOverdueFeeService() {
        return this.getOfflineRepaymentOverdueFeeService() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentOverdueFeeService());
    }

    public BigDecimal skipNullGetOfflineRepaymentOverdueFeeOther() {
        return this.getOfflineRepaymentOverdueFeeOther() == null ? BigDecimal.ZERO
                : new BigDecimal(this.getOfflineRepaymentOverdueFeeOther());
    }
}
