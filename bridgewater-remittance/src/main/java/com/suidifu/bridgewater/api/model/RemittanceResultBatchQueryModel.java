package com.suidifu.bridgewater.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang.StringUtils;

/**
 * Created by chengll on 17-4-6.
 * 查询结果(批量)类
 */
public class RemittanceResultBatchQueryModel {

    /**
     * 原放款请求号
     */
    private String oriRequestNo;
    /**
     * 商户订单号
     */
    private String remittanceId;

    /**
     * 贷款合同唯一编号
     */
    private String uniqueId;

    /**
     * 校验失败信息
     */
    private String checkFailedMsg;

    public String getOriRequestNo() {
        return oriRequestNo;
    }

    public void setOriRequestNo(String oriRequestNo) {
        this.oriRequestNo = oriRequestNo;
    }

    public String getRemittanceId() {
        return remittanceId;
    }

    public void setRemittanceId(String remittanceId) {
        this.remittanceId = remittanceId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @JSONField(serialize = false)
    public String getCheckFailedMsg() {
        return checkFailedMsg;
    }

    public void setCheckFailedMsg(String checkFailedMsg) {
        this.checkFailedMsg = checkFailedMsg;
    }

    @JSONField(serialize = false)
    public boolean isValid() {

        if(StringUtils.isEmpty(this.oriRequestNo) && StringUtils.isEmpty(this.remittanceId)) {
            this.checkFailedMsg = "请求列表中原始放款请求号［oriRequestNo］和商户订单 [remittanceId]都为空！";
            return false;
        }

        if(StringUtils.isEmpty(this.uniqueId)) {
            this.checkFailedMsg = "请求列表中存在为空的贷款合同唯一编号［uniqueId］！";
            return false;
        }
        return true;
    }

}
