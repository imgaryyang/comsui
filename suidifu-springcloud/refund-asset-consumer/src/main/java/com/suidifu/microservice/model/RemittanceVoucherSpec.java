package com.suidifu.microservice.model;

import java.util.HashMap;
import java.util.Map;

import com.suidifu.microservice.enume.SourceDocumentDetailStatus;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;

public class RemittanceVoucherSpec {
	public static final Map<SourceDocumentDetailStatus,SourceDocumentStatus> SourceDocumentStatusConversion=new HashMap<SourceDocumentDetailStatus,SourceDocumentStatus>(){
		{
			put(SourceDocumentDetailStatus.UNSUCCESS, SourceDocumentStatus.CREATE);
			put(SourceDocumentDetailStatus.SUCCESS, SourceDocumentStatus.SIGNED);
			put(SourceDocumentDetailStatus.INVALID,SourceDocumentStatus.INVALID);
		}
	};
	public final static String TradeUuid="TradeUuid";
	public final static String BankName="BankName";
	
	public final static String RemittanceBillType="RemittanceBillType";
	
	public final static String RemittancePlanExecLog="RemittancePlanExecLog";
	public final static String RemittanceRefundBill="RemittanceRefundBill";
	
	public final static String SystemBillIdentity="SystemBillIdentity";
	
	public final static String PaymentInstitutionName="PaymentInstitutionName";
	
	public final static String ContractUuid="ContractUuid";
	
	public final static String ContractUniqueId="ContractUniqueId";

	public final static String ContractNo="ContractNo";

}
