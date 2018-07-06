package com.suidifu.owlman.microservice.spec;

import com.suidifu.owlman.microservice.enumation.JournalVoucherMapingStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.ReconciliationType;
import com.suidifu.owlman.microservice.enumation.SecondJournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ThirdJournalVoucherType;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Nov 18, 2016 5:55:19 PM
 * 类说明
 */
public class ThirdPartVoucherSourceMapSpec {
    public static final String FIRSTOUTLIER_DEDUCTAPPLICATION = "deduct_application";
    /* secondOutlierDocType, 扣款单来源：1系统发起线上支付单 2 接口发起扣款单*/
    public static final String SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT = "system_on_line_payment";
    public static final String SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET = "interface_on_line_payment";
    public static final String SECONDOUTLIER_APP_UPLOAD_UPLOAD = "app_upload_payment";

    public static final Map<RepaymentType, ThirdJournalVoucherType> repaymentTypeToThirdJournalVoucherTypeMap =
            new EnumMap<>(RepaymentType.class);
    public static final Map<ThirdJournalVoucherType, RepaymentType> thirdJournalVoucherTypeTorepaymentTypeMap =
            new EnumMap<>(ThirdJournalVoucherType.class);
    public static final Map<PaymentInstitutionName, CashFlowChannelType> cashFlowChannelTypeMap =
            new EnumMap<>(PaymentInstitutionName.class);
    public static final Map<JournalVoucherStatus, JournalVoucherMapingStatus> journalVoucherStatusToJournalVoucherMapingStatusMap =
            new EnumMap<>(JournalVoucherStatus.class);
    public static final Map<JournalVoucherMapingStatus, JournalVoucherStatus> journalVoucherMapingStatusToJournalVoucherStatusMap =
            new EnumMap<>(JournalVoucherMapingStatus.class);
    /****************凭证来源 转换map******************/
    public static final Map<String, SecondJournalVoucherType> SecondJournalVoucherTypeMap = new HashMap<>();
    public static final Map<SourceType, String> sourceTypeMapSpec = new EnumMap<>(SourceType.class);
    public static final Map<PayWay, String> payWayMapSpec = new EnumMap<>(PayWay.class);
    public static final Map<String, Integer> cashFlowFrontEndShowStringMap = new HashMap<>();
    public static final Map<String, Integer> repayTypeStringFrontEndShowMap = new HashMap<>();
    public static final Map<String, Integer> voucherSourceStringFrontEndShowMap = new HashMap<>();
    public static final Map<String, Integer> voucherStatusStringFrontEndShowMap = new HashMap<>();
    public static final Map<Integer, String> cashFlowStringMap = new HashMap<>();
    public static final Map<Integer, String> repayTypeStringMap = new HashMap<>();
    public static final Map<Integer, String> voucherSourceStringMap = new HashMap<>();
    public static final Map<Integer, String> voucherStatusStringMap = new HashMap<>();
    public static final Map<String, Boolean> repayementOrderGroupWayToPayWayMappingTableClone = new HashMap<>();
    public static final Map<String, String> reconciliationVoucherTypeTable = new HashMap<>();
    public static final Map<String, String> repaymentBusinessCheckTable = new HashMap<>();
    public static final Map<String, String> reconciliationRepaymentOrderTable = new HashMap<>();

    static {
        repaymentTypeToThirdJournalVoucherTypeMap.put(RepaymentType.ADVANCE, ThirdJournalVoucherType.ADVANCE);
        repaymentTypeToThirdJournalVoucherTypeMap.put(RepaymentType.NORMAL, ThirdJournalVoucherType.NORMAL);
        repaymentTypeToThirdJournalVoucherTypeMap.put(RepaymentType.OVERDUE, ThirdJournalVoucherType.OVERDUE);

        thirdJournalVoucherTypeTorepaymentTypeMap.put(ThirdJournalVoucherType.ADVANCE, RepaymentType.ADVANCE);
        thirdJournalVoucherTypeTorepaymentTypeMap.put(ThirdJournalVoucherType.NORMAL, RepaymentType.NORMAL);
        thirdJournalVoucherTypeTorepaymentTypeMap.put(ThirdJournalVoucherType.OVERDUE, RepaymentType.OVERDUE);

        cashFlowChannelTypeMap.put(PaymentInstitutionName.UNIONPAYGZ, CashFlowChannelType.Unionpay);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.SUPERBANK, CashFlowChannelType.SUPERBANK);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.DIRECTBANK, CashFlowChannelType.DirectBank);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.BAOFU, CashFlowChannelType.BaoFu);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.JIN_YUN_TONG, CashFlowChannelType.JinYunTong);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.MIN_SHENG_PAY, CashFlowChannelType.MinShengPay);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.SINA_PAY, CashFlowChannelType.SinaPay);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.ZHONG_JIN_PAY, CashFlowChannelType.ZhongJinPay);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.JIANHANG, CashFlowChannelType.JIANHANG);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.TONGLIAN, CashFlowChannelType.TONGLIAN);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.YIJIFU, CashFlowChannelType.YIJIFU);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.YEE_PAY, CashFlowChannelType.YEE_PAY);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.LDYS, CashFlowChannelType.LDYS);
        cashFlowChannelTypeMap.put(PaymentInstitutionName.E_PAY, CashFlowChannelType.E_PAY);

        journalVoucherStatusToJournalVoucherMapingStatusMap.put(JournalVoucherStatus.VOUCHER_CREATED,
                JournalVoucherMapingStatus.WRITE_OFFING);
        journalVoucherStatusToJournalVoucherMapingStatusMap.put(JournalVoucherStatus.VOUCHER_ISSUED,
                JournalVoucherMapingStatus.WRITE_OFF);

        journalVoucherMapingStatusToJournalVoucherStatusMap.put(JournalVoucherMapingStatus.WRITE_OFFING,
                JournalVoucherStatus.VOUCHER_CREATED);
        journalVoucherMapingStatusToJournalVoucherStatusMap.put(JournalVoucherMapingStatus.WRITE_OFF,
                JournalVoucherStatus.VOUCHER_ISSUED);

        SecondJournalVoucherTypeMap.put(SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT,
                SecondJournalVoucherType.ONLINE_PAYMENT_BILL);
        SecondJournalVoucherTypeMap.put(SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET,
                SecondJournalVoucherType.INTERFACE_PAYMNET_BILL);
        SecondJournalVoucherTypeMap.put(SECONDOUTLIER_APP_UPLOAD_UPLOAD,
                SecondJournalVoucherType.APP_UPLOAD_PAYMENT_BILL);

        reconciliationVoucherTypeTable.put(VoucherType.REPURCHASE.getKey(),
                "reconciliationForRepurchaseDocumentHandler");
        reconciliationVoucherTypeTable.put(VoucherType.PAY.getKey(),
                "reconciliationForSubrogationDocumentHandler");
        reconciliationVoucherTypeTable.put(VoucherType.MERCHANT_REFUND.getKey(),
                "reconciliationForSubrogationDocumentHandler");
        reconciliationVoucherTypeTable.put(VoucherType.ADVANCE.getKey(),
                "reconciliationForSubrogationDocumentHandler");
        reconciliationVoucherTypeTable.put(VoucherType.ACTIVE_PAY.getKey(),
                "reconciliationForInitiativePaymentDocumentHandler");
        reconciliationVoucherTypeTable.put(VoucherType.OTHER_PAY.getKey(),
                "reconciliationForInitiativePaymentDocumentHandler");
        reconciliationVoucherTypeTable.put(ReconciliationType.RECONCILIATION_AUTO_RECOVER_SETTLEMENT_SHEET.getKey(),
                "reconciliationForReconciliationSettlementSheetDocumentHandler");
        reconciliationVoucherTypeTable.put(VoucherType.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(),
                "reconciliationForDeductApiDocumentHandler");
        reconciliationVoucherTypeTable.put(VoucherType.GUARANTEE.getKey(),
                "reconciliationForGuaranteeVoucherHandler");
        reconciliationVoucherTypeTable.put(ReconciliationType.RECONCILIATION_DEDUCT_REFUND.getKey(),
                "reconciliationFoDeductApiRefundHandler");
        reconciliationVoucherTypeTable.put(ReconciliationType.RECONCILIATION_Clearing_Voucher.getKey(),
                "reconciliationForClearingVoucherHandler");

        //线上代扣 ——线上代扣
        repayementOrderGroupWayToPayWayMappingTableClone.put(RepaymentWayGroupType.
                ONLINE_DEDUCT_REPAYMENT_ORDER_TYPE.name() + "_" + PayWay.MERCHANT_DEDUCT.name(), Boolean.TRUE);
        //在线支付 ——线上代扣
        repayementOrderGroupWayToPayWayMappingTableClone.put(RepaymentWayGroupType.
                ONLINE_DEDUCT_REPAYMENT_ORDER_ONLINE_PAYMENT_TYPE.name() + "_" + PayWay.MERCHANT_DEDUCT.name(), Boolean.TRUE);
        //(委托转付,商户代偿，差额划拨，回购) —— 线下转账
        repayementOrderGroupWayToPayWayMappingTableClone.put(RepaymentWayGroupType.
                ALTER_OFFLINE_REPAYMENT_ORDER_TYPE.name() + "_" + PayWay.OFFLINE_TRANSFER.name(), Boolean.TRUE);
        //(主动还款，他人贷款)——线下转账
        repayementOrderGroupWayToPayWayMappingTableClone.put(RepaymentWayGroupType.
                OWNER_OFFLINE_REPAYMENT_ORDER_TYPE.name() + "_" + PayWay.OFFLINE_TRANSFER.name(), Boolean.TRUE);
        //(商户代扣)—— 商户代扣
        repayementOrderGroupWayToPayWayMappingTableClone.put(RepaymentWayGroupType.
                BUSINESS_DEDUCT_REPAYMENT_ORDER_TYPE.name() + "_" + PayWay.BUSINESS_DEDUCT.name(), Boolean.TRUE);

        cashFlowStringMap.put(CashFlowChannelType.Unionpay.ordinal(), "广银联");
        cashFlowStringMap.put(CashFlowChannelType.BaoFu.ordinal(), "宝付");
        cashFlowStringMap.put(CashFlowChannelType.JinYunTong.ordinal(), "金运通");
        cashFlowStringMap.put(CashFlowChannelType.MinShengPay.ordinal(), "民生支付");
        cashFlowStringMap.put(CashFlowChannelType.SinaPay.ordinal(), "新浪支付");
        cashFlowStringMap.put(CashFlowChannelType.ZhongJinPay.ordinal(), "中金支付");
        cashFlowStringMap.put(CashFlowChannelType.JIANHANG.ordinal(), "建行");
        cashFlowStringMap.put(CashFlowChannelType.TONGLIAN.ordinal(), "通联");
        cashFlowStringMap.put(CashFlowChannelType.YEE_PAY.ordinal(), "易宝支付");
        cashFlowStringMap.put(CashFlowChannelType.LDYS.ordinal(), "联动优势");
        cashFlowStringMap.put(CashFlowChannelType.YEE_PAY.ordinal(), "网易支付");

        sourceTypeMapSpec.put(SourceType.SYSTEMONLINEDEDUCT, SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT);
        sourceTypeMapSpec.put(SourceType.INTERFACEONLINEDEDUCT, SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET);
        sourceTypeMapSpec.put(SourceType.APPUPLOAD, SECONDOUTLIER_APP_UPLOAD_UPLOAD);

        payWayMapSpec.put(PayWay.MERCHANT_DEDUCT_EASY_PAY, SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET);
        payWayMapSpec.put(PayWay.MERCHANT_DEDUCT, SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET);
        payWayMapSpec.put(PayWay.BUSINESS_DEDUCT, SECONDOUTLIER_APP_UPLOAD_UPLOAD);

        cashFlowFrontEndShowStringMap.put("广银联", CashFlowChannelType.Unionpay.ordinal());
        cashFlowFrontEndShowStringMap.put("宝付", CashFlowChannelType.BaoFu.ordinal());
        cashFlowFrontEndShowStringMap.put("建行", CashFlowChannelType.JIANHANG.ordinal());

        repayTypeStringFrontEndShowMap.put("提前划扣", ThirdJournalVoucherType.ADVANCE.ordinal());
        repayTypeStringFrontEndShowMap.put("正常", ThirdJournalVoucherType.NORMAL.ordinal());
        repayTypeStringFrontEndShowMap.put("逾期", ThirdJournalVoucherType.OVERDUE.ordinal());

        voucherSourceStringFrontEndShowMap.put("系统线上支付单", SecondJournalVoucherType.ONLINE_PAYMENT_BILL.ordinal());
        voucherSourceStringFrontEndShowMap.put("接口线上支付单", SecondJournalVoucherType.INTERFACE_PAYMNET_BILL.ordinal());

        voucherStatusStringFrontEndShowMap.put("核销中", JournalVoucherStatus.VOUCHER_CREATED.ordinal());
        voucherStatusStringFrontEndShowMap.put("已核销", JournalVoucherStatus.VOUCHER_ISSUED.ordinal());

        repayTypeStringMap.put(ThirdJournalVoucherType.ADVANCE.ordinal(), "提前划扣");
        repayTypeStringMap.put(ThirdJournalVoucherType.NORMAL.ordinal(), "正常");
        repayTypeStringMap.put(ThirdJournalVoucherType.OVERDUE.ordinal(), "逾期");

        voucherSourceStringMap.put(SecondJournalVoucherType.ONLINE_PAYMENT_BILL.ordinal(), "系统线上支付单");
        voucherSourceStringMap.put(SecondJournalVoucherType.INTERFACE_PAYMNET_BILL.ordinal(), "接口线上支付单");
        voucherSourceStringMap.put(SecondJournalVoucherType.APP_UPLOAD_PAYMENT_BILL.ordinal(), "商户上传扣款凭证");

        voucherStatusStringMap.put(JournalVoucherStatus.VOUCHER_CREATED.ordinal(), "核销中");
        voucherStatusStringMap.put(JournalVoucherStatus.VOUCHER_ISSUED.ordinal(), "已核销");

        repaymentBusinessCheckTable.put(RepaymentBusinessType.REPAYMENT_PLAN.getKey(), "repaymentPlanCheckHandler");
        repaymentBusinessCheckTable.put(RepaymentBusinessType.REPURCHASE.getKey(), "repurchaseCheckHandler");


        reconciliationRepaymentOrderTable.put(RepaymentWay.MERCHANT_PAY.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.MERCHANT_TRANSFER.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.MERCHANT_TRANSFER_BALANCE.getKey(), "reconciliationRepaymentOrderForSubrogationDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.ACTIVE_PAY.getKey(), "reconciliationRepaymentOrderForInitiativePaymentDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.OTHER_PAY.getKey(), "reconciliationRepaymentOrderForInitiativePaymentDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.REPURCHASE.getKey(),
                "reconciliationRepaymentOrderForRepurchaseDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.MERCHANT_DEDUCT_EASY_PAY.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.ON_LINE_DEDUCT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.MERCHANT_DEDUCT_ONLINE_PAYMENT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
        reconciliationRepaymentOrderTable.put(RepaymentWay.MERCHANT_DEDUCT.getKey(), "reconciliationRepaymentOrderForDeductApiDocumentHandler");
    }

    private ThirdPartVoucherSourceMapSpec() {
    }
}