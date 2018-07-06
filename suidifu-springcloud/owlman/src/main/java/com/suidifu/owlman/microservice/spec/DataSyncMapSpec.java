package com.suidifu.owlman.microservice.spec;

import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import java.util.HashMap;
import java.util.Map;

public class DataSyncMapSpec {
    /**
     * 低到高位组合判定还款类型
     * 个位：1：已担保 0: 未担保
     * 十位：2 逾期待确认 1：已逾期 0: 未逾期
     * 百位：1：提前全部 2：提前部分 0：非提前还款
     * 千位：1:提前划扣 0：非提前划扣
     */
    public final static Map<String, String> RepayKindMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1998932252096520683L;

        {
            put("0000", DataSyncRepayKind.NORMAL_REPAYMENT);
            put("XXX1", DataSyncRepayKind.HAS_GUARANTEE_AND_CUTSOMER_REPAYMENT);
            put("XX1X", DataSyncRepayKind.OVERDUE_REPAYMENT);
            put("XX2X", DataSyncRepayKind.OVERDUE_WAIT_FOR_SURE);
            put("0100", DataSyncRepayKind.PRE_REPAYMENT_ALL);
            put("0200", DataSyncRepayKind.PRE_REPAYMENT_PART);
            put("1100", DataSyncRepayKind.DRAW_IN_ADVANCE);
            put("1200", DataSyncRepayKind.DRAW_IN_ADVANCE);
            put("1000", DataSyncRepayKind.DRAW_IN_ADVANCE);
        }
    };
    public static final Map<JournalVoucherType, Integer> jorunalVoucherTypeVSDeductTypeMap = new HashMap<JournalVoucherType, Integer>() {
        {
            put(JournalVoucherType.ONLINE_DEDUCT_BACK_ISSUE, DataSyncDeductType.DEDUCT);
            put(JournalVoucherType.OFFLINE_BILL_ISSUE, DataSyncDeductType.INITIATIVE_PAY);
            put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER, DataSyncDeductType.ENTRUST_TURN_PAY);
            put(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER, DataSyncDeductType.DEDUCT);
            put(JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE, DataSyncDeductType.MERCHANT_PAY);
            put(JournalVoucherType.TRANSFER_BILL_INITIATIVE, DataSyncDeductType.INITIATIVE_PAY);
            put(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE, DataSyncDeductType.MERCHANT_PAY);

            put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE, DataSyncDeductType.MERCHANT_PAY);
            put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE, DataSyncDeductType.VOUCHER_BALANCE);
            put(JournalVoucherType.TRANSFER_BILL_BY_GUARANTEE_VOUCHER, DataSyncDeductType.MERCHANT_PAY);
        }
    };
    public static final Map<JournalVoucherType, String> jorunalVoucherTypeVSDataSyncRepayKind = new HashMap<JournalVoucherType, String>() {
        {
            put(JournalVoucherType.ONLINE_DEDUCT_BACK_ISSUE, DataSyncRepayKind.UN_KNOWN);
            put(JournalVoucherType.OFFLINE_BILL_ISSUE, DataSyncRepayKind.UN_KNOWN);
            put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER, DataSyncRepayKind.UN_KNOWN);
            put(JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER, DataSyncRepayKind.UN_KNOWN);
            put(JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE, DataSyncRepayKind.HAS_GUARANTEE);
            put(JournalVoucherType.TRANSFER_BILL_INITIATIVE, DataSyncRepayKind.UN_KNOWN);
            put(JournalVoucherType.TRANSFER_BILL_BY_REPURCHASE, DataSyncRepayKind.REPO);
            put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_ADVANCE, DataSyncRepayKind.UN_KNOWN);
            put(JournalVoucherType.TRANSFER_BILL_BY_VOUCHER_BALANCE, DataSyncRepayKind.UN_KNOWN);
            put(JournalVoucherType.TRANSFER_BILL_BY_GUARANTEE_VOUCHER, DataSyncRepayKind.HAS_GUARANTEE);
        }
    };
    public static final Map<CashFlowChannelType, Integer> journalVoucherTypeVSDeductWayMap = new HashMap<CashFlowChannelType, Integer>() {
        {
            put(CashFlowChannelType.Unionpay, DeductWay.GZUNION);
            put(CashFlowChannelType.BaoFu, DeductWay.BAOFU);
            put(CashFlowChannelType.ZhongJinPay, DeductWay.ZHONGJIN);
            put(CashFlowChannelType.UcfPay, DeductWay.UcfPay);
            put(CashFlowChannelType.SinaPay, DeductWay.SinaPay);
        }
    };

    public static final String getRepayKindInRule(int overDueStatus, int guaranteeStatus, int advanceRepaymentStatus, int advanceDrawStatus) {

        if (guaranteeStatus == 1) {
            return "XXX1";
        }
        if (overDueStatus == 1) {
            return "XX1X";
        }
        if (overDueStatus == 2) {
            return "XX2X";
        }
        StringBuffer stringBuffer = new StringBuffer(4);
        stringBuffer.append(advanceDrawStatus).append(advanceRepaymentStatus).append(overDueStatus).append(guaranteeStatus).toString();
        return stringBuffer.toString();
    }

    public static final class DataSyncDeductType {

        public static final Integer UNKNOWN = 0;
        //代扣
        public static final Integer DEDUCT = 1;
        //委托转付
        public static final Integer ENTRUST_TURN_PAY = 2;
        //主动付款
        public static final Integer INITIATIVE_PAY = 3;
        //商户代偿
        public static final Integer MERCHANT_PAY = 4;
        //差额划拨
        public static final Integer VOUCHER_BALANCE = 5;
    }

    public static final class DataSyncRepayKind {

        //未知
        public static final String UN_KNOWN = "00";
        //正常还款
        public static final String NORMAL_REPAYMENT = "01";
        //逾期还款
        public static final String OVERDUE_REPAYMENT = "02";
        //担保补足
        public static final String HAS_GUARANTEE = "03";
        //担保补足后客户还款
        public static final String HAS_GUARANTEE_AND_CUTSOMER_REPAYMENT = "04";
        //回购
        public static final String REPO = "05";
        //提前全部还款
        public static final String PRE_REPAYMENT_ALL = "06";
        //提前部分还款
        public static final String PRE_REPAYMENT_PART = "07";
        //提前划扣
        public static final String DRAW_IN_ADVANCE = "08";
        //逾期待确认还款
        public static final String OVERDUE_WAIT_FOR_SURE = "09";

    }

    public static final class DeductWay {
        public static final Integer GZUNION = 1;
        public static final Integer BAOFU = 2;
        public static final Integer ZHONGJIN = 3;
        public static final Integer UcfPay = 4;
        public static final Integer SinaPay = 5;
    }


}
