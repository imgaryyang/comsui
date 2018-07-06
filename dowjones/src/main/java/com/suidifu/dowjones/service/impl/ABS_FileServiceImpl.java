package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.config.PathConfig;
import com.suidifu.dowjones.dao.ABS_RepaymentAndContractDAO;
import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.model.ABS_ContractChangeExcel;
import com.suidifu.dowjones.model.ABS_CurrentContractDetailExcel;
import com.suidifu.dowjones.model.ABS_RepaymentExcel;
import com.suidifu.dowjones.service.ABS_FileService;
import com.suidifu.dowjones.utils.*;
import com.suidifu.dowjones.vo.enumeration.AuditOverdueStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.datanucleus.store.types.backed.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.sum;

/**
 * Created by zxj on 2018/2/7.
 */
@Service("ABS_FileService")
@Slf4j
public class ABS_FileServiceImpl extends BaseDAO implements ABS_FileService {
    @Autowired
    private ABS_RepaymentAndContractDAO repaymentAndContractDAO;
    @Autowired
    private FTPUtils ftpUtils;
    @Resource
    private PathConfig pathConfig;

    @Override
    public void doActualRepaymentFileData(Date time) {
        long startTime = System.currentTimeMillis();
        Date start = DateUtils.substractDays(DateUtils.getDateFrom(DateUtils.getDateFormatYYMMDD(time) + " 23:00:00", "yyyy-MM-dd HH:mm:ss"), 2);
        Date end = DateUtils.substractDays(DateUtils.getDateFrom(DateUtils.getDateFormatYYMMDD(time) + " 23:00:00", "yyyy-MM-dd HH:mm:ss"), 1);
        log.info("Start doing actual repayment file data for [:{}, :{}], time is :{}", DateUtils.getDateFormatYYMMDD(start), DateUtils.getDateFormatYYMMDD(end), startTime);

        String startDate = DateUtils.getDateFormatYYMMDDHHMMSS(start);
        String endDate = DateUtils.getDateFormatYYMMDDHHMMSS(end);

        //还款数据
        List<ABS_RepaymentExcel> repaymentExcels = new ArrayList<>();

        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            log.info("Error params for [:{}, :{}], time is :{}", startDate, endDate, startTime);
            String emptyFullPath = saveActualRepaymentResult2File(time , repaymentExcels);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        //获取还款信息
        Dataset<Row> tencentAbsTrailData = repaymentAndContractDAO.getTencentAbsTrailData(start, end, 1);

        if (tencentAbsTrailData == null || tencentAbsTrailData.count() <= 0) {
            log.info("Error tencentAbsTrailData is empty");
            String emptyFullPath = saveActualRepaymentResult2File(time , repaymentExcels);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }

        Set<Object> contractUuidList = new HashSet<>();
        Set<Object> assetUuidList = new HashSet<>();
        Set<Object> financialContractUuidList = new HashSet<>();

        for (Row row : tencentAbsTrailData.collectAsList()) {
            contractUuidList.add(row.getAs("contract_uuid"));;
            assetUuidList.add(row.getAs("asset_set_uuid"));
            financialContractUuidList.add(row.getAs("financial_contract_uuid"));
        }
        //获取贷款合同
        Dataset<Row> contractDataset = repaymentAndContractDAO.getContractDataset(contractUuidList);
        //获取信托合同
        Dataset<Row> financialContractDataset = repaymentAndContractDAO.getFinancialContractDataset(financialContractUuidList);
        //获取还款计划
        Dataset<Row> assetsetDataset = repaymentAndContractDAO.getAssetDatasetByAssetUuid(assetUuidList);
        //获取应收费用七项之和
        Dataset<Row> assetcDataset = repaymentAndContractDAO.getAssetSetExtraCharge(assetUuidList);
        //获取实收本金、利息、七项费用之和
        Dataset<Row> paidOffDs = repaymentAndContractDAO.getPaidOffDs(assetUuidList);

        if (contractDataset == null) {
            log.info("Error contractDataset is null");
            String emptyFullPath = saveActualRepaymentResult2File(time , repaymentExcels);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        for (Row contract : contractDataset.collectAsList()) {
            if (assetsetDataset.collectAsList().size() <= 0) return;
            Dataset<Row> currentAssetDataset = assetsetDataset.filter(assetsetDataset.col("contract_uuid").equalTo(contract.getAs("uuid")));
            if (currentAssetDataset.collectAsList().size() <= 0) return;
            //应收费用七项之和
            currentAssetDataset = currentAssetDataset.join(assetcDataset, currentAssetDataset.col("asset_uuid").equalTo(assetcDataset.col("asset_set_uuid")), "left_outer").drop("asset_set_uuid");
            //实收本金、利息、七项费用之和
            currentAssetDataset = currentAssetDataset.join(paidOffDs, currentAssetDataset.col("asset_uuid").equalTo(paidOffDs.col("related_lv_1_asset_uuid")), "left_outer");

            Row financialContract = financialContractDataset.filter(col("financial_contract_uuid").equalTo(contract.getAs("financial_contract_uuid"))).first();
            for (Row row: currentAssetDataset.collectAsList()) {
                ABS_RepaymentExcel repaymentExcel = new ABS_RepaymentExcel();
                repaymentExcel.setProjectId("pl00451");
                repaymentExcel.setAgencyId("10073");
                repaymentExcel.setUniqueId(fetchFieldValue(contract,"unique_id"));
                repaymentExcel.setCurrentPeriod(fetchFieldValue(row,"current_period"));
                repaymentExcel.setAssetRecycleDate(fetchFieldValue(row,"asset_recycle_date"));
                repaymentExcel.setAssetPrincipalValue(fetchFieldValue(row,"asset_principal_value"));
                repaymentExcel.setAssetInterestValue(fetchFieldValue(row,"asset_interest_value"));
                String chargeAmount = fetchFieldValue(row, "charge_amount");
                repaymentExcel.setAssetChangeAmount(chargeAmount == null ? "0.00" : chargeAmount);
                repaymentExcel.setLoanAssetPrincipal(fetchFieldValue(row,"loan_asset_principal"));
                repaymentExcel.setLoanAssetInterest(fetchFieldValue(row,"loan_asset_interest"));
                repaymentExcel.setLoanAssetAmount(fetchFieldValue(row,"loan_charge_amount"));
                repaymentExcel.setActualRecycleDate(DateUtils.transferYYYYMMDDHHmmss2YYYYMMDD(fetchFieldValue(row,"actual_recycle_date")));
                repaymentExcel.setCurrentPeriodBalance(null);

                String status = "ZHENGCHANG";
                Date actualRecycleDate = DateUtils.getDateFrom(fetchFieldValue(row,"actual_recycle_date"), "yyyy-MM-dd HH:mm:ss");
                Date assetRecycleDate = DateUtils.getDateFrom(fetchFieldValue(row,"asset_recycle_date"), "yyyy-MM-dd");
                if (fetchFieldValue(row,"plan_type") != null && Objects.equals(fetchFieldValue(row, "plan_type"), "1")) {
                    status = "ZAOCHANG";
                } else if (actualRecycleDate != null && assetRecycleDate != null) {
                    if (actualRecycleDate.compareTo(assetRecycleDate) < 0) {
                        status = "ZAOCHANG";
                    } else if (actualRecycleDate.compareTo(DateUtils.addDays(assetRecycleDate, Integer.parseInt(financialContract.getAs("loan_overdue_start_day") + ""))) > 0) {
                        status = "YUQI";
                    }
                }
                repaymentExcel.setCurrentPeriodStatus(status);

                repaymentExcels.add(repaymentExcel);
            }
        }

        log.info("result data set row size :{}", repaymentExcels.size());

        //保存还款数据 以及导出到文件
        long saveDataStartTime = System.currentTimeMillis();

        log.info("Start save repayment excels data file  time is :{}", saveDataStartTime);

        String fullPath = saveActualRepaymentResult2File(time , repaymentExcels);

        ftpUtils.uploadFileToTencent(new File(fullPath), time);
        log.info("Save repayment excels data file path is :{}", fullPath);

        long saveDataEndTime = System.currentTimeMillis();
        log.info("End save repayment excels file data end, time used :{}ms", saveDataEndTime - saveDataStartTime);

        log.info("Total time Used :{}", saveDataEndTime - startTime);
        log.info("End doing actual repayment file data for [:{}, :{}], time is :{}", DateUtils.getDateFormatYYMMDD(start), DateUtils.getDateFormatYYMMDD(end), saveDataEndTime);
    }

    @Override
    public void doContractChangeFileData(Date time) {
        long startTime = System.currentTimeMillis();
        Date start = DateUtils.substractDays(DateUtils.getDateFrom(DateUtils.getDateFormatYYMMDD(time) + " 23:00:00", "yyyy-MM-dd HH:mm:ss"), 2);
        Date end = DateUtils.substractDays(DateUtils.getDateFrom(DateUtils.getDateFormatYYMMDD(time) + " 23:00:00", "yyyy-MM-dd HH:mm:ss"), 1);
        log.info("Start doing contract change file data for [:{}, :{}], time is :{}", DateUtils.getDateFormatYYMMDD(start), DateUtils.getDateFormatYYMMDD(end), startTime);

        String startDate = DateUtils.getDateFormatYYMMDDHHMMSS(start);
        String endDate = DateUtils.getDateFormatYYMMDDHHMMSS(end);

        List<ABS_ContractChangeExcel> contractChangeExcels = new ArrayList<>();

        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            log.info("Error params for [:{}, :{}], time is :{}", startDate, endDate, startTime);
            String emptyFullPath = saveContractChangeResult2File(time , contractChangeExcels);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        //获取贷款合同变更信息
        Dataset<Row> tencentAbsTrailData = repaymentAndContractDAO.getTencentAbsTrailData(start, end, 0);

        if (tencentAbsTrailData == null || tencentAbsTrailData.count() <= 0) {
            log.info("Error tencentAbsTrailData is empty");
            String emptyFullPath = saveContractChangeResult2File(time , contractChangeExcels);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        Set<Object> contractUuidList = new HashSet<>();
        Set<Object> assetUuidList = new HashSet<>();

        for (Row row : tencentAbsTrailData.collectAsList()) {
            contractUuidList.add(row.getAs("contract_uuid"));
            assetUuidList.add(row.getAs("asset_set_uuid"));
        }
        //获取贷款合同
        Dataset<Row> contractDataset = repaymentAndContractDAO.getContractDataset(contractUuidList);
        if (contractDataset == null || contractDataset.count() <= 0) {
            log.info("Error contractDataset is empty");
            String emptyFullPath = saveContractChangeResult2File(time , contractChangeExcels);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        //获取还款计划
        Dataset<Row> assetsetDataset = repaymentAndContractDAO.getAssetDatasetByAssetUuid(assetUuidList);

        //获取客户uuid list
        List<Object> customerUuidList = new ArrayList<>();
        for (Row row : contractDataset.collectAsList()) {
            customerUuidList.add(row.getAs("customer_uuid"));
        }
        //获取客户信息
        Dataset<Row> customerDataset = repaymentAndContractDAO.getCustomerDataset(customerUuidList);

        Dataset<Row> resultDataset = contractDataset.join(customerDataset, contractDataset.col("customer_uuid").equalTo(customerDataset.col("customer_uuid")), "left_outer");

        for (Row row : resultDataset.collectAsList()) {
            ABS_ContractChangeExcel excel = new ABS_ContractChangeExcel();
            excel.setProjectId("pl00451");
            excel.setAgencyId("10073");
            excel.setUniqueId(fetchFieldValue(row,"unique_id"));
            excel.setCustomerName(fetchFieldValue(row,"customer_name"));
            excel.setIdCard(fetchFieldValue(row,"customer_account"));
            excel.setMobile(fetchFieldValue(row,"customer_mobile"));
            excel.setIsChangeRecycleDate("NO");
            excel.setIsPrepayment("NO");
            excel.setIsOtherFeeChange("YES");
            Row absTrail = tencentAbsTrailData.filter(tencentAbsTrailData.col("contract_uuid").equalTo(fetchFieldValue(row,"uuid"))).sort(col("last_modified_time")).first();
            excel.setOtherFeeChangeOfApplication(DateUtils.getDateFormatYYMMDD((absTrail.getAs("last_modified_time"))));
            //当前所有还款计划
            Dataset<Row> curAssetecData = assetsetDataset.filter(assetsetDataset.col("contract_uuid").equalTo(row.getAs("uuid")));
            // 有效的还款计划
            Dataset<Row> validAssetSets = curAssetecData.filter(curAssetecData.col("active_status").equalTo(0));
            //提前还款的还款计划
            Dataset<Row> prepaymentAssetSets = validAssetSets.filter(validAssetSets.col("plan_type").equalTo("1"));
            if (prepaymentAssetSets != null && prepaymentAssetSets.count() > 0) {//提前还款
                excel.setIsPrepayment("YES");
                Row first = prepaymentAssetSets.first();
                excel.setPrepaymentDate(fetchFieldValue(first,"asset_recycle_date"));
                excel.setPrepaymentDateOfApplication(DateUtils.getDateFormatYYMMDD(first.getAs("asset_create_time")));
                excel.setOtherFeeChangeOfApplication(DateUtils.getDateFormatYYMMDD(first.getAs("asset_create_time")));
            }
            contractChangeExcels.add(excel);
        }

        log.info("result data set row size :{}", contractChangeExcels.size());

        //保存贷款合同数据 以及导出到文件
        long saveDataStartTime = System.currentTimeMillis();

        log.info("Start save contract change excels data file  time is :{}", saveDataStartTime);

        String fullPath = saveContractChangeResult2File(time , contractChangeExcels);

        ftpUtils.uploadFileToTencent(new File(fullPath), time);
        log.info("Save contract change excels data file path is :{}", fullPath);

        long saveDataEndTime = System.currentTimeMillis();
        log.info("End save contract change excels file data end, time used :{}ms", saveDataEndTime - saveDataStartTime);

        log.info("Total time Used :{}", saveDataEndTime - startTime);
        log.info("End doing contract change file for [:{}, :{}], time is :{}", DateUtils.getDateFormatYYMMDD(start), DateUtils.getDateFormatYYMMDD(end), saveDataEndTime);

    }

    @Override
    public void doCurrentContractDetailFileData(Date time) {
        long startTime = System.currentTimeMillis();
        Date start = DateUtils.substractDays(DateUtils.getDateFrom(DateUtils.getDateFormatYYMMDD(time) + " 23:00:00", "yyyy-MM-dd HH:mm:ss"), 2);
        Date end = DateUtils.substractDays(DateUtils.getDateFrom(DateUtils.getDateFormatYYMMDD(time) + " 23:00:00", "yyyy-MM-dd HH:mm:ss"), 1);
        log.info("Start doing current contract detail file data for [:{}, :{}], time is :{}", DateUtils.getDateFormatYYMMDD(start), DateUtils.getDateFormatYYMMDD(end), startTime);

        String startDate = DateUtils.getDateFormatYYMMDDHHMMSS(start);
        String endDate = DateUtils.getDateFormatYYMMDDHHMMSS(end);

        List<ABS_CurrentContractDetailExcel> excelList = new ArrayList<>();

        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            log.info("Error params for [:{}, :{}], time is :{}", startDate, endDate, startTime);
            String emptyFullPath = saveCurrentContractDetailResult2File(time , excelList);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        //获取还款信息
        Dataset<Row> tencentAbsTrailData = repaymentAndContractDAO.getTencentAbsTrailData(start, end, 1);

        if (tencentAbsTrailData == null || tencentAbsTrailData.count() <= 0) {
            log.info("Error tencentAbsTrailData is empty");
            String emptyFullPath = saveCurrentContractDetailResult2File(time , excelList);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        Set<Object> contractUuidList = new HashSet<>();
        Set<Object> financialContractUuidList = new HashSet<>();

        for (Row row : tencentAbsTrailData.collectAsList()) {
            contractUuidList.add(row.getAs("contract_uuid"));
            financialContractUuidList.add(row.getAs("financial_contract_uuid"));
        }
        //获取发生还款的贷款合同
        Dataset<Row> contractDataset = repaymentAndContractDAO.getContractDataset(contractUuidList);
        if (contractDataset == null || contractDataset.count() <= 0) {
            log.info("Error contractDataset is empty");
            String emptyFullPath = saveCurrentContractDetailResult2File(time , excelList);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }
        //获取信托合同
        Dataset<Row> financialContractDataset = repaymentAndContractDAO.getFinancialContractDataset(financialContractUuidList);
        //获取客户uuid list
        List<Object> customerUuidList = new ArrayList<>();
        for (Row row : contractDataset.collectAsList()) {
            customerUuidList.add(row.getAs("customer_uuid"));
        }
        //获取客户信息
        Dataset<Row> customerDataset = repaymentAndContractDAO.getCustomerDataset(customerUuidList);
        //获取所有的还款计划
        Dataset<Row> assetsetDataset = repaymentAndContractDAO.getAssetDatasetByContractUuid(contractUuidList);

        if (assetsetDataset == null || assetsetDataset.count() <= 0) {
            log.info("Error assetsetDataset is null");
            String emptyFullPath = saveCurrentContractDetailResult2File(time , excelList);
            ftpUtils.uploadFileToTencent(new File(emptyFullPath), time);
            return;
        }

        //获取应收未收信息
        Dataset<Row> remainingAmountDataset = repaymentAndContractDAO.getRemainingAmountDataset(contractUuidList);

        Dataset<Row> resultDataset = contractDataset.join(customerDataset, contractDataset.col("customer_uuid").equalTo(customerDataset.col("customer_uuid")), "left_outer");

        resultDataset = resultDataset.join(remainingAmountDataset, resultDataset.col("uuid").equalTo(remainingAmountDataset.col("contract_uuid")), "left_outer");

        for (Row row : resultDataset.collectAsList()) {
            String interestRate = "";
            String rateCycle = fetchFieldValue(row,"interest_rate_cycle");
            String rate = fetchFieldValue(row,"interest_rate");
            if (rate != null) {
                if (Objects.equals(rateCycle, "2")) { //日
                    interestRate = String.valueOf(new BigDecimal(rate).multiply(new BigDecimal(365)).multiply(new BigDecimal(100)).stripTrailingZeros());
                } else if (Objects.equals(rateCycle, "1")) { //月
                    interestRate = String.valueOf(new BigDecimal(rate).multiply(new BigDecimal(12)).multiply(new BigDecimal(100)).stripTrailingZeros());
                } else { //年
                    interestRate = String.valueOf(new BigDecimal(rate).multiply(new BigDecimal(100)).stripTrailingZeros());
                }
            }
            //当前所有还款计划
            Dataset<Row> curAssetecData = assetsetDataset.filter(assetsetDataset.col("contract_uuid").equalTo(row.getAs("uuid")));

            // 第一版本版本号
            Object firstVersion  = curAssetecData.sort("asset_create_time").first().getAs("version_no");
            Dataset<Row> allFirstVersions = curAssetecData.filter(curAssetecData.col("version_no").equalTo(firstVersion));

            // 有效的还款计划
            Dataset<Row> validAssetSets = curAssetecData.filter(curAssetecData.col("active_status").equalTo(0));
            // 未结清的还款计划
            Dataset<Row> unclearAssetSets = validAssetSets.filter(validAssetSets.col("asset_status").equalTo(0));
            // 未结清的期数
            Long unclearPeriod = unclearAssetSets.count();
            // 未结清的第一期还款计划的应还日
            Object lastAssetRecycleDate = null;
            // 有未结清
            if (unclearAssetSets.count() != 0){
                // 未结清的第一期还款计划的应还日
                lastAssetRecycleDate = unclearAssetSets.sort("current_period").first().getAs("asset_recycle_date");
            }
            // 已还期数
            long clearPeriod = validAssetSets.filter(validAssetSets.col("asset_status").equalTo(1)).count();
            //当前信托合同
            Row financialContract = financialContractDataset.filter(financialContractDataset.col("financial_contract_uuid").equalTo(row.getAs("financial_contract_uuid"))).first();
            //宽限日
            int loanOverdueStartDay = Integer.parseInt(financialContract.getAs("loan_overdue_start_day") + "");
            //历史单次最长逾期天数
            int maxOverdueDays = 0;
            //逾期日期集合（去重复）
            Set<String> overdueDateList = new HashSet<>();
            for (Row assetset: curAssetecData.collectAsList()) {
                Date overdueBeginDate = time; //逾期开始日
                Date overdueEndDate = time; //逾期结束日
                int overdueDays = 0; //当前逾期天数
                if (Objects.equals(fetchFieldValue(assetset, "asset_status"), "1")) { //结清
                    overdueEndDate = assetset.getAs("actual_recycle_date");
                }
                if (Objects.equals(fetchFieldValue(assetset, "overdue_status"), "0"))  continue; //逾期状态 正常

                if (Objects.equals(fetchFieldValue(assetset, "overdue_status"), "1")) { //逾期状态 待确认
                    Date assetRecycleDate = assetset.getAs("asset_recycle_date");
                    overdueBeginDate = DateUtils.addDays(assetRecycleDate, loanOverdueStartDay);
                }

                if (Objects.equals(fetchFieldValue(assetset, "overdue_status"), "2")) {//逾期状态 已逾期
                    overdueBeginDate = assetset.getAs("overdue_date");
                }

                for (Date i = overdueBeginDate; !i.after(overdueEndDate); i = DateUtils.addDays(i, 1)) {
                    overdueDays += 1;
                    overdueDateList.add(DateUtils.getDateFormatYYMMDD(i));
                }
                maxOverdueDays = Math.max(overdueDays, maxOverdueDays);
            }

            ABS_CurrentContractDetailExcel excel = new ABS_CurrentContractDetailExcel();
            excel.setProjectId("pl00451");
            excel.setAgencyId("10073");
            excel.setUniqueId(fetchFieldValue(row,"unique_id"));
            excel.setCustomerName(fetchFieldValue(row,"customer_name"));
            excel.setIdCard(fetchFieldValue(row,"customer_account"));
            excel.setMobile(fetchFieldValue(row,"customer_mobile"));
            excel.setTotalAmount(fetchFieldValue(row,"total_amount"));
            excel.setInterestRate(interestRate);
            excel.setPeriods("" + allFirstVersions.count());
            excel.setLoanPeriods(unclearPeriod == 0 ? "" + allFirstVersions.count() : clearPeriod + "");
            excel.setLastPeriods(unclearPeriod + "");
            excel.setLastPrincipalValue(fetchFieldValue(row,"remaining_principal"));
            excel.setLastInterestValue(fetchFieldValue(row,"remaining_interest"));
            excel.setLastOtherFee(fetchFieldValue(row,"remaining_fee"));
            excel.setNestPaymentDate(lastAssetRecycleDate == null ? null : lastAssetRecycleDate + "");
            excel.setIsChangeAssetset("NO");
            excel.setBeginDate(fetchFieldValue(row,"begin_date"));
            excel.setEndDate(fetchFieldValue(row,"end_date"));
            excel.setCurrentOverdueDays(overdueDateList.size() + "");
            excel.setMaxOverdueDays(maxOverdueDays + "");

            excelList.add(excel);
        }

        log.info("result excel list size :{}", excelList.size());

        //保存贷款合同未尝数据 以及导出到文件
        long saveDataStartTime = System.currentTimeMillis();

        log.info("Start save current contract detail excels data file  time is :{}", saveDataStartTime);

        String fullPath = saveCurrentContractDetailResult2File(time , excelList);

        ftpUtils.uploadFileToTencent(new File(fullPath), time);

        log.info("Save current contract detail excels data file path is :{}", fullPath);

        long saveDataEndTime = System.currentTimeMillis();
        log.info("End save  current contract detail excels file data end, time used :{}ms", saveDataEndTime - saveDataStartTime);

        log.info("Total time Used :{}", saveDataEndTime - startTime);
        log.info("End doing  current contract detail file for [:{}, :{}], time is :{}", DateUtils.getDateFormatYYMMDD(start), DateUtils.getDateFormatYYMMDD(end), saveDataEndTime);
    }

    private String saveActualRepaymentResult2File(Date time, List<ABS_RepaymentExcel> excels) {
        String fileName = "PU_YNXT_XF_INSTALLMENT_TX_" + DateUtils.getDateFormatYYMMDD(time).replace("-", "") + ".csv";
        if (excels.isEmpty()) {
            log.info("Excels is empty");
        }
        ExcelUtil<ABS_RepaymentExcel> excel = new ExcelUtil<>(ABS_RepaymentExcel.class);
        List<String> csvData = excel.exportDatasToCSV(excels);
        return getFullPath(fileName, csvData);
    }

    private String saveContractChangeResult2File(Date time, List<ABS_ContractChangeExcel> excels) {
        String fileName = "PU_YNXT_XF_SCENE_TX_" + DateUtils.getDateFormatYYMMDD(time).replace("-", "") + ".csv";
        if (excels.isEmpty()) {
            log.info("Excels is empty");
        }
        ExcelUtil<ABS_ContractChangeExcel> excel = new ExcelUtil<>(ABS_ContractChangeExcel.class);
        List<String> csvData = excel.exportDatasToCSV(excels);
        return getFullPath(fileName, csvData);
    }

    private String saveCurrentContractDetailResult2File(Date time, List<ABS_CurrentContractDetailExcel> excels) {
        String fileName = "PU_YNXT_XF_ASSET_TX_" + DateUtils.getDateFormatYYMMDD(time).replace("-", "") + ".csv";
        if (excels.isEmpty()) {
            log.info("Excels is empty");
        }
        ExcelUtil<ABS_CurrentContractDetailExcel> excel = new ExcelUtil<>(ABS_CurrentContractDetailExcel.class);
        List<String> csvData = excel.exportDatasToCSV(excels);
        return getFullPath(fileName, csvData);
    }

    private String getFullPath( String fileName,  List<String> csvData) {
        String fullPath = "";
        FileOutputStream outResult = null;
        String path = pathConfig.getTencentAbsReportTask();
        try {
            fullPath = path + fileName;
            File resultFile = new File(fullPath);
            outResult = new FileOutputStream(resultFile);
            ExcelUtil.writeCSVToOutputStream(csvData, outResult, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outResult);
        }
        return fullPath;
    }

    private String fetchFieldValue(Row row, String fieldName) {

        Object x;
        try {
            x = row.getAs(fieldName);
        } catch (Exception ignored) {
            x = null;
        }
        return x == null ? null : x.toString();
    }

    private Map<String, String> repaymentWayMap = new HashMap<String, String>() {
        {
            put("0", "等额本息");
            put("1", "等额本金");
            put("2", "自定义");
            put("3", "随借随还");
        }
    };
}
