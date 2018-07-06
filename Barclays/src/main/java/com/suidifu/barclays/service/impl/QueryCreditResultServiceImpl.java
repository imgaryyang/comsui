package com.suidifu.barclays.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.barclays.exception.BarclaysPersistenceException;
import com.suidifu.barclays.service.QueryCreditResultService;
import com.suidifu.coffer.entity.BusinessProcessStatus;
import com.suidifu.coffer.entity.BusinessRequestStatus;
import com.suidifu.coffer.entity.QueryCreditResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QueryCreditResultServiceImpl
 *
 * @author whb
 * @date 2017/6/25
 */
@Component(value = "queryCreditResultService")
public class QueryCreditResultServiceImpl implements QueryCreditResultService {

    @Autowired private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(QueryCreditResult queryCreditResult) throws BarclaysPersistenceException {
        if (null == queryCreditResult) {
            return;
        }
        List<QueryCreditResult> resultList = queryCreditResult.getQueryCreditResultList();
        // 当QueryCreditResultList为空时不保存
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }

        final String addSql = "insert into third_party_query_credit_result (" +
                "comm_code,err_msg,source_account_no,source_account_name,destination_account_no," +
                "destination_account_name,transaction_amount,acceptance_time,transaction_voucher_no," +
                "channel_sequence_no, request_status,process_status,business_result_msg," +
                "business_success_time,batch_no) values(:commCode,:errMsg,:sourceAccountNo," +
                ":sourceAccountName,:destinationAccountNo,:destinationAccountName,:transactionAmount," +
                ":acceptanceTime,:transactionVoucherNo,:channelSequenceNo,:requestStatus,:processStatus," +
                ":businessResultMsg,:businessSuccessTime,:batchNo);";

        SqlParameterSource[] batchArgs = new SqlParameterSource[resultList.size()];

        try {
            for (int i = 0; i < resultList.size(); i++) {
                QueryCreditResult bean = resultList.get(i);
                bean.setBatchNo(queryCreditResult.getBatchNo());
                batchArgs[i] = new MapSqlParameterSource(generateParamMap(bean));
            }
            jdbcTemplate.batchUpdate(addSql, batchArgs);
        } catch (Exception e) {
            throw new BarclaysPersistenceException("batch insert error : " + e.getMessage());
        }
    }

    @Override
    public QueryCreditResult getByBatchNoAndDetailNo(String batchNo, String transactionVoucherNo) throws BarclaysPersistenceException {
        if (StringUtils.isEmpty(batchNo) || StringUtils.isEmpty(transactionVoucherNo)) {
            return null;
        }
        final String getSql = "select comm_code commCode,err_msg errMsg,source_account_no sourceAccountNo," +
                "source_account_name sourceAccountName,destination_account_no destinationAccountNo," +
                "destination_account_name destinationAccountName,transaction_amount transactionAmount," +
                "acceptance_time acceptanceTime,transaction_voucher_no transactionVoucherNo," +
                "channel_sequence_no channelSequenceNo,request_status requestStatus,process_status processStatus," +
                "business_result_msg businessResultMsg,business_success_time businessSuccessTime,batch_no batchNo " +
                "from third_party_query_credit_result where batch_no = :batchNo " +
                "and transaction_voucher_no = :transactionVoucherNo";
        Map<String, Object> paramsMap = new HashMap<>(2);
        paramsMap.put("batchNo", batchNo);
        paramsMap.put("transactionVoucherNo", transactionVoucherNo);

        try {
            List<QueryCreditResult> resultList = jdbcTemplate.queryForList(getSql, paramsMap, QueryCreditResult.class);
            if (CollectionUtils.isEmpty(resultList)) {
                return null;
            }
            QueryCreditResult result = resultList.get(0);
            if (BusinessProcessStatus.SUCCESS.equals(result.getProcessStatus())) {
                return result;
            }
            return null;
        } catch (DataAccessException e) {
            throw new BarclaysPersistenceException("getByBatchNoAndDetailNo error : " + e.getMessage());
        }
    }

    @Override
    public void delByBatchNo(String batchNo) throws BarclaysPersistenceException {
        if (StringUtils.isEmpty(batchNo)) {
            return;
        }
        String delSql = "delete from third_party_query_credit_result where batch_no = :batchNo";
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("batchNo", batchNo);
        try {
            jdbcTemplate.update(delSql, paramMap);
        } catch (DataAccessException e) {
            throw new BarclaysPersistenceException("del by batch no error : " + e.getMessage());
        }
    }

    public QueryCreditResult getByDetailNoAndInList(String transactionVoucherNo, QueryCreditResult queryCreditResult) {
        if (null == queryCreditResult) {
            return new QueryCreditResult(BusinessRequestStatus.NOTRECEIVE);
        }
        List<QueryCreditResult> resultList = queryCreditResult.getQueryCreditResultList();
        if (StringUtils.isEmpty(transactionVoucherNo) || CollectionUtils.isEmpty(resultList)) {
            return new QueryCreditResult(BusinessRequestStatus.NOTRECEIVE);
        }
        for (QueryCreditResult result : resultList) {
            if (transactionVoucherNo.equals(result.getTransactionVoucherNo())) {
                return result;
            }
        }
        return new QueryCreditResult(BusinessRequestStatus.NOTRECEIVE);
    }

    private Map<String, Object> generateParamMap(QueryCreditResult bean) {
        String beanJson = JSON.toJSONStringWithDateFormat(bean,
                "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
        return JsonUtils.parse(beanJson);
    }
}
