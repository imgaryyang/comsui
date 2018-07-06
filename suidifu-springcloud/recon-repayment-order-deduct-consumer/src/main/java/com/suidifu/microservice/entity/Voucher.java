package com.suidifu.microservice.entity;

import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.model.api.query.VoucherQueryApiResponse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

/**
 * 凭证
 * 	(商户付款凭证，主动付款凭证，第三方付款凭证)
 * @author louguanyang
 *
 */
@Entity
@Table(name = "t_voucher")
public class Voucher {

	@Id
	@GeneratedValue
	private long id;
	
	private String uuid;

	/**
	 * 凭证编号
	 */
	private String voucherNo;
	
	private String financialContractUuid;

	@Deprecated
	private String sourceDocumentUuid;
	
	private BigDecimal amount;
	
	@Enumerated(EnumType.ORDINAL)
	private SourceDocumentDetailStatus status = SourceDocumentDetailStatus.UNSUCCESS;
	
	/**
	 *  凭证来源
	 *  {@link VoucherSource}.key
	 */
	private String firstType;
	private String firstNo;//商户付款凭证：接口请求编号; 第三方扣款凭证：deductDetailUuid;
	/**
	 * 凭证类型
	 *  {@link VoucherType}.key
	 */
	private String secondType;
	private String secondNo;//商户付款凭证：外部打款流水号;第三方扣款凭证:deductDetailApplicationUuid;
	
	/**
	 * 收款账户号
	 */
	private String receivableAccountNo;
	/**
	 * 付款账户号
	 */
	private String paymentAccountNo;
	/**
	 * 付款银行帐户名称
	 */
	private String paymentName;
	/**
	 * 付款银行名称
	 */
	private String paymentBank;
	
	/**
	 * 校验状态
	 */
	@Enumerated(EnumType.ORDINAL)
	private SourceDocumentDetailCheckState checkState = SourceDocumentDetailCheckState.UNCHECKED;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	private Date lastModifiedTime;
	
	private String contractNo;
	
	private String comment;

	/**
	 * 关联流水唯一编号
	 */
	private String cashFlowUuid;

	/**
	 * 交易时间
	 */
	private Date transactionTime;
	
	/**
	 * 
	 * hjl
	 * 2017年10月19日
	 */
	private BigDecimal detailAmount;

	/**
	 * qinweichao
	 * 2017年12月05日
	 * SourceDocumentDetai存文件路径
	 */
	private String sourceDocumentDetailsFilePath;
	
	public BigDecimal getDetailAmount() {
		return detailAmount;
	}

	public void setDetailAmount(BigDecimal detailAmount) {
		this.detailAmount = detailAmount;
	}
	
	public boolean TemporaryDepositAmountIsNull(){
		if(detailAmount==null || detailAmount.compareTo(BigDecimal.ZERO)<=0){
			return true;
		}
		return false;
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

	public String getFinancialContractUuid() {
		return financialContractUuid;
	}

	public void setFinancialContractUuid(String financialContractUuid) {
		this.financialContractUuid = financialContractUuid;
	}

	public String getSourceDocumentUuid() {
		return sourceDocumentUuid;
	}

	public void setSourceDocumentUuid(String sourceDocumentUuid) {
		this.sourceDocumentUuid = sourceDocumentUuid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public SourceDocumentDetailStatus getStatus() {
		return status;
	}

	public void setStatus(SourceDocumentDetailStatus status) {
		this.status = status;
	}

	public String getFirstType() {
		return firstType;
	}

	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}

	public String getFirstNo() {
		return firstNo;
	}

	public void setFirstNo(String firstNo) {
		this.firstNo = firstNo;
	}

	public String getSecondType() {
		return secondType;
	}

	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}

	public String getSecondNo() {
		return secondNo;
	}

	public void setSecondNo(String secondNo) {
		this.secondNo = secondNo;
	}

	public String getReceivableAccountNo() {
		return receivableAccountNo;
	}

	public void setReceivableAccountNo(String receivableAccountNo) {
		this.receivableAccountNo = receivableAccountNo;
	}

	public String getPaymentAccountNo() {
		return paymentAccountNo;
	}

	public void setPaymentAccountNo(String paymentAccountNo) {
		this.paymentAccountNo = paymentAccountNo;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public SourceDocumentDetailCheckState getCheckState() {
		return checkState;
	}

	public void setCheckState(SourceDocumentDetailCheckState checkState) {
		this.checkState = checkState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCashFlowUuid() {
		return cashFlowUuid;
	}

	public void setCashFlowUuid(String cashFlowUuid) {
		this.cashFlowUuid = cashFlowUuid;
	}

	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getSourceDocumentDetailsFilePath() {
		return sourceDocumentDetailsFilePath;
	}

	public void setSourceDocumentDetailsFilePath(String sourceDocumentDetailsFilePath) {
		this.sourceDocumentDetailsFilePath = sourceDocumentDetailsFilePath;
	}

	public Voucher() {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.voucherNo = GeneratorUtils.generateVoucherNo();
		this.status = SourceDocumentDetailStatus.UNSUCCESS;
		this.checkState = SourceDocumentDetailCheckState.UNCHECKED;
		this.createTime = new Date();
		this.lastModifiedTime = new Date();
	}

	public Voucher(String financialContractUuid, BigDecimal amount, String firstType,
			String firstNo, String secondType, String secondNo, String receivableAccountNo, 
			String paymentAccountNo,String paymentName, String paymentBank, SourceDocumentDetailCheckState checkState, 
			String cashFlowUuid, Date transactionTime, String comment, BigDecimal detailAmount) {
		this();
		this.financialContractUuid = financialContractUuid;
		this.amount = amount;
		this.firstType = firstType;
		this.firstNo = firstNo;
		this.secondType = secondType;
		this.secondNo = secondNo;
		this.receivableAccountNo = receivableAccountNo;
		this.paymentAccountNo = paymentAccountNo;
		this.paymentName = paymentName;
		this.paymentBank = paymentBank;
		this.checkState = checkState;
		this.cashFlowUuid = cashFlowUuid;
		this.transactionTime = transactionTime;
		this.comment = comment;
		this.detailAmount = detailAmount;
	}

	public static Voucher createActivePaymentVoucher(String financialContractUuid,BigDecimal voucherAmount, 
			String firstNo, String secondType, String bankTransactionNo, String receivableAccountNo,
			String paymentAccountNo, String paymentName, String paymentBank,SourceDocumentDetailCheckState checkState,
			String cashFlowUuid, Date transactionTime, String comment, BigDecimal writrOffAmount) {
		try {
			String voucherSource = VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey();
			Voucher voucher = new Voucher(financialContractUuid, voucherAmount, voucherSource, firstNo, secondType, bankTransactionNo, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, checkState, cashFlowUuid, transactionTime, comment, writrOffAmount);
			voucher.setCheckState(checkState);
			return voucher;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean isUncheck() {
		return getCheckState() == SourceDocumentDetailCheckState.UNCHECKED;
	}
	
	public boolean isCheckFails() {
		return getCheckState() == SourceDocumentDetailCheckState.CHECK_FAILS;
	}

	public boolean is_business_payment_voucher() {
		return StringUtils.equals(VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey(), firstType);
	}

	public boolean is_third_party_deduction_voucher() {
		return StringUtils.equals(VoucherSource.THIRD_PARTY_DEDUCTION_VOUCHER.getKey(), firstType);
	}
	
	public boolean is_active_payment_voucher() {
		return StringUtils.equals(VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey(), firstType);
	}

	public boolean isInvalid() {
		return this.getStatus() == SourceDocumentDetailStatus.INVALID;
	}

	public String getVoucherType() {
		try {
			return VoucherType.fromKey(this.getSecondType()).getChineseMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
	}

	public String getVoucherSource() {
		try {
			return VoucherSource.fromKey(this.getFirstType()).getChineseMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
	}
	
	public String getVoucherStatus() {
		try {
			return this.getStatus().getChineseName();
		} catch (Exception e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
	}
	
	public static List<Map<String, Object>> getVoucherStatusList() {
		List<Map<String, Object>> voucherStatusList = EnumUtil.getKVList(SourceDocumentDetailStatus.class);
		Integer key = SourceDocumentDetailStatus.values().length;
		
		Map<String, Object> uncommitted = new HashMap<>();
		uncommitted.put("key", key);
		uncommitted.put("value", SourceDocumentDetailCheckState.UNCOMMITTED.getChineseName());
		voucherStatusList.add(uncommitted);
		
		Map<String, Object> checkFails = new HashMap<>();
		checkFails.put("key", key+1);
		checkFails.put("value", SourceDocumentDetailCheckState.CHECK_FAILS.getChineseName());
		voucherStatusList.add(checkFails);
		
		Map<String, Object> unchecked = new HashMap<>();
		unchecked.put("key", key+2);
		unchecked.put("value", SourceDocumentDetailCheckState.UNCHECKED.getChineseName());
		voucherStatusList.add(unchecked);
		return voucherStatusList;
	}
	
	public String getVoucherStatusMessage() {
		if(status == SourceDocumentDetailStatus.UNSUCCESS && (checkState == SourceDocumentDetailCheckState.UNCHECKED || checkState == SourceDocumentDetailCheckState.UNCOMMITTED || checkState == SourceDocumentDetailCheckState.CHECK_FAILS)) {
			return checkState.getChineseName();
		}
		return status.getChineseName();
	}

	public VoucherQueryApiResponse getVoucherQueryApiResponse(String financialContractCode) {
		try {
			VoucherQueryApiResponse response = new VoucherQueryApiResponse();
			response.setFinancialContractCode(financialContractCode);
			response.setVoucherNo(this.voucherNo);
			response.setVoucherSourceNo(this.firstNo);
			response.setBankTransactionNo(this.secondNo);
			response.setVoucherAmount(this.amount);
			response.setVoucherCheckState(this.checkState.ordinal());
			response.setVoucherStatus(this.status.ordinal());
			response.setDetail(Collections.emptyList());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Voucher(String financialContractUuid, String cashFlowUuid,Date transactionTime, BigDecimal amount, String firstType,
			String firstNo, String secondType, String secondNo, String receivableAccountNo, String paymentAccountNo,
			String paymentName, String paymentBank, BigDecimal detailAmount) {
		this();
		this.financialContractUuid = financialContractUuid;
		this.cashFlowUuid = cashFlowUuid;
		this.transactionTime = transactionTime;
		this.amount = amount;
		this.firstType = firstType;
		this.firstNo = firstNo;
		this.secondType = secondType;
		this.secondNo = secondNo;
		this.receivableAccountNo = receivableAccountNo;
		this.paymentAccountNo = paymentAccountNo;
		this.paymentName = paymentName;
		this.paymentBank = paymentBank;
		this.detailAmount = detailAmount;
	}

	public boolean isActiveVoucher(){
		return VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey().equals(getFirstType());
	}
	
	public VoucherSource getVoucherSourceEnum(){
		return VoucherSource.fromKey(this.getFirstType());
	}
}
