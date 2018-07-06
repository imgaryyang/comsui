package com.suidifu.morganstanley.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetailInfoModel;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.excel.ExcelVoAttribute;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by luffych on 2017/5/21.
 */
public class RepaymentOrderDetail {

    private String repaymentOrderDetailUuid = UUID.randomUUID().toString();

    /**
     * 贷款合同编号
     */
    @ExcelVoAttribute(name = "contractNo", column = "A")
    private String contractNo;

    /**
     * 贷款合同唯一编号
     */
    @ExcelVoAttribute(name = "contractUniqueId", column = "B")
    private String contractUniqueId;

    /**
     * 还款方式
     */
    @ExcelVoAttribute(name = "repaymentWay", column = "C")
    private String repaymentWay;

    /**
     * 还款业务编号
     */
    @ExcelVoAttribute(name = "repaymentBusinessNo", column = "D")
    private String repaymentBusinessNo;

    /**
     * 设定还款时间
     */
    @ExcelVoAttribute(name = "plannedDate", column = "F")
    private String plannedDate;

    /**
     * 业务总金额
     */
    @ExcelVoAttribute(name = "detailsTotalAmount", column = "G")
    private String detailsTotalAmount;

    /**
     * 业务总金额明细
     */
    @ExcelVoAttribute(name = "detailsAmountJsonList", column = "H")
    private String detailsAmountJsonList;

    private List<RepaymentOrderDetailInfoModel> repaymentOrderDetailInfoList;

    /**
     * 校验失败信息
     */
    private String checkFailedMsg;

    public RepaymentOrderDetail() {
    }

    public RepaymentOrderDetail(String contractNo, String contractUniqueId, String repaymentWay, String repaymentBusinessNo,
                                String plannedDate, String detailsTotalAmount, String detailsAmountJsonList) {
        this.contractNo = contractNo;
        this.contractUniqueId = contractUniqueId;
        this.repaymentWay = repaymentWay;
        this.repaymentBusinessNo = repaymentBusinessNo;
        this.plannedDate = plannedDate;
        this.detailsTotalAmount = detailsTotalAmount;
        this.detailsAmountJsonList = detailsAmountJsonList;
    }

    @JSONField(serialize = false)
    public String getRepaymentOrderDetailUuid() {
        return repaymentOrderDetailUuid;
    }

    @JSONField(serialize = false)
    public Date getParsedPlannedDate() {
        if (StringUtils.isEmpty(this.plannedDate)) {
            return DateUtils.getToday();
        } else {
            return DateUtils.parseDate(this.plannedDate, "yyyy-MM-dd HH:mm:ss");
        }
    }

    public String getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(String plannedDate) {
        this.plannedDate = plannedDate;
    }

    public String getCheckFailedMsg() {
        return checkFailedMsg;
    }

    public void setCheckFailedMsg(String checkFailedMsg) {
        this.checkFailedMsg = checkFailedMsg;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractUniqueId() {
        return contractUniqueId;
    }

    public void setContractUniqueId(String contractUniqueId) {
        this.contractUniqueId = contractUniqueId;
    }

    @JSONField(serialize = false)
    public Long getFormatRepaymentWay() {
        return Long.parseLong(this.repaymentWay);
    }

    public String getRepaymentWay() {
        return repaymentWay;
    }

    public void setRepaymentWay(String repaymentWay) {
        this.repaymentWay = repaymentWay;
    }

    public String getRepaymentBusinessNo() {
        return repaymentBusinessNo;
    }

    public void setRepaymentBusinessNo(String repaymentBusinessNo) {
        this.repaymentBusinessNo = repaymentBusinessNo;
    }


    @JSONField(serialize = false)
    public BigDecimal getFormatDetailsTotalAmount() {
        return new BigDecimal(this.detailsTotalAmount);
    }

    public String getDetailsTotalAmount() {
        return detailsTotalAmount;
    }

    public void setDetailsTotalAmount(String detailsTotalAmount) {
        this.detailsTotalAmount = detailsTotalAmount;
    }

    @JSONField(serialize = false)
    public List<RepaymentOrderDetailInfoModel> getFormatRepaymentOrderDetailInfoList() {
        if (CollectionUtils.isNotEmpty(this.repaymentOrderDetailInfoList)) {
            return repaymentOrderDetailInfoList;
        }
        this.repaymentOrderDetailInfoList = JsonUtils.parseArray(this.detailsAmountJsonList, RepaymentOrderDetailInfoModel.class);
        if (CollectionUtils.isNotEmpty(repaymentOrderDetailInfoList)) {
            return repaymentOrderDetailInfoList;
        }
        return null;
    }

    public String getDetailsAmountJsonList() {
        return detailsAmountJsonList;
    }

    public void setDetailsAmountJsonList(String detailsAmountJsonList) {
        this.detailsAmountJsonList = detailsAmountJsonList;
    }

    @JSONField(serialize = false)
    public boolean isValid() {

        if (StringUtils.isEmpty(this.contractNo) && StringUtils.isEmpty(this.contractUniqueId)) {
            this.checkFailedMsg = "贷款合同唯一编号［uniqueId］或者贷款合同编号[contractNo]，不能为空！";
            return false;
        }
        if (StringUtils.isEmpty(this.repaymentWay)) {
            this.checkFailedMsg = "还款方式［repaymentWay],不能为空！";
            return false;
        }
        if (StringUtils.isEmpty(this.repaymentBusinessNo)) {
            this.checkFailedMsg = "还款业务编号［repaymentBusinessNo],不能为空！";
            return false;
        }
        if (StringUtils.isEmpty(this.plannedDate) || this.getParsedPlannedDate() == null) {
            this.checkFailedMsg = "设定还款时间［plannedDate],不能为空！";
            return false;
        }
        if (StringUtils.isEmpty(this.detailsAmountJsonList) && CollectionUtils.isEmpty(this.getFormatRepaymentOrderDetailInfoList())) {
            this.checkFailedMsg = "业务总金额明细[detailsAmountJsonList],不能为空";
            return false;
        }
        try {
            BigDecimal formatDetailsTotalAmount = this.getFormatDetailsTotalAmount();
            if (formatDetailsTotalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                this.checkFailedMsg = "业务总金额［detailsTotalAmount］，金额需高于0.00！";
                return false;
            }
            if (formatDetailsTotalAmount.scale() > 2) {
                this.checkFailedMsg = "业务总金额［detailsTotalAmount］，小数点后只保留2位！";
                return false;
            }
        } catch (Exception e) {
            this.checkFailedMsg = "业务总金额［detailsTotalAmount］，格式错误！";
            return false;
        }
        return true;
    }


}
