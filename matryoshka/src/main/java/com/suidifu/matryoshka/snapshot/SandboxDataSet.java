package com.suidifu.matryoshka.snapshot;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贷后砂箱数据集合
 * @author louguanyang on 2017/5/8.
 */
@Data
@NoArgsConstructor
public class SandboxDataSet {
    private FinancialContractSnapshot financialContractSnapshot;
    private ContractSnapshot contractSnapshot;
    private RepurchaseDocSnapshot repurchaseDocSnapshot;
    private List<PaymentPlanSnapshot> paymentPlanSnapshotList;
    private Map<String, String> extraData;

    public SandboxDataSet(FinancialContractSnapshot financialContractSnapshot, ContractSnapshot contractSnapshot,RepurchaseDocSnapshot repurchaseDocSnapshot,
                          List<PaymentPlanSnapshot> paymentPlanSnapshotList) {
        super();
        this.financialContractSnapshot = financialContractSnapshot;
        this.contractSnapshot = contractSnapshot;
        this.repurchaseDocSnapshot = repurchaseDocSnapshot;
        this.paymentPlanSnapshotList = paymentPlanSnapshotList;
        this.extraData = new HashMap<>();
    }

	public SandboxDataSet(HashMap<String, String> extraData) {
		this.extraData = extraData;
		this.financialContractSnapshot = null;
        this.contractSnapshot = null;
        this.paymentPlanSnapshotList = Collections.emptyList();
    }

    /**
     * @return 身份证号
     */
    public String getIdCardNo() {
        try {
            return getContractSnapshot().getCustomerAccountSnapshot().getAccount();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 贷款合同UniqueId
     */
    public String getContractUniqueId() {
        try {
            return getContractSnapshot().getUniqueId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 贷款合同uuid
     */
    public String getContractUuid() {
        try {
            return getContractSnapshot().getUuid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 信托合同financialContractUuid
     */
    public String getFinancialContractUuid() {
        try {
            return getFinancialContractSnapshot().getFinancialContractUuid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
