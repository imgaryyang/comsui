package com.suidifu.bridgewater.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModifyRemittanceApplicationModel {

    // 请求编号
    private String requestNo;
    // 商户订单号
    private String remittanceId;
    // 交易号
    private String tradeNo;
    // 信托产品代码
    private String financialProductCode;
    // 放款请求编号
    private String remittanceTradeNo;
    // 变更放款明细
    private String remittanceDetails;
    // 审核人
    private String approver;
    // 审核时间
    private String approvedTime;
    // 备注
    private String comment;

    private List<RemittanceDetail> remittanceDetailList;

    private String remittanceApplicationUuid;

    private String errorMsg;

    public ModifyRemittanceApplicationModel() {

    }

    @JSONField(serialize = false)
    public List<RemittanceDetail> getRemittanceDetailList(){

        if(CollectionUtils.isNotEmpty(this.remittanceDetailList)) {
            return this.remittanceDetailList;
        }
        this.remittanceDetailList = JsonUtils.parseArray(this.remittanceDetails, RemittanceDetail.class);
        if(CollectionUtils.isNotEmpty(this.remittanceDetailList)) {
            Collections.sort(this.remittanceDetailList);
            return this.remittanceDetailList;
        }
        return null;
    }

    @JSONField(serialize = false)
    public boolean isValid(){
        if(StringUtils.isBlank(this.getRequestNo()) && StringUtils.isBlank(this.getRemittanceId())){
            this.setErrorMsg("请求唯一标识［requestNo］和 商户订单号 [remittanceId]，至少填写一个！");
            return false;
        }

        if(StringUtils.isBlank(this.getTradeNo())){
            this.setErrorMsg("交易号[tradeNo],不能为空");
            return false;
        }

        if(StringUtils.isBlank(this.getFinancialProductCode())){
            this.setErrorMsg("信托产品代码[financialProductCode],不能为空");
            return false;
        }

        if(StringUtils.isBlank(this.getRemittanceTradeNo())){
            this.setErrorMsg("放款请求编号[remittanceTradeNo],不能为空");
            return false;
        }

        return validateRemittanceDetails();
    }

    private boolean validateRemittanceDetails(){
        List<RemittanceDetail> remittanceDetailList = this.getRemittanceDetailList();
        if (CollectionUtils.isEmpty(remittanceDetailList)) {
            this.setErrorMsg("变更放款明细列表［remittanceDetails］，格式错误，不能为空列表！");
            return false;
        }

        Set<String> detailNoSet = new HashSet<String>();
        for (int i =0;i<remittanceDetailList.size();i++){
            RemittanceDetail remittanceDetail = remittanceDetailList.get(i);
            if(!remittanceDetail.isValid()) {
                this.setErrorMsg("放款明细列表［remittanceDetails］，行［"+i+"］，" +remittanceDetail.getCheckFailedMsg());
                return false;
            }
            detailNoSet.add(remittanceDetail.getDetailNo());
        }
        if(detailNoSet.size() != remittanceDetailList.size()) {
            this.setErrorMsg("放款明细列表［remittanceDetails］内，明细记录号［detailNo］有重复！");
            return false;
        }
        return true;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getFinancialProductCode() {
        return financialProductCode;
    }

    public void setFinancialProductCode(String financialProductCode) {
        this.financialProductCode = financialProductCode;
    }

    public String getRemittanceTradeNo() {
        return remittanceTradeNo;
    }

    public void setRemittanceTradeNo(String remittanceTradeNo) {
        this.remittanceTradeNo = remittanceTradeNo;
    }

    public String getRemittanceDetails() {
        return remittanceDetails;
    }

    public void setRemittanceDetails(String remittanceDetails) {
        this.remittanceDetails = remittanceDetails;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRemittanceId() {
        return remittanceId;
    }

    public void setRemittanceId(String remittanceId) {
        this.remittanceId = remittanceId;
    }

    @JSONField(serialize = false)
    public String getErrorMsg() {
        return errorMsg;
    }

    @JSONField(deserialize = false)
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @JSONField(serialize = false)
    public String getRemittanceApplicationUuid() {
        return remittanceApplicationUuid;
    }

    @JSONField(deserialize = false)
    public void setRemittanceApplicationUuid(String remittanceApplicationUuid) {
        this.remittanceApplicationUuid = remittanceApplicationUuid;
    }
}
