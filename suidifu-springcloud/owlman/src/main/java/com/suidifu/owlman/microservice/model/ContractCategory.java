package com.suidifu.owlman.microservice.model;

import lombok.Data;

@Data
public class ContractCategory {
    //financialContractUuid
    private String relatedBillContractInfoLv1;
    //contractUuid
    private String relatedBillContractInfoLv2;
    //assetSetUuid
    private String relatedBillContractInfoLv3;

    //信托项目名称
    private String relatedBillContractNoLv1;
    //合同编号
    private String relatedBillContractNoLv2;
    //还款计划编号
    private String relatedBillContractNoLv3;
    //订单编号（orderNo）
    private String relatedBillContractNoLv4;
}
