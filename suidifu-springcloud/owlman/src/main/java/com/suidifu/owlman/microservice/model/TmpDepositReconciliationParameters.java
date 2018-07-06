package com.suidifu.owlman.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 滞留单核销参数
 *
 * @author louguanyang at 2018/2/28 16:23
 * @mail louguanyang@hzsuidifu.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmpDepositReconciliationParameters implements Serializable {

    private static final long serialVersionUID = 2304056127790320838L;

    private String sourceDocumentUuid;

    private String sourceDocumentDetailUuid;

    private String financialContractNo;

    private String ledgerBookNo;

    private String tmpDepositDocUuid;

    private String secondType;

    private BigDecimal detailTotalAmount;

    private String secondNo;

    public TmpDepositReconciliationParameters(String sourceDocumentUuid, String sourceDocumentDetailUuid,
                                              String financialContractNo, String ledgerBookNo, String tmpDepositDocUuid, BigDecimal detailTotalAmount, String secondNo) {
        super();
        this.sourceDocumentUuid = sourceDocumentUuid;
        this.sourceDocumentDetailUuid = sourceDocumentDetailUuid;
        this.financialContractNo = financialContractNo;
        this.ledgerBookNo = ledgerBookNo;
        this.tmpDepositDocUuid = tmpDepositDocUuid;
        this.detailTotalAmount = detailTotalAmount;
        this.secondNo = secondNo;
    }
}
