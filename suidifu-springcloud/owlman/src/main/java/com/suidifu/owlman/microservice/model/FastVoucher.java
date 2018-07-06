/**
 *
 */
package com.suidifu.owlman.microservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wukai
 */
@Data
@NoArgsConstructor
public class FastVoucher {
    private String uuid;

    private String firstNo;

    private String financialContractUuid;

    private String cashFlowUuid;
}