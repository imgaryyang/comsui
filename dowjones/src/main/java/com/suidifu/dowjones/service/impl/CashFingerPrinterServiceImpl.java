package com.suidifu.dowjones.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.dowjones.dao.CashFingerPrinterDAO;
import com.suidifu.dowjones.service.CashFingerPrinterService;
import com.suidifu.dowjones.utils.*;
import com.suidifu.dowjones.vo.request.FileParameter;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import com.suidifu.dowjones.vo.request.QueryParameter;
import com.suidifu.dowjones.vo.response.CashFingerPrinter;
import com.suidifu.dowjones.vo.response.CashFingerPrinterOfCashFlow;
import com.suidifu.dowjones.vo.response.CashFingerPrinterOfOrderNO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 21:34 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@Service
public class CashFingerPrinterServiceImpl implements CashFingerPrinterService, Serializable {
    @Resource
    private transient CashFingerPrinterDAO cashFingerPrinterDAO;
    @Resource
    private transient HTTPCallBack httpCallBack;
    @Resource
    private transient FileUtils fileUtils;
    @Resource
    private transient FTPUtils ftpUtils;

    public void save(FingerPrinterParameter fingerPrinterParameter) {
        cashFingerPrinterDAO.save(fingerPrinterParameter);
    }

    public FingerPrinterParameter[] loadScheduleJob() {
        return cashFingerPrinterDAO.loadScheduleJob();
    }

    public FingerPrinterParameter[] loadScheduleJob(String financialContractUuid) {
        return cashFingerPrinterDAO.loadScheduleJob(financialContractUuid);
    }

    public String post(List<String> fileNameList, String dataStreamUuid) throws JsonProcessingException {
        return httpCallBack.post(fileNameList, dataStreamUuid);
    }

    public List<String> operateFile(FingerPrinterParameter fingerPrinterParameter) throws IOException {
        String uploadPath = Constants.FTP_PATH + fingerPrinterParameter.getDataStreamUuid() + Constants.SLASH;
        QueryParameter queryParameter = cashFingerPrinterDAO.getQueryParameters(fingerPrinterParameter.getFinancialContractUuid());

        List<String> headers = new ArrayList<>();
        List content = new ArrayList();
        if ("0".equals(fingerPrinterParameter.getTaskId())) { // 借方流水管理表
            Dataset<Row> rows = cashFingerPrinterDAO.
                    getCashFingerPrinterOfZeroCashFlow(queryParameter);
            headers = getCashFingerPrinterColumns(rows);
            content = getCashFingerPrinter(queryParameter, rows);
            log.info("result size is:{},financialContractUuid is:{}", content.size(), queryParameter.getFinancialContractUuid());
        }

        if ("1".equals(fingerPrinterParameter.getTaskId())) { // 还款订单
            Dataset<Row> rows = cashFingerPrinterDAO.
                    getCashFingerPrinterFromOrderNO(queryParameter);
            headers = getCashFingerPrinterFromOrderNOColumns(rows);
            content = rows.as(Encoders.bean(CashFingerPrinterOfOrderNO.class)).
                    collectAsList();
            log.info("result size is:{},financialContractUuid is:{}", content.size(), queryParameter.getFinancialContractUuid());
        }

        if ("2".equals(fingerPrinterParameter.getTaskId())) { // 在途资金
            Dataset<Row> rows = cashFingerPrinterDAO.
                    getCashFingerPrinterFromOutlierChannelCode(queryParameter);
            headers = getCashFingerPrinterColumns(rows);
            content = rows.as(Encoders.bean(CashFingerPrinter.class)).collectAsList();
            log.info("result size is:{},financialContractUuid is:{}", content.size(), queryParameter.getFinancialContractUuid());
        }

        log.info("\n\n\n\n\ntaskID is:{},content size is:{}\n\n\n\n\n",
                fingerPrinterParameter.getTaskId(), content.size());
        String path = saveFile(headers, content, fingerPrinterParameter.getPath());

        ftpUtils.uploadFile(new File(path), uploadPath);

        List<String> filePaths = new ArrayList<>(ftpUtils.listFiles(uploadPath, Constants.CSV));
        for (String fileName : filePaths) {
            log.info("fileName is:{}", fileName);
        }

        return filePaths;
    }

    public List<String> operateFile(FingerPrinterParameter fingerPrinterParameter, String date) throws IOException {
        String uploadPath = Constants.FTP_PATH + fingerPrinterParameter.getDataStreamUuid() + Constants.SLASH;
        QueryParameter queryParameter = cashFingerPrinterDAO.getQueryParameters(fingerPrinterParameter.getFinancialContractUuid());

        List<String> headers = new ArrayList<>();
        List content = new ArrayList();
        if ("0".equals(fingerPrinterParameter.getTaskId())) {
            Dataset<Row> rows = cashFingerPrinterDAO.
                    getCashFingerPrinterOfZeroCashFlow(queryParameter, date);
            headers = getCashFingerPrinterColumns(rows);
            content = getCashFingerPrinter(queryParameter, rows, date);
            log.info("result size is:{},financialContractUuid is:{}", content.size(), queryParameter.getFinancialContractUuid());
        }

        if ("1".equals(fingerPrinterParameter.getTaskId())) {
            Dataset<Row> rows = cashFingerPrinterDAO.getCashFingerPrinterFromOrderNO(queryParameter, date);
            headers = getCashFingerPrinterFromOrderNOColumns(rows);
            content = rows.as(Encoders.bean(CashFingerPrinterOfOrderNO.class)).
                    collectAsList();
            log.info("result size is:{},financialContractUuid is:{}", content.size(), queryParameter.getFinancialContractUuid());
        }

        if ("2".equals(fingerPrinterParameter.getTaskId())) {
            Dataset<Row> rows = cashFingerPrinterDAO.getCashFingerPrinterFromOutlierChannelCode(queryParameter, date);
            headers = getCashFingerPrinterColumns(rows);
            content = rows.as(Encoders.bean(CashFingerPrinter.class)).collectAsList();
            log.info("result size is:{},financialContractUuid is:{}", content.size(), queryParameter.getFinancialContractUuid());
        }

        log.info("\n\n\n\n\ntaskID is:{},content size is:{}\n\n\n\n\n",
                fingerPrinterParameter.getTaskId(), content.size());
        String path = saveFile(headers, content, fingerPrinterParameter.getPath(), date);

        ftpUtils.uploadFile(new File(path), uploadPath);

        List<String> filePaths = new ArrayList<>(ftpUtils.listFiles(uploadPath, Constants.CSV));
        for (String fileName : filePaths) {
            log.info("fileName is:{}", fileName);
        }

        return filePaths;
    }

    private List<CashFingerPrinter> getCashFingerPrinter(QueryParameter queryParameter, Dataset<Row> rows) {
        List<CashFingerPrinter> result = new ArrayList<>();

        //1.1
        List<CashFingerPrinter> cashFingerPrinters = rows.
                as(Encoders.bean(CashFingerPrinter.class)).
                collectAsList();

        //1.2
        List<CashFingerPrinter> cashFingerPrintersOfOneCashFlow = cashFingerPrinterDAO.
                getCashFingerPrinterOfOneCashFlow(queryParameter).
                as(Encoders.bean(CashFingerPrinter.class)).
                collectAsList();

        //1.3
        List<CashFingerPrinterOfCashFlow> cashFingerPrinterOfCashFlows = cashFingerPrinterDAO.
                getCashFingerPrinterOfCashFlow(queryParameter, DateUtils.getYesterdayFormatYYMMDD()).
                as(Encoders.bean(CashFingerPrinterOfCashFlow.class)).
                collectAsList();

        copyBean(result, cashFingerPrinterOfCashFlows);

        result.addAll(cashFingerPrinters);
        result.addAll(cashFingerPrintersOfOneCashFlow);

        log.info("result size is:{}", result.size());

        return result;
    }

    private void copyBean(List<CashFingerPrinter> result, List<CashFingerPrinterOfCashFlow> cashFingerPrinterOfCashFlows) {
        for (CashFingerPrinterOfCashFlow cashFingerPrinterOfCashFlow : cashFingerPrinterOfCashFlows) {
            MapperFactory factory = CopyUtils.init();

            factory.registerClassMap(factory.classMap(CashFingerPrinterOfCashFlow.class, CashFingerPrinter.class)
                    //设置正向空值不复制，反向空值不复制
                    .mapNulls(false).mapNullsInReverse(false)
                    .byDefault().toClassMap());

            MapperFacade mapper = factory.getMapperFacade();
            result.add(mapper.map(cashFingerPrinterOfCashFlow, CashFingerPrinter.class));
        }
    }

    private List<CashFingerPrinter> getCashFingerPrinter(QueryParameter queryParameter, Dataset<Row> rows, String date) {
        List<CashFingerPrinter> result = new ArrayList<>();

        //1.1
        List<CashFingerPrinter> cashFingerPrinters = rows.
                as(Encoders.bean(CashFingerPrinter.class)).
                collectAsList();

        //1.2
        List<CashFingerPrinter> cashFingerPrintersOfOneCashFlow = cashFingerPrinterDAO.
                getCashFingerPrinterOfOneCashFlow(queryParameter, date).
                as(Encoders.bean(CashFingerPrinter.class)).
                collectAsList();

        //1.3
        List<CashFingerPrinterOfCashFlow> cashFingerPrinterOfCashFlows = cashFingerPrinterDAO.
                getCashFingerPrinterOfCashFlow(queryParameter, DateUtils.getYesterdayFormatYYMMDD()).
                as(Encoders.bean(CashFingerPrinterOfCashFlow.class)).
                collectAsList();

        copyBean(result, cashFingerPrinterOfCashFlows);

        result.addAll(cashFingerPrinters);
        result.addAll(cashFingerPrintersOfOneCashFlow);

        log.info("result size is:{},financialContractUuid is:{}", result.size(), queryParameter.getFinancialContractUuid());
        return result;
    }

    private String saveFile(List<String> headers, List content, String filePath) throws IOException {
        FileParameter fileParameter = new FileParameter();
        fileParameter.setFilePath(filePath);
        fileParameter.setFileName(DateUtils.getFileDate(new Date()) + "." + Constants.CSV);

        log.info("\n\n\n\n\nfilePath is:{},fileName is:{}\n\n\n\n\n",
                filePath, fileParameter.getFileName());
        return fileUtils.save(headers, content, fileParameter);
    }

    private String saveFile(List<String> headers, List content, String filePath, String date) throws IOException {
        FileParameter fileParameter = new FileParameter();
        fileParameter.setFilePath(filePath);
        fileParameter.setFileName(DateUtils.getFileDate(date) + "." + Constants.CSV);

        log.info("\n\n\n\n\nfilePath is:{},fileName is:{}\n\n\n\n\n",
                filePath, fileParameter.getFileName());
        return fileUtils.save(headers, content, fileParameter);
    }

    private List<String> getCashFingerPrinterColumns(Dataset<Row> rows) {
        return Arrays.asList(rows.withColumnRenamed("bankCard", "银行卡").
                withColumnRenamed("fingerPrinter", "指纹").
                withColumnRenamed("orderNo", "订单号").
                withColumnRenamed("totalAmount", "总额").
                withColumnRenamed("principal", "本金").
                withColumnRenamed("interest", "利息").
                withColumnRenamed("loanServiceFee", "贷款服务费").
                withColumnRenamed("loanTechFee", "贷款技术费").
                withColumnRenamed("loanOtherFee", "贷款其他费用").
                withColumnRenamed("punishment", "罚息").
                withColumnRenamed("overdueFee", "逾期违约金").
                withColumnRenamed("overdueServiceFee", "逾期服务费").
                withColumnRenamed("overdueOtherFee", "逾期其他费用").
                columns());
    }

    private List<String> getCashFingerPrinterFromOrderNOColumns(Dataset<Row> rows) {
        return Arrays.asList(rows.withColumnRenamed("orderNo", "订单号").
                withColumnRenamed("totalAmount", "总额").
                withColumnRenamed("principal", "本金").
                withColumnRenamed("interest", "利息").
                withColumnRenamed("loanServiceFee", "贷款服务费").
                withColumnRenamed("loanTechFee", "贷款技术费").
                withColumnRenamed("loanOtherFee", "贷款其他费用").
                withColumnRenamed("punishment", "罚息").
                withColumnRenamed("overdueFee", "逾期违约金").
                withColumnRenamed("overdueServiceFee", "逾期服务费").
                withColumnRenamed("overdueOtherFee", "逾期其他费用").
                columns());
    }
}