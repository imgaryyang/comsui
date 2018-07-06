package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.CashFingerPrinterDAO;
import com.suidifu.dowjones.model.RepaymentExcel;
import com.suidifu.dowjones.service.CashFingerPrinterServiceV2;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.vo.request.QueryParameter;
import com.sun.corba.se.spi.ior.ObjectKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 21:34 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@Service
public class CashFingerPrinterServiceV2Impl implements CashFingerPrinterServiceV2, Serializable {

    @Resource
    private transient CashFingerPrinterDAO cashFingerPrinterDAO;


    @Override
    public void doCashFingerPrinter_DebitCashFlow(String financialContractUuid, Date time) {

        QueryParameter queryParameter = cashFingerPrinterDAO.getQueryParameters(financialContractUuid);

        if (queryParameter == null){
            log.error("financial_contract or account data error {} {}", financialContractUuid, time);
            return;
        }

        String date = DateUtils.getDateFormatYYMMDD(time);

        Dataset<Row> rows0 = cashFingerPrinterDAO.getCashFingerPrinterOfZeroCashFlow(queryParameter, date);
        log.info("rows0-count {}", rows0.count());

        Dataset<Row> cashFingerPrintersOfOneCashFlow = cashFingerPrinterDAO.getCashFingerPrinterOfOneCashFlow(queryParameter, date);
        log.info("cashFingerPrintersOfOneCashFlow-count {}", cashFingerPrintersOfOneCashFlow.count());
        Dataset<Row> cashFingerPrinterOfCashFlows = getCashFingerPrinterOfCashFlow(queryParameter, date);
        log.info("cashFingerPrinterOfCashFlows-count {}", cashFingerPrinterOfCashFlows.count());
        Dataset<Row> result = unionAllAndFillBlank(rows0, cashFingerPrintersOfOneCashFlow, cashFingerPrinterOfCashFlows, date, financialContractUuid);
        log.info("result-count {}", result.count());

//        Object[] fingerPrinters = extractFingerPrinter(result);
        Dataset<Row> accountInfos = cashFingerPrinterDAO.getAccountInfoFromCashFlow(queryParameter, null);
        result = result.join(accountInfos, result.col("fingerPrinter").equalTo(accountInfos.col("join_key")), "inner")
            .drop("join_key");
        result.printSchema();
        result.show();

        cashFingerPrinterDAO.saveData2TableWithAppendMode(result, "debit_cash_flow", date, financialContractUuid);
    }

    @Override
    public void doCashFingerPrinter_RepaymentOrderSummary(String financialContractUuid, Date time) {

        QueryParameter queryParameter = cashFingerPrinterDAO.getQueryParameters(financialContractUuid);

        if (queryParameter == null){
            log.error("financial_contract or account data error {} {}", financialContractUuid, time);
            return;
        }

        String date = DateUtils.getDateFormatYYMMDD(time);

        Dataset<Row> rows1 = cashFingerPrinterDAO.getCashFingerPrinterFromOrderNO(queryParameter, date);
        rows1 = fillBlank(rows1, date, financialContractUuid);
        cashFingerPrinterDAO.saveData2TableWithAppendMode(rows1, "repayment_order_summary", date, financialContractUuid);
    }


    @Override
    public void doCashFingerPrinter_OnlinePaymentDetailsInTransit(String financialContractUuid, Date time) {

        QueryParameter queryParameter = cashFingerPrinterDAO.getQueryParameters(financialContractUuid);

        if (queryParameter == null){
            log.error("financial_contract or account data error {} {}", financialContractUuid, time);
            return;
        }

        String date = DateUtils.getDateFormatYYMMDD(time);

        Dataset<Row> row2 = cashFingerPrinterDAO.getCashFingerPrinterFromOutlierChannelCode(queryParameter, date);
        row2 = fillBlank(row2, date, financialContractUuid);
        cashFingerPrinterDAO.saveData2TableWithAppendMode(row2, "online_payment_details_in_transit", date, financialContractUuid);
    }

    @Override
    public void doCashFingerPrinter_BankCorporateCashFlow(String financialContractUuid, Date time) {
        QueryParameter queryParameter = cashFingerPrinterDAO.getQueryParameters(financialContractUuid);
        String date = DateUtils.getDateFormatYYMMDD(time);
        String endTime = DateUtils.convertNextDate2String(time);
        Dataset<Row> result = cashFingerPrinterDAO.getBankCorporateCashFlow(queryParameter, date, endTime);
        result = fillBlank(result, date, financialContractUuid);
        cashFingerPrinterDAO.saveData2TableWithAppendMode(result, "bank_corporate_cash_flow", date, financialContractUuid);
    }

    private Object[] extractFingerPrinter(Dataset<Row> rows0){
        List<String> fingerPrinterList = new ArrayList<>();
        Iterator<Row> iter = rows0.select("fingerPrinter").toJavaRDD().toLocalIterator();
        while (iter.hasNext()) {
            fingerPrinterList.add(iter.next().getString(0));
        }
        Object[] fingerPrinters = new Object[fingerPrinterList.size()];
        fingerPrinterList.toArray(fingerPrinters);
        return fingerPrinters;
    }

    private Dataset<Row> getCashFingerPrinterOfCashFlow(QueryParameter queryParameter, String date){
        return cashFingerPrinterDAO.getCashFingerPrinterOfCashFlow(queryParameter,date)
            .withColumn("principal",functions.lit(null))
            .withColumn("interest", functions.lit(null))
            .withColumn("loanServiceFee", functions.lit(null))
            .withColumn("loanTechFee", functions.lit(null))
            .withColumn("loanOtherFee", functions.lit(null))
            .withColumn("punishment", functions.lit(null))
            .withColumn("overdueFee", functions.lit(null))
            .withColumn("overdueServiceFee", functions.lit(null))
            .withColumn("overdueOtherFee", functions.lit(null));
    }

    private Dataset<Row> unionAllAndFillBlank(Dataset<Row> rows0, Dataset<Row> cashFingerPrintersOfOneCashFlow,
        Dataset<Row> cashFingerPrinterOfCashFlows, String date, String financialContractUuid) {
        Dataset<Row> result = rows0
            .union(cashFingerPrintersOfOneCashFlow)
            .union(cashFingerPrinterOfCashFlows);

        result =  result.groupBy("fingerPrinter").agg(
            functions.sum("principal").as("principal"),
            functions.sum("interest").as("interest"),
            functions.sum("loanServiceFee").as("loanServiceFee"),
            functions.sum("loanTechFee").as("loanTechFee"),
            functions.sum("loanOtherFee").as("loanOtherFee"),
            functions.sum("punishment").as("punishment"),
            functions.sum("overdueFee").as("overdueFee"),
            functions.sum("overdueServiceFee").as("overdueServiceFee"),
            functions.sum("overdueOtherFee").as("overdueOtherFee"));

        return fillBlank(result, date, financialContractUuid);
    }

    private Dataset<Row> fillBlank(Dataset<Row> rows, String date, String financialContractUuid){
        return rows.withColumn("create_date", functions.lit(date))
            .withColumn("financial_contract_uuid", functions.lit(financialContractUuid));
    }




}