package com.suidifu.microservice.model;

import org.apache.commons.lang.StringUtils;

import com.zufangbao.sun.yunxin.entity.api.VoucherType;

public class RepaymentPlanDocumentModel {

	// 未知
	private static final int UNKNOWN_TYPE = -1;
	// 商户付款凭证
	private static final int BUSINESS_PAYMENT_VOUCHER_TYPE = 1;
	// 主动付款凭证
	private static final int ACTIVE_PAYMENT_VOUCHER_TYPE = 2;
	// 第三方扣款凭证
	private static final int THIRD_PARTY_DEDUCTION_VOUCHER_TYPE = 3;

	private long id;
	private String uuid;
	private int voucherType;
	/** 凭证编号 **/
	private String voucherNo;
	/** 收款账户号--专户账户 **/
	private String receivableAccountNo;
	/** 付款银行名称--往来机构名称 **/
	private String paymentBank;
	/** 付款银行帐户名称--账户名 **/
	private String paymentName;
	/** 付款账户号--机构账户号 **/
	private String paymentAccountNo;
	/** 凭证金额 **/
	private String amount;
	/** 凭证类型  **/
	private String voucherTypeCn;
	/** 凭证内容 **/
	private String content;
	/** 凭证来源 **/
	private String voucherSource;
	/** 凭证状态 **/
	private String status;


	public RepaymentPlanDocumentModel() {
		super();
	}
	public RepaymentPlanDocumentModel(Voucher voucher) {
		if(voucher == null){
			return ;
		}
		this.id = voucher.getId();
		this.uuid = voucher.getUuid();
		this.voucherNo = voucher.getVoucherNo();
		this.receivableAccountNo = voucher.getReceivableAccountNo();
		this.paymentBank = voucher.getPaymentBank();
		this.paymentName = voucher.getPaymentName();
		this.paymentAccountNo = voucher.getPaymentAccountNo();
		this.amount = voucher.getAmount().toString();
		VoucherType voucherTypeEnum = VoucherType.fromKey(voucher.getSecondType());
		this.voucherTypeCn = voucherTypeEnum == null ? StringUtils.EMPTY : voucherTypeEnum.getChineseMessage();
		this.voucherType = voucherTypeEnum == null ? -1 : voucherTypeEnum.ordinal();
		this.content = "";
		this.voucherSource = voucher.getVoucherSource();
		this.status = voucher.getVoucherStatus();
	}

	public RepaymentPlanDocumentModel(JournalVoucher jv, SourceDocumentDetail sdDetail){
		if(jv == null){
			return ;
		}
		this.id = jv.getId();
		this.uuid = jv.getJournalVoucherUuid();
		this.voucherNo = jv.getJournalVoucherNo();
		this.receivableAccountNo = jv.getSourceDocumentLocalPartyAccount();
		this.voucherTypeCn = StringUtils.EMPTY;
		this.voucherType = -1;
		if(sdDetail != null){
			this.paymentBank = sdDetail.getPaymentBank();
			VoucherType vourcherTypeEnum = VoucherType.fromKey(sdDetail.getSecondType());
			this.voucherTypeCn = vourcherTypeEnum == null ? "" : vourcherTypeEnum.getChineseMessage();
			this.voucherType = vourcherTypeEnum == null ? -1 : vourcherTypeEnum.ordinal();
		}
		this.paymentName = jv.getSourceDocumentCounterPartyName();
		this.paymentAccountNo = jv.getSourceDocumentCounterPartyAccount();
		this.amount = jv.getBookingAmount().toString();
		this.content = "";
		this.voucherSource = jv.getSecondJournalVoucherType()==null?"":ThirdPartVoucherSourceMapSpec.voucherSourceStringMap.get(jv.getSecondJournalVoucherType().ordinal());
		this.status = jv.getStatusName();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getReceivableAccountNo() {
		return receivableAccountNo;
	}
	public void setReceivableAccountNo(String receivableAccountNo) {
		this.receivableAccountNo = receivableAccountNo;
	}
	public String getPaymentBank() {
		return paymentBank;
	}
	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}
	public String getPaymentName() {
		return paymentName;
	}
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}
	public String getPaymentAccountNo() {
		return paymentAccountNo;
	}
	public void setPaymentAccountNo(String paymentAccountNo) {
		this.paymentAccountNo = paymentAccountNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getVoucherTypeCn() {
		return voucherTypeCn;
	}
	public void setVoucherTypeCn(String voucherTypeCn) {
		this.voucherTypeCn = voucherTypeCn;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getVoucherSource() {
		return voucherSource;
	}
	public void setVoucherSource(String voucherSource) {
		this.voucherSource = voucherSource;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(int voucherType) {
		this.voucherType = voucherType;
	}
	public int getRealLink(){
		if(VoucherType.createBusinessPaymentVoucherType(this.voucherType) != null){
			return BUSINESS_PAYMENT_VOUCHER_TYPE;
		}

		if(VoucherType.createThirdPartyDeductionVoucherType(this.voucherType) != null){
			return THIRD_PARTY_DEDUCTION_VOUCHER_TYPE;
		}

		if(VoucherType.createActivePaymentVoucherType(this.voucherType) != null){
			return ACTIVE_PAYMENT_VOUCHER_TYPE;
		}
		return UNKNOWN_TYPE;
	}
}
