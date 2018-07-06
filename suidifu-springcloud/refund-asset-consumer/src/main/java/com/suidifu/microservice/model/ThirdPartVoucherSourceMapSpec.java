package com.suidifu.microservice.model;

import java.util.HashMap;
import java.util.Map;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.enume.SecondJournalVoucherType;
import com.suidifu.microservice.enume.ThirdJournalVoucherType;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;

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
	
	

	/****************凭证来源 转换map******************/	
	public static  final Map<String, SecondJournalVoucherType> SecondJournalVoucherTypeMap = new HashMap<String,SecondJournalVoucherType>(){{
		put(SourceDocument.SECONDOUTLIER_SYSTEM_ON_LINE_PAYMENT, SecondJournalVoucherType.ONLINE_PAYMENT_BILL);
		put(SourceDocument.SECONDOUTLIER_INTERFACE_ON_LINE_PAYMNET, SecondJournalVoucherType.INTERFACE_PAYMNET_BILL);
		put(SourceDocument.SECONDOUTLIER_APP_UPLOAD_UPLOAD, SecondJournalVoucherType.APP_UPLOAD_PAYMENT_BILL);
//		put(SourceDocument.SECONDOUTLIER_REPAYMENT_ORDER, SecondJournalVoucherType.REPAYMENT_ORDER);
		
	}};
	
	
	
}
