package com.suidifu.barclays.service;

import com.suidifu.barclays.exception.BarclaysPersistenceException;
import com.suidifu.coffer.entity.QueryCreditResult;

/**
 * QueryCreditResultService
 *
 * @author whb
 * @date 2017/6/23
 */

public interface QueryCreditResultService {

    /**
     * Batch insert.
     *
     * @param queryCreditResult the query credit result
     * @throws BarclaysPersistenceException the barclays persistence exception
     * @author whb
     * @date 2017 -06-25
     */
    void batchInsert(QueryCreditResult queryCreditResult) throws BarclaysPersistenceException;

    /**
     * 根据批次号和交易请求号获取
     *
     * @param batchNo              the batch no
     * @param transactionVoucherNo the transaction voucher no
     * @return the by batch no and detail no
     * @throws BarclaysPersistenceException the barclays persistence exception
     * @author whb
     * @date 2017 -06-25
     */
    QueryCreditResult getByBatchNoAndDetailNo(String batchNo, String transactionVoucherNo) throws BarclaysPersistenceException;

    /**
     * 根据批次号删除
     *
     * @param batchNo the batch no
     * @throws BarclaysPersistenceException the barclays persistence exception * @author whb
     * @date 2017 -06-25
     * @author whb
     */
    void delByBatchNo(String batchNo) throws BarclaysPersistenceException;

    /**
     * Gets by detail no and in list.
     *
     * @param transactionVoucherNo the transaction voucher no
     * @param queryCreditResult    the query credit result
     * @return the by detail no and in list
     * @author whb
     * @date 2017 -06-26
     */
    QueryCreditResult getByDetailNoAndInList(String transactionVoucherNo, QueryCreditResult queryCreditResult);
}
