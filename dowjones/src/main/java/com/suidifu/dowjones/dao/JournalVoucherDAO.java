package com.suidifu.dowjones.dao;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Date;

/**
 * @author xwq
 */
public interface JournalVoucherDAO {
    /**
     * 回购数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getRepurchaseData(String financialContractUuid, Date time);

    /**
     * 担保数据
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getGuaranteeData(String financialContractUuid, Date time);

    /**
     * 实际还款-线上还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getOnlineRepaymentData(String financialContractUuid, Date time);

    /**
     * 实际还款-线下还款
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getOfflineRepaymentData(String financialContractUuid, Date time);

    /**
     * 实际还款-线下支付单
     *
     * @param financialContractUuid 信托合同uuid
     * @param time                  某天
     * @return
     */
    Dataset<Row> getOfflinePaymentData(String financialContractUuid, Date time);
}
