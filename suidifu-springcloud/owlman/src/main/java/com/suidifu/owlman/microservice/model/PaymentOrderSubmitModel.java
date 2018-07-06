package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.utils.JsonUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderSubmitModel {
    /**
     * 信托合同Uuid
     **/
    private String financialContractUuid;
    /**
     * 信托合同编号
     **/
    private String financialContractNo;
    /**
     * 还款订单号,五维订单号
     **/
    private String orderUuid;
    /**
     * 外部凭据uuid:如Cashflow.cashFlowUuid
     **/
    private String cashFlowUuid;
    /**
     * 对方户名
     **/
    private String counterAccountName;
    /**
     * 对方账号
     **/
    private String counterAccountNo;
    /**
     * 对方开户行
     **/
    private String counterBankName;
    /**
     * 支付方式
     **/
    private Integer payWay;
    /**
     * 备注
     **/
    private String remark;
    /**
     * 多条流水uuid
     **/
    private String cashFlowUuidList;

    public List<String> getCashFlowList() {
        return JsonUtils.parseArray(this.cashFlowUuidList, String.class);
    }
}