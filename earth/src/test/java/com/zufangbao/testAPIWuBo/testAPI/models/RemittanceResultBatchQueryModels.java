package com.zufangbao.testAPIWuBo.testAPI.models;

/**
 * Created by Cool on 2017/8/14.
 */
public class RemittanceResultBatchQueryModels {
    private String oriRequestNo = null;
    private String remittanceId= null;
    private String uniqueId = null;


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
}
