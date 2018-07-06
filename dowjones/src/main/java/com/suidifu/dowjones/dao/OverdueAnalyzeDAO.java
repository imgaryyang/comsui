package com.suidifu.dowjones.dao;

import com.suidifu.dowjones.model.QueryConditionValue;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/22 <br>
 * @time: 上午11:14 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface OverdueAnalyzeDAO {
    String[] getRemittanceUnclearLoansBy(StaticOverdueRateInputParameter inputParameter, RepurchaseMode repurchaseMode);

    String[] getOverdueLoanPrincipalByMonthAndOverdueAssetSet(StaticOverdueRateInputParameter inputParameter, RepurchaseMode repurchaseMode);

    String[] getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(StaticOverdueRateInputParameter inputParameter, RepurchaseMode repurchaseMode);

    BigDecimal getUnEarnedPrincipalFromRepurchaseBy(String[] contractUuids, boolean withInterest);

    BigDecimal getUnEarnedPrincipalInterestFromLedgerBookBy(InputParameter inputParameter, String[] contractUuids);

    BigDecimal getUnEarnedPrincipalFromLedgerBookBy(InputParameter inputParameter, String[] contractUuids);

    String[] getUnclearContractUuidsBy(InputParameter inputParameter, RepurchaseMode repurchaseMode);

    String[] getContractUuidsByUnConfirmedAssetSet(InputParameter inputParameter, RepurchaseMode repurchaseMode);

    QueryConditionValue initQueryConditionValue(String contractUuid);

    BigDecimal getRemittanceTotalAmountByMonth(StaticOverdueRateInputParameter inputParameter);

    String[] getContractUuidsByOverdueAssetSet(InputParameter inputParameter, RepurchaseMode repurchaseMode);

    void saveData(StatisticsReport statisticsReport) throws IOException;

    List<StatisticsReport> loadAllStatisticsReport(StatisticsReport statisticsReport);

    Dataset<Row> getContractUuidsByUnConfirmedAssetSet(InputParameter inputParameter);

    Dataset<Row> getUnEarnedPrincipalFromLedgerBookBy(InputParameter inputParameter);
}