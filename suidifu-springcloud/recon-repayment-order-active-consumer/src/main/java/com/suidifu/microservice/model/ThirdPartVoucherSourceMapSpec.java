package com.suidifu.microservice.model;

import com.suidifu.owlman.microservice.enumation.JournalVoucherMapingStatus;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.SecondJournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ThirdJournalVoucherType;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import java.util.HashMap;
import java.util.Map;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Nov 18, 2016 5:55:19 PM 
* 类说明 
*/
public class ThirdPartVoucherSourceMapSpec {


	

	public static final  Map<RepaymentType,ThirdJournalVoucherType> repaymentTypeToThirdJournalVoucherTypeMap = new HashMap<RepaymentType,ThirdJournalVoucherType>(){{
		put(RepaymentType.ADVANCE, ThirdJournalVoucherType.ADVANCE);
		put(RepaymentType.NORMAL, ThirdJournalVoucherType.NORMAL);
		put(RepaymentType.OVERDUE, ThirdJournalVoucherType.OVERDUE);
	}};
	
	public static final  Map<ThirdJournalVoucherType,RepaymentType> thirdJournalVoucherTypeTorepaymentTypeMap = new HashMap<ThirdJournalVoucherType,RepaymentType>(){{
		put(ThirdJournalVoucherType.ADVANCE, RepaymentType.ADVANCE);
		put(ThirdJournalVoucherType.NORMAL, RepaymentType.NORMAL);
		put(ThirdJournalVoucherType.OVERDUE, RepaymentType.OVERDUE);
	}};
	
	
	public static final  Map<PaymentInstitutionName,CashFlowChannelType> cashFlowChannelTypeMap = new HashMap<PaymentInstitutionName,CashFlowChannelType>(){{
		put(PaymentInstitutionName.UNIONPAYGZ, CashFlowChannelType.Unionpay);
		put(PaymentInstitutionName.SUPERBANK, CashFlowChannelType.SUPERBANK);
		put(PaymentInstitutionName.DIRECTBANK, CashFlowChannelType.DirectBank);
		put(PaymentInstitutionName.BAOFU, CashFlowChannelType.BaoFu);
		put(PaymentInstitutionName.JIN_YUN_TONG, CashFlowChannelType.JinYunTong);
		put(PaymentInstitutionName.MIN_SHENG_PAY, CashFlowChannelType.MinShengPay);
		put(PaymentInstitutionName.SINA_PAY, CashFlowChannelType.SinaPay);
		put(PaymentInstitutionName.ZHONG_JIN_PAY, CashFlowChannelType.ZhongJinPay);
		put(PaymentInstitutionName.JIANHANG, CashFlowChannelType.JIANHANG);
		put(PaymentInstitutionName.TONGLIAN, CashFlowChannelType.TONGLIAN);
		put(PaymentInstitutionName.YIJIFU, CashFlowChannelType.YIJIFU);
		put(PaymentInstitutionName.YEE_PAY, CashFlowChannelType.YEE_PAY);
        put(PaymentInstitutionName.LDYS, CashFlowChannelType.LDYS);
		put(PaymentInstitutionName.E_PAY, CashFlowChannelType.E_PAY);
	}};

	public static final  Map<JournalVoucherStatus,JournalVoucherMapingStatus> journalVoucherStatusToJournalVoucherMapingStatusMap = new HashMap<JournalVoucherStatus,JournalVoucherMapingStatus>(){{
		put(JournalVoucherStatus.VOUCHER_CREATED, JournalVoucherMapingStatus.WRITE_OFFING);
		put(JournalVoucherStatus.VOUCHER_ISSUED, JournalVoucherMapingStatus.WRITE_OFF);
	}};
	
	public static final  Map<JournalVoucherMapingStatus,JournalVoucherStatus> journalVoucherMapingStatusToJournalVoucherStatusMap = new HashMap<JournalVoucherMapingStatus,JournalVoucherStatus>(){{
		put(JournalVoucherMapingStatus.WRITE_OFFING, JournalVoucherStatus.VOUCHER_CREATED);
		put(JournalVoucherMapingStatus.WRITE_OFF, JournalVoucherStatus.VOUCHER_ISSUED);
	}};
	
	
	
	
	/****************凭证来源 转换map******************/	
	public static  final Map<String, SecondJournalVoucherType> SecondJournalVoucherTypeMap = new HashMap<String,SecondJournalVoucherType>(){{
		put(SourceDocument.SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT, SecondJournalVoucherType.ONLINE_PAYMENT_BILL);
		put(SourceDocument.SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET, SecondJournalVoucherType.INTERFACE_PAYMNET_BILL);
		put(SourceDocument.SECONDOUTLIER_APP_UPLOAD_UPLOAD, SecondJournalVoucherType.APP_UPLOAD_PAYMENT_BILL);
//		put(SourceDocument.SECONDOUTLIER_REPAYMENT_ORDER, SecondJournalVoucherType.REPAYMENT_ORDER);
		
	}};
	
	public static final  Map<SourceType,String> sourceTypeMapSpec = new HashMap<SourceType,String>(){{
		put(SourceType.SYSTEMONLINEDEDUCT,SourceDocument.SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT);
		put(SourceType.INTERFACEONLINEDEDUCT,SourceDocument.SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET );
		put(SourceType.APPUPLOAD,SourceDocument.SECONDOUTLIER_APP_UPLOAD_UPLOAD );
//		put(SourceType.REPAYMENTORDER,SourceDocument.SECONDOUTLIER_REPAYMENT_ORDER );
	}};
	
	public static final  Map<PayWay,String> payWayMapSpec = new HashMap<PayWay,String>(){{
		put(PayWay.MERCHANT_DEDUCT_EASY_PAY,SourceDocument.SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET);
		put(PayWay.MERCHANT_DEDUCT,SourceDocument.SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET );
		put(PayWay.BUSINESS_DEDUCT,SourceDocument.SECONDOUTLIER_APP_UPLOAD_UPLOAD );
	}};
	
	/**Stirng to integer  just for  frontEnd **/
	
	public static  final Map<String, Integer>  cashFlowFrontEndShowStringMap = new  HashMap<String,Integer>(){{
		put( "广银联",CashFlowChannelType.Unionpay.ordinal());
		put( "宝付",CashFlowChannelType.BaoFu.ordinal());
		put( "建行",CashFlowChannelType.JIANHANG.ordinal());
//		put( "金运通",CashFlowChannelType.JinYunTong.ordinal());
//		put( "民生支付",CashFlowChannelType.MinShengPay.ordinal());
//		put( "新浪支付",CashFlowChannelType.SinaPay.ordinal());
//		put( "中金支付",CashFlowChannelType.ZhongJinPay.ordinal());
	}};
	
	public static  final Map<String, Integer>  repayTypeStringFrontEndShowMap = new  HashMap<String,Integer>(){{
		put( "提前划扣",ThirdJournalVoucherType.ADVANCE.ordinal());
		put( "正常" ,ThirdJournalVoucherType.NORMAL.ordinal());
		put( "逾期" ,ThirdJournalVoucherType.OVERDUE.ordinal());
	}};
	public static  final Map<String, Integer>  voucherSourceStringFrontEndShowMap = new  HashMap<String,Integer>(){{
		put( "系统线上支付单",SecondJournalVoucherType.ONLINE_PAYMENT_BILL.ordinal());
		put( "接口线上支付单",SecondJournalVoucherType.INTERFACE_PAYMNET_BILL.ordinal());
//		put( "商户上传扣款凭证",SecondJournalVoucherType.APP_UPLOAD_PAYMENT_BILL.ordinal());
	}};
	public static  final Map<String, Integer>  voucherStatusStringFrontEndShowMap = new  HashMap<String,Integer>(){{
		put( "核销中",JournalVoucherStatus.VOUCHER_CREATED.ordinal());
		put( "已核销",JournalVoucherStatus.VOUCHER_ISSUED.ordinal());
	}};
	
	
	/**Integer to String**/
	
	public static  final Map<Integer, String>  cashFlowStringMap = new  HashMap<Integer,String>(){{
		put( CashFlowChannelType.Unionpay.ordinal(),"广银联");
		put( CashFlowChannelType.BaoFu.ordinal(),"宝付");
		put( CashFlowChannelType.JinYunTong.ordinal(),"金运通");
		put( CashFlowChannelType.MinShengPay.ordinal(),"民生支付");
		put( CashFlowChannelType.SinaPay.ordinal(),"新浪支付");
		put( CashFlowChannelType.ZhongJinPay.ordinal(),"中金支付");
		put( CashFlowChannelType.JIANHANG.ordinal(),"建行");
		put( CashFlowChannelType.TONGLIAN.ordinal(),"通联");
		put( CashFlowChannelType.YEE_PAY.ordinal(),"易宝支付");
        put(CashFlowChannelType.LDYS.ordinal(), "联动优势");
		put (CashFlowChannelType.YEE_PAY.ordinal(), "网易支付");
	}};

	public static  final Map<Integer, String>  repayTypeStringMap = new  HashMap<Integer,String>(){{
		put( ThirdJournalVoucherType.ADVANCE.ordinal(),"提前划扣");
		put( ThirdJournalVoucherType.NORMAL.ordinal(),"正常");
		put( ThirdJournalVoucherType.OVERDUE.ordinal(),"逾期");
	}};
	public static  final Map<Integer, String>  voucherSourceStringMap = new  HashMap<Integer,String>(){{
		put( SecondJournalVoucherType.ONLINE_PAYMENT_BILL.ordinal(),"系统线上支付单");
		put( SecondJournalVoucherType.INTERFACE_PAYMNET_BILL.ordinal(),"接口线上支付单");
		put( SecondJournalVoucherType.APP_UPLOAD_PAYMENT_BILL.ordinal(),"商户上传扣款凭证");
	}};
	public static  final Map<Integer, String>  voucherStatusStringMap = new  HashMap<Integer,String>(){{
		put( JournalVoucherStatus.VOUCHER_CREATED.ordinal(),"核销中");
		put( JournalVoucherStatus.VOUCHER_ISSUED.ordinal(),"已核销");
	}};
	
	
	
	
}
