package com.zufangbao.earth.yunxin.api.model.command;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.NO_SUCH_VOUCHER_TYPE;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 商户付款凭证指令模型
 *
 * @author louguanyang
 */
public class BusinessPaymentVoucherCommandModel {
    /**
     * 功能代码（必填）
     */
    private String fn;
    /**
     * 请求唯一标识（必填）
     */
    private String requestNo;
    /**
     * 交易类型(0:提交，1:撤销)（必填）
     */
    private Integer transactionType;
    /**
     * 凭证类型(0:委托转付，1:担保补足，2:回购，3:差额划拨)（必填）
     */
    private Integer voucherType;
    /**
     * 凭证金额（必填）
     */
    private BigDecimal voucherAmount = BigDecimal.ZERO;
    /**
     * 信托产品代码（必填）
     */
    private String financialContractNo;
    /**
     * 收款账户号（选填）
     */
    private String receivableAccountNo;
    /**
     * 付款账户号（必填）
     */
    private String paymentAccountNo;
    /**
     * 付款银行帐户名称（必填）
     */
    private String paymentName;
    /**
     * 付款银行名称（必填）
     */
    private String paymentBank;
    /**
     * 银行流水号（必填）
     */
    private String bankTransactionNo;
    /**
     * 明细（必填）
     */
    private String detail;
    /**
     * 校验失败信息
     */
    private String checkFailedMsg;

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(Integer voucherType) {
        this.voucherType = voucherType;
    }

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public String getFinancialContractNo() {
        return financialContractNo;
    }

    public void setFinancialContractNo(String financialContractNo) {
        this.financialContractNo = financialContractNo;
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

    public String getBankTransactionNo() {
        return bankTransactionNo;
    }

    public void setBankTransactionNo(String bankTransactionNo) {
        this.bankTransactionNo = bankTransactionNo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCheckFailedMsg() {
        return checkFailedMsg;
    }

    public void setCheckFailedMsg(String checkFailedMsg) {
        this.checkFailedMsg = checkFailedMsg;
    }

    @JSONField(serialize = false)
    public List<BusinessPaymentVoucherDetail> getDetailModel() {
        List<BusinessPaymentVoucherDetail> detailModels = JsonUtils.parseArray(this.detail, BusinessPaymentVoucherDetail.class);
        if (CollectionUtils.isEmpty(detailModels)) {
            throw new ApiException(ApiResponseCode.WRONG_FORMAT);
        }
        return detailModels;
    }

	@JSONField(serialize = false)
	public boolean checkSubmitParams() {
		return checkParams() && checkDetail();
	}
    @JSONField(serialize = false)
    public boolean checkUndoParams() {
        return checkNecessaryParam();
    }

    @JSONField(serialize = false)
    private boolean checkNecessaryParam() {
        if (StringUtils.isEmpty(this.requestNo)) {
            this.checkFailedMsg = "请求唯一标识［requestNo］，不能为空！";
            return false;
        }
        if (StringUtils.isEmpty(this.bankTransactionNo)) {
            this.checkFailedMsg = "付款账户流水号［bankTransactionNo］，不能为空！";
            return false;
        }
        if (StringUtils.isEmpty(this.financialContractNo)) {
            this.checkFailedMsg = "信托产品代码［financialContractNo］，不能为空！";
            return false;
        }
        return true;
    }

	@JSONField(serialize = false)
	private boolean checkParams() {
		if(!checkNecessaryParam()) {
			return false;
		}
		if (this.voucherType == null) {
			this.checkFailedMsg = "凭证类型［voucherType］，不能为空！";
			return false;
		}
		if (VoucherType.createBusinessPaymentVoucherType(this.voucherType) == null) {
			this.checkFailedMsg = "商户付款凭证类型［voucherType］错误！";
			return false;
		}
		if (this.voucherAmount.compareTo(BigDecimal.ZERO) <= 0) {
			this.checkFailedMsg = "凭证金额［voucherAmount］，必需大于0.00！";
			return false;
		}
		if (StringUtils.isEmpty(this.paymentAccountNo)) {
			this.checkFailedMsg = "付款账户号［paymentAccountNo］，不能为空！";
			return false;
		}
		if (StringUtils.isEmpty(this.paymentName)) {
			this.checkFailedMsg = "付款银行帐户名称［paymentName］，不能为空！";
			return false;
		}
		if (StringUtils.isEmpty(this.paymentBank)) {
			this.checkFailedMsg = "付款银行名称［paymentBank］，不能为空！";
			return false;
		}
		return true;
	}

	@JSONField(serialize = false)
	private boolean checkDetail() {
		try {
			if (StringUtils.isEmpty(this.detail)) {
				this.checkFailedMsg = "明细［detail］，不能为空！";
				return false;
			}
			/* 9.25 取消 回购类型 对商户还款计划编号,还款计划编号,期数的校验*/
			if (null != this.voucherType && this.voucherType != VoucherType.REPURCHASE.getOrdinal()) {
				List<String> repayScheduleNoList = new ArrayList<>();
				List<Integer> currentPeriodList = new ArrayList<>();
				List<String> repaymentPlanNoList = new ArrayList<>();
				for (BusinessPaymentVoucherDetail detail : getDetailModel()) {
					if (detail.isNotEmptyrepayScheduleNoAndrepaymentPlanNoAndrepaymentNumber()) {
						this.checkFailedMsg = "商户还款计划编号[repayScheduleNo],还款计划编号[repaymentPlanNo],期数[currentPeriod]  至少一个不为空 !!!";
						return false;
					}
					String repayScheduleNo = detail.getRepayScheduleNo();
					if (!StringUtils.isEmpty(repayScheduleNo)) {
						if (repayScheduleNoList.contains(repayScheduleNo)) {
							this.checkFailedMsg = "商户还款计划编号[repayScheduleNo] 不能重复!!!";
							return false;
						}
						repayScheduleNoList.add(repayScheduleNo);
					}
					Integer currentPeriod = detail.getCurrentPeriod();
					if (currentPeriod != null && currentPeriod != 0) {
						if (currentPeriodList.contains(currentPeriod)) {
							this.checkFailedMsg = "明细中的期数不可重复相同[currentPeriod]";
							return false;
						}
						currentPeriodList.add(currentPeriod);
					}
					String repaymentPlanNo = detail.getRepaymentPlanNo();
					if (StringUtils.isNotEmpty(repaymentPlanNo)) {
						if (repaymentPlanNoList.contains(repaymentPlanNo)) {
							this.checkFailedMsg = "还款计划编号重复, repaymentPlanNo:" + repaymentPlanNo;
							return false;
						}
						repaymentPlanNoList.add(repaymentPlanNo);
					}
				}
			}
			if (this.voucherType != null) {
				boolean repaymentPlanNotNullFlag = VoucherType.createRepaymentPlanNotNull(this.voucherType) != null;
				if(getDetailModel().stream().anyMatch(detail -> !detail.isValid(repaymentPlanNotNullFlag))) {
					this.checkFailedMsg = "凭证明细内容错误［detail］，字段格式错误！";
					return false;
				}
			}
			BigDecimal detailAmount = BigDecimal.ZERO;
			List<BusinessPaymentVoucherDetail> detailModel = getDetailModel();
			for (BusinessPaymentVoucherDetail detail : detailModel) {
				if(!checkRepurchaseDetail(detail)){
					return false;
				}
				detailAmount = detailAmount.add(detail.getAmount());
				if(!StringUtils.equals(detail.getTransactionTime(), DateUtils.DATE_FORMAT_0001_01_01) && !StringUtils.isEmpty(detail.getTransactionTime()) && detail.getTransactionDate() == null){
					this.checkFailedMsg = "设定还款日期,格式有误！";
					return false;
				}
			}
			if(this.voucherAmount.compareTo(detailAmount) != 0) {
				this.checkFailedMsg = "凭证金额与明细总金额不匹配！";
				return false;
			}
			return true;
		} catch (GlobalRuntimeException e) {
			this.checkFailedMsg = e.getMessage();
			return false;
		}
	}

	@JSONField(serialize = false)
	private boolean checkRepurchaseDetail(BusinessPaymentVoucherDetail detail) {
		if(!Integer.valueOf(VoucherType.REPURCHASE.getOrdinal()).equals(this.voucherType)){
			return true;
		}
		BigDecimal detailTotalAmount= AmountUtils
				.addAll(detail.getPrincipal(),detail.getInterest(),detail.getPenaltyFee(),detail.getOtherCharge());
		BigDecimal totalAmount=detail.getAmount();

		if(detailTotalAmount.compareTo(BigDecimal.ZERO) ==1 && detailTotalAmount.compareTo(totalAmount) != 0) {
			this.checkFailedMsg = "回购凭证明细总金额与明细其他金额总和不匹配！";
			return false;
		}
		return true;
	}
	@JSONField(serialize = false)
	public VoucherType getVoucherTypeEnum() {
		VoucherType business_payment_voucher_type = VoucherType.createBusinessPaymentVoucherType(getVoucherType());
		if(business_payment_voucher_type == null) {
			throw new ApiException(NO_SUCH_VOUCHER_TYPE);
		}
		return business_payment_voucher_type;
	}

	@JSONField(serialize = false)
	public Voucher createBusinessVoucher(String financialContractUuid, String cashFlowUuid, Date transactionTime) {
		try {
			String voucherSource = VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey();
			return createVoucher(financialContractUuid, cashFlowUuid, transactionTime, voucherSource);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@JSONField(serialize = false)
	private Voucher createVoucher(String financialContractUuid, String cashFlowUuid, Date transactionTime, String voucherSource) {
		BigDecimal voucherAmount = this.getVoucherAmount();
		String firstNo = this.getRequestNo();
		String secondType = this.getVoucherTypeEnum().getKey();
		String secondNo = this.getBankTransactionNo();
		String receivableAccountNo = this.getReceivableAccountNo();
		String paymentAccountNo = this.getPaymentAccountNo();
		String paymentName = this.getPaymentName();
		String paymentBank = this.getPaymentBank();
		return new Voucher(financialContractUuid, cashFlowUuid, transactionTime, voucherAmount, voucherSource, firstNo, secondType, secondNo, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, voucherAmount);
	}

}
