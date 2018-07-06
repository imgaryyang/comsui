package com.suidifu.dowjones.dao;

import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import com.suidifu.dowjones.vo.request.QueryParameter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/5 <br>
 * @time: 20:09 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface CashFingerPrinterDAO {
    FingerPrinterParameter[] loadScheduleJob();

    FingerPrinterParameter[] loadScheduleJob(String financialContractUuid);

    Dataset<Row> getCashFingerPrinterOfOneCashFlow(QueryParameter queryParameter);

    Dataset<Row> getCashFingerPrinterOfOneCashFlow(QueryParameter queryParameter, String date);

    QueryParameter getQueryParameters(String financialContractUuid);

    void save(FingerPrinterParameter fingerPrinterParameter);

    Dataset<Row> getCashFingerPrinterOfZeroCashFlow(QueryParameter queryParameter);

    Dataset<Row> getCashFingerPrinterOfZeroCashFlow(QueryParameter queryParameter, String date);

    Dataset<Row> getCashFingerPrinterOfCashFlow(QueryParameter queryParameter, String date);

    Dataset<Row> getCashFingerPrinterFromOrderNO(QueryParameter queryParameter);

    Dataset<Row> getCashFingerPrinterFromOrderNO(QueryParameter queryParameter, String date);

    String[] getOutlierChannelCode(String financialContractUuid);

    Dataset<Row> getCashFingerPrinterFromOutlierChannelCode(QueryParameter queryParameter);

    Dataset<Row> getCashFingerPrinterFromOutlierChannelCode(QueryParameter queryParameter, String date);

    void saveData2TableWithAppendMode(Dataset<Row> result, String table, String createDate, String financialContractUuid);

    Dataset<Row> getAccountInfoFromCashFlow(QueryParameter queryParameter, Object[] fingerPrinters);

    Dataset<Row> getBankCorporateCashFlow(QueryParameter queryParameter, String beginTime, String endTime);
}