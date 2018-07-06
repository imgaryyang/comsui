package com.suidifu.owlman.microservice.handler;

import com.suidifu.owlman.microservice.model.TmpDepositReconciliationParameters;

import java.util.List;
import java.util.Map;

/**
 * 商户滞留单核销
 *
 * @author louguanyang at 2018/2/28 16:09
 * @mail louguanyang@hzsuidifu.com
 */
public interface TmpDepositReconciliationHandler {

    /**
     * stage one  数据切面
     *
     * @param detailUuids 明细Uuid列表
     * @return xxx
     */
    Map<String, String> criticalMarker(List<String> detailUuids);

    /**
     * stage two 验证明细列表
     *
     * @param params 待校验数据列表
     * @return xxx
     */
    boolean validateDetailList(List<TmpDepositReconciliationParameters> params);

    /**
     * stage three 虚拟账户转账
     *
     * @param params 数据列表
     * @return xxx
     */
    public abstract boolean virtualAccountTransfer(List<TmpDepositReconciliationParameters> params);

    /**
     * stage fourth 核销明细
     *
     * @param params 数据列表
     * @return xxx
     */
    public abstract boolean recoverDetails(List<TmpDepositReconciliationParameters> params);

    /**
     * stage fifth 解冻资金
     *
     * @param params 数据列表
     * @return xxx
     */
    public abstract boolean unfreezeCapital(List<TmpDepositReconciliationParameters> params);
}
