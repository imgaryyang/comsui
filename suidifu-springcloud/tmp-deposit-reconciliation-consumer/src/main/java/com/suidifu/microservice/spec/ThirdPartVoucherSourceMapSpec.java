package com.suidifu.microservice.spec;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.owlman.microservice.enumation.SecondJournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ThirdJournalVoucherType;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Nov 18, 2016 5:55:19 PM 类说明
 */
public class ThirdPartVoucherSourceMapSpec {

  public static final Map<RepaymentType, ThirdJournalVoucherType> REPAYMENT_TYPE_TO_THIRD_JOURNAL_VOUCHER_TYPE_MAP = new HashMap<RepaymentType, ThirdJournalVoucherType>() {
    private static final long serialVersionUID = -4019008534567699649L;

    {
    put(RepaymentType.ADVANCE, ThirdJournalVoucherType.ADVANCE);
    put(RepaymentType.NORMAL, ThirdJournalVoucherType.NORMAL);
    put(RepaymentType.OVERDUE, ThirdJournalVoucherType.OVERDUE);
  }};

  /****************凭证来源 转换map******************/
  public static final Map<String, SecondJournalVoucherType> SECOND_JOURNAL_VOUCHER_TYPE_MAP = new HashMap<String, SecondJournalVoucherType>() {
    private static final long serialVersionUID = -6501226397670769322L;

    {
    put(SourceDocument.SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT, SecondJournalVoucherType.ONLINE_PAYMENT_BILL);
    put(SourceDocument.SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET, SecondJournalVoucherType.INTERFACE_PAYMNET_BILL);
    put(SourceDocument.SECONDOUTLIER_APP_UPLOAD_UPLOAD, SecondJournalVoucherType.APP_UPLOAD_PAYMENT_BILL);

  }};

}
