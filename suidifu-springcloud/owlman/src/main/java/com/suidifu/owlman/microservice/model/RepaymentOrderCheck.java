package com.suidifu.owlman.microservice.model;

import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.model.FastContract;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentOrderCheck {
    private boolean isValid = true;
    private String errorMsg = "";

    private String repaymentBusinessUuid;
    private RepaymentBusinessType repaymentBusinessType;
    private FastContract fastContract;
    private RepurchaseDoc repurchaseDoc;
    private FastAssetSet fastAssetSet;
    private String merId;
    private String financialContractUuid;
    private String financialContractNo;
    private boolean isAllowDetailEmpty;

    public String getCustomerUuid() {
        return fastContract == null ? "" : fastContract.getCustomerUuid();
    }

    public String getFastContractUuid() {
        return fastContract == null ? "" : fastContract.getUuid();
    }

    public void fail(String errorMsg) {
        this.isValid = false;
        this.errorMsg = errorMsg;
    }
}