package com.zufangbao.earth.yunxin.api.model.command;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ActivePaymentVoucherDetail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 主动付款凭证指令模型
 *
 * @author louguanyang
 */
@Data
@CommonsLog
public class ActivePaymentVoucherCommandModel {

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
   * 凭证类型(5:主动付款，6:他人代付)（必填）
   */
  private Integer voucherType;
  /**
   * 收款账户号（选填）
   */
  private String receivableAccountNo;
  /**
   * 付款银行名称（必填）
   */
  private String paymentBank;
  /**
   * 银行流水号（必填）
   */
  private String bankTransactionNo;
  /**
   * 凭证金额（必填）
   */
  private BigDecimal voucherAmount = BigDecimal.ZERO;
  /**
   * 付款银行帐户名称（必填）
   */
  private String paymentName;
  /**
   * 付款账户号（必填）
   */
  private String paymentAccountNo;
  /**
   * 信托产品代码（必填）
   */
  private String financialContractNo;
  /**
   * 明细（必填）
   */
  private String detail;
  /**
   * 校验失败信息
   */
  private String checkFailedMsg;

  @JSONField(serialize = false)
  public boolean submitParamsError() {
    if (isNecessaryParamsError()) {
      return true;
    }
    if (this.voucherType == null) {
      this.checkFailedMsg = "凭证类型［voucherType］，不能为空！";
      return true;
    }
    if (this.getVoucherTypeEnum() == null) {
      this.checkFailedMsg = "凭证类型［voucherType］错误，内容不支持！";
      return true;
    }
    if (this.voucherAmount.compareTo(BigDecimal.ZERO) <= 0) {
      this.checkFailedMsg = "凭证金额［voucherAmount］，必需大于0.00！";
      return true;
    }
    if (StringUtils.isEmpty(this.paymentAccountNo)) {
      this.checkFailedMsg = "付款账户号［paymentAccountNo］，不能为空！";
      return true;
    }
    if (StringUtils.isEmpty(this.paymentName)) {
      this.checkFailedMsg = "付款银行帐户名称［paymentName］，不能为空！";
      return true;
    }
    if (StringUtils.isEmpty(this.paymentBank)) {
      this.checkFailedMsg = "付款银行名称［paymentBank］，不能为空！";
      return true;
    }
    if (StringUtils.isEmpty(this.financialContractNo)) {
      this.checkFailedMsg = "信托产品代码［financialContractNo］，不能为空！";
      return true;
    }
    return detailHasError();
  }

  @JSONField(serialize = false)
  public VoucherType getVoucherTypeEnum() {
    return VoucherType.createActivePaymentVoucherType(this.voucherType);
  }


  @JSONField(serialize = false)
  private boolean isNecessaryParamsError() {
    if (StringUtils.isEmpty(this.bankTransactionNo)) {
      this.checkFailedMsg = "付款账户流水号［bankTransactionNo］，不能为空！";
      return true;
    }
    return false;
  }

  @JSONField(serialize = false)
  public boolean undoParamsError() {
    return isNecessaryParamsError();
  }

  @JSONField(serialize = false)
  public List<ActivePaymentVoucherDetail> getDetailModel() {
    List<ActivePaymentVoucherDetail> detailModels = JsonUtils
        .parseArray(this.detail, ActivePaymentVoucherDetail.class);
    if (CollectionUtils.isEmpty(detailModels)) {
      throw new ApiException(ApiResponseCode.WRONG_FORMAT);
    }
    return detailModels;
  }

  @JSONField(serialize = false)
  private boolean detailHasError() {
    try {

      if (StringUtils.isEmpty(this.detail)) {
        this.checkFailedMsg = "明细［detail］，不能为空！";
        return true;
      }
      List<String> repayScheduleNoList = new ArrayList<>();
      List<String> repaymentPlanNoList = new ArrayList<>();
      List<Integer> currentPeriodList = new ArrayList<>();
      BigDecimal detailAmount = BigDecimal.ZERO;
      for (ActivePaymentVoucherDetail voucherDetail : getDetailModel()) {
        if (voucherDetail.paramsError()) {
          this.checkFailedMsg = "请选填其中一种编号［uniqueId，contractNo］！";
          return true;
        }
        if (voucherDetail.isNotEmptyRepaymentPlanNoAndRepayScheduleNoAndrepaymentNumber()) {
          this.checkFailedMsg = "商户还款计划编号[repayScheduleNo],还款计划编号[repaymentPlanNo],期数[currentPeriod]  三者不可全为空,至少有一个不为空 !!!";
          return true;
        }
        String repayScheduleNo = voucherDetail.getRepayScheduleNo();
        if (StringUtils.isNotEmpty(repayScheduleNo)) {
          if (repayScheduleNoList.contains(repayScheduleNo)) {
            this.checkFailedMsg = "商户还款计划编号[repayScheduleNo] 不能重复!!!";
            return true;
          }
          repayScheduleNoList.add(repayScheduleNo);
        }
        String repaymentPlanNo = voucherDetail.getRepaymentPlanNo();
        if (StringUtils.isNotEmpty(repaymentPlanNo)) {
          if (repaymentPlanNoList.contains(repaymentPlanNo)) {
            this.checkFailedMsg = "商户还款计划编号[repaymentPlanNo] 不能重复!!!";
            return true;
          }
          repaymentPlanNoList.add(repaymentPlanNo);
        }
        Integer currentPeriod = voucherDetail.getCurrentPeriod();
        if (currentPeriod != null && currentPeriod != 0) {
          if (currentPeriodList.contains(currentPeriod)) {
            this.checkFailedMsg = "明细中存在相同的期数[currentPeriod]!!!";
            return true;
          }
          currentPeriodList.add(currentPeriod);
        }
        detailAmount = detailAmount.add(voucherDetail.getAmount());
      }
      if (getDetailModel().stream().anyMatch(detail -> !detail.isValid())) {
        this.checkFailedMsg = "凭证明细内容错误［detail］，字段格式错误！";
        return true;
      }
      if (this.voucherAmount.compareTo(detailAmount) != 0) {
        this.checkFailedMsg = "凭证金额与明细总金额不匹配！";
        return true;
      }
      return false;
    } catch (GlobalRuntimeException e) {
      this.checkFailedMsg = e.getMessage();
      return true;
    }
  }

  @JSONField(serialize = false)
  private List<String> repaymentPlanNoList;

  @JSONField(serialize = false)
  public List<String> get_repayment_plan_no_list() {
    try {
      if (CollectionUtils.isEmpty(repaymentPlanNoList)) {
        repaymentPlanNoList = getDetailModel().stream()
            .map(ActivePaymentVoucherDetail::getRepaymentPlanNo).collect(
                Collectors.toList());
      }
      return repaymentPlanNoList;
    } catch (Exception e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
}